

-- 短信发送表
drop table if exists yy_sms;
create table if not exists yy_sms
(
    id            bigint(20) not null AUTO_INCREMENT comment 'id',
    bus_id        bigint(20) not null comment '业务id',
    bus_type      varchar(25) comment '业务类型',
    mobile        varchar(15) comment '手机号',
    content       varchar(255) comment '内容',
    cell          varchar(15) comment '扩展号',
    status        varchar(50) comment '状态',
    remark        varchar(255) comment '备注',
    send_time     timestamp comment '发送时间',
    create_time   timestamp default CURRENT_TIMESTAMP comment '创建时间',
    create_by     varchar(255) comment '创建人',
    update_time   timestamp default CURRENT_TIMESTAMP comment '修改时间',
    update_by     varchar(255) comment '修改人',
    primary key(id)
    );
alter table yy_sms comment '短信发送';


-- 工作时间表
drop table if exists yy_work_time;
create table if not exists yy_work_time
(
    id            bigint(20) not null AUTO_INCREMENT comment 'id',
    dept_id       bigint(20) not null comment '部门 id',
    begin_time    varchar(10) comment '开始时间: 08:00',
    end_time      varchar(10) comment '结束时间: 08:30',
    status        varchar(50) comment '状态',
    remark        varchar(255) comment '备注',
    primary key(id)
    );
alter table yy_work_time comment '工作时间';

-- 套餐表
drop table if exists yy_term;
create table if not exists yy_term
(
    id                bigint(20) not null AUTO_INCREMENT comment 'id',
    dept_id           bigint(20) not null comment '部门 id',
    code              varchar(50) not null comment '编码',
    name              varchar(200) not null comment '名称',
    price             bigint(20) default 0 comment '现在价格',
    original_price    bigint(20) default 0 comment '原价',
    times             int default 0 comment '次数',
    unit              varchar(50) comment '单位',
    status            varchar(50) comment '状态',
    remark        varchar(255) comment '备注',
    primary key(id)
    );
alter table yy_term comment '套餐';

-- 套餐资源组对应表
drop table if exists yy_term_resource_group;
create table if not exists yy_term_resource_group
(
    term_id           bigint(20) not null comment '套餐 id',
    resource_group_id bigint(20) not null comment '资源组 id',
    primary key(term_id, resource_group_id)
    );
alter table yy_term_resource_group comment '套餐资源组对应表';

-- 资源组
drop table if exists yy_resource_group;
create table if not exists yy_resource_group
(
    id        bigint(20) not null AUTO_INCREMENT comment 'id',
    dept_id   bigint(20) not null comment '部门 id',
    name      varchar(200) not null comment '名称',
    status    varchar(50) comment '状态',
    remark    varchar(255) comment '备注',
    primary key(id)
    );
alter table yy_resource_group comment '资源组';

-- 资源分类
drop table if exists yy_resource_category;
create table if not exists yy_resource_category
(
    id      bigint(20) not null AUTO_INCREMENT comment 'id',
    dept_id bigint(20) not null comment '部门 id',name   varchar(50) not null comment '名称',
    `count` int default 0 comment '资源总数',
    status  varchar(50) comment '状态',
    remark  varchar(255) comment '备注',
    primary key(id)
    );
alter table yy_resource_category comment '资源分类';

-- 资源组分类对应表
drop table if exists yy_resource_group_category;
create table if not exists yy_resource_group_category
(
    resource_group_id    bigint(20) not null comment '组 id',
    resource_category_id bigint(20) not null comment '分类 id',
    primary key(resource_group_id, resource_category_id)
    );
alter table yy_resource_group_category comment '资源组分类对应表';

-- 资源
drop table if exists yy_resource;
create table if not exists yy_resource
(
    id                   bigint(20) not null AUTO_INCREMENT comment 'id',
    dept_id              bigint(20) not null comment '部门 id',
    resource_category_id bigint(20) not null comment '分类 id',
    name                 varchar(50) not null comment '名称',
    count                int default 1 comment '数量',
    status               varchar(50) comment '状态',
    remark               varchar(255) comment '备注',
    primary key(id)
    );
alter table yy_resource comment '资源';

-- 患者
drop table if exists yy_patient;
create table if not exists yy_patient
(
    id          bigint(20) not null AUTO_INCREMENT comment 'id',
    code        varchar(30) comment '外部系统ID',
    mrn         varchar(20) not null comment '档案号',
    name        varchar(50) not null comment '名称',
    phone       varchar(15) not null comment '电话',
    status      varchar(50) comment '状态',
    remark      varchar(255) comment '备注',
    primary key(id)
    );
alter table yy_patient comment '患者';

-- 患者套餐
drop table if exists yy_patient_term;
create table if not exists yy_patient_term
(
    id                  bigint(20) not null AUTO_INCREMENT comment 'id',
    patient_id          bigint(20) not null comment '患者 id',
    term_code           varchar(50) not null comment '原套餐编码',
    term_name           varchar(200) not null comment '原套餐名称',
    term_price          bigint(20) default 0 comment '原套餐现在价格',
    term_original_price bigint(20) default 0 comment '原套餐原价',
    term_times          int default 0 comment '原套餐次数',
    term_unit           varchar(50) comment '原套餐单位',
    price               bigint(20) default 0 comment '购买价格',
    times               int default 0 comment '剩余次数',
    status              varchar(50) comment '状态',
    remark              varchar(255) comment '备注',
    create_time         timestamp default CURRENT_TIMESTAMP comment '创建时间',
    create_by           varchar(255) comment '创建人',
    update_time         timestamp default CURRENT_TIMESTAMP comment '修改时间',
    update_by           varchar(255) comment '修改人',
    primary key(id)
    );
alter table yy_patient_term comment '患者套餐';

-- 患者套餐日志
drop table if exists yy_patient_term_log;
create table if not exists yy_patient_term_log
(
    id              bigint(20) not null AUTO_INCREMENT comment 'id',
    patient_term_id bigint(20) not null comment '患者套餐 id',
    content         varchar(250) comment '内容',
    type            varchar(250) comment '类型: 字段名称',
    `before`        varchar(250) comment '修改前的值',
    `after`         varchar(250) comment '修改后的值',
    create_time     timestamp default CURRENT_TIMESTAMP comment '创建时间',
    create_by       varchar(255) comment '创建人',
    primary key(id)
    );
alter table yy_patient_term_log comment '患者套餐日志';

-- 预约
drop table if exists yy_reserve;
create table if not exists yy_reserve
(
    id                bigint(20) not null AUTO_INCREMENT comment 'id',
    dept_id           bigint(20) not null comment '部门 id',
    patient_id        bigint(20) not null comment '患者 id',
    term_id           bigint(20) not null comment '套餐 id',
    patient_term_id   bigint(20) not null comment '患者套餐 id',
    resource_group_id bigint(20) comment '资源组 id: 可以为空, 非必选',
    work_time_id      bigint(20) not null comment 'id',
    date              varchar(20) not null comment '预约日期',
    begin_time    varchar(10) comment '开始时间: 08:00',
    end_time      varchar(10) comment '结束时间: 08:30',
    status            varchar(50) comment '状态',
    remark            varchar(250) comment '备注',
    create_time       timestamp default CURRENT_TIMESTAMP comment '创建时间',
    create_by         varchar(255) comment '创建人',
    update_time       timestamp default CURRENT_TIMESTAMP comment '修改时间',
    update_by         varchar(255) comment '修改人',
    primary key(id)
    );
alter table yy_reserve comment '预约';

-- 预约日志
drop table if exists yy_reserve_log;
create table if not exists yy_reserve_log
(
    id         bigint(20) not null AUTO_INCREMENT comment 'id',
    reserve_id bigint(20) not null comment '预约 id',
    content    varchar(250) comment '内容',
    type       varchar(250) comment '类型: 字段名称',
    `before`   varchar(250) comment '修改前的值',
    `after`    varchar(250) comment '修改后的值',
    create_time timestamp default CURRENT_TIMESTAMP comment '创建时间',
    create_by varchar(255) comment '创建人',
    primary key(id)
    );
alter table yy_reserve_log comment '预约日志';

-- 预约核销
drop table if exists yy_reserve_verify;
create table if not exists yy_reserve_verify
(
    id                bigint(20) not null AUTO_INCREMENT comment 'id',
    reserve_id        bigint(20) not null comment 'id',
    resource_group_id bigint(20) not null comment 'id',
    status            varchar(50) comment '状态',
    remark            varchar(250) comment '备注',
    create_time       timestamp default CURRENT_TIMESTAMP comment '创建时间',
    create_by         varchar(255) comment '创建人',
    update_time       timestamp default CURRENT_TIMESTAMP comment '修改时间',
    update_by         varchar(255) comment '修改人',
    primary key(id)
    );
alter table yy_reserve_verify comment '预约核销';

-- 预约核销资源
drop table if exists yy_reserve_resource;
create table if not exists yy_reserve_resource
(
    reserve_verify_id bigint(20) not null comment 'id',
    resource_id       bigint(20) not null comment 'id',
    primary key(reserve_verify_id, resource_id)
    );
alter table yy_reserve_resource comment '预约核销资源';

-- 预约资源
drop table if exists yy_reserve_resource;
create table if not exists yy_reserve_resource
(
    reserve_id           bigint(20) not null comment '预约id',
    resource_group_id    bigint(20) not null comment '资源分组id',
    resource_category_id bigint(20) not null comment '资源分类id',
    resource_id          bigint(20) comment '资源id',
    primary key(reserve_id, resource_group_id, resource_category_id)
    );
alter table yy_reserve_resource comment '预约核销资源';

