
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
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, '美团', 'meituan', 1, 'admin', 'admin', now(), now() from sys_dict where name = 'patient_source';

-- 增加 patient_status 字典
insert into sys_dict (create_by, create_time, update_by, update_time, description, name) values ('admin', '03/18/2021 17:15:14.976', 'admin', '03/18/2021 17:15:14.976', '患者状态', 'patient_status');
insert into sys_dict_detail (create_by, create_time, update_by, update_time, dict_id, dict_sort, label, value) values ('admin', '03/18/2021 17:16:53.555', 'admin', '03/18/2021 17:16:53.555', 13, 1, '有效', '1');
insert into sys_dict_detail (create_by, create_time, update_by, update_time, dict_id, dict_sort, label, value) values ('admin', '03/18/2021 17:17:03.745', 'admin', '03/18/2021 17:17:03.745', 13, 2, '无效', '0');


-- 修改 resource_group 表
alter table yy_resource_group add com_id bigint(20) DEFAULT NULL comment '公司 ID' after id;
alter table yy_resource_group add org_id bigint(20) DEFAULT NULL comment '组织 ID' after id;
alter table yy_resource_group modify dept_id bigint(20) DEFAULT NULL COMMENT '部门 ID';

update yy_resource_group a
    inner join yy_resource_group b on a.id = b.id
    set a.org_id = 21, a.com_id = b.dept_id, a.status = 1;

update yy_resource_group set dept_id = null;

--
insert into sys_dict (name, description, create_by, update_by, create_time, update_time) values('resouce_group_status', '资源组状态', 'admin', 'admin', now(), now());
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, '有效', '1', 1, 'admin', 'admin', now(), now() from sys_dict where name = 'resouce_group_status';
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, '无效', '0', 2, 'admin', 'admin', now(), now() from sys_dict where name = 'resouce_group_status';


-- 修改 resource_category 表
alter table yy_resource_category add com_id bigint(20) DEFAULT NULL comment '公司 ID' after id;
alter table yy_resource_category add org_id bigint(20) DEFAULT NULL comment '组织 ID' after id;
alter table yy_resource_category modify dept_id bigint(20) DEFAULT NULL COMMENT '部门 ID';

update yy_resource_category a
    inner join yy_resource_category b on a.id = b.id
    set a.org_id = 21, a.com_id = b.dept_id, a.status = 1;

update yy_resource_category set dept_id = null;

--
insert into sys_dict (name, description, create_by, update_by, create_time, update_time) values('resouce_category_status', '资源分类状态', 'admin', 'admin', now(), now());
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, '有效', '1', 1, 'admin', 'admin', now(), now() from sys_dict where name = 'resouce_category_status';
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, '无效', '0', 2, 'admin', 'admin', now(), now() from sys_dict where name = 'resouce_category_status';


-- 修改 resource 表
alter table yy_resource add com_id bigint(20) DEFAULT NULL comment '公司 ID' after id;
alter table yy_resource add org_id bigint(20) DEFAULT NULL comment '组织 ID' after id;
alter table yy_resource modify dept_id bigint(20) DEFAULT NULL COMMENT '部门 ID';

update yy_resource a
    inner join yy_resource b on a.id = b.id
    set a.org_id = 21, a.com_id = b.dept_id, a.status = 1;

update yy_resource set dept_id = null;

--
insert into sys_dict (name, description, create_by, update_by, create_time, update_time) values('resouce_status', '资源状态', 'admin', 'admin', now(), now());
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, '有效', '1', 1, 'admin', 'admin', now(), now() from sys_dict where name = 'resouce_status';
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, '无效', '0', 2, 'admin', 'admin', now(), now() from sys_dict where name = 'resouce_status';


-- 修改 term 表
alter table yy_term add com_id bigint(20) DEFAULT NULL comment '公司 ID' after id;
alter table yy_term add org_id bigint(20) DEFAULT NULL comment '组织 ID' after id;
alter table yy_term modify dept_id bigint(20) DEFAULT NULL COMMENT '部门 ID';

update yy_term a
    inner join yy_term b on a.id = b.id
    set a.org_id = 21, a.com_id = b.dept_id, a.status = 1;

update yy_term set dept_id = null;

insert into sys_dict (name, description, create_by, update_by, create_time, update_time) values('term_status', '套餐状态', 'admin', 'admin', now(), now());
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, '有效', '1', 1, 'admin', 'admin', now(), now() from sys_dict where name = 'term_status';
insert into sys_dict_detail (dict_id, label, value, dict_sort, create_by, update_by, create_time, update_time) select dict_id, '无效', '0', 2, 'admin', 'admin', now(), now() from sys_dict where name = 'term_status';











