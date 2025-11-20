-- 为公告表添加关联类型和关联ID字段
ALTER TABLE sys_notice
ADD COLUMN relation_type VARCHAR(20) COMMENT '关联类型：meeting-业主大会，opinion-意见征询' AFTER status,
ADD COLUMN relation_id BIGINT(20) COMMENT '关联的业务ID' AFTER relation_type;

-- 添加索引以提高查询性能
CREATE INDEX idx_notice_relation ON sys_notice(relation_type, relation_id);
