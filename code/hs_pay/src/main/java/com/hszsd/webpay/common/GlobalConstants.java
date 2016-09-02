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
         * 1:启用;2:不启用
         * */
        int IS_ENABLE = 1;
        int NOT_ENABLE = 2;

        /**
         * 异步通知次数
         * 默认为4次
        */
        int CALLBACK_DEFAULT_COUNT = 4;


        /**
         * 商户返回确认收到异步通知标识
         */
        String BACK_MESSAGE = "OK";

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
        String YEEPAY_REQ="YEEPAY_REQ";
        String YEEPAY_RES="YEEPAY_RES";

        /**
         * redis key前缀
         * */
        String REDIS_TRANSID_PREFIX = "hspay:transId:";
        String REDIS_PAYPASSWORD_PREFIX = "hspay:payPassWord:";
        String REDIS_ORDERID_PREFIX = "hspay:orderId:";
        String REDIS_CARDNO_PREFIX = "hspay:cardno:";
        String REDIS_CALLBACKURL_PREFIX = "hspay:bindcardcallbackurl:";

        /**
         * 用户实名认证状态 0：未认证；1:已认证
         */
        int REAL_STATUS_0 = 0;
        int REAL_STATUS_1 = 1;

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
         *  充值类型 1:线上 2:线下 3:移动充值
         */
        int ONLINE = 1;
        int OFFLINE = 2;
        int MOBILE = 3;

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
         * TradeRecordPO 	operateType
         *  操作类型 1 充值 11网银在线充值 12 汇潮充值 13 宝付充值 14 易宝充值
         *          2 支付  21余额支付 22招商币支付  23组合支付（余额和招商币支付） 24快捷支付
         *          3 退回  31增加余额  32增加招商币 33增加余额增加招商币
         */
        int RECHARGE = 1;
        int RECHARGE_ONLINE = 11;
        int RECHARGE_HUICHAO = 12;
        int RECHARGE_BAOFOO = 13;
        int RECHARGE_YEEPAY = 14;
        int PAY = 2;
        int BALANCE_PAY = 21;
        int CREDIT_PAY = 22;
        int ASSEMBLE_PAY = 23;
        int QUICK_PAY = 24;
        int ROLLBACK = 3;
        int REFUND = 31;
        int ADD_CREDIT = 32;
        int ASSEMBLE_REFUND = 33;

        /**
         * TradeRecordPO 	orderType
         *  订单类型 1正宗商城 2中天物业 3中天商城
         */
        int ORDER_TYPE_ZZSC = 1;
        int ORDER_TYPE_ZTWY = 2;
        int ORDER_TYPE_ZTSC = 3;

        /**
         *交易状态（1:等待处理 2:交易成功 3:交易失败）
         */
        int TRADE_STATUS_1 =1;
        int TRADE_STATUS_2 =2;
        int TRADE_STATUS_3 =3;

        /**
         *平台来源（SH:商城、P2P:p2p、H5：微信）
         */
        String SOURCE_CODE_SH = "SH";
        String SOURCE_CODE_P2P = "P2P";
        String SOURCE_CODE_H5 = "H5";
    }

    /**
     *账户日志记录静态数据
     */
    interface ACCOUNT_LOG{
        /**
         * AccountLog type
         *  日志类型  1 充值 24 商品购买 25 商品退货 26 线上冻结可用 27 线上解除冻结  28冻结转账  29 缴费入账 30 货款入账
         */
        int TYPE_1 = 1;
        int TYPE_24 = 24;
        int TYPE_25 = 25;
        int TYPE_26 = 26;
        int TYPE_27 = 27;
        int TYPE_28 = 28;
        int TYPE_29 = 29;
        int TYPE_30 = 30;
    }

    /**
     *招商币日志记录静态数据
     */
    interface CREDIT_LOG{
        /**
         * CreditLog typeId
         *  日志类型  5、商城购物扣除招商币 6、商城购物获招商币
         */
        int TYPEID_5 = 5;
        int TYPEID_6 = 6;
    }

    /**
     *用户快捷支付银行卡关系表静态数据
     */
    interface ACCOUNT_QUICK_BANK{
        /**
         * AccountQuickBankPO userType
         *  用户标识类型  0：IMEI 1：MAC 地址 2：用户ID（默认） 3：用户Email 4：用户手机号 5：用户身份证号 6：用户纸质订单协议号
         */
        int USERTYPE_1 = 1;
        int USERTYPE_2 = 2;
        int USERTYPE_3 = 3;
        int USERTYPE_4 = 4;
        int USERTYPE_5 = 5;
        int USERTYPE_6 = 6;

        /**
         * AccountQuickBankPO bindStatus
         *  绑定状态 1:已绑定  2:已解绑 3:未绑定
         */
        int BINDSTATUS_1 = 1;
        int BINDSTATUS_2 = 2;
        int BINDSTATUS_3 = 3;



        /**
         * AccountQuickBankPO validStatus
         *  验证状态  1:已认证 2:未认证
         */
        int VALIDSTATUS_1 = 1;
        int VALIDSTATUS_2 = 2;
    }


}
