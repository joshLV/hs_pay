package com.hszsd.webpay.web.form;

import com.alibaba.dubbo.common.utils.StringUtils;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 封装充值数据表单对象
 * Created by gzhengDu on 2016/7/1.
 */
public class RechargeForm implements Serializable{

    private static final long serialVersionUID = 5244792068790483301L;

    String transId;//交易流水号
    String userId;//用户编号
    String onlineBankId;//充值方式ID
    String money;//充值金额
    String validCaptcha;//验证码
    String[] bankCode = new String[2];//拆分银行ID信息
    String sourceCode; //平台来源
    String payPassword; //交易密码

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOnlineBankId() {
        return onlineBankId;
    }

    public void setOnlineBankId(String onlineBankId) {
        this.onlineBankId = onlineBankId;
        if(StringUtils.isNotEmpty(onlineBankId)){
            if(onlineBankId.indexOf("_") >=0){
                this.bankCode = onlineBankId.split("_");
            }else{
                this.bankCode[0] = onlineBankId;
            }
        }
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getValidCaptcha() {
        return validCaptcha;
    }

    public void setValidCaptcha(String validCaptcha) {
        this.validCaptcha = validCaptcha;
    }

    public String[] getBankCode() {
        return bankCode;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    @Override
    public String toString() {
        return "RechargeForm{" +
                "transId='" + transId + '\'' +
                ", userId='" + userId + '\'' +
                ", onlineBankId='" + onlineBankId + '\'' +
                ", money='" + money + '\'' +
                ", validCaptcha='" + validCaptcha + '\'' +
                ", bankCode=" + Arrays.toString(bankCode) +
                ", sourceCode='" + sourceCode + '\'' +
                ", payPassword='" + payPassword + '\'' +
                '}';
    }
}
