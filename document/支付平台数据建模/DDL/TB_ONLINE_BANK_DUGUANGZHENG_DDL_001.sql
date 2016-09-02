drop table TB_ONLINE_BANK cascade constraints;

/*==============================================================*/
/* Table: TB_ONLINE_BANK                                        */
/*==============================================================*/
create table TB_ONLINE_BANK 
(
   ID                   NUMBER(11)           not null,
   BANK_NAME            VARCHAR2(64),
   BANK_LOGO            VARCHAR2(64),
   BANK_VALUE           VARCHAR2(64),
   PAYMENT_INTERFACE_ID VARCHAR2(64),
   IS_ENABLE            INTEGER,
   constraint PK_TB_ONLINE_BANK primary key (ID)
);

comment on table TB_ONLINE_BANK is
'在线支付银行信息表';

comment on column TB_ONLINE_BANK.ID is
'主键编号';

comment on column TB_ONLINE_BANK.BANK_NAME is
'银行名称';

comment on column TB_ONLINE_BANK.BANK_LOGO is
'logo地址';

comment on column TB_ONLINE_BANK.BANK_VALUE is
'银行标识';

comment on column TB_ONLINE_BANK.PAYMENT_INTERFACE_ID is
'第三方支付接口ID';

comment on column TB_ONLINE_BANK.IS_ENABLE is
'是否启用 1:启用 2:不启用';
