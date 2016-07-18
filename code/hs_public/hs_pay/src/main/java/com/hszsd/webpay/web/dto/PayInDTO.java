package com.hszsd.webpay.web.dto;

import com.hszsd.user.dto.User;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * 支付入参业务类
 * Created by gzhengDu on 2016/7/4.
 */
public class PayInDTO implements Serializable{

    private static final long serialVersionUID = -7054645706622534081L;

    //支付方式
    private String type;

    //银行
    private String bankAddr;

    //支付金额
    private String money;

    //当前支付用户信息
    private User user;

    //会话请求
    private HttpServletRequest request;

    public PayInDTO() {
    }

    public PayInDTO(String type, String bankAddr, String money, User user, HttpServletRequest request) {
        this.type = type;
        this.bankAddr = bankAddr;
        this.money = money;
        this.user = user;
        this.request = request;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBankAddr() {
        return bankAddr;
    }

    public void setBankAddr(String bankAddr) {
        this.bankAddr = bankAddr;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public String toString() {
        return "PayInDTO{" +
                "type='" + type + '\'' +
                ", money='" + money + '\'' +
                ", bankAddr='" + bankAddr + '\'' +
                ", user=" + user +
                ", request=" + request +
                '}';
    }
}
