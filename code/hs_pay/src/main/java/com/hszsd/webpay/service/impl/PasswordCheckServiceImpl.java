package com.hszsd.webpay.service.impl;

import com.hszsd.common.util.Result;
import com.hszsd.common.util.ResultCode;
import com.hszsd.user.service.UserService;
import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.common.ResultInfo;
import com.hszsd.webpay.service.PasswordCheckService;
import com.jiangshikun.parrot.dao.RedisBaseDao;
import com.jiangshikun.parrot.dao.StringDao;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

/**
 * 密码校验逻辑实现
 * Created by gzhengDu on 2016/8/11.
 */
@Service("passwordCheckService")
public class PasswordCheckServiceImpl implements PasswordCheckService {
    private static final Logger logger = LoggerFactory.getLogger(PasswordCheckService.class);

    @Autowired
    private UserService userService;

    private StringDao stringDao = RedisBaseDao.getStringDao();
    //一天总秒数
    private static final int time = 24*60*60;

    /**
     * 验证支付密码
     * @param userId
     * @param password
     * @return
     */
    @Override
    public ResultInfo checkPayPassWord(String userId, String password){
        logger.info("checkPayPassWord is starting and userId={}", userId);
        ResultInfo resultInfo = new ResultInfo();
        String value = stringDao.getValue(GlobalConstants.COMMON.REDIS_PAYPASSWORD_PREFIX + userId);
        //首先查看支付密码输入错误次数，大于或等于5次，直接返回“账户锁定”；小于5次才去验证支付密码
        if(StringUtils.isNotEmpty(value)&&Integer.parseInt(value)>=5){
            String errorMsg = getErrorMsg(userId);
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(errorMsg);
            return resultInfo;
        }
        Result existsUserPayPassword = userService.isExistsUserPayPassword(userId, password);
        //用户未设置交易密码
        if(ResultCode.USER_PLYPASSWORD_NULL.equals(existsUserPayPassword.getResCode())){
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.PAYPASSWORD_NOSET.getMsg());
            return resultInfo;
        }
        //支付密码不正确
        if (!ResultCode.RES_OK.equals(existsUserPayPassword.getResCode())){
            String errorMsg = getErrorMsg(userId);
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(errorMsg);
            return resultInfo;
        }
        stringDao.remove(GlobalConstants.COMMON.REDIS_PAYPASSWORD_PREFIX  + userId);
        resultInfo.setIsSuccess(true);
        return resultInfo;
    }

    /**
     *
     * 当支付密码验证错误时，将错误次数存在redis中
     * 键：“hspay:payPassWord”+ userId； 值：错误次数
     * 返回页面提示语
     *
     */
    @Override
    public String getErrorMsg(String userId){
        String value = stringDao.getValue(GlobalConstants.COMMON.REDIS_PAYPASSWORD_PREFIX  + userId);
        int count = StringUtils.isEmpty(value)?1:Integer.valueOf(value)+1;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        //设置失效时间，当前时间截止到凌晨0点的总秒数
        int expire = time-calendar.get(Calendar.HOUR_OF_DAY)*3600-calendar.get(calendar.MINUTE)*60-calendar.get(calendar.SECOND);
        stringDao.save(GlobalConstants.COMMON.REDIS_PAYPASSWORD_PREFIX  + userId, String.valueOf(count), expire);
        String errorMsg = "";
        if(StringUtils.isEmpty(value)){
            errorMsg = "密码输入错误！您今天还有" + String.valueOf(5-count)+"次输入机会";
            return errorMsg;
        }
        if(Integer.parseInt(value)>= 4){
            errorMsg = "密码输入错误5次，账户已被锁定，请24小时以后再试！";
            return errorMsg;
        }
        errorMsg = "密码输入错误！您今天还有" + String.valueOf(5-count)+"次输入机会";
        return errorMsg;
    }
}
