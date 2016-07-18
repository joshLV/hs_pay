package com.hszsd.webpay.common;

/**
 * 常用静态变量
 * Created by gzhengDu on 2016/6/29.
 */
public class GlobalConstants {

    /*是否启用标志1：启用;0:不启用*/
    public static final int IS_ENABLE = 1;
    public static final int NOT_ENABLE = 0;

    /**
     * RechargeRecord status
     * 0 –未审核 1通过 2不通过 3充值失败
     */
    public static final int WATI_CHECK = 0;
    public static final int ALLOW_CHECKED = 1;
    public static final int NOALLOW_CHECKED = 2;
    public static final int RECHARGE_FAILED = 3;

    /**
     * RechargeRecord 	type
     *  充值类型 1 线上 2 线下
     */
    public static final int ONLINE =1;
    public static final int OFFLINE =2;

}
