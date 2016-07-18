package com.hszsd.webpay.common;

/**
 * 返回结果枚举类
 * Created by gzhengDu on 2016/6/28.
 */
public enum ResultConstants {
    OPERATOR_SUCCESS(200,"操作成功！"),
    OPERATOR_FAIL(404,"操作失败！"),
    PARAMETERS_ISNULL(300,"参数为空！"),
    SESSION_TIME_OUT(405,"用户信息已过期，请重新登录");

    int code;
    String msg;
    ResultConstants(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
