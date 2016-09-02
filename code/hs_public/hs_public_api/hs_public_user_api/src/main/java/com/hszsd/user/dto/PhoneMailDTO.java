package com.hszsd.user.dto;

import java.io.Serializable;

/**
 * 用户手机号，用户邮箱实体
 * @author yangwenjian
 * @version V1.0.0
 */
public class PhoneMailDTO implements Serializable {

    private static final long serialVersionUID = -2536681668328186677L;

    private  String  userId;
    private  String  phone;
    private  String  email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "PhoneMailDTO{" +
                "email='" + email + '\'' +
                ", userId='" + userId + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
