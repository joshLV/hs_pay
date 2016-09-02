package com.hszsd.webpay.web.form;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 封装交易信息表单
 * Created by gzhengDu on 2016/7/16.
 */
public class TradeForm implements Serializable{

    private static final long serialVersionUID = 8911686542071675724L;
    //交易记录流水号
    private String transId;

    //用户编号
    private String userId;

    //操作金额
    private BigDecimal money;

    //操作积分
    private BigDecimal credit;

    //手机号
    private String mobile;

    //来源方订单号
    private String orderId;

    //平台来源（SH:商城 P2P:p2p平台 H5:微信）
    private String sourceCode;

    //前台回调地址
    private String returnUrl;

    //后台回调地址
    private String noticeUrl;

    //备注
    private String remark;

    //MD5签名信息
    private String MD5Sign;

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

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMD5Sign() {
        return MD5Sign;
    }

    public void setMD5Sign(String MD5Sign) {
        this.MD5Sign = MD5Sign;
    }

    @Override
    public String toString() {
        return "TradeForm{" +
                "transId='" + transId + '\'' +
                ", userId='" + userId + '\'' +
                ", money=" + money +
                ", credit=" + credit +
                ", mobile='" + mobile + '\'' +
                ", orderId='" + orderId + '\'' +
                ", sourceCode='" + sourceCode + '\'' +
                ", returnUrl='" + returnUrl + '\'' +
                ", noticeUrl='" + noticeUrl + '\'' +
                ", remark='" + remark + '\'' +
                ", MD5Sign='" + MD5Sign + '\'' +
                '}';
    }
}
