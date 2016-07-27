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
        /**
         * 是否启用标志
         * 1:启用;0:不启用
         * */
        int IS_ENABLE = 1;
        int NOT_ENABLE = 0;

        /**
         * 异步通知次数
         * 默认为4次
        */
        int CALLBACK_DEFAULT_COUNT = 4;

        /**
         * MD5签名顺序模板名称
         * */
        String RECHARGE_REQ="RECHARGE_REQ";
        String RECHARGE_RES="RECHARGE_RES";
        String BALANCEPAY_REQ="BALANCEPAY_REQ";
        String BALANCEPAY_RES="BALANCEPAY_RES";
        String REFUND_REQ="REFUND_REQ";
        String REFUND_RES="REFUND_RES";
        String CREDITPAY_REQ="CREDITPAY_REQ";
        String CREDITPAY_RES="CREDITPAY_RES";
        String ADDCREDIT_REQ="ADDCREDIT_REQ";
        String ADDCREDIT_RES="ADDCREDIT_RES";
        String ASSEMBLEPAY_REQ="ASSEMBLEPAY_REQ";
        String ASSEMBLEPAY_RES="ASSEMBLEPAY_RES";
        String ASSEMBLEREFUND_REQ="ASSEMBLEREFUND_REQ";
        String ASSEMBLEREFUND_RES="ASSEMBLEREFUND_RES";

    }

    /**
     *充值静态数据
     */
    interface RECHARGE{
        /**
         * RechargeRecord status
         * 0:未审核 1:通过 2:不通过 3:充值失败
         */
        int WATI_CHECK = 0;
        int ALLOW_CHECKED = 1;
        int NOALLOW_CHECKED = 2;
        int RECHARGE_FAILED = 3;

        /**
         * RechargeRecord 	type
         *  充值类型 1:线上 2:线下
         */
        int ONLINE =1;
        int OFFLINE =2;

        /**
         * 用户账户类型标识
         * 1:投资人 2:借款人
         * */
        int USERTYPE_INVESTOR = 1;
        int USERTYPE_BORROWER = 2;

        /**
         * 账号实名认证状态标识
         * 0:未认证 1:已认证
         */
        int REALSTATUS_ISNOT = 0;
        int REALSTATUS_ISYES = 1;
    }


    /**
     *交易记录静态数据
     */
    interface TRADE_RECORD{
        /**
         * TradeRecord 	operateType
         *  操作类型 1:充值 2:余额支付 3:余额退款 4:积分支付 5:积分退款 6:组合支付-积分与余额 7:组合退款-积分与余额
         */
        int RECHARGE =1;
        int BALANCE_PAY =2;
        int REFUND = 3;
        int CREDIT_PAY = 4;
        int ADD_CREDIT = 5;
        int ASSEMBLE_PAY = 6;
        int ASSEMBLE_REFUND = 7;

        /**
         *交易状态（1:等待处理 2:交易成功 3:交易失败 4:第三方充值成功，资金记录修改失败）
         */
        int TRADE_STATUS_1 =1;
        int TRADE_STATUS_2 =2;
        int TRADE_STATUS_3 =3;
        int TRADE_STATUS_4 =4;
    }


}
