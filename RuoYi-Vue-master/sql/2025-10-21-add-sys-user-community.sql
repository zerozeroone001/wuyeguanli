-- 为 sys_user 表新增 community_id 字段，用于绑定用户所属小区
ALTER TABLE `sys_user`
    ADD COLUMN `community_id` BIGINT(20) NULL DEFAULT NULL COMMENT '所属小区ID';

-- 可选：为现有的超级管理员设置 NULL，后续根据需要补录其他账号的小区
