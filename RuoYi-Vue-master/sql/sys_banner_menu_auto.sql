-- 轮播图管理菜单权限 SQL（自动查询版本）
-- 自动获取"系统管理"菜单ID作为父菜单

-- 1. 轮播图管理主菜单
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
SET @parent_menu_id = LAST_INSERT_ID();

-- 2. 轮播图查询按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES
('轮播图查询', @parent_menu_id, 1, '#', '', 1, 0, 'F', '0', '0', 'system:banner:query', '#', 'admin', NOW(), 'admin', NOW(), '');

-- 3. 轮播图新增按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES
('轮播图新增', @parent_menu_id, 2, '#', '', 1, 0, 'F', '0', '0', 'system:banner:add', '#', 'admin', NOW(), 'admin', NOW(), '');

-- 4. 轮播图修改按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES
('轮播图修改', @parent_menu_id, 3, '#', '', 1, 0, 'F', '0', '0', 'system:banner:edit', '#', 'admin', NOW(), 'admin', NOW(), '');

-- 5. 轮播图删除按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES
('轮播图删除', @parent_menu_id, 4, '#', '', 1, 0, 'F', '0', '0', 'system:banner:remove', '#', 'admin', NOW(), 'admin', NOW(), '');

-- 6. 轮播图导出按钮
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES
('轮播图导出', @parent_menu_id, 5, '#', '', 1, 0, 'F', '0', '0', 'system:banner:export', '#', 'admin', NOW(), 'admin', NOW(), '');

-- 查询验证
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

-- 提示：如果需要删除这些菜单，请执行以下SQL
-- DELETE FROM sys_menu WHERE menu_name LIKE '%轮播图%' OR perms LIKE '%banner%';
