drop table TB_BANK_INFO cascade constraints;

/*==============================================================*/
/* Table: TB_BANK_INFO                                          */
/*==============================================================*/
create table TB_BANK_INFO 
(
   BANK_ID              NUMBER(11)           not null,
   BANK_NAME            VARCHAR2(64),
   BANK_VALUE           VARCHAR2(64),
   BANK_LOGO_PC         VARCHAR2(64),
   BANK_LOGO_MOB        VARCHAR2(64),
   QUICK_FLAG           INTEGER,
   IS_ENABLE_PC         INTEGER,
   IS_ENABLE_MOB        INTEGER,
   constraint PK_TB_BANK_INFO primary key (BANK_ID)
);

comment on table TB_BANK_INFO is
'支付平台银行信息表';

comment on column TB_BANK_INFO.BANK_ID is
'银行编号';

comment on column TB_BANK_INFO.BANK_NAME is
'银行名称';

comment on column TB_BANK_INFO.BANK_VALUE is
'银行标识';

comment on column TB_BANK_INFO.BANK_LOGO_PC is
'PC端logo地址';

comment on column TB_BANK_INFO.BANK_LOGO_MOB is
'移动端logo地址';

comment on column TB_BANK_INFO.QUICK_FLAG is
'是否支持快捷支付 1:支持 2:不支持';

comment on column TB_BANK_INFO.IS_ENABLE_PC is
'PC端是否启用 1:启用 2:不启用';

comment on column TB_BANK_INFO.IS_ENABLE_MOB is
'移动端是否启用: 1: 启用 2:不启用';
