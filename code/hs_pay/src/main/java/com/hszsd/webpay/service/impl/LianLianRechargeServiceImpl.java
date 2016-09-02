package com.hszsd.webpay.service.impl;

import com.hszsd.common.util.date.DateUtil;
import com.hszsd.common.util.math.MathUtil;
import com.hszsd.entity.RechargeRecord;
import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.common.RechargeType;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.config.LianLianConfig;
import com.hszsd.webpay.service.RechargeService;
import com.hszsd.webpay.util.LianLianRechargeUtils;
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
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by gzhengDu on 2016/7/11.
 */
@Service("lianLianRechargeService")
public class LianLianRechargeServiceImpl extends RechargeService {
    private static final Logger logger = LoggerFactory.getLogger(LianLianRechargeServiceImpl.class);

    /**
     * 返回连连充值类型对象
     * @return RechargeType.LIANLIAN
     */
    @Override
    public RechargeType type() {
        return RechargeType.LIANLIAN;
    }

    /**
     * 连连充值
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
        saveRechargeRecord.setTradeNo(map.get("no_order"));
        saveRechargeRecord.setUserId(rechargeInDTO.getUser().getUserId());
        saveRechargeRecord.setStatus(GlobalConstants.RECHARGE.WATI_CHECK);
        saveRechargeRecord.setMoney(BigDecimal.valueOf(MathUtil.divide(Double.valueOf(map.get("money_order")), 1, 2)));
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
                        "充值金额：",
                        saveRechargeRecord.getMoney(),
                        "元"
                )
        );
        saveRechargeRecord.setAddtime(System.currentTimeMillis());
        //根据dubbo服务器处理结果封装返回参数
        try {
            if(!coreService.insertRechargeRecord(saveRechargeRecord)){
                rechargeOutDTO.setResult(ResultConstants.OPERATOR_FAIL);
                return rechargeOutDTO;
            }
        }catch (Exception e){
            logger.error("lianlianRechargeServiceImpl occurs an error and cause by {}", e.getMessage());
            rechargeOutDTO.setResult(ResultConstants.OPERATOR_FAIL);
            return rechargeOutDTO;
        }

        rechargeOutDTO.setRequest(StringUtils.join(paymentInterfaceDTO.getPayUrl(),"?", WebUtils.generateUrl(map)));
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

        //获取第三方充值的配置信息
        PaymentInterfaceDTO paymentInterfaceDTO = super.getPaymentInterfaceDTO();
        //获取第三方充值接口返回参数
        Map<String, String> map = WebUtils.requestToMap(rechargeInDTO.getRequest());
        logger.info("lianlianFront paramMap={}", map);
        //验证MD5信息
        String checkMD5Sign = LianLianRechargeUtils.getInstance().generateLianLianSign(map, LianLianConfig.SIGN_TYPE_MD5, paymentInterfaceDTO.getMerchantKey());

        if(!checkMD5Sign.equals(map.get("sign"))){
            logger.error("连连充值MD5信息不一致，处理失败！");
            return ;
        }

        //MD5验证通过后，验证支付结果，如果成功则更新个人账号、充值记录、交易记录
        if(LianLianConfig.RESULT_SUCCESS.equals(map.get("result_pay"))) {
            try {
                //更新个人账号、充值记录、交易记录
                saveBackForSuccess(map.get("no_order"), String.valueOf(MathUtil.round(MathUtil.multiply(map.get("money_order"), "100"), 0)));
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

        //获取第三方充值的配置信息
        PaymentInterfaceDTO paymentInterfaceDTO = super.getPaymentInterfaceDTO();
        //获取第三方充值接口返回参数
        Map<String, String> map = LianLianRechargeUtils.getInstance().requestToMap(rechargeInDTO.getRequest());
        logger.info("lianlianBack paramMap={}", map);

        //验证MD5信息是否相等
        String checkMD5Sign = LianLianRechargeUtils.getInstance().generateLianLianSign(map, LianLianConfig.SIGN_TYPE_MD5, paymentInterfaceDTO.getMerchantKey());
        if(!checkMD5Sign.equals(map.get("sign"))){
            rechargeOutDTO.setBean(LianLianConfig.FAIL);
            logger.error("连连充值MD5信息不一致，处理失败！");
            return rechargeOutDTO;
        }

        //声明业务处理成功标识
        boolean saveSuccessFlag = false;
        //MD5验证通过后，验证支付结果，如果成功则更新个人账号、充值记录、交易记录
        if(LianLianConfig.RESULT_SUCCESS.equals(map.get("result_pay"))) {
            try {
                //更新个人账号、充值记录、交易记录
                saveSuccessFlag = saveBackForSuccess(map.get("no_order"), String.valueOf(MathUtil.round(MathUtil.multiply(map.get("money_order"), "100"), 0)));
            } catch (Exception e) {
                logger.error("back occurs an error and cause by {}", e.getMessage());
            }
            rechargeOutDTO.setResult(LianLianConfig.RESULT_SUCCESS);
        }else{
            try {
                //充值失败，更新充值记录、交易记录状态
                saveSuccessFlag = saveBackForFail(map.get("no_order"), String.valueOf(MathUtil.round(MathUtil.multiply(map.get("money_order"), "100"), 0)));
            } catch (Exception e) {
                logger.error("back occurs an error and cause by {}", e.getMessage());
            }
        }

        //判断业务处理是否成功
        if(!saveSuccessFlag) {
            logger.info("back failed and saveSuccessFlag={}", saveSuccessFlag);
            rechargeOutDTO.setBean(LianLianConfig.FAIL);
            return rechargeOutDTO;
        }
        //生成交易回调数据（使用定时任务查询需要回调的数据通知商户）
        try{
            saveTradeCallBack(map.get("no_order"));
        }catch (Exception e){
            logger.error("back occurs an error and cause by {}", e.getMessage());
            rechargeOutDTO.setBean(LianLianConfig.FAIL);
            return rechargeOutDTO;
        }
        rechargeOutDTO.setBean(LianLianConfig.SUCCESS);
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
     * 初始化连连充值接口数据
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
        map.put("version", LianLianConfig.VERSION);//版本号
        map.put("oid_partner", paymentInterfaceDTO.getMerchantId());//充值交易商户编号
        map.put("user_id", rechargeInDTO.getUser().getUserId());//商户用户唯一编号
        map.put("timestamp", DateUtil.dateToString(new Date(), LianLianConfig.ORDER_DATE_FORMAT));//时间戳
        map.put("sign_type", LianLianConfig.SIGN_TYPE_MD5);//签名方式
        map.put("sign","");//签名
        map.put("busi_partner", LianLianConfig.BUSI_PARTNER_VIRTUAL);//商户业务类型
        map.put("no_order", rechargeInDTO.getTransId());//商户唯一订单号
        map.put("dt_order", DateUtil.dateToString(new Date(), LianLianConfig.ORDER_DATE_FORMAT));//商户订单时间
        map.put("name_goods", LianLianConfig.NAME_GOODS);//商品名称
        map.put("money_order", String.valueOf(MathUtil.round(rechargeInDTO.getMoney(), 2)));//交易金额 单位：元
        map.put("notify_url", paymentInterfaceDTO.getNoticeUrl());//服务器异步通知地址
        map.put("risk_item", "");//风险控制参数

        //非必输项
        map.put("charset_name", "utf-8");//参数字符编码集
        map.put("platform", "");//平台来源标识
        map.put("info_order", "");//订单描述
        map.put("url_return", paymentInterfaceDTO.getReturnUrl());//充值结束回显url
        map.put("userreq_ip", "");//用户端申请IP
        map.put("url_order", "");//订单地址
        map.put("valid_order", LianLianConfig.ORDER_VALIDITY_PERIOD);//订单有效时间
        map.put("bank_code", "");//指定银行网银编号
        map.put("pay_type", "");//充值方式
        map.put("no_agree", "");//签约协议号
        map.put("shareing_data", "");//分账信息数据
        map.put("id_type", "");//证件类型
        map.put("id_no", "");//证件号码
        map.put("acct_name", "");//银行账号姓名
        map.put("flag_modify", "");//修改标记
        map.put("card_no", "");//银行卡号
        map.put("back_url", "");//返回修改信息地址

        //封装接口数据并生成签名
        map.put("sign", LianLianRechargeUtils.getInstance().generateLianLianSign(map, LianLianConfig.SIGN_TYPE_MD5, paymentInterfaceDTO.getMerchantKey()));
        return map;
    }

    @Override
    public String MD5Info(String str) {
        return null;
    }
}
