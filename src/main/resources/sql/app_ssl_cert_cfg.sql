/*
Navicat MariaDB Data Transfer

Source Server         : 10.0.6.249
Source Server Version : 100214
Source Host           : 10.0.6.249:3306
Source Database       : gwall

Target Server Type    : MariaDB
Target Server Version : 100214
File Encoding         : 65001

Date: 2018-08-20 20:39:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for app_ssl_cert_cfg
-- ----------------------------
DROP TABLE IF EXISTS `app_ssl_cert_cfg`;
CREATE TABLE `app_ssl_cert_cfg` (
  `cfg_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `district` varchar(128) NOT NULL,
  `cfg_keywords` varchar(1024) NOT NULL,
  `app_code` int(11) NOT NULL,
  `behav_code` int(11) DEFAULT NULL,
  `spec_service_id` varchar(255) NOT NULL COMMENT 'specific_service_cfg .spec_service_id',
  `cfg_desc` varchar(128) DEFAULT '',
  `action` int(11) NOT NULL COMMENT '1ï¼šé˜»æ–­ï¼ˆrejectï¼‰2ï¼šç›‘æµ‹ï¼ˆmonitï¼‰5ï¼šç®¡æŽ§ç™½åå•ï¼ˆControl whitelistï¼‰6ï¼šç›‘æµ‹ç™½åå•ï¼ˆmonit whitelistï¼‰7ï¼šç®¡æŽ§ç›‘æµ‹éƒ½ç™½åå•ï¼ˆglobal whitelistï¼‰',
  `is_valid` int(11) NOT NULL COMMENT '0æ— æ•ˆï¼Œ1æœ‰æ•ˆï¼Œ-1åˆ é™¤\r\n1ï¼‰	æœªå®¡æ ¸æ—¶é…ç½®å¯åˆ é™¤\r\n2ï¼‰	å®¡æ ¸é€šè¿‡ï¼Œæ­¤å­—æ®µç½®1\r\n3ï¼‰	å–æ¶ˆå®¡æ ¸é€šè¿‡ï¼Œæ­¤å­—æ®µç½®0',
  `is_audit` int(11) NOT NULL COMMENT '0æœªå®¡æ ¸ï¼Œ1å®¡æ ¸é€šè¿‡ï¼Œ2å®¡æ ¸æœªé€šè¿‡ï¼Œ3å–æ¶ˆå®¡æ ¸é€šè¿‡\r\n1ï¼‰	å®¡æ ¸æœªé€šè¿‡ï¼Œé…ç½®å¯ä¿®æ”¹\r\n2ï¼‰	å®¡æ ¸é€šè¿‡ï¼Œé…ç½®ä¸å¯åˆ é™¤ï¼Œåªèƒ½å–æ¶ˆå®¡æ ¸é€šè¿‡',
  `creator_id` int(11) NOT NULL COMMENT 'sys_user.id',
  `create_time` datetime NOT NULL,
  `editor_id` int(11) DEFAULT NULL COMMENT '来自sys_user.id',
  `edit_time` datetime DEFAULT NULL,
  `auditor_id` int(11) DEFAULT NULL COMMENT '来自sys_user.id',
  `audit_time` datetime DEFAULT NULL,
  `service_id` int(11) NOT NULL COMMENT '业务id',
  `request_id` int(11) NOT NULL COMMENT '来自request_info.id',
  `compile_id` int(11) NOT NULL,
  `is_area_effective` int(11) NOT NULL DEFAULT 0,
  `classify` varchar(128) DEFAULT '',
  `attribute` varchar(128) DEFAULT '',
  `lable` varchar(128) DEFAULT '',
  `area_effective_ids` varchar(1024) DEFAULT '',
  `function_id` int(11) NOT NULL,
  `ratelimit` int(11) DEFAULT 0 COMMENT '限速比例',
  `cfg_type` varchar(64) NOT NULL DEFAULT '',
  `cfg_region_code` int(11) NOT NULL,
  `expr_type` int(11) NOT NULL DEFAULT 0 COMMENT '0：无表达式，1：与表达式',
  `match_method` int(11) NOT NULL DEFAULT 3 COMMENT 'expr_type:0时有意义，其它情况必须置0。0：子串匹配；1：右匹配；2：左匹配；3：完全匹配',
  `is_hexbin` int(11) NOT NULL DEFAULT 0 COMMENT '默认为0:大小写不敏感，且非HEX;1:HEX格式二进制;2:大小写敏感，且非HEX',
  `user_region1` varchar(1024) DEFAULT '' COMMENT '预留自定义域1',
  `user_region2` varchar(1024) DEFAULT '' COMMENT '预留自定义域2',
  `user_region3` varchar(1024) DEFAULT '' COMMENT '预留自定义域3',
  `user_region4` varchar(1024) DEFAULT '' COMMENT '预留自定义域4',
  `user_region5` varchar(1024) DEFAULT '' COMMENT '预留自定义域5',
  PRIMARY KEY (`cfg_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
