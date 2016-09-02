drop table TB_TRADE_RECORD cascade constraints;

/*==============================================================*/
/* Table: TB_TRADE_RECORD                                       */
/*==============================================================*/
create table TB_TRADE_RECORD 
(
   TRANS_ID             VARCHAR2(32)         not null,
   USER_ID              VARCHAR2(32),
   TO_USER_ID           VARCHAR2(32),
   MONEY                NUMBER(16,6),
   CREDIT               NUMBER(16,6),
   MOBILE               VARCHAR2(32),
   ORDER_ID             VARCHAR2(32),
   PRODUCT_NAME         VARCHAR2(100),
   SOURCE_CODE          VARCHAR2(32),
   OPERATE_TYPE         INTEGER,
   ORDER_TYPE           INTEGER,
   TRADE_STATUS         INTEGER,
   RETURN_URL           VARCHAR2(200),
   NOTICE_URL           VARCHAR2(200),
   CREATE_DATE          DATE,
   CREATE_BY            VARCHAR2(32),
   UPDATE_DATE          DATE,
   UPDATE_BY            VARCHAR2(32),
   REMARK               VARCHAR2(255),
   constraint PK_TB_TRADE_RECORD primary key (TRANS_ID)
);

comment on table TB_TRADE_RECORD is
'交易记录表';

comment on column TB_TRADE_RECORD.TRANS_ID is
'流水号';

comment on column TB_TRADE_RECORD.USER_ID is
'用户编号';

comment on column TB_TRADE_RECORD.TO_USER_ID is
'转入账号';

comment on column TB_TRADE_RECORD.MONEY is
'操作金额';

comment on column TB_TRADE_RECORD.CREDIT is
'操作积分';

comment on column TB_TRADE_RECORD.MOBILE is
'手机号';

comment on column TB_TRADE_RECORD.ORDER_ID is
'来源方订单号';

comment on column TB_TRADE_RECORD.PRODUCT_NAME is
'商品名称';

comment on column TB_TRADE_RECORD.SOURCE_CODE is
'平台来源（SH:商城 P2P:p2p平台 H5:微信）';

comment on column TB_TRADE_RECORD.OPERATE_TYPE is
'操作类型(1充值 11网银在线 12汇潮充值 13宝付充值 14易宝充值 2支付 21余额支付 22招商币支付 23组合支付（余额和招商币支付） 24快捷支付 3退回 31增加余额 32增加招商币 33增加余额增加招商币)';

comment on column TB_TRADE_RECORD.ORDER_TYPE is
'订单类型（1:正宗商城 2:中天物业 3:中天商城）';

comment on column TB_TRADE_RECORD.TRADE_STATUS is
'交易状态（1:等待处理 2:交易成功 3:交易失败）';

comment on column TB_TRADE_RECORD.RETURN_URL is
'前台回调地址';

comment on column TB_TRADE_RECORD.NOTICE_URL is
'后台回调地址';

comment on column TB_TRADE_RECORD.CREATE_DATE is
'创建时间';

comment on column TB_TRADE_RECORD.CREATE_BY is
'创建人';

comment on column TB_TRADE_RECORD.UPDATE_DATE is
'更新时间';

comment on column TB_TRADE_RECORD.UPDATE_BY is
'更新人';

comment on column TB_TRADE_RECORD.REMARK is
'备注';
