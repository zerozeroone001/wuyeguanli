-- Add congduo and congduo_count columns to sys_property_meeting_topic table
ALTER TABLE `sys_property_meeting_topic` ADD COLUMN `congduo` char(1) DEFAULT '0' COMMENT '是否开启从多选项(0否 1是)';
ALTER TABLE `sys_property_meeting_topic` ADD COLUMN `congduo_count` bigint(20) DEFAULT 0 COMMENT '从多票数';