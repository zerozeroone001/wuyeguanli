-- 1. Create property application table
CREATE TABLE `sys_estate_user_apply` (
  `apply_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '申请ID',
  `user_id` bigint(20) NOT NULL COMMENT '申请用户ID',
  `community_id` bigint(20) NOT NULL COMMENT '小区ID',
  `building_name` varchar(50) NOT NULL COMMENT '楼栋名称',
  `unit_name` varchar(50) DEFAULT NULL COMMENT '单元名称',
  `room_number` varchar(50) NOT NULL COMMENT '房号',
  `real_name` varchar(50) NOT NULL COMMENT '真实姓名',
  `id_card_no` varchar(18) DEFAULT NULL COMMENT '身份证号',
  `phonenumber` varchar(11) NOT NULL COMMENT '手机号码',
  `id_card_front_url` varchar(255) DEFAULT NULL COMMENT '身份证正面',
  `id_card_back_url` varchar(255) DEFAULT NULL COMMENT '身份证背面',
  `status` char(1) DEFAULT '0' COMMENT '审核状态(0待审核 1通过 2拒绝)',
  `audit_remark` varchar(500) DEFAULT NULL COMMENT '审核备注',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志(0代表存在 2代表删除)',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`apply_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='房产认证申请表';

-- 2. Backup sys_owner_profile before modification (Just in case)
CREATE TABLE `sys_owner_profile_bk_20251207` AS SELECT * FROM `sys_owner_profile`;

-- 3. Modify sys_owner_profile to be minimal (Owner Registry)
-- Note: In a real production scenario, we would migrate data to sys_estate_user_apply first.
-- Here we will just DROP the columns as requested, assuming new data flow.
-- BUT, to prevent total data loss of "who is who", we rely on sys_user join for name/phone.
ALTER TABLE `sys_owner_profile` 
  DROP COLUMN `real_name`,
  DROP COLUMN `id_card_no`,
  DROP COLUMN `id_card_front_url`,
  DROP COLUMN `id_card_back_url`,
  DROP COLUMN `auth_status`,
  DROP COLUMN `building_no`,
  DROP COLUMN `unit_no`,
  DROP COLUMN `room_no`,
  DROP COLUMN `auth_remark`,
  DROP COLUMN `phonenumber`,
  DROP COLUMN `contact_number`,
  DROP COLUMN `property_count`,
  DROP COLUMN `merged_properties`;

-- Ensure unique constraint on community + user
ALTER TABLE `sys_owner_profile` ADD UNIQUE KEY `uk_community_user` (`community_id`, `user_id`);
