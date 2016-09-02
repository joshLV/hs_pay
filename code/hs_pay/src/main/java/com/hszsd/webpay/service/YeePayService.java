package com.hszsd.webpay.service;

import com.hszsd.user.dto.GetUserInfoDTO;
import com.hszsd.webpay.common.ResultInfo;
import com.hszsd.webpay.web.form.BindCardConfirmForm;
import com.hszsd.webpay.web.form.BindCardForm;
import com.hszsd.webpay.web.form.BindCardPayForm;

/**
 * 易宝快捷支付业务层接口
 * Created by suocy on 2016/7/15.
 */
public interface YeePayService {

    /**
     * 绑卡请求
     * @param bindCardForm 绑卡表单对象
     * @param userInfoDTO 用户信息对象
     * @return
     */
    ResultInfo bindCard(BindCardForm bindCardForm,GetUserInfoDTO userInfoDTO);

    /**
     * 确认绑卡请求
     * @param bindCardConfirmForm 确定绑卡表单对象
     * @param userInfoDTO 用户信息对象
     * @return
     */
    ResultInfo bindCardConfirm(BindCardConfirmForm bindCardConfirmForm, GetUserInfoDTO userInfoDTO);


    /**
     * 查询用户绑定的快捷支付卡
     * @param userId
     * @return
     */
    ResultInfo queryQuickBank(String userId);

    /**
     * 快捷卡支付--不需要短信验证
     * @param bindCardPayForm
     * @param backUrl  支付回调地址
     * @return
     */
    ResultInfo quickCardPay(BindCardPayForm bindCardPayForm, String backUrl);

    /**
     * 快捷支付回调
     * @param data
     * @param encryptKey
     * @return 处理是否成功
     */
    boolean callBack(String data, String encryptKey);


}
