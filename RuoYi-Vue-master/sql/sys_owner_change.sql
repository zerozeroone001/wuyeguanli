
CREATE TABLE `sys_owner_change` (
  `change_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '变更ID',
  `community_id` bigint(20) NOT NULL COMMENT '小区ID',
  `property_id` bigint(20) NOT NULL COMMENT '房产ID',
  `old_owner_name` varchar(50) DEFAULT NULL COMMENT '原业主姓名',
  `old_owner_phone` varchar(20) DEFAULT NULL COMMENT '原业主电话',
  `new_owner_name` varchar(50) NOT NULL COMMENT '新业主姓名',
  `new_owner_phone` varchar(20) NOT NULL COMMENT '新业主电话',
  `new_owner_id_card` varchar(20) DEFAULT NULL COMMENT '新业主身份证号',
  `property_cert_image` varchar(500) DEFAULT NULL COMMENT '房产证图片',
  `status` char(1) DEFAULT '0' COMMENT '状态（0待审核 1通过 2驳回）',
  `audit_remark` varchar(500) DEFAULT NULL COMMENT '审核备注',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  PRIMARY KEY (`change_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='业主变更申请表';
