package com.hszsd.webpay.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hszsd.common.util.date.DateUtil;
import com.hszsd.common.util.string.StringUtil;
import com.hszsd.core.service.CoreService;
import com.hszsd.entity.AccountLog;
import com.hszsd.entity.CreditLog;
import com.hszsd.entity.RechargeRecord;
import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.common.RechargeType;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.common.ResultInfo;
import com.hszsd.webpay.condition.TradeRecordCondition;
import com.hszsd.webpay.dao.TradeCallbackDao;
import com.hszsd.webpay.dao.TradeRecordDao;
import com.hszsd.webpay.po.TradeCallbackPO;
import com.hszsd.webpay.po.TradeRecordPO;
import com.hszsd.webpay.service.CommonService;
import com.hszsd.webpay.service.PaymentService;
import com.hszsd.webpay.service.RechargeApplicationService;
import com.hszsd.webpay.util.JsonUtil;
import com.hszsd.webpay.web.dto.PaymentInterfaceDTO;
import com.hszsd.webpay.web.dto.TradeRecordDTO;
import com.hszsd.webpay.web.form.SelectTradesForm;
import com.jiangshikun.parrot.dao.RedisBaseDao;
import com.jiangshikun.parrot.dao.StringDao;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 交易记录业务层接口实现
 * Created by suocy on 2016/7/15.
 */
@Service("paymentService")
@Transactional(rollbackFor = {Exception.class,RuntimeException.class})
public class PaymentServiceImpl implements PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private TradeRecordDao tradeRecordDao;
    @Autowired
    private CommonService commonService;
    @Autowired
    private TradeCallbackDao tradeCallbackDao;
    @Autowired
    private CoreService coreService;
    @Autowired
    private RechargeApplicationService rechargeApplicationService;

    private StringDao stringDao = RedisBaseDao.getStringDao();
    private static final int ONE_DAY = 24*60*60;


    /**
     * 创建交易记录
     * @return map
     */
    @Override
    public ResultInfo createTradeRecord(Map<String,String> map){
        ResultInfo resultInfo = new ResultInfo();
        TradeRecordPO tradeRecord = new TradeRecordPO();
        tradeRecord.setTransId(map.get("transId"));
        tradeRecord.setUserId(map.get("userId"));
        String money = map.get("money");
        if(!StringUtils.isEmpty(money)){
            try{
                BigDecimal moneyBigDecimal = new BigDecimal(money).setScale(2, BigDecimal.ROUND_HALF_UP);
                if(moneyBigDecimal.compareTo(BigDecimal.ZERO)< 1){
                    logger.error("createTradeRecord occurs an error and cause by: 金额不为正数");
                    resultInfo.setIsSuccess(false);
                    resultInfo.setResult(ResultConstants.MONEY_WRONG);
                    return resultInfo;
                }
                tradeRecord.setMoney(moneyBigDecimal);
            }catch(Exception e){
                logger.error("createTradeRecord occurs an error and cause by money format is error");
                resultInfo.setIsSuccess(false);
                resultInfo.setResult(ResultConstants.MONEY_WRONG);
                return resultInfo;
            }

        }
        String credit = map.get("credit");
        if(!StringUtils.isEmpty(credit)){
            try{
                BigDecimal creditBigDecimal = new BigDecimal(credit).setScale(0,BigDecimal.ROUND_HALF_UP);
                if(creditBigDecimal.compareTo(BigDecimal.ZERO)< 1){
                    logger.error("createTradeRecord occurs an error and cause by：积分不为正数");
                    resultInfo.setIsSuccess(false);
                    resultInfo.setResult(ResultConstants.CREDIT_WRONG);
                    return resultInfo;
                }
                tradeRecord.setCredit(creditBigDecimal);
            }catch(Exception e){
                logger.error("createTradeRecord occurs an error and cause by credit format is error");
                resultInfo.setIsSuccess(false);
                resultInfo.setResult(ResultConstants.CREDIT_WRONG);
                return resultInfo;
            }

        }
        tradeRecord.setProductName(map.get("proName"));
        tradeRecord.setMobile(map.get("mobile"));
        tradeRecord.setOrderId(map.get("orderId"));
        tradeRecord.setSourceCode(map.get("sourceCode"));
        tradeRecord.setOperateType(Integer.parseInt(map.get("operateType")));
        tradeRecord.setOrderType(Integer.parseInt(map.get("orderType")));
        tradeRecord.setTradeStatus(GlobalConstants.TRADE_RECORD.TRADE_STATUS_1);
        tradeRecord.setCreateDate(new Date());
        tradeRecord.setReturnUrl(map.get("returnUrl"));
        tradeRecord.setNoticeUrl(map.get("noticeUrl"));
        tradeRecord.setRemark(map.get("remark"));
        tradeRecord.setToUserId(map.get("toUserId"));

        if(tradeRecordDao.insert(tradeRecord) < 1){
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
            return resultInfo;
        }
        resultInfo.setIsSuccess(true);
        resultInfo.setResult(ResultConstants.OPERATOR_SUCCESS);
        return resultInfo;
    }

    /**
     * 订单支付业务处理
     * @param transId 交易序号
     * @param operateType 操作类型
     * @return
     */
    @Override
    public ResultInfo editTradeRecord(String transId, int operateType){
        ResultInfo resultInfo = new ResultInfo();
        TradeRecordDTO tradeRecordDTO = tradeRecordDao.selectByPrimaryKey(transId);
        int orderType = tradeRecordDTO.getOrderType();
        if(orderType == GlobalConstants.TRADE_RECORD.ORDER_TYPE_ZZSC){ //正宗商城
            resultInfo = editTradeRecordZZSC(transId);
        }
        else if(orderType == GlobalConstants.TRADE_RECORD.ORDER_TYPE_ZTWY){ //中天物业
            resultInfo = editTradeRecordZTWY(transId,operateType);
        }
        else if(orderType == GlobalConstants.TRADE_RECORD.ORDER_TYPE_ZTSC){ //微商城
            resultInfo = editTradeRecordZTSC(transId,operateType);
        }else{
            logger.error("editTradeRecord --> 未知的订单类型orderType：{}...", orderType);
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
        }
        return resultInfo;
    }

    /**
     * 正宗商城订单支付业务处理
     * 1.订单去重
     * 2.功能服务器接口进行金额或积分增减操作
     * 3.根据操作结果进行数据处理和回调外部系统
     * @param transId 交易序号
     * @return
     */
    @Override
    public ResultInfo editTradeRecordZZSC(String transId){
        ResultInfo resultInfo = new ResultInfo();
        TradeRecordDTO tradeRecordDTO = tradeRecordDao.selectByPrimaryKey(transId);
        String orderId = tradeRecordDTO.getOrderId();
        //检查重复订单
        if(!commonService.checkRepeatedOrder(orderId)){
            //修改交易记录状态为失败，并保存交易异步通知信息
            editTradeStatusAndCallBack(transId,GlobalConstants.TRADE_RECORD.TRADE_STATUS_3,0);
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.ORDER_REPEAT);
            return resultInfo;
        }
        stringDao.save(GlobalConstants.COMMON.REDIS_ORDERID_PREFIX + orderId, orderId,ONE_DAY);
        //调功能服务器接口,操作金额、积分
        boolean optFlag = this.optCoreAccountZZSC(tradeRecordDTO);
        if(!optFlag){
            stringDao.remove(GlobalConstants.COMMON.REDIS_ORDERID_PREFIX + orderId);
            logger.error("editTradeRecordZZSC occurs an error because dubbox interface is failed");
            //修改交易记录状态为失败，并保存交易异步通知信息
            editTradeStatusAndCallBack(transId,GlobalConstants.TRADE_RECORD.TRADE_STATUS_3,0);
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
            return resultInfo;
        }

        //修改交易记录状态为成功，并保存交易异步通知信息
        editTradeStatusAndCallBack(transId,GlobalConstants.TRADE_RECORD.TRADE_STATUS_2,0);
        stringDao.remove(GlobalConstants.COMMON.REDIS_ORDERID_PREFIX + orderId);
        resultInfo.setIsSuccess(true);
        resultInfo.setResult(ResultConstants.OPERATOR_SUCCESS);
        return resultInfo;
    }

    /**
     * 中天物业订单缴费业务处理--余额支付
     * 1.订单去重
     * 2.功能服务器接口进行金额操作
     * 3.根据操作结果进行数据处理和回调外部系统
     * @param transId 交易序号
     * @param operateType 操作类型
     * @return
     */
    @Override
    public ResultInfo editTradeRecordZTWY(String transId, int operateType){
        ResultInfo resultInfo = new ResultInfo();
        TradeRecordDTO tradeRecordDTO = tradeRecordDao.selectByPrimaryKey(transId);
        String orderId = tradeRecordDTO.getOrderId();
        //检查重复订单
        if(!commonService.checkRepeatedOrder(orderId)){
            //修改交易记录状态为失败，并保存交易异步通知信息
            editTradeStatusAndCallBack(transId,GlobalConstants.TRADE_RECORD.TRADE_STATUS_3,operateType);
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.ORDER_REPEAT);
            return resultInfo;
        }
        stringDao.save(GlobalConstants.COMMON.REDIS_ORDERID_PREFIX + orderId, orderId,ONE_DAY);
        //调功能服务器接口,操作金额、积分
        boolean optFlag = this.optCoreAccountZTWY(tradeRecordDTO,operateType);

        if(!optFlag){
            stringDao.remove(GlobalConstants.COMMON.REDIS_ORDERID_PREFIX + orderId);
            logger.error("editTradeRecordZTWY occurs an error because dubbox interface is failed");
            //修改交易记录状态为失败，并保存交易异步通知信息
            editTradeStatusAndCallBack(transId,GlobalConstants.TRADE_RECORD.TRADE_STATUS_3,operateType);
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
            return resultInfo;
        }

        //修改交易记录状态为成功，并保存交易异步通知信息
        editTradeStatusAndCallBack(transId,GlobalConstants.TRADE_RECORD.TRADE_STATUS_2,operateType);
        stringDao.remove(GlobalConstants.COMMON.REDIS_ORDERID_PREFIX + orderId);
        resultInfo.setIsSuccess(true);
        resultInfo.setResult(ResultConstants.OPERATOR_SUCCESS);
        return resultInfo;
    }

    /**
     * 微商城订单支付业务处理--余额支付
     * 1.订单去重
     * 2.功能服务器接口进行金额操作
     * 3.根据操作结果进行数据处理和回调外部系统
     * @param transId 交易序号
     * @param operateType 操作类型
     * @return
     */
    @Override
    public ResultInfo editTradeRecordZTSC(String transId, int operateType){
        ResultInfo resultInfo = new ResultInfo();
        TradeRecordDTO tradeRecordDTO = tradeRecordDao.selectByPrimaryKey(transId);
        String orderId = tradeRecordDTO.getOrderId();
        //检查重复订单
        if(!commonService.checkRepeatedOrder(orderId)){
            //修改交易记录状态为失败，并保存交易异步通知信息
            editTradeStatusAndCallBack(transId,GlobalConstants.TRADE_RECORD.TRADE_STATUS_3,operateType);
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.ORDER_REPEAT);
            return resultInfo;
        }
        stringDao.save(GlobalConstants.COMMON.REDIS_ORDERID_PREFIX + orderId, orderId,ONE_DAY);
        //调功能服务器接口,操作金额、积分
        boolean optFlag = this.optCoreAccountZTSC(tradeRecordDTO,operateType);
        if(!optFlag){
            stringDao.remove(GlobalConstants.COMMON.REDIS_ORDERID_PREFIX + orderId);
            logger.error("editTradeRecordZTSC occurs an error because dubbox interface is failed");
            //修改交易记录状态为失败，并保存交易异步通知信息
            editTradeStatusAndCallBack(transId,GlobalConstants.TRADE_RECORD.TRADE_STATUS_3,operateType);
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
            return resultInfo;
        }

        //修改交易记录状态为成功，并保存交易异步通知信息
        editTradeStatusAndCallBack(transId,GlobalConstants.TRADE_RECORD.TRADE_STATUS_2,operateType);
        stringDao.remove(GlobalConstants.COMMON.REDIS_ORDERID_PREFIX + orderId);
        resultInfo.setIsSuccess(true);
        resultInfo.setResult(ResultConstants.OPERATOR_SUCCESS);
        return resultInfo;
    }

    /**
     * 通过交易流水号，查询交易记录
     * @param transId
     * @return
     */
    @Override
    public ResultInfo queryTradeRecord(String transId) {
        ResultInfo resultInfo = new ResultInfo();
        TradeRecordDTO tradeRecordDTO = null;
        try{
            tradeRecordDTO  = tradeRecordDao.selectByPrimaryKey(transId);
        }catch(Exception e){
            logger.error("queryTradeRecord occurs an error and cause by {}", e.getMessage());
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
            return resultInfo;
        }

        if(tradeRecordDTO == null){
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
            return resultInfo;
        }
        resultInfo.setIsSuccess(true);
        resultInfo.setResult(ResultConstants.OPERATOR_SUCCESS);
        resultInfo.setParams(tradeRecordDTO);
        return resultInfo;
    }

    /**
     * 修改交易记录状态，并保存交易异步通知信息
     * @param transId
     * @param tradeStatus
     * @param operateType
     * @return
     */
    @Override
    public boolean editTradeStatusAndCallBack(String transId, int tradeStatus, int operateType){
        logger.info("PaymentService -->editTradeStatusAndCallBack is starting,transId = {},tradeStatus = {}",transId,tradeStatus);
        //修改交易记录状态
        TradeRecordPO tradeRecordPO = new TradeRecordPO();
        tradeRecordPO.setTransId(transId);
        tradeRecordPO.setUpdateDate(new Date());
        tradeRecordPO.setTradeStatus(tradeStatus);
        if(operateType!=0){
            tradeRecordPO.setOperateType(operateType);
        }
        tradeRecordDao.updateByPrimaryKeySelective(tradeRecordPO);
        //立即异步通知
        TradeRecordDTO tradeRecordDTO  = tradeRecordDao.selectByPrimaryKey(transId);
        String backMag = commonService.asyncNoticeMerchant(tradeRecordDTO);
        if(!GlobalConstants.COMMON.BACK_MESSAGE.equals(backMag.toUpperCase())){
            //通知失败后，保存交易异步通知信息
            TradeCallbackPO tradeCallbackPO = new TradeCallbackPO();
            tradeCallbackPO.setTransId(transId);
            tradeCallbackPO.setCount(GlobalConstants.COMMON.CALLBACK_DEFAULT_COUNT);
            tradeCallbackPO.setCreateDate(new Date());
            tradeCallbackDao.insert(tradeCallbackPO);
        }
        logger.info("PaymentService -->editTradeStatusAndCallBack end");
        return true;
    }


    /**
     * 正宗商城功能服务器接口：操作金额与积分
     * @param tradeRecordDTO
     * @return
     */
    @Override
    public boolean optCoreAccountZZSC(TradeRecordDTO tradeRecordDTO) {
        boolean result = false;
        int operateType = tradeRecordDTO.getOperateType();
        String transId = tradeRecordDTO.getTransId();
        String userId = tradeRecordDTO.getUserId();
        BigDecimal money = tradeRecordDTO.getMoney();
        BigDecimal credit = tradeRecordDTO.getCredit();
        String remark = tradeRecordDTO.getRemark();
        AccountLog accountLog;
        CreditLog creditLog;
        try{
            switch (operateType){
                //减余额
                case GlobalConstants.TRADE_RECORD.BALANCE_PAY:
                    accountLog = initAccountLog(userId, money, remark, GlobalConstants.ACCOUNT_LOG.TYPE_24,"");
                    logger.info("optCoreAccountZZSC--->subUseTotalMoney:accountLog={},transId={}",JsonUtil.obj2json(accountLog),transId);
                    result =  coreService.subUseTotalMoney(accountLog,transId);
                    logger.info("optCoreAccountZZSC--->subUseTotalMoney:result={}",result);
                    break;
                //加余额
                case GlobalConstants.TRADE_RECORD.REFUND:
                    accountLog = initAccountLog(userId, money, remark, GlobalConstants.ACCOUNT_LOG.TYPE_25,"");
                    logger.info("optCoreAccountZZSC--->addUseTotalMoney:accountLog={},transId={}",JsonUtil.obj2json(accountLog),transId);
                    result =  coreService.addUseTotalMoney(accountLog,transId);
                    logger.info("optCoreAccountZZSC--->addUseTotalMoney:result={}",result);
                    break;
                //减积分
                case GlobalConstants.TRADE_RECORD.CREDIT_PAY:
                    creditLog = initCreditLog(userId, credit, remark, GlobalConstants.CREDIT_LOG.TYPEID_5);
                    logger.info("optCoreAccountZZSC--->subUseTotalCredit:creditLog={},transId={}",JsonUtil.obj2json(creditLog),transId);
                    result = coreService.subUseTotalCredit(creditLog, transId);
                    logger.info("optCoreAccountZZSC--->subUseTotalCredit:result={}",result);
                    break;
                //加积分
                case GlobalConstants.TRADE_RECORD.ADD_CREDIT:
                    creditLog = initCreditLog(userId, credit, remark, GlobalConstants.CREDIT_LOG.TYPEID_6);
                    logger.info("optCoreAccountZZSC--->addUseTotalCredit:creditLog={},transId={}",JsonUtil.obj2json(creditLog),transId);
                    result = coreService.addUseTotalCredit(creditLog, transId);
                    logger.info("optCoreAccountZZSC--->addUseTotalCredit:result={}",result);
                    break;
                //组合支付
                case GlobalConstants.TRADE_RECORD.ASSEMBLE_PAY:
                    accountLog = initAccountLog(userId, money, remark, GlobalConstants.ACCOUNT_LOG.TYPE_24,"");
                    creditLog = initCreditLog(userId, credit, remark, GlobalConstants.CREDIT_LOG.TYPEID_5);
                    logger.info("optCoreAccountZZSC--->subUseTotal:accountLog={},creditLog={},transId={}",JsonUtil.obj2json(accountLog),JsonUtil.obj2json(creditLog), transId);
                    result = coreService.subUseTotal(accountLog, creditLog, transId);
                    logger.info("optCoreAccountZZSC--->subUseTotal:result={}",result);
                    break;
                //组合退款
                case GlobalConstants.TRADE_RECORD.ASSEMBLE_REFUND:
                    accountLog = initAccountLog(userId, money, remark, GlobalConstants.ACCOUNT_LOG.TYPE_25,"");
                    creditLog = initCreditLog(userId, credit, remark, GlobalConstants.CREDIT_LOG.TYPEID_6);
                    logger.info("optCoreAccountZZSC--->addUseTotal:accountLog={},creditLog={},transId={}", JsonUtil.obj2json(accountLog),JsonUtil.obj2json(creditLog), transId);
                    result = coreService.addUseTotal(accountLog, creditLog, transId);
                    logger.info("optCoreAccountZZSC--->addUseTotal:result={}",result);
                default:
                    logger.error("optCoreAccountZZSC--->未匹配到的操作类型，operateType={}",operateType);
                    break;
            }
        }catch(Exception e){
            logger.error("optCoreAccountZZSC occurs an error because dubbox interface is occurs an Exception :{}",e.getMessage());
            result = false;
        }
        return result;
    }

    /**
     * 中天物业功能服务器接口：操作金额
     * @param tradeRecordDTO
     * @param operateType
     * @return
     */
    @Override
    public boolean optCoreAccountZTWY(TradeRecordDTO tradeRecordDTO, int operateType) {
        boolean result = false;
        operateType = operateType==0? tradeRecordDTO.getOperateType():operateType;
        String transId = tradeRecordDTO.getTransId();
        String userId = tradeRecordDTO.getUserId();
        BigDecimal money = tradeRecordDTO.getMoney();
        String remark = tradeRecordDTO.getRemark();
        String toUserId = tradeRecordDTO.getToUserId();
        AccountLog accountLog;
        try{
            switch (operateType){
                //余额支付
                case GlobalConstants.TRADE_RECORD.BALANCE_PAY:
                    accountLog = initAccountLog(userId, money, remark + "--缴费冻结", GlobalConstants.ACCOUNT_LOG.TYPE_26,toUserId);
                    logger.info("optCoreAccountZTWY--->moveUseToNoUse:accountLog={},transId={}",JsonUtil.obj2json(accountLog),transId);
                    result =  coreService.moveUseToNoUse(accountLog,transId);
                    logger.info("optCoreAccountZTWY--->moveUseToNoUse:result={}",result);
                    break;
                //快捷卡支付
                case GlobalConstants.TRADE_RECORD.QUICK_PAY:
                    AccountLog rechargeAccountLog = initAccountLog(userId, money, remark + "--易宝充值", GlobalConstants.ACCOUNT_LOG.TYPE_1,toUserId);
                    accountLog = initAccountLog(userId, money, remark + "--缴费冻结", GlobalConstants.ACCOUNT_LOG.TYPE_26,toUserId);
                    //组装充值记录对象
                    RechargeRecord rechargeRecord = new RechargeRecord();
                    rechargeRecord.setUserId(userId);
                    rechargeRecord.setTradeNo(transId);
                    rechargeRecord.setStatus(GlobalConstants.RECHARGE.ALLOW_CHECKED);
                    rechargeRecord.setMoney(tradeRecordDTO.getMoney());
                    PaymentInterfaceDTO paymentInterface = rechargeApplicationService.createPaymentInterface(RechargeType.YEEPAY.getCode());
                    rechargeRecord.setPayment(paymentInterface.getInterfaceValue());
                    rechargeRecord.setType(GlobalConstants.RECHARGE.ONLINE);
                    rechargeRecord.setRemark(remark + "--易宝充值");
                    rechargeRecord.setAddtime(System.currentTimeMillis());
                    rechargeRecord.setAuditTime(System.currentTimeMillis());

                    logger.info("optCoreAccountZTWY--->addNoUseTotalMoney:rechargeAccountLog={},accountLog={},transId={}",JsonUtil.obj2json(rechargeAccountLog),JsonUtil.obj2json(accountLog),transId);
                    result =  coreService.addNoUseTotalMoney(rechargeAccountLog,accountLog,rechargeRecord,transId);
                    logger.info("optCoreAccountZTWY--->addNoUseTotalMoney:result={}",result);
                    break;
                default:
                    logger.error("optCoreAccountZTWY--->未匹配到的操作类型,operateType={}",operateType);
                    break;
            }
        }catch(Exception e){
            logger.error("optCoreAccountZTWY occurs an error because dubbox interface is occurs an Exception :{}",e.getMessage());
            result = false;
        }
        return result;
    }

    /**
     * 微商城（中天商城）功能服务器接口：操作金额
     * @param tradeRecordDTO
     * @param operateType
     * @return
     */
    @Override
    public boolean optCoreAccountZTSC(TradeRecordDTO tradeRecordDTO, int operateType) {
        boolean result = false;
        operateType = operateType==0? tradeRecordDTO.getOperateType():operateType;
        String transId = tradeRecordDTO.getTransId();
        String userId = tradeRecordDTO.getUserId();
        BigDecimal money = tradeRecordDTO.getMoney();
        String remark = tradeRecordDTO.getRemark();
        String toUserId = tradeRecordDTO.getToUserId();
        AccountLog fromAccountLog;
        AccountLog toAccountLog;
        try{
            switch (operateType){
                //余额支付
                case GlobalConstants.TRADE_RECORD.BALANCE_PAY:
                    fromAccountLog = initAccountLog(userId, money, remark + "--微商城支付扣款", GlobalConstants.ACCOUNT_LOG.TYPE_24,toUserId);
                    toAccountLog = initAccountLog(toUserId, money, remark  + "--微商城入账", GlobalConstants.ACCOUNT_LOG.TYPE_30,"");
                    logger.info("optCoreAccountZTSC--->transeferUseToUse:fromAccountLog={},toAccountLog={},transId={}",JsonUtil.obj2json(fromAccountLog),JsonUtil.obj2json(toAccountLog),transId);
                    result =  coreService.transeferUseToUse(fromAccountLog,toAccountLog,transId);
                    logger.info("optCoreAccountZTSC--->transeferUseToUse:result={}",result);
                    break;
                //快捷卡支付
                case GlobalConstants.TRADE_RECORD.QUICK_PAY:
                    AccountLog addAccountLog = initAccountLog(userId, money, remark + "--易宝充值", GlobalConstants.ACCOUNT_LOG.TYPE_1, toUserId);
                    fromAccountLog = initAccountLog(userId, money, remark + "--微商城支付扣款", GlobalConstants.ACCOUNT_LOG.TYPE_24,toUserId);
                    toAccountLog = initAccountLog(toUserId, money, remark  + "--微商城入账", GlobalConstants.ACCOUNT_LOG.TYPE_30,"");
                    //组装充值记录对象
                    RechargeRecord rechargeRecord = new RechargeRecord();
                    rechargeRecord.setUserId(userId);
                    rechargeRecord.setTradeNo(transId);
                    rechargeRecord.setStatus(GlobalConstants.RECHARGE.ALLOW_CHECKED);
                    rechargeRecord.setMoney(tradeRecordDTO.getMoney());
                    PaymentInterfaceDTO paymentInterface = rechargeApplicationService.createPaymentInterface(RechargeType.YEEPAY.getCode());
                    rechargeRecord.setPayment(paymentInterface.getInterfaceValue());
                    rechargeRecord.setType(GlobalConstants.RECHARGE.ONLINE);
                    rechargeRecord.setRemark(remark + "--易宝充值");
                    rechargeRecord.setAddtime(System.currentTimeMillis());
                    rechargeRecord.setAuditTime(System.currentTimeMillis());

                    logger.info("optCoreAccountZTSC--->transeferUseToUse:fromAccountLog={},toAccountLog={},transId={}",JsonUtil.obj2json(fromAccountLog),JsonUtil.obj2json(toAccountLog),transId);
                    result =  coreService.transeferUseToUse(addAccountLog,fromAccountLog,toAccountLog,rechargeRecord,transId);
                    logger.info("optCoreAccountZTSC--->transeferUseToUse:result={}",result);
                    break;
                default:
                    logger.error("optCoreAccountZTSC--->未匹配到的操作类型，operateType={}",operateType);
                    break;
            }
        }catch(Exception e){
            logger.error("optCoreAccountZTSC occurs an error because dubbox interface is occurs an Exception :{}",e.getMessage());
            result = false;
        }
        return result;
    }


    /**
     * 根据条件分页查询交易记录
     * @param selectTradesForm 查询条件对象
     * @return
     */
    @Override
    public ResultInfo queryTradeRecords(SelectTradesForm selectTradesForm) {
        ResultInfo resultInfo = new ResultInfo();
        PageHelper.startPage(selectTradesForm.getPageNum(),selectTradesForm.getPageSize());
        List<TradeRecordDTO> tradeRecordDTOs;
        try{
            TradeRecordCondition condition = new TradeRecordCondition();
            TradeRecordCondition.Criteria criteria = condition.createCriteria();
            //交易记录流水号
            if(!StringUtils.isEmpty(selectTradesForm.getTransId())){
                criteria.andTransIdEqualTo(selectTradesForm.getTransId());
            }
            //用户编号
            if(!StringUtils.isEmpty(selectTradesForm.getUserId())){
                criteria.andTransIdEqualTo(selectTradesForm.getUserId());
            }
            //金额
            String minMoney = selectTradesForm.getMinMoney();
            if(!StringUtils.isEmpty(minMoney)){
                criteria.andMoneyGreaterThanOrEqualTo(new BigDecimal(minMoney));
            }
            String maxMoney = selectTradesForm.getMaxMoney();
            if(!StringUtils.isEmpty(maxMoney)){
                criteria.andMoneyLessThanOrEqualTo(new BigDecimal(maxMoney));
            }
            //积分
            String minCredit = selectTradesForm.getMinCredit();
            if(!StringUtils.isEmpty(minCredit)){
                criteria.andCreditGreaterThanOrEqualTo(new BigDecimal(minCredit));
            }
            String maxCredit = selectTradesForm.getMaxCredit();
            if(!StringUtils.isEmpty(maxCredit)){
                criteria.andCreditLessThanOrEqualTo(new BigDecimal(maxCredit));
            }
            //用户手机
            if(!StringUtils.isEmpty(selectTradesForm.getMobile())){
                criteria.andMobileEqualTo(selectTradesForm.getMobile());
            }
            //来源方订单号
            if(!StringUtils.isEmpty(selectTradesForm.getOrderId())){
                criteria.andOrderIdEqualTo(selectTradesForm.getOrderId());
            }
            //平台来源（SH:商城 P2P:p2p平台 H5:微信）
            if(!StringUtils.isEmpty(selectTradesForm.getSourceCode())){
                criteria.andSourceCodeEqualTo(selectTradesForm.getSourceCode());
            }
            //操作类型
            String operateType = selectTradesForm.getOperateType();
            if(!StringUtils.isEmpty(operateType)){
                criteria.andOperateTypeEqualTo(Integer.parseInt(operateType));
            }
            //订单类型
            String orderType = selectTradesForm.getOrderType();
            if(!StringUtils.isEmpty(orderType)){
                criteria.andOrderTypeEqualTo(Integer.parseInt(orderType));
            }
            //交易状态（1:等待处理 2:交易成功 3:交易失败）
            String tradeStatus = selectTradesForm.getTradeStatus();
            if(!StringUtils.isEmpty(tradeStatus)){
                criteria.andTradeStatusEqualTo(Integer.parseInt(tradeStatus));
            }
            //交易时间
            String minTime = selectTradesForm.getMinTime();
            if(!StringUtils.isEmpty(minTime)){
                Date minDate = DateUtil.StrigToDateLong(minTime);
                criteria.andCreateDateGreaterThanOrEqualTo(minDate);
            }
            String maxTime = selectTradesForm.getMaxTime();
            if(!StringUtils.isEmpty(maxTime)){
                Date maxDate = DateUtil.StrigToDateLong(maxTime);
                criteria.andCreateDateLessThanOrEqualTo(maxDate);
            }
            //排序
            if(!StringUtils.isEmpty(selectTradesForm.getOrderBy())){
                String orderBy = StringUtil.camelToUnderline(selectTradesForm.getOrderBy());
                if(!StringUtils.isEmpty(selectTradesForm.getSort())){
                    condition.setOrderByClause(orderBy+" "+selectTradesForm.getSort());
                }else{
                    condition.setOrderByClause(orderBy);
                }
            }
            tradeRecordDTOs = tradeRecordDao.selectByCondition(condition);
        }catch(Exception e){
            e.printStackTrace();
            logger.error("PaymentServiceImpl.queryTradeRecords occurs an error and cause by {}", e.getMessage());
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
            return resultInfo;

        }
        resultInfo.setIsSuccess(true);
        resultInfo.setResult(ResultConstants.OPERATOR_SUCCESS);
        PageInfo pageInfo = new PageInfo(tradeRecordDTOs);
        resultInfo.setParams(pageInfo);
        return resultInfo;
    }

    /**
     * 初始化账户资金日志对象
     * @param userId
     * @param money
     * @param remark
     * @param type
     * @return
     */
    private AccountLog initAccountLog(String userId,BigDecimal money, String remark,int type,String toUserId){
        AccountLog accountLog = new AccountLog();
        accountLog.setUserId(userId);
        accountLog.setMoney(money);
        accountLog.setType(type);
        accountLog.setRemark(remark);
        accountLog.setToUser(toUserId);
        return accountLog;
    }

    /**
     * 初始化账户积分日志对象
     * @param userId
     * @param credit
     * @param remark
     * @param type
     * @return
     */
    private CreditLog initCreditLog(String userId, BigDecimal credit, String remark, int type){
        CreditLog creditLog = new CreditLog();
        creditLog.setUserId(userId);
        creditLog.setTypeId(type);
        creditLog.setCreditValue(credit);
        creditLog.setRemark(remark);
        return creditLog;
    }
}
