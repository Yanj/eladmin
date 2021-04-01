
-- 修改 patient 表
alter table yy_patient add dept_id bigint(20) DEFAULT NULL comment '部门 ID' after id;
alter table yy_patient add com_id bigint(20) DEFAULT NULL comment '公司 ID' after id;
alter table yy_patient add org_id bigint(20) DEFAULT NULL comment '组织 ID' after id;
alter table yy_patient add source varchar(50) DEFAULT NULL COMMENT '来源' after dept_id;
alter table yy_patient add col5 varchar(255) DEFAULT NULL COMMENT '自定义 5' after phone;
alter table yy_patient add col4 varchar(255) DEFAULT NULL COMMENT '自定义 4' after phone;
alter table yy_patient add col3 varchar(255) DEFAULT NULL COMMENT '自定义 3' after phone;
alter table yy_patient add col2 varchar(255) DEFAULT NULL COMMENT '自定义 2' after phone;
alter table yy_patient add col1 varchar(255) DEFAULT NULL COMMENT '自定义 1' after phone;

update yy_patient set org_id = 21, com_id = 32, status = 1, source = 'HIS';

alter table yy_patient modify mrn varchar(20) DEFAULT NULL COMMENT '档案号';

-- 增加 patient_source 字典
insert into sys_dict (name, description, create_by, update_by, create_time, update_time) values('patient_source', '患者来源', 'admin', 'admin', now(), now());
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, 'HIS', 'HIS', 1, 'admin', 'admin', now(), now() from sys_dict where name = 'patient_source';
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, '美团', 'MEITUAN', 1,
                                                                                                                      'admin', 'admin', now(), now() from sys_dict where name = 'patient_source';

-- 增加 patient_status 字典
insert into sys_dict (create_by, create_time, update_by, update_time, description, name) values ('admin', now(), 'admin', now(), '患者状态', 'patient_status');
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, '有效', '1', 1, 'admin', 'admin', now(), now() from sys_dict where name = 'patient_status';
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, '无效', '0', 2, 'admin', 'admin', now(), now() from sys_dict where name = 'patient_status';


-- 修改 resource_group 表
alter table yy_resource_group add com_id bigint(20) DEFAULT NULL comment '公司 ID' after id;
alter table yy_resource_group add org_id bigint(20) DEFAULT NULL comment '组织 ID' after id;
alter table yy_resource_group modify dept_id bigint(20) DEFAULT NULL COMMENT '部门 ID';
alter table yy_resource_group add sort int default null comment '排序' after name;

update yy_resource_group a
    inner join yy_resource_group b on a.id = b.id
    set a.org_id = 21, a.com_id = b.dept_id, a.status = 1;

update yy_resource_group set dept_id = null;

--
insert into sys_dict (name, description, create_by, update_by, create_time, update_time) values('resource_group_status', '资源组状态', 'admin', 'admin', now(), now());
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, '有效', '1', 1, 'admin', 'admin', now(), now() from sys_dict where name = 'resource_group_status';
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, '无效', '0', 2, 'admin', 'admin', now(), now() from sys_dict where name = 'resource_group_status';


-- 修改 resource_category 表
alter table yy_resource_category add com_id bigint(20) DEFAULT NULL comment '公司 ID' after id;
alter table yy_resource_category add org_id bigint(20) DEFAULT NULL comment '组织 ID' after id;
alter table yy_resource_category modify dept_id bigint(20) DEFAULT NULL COMMENT '部门 ID';

update yy_resource_category a
    inner join yy_resource_category b on a.id = b.id
    set a.org_id = 21, a.com_id = b.dept_id, a.status = 1;

update yy_resource_category set dept_id = null;

--
insert into sys_dict (name, description, create_by, update_by, create_time, update_time) values('resource_category_status', '资源分类状态', 'admin', 'admin', now(), now());
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, '有效', '1', 1, 'admin', 'admin', now(), now() from sys_dict where name = 'resource_category_status';
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, '无效', '0', 2, 'admin', 'admin', now(), now() from sys_dict where name = 'resource_category_status';


-- 修改 resource 表
alter table yy_resource add com_id bigint(20) DEFAULT NULL comment '公司 ID' after id;
alter table yy_resource add org_id bigint(20) DEFAULT NULL comment '组织 ID' after id;
alter table yy_resource modify dept_id bigint(20) DEFAULT NULL COMMENT '部门 ID';

update yy_resource a
    inner join yy_resource b on a.id = b.id
    set a.org_id = 21, a.com_id = b.dept_id, a.status = 1;

update yy_resource set dept_id = null;

--
insert into sys_dict (name, description, create_by, update_by, create_time, update_time) values('resource_status', '资源状态', 'admin', 'admin', now(), now());
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, '有效', '1', 1, 'admin', 'admin', now(), now() from sys_dict where name = 'resource_status';
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, '无效', '0', 2, 'admin', 'admin', now(), now() from sys_dict where name = 'resource_status';


-- 修改 term 表
alter table yy_term add com_id bigint(20) DEFAULT NULL comment '公司 ID' after id;
alter table yy_term add org_id bigint(20) DEFAULT NULL comment '组织 ID' after id;
alter table yy_term modify dept_id bigint(20) DEFAULT NULL COMMENT '部门 ID';
alter table yy_term add duration int default null comment '时长' after unit;
alter table yy_term add operator_count int default null comment '操作员数量' after duration;
alter table yy_term add term_sort int default null comment '套餐排序' after operator_count;

update yy_term a inner join yy_term b on a.id = b.id set a.org_id = 21, a.com_id = b.dept_id, a.status = 1;
update yy_term set dept_id = null;
update yy_term set duration = 30;
update yy_term set operator_count = 1;

insert into sys_dict (name, description, create_by, update_by, create_time, update_time) values('term_status', '套餐状态', 'admin', 'admin', now(), now());
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, '有效', '1', 1, 'admin', 'admin', now(), now() from sys_dict where name = 'term_status';
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, '无效', '0', 2, 'admin', 'admin', now(), now() from sys_dict where name = 'term_status';



-- 修改 yy_reserve 表
alter table yy_reserve add com_id bigint(20) DEFAULT NULL comment '公司 ID' after id;
alter table yy_reserve add org_id bigint(20) DEFAULT NULL comment '组织 ID' after id;
alter table yy_reserve modify dept_id bigint(20) DEFAULT NULL COMMENT '部门 ID';
alter table yy_reserve add verify_status varchar(50) default null comment '核销状态' after end_time;

update yy_reserve a inner join yy_reserve b on a.id = b.id set a.org_id = 21, a.com_id = b.dept_id;
update yy_reserve set dept_id = null;
update yy_reserve a inner join yy_reserve b on a.id = b.id set a.verify_status = b.status;
update yy_reserve set status = '1';

-- 修改 yy_patient_term 表
alter table yy_patient_term add dept_id bigint(20) DEFAULT NULL COMMENT '部门 ID' after id;
alter table yy_patient_term add com_id bigint(20) DEFAULT NULL comment '公司 ID' after id;
alter table yy_patient_term add org_id bigint(20) DEFAULT NULL comment '组织 ID' after id;
alter table yy_patient_term add `type` varchar(50) DEFAULT NULL comment '患者套餐类型' after pat_item_id;
alter table yy_patient_term add term_id bigint(20) DEFAULT NULL comment '套餐 ID' after patient_id;
alter table yy_patient_term add total_times int default null comment '总次数' after price;
alter table yy_patient_term add term_duration int default null comment '套餐时长' after term_unit;
alter table yy_patient_term add duration int default null comment '时长' after free_times;
alter table yy_patient_term add term_operator_count int default null comment '套餐操作员数量' after term_duration;
alter table yy_patient_term add operator_count int default null comment '操作员数量' after duration;

update yy_patient_term a inner join yy_term b on a.term_code = b.code set a.term_id = b.id;
update yy_patient_term set org_id = 21, com_id = 32, status = 1;
update yy_patient_term set type = 'his' where pid is null and remark is null;
update yy_patient_term set type = 'free' where pid is null and remark is not null;
update yy_patient_term a inner join yy_patient_term b on a.id = b.id set a.total_times = b.times;
update yy_patient_term set term_duration = 30, duration = 30;
update yy_patient_term set term_operator_count = 1, operator_count = 1;

update yy_patient_term t1
inner join(
  select pt.id, pt.total_times, pt.times, count(1) count
  from yy_patient_term pt
  inner join yy_reserve r on pt.id = r.patient_term_id and r.verify_status <> 'canceled'
  group by pt.id
) t2 on t1.id = t2.id
set t1.total_times = t2.total_times + t2.count;

insert into sys_dict (name, description, create_by, update_by, create_time, update_time) values('patient_term_status', '患者套餐状态', 'admin', 'admin', now(), now());
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, '有效', '1', 1, 'admin', 'admin', now(), now() from sys_dict where name = 'patient_term_status';
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, '无效', '0', 2, 'admin', 'admin', now(), now() from sys_dict where name = 'patient_term_status';

insert into sys_dict (name, description, create_by, update_by, create_time, update_time) values('patient_term_type', '患者套餐类型', 'admin', 'admin', now(), now());
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, 'HIS', 'his', 1, 'admin', 'admin', now(), now() from sys_dict where name = 'patient_term_type';
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, '免费', 'free', 2, 'admin', 'admin', now(), now() from sys_dict where name = 'patient_term_type';


-- 修改 work_time 表
alter table yy_work_time add com_id bigint(20) default null comment '公司ID' after id;
alter table yy_work_time add org_id bigint(20) default null comment '组织ID' after id;
alter table yy_work_time modify dept_id bigint(20) default null comment '部门ID';
alter table yy_work_time add duration int default null comment '时间' after end_time;

update yy_work_time a inner join yy_work_time b on a.id = b.id set a.org_id = 21, a.com_id = b.dept_id;
update yy_work_time set dept_id = null;
update yy_work_time set status = 1;
update yy_work_time a inner join (
    select id,cast((time_to_sec(timediff(end_time, begin_time)) / 60) as signed) as duration from yy_work_time
    ) b on a.id = b.id set a.duration = b.duration;

insert into sys_dict (name, description, create_by, update_by, create_time, update_time) values('work_time_status', '工作时间状态', 'admin', 'admin', now(), now());
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, '有效', '1', 1, 'admin', 'admin', now(), now() from sys_dict where name = 'work_time_status';
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, '无效', '0', 2, 'admin', 'admin', now(), now() from sys_dict where name = 'work_time_status';


-- 修改 reserve_resource表
alter table yy_reserve_resource add work_time_id bigint(20) default null comment '时段ID' after reserve_id;
alter table yy_reserve_resource add date varchar(20) default null comment '预约日期' after reserve_id;
alter table yy_reserve_resource add status varchar(50) default null comment '状态';

update yy_reserve_resource a inner join yy_reserve b on a.reserve_id = b.id set a.date = b.date, a.work_time_id = b.work_time_id;
update yy_reserve_resource set status = 1;

-- 新增操作员表
create table yy_reserve_operator(
                                    reserve_id bigint(20) not null comment '预约ID',
                                    user_id bigint(20) not null comment '用户ID',
                                    primary key(reserve_id, user_id)
);
alter table yy_reserve_operator comment '预约操作员表';

insert into yy_reserve_operator select id, operator_id from yy_reserve where operator_id is not null;


-- 操作字典表
delete from sys_dict_detail where dict_id in (select dict_id from sys_dict where name = 'reserve_status');
delete from sys_dict where name = 'reserve_status';

insert into sys_dict (name, description, create_by, update_by, create_time, update_time) values('reserve_status', '预约状态', 'admin', 'admin', now(), now());
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, '有效', '1', 1, 'admin', 'admin', now(), now() from sys_dict where name = 'reserve_status';
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, '无效', '0', 2, 'admin', 'admin', now(), now() from sys_dict where name = 'reserve_status';

insert into sys_dict (name, description, create_by, update_by, create_time, update_time) values('reserve_verify_status', '预约核销状态', 'admin', 'admin', now(), now());
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, '已预约', 'init', 1, 'admin', 'admin', now(), now() from sys_dict where name = 'reserve_verify_status';
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, '已签到', 'check_in', 2, 'admin', 'admin', now(), now() from sys_dict where name = 'reserve_verify_status';
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, '已核销', 'verified', 3, 'admin', 'admin', now(), now() from sys_dict where name = 'reserve_verify_status';
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, '已取消', 'canceled', 4, 'admin', 'admin', now(), now() from sys_dict where name = 'reserve_verify_status';



update sys_menu set name = 'Hospital', component = 'patientReserve/hospital/index' where menu_id = 192;

update sys_menu set name = 'Resource', component = 'patientReserve/resource/index' where menu_id = 200;

update sys_menu set name = 'Term', component = 'patientReserve/term/index' where menu_id = 204;

update sys_menu set name = 'Patient', component = 'patientReserve/patient/index' where menu_id = 212;

update sys_menu set name = 'PatientTerm', component = 'patientReserve/patientTerm/index' where menu_id = 224;

update sys_menu set name = 'Reserve', component = 'patientReserve/reserve/index' where menu_id = 232;

update sys_menu set name = 'WorkTime', component = 'patientReserve/workTime/index' where menu_id = 249;

update sys_menu set name = 'ResourceCategory', component = 'patientReserve/resourceCategory/index' where menu_id = 253;

update sys_menu set name = 'ResourceGroup', component = 'patientReserve/resourceGroup/index' where menu_id = 257;

update sys_menu set name = 'WorkTimeReserveList', component = 'statistics/workTimeReserveList/index', path = 'workTimeReserveList', permission = 'reserve:list' where menu_id = 261;

update sys_menu set name = 'ReservePanel', component = 'patientReserve/reservePanel/index', path = 'reservePanel', permission = 'reserve:list' where menu_id = 262;

insert into sys_menu (menu_id, pid, sub_count,type,title,name,component,menu_sort,icon,path,i_frame,cache,hidden,permission,create_by, update_by,create_time, update_time)
values(274, 191,0,1,'员工工作量','UserWorkCount','statistics/userWorkCount/index',210,null,'userWorkCount',0,1,0,'patientTerm:list','admin','admin',now(),now());

insert into sys_menu (menu_id, pid, sub_count,type,title,name,component,menu_sort,icon,path,i_frame,cache,hidden,permission,create_by, update_by,create_time, update_time)
values(275, 191,0,1,'患者套餐用量','PatientTermTimesCount','statistics/patientTermTimesCount/index',220,null,'patientTermTimesCount',0,1,0,'patientTerm:list','admin','admin',now(),now());

insert into sys_roles_menus(role_id, menu_id) values(1, 274),(2,274),(3, 274), (4, 274), (5, 274), (6, 274);
insert into sys_roles_menus(role_id, menu_id) values(1, 275),(2,275),(3, 275), (4, 275), (5, 275), (6, 275);

