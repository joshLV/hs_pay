package com.hszsd.webpay.web.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户快捷支付银行卡关系业务类
 * Created by gzhengDu on 2016/8/8.
 */
public class AccountQuickBankDTO implements Serializable {
    private static final long serialVersionUID = 7591618703941986887L;

    private String id; //银行卡标识
    private int userType; //用户类型
    private String userId; //用户编号
    private String requestId; //绑卡请求标识
    private String userIp; //用户IP
    private String cardTop; //卡号前六位
    private String cardLast; //卡号后四位
    private String bankCode; //银行编号
    private int bindStatus; //绑定标识：1绑定成功 2解绑
    private int validStatus; //认证状态：1认证成功 2认证失败 3认证中
    private Date createDate; //创建时间
    private String createBy; //创建人
    private Date updateDate; //修改时间
    private String updateBy; //修改人

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getCardTop() {
        return cardTop;
    }

    public void setCardTop(String cardTop) {
        this.cardTop = cardTop;
    }

    public String getCardLast() {
        return cardLast;
    }

    public void setCardLast(String cardLast) {
        this.cardLast = cardLast;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public int getBindStatus() {
        return bindStatus;
    }

    public void setBindStatus(int bindStatus) {
        this.bindStatus = bindStatus;
    }

    public int getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(int validStatus) {
        this.validStatus = validStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Override
    public String toString() {
        return "AccountQuickBankDTO{" +
                "id='" + id + '\'' +
                ", userType=" + userType +
                ", userId='" + userId + '\'' +
                ", requestId='" + requestId + '\'' +
                ", userIp='" + userIp + '\'' +
                ", cardTop='" + cardTop + '\'' +
                ", cardLast='" + cardLast + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", bindStatus=" + bindStatus +
                ", validStatus=" + validStatus +
                ", createDate=" + createDate +
                ", createBy='" + createBy + '\'' +
                ", updateDate=" + updateDate +
                ", updateBy='" + updateBy + '\'' +
                '}';
    }
}
