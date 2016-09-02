package com.hszsd.webpay.service.impl;

import com.hszsd.common.util.math.MathUtil;
import com.hszsd.entity.RechargeRecord;
import com.hszsd.md5.beartool.MD5;
import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.common.RechargeType;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.config.OnlineConfig;
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
import java.util.Map;
import java.util.TreeMap;

/**
 * 网银在线充值服务业务层
 * Created by gzhengDu on 2016/7/12.
 */
@Service("onlineRechargeService")
public class OnlineRechargeServiceImpl extends RechargeService {
    private static final Logger logger = LoggerFactory.getLogger(OnlineRechargeServiceImpl.class);

    /**
     * 返回网银在线充值类型对象
     * @return RechargeType.MOBILE
     */
    @Override
    public RechargeType type() {
        return RechargeType.ONLINE;
    }

    /**
     * 网银在线
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
        saveRechargeRecord.setTradeNo(map.get("v_oid"));
        saveRechargeRecord.setUserId(rechargeInDTO.getUser().getUserId());
        saveRechargeRecord.setStatus(GlobalConstants.RECHARGE.WATI_CHECK);
        saveRechargeRecord.setMoney(BigDecimal.valueOf(MathUtil.divide(Double.valueOf(map.get("v_amount")), 1, 2)));
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
            logger.error("OnlineRechargeServiceImpl occurs an error and cause by {}", e.getMessage());
            rechargeOutDTO.setResult(ResultConstants.OPERATOR_FAIL);
            return rechargeOutDTO;
        }

        rechargeOutDTO.setRequest(StringUtils.join(paymentInterfaceDTO.getPayUrl(),"?", WebUtils.generateUrl(map)));
        rechargeOutDTO.setResult(ResultConstants.OPERATOR_SUCCESS);
        logger.info("OnlineRechargeServiceImpl success and rechargeOutDTO={}", rechargeOutDTO);
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
        logger.info("onlineFront paramMap={}", map);

        //验证MD5信息
        if(!checkMD5Sign(map)){
            logger.error("网银在线MD5信息不一致，处理失败！");
            return ;
        }

        //MD5验证通过后，验证支付结果，如果成功则更新个人账号、充值记录、交易记录
        if(OnlineConfig.RESULT_SUCCESS.equals(map.get("v_pstatus"))) {
            try {
                //更新个人账号、充值记录、交易记录
                saveBackForSuccess(map.get("v_oid"), String.valueOf(MathUtil.round(MathUtil.multiply(map.get("v_amount"), "100"), 0)));
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
        logger.info("onlineBack paramMap={}", map);

        //声明业务处理成功标识
        boolean saveSuccessFlag = false;
        //验证MD5信息是否相等
        if(!checkMD5Sign(map)){
            rechargeOutDTO.setBean(OnlineConfig.FAIL);
            logger.error("网银在线MD5信息不一致，处理失败！");
            return rechargeOutDTO;

        }

        //MD5验证通过后，验证支付结果，如果成功则更新个人账号、充值记录、交易记录
        if(OnlineConfig.RESULT_SUCCESS.equals(map.get("v_pstatus"))) {
            try {
                //更新个人账号、充值记录、交易记录
                saveSuccessFlag = saveBackForSuccess(map.get("v_oid"), String.valueOf(MathUtil.round(MathUtil.multiply(map.get("v_amount"), "100"), 0)));
            } catch (Exception e) {
                logger.error("back occurs an error and cause by {}", e.getMessage());
            }
        }else{
            try {
                //充值失败，更新充值记录、交易记录状态
                saveSuccessFlag = saveBackForFail(map.get("v_oid"), String.valueOf(MathUtil.round(MathUtil.multiply(map.get("v_amount"), "100"), 0)));
            } catch (Exception e) {
                logger.error("back occurs an error and cause by {}", e.getMessage());
            }
        }

        //判断业务处理是否成功
        if(!saveSuccessFlag) {
            logger.info("back failed and saveSuccessFlag={}", saveSuccessFlag);
            rechargeOutDTO.setBean(OnlineConfig.FAIL);
            return rechargeOutDTO;
        }
        //生成交易回调数据（使用定时任务查询需要回调的数据通知商户）
        try{
            saveTradeCallBack(map.get("v_oid"));
        }catch (Exception e){
            logger.error("back occurs an error and cause by {}", e.getMessage());
            rechargeOutDTO.setBean(OnlineConfig.FAIL);
            return rechargeOutDTO;
        }
        rechargeOutDTO.setBean(OnlineConfig.SUCCESS);
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
     * 初始化网银在线接口数据
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
        map.put("v_mid", paymentInterfaceDTO.getMerchantId());//商户ID
        map.put("v_oid", rechargeInDTO.getTransId());//订单编号
        map.put("v_amount", String.valueOf(MathUtil.round(rechargeInDTO.getMoney(), 2)));//订单总金额 单位：元
        map.put("v_moneytype", OnlineConfig.MONEY_TYPE_CNY);//币种
        //正式环境
        map.put("v_url", paymentInterfaceDTO.getReturnUrl());//URL地址
        map.put("pmode_id", rechargeInDTO.getBankId());//网银直连使用参数

        //封装接口数据并生成签名，{param}表示param的值
        //签名规则：“{v_amount}{v_moneytype}{v_oid}{v_mid}{v_url}{MD5Key}”
        String signData[] = new String[OnlineConfig.reqVo.size()];
        for(int i = 0; i< OnlineConfig.reqVo.size(); i++){
            signData[i] = map.get(OnlineConfig.reqVo.get(i));
        }
        signData[OnlineConfig.reqVo.size()-1] = paymentInterfaceDTO.getMerchantKey();
        String sign = WebUtils.generateStr(signData, "");
        map.put("v_md5info", MD5Info(sign));//MD5验证码

        //非必输项
        map.put("remark 1", "");//备注1
        map.put("remark2", StringUtils.join("[url:=", paymentInterfaceDTO.getNoticeUrl(), "]"));//备注2
        map.put("v_rcvname", "");//收货人姓名
        map.put("v_rcvaddr", "");//收货人地址
        map.put("v_rcvtel", "");//收货人电话
        map.put("v_rcvpost", "");//收货人邮编
        map.put("v_rcvemail", "");//收货人Email
        map.put("v_ordername", "");//收货人手机号
        map.put("v_ordername", "");//订货人姓名
        map.put("v_orderaddr", "");//订货人地址
        map.put("v_ordertel", "");//订货人电话
        map.put("v_orderpost", "");//订货人邮编
        map.put("v_orderemail", "");//订货人Email
        map.put("v_ordermobile", "");//订货人手机号

        logger.info("initSignData success and map={}", map);
        return map;
    }

    /**
     * 网银在线MD5加密算法
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

        //签名规则：{v_oid}{v_pstatus}{v_amount}{v_moneytype}{MD5Key}
        paramMap.put("MD5Key", paymentInterfaceDTO.getMerchantKey());
        String[] signData = new String[OnlineConfig.resVo.size()];
        for(int i = 0; i< OnlineConfig.resVo.size(); i++){
            signData[i] = paramMap.get(OnlineConfig.resVo.get(i));
        }
        String checkMd5Sign = MD5Info(WebUtils.generateStr(signData, ""));

        return checkMd5Sign.equals(paramMap.get("v_md5str"));
    }
}
