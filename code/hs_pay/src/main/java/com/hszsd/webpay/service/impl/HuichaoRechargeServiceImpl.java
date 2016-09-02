package com.hszsd.webpay.service.impl;

import com.hszsd.common.util.date.DateUtil;
import com.hszsd.common.util.math.MathUtil;
import com.hszsd.entity.RechargeRecord;
import com.hszsd.md5.beartool.MD5;
import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.common.RechargeType;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.common.SubmitType;
import com.hszsd.webpay.config.HuichaoConfig;
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

    /**
     * 返回汇潮充值类型对象
     * @return RechargeType.HUICHAO
     */
    @Override
    public RechargeType type() {
        return RechargeType.HUICHAO;
    }

    /**
     * 汇潮充值
     * @param rechargeInDTO 充值入参
     * @return RechargeOutDTO 充值返回结果
     */
    @Override
    @Transactional
    public RechargeOutDTO recharge(RechargeInDTO rechargeInDTO) {
        logger.info("recharge is starting and rechargeInDTO={}", rechargeInDTO);
        RechargeOutDTO rechargeOutDTO = new RechargeOutDTO();

        //获取第三方充值接口配置信息
        PaymentInterfaceDTO paymentInterfaceDTO = super.getPaymentInterfaceDTO();
        //初始化第三方充值参数
        Map<String, String> map = initSignData(paymentInterfaceDTO, rechargeInDTO);
        //初始化充值记录并保存
        RechargeRecord saveRechargeRecord = new RechargeRecord();
        saveRechargeRecord.setTradeNo(map.get("BillNo"));
        saveRechargeRecord.setUserId(rechargeInDTO.getUser().getUserId());
        saveRechargeRecord.setStatus(GlobalConstants.RECHARGE.WATI_CHECK);
        saveRechargeRecord.setMoney(BigDecimal.valueOf(MathUtil.divide(Double.valueOf(map.get("Amount")), 1, 2)));
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
            logger.error("HuichaoRechargeServiceImpl occurs an error and cause by {}", e.getMessage());
            rechargeOutDTO.setResult(ResultConstants.OPERATOR_FAIL);
            return rechargeOutDTO;
        }

        rechargeOutDTO.setUrlType(SubmitType.FORM.toString());
        rechargeOutDTO.setRequest(StringUtils.join(paymentInterfaceDTO.getPayUrl(),"&", WebUtils.generateUrl(map)));
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
        logger.info("huichaoFront paramMap={}", map);

        //验证MD5信息
        if(!checkMD5Sign(map)){
            logger.error("汇潮充值MD5信息不一致，处理失败！");
            return ;
        }

        //MD5验证通过后，验证支付结果，如果成功则更新个人账号、充值记录、交易记录
        if(HuichaoConfig.RESULT_SUCCESS.equals(map.get("Succeed"))) {
            try {
                //更新个人账号、充值记录、交易记录
                saveBackForSuccess(map.get("BillNo"), String.valueOf(MathUtil.round(MathUtil.multiply(map.get("Amount"), "100"), 0)));
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
        logger.info("huichaoBack paramMap={}", map);

        //声明业务处理成功标识
        boolean saveSuccessFlag = false;
        //验证MD5信息是否相等
        if(!checkMD5Sign(map)){
            rechargeOutDTO.setBean(HuichaoConfig.FAIL);
            logger.error("汇潮充值MD5信息不一致，处理失败！");
            return rechargeOutDTO;
        }

        //MD5验证通过后，验证支付结果，如果成功则更新个人账号、充值记录、交易记录
        if(HuichaoConfig.RESULT_SUCCESS.equals(map.get("Succeed"))) {
            try {
                //更新个人账号、充值记录、交易记录
                saveSuccessFlag = saveBackForSuccess(map.get("BillNo"), String.valueOf(MathUtil.round(MathUtil.multiply(map.get("Amount"), "100"), 0)));
            } catch (Exception e) {
                logger.error("back occurs an error and cause by {}", e.getMessage());
            }
            rechargeOutDTO.setResult(HuichaoConfig.RESULT_SUCCESS);
        }else{
            try {
                //充值失败，更新充值记录、交易记录状态
                saveSuccessFlag = saveBackForFail(map.get("BillNo"), String.valueOf(MathUtil.round(MathUtil.multiply(map.get("Amount"), "100"), 0)));
            } catch (Exception e) {
                logger.error("back occurs an error and cause by {}", e.getMessage());
            }
        }

        //判断业务处理是否成功
        if(!saveSuccessFlag) {
            logger.info("back failed and saveSuccessFlag={}", saveSuccessFlag);
            rechargeOutDTO.setBean(HuichaoConfig.FAIL);
            return rechargeOutDTO;
        }
        //生成交易回调数据（使用定时任务查询需要回调的数据通知商户）
        try{
            saveTradeCallBack(map.get("BillNo"));
        }catch (Exception e){
            logger.error("back occurs an error and cause by {}", e.getMessage());
            rechargeOutDTO.setBean(HuichaoConfig.FAIL);
            return rechargeOutDTO;
        }
        rechargeOutDTO.setBean(HuichaoConfig.SUCCESS);
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
        //签名规则：“{MerNo}&{BillNo}&{Amount}&{ReturnURL}&{MD5Key}”
        String signData[] = new String[HuichaoConfig.reqVo.size()];
        for(int i=0;i<HuichaoConfig.reqVo.size();i++){
            signData[i] = map.get(HuichaoConfig.reqVo.get(i));
        }
        signData[HuichaoConfig.reqVo.size()-1] = paymentInterfaceDTO.getMerchantKey();
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

    /**
     * 根据参数校验MD5加密串是否正确
     * @param paramMap 封装待验证的数据
     * @return boolean 是否验证通过
     */
    public boolean checkMD5Sign(Map<String, String> paramMap){
        //获取第三方充值的配置信息
        PaymentInterfaceDTO paymentInterfaceDTO = super.getPaymentInterfaceDTO();

        //签名规则：{BillNo}&{Amount}&{Succeed}&{MD5Key}
        paramMap.put("MD5Key", paymentInterfaceDTO.getMerchantKey());
        String signData[] = new String[HuichaoConfig.resVo.size()];
        for(int i=0;i<HuichaoConfig.resVo.size();i++){
            signData[i] = paramMap.get(HuichaoConfig.resVo.get(i));
        }
        String checkMd5Sign = MD5Info(WebUtils.generateStr(signData, "&"));

        return checkMd5Sign.equals(paramMap.get("SignMD5info"));
    }
}
