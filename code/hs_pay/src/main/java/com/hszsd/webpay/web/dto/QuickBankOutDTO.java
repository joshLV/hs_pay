package com.hszsd.webpay.web.dto;

import java.io.Serializable;


/**
 * 用户快捷支付银行卡返回结果业务类
 * Created by suocy on 2016/8/8.
 */
public class QuickBankOutDTO implements Serializable {

    private String id; //银行卡ID
    private String cardTop; //卡号前六位
    private String cardLast; //卡号后四位
    private String logo; //银行标识
    private String bankName; //银行名称

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCardTop() {
        return cardTop;
    }

    public void setCardTop(String cardTop) {
        this.cardTop = cardTop;
    }

    public String getCardLast() {
        return cardLast;
    }

    public void setCardLast(String cardLast) {
        this.cardLast = cardLast;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
