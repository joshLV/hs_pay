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

    //操作类型(1:充值 2:余额支付 3:余额退款 4:积分支付 5:积分退款 6:组合支付-积分与余额 7:组合退款-积分与余额)
    private int operateType;

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

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
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

    public String getMD5Sign() {
        return MD5Sign;
    }

    public void setMD5Sign(String MD5Sign) {
        this.MD5Sign = MD5Sign;
    }


}
