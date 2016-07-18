package com.hszsd.webpay.service;

import com.hszsd.user.service.UserService;
import com.hszsd.webpay.dao.RechargeRecordDao;
import com.hszsd.webpay.web.dto.PayInDTO;
import com.hszsd.webpay.web.dto.PayOutDTO;
import com.hszsd.webpay.web.dto.PaymentInterfaceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 抽象支付服务业务层
 * Created by gzhengDu on 2016/7/4.
 */
public abstract class PayService {
    private static final Logger logger = LoggerFactory.getLogger(PayService.class);

    @Autowired
    public TransIdService transIdService;

    @Autowired
    public RechargeRecordDao rechargeRecordDao;

    @Autowired
    public PayApplicationService payApplicationService;

    //@Autowired
    //public AccountService accountService;

    //@Autowired
    //public AccountLogDao accountLogDao;

    @Autowired
    public UserService userService;

    //@Autowired
    //private LineCouponService couponService;

    //private KeyDao keyDao = RedisBaseDao.getKeyDao();
    //private SetDao setDao = RedisBaseDao.getSetDao();
    /**
     * 返回当前支付方式标识
     * @return String 返回支付方式标识
     */
    public abstract String key();

    /**
     * 获取交易编码
     * @param strings 交易编码构成字符串
     * @return String 交易编码
     */
    public abstract String getId(String...strings);/*{
        StringBuffer sb = new StringBuffer();
        if(StringUtils.isNotEmpty(prefix)){
            sb.append(prefix);
        }
        //获取时间戳
        sb.append(DateUtil.dateToString(new Date(), "yyyyMMdd"));
        sb.append(transIdService.initTransNo());
        return sb.toString();
    }*/

    /**
     * 支付
     * @param payInDTO 支付入参
     * @return PayOutDTO 支付返回结果
     */
    public abstract PayOutDTO pay(PayInDTO payInDTO);

    /**
     * 前台回调
     * @param payInDTO
     * @return
     */
    public abstract PayOutDTO front(PayInDTO payInDTO);

    /**
     * 后台回调
     * @param payInDTO
     * @return
     */
    public abstract PayOutDTO back(PayInDTO payInDTO);

    /**
     * 查询接口
     * @param payInDTO
     * @return
     */
    public abstract PayOutDTO query(PayInDTO payInDTO);


    /**
     * 根据不同支付方式初始化相应支付接口数据
     * @param paymentInterfaceDTO 支付接口配置信息
     * @param payInDTO 支付入参
     * @return Map 支付接口数据集合
     */
    public abstract Map<String, String> initSignData(PaymentInterfaceDTO paymentInterfaceDTO, PayInDTO payInDTO);

    /**
     * 将源字符串转为MD5校验码
     * @param str 源字符串
     * @return MD5校验码
     */
    public abstract String MD5Info(String str);

    /**
     * 日志写入
     */
    public final void saveLog(){}

    /**
     * 保存充值记录
     * @param rechargeRecord
     */
    /*public final void saveRechargeRecord(RechargeRecord rechargeRecord){
        rechargeRecordDao.add(rechargeRecord);
    }*/

    //private KeyDao keyDao = RedisBaseDao.getKeyDao();

