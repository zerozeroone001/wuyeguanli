CREATE TABLE `sys_warranty` (
  `warranty_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '保修ID',
  `equipment_name` varchar(255) NOT NULL COMMENT '设备名称',
  `equipment_location` varchar(255) NOT NULL COMMENT '设备位置',
  `description` text NOT NULL COMMENT '问题描述',
  `reporter_name` varchar(30) NOT NULL COMMENT '上报人姓名',
  `reporter_phone` varchar(11) NOT NULL COMMENT '上报人电话',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0待处理 1处理中 2已完成）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`warranty_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='设备保修表';
