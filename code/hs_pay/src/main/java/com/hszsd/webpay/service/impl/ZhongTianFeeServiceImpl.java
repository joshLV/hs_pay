package com.hszsd.webpay.service.impl;

import com.hszsd.core.service.CoreService;
import com.hszsd.entity.AccountLog;
import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.service.ZhongTianFeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 中天缴费业务层接口实现
 * Created by suocy on 2016/8/25.
 */
@Service("zhongTianFeeService")
@Transactional(rollbackFor = {Exception.class,RuntimeException.class})
public class ZhongTianFeeServiceImpl implements ZhongTianFeeService {
    private static final Logger logger = LoggerFactory.getLogger(ZhongTianFeeServiceImpl.class);

    @Autowired
    private CoreService coreService;

    /**
     * 根据ERP通知缴费结果，调功能服务器接口
     * @param params
     * @param flag ture：成功，false：失败
     * @return
     */
    @Override
    public boolean ztwyFeeNotice(Map<String, String> params, boolean flag) {
        String transId = params.get("transId");
        String userId = params.get("userId");
        String toUserId = params.get("toUserId");
        String remark = params.get("remark");
        BigDecimal money = new BigDecimal(params.get("money")).setScale(2, BigDecimal.ROUND_HALF_UP);
        boolean optResult;
        try{
            if(flag){
                AccountLog fromAccountLog = initAccountLog(userId, money, remark + "--交易成功，冻结转账", GlobalConstants.ACCOUNT_LOG.TYPE_28, toUserId);
                AccountLog toAccountLog = initAccountLog(toUserId, money, remark + "--缴费转入", GlobalConstants.ACCOUNT_LOG.TYPE_29, "");
                optResult = coreService.transeferNoUseToUse(fromAccountLog, toAccountLog, transId);
            }else{
                AccountLog accountLog = initAccountLog(userId, money, remark + "--交易失败，冻结转可用", GlobalConstants.ACCOUNT_LOG.TYPE_27, "");
                optResult = coreService.moveNoUseToUse(accountLog, transId);
            }
        }catch (Exception e){
            logger.error("coreService interface is occurs an Exception :{}",e.getMessage());
            optResult = false;
        }
        return optResult;
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

}
