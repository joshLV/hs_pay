package com.hszsd.webpay.service.impl;

import com.hszsd.common.util.date.DateUtil;
import com.hszsd.common.util.math.MathUtil;
import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.common.RechargeModel;
import com.hszsd.webpay.common.RechargeType;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.config.BaoFooConfig;
import com.hszsd.webpay.dao.RechargeRecordDao;
import com.hszsd.webpay.po.RechargeRecordPO;
import com.hszsd.webpay.service.RechargeService;
import com.hszsd.webpay.util.WebUtils;
import com.hszsd.webpay.web.dto.RechargeInDTO;
import com.hszsd.webpay.web.dto.RechargeOutDTO;
import com.hszsd.webpay.web.dto.PaymentInterfaceDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RechargeRecordDao rechargeRecordDao;

    /**
     * 返回宝付的标识
     * @return String 宝付标识：22
     */
    @Override
    public String key() {
        return RechargeType.BAOFOO.getCode();
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
        RechargeRecordPO rechargeRecordPO = new RechargeRecordPO();
        rechargeRecordPO.setTradeNo(map.get("TransID"));
        rechargeRecordPO.setUserId(rechargeInDTO.getUser().getUserId());
        rechargeRecordPO.setStatus(GlobalConstants.RECHARGE.WATI_CHECK);
        double money = MathUtil.divide(Double.valueOf(map.get("OrderMoney")), 100, 2);
        rechargeRecordPO.setMoney(new BigDecimal(money));
        rechargeRecordPO.setPayment(paymentInterfaceDTO.getInterfaceValue());
        rechargeRecordPO.setType(GlobalConstants.RECHARGE.ONLINE);
        rechargeRecordPO.setRemark(
                StringUtils.join(
                        "用户",
                        rechargeInDTO.getUser().getUsername(),
                        "通过",
                        paymentInterfaceDTO.getName(),
                        "充值，订单号：",
                        rechargeRecordPO.getTradeNo(),
                        "充值金额",
                        rechargeRecordPO.getMoney(),
                        "元"));
        rechargeRecordPO.setAddtime(System.currentTimeMillis());
        int flag = rechargeRecordDao.insert(rechargeRecordPO);
        //封装充值返回结果
        if(flag > 0){
            rechargeOutDTO.setRequest(StringUtils.join(paymentInterfaceDTO.getPayUrl(),"?", WebUtils.generateUrl(map)));
            rechargeOutDTO.setResult(ResultConstants.OPERATOR_SUCCESS);
            return rechargeOutDTO;
        }
        rechargeOutDTO.setResult(ResultConstants.OPERATOR_FAIL);
        return rechargeOutDTO;
    }

    /**
     * 前台回调方法
     * @param rechargeInDTO 充值入参
     * @return RechargeOutDTO 充值操作结果
     */
    @Override
    public RechargeOutDTO front(RechargeInDTO rechargeInDTO) {
        return back(rechargeInDTO);
    }

    /**
     * 后台回调方法
     * @param rechargeInDTO 充值入参
     * @return RechargeOutDTO 充值操作结果
     */
    @Override
    public RechargeOutDTO back(RechargeInDTO rechargeInDTO) {
        logger.info("back is starting and rechargeIndDTO={}", rechargeInDTO);
        RechargeOutDTO rechargeOutDTO = new RechargeOutDTO();

        //获取第三方充值的配置信息
        PaymentInterfaceDTO paymentInterfaceDTO = super.getPaymentInterfaceDTO();
        //获取第三方充值接口返回参数
        Map<String, String> map = WebUtils.requestToMap(rechargeInDTO.getRequest());
        //签名规则：MemberID={MemberID}~|~TerminalID={TerminalID}~|~TransID={TransID}
        //                      ~|~Result={Result}~|~ResultDesc={ResultDesc}~|~FactMoney={FactMoney}
        //                      ~|~AdditionalInfo={AdditionalInfo}~|~SuccTime={SuccTime}~|~Md5Sign={Md5key}
        map.put("Md5Sign", paymentInterfaceDTO.getMerchantKey());
        String md5Sign = MD5Info(WebUtils.generateStr(map, BaoFooConfig.resVo, "=", "~|~"));

        //验证MD5信息是否相等
        if(map.get("Md5Sign") != null &&
                md5Sign.equals(map.get("Md5Sign"))){
            if(BaoFooConfig.RESULT_SUCCESS.equals(map.get("Result")) &&
                    BaoFooConfig.RESULT_DESC_SUCCESS.equals(map.get("ResultDesc"))) {
                try {
                    //更新个人账号、充值记录、交易记录
                    saveBackForSuccess(map.get("TransId"), map.get("FactMoney"));
                } catch (Exception e) {
                    logger.error("back occurs an error and cause by {}", e.getMessage());
                }
                rechargeOutDTO.setResult(BaoFooConfig.RESULT_SUCCESS);
            }else{
                //如果是异步通知失败
                if(RechargeModel.BACK.equals(rechargeInDTO.getRechargeModel())){
                    try {
                        //充值失败，更新充值记录、交易记录状态
                        saveBackForFail(map.get("TransId"));
                    } catch (Exception e) {
                        logger.error("back occurs an error and cause by {}", e.getMessage());
                    }
                }
                rechargeOutDTO.setResult(BaoFooConfig.RESULT_FAIL);
            }

            //生成交易回调数据
            saveTradeCallBack(map.get("orderId"));

            rechargeOutDTO.setBean(BaoFooConfig.SUCCESS);
            return rechargeOutDTO;
        }

        rechargeOutDTO.setBean(BaoFooConfig.FAIL);
        logger.error("宝付充值MD5信息不一致，处理失败！");
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
        map.put("TerminalID", BaoFooConfig.TERMINAL_ID);//终端ID
        map.put("InterfaceVersion", BaoFooConfig.INTERFACE_VERSION);//接口版本
        map.put("KeyType", BaoFooConfig.KEY_TYPE);//加密类型
        map.put("PayID", rechargeInDTO.getBankAddr());//功能ID
        map.put("TradeDate", DateUtil.dateToString(new Date(), BaoFooConfig.TRADE_DATE_FORMAT));//订单日期
        map.put("TransID", rechargeInDTO.getTransId());//订单号
        map.put("OrderMoney", String.valueOf(MathUtil.round(MathUtil.multiply(rechargeInDTO.getMoney(), 100), 0)));//订单金额 单位：分
        map.put("NoticeType", BaoFooConfig.NOTICE_TYPE);//通知类型
        map.put("PageUrl", paymentInterfaceDTO.getReturnUrl());//页面返回地址
        map.put("ReturnUrl", paymentInterfaceDTO.getNoticeUrl());//交易通知地址
        //封装接口数据并生成签名
        //签名规则：“{MemberID}|{PayID}|{TradeDate}|{TransID}|{OrderMoney}|{PageUrl}|{ReturnUrl}|{NoticeType}|{Md5key}”
        String signData[] = new String[BaoFooConfig.reqVo.length];
        for(int i=0;i<BaoFooConfig.reqVo.length;i++){
            signData[i] = map.get(BaoFooConfig.reqVo[i]);
        }
        signData[BaoFooConfig.reqVo.length-1] = paymentInterfaceDTO.getMerchantKey();
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
}
