package com.hszsd.webpay.web.dto;

import java.io.Serializable;

/**
 * 线上银行信息业务类
 * Created by gzhengDu on 2016/6/28.
 */
public class OnlineBankDTO implements Serializable {
    private static final long serialVersionUID = -8679567352426433920L;

    //主键
    private long id;

    //银行名称
    private String bankName;

    //银行logo
    private String bankLogo;

    //银行编码
    private String bankCode;

    //银行名称
    private String bankValue;

    //第三方支付接口
    private String paymentInterfaceId;

    //是否启用
    private int isEnable;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankLogo() {
        return bankLogo;
    }

    public void setBankLogo(String bankLogo) {
        this.bankLogo = bankLogo;
    }

    public String getBankValue() {
        return bankValue;
    }

    public void setBankValue(String bankValue) {
        this.bankValue = bankValue;
    }

    public String getPaymentInterfaceId() {
        return paymentInterfaceId;
    }

    public void setPaymentInterfaceId(String paymentInterfaceId) {
        this.paymentInterfaceId = paymentInterfaceId;
    }

    public int getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(int isEnable) {
        this.isEnable = isEnable;
    }

    @Override
    public String toString() {
        return "OnlineBankDTO{" +
                "id=" + id +
                ", bankName='" + bankName + '\'' +
                ", bankLogo='" + bankLogo + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", bankValue='" + bankValue + '\'' +
                ", paymentInterfaceId='" + paymentInterfaceId + '\'' +
                ", isEnable=" + isEnable +
                '}';
    }
}
