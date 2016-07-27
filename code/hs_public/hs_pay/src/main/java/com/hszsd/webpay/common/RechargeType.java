package com.hszsd.webpay.common;

/**
 * 充值方式
 * 18: 移动在线
 * 22：宝付
 * 19：网易在线
 * 21：连连支付
 * 24：汇潮支付
 * 30：贵阳银行
 * 1000：易宝支付
 * Created by gzhengDu on 2016/7/4.
 */
public enum RechargeType {
    MOBILE("18"), BAOFOO("22"), ONLINE("19"), LIANLIAN("21"), HUICHAO("24"), MERCHANT("30"), YEEPAY("1000");

    private String code;

    RechargeType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
