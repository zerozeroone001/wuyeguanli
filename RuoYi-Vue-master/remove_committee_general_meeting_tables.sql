-- 删除业主委员会会议和业主大会会议相关表
-- 执行前请确保已备份相关数据

-- 删除业主委员会会议表
DROP TABLE IF EXISTS `sys_committee_meeting`;

-- 删除业主大会会议表  
DROP TABLE IF EXISTS `sys_general_meeting`;

-- 删除相关的菜单权限（如果存在）
DELETE FROM `sys_menu` WHERE `menu_name` LIKE '%业主委员会会议%' OR `menu_name` LIKE '%业主大会会议%';

-- 删除相关的角色菜单关联（如果存在）
DELETE FROM `sys_role_menu` WHERE `menu_id` IN (
    SELECT `menu_id` FROM `sys_menu` WHERE `menu_name` LIKE '%业主委员会会议%' OR `menu_name` LIKE '%业主大会会议%'
);

-- 删除相关的字典数据（如果存在）
DELETE FROM `sys_dict_data` WHERE `dict_type` IN ('sys_committee_meeting_status', 'sys_general_meeting_status');

-- 删除相关的字典类型（如果存在）
DELETE FROM `sys_dict_type` WHERE `dict_type` IN ('sys_committee_meeting_status', 'sys_general_meeting_status');