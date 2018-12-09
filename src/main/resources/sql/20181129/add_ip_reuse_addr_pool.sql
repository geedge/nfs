-- ----------------------------
-- 地址池管理表
-- ----------------------------
DROP TABLE IF EXISTS `ip_reuse_addr_pool`;
CREATE TABLE `ip_reuse_addr_pool` (
  `cfg_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `addr_pool_id` int(11) NOT NULL COMMENT '地址池ID',
  `addr_pool_name` varchar(128) DEFAULT NULL COMMENT '地址池名称',
  `ip_total` int(11) NOT NULL DEFAULT 0 COMMENT '地址池IP总数',
  `available_ip_total` int(11) NOT NULL DEFAULT 0 COMMENT '地址池可用IP数',
  `action` int(11) NOT NULL COMMENT '1:阻断，2：监测， 5： FD 白名单，6：监测白名单，7: FD 监测都白名单，应与业务ID所代表的逻辑相匹配，8-灰名单',
  `is_valid` int(11) NOT NULL COMMENT '0无效，1有效，-1删除\r\n1）	未审核时配置可删除\r\n2）	审核通过，此字段置1\r\n3）	取消审核通过，此字段置0',
  `is_audit` int(11) NOT NULL COMMENT '0未审核，1审核通过，2审核未通过，3取消审核通过\r\n1）	审核未通过，配置可修改\r\n2）	审核通过，配置不可删除，只能取消审核通过',
  `cfg_region_code` int(11) DEFAULT NULL,
  `cfg_type` varchar(64) DEFAULT '',
  `function_id` int(11) NOT NULL,
  `service_id` int(11) NOT NULL COMMENT '参考系统业务类型管理表',
  `compile_id` int(11) NOT NULL COMMENT '取自服务接口返回的maat配置的编译id，配置初始入库时获取。',
  `is_area_effective` int(11) NOT NULL DEFAULT 0 COMMENT '0否，1是',
  `area_effective_ids` varchar(1024) DEFAULT NULL COMMENT '多个以英文逗号分隔',
  `request_id` int(11) NOT NULL COMMENT '取自request_info.id',
  `classify` varchar(128) DEFAULT '' COMMENT '分类id，多个用英文逗号分隔',
  `attribute` varchar(128) DEFAULT '' COMMENT '性质id，多个用英文逗号分隔',
  `lable` varchar(128) DEFAULT '' COMMENT '标签id,多个用英文逗号分隔',
  `creator_id` int(11) NOT NULL COMMENT '取自sys_user.id',
  `create_time` datetime NOT NULL,
  `editor_id` int(11) DEFAULT NULL COMMENT '取自sys_user.id',
  `edit_time` datetime DEFAULT NULL,
  `auditor_id` int(11) DEFAULT NULL COMMENT '取自sys_user.id',
  `audit_time` datetime DEFAULT NULL,
  `description` varchar(200) DEFAULT '' COMMENT '描述信息',
  `cancel_request_id` int(11) DEFAULT NULL COMMENT '取消审核来函',
  `user_region1` varchar(1024) DEFAULT '' COMMENT '预留自定义域1',
  `user_region2` varchar(1024) DEFAULT '' COMMENT '预留自定义域2',
  `user_region3` varchar(1024) DEFAULT '' COMMENT '预留自定义域3',
  `user_region4` varchar(1024) DEFAULT '' COMMENT '预留自定义域4',
  `user_region5` varchar(1024) DEFAULT '' COMMENT '预留自定义域5',
  PRIMARY KEY (`cfg_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4;
