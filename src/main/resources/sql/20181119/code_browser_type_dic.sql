/*
Navicat MySQL Data Transfer

Source Server         : 192.168.10.204_3306
Source Server Version : 50505
Source Host           : 192.168.10.204:3306
Source Database       : gwall_dev

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2018-11-19 16:52:26
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for code_browser_type_dic
-- ----------------------------
DROP TABLE IF EXISTS `code_browser_type_dic`;
CREATE TABLE `code_browser_type_dic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `browser_type` varchar(255) DEFAULT NULL,
  `view_code` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of code_browser_type_dic
-- ----------------------------
INSERT INTO `code_browser_type_dic` VALUES ('1', 'Unknow', '0', '');
INSERT INTO `code_browser_type_dic` VALUES ('2', 'Google Chrome', '400001', '');
INSERT INTO `code_browser_type_dic` VALUES ('3', 'Mozilla Firefox', '400101', '');
INSERT INTO `code_browser_type_dic` VALUES ('4', 'Safari', '400201', '');
INSERT INTO `code_browser_type_dic` VALUES ('5', 'MSIE', '400301', '');
INSERT INTO `code_browser_type_dic` VALUES ('6', 'Opera', '400401', '');
INSERT INTO `code_browser_type_dic` VALUES ('7', 'Yandex', '400501', '');
INSERT INTO `code_browser_type_dic` VALUES ('8', 'Android brower', '400601', '');
INSERT INTO `code_browser_type_dic` VALUES ('9', 'Konqueror', '400701', '');
INSERT INTO `code_browser_type_dic` VALUES ('10', 'Bing', '400801', '');
INSERT INTO `code_browser_type_dic` VALUES ('11', 'SogouMobileBrowser', '400901', '');
INSERT INTO `code_browser_type_dic` VALUES ('12', 'SogouBrowser', '401001', '');
INSERT INTO `code_browser_type_dic` VALUES ('13', 'OPPO', '401101', '');
INSERT INTO `code_browser_type_dic` VALUES ('14', 'UCBrowser', '401201', '');
INSERT INTO `code_browser_type_dic` VALUES ('15', '2345Browser', '401301', '');
INSERT INTO `code_browser_type_dic` VALUES ('16', 'QQBrowser', '401401', '');
INSERT INTO `code_browser_type_dic` VALUES ('17', 'QQMobileBrowser', '401501', '');
INSERT INTO `code_browser_type_dic` VALUES ('18', 'baidubrowser', '401601', '');
INSERT INTO `code_browser_type_dic` VALUES ('19', 'LieBao', '401701', '');
INSERT INTO `code_browser_type_dic` VALUES ('20', 'MiuiBrowser', '401801', '');
INSERT INTO `code_browser_type_dic` VALUES ('21', 'SamsungBrowser', '401901', '');
INSERT INTO `code_browser_type_dic` VALUES ('22', 'IE', '402001', '');
INSERT INTO `code_browser_type_dic` VALUES ('23', 'GriOS', '402101', '');
INSERT INTO `code_browser_type_dic` VALUES ('24', '360SE', '402201', '');
INSERT INTO `code_browser_type_dic` VALUES ('25', 'Maxthon', '402301', '');
INSERT INTO `code_browser_type_dic` VALUES ('26', 'The World', '402401', '');
INSERT INTO `code_browser_type_dic` VALUES ('27', 'JuziBrowser', '402501', '');
INSERT INTO `code_browser_type_dic` VALUES ('28', 'TencentTraveler', '402601', '');
INSERT INTO `code_browser_type_dic` VALUES ('29', 'Unspecified', '268435455', '');
