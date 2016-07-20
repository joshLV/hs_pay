package com.hszsd.webpay.common;

/**
 * 返回结果枚举类
 * Created by gzhengDu on 2016/6/28.
 */
public enum ResultConstants {
    OPERATOR_SUCCESS("0000","操作成功！"),
    OPERATOR_FAIL("1000","操作失败！"),
    PARAMETERS_ISNULL("2000","参数为空！"),
    SESSION_TIME_OUT("3000","用户信息已过期，请重新登录");

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