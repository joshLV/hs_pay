package com.hszsd.account.dto;

import java.io.Serializable;

/**
 *更新用户绑卡信息实体类
 * @author YangWenJian
 * @version V1.0.0
 */
public class AccountBankCommDTO implements Serializable {
    private static final long serialVersionUID = -7833735341645207320L;
    //用户编号
    private String userId;

    //银行帐号
    private String account;

    //开户行
    private String branch;

    //银行代码
    private String bankCode;

    //绑卡请求标识
    private String requestId;

    //用户IP
    private String userIp;

    //用户类型 0：IMEI 1：MAC 地址 2：用户ID（默认）3：用户Email 4：用户手机号 5：用户身份证号 6：用户纸质订单协议号
    private UserType userType;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }


    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "AccountBankCommDTO{" +
                "account='" + account + '\'' +
                ", userId='" + userId + '\'' +
                ", branch='" + branch + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", requestId='" + requestId + '\'' +
                ", userIp='" + userIp + '\'' +
                ", userType=" + userType +
                '}';
    }
}
