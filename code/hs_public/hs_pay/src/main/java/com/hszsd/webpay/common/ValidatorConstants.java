package com.hszsd.webpay.common;

/**
 * 数据校验结果枚举类
 * Created by gzhengDu on 2016/7/1.
 */
public enum ValidatorConstants {
    CAPTCHA_ISNULL("301", "验证码不能为空，请重新输入！"),
    BANKID_ISNULL("302", "请选择银行！"),
    AMOUNT_ISNULL("303", "充值金额不能为空！"),
    AMOUNT_WRONGFORMAT("304", "输入的金额格式不正确！"),
    CAPTCHA_ISWRONG("305", "验证码输入错误，请重新输入！");

    private String code;
    private String msg;

    ValidatorConstants(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
