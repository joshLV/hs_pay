package com.hszsd.webpay.web.form;

import com.alibaba.dubbo.common.utils.StringUtils;

/**
 * 封装充值数据表单对象
 * Created by gzhengDu on 2016/7/1.
 */
public class RechargeForm {

    String onlineBankId;//支付方式ID
    String amount;//支付金额
    String validCaptcha;//验证码
    String[] bankCode = new String[2];//拆分银行ID信息

    public String getOnlineBankId() {
        return onlineBankId;
    }

    public void setOnlineBankId(String onlineBankId) {
        this.onlineBankId = onlineBankId;
        if(StringUtils.isNotEmpty(onlineBankId)){
            if(onlineBankId.indexOf("_") >=0){
                this.bankCode = onlineBankId.split("_");
            }
            this.bankCode[0] = onlineBankId;
        }
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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
}
