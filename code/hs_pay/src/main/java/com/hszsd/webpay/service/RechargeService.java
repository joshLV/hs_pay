package com.hszsd.webpay.service;

import com.hszsd.common.util.Result;
import com.hszsd.common.util.ResultCode;
import com.hszsd.common.util.math.MathUtil;
import com.hszsd.core.service.CoreService;
import com.hszsd.entity.AccountLog;
import com.hszsd.entity.RechargeRecord;
import com.hszsd.user.dto.GetUserInfoDTO;
import com.hszsd.user.service.UserService;
import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.common.RechargeType;
import com.hszsd.webpay.dao.TradeCallbackDao;
import com.hszsd.webpay.dao.TradeRecordDao;
import com.hszsd.webpay.po.TradeCallbackPO;
import com.hszsd.webpay.po.TradeRecordPO;
import com.hszsd.webpay.util.JsonUtil;
import com.hszsd.webpay.web.dto.PaymentInterfaceDTO;
import com.hszsd.webpay.web.dto.RechargeInDTO;
import com.hszsd.webpay.web.dto.RechargeOutDTO;
import com.hszsd.webpay.web.dto.TradeCallbackDTO;
import com.hszsd.webpay.web.dto.TradeRecordDTO;
import com.jiangshikun.parrot.dao.KeyDao;
import com.jiangshikun.parrot.dao.RedisBaseDao;
import com.jiangshikun.parrot.dao.SetDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 抽象支付服务业务层
 * Created by gzhengDu on 2016/7/4.
 */
public abstract class RechargeService {
    private static final Logger logger = LoggerFactory.getLogger(RechargeService.class);

    @Autowired
    public RechargeApplicationService rechargeApplicationService;

    @Autowired
    public UserService userService;

    @Autowired
    public CoreService coreService;

    @Autowired
    public CommonService commonService;

    @Autowired
    public TradeRecordDao tradeRecordDao;

    @Autowired
    public TradeCallbackDao tradeCallbackDao;

    private KeyDao keyDao = RedisBaseDao.getKeyDao();
    private SetDao setDao = RedisBaseDao.getSetDao();

    /**
     * 返回当前充值方式
     * @return String 返回充值方式
     */
    public abstract RechargeType type();

    /**
     * 充值
     * @param rechargeInDTO 充值入参
     * @return RechargeOutDTO 充值返回结果
     */
    public abstract RechargeOutDTO recharge(RechargeInDTO rechargeInDTO) throws Exception;

    /**
     * 前台回调方法
     * 1. 验证MD5是否正确
     * 2. 判断支付结果是否成功
     * 2-1. 成功则更新个人账号、充值记录、交易记录
     * 2-2. 失败不做处理
     * @param rechargeInDTO 充值入参
     */
    public abstract void front(RechargeInDTO rechargeInDTO);

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
    public abstract RechargeOutDTO back(RechargeInDTO rechargeInDTO);

    /**
     * 查询接口
     * @param rechargeInDTO
     * @return
     */
    public abstract RechargeOutDTO query(RechargeInDTO rechargeInDTO);
    
    /**
     * 根据不同充值方式初始化相应充值接口数据
     * @param paymentInterfaceDTO 充值接口配置信息
     * @param rechargeInDTO 充值入参
     * @return Map 充值接口数据集合
     */
    public abstract Map<String, String> initSignData(PaymentInterfaceDTO paymentInterfaceDTO, RechargeInDTO rechargeInDTO);

    /**
     * 将源字符串转为MD5校验码
     * @param str 源字符串
     * @return MD5校验码
     */
    public abstract String MD5Info(String str);

    /**
     * 充值成功后 保存相关记录，并返回是否处理成功状态
     * @param transId 交易流水号
     * @param money  交易金额 以分为单位
     * @return boolean 业务处理成功标识
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public synchronized boolean saveBackForSuccess(String transId, String money) throws Exception{
        //redis关键字，用于判断当前交易流水号是否正在处理中，如果正在处理则跳过
        String redisTransIdKey = StringUtils.join(GlobalConstants.COMMON.REDIS_TRANSID_PREFIX, transId);
        //业务处理成功标识
        boolean saveSuccessFlag = false;
        if(!keyDao.isExits(redisTransIdKey)){
            setDao.save(redisTransIdKey,transId);
            //获取交易记录并判断交易记录是否存在
            TradeRecordDTO tradeRecordDTO = tradeRecordDao.selectByPrimaryKey(transId);
            if(tradeRecordDTO == null){
                logger.error("saveBackForSuccess failed and trade record isn't exist and transId={}", transId);
                keyDao.delKey(redisTransIdKey);
                return saveSuccessFlag;
            }
            //交易记录状态为成功，则表示该笔交易已处理成功过，直接返回提示信息
            if(GlobalConstants.TRADE_RECORD.TRADE_STATUS_2 == tradeRecordDTO.getTradeStatus()){
                logger.error("saveBackForSuccess failed and trade record has checked and transId={}", transId);
                keyDao.delKey(redisTransIdKey);
                saveSuccessFlag = true;
                return saveSuccessFlag;
            }

            //更新交易记录状态
            TradeRecordPO tradeRecordPO = new TradeRecordPO();
            try {
                BeanUtils.copyProperties(tradeRecordDTO, tradeRecordPO);
            }catch (Exception e){
                logger.error("saveBackForSuccess occurs an error and cause by {} and tradeRecordDTO={}", e.getMessage(), tradeRecordDTO);
                keyDao.delKey(redisTransIdKey);
                return saveSuccessFlag;
            }
            tradeRecordPO.setOperateType(Integer.parseInt(type().getCode()));
            tradeRecordPO.setMoney(BigDecimal.valueOf(MathUtil.divide(Double.valueOf(money), 100)));
            tradeRecordPO.setTradeStatus(GlobalConstants.TRADE_RECORD.TRADE_STATUS_2);
            tradeRecordPO.setUpdateDate(new Date());

            if(tradeRecordDao.updateByPrimaryKeySelective(tradeRecordPO) <= 0){
                logger.error("saveBackForSuccess failed and updateByPrimaryKeySelective failed and tradeRecordPO={}", JsonUtil.obj2json(tradeRecordPO));
                keyDao.delKey(redisTransIdKey);
                return saveSuccessFlag;
            }

            //查询充值记录并判断是否存在且状态为未审核
            RechargeRecord rechargeRecord = coreService.selectRechargeRecordByTradeNo(transId);
            if(rechargeRecord == null || GlobalConstants.RECHARGE.WATI_CHECK != rechargeRecord.getStatus()){
                logger.info("saveBackForSuccess failed , recharge record isn't exist or has been checked");
                keyDao.delKey(redisTransIdKey);
                return saveSuccessFlag;
            }

            //封装充值对象、资金日志对象并调用dubbo服务修改充值记录、资金记录、资金账户
            // 1. 调用成功，继续执行
            // 2. 调用失败，回滚数据，交易记录状态为等待处理并直接返回
            Result userResult = userService.getUserInfo(rechargeRecord.getUserId());
            if (!ResultCode.RES_OK.equals(userResult.getResCode())) {
                logger.error("saveBackForSuccess failed and UserResult={}", userResult);
                keyDao.delKey(redisTransIdKey);
                return saveSuccessFlag;
            }
            GetUserInfoDTO userInfoDTO = (GetUserInfoDTO) userResult.getResult();
            RechargeRecord saveRechargeRecord = new RechargeRecord();
            BeanUtils.copyProperties(rechargeRecord, saveRechargeRecord);
            String remark = StringUtils.join("用户",
                    userInfoDTO.getUsername(),
                    "通过",
                    type().getName(),
                    "充值成功，订单号：",
                    transId
                    ,"充值金额",
                    MathUtil.divide(Double.valueOf(money), 100)
            );
            saveRechargeRecord.setId(rechargeRecord.getId());
            saveRechargeRecord.setTradeNo(transId);
            saveRechargeRecord.setMoney(BigDecimal.valueOf(MathUtil.divide(Double.valueOf(money), 100)));
            saveRechargeRecord.setStatus(GlobalConstants.RECHARGE.ALLOW_CHECKED);
            saveRechargeRecord.setAuditTime(System.currentTimeMillis());
            saveRechargeRecord.setRemark(remark);
            AccountLog saveAccountLog = new AccountLog();
            saveAccountLog.setUserId(rechargeRecord.getUserId());
            saveAccountLog.setMoney(rechargeRecord.getMoney());
            saveAccountLog.setType(GlobalConstants.ACCOUNT_LOG.TYPE_1);
            saveAccountLog.setRemark(remark);

            saveSuccessFlag = coreService.addUseTotalMoney(saveAccountLog, saveRechargeRecord, transId);
            if(!saveSuccessFlag){
                keyDao.delKey(redisTransIdKey);
                throw new Exception(
                        StringUtils.join(
                                "dubbo is failed, rechargeRecord=",
                                JsonUtil.obj2json(saveRechargeRecord),
                                "saveAccountLog=",
                                JsonUtil.obj2json(saveAccountLog)));
            }
            keyDao.delKey(redisTransIdKey);
            saveSuccessFlag = true;
        }

        return saveSuccessFlag;
    }

    /**
     * 第三方充值异步通知返回失败时，回滚相关记录，并返回是否处理成功状态
     * @param transId 交易流水号
     * @param money   操作金额
     * @return boolean 是否处理成功
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public synchronized boolean saveBackForFail(String transId, String money) throws Exception{
        //redis关键字，用于判断当前交易流水号是否正在处理中，如果正在处理则跳过
        String redisTransIdKey = StringUtils.join(GlobalConstants.COMMON.REDIS_TRANSID_PREFIX, transId);
        //业务处理成功标识
        boolean saveSuccessFlag = false;
        if(!keyDao.isExits(redisTransIdKey)){
            setDao.save(redisTransIdKey,transId);
            //查询交易记录
            TradeRecordDTO tradeRecordDTO = tradeRecordDao.selectByPrimaryKey(transId);
            //更新交易记录状态
            TradeRecordPO tradeRecordPO = new TradeRecordPO();
            try {
                BeanUtils.copyProperties(tradeRecordDTO, tradeRecordPO);
            }catch (Exception e){
                logger.error("saveBackForFail occurs an error and cause by {} and tradeRecordDTO={}", e.getMessage(), tradeRecordDTO);
                keyDao.delKey(redisTransIdKey);
                return saveSuccessFlag;
            }
            tradeRecordPO.setOperateType(Integer.parseInt(type().getCode()));
            tradeRecordPO.setMoney(BigDecimal.valueOf(MathUtil.divide(Double.valueOf(money), 100)));
            tradeRecordPO.setTradeStatus(GlobalConstants.TRADE_RECORD.TRADE_STATUS_3);
            tradeRecordPO.setUpdateDate(new Date());
            if(tradeRecordDao.updateByPrimaryKeySelective(tradeRecordPO) <= 0){
                logger.error("saveBackForFail failed and updateByPrimarySelective failed and tradeRecordPO={}", JsonUtil.obj2json(tradeRecordPO));
                keyDao.delKey(redisTransIdKey);
                return saveSuccessFlag;
            }

            //如果交易状态为
            // 1: 等待处理 更新充值记录状态
            // 2: 交易成功 更新充值记录状态
            if(GlobalConstants.TRADE_RECORD.TRADE_STATUS_1 == tradeRecordDTO.getTradeStatus()){
                //调用dubbo进行充值记录状态更新，如果调用失败则回滚
                RechargeRecord rechargeRecord = coreService.selectRechargeRecordByTradeNo(transId);
                //查询充值记录并判断是否存在且状态为未审核
                if(rechargeRecord == null){
                    logger.info("saveBackForFail failed , recharge record isn't exist");
                    keyDao.delKey(redisTransIdKey);
                    return saveSuccessFlag;
                }
                Result userResult = userService.getUserInfo(rechargeRecord.getUserId());
                if (!ResultCode.RES_OK.equals(userResult.getResCode())) {
                    logger.error("saveBackForSuccess failed and UserResult={}", userResult);
                    keyDao.delKey(redisTransIdKey);
                    return saveSuccessFlag;
                }
                GetUserInfoDTO userInfoDTO = (GetUserInfoDTO) userResult.getResult();
                RechargeRecord saveRechargeRecord = new RechargeRecord();
                BeanUtils.copyProperties(rechargeRecord, saveRechargeRecord);
                saveRechargeRecord.setStatus(GlobalConstants.RECHARGE.NOALLOW_CHECKED);
                saveRechargeRecord.setAuditTime(System.currentTimeMillis());
                saveRechargeRecord.setRemark(
                        StringUtils.join("用户",
                        userInfoDTO.getUsername(),
                        "通过",
                        type().getName(),
                        "充值失败，充值金额",
                        MathUtil.divide(Double.valueOf(money), 100)));
                saveSuccessFlag = coreService.updateRechargeRecord(saveRechargeRecord);

                keyDao.delKey(redisTransIdKey);
                if(!saveSuccessFlag){
                    throw new Exception(
                            StringUtils.join(
                                    "dubbo is failed, rechargeRecord=",
                                    JsonUtil.obj2json(saveRechargeRecord)));
                }
                saveSuccessFlag = true;
                return saveSuccessFlag;
            }
            /*if(GlobalConstants.TRADE_RECORD.TRADE_STATUS_2 == tradeRecordDTO.getTradeStatus()){
                //调用dubbo撤销资金记录，如果调用失败则回滚
            }*/

            keyDao.delKey(redisTransIdKey);
            saveSuccessFlag = true;
        }

        return saveSuccessFlag;
    }

    /**
     * 异步通知商户处理结果
     * 如果商户没有返回确认信息则添加一条回调记录，由定时任务定时异步通知商户
     * @param transId 交易流水号
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveTradeCallBack(String transId){
        logger.info("saveTradeCallBack is starting and transId={}", transId);
        TradeRecordDTO tradeRecordDTO = tradeRecordDao.selectByPrimaryKey(transId);
        if(tradeRecordDTO == null){
            logger.info("saveTradeCallBack failed and trade record is null");
            return ;
        }
        //异步通知商户
        String backMessage = commonService.asyncNoticeMerchant(tradeRecordDTO);
        //判断商户返回通知
        if(backMessage == null || !GlobalConstants.COMMON.BACK_MESSAGE.equals(backMessage.toUpperCase())){
            TradeCallbackDTO tradeCallbackDTO = tradeCallbackDao.selectByPrimaryKey(transId);
            if(tradeCallbackDTO != null){
                logger.info("saveTradeCallBack failed and trade callback record is exist");
                return ;
            }
            TradeCallbackPO tradeCallbackPO = new TradeCallbackPO();
            tradeCallbackPO.setTransId(transId);
            tradeCallbackPO.setCount(GlobalConstants.COMMON.CALLBACK_DEFAULT_COUNT);
            tradeCallbackPO.setCreateDate(new Date());
            tradeCallbackDao.insert(tradeCallbackPO);
        }
    }

    /**
     * 获取第三方充值接口配置信息
     * @return PaymentInterfaceDTO 第三方充值接口配置信息
     */
    public final PaymentInterfaceDTO getPaymentInterfaceDTO(){
        return rechargeApplicationService.createPaymentInterface(type().getCode());
    }

}
