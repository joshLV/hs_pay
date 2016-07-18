package com.hszsd.webpay.service.impl;

import beartool.MD5;
import com.hszsd.common.util.date.DateUtil;
import com.hszsd.common.util.math.MathUtil;
import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.common.PayType;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.common.Type;
import com.hszsd.webpay.config.BaoFooConfig;
import com.hszsd.webpay.config.HuichaoConfig;
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

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * 汇潮支付服务业务层
 * Created by gzhengDu on 2016/7/5.
 */
@Service("huichaoPayService")
public class HuichaoPayServiceImpl extends PayService{

    private static final Logger logger = LoggerFactory.getLogger(HuichaoPayServiceImpl.class);

    @Autowired
    private RechargeRecordDao rechargeRecordDao;

    /**
     * 返回汇潮支付标识
     * @return String 汇潮标识：24
     */
    @Override
    public String key() {
        return PayType.HUICHAO.getCode();
    }

    /**
     * 获取交易编码
     * 规则：商户编号订单日期(yyyymmdd)流水号
     * @param strings 商户编号
     * @return String 交易编码
     */
    @Override
    public String getId(String...strings) {
        StringBuffer sb = new StringBuffer();
        sb.append(strings);
        //获取时间戳
        sb.append(DateUtil.dateToString(new Date(), "yyyyMMdd"));
        sb.append(transIdService.initTransNo());
        return sb.toString();
    }

    /**
     * 汇潮支付
     * @param payInDTO 支付入参
     * @return PayOutDTO 支付返回结果
     */
    @Override
    public PayOutDTO pay(PayInDTO payInDTO) {
        logger.info("pay is starting and payInDTO={}",payInDTO);
        PayOutDTO payOutDTO = new PayOutDTO();

        //获取第三方支付接口配置信息
        PaymentInterfaceDTO paymentInterfaceDTO = super.getPaymentInterfaceDTO();
        //初始化第三方支付参数
        Map<String, String> map = initSignData(paymentInterfaceDTO, payInDTO);
        //初始化充值记录并保存
        RechargeRecordPO rechargeRecordPO = new RechargeRecordPO();
        rechargeRecordPO.setTradeNo(map.get("BillNo"));
        rechargeRecordPO.setUserId(payInDTO.getUser().getUserId());
        rechargeRecordPO.setStatus(GlobalConstants.WATI_CHECK);
        double money = MathUtil.divide(Double.valueOf(map.get("Amount")), 1, 2);
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
            payOutDTO.setUrlType(Type.FORM.toString());
            payOutDTO.setRequest(StringUtils.join(paymentInterfaceDTO.getPayUrl(),"&", PayUtils.generateUrl(map)));
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
        //签名规则：{BillNo}&{Amount}&{Succeed}&{MD5key}
        map.put("MD5Key", paymentInterfaceDTO.getMerchantKey());
        String signData[] = new String[HuichaoConfig.resVo.length];
        for(int i=0;i<HuichaoConfig.resVo.length;i++){
            signData[i] = map.get(HuichaoConfig.resVo[i]);
        }
        String md5Sign = MD5Info(PayUtils.generateStr(signData, "&"));

        //验证MD5信息是否相等
        if(map.get("SignMD5info") != null &&
                md5Sign.equals(map.get("SignMD5info"))){
            if(HuichaoConfig.RESULT_SUCCESS.equals(map.get("Succeed"))) {
                //更新个人账号、充值记录、交易记录
                saveBack(map.get("BillNo"), String.valueOf(MathUtil.round(MathUtil.multiply(map.get("Amount"), 100), 0)));
                payOutDTO.setResult(HuichaoConfig.RESULT_SUCCESS);
            }else{
                //支付失败，更新充值记录状态
                saveBackForFail(map.get("BillNo"));
            }
            payOutDTO.setBean(HuichaoConfig.SUCCESS);
            return payOutDTO;
        }

        payOutDTO.setBean(HuichaoConfig.FAIL);
        logger.error("汇潮支付MD5信息不一致，处理失败！");
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
     * 初始化汇潮支付接口数据
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
        map.put("MerNo", paymentInterfaceDTO.getMerchantId());//商户号
        map.put("BillNo", getId(paymentInterfaceDTO.getMerchantId()));//订单号
        map.put("Amount", String.valueOf(MathUtil.round(payInDTO.getMoney(), 2)));//金额 单位：元
        map.put("ReturnURL", paymentInterfaceDTO.getReturnUrl());//页面跳转同步通知页面
        map.put("AdviceURL", paymentInterfaceDTO.getNoticeUrl());//服务器异步通知路径

        //封装接口数据并生成签名，{param}表示param的值
        //签名规则：“{MerNo}&{BillNo}&{Amount}&{ReturnURL}&{MD5key}”
        String signData[] = new String[HuichaoConfig.reqVo.length];
        for(int i=0;i<HuichaoConfig.reqVo.length;i++){
            signData[i] = map.get(HuichaoConfig.reqVo[i]);
        }
        signData[HuichaoConfig.reqVo.length-1] = paymentInterfaceDTO.getMerchantKey();
        String sign = PayUtils.generateStr(signData, "&");
        map.put("SignInfo", MD5Info(sign));//签名信息
        map.put("orderTime", DateUtil.dateToString(new Date(), HuichaoConfig.ORDER_DATE_FORMAT));//请求时间

        //非必输项
        map.put("defaultBankNumber", "");//银行编码
        map.put("Remark", "");//备注
        map.put("products", "");//物品信息

        return map;
    }

    /**
     * 汇潮MD5加密算法, 需要将值转为大写
     * @param str 源字符串
     * @return MD5校验码
     */
    @Override
    public String MD5Info(String str) {
        return new MD5().getMD5ofStr(str).toUpperCase();
    }
}
