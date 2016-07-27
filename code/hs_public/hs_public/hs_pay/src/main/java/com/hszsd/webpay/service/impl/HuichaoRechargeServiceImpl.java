package com.hszsd.webpay.service.impl;

import com.hszsd.common.util.date.DateUtil;
import com.hszsd.common.util.math.MathUtil;
import com.hszsd.md5.beartool.MD5;
import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.common.RechargeModel;
import com.hszsd.webpay.common.RechargeType;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.common.SubmitType;
import com.hszsd.webpay.config.HuichaoConfig;
import com.hszsd.webpay.dao.RechargeRecordDao;
import com.hszsd.webpay.po.RechargeRecordPO;
import com.hszsd.webpay.service.RechargeService;
import com.hszsd.webpay.util.WebUtils;
import com.hszsd.webpay.web.dto.PaymentInterfaceDTO;
import com.hszsd.webpay.web.dto.RechargeInDTO;
import com.hszsd.webpay.web.dto.RechargeOutDTO;
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
 * 汇潮充值服务业务层
 * Created by gzhengDu on 2016/7/5.
 */
@Service("huichaoRechargeService")
public class HuichaoRechargeServiceImpl extends RechargeService {

    private static final Logger logger = LoggerFactory.getLogger(HuichaoRechargeServiceImpl.class);

    @Autowired
    private RechargeRecordDao rechargeRecordDao;

    /**
     * 返回汇潮充值标识
     * @return String 汇潮标识：24
     */
    @Override
    public String key() {
        return RechargeType.HUICHAO.getCode();
    }

    /**
     * 汇潮充值
     * @param rechargeInDTO 充值入参
     * @return RechargeOutDTO 充值返回结果
     */
    @Override
    public RechargeOutDTO recharge(RechargeInDTO rechargeInDTO) {
        logger.info("recharge is starting and rechargeInDTO={}", rechargeInDTO);
        RechargeOutDTO rechargeOutDTO = new RechargeOutDTO();

        //获取第三方充值接口配置信息
        PaymentInterfaceDTO paymentInterfaceDTO = super.getPaymentInterfaceDTO();
        //初始化第三方充值参数
        Map<String, String> map = initSignData(paymentInterfaceDTO, rechargeInDTO);
        //初始化充值记录并保存
        RechargeRecordPO rechargeRecordPO = new RechargeRecordPO();
        rechargeRecordPO.setTradeNo(map.get("BillNo"));
        rechargeRecordPO.setUserId(rechargeInDTO.getUser().getUserId());
        rechargeRecordPO.setStatus(GlobalConstants.RECHARGE.WATI_CHECK);
        double money = MathUtil.divide(Double.valueOf(map.get("Amount")), 1, 2);
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
            rechargeOutDTO.setUrlType(SubmitType.FORM.toString());
            rechargeOutDTO.setRequest(StringUtils.join(paymentInterfaceDTO.getPayUrl(),"&", WebUtils.generateUrl(map)));
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
        //签名规则：{BillNo}&{Amount}&{Succeed}&{MD5key}
        map.put("MD5Key", paymentInterfaceDTO.getMerchantKey());
        String signData[] = new String[HuichaoConfig.resVo.length];
        for(int i=0;i<HuichaoConfig.resVo.length;i++){
            signData[i] = map.get(HuichaoConfig.resVo[i]);
        }
        String md5Sign = MD5Info(WebUtils.generateStr(signData, "&"));

        //验证MD5信息是否相等
        if(map.get("SignMD5info") != null &&
                md5Sign.equals(map.get("SignMD5info"))){
            if(HuichaoConfig.RESULT_SUCCESS.equals(map.get("Succeed"))) {
                try {
                    //更新个人账号、充值记录、交易记录
                    saveBackForSuccess(map.get("BillNo"), String.valueOf(MathUtil.round(MathUtil.multiply(map.get("Amount"), 100), 0)));
                } catch (Exception e) {
                    logger.error("back occurs an error and cause by {}", e.getMessage());
                }
                rechargeOutDTO.setResult(HuichaoConfig.RESULT_SUCCESS);
            }else{
                //判断是否为异步通知
                if(RechargeModel.BACK.equals(rechargeInDTO.getRechargeModel())){
                    try {
                        //充值失败，更新充值记录、交易记录状态
                        saveBackForFail(map.get("TransId"));
                    } catch (Exception e) {
                        logger.error("back occurs an error and cause by {}", e.getMessage());
                    }
                }
            }
            //生成交易回调数据
            saveTradeCallBack(map.get("orderId"));

            rechargeOutDTO.setBean(HuichaoConfig.SUCCESS);
            return rechargeOutDTO;
        }

        rechargeOutDTO.setBean(HuichaoConfig.FAIL);
        logger.error("汇潮充值MD5信息不一致，处理失败！");
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
     * 初始化汇潮充值接口数据
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
        map.put("MerNo", paymentInterfaceDTO.getMerchantId());//商户号
        map.put("BillNo", rechargeInDTO.getTransId());//订单号
        map.put("Amount", String.valueOf(MathUtil.round(rechargeInDTO.getMoney(), 2)));//金额 单位：元
        map.put("ReturnURL", paymentInterfaceDTO.getReturnUrl());//页面跳转同步通知页面
        map.put("AdviceURL", paymentInterfaceDTO.getNoticeUrl());//服务器异步通知路径

        //封装接口数据并生成签名，{param}表示param的值
        //签名规则：“{MerNo}&{BillNo}&{Amount}&{ReturnURL}&{MD5key}”
        String signData[] = new String[HuichaoConfig.reqVo.length];
        for(int i=0;i<HuichaoConfig.reqVo.length;i++){
            signData[i] = map.get(HuichaoConfig.reqVo[i]);
        }
        signData[HuichaoConfig.reqVo.length-1] = paymentInterfaceDTO.getMerchantKey();
        String sign = WebUtils.generateStr(signData, "&");
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
        return new MD5().generateMD5Sign(str).toUpperCase();
    }
}
