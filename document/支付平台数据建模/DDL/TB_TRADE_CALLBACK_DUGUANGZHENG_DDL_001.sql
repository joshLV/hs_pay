drop table TB_TRADE_CALLBACK cascade constraints;

/*==============================================================*/
/* Table: TB_TRADE_CALLBACK                                     */
/*==============================================================*/
create table TB_TRADE_CALLBACK 
(
   TRANS_ID             VARCHAR2(32)         not null,
   COUNT                INTEGER,
   CREATE_DATE          DATE,
   CREATE_BY            VARCHAR2(32),
   UPDATE_DATE          DATE,
   UPDATE_BY            VARCHAR2(32),
   REMARK               VARCHAR2(255),
   constraint PK_TB_TRADE_CALLBACK primary key (TRANS_ID)
);

comment on column TB_TRADE_CALLBACK.TRANS_ID is
'流水号';

comment on column TB_TRADE_CALLBACK.COUNT is
'回调次数（最大四次）';

comment on column TB_TRADE_CALLBACK.CREATE_DATE is
'创建时间';

comment on column TB_TRADE_CALLBACK.CREATE_BY is
'创建人';

comment on column TB_TRADE_CALLBACK.UPDATE_DATE is
'更新时间';

comment on column TB_TRADE_CALLBACK.UPDATE_BY is
'更新人';

comment on column TB_TRADE_CALLBACK.REMARK is
'备注';
