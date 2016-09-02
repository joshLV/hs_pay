package com.hszsd.webpay.service.impl;

import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.common.ResultInfo;
import com.hszsd.webpay.condition.AccountBankCondition;
import com.hszsd.webpay.condition.AccountQuickBankCondition;
import com.hszsd.webpay.config.YeePayConfig;
import com.hszsd.webpay.dao.AccountBankDao;
import com.hszsd.webpay.dao.AccountQuickBankDao;
import com.hszsd.webpay.service.UnbindService;
import com.hszsd.webpay.util.YeePayUtil;
import com.hszsd.webpay.web.dto.AccountQuickBankDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解绑快捷卡业务层接口
 * Created by gzhengDu on 2016/8/15.
 */
@Service("unbindService")
public class UnbindServiceImpl implements UnbindService{
    private final static Logger logger = LoggerFactory.getLogger(UnbindService.class);

    @Autowired
    private AccountQuickBankDao accountQuickBankDao;

    @Autowired
    private AccountBankDao accountBankDao;
    /**
     * 易宝查询绑卡
     * 根据userId调用易宝绑卡查询接口获取帮打列表
     * @param userId 用户标识ID
     * @return
     */
    @Override
    public ResultInfo yeePayQueryBind(String userId) {
        logger.info("yeePayQueryBind is starting and userId={}", userId);
        ResultInfo resultInfo = new ResultInfo();

        Map<String, String> queryResMap = Collections.emptyMap();
        try{
            //调用易宝查询接口查询出用户的绑卡信息
            queryResMap = YeePayUtil.queryAuthbindList(userId, GlobalConstants.ACCOUNT_QUICK_BANK.USERTYPE_2);
            logger.info("yeePayQueryBind queryResMap={}", queryResMap);
            resultInfo.setParams(queryResMap);
            if(StringUtils.isNotEmpty(queryResMap.get("error_code")) || StringUtils.isNotEmpty(queryResMap.get("customError"))){
                resultInfo.setIsSuccess(false);
                resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
                return resultInfo;
            }
        }catch (Exception e){
            logger.error("yeePayQueryBind occurs an error and cause by {}", e.getMessage());
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
            return resultInfo;
        }

        resultInfo.setIsSuccess(true);
        resultInfo.setResult(ResultConstants.OPERATOR_SUCCESS);
        return resultInfo;
    }

    /**
     * 易宝解绑卡
     * 1. 根据userId，bindid调用易宝解绑接口完成解绑操作
     * 2. 解绑完成后删除相应表的数据
     * @param userId 用户标识ID
     * @param bindId 绑卡ID
     * @param cardTop 卡前四位
     * @param cardLast 卡后四位
     * @return
     */
    @Override
    public ResultInfo yeePayUnbind(String userId, String bindId, String cardTop, String cardLast) {
        logger.info("yeePayUnbind is starting and userId={}, bindId={}", userId, bindId);
        ResultInfo resultInfo = new ResultInfo();

        //封装易宝解绑参数
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("merchantaccount", YeePayConfig.MERCHANT_ACCOUNT);
        paramMap.put("identityid", userId);
        paramMap.put("identitytype", String.valueOf(GlobalConstants.ACCOUNT_QUICK_BANK.USERTYPE_2));
        paramMap.put("bindid", bindId);
        Map<String, String> unbindResMap = Collections.emptyMap();
        try {
            //调用易宝的解绑接口完成解绑操作
            unbindResMap = YeePayUtil.unbindBankcard(paramMap);
            logger.info("yeePayUnbind unbindResMap={}", unbindResMap);
            resultInfo.setParams(unbindResMap);
            if(StringUtils.isNotEmpty(unbindResMap.get("error_code")) || StringUtils.isNotEmpty(unbindResMap.get("customError"))){
                resultInfo.setIsSuccess(false);
                resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
                return resultInfo;
            }

            resultInfo.setIsSuccess(true);
            resultInfo.setResult(ResultConstants.OPERATOR_SUCCESS);
        } catch (Exception e) {
            logger.error("yeePayUnbind occurs an error and cause by {}", e.getMessage());
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
            return resultInfo;
        }

        //删除相应表中内容
        if(resultInfo.getIsSuccess()){
            //查询需要解绑的相关信息
            AccountQuickBankCondition condition = new AccountQuickBankCondition();
            condition.or()
                    .andUserIdEqualTo(userId)
                    .andCardTopEqualTo(cardTop)
                    .andCardLastEqualTo(cardLast);
            List<AccountQuickBankDTO> accountQuickBankDTOs = accountQuickBankDao.selectByCondition(condition);

            //封装删除条件
            List<String> delRequestIds = new ArrayList<String>();
            for(AccountQuickBankDTO quickBankDTO: accountQuickBankDTOs){
                delRequestIds.add(quickBankDTO.getRequestId());
            }
            AccountQuickBankCondition delQuickBankConditon = new AccountQuickBankCondition();
            delQuickBankConditon.or()
                    .andRequestIdIn(delRequestIds);
            AccountBankCondition delBankCondition = new AccountBankCondition();
            delBankCondition.or()
                    .andRequestIdIn(delRequestIds);

            accountQuickBankDao.deleteByCondition(delQuickBankConditon);
            accountBankDao.deleteByCondition(delBankCondition);

        }

        return resultInfo;
    }

}
