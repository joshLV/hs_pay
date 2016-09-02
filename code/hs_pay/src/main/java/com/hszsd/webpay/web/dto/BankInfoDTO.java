package com.hszsd.webpay.web.dto;

import java.io.Serializable;

/**
 * 支付平台银行信息业务类
 * Created by gzhengDu on 2016/8/8.
 */
public class BankInfoDTO implements Serializable {
    private static final long serialVersionUID = -1441463350133726558L;

    private long bankId; //银行编号
    private String bankName; //银行名称
    private String bankValue; //银行标识
    private String bankLogoPc; //Pc端logo地址
    private String bankLogoMob; //移动端logo地址
    private int quickFlag; //是否支持快捷支付标识 1:支持 2:不支持
    private int isEnablePc; //Pc端是否启用 1:启用 2:不启用
    private int isEnableMob; //移动端是否启用 1:启用 2:不启用

    public long getBankId() {
        return bankId;
    }

    public void setBankId(long bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankValue() {
        return bankValue;
    }

    public void setBankValue(String bankValue) {
        this.bankValue = bankValue;
    }

    public String getBankLogoPc() {
        return bankLogoPc;
    }

    public void setBankLogoPc(String bankLogoPc) {
        this.bankLogoPc = bankLogoPc;
    }

    public String getBankLogoMob() {
        return bankLogoMob;
    }

    public void setBankLogoMob(String bankLogoMob) {
        this.bankLogoMob = bankLogoMob;
    }

    public int getQuickFlag() {
        return quickFlag;
    }

    public void setQuickFlag(int quickFlag) {
        this.quickFlag = quickFlag;
    }

    public int getIsEnablePc() {
        return isEnablePc;
    }

    public void setIsEnablePc(int isEnablePc) {
        this.isEnablePc = isEnablePc;
    }

    public int getIsEnableMob() {
        return isEnableMob;
    }

    public void setIsEnableMob(int isEnableMob) {
        this.isEnableMob = isEnableMob;
    }

    @Override
    public String toString() {
        return "BankInfoDTO{" +
                "bankId=" + bankId +
                ", bankName='" + bankName + '\'' +
                ", bankValue='" + bankValue + '\'' +
                ", bankLogoPc='" + bankLogoPc + '\'' +
                ", bankLogoMob='" + bankLogoMob + '\'' +
                ", quickFlag=" + quickFlag +
                ", isEnablePc=" + isEnablePc +
                ", isEnableMob=" + isEnableMob +
                '}';
    }
}
