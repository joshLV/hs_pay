package com.hszsd.webpay.service;

import com.hszsd.webpay.common.ResultInfo;

/**
 * 密码校验service
 * Created by gzhengDu on 2016/8/11.
 */
public interface PasswordCheckService {

    /**
     * 验证支付密码
     * @param userId
     * @param password
     * @return
     */
    ResultInfo checkPayPassWord(String userId, String password);

    /**
     *
     * 当支付密码验证错误时，将错误次数存在redis中
     * 键：“hspay:payPassWord:”+ userId； 值：错误次数
     * 返回页面提示语
     *
     */
    String getErrorMsg(String userId);
}
