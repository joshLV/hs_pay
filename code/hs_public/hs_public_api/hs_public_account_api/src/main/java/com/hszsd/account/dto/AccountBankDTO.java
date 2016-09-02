package com.hszsd.account.dto;

import java.io.Serializable;

/**
 * 用户银行账户 */
public class AccountBankDTO implements Serializable{

    private static final long serialVersionUID = 8017940250552301370L;
    //主键编号
    private Long id;

    //用户编号
    private String userId;

    //银行帐号
    private String account;

    //银行名称
    private String brank;

    //开户行
    private String branch;

    //省
    private String province;

    //市
    private String area;

    //区
    private String qu;

    //添加时间
    private Long addtime;

    //快捷支付银行卡绑定标识
    private String requestId;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Long getAddtime() {
        return addtime;
    }

    public void setAddtime(Long addtime) {
        this.addtime = addtime;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getBrank() {
        return brank;
    }

    public void setBrank(String brank) {
        this.brank = brank;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getQu() {
        return qu;
    }

    public void setQu(String qu) {
        this.qu = qu;
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

    @Override
    public String toString() {
        return "AccountBankDTO{" +
                "account='" + account + '\'' +
                ", id=" + id +
                ", userId='" + userId + '\'' +
                ", brank='" + brank + '\'' +
                ", branch='" + branch + '\'' +
                ", province='" + province + '\'' +
                ", area='" + area + '\'' +
                ", qu='" + qu + '\'' +
                ", addtime=" + addtime +
                ", requestId='" + requestId + '\'' +
                '}';
    }
}