package com.hszsd.webpay.common;

/**
 * 返回结果枚举类
 * Created by gzhengDu on 2016/6/28.
 */
public enum ResultConstants {
    OPERATOR_SUCCESS("0000", "操作成功"),
    OPERATOR_FAIL("1000", "操作失败"),
    ORDER_REPEAT("1001", "支付失败，订单重复提交"),
    USER_ERROR("1002", "登录用户与支付用户不一致"),
    REALSTATUS_ERROR("1003", "该用户未实名验证"),
    PARAMETERS_ISNULL("2000","参数为空"),
    CAPTCHA_ISNULL("2001", "验证码不能为空，请重新输入"),
    BANKID_ISNULL("2002", "请选择银行"),
    MONEY_ISNULL("2003", "充值金额不能为空！"),
    MONEY_WRONGFORMAT("2004", "输入的金额格式不正确"),
    CAPTCHA_ISWRONG("2005", "验证码输入错误，请重新输入"),
    USERID_ISNULL("2006", "用户编号参数为空"),
    MOBILE_ISNULL("2007", "手机号参数为空"),
    SOURCECODE_ISNULL("2008", "平台来源参数为空"),
    SOURCECODE_ISWRONG("2009", "未定义的平台来源"),
    RETURNURL_ISNULL("2010", "前台回调地址参数为空"),
    NOTICEURL_ISNULL("2011", "后台回调地址参数为空"),
    MD5SIGN_ISNULL("2012", "MD5签名参数为空"),
    MD5SIGN_ISWRONG("2013", "MD5签名参数不匹配"),
    PAYPASSWORD_ISNULL("2014", "支付密码为空"),
    PAYPASSWORD_ISWRONG("2015", "支付密码错误，请重新输入"),
    TRANSID_ISNULL("2016", "交易流水号为空"),
    CREDIT_WRONG("2017", "招商币格式不正确，必须大于零"),
    TRANSID_ISWRONG("2018", "该交易流水号不存在"),
    CARDNO_ISNULL("2019", "银行卡号为空"),
    REGISTERTEL_ISNULL("2020", "银行预留手机号为空"),
    SMSCODE_ISNULL("2021", "短信验证码为空"),
    REQUESTID_ISNULL("2022", "绑卡请求号为空"),
    CARDTOP_ISNULL("2023", "卡号前六位为空"),
    CARDLAST_ISNULL("2024", "卡号后四位为空"),
    QUICKBANK_ISEXIST("7004", "用户已绑定快捷支付卡"),
    PAYPASSWORD_NOSET("2026", "未设置交易密码，请去个人中心设置"),
    PAGENUM_ISWRONG("2027", "当前页必须是正整数"),
    PAGESIZE_ISWRONG("2028", "每页条数必须是正整数"),
    MONEY_WRONG("2029", "金额格式不正确，必须大于零"),
    SESSION_TIME_OUT("3000","用户信息已过期，请重新登录"),
    USER_ISERROR("5000", "用户账号异常"),
    REALSTATUS_ISNOT("5003", "用户未实名认证"),
    WAIT_OPERATOR("9000", "等待处理");

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