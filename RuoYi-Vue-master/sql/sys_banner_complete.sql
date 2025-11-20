-- 轮播图管理完整SQL脚本
-- 包含：数据库表、示例数据、菜单权限、角色权限分配

-- ============================================
-- 第一部分：创建轮播图表和示例数据
-- ============================================

-- 创建轮播图表
DROP TABLE IF EXISTS `sys_banner`;
CREATE TABLE `sys_banner` (
  `banner_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '轮播图ID',
  `banner_title` VARCHAR(100) NOT NULL COMMENT '轮播图标题',
  `banner_image` VARCHAR(500) NOT NULL COMMENT '轮播图图片URL',
  `link_type` VARCHAR(20) DEFAULT NULL COMMENT '链接类型：meeting-业主大会，opinion-意见征询，url-自定义链接',
  `link_id` BIGINT(20) DEFAULT NULL COMMENT '关联的业务ID（业主大会ID或意见征询ID）',
  `link_url` VARCHAR(500) DEFAULT NULL COMMENT '自定义链接URL',
  `sort_order` INT(4) DEFAULT 0 COMMENT '显示顺序',
  `status` CHAR(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `community_id` BIGINT(20) DEFAULT NULL COMMENT '小区ID',
  `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`banner_id`),
  KEY `idx_status` (`status`),
  KEY `idx_sort_order` (`sort_order`),
  KEY `idx_link` (`link_type`, `link_id`),
  KEY `idx_community` (`community_id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='轮播图管理表';

-- 插入示例数据
INSERT INTO `sys_banner` (`banner_title`, `banner_image`, `link_type`, `link_id`, `link_url`, `sort_order`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
('智慧物业管理系统', 'https://img.icons8.com/color/480/apartment.png', NULL, NULL, NULL, 1, '0', 'admin', NOW(), 'admin', NOW(), '首页轮播图1'),
('温馨家园服务', 'https://img.icons8.com/color/480/home.png', NULL, NULL, NULL, 2, '0', 'admin', NOW(), 'admin', NOW(), '首页轮播图2'),
('社区服务平台', 'https://img.icons8.com/color/480/building.png', NULL, NULL, NULL, 3, '0', 'admin', NOW(), 'admin', NOW(), '首页轮播图3');

-- ============================================
-- 第二部分：添加菜单权限
-- ============================================

-- 1. 轮播图管理主菜单（自动获取系统管理菜单ID）
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT
    '轮播图管理' AS menu_name,
    menu_id AS parent_id,
    6 AS order_num,
    'banner' AS path,
    'system/banner/index' AS component,
    1 AS is_frame,
    0 AS is_cache,
    'C' AS menu_type,
    '0' AS visible,
    '0' AS status,
    'system:banner:list' AS perms,
    'picture' AS icon,
    'admin' AS create_by,
    NOW() AS create_time,
    'admin' AS update_by,
    NOW() AS update_time,
    '轮播图管理菜单' AS remark
FROM sys_menu
WHERE menu_name = '系统管理' AND parent_id = 0
LIMIT 1;

-- 获取刚插入的菜单ID
SET @banner_menu_id = LAST_INSERT_ID();

-- 2. 轮播图查询按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES
('轮播图查询', @banner_menu_id, 1, '#', '', 1, 0, 'F', '0', '0', 'system:banner:query', '#', 'admin', NOW(), 'admin', NOW(), '');

-- 3. 轮播图新增按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES
('轮播图新增', @banner_menu_id, 2, '#', '', 1, 0, 'F', '0', '0', 'system:banner:add', '#', 'admin', NOW(), 'admin', NOW(), '');

-- 4. 轮播图修改按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES
('轮播图修改', @banner_menu_id, 3, '#', '', 1, 0, 'F', '0', '0', 'system:banner:edit', '#', 'admin', NOW(), 'admin', NOW(), '');

-- 5. 轮播图删除按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES
('轮播图删除', @banner_menu_id, 4, '#', '', 1, 0, 'F', '0', '0', 'system:banner:remove', '#', 'admin', NOW(), 'admin', NOW(), '');

-- 6. 轮播图导出按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES
('轮播图导出', @banner_menu_id, 5, '#', '', 1, 0, 'F', '0', '0', 'system:banner:export', '#', 'admin', NOW(), 'admin', NOW(), '');

-- ============================================
-- 第三部分：为管理员角色分配权限
-- ============================================

-- 为管理员角色（role_id=1）分配轮播图管理菜单权限
-- 注意：如果你的管理员角色ID不是1，请修改下面的role_id值

-- 获取所有轮播图相关菜单的ID
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1 AS role_id, menu_id
FROM sys_menu
WHERE menu_name LIKE '%轮播图%' OR perms LIKE '%banner%'
ON DUPLICATE KEY UPDATE role_id = role_id; -- 避免重复插入

-- ============================================
-- 第四部分：验证查询
-- ============================================

-- 查询轮播图表数据
SELECT '=== 轮播图表数据 ===' AS info;
SELECT * FROM sys_banner ORDER BY sort_order;

-- 查询轮播图菜单
SELECT '=== 轮播图菜单 ===' AS info;
SELECT
    m1.menu_id,
    m1.menu_name,
    m1.parent_id,
    m2.menu_name AS parent_name,
    m1.order_num,
    m1.path,
    m1.component,
    m1.menu_type,
    m1.perms,
    m1.icon,
    m1.status
FROM sys_menu m1
LEFT JOIN sys_menu m2 ON m1.parent_id = m2.menu_id
WHERE m1.menu_name LIKE '%轮播图%' OR m1.perms LIKE '%banner%'
ORDER BY m1.parent_id, m1.order_num;

-- 查询管理员角色的轮播图权限
SELECT '=== 管理员角色轮播图权限 ===' AS info;
SELECT
    r.role_id,
    r.role_name,
    m.menu_id,
    m.menu_name,
    m.perms
FROM sys_role r
JOIN sys_role_menu rm ON r.role_id = rm.role_id
JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE (m.menu_name LIKE '%轮播图%' OR m.perms LIKE '%banner%')
  AND r.role_id = 1;

-- ============================================
-- 第五部分：回滚脚本（如需删除）
-- ============================================

/*
-- 删除角色菜单关联
DELETE FROM sys_role_menu WHERE menu_id IN (
    SELECT menu_id FROM sys_menu WHERE menu_name LIKE '%轮播图%' OR perms LIKE '%banner%'
);

-- 删除菜单
DELETE FROM sys_menu WHERE menu_name LIKE '%轮播图%' OR perms LIKE '%banner%';

-- 删除轮播图表
DROP TABLE IF EXISTS sys_banner;
*/

-- ============================================
-- 说明
-- ============================================
/*
字段说明：
- menu_type: C=菜单 F=按钮
- visible: 0=显示 1=隐藏
- status: 0=正常 1=停用
- is_frame: 0=否 1=是（是否外链）
- is_cache: 0=缓存 1=不缓存

使用说明：
1. 执行此脚本将自动创建表、菜单、权限
2. 默认为role_id=1（管理员）分配所有权限
3. 如需为其他角色分配权限，请修改role_id值或手动在后台分配
4. 如需回滚，取消注释"回滚脚本"部分并执行
*/
