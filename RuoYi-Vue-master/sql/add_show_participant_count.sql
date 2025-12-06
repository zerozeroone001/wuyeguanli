-- Add show_participant_count column to sys_property_meeting table
ALTER TABLE sys_property_meeting ADD COLUMN show_participant_count CHAR(1) DEFAULT '1' COMMENT '是否展示参与人数(0否 1是)';
