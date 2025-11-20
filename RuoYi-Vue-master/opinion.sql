CREATE TABLE `sys_meeting_opinion` (
  `opinion_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `topic_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `opinion` varchar(500) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`opinion_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
