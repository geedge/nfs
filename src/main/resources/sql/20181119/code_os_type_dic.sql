/*
Navicat MySQL Data Transfer

Source Server         : 192.168.10.204_3306
Source Server Version : 50505
Source Host           : 192.168.10.204:3306
Source Database       : gwall_dev

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2018-11-19 16:52:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for code_os_type_dic
-- ----------------------------
DROP TABLE IF EXISTS `code_os_type_dic`;
CREATE TABLE `code_os_type_dic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `os_type` varchar(255) DEFAULT NULL,
  `view_code` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of code_os_type_dic
-- ----------------------------
INSERT INTO `code_os_type_dic` VALUES ('1', 'Unknow', '0', '');
INSERT INTO `code_os_type_dic` VALUES ('2', 'Windows', '300001', '');
INSERT INTO `code_os_type_dic` VALUES ('3', 'Linux', '300101', '');
INSERT INTO `code_os_type_dic` VALUES ('4', 'Mac OS X', '300201', '');
INSERT INTO `code_os_type_dic` VALUES ('5', 'OpenBSD', '300301', '');
INSERT INTO `code_os_type_dic` VALUES ('6', 'FreeBSD', '300401', '');
INSERT INTO `code_os_type_dic` VALUES ('7', 'Solaris', '300501', '');
INSERT INTO `code_os_type_dic` VALUES ('8', 'OpenVMS', '300601', '');
INSERT INTO `code_os_type_dic` VALUES ('9', 'NeXTSTEP', '300701', '');
INSERT INTO `code_os_type_dic` VALUES ('10', 'HP-UX', '300801', '');
INSERT INTO `code_os_type_dic` VALUES ('11', 'Tru64', '300901', '');
INSERT INTO `code_os_type_dic` VALUES ('12', 'iOS ', '301001', '');
INSERT INTO `code_os_type_dic` VALUES ('13', 'Blackberry', '301101', '');
INSERT INTO `code_os_type_dic` VALUES ('14', 'Windows Phone', '301201', null);
INSERT INTO `code_os_type_dic` VALUES ('15', 'Android', '301301', null);
INSERT INTO `code_os_type_dic` VALUES ('16', 'Symbian', '301401', null);
INSERT INTO `code_os_type_dic` VALUES ('17', 'Unspecified', '268435455', null);
