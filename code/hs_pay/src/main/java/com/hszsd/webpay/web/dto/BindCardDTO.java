package com.hszsd.webpay.web.dto;

/**
 * Created by suocy on 2016-8-5.
 */
public class BindCardDTO {
    /**
     * 用户标识
     */
    private String identityId;

    /**
     * 用户标识类型
     */
    private int identityType;

    /**
     * 绑卡请求号
     */
    private String requestId;

    /**
     * 银行卡号
     */
    private String cardNo;

    /**
     * 证件类型
     */
    private String idCardType;

    /**
     * 证件号
     */
    private String idCardNo;

    /**
     * 持卡人姓名
     */
    private String username;

    /**
     * 银行预留手机号
     */
    private String phone;

    /**
     * 用户请求IP
     */
    private String userIp;

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public int getIdentityType() {
        return identityType;
    }

    public void setIdentityType(int identityType) {
        this.identityType = identityType;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getIdCardType() {
        return idCardType;
    }

    public void setIdCardType(String idCardType) {
        this.idCardType = idCardType;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }
}
