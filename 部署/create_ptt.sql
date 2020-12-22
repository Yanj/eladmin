
drop table if exists ptt_resource_type;

drop table if exists ptt_resource;

drop table if exists ptt_term;

drop table if exists ptt_term_resource_type;

drop table if exists ptt_patient;

drop table if exists ptt_patient_dept;

drop table if exists ptt_patient_follow;

drop table if exists ptt_patient_term;

drop table if exists ptt_patient_term_log;

drop table if exists ptt_patient_term_reserve;

drop table if exists ptt_patient_term_reserve_log;

drop table if exists ptt_patient_term_reserve_resource;

drop table if exists ptt_cus;

drop table if exists ptt_patient_cus;

drop table if exists ptt_his_log;



/*==============================================================*/
/* Table: ptt_resource_type                                     */
/*==============================================================*/
create table ptt_resource_type
(
   id                   bigint(20) auto_increment not null comment 'id',
   dept_id              bigint(20) comment '部门id',
   name                 varchar(255) comment '名称',
   max_count            bigint(10) default 0 comment '最大数量: 0-无限制',
   status               varchar(255) comment '状态',
   create_by            varchar(255) comment '创建人',
   update_by            varchar(255) comment '修改人',
   create_time          timestamp default CURRENT_TIMESTAMP comment '创建时间',
   update_time          timestamp default CURRENT_TIMESTAMP comment '修改时间',
   remark               varchar(255) comment '备注',
   primary key (id)
);

alter table ptt_resource_type comment '资源类型';

/*==============================================================*/
/* Table: ptt_resource                                          */
/*==============================================================*/
create table ptt_resource
(
   id                   bigint(20) auto_increment not null comment 'id',
   resource_type_id     bigint(20) comment '类型id',
   dept_id              bigint(20) comment '部门id',
   code                 varchar(255) comment '编码',
   name                 varchar(255) comment '名称',
   count                bigint(10) default 1 comment '数量',
   status               varchar(255) comment '状态',
   create_by            varchar(255) comment '创建人',
   update_by            varchar(255) comment '修改人',
   create_time          timestamp default CURRENT_TIMESTAMP comment '创建时间',
   update_time          timestamp default CURRENT_TIMESTAMP comment '修改时间',
   remark               varchar(255) comment '备注',
   primary key (id)
);

alter table ptt_resource comment '资源';

/*==============================================================*/
/* Table: ptt_term                                              */
/*==============================================================*/
create table ptt_term
(
   id                   bigint(20) auto_increment not null comment 'id',
   code                 varchar(255) comment '套餐编码',
   name                 varchar(255) comment '套餐名称',
   description          varchar(1000) comment '套餐描述',
   duration             int default 0 comment '套餐服务时间(分钟): 0-未设置',
   times                int comment '次数',
   unit                 varchar(50) comment '单位',
   price                bigint(20) comment '单价',
   amount               bigint(20) comment '总价',
   status               varchar(255) comment '状态',
   create_by            varchar(255) comment '创建人',
   update_by            varchar(255) comment '修改人',
   create_time          timestamp default CURRENT_TIMESTAMP comment '创建时间',
   update_time          timestamp default CURRENT_TIMESTAMP comment '修改时间',
   remark               varchar(255) comment '备注',
   primary key (id)
);

alter table ptt_term comment '套餐';

/*==============================================================*/
/* Table: ptt_term_resource_type                                */
/*==============================================================*/
create table ptt_term_resource_type
(
   id                   bigint(20) auto_increment not null comment 'id',
   term_id              bigint(20) comment '套餐id',
   dept_id              bigint(20) comment '部门id',
   resource_type_id     bigint(20) comment '资源类型id',
   nullable             int default 0 comment '是否必填: 0-必填, 1-非必填',
   status               varchar(255) comment '状态',
   create_by            varchar(255) comment '创建人',
   update_by            varchar(255) comment '修改人',
   create_time          timestamp default CURRENT_TIMESTAMP comment '创建时间',
   update_time          timestamp default CURRENT_TIMESTAMP comment '修改时间',
   remark               varchar(255) comment '备注',
   primary key (id)
);

alter table ptt_term_resource_type comment '套餐资源类型';

/*==============================================================*/
/* Table: ptt_patient                                           */
/*==============================================================*/
create table ptt_patient
(
   id                   bigint(20) auto_increment not null comment 'id',
   patient_id           bigint(20) comment '患者 id',
   name                 varchar(50) comment '姓名',
   mrn                  varchar(50) comment '患者档案编号',
   cert_no              varchar(50) comment '身份证',
   phone                varchar(15) comment '电话',
   birthday             date comment '生日',
   profession           varchar(50) comment '职业',
   address              varchar(255) comment '地址',
   contact_name         varchar(50) comment '联系人',
   contact_phone        varchar(15) comment '联系电话',
   contact_relation     varchar(100) comment '联系人关系',
   status               varchar(255) comment '状态',
   create_by            varchar(255) comment '创建人',
   update_by            varchar(255) comment '修改人',
   create_time          timestamp default CURRENT_TIMESTAMP comment '创建时间',
   update_time          timestamp default CURRENT_TIMESTAMP comment '修改时间',
   remark               varchar(255) comment '备注',
   primary key (id)
);

alter table ptt_patient comment '患者信息';

/*==============================================================*/
/* Table: ptt_patient_dept                                      */
/*==============================================================*/
create table ptt_patient_dept
(
   patient_id           bigint(20) comment '患者 id',
   dept_id              bigint(20) comment '医院 id',
   primary key (patient_id, dept_id)
);

alter table ptt_patient_dept comment '患者医院信息';

/*==============================================================*/
/* Table: ptt_patient_follow                                    */
/*==============================================================*/
create table ptt_patient_follow
(
   id                   bigint(20) auto_increment not null comment 'id',
   patient_id           bigint(20) comment '患者 id',
   follow_in            varchar(500) comment '跟进信息',
   status               varchar(255) comment '状态',
   create_by            varchar(255) comment '创建人',
   update_by            varchar(255) comment '修改人',
   create_time          timestamp default CURRENT_TIMESTAMP comment '创建时间',
   update_time          timestamp default CURRENT_TIMESTAMP comment '修改时间',
   remark               varchar(255) comment '备注',
   primary key (id)
);

alter table ptt_patient_follow comment '患者跟进信息';

/*==============================================================*/
/* Table: ptt_patient_term                                      */
/*==============================================================*/
create table ptt_patient_term
(
   id                   bigint(20) auto_increment not null comment 'id',
   patient_id           bigint(20) comment '患者ID',
   term_id              bigint(20) comment '套餐ID',
   term_code            varchar(255) comment '套餐编码',
   term_name            varchar(255) comment '套餐名称',
   term_description     varchar(1000) comment '套餐描述',
   term_duration        int default 0 comment '套餐服务时间(分钟): 0-未设置',
   term_times           int comment '套餐次数',
   term_unit            varchar(50) comment '套餐单位',
   term_price           bigint(20) comment '套餐单价',
   term_amount          bigint(20) comment '套餐总价',
   times                int comment '剩余次数',
   amount               bigint(20) comment '实际支付',
   last_dept_id         bigint(20) comment '医院 id: 上一次预约的医院',
   status               varchar(255) comment '状态',
   create_by            varchar(255) comment '创建人',
   update_by            varchar(255) comment '修改人',
   create_time          timestamp default CURRENT_TIMESTAMP comment '创建时间',
   update_time          timestamp default CURRENT_TIMESTAMP comment '修改时间',
   remark               varchar(255) comment '备注',
   primary key (id)
);

alter table ptt_patient_term comment '患者套餐';

/*==============================================================*/
/* Table: ptt_patient_term_log                                  */
/*==============================================================*/
create table ptt_patient_term_log
(
   id                         bigint(20) auto_increment not null comment 'id',
   term_id                    bigint(20) comment '套餐ID',
   patient_term_id            bigint(29) not null comment '患者套餐 id',
   patient_term_reserve_id    bigint(20) comment '预约 id',
   before_times               int comment '变更前次数',
   after_times                int comment '变更后次数',
   content                    varchar(500) comment '内容',
   status                     varchar(255) comment '状态',
   create_by                  varchar(255) comment '创建人',
   update_by                  varchar(255) comment '修改人',
   create_time                timestamp default CURRENT_TIMESTAMP comment '创建时间',
   update_time                timestamp default CURRENT_TIMESTAMP comment '修改时间',
   remark                     varchar(255) comment '备注',
   primary key (id)
);

alter table ptt_patient_term_log comment '患者套餐日志';

/*==============================================================*/
/* Table: ptt_patient_term_reserve                              */
/*==============================================================*/
create table ptt_patient_term_reserve
(
    id                   bigint(20) auto_increment not null comment 'id',
    term_id              bigint(20) comment '套餐ID',
    patient_term_id      bigint(20) comment '患者套餐 id',
    dept_id              bigint(20) comment '医院 id',
    date                 varchar(10) comment '预约日期',
    begin_time           varchar(5) comment '开始时间',
    end_time             varchar(5) comment '结束时间',
    actual_begin_time    varchar(5) comment '实际开始时间',
    actual_end_time      varchar(5) comment '实际结束时间',
    status               varchar(255) comment '状态',
    create_by            varchar(255) comment '创建人',
    update_by            varchar(255) comment '修改人',
    create_time          timestamp default CURRENT_TIMESTAMP comment '创建时间',
    update_time          timestamp default CURRENT_TIMESTAMP comment '修改时间',
    remark               varchar(255) comment '备注',
    primary key (id)
);

alter table ptt_patient_term_reserve comment '患者套餐预约';

/*==============================================================*/
/* Table: ptt_patient_term_reserve_log                          */
/*==============================================================*/
create table ptt_patient_term_reserve_log
(
   id                         bigint(20) auto_increment not null comment 'id',
   patient_term_reserve_id    bigint(20) comment '患者套餐预约 id',
   before_status              varchar(255) comment '变更前状态',
   after_status               varchar(255) comment '变更后状态',
   content                    varchar(500) comment '变更内容',
   status                     varchar(255) comment '状态',
   create_by                  varchar(255) comment '创建人',
   update_by                  varchar(255) comment '修改人',
   create_time                timestamp default CURRENT_TIMESTAMP comment '创建时间',
   update_time                timestamp default CURRENT_TIMESTAMP comment '修改时间',
   remark                     varchar(255) comment '备注',
   primary key (id)
);

alter table ptt_patient_term_reserve_log comment '患者套餐预约日志';

/*==============================================================*/
/* Table: ptt_patient_term_reserve_resource                     */
/*==============================================================*/
create table ptt_patient_term_reserve_resource
(
   id                         bigint(20) auto_increment not null comment 'id',
   patient_term_reserve_id    bigint(20) comment '患者套餐预约 id',
   resource_id                bigint(20) comment '资源 id',
   status                     varchar(255) comment '状态',
   create_by                  varchar(255) comment '创建人',
   update_by                  varchar(255) comment '修改人',
   create_time                timestamp default CURRENT_TIMESTAMP comment '创建时间',
   update_time                timestamp default CURRENT_TIMESTAMP comment '修改时间',
   remark                     varchar(255) comment '备注',
   primary key (id)
);

alter table ptt_patient_term_reserve_resource comment '患者套餐资源';

/*==============================================================*/
/* Table: ptt_cus                                               */
/*==============================================================*/
create table ptt_cus
(
   id                   bigint(20) auto_increment not null comment 'id',
   dept_id              bigint(20) comment '医院 id',
   type                 varchar(255) comment '类型: text-文本, dict-字典',
   dict_id              bigint(20) comment '字典 id',
   title                varchar(255) comment '标题',
   cus_sort             int comment '排序',
   status               varchar(255) comment '状态',
   create_by            varchar(255) comment '创建人',
   update_by            varchar(255) comment '修改人',
   create_time          timestamp default CURRENT_TIMESTAMP comment '创建时间',
   update_time          timestamp default CURRENT_TIMESTAMP comment '修改时间',
   remark               varchar(255) comment '备注',
   primary key (id)
);

alter table ptt_cus comment '自定义信息';

/*==============================================================*/
/* Table: ptt_patient_cus                                       */
/*==============================================================*/
create table ptt_patient_cus
(
   id                   bigint(20) auto_increment not null comment 'id',
   cus_id               bigint(20) comment '自定义信息 id',
   dept_id              bigint(20) comment '医院 id',
   patient_id           bigint(20) comment '患者 id',
   type                 varchar(255) comment '类型: text-文本, dict-字典',
   dict_id              bigint(20) comment '字典 id',
   value                varchar(255) comment '内容',
   status               varchar(255) comment '状态',
   create_by            varchar(255) comment '创建人',
   update_by            varchar(255) comment '修改人',
   create_time          timestamp default CURRENT_TIMESTAMP comment '创建时间',
   update_time          timestamp default CURRENT_TIMESTAMP comment '修改时间',
   remark               varchar(255) comment '备注',
   primary key (id)
);

alter table ptt_patient_cus comment '患者自定义信息';

/*==============================================================*/
/* Table: ptt_his_log                                           */
/*==============================================================*/
create table ptt_his_log
(
   pat_item_id          bigint(20) auto_increment not null comment '项目购买ID',
   visit_id             bigint(20) comment '就诊ID',
   patient_id           bigint(20) comment '患者ID',
   name                 varchar(50) comment '患者姓名',
   mobile_phone         varchar(50) comment '患者电话',
   mrn                  varchar(100) comment '患者档案编号',
   visit_dept           varchar(255) comment '就诊科室',
   visit_date           timestamp comment '就诊日期',
   item_code            varchar(255) comment '项目编码',
   item_name            varchar(255) comment '项目名称',
   price                bigint(20) comment '单价',
   amount               bigint(20) comment '数量',
   unit                 varchar(255) comment '单位',
   costs                bigint(20) comment '应收金额',
   actual_costs         bigint(20) comment '实收金额',
   create_by            varchar(255) comment '创建人',
   update_by            varchar(255) comment '修改人',
   create_time          timestamp default CURRENT_TIMESTAMP comment '创建时间',
   update_time          timestamp default CURRENT_TIMESTAMP comment '修改时间',
   remark               varchar(255) comment '备注',
   primary key (pat_item_id)
);

alter table ptt_his_log comment 'HIS查询记录';




















