package com.hszsd.webpay.web.dto;

import com.hszsd.webpay.common.Type;

import java.io.Serializable;

/**
 * 支付返回结果业务类
 * Created by gzhengDu on 2016/7/4.
 */
public class PayOutDTO implements Serializable{

    private static final long serialVersionUID = -7303944354638862590L;

    //操作结果
    private Object result;

    //返回请求地址 URL或者FORM
    private String request = "";

    //返回对象
    private Object bean;

    //默认URL
    private String urlType = Type.URL.toString();

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public String getUrlType() {
        return urlType;
    }

    public void setUrlType(String urlType) {
        this.urlType = urlType;
    }

    @Override
    public String toString() {
        return "PayOutDTO{" +
                "result=" + result +
                ", request='" + request + '\'' +
                ", bean=" + bean +
                ", urlType='" + urlType + '\'' +
                '}';
    }
}
