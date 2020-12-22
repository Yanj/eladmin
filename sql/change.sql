

alter table `sys_dept` add `level` int(5) comment '级别';
ALTER TABLE `sys_dept` change `level` `level` int(5) NULL COMMENT '级别' after sub_count;







