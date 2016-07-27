package com.hszsd.webpay.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 第三方支付接口业务类
 * Created by gzhengDu on 2016/6/28.
 */
public class PaymentInterfaceDTO implements Serializable {

    private static final long serialVersionUID = -4154061899161489666L;

    //主键
    private long id;

    //商户号
    private String merchantId;

    //商户key
    private String merchantKey;

    //充值费率
    private BigDecimal rechargeFee;

    //支付地址
    private String payUrl;

    //回调地址
    private String returnUrl;

    //异步通知地址
    private String noticeUrl;

    //是否启用直连 0不启用 1启动
    private int isEnableSingle;

    //编码格式
    private String chartset;

    //接口转入账户
    private String interfaceIntoAccount;

    //接口名称
    private String name;

    //接口代表值
    private String interfaceValue;

    //是否启用非直连 0不启用 1启用
    private int isEnableUnsingle;

    //加密方式MD5 RSA
    private String signType;

    //支付方式
    private String payStyle;

    //卖家Email
    private String sellerEmail;

    //处理模式
    private String transport;

    //商品描述
    private String orderDescription;

    //排序 数字越小越靠前
    private long orderBy;

    //logo地址
    private String logoUrl;

    //状态 0不显示 1显示
    private int state;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId == null ? null : merchantId.trim();
    }

    public String getMerchantKey() {
        return merchantKey;
    }

    public void setMerchantKey(String merchantKey) {
        this.merchantKey = merchantKey == null ? null : merchantKey.trim();
    }

    public BigDecimal getRechargeFee() {
        return rechargeFee;
    }

    public void setRechargeFee(BigDecimal rechargeFee) {
        this.rechargeFee = rechargeFee;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl == null ? null : payUrl.trim();
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl == null ? null : returnUrl.trim();
    }

    public String getNoticeUrl() {
        return noticeUrl;
    }

    public void setNoticeUrl(String noticeUrl) {
        this.noticeUrl = noticeUrl == null ? null : noticeUrl.trim();
    }

    public int getIsEnableSingle() {
        return isEnableSingle;
    }

    public void setIsEnableSingle(int isEnableSingle) {
        this.isEnableSingle = isEnableSingle;
    }

    public String getChartset() {
        return chartset;
    }

    public void setChartset(String chartset) {
        this.chartset = chartset == null ? null : chartset.trim();
    }

    public String getInterfaceIntoAccount() {
        return interfaceIntoAccount;
    }

    public void setInterfaceIntoAccount(String interfaceIntoAccount) {
        this.interfaceIntoAccount = interfaceIntoAccount == null ? null : interfaceIntoAccount.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getInterfaceValue() {
        return interfaceValue;
    }

    public void setInterfaceValue(String interfaceValue) {
        this.interfaceValue = interfaceValue == null ? null : interfaceValue.trim();
    }

    public int getIsEnableUnsingle() {
        return isEnableUnsingle;
    }

    public void setIsEnableUnsingle(int isEnableUnsingle) {
        this.isEnableUnsingle = isEnableUnsingle;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType == null ? null : signType.trim();
    }

    public String getPayStyle() {
        return payStyle;
    }

    public void setPayStyle(String payStyle) {
        this.payStyle = payStyle == null ? null : payStyle.trim();
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail == null ? null : sellerEmail.trim();
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport == null ? null : transport.trim();
    }

    public String getOrderDescription() {
        return orderDescription;
    }

    public void setOrderDescription(String orderDescription) {
        this.orderDescription = orderDescription == null ? null : orderDescription.trim();
    }

    public long getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(long orderBy) {
        this.orderBy = orderBy;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl == null ? null : logoUrl.trim();
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "PaymentInterfaceDTO{" +
                "id=" + id +
                ", merchantId='" + merchantId + '\'' +
                ", merchantKey='" + merchantKey + '\'' +
                ", rechargeFee=" + rechargeFee +
                ", payUrl='" + payUrl + '\'' +
                ", returnUrl='" + returnUrl + '\'' +
                ", noticeUrl='" + noticeUrl + '\'' +
                ", isEnableSingle=" + isEnableSingle +
                ", chartset='" + chartset + '\'' +
                ", interfaceIntoAccount='" + interfaceIntoAccount + '\'' +
                ", name='" + name + '\'' +
                ", interfaceValue='" + interfaceValue + '\'' +
                ", isEnableUnsingle=" + isEnableUnsingle +
                ", signType='" + signType + '\'' +
                ", payStyle='" + payStyle + '\'' +
                ", sellerEmail='" + sellerEmail + '\'' +
                ", transport='" + transport + '\'' +
                ", orderDescription='" + orderDescription + '\'' +
                ", orderBy=" + orderBy +
                ", logoUrl='" + logoUrl + '\'' +
                ", state=" + state +
                '}';
    }
}