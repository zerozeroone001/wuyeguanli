-- 公证存证功能数据库优化脚本
-- 执行时间：2025-01-27

-- 1. 优化 sys_notary_application 表
ALTER TABLE `sys_notary_application` 
ADD COLUMN `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
ADD COLUMN `completed_time` datetime DEFAULT NULL COMMENT '完成时间',
ADD COLUMN `rejected_reason` varchar(500) DEFAULT NULL COMMENT '拒绝原因',
ADD COLUMN `fee_amount` decimal(10,2) DEFAULT 0.00 COMMENT '公证费用',
ADD COLUMN `payment_status` char(1) DEFAULT '0' COMMENT '支付状态（0未支付 1已支付 2已退款）',
ADD COLUMN `payment_time` datetime DEFAULT NULL COMMENT '支付时间',
ADD COLUMN `payment_method` varchar(50) DEFAULT NULL COMMENT '支付方式',
ADD COLUMN `transaction_id` varchar(100) DEFAULT NULL COMMENT '交易流水号',
ADD COLUMN `reviewer_id` bigint(20) DEFAULT NULL COMMENT '审核人员ID',
ADD COLUMN `review_time` datetime DEFAULT NULL COMMENT '审核时间',
ADD COLUMN `review_comment` varchar(500) DEFAULT NULL COMMENT '审核意见',
ADD COLUMN `notary_person_id` bigint(20) DEFAULT NULL COMMENT '公证员ID',
ADD COLUMN `certificate_no` varchar(100) DEFAULT NULL COMMENT '公证书编号',
ADD COLUMN `certificate_type` varchar(50) DEFAULT NULL COMMENT '证书类型',
ADD COLUMN `validity_period` int(11) DEFAULT NULL COMMENT '有效期（天数）',
ADD COLUMN `expiry_date` datetime DEFAULT NULL COMMENT '过期时间',
ADD COLUMN `is_urgent` char(1) DEFAULT '0' COMMENT '是否加急（0否 1是）',
ADD COLUMN `urgent_fee` decimal(10,2) DEFAULT 0.00 COMMENT '加急费用',
ADD COLUMN `notary_office_name` varchar(200) DEFAULT NULL COMMENT '公证处名称',
ADD COLUMN `applicant_type` varchar(50) DEFAULT 'individual' COMMENT '申请人类型（individual个人 company企业）',
ADD COLUMN `company_name` varchar(200) DEFAULT NULL COMMENT '企业名称',
ADD COLUMN `legal_representative` varchar(100) DEFAULT NULL COMMENT '法定代表人',
ADD COLUMN `business_license` varchar(100) DEFAULT NULL COMMENT '营业执照号',
ADD COLUMN `contact_email` varchar(100) DEFAULT NULL COMMENT '联系邮箱',
ADD COLUMN `emergency_contact` varchar(100) DEFAULT NULL COMMENT '紧急联系人',
ADD COLUMN `emergency_phone` varchar(20) DEFAULT NULL COMMENT '紧急联系电话';

-- 2. 优化 sys_notary_attachment 表
ALTER TABLE `sys_notary_attachment`
ADD COLUMN `file_size` bigint(20) DEFAULT NULL COMMENT '文件大小（字节）',
ADD COLUMN `file_hash` varchar(64) DEFAULT NULL COMMENT '文件哈希值',
ADD COLUMN `upload_time` datetime DEFAULT NULL COMMENT '上传时间',
ADD COLUMN `upload_by` varchar(64) DEFAULT NULL COMMENT '上传人',
ADD COLUMN `is_required` char(1) DEFAULT '1' COMMENT '是否必需（0否 1是）',
ADD COLUMN `verification_status` char(1) DEFAULT '0' COMMENT '验证状态（0未验证 1已验证 2验证失败）',
ADD COLUMN `verification_result` varchar(500) DEFAULT NULL COMMENT '验证结果',
ADD COLUMN `verification_time` datetime DEFAULT NULL COMMENT '验证时间',
ADD COLUMN `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）';

-- 3. 创建公证流程记录表
CREATE TABLE `sys_notary_process_log` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `notary_id` bigint(20) NOT NULL COMMENT '公证ID',
  `process_step` varchar(50) NOT NULL COMMENT '流程步骤',
  `step_name` varchar(100) NOT NULL COMMENT '步骤名称',
  `status` varchar(20) NOT NULL COMMENT '状态',
  `operator_id` bigint(20) DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(100) DEFAULT NULL COMMENT '操作人姓名',
  `operation_time` datetime NOT NULL COMMENT '操作时间',
  `comment` varchar(500) DEFAULT NULL COMMENT '备注',
  `attachment_url` varchar(500) DEFAULT NULL COMMENT '附件URL',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`log_id`),
  KEY `idx_notary_id` (`notary_id`),
  KEY `idx_operation_time` (`operation_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公证流程记录表';

-- 4. 创建公证类型配置表
CREATE TABLE `sys_notary_type_config` (
  `config_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `type_code` varchar(50) NOT NULL COMMENT '类型代码',
  `type_name` varchar(100) NOT NULL COMMENT '类型名称',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `base_fee` decimal(10,2) DEFAULT 0.00 COMMENT '基础费用',
  `urgent_fee` decimal(10,2) DEFAULT 0.00 COMMENT '加急费用',
  `processing_days` int(11) DEFAULT 7 COMMENT '处理天数',
  `urgent_days` int(11) DEFAULT 3 COMMENT '加急处理天数',
  `required_documents` text COMMENT '必需材料（JSON格式）',
  `optional_documents` text COMMENT '可选材料（JSON格式）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `sort_order` int(11) DEFAULT 0 COMMENT '排序',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`),
  UNIQUE KEY `uk_type_code` (`type_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公证类型配置表';

-- 5. 创建公证费用记录表
CREATE TABLE `sys_notary_fee_record` (
  `fee_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '费用ID',
  `notary_id` bigint(20) NOT NULL COMMENT '公证ID',
  `fee_type` varchar(50) NOT NULL COMMENT '费用类型（base基础费 urgent加急费 other其他）',
  `fee_name` varchar(100) NOT NULL COMMENT '费用名称',
  `amount` decimal(10,2) NOT NULL COMMENT '金额',
  `status` char(1) DEFAULT '0' COMMENT '状态（0未支付 1已支付 2已退款）',
  `payment_time` datetime DEFAULT NULL COMMENT '支付时间',
  `payment_method` varchar(50) DEFAULT NULL COMMENT '支付方式',
  `transaction_id` varchar(100) DEFAULT NULL COMMENT '交易流水号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`fee_id`),
  KEY `idx_notary_id` (`notary_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公证费用记录表';

-- 6. 插入公证类型配置数据
INSERT INTO `sys_notary_type_config` (`type_code`, `type_name`, `description`, `base_fee`, `urgent_fee`, `processing_days`, `urgent_days`, `required_documents`, `optional_documents`, `status`, `sort_order`) VALUES
('document', '文件公证', '合同、协议、声明等文件的真实性公证', 200.00, 100.00, 7, 3, '["身份证", "文件原件"]', '["营业执照", "授权委托书"]', '0', 1),
('signature', '签名公证', '签字、盖章行为的真实性公证', 150.00, 80.00, 5, 2, '["身份证", "签名样本"]', '["营业执照", "印章证明"]', '0', 2),
('identity', '身份公证', '身份证明、学历证明等身份信息公证', 100.00, 50.00, 3, 1, '["身份证", "户口本"]', '["学历证书", "工作证明"]', '0', 3),
('property', '财产公证', '房产、车辆等财产权属证明公证', 300.00, 150.00, 10, 5, '["身份证", "产权证", "购房合同"]', '["结婚证", "离婚证", "遗嘱"]', '0', 4),
('inheritance', '继承公证', '遗产继承相关的公证服务', 500.00, 200.00, 15, 7, '["身份证", "死亡证明", "亲属关系证明", "遗产证明"]', '["遗嘱", "法院判决书"]', '0', 5),
('power', '委托公证', '授权委托书等委托关系公证', 180.00, 90.00, 5, 2, '["身份证", "委托书", "受托人身份证"]', '["营业执照", "授权书"]', '0', 6);

-- 7. 添加索引优化
ALTER TABLE `sys_notary_application` 
ADD INDEX `idx_user_id` (`user_id`),
ADD INDEX `idx_status` (`status`),
ADD INDEX `idx_type` (`type`),
ADD INDEX `idx_apply_time` (`apply_time`),
ADD INDEX `idx_create_time` (`create_time`),
ADD INDEX `idx_notary_office_id` (`notary_office_id`);

ALTER TABLE `sys_notary_attachment`
ADD INDEX `idx_notary_id` (`notary_id`),
ADD INDEX `idx_file_type` (`file_type`),
ADD INDEX `idx_upload_time` (`upload_time`);

-- 8. 更新现有数据的默认值
UPDATE `sys_notary_application` SET 
  `del_flag` = '0',
  `fee_amount` = 200.00,
  `payment_status` = '0',
  `applicant_type` = 'individual'
WHERE `del_flag` IS NULL;

UPDATE `sys_notary_attachment` SET 
  `del_flag` = '0',
  `is_required` = '1',
  `verification_status` = '0'
WHERE `del_flag` IS NULL;
