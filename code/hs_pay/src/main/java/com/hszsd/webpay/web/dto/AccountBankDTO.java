package com.hszsd.webpay.web.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户绑定银行卡信息业务对象
 * Created by gzhengDu on 2016/8/8.
 */
public class AccountBankDTO implements Serializable{

    private static final long serialVersionUID = -6336211761062897728L;

    private String id; //主键标识
    private String userId; //用户编号
    private String account; //银行账号
    private String bank; //银行
    private String requestId; //快捷支付标识
    private Date createDate; //创建时间
    private String createBy; //创建人
    private Date updateDate; //更新时间
    private String updateBy; //更新人
    private String remark; //备注

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
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
        return "AccountBankDTO{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", account='" + account + '\'' +
                ", bank='" + bank + '\'' +
                ", requestId='" + requestId + '\'' +
                ", createDate=" + createDate +
                ", createBy='" + createBy + '\'' +
                ", updateDate=" + updateDate +
                ", updateBy='" + updateBy + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
