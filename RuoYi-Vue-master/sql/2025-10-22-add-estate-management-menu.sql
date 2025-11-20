-- 小区管理页面菜单初始化脚本
-- 请根据实际菜单结构调整父级菜单 @parentId。默认挂载在“系统管理”根节点（ID=1）。

SET @parentId := (SELECT menu_id FROM sys_menu WHERE menu_name = '物业管理' LIMIT 1);
SET @parentId := IFNULL(@parentId, 1);

SET @menuId := (SELECT IFNULL(MAX(menu_id), 0) + 1 FROM sys_menu);

INSERT INTO sys_menu (
    menu_id, menu_name, parent_id, order_num, path, component,
    is_frame, is_cache, menu_type, visible, status, perms, icon,
    create_by, create_time, remark
) VALUES (
    @menuId, '小区管理', @parentId, 10, 'estate', 'system/estate/index',
    1, 0, 'C', '0', '0', NULL, 'community',
    'admin', NOW(), '小区、楼栋、门牌一体化维护页面'
);

-- 预置常用按钮权限（若已有对应权限可忽略下面的插入语句）
INSERT INTO sys_menu (
    menu_id, menu_name, parent_id, order_num, path, component,
    is_frame, is_cache, menu_type, visible, status, perms, icon,
    create_by, create_time, remark
)
SELECT
    @menuId + idx,
    menu_name,
    @menuId,
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
    SELECT 1 AS idx, '小区查询' AS menu_name, 1 AS order_num, 'system:community:list' AS perms, '查询小区信息' AS remark UNION ALL
    SELECT 2, '小区新增', 2, 'system:community:add', '新增小区' UNION ALL
    SELECT 3, '小区修改', 3, 'system:community:edit', '修改小区' UNION ALL
    SELECT 4, '小区删除', 4, 'system:community:remove', '删除小区' UNION ALL
    SELECT 5, '小区导出', 5, 'system:community:export', '导出小区数据' UNION ALL
    SELECT 6, '房产查询', 6, 'system:property:list', '查询房产信息' UNION ALL
    SELECT 7, '房产新增', 7, 'system:property:add', '新增房产信息' UNION ALL
    SELECT 8, '房产修改', 8, 'system:property:edit', '修改房产信息' UNION ALL
    SELECT 9, '房产删除', 9, 'system:property:remove', '删除房产信息' UNION ALL
    SELECT 10, '房产导出', 10, 'system:property:export', '导出房产信息'
) btn
WHERE NOT EXISTS (SELECT 1 FROM sys_menu m WHERE m.perms = btn.perms);
