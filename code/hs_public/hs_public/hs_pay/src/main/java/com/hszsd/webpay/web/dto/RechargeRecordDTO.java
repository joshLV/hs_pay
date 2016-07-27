package com.hszsd.webpay.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 充值记录业务类
 * Created by gzhengDu on 2016/7/5.
 */
public class RechargeRecordDTO implements Serializable {
    private static final long serialVersionUID = 132912760922259903L;

    //主键编号
    private long id;

    //充值交易流水号
    private String tradeNo;

    //用户编号
    private String userId;

    //充值状态
    private int status;

    //交易金额
    private BigDecimal money;

    //在线充值接口
    private String payment;

    //接口返回值
    private String retvalue;

    //充值类型
    private int type;

    //备注
    private String remark;

    //到账时间
    private long addtime;

    //审核时间
    private long auditTime;

    //审核人ID
    private long auditUserid;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getRetvalue() {
        return retvalue;
    }

    public void setRetvalue(String retvalue) {
        this.retvalue = retvalue;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getAddtime() {
        return addtime;
    }

    public void setAddtime(long addtime) {
        this.addtime = addtime;
    }

    public long getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(long auditTime) {
        this.auditTime = auditTime;
    }

    public long getAuditUserid() {
        return auditUserid;
    }

    public void setAuditUserid(long auditUserid) {
        this.auditUserid = auditUserid;
    }

    @Override
    public String toString() {
        return "RechargeRecordDTO{" +
                "id=" + id +
                ", tradeNo='" + tradeNo + '\'' +
                ", userId='" + userId + '\'' +
                ", status=" + status +
                ", money=" + money +
                ", payment='" + payment + '\'' +
                ", retvalue='" + retvalue + '\'' +
                ", type=" + type +
                ", remark='" + remark + '\'' +
                ", addtime=" + addtime +
                ", auditTime=" + auditTime +
                ", auditUserid=" + auditUserid +
                '}';
    }
}
