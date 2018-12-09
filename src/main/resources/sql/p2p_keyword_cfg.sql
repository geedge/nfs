/*
Navicat MariaDB Data Transfer

Source Server         : 10.0.6.249
Source Server Version : 100214
Source Host           : 10.0.6.249:3306
Source Database       : gwall

Target Server Type    : MariaDB
Target Server Version : 100214
File Encoding         : 65001

Date: 2018-08-18 21:11:57
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for p2p_keyword_cfg
-- ----------------------------
DROP TABLE IF EXISTS `p2p_keyword_cfg`;
CREATE TABLE `p2p_keyword_cfg` (
  `cfg_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `cfg_desc` varchar(128) DEFAULT NULL,
  `cfg_keywords` varchar(64) NOT NULL,
  `action` int(11) NOT NULL COMMENT '1:阻断，2：监测， 5： FD 白名单，6：监测白名单，7: FD 监测都白名单，应与业务ID所代表的逻辑相匹配，8-灰名单',
  `is_valid` int(11) NOT NULL COMMENT '0无效，1有效，-1删除;1）	未审核时配置可删除;2）	审核通过，此字段置1;3）	取消审核通过，此字段置0',
  `is_audit` int(11) NOT NULL COMMENT '0未审核，1审核通过，2审核未通过，3取消审核通过;1）	审核未通过，配置可修改;2）	审核通过，配置不可删除，只能取消审核通过',
  `creator_id` int(11) NOT NULL COMMENT '取自sys_user.id',
  `create_time` datetime NOT NULL,
  `editor_id` int(11) DEFAULT NULL COMMENT '取自sys_user.id',
  `edit_time` datetime DEFAULT NULL,
  `auditor_id` int(11) DEFAULT NULL COMMENT '取自sys_user.id',
  `audit_time` datetime DEFAULT NULL,
  `service_id` int(11) NOT NULL COMMENT '参考系统业务类型管理表',
  `request_id` int(11) NOT NULL COMMENT '取自request_info.id',
  `compile_id` int(11) NOT NULL COMMENT '取自服务接口返回的maat配置的编译id，配置初始入库时获取。',
  `is_area_effective` int(11) NOT NULL COMMENT '0否，1是',
  `classify` varchar(128) DEFAULT NULL COMMENT '分类id，多个用英文逗号分隔',
  `attribute` varchar(128) DEFAULT NULL COMMENT '性质id，多个用英文逗号分隔',
  `lable` varchar(128) DEFAULT NULL COMMENT '标签id,多个用英文逗号分隔',
  `expr_type` int(11) NOT NULL COMMENT '0：无表达式，1：与表达式',
  `match_method` int(11) NOT NULL COMMENT 'expr_type:0时有意义，其它情况必须置0。0：子串匹配；1：右匹配；2：左匹配；3：完全匹配',
  `is_hexbin` int(11) NOT NULL COMMENT '默认为0:大小写不敏感，且非HEX;1:HEX格式二进制;2:大小写敏感，且非HEX',
  `area_effective_ids` varchar(1024) DEFAULT NULL COMMENT '多个英文逗号分隔',
  `function_id` int(11) DEFAULT NULL,
  `cfg_region_code` int(11) DEFAULT NULL,
  `cfg_type` varchar(64) DEFAULT NULL,
  `user_region1` varchar(1024) DEFAULT '' COMMENT '预留自定义域1',
  `user_region2` varchar(1024) DEFAULT '' COMMENT '预留自定义域2',
  `user_region3` varchar(1024) DEFAULT '' COMMENT '预留自定义域3',
  `user_region4` varchar(1024) DEFAULT '' COMMENT '预留自定义域4',
  `user_region5` varchar(1024) DEFAULT '' COMMENT '预留自定义域5',
  PRIMARY KEY (`cfg_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
