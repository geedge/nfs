/*
Navicat MariaDB Data Transfer

Source Server         : 192.168.10.204
Source Server Version : 100214
Source Host           : 192.168.10.204:3306
Source Database       : gwall_dev

Target Server Type    : MariaDB
Target Server Version : 100214
File Encoding         : 65001

Date: 2018-10-30 10:45:57
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for group_area_info
-- ----------------------------
DROP TABLE IF EXISTS `group_area_info`;
CREATE TABLE `group_area_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NOT NULL COMMENT '策略组group_id',
  `area_code` int(11) NOT NULL COMMENT '区域字典Code值',
  `is_valid` int(2) NOT NULL DEFAULT 1 COMMENT '-1删除 1有效 0无效',
  `creator_id` int(11) NOT NULL,
  `create_time` datetime NOT NULL,
  `editor_id` int(11) DEFAULT NULL,
  `edit_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4;
