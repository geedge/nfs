#用户管理表
DROP TABLE IF EXISTS `user_manage`;
CREATE TABLE `user_manage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) NOT NULL,
  `user_pwd` varchar(255) NOT NULL,
  `server_ip` varchar(1000) NOT NULL,
  `user_type` varchar(256) DEFAULT NULL,
  `is_valid` int(11) NOT NULL COMMENT '0无效，1有效，-1删除\r\n',
  `is_audit` int(11) NOT NULL COMMENT '0未审核，1审核通过，2审核未通过，3取消审核通过\r\n1）	审核未通过，配置可修改\r\n2）	审核通过，配置不可删除，只能取消审核通过',
  `creator_id` int(11) NOT NULL COMMENT '取自sys_user.id',
  `create_time` datetime NOT NULL,
  `editor_id` int(11) DEFAULT NULL COMMENT '取自sys_user.id',
  `edit_time` datetime DEFAULT NULL,
  `remarks` varchar(255) DEFAULT '' COMMENT '描述信息',
  `user_region1` varchar(1024) DEFAULT '' COMMENT '预留自定义域1',
  `user_region2` varchar(1024) DEFAULT '' COMMENT '预留自定义域2',
  `user_region3` varchar(1024) DEFAULT '' COMMENT '预留自定义域3',
  `user_region4` varchar(1024) DEFAULT '' COMMENT '预留自定义域4',
  `user_region5` varchar(1024) DEFAULT '' COMMENT '预留自定义域5',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4;