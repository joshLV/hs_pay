package com.hszsd.webpay.service.impl;

import com.hszsd.account.service.AccountService;
import com.hszsd.common.util.Result;
import com.hszsd.common.util.math.MathUtil;
import com.hszsd.entity.RechargeRecord;
import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.common.RechargeType;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.config.YeePayConfig;
import com.hszsd.webpay.dao.AccountQuickBankDao;
import com.hszsd.webpay.service.RechargeService;
import com.hszsd.webpay.util.HttpUtils;
import com.hszsd.webpay.util.JsonUtil;
import com.hszsd.webpay.util.YeePayUtil;
import com.hszsd.webpay.web.dto.AccountQuickBankDTO;
import com.hszsd.webpay.web.dto.PaymentInterfaceDTO;
import com.hszsd.webpay.web.dto.RechargeInDTO;
import com.hszsd.webpay.web.dto.RechargeOutDTO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 易宝充值服务业务层
 * Created by gzhengDu on 2016/8/11.
 */
@Service("yeepayRechargeService")
public class YeepayRechargeServiceImpl extends RechargeService{
    private static final Logger logger = LoggerFactory.getLogger(YeepayRechargeServiceImpl.class);

    @Autowired
    private AccountQuickBankDao accountQuickBankDao;

    @Autowired
    private AccountService accountService;

    /**
     * 返回易宝充值类型对象
     * @return RechargeType.YEEPAY
     */
    @Override
    public RechargeType type() {
        return RechargeType.YEEPAY;
    }

    /**
     * 易宝充值
     * @param rechargeInDTO 充值入参
     * @return RechargeOutDTO 充值返回结果
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public RechargeOutDTO recharge(RechargeInDTO rechargeInDTO) throws Exception{
        logger.info("recharge is starting and rechargeInDTO={}", rechargeInDTO);
        RechargeOutDTO rechargeOutDTO = new RechargeOutDTO();

        //获取第三方接口配置信息
        PaymentInterfaceDTO paymentInterfaceDTO = super.getPaymentInterfaceDTO();
        //初始化第三方充值参数
        Map<String, String> map = initSignData(paymentInterfaceDTO, rechargeInDTO);
        if(map.size() == 0){
            logger.error("recharge failed and initSignData failed");
            rechargeOutDTO.setResult(ResultConstants.OPERATOR_FAIL);
            return rechargeOutDTO;
        }
        //初始化充值记录并保存
        RechargeRecord saveRechargeRecord = new RechargeRecord();
        saveRechargeRecord.setTradeNo(map.get("orderid"));
        saveRechargeRecord.setUserId(rechargeInDTO.getUser().getUserId());
        saveRechargeRecord.setStatus(GlobalConstants.RECHARGE.WATI_CHECK);
        saveRechargeRecord.setMoney(BigDecimal.valueOf(MathUtil.divide(Double.valueOf(map.get("amount")), 100, 2)));
        saveRechargeRecord.setPayment(paymentInterfaceDTO.getInterfaceValue());
        saveRechargeRecord.setType(GlobalConstants.RECHARGE.MOBILE);
        saveRechargeRecord.setRemark(
                StringUtils.join(
                        "用户",
                        rechargeInDTO.getUser().getUsername(),
                        "通过",
                        type().getName(),
                        "充值，订单号：",
                        saveRechargeRecord.getTradeNo(),
                        "，充值金额：",
                        saveRechargeRecord.getMoney(),
                        "元"
                )
        );
        saveRechargeRecord.setAddtime(System.currentTimeMillis());

        //根据dubbo服务器处理结果封装返回参数
        try {
            if (!coreService.insertRechargeRecord(saveRechargeRecord)) {
                rechargeOutDTO.setResult(ResultConstants.OPERATOR_FAIL);
                return rechargeOutDTO;
            }
        }catch (Exception e){
            logger.error("YeePayRechargeServiceImpl occurs an error and cause by {}", e.getMessage());
            rechargeOutDTO.setResult(ResultConstants.OPERATOR_FAIL);
            return rechargeOutDTO;
        }

        //向快捷支付发送请求
        Map<String, String> resultMap = YeePayUtil.directBindPay(map);
        //请求失败, 回滚数据
        if(resultMap.get("customError")!=null || resultMap.get("error_code")!=null){
            logger.info("YeePayRechargeServiceImpl failed and directBindPay failed, resultMap={}", resultMap);
            rechargeOutDTO.setResult(ResultConstants.OPERATOR_FAIL);
            return rechargeOutDTO;
        }
        rechargeOutDTO.setBean(resultMap);
        rechargeOutDTO.setResult(ResultConstants.OPERATOR_SUCCESS);
        return rechargeOutDTO;
    }

    /**
     * 前台回调方法
     * 易宝快捷充值不存在同步回调
     * @param rechargeInDTO 充值入参
     */
    @Override
    public void front(RechargeInDTO rechargeInDTO) {

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

        //声明业务处理成功标识
        boolean saveSuccessFlag = false;
        //获取第三方充值接口返回参数
        Map<String, String> map = YeePayUtil.decryptCallbackData(rechargeInDTO.getRequest().getParameter("data"), rechargeInDTO.getRequest().getParameter("encryptkey"));
        logger.info("yeePayBack paramMap={}", map);

        //验证MD5信息
        if(map.get("customError") != null){
            rechargeOutDTO.setBean(YeePayConfig.FAIL);
            logger.error("易宝充值加密信息不一致，处理失败！");
            return rechargeOutDTO;
        }

        //MD5验证通过后，验证支付结果，如果成功则更新个人账号、充值记录、交易记录
        if(YeePayConfig.RESULT_SUCCESS.equals(map.get("status"))) {
            try {
                //更新个人账号、充值记录、交易记录
                saveSuccessFlag = saveBackForSuccess(map.get("orderid"), map.get("amount"));
            } catch (Exception e) {
                logger.error("back occurs an error and cause by {}", e.getMessage());
            }
        }else{
            try {
                //充值失败，更新充值记录、交易记录状态、撤销资金操作
                saveSuccessFlag = saveBackForFail(map.get("orderid"), map.get("amount"));
            } catch (Exception e) {
                logger.error("back occurs an error and cause by {}", e.getMessage());
            }
        }

        //判断业务处理成功是否
        if(!saveSuccessFlag) {
            logger.info("back failed and saveSuccessFlag={}", saveSuccessFlag);
            rechargeOutDTO.setBean(YeePayConfig.FAIL);
        }

        //生成交易回调数据（使用定时任务查询需要回调的数据通知商户）
        try {
            saveTradeCallBack(map.get("orderid"));
        }catch (Exception e){
            logger.error("back occurs an error and cause by {}", e.getMessage());
            rechargeOutDTO.setBean(YeePayConfig.FAIL);
            return rechargeOutDTO;
        }
        rechargeOutDTO.setBean(YeePayConfig.SUCCESS);
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
     * 初始化易宝充值接口数据
     * @param paymentInterfaceDTO 充值接口配置信息
     * @param rechargeInDTO 充值入参
     * @return Map 充值接口数据集合
     */
    @Override
    public Map<String, String> initSignData(PaymentInterfaceDTO paymentInterfaceDTO, RechargeInDTO rechargeInDTO) {
        logger.info("initSignData is starting and paymentInterfaceDTO={}, rechargeInDTO={}", paymentInterfaceDTO, rechargeInDTO);
        Map<String, String> map = new HashMap<String, String>();
        AccountQuickBankDTO accountQuickBankDTO = accountQuickBankDao.selectByPrimaryKey(rechargeInDTO.getBankId());
        if(accountQuickBankDTO == null){
            try {
                //调用dubbox查询用户快捷卡信息
                com.hszsd.account.dto.AccountQuickBankDTO dubboCondition = new com.hszsd.account.dto.AccountQuickBankDTO();
                dubboCondition.setUserId(rechargeInDTO.getUser().getUserId());
                dubboCondition.setRequestId(rechargeInDTO.getBankId());
                Result result = accountService.queryAccountQuickBank(dubboCondition);
                if(ResultConstants.OPERATOR_SUCCESS.getCode().equals(result.getResCode())){
                    List<com.hszsd.account.dto.AccountQuickBankDTO> dubboQuickBanks = (List<com.hszsd.account.dto.AccountQuickBankDTO>) result.getResult();
                    if(dubboQuickBanks.size() > 0) {
                        accountQuickBankDTO = new AccountQuickBankDTO();
                        BeanUtils.copyProperties(accountQuickBankDTO, dubboQuickBanks.get(0));
                    }
                }
            }catch (Exception e){
                logger.error("initSignData occurs an error and cause by {}", e.getMessage());
            }finally {
                if(accountQuickBankDTO == null){
                    logger.error("initSignData failed and account quick bank isn't exist and id={}", rechargeInDTO.getBankId());
                    return map;
                }
            }
        }
        //封装充值接口参数
        //必输项
        map.put("orderid", rechargeInDTO.getTransId());//商户生成的订单号
        map.put("amount", String.valueOf((int) MathUtil.multiply(rechargeInDTO.getMoney(), "100"))); //订单金额 单位：分
        map.put("productname", "用户充值"); //商品名称
        map.put("identityid", rechargeInDTO.getUser().getUserId()); //用户标识
        map.put("card_top", accountQuickBankDTO.getCardTop()); //卡号前六位
        map.put("card_last", accountQuickBankDTO.getCardLast()); //卡号后四位
        map.put("callbackurl", paymentInterfaceDTO.getNoticeUrl());
        map.put("userip", HttpUtils.getRemoteHost(rechargeInDTO.getRequest()));
        logger.info("initSignData success and return map={}", JsonUtil.obj2json(map));

        return map;
    }

    /**
     * 易宝MD5加密算法
     * @param str 源字符串
     * @return MD5校验码
     */
    @Override
    public String MD5Info(String str) {
        return null;
    }
}
