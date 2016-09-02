drop table TB_ACCOUNT_QUICK_BANK cascade constraints;

/*==============================================================*/
/* Table: TB_ACCOUNT_QUICK_BANK                                 */
/*==============================================================*/
create table TB_ACCOUNT_QUICK_BANK 
(
   ID                   VARCHAR2(32)         not null,
   USER_TYPE            NUMBER(2),
   USER_ID              VARCHAR2(32),
   REQUEST_ID           VARCHAR2(50),
   USER_IP              VARCHAR2(32),
   CARD_TOP             VARCHAR2(32),
   CARD_LAST            VARCHAR2(32),
   BANK_CODE            VARCHAR2(32),
   BIND_STATUS          NUMBER(2),
   VALID_STATUS         NUMBER(2),
   CREATE_DATE          DATE,
   CREATE_BY            VARCHAR2(32),
   UPDATE_DATE          DATE,
   UPDATE_BY            VARCHAR2(32),
   constraint PK_TB_ACCOUNT_QUICK_BANK primary key (ID)
);

comment on table TB_ACCOUNT_QUICK_BANK is
'用户快捷支付银行卡关系表';

comment on column TB_ACCOUNT_QUICK_BANK.ID is
'银行卡标识';

comment on column TB_ACCOUNT_QUICK_BANK.USER_TYPE is
'0：IMEI
1：MAC 地址
2：用户ID（默认）
3：用户Email
4：用户手机号
5：用户身份证号
6：用户纸质订单协议号';

comment on column TB_ACCOUNT_QUICK_BANK.USER_ID is
'用户标识';

comment on column TB_ACCOUNT_QUICK_BANK.REQUEST_ID is
'绑卡请求标识';

comment on column TB_ACCOUNT_QUICK_BANK.USER_IP is
'用户IP';

comment on column TB_ACCOUNT_QUICK_BANK.CARD_TOP is
'卡号前6位';

comment on column TB_ACCOUNT_QUICK_BANK.CARD_LAST is
'卡号后4位';

comment on column TB_ACCOUNT_QUICK_BANK.BANK_CODE is
'银行编号';

comment on column TB_ACCOUNT_QUICK_BANK.BIND_STATUS is
'1:已绑定  2:已解绑 3:未绑定';

comment on column TB_ACCOUNT_QUICK_BANK.VALID_STATUS is
'1:已认证 2:未认证';

comment on column TB_ACCOUNT_QUICK_BANK.CREATE_DATE is
'创建时间';

comment on column TB_ACCOUNT_QUICK_BANK.CREATE_BY is
'创建人';

comment on column TB_ACCOUNT_QUICK_BANK.UPDATE_DATE is
'修改时间';

comment on column TB_ACCOUNT_QUICK_BANK.UPDATE_BY is
'修改人';
