-- -------------------------------------------------------------
-- TablePlus 3.12.0(354)
--
-- https://tableplus.com/
--
-- Database: eladmin
-- Generation Time: 2020-12-30 18:50:28.8730
-- -------------------------------------------------------------


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
DROP TABLE IF EXISTS `code_column_config`;
CREATE TABLE `code_column_config` (
  `column_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `table_name` varchar(255) DEFAULT NULL,
  `column_name` varchar(255) DEFAULT NULL,
  `column_type` varchar(255) DEFAULT NULL,
  `dict_name` varchar(255) DEFAULT NULL,
  `extra` varchar(255) DEFAULT NULL,
  `form_show` bit(1) DEFAULT NULL,
  `form_type` varchar(255) DEFAULT NULL,
  `key_type` varchar(255) DEFAULT NULL,
  `list_show` bit(1) DEFAULT NULL,
  `not_null` bit(1) DEFAULT NULL,
  `query_type` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `date_annotation` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`column_id`) USING BTREE,
  KEY `idx_table_name` (`table_name`)
) ENGINE=InnoDB AUTO_INCREMENT=402 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='代码生成字段信息存储';

DROP TABLE IF EXISTS `code_gen_config`;
CREATE TABLE `code_gen_config` (
  `config_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `table_name` varchar(255) DEFAULT NULL COMMENT '表名',
  `author` varchar(255) DEFAULT NULL COMMENT '作者',
  `cover` bit(1) DEFAULT NULL COMMENT '是否覆盖',
  `module_name` varchar(255) DEFAULT NULL COMMENT '模块名称',
  `pack` varchar(255) DEFAULT NULL COMMENT '至于哪个包下',
  `path` varchar(255) DEFAULT NULL COMMENT '前端代码生成的路径',
  `api_path` varchar(255) DEFAULT NULL COMMENT '前端Api文件路径',
  `prefix` varchar(255) DEFAULT NULL COMMENT '表前缀',
  `api_alias` varchar(255) DEFAULT NULL COMMENT '接口名称',
  PRIMARY KEY (`config_id`) USING BTREE,
  KEY `idx_table_name` (`table_name`(100))
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='代码生成器配置';

DROP TABLE IF EXISTS `mnt_app`;
CREATE TABLE `mnt_app` (
  `app_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) DEFAULT NULL COMMENT '应用名称',
  `upload_path` varchar(255) DEFAULT NULL COMMENT '上传目录',
  `deploy_path` varchar(255) DEFAULT NULL COMMENT '部署路径',
  `backup_path` varchar(255) DEFAULT NULL COMMENT '备份路径',
  `port` int(255) DEFAULT NULL COMMENT '应用端口',
  `start_script` varchar(4000) DEFAULT NULL COMMENT '启动脚本',
  `deploy_script` varchar(4000) DEFAULT NULL COMMENT '部署脚本',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`app_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='应用管理';

DROP TABLE IF EXISTS `mnt_database`;
CREATE TABLE `mnt_database` (
  `db_id` varchar(50) NOT NULL COMMENT 'ID',
  `name` varchar(255) NOT NULL COMMENT '名称',
  `jdbc_url` varchar(255) NOT NULL COMMENT 'jdbc连接',
  `user_name` varchar(255) NOT NULL COMMENT '账号',
  `pwd` varchar(255) NOT NULL COMMENT '密码',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`db_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='数据库管理';

DROP TABLE IF EXISTS `mnt_deploy`;
CREATE TABLE `mnt_deploy` (
  `deploy_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `app_id` bigint(20) DEFAULT NULL COMMENT '应用编号',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`deploy_id`) USING BTREE,
  KEY `FK6sy157pseoxx4fmcqr1vnvvhy` (`app_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='部署管理';

DROP TABLE IF EXISTS `mnt_deploy_history`;
CREATE TABLE `mnt_deploy_history` (
  `history_id` varchar(50) NOT NULL COMMENT 'ID',
  `app_name` varchar(255) NOT NULL COMMENT '应用名称',
  `deploy_date` datetime NOT NULL COMMENT '部署日期',
  `deploy_user` varchar(50) NOT NULL COMMENT '部署用户',
  `ip` varchar(20) NOT NULL COMMENT '服务器IP',
  `deploy_id` bigint(20) DEFAULT NULL COMMENT '部署编号',
  PRIMARY KEY (`history_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='部署历史管理';

DROP TABLE IF EXISTS `mnt_deploy_server`;
CREATE TABLE `mnt_deploy_server` (
  `deploy_id` bigint(20) NOT NULL COMMENT '部署ID',
  `server_id` bigint(20) NOT NULL COMMENT '服务ID',
  PRIMARY KEY (`deploy_id`,`server_id`) USING BTREE,
  KEY `FKeaaha7jew9a02b3bk9ghols53` (`server_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='应用与服务器关联';

DROP TABLE IF EXISTS `mnt_server`;
CREATE TABLE `mnt_server` (
  `server_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `account` varchar(50) DEFAULT NULL COMMENT '账号',
  `ip` varchar(20) DEFAULT NULL COMMENT 'IP地址',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `port` int(11) DEFAULT NULL COMMENT '端口',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`server_id`) USING BTREE,
  KEY `idx_ip` (`ip`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='服务器管理';

DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `pid` bigint(20) DEFAULT NULL COMMENT '上级部门',
  `sub_count` int(5) DEFAULT '0' COMMENT '子部门数目',
  `level` int(5) DEFAULT NULL COMMENT '级别',
  `name` varchar(255) NOT NULL COMMENT '名称',
  `dept_sort` int(5) DEFAULT '999' COMMENT '排序',
  `enabled` bit(1) NOT NULL COMMENT '状态',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE,
  KEY `inx_pid` (`pid`),
  KEY `inx_enabled` (`enabled`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='部门';

DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `dict_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) NOT NULL COMMENT '字典名称',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dict_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='数据字典';

DROP TABLE IF EXISTS `sys_dict_detail`;
CREATE TABLE `sys_dict_detail` (
  `detail_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `dict_id` bigint(11) DEFAULT NULL COMMENT '字典id',
  `label` varchar(255) NOT NULL COMMENT '字典标签',
  `value` varchar(255) NOT NULL COMMENT '字典值',
  `dict_sort` int(5) DEFAULT NULL COMMENT '排序',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`detail_id`) USING BTREE,
  KEY `FK5tpkputc6d9nboxojdbgnpmyb` (`dict_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='数据字典详情';

DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job` (
  `job_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) NOT NULL COMMENT '岗位名称',
  `enabled` bit(1) NOT NULL COMMENT '岗位状态',
  `job_sort` int(5) DEFAULT NULL COMMENT '排序',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`job_id`) USING BTREE,
  UNIQUE KEY `uniq_name` (`name`),
  KEY `inx_enabled` (`enabled`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='岗位';

DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `description` varchar(255) DEFAULT NULL,
  `log_type` varchar(255) DEFAULT NULL,
  `method` varchar(255) DEFAULT NULL,
  `params` text,
  `request_ip` varchar(255) DEFAULT NULL,
  `time` bigint(20) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `browser` varchar(255) DEFAULT NULL,
  `exception_detail` text,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`log_id`) USING BTREE,
  KEY `log_create_time_index` (`create_time`),
  KEY `inx_log_type` (`log_type`)
) ENGINE=InnoDB AUTO_INCREMENT=6855 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统日志';

DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `pid` bigint(20) DEFAULT NULL COMMENT '上级菜单ID',
  `sub_count` int(5) DEFAULT '0' COMMENT '子菜单数目',
  `type` int(11) DEFAULT NULL COMMENT '菜单类型',
  `title` varchar(255) DEFAULT NULL COMMENT '菜单标题',
  `name` varchar(255) DEFAULT NULL COMMENT '组件名称',
  `component` varchar(255) DEFAULT NULL COMMENT '组件',
  `menu_sort` int(5) DEFAULT NULL COMMENT '排序',
  `icon` varchar(255) DEFAULT NULL COMMENT '图标',
  `path` varchar(255) DEFAULT NULL COMMENT '链接地址',
  `i_frame` bit(1) DEFAULT NULL COMMENT '是否外链',
  `cache` bit(1) DEFAULT b'0' COMMENT '缓存',
  `hidden` bit(1) DEFAULT b'0' COMMENT '隐藏',
  `permission` varchar(255) DEFAULT NULL COMMENT '权限',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`menu_id`) USING BTREE,
  UNIQUE KEY `uniq_title` (`title`),
  UNIQUE KEY `uniq_name` (`name`),
  KEY `inx_pid` (`pid`)
) ENGINE=InnoDB AUTO_INCREMENT=262 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统菜单';

DROP TABLE IF EXISTS `sys_quartz_job`;
CREATE TABLE `sys_quartz_job` (
  `job_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `bean_name` varchar(255) DEFAULT NULL COMMENT 'Spring Bean名称',
  `cron_expression` varchar(255) DEFAULT NULL COMMENT 'cron 表达式',
  `is_pause` bit(1) DEFAULT NULL COMMENT '状态：1暂停、0启用',
  `job_name` varchar(255) DEFAULT NULL COMMENT '任务名称',
  `method_name` varchar(255) DEFAULT NULL COMMENT '方法名称',
  `params` varchar(255) DEFAULT NULL COMMENT '参数',
  `description` varchar(255) DEFAULT NULL COMMENT '备注',
  `person_in_charge` varchar(100) DEFAULT NULL COMMENT '负责人',
  `email` varchar(100) DEFAULT NULL COMMENT '报警邮箱',
  `sub_task` varchar(100) DEFAULT NULL COMMENT '子任务ID',
  `pause_after_failure` bit(1) DEFAULT NULL COMMENT '任务失败后是否暂停',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`job_id`) USING BTREE,
  KEY `inx_is_pause` (`is_pause`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='定时任务';

DROP TABLE IF EXISTS `sys_quartz_log`;
CREATE TABLE `sys_quartz_log` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `bean_name` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `cron_expression` varchar(255) DEFAULT NULL,
  `exception_detail` text,
  `is_success` bit(1) DEFAULT NULL,
  `job_name` varchar(255) DEFAULT NULL,
  `method_name` varchar(255) DEFAULT NULL,
  `params` varchar(255) DEFAULT NULL,
  `time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='定时任务日志';

DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) NOT NULL COMMENT '名称',
  `level` int(255) DEFAULT NULL COMMENT '角色级别',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `data_scope` varchar(255) DEFAULT NULL COMMENT '数据权限',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`role_id`) USING BTREE,
  UNIQUE KEY `uniq_name` (`name`),
  KEY `role_name_index` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='角色表';

DROP TABLE IF EXISTS `sys_roles_depts`;
CREATE TABLE `sys_roles_depts` (
  `role_id` bigint(20) NOT NULL,
  `dept_id` bigint(20) NOT NULL,
  PRIMARY KEY (`role_id`,`dept_id`) USING BTREE,
  KEY `FK7qg6itn5ajdoa9h9o78v9ksur` (`dept_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='角色部门关联';

DROP TABLE IF EXISTS `sys_roles_menus`;
CREATE TABLE `sys_roles_menus` (
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`menu_id`,`role_id`) USING BTREE,
  KEY `FKcngg2qadojhi3a651a5adkvbq` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='角色菜单关联';

DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门名称',
  `username` varchar(255) DEFAULT NULL COMMENT '用户名',
  `nick_name` varchar(255) DEFAULT NULL COMMENT '昵称',
  `gender` varchar(2) DEFAULT NULL COMMENT '性别',
  `phone` varchar(255) DEFAULT NULL COMMENT '手机号码',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `avatar_name` varchar(255) DEFAULT NULL COMMENT '头像地址',
  `avatar_path` varchar(255) DEFAULT NULL COMMENT '头像真实路径',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `is_admin` bit(1) DEFAULT b'0' COMMENT '是否为admin账号',
  `enabled` bigint(20) DEFAULT NULL COMMENT '状态：1启用、0禁用',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新着',
  `pwd_reset_time` datetime DEFAULT NULL COMMENT '修改密码的时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE KEY `UK_kpubos9gc2cvtkb0thktkbkes` (`email`) USING BTREE,
  UNIQUE KEY `username` (`username`) USING BTREE,
  UNIQUE KEY `uniq_username` (`username`),
  UNIQUE KEY `uniq_email` (`email`),
  KEY `FK5rwmryny6jthaaxkogownknqp` (`dept_id`) USING BTREE,
  KEY `FKpq2dhypk2qgt68nauh2by22jb` (`avatar_name`) USING BTREE,
  KEY `inx_enabled` (`enabled`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统用户';

DROP TABLE IF EXISTS `sys_users_jobs`;
CREATE TABLE `sys_users_jobs` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `job_id` bigint(20) NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`,`job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sys_users_roles`;
CREATE TABLE `sys_users_roles` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`) USING BTREE,
  KEY `FKq4eq273l04bpu4efj0jd0jb98` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='用户角色关联';

DROP TABLE IF EXISTS `tool_alipay_config`;
CREATE TABLE `tool_alipay_config` (
  `config_id` bigint(20) NOT NULL COMMENT 'ID',
  `app_id` varchar(255) DEFAULT NULL COMMENT '应用ID',
  `charset` varchar(255) DEFAULT NULL COMMENT '编码',
  `format` varchar(255) DEFAULT NULL COMMENT '类型 固定格式json',
  `gateway_url` varchar(255) DEFAULT NULL COMMENT '网关地址',
  `notify_url` varchar(255) DEFAULT NULL COMMENT '异步回调',
  `private_key` text COMMENT '私钥',
  `public_key` text COMMENT '公钥',
  `return_url` varchar(255) DEFAULT NULL COMMENT '回调地址',
  `sign_type` varchar(255) DEFAULT NULL COMMENT '签名方式',
  `sys_service_provider_id` varchar(255) DEFAULT NULL COMMENT '商户号',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='支付宝配置类';

DROP TABLE IF EXISTS `tool_email_config`;
CREATE TABLE `tool_email_config` (
  `config_id` bigint(20) NOT NULL COMMENT 'ID',
  `from_user` varchar(255) DEFAULT NULL COMMENT '收件人',
  `host` varchar(255) DEFAULT NULL COMMENT '邮件服务器SMTP地址',
  `pass` varchar(255) DEFAULT NULL COMMENT '密码',
  `port` varchar(255) DEFAULT NULL COMMENT '端口',
  `user` varchar(255) DEFAULT NULL COMMENT '发件者用户名',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='邮箱配置';

DROP TABLE IF EXISTS `tool_local_storage`;
CREATE TABLE `tool_local_storage` (
  `storage_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `real_name` varchar(255) DEFAULT NULL COMMENT '文件真实的名称',
  `name` varchar(255) DEFAULT NULL COMMENT '文件名',
  `suffix` varchar(255) DEFAULT NULL COMMENT '后缀',
  `path` varchar(255) DEFAULT NULL COMMENT '路径',
  `type` varchar(255) DEFAULT NULL COMMENT '类型',
  `size` varchar(100) DEFAULT NULL COMMENT '大小',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`storage_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='本地存储';

DROP TABLE IF EXISTS `tool_picture`;
CREATE TABLE `tool_picture` (
  `picture_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `filename` varchar(255) DEFAULT NULL COMMENT '图片名称',
  `md5code` varchar(255) DEFAULT NULL COMMENT '文件的MD5值',
  `size` varchar(255) DEFAULT NULL COMMENT '图片大小',
  `url` varchar(255) DEFAULT NULL COMMENT '图片地址',
  `delete_url` varchar(255) DEFAULT NULL COMMENT '删除的URL',
  `height` varchar(255) DEFAULT NULL COMMENT '图片高度',
  `width` varchar(255) DEFAULT NULL COMMENT '图片宽度',
  `username` varchar(255) DEFAULT NULL COMMENT '用户名称',
  `create_time` datetime DEFAULT NULL COMMENT '上传日期',
  PRIMARY KEY (`picture_id`) USING BTREE,
  UNIQUE KEY `uniq_md5_code` (`md5code`),
  KEY `inx_url` (`url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='Sm.Ms图床';

DROP TABLE IF EXISTS `tool_qiniu_config`;
CREATE TABLE `tool_qiniu_config` (
  `config_id` bigint(20) NOT NULL COMMENT 'ID',
  `access_key` text COMMENT 'accessKey',
  `bucket` varchar(255) DEFAULT NULL COMMENT 'Bucket 识别符',
  `host` varchar(255) NOT NULL COMMENT '外链域名',
  `secret_key` text COMMENT 'secretKey',
  `type` varchar(255) DEFAULT NULL COMMENT '空间类型',
  `zone` varchar(255) DEFAULT NULL COMMENT '机房',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='七牛云配置';

DROP TABLE IF EXISTS `tool_qiniu_content`;
CREATE TABLE `tool_qiniu_content` (
  `content_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `bucket` varchar(255) DEFAULT NULL COMMENT 'Bucket 识别符',
  `name` varchar(255) DEFAULT NULL COMMENT '文件名称',
  `size` varchar(255) DEFAULT NULL COMMENT '文件大小',
  `type` varchar(255) DEFAULT NULL COMMENT '文件类型：私有或公开',
  `url` varchar(255) DEFAULT NULL COMMENT '文件url',
  `suffix` varchar(255) DEFAULT NULL COMMENT '文件后缀',
  `update_time` datetime DEFAULT NULL COMMENT '上传或同步的时间',
  PRIMARY KEY (`content_id`) USING BTREE,
  UNIQUE KEY `uniq_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='七牛云文件存储';

DROP TABLE IF EXISTS `yy_patient`;
CREATE TABLE `yy_patient` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(30) DEFAULT NULL COMMENT '外部系统ID',
  `mrn` varchar(20) NOT NULL COMMENT '档案号',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `phone` varchar(15) NOT NULL COMMENT '电话',
  `status` varchar(50) DEFAULT NULL COMMENT '状态',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='患者';

DROP TABLE IF EXISTS `yy_patient_term`;
CREATE TABLE `yy_patient_term` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `patient_id` bigint(20) NOT NULL COMMENT '患者 id',
  `term_code` varchar(50) NOT NULL COMMENT '原套餐编码',
  `term_name` varchar(200) NOT NULL COMMENT '原套餐名称',
  `term_price` bigint(20) DEFAULT '0' COMMENT '原套餐现在价格',
  `term_original_price` bigint(20) DEFAULT '0' COMMENT '原套餐原价',
  `term_times` int(11) DEFAULT '0' COMMENT '原套餐次数',
  `term_unit` varchar(50) DEFAULT NULL COMMENT '原套餐单位',
  `price` bigint(20) DEFAULT '0' COMMENT '购买价格',
  `times` int(11) DEFAULT '0' COMMENT '剩余次数',
  `status` varchar(50) DEFAULT NULL COMMENT '状态',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `update_time` timestamp NOT NULL COMMENT '修改时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COMMENT='患者套餐';

DROP TABLE IF EXISTS `yy_patient_term_log`;
CREATE TABLE `yy_patient_term_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `patient_term_id` bigint(20) NOT NULL COMMENT '患者套餐 id',
  `content` varchar(250) DEFAULT NULL COMMENT '内容',
  `type` varchar(250) DEFAULT NULL COMMENT '类型: 字段名称',
  `before` varchar(250) DEFAULT NULL COMMENT '修改前的值',
  `after` varchar(250) DEFAULT NULL COMMENT '修改后的值',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COMMENT='患者套餐日志';

DROP TABLE IF EXISTS `yy_reserve`;
CREATE TABLE `yy_reserve` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `dept_id` bigint(20) NOT NULL COMMENT '部门 id',
  `patient_id` bigint(20) NOT NULL COMMENT '患者 id',
  `term_id` bigint(20) NOT NULL COMMENT '套餐 id',
  `patient_term_id` bigint(20) NOT NULL COMMENT '患者套餐 id',
  `resource_group_id` bigint(20) DEFAULT NULL COMMENT '资源组 id: 可以为空, 非必选',
  `work_time_id` bigint(20) NOT NULL COMMENT 'id',
  `date` varchar(20) NOT NULL COMMENT '预约日期',
  `begin_time` varchar(10) DEFAULT NULL COMMENT '开始时间: 08:00',
  `end_time` varchar(10) DEFAULT NULL COMMENT '结束时间: 08:30',
  `status` varchar(50) DEFAULT NULL COMMENT '状态',
  `remark` varchar(250) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `update_time` timestamp NOT NULL COMMENT '修改时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COMMENT='预约';

DROP TABLE IF EXISTS `yy_reserve_log`;
CREATE TABLE `yy_reserve_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `reserve_id` bigint(20) NOT NULL COMMENT '预约 id',
  `content` varchar(250) DEFAULT NULL COMMENT '内容',
  `type` varchar(250) DEFAULT NULL COMMENT '类型: 字段名称',
  `before` varchar(250) DEFAULT NULL COMMENT '修改前的值',
  `after` varchar(250) DEFAULT NULL COMMENT '修改后的值',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COMMENT='预约日志';

DROP TABLE IF EXISTS `yy_reserve_resource`;
CREATE TABLE `yy_reserve_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `reserve_id` bigint(20) NOT NULL COMMENT '预约id',
  `resource_category_id` bigint(20) NOT NULL COMMENT '资源分类id',
  `resource_group_id` bigint(20) DEFAULT NULL COMMENT '资源分组id',
  `resource_id` bigint(20) DEFAULT NULL COMMENT '资源id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COMMENT='预约资源';

DROP TABLE IF EXISTS `yy_reserve_verify`;
CREATE TABLE `yy_reserve_verify` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `reserve_id` bigint(20) NOT NULL COMMENT 'id',
  `resource_group_id` bigint(20) NOT NULL COMMENT 'id',
  `status` varchar(50) DEFAULT NULL COMMENT '状态',
  `remark` varchar(250) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `update_time` timestamp NOT NULL COMMENT '修改时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='预约核销';

DROP TABLE IF EXISTS `yy_resource`;
CREATE TABLE `yy_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `dept_id` bigint(20) NOT NULL COMMENT '部门 id',
  `resource_category_id` bigint(20) NOT NULL COMMENT '分类 id',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `count` int(11) DEFAULT '1' COMMENT '数量',
  `status` varchar(50) DEFAULT NULL COMMENT '状态',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COMMENT='资源';

DROP TABLE IF EXISTS `yy_resource_category`;
CREATE TABLE `yy_resource_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `dept_id` bigint(20) NOT NULL COMMENT '部门 id',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `count` int(11) DEFAULT '0' COMMENT '资源总数',
  `status` varchar(50) DEFAULT NULL COMMENT '状态',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COMMENT='资源分类';

DROP TABLE IF EXISTS `yy_resource_group`;
CREATE TABLE `yy_resource_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `dept_id` bigint(20) NOT NULL COMMENT '部门 id',
  `name` varchar(200) NOT NULL COMMENT '名称',
  `status` varchar(50) DEFAULT NULL COMMENT '状态',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COMMENT='资源组';

DROP TABLE IF EXISTS `yy_resource_group_category`;
CREATE TABLE `yy_resource_group_category` (
  `resource_group_id` bigint(20) NOT NULL COMMENT '组 id',
  `resource_category_id` bigint(20) NOT NULL COMMENT '分类 id',
  PRIMARY KEY (`resource_group_id`,`resource_category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源组分类对应表';

DROP TABLE IF EXISTS `yy_sms`;
CREATE TABLE `yy_sms` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `bus_id` bigint(20) NOT NULL COMMENT '业务id',
  `bus_type` varchar(25) DEFAULT NULL COMMENT '业务类型',
  `mobile` varchar(15) DEFAULT NULL COMMENT '手机号',
  `content` varchar(255) DEFAULT NULL COMMENT '内容',
  `cell` varchar(15) DEFAULT NULL COMMENT '扩展号',
  `status` varchar(50) DEFAULT NULL COMMENT '状态',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `send_time` timestamp DEFAULT NULL COMMENT '发送时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `update_time` timestamp DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COMMENT='短信发送';

DROP TABLE IF EXISTS `yy_term`;
CREATE TABLE `yy_term` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `dept_id` bigint(20) NOT NULL COMMENT '部门 id',
  `code` varchar(50) NOT NULL COMMENT '编码',
  `name` varchar(200) NOT NULL COMMENT '名称',
  `price` bigint(20) DEFAULT '0' COMMENT '现在价格',
  `original_price` bigint(20) DEFAULT '0' COMMENT '原价',
  `times` int(11) DEFAULT '0' COMMENT '次数',
  `unit` varchar(50) DEFAULT NULL COMMENT '单位',
  `status` varchar(50) DEFAULT NULL COMMENT '状态',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COMMENT='套餐';

DROP TABLE IF EXISTS `yy_term_resource_group`;
CREATE TABLE `yy_term_resource_group` (
  `term_id` bigint(20) NOT NULL COMMENT '套餐 id',
  `resource_group_id` bigint(20) NOT NULL COMMENT '资源组 id',
  PRIMARY KEY (`term_id`,`resource_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='套餐资源组对应表';

DROP TABLE IF EXISTS `yy_work_time`;
CREATE TABLE `yy_work_time` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `dept_id` bigint(20) NOT NULL COMMENT '部门 id',
  `begin_time` varchar(10) DEFAULT NULL COMMENT '开始时间: 08:00',
  `end_time` varchar(10) DEFAULT NULL COMMENT '结束时间: 08:30',
  `status` varchar(50) DEFAULT NULL COMMENT '状态',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COMMENT='工作时间';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;