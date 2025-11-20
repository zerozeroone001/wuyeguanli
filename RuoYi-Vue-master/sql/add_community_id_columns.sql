-- 添加小区ID字段到涉及的业务表
-- 执行前请确认数据库版本支持 IF NOT EXISTS 语法（MySQL 8.0 及以上）

ALTER TABLE `sys_complaint`
    ADD COLUMN IF NOT EXISTS `community_id` BIGINT(20) DEFAULT NULL COMMENT '小区ID';

ALTER TABLE `sys_fund_transaction`
    ADD COLUMN IF NOT EXISTS `community_id` BIGINT(20) DEFAULT NULL COMMENT '小区ID';

ALTER TABLE `sys_handover_inspection`
    ADD COLUMN IF NOT EXISTS `community_id` BIGINT(20) DEFAULT NULL COMMENT '小区ID';

ALTER TABLE `sys_inspection_plan`
    ADD COLUMN IF NOT EXISTS `community_id` BIGINT(20) DEFAULT NULL COMMENT '小区ID';

ALTER TABLE `sys_notary_application`
    ADD COLUMN IF NOT EXISTS `community_id` BIGINT(20) DEFAULT NULL COMMENT '小区ID';

ALTER TABLE `sys_notice`
    ADD COLUMN IF NOT EXISTS `community_id` BIGINT(20) DEFAULT NULL COMMENT '小区ID';

ALTER TABLE `sys_owner_profile`
    ADD COLUMN IF NOT EXISTS `community_id` BIGINT(20) DEFAULT NULL COMMENT '小区ID';

ALTER TABLE `sys_property_complaint`
    ADD COLUMN IF NOT EXISTS `community_id` BIGINT(20) DEFAULT NULL COMMENT '小区ID';

ALTER TABLE `sys_property_contract`
    ADD COLUMN IF NOT EXISTS `community_id` BIGINT(20) DEFAULT NULL COMMENT '小区ID';

ALTER TABLE `sys_property_fund_flow`
    ADD COLUMN IF NOT EXISTS `community_id` BIGINT(20) DEFAULT NULL COMMENT '小区ID';

ALTER TABLE `sys_property_meeting`
    ADD COLUMN IF NOT EXISTS `community_id` BIGINT(20) DEFAULT NULL COMMENT '小区ID';

ALTER TABLE `sys_property_regulation`
    ADD COLUMN IF NOT EXISTS `community_id` BIGINT(20) DEFAULT NULL COMMENT '小区ID';

ALTER TABLE `sys_regulation_category`
    ADD COLUMN IF NOT EXISTS `community_id` BIGINT(20) DEFAULT NULL COMMENT '小区ID';

ALTER TABLE `sys_visitor`
    ADD COLUMN IF NOT EXISTS `community_id` BIGINT(20) DEFAULT NULL COMMENT '小区ID';

ALTER TABLE `sys_warranty`
    ADD COLUMN IF NOT EXISTS `community_id` BIGINT(20) DEFAULT NULL COMMENT '小区ID';
