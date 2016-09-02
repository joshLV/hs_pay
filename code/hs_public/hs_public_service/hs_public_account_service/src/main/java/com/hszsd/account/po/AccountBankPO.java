package com.hszsd.account.po;

/**
 * 用户银行账户 */
public class AccountBankPO {

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

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getAccount() {
        return account;
    }

    public String getBrank() {
        return brank;
    }

    public String getBranch() {
        return branch;
    }

    public String getProvince() {
        return province;
    }

    public String getArea() {
        return area;
    }

    public String getQu() {
        return qu;
    }

    public Long getAddtime() {
        return addtime;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setBrank(String brank) {
        this.brank = brank;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setQu(String qu) {
        this.qu = qu;
    }

    public void setAddtime(Long addtime) {
        this.addtime = addtime;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "AccountBankPO{" +
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