package com.hszsd.webpay.service.impl;

import com.hszsd.md5.beartool.MD5;
import com.hszsd.common.util.date.DateUtil;
import com.hszsd.common.util.math.MathUtil;
import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.common.RechargeModel;
import com.hszsd.webpay.common.RechargeType;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.config.OnlineConfig;
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
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * 网银在线充值服务业务层
 * Created by gzhengDu on 2016/7/12.
 */
@Service("onlineRechargeService")
public class OnlineRechargeServiceImpl extends RechargeService {
    private static final Logger logger = LoggerFactory.getLogger(OnlineRechargeServiceImpl.class);

    @Autowired
    private RechargeRecordDao rechargeRecordDao;

    /**
     * 返回网银在线的标识
     * @return String 网银在线标识：22
     */
    @Override
    public String key() {
        return RechargeType.ONLINE.getCode();
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
        RechargeRecordPO rechargeRecordPO = new RechargeRecordPO();
        rechargeRecordPO.setTradeNo(map.get("v_oid"));
        rechargeRecordPO.setUserId(rechargeInDTO.getUser().getUserId());
        rechargeRecordPO.setStatus(GlobalConstants.RECHARGE.WATI_CHECK);
        double money = MathUtil.divide(Double.valueOf(map.get("v_amount")), 1, 2);
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
        //签名规则：{v_oid}{v_pstatus}{v_amount}{v_moneytype}{Md5Key}
        map.put("MD5Key", paymentInterfaceDTO.getMerchantKey());
        String[] signData = new String[OnlineConfig.resVo.length];
        for(int i = 0; i< OnlineConfig.resVo.length; i++){
            signData[i] = map.get(OnlineConfig.resVo[i]);
        }
        String md5Sign = MD5Info(WebUtils.generateStr(signData, ""));

        //验证MD5信息是否相等
        if(map.get("v_md5str") != null &&
                md5Sign.equals(map.get("v_md5str"))){
            if(OnlineConfig.RESULT_SUCCESS.equals(map.get("v_pstatus"))) {
                try {
                    //更新个人账号、充值记录、交易记录
                    saveBackForSuccess(map.get("v_oid"), String.valueOf(MathUtil.round(MathUtil.multiply(map.get("v_amount"), 100), 0)));
                } catch (Exception e) {
                    logger.error("back occurs an error and cause by {}", e.getMessage());
                }
                rechargeOutDTO.setResult(OnlineConfig.RESULT_SUCCESS);
            }else{
                //判断是否为异步通知
                if(RechargeModel.BACK.equals(rechargeInDTO.getRechargeModel())){
                    try {
                        //充值失败，更新充值记录、交易记录状态
                        saveBackForFail(map.get("v_oid"));
                    } catch (Exception e) {
                        logger.error("back occurs an error and cause by {}", e.getMessage());
                    }
                }
                rechargeOutDTO.setResult(OnlineConfig.RESULT_FAIL);
            }
            //生成交易回调数据
            saveTradeCallBack(map.get("orderId"));

            rechargeOutDTO.setBean(OnlineConfig.SUCCESS);
            return rechargeOutDTO;
        }

        rechargeOutDTO.setResult(OnlineConfig.RESULT_FAIL);
        rechargeOutDTO.setBean(OnlineConfig.FAIL);
        logger.error("网银在线MD5信息不一致，处理失败！");
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
        map.put("v_url", paymentInterfaceDTO.getReturnUrl());//URL地址
        map.put("pmode_id", rechargeInDTO.getBankAddr());//接口文档中未找到该参数

        //封装接口数据并生成签名，{param}表示param的值
        //签名规则：“{v_amount}{v_moneytype}{v_oid}{v_mid}{v_url}{Md5key}”
        String signData[] = new String[OnlineConfig.reqVo.length];
        for(int i = 0; i< OnlineConfig.reqVo.length; i++){
            signData[i] = map.get(OnlineConfig.reqVo[i]);
        }
        signData[OnlineConfig.reqVo.length-1] = paymentInterfaceDTO.getMerchantKey();
        String sign = WebUtils.generateStr(signData, "");
        map.put("v_md5info", MD5Info(sign));//MD5验证码

        //非必输项
        map.put("remark 1", "");//备注1
        map.put("remark2", StringUtils.join("[:=", paymentInterfaceDTO.getNoticeUrl(), "]"));//备注2
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
}
