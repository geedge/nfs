/*
SQLyog Professional v12.08 (64 bit)
MySQL - 10.2.14-MariaDB-log 
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;
DROP TABLE IF EXISTS `asn_ip_cfg`;
CREATE TABLE `asn_ip_cfg` (
	`cfg_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
	`cfg_desc` VARCHAR(128) DEFAULT NULL,
	`ip_type` INT(11) NOT NULL COMMENT 'IPV4=4,IPV6=6',
	`direction` INT(11) NOT NULL COMMENT '0双向，1单向，默认缺省为双向。',
	`protocol` INT(11) NOT NULL COMMENT '6表示tcp,17表示udp，0表示任意',
	`protocol_id` INT(11) NOT NULL COMMENT '非0时，maat规范需写入通用IP配置表与通用协议类型配置表 此表固定写0',
	`action` INT(11) NOT NULL COMMENT '1:阻断，2：监测， 5： FD 白名单，6：监测白名单，7: FD 监测都白名单，应与业务ID所代表的逻辑相匹配，8-灰名单',
	`is_valid` INT(11) NOT NULL COMMENT '0无效，1有效，-1删除;1）	未审核时配置可删除;2）	审核通过，此字段置1;3）	取消审核通过，此字段置0',
	`is_audit` INT(11) NOT NULL COMMENT '0未审核，1审核通过，2审核未通过，3取消审核通过;1）	审核未通过，配置可修改;2）	审核通过，配置不可删除，只能取消审核通过',
	`creator_id` INT(11) NOT NULL COMMENT '取自sys_user.id',
	`create_time` DATETIME NOT NULL,
	`editor_id` INT(11) DEFAULT NULL COMMENT '取自sys_user.id',
	`edit_time` DATETIME DEFAULT NULL,
	`auditor_id` INT(11) DEFAULT NULL COMMENT '取自sys_user.id',
	`audit_time` DATETIME DEFAULT NULL,
	`service_id` INT(11) NOT NULL COMMENT '参考系统业务类型管理表',
	`request_id` INT(11) NOT NULL COMMENT '取自request_info.id',
	`region_id` INT(11) NOT NULL COMMENT '取自服务接口返回的maat配置的域id，配置初始入库时获取。',
	`is_area_effective` INT(11) NOT NULL COMMENT '0否，1是',
	`classify` VARCHAR(128) DEFAULT NULL COMMENT '分类id，多个用英文逗号分隔',
	`attribute` VARCHAR(128) DEFAULT NULL COMMENT '性质id，多个用英文逗号分隔',
	`lable` VARCHAR(128) DEFAULT NULL COMMENT '标签id,多个用英文逗号分隔',
	`area_effective_ids` VARCHAR(1024) DEFAULT NULL COMMENT '多个英文逗号分隔',
	`function_id` INT(11) DEFAULT NULL,
	`cfg_region_code` INT(11) DEFAULT NULL,
	`cfg_type` VARCHAR(64) DEFAULT NULL,
	`ip_pattern` INT(11) NOT NULL,
	`src_ip_address` VARCHAR(128) NOT NULL,
	`port_pattern` INT (11) NOT NULL,
	`src_port` VARCHAR (16) NOT NULL,
	`dest_ip_address` VARCHAR (128) DEFAULT NULL,
	`dest_port` VARCHAR(16) DEFAULT NULL,
	`ratelimit` VARCHAR(10) DEFAULT NULL,
	`asn_ip_group` INT(11) DEFAULT NULL,
	`user_region1` VARCHAR(1024) DEFAULT '' COMMENT '预留自定义域1',
	`user_region2` VARCHAR(1024) DEFAULT '' COMMENT '预留自定义域2',
	`user_region3` VARCHAR(1024) DEFAULT '' COMMENT '预留自定义域3',
	`user_region4` VARCHAR(1024) DEFAULT '' COMMENT '预留自定义域4',
	`user_region5` VARCHAR(1024) DEFAULT '' COMMENT '预留自定义域5',
	PRIMARY KEY (`cfg_id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8; 


#修改config_group_info comment, 新增asn说明
ALTER TABLE config_group_info MODIFY group_type INT(11) NULL COMMENT '1:协议，2：内容，3：区域 ,4 asn'
#修改policy_group_info comment, 新增asn说明,并新增一列 service_group_id用于存放从综合服务获取的组号
ALTER TABLE policy_group_info MODIFY group_type INT(2) NULL COMMENT '1:dns 2:ip复用, 4 asn'
ALTER TABLE policy_group_info ADD service_group_id INT(11) NULL COMMENT '从综合服务获取的组号，用于存放asn的组号.由于group_id是自增的主键，从综合服务获取的组号会与之冲突'
#GROUP_TYPE字典更新，增加一列ASN
SELECT * FROM sys_data_dictionary_item  WHERE dictionary_id=(SELECT ID FROM sys_data_dictionary_name WHERE mark='GROUP_TYPE')
INSERT INTO sys_data_dictionary_item (item_code,item_value,item_desc,item_sort,STATUS,TYPE,dictionary_id)
VALUE(4,'ASN','ANS号',0,1,1,83)