    //private SetDao setDao = RedisBaseDao.getSetDao();
    /**
     * 支付成功后 保存相关记录
     * @param orderId 订单ID
     * @param amount  交易金额 以分为单位
     *
     */
    @Transactional
    public synchronized void saveBack(String orderId, String amount){
        /*if(!keyDao.isExits(orderId)){
            setDao.save(orderId,orderId);
            //获取充值记录
            RechargeRecord rechargeRecord = rechargeRecordDao.getOne(orderId);
            //获取用户信息
            Result userResult = userService.getIdUser(rechargeRecord.getUserId());
            if(userResult.getCode())
            User user = null;
            //充值记录是否存在 并且充值记录未审核
            if(rechargeRecord==null || Constants.WATI_CHECK!=rechargeRecord.getStatus()){
                logger.info("updatePayMent by mobile  fail 充值记录不存在 或者 交易记录已审核通过， 系统返回 ! orderId =" + orderId + ", amount =" + amount);
            } else{
                //更新充值记录
                rechargeRecord.setMoney(MathUtils.divide(amount, 100)); //将分转换为元
                rechargeRecord.setStatus(Constants.ALLOW_CHECKED);
                rechargeRecord.setFromStatus(Constants.WATI_CHECK);
                rechargeRecord.setAuditTime(System.currentTimeMillis());
                rechargeRecord.setRemark("用户" +user.getUsername()+ "通过"+getPayMent().getName()+"充值成功 充值金额 "+  MathUtils.divide(amount, 100));
                if(rechargeRecordDao.update("", rechargeRecord) == 1){
                    logger.info("updatePayMent by mobile 充值状态更新成功  orderId =" + orderId + ", amount =" + amount+",user="+user.getUsername());
                    Account accountup  = new Account();
                    accountup.setUseMoney(rechargeRecord.getMoney());
                    accountup.setTotal(rechargeRecord.getMoney());
                    accountup.setUserId(rechargeRecord.getUserId());
                    accountDao.updateByMap(accountup);
                    Account account = accountDao.getAccountByUserId(rechargeRecord.getUserId());
                    //增加资金记录
                    AccountLog accountLog = new AccountLog();
                    accountLog.setUserId(rechargeRecord.getUserId());
                    accountLog.setUsername(user.getUsername());
                    accountLog.setType(Constants.RECHARGE);
                    accountLog.setTotal(account.getTotal());
                    accountLog.setMoney(rechargeRecord.getMoney());
                    accountLog.setUseMoney(account.getUseMoney());
                    accountLog.setNoUseMoney(account.getNoUseMoney());
                    accountLog.setCollection(account.getCollection());
                    accountLog.setAddTime(System.currentTimeMillis());
                    accountLog.setRemark("用户："+user.getUsername()+"通过"+getPayMent().getName()+"在线充值成功，充值金额："+rechargeRecord.getMoney());
                    logger.info("updatePayMent by mobile 用户资金记录添加   AccountLog =" +accountLog+",user="+user.getUsername());
                    accountLogDao.add(accountLog);
                    //获取兑换券
                    couponService.addCoupon(Constants.COUPON_PAY_CODE, rechargeRecord.getMoney().doubleValue(), rechargeRecord.getUserId());
                }
            }
            keyDao.delKey(orderId);
        }*/
    }

    /**
     * 第三方支付返回充值失败信息时更新充值记录状态
     * @param orderId 订单号
     */
    public synchronized void saveBackForFail(String orderId){
        /*if(!keyDao.isExits(orderId)){
            setDao.save(orderId,orderId);
            //获取充值记录
            RechargeRecordCondition rechargeRecordCondition = new RechargeRecordCondition();
            rechargeRecordCondition.or().andTradeNoEqualTo(orderId);
            List<RechargeRecordDTO> rechargeRecordDTOs = rechargeRecordDao.selectByCondition(rechargeRecordCondition);

            //充值记录是否存在 并且充值记录未审核
            if(rechargeRecordDTOs.size() <= 0 ||
                    GlobalConstants.WATI_CHECK != rechargeRecordDTOs.get(0).getStatus()){
                logger.info("updatePayMent by mobile  fail 充值记录不存在 或者 交易记录已审核通过， 系统返回 ! orderId =" + orderId + ", amount =" + amount);
            } else{
                //更新充值记录
                RechargeRecordPO rechargeRecordPO = new RechargeRecordPO();
                rechargeRecordPO.setStatus(GlobalConstants.RECHARGE_FAILED);
                rechargeRecordPO.setAuditTime(System.currentTimeMillis());
                rechargeRecordPO.setRemark("充值失败！");
                rechargeRecordDao.updateByConditionSelective(rechargeRecordPO, rechargeRecordCondition);
            }
            keyDao.delKey(orderId);
        }*/
    }

    /**
     * 获取第三方支付接口配置信息
     * @return PaymentInterfaceDTO 第三方支付接口配置信息
     */
    public final PaymentInterfaceDTO getPaymentInterfaceDTO(){
        return payApplicationService.createPaymentInterface(key());
    }
}
