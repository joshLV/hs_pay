package com.hszsd.account.dto;

/**
 * Created by YangWenJian on 2016-8-15.
 */
public enum UserType {

    IMEI(0,"IMEI"),MACURL(1,"MAC 地址"),USERID(2,"用户ID（默认）"),UEREMAIL(3,"用户Email"),PHONE(4,"用户手机号"),CARDID(5,"用户身份证号"),ORDERID(6,"用户纸质订单协议号");
    private int id;
    private String mark;

    UserType(int id,String mark){
        this.id=id;
        this.mark=mark;
    }

    public int getId() {
        return id;
    }

    public String getMark() {
        return mark;
    }
}
