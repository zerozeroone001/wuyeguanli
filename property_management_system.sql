-- =========================================
-- 智慧物业管理系统数据库表结构
-- 版本: V2.0 (基于若依框架)
-- 创建日期: 2024年
-- 说明: 本文件包含物业管理系统所需的所有数据库表结构
-- =========================================

-- ----------------------------
-- 1. 小区基础信息表
-- ----------------------------
DROP TABLE IF EXISTS `property_community`;
CREATE TABLE `property_community` (
  `community_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '小区ID',
  `community_name` varchar(100) NOT NULL COMMENT '小区名称',
  `community_code` varchar(50) NOT NULL COMMENT '小区编码',
  `address` varchar(200) NOT NULL COMMENT '小区地址',
  `developer` varchar(100) DEFAULT NULL COMMENT '开发商',
  `property_company` varchar(100) DEFAULT NULL COMMENT '物业公司',
  `total_buildings` int(11) DEFAULT 0 COMMENT '总楼栋数',
  `total_units` int(11) DEFAULT 0 COMMENT '总单元数',
  `total_rooms` int(11) DEFAULT 0 COMMENT '总房间数',
  `built_area` decimal(10,2) DEFAULT NULL COMMENT '建筑面积',
  `green_rate` decimal(5,2) DEFAULT NULL COMMENT '绿化率',
  `plot_ratio` decimal(5,2) DEFAULT NULL COMMENT '容积率',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `contact_email` varchar(50) DEFAULT NULL COMMENT '联系邮箱',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`community_id`),
  UNIQUE KEY `uk_community_code` (`community_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='小区基础信息表';

-- ----------------------------
-- 2. 楼栋信息表
-- ----------------------------
DROP TABLE IF EXISTS `property_building`;
CREATE TABLE `property_building` (
  `building_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '楼栋ID',
  `community_id` bigint(20) NOT NULL COMMENT '小区ID',
  `building_name` varchar(50) NOT NULL COMMENT '楼栋名称',
  `building_code` varchar(30) NOT NULL COMMENT '楼栋编号',
  `total_floors` int(11) DEFAULT 0 COMMENT '总层数',
  `total_units` int(11) DEFAULT 0 COMMENT '总单元数',
  `built_area` decimal(10,2) DEFAULT NULL COMMENT '建筑面积',
  `completion_date` date DEFAULT NULL COMMENT '竣工日期',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`building_id`),
  KEY `idx_community_id` (`community_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='楼栋信息表';

-- ----------------------------
-- 3. 房屋信息表
-- ----------------------------
DROP TABLE IF EXISTS `property_house`;
CREATE TABLE `property_house` (
  `house_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '房屋ID',
  `community_id` bigint(20) NOT NULL COMMENT '小区ID',
  `building_id` bigint(20) NOT NULL COMMENT '楼栋ID',
  `unit_number` varchar(10) NOT NULL COMMENT '单元号',
  `floor_number` int(11) NOT NULL COMMENT '楼层号',
  `room_number` varchar(10) NOT NULL COMMENT '房间号',
  `house_code` varchar(50) NOT NULL COMMENT '房屋编码',
  `house_type` varchar(20) DEFAULT NULL COMMENT '房屋类型（住宅、商铺、车位等）',
  `built_area` decimal(8,2) DEFAULT NULL COMMENT '建筑面积',
  `usable_area` decimal(8,2) DEFAULT NULL COMMENT '使用面积',
  `orientation` varchar(20) DEFAULT NULL COMMENT '朝向',
  `decoration_status` varchar(20) DEFAULT NULL COMMENT '装修状态',
  `property_right_type` varchar(20) DEFAULT NULL COMMENT '产权性质',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`house_id`),
  UNIQUE KEY `uk_house_code` (`house_code`),
  KEY `idx_community_building` (`community_id`, `building_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='房屋信息表';

-- ----------------------------
-- 4. 业主信息表
-- ----------------------------
DROP TABLE IF EXISTS `property_owner`;
CREATE TABLE `property_owner` (
  `owner_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '业主ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '关联用户ID',
  `owner_name` varchar(50) NOT NULL COMMENT '业主姓名',
  `id_card` varchar(18) NOT NULL COMMENT '身份证号',
  `phone` varchar(11) NOT NULL COMMENT '手机号码',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `wechat_openid` varchar(100) DEFAULT NULL COMMENT '微信OpenID',
  `gender` char(1) DEFAULT '0' COMMENT '性别（0男 1女 2未知）',
  `birth_date` date DEFAULT NULL COMMENT '出生日期',
  `address` varchar(200) DEFAULT NULL COMMENT '联系地址',
  `auth_status` char(1) DEFAULT '0' COMMENT '实名认证状态（0待认证 1已认证 2认证失败）',
  `auth_time` datetime DEFAULT NULL COMMENT '认证时间',
  `owner_type` char(1) DEFAULT '1' COMMENT '业主类型（1业主 2租户 3其他）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`owner_id`),
  UNIQUE KEY `uk_id_card` (`id_card`),
  UNIQUE KEY `uk_phone` (`phone`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='业主信息表';

-- ----------------------------
-- 5. 业主房屋关系表
-- ----------------------------
DROP TABLE IF EXISTS `property_owner_house`;
CREATE TABLE `property_owner_house` (
  `relation_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关系ID',
  `owner_id` bigint(20) NOT NULL COMMENT '业主ID',
  `house_id` bigint(20) NOT NULL COMMENT '房屋ID',
  `relation_type` char(1) NOT NULL COMMENT '关系类型（1产权人 2使用人 3租户）',
  `ownership_ratio` decimal(5,2) DEFAULT 100.00 COMMENT '产权比例',
  `start_date` date DEFAULT NULL COMMENT '开始日期',
  `end_date` date DEFAULT NULL COMMENT '结束日期',
  `certificate_number` varchar(50) DEFAULT NULL COMMENT '房产证号',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`relation_id`),
  KEY `idx_owner_house` (`owner_id`, `house_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='业主房屋关系表';

-- ----------------------------
-- 6. 业主大会信息表
-- ----------------------------
DROP TABLE IF EXISTS `property_meeting`;
CREATE TABLE `property_meeting` (
  `meeting_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '会议ID',
  `community_id` bigint(20) NOT NULL COMMENT '小区ID',
  `meeting_title` varchar(200) NOT NULL COMMENT '会议标题',
  `meeting_type` char(1) NOT NULL COMMENT '会议类型（1业主大会 2业委会会议 3临时会议）',
  `meeting_content` text COMMENT '会议内容',
  `meeting_date` datetime NOT NULL COMMENT '会议时间',
  `meeting_location` varchar(200) DEFAULT NULL COMMENT '会议地点',
  `organizer` varchar(50) NOT NULL COMMENT '组织者',
  `total_owners` int(11) DEFAULT 0 COMMENT '应参与业主总数',
  `attended_owners` int(11) DEFAULT 0 COMMENT '实际参与业主数',
  `voting_method` varchar(20) DEFAULT 'online' COMMENT '投票方式（online线上 offline线下 hybrid混合）',
  `start_time` datetime DEFAULT NULL COMMENT '投票开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '投票结束时间',
  `meeting_status` char(1) DEFAULT '0' COMMENT '会议状态（0筹备中 1进行中 2已结束 3已取消）',
  `meeting_result` text COMMENT '会议结果',
  `attachment_urls` text COMMENT '附件URLs（JSON格式）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`meeting_id`),
  KEY `idx_community_id` (`community_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='业主大会信息表';

-- ----------------------------
-- 7. 投票议题表
-- ----------------------------
DROP TABLE IF EXISTS `property_vote_topic`;
CREATE TABLE `property_vote_topic` (
  `topic_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '议题ID',
  `meeting_id` bigint(20) NOT NULL COMMENT '会议ID',
  `topic_title` varchar(200) NOT NULL COMMENT '议题标题',
  `topic_content` text NOT NULL COMMENT '议题内容',
  `topic_type` char(1) DEFAULT '1' COMMENT '议题类型（1单选 2多选 3文本）',
  `vote_options` text COMMENT '投票选项（JSON格式）',
  `required_ratio` decimal(5,2) DEFAULT 50.00 COMMENT '通过所需比例',
  `total_votes` int(11) DEFAULT 0 COMMENT '总票数',
  `agree_votes` int(11) DEFAULT 0 COMMENT '同意票数',
  `disagree_votes` int(11) DEFAULT 0 COMMENT '反对票数',
  `abstain_votes` int(11) DEFAULT 0 COMMENT '弃权票数',
  `vote_result` char(1) DEFAULT '0' COMMENT '投票结果（0进行中 1通过 2未通过）',
  `sort_order` int(11) DEFAULT 0 COMMENT '排序',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`topic_id`),
  KEY `idx_meeting_id` (`meeting_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='投票议题表';

-- ----------------------------
-- 8. 投票记录表
-- ----------------------------
DROP TABLE IF EXISTS `property_vote_record`;
CREATE TABLE `property_vote_record` (
  `vote_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '投票ID',
  `topic_id` bigint(20) NOT NULL COMMENT '议题ID',
  `owner_id` bigint(20) NOT NULL COMMENT '业主ID',
  `house_id` bigint(20) NOT NULL COMMENT '房屋ID',
  `vote_option` varchar(100) NOT NULL COMMENT '投票选项',
  `vote_weight` decimal(8,2) DEFAULT 1.00 COMMENT '投票权重（按面积计算）',
  `vote_time` datetime NOT NULL COMMENT '投票时间',
  `vote_method` varchar(20) DEFAULT 'online' COMMENT '投票方式（online线上 offline线下 phone电话）',
  `ip_address` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `device_info` varchar(200) DEFAULT NULL COMMENT '设备信息',
  `status` char(1) DEFAULT '0' COMMENT '状态（0有效 1无效）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`vote_id`),
  UNIQUE KEY `uk_topic_owner_house` (`topic_id`, `owner_id`, `house_id`),
  KEY `idx_owner_id` (`owner_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='投票记录表';

-- ----------------------------
-- 9. 意见征询表
-- ----------------------------
DROP TABLE IF EXISTS `property_consultation`;
CREATE TABLE `property_consultation` (
  `consultation_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '征询ID',
  `community_id` bigint(20) NOT NULL COMMENT '小区ID',
  `title` varchar(200) NOT NULL COMMENT '征询标题',
  `content` text NOT NULL COMMENT '征询内容',
  `consultation_type` varchar(20) DEFAULT 'survey' COMMENT '征询类型（survey调研 feedback反馈 suggestion建议）',
  `form_config` text COMMENT '表单配置（JSON格式）',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `target_owners` text COMMENT '目标业主（JSON格式，为空表示全体）',
  `total_responses` int(11) DEFAULT 0 COMMENT '总回复数',
  `anonymous_allowed` char(1) DEFAULT '1' COMMENT '是否允许匿名（0否 1是）',
  `consultation_status` char(1) DEFAULT '0' COMMENT '状态（0草稿 1进行中 2已结束 3已取消）',
  `result_summary` text COMMENT '结果摘要',
  `attachment_urls` text COMMENT '附件URLs（JSON格式）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`consultation_id`),
  KEY `idx_community_id` (`community_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='意见征询表';

-- ----------------------------
-- 10. 征询回复表
-- ----------------------------
DROP TABLE IF EXISTS `property_consultation_response`;
CREATE TABLE `property_consultation_response` (
  `response_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '回复ID',
  `consultation_id` bigint(20) NOT NULL COMMENT '征询ID',
  `owner_id` bigint(20) DEFAULT NULL COMMENT '业主ID（匿名时为空）',
  `response_data` text NOT NULL COMMENT '回复数据（JSON格式）',
  `response_time` datetime NOT NULL COMMENT '回复时间',
  `is_anonymous` char(1) DEFAULT '0' COMMENT '是否匿名（0否 1是）',
  `ip_address` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `device_info` varchar(200) DEFAULT NULL COMMENT '设备信息',
  `status` char(1) DEFAULT '0' COMMENT '状态（0有效 1无效）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`response_id`),
  KEY `idx_consultation_id` (`consultation_id`),
  KEY `idx_owner_id` (`owner_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='征询回复表';

-- ----------------------------
-- 11. 投诉建议表
-- ----------------------------
DROP TABLE IF EXISTS `property_complaint`;
CREATE TABLE `property_complaint` (
  `complaint_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '投诉ID',
  `community_id` bigint(20) NOT NULL COMMENT '小区ID',
  `owner_id` bigint(20) NOT NULL COMMENT '业主ID',
  `complaint_title` varchar(200) NOT NULL COMMENT '投诉标题',
  `complaint_content` text NOT NULL COMMENT '投诉内容',
  `complaint_type` varchar(20) NOT NULL COMMENT '投诉类型（service服务 facility设施 noise噪音 security安全 other其他）',
  `urgency_level` char(1) DEFAULT '2' COMMENT '紧急程度（1低 2中 3高）',
  `contact_phone` varchar(11) DEFAULT NULL COMMENT '联系电话',
  `contact_address` varchar(200) DEFAULT NULL COMMENT '联系地址',
  `evidence_urls` text COMMENT '证据文件URLs（JSON格式）',
  `complaint_status` char(1) DEFAULT '0' COMMENT '处理状态（0待处理 1处理中 2已解决 3已关闭）',
  `assigned_to` varchar(64) DEFAULT NULL COMMENT '分配给',
  `response_content` text COMMENT '回复内容',
  `response_time` datetime DEFAULT NULL COMMENT '回复时间',
  `response_by` varchar(64) DEFAULT NULL COMMENT '回复人',
  `satisfaction_rating` int(11) DEFAULT NULL COMMENT '满意度评分（1-5）',
  `satisfaction_comment` varchar(500) DEFAULT NULL COMMENT '满意度评价',
  `is_anonymous` char(1) DEFAULT '0' COMMENT '是否匿名（0否 1是）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`complaint_id`),
  KEY `idx_community_owner` (`community_id`, `owner_id`),
  KEY `idx_status` (`complaint_status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='投诉建议表';

-- ----------------------------
-- 12. 合同管理表
-- ----------------------------
DROP TABLE IF EXISTS `property_contract`;
CREATE TABLE `property_contract` (
  `contract_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '合同ID',
  `community_id` bigint(20) NOT NULL COMMENT '小区ID',
  `contract_name` varchar(200) NOT NULL COMMENT '合同名称',
  `contract_code` varchar(50) NOT NULL COMMENT '合同编号',
  `contract_type` varchar(20) NOT NULL COMMENT '合同类型（property物业 construction施工 service服务 other其他）',
  `party_a` varchar(100) NOT NULL COMMENT '甲方',
  `party_b` varchar(100) NOT NULL COMMENT '乙方',
  `contract_amount` decimal(12,2) DEFAULT NULL COMMENT '合同金额',
  `sign_date` date NOT NULL COMMENT '签订日期',
  `start_date` date NOT NULL COMMENT '开始日期',
  `end_date` date NOT NULL COMMENT '结束日期',
  `contract_content` text COMMENT '合同内容',
  `key_terms` text COMMENT '关键条款',
  `payment_terms` text COMMENT '付款条款',
  `performance_requirements` text COMMENT '履约要求',
  `contract_status` char(1) DEFAULT '0' COMMENT '合同状态（0草稿 1生效 2履行中 3已完成 4已终止）',
  `file_urls` text COMMENT '合同文件URLs（JSON格式）',
  `responsible_person` varchar(50) DEFAULT NULL COMMENT '负责人',
  `contact_info` varchar(100) DEFAULT NULL COMMENT '联系方式',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`contract_id`),
  UNIQUE KEY `uk_contract_code` (`contract_code`),
  KEY `idx_community_id` (`community_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='合同管理表';

-- ----------------------------
-- 13. 合同履行记录表
-- ----------------------------
DROP TABLE IF EXISTS `property_contract_performance`;
CREATE TABLE `property_contract_performance` (
  `performance_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '履行记录ID',
  `contract_id` bigint(20) NOT NULL COMMENT '合同ID',
  `performance_date` date NOT NULL COMMENT '履行日期',
  `performance_content` text NOT NULL COMMENT '履行内容',
  `performance_amount` decimal(12,2) DEFAULT NULL COMMENT '履行金额',
  `performance_status` char(1) DEFAULT '1' COMMENT '履行状态（1正常履行 2延期履行 3部分履行 4未履行）',
  `quality_rating` int(11) DEFAULT NULL COMMENT '质量评分（1-5）',
  `evidence_urls` text COMMENT '履行证据URLs（JSON格式）',
  `evaluator` varchar(50) DEFAULT NULL COMMENT '评估人',
  `evaluation_comment` text COMMENT '评估意见',
  `next_milestone` date DEFAULT NULL COMMENT '下次里程碑日期',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`performance_id`),
  KEY `idx_contract_id` (`contract_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='合同履行记录表';

-- ----------------------------
-- 14. 资金管理表
-- ----------------------------
DROP TABLE IF EXISTS `property_fund`;
CREATE TABLE `property_fund` (
  `fund_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资金ID',
  `community_id` bigint(20) NOT NULL COMMENT '小区ID',
  `fund_type` varchar(20) NOT NULL COMMENT '资金类型（operating经营性 maintenance维修 reserve储备 other其他）',
  `fund_name` varchar(100) NOT NULL COMMENT '资金名称',
  `fund_source` varchar(100) DEFAULT NULL COMMENT '资金来源',
  `initial_amount` decimal(12,2) DEFAULT 0.00 COMMENT '初始金额',
  `current_balance` decimal(12,2) DEFAULT 0.00 COMMENT '当前余额',
  `account_number` varchar(50) DEFAULT NULL COMMENT '账户号码',
  `bank_name` varchar(100) DEFAULT NULL COMMENT '开户银行',
  `manager` varchar(50) DEFAULT NULL COMMENT '管理人',
  `approval_authority` varchar(100) DEFAULT NULL COMMENT '审批权限',
  `usage_rules` text COMMENT '使用规则',
  `fund_status` char(1) DEFAULT '1' COMMENT '资金状态（0冻结 1正常 2已清空）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`fund_id`),
  KEY `idx_community_type` (`community_id`, `fund_type`)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='资金管理表';

-- ----------------------------
-- 15. 资金收支记录表
-- ----------------------------
DROP TABLE IF EXISTS `property_fund_transaction`;
CREATE TABLE `property_fund_transaction` (
  `transaction_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '交易ID',
  `fund_id` bigint(20) NOT NULL COMMENT '资金ID',
  `transaction_type` char(1) NOT NULL COMMENT '交易类型（1收入 2支出）',
  `transaction_category` varchar(50) NOT NULL COMMENT '交易分类',
  `amount` decimal(12,2) NOT NULL COMMENT '交易金额',
  `balance_after` decimal(12,2) NOT NULL COMMENT '交易后余额',
  `transaction_date` datetime NOT NULL COMMENT '交易日期',
  `description` varchar(500) NOT NULL COMMENT '交易描述',
  `related_contract_id` bigint(20) DEFAULT NULL COMMENT '关联合同ID',
  `approval_status` char(1) DEFAULT '0' COMMENT '审批状态（0待审批 1已审批 2已拒绝）',
  `approved_by` varchar(64) DEFAULT NULL COMMENT '审批人',
  `approved_time` datetime DEFAULT NULL COMMENT '审批时间',
  `approval_comment` varchar(500) DEFAULT NULL COMMENT '审批意见',
  `voucher_number` varchar(50) DEFAULT NULL COMMENT '凭证号',
  `attachment_urls` text COMMENT '附件URLs（JSON格式）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`transaction_id`),
  KEY `idx_fund_id` (`fund_id`),
  KEY `idx_transaction_date` (`transaction_date`)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='资金收支记录表';

-- ----------------------------
-- 16. 法律咨询表
-- ----------------------------
DROP TABLE IF EXISTS `property_legal_consultation`;
CREATE TABLE `property_legal_consultation` (
  `consultation_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '咨询ID',
  `community_id` bigint(20) NOT NULL COMMENT '小区ID',
  `owner_id` bigint(20) DEFAULT NULL COMMENT '业主ID（匿名时为空）',
  `consultation_title` varchar(200) NOT NULL COMMENT '咨询标题',
  `consultation_content` text NOT NULL COMMENT '咨询内容',
  `consultation_type` varchar(20) DEFAULT 'general' COMMENT '咨询类型（property物业 contract合同 dispute纠纷 general一般）',
  `urgency_level` char(1) DEFAULT '2' COMMENT '紧急程度（1低 2中 3高）',
  `contact_method` varchar(20) DEFAULT 'platform' COMMENT '联系方式（platform平台 phone电话 email邮件）',
  `contact_info` varchar(100) DEFAULT NULL COMMENT '联系信息',
  `is_anonymous` char(1) DEFAULT '0' COMMENT '是否匿名（0否 1是）',
  `consultation_status` char(1) DEFAULT '0' COMMENT '处理状态（0待处理 1处理中 2已回复 3已关闭）',
  `assigned_lawyer` varchar(100) DEFAULT NULL COMMENT '指派律师',
  `response_content` text COMMENT '回复内容',
  `response_time` datetime DEFAULT NULL COMMENT '回复时间',
  `response_by` varchar(64) DEFAULT NULL COMMENT '回复人',
  `satisfaction_rating` int(11) DEFAULT NULL COMMENT '满意度评分（1-5）',
  `satisfaction_comment` varchar(500) DEFAULT NULL COMMENT '满意度评价',
  `attachment_urls` text COMMENT '附件URLs（JSON格式）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`consultation_id`),
  KEY `idx_community_owner` (`community_id`, `owner_id`),
  KEY `idx_status` (`consultation_status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='法律咨询表';

-- ----------------------------
-- 17. 制度文件表
-- ----------------------------
DROP TABLE IF EXISTS `property_regulation`;
CREATE TABLE `property_regulation` (
  `regulation_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '制度ID',
  `community_id` bigint(20) NOT NULL COMMENT '小区ID',
  `regulation_title` varchar(200) NOT NULL COMMENT '制度标题',
  `regulation_code` varchar(50) DEFAULT NULL COMMENT '制度编号',
  `regulation_type` varchar(20) NOT NULL COMMENT '制度类型（management管理 financial财务 security安全 other其他）',
  `regulation_content` longtext NOT NULL COMMENT '制度内容',
  `version_number` varchar(20) DEFAULT '1.0' COMMENT '版本号',
  `effective_date` date NOT NULL COMMENT '生效日期',
  `expiry_date` date DEFAULT NULL COMMENT '失效日期',
  `approval_date` date DEFAULT NULL COMMENT '审批日期',
  `approved_by` varchar(100) DEFAULT NULL COMMENT '审批机构',
  `regulation_status` char(1) DEFAULT '0' COMMENT '制度状态（0草稿 1生效 2已废止）',
  `publish_scope` varchar(20) DEFAULT 'all' COMMENT '发布范围（all全体 owner业主 staff员工）',
  `attachment_urls` text COMMENT '附件URLs（JSON格式）',
  `view_count` int(11) DEFAULT 0 COMMENT '查看次数',
  `download_count` int(11) DEFAULT 0 COMMENT '下载次数',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`regulation_id`),
  KEY `idx_community_type` (`community_id`, `regulation_type`),
  KEY `idx_effective_date` (`effective_date`)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='制度文件表';

-- ----------------------------
-- 18. 公证存证记录表
-- ----------------------------
DROP TABLE IF EXISTS `property_notary_record`;
CREATE TABLE `property_notary_record` (
  `record_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `community_id` bigint(20) NOT NULL COMMENT '小区ID',
  `business_type` varchar(50) NOT NULL COMMENT '业务类型（vote投票 meeting会议 contract合同 fund资金 other其他）',
  `business_id` bigint(20) NOT NULL COMMENT '业务ID',
  `notary_title` varchar(200) NOT NULL COMMENT '存证标题',
  `notary_content` text NOT NULL COMMENT '存证内容',
  `evidence_hash` varchar(128) NOT NULL COMMENT '证据哈希值',
  `notary_platform` varchar(50) DEFAULT 'blockchain' COMMENT '公证平台（blockchain区块链 thirdparty第三方）',
  `platform_response` text COMMENT '平台响应（JSON格式）',
  `notary_certificate` varchar(200) DEFAULT NULL COMMENT '公证证书号',
  `notary_status` char(1) DEFAULT '0' COMMENT '公证状态（0待处理 1已公证 2公证失败）',
  `notary_time` datetime DEFAULT NULL COMMENT '公证时间',
  `verification_url` varchar(500) DEFAULT NULL COMMENT '验证链接',
  `cost` decimal(10,2) DEFAULT 0.00 COMMENT '公证费用',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`record_id`),
  UNIQUE KEY `uk_business` (`business_type`, `business_id`),
  KEY `idx_community_id` (`community_id`),
  KEY `idx_notary_time` (`notary_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='公证存证记录表';

-- ----------------------------
-- 19. 系统通知表
-- ----------------------------
DROP TABLE IF EXISTS `property_notification`;
CREATE TABLE `property_notification` (
  `notification_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '通知ID',
  `community_id` bigint(20) NOT NULL COMMENT '小区ID',
  `notification_title` varchar(200) NOT NULL COMMENT '通知标题',
  `notification_content` text NOT NULL COMMENT '通知内容',
  `notification_type` varchar(20) NOT NULL COMMENT '通知类型（meeting会议 vote投票 announcement公告 maintenance维护 emergency紧急）',
  `priority_level` char(1) DEFAULT '2' COMMENT '优先级（1低 2中 3高）',
  `target_audience` varchar(20) DEFAULT 'all' COMMENT '目标受众（all全体 owner业主 committee委员会）',
  `target_users` text COMMENT '目标用户（JSON格式，为空表示全体）',
  `send_method` varchar(50) DEFAULT 'platform' COMMENT '发送方式（platform平台 sms短信 wechat微信 email邮件）',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  `read_count` int(11) DEFAULT 0 COMMENT '已读数量',
  `total_recipients` int(11) DEFAULT 0 COMMENT '总接收人数',
  `notification_status` char(1) DEFAULT '0' COMMENT '通知状态（0草稿 1已发送 2已撤回）',
  `attachment_urls` text COMMENT '附件URLs（JSON格式）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notification_id`),
  KEY `idx_community_type` (`community_id`, `notification_type`),
  KEY `idx_send_time` (`send_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='系统通知表';

-- ----------------------------
-- 20. 通知阅读记录表
-- ----------------------------
DROP TABLE IF EXISTS `property_notification_read`;
CREATE TABLE `property_notification_read` (
  `read_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '阅读记录ID',
  `notification_id` bigint(20) NOT NULL COMMENT '通知ID',
  `owner_id` bigint(20) NOT NULL COMMENT '业主ID',
  `read_time` datetime NOT NULL COMMENT '阅读时间',
  `read_device` varchar(50) DEFAULT NULL COMMENT '阅读设备',
  `ip_address` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`read_id`),
  UNIQUE KEY `uk_notification_owner` (`notification_id`, `owner_id`),
  KEY `idx_owner_id` (`owner_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='通知阅读记录表';

-- ----------------------------
-- 21. 文件附件表
-- ----------------------------
DROP TABLE IF EXISTS `property_attachment`;
CREATE TABLE `property_attachment` (
  `attachment_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '附件ID',
  `business_type` varchar(50) NOT NULL COMMENT '业务类型',
  `business_id` bigint(20) NOT NULL COMMENT '业务ID',
  `file_name` varchar(200) NOT NULL COMMENT '文件名',
  `file_path` varchar(500) NOT NULL COMMENT '文件路径',
  `file_url` varchar(500) NOT NULL COMMENT '文件URL',
  `file_size` bigint(20) DEFAULT NULL COMMENT '文件大小（字节）',
  `file_type` varchar(50) DEFAULT NULL COMMENT '文件类型',
  `file_extension` varchar(10) DEFAULT NULL COMMENT '文件扩展名',
  `upload_by` varchar(64) DEFAULT NULL COMMENT '上传人',
  `upload_time` datetime DEFAULT NULL COMMENT '上传时间',
  `download_count` int(11) DEFAULT 0 COMMENT '下载次数',
  `is_public` char(1) DEFAULT '0' COMMENT '是否公开（0否 1是）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`attachment_id`),
  KEY `idx_business` (`business_type`, `business_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='文件附件表';

-- ----------------------------
-- 22. 系统配置表
-- ----------------------------
DROP TABLE IF EXISTS `property_system_config`;
CREATE TABLE `property_system_config` (
  `config_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `community_id` bigint(20) DEFAULT NULL COMMENT '小区ID（为空表示全局配置）',
  `config_key` varchar(100) NOT NULL COMMENT '配置键',
  `config_value` text COMMENT '配置值',
  `config_type` varchar(20) DEFAULT 'string' COMMENT '配置类型（string字符串 number数字 boolean布尔 json对象）',
  `config_group` varchar(50) DEFAULT 'system' COMMENT '配置分组',
  `config_desc` varchar(200) DEFAULT NULL COMMENT '配置描述',
  `is_encrypted` char(1) DEFAULT '0' COMMENT '是否加密（0否 1是）',
  `sort_order` int(11) DEFAULT 0 COMMENT '排序',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`),
  UNIQUE KEY `uk_community_key` (`community_id`, `config_key`)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='系统配置表';

-- ----------------------------
-- 插入初始数据
-- ----------------------------

-- 插入默认小区信息
INSERT INTO `property_community` VALUES 
(1, '示例花园小区', 'DEMO_GARDEN', '北京市朝阳区示例路123号', '示例地产开发有限公司', '示例物业管理有限公司', 
5, 20, 200, 50000.00, 35.00, 2.50, '400-123-4567', 'service@demo-garden.com', '0', '0', 
'admin', NOW(), '', NULL, '示例小区，用于系统演示');

-- 插入系统配置
INSERT INTO `property_system_config` VALUES 
(1, NULL, 'system.vote.default_duration', '7', 'number', 'vote', '默认投票持续天数', '0', 1, '0', '0', 'admin', NOW(), '', NULL, '投票活动的默认持续时间'),
(2, NULL, 'system.vote.pass_ratio', '50', 'number', 'vote', '默认通过比例(%)', '0', 2, '0', '0', 'admin', NOW(), '', NULL, '投票议题的默认通过比例'),
(3, NULL, 'system.notification.sms_enabled', 'false', 'boolean', 'notification', '是否启用短信通知', '0', 3, '0', '0', 'admin', NOW(), '', NULL, '系统通知是否通过短信发送'),
(4, NULL, 'system.file.max_size', '10485760', 'number', 'file', '文件上传最大大小(字节)', '0', 4, '0', '0', 'admin', NOW(), '', NULL, '单个文件上传的最大大小限制'),
(5, NULL, 'system.wechat.app_id', '', 'string', 'wechat', '微信小程序AppID', '1', 5, '0', '0', 'admin', NOW(), '', NULL, '微信小程序的应用ID');

-- =========================================
-- 创建索引以优化查询性能
-- =========================================

-- 为常用查询字段创建复合索引
CREATE INDEX `idx_owner_auth_status` ON `property_owner` (`auth_status`, `status`);
CREATE INDEX `idx_meeting_date_status` ON `property_meeting` (`meeting_date`, `meeting_status`);
CREATE INDEX `idx_complaint_create_time` ON `property_complaint` (`create_time`);
CREATE INDEX `idx_consultation_end_time` ON `property_consultation` (`end_time`);
CREATE INDEX `idx_fund_transaction_date_type` ON `property_fund_transaction` (`transaction_date`, `transaction_type`);

-- =========================================
-- 数据库表结构创建完成
-- 说明：
-- 1. 所有表均继承若依BaseEntity规范，包含标准的审计字段
-- 2. 使用统一的命名规范：property_ 前缀 + 业务名称
-- 3. 支持软删除：del_flag字段标记删除状态
-- 4. 支持数据权限：可按小区、部门等维度控制数据访问
-- 5. 所有金额字段使用DECIMAL类型确保精度
-- 6. 时间字段统一使用DATETIME类型
-- 7. 状态字段使用CHAR(1)类型，便于字典管理
-- 8. 大文本字段使用TEXT或LONGTEXT类型
-- 9. 文件存储采用URL方式，支持云存储
-- 10. 预留扩展字段remark用于存储额外信息
-- =========================================