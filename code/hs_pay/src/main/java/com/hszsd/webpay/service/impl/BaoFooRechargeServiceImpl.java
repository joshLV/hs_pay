package com.hszsd.webpay.service.impl;

import com.hszsd.common.util.date.DateUtil;
import com.hszsd.common.util.math.MathUtil;
import com.hszsd.entity.RechargeRecord;
import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.common.RechargeType;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.config.BaoFooConfig;
import com.hszsd.webpay.service.RechargeService;
import com.hszsd.webpay.util.WebUtils;
import com.hszsd.webpay.web.dto.PaymentInterfaceDTO;
import com.hszsd.webpay.web.dto.RechargeInDTO;
import com.hszsd.webpay.web.dto.RechargeOutDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * 宝付充值服务业务层
 * Created by gzhengDu on 2016/7/5.
 */
@Service("baoFooRechargeService")
public class BaoFooRechargeServiceImpl extends RechargeService {
    private static final Logger logger = LoggerFactory.getLogger(BaoFooRechargeServiceImpl.class);

    /**
     * 返回宝付充值类型对象
     * @return RechargeType.BAOFOO
     */
    @Override
    public RechargeType type() {
        return RechargeType.BAOFOO;
    }

    /**
     * 宝付充值
     * @param rechargeInDTO 充值入参
     * @return RechargeOutDTO 充值返回结果
     */
    @Override
    @Transactional
    public RechargeOutDTO recharge(RechargeInDTO rechargeInDTO) {
        logger.info("recharge is starting and rechargeInDTO={}", rechargeInDTO);
        RechargeOutDTO rechargeOutDTO = new RechargeOutDTO();

        //获取第三方接口配置信息
        PaymentInterfaceDTO paymentInterfaceDTO = super.getPaymentInterfaceDTO();
        //初始化第三方充值参数
        Map<String, String> map = initSignData(paymentInterfaceDTO, rechargeInDTO);
        //初始化充值记录并保存
        RechargeRecord saveRechargeRecord = new RechargeRecord();
        saveRechargeRecord.setTradeNo(map.get("TransID"));
        saveRechargeRecord.setUserId(rechargeInDTO.getUser().getUserId());
        saveRechargeRecord.setStatus(GlobalConstants.RECHARGE.WATI_CHECK);
        saveRechargeRecord.setMoney(BigDecimal.valueOf(MathUtil.divide(Double.valueOf(map.get("OrderMoney")), 100, 2)));
        saveRechargeRecord.setPayment(paymentInterfaceDTO.getInterfaceValue());
        saveRechargeRecord.setType(GlobalConstants.RECHARGE.ONLINE);
        saveRechargeRecord.setRemark(
                StringUtils.join(
                        "用户",
                        rechargeInDTO.getUser().getUsername(),
                        "通过",
                        type().getName(),
                        "充值，订单号：",
                        saveRechargeRecord.getTradeNo(),
                        "，充值金额：",
                        saveRechargeRecord.getMoney(),
                        "元"
                )
        );
        saveRechargeRecord.setAddtime(System.currentTimeMillis());

        //根据dubbo服务器处理结果封装返回参数
        try {
            if (!coreService.insertRechargeRecord(saveRechargeRecord)) {
                rechargeOutDTO.setResult(ResultConstants.OPERATOR_FAIL);
                return rechargeOutDTO;
            }
        }catch (Exception e){
            logger.error("BaoFooRechargeServiceImpl occurs an error and cause by {}", e.getMessage());
            rechargeOutDTO.setResult(ResultConstants.OPERATOR_FAIL);
            return rechargeOutDTO;
        }
        // 正式地址
        rechargeOutDTO.setRequest(StringUtils.join(paymentInterfaceDTO.getPayUrl(),"?", WebUtils.generateUrl(map)));
        // 测试地址
        //rechargeOutDTO.setRequest(StringUtils.join("https://vgw.baofoo.com/payindex","?", WebUtils.generateUrl(map)));

        rechargeOutDTO.setResult(ResultConstants.OPERATOR_SUCCESS);
        return rechargeOutDTO;
    }

    /**
     * 前台回调方法
     * 1. 验证MD5是否正确
     * 2. 判断支付结果是否成功
     * 2-1. 成功则更新个人账号、充值记录、交易记录
     * 2-2. 失败不做处理
     * @param rechargeInDTO 充值入参
     */
    @Override
    public void front(RechargeInDTO rechargeInDTO) {
        logger.info("front is starting and rechargeIndDTO={}", rechargeInDTO);

        //获取第三方充值接口返回参数
        Map<String, String> map = WebUtils.requestToMap(rechargeInDTO.getRequest());
        logger.info("baoFooFront paramMap={}", map);

        //验证MD5信息
        if(!checkMD5Sign(map)){
            logger.error("宝付充值MD5信息不一致，处理失败！");
            return ;
        }
        //MD5验证通过后，验证支付结果，如果成功则更新个人账号、充值记录、交易记录
        if(BaoFooConfig.RESULT_SUCCESS.equals(map.get("Result")) &&
                BaoFooConfig.RESULT_DESC_SUCCESS.equals(map.get("ResultDesc"))) {
            try {
                //更新个人账号、充值记录、交易记录
                saveBackForSuccess(map.get("TransID"), map.get("FactMoney"));
            } catch (Exception e) {
                logger.error("front occurs an error and cause by {}", e.getMessage());
            }
        }

    }

    /**
     * 后台回调方法
     * 1. 验证MD5是否正确
     * 2. 判断支付结果是否成功
     * 2-1. 支付成功则更新个人账号、充值记录、交易记录。
     * 2-2. 支付失败则更新充值记录、交易记录状态，如果前台回调成功则还需进行撤销资金操作状态。
     * 3. 判断该更新结果；
     * 3-1. 更新成功，则添加回调记录并返回确认成功信息给第三方支付平台；
     * 3-2. 更新失败，返回确认失败信息给第三方支付平台，由第三方支付平台再次发起异步通知
     * @param rechargeInDTO 充值入参
     * @return RechargeOutDTO 充值操作结果
     */
    @Override
    public RechargeOutDTO back(RechargeInDTO rechargeInDTO) {
        logger.info("back is starting and rechargeIndDTO={}", rechargeInDTO);
        RechargeOutDTO rechargeOutDTO = new RechargeOutDTO();

        //获取第三方充值接口返回参数
        Map<String, String> map = WebUtils.requestToMap(rechargeInDTO.getRequest());
        logger.info("baoFooBack paramMap={}", map);

        //声明业务处理成功标识
        boolean saveSuccessFlag = false;
        //验证MD5信息
        if(!checkMD5Sign(map)){
            rechargeOutDTO.setBean(BaoFooConfig.FAIL);
            logger.error("宝付充值MD5信息不一致，处理失败！");
            return rechargeOutDTO;
        }

        //MD5验证通过后，验证支付结果，如果成功则更新个人账号、充值记录、交易记录
        if(BaoFooConfig.RESULT_SUCCESS.equals(map.get("Result")) &&
                BaoFooConfig.RESULT_DESC_SUCCESS.equals(map.get("ResultDesc"))) {
            try {
                //更新个人账号、充值记录、交易记录
                saveSuccessFlag = saveBackForSuccess(map.get("TransID"), map.get("FactMoney"));
            } catch (Exception e) {
                logger.error("back occurs an error and cause by {}", e.getMessage());
            }
        }else{
            try {
                //充值失败，更新充值记录、交易记录状态、撤销资金操作
                saveSuccessFlag = saveBackForFail(map.get("TransID"), map.get("FactMoney"));
            } catch (Exception e) {
                logger.error("back occurs an error and cause by {}", e.getMessage());
            }
        }

        //判断业务处理成功是否
        if(!saveSuccessFlag) {
            logger.info("back failed and saveSuccessFlag={}", saveSuccessFlag);
            rechargeOutDTO.setBean(BaoFooConfig.FAIL);
        }

        //生成交易回调数据（使用定时任务查询需要回调的数据通知商户）
        try {
            saveTradeCallBack(map.get("TransID"));
        }catch (Exception e){
            logger.error("back occurs an error and cause by {}", e.getMessage());
            rechargeOutDTO.setBean(BaoFooConfig.FAIL);
            return rechargeOutDTO;
        }
        rechargeOutDTO.setBean(BaoFooConfig.SUCCESS);
        return rechargeOutDTO;
    }

    /**
     * 充值查询接口
     * @param rechargeInDTO 充值入参
     * @return RechargeOutDTO 充值返回结果
     */
    @Override
    public RechargeOutDTO query(RechargeInDTO rechargeInDTO) {
        return null;
    }

