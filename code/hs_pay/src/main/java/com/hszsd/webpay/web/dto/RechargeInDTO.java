package com.hszsd.webpay.web.dto;

import com.hszsd.user.dto.GetUserInfoDTO;
import com.hszsd.webpay.web.form.RechargeForm;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * 支付入参业务类
 * Created by gzhengDu on 2016/7/4.
 */
public class RechargeInDTO implements Serializable{

    private static final long serialVersionUID = -7054645706622534081L;

    //交易流水号
    private String transId;

    //支付方式
    private String type;

    //银行
    private String bankId;

    //支付金额
    private String money;

    //当前支付用户信息
    private GetUserInfoDTO user;

    //会话请求
    private transient HttpServletRequest request;

    public RechargeInDTO() {
    }

    public RechargeInDTO(RechargeForm rechargeForm, GetUserInfoDTO user, HttpServletRequest request) {
        this.transId = rechargeForm.getTransId();
        this.type = rechargeForm.getBankCode()[0];
        this.bankId = rechargeForm.getBankCode()[1];
        this.money = rechargeForm.getMoney();
        this.user = user;
        this.request = request;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public GetUserInfoDTO getUser() {
        return user;
    }

    public void setUser(GetUserInfoDTO user) {
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
        return "RechargeInDTO{" +
                "transId='" + transId + '\'' +
                ", type='" + type + '\'' +
                ", bankId='" + bankId + '\'' +
                ", money='" + money + '\'' +
                ", user=" + user +
                ", request=" + request +
                '}';
    }
}
