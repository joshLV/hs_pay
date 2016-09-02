package com.hszsd.webpay.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 第三方支付接口业务类
 * Created by gzhengDu on 2016/6/28.
 */
public class PaymentInterfaceDTO implements Serializable {

    private static final long serialVersionUID = -4154061899161489666L;

    //接口编号
    private long interfaceId;

    //接口名称
    private String interfaceName;

    //接口标识值
    private String interfaceValue;

    //商户号
    private String merchantId;

    //商户key
    private String merchantKey;

    //充值费率
    private BigDecimal rechargeFee;

    //支付地址
    private String payUrl;

    //页面返回地址
    private String returnUrl;

    //异步通知地址
    private String noticeUrl;

    //编码格式
    private String charset;

    //加密方式MD5 RSA
    private String signType;

    //logo地址
    private String logoUrl;

    //排序 数字越小越靠前
    private int orderBy;

    //是否启用 1:启用 2:不启用
    private int isEnable;

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

    public long getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(long interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getInterfaceValue() {
        return interfaceValue;
    }

    public void setInterfaceValue(String interfaceValue) {
        this.interfaceValue = interfaceValue;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantKey() {
        return merchantKey;
    }

    public void setMerchantKey(String merchantKey) {
        this.merchantKey = merchantKey;
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
        this.payUrl = payUrl;
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

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public int getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }

    public int getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(int isEnable) {
        this.isEnable = isEnable;
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
        return "PaymentInterfaceDTO{" +
                "interfaceId=" + interfaceId +
                ", interfaceName='" + interfaceName + '\'' +
                ", interfaceValue='" + interfaceValue + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", merchantKey='" + merchantKey + '\'' +
                ", rechargeFee=" + rechargeFee +
                ", payUrl='" + payUrl + '\'' +
                ", returnUrl='" + returnUrl + '\'' +
                ", noticeUrl='" + noticeUrl + '\'' +
                ", charset='" + charset + '\'' +
                ", signType='" + signType + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                ", orderBy=" + orderBy +
                ", isEnable=" + isEnable +
                ", createDate=" + createDate +
                ", createBy='" + createBy + '\'' +
                ", updateDate=" + updateDate +
                ", updateBy='" + updateBy + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}