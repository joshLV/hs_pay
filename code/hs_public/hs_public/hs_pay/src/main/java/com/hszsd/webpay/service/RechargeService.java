package com.hszsd.webpay.service;

import com.hszsd.common.util.Result;
import com.hszsd.common.util.ResultCode;
import com.hszsd.common.util.math.MathUtil;
import com.hszsd.user.dto.GetUserInfoDTO;
import com.hszsd.user.service.UserService;
import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.condition.RechargeRecordCondition;
import com.hszsd.webpay.dao.RechargeRecordDao;
import com.hszsd.webpay.dao.TradeCallbackDao;
import com.hszsd.webpay.dao.TradeRecordDao;
import com.hszsd.webpay.po.RechargeRecordPO;
import com.hszsd.webpay.po.TradeCallbackPO;
import com.hszsd.webpay.po.TradeRecordPO;
import com.hszsd.webpay.web.dto.PaymentInterfaceDTO;
import com.hszsd.webpay.web.dto.RechargeInDTO;
import com.hszsd.webpay.web.dto.RechargeOutDTO;
import com.hszsd.webpay.web.dto.RechargeRecordDTO;
import com.hszsd.webpay.web.dto.TradeRecordDTO;
import com.jiangshikun.parrot.dao.KeyDao;
import com.jiangshikun.parrot.dao.RedisBaseDao;
import com.jiangshikun.parrot.dao.SetDao;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.runtime.parser.node.MathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 抽象支付服务业务层
 * Created by gzhengDu on 2016/7/4.
 */
public abstract class RechargeService {
    private static final Logger logger = LoggerFactory.getLogger(RechargeService.class);

    @Autowired
    public TransIdService transIdService;

    @Autowired
    public RechargeApplicationService rechargeApplicationService;

    @Autowired
    public UserService userService;

    @Autowired
    public RechargeRecordDao rechargeRecordDao;

    @Autowired
    public TradeRecordDao tradeRecordDao;

    @Autowired
    public TradeCallbackDao tradeCallbackDao;

    private KeyDao keyDao = RedisBaseDao.getKeyDao();
    private SetDao setDao = RedisBaseDao.getSetDao();

    /**
     * 返回当前充值方式标识
     * @return String 返回充值方式标识
     */
    public abstract String key();

    /**
     * 充值
     * @param rechargeInDTO 充值入参
     * @return RechargeOutDTO 充值返回结果
     */
    public abstract RechargeOutDTO recharge(RechargeInDTO rechargeInDTO);

    /**
     * 前台回调
     * @param rechargeInDTO
     * @return
     */
    public abstract RechargeOutDTO front(RechargeInDTO rechargeInDTO);

    /**
     * 后台回调
     * @param rechargeInDTO
     * @return
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
     * 充值成功后 保存相关记录
     * @param transId 交易流水号
     * @param amount  交易金额 以分为单位
     *
     */
    @Transactional
    public synchronized void saveBackForSuccess(String transId, String amount) throws Exception{
        if(!keyDao.isExits(transId)){
            setDao.save(transId,transId);
            //获取充值记录
            RechargeRecordCondition condition = new RechargeRecordCondition();
            condition.or().andTradeNoEqualTo(transId);
            List<RechargeRecordDTO> rechargeRecordDTOs = rechargeRecordDao.selectByCondition(condition);
            //充值记录是否存在 并且充值记录未审核
            if(rechargeRecordDTOs.size() <= 0){
                logger.error("saveBackForSuccess failed and recharge record isn't exist");
                keyDao.delKey(transId);
                return ;
            }
            RechargeRecordDTO rechargeRecordDTO = rechargeRecordDTOs.get(0);
            if(GlobalConstants.RECHARGE.WATI_CHECK != rechargeRecordDTO.getStatus()){
                logger.error("saveBackForSuccess failed and recharge record has checked");
                keyDao.delKey(transId);
                return ;
            }

            RechargeRecordPO rechargeRecordPO = new RechargeRecordPO();
            try {
                BeanUtils.copyProperties(rechargeRecordPO, rechargeRecordDTO);
            }catch (Exception e){
                logger.error("saveBackForSuccess occurs an error and cause by {}", e.getMessage());
            }
            //更新充值记录
            rechargeRecordPO.setMoney(BigDecimal.valueOf(MathUtil.divide(Double.valueOf(amount), 100)));
            rechargeRecordPO.setStatus(GlobalConstants.RECHARGE.ALLOW_CHECKED);
            rechargeRecordPO.setAuditTime(System.currentTimeMillis());
            rechargeRecordPO.setRemark(
                    StringUtils.join("用户",
                            "通过",
                            this.getPaymentInterfaceDTO().getName(),
                            "充值成功 充值金额 ",
                            String.valueOf(MathUtils.divide(Double.valueOf(amount), 100))));
            condition.or().andStatusEqualTo(GlobalConstants.RECHARGE.WATI_CHECK);
            rechargeRecordDao.updateByConditionSelective(rechargeRecordPO, condition);

            //更新交易记录状态
            TradeRecordDTO tradeRecordDTO = tradeRecordDao.selectByPrimaryKey(transId);
            TradeRecordPO tradeRecordPO = new TradeRecordPO();
            BeanUtils.copyProperties(tradeRecordPO, tradeRecordDTO);
            tradeRecordPO.setTradeStatus(GlobalConstants.TRADE_RECORD.TRADE_STATUS_2);
            tradeRecordPO.setUpdateDate(new Date());
            tradeRecordDao.updateByPrimaryKeySelective(tradeRecordPO);

            boolean accountFlag = true;//账户、资金记录 dubbo调用
            if(!accountFlag){
                throw new RuntimeException();
            }

            keyDao.delKey(transId);
        }
    }

    /**
     * 第三方充值返回充值失败信息时更新充值记录状态
     * @param transId 交易流水号
     */
    @Transactional
    public synchronized void saveBackForFail(String transId) throws Exception{
        if(!keyDao.isExits(transId)){
            setDao.save(transId,transId);
            //获取充值记录
            RechargeRecordCondition condition = new RechargeRecordCondition();
            condition.or().andTradeNoEqualTo(transId);
            List<RechargeRecordDTO> rechargeRecordDTOs = rechargeRecordDao.selectByCondition(condition);
            //充值记录是否存在 并且充值记录未审核
            if(rechargeRecordDTOs.size() <= 0){
                logger.error("saveBackForFail failed and recharge record isn't exist");
                keyDao.delKey(transId);
                return ;
            }
            RechargeRecordDTO rechargeRecordDTO = rechargeRecordDTOs.get(0);
            if(GlobalConstants.RECHARGE.WATI_CHECK != rechargeRecordDTO.getStatus()){
                logger.error("saveBackForFail failed and recharge record has checked");
                keyDao.delKey(transId);
                return ;
            }

            RechargeRecordPO rechargeRecordPO = new RechargeRecordPO();
            try {
                BeanUtils.copyProperties(rechargeRecordPO, rechargeRecordDTO);
            }catch (Exception e){
                logger.error("saveBackForFail occurs an error and cause by {}", e.getMessage());
            }
            //更新充值记录
            rechargeRecordPO.setStatus(GlobalConstants.RECHARGE.RECHARGE_FAILED);
            rechargeRecordPO.setAuditTime(System.currentTimeMillis());
            rechargeRecordPO.setRemark(
                    StringUtils.join("用户",
                            "通过",
                            this.getPaymentInterfaceDTO().getName(),
                            "充值失败 "));
            condition.or().andStatusEqualTo(GlobalConstants.RECHARGE.WATI_CHECK);
            rechargeRecordDao.updateByConditionSelective(rechargeRecordPO, condition);

            //更新交易记录状态
            TradeRecordDTO tradeRecordDTO = tradeRecordDao.selectByPrimaryKey(transId);
            TradeRecordPO tradeRecordPO = new TradeRecordPO();
            BeanUtils.copyProperties(tradeRecordPO, tradeRecordDTO);
            tradeRecordPO.setTradeStatus(GlobalConstants.TRADE_RECORD.TRADE_STATUS_3);
            tradeRecordPO.setUpdateDate(new Date());
            tradeRecordDao.updateByPrimaryKeySelective(tradeRecordPO);

            keyDao.delKey(transId);
        }
    }

    /**
     * 根据交易流水号生成回调记录
     * @param transId 交易流水号
     */
    @Transactional
    public void saveTradeCallBack(String transId){
        TradeCallbackPO tradeCallbackPO = new TradeCallbackPO();
        tradeCallbackPO.setTransId(transId);
        tradeCallbackPO.setCount(GlobalConstants.COMMON.CALLBACK_DEFAULT_COUNT);
        tradeCallbackPO.setCreateDate(new Date());
        tradeCallbackDao.insert(tradeCallbackPO);
    }

    /**
     * 获取第三方充值接口配置信息
     * @return PaymentInterfaceDTO 第三方充值接口配置信息
     */
    public final PaymentInterfaceDTO getPaymentInterfaceDTO(){
        return rechargeApplicationService.createPaymentInterface(key());
    }

}
