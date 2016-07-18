package com.hszsd.webpay.service.impl;

import com.hszsd.common.util.date.DateUtil;
import com.hszsd.common.util.math.MathUtil;
import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.common.PayType;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.config.LianLianConfig;
import com.hszsd.webpay.dao.RechargeRecordDao;
import com.hszsd.webpay.po.RechargeRecordPO;
import com.hszsd.webpay.service.PayService;
import com.hszsd.webpay.util.LianLianPayUtils;
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
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by gzhengDu on 2016/7/11.
 */
@Service("lianLianService")
public class LianLianPayServiceImpl extends PayService {
    private static final Logger logger = LoggerFactory.getLogger(LianLianPayServiceImpl.class);

    @Autowired
    private RechargeRecordDao rechargeRecordDao;

    /**
     * 返回连连支付的标识
     * @return String 连连支付标识：21
     */
    @Override
    public String key() {
        return PayType.LIANLIAN.getCode();
    }

    /**
     * 获取交易编码
     * 规则：HSLL订单日期(yyyymmdd)流水号
     * @param strings 空字符
     * @return String 交易编码
     */
    @Override
    public String getId(String...strings) {
        StringBuffer sb = new StringBuffer();
        sb.append(LianLianConfig.ORDERNO_PREFIX);
        //获取时间戳
        sb.append(DateUtil.dateToString(new Date(), "yyyyMMdd"));
        sb.append(transIdService.initTransNo());
        return sb.toString();
    }

    /**
     * 连连支付
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
        rechargeRecordPO.setTradeNo(map.get("no_order"));
        rechargeRecordPO.setUserId(payInDTO.getUser().getUserId());
        rechargeRecordPO.setStatus(GlobalConstants.WATI_CHECK);
        double money = MathUtil.divide(Double.valueOf(map.get("money_order")), 1, 2);
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
            payOutDTO.setRequest(StringUtils.join(paymentInterfaceDTO.getPayUrl(),"?", PayUtils.generateUrl(map)));
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
        Map<String, String> map = LianLianPayUtils.getInstance().requestToMap(payInDTO.getRequest());
        //生成MD5签名信息
        String md5Sign = LianLianPayUtils.getInstance().generateLianLianSign(map, LianLianConfig.SIGN_TYPE_MD5, paymentInterfaceDTO.getMerchantKey());

        //验证MD5信息是否相等
        if(map.get("sign") != null &&
                md5Sign.equals(map.get("sign"))){
            if(LianLianConfig.RESULT_SUCCESS.equals(map.get("result_pay"))) {
                //更新个人账号、充值记录、交易记录
                saveBack(map.get("no_order"), String.valueOf(MathUtil.round(MathUtil.multiply(map.get("money_order"), 100), 0)));
                payOutDTO.setResult(LianLianConfig.RESULT_SUCCESS);
            }else{
                //支付失败，更新充值记录状态
                saveBackForFail(map.get("no_order"));
            }
            payOutDTO.setBean(LianLianConfig.SUCCESS);
            return payOutDTO;
        }

        payOutDTO.setBean(LianLianConfig.FAIL);
        logger.error("连连支付MD5信息不一致，处理失败！");
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
     * 初始化连连支付接口数据
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
        map.put("version", LianLianConfig.VERSION);//版本号
        map.put("oid_partner", paymentInterfaceDTO.getMerchantId());//支付交易商户编号
        map.put("user_id", payInDTO.getUser().getUserId());//商户用户唯一编号
        map.put("timestamp", DateUtil.dateToString(new Date(), LianLianConfig.ORDER_DATE_FORMAT));//时间戳
        map.put("sign_type", LianLianConfig.SIGN_TYPE_MD5);//签名方式
        map.put("sign","");//签名
        map.put("busi_partner", LianLianConfig.BUSI_PARTNER_VIRTUAL);//商户业务类型
        map.put("no_order", getId(LianLianConfig.ORDERNO_PREFIX));//商户唯一订单号
        map.put("dt_order", DateUtil.dateToString(new Date(), LianLianConfig.ORDER_DATE_FORMAT));//商户订单时间
        map.put("name_goods", LianLianConfig.NAME_GOODS);//商品名称
        map.put("money_order", String.valueOf(MathUtil.round(payInDTO.getMoney(), 2)));//交易金额 单位：元
        map.put("notify_url", paymentInterfaceDTO.getNoticeUrl());//服务器异步通知地址
        map.put("risk_item", "");//风险控制参数

        //非必输项
        map.put("charset_name", "utf-8");//参数字符编码集
        map.put("platform", "");//平台来源标识
        map.put("info_order", "");//订单描述
        map.put("url_return", paymentInterfaceDTO.getReturnUrl());//支付结束回显url
        map.put("userreq_ip", "");//用户端申请IP
        map.put("url_order", "");//订单地址
        map.put("valid_order", LianLianConfig.ORDER_VALIDITY_PERIOD);//订单有效时间
        map.put("bank_code", "");//指定银行网银编号
        map.put("pay_type", "");//支付方式
        map.put("no_agree", "");//签约协议号
        map.put("shareing_data", "");//分账信息数据
        map.put("id_type", "");//证件类型
        map.put("id_no", "");//证件号码
        map.put("acct_name", "");//银行账号姓名
        map.put("flag_modify", "");//修改标记
        map.put("card_no", "");//银行卡号
        map.put("back_url", "");//返回修改信息地址

        //封装接口数据并生成签名
        map.put("sign", LianLianPayUtils.getInstance().generateLianLianSign(map, LianLianConfig.SIGN_TYPE_MD5, paymentInterfaceDTO.getMerchantKey()));
        return map;
    }

    @Override
    public String MD5Info(String str) {
        return null;
    }
}
