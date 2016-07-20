package com.hszsd.webpay.common;

/**
 * 常用静态变量
 * Created by gzhengDu on 2016/6/29.
 */
public interface GlobalConstants {

    /**
     *通用静态数据
     */
    interface COMMON{
        /*是否启用标志1：启用;0:不启用*/
        public static final int IS_ENABLE = 1;
        public static final int NOT_ENABLE = 0;

    }

    /**
     *充值静态数据
     */
    interface RECHARGE{
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


    /**
     *交易记录静态数据
     */
    interface TRADE_RECORD{
        /**
         * TradeRecord 	operateType
         *  操作类型 1:充值 2:余额支付 3:余额退款 4:积分支付 5:积分退款 6:组合支付-积分与余额 7:组合退款-积分与余额
         */
        public static final int RECHARGE =1;
        public static final int BALANCE_PAY =2;
        public static final int REFUND = 3;
        public static final int CREDIT_PAY = 4;
        public static final int ADD_CREDIT = 5;
        public static final int ASSEMBLE_PAY = 6;
        public static final int ASSEMBLE_REFUND = 7;

    }


}
