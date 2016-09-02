drop table TB_PAYMENT_INTERFACE cascade constraints;

/*==============================================================*/
/* Table: TB_PAYMENT_INTERFACE                                  */
/*==============================================================*/
create table TB_PAYMENT_INTERFACE 
(
   INTERFACE_ID         NUMBER(11)           not null,
   INTERFACE_NAME       VARCHAR2(255),
   INTERFACE_VALUE      VARCHAR2(255),
   MERCHANT_ID          VARCHAR2(255),
   MERCHANT_KEY         VARCHAR2(255),
   RECHARGE_FEE         NUMBER(6,2),
   PAY_URL              VARCHAR2(255),
   RETURN_URL           VARCHAR2(255),
   NOTICE_URL           VARCHAR2(255),
   CHARSET              VARCHAR2(255),
   SIGN_TYPE            VARCHAR2(255),
   LOGO_URL             VARCHAR2(255),
   ORDER_BY             NUMBER(5),
   IS_ENABLE            INTEGER,
   CREATE_DATE          DATE,
   CREATE_BY            VARCHAR2(32),
   UPDATE_DATE          DATE,
   UPDATE_BY            VARCHAR2(32),
   REMARK               VARCHAR2(500),
   constraint PK_TB_PAYMENT_INTERFACE primary key (INTERFACE_ID)
);

comment on table TB_PAYMENT_INTERFACE is
'第三方支付信息表';

comment on column TB_PAYMENT_INTERFACE.INTERFACE_ID is
'接口编号';

comment on column TB_PAYMENT_INTERFACE.INTERFACE_NAME is
'接口名称';

comment on column TB_PAYMENT_INTERFACE.INTERFACE_VALUE is
'接口标识值';

comment on column TB_PAYMENT_INTERFACE.MERCHANT_ID is
'商户号';

comment on column TB_PAYMENT_INTERFACE.MERCHANT_KEY is
'商户key';

comment on column TB_PAYMENT_INTERFACE.RECHARGE_FEE is
'充值费率';

comment on column TB_PAYMENT_INTERFACE.PAY_URL is
'支付地址';

comment on column TB_PAYMENT_INTERFACE.RETURN_URL is
'页面返回地址';

comment on column TB_PAYMENT_INTERFACE.NOTICE_URL is
'异步通知地址';

comment on column TB_PAYMENT_INTERFACE.CHARSET is
'编码格式';

comment on column TB_PAYMENT_INTERFACE.SIGN_TYPE is
'加密方式 MD5 RSA';

comment on column TB_PAYMENT_INTERFACE.LOGO_URL is
'logo地址';

comment on column TB_PAYMENT_INTERFACE.ORDER_BY is
'排序 数字越小越靠前';

comment on column TB_PAYMENT_INTERFACE.IS_ENABLE is
'是否启用1启用 2不启用';

comment on column TB_PAYMENT_INTERFACE.CREATE_DATE is
'创建时间';

comment on column TB_PAYMENT_INTERFACE.CREATE_BY is
'创建人';

comment on column TB_PAYMENT_INTERFACE.UPDATE_DATE is
'更新时间';

comment on column TB_PAYMENT_INTERFACE.UPDATE_BY is
'更新人';

comment on column TB_PAYMENT_INTERFACE.REMARK is
'备注';
