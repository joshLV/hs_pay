package com.hszsd.webpay.service.impl;

import com.hisun.iposm.HiiposmUtil;
import com.hszsd.common.util.date.DateUtil;
import com.hszsd.common.util.math.MathUtil;
import com.hszsd.entity.RechargeRecord;
import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.common.RechargeType;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.config.MobileConfig;
import com.hszsd.webpay.dao.BankInfoDao;
import com.hszsd.webpay.service.RechargeService;
import com.hszsd.webpay.util.HttpUtils;
import com.hszsd.webpay.util.WebUtils;
import com.hszsd.webpay.web.dto.BankInfoDTO;
import com.hszsd.webpay.web.dto.PaymentInterfaceDTO;
import com.hszsd.webpay.web.dto.RechargeInDTO;
import com.hszsd.webpay.web.dto.RechargeOutDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * 中移动和包充值服务业务层
 * Created by gzhengDu on 2016/7/12.
 */
@Service("mobileRechargeService")
public class MobileRechargeServiceImpl extends RechargeService {
    private static final Logger logger = LoggerFactory.getLogger(MobileRechargeServiceImpl.class);

    @Autowired
    private BankInfoDao bankInfoDao;

    private static HiiposmUtil hiiposmUtil = new HiiposmUtil();

    /**
     * 返回移动和包充值类型对象
     * @return RechargeType.MOBILE
     */
    @Override
    public RechargeType type() {
        return RechargeType.MOBILE;
    }

    /**
     * 和包充值
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
        saveRechargeRecord.setTradeNo(map.get("orderId"));
        saveRechargeRecord.setUserId(rechargeInDTO.getUser().getUserId());
        saveRechargeRecord.setStatus(GlobalConstants.RECHARGE.WATI_CHECK);
        saveRechargeRecord.setMoney(BigDecimal.valueOf(MathUtil.divide(Double.valueOf(map.get("amount")), 100, 2)));
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
            logger.error("MobileRechargeServiceImpl occurs an error and cause by {}", e.getMessage());
            rechargeOutDTO.setResult(ResultConstants.OPERATOR_FAIL);
            return rechargeOutDTO;
        }

        rechargeOutDTO.setRequest(StringUtils.join(paymentInterfaceDTO.getPayUrl(),"?", WebUtils.generateUrl(map)));
        if(StringUtils.isEmpty(rechargeInDTO.getBankId())){
            sendHttp(map, paymentInterfaceDTO, rechargeOutDTO);
            return rechargeOutDTO;
        }
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
        logger.info("mobileFront paramMap={}", map);

        //验证MD5信息
        if(!checkMD5Sign(map)){
            logger.error("和包充值MD5信息不一致，处理失败！");
            return ;
        }

        //MD5验证通过后，验证支付结果，如果成功则更新个人账号、充值记录、交易记录
        if(MobileConfig.RESULT_SUCCESS.equals(map.get("status"))) {
            try {
                //更新个人账号、充值记录、交易记录
                saveBackForSuccess(map.get("orderId"), map.get("amount"));
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
        logger.info("mobileBack paramMap={}", map);

        //验证MD5信息是否相等
        if(!checkMD5Sign(map)){
            rechargeOutDTO.setBean(MobileConfig.FAIL);
            logger.error("和包充值MD5信息不一致，处理失败！");
            return rechargeOutDTO;
        }

        //声明业务处理成功标识
        boolean saveSuccessFlag = false;
        //MD5验证通过后，验证支付结果，如果成功则更新个人账号、充值记录、交易记录
        if(MobileConfig.RESULT_SUCCESS.equals(map.get("status"))) {
            //更新个人账号、充值记录、交易记录
            try {
                saveSuccessFlag = saveBackForSuccess(map.get("orderId"), map.get("amount"));
            }catch (Exception e){
                logger.error("back occurs an error and cause by {}", e.getMessage());
            }
        }else{
            try {
                //充值失败，更新充值记录、交易记录状态
                saveSuccessFlag = saveBackForFail(map.get("orderId"), map.get("amount"));
            } catch (Exception e) {
                logger.error("back occurs an error and cause by {}", e.getMessage());
            }
        }

        //判断业务处理是否成功
        if(!saveSuccessFlag) {
            logger.info("back failed and saveSuccessFlag={}", saveSuccessFlag);
            rechargeOutDTO.setBean(MobileConfig.FAIL);
            return rechargeOutDTO;
        }
        //生成交易回调数据（使用定时任务查询需要回调的数据通知商户）
        try{
            saveTradeCallBack(map.get("orderId"));
        }catch (Exception e){
            logger.error("back occurs an error and cause by {}", e.getMessage());
            rechargeOutDTO.setBean(MobileConfig.FAIL);
            return rechargeOutDTO;
        }
        rechargeOutDTO.setBean(MobileConfig.SUCCESS);
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
     * 初始化和包充值接口数据
     * @param paymentInterfaceDTO 充值接口配置信息
     * @param rechargeInDTO 充值入参
     * @return Map 充值接口数据集合
     */
    @Override
    public Map<String, String> initSignData(PaymentInterfaceDTO paymentInterfaceDTO, RechargeInDTO rechargeInDTO) {
        logger.info("initSignData is starting and paymentInterfaceDTO={}, rechargeInDTO={}", paymentInterfaceDTO, rechargeInDTO);
        //封装充值接口参数
        Map<String, String> map = new TreeMap<String, String>();
        BankInfoDTO bankInfoDTO = new BankInfoDTO();
        if(StringUtils.isNotEmpty(rechargeInDTO.getBankId())){
            bankInfoDTO = bankInfoDao.selectByPrimaryKey(Long.parseLong(rechargeInDTO.getBankId()));
        }
        //协议参数
        map.put("characterSet", MobileConfig.CHARSET_UTF8);//字符集
        map.put("calbackUrl", paymentInterfaceDTO.getReturnUrl());//页面返回URL
        map.put("notifyUrl", paymentInterfaceDTO.getNoticeUrl());//后台通知URL
        map.put("ipAddress", HttpUtils.getRemoteHost(rechargeInDTO.getRequest()));//IP地址
        map.put("merchantId", paymentInterfaceDTO.getMerchantId());//商户编号
        map.put("requestId", rechargeInDTO.getTransId());//商户请求号
        map.put("signType", MobileConfig.SIGN_TYPE_MD5);//签名方式
        map.put("type", MobileConfig.TYPE);//接口类型
        map.put("version", MobileConfig.VERSION);//版本号
        //map.put("merchantCert", "");//商户证书公钥 签名方式为RSA时为必输项
        //业务参数
        map.put("amount", String.valueOf(MathUtil.round(MathUtil.multiply(rechargeInDTO.getMoney(), "100"), 0)));//订单金额 单位：分
        map.put("bankAbbr", bankInfoDTO.getBankValue());//银行代码
        map.put("currency", MobileConfig.CURRENCY_CNY);//币种
        map.put("orderDate", DateUtil.dateToString(new Date(), MobileConfig.DATE_FORMAT));//订单提交日期
        map.put("orderId", rechargeInDTO.getTransId());//商户订单号
        map.put("merAcDate", DateUtil.dateToString(new Date(), MobileConfig.DATE_FORMAT));//商户会计日期
        map.put("period", MobileConfig.PERIOD);//有效期数量 与订单有效期单位组成订单有效期
        map.put("periodUnit", MobileConfig.PERIOD_UNIT_MINUTE);//有效期单位
        map.put("merchantAbbr", "");//商户展示名称
        map.put("productDesc", "");//商品描述
        map.put("productId", "");//商品编号
        map.put("productName", MobileConfig.PRODUCT_NAME);//商品名称
        map.put("productNum", "");//商品数量
        map.put("reserved1", "");//保留字段1
        map.put("reserved2", "");//保留字段2
        map.put("userToken", "");//用户标识
        map.put("showUrl", "");//商品展示地址
        map.put("couponsFlag", "");//营销工具使用控制

        //封装接口数据并生成签名，{param}表示param的值
        //签名规则：
        String signData[] = new String[MobileConfig.reqVo.size()];
        for(int i=0;i<MobileConfig.reqVo.size();i++){
            signData[i] = map.get(MobileConfig.reqVo.get(i));
        }
        map.put("hmac", hiiposmUtil.MD5Sign(WebUtils.generateStr(signData, ""), paymentInterfaceDTO.getMerchantKey()));//签名数据

        return map;
    }

    /**
     * 和包充值MD5加密算法
     * @param str 源字符串
     * @return MD5校验码
     */
    @Override
    public String MD5Info(String str) {
        return null;
    }

    /**
     * 发送双向认证 HTPP请求
     * @param map
     * @param paymentInterfaceDTO
     * @param rechargeOutDTO
     */
    @SuppressWarnings("static-access")
    private void sendHttp(Map<String, String> map, PaymentInterfaceDTO paymentInterfaceDTO, RechargeOutDTO rechargeOutDTO){
        try {
            //发起http请求，并获取响应报文
            String res = hiiposmUtil.sendAndRecv(rechargeOutDTO.getRequest(), map.get("hmac"), map.get("characterSet"));
            //获得手机充值平台的消息摘要，用于验签,
            String hmac = hiiposmUtil.getValue(res, "hmac");
            String vfsign = StringUtils.join(hiiposmUtil.getValue(res, "merchantId")
                    , hiiposmUtil.getValue(res, "requestId")
                    , hiiposmUtil.getValue(res, "signType")
                    , hiiposmUtil.getValue(res, "type")
                    , hiiposmUtil.getValue(res, "version")
                    , hiiposmUtil.getValue(res, "returnCode")
                    , URLDecoder.decode(hiiposmUtil.getValue(res, "message"),"UTF-8")
                    , hiiposmUtil.getValue(res, "payUrl"));
            //响应码
            String code = hiiposmUtil.getValue(res, "returnCode");
            //校验响应码
            if(code==null || !"000000".equals(code)){
                rechargeOutDTO.setBean(StringUtils.join(
                        "下单错误:["
                        , code
                        , "]"
                        , URLDecoder.decode(hiiposmUtil.getValue(res,"message"),"UTF-8")));
                rechargeOutDTO.setResult(ResultConstants.OPERATOR_FAIL);
                return;
            }
            // -- 验证签名
            if (!hiiposmUtil.MD5Verify(vfsign, hmac, paymentInterfaceDTO.getMerchantKey())) {
                rechargeOutDTO.setBean("验签失败");
                return;
            }
            rechargeOutDTO.setResult(ResultConstants.OPERATOR_SUCCESS);
            //设置请求地址
            rechargeOutDTO.setRequest(hiiposmUtil.getRedirectUrl(hiiposmUtil.getValue(res, "payUrl")));
        } catch (Exception e) {
            e.printStackTrace();
            rechargeOutDTO.setBean("请求双向认证失败");
            rechargeOutDTO.setResult(ResultConstants.OPERATOR_FAIL);
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

        //签名规则：{merchantId}{payNo}{returnCode}{message}{signType}{type}{version}{amount}{amtItem}{bankAbbr}{mobile}{orderId}{payDate}{accountDate}{reserved1}{reserved2}{status}{orderDate}{fee}
        String[] signData = new String[MobileConfig.resVo.size()];
        for(int i = 0; i< MobileConfig.resVo.size(); i++){
            signData[i] = paramMap.get(MobileConfig.resVo.get(i));
        }

        return hiiposmUtil.MD5Verify(WebUtils.generateStr(signData, ""), paramMap.get("hmac"), paymentInterfaceDTO.getMerchantKey());
    }
}