    /**
     * 初始化宝付充值接口数据
     * @param paymentInterfaceDTO 充值接口配置信息
     * @param rechargeInDTO 充值入参
     * @return Map 充值接口数据集合
     */
    @Override
    public Map<String, String> initSignData(PaymentInterfaceDTO paymentInterfaceDTO, RechargeInDTO rechargeInDTO) {
        logger.info("initSignData is starting and paymentInterfaceDTO={}, rechargeInDTO={}", paymentInterfaceDTO, rechargeInDTO);
        //封装充值接口参数
        Map<String, String> map = new TreeMap<String, String>();
        //必输项
        map.put("MemberID", paymentInterfaceDTO.getMerchantId());//商户ID
        //map.put("MemberID", "100000178");//商户ID 测试商户号:100000178
        map.put("TerminalID", BaoFooConfig.TERMINAL_ID);//终端ID
        //map.put("TerminalID", "10000001");//终端ID 测试终端号：10000001
        map.put("InterfaceVersion", BaoFooConfig.INTERFACE_VERSION);//接口版本
        map.put("KeyType", BaoFooConfig.KEY_TYPE);//加密类型
        map.put("PayID", rechargeInDTO.getBankId());//功能ID
        map.put("TradeDate", DateUtil.dateToString(new Date(), BaoFooConfig.TRADE_DATE_FORMAT));//订单日期
        map.put("TransID", rechargeInDTO.getTransId());//订单号
        map.put("OrderMoney", String.valueOf(MathUtil.multiply(rechargeInDTO.getMoney(), "100")));//订单金额 单位：分
        map.put("NoticeType", BaoFooConfig.NOTICE_TYPE);//通知类型
        map.put("PageUrl", paymentInterfaceDTO.getReturnUrl());//页面返回地址
        map.put("ReturnUrl", paymentInterfaceDTO.getNoticeUrl());//交易通知地址

        //封装接口数据并生成签名
        //签名规则：“{MemberID}|{PayID}|{TradeDate}|{TransID}|{OrderMoney}|{PageUrl}|{ReturnUrl}|{NoticeType}|{MD5Key}”
        String signData[] = new String[BaoFooConfig.reqVo.size()];
        for(int i=0;i<BaoFooConfig.reqVo.size();i++){
            signData[i] = map.get(BaoFooConfig.reqVo.get(i));
        }
        //正式库秘钥
        signData[BaoFooConfig.reqVo.size()-1] = paymentInterfaceDTO.getMerchantKey();
        //测试秘钥
        //signData[BaoFooConfig.reqVo.size()-1] = "abcdefg";
        String sign = WebUtils.generateStr(signData, "|");
        map.put("Signature", MD5Info(sign));

        //非必输项
        map.put("ProductName", "");//商品名称
        map.put("Amount", "");//商品数量
        map.put("Username", "");//用户名称
        map.put("AdditionalInfo", "");//附加字段

        return map;
    }

    /**
     * 宝付MD5加密算法
     * @param str 源字符串
     * @return MD5校验码
     */
    @Override
    public String MD5Info(String str) {
        try{
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes("utf-8"));

            byte[] digest = md5.digest();

            StringBuffer hexString = new StringBuffer();
            for(int i=0;i<digest.length;i++){
                /*
                byte & 0x000000FF的作用是，如果digest[i]是负数，则会清除前面24个零，正的byte整型不受影响。
                (...) | 0xFFFFFF00的作用是，如果digest[i]是正数，则置前24位为一，
                这样toHexString输出一个小于等于15的byte整型的十六进制时，倒数第二位为零且不会被丢弃，这样可以通过substring方法进行截取最后两位即可。
                */
                hexString.append(Integer.toHexString((digest[i] & 0x000000FF) | 0xFFFFFF00).substring(6));
            }
            return hexString.toString();
        }catch (Exception e){
            logger.error("MD5Info occurs an error and cause by {}", e.getMessage());
            return "";
        }
    }

    /**
     * 根据参数校验MD5加密串是否正确
     * @param paramMap 封装待验证的数据
     * @return boolean 是否验证通过
     */
    public boolean checkMD5Sign(Map<String, String> paramMap){
        //获取第三方充值的配置信息
        PaymentInterfaceDTO paymentInterfaceDTO = super.getPaymentInterfaceDTO();

        String md5Sign = paramMap.get("Md5Sign");//第三方加密字符串
        //签名规则：MemberID={MemberID}~|~TerminalID={TerminalID}~|~TransID={TransID}
        //                      ~|~Result={Result}~|~ResultDesc={ResultDesc}~|~FactMoney={FactMoney}
        //                      ~|~AdditionalInfo={AdditionalInfo}~|~SuccTime={SuccTime}~|~Md5Sign={MD5Key}
        //正式秘钥
        paramMap.put("Md5Sign", paymentInterfaceDTO.getMerchantKey());
        //测试秘钥
        //paramMap.put("Md5Sign", "abcdefg");
        String checkMd5Sign = MD5Info(WebUtils.generateStr(paramMap, (String[]) BaoFooConfig.resVo.toArray(), "=", "~|~"));

        return checkMd5Sign.equals(md5Sign);
    }
}
