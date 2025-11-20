-- 菜单权限 SQL
-- 轮播图管理菜单和权限

-- 查询系统管理菜单的menu_id（假设为1）
-- 如果你的系统管理菜单ID不是1，请修改下面的parent_id值

-- 1. 轮播图管理主菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES
('轮播图管理', 1, 6, 'banner', 'system/banner/index', 1, 0, 'C', '0', '0', 'system:banner:list', 'picture', 'admin', NOW(), 'admin', NOW(), '轮播图管理菜单');

-- 获取刚插入的菜单ID（用于子菜单）
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

-- 说明：
-- menu_type: C=菜单 F=按钮
-- visible: 0=显示 1=隐藏
-- status: 0=正常 1=停用
-- is_frame: 0=否 1=是（是否外链）
-- is_cache: 0=缓存 1=不缓存
-- parent_id: 1通常是"系统管理"菜单的ID，如果不是请根据实际情况修改
