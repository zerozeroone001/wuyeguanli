-- 问卷调查功能：自定义表单模板表
CREATE TABLE IF NOT EXISTS `sys_form_template` (
  `form_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '模板ID',
  `form_name` varchar(100) NOT NULL COMMENT '模板名称',
  `form_type` varchar(50) DEFAULT 'SURVEY' COMMENT '模板类型',
  `form_schema` longtext COMMENT 'PC端表单结构(JSON)',
  `mobile_schema` longtext COMMENT '移动端表单结构(JSON)',
  `form_config` longtext COMMENT '表单配置(JSON)',
  `appearance_config` longtext COMMENT '外观配置(JSON)',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(64) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`form_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='自定义表单模板';

-- 问卷调查功能：表单提交记录表
CREATE TABLE IF NOT EXISTS `sys_form_submission` (
  `submission_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '提交ID',
  `form_id` bigint(20) NOT NULL COMMENT '表单模板ID',
  `poll_id` bigint(20) NOT NULL COMMENT '问卷ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `community_id` bigint(20) DEFAULT NULL COMMENT '小区ID',
  `client_type` varchar(20) DEFAULT 'mobile' COMMENT '提交终端（pc/mobile）',
  `submit_ip` varchar(64) DEFAULT NULL COMMENT '提交IP',
  `status` char(1) DEFAULT '0' COMMENT '状态（0有效 1撤销）',
  `answers_data` longtext COMMENT '答案内容(JSON)',
  `submit_time` datetime DEFAULT NULL COMMENT '提交时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(64) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`submission_id`),
  UNIQUE KEY `uk_poll_user` (`poll_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='表单提交记录';

-- 意见征询菜单初始化
SET @pollParent := (SELECT menu_id FROM sys_menu WHERE menu_name = '物业管理' LIMIT 1);
SET @pollParent := IFNULL(@pollParent, 1);
SET @pollMenuId := (SELECT IFNULL(MAX(menu_id), 0) + 1 FROM sys_menu);

INSERT INTO sys_menu (
    menu_id, menu_name, parent_id, order_num, path, component,
    is_frame, is_cache, menu_type, visible, status, perms, icon,
    create_by, create_time, remark
) VALUES (
    @pollMenuId, '意见征询', @pollParent, 30, 'poll', 'system/poll/index',
    1, 0, 'C', '0', '0', NULL, 'table',
    'admin', NOW(), '意见征询问卷管理页面'
);

-- 表单设计页（隐藏菜单，供路由直接访问）
INSERT INTO sys_menu (
    menu_id, menu_name, parent_id, order_num, path, component,
    is_frame, is_cache, menu_type, visible, status, perms, icon,
    create_by, create_time, remark
) VALUES (
    @pollMenuId + 1, '问卷设计', @pollMenuId, 99, 'poll/designer', 'system/poll/designer/index',
    1, 0, 'C', '1', '0', NULL, '#',
    'admin', NOW(), '问卷表单设计页面'
);

-- 操作权限（若已存在对应权限，插入语句会跳过）
INSERT INTO sys_menu (
    menu_id, menu_name, parent_id, order_num, path, component,
    is_frame, is_cache, menu_type, visible, status, perms, icon,
    create_by, create_time, remark
)
SELECT
    @pollMenuId + idx,
    menu_name,
    @pollMenuId,
    order_num,
    '',
    '',
    1,
    0,
    'F',
    '0',
    '0',
    perms,
    '#',
    'admin',
    NOW(),
    remark
FROM (
    SELECT 2 AS idx, '问卷查询' AS menu_name, 1 AS order_num, 'system:poll:list' AS perms, '查询问卷列表' AS remark UNION ALL
    SELECT 3, '问卷新增', 2, 'system:poll:add', '新增问卷' UNION ALL
    SELECT 4, '问卷修改', 3, 'system:poll:edit', '修改问卷' UNION ALL
    SELECT 5, '问卷删除', 4, 'system:poll:remove', '删除问卷' UNION ALL
    SELECT 6, '问卷导出', 5, 'system:poll:export', '导出问卷'
) btn
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = btn.perms);
