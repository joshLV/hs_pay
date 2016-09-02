package com.hszsd.webpay.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易记录业务类
 * Created by gzhengDu on 2016/7/16.
 */
public class TradeRecordDTO implements Serializable {

    private static final long serialVersionUID = 5173388003400866166L;

    //交易记录流水号
    private String transId;

    //用户编号
    private String userId;

    //转入账号
    private String toUserId;

    //操作金额
    private BigDecimal money;

    //操作积分
    private BigDecimal credit;

    //手机号
    private String mobile;

    //来源方订单号
    private String orderId;

    //商品名称
    private String productName;

    //平台来源（SH:商城 P2P:p2p平台 H5:微信）
    private String sourceCode;

    //操作类型(1 充值 11网银在线充值 12 汇潮充值 13 宝付充值 14 易宝充值
    //          2 支付  21余额支付 22招商币支付  23组合支付（余额和招商币支付） 24快捷支付
    //          3 退回  31增加余额  32增加招商币 33增加余额增加招商币)
    private int operateType;

    //订单类型(1正宗商城 2中天物业 3中天商城)
    private int orderType;

    //交易状态（1:等待处理 2:交易成功 3:交易失败）
    private int tradeStatus;

    //前台回调地址
    private String returnUrl;

    //后台回调地址
    private String noticeUrl;

    //创建时间
    private Date createDate;

    //创建人
    private String createBy;

    //更新时间
    private Date updateDate;

    //更新人
    private String updateBy;

    //备注
    private String remark;

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

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(int tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getNoticeUrl() {
        return noticeUrl;
    }

    public void setNoticeUrl(String noticeUrl) {
        this.noticeUrl = noticeUrl;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "TradeRecordDTO{" +
                "transId='" + transId + '\'' +
                ", userId='" + userId + '\'' +
                ", toUserId='" + toUserId + '\'' +
                ", money=" + money +
                ", credit=" + credit +
                ", mobile='" + mobile + '\'' +
                ", orderId='" + orderId + '\'' +
                ", productName='" + productName + '\'' +
                ", sourceCode='" + sourceCode + '\'' +
                ", operateType=" + operateType +
                ", orderType=" + orderType +
                ", tradeStatus=" + tradeStatus +
                ", returnUrl='" + returnUrl + '\'' +
                ", noticeUrl='" + noticeUrl + '\'' +
                ", createDate=" + createDate +
                ", createBy='" + createBy + '\'' +
                ", updateDate=" + updateDate +
                ", updateBy='" + updateBy + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
