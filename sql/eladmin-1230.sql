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
  `send_time` timestamp NOT NULL COMMENT '发送时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `update_time` timestamp NOT NULL COMMENT '修改时间',
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

INSERT INTO `code_column_config` (`column_id`, `table_name`, `column_name`, `column_type`, `dict_name`, `extra`, `form_show`, `form_type`, `key_type`, `list_show`, `not_null`, `query_type`, `remark`, `date_annotation`) VALUES
('187', 'rcv_user_notification', 'id', 'bigint', NULL, '', b'1', NULL, 'PRI', b'1', b'1', NULL, 'id', NULL),
('188', 'rcv_user_notification', 'user_id', 'bigint', NULL, '', b'1', '', '', b'1', b'0', NULL, '用户id', NULL),
('189', 'rcv_user_notification', 'org_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '机构id', NULL),
('190', 'rcv_user_notification', 'dept_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '部门id', NULL),
('191', 'rcv_user_notification', 'content', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '内容', NULL),
('192', 'rcv_user_notification', 'status', 'int', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '状态', NULL),
('193', 'rcv_user_notification', 'create_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '创建人', NULL),
('194', 'rcv_user_notification', 'update_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '修改人', NULL),
('195', 'rcv_user_notification', 'create_time', 'timestamp', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '创建时间', NULL),
('196', 'rcv_user_notification', 'update_time', 'timestamp', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '修改时间', NULL),
('197', 'rcv_user_notification', 'remark', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '备注', NULL),
('198', 'rcv_user_item', 'id', 'bigint', NULL, '', b'1', NULL, 'PRI', b'1', b'1', NULL, 'id', NULL),
('199', 'rcv_user_item', 'user_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '用户ID', NULL),
('200', 'rcv_user_item', 'item_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '套餐ID', NULL),
('201', 'rcv_user_item', 'item_code', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '套餐编码', NULL),
('202', 'rcv_user_item', 'item_name', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '套餐名称', NULL),
('203', 'rcv_user_item', 'item_price', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '套餐单价', NULL),
('204', 'rcv_user_item', 'item_times', 'int', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '套餐次数', NULL),
('205', 'rcv_user_item', 'item_unit', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '套餐单位', NULL),
('206', 'rcv_user_item', 'item_amount', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '套餐总价', NULL),
('207', 'rcv_user_item', 'amount', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '实际支付', NULL),
('208', 'rcv_user_item', 'times', 'int', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '剩余次数', NULL),
('209', 'rcv_user_item', 'org_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '上次预约机构', NULL),
('210', 'rcv_user_item', 'dept_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '上次预约部门', NULL),
('211', 'rcv_user_item', 'reserve_date', 'date', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '上次预约日期', NULL),
('212', 'rcv_user_item', 'org_change_count', 'int', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '机构变更次数', NULL),
('213', 'rcv_user_item', 'status', 'int', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '状态', NULL),
('214', 'rcv_user_item', 'create_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '创建人', NULL),
('215', 'rcv_user_item', 'update_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '修改人', NULL),
('216', 'rcv_user_item', 'create_time', 'timestamp', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '创建时间', NULL),
('217', 'rcv_user_item', 'update_time', 'timestamp', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '修改时间', NULL),
('218', 'rcv_user_item', 'remark', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '备注', NULL),
('219', 'rcv_user', 'id', 'bigint', NULL, '', b'1', NULL, 'PRI', b'1', b'1', NULL, 'id', NULL),
('220', 'rcv_user', 'name', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '姓名', NULL),
('221', 'rcv_user', 'cert_no', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '身份证', NULL),
('222', 'rcv_user', 'phone', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '电话', NULL),
('223', 'rcv_user', 'birthday', 'date', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '生日', NULL),
('224', 'rcv_user', 'profession', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '职业', NULL),
('225', 'rcv_user', 'address', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '地址', NULL),
('226', 'rcv_user', 'contact_name', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '联系人', NULL),
('227', 'rcv_user', 'contact_phone', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '联系电话', NULL),
('228', 'rcv_user', 'contact_relation', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '联系人关系', NULL),
('229', 'rcv_user', 'status', 'int', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '状态', NULL),
('230', 'rcv_user', 'create_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '创建人', NULL),
('231', 'rcv_user', 'update_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '修改人', NULL),
('232', 'rcv_user', 'create_time', 'timestamp', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '创建时间', NULL),
('233', 'rcv_user', 'update_time', 'timestamp', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '修改时间', NULL),
('234', 'rcv_user', 'remark', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '备注', NULL),
('235', 'rcv_user', 'col1', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, 'col1', NULL),
('236', 'rcv_user', 'col2', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, 'col2', NULL),
('237', 'rcv_user', 'col3', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, 'col3', NULL),
('238', 'rcv_user', 'col4', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, 'col4', NULL),
('239', 'rcv_user', 'col5', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, 'col5', NULL),
('240', 'rcv_item_resource_type', 'id', 'bigint', NULL, '', b'1', NULL, 'PRI', b'1', b'1', NULL, 'id', NULL),
('241', 'rcv_item_resource_type', 'item_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '套餐id', NULL),
('242', 'rcv_item_resource_type', 'resource_type_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '资源类型id', NULL),
('243', 'rcv_item_resource_type', 'org_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '机构id', NULL),
('244', 'rcv_item_resource_type', 'dept_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '部门id', NULL),
('245', 'rcv_item_resource_type', 'status', 'int', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '状态', NULL),
('246', 'rcv_item_resource_type', 'create_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '创建人', NULL),
('247', 'rcv_item_resource_type', 'update_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '修改人', NULL),
('248', 'rcv_item_resource_type', 'create_time', 'timestamp', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '创建时间', NULL),
('249', 'rcv_item_resource_type', 'update_time', 'timestamp', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '修改时间', NULL),
('250', 'rcv_item_resource_type', 'remark', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '备注', NULL),
('251', 'rcv_his_log', 'pat_item_id', 'bigint', NULL, '', b'1', NULL, 'PRI', b'1', b'1', NULL, '项目购买ID', NULL),
('252', 'rcv_his_log', 'visit_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '就诊ID', NULL),
('253', 'rcv_his_log', 'patient_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '患者ID', NULL),
('254', 'rcv_his_log', 'name', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '患者姓名', NULL),
('255', 'rcv_his_log', 'mobile_phone', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '患者电话', NULL),
('256', 'rcv_his_log', 'mrn', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '患者档案编号', NULL),
('257', 'rcv_his_log', 'visit_dept', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '就诊科室', NULL),
('258', 'rcv_his_log', 'visit_date', 'timestamp', NULL, 'on update CURRENT_TIMESTAMP', b'1', NULL, '', b'1', b'1', NULL, '就诊日期', NULL),
('259', 'rcv_his_log', 'item_code', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '项目编码', NULL),
('260', 'rcv_his_log', 'item_name', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '项目名称', NULL),
('261', 'rcv_his_log', 'price', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '单价', NULL),
('262', 'rcv_his_log', 'amount', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '数量', NULL),
('263', 'rcv_his_log', 'unit', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '单位', NULL),
('264', 'rcv_his_log', 'costs', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '应收金额', NULL),
('265', 'rcv_his_log', 'actual_costs', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '实收金额', NULL),
('266', 'rcv_his_log', 'create_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '创建人', NULL),
('267', 'rcv_his_log', 'update_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '修改人', NULL),
('268', 'rcv_his_log', 'create_time', 'timestamp', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '创建时间', NULL),
('269', 'rcv_his_log', 'update_time', 'timestamp', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '修改时间', NULL),
('270', 'rcv_his_log', 'remark', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '备注', NULL),
('271', 'rcv_dept', 'id', 'bigint', NULL, '', b'1', NULL, 'PRI', b'1', b'1', NULL, 'id', NULL),
('272', 'rcv_dept', 'org_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '机构id', NULL),
('273', 'rcv_dept', 'name', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '名称', NULL),
('274', 'rcv_dept', 'parent_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '上级部门', NULL),
('275', 'rcv_dept', 'status', 'int', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '状态', NULL),
('276', 'rcv_dept', 'create_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '创建人', NULL),
('277', 'rcv_dept', 'update_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '修改人', NULL),
('278', 'rcv_dept', 'create_time', 'timestamp', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '创建时间', NULL),
('279', 'rcv_dept', 'update_time', 'timestamp', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '修改时间', NULL),
('280', 'rcv_dept', 'remark', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '备注', NULL),
('281', 'rcv_resource_type', 'id', 'bigint', NULL, '', b'1', NULL, 'PRI', b'1', b'1', NULL, 'id', NULL),
('282', 'rcv_resource_type', 'org_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '机构id', NULL),
('283', 'rcv_resource_type', 'dept_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '部门id', NULL),
('284', 'rcv_resource_type', 'name', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '名称', NULL),
('285', 'rcv_resource_type', 'status', 'int', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '状态', NULL),
('286', 'rcv_resource_type', 'create_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '创建人', NULL),
('287', 'rcv_resource_type', 'update_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '修改人', NULL),
('288', 'rcv_resource_type', 'create_time', 'timestamp', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '创建时间', NULL),
('289', 'rcv_resource_type', 'update_time', 'timestamp', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '修改时间', NULL),
('290', 'rcv_resource_type', 'remark', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '备注', NULL),
('291', 'rcv_resource', 'id', 'bigint', NULL, '', b'1', NULL, 'PRI', b'1', b'1', NULL, 'id', NULL),
('292', 'rcv_resource', 'org_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '机构id', NULL),
('293', 'rcv_resource', 'dept_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '部门id', NULL),
('294', 'rcv_resource', 'type_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '类型id', NULL),
('295', 'rcv_resource', 'code', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '编码', NULL),
('296', 'rcv_resource', 'name', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '名称', NULL),
('297', 'rcv_resource', 'status', 'int', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '状态', NULL),
('298', 'rcv_resource', 'create_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '创建人', NULL),
('299', 'rcv_resource', 'update_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '修改人', NULL),
('300', 'rcv_resource', 'create_time', 'timestamp', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '创建时间', NULL),
('301', 'rcv_resource', 'update_time', 'timestamp', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '修改时间', NULL),
('302', 'rcv_resource', 'remark', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '备注', NULL),
('303', 'rcv_receipt_setting', 'id', 'bigint', NULL, '', b'1', NULL, 'PRI', b'1', b'1', NULL, 'id', NULL),
('304', 'rcv_receipt_setting', 'title', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '标题', NULL),
('305', 'rcv_receipt_setting', 'org_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '机构id', NULL),
('306', 'rcv_receipt_setting', 'dept_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '部门id', NULL),
('307', 'rcv_receipt_setting', 'status', 'int', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '状态', NULL),
('308', 'rcv_receipt_setting', 'create_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '创建人', NULL),
('309', 'rcv_receipt_setting', 'update_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '修改人', NULL),
('310', 'rcv_receipt_setting', 'create_time', 'timestamp', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '创建时间', NULL),
('311', 'rcv_receipt_setting', 'update_time', 'timestamp', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '修改时间', NULL),
('312', 'rcv_receipt_setting', 'remark', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '备注', NULL),
('313', 'rcv_printer_setting', 'id', 'bigint', NULL, '', b'1', NULL, 'PRI', b'1', b'1', NULL, 'id', NULL),
('314', 'rcv_printer_setting', 'name', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '名称', NULL),
('315', 'rcv_printer_setting', 'address', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '地址', NULL),
('316', 'rcv_printer_setting', 'protocol', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '协议', NULL),
('317', 'rcv_printer_setting', 'org_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '机构id', NULL),
('318', 'rcv_printer_setting', 'dept_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '部门id', NULL),
('319', 'rcv_printer_setting', 'status', 'int', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '状态', NULL),
('320', 'rcv_printer_setting', 'create_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '创建人', NULL),
('321', 'rcv_printer_setting', 'update_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '修改人', NULL),
('322', 'rcv_printer_setting', 'create_time', 'timestamp', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '创建时间', NULL),
('323', 'rcv_printer_setting', 'update_time', 'timestamp', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '修改时间', NULL),
('324', 'rcv_printer_setting', 'remark', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '备注', NULL),
('325', 'rcv_item_reserve_log', 'id', 'bigint', NULL, '', b'1', NULL, 'PRI', b'1', b'1', NULL, 'id', NULL),
('326', 'rcv_item_reserve_log', 'reserve_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '预约id', NULL),
('327', 'rcv_item_reserve_log', 'content', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '变更内容', NULL),
('328', 'rcv_item_reserve_log', 'before_status', 'int', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '之前状态', NULL),
('329', 'rcv_item_reserve_log', 'after_status', 'int', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '现在状态', NULL),
('330', 'rcv_item_reserve_log', 'create_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '创建人', NULL),
('331', 'rcv_item_reserve_log', 'update_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '修改人', NULL),
('332', 'rcv_item_reserve_log', 'create_time', 'timestamp', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '创建时间', NULL),
('333', 'rcv_item_reserve_log', 'update_time', 'timestamp', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '修改时间', NULL),
('334', 'rcv_item_reserve_log', 'remark', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '备注', NULL),
('335', 'rcv_org', 'id', 'bigint', NULL, '', b'1', NULL, 'PRI', b'1', b'1', NULL, 'id', NULL),
('336', 'rcv_org', 'name', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '机构名称', NULL),
('337', 'rcv_org', 'address', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '机构地址', NULL),
('338', 'rcv_org', 'contact_name', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '联系人', NULL),
('339', 'rcv_org', 'contact_phone', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '联系电话', NULL),
('340', 'rcv_org', 'status', 'int', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '状态', NULL),
('341', 'rcv_org', 'create_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '创建人', NULL),
('342', 'rcv_org', 'update_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '修改人', NULL),
('343', 'rcv_org', 'create_time', 'timestamp', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '创建时间', NULL),
('344', 'rcv_org', 'update_time', 'timestamp', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '修改时间', NULL),
('345', 'rcv_org', 'remark', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '备注', NULL),
('346', 'rcv_item_reserve', 'id', 'bigint', NULL, '', b'1', NULL, 'PRI', b'1', b'1', NULL, 'id', NULL),
('347', 'rcv_item_reserve', 'item_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '套餐id', NULL),
('348', 'rcv_item_reserve', 'org_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '机构id', NULL),
('349', 'rcv_item_reserve', 'dept_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '部门Id', NULL),
('350', 'rcv_item_reserve', 'code', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '唯一编码', NULL),
('351', 'rcv_item_reserve', 'date', 'date', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '预约日期', NULL),
('352', 'rcv_item_reserve', 'begin_time', 'time', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '开始时间', NULL),
('353', 'rcv_item_reserve', 'end_tiem', 'time', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '结束时间', NULL),
('354', 'rcv_item_reserve', 'time_amount', 'int', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '时长', NULL),
('355', 'rcv_item_reserve', 'actual_begin_time', 'time', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '实际开始时间', NULL),
('356', 'rcv_item_reserve', 'actual_end_time', 'time', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '实际结束时间', NULL),
('357', 'rcv_item_reserve', 'status', 'int', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '状态', NULL),
('358', 'rcv_item_reserve', 'resource_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '资源id', NULL),
('359', 'rcv_item_reserve', 'create_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '创建人', NULL),
('360', 'rcv_item_reserve', 'update_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '修改人', NULL),
('361', 'rcv_item_reserve', 'create_time', 'timestamp', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '创建时间', NULL),
('362', 'rcv_item_reserve', 'update_time', 'timestamp', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '修改时间', NULL),
('363', 'rcv_item_reserve', 'remark', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '备注', NULL),
('364', 'rcv_item_time_setting', 'id', 'bigint', NULL, '', b'1', NULL, 'PRI', b'1', b'1', NULL, 'id', NULL),
('365', 'rcv_item_time_setting', 'item_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '套餐id', NULL),
('366', 'rcv_item_time_setting', 'time_amout', 'int', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '套餐时长', NULL),
('367', 'rcv_item_time_setting', 'org_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '机构id', NULL),
('368', 'rcv_item_time_setting', 'dept_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '部门id', NULL),
('369', 'rcv_item_time_setting', 'status', 'int', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '状态', NULL),
('370', 'rcv_item_time_setting', 'create_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '创建人', NULL),
('371', 'rcv_item_time_setting', 'update_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '修改人', NULL),
('372', 'rcv_item_time_setting', 'create_time', 'timestamp', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '创建时间', NULL),
('373', 'rcv_item_time_setting', 'update_time', 'timestamp', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '修改时间', NULL),
('374', 'rcv_item_time_setting', 'remark', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '备注', NULL),
('375', 'rcv_item', 'id', 'bigint', NULL, '', b'1', NULL, 'PRI', b'1', b'1', NULL, 'id', NULL),
('376', 'rcv_item', 'code', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '套餐编码', NULL),
('377', 'rcv_item', 'name', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '套餐名称', NULL),
('378', 'rcv_item', 'price', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '单价', NULL),
('379', 'rcv_item', 'times', 'int', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '次数', NULL),
('380', 'rcv_item', 'unit', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '单位', NULL),
('381', 'rcv_item', 'amount', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '总价', NULL),
('382', 'rcv_item', 'status', 'int', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '状态', NULL),
('383', 'rcv_item', 'create_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '创建人', NULL),
('384', 'rcv_item', 'update_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '修改人', NULL),
('385', 'rcv_item', 'create_time', 'timestamp', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '创建时间', NULL),
('386', 'rcv_item', 'update_time', 'timestamp', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '修改时间', NULL),
('387', 'rcv_item', 'remark', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '备注', NULL),
('388', 'rcv_user_custom_col_setting', 'id', 'bigint', NULL, '', b'1', NULL, 'PRI', b'1', b'1', NULL, 'id', NULL),
('389', 'rcv_user_custom_col_setting', 'org_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '机构id', NULL),
('390', 'rcv_user_custom_col_setting', 'dept_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '部门id', NULL),
('391', 'rcv_user_custom_col_setting', 'name', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '列名称', NULL),
('392', 'rcv_user_custom_col_setting', 'description', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '列描述', NULL),
('393', 'rcv_user_custom_col_setting', 'dict_id', 'bigint', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '字典id', NULL),
('394', 'rcv_user_custom_col_setting', 'dict_name', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '字典名称', NULL),
('395', 'rcv_user_custom_col_setting', 'dict_description', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '字典描述', NULL),
('396', 'rcv_user_custom_col_setting', 'status', 'int', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '状态', NULL),
('397', 'rcv_user_custom_col_setting', 'create_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '创建人', NULL),
('398', 'rcv_user_custom_col_setting', 'update_by', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '修改人', NULL),
('399', 'rcv_user_custom_col_setting', 'create_time', 'timestamp', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '创建时间', NULL),
('400', 'rcv_user_custom_col_setting', 'update_time', 'timestamp', NULL, '', b'1', NULL, '', b'1', b'1', NULL, '修改时间', NULL),
('401', 'rcv_user_custom_col_setting', 'remark', 'varchar', NULL, '', b'1', NULL, '', b'1', b'0', NULL, '备注', NULL);

INSERT INTO `code_gen_config` (`config_id`, `table_name`, `author`, `cover`, `module_name`, `pack`, `path`, `api_path`, `prefix`, `api_alias`) VALUES
('6', 'rcv_user_notification', 'yanjun', b'0', 'eladmin-recovery', 'me.zhengjie.modules.recovery', '/Users/yanjun/Downloads/eladmin-web/src/views/recovery/', '/Users/yanjun/Downloads/eladmin-web/src/api', NULL, '患者预约提醒'),
('7', 'rcv_user_item', 'yanjun', b'0', 'eladmin-recovery', 'me.zhengjie.modules.recovery', '/Users/yanjun/Downloads/eladmin-web/src/views/recovery/userItem', '/Users/yanjun/Downloads/eladmin-web/src/api', NULL, '用户套餐'),
('8', 'rcv_user', 'yanjun', b'0', 'eladmin-recovery', 'me.zhengjie.modules.recovery', '/Users/yanjun/Downloads/eladmin-web/src/views/recovery/user', '/Users/yanjun/Downloads/eladmin-web/src/api', NULL, '患者信息'),
('9', 'rcv_item_resource_type', 'yanjun', b'0', 'eladmin-recovery', 'me.zhengjie.modules.recovery', '/Users/yanjun/Downloads/eladmin-web/src/views/recovery/itemResourceType', '/Users/yanjun/Downloads/eladmin-web/src/api', NULL, '套餐资源类型'),
('10', 'rcv_his_log', 'yanjun', b'0', 'eladmin-recovery', 'me.zhengjie.modules.recovery', '/Users/yanjun/Downloads/eladmin-web/src/views/recovery/hisLog', '/Users/yanjun/Downloads/eladmin-web/src/api', NULL, 'HIS查询日志'),
('11', 'rcv_dept', 'yanjun', b'0', 'eladmin-recovery', 'me.zhengjie.modules.recovery', '/Users/yanjun/Downloads/eladmin-web/src/views/recovery/dept', '/Users/yanjun/Downloads/eladmin-web/src/api', NULL, '部门'),
('12', 'rcv_resource_type', 'yanjun', b'0', 'eladmin-recovery', 'me.zhengjie.modules.recovery', '/Users/yanjun/Downloads/eladmin-web/src/views/recovery/resourceType', '/Users/yanjun/Downloads/eladmin-web/src/api', NULL, '资源类型'),
('13', 'rcv_resource', 'yanjun', b'0', 'eladmin-recovery', 'me.zhengjie.modules.recovery', '/Users/yanjun/Downloads/eladmin-web/src/views/recovery/resource', '/Users/yanjun/Downloads/eladmin-web/src/api', NULL, '资源'),
('14', 'rcv_receipt_setting', ' yanjun', b'0', 'eladmin-recovery', 'me.zhengjie.modules.recovery', '/Users/yanjun/Downloads/eladmin-web/src/views/recovery/receiptSetting', '/Users/yanjun/Downloads/eladmin-web/src/api', NULL, '小票设置'),
('15', 'rcv_printer_setting', 'yanjun', b'0', 'eladmin-recovery', 'me.zhengjie.modules.recovery', '/Users/yanjun/Downloads/eladmin-web/src/views/recovery/printerSetting', '/Users/yanjun/Downloads/eladmin-web/src/api', NULL, '打印机设置'),
('16', 'rcv_item_reserve_log', 'yanjun', b'0', 'eladmin-recovery', 'me.zhengjie.modules.recovery', '/Users/yanjun/Downloads/eladmin-web/src/views/recovery/itemReserveLog', '/Users/yanjun/Downloads/eladmin-web/src/api', NULL, '套餐预约记录日志'),
('17', 'rcv_org', 'yanjun', b'0', 'eladmin-recovery', 'me.zhengjie.modules.recovery', '/Users/yanjun/Downloads/eladmin-web/src/views/recovery/org', '/Users/yanjun/Downloads/eladmin-web/src/api', NULL, '机构'),
('18', 'rcv_item_reserve', 'yanjun', b'0', 'eladmin-recovery', 'me.zhengjie.modules.recovery', '/Users/yanjun/Downloads/eladmin-web/src/views/recovery/itemReserve', '/Users/yanjun/Downloads/eladmin-web/src/api', NULL, '套餐预约记录'),
('19', 'rcv_item_time_setting', 'yanjun', b'0', 'eladmin-recovery', 'me.zhengjie.modules.recovery', '/Users/yanjun/Downloads/eladmin-web/src/views/recovery/itemTimeSetting', '/Users/yanjun/Downloads/eladmin-web/src/api', NULL, '套餐时长设置'),
('20', 'rcv_item', 'yanjun', b'0', 'eladmin-recovery', 'me.zhengjie.modules.recovery', '/Users/yanjun/Downloads/eladmin-web/src/views/recovery/item', '/Users/yanjun/Downloads/eladmin-web/src/api', NULL, '套餐'),
('21', 'rcv_user_custom_col_setting', 'yanjun', b'0', 'eladmin-recovery', 'me.zhengjie.modules.recovery', '/Users/yanjun/Downloads/eladmin-web/src/views/recovery/userCustomColSetting', '/Users/yanjun/Downloads/eladmin-web/src/api', NULL, '用户自定义列设置');

INSERT INTO `mnt_server` (`server_id`, `account`, `ip`, `name`, `password`, `port`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES
('1', 'root', '132.232.129.20', '腾讯云', 'Dqjdda1996.', '8013', NULL, NULL, '2019-11-24 20:35:02', NULL);

INSERT INTO `sys_dept` (`dept_id`, `pid`, `sub_count`, `level`, `name`, `dept_sort`, `enabled`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES
('21', NULL, '1', '0', '总部', '0', b'1', 'admin', 'admin', '2020-11-22 10:34:29', '2020-11-22 10:34:59'),
('27', '21', '0', '1', '科技公司', '1', b'1', 'admin', 'duan', '2020-11-22 11:45:32', '2020-12-21 11:13:42'),
('32', '21', '0', '1', '成都妇幼保健医院', '2', b'1', 'admin', 'admin', '2020-12-05 17:01:05', '2020-12-05 17:01:05');

INSERT INTO `sys_dict` (`dict_id`, `name`, `description`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES
('1', 'user_status', '用户状态', NULL, NULL, '2019-10-27 20:31:36', NULL),
('4', 'dept_status', '部门状态', NULL, NULL, '2019-10-27 20:31:36', NULL),
('5', 'job_status', '岗位状态', NULL, NULL, '2019-10-27 20:31:36', NULL),
('6', 'machinery_type', '机器类型', 'admin', 'admin', '2020-10-21 10:47:44', '2020-10-21 10:47:44'),
('7', 'his_setting_status', 'HIS设置状态', 'admin', 'admin', '2020-10-24 17:43:00', '2020-10-24 17:43:00'),
('8', 'rcv_org_status', '机构状态', 'admin', 'admin', '2020-10-26 14:35:25', '2020-10-26 14:35:25'),
('9', 'user_col_level', '患者级别', 'admin', 'admin', '2020-11-12 15:10:03', '2020-11-12 15:10:03'),
('10', 'user_col_status', '患者状态', 'admin', 'admin', '2020-11-12 15:12:41', '2020-11-12 15:12:41'),
('11', 'reserve_status', '预约状态', 'admin', 'admin', '2020-12-28 14:43:28', '2020-12-28 14:43:28');

INSERT INTO `sys_dict_detail` (`detail_id`, `dict_id`, `label`, `value`, `dict_sort`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES
('1', '1', '激活', 'true', '1', NULL, NULL, '2019-10-27 20:31:36', NULL),
('2', '1', '禁用', 'false', '2', NULL, NULL, NULL, NULL),
('3', '4', '启用', 'true', '1', NULL, NULL, NULL, NULL),
('4', '4', '停用', 'false', '2', NULL, NULL, '2019-10-27 20:31:36', NULL),
('5', '5', '启用', 'true', '1', NULL, NULL, NULL, NULL),
('6', '5', '停用', 'false', '2', NULL, NULL, '2019-10-27 20:31:36', NULL),
('7', '6', '康复仪', 'kangfuyi', '0', 'admin', 'admin', '2020-10-21 10:48:49', '2020-10-21 10:48:49'),
('8', '6', '塑形仪', 'suxingyi', '1', 'admin', 'admin', '2020-10-21 10:49:47', '2020-10-21 10:49:47'),
('9', '7', '激活', 'true', '1', 'admin', 'admin', '2020-10-24 17:44:01', '2020-10-24 17:44:01'),
('10', '7', '禁用', 'false', '2', 'admin', 'admin', '2020-10-24 17:44:24', '2020-10-24 17:44:24'),
('11', '8', '有效', '1', '1', 'admin', 'admin', '2020-10-26 14:36:07', '2020-10-26 14:36:07'),
('12', '8', '无效', '2', '2', 'admin', 'admin', '2020-10-26 14:36:15', '2020-10-26 14:36:15'),
('13', '9', 'A', 'a', '1', 'admin', 'admin', '2020-11-12 15:10:33', '2020-11-12 15:10:33'),
('14', '9', 'B', 'b', '2', 'admin', 'admin', '2020-11-12 15:10:38', '2020-11-12 15:10:38'),
('15', '9', 'C', 'c', '3', 'admin', 'admin', '2020-11-12 15:10:43', '2020-11-12 15:10:43'),
('16', '9', 'D', 'd', '4', 'admin', 'admin', '2020-11-12 15:10:52', '2020-11-12 15:10:52'),
('17', '9', 'E', 'e', '5', 'admin', 'admin', '2020-11-12 15:11:00', '2020-11-12 15:11:00'),
('18', '9', 'F', 'f', '6', 'admin', 'admin', '2020-11-12 15:11:06', '2020-11-12 15:11:06'),
('19', '11', '已预约', 'init', '1', 'admin', 'admin', '2020-12-28 14:43:47', '2020-12-28 14:43:47'),
('20', '11', '已签到', 'check_in', '2', 'admin', 'admin', '2020-12-28 14:44:07', '2020-12-28 14:44:07'),
('21', '11', '已核销', 'verified', '3', 'admin', 'admin', '2020-12-28 14:44:19', '2020-12-28 14:44:19'),
('22', '11', '已取消', 'canceled', '4', 'admin', 'admin', '2020-12-28 14:44:30', '2020-12-28 14:44:30');

INSERT INTO `sys_job` (`job_id`, `name`, `enabled`, `job_sort`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES
('13', '医护主管', b'1', '0', 'admin', 'admin', '2020-10-21 10:43:46', '2020-11-22 11:06:05'),
('14', '值班护士', b'1', '2', 'admin', 'admin', '2020-10-21 10:44:13', '2020-11-22 11:06:16'),
('15', '推广专员', b'1', '3', 'admin', 'admin', '2020-10-21 10:44:26', '2020-11-22 11:06:33'),
('16', '研发主管', b'1', '1', 'admin', 'admin', '2020-11-22 11:01:09', '2020-11-22 11:05:59');

INSERT INTO `sys_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES
('1', NULL, '7', '0', '系统管理', NULL, NULL, '1', 'system', 'system', b'0', b'0', b'0', NULL, NULL, NULL, '2018-12-18 15:11:29', NULL),
('2', '1', '3', '1', '用户管理', 'User', 'system/user/index', '2', 'peoples', 'user', b'0', b'0', b'0', 'user:list', NULL, NULL, '2018-12-18 15:14:44', NULL),
('3', '1', '3', '1', '角色管理', 'Role', 'system/role/index', '3', 'role', 'role', b'0', b'0', b'0', 'roles:list', NULL, NULL, '2018-12-18 15:16:07', NULL),
('5', '1', '3', '1', '菜单管理', 'Menu', 'system/menu/index', '5', 'menu', 'menu', b'0', b'0', b'0', 'menu:list', NULL, NULL, '2018-12-18 15:17:28', NULL),
('6', NULL, '5', '0', '系统监控', NULL, NULL, '10', 'monitor', 'monitor', b'0', b'0', b'0', NULL, NULL, NULL, '2018-12-18 15:17:48', NULL),
('7', '6', '0', '1', '操作日志', 'Log', 'monitor/log/index', '11', 'log', 'logs', b'0', b'0', b'0', NULL, NULL, NULL, '2018-12-18 15:18:26', NULL),
('9', '6', '0', '1', 'SQL监控', 'Sql', 'monitor/sql/index', '18', 'sqlMonitor', 'druid', b'0', b'0', b'0', NULL, NULL, NULL, '2018-12-18 15:19:34', NULL),
('10', NULL, '5', '0', '组件管理', NULL, NULL, '50', 'zujian', 'components', b'0', b'0', b'0', NULL, NULL, NULL, '2018-12-19 13:38:16', NULL),
('11', '10', '0', '1', '图标库', 'Icons', 'components/icons/index', '51', 'icon', 'icon', b'0', b'0', b'0', NULL, NULL, NULL, '2018-12-19 13:38:49', NULL),
('14', '36', '0', '1', '邮件工具', 'Email', 'tools/email/index', '35', 'email', 'email', b'0', b'0', b'0', NULL, NULL, NULL, '2018-12-27 10:13:09', NULL),
('15', '10', '0', '1', '富文本', 'Editor', 'components/Editor', '52', 'fwb', 'tinymce', b'0', b'0', b'0', NULL, NULL, NULL, '2018-12-27 11:58:25', NULL),
('16', '36', '2', '1', '图床管理', 'Pictures', 'tools/picture/index', '33', 'image', 'pictures', b'0', b'0', b'0', 'pictures:list', NULL, NULL, '2018-12-28 09:36:53', NULL),
('18', '36', '3', '1', '存储管理', 'Storage', 'tools/storage/index', '34', 'qiniu', 'storage', b'0', b'0', b'0', 'storage:list', NULL, NULL, '2018-12-31 11:12:15', NULL),
('19', '36', '0', '1', '支付宝工具', 'AliPay', 'tools/aliPay/index', '37', 'alipay', 'aliPay', b'0', b'0', b'0', NULL, NULL, NULL, '2018-12-31 14:52:38', NULL),
('21', NULL, '2', '0', '多级菜单', NULL, '', '900', 'menu', 'nested', b'0', b'0', b'0', NULL, NULL, NULL, '2019-01-04 16:22:03', NULL),
('22', '21', '2', '1', '二级菜单1', NULL, 'nested/menu1/index', '999', 'menu', 'menu1', b'0', b'0', b'0', NULL, NULL, NULL, '2019-01-04 16:23:29', NULL),
('23', '21', '0', '1', '二级菜单2', NULL, 'nested/menu2/index', '999', 'menu', 'menu2', b'0', b'0', b'0', NULL, NULL, NULL, '2019-01-04 16:23:57', NULL),
('24', '22', '0', '1', '三级菜单1', NULL, 'nested/menu1/menu1-1', '999', 'menu', 'menu1-1', b'0', b'0', b'0', NULL, NULL, NULL, '2019-01-04 16:24:48', NULL),
('27', '22', '0', '1', '三级菜单2', NULL, 'nested/menu1/menu1-2', '999', 'menu', 'menu1-2', b'0', b'0', b'0', NULL, NULL, NULL, '2019-01-07 17:27:32', NULL),
('28', '1', '3', '1', '任务调度', 'Timing', 'system/timing/index', '999', 'timing', 'timing', b'0', b'0', b'0', 'timing:list', NULL, NULL, '2019-01-07 20:34:40', NULL),
('30', '36', '0', '1', '代码生成', 'GeneratorIndex', 'generator/index', '32', 'dev', 'generator', b'0', b'1', b'0', NULL, NULL, NULL, '2019-01-11 15:45:55', NULL),
('32', '6', '0', '1', '异常日志', 'ErrorLog', 'monitor/log/errorLog', '12', 'error', 'errorLog', b'0', b'0', b'0', NULL, NULL, NULL, '2019-01-13 13:49:03', NULL),
('33', '10', '0', '1', 'Markdown', 'Markdown', 'components/MarkDown', '53', 'markdown', 'markdown', b'0', b'0', b'0', NULL, NULL, NULL, '2019-03-08 13:46:44', NULL),
('34', '10', '0', '1', 'Yaml编辑器', 'YamlEdit', 'components/YamlEdit', '54', 'dev', 'yaml', b'0', b'0', b'0', NULL, NULL, NULL, '2019-03-08 15:49:40', NULL),
('35', '1', '3', '1', '部门管理', 'Dept', 'system/dept/index', '6', 'dept', 'dept', b'0', b'0', b'0', 'dept:list', NULL, NULL, '2019-03-25 09:46:00', NULL),
('36', NULL, '8', '0', '系统工具', NULL, '', '30', 'sys-tools', 'sys-tools', b'0', b'0', b'0', NULL, NULL, NULL, '2019-03-29 10:57:35', NULL),
('37', '1', '3', '1', '岗位管理', 'Job', 'system/job/index', '7', 'Steve-Jobs', 'job', b'0', b'0', b'0', 'job:list', NULL, NULL, '2019-03-29 13:51:18', NULL),
('38', '36', '0', '1', '接口文档', 'Swagger', 'tools/swagger/index', '36', 'swagger', 'swagger2', b'0', b'0', b'0', NULL, NULL, NULL, '2019-03-29 19:57:53', NULL),
('39', '1', '3', '1', '字典管理', 'Dict', 'system/dict/index', '8', 'dictionary', 'dict', b'0', b'0', b'0', 'dict:list', NULL, NULL, '2019-04-10 11:49:04', NULL),
('41', '6', '0', '1', '在线用户', 'OnlineUser', 'monitor/online/index', '10', 'Steve-Jobs', 'online', b'0', b'0', b'0', NULL, NULL, NULL, '2019-10-26 22:08:43', NULL),
('44', '2', '0', '2', '用户新增', NULL, '', '2', '', '', b'0', b'0', b'0', 'user:add', NULL, NULL, '2019-10-29 10:59:46', NULL),
('45', '2', '0', '2', '用户编辑', NULL, '', '3', '', '', b'0', b'0', b'0', 'user:edit', NULL, NULL, '2019-10-29 11:00:08', NULL),
('46', '2', '0', '2', '用户删除', NULL, '', '4', '', '', b'0', b'0', b'0', 'user:del', NULL, NULL, '2019-10-29 11:00:23', NULL),
('48', '3', '0', '2', '角色创建', NULL, '', '2', '', '', b'0', b'0', b'0', 'roles:add', NULL, NULL, '2019-10-29 12:45:34', NULL),
('49', '3', '0', '2', '角色修改', NULL, '', '3', '', '', b'0', b'0', b'0', 'roles:edit', NULL, NULL, '2019-10-29 12:46:16', NULL),
('50', '3', '0', '2', '角色删除', NULL, '', '4', '', '', b'0', b'0', b'0', 'roles:del', NULL, NULL, '2019-10-29 12:46:51', NULL),
('52', '5', '0', '2', '菜单新增', NULL, '', '2', '', '', b'0', b'0', b'0', 'menu:add', NULL, NULL, '2019-10-29 12:55:07', NULL),
('53', '5', '0', '2', '菜单编辑', NULL, '', '3', '', '', b'0', b'0', b'0', 'menu:edit', NULL, NULL, '2019-10-29 12:55:40', NULL),
('54', '5', '0', '2', '菜单删除', NULL, '', '4', '', '', b'0', b'0', b'0', 'menu:del', NULL, NULL, '2019-10-29 12:56:00', NULL),
('56', '35', '0', '2', '部门新增', NULL, '', '2', '', '', b'0', b'0', b'0', 'dept:add', NULL, NULL, '2019-10-29 12:57:09', NULL),
('57', '35', '0', '2', '部门编辑', NULL, '', '3', '', '', b'0', b'0', b'0', 'dept:edit', NULL, NULL, '2019-10-29 12:57:27', NULL),
('58', '35', '0', '2', '部门删除', NULL, '', '4', '', '', b'0', b'0', b'0', 'dept:del', NULL, NULL, '2019-10-29 12:57:41', NULL),
('60', '37', '0', '2', '岗位新增', NULL, '', '2', '', '', b'0', b'0', b'0', 'job:add', NULL, NULL, '2019-10-29 12:58:27', NULL),
('61', '37', '0', '2', '岗位编辑', NULL, '', '3', '', '', b'0', b'0', b'0', 'job:edit', NULL, NULL, '2019-10-29 12:58:45', NULL),
('62', '37', '0', '2', '岗位删除', NULL, '', '4', '', '', b'0', b'0', b'0', 'job:del', NULL, NULL, '2019-10-29 12:59:04', NULL),
('64', '39', '0', '2', '字典新增', NULL, '', '2', '', '', b'0', b'0', b'0', 'dict:add', NULL, NULL, '2019-10-29 13:00:17', NULL),
('65', '39', '0', '2', '字典编辑', NULL, '', '3', '', '', b'0', b'0', b'0', 'dict:edit', NULL, NULL, '2019-10-29 13:00:42', NULL),
('66', '39', '0', '2', '字典删除', NULL, '', '4', '', '', b'0', b'0', b'0', 'dict:del', NULL, NULL, '2019-10-29 13:00:59', NULL),
('70', '16', '0', '2', '图片上传', NULL, '', '2', '', '', b'0', b'0', b'0', 'pictures:add', NULL, NULL, '2019-10-29 13:05:34', NULL),
('71', '16', '0', '2', '图片删除', NULL, '', '3', '', '', b'0', b'0', b'0', 'pictures:del', NULL, NULL, '2019-10-29 13:05:52', NULL),
('73', '28', '0', '2', '任务新增', NULL, '', '2', '', '', b'0', b'0', b'0', 'timing:add', NULL, NULL, '2019-10-29 13:07:28', NULL),
('74', '28', '0', '2', '任务编辑', NULL, '', '3', '', '', b'0', b'0', b'0', 'timing:edit', NULL, NULL, '2019-10-29 13:07:41', NULL),
('75', '28', '0', '2', '任务删除', NULL, '', '4', '', '', b'0', b'0', b'0', 'timing:del', NULL, NULL, '2019-10-29 13:07:54', NULL),
('77', '18', '0', '2', '上传文件', NULL, '', '2', '', '', b'0', b'0', b'0', 'storage:add', NULL, NULL, '2019-10-29 13:09:09', NULL),
('78', '18', '0', '2', '文件编辑', NULL, '', '3', '', '', b'0', b'0', b'0', 'storage:edit', NULL, NULL, '2019-10-29 13:09:22', NULL),
('79', '18', '0', '2', '文件删除', NULL, '', '4', '', '', b'0', b'0', b'0', 'storage:del', NULL, NULL, '2019-10-29 13:09:34', NULL),
('80', '6', '0', '1', '服务监控', 'ServerMonitor', 'monitor/server/index', '14', 'codeConsole', 'server', b'0', b'0', b'0', 'monitor:list', NULL, 'admin', '2019-11-07 13:06:39', '2020-05-04 18:20:50'),
('82', '36', '0', '1', '生成配置', 'GeneratorConfig', 'generator/config', '33', 'dev', 'generator/config/:tableName', b'0', b'1', b'1', '', NULL, NULL, '2019-11-17 20:08:56', NULL),
('83', '10', '0', '1', '图表库', 'Echarts', 'components/Echarts', '50', 'chart', 'echarts', b'0', b'1', b'0', '', NULL, NULL, '2019-11-21 09:04:32', NULL),
('90', NULL, '5', '1', '运维管理', 'Mnt', '', '20', 'mnt', 'mnt', b'0', b'0', b'0', NULL, NULL, NULL, '2019-11-09 10:31:08', NULL),
('92', '90', '3', '1', '服务器', 'ServerDeploy', 'mnt/server/index', '22', 'server', 'mnt/serverDeploy', b'0', b'0', b'0', 'serverDeploy:list', NULL, NULL, '2019-11-10 10:29:25', NULL),
('93', '90', '3', '1', '应用管理', 'App', 'mnt/app/index', '23', 'app', 'mnt/app', b'0', b'0', b'0', 'app:list', NULL, NULL, '2019-11-10 11:05:16', NULL),
('94', '90', '3', '1', '部署管理', 'Deploy', 'mnt/deploy/index', '24', 'deploy', 'mnt/deploy', b'0', b'0', b'0', 'deploy:list', NULL, NULL, '2019-11-10 15:56:55', NULL),
('97', '90', '1', '1', '部署备份', 'DeployHistory', 'mnt/deployHistory/index', '25', 'backup', 'mnt/deployHistory', b'0', b'0', b'0', 'deployHistory:list', NULL, NULL, '2019-11-10 16:49:44', NULL),
('98', '90', '3', '1', '数据库管理', 'Database', 'mnt/database/index', '26', 'database', 'mnt/database', b'0', b'0', b'0', 'database:list', NULL, NULL, '2019-11-10 20:40:04', NULL),
('102', '97', '0', '2', '删除', NULL, '', '999', '', '', b'0', b'0', b'0', 'deployHistory:del', NULL, NULL, '2019-11-17 09:32:48', NULL),
('103', '92', '0', '2', '服务器新增', NULL, '', '999', '', '', b'0', b'0', b'0', 'serverDeploy:add', NULL, NULL, '2019-11-17 11:08:33', NULL),
('104', '92', '0', '2', '服务器编辑', NULL, '', '999', '', '', b'0', b'0', b'0', 'serverDeploy:edit', NULL, NULL, '2019-11-17 11:08:57', NULL),
('105', '92', '0', '2', '服务器删除', NULL, '', '999', '', '', b'0', b'0', b'0', 'serverDeploy:del', NULL, NULL, '2019-11-17 11:09:15', NULL),
('106', '93', '0', '2', '应用新增', NULL, '', '999', '', '', b'0', b'0', b'0', 'app:add', NULL, NULL, '2019-11-17 11:10:03', NULL),
('107', '93', '0', '2', '应用编辑', NULL, '', '999', '', '', b'0', b'0', b'0', 'app:edit', NULL, NULL, '2019-11-17 11:10:28', NULL),
('108', '93', '0', '2', '应用删除', NULL, '', '999', '', '', b'0', b'0', b'0', 'app:del', NULL, NULL, '2019-11-17 11:10:55', NULL),
('109', '94', '0', '2', '部署新增', NULL, '', '999', '', '', b'0', b'0', b'0', 'deploy:add', NULL, NULL, '2019-11-17 11:11:22', NULL),
('110', '94', '0', '2', '部署编辑', NULL, '', '999', '', '', b'0', b'0', b'0', 'deploy:edit', NULL, NULL, '2019-11-17 11:11:41', NULL),
('111', '94', '0', '2', '部署删除', NULL, '', '999', '', '', b'0', b'0', b'0', 'deploy:del', NULL, NULL, '2019-11-17 11:12:01', NULL),
('112', '98', '0', '2', '数据库新增', NULL, '', '999', '', '', b'0', b'0', b'0', 'database:add', NULL, NULL, '2019-11-17 11:12:43', NULL),
('113', '98', '0', '2', '数据库编辑', NULL, '', '999', '', '', b'0', b'0', b'0', 'database:edit', NULL, NULL, '2019-11-17 11:12:58', NULL),
('114', '98', '0', '2', '数据库删除', NULL, '', '999', '', '', b'0', b'0', b'0', 'database:del', NULL, NULL, '2019-11-17 11:13:14', NULL),
('116', '36', '0', '1', '生成预览', 'Preview', 'generator/preview', '999', 'java', 'generator/preview/:tableName', b'0', b'1', b'1', NULL, NULL, NULL, '2019-11-26 14:54:36', NULL),
('191', NULL, '10', '0', '康约平台', NULL, NULL, '2', 'date', 'ptt', b'0', b'0', b'0', NULL, 'admin', 'admin', '2020-12-01 11:25:58', '2020-12-01 11:25:58'),
('192', '191', '3', '1', '医院管理', 'Hospital', 'yy/hospital/index', '90', 'app', 'hospital', b'0', b'0', b'0', 'hospital:list', 'admin', 'admin', '2020-12-01 11:27:18', '2020-12-30 17:52:10'),
('193', '192', '0', '2', '医院新增', NULL, NULL, '1', NULL, NULL, b'0', b'0', b'0', 'hospital:add', 'admin', 'admin', '2020-12-01 11:27:48', '2020-12-24 23:49:13'),
('194', '192', '0', '2', '医院修改', NULL, NULL, '2', NULL, NULL, b'0', b'0', b'0', 'hospital:edit', 'admin', 'admin', '2020-12-01 11:28:05', '2020-12-24 23:49:23'),
('195', '192', '0', '2', '医院删除', NULL, NULL, '3', NULL, NULL, b'0', b'0', b'0', 'hospital:del', 'admin', 'admin', '2020-12-01 11:28:21', '2020-12-24 23:49:42'),
('200', '191', '3', '1', '资源管理', 'Resource', 'yy/resource/index', '7', 'database', 'resource', b'0', b'0', b'0', 'resource:list', 'admin', 'admin', '2020-12-01 18:15:57', '2020-12-30 17:51:33'),
('201', '200', '0', '2', '资源新增', NULL, NULL, '1', NULL, NULL, b'0', b'0', b'0', 'resource:add', 'admin', 'admin', '2020-12-01 18:16:22', '2020-12-05 10:47:27'),
('202', '200', '0', '2', '资源编辑', NULL, NULL, '2', NULL, NULL, b'0', b'0', b'0', 'resource:edit', 'admin', 'admin', '2020-12-01 18:16:46', '2020-12-05 10:47:36'),
('203', '200', '0', '2', '资源删除', NULL, NULL, '3', NULL, NULL, b'0', b'0', b'0', 'resource:del', 'admin', 'admin', '2020-12-01 18:17:04', '2020-12-05 10:47:51'),
('204', '191', '3', '1', '套餐管理', 'Term', 'yy/term/index', '4', 'database', 'term', b'0', b'0', b'0', 'term:list', 'admin', 'admin', '2020-12-01 22:36:29', '2020-12-25 23:55:55'),
('205', '204', '0', '2', '套餐添加', NULL, NULL, '1', NULL, NULL, b'0', b'0', b'0', 'term:add', 'admin', 'admin', '2020-12-01 22:37:07', '2020-12-01 22:37:07'),
('206', '204', '0', '2', '套餐修改', NULL, NULL, '2', NULL, NULL, b'0', b'0', b'0', 'term:edit', 'admin', 'admin', '2020-12-01 22:37:34', '2020-12-01 22:37:34'),
('207', '204', '0', '2', '套餐移除', NULL, NULL, '3', NULL, NULL, b'0', b'0', b'0', 'term:del', 'admin', 'admin', '2020-12-01 22:37:51', '2020-12-01 22:37:51'),
('212', '191', '3', '1', '患者管理', 'Patient', 'yy/patient/index', '2', 'people', 'patient', b'0', b'0', b'0', 'patient:list', 'admin', 'admin', '2020-12-02 16:58:39', '2020-12-30 17:50:49'),
('213', '212', '0', '2', '患者添加', NULL, NULL, '1', NULL, NULL, b'0', b'0', b'0', 'patient:add', 'admin', 'admin', '2020-12-02 16:59:01', '2020-12-02 16:59:01'),
('214', '212', '0', '2', '患者修改', NULL, NULL, '2', NULL, NULL, b'0', b'0', b'0', 'patient:edit', 'admin', 'admin', '2020-12-02 16:59:25', '2020-12-02 16:59:25'),
('215', '212', '0', '2', '患者移除', NULL, NULL, '3', NULL, NULL, b'0', b'0', b'0', 'patient:del', 'admin', 'admin', '2020-12-02 16:59:40', '2020-12-02 16:59:40'),
('224', '191', '3', '1', '患者套餐', 'PatientTerm', 'yy/patientTerm/index', '3', 'database', 'patientTerm', b'0', b'0', b'0', 'patientTerm:list', 'admin', 'admin', '2020-12-03 16:52:07', '2020-12-30 17:50:58'),
('225', '224', '0', '2', '患者套餐新增', NULL, NULL, '1', NULL, NULL, b'0', b'0', b'0', 'patientTerm:add', 'admin', 'admin', '2020-12-03 16:52:31', '2020-12-30 17:47:08'),
('226', '224', '0', '2', '患者套餐编辑', NULL, NULL, '2', NULL, NULL, b'0', b'0', b'0', 'patientTerm:edit', 'admin', 'admin', '2020-12-03 16:52:49', '2020-12-30 17:47:29'),
('227', '224', '0', '2', '患者套餐删除', NULL, NULL, '3', NULL, NULL, b'0', b'0', b'0', 'patientTerm:del', 'admin', 'admin', '2020-12-03 16:53:05', '2020-12-30 17:47:45'),
('232', '191', '3', '1', '预约管理', 'Reserve', 'yy/reserve/index', '1', 'develop', 'reserve', b'0', b'0', b'0', 'reserve:list', 'admin', 'admin', '2020-12-05 15:09:29', '2020-12-30 17:50:14'),
('233', '232', '0', '2', '预约新增', NULL, NULL, '1', NULL, NULL, b'0', b'0', b'0', 'reserve:add', 'admin', 'admin', '2020-12-05 15:10:22', '2020-12-30 17:48:44'),
('234', '232', '0', '2', '预约编辑', NULL, NULL, '2', NULL, NULL, b'0', b'0', b'0', 'reserve:edit', 'admin', 'admin', '2020-12-05 15:10:43', '2020-12-30 17:48:57'),
('235', '232', '0', '2', '预约删除', NULL, NULL, '3', NULL, NULL, b'0', b'0', b'0', 'reserve:del', 'admin', 'admin', '2020-12-05 15:10:58', '2020-12-30 17:49:10'),
('249', '191', '3', '1', '工作时间管理', 'WorkTime', 'yy/workTime/index', '80', 'date', 'workTime', b'0', b'0', b'0', 'workTime:list', 'admin', 'admin', '2020-12-25 00:24:00', '2020-12-30 17:52:03'),
('250', '249', '0', '2', '工作时间新增', NULL, NULL, '1', NULL, NULL, b'0', b'0', b'0', 'workTime:add', 'admin', 'admin', '2020-12-25 00:24:47', '2020-12-25 00:24:47'),
('251', '249', '0', '2', '工作时间编辑', NULL, NULL, '2', NULL, NULL, b'0', b'0', b'0', 'workTime:edit', 'admin', 'admin', '2020-12-25 00:25:09', '2020-12-25 00:25:09'),
('252', '249', '0', '2', '工作时间删除', NULL, NULL, '3', NULL, NULL, b'0', b'0', b'0', 'workTime:del', 'admin', 'admin', '2020-12-25 00:25:26', '2020-12-25 00:25:26'),
('253', '191', '3', '1', '资源分类', 'ResourceCategory', 'yy/resourceCategory/index', '6', 'dictionary', 'resourceCategory', b'0', b'0', b'0', 'resourceCategory:list', 'admin', 'admin', '2020-12-25 13:07:01', '2020-12-30 17:51:24'),
('254', '253', '0', '2', '资源分类新增', NULL, NULL, '1', NULL, NULL, b'0', b'0', b'0', 'resourceCategory:add', 'admin', 'admin', '2020-12-25 13:07:20', '2020-12-25 13:07:20'),
('255', '253', '0', '2', '资源分类编辑', NULL, NULL, '2', NULL, NULL, b'0', b'0', b'0', 'resourceCategory:edit', 'admin', 'admin', '2020-12-25 13:07:37', '2020-12-25 13:07:37'),
('256', '253', '0', '2', '资源分享删除', NULL, NULL, '3', NULL, NULL, b'0', b'0', b'0', 'resourceCategory:del', 'admin', 'admin', '2020-12-25 13:07:53', '2020-12-25 13:07:53'),
('257', '191', '3', '1', '资源分组', 'ResourceGroup', 'yy/resourceGroup/index', '5', 'date', 'resourceGroup', b'0', b'0', b'0', 'resourceGroup:list', 'admin', 'admin', '2020-12-25 14:55:44', '2020-12-30 17:51:17'),
('258', '257', '0', '2', '资源分组新增', NULL, NULL, '1', NULL, NULL, b'0', b'0', b'0', 'resourceGroup:add', 'admin', 'admin', '2020-12-25 14:56:06', '2020-12-25 14:56:06'),
('259', '257', '0', '2', '资源分组编辑', NULL, NULL, '2', NULL, NULL, b'0', b'0', b'0', 'resourceGroup:edit', 'admin', 'admin', '2020-12-25 14:56:23', '2020-12-25 14:56:23'),
('260', '257', '0', '2', '资源分组删除', NULL, NULL, '3', NULL, NULL, b'0', b'0', b'0', 'resourceGroup:del', 'admin', 'admin', '2020-12-25 14:56:38', '2020-12-25 14:56:38'),
('261', '191', '0', '1', '预约看板', 'ReserveCount', 'yy/reserveCount/index', '0', 'codeConsole', 'reserveCount', b'0', b'0', b'0', 'reserve:list', 'admin', 'admin', '2020-12-28 22:23:59', '2020-12-28 22:23:59');

INSERT INTO `sys_quartz_job` (`job_id`, `bean_name`, `cron_expression`, `is_pause`, `job_name`, `method_name`, `params`, `description`, `person_in_charge`, `email`, `sub_task`, `pause_after_failure`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES
('2', 'testTask', '0/5 * * * * ?', b'1', '测试1', 'run1', 'test', '带参测试，多参使用json', '测试', NULL, NULL, NULL, NULL, 'admin', '2019-08-22 14:08:29', '2020-05-05 17:26:19'),
('3', 'testTask', '0/5 * * * * ?', b'1', '测试', 'run', '', '不带参测试', 'Zheng Jie', '', '2,6', b'1', NULL, 'admin', '2019-09-26 16:44:39', '2020-05-05 20:45:39'),
('5', 'Test', '0/5 * * * * ?', b'1', '任务告警测试', 'run', NULL, '测试', 'test', '', NULL, b'1', 'admin', 'admin', '2020-05-05 20:32:41', '2020-05-05 20:36:13'),
('6', 'testTask', '0/5 * * * * ?', b'1', '测试3', 'run2', NULL, '测试3', 'Zheng Jie', '', NULL, b'1', 'admin', 'admin', '2020-05-05 20:35:41', '2020-05-05 20:36:07');

INSERT INTO `sys_role` (`role_id`, `name`, `level`, `description`, `data_scope`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES
('1', '超级管理员', '1', '-', '全部', NULL, 'admin', '2018-11-23 11:04:37', '2020-12-30 18:40:49'),
('2', '普通用户', '3', '-', '全部', NULL, 'admin', '2018-11-23 13:09:06', '2020-12-30 18:40:44'),
('3', '系统管理员', '2', NULL, '全部', 'admin', 'admin', '2020-11-22 11:01:58', '2020-12-30 18:40:39');

INSERT INTO `sys_roles_menus` (`menu_id`, `role_id`) VALUES
('1', '1'),
('2', '1'),
('3', '1'),
('5', '1'),
('6', '1'),
('7', '1'),
('9', '1'),
('10', '1'),
('11', '1'),
('14', '1'),
('15', '1'),
('16', '1'),
('18', '1'),
('19', '1'),
('21', '1'),
('22', '1'),
('23', '1'),
('24', '1'),
('27', '1'),
('28', '1'),
('30', '1'),
('32', '1'),
('33', '1'),
('34', '1'),
('35', '1'),
('36', '1'),
('37', '1'),
('38', '1'),
('39', '1'),
('41', '1'),
('80', '1'),
('82', '1'),
('83', '1'),
('90', '1'),
('92', '1'),
('93', '1'),
('94', '1'),
('97', '1'),
('98', '1'),
('116', '1'),
('191', '1'),
('191', '2'),
('191', '3'),
('192', '1'),
('192', '2'),
('192', '3'),
('193', '1'),
('193', '2'),
('193', '3'),
('194', '1'),
('194', '2'),
('194', '3'),
('195', '1'),
('195', '2'),
('195', '3'),
('200', '1'),
('200', '2'),
('200', '3'),
('201', '1'),
('201', '2'),
('201', '3'),
('202', '1'),
('202', '2'),
('202', '3'),
('203', '1'),
('203', '2'),
('203', '3'),
('204', '1'),
('204', '2'),
('204', '3'),
('205', '1'),
('205', '2'),
('205', '3'),
('206', '1'),
('206', '2'),
('206', '3'),
('207', '1'),
('207', '2'),
('207', '3'),
('212', '1'),
('212', '2'),
('212', '3'),
('213', '1'),
('213', '2'),
('213', '3'),
('214', '1'),
('214', '2'),
('214', '3'),
('215', '1'),
('215', '2'),
('215', '3'),
('224', '1'),
('224', '2'),
('224', '3'),
('225', '1'),
('225', '2'),
('225', '3'),
('226', '1'),
('226', '2'),
('226', '3'),
('227', '1'),
('227', '2'),
('227', '3'),
('232', '1'),
('232', '2'),
('232', '3'),
('233', '1'),
('233', '2'),
('233', '3'),
('234', '1'),
('234', '2'),
('234', '3'),
('235', '1'),
('235', '2'),
('235', '3'),
('249', '1'),
('249', '2'),
('249', '3'),
('250', '1'),
('250', '2'),
('250', '3'),
('251', '1'),
('251', '2'),
('251', '3'),
('252', '1'),
('252', '2'),
('252', '3'),
('253', '1'),
('253', '2'),
('253', '3'),
('254', '1'),
('254', '2'),
('254', '3'),
('255', '1'),
('255', '2'),
('255', '3'),
('256', '1'),
('256', '2'),
('256', '3'),
('257', '1'),
('257', '2'),
('257', '3'),
('258', '1'),
('258', '2'),
('258', '3'),
('259', '1'),
('259', '2'),
('259', '3'),
('260', '1'),
('260', '2'),
('260', '3'),
('261', '1'),
('261', '2'),
('261', '3');

INSERT INTO `sys_user` (`user_id`, `dept_id`, `username`, `nick_name`, `gender`, `phone`, `email`, `avatar_name`, `avatar_path`, `password`, `is_admin`, `enabled`, `create_by`, `update_by`, `pwd_reset_time`, `create_time`, `update_time`) VALUES
('1', '27', 'admin', '管理员', '男', '18888888888', '201507802@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'1', '1', NULL, 'admin', '2020-05-03 16:38:31', '2018-08-23 09:11:56', '2020-12-05 17:00:13'),
('2', '27', 'test', '测试', '男', '18888888888', '231@qq.com', NULL, NULL, '$2a$10$4XcyudOYTSz6fue6KFNMHeUQnCX5jbBQypLEnGk1PmekXt5c95JcK', b'0', '1', 'admin', 'admin', NULL, '2020-05-05 11:15:49', '2020-12-05 17:00:20'),
('3', '32', 'duan', 'duan', '男', '13300000000', 'duan@qq.com', NULL, NULL, '$2a$10$1SvFmdgO7S.8cyp/ubsJSu6dZgvqfoKZAhIb92dt92fzWJAhcg/Ei', b'0', '1', 'admin', 'admin', NULL, '2020-12-21 11:02:30', '2020-12-30 17:43:17');

INSERT INTO `sys_users_jobs` (`user_id`, `job_id`) VALUES
('1', '13'),
('1', '14'),
('1', '15'),
('1', '16'),
('2', '13'),
('2', '14'),
('2', '15'),
('3', '16');

INSERT INTO `sys_users_roles` (`user_id`, `role_id`) VALUES
('1', '1'),
('2', '2'),
('3', '3');

INSERT INTO `tool_alipay_config` (`config_id`, `app_id`, `charset`, `format`, `gateway_url`, `notify_url`, `private_key`, `public_key`, `return_url`, `sign_type`, `sys_service_provider_id`) VALUES
('1', '2016091700532697', 'utf-8', 'JSON', 'https://openapi.alipaydev.com/gateway.do', 'http://api.auauz.net/api/aliPay/notify', 'MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC5js8sInU10AJ0cAQ8UMMyXrQ+oHZEkVt5lBwsStmTJ7YikVYgbskx1YYEXTojRsWCb+SH/kDmDU4pK/u91SJ4KFCRMF2411piYuXU/jF96zKrADznYh/zAraqT6hvAIVtQAlMHN53nx16rLzZ/8jDEkaSwT7+HvHiS+7sxSojnu/3oV7BtgISoUNstmSe8WpWHOaWv19xyS+Mce9MY4BfseFhzTICUymUQdd/8hXA28/H6osUfAgsnxAKv7Wil3aJSgaJczWuflYOve0dJ3InZkhw5Cvr0atwpk8YKBQjy5CdkoHqvkOcIB+cYHXJKzOE5tqU7inSwVbHzOLQ3XbnAgMBAAECggEAVJp5eT0Ixg1eYSqFs9568WdetUNCSUchNxDBu6wxAbhUgfRUGZuJnnAll63OCTGGck+EGkFh48JjRcBpGoeoHLL88QXlZZbC/iLrea6gcDIhuvfzzOffe1RcZtDFEj9hlotg8dQj1tS0gy9pN9g4+EBH7zeu+fyv+qb2e/v1l6FkISXUjpkD7RLQr3ykjiiEw9BpeKb7j5s7Kdx1NNIzhkcQKNqlk8JrTGDNInbDM6inZfwwIO2R1DHinwdfKWkvOTODTYa2MoAvVMFT9Bec9FbLpoWp7ogv1JMV9svgrcF9XLzANZ/OQvkbe9TV9GWYvIbxN6qwQioKCWO4GPnCAQKBgQDgW5MgfhX8yjXqoaUy/d1VjI8dHeIyw8d+OBAYwaxRSlCfyQ+tieWcR2HdTzPca0T0GkWcKZm0ei5xRURgxt4DUDLXNh26HG0qObbtLJdu/AuBUuCqgOiLqJ2f1uIbrz6OZUHns+bT/jGW2Ws8+C13zTCZkZt9CaQsrp3QOGDx5wKBgQDTul39hp3ZPwGNFeZdkGoUoViOSd5Lhowd5wYMGAEXWRLlU8z+smT5v0POz9JnIbCRchIY2FAPKRdVTICzmPk2EPJFxYTcwaNbVqL6lN7J2IlXXMiit5QbiLauo55w7plwV6LQmKm9KV7JsZs5XwqF7CEovI7GevFzyD3w+uizAQKBgC3LY1eRhOlpWOIAhpjG6qOoohmeXOphvdmMlfSHq6WYFqbWwmV4rS5d/6LNpNdL6fItXqIGd8I34jzql49taCmi+A2nlR/E559j0mvM20gjGDIYeZUz5MOE8k+K6/IcrhcgofgqZ2ZED1ksHdB/E8DNWCswZl16V1FrfvjeWSNnAoGAMrBplCrIW5xz+J0Hm9rZKrs+AkK5D4fUv8vxbK/KgxZ2KaUYbNm0xv39c+PZUYuFRCz1HDGdaSPDTE6WeWjkMQd5mS6ikl9hhpqFRkyh0d0fdGToO9yLftQKOGE/q3XUEktI1XvXF0xyPwNgUCnq0QkpHyGVZPtGFxwXiDvpvgECgYA5PoB+nY8iDiRaJNko9w0hL4AeKogwf+4TbCw+KWVEn6jhuJa4LFTdSqp89PktQaoVpwv92el/AhYjWOl/jVCm122f9b7GyoelbjMNolToDwe5pF5RnSpEuDdLy9MfE8LnE3PlbE7E5BipQ3UjSebkgNboLHH/lNZA5qvEtvbfvQ==', 'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAut9evKRuHJ/2QNfDlLwvN/S8l9hRAgPbb0u61bm4AtzaTGsLeMtScetxTWJnVvAVpMS9luhEJjt+Sbk5TNLArsgzzwARgaTKOLMT1TvWAK5EbHyI+eSrc3s7Awe1VYGwcubRFWDm16eQLv0k7iqiw+4mweHSz/wWyvBJVgwLoQ02btVtAQErCfSJCOmt0Q/oJQjj08YNRV4EKzB19+f5A+HQVAKy72dSybTzAK+3FPtTtNen/+b5wGeat7c32dhYHnGorPkPeXLtsqqUTp1su5fMfd4lElNdZaoCI7osZxWWUo17vBCZnyeXc9fk0qwD9mK6yRAxNbrY72Xx5VqIqwIDAQAB', 'http://api.auauz.net/api/aliPay/return', 'RSA2', '2088102176044281');

INSERT INTO `yy_patient` (`id`, `code`, `mrn`, `name`, `phone`, `status`, `remark`) VALUES
('5', '243592153', '353045', '瞿佳卉', '18782995300', NULL, NULL);

INSERT INTO `yy_patient_term` (`id`, `patient_id`, `term_code`, `term_name`, `term_price`, `term_original_price`, `term_times`, `term_unit`, `price`, `times`, `status`, `remark`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES
('17', '5', 'ZLA073008006', '产后康复（仪器+手法）-腹直肌分离治疗', '30000', '30000', '1', '每部位', '200000', '10', NULL, NULL, '2020-12-30 18:43:10', NULL, '2020-12-30 18:43:10', NULL),
('18', '5', 'ZLC060010', '骨盆修复', '396000', '396000', '1', '次', '68000', '1', NULL, NULL, '2020-12-30 18:43:10', NULL, '2020-12-30 18:43:10', NULL),
('19', '5', 'ZLA070006', '盆底修复', '49800', '49800', '1', '次', '200000', '9', NULL, NULL, '2020-12-30 18:43:10', NULL, '2020-12-30 18:43:22', NULL);

INSERT INTO `yy_patient_term_log` (`id`, `patient_term_id`, `content`, `type`, `before`, `after`, `create_time`, `create_by`) VALUES
('26', '19', '预约新增', 'times', '10', '9', '2020-12-30 18:43:22', NULL);

INSERT INTO `yy_reserve` (`id`, `dept_id`, `patient_id`, `term_id`, `patient_term_id`, `resource_group_id`, `work_time_id`, `date`, `begin_time`, `end_time`, `status`, `remark`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES
('24', '32', '5', '21', '19', '38', '4', '2020-12-30', '08:00', '08:30', 'init', NULL, '2020-12-30 18:43:22', NULL, '2020-12-30 18:43:22', NULL);

INSERT INTO `yy_reserve_resource` (`id`, `reserve_id`, `resource_category_id`, `resource_group_id`, `resource_id`) VALUES
('15', '24', '10', '38', NULL);

INSERT INTO `yy_resource` (`id`, `dept_id`, `resource_category_id`, `name`, `count`, `status`, `remark`) VALUES
('1', '27', '2', '分离机 001', '1', NULL, NULL),
('3', '32', '5', 'INDIBA', '1', NULL, NULL),
('4', '32', '6', '产后塑形仪', '4', NULL, NULL),
('5', '32', '7', '菲蜜丽', '1', NULL, NULL),
('6', '32', '8', '骨盆理疗床', '2', NULL, NULL),
('7', '32', '9', '理疗师001', '1', NULL, NULL),
('8', '32', '10', '盆底修复A', '2', NULL, NULL),
('9', '32', '11', '盆底修复B', '2', NULL, NULL),
('10', '32', '12', '中医医生001', '1', NULL, NULL),
('11', '32', '12', '中医医生002', '1', NULL, NULL);

INSERT INTO `yy_resource_category` (`id`, `dept_id`, `name`, `count`, `status`, `remark`) VALUES
('5', '32', 'INDIBA', '1', NULL, NULL),
('6', '32', '产后塑形仪', '4', NULL, NULL),
('7', '32', '菲蜜丽', '1', NULL, NULL),
('8', '32', '骨盆理疗床', '2', NULL, NULL),
('9', '32', '理疗师', '1', NULL, NULL),
('10', '32', '盆底修复A', '2', NULL, NULL),
('11', '32', '盆底修复B', '2', NULL, NULL),
('12', '32', '中医医生', '2', NULL, NULL);

INSERT INTO `yy_resource_group` (`id`, `dept_id`, `name`, `status`, `remark`) VALUES
('1', '27', '腹直肌分离资源组', NULL, NULL),
('3', '27', '套餐 2 资源分组', NULL, NULL),
('33', '32', '产后塑形仪', NULL, NULL),
('34', '32', 'INDIBA', NULL, NULL),
('35', '32', '菲蜜丽', NULL, NULL),
('36', '32', '骨盆理疗床', NULL, NULL),
('37', '32', '理疗师', NULL, NULL),
('38', '32', '盆底修复A', NULL, NULL),
('39', '32', '盆底修复B', NULL, NULL),
('40', '32', '中医医生', NULL, NULL);

INSERT INTO `yy_resource_group_category` (`resource_group_id`, `resource_category_id`) VALUES
('1', '2'),
('3', '4'),
('33', '6'),
('34', '5'),
('35', '7'),
('36', '8'),
('37', '9'),
('38', '10'),
('39', '11'),
('40', '12');

INSERT INTO `yy_sms` (`id`, `bus_id`, `bus_type`, `mobile`, `content`, `cell`, `status`, `remark`, `send_time`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES
('2', '1', 'reserve', '13300000001', '您预约的套餐[套餐1], 预约时间2020-12-29 08:30, 已签到成功, 科技公司', NULL, 'init', NULL, '2020-12-28 15:07:24', '2020-12-28 15:07:24', NULL, '2020-12-28 15:07:24', NULL),
('3', '1', 'reserve', '13300000001', '您预约的套餐[套餐1], 预约时间2020-12-29 08:30, 已签到成功, 科技公司', NULL, 'init', NULL, '2020-12-28 15:11:34', '2020-12-28 15:11:34', NULL, '2020-12-28 15:11:34', NULL),
('4', '1', 'reserve', '13300000001', '您预约的套餐[套餐1], 预约时间 2020-12-29 08:30, 已使用成功, 感谢您的信任, 科技公司', NULL, 'init', NULL, '2020-12-28 17:21:21', '2020-12-28 17:21:21', NULL, '2020-12-28 17:21:21', NULL),
('5', '1', 'reserve', '13300000001', '您预约的套餐[套餐1], 预约时间2020-12-29 08:30, 已签到成功, 科技公司', NULL, 'init', NULL, '2020-12-28 17:23:32', '2020-12-28 17:23:32', NULL, '2020-12-28 17:23:32', NULL),
('6', '2', 'reserve', '13300000001', '您购买的套餐[套餐 2], 已预约成功, 请于2020-12-29 08:30前到我院使用, 感谢您的信任, 科技公司', NULL, 'init', NULL, '2020-12-28 17:42:06', '2020-12-28 17:42:06', NULL, '2020-12-28 17:42:06', NULL),
('7', '2', 'reserve', '13300000001', '您预约的套餐[套餐 2], 预约时间2020-12-29 08:30, 已签到成功, 科技公司', NULL, 'init', NULL, '2020-12-28 17:42:22', '2020-12-28 17:42:22', NULL, '2020-12-28 17:42:22', NULL),
('8', '3', 'reserve', '13300000001', '您购买的套餐[套餐1], 已预约成功, 请于2020-12-29 08:30前到我院使用, 感谢您的信任, 科技公司', NULL, 'init', NULL, '2020-12-28 17:49:19', '2020-12-28 17:49:19', NULL, '2020-12-28 17:49:19', NULL),
('9', '3', 'reserve', '13300000001', '您预约的套餐[套餐1], 预约时间2020-12-29 08:30, 已签到成功, 科技公司', NULL, 'init', NULL, '2020-12-28 17:49:29', '2020-12-28 17:49:29', NULL, '2020-12-28 17:49:29', NULL),
('10', '3', 'reserve', '13300000001', '您预约的套餐[套餐1], 预约时间 2020-12-29 08:30, 已使用成功, 感谢您的信任, 科技公司', NULL, 'init', NULL, '2020-12-28 17:50:39', '2020-12-28 17:50:39', NULL, '2020-12-28 17:50:39', NULL),
('11', '4', 'reserve', '13300000001', '您购买的套餐[套餐1], 已预约成功, 请于2020-12-29 08:00前到我院使用, 感谢您的信任, 科技公司', NULL, 'init', NULL, '2020-12-28 17:51:51', '2020-12-28 17:51:51', NULL, '2020-12-28 17:51:51', NULL),
('12', '4', 'reserve', '13300000001', '您预约的套餐[套餐1], 预约时间2020-12-29 08:00, 已签到成功, 科技公司', NULL, 'init', NULL, '2020-12-28 17:55:17', '2020-12-28 17:55:17', NULL, '2020-12-28 17:55:17', NULL),
('13', '4', 'reserve', '13300000001', '您预约的套餐[套餐1], 预约时间 2020-12-29 08:00, 已使用成功, 感谢您的信任, 科技公司', NULL, 'init', NULL, '2020-12-28 17:55:29', '2020-12-28 17:55:29', NULL, '2020-12-28 17:55:29', NULL),
('14', '5', 'reserve', '18782995300', '您购买的套餐[盆底修复], 已预约成功, 请于2020-12-29 08:00前到我院使用, 感谢您的信任, 成都妇幼保健医院', NULL, 'init', NULL, '2020-12-29 15:52:42', '2020-12-29 15:52:42', NULL, '2020-12-29 15:52:42', NULL),
('15', '6', 'reserve', '18782995300', '您购买的套餐[骨盆修复], 已预约成功, 请于2020-12-30 08:00前到我院使用, 感谢您的信任, 成都妇幼保健医院', NULL, 'init', NULL, '2020-12-29 16:10:07', '2020-12-29 16:10:07', NULL, '2020-12-29 16:10:07', NULL),
('16', '5', 'reserve', '18782995300', '您预约的套餐[盆底修复], 预约时间2020-12-29 08:00, 已签到成功, 成都妇幼保健医院', NULL, 'init', NULL, '2020-12-29 16:18:34', '2020-12-29 16:18:34', NULL, '2020-12-29 16:18:34', NULL),
('17', '7', 'reserve', '18782995300', '您购买的套餐[骨盆修复], 已预约成功, 请于2020-12-29 08:00前到我院使用, 感谢您的信任, 成都妇幼保健医院', NULL, 'init', NULL, '2020-12-29 18:31:56', '2020-12-29 18:31:56', NULL, '2020-12-29 18:31:56', NULL),
('18', '8', 'reserve', '18782995300', '您购买的套餐[盆底修复], 已预约成功, 请于2020-12-29 08:00前到我院使用, 感谢您的信任, 成都妇幼保健医院', NULL, 'init', NULL, '2020-12-29 18:40:08', '2020-12-29 18:40:08', NULL, '2020-12-29 18:40:08', NULL),
('19', '10', 'reserve', '18782995300', '您购买的套餐[盆底修复], 已预约成功, 请于2020-12-30 08:00前到我院使用, 感谢您的信任, 成都妇幼保健医院', NULL, 'init', NULL, '2020-12-30 13:17:05', '2020-12-30 13:17:05', NULL, '2020-12-30 13:17:05', NULL),
('20', '11', 'reserve', '18782995300', '您购买的套餐[盆底修复], 已预约成功, 请于2020-12-30 08:00前到我院使用, 感谢您的信任, 成都妇幼保健医院', NULL, 'init', NULL, '2020-12-30 13:34:38', '2020-12-30 13:34:38', NULL, '2020-12-30 13:34:38', NULL),
('21', '12', 'reserve', '18782995300', '您购买的套餐[盆底修复], 已预约成功, 请于2020-12-30 08:00前到我院使用, 感谢您的信任, 成都妇幼保健医院', NULL, 'init', NULL, '2020-12-30 13:34:56', '2020-12-30 13:34:56', NULL, '2020-12-30 13:34:56', NULL),
('22', '13', 'reserve', '18782995300', '您购买的套餐[盆底修复], 已预约成功, 请于2020-12-30 08:00前到我院使用, 感谢您的信任, 成都妇幼保健医院', NULL, 'init', NULL, '2020-12-30 13:35:14', '2020-12-30 13:35:14', NULL, '2020-12-30 13:35:14', NULL),
('23', '14', 'reserve', '18782995300', '您购买的套餐[盆底修复], 已预约成功, 请于2020-12-30 08:00前到我院使用, 感谢您的信任, 成都妇幼保健医院', NULL, 'init', NULL, '2020-12-30 13:35:28', '2020-12-30 13:35:28', NULL, '2020-12-30 13:35:28', NULL),
('24', '14', 'reserve', '18782995300', '您预约的套餐[盆底修复], 预约时间2020-12-30 08:00, 已签到成功, 成都妇幼保健医院', NULL, 'init', NULL, '2020-12-30 13:36:42', '2020-12-30 13:36:42', NULL, '2020-12-30 13:36:42', NULL),
('25', '14', 'reserve', '18782995300', '您预约的套餐[盆底修复], 预约时间 2020-12-30 08:00, 已使用成功, 感谢您的信任, 成都妇幼保健医院', NULL, 'init', NULL, '2020-12-30 14:13:36', '2020-12-30 14:13:36', NULL, '2020-12-30 14:13:36', NULL),
('26', '13', 'reserve', '18782995300', '您预约的套餐[盆底修复], 预约时间2020-12-30 08:00, 已签到成功, 成都妇幼保健医院', NULL, 'init', NULL, '2020-12-30 14:21:20', '2020-12-30 14:21:20', NULL, '2020-12-30 14:21:20', NULL),
('27', '13', 'reserve', '18782995300', '您预约的套餐[盆底修复], 预约时间 2020-12-30 08:00, 已使用成功, 感谢您的信任, 成都妇幼保健医院', NULL, 'init', NULL, '2020-12-30 14:21:33', '2020-12-30 14:21:33', NULL, '2020-12-30 14:21:33', NULL),
('28', '12', 'reserve', '18782995300', '您预约的套餐[盆底修复], 预约时间2020-12-30 08:00, 已签到成功, 成都妇幼保健医院', NULL, 'init', NULL, '2020-12-30 14:26:26', '2020-12-30 14:26:26', NULL, '2020-12-30 14:26:26', NULL),
('29', '12', 'reserve', '18782995300', '您预约的套餐[盆底修复], 预约时间 2020-12-30 08:00, 已使用成功, 感谢您的信任, 成都妇幼保健医院', NULL, 'init', NULL, '2020-12-30 14:26:59', '2020-12-30 14:26:59', NULL, '2020-12-30 14:26:59', NULL),
('30', '11', 'reserve', '18782995300', '您预约的套餐[盆底修复], 预约时间2020-12-30 08:00, 已签到成功, 成都妇幼保健医院', NULL, 'init', NULL, '2020-12-30 14:31:15', '2020-12-30 14:31:15', NULL, '2020-12-30 14:31:15', NULL),
('31', '11', 'reserve', '18782995300', '您预约的套餐[盆底修复], 预约时间 2020-12-30 08:00, 已使用成功, 感谢您的信任, 成都妇幼保健医院', NULL, 'init', NULL, '2020-12-30 14:31:17', '2020-12-30 14:31:17', NULL, '2020-12-30 14:31:17', NULL),
('32', '15', 'reserve', '18782995300', '您购买的套餐[盆底修复], 已预约成功, 请于2020-12-30 08:00前到我院使用, 感谢您的信任, 成都妇幼保健医院', NULL, 'init', NULL, '2020-12-30 15:48:17', '2020-12-30 15:48:17', NULL, '2020-12-30 15:48:17', NULL),
('33', '16', 'reserve', '18782995300', '您购买的套餐[盆底修复], 已预约成功, 请于2020-12-30 08:00前到我院使用, 感谢您的信任, 成都妇幼保健医院', NULL, 'init', NULL, '2020-12-30 16:47:22', '2020-12-30 16:47:22', NULL, '2020-12-30 16:47:22', NULL),
('34', '17', 'reserve', '18782995300', '您购买的套餐[盆底修复], 已预约成功, 请于2020-12-30 10:30前到我院使用, 感谢您的信任, 成都妇幼保健医院', NULL, 'init', NULL, '2020-12-30 16:47:31', '2020-12-30 16:47:31', NULL, '2020-12-30 16:47:31', NULL),
('35', '18', 'reserve', '18782995300', '您购买的套餐[盆底修复], 已预约成功, 请于2020-12-30 08:30前到我院使用, 感谢您的信任, 成都妇幼保健医院', NULL, 'init', NULL, '2020-12-30 16:49:50', '2020-12-30 16:49:50', NULL, '2020-12-30 16:49:50', NULL),
('36', '19', 'reserve', '18782995300', '您购买的套餐[盆底修复], 已预约成功, 请于2020-12-30 09:30前到我院使用, 感谢您的信任, 成都妇幼保健医院', NULL, 'init', NULL, '2020-12-30 16:52:44', '2020-12-30 16:52:44', NULL, '2020-12-30 16:52:44', NULL),
('37', '20', 'reserve', '18782995300', '您购买的套餐[盆底修复], 已预约成功, 请于2020-12-30 09:00前到我院使用, 感谢您的信任, 成都妇幼保健医院', NULL, 'init', NULL, '2020-12-30 16:56:14', '2020-12-30 16:56:14', NULL, '2020-12-30 16:56:14', NULL),
('38', '21', 'reserve', '18782995300', '您购买的套餐[盆底修复], 已预约成功, 请于2020-12-30 10:00前到我院使用, 感谢您的信任, 成都妇幼保健医院', NULL, 'init', NULL, '2020-12-30 16:58:40', '2020-12-30 16:58:40', NULL, '2020-12-30 16:58:40', NULL),
('39', '22', 'reserve', '18782995300', '您购买的套餐[盆底修复], 已预约成功, 请于2020-12-30 11:00前到我院使用, 感谢您的信任, 成都妇幼保健医院', NULL, 'init', NULL, '2020-12-30 17:01:44', '2020-12-30 17:01:44', NULL, '2020-12-30 17:01:44', NULL),
('40', '22', 'reserve', '18782995300', '您预约的套餐[盆底修复], 预约时间2020-12-30 11:00, 已签到成功, 成都妇幼保健医院', NULL, 'init', NULL, '2020-12-30 17:43:52', '2020-12-30 17:43:52', NULL, '2020-12-30 17:43:52', NULL),
('41', '22', 'reserve', '18782995300', '您预约的套餐[盆底修复], 预约时间 2020-12-30 11:00, 已使用成功, 感谢您的信任, 成都妇幼保健医院', NULL, 'init', NULL, '2020-12-30 17:43:58', '2020-12-30 17:43:58', NULL, '2020-12-30 17:43:58', NULL),
('42', '22', 'reserve', '18782995300', '您预约的套餐[盆底修复], 预约时间2020-12-30 11:00, 已签到成功, 成都妇幼保健医院', NULL, 'init', NULL, '2020-12-30 17:44:07', '2020-12-30 17:44:07', NULL, '2020-12-30 17:44:07', NULL),
('43', '23', 'reserve', '18782995300', '您购买的套餐[盆底修复], 已预约成功, 请于2020-12-31 08:00前到我院使用, 感谢您的信任, 成都妇幼保健医院', NULL, 'init', NULL, '2020-12-30 17:53:52', '2020-12-30 17:53:52', NULL, '2020-12-30 17:53:52', NULL),
('44', '24', 'reserve', '18782995300', '您购买的套餐[盆底修复], 已预约成功, 请于2020-12-30 08:00前到我院使用, 感谢您的信任, 成都妇幼保健医院', NULL, 'init', NULL, '2020-12-30 18:43:22', '2020-12-30 18:43:22', NULL, '2020-12-30 18:43:22', NULL);

INSERT INTO `yy_term` (`id`, `dept_id`, `code`, `name`, `price`, `original_price`, `times`, `unit`, `status`, `remark`) VALUES
('1', '27', '01', '套餐 1', '100', '100', '10', '次', NULL, NULL),
('2', '27', '02', '套餐 2', '200', '2000', '20', '次', NULL, NULL),
('3', '32', 'ZLC070100', '魔力射频治疗-身体', '168000', '168000', '1', '次', NULL, NULL),
('4', '32', 'ZLC060031', 'INDIBA腿部', '228000', '228000', '1', '次', NULL, NULL),
('5', '32', 'ZLA073008', '产后康复（仪器+手法）', '30000', '30000', '1', '次', NULL, NULL),
('6', '32', 'ZLA073008004', '产后康复（仪器+手法）-产后排残乳', '56000', '56000', '1', '次', NULL, NULL),
('7', '32', 'ZLA073008001', '产后康复（仪器+手法）-产后塑形', '30000', '30000', '1', '每部位', NULL, NULL),
('8', '32', 'ZLA073008006', '产后康复（仪器+手法）-腹直肌分离治疗', '30000', '30000', '1', '每部位', NULL, NULL),
('9', '32', 'ZLA073008007', '产后康复（仪器+手法）-肩颈理疗', '36000', '36000', '1', '每部位', NULL, NULL),
('10', '32', 'ZLA073008008', '产后康复（仪器+手法）-腰腿疼痛', '30000', '30000', '1', '每部位', NULL, NULL),
('11', '32', 'ZLA073008002', '产后康复（仪器+手法）-子宫复旧', '36000', '36000', '1', '每部位', NULL, NULL),
('12', '32', 'ZLA070002', '催乳', '58000', '58000', '1', '次', NULL, NULL),
('13', '32', 'ZLA070001', '乳房按摩', '10000', '10000', '1', '次', NULL, NULL),
('14', '32', 'ZLA070015', '乳房塑形', '48000', '48000', '1', '次', NULL, NULL),
('15', '32', 'ZLA070003', '子宫复旧', '48000', '48000', '1', '次', NULL, NULL),
('16', '32', 'ZLC140019', '阴道激光治疗', '840000', '840000', '1', '次', NULL, NULL),
('17', '32', 'ZLC140020', '外阴激光治疗', '480000', '480000', '1', '次', NULL, NULL),
('18', '32', 'ZLC140021', '乳头激光治疗', '480000', '480000', '1', '次', NULL, NULL),
('19', '32', 'ZLC060010', '骨盆修复', '396000', '396000', '1', '次', NULL, NULL),
('20', '32', 'ZLA073011', '通乳', '38000', '38000', '1', '人次', NULL, NULL),
('21', '32', 'ZLA070006', '盆底修复', '49800', '49800', '1', '次', NULL, NULL),
('22', '32', 'ZLC150001', '成人针灸疗法', '20000', '20000', '1', '次', NULL, NULL),
('23', '32', 'ZLA073010', '通乳', '58000', '58000', '1', '人次', NULL, NULL),
('24', '32', 'ZLC150019', '药浴', '20000', '20000', '1', '次', NULL, NULL),
('25', '32', 'ZLC150017', '孕妇局部推拿', '16000', '16000', '1', '部位', NULL, NULL),
('26', '32', 'ZLC150018', '孕妇全身推拿', '52000', '52000', '1', '次', NULL, NULL),
('27', '32', 'ZLC153027', '孕妇身体局部疼痛缓解', '16000', '16000', '1', '项', NULL, NULL);

INSERT INTO `yy_term_resource_group` (`term_id`, `resource_group_id`) VALUES
('1', '1'),
('2', '3'),
('3', '34'),
('4', '34'),
('5', '33'),
('6', '33'),
('7', '33'),
('8', '33'),
('9', '33'),
('10', '33'),
('11', '33'),
('12', '33'),
('13', '33'),
('14', '33'),
('15', '33'),
('16', '35'),
('17', '35'),
('18', '35'),
('19', '36'),
('20', '37'),
('21', '38'),
('21', '39'),
('22', '40'),
('23', '40'),
('24', '40'),
('25', '40'),
('26', '40'),
('27', '40');

INSERT INTO `yy_work_time` (`id`, `dept_id`, `begin_time`, `end_time`, `status`, `remark`) VALUES
('2', '27', '08:00', '08:30', NULL, NULL),
('3', '27', '08:30', '09:00', NULL, NULL),
('4', '32', '08:00', '08:30', NULL, NULL),
('5', '32', '08:30', '09:00', NULL, NULL),
('6', '32', '09:00', '09:30', NULL, NULL),
('7', '32', '09:30', '10:00', NULL, NULL),
('8', '32', '10:00', '10:30', NULL, NULL),
('9', '32', '10:30', '11:00', NULL, NULL),
('10', '32', '11:00', '11:30', NULL, NULL),
('11', '32', '11:30', '12:00', NULL, NULL),
('12', '32', '12:00', '12:30', NULL, NULL),
('13', '32', '12:30', '13:00', NULL, NULL),
('14', '32', '13:00', '13:30', NULL, NULL),
('15', '32', '13:30', '14:00', NULL, NULL),
('16', '32', '14:00', '14:30', NULL, NULL),
('17', '32', '14:30', '15:00', NULL, NULL),
('18', '32', '15:00', '15:30', NULL, NULL),
('19', '32', '15:30', '16:00', NULL, NULL),
('20', '32', '16:00', '16:30', NULL, NULL),
('21', '32', '16:30', '17:00', NULL, NULL),
('22', '32', '17:00', '17:30', NULL, NULL),
('23', '32', '17:30', '18:00', NULL, NULL),
('24', '32', '18:00', '18:30', NULL, NULL);



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;