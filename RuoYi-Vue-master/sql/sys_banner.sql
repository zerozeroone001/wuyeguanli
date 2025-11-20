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

-- 查询验证
SELECT * FROM sys_banner ORDER BY sort_order;
