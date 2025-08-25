CREATE TABLE `sys_visitor` (
  `visitor_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '访客ID',
  `visitor_name` varchar(30) NOT NULL COMMENT '访客姓名',
  `visitor_phone` varchar(11) NOT NULL COMMENT '访客手机号',
  `id_card` varchar(18) DEFAULT NULL COMMENT '身份证号',
  `visit_date` datetime DEFAULT NULL COMMENT '来访日期',
  `reason` varchar(255) NOT NULL COMMENT '事由',
  `license_plate` varchar(8) DEFAULT NULL COMMENT '车牌号',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0待审核 1已通过 2已拒绝）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`visitor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='访客登记表';
