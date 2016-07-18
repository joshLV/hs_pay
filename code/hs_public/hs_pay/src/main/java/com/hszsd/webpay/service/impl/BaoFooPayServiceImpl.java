package com.hszsd.webpay.service.impl;

import com.hszsd.common.util.date.DateUtil;
import com.hszsd.common.util.math.MathUtil;
import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.common.PayType;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.config.BaoFooConfig;
import com.hszsd.webpay.dao.RechargeRecordDao;
import com.hszsd.webpay.po.RechargeRecordPO;
import com.hszsd.webpay.service.PayService;
import com.hszsd.webpay.util.PayUtils;
import com.hszsd.webpay.web.dto.PayInDTO;
import com.hszsd.webpay.web.dto.PayOutDTO;
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
 * 宝付支付服务业务层
 * Created by gzhengDu on 2016/7/5.
 */
@Service("baoFooPayService")
public class BaoFooPayServiceImpl extends PayService{
    private static final Logger logger = LoggerFactory.getLogger(BaoFooPayServiceImpl.class);

    @Autowired
    private RechargeRecordDao rechargeRecordDao;

    /**
     * 返回宝付的标识
     * @return String 宝付标识：22
     */
    @Override
    public String key() {
        return PayType.BAOFOO.getCode();
    }

    /**
     * 获取交易编码
     * 规则：HSB+订单日期(yyyymmdd)+流水号
     * @param strings 空字符
     * @return String 交易编码
     */
    @Override
    public String getId(String...strings) {
        StringBuffer sb = new StringBuffer();
        sb.append(BaoFooConfig.TRANSID_PREFIX);
        //获取时间戳
        sb.append(DateUtil.dateToString(new Date(), "yyyyMMdd"));
        sb.append(transIdService.initTransNo());
        return sb.toString();
    }

    /**
     * 宝付支付
     * @param payInDTO 支付入参
     * @return PayOutDTO 支付返回结果
     */
    @Override
    @Transactional
    public PayOutDTO pay(PayInDTO payInDTO) {
        logger.info("pay is starting and payInDTO={}", payInDTO);
        PayOutDTO payOutDTO = new PayOutDTO();

        //获取第三方接口配置信息
        PaymentInterfaceDTO paymentInterfaceDTO = super.getPaymentInterfaceDTO();
        //初始化第三方支付参数
        Map<String, String> map = initSignData(paymentInterfaceDTO, payInDTO);
        //初始化充值记录并保存
        RechargeRecordPO rechargeRecordPO = new RechargeRecordPO();
        rechargeRecordPO.setTradeNo(map.get("TransID"));
        rechargeRecordPO.setUserId(payInDTO.getUser().getUserId());
        rechargeRecordPO.setStatus(GlobalConstants.WATI_CHECK);
        double money = MathUtil.divide(Double.valueOf(map.get("OrderMoney")), 100, 2);
        rechargeRecordPO.setMoney(new BigDecimal(money));
        rechargeRecordPO.setPayment(paymentInterfaceDTO.getInterfaceValue());
        rechargeRecordPO.setType(GlobalConstants.ONLINE);
        rechargeRecordPO.setRemark(
                StringUtils.join(
                        "用户",
                        payInDTO.getUser().getUsername(),
                        "通过",
                        paymentInterfaceDTO.getName(),
                        "充值，订单号：",
                        rechargeRecordPO.getTradeNo(),
                        "充值金额",
                        rechargeRecordPO.getMoney(),
                        "元"));
        rechargeRecordPO.setAddtime(System.currentTimeMillis());
        int flag = rechargeRecordDao.insert(rechargeRecordPO);
        //封装支付返回结果
        if(flag > 0){
            payOutDTO.setRequest(StringUtils.join(paymentInterfaceDTO.getPayUrl(),"?",PayUtils.generateUrl(map)));
            payOutDTO.setResult(ResultConstants.OPERATOR_SUCCESS);
            return payOutDTO;
        }
        payOutDTO.setResult(ResultConstants.OPERATOR_FAIL);
        return payOutDTO;
    }

    /**
     * 前台回调方法
     * @param payInDTO 支付入参
     * @return PayOutDTO 支付操作结果
     */
    @Override
    public PayOutDTO front(PayInDTO payInDTO) {
        return back(payInDTO);
    }

    /**
     * 后台回调方法
     * @param payInDTO 支付入参
     * @return PayOutDTO 支付操作结果
     */
    @Override
    public PayOutDTO back(PayInDTO payInDTO) {
        logger.info("back is starting and payIndDTO={}", payInDTO);
        PayOutDTO payOutDTO = new PayOutDTO();

        //获取第三方支付的配置信息
        PaymentInterfaceDTO paymentInterfaceDTO = super.getPaymentInterfaceDTO();
        //获取第三方支付接口返回参数
        Map<String, String> map = PayUtils.requestToMap(payInDTO.getRequest());
        //签名规则：MemberID={MemberID}~|~TerminalID={TerminalID}~|~TransID={TransID}
        //                      ~|~Result={Result}~|~ResultDesc={ResultDesc}~|~FactMoney={FactMoney}
        //                      ~|~AdditionalInfo={AdditionalInfo}~|~SuccTime={SuccTime}~|~Md5Sign={Md5key}
        map.put("Md5Sign", paymentInterfaceDTO.getMerchantKey());
        String md5Sign = MD5Info(PayUtils.generateSign(map, BaoFooConfig.resVo, "=", "~|~"));

        //验证MD5信息是否相等
        if(map.get("Md5Sign") != null &&
                md5Sign.equals(map.get("Md5Sign"))){
            if(BaoFooConfig.RESULT_SUCCESS.equals(map.get("Result")) &&
                    BaoFooConfig.RESULT_DESC_SUCCESS.equals(map.get("ResultDesc"))) {
                //更新个人账号、充值记录、交易记录
                saveBack(map.get("TransId"), map.get("FactMoney"));
                payOutDTO.setResult(BaoFooConfig.RESULT_SUCCESS);
            }else{
                //支付失败，更新充值记录状态
                saveBackForFail(map.get("TransId"));
                payOutDTO.setResult(BaoFooConfig.RESULT_FAIL);
            }
            payOutDTO.setBean(BaoFooConfig.SUCCESS);
            return payOutDTO;
        }

        payOutDTO.setBean(BaoFooConfig.FAIL);
        logger.error("宝付支付MD5信息不一致，处理失败！");
        return payOutDTO;
    }

    /**
     * 支付查询接口
     * @param payInDTO 支付入参
     * @return PayOutDTO 支付返回结果
     */
    @Override
    public PayOutDTO query(PayInDTO payInDTO) {
        return null;
    }

    /**
     * 初始化宝付支付接口数据
     * @param paymentInterfaceDTO 支付接口配置信息
     * @param payInDTO 支付入参
     * @return Map 支付接口数据集合
     */
    @Override
    public Map<String, String> initSignData(PaymentInterfaceDTO paymentInterfaceDTO, PayInDTO payInDTO) {
        logger.info("initSignData is starting and paymentInterfaceDTO={}, payInDTO={}", paymentInterfaceDTO, payInDTO);
        //封装支付接口参数
        Map<String, String> map = new TreeMap<String, String>();
        //必输项
        map.put("MemberID", paymentInterfaceDTO.getMerchantId());//商户ID
        map.put("TerminalID", BaoFooConfig.TERMINAL_ID);//终端ID
        map.put("InterfaceVersion", BaoFooConfig.INTERFACE_VERSION);//接口版本
        map.put("KeyType", BaoFooConfig.KEY_TYPE);//加密类型
        map.put("PayID", payInDTO.getBankAddr());//功能ID
        map.put("TradeDate", DateUtil.dateToString(new Date(), BaoFooConfig.TRADE_DATE_FORMAT));//订单日期
        map.put("TransID", getId());//订单号
        map.put("OrderMoney", String.valueOf(MathUtil.round(MathUtil.multiply(payInDTO.getMoney(), 100), 0)));//订单金额 单位：分
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
        String sign = PayUtils.generateStr(signData, "|");
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
