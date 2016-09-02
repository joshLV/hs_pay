package com.hszsd.account.dto;


import java.io.Serializable;

/**
 * 用户快捷支付银行卡关系表
 * @author YangWenJian
 * @version V1.0.0
 *
 **/
public class AccountQuickBankDTO implements Serializable{

    private static final long serialVersionUID = 191266212993750575L;
    private  String id;

    //用户标识
    private String userId;

    //用户类型 0：IMEI 1：MAC 地址 2：用户ID（默认）3：用户Email 4：用户手机号 5：用户身份证号 6：用户纸质订单协议号
    private Integer userType;


    //绑卡请求标识
    private String requestId;


    //用户IP
    private String userIp;

    //卡号前6位
    private String cardTop;

    //卡号后4位
    private String cardLast;

    //银行编号
    private String bankCode;

    //绑定状态 1、绑定成功  -1、解绑
    private Integer bindStatus;

    //认证状态 1、认证成功 -1、认证失败 0、认证中
    private Integer validStatus;

    //创建时间
    private Long createTime;

    //创建人
    private String createBy;

    //修改时间
    private Long modifyTime;

    //修改人
    private String modifyBy;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserType() {
        return this.userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUserIp() {
        return this.userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getCardTop() {
        return this.cardTop;
    }

    public void setCardTop(String cardTop) {
        this.cardTop = cardTop;
    }

    public String getCardLast() {
        return this.cardLast;
    }

    public void setCardLast(String cardLast) {
        this.cardLast = cardLast;
    }

    public String getBankCode() {
        return this.bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public Integer getBindStatus() {
        return this.bindStatus;
    }

    public void setBindStatus(Integer bindStatus) {
        this.bindStatus = bindStatus;
    }

    public Integer getValidStatus() {
        return this.validStatus;
    }

    public void setValidStatus(Integer validStatus) {
        this.validStatus = validStatus;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Long getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyBy() {
        return this.modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }


    @Override
    public String toString() {
        return "AccountQuickBankDTO{" +
                "bankCode='" + bankCode + '\'' +
                ", id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", userType=" + userType +
                ", requestId='" + requestId + '\'' +
                ", userIp='" + userIp + '\'' +
                ", cardTop='" + cardTop + '\'' +
                ", cardLast='" + cardLast + '\'' +
                ", bindStatus=" + bindStatus +
                ", validStatus=" + validStatus +
                ", createTime=" + createTime +
                ", createBy='" + createBy + '\'' +
                ", modifyTime=" + modifyTime +
                ", modifyBy='" + modifyBy +
                '}';
    }
}