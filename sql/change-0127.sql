
-- HIS查询日志
DROP TABLE IF EXISTS `yy_his_log`;
CREATE TABLE `yy_his_log`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `pat_item_id`  bigint(20) DEFAULT NULL COMMENT '项目购买ID',
    `visit_id`     bigint(20) DEFAULT NULL COMMENT '就诊ID',
    `patient_id`   bigint(20) DEFAULT NULL COMMENT '患者ID',
    `name`         varchar(50)        DEFAULT NULL COMMENT '患者姓名',
    `mobile_phone` varchar(50)        DEFAULT NULL COMMENT '患者电话',
    `mrn`          varchar(100)       DEFAULT NULL COMMENT '患者档案编号',
    `visit_dept`   varchar(255)       DEFAULT NULL COMMENT '就诊科室',
    `visit_date`   timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '就诊日期',
    `item_code`    varchar(255)       DEFAULT NULL COMMENT '项目编码',
    `item_name`    varchar(255)       DEFAULT NULL COMMENT '项目名称',
    `price`        bigint(20) DEFAULT NULL COMMENT '单价',
    `amount`       bigint(20) DEFAULT NULL COMMENT '数量',
    `unit`         varchar(255)       DEFAULT NULL COMMENT '单位',
    `costs`        bigint(20) DEFAULT NULL COMMENT '应收金额',
    `actual_costs` bigint(20) DEFAULT NULL COMMENT '实收金额',
    `create_by`    varchar(255)       DEFAULT NULL COMMENT '创建人',
    `update_by`    varchar(255)       DEFAULT NULL COMMENT '修改人',
    `create_time`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `remark`       varchar(255)       DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='HIS查询记录';

-- 患者项目增加购买 id
alter table yy_patient_term add pat_item_id varchar(100) default null comment '购买ID' after id;
