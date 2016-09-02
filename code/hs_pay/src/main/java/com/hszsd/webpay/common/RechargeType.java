package com.hszsd.webpay.common;

/**
 * 充值方式
 * 11：网银在线
 * 12：汇潮支付
 * 13：宝付
 * 14：易宝支付
 * 15: 移动和包支付
 * 16：连连支付
 * 17：贵阳银行
 * Created by gzhengDu on 2016/7/4.
 */
public enum RechargeType {
    ONLINE("11", "网银在线"), HUICHAO("12", "汇潮支付"), BAOFOO("13", "宝付支付"), YEEPAY("14", "易宝支付"), MOBILE("15", "中移动和包支付"), LIANLIAN("16", "连连支付"), MERCHANT("17", "贵阳银行");

    private String code;

    private String name;

    RechargeType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
