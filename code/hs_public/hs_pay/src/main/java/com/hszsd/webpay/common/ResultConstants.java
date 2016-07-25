package com.hszsd.webpay.common;

/**
 * 返回结果枚举类
 * Created by gzhengDu on 2016/6/28.
 */
public enum ResultConstants {
    OPERATOR_SUCCESS("0000","操作成功！"),
    OPERATOR_FAIL("1000","操作失败！"),
    PARAMETERS_ISNULL("2000","参数为空！"),
    SESSION_TIME_OUT("3000","用户信息已过期，请重新登录"),
    //校验结果
    CAPTCHA_ISNULL("2001", "验证码不能为空，请重新输入！"),
    BANKID_ISNULL("2002", "请选择银行！"),
    AMOUNT_ISNULL("2003", "充值金额不能为空！"),
    AMOUNT_WRONGFORMAT("2004", "输入的金额格式不正确！"),
    CAPTCHA_ISWRONG("2005", "验证码输入错误，请重新输入！"),
    USERID_ISNULL("2006", "用户编号参数为空！"),
    MOBILE_ISNULL("2007", "手机号参数为空！"),
    SOURCECODE_ISNULL("2008", "平台来源参数为空！"),
    RETURNURL_ISNULL("2009", "前台回调地址参数为空！"),
    NOTICEURL_ISNULL("2010", "后台回调地址参数为空！"),
    MD5SIGN_ISNULL("2011", "MD5签名参数为空！"),
    MD5SIGN_ISWRONG("2012", "MD5签名参数不匹配！"),
    PAYPASSWORD_ISNULL("2013", "支付密码为空！"),
    PAYPASSWORD_ISWRONG("2014", "支付密码错误，请重新输入！"),
    TRANSID_ISNULL("2015", "交易流水号为空！");

    String code;
    String msg;
    ResultConstants(String code, String msg){
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