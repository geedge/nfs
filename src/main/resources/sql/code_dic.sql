/*
Navicat MySQL Data Transfer

Source Server         : 10.0.6.249
Source Server Version : 50505
Source Host           : 10.0.6.249:3306
Source Database       : gwall

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2018-08-15 20:07:57
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `code_app_dic`
-- ----------------------------
DROP TABLE IF EXISTS `code_app_dic`;
CREATE TABLE `code_app_dic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(255) DEFAULT '',
  `app_name_zh` varchar(255) DEFAULT NULL,
  `view_code` varchar(255) DEFAULT '' COMMENT '应用编码',
  `icon` varchar(255) DEFAULT '',
  `behavior` varchar(255) DEFAULT '' COMMENT '行为细分',
  `core_app` int(11) DEFAULT NULL COMMENT '是否是核心应用（1：是，0：否）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=399 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of code_app_dic
-- ----------------------------
INSERT INTO `code_app_dic` VALUES ('1', 'Unknow', '超出APP范畴', '0', '', '', '1');
INSERT INTO `code_app_dic` VALUES ('2', 'Skype', 'Skype', '100001', '', 'Skype video calls/calls\nSkype voice\nSkype message\nSkype transfer', '1');
INSERT INTO `code_app_dic` VALUES ('3', 'Instagram', 'Instagram', '100101', '', 'Instagram video\nInstagram message\nInstagram live\nInstagram adding photos and videos', '1');
INSERT INTO `code_app_dic` VALUES ('4', 'Youtube', 'Youtube', '100201', '', 'Youtube video\nYoutube live\nYoutube adding videos', '1');
INSERT INTO `code_app_dic` VALUES ('5', 'Facebook', 'Facebook', '100301', '', 'Facebook video\nFacebook live\nFacebook message', '1');
INSERT INTO `code_app_dic` VALUES ('6', 'What‘s App', 'What‘s App', '100401', '', 'What\'s App message\nWhat\'s App calls/video calls\nWhat\'s App transfer', '1');
INSERT INTO `code_app_dic` VALUES ('7', 'Periscope', 'Periscope', '100501', '', '', '1');
INSERT INTO `code_app_dic` VALUES ('8', 'Viber', 'Viber', '100601', '', 'Viber calls/video calls\nViber message\nViber transfer', '1');
INSERT INTO `code_app_dic` VALUES ('9', 'Twitter', 'Twitter', '100701', '', '', '1');
INSERT INTO `code_app_dic` VALUES ('10', 'Vkontakte', 'Vkontakte', '100801', '', 'Vkontakte video\nVkontakte message\nVkontakte live', '1');
INSERT INTO `code_app_dic` VALUES ('11', 'Telegram', 'Telegram', '100901', '', 'Telegram message\nTelegram calls/video calls\nTelegram transfer', '1');
INSERT INTO `code_app_dic` VALUES ('12', 'Odnoklassniki', 'Odnoklassniki', '101001', '', 'Odnoklassniki message\nOdnoklassniki video', '1');
INSERT INTO `code_app_dic` VALUES ('13', 'Mail agent', 'Mail agent', '101101', '', 'Mail agent message\nMail agent calls\nMail agent transfer', '1');
INSERT INTO `code_app_dic` VALUES ('14', 'FaceTime', 'FaceTime', '101201', '', 'Facetime video calls\nFacetime voice calls', '1');
INSERT INTO `code_app_dic` VALUES ('15', 'Snapchat', 'Snapchat', '101301', '', '', '1');
INSERT INTO `code_app_dic` VALUES ('16', 'Google', 'Google', '101401', '', '', '1');
INSERT INTO `code_app_dic` VALUES ('17', 'Yandex', 'Yandex', '101501', '', '', '1');
INSERT INTO `code_app_dic` VALUES ('18', 'Yandex Browser Turbo', 'Yandex Browser Turbo', '101601', '', '', '1');
INSERT INTO `code_app_dic` VALUES ('19', 'Opera', 'Opera', '101701', '', '', '1');
INSERT INTO `code_app_dic` VALUES ('20', 'Opera Turbo', 'Opera Turbo', '101702', '', '', '1');
INSERT INTO `code_app_dic` VALUES ('21', 'Opera mini', 'Opera mini', '101703', '', '', '1');
INSERT INTO `code_app_dic` VALUES ('22', 'AppStore', 'AppStore', '101801', '', '', '1');
INSERT INTO `code_app_dic` VALUES ('23', 'PlayMarket', 'PlayMarket', '101901', '', '  ', '1');
INSERT INTO `code_app_dic` VALUES ('24', 'Drugvokrug social networking', 'Drugvokrug social networking', '102001', '', '', '1');
INSERT INTO `code_app_dic` VALUES ('25', 'Badoo social networking', 'Badoo social networking', '102101', '', '', '1');
INSERT INTO `code_app_dic` VALUES ('26', 'WeChat', 'WeChat', '102201', '', '', '1');
INSERT INTO `code_app_dic` VALUES ('27', 'Zello', 'Zello', '102301', '', '', '1');
INSERT INTO `code_app_dic` VALUES ('28', 'Freegate', 'Freegate', '102401', '', '', '1');
INSERT INTO `code_app_dic` VALUES ('29', 'Psiphon', 'Psiphon', '102501', '', '', '1');
INSERT INTO `code_app_dic` VALUES ('30', 'TOR', 'TOR', '102601', '', '', '1');
INSERT INTO `code_app_dic` VALUES ('31', 'Tor browser', 'Tor browser', '102602', '', '', '1');
INSERT INTO `code_app_dic` VALUES ('32', 'Tor mode', 'Tor mode', '102603', '', '', '1');
INSERT INTO `code_app_dic` VALUES ('33', 'Orbot', 'Orbot', '102701', '', '', '1');
INSERT INTO `code_app_dic` VALUES ('34', 'Tunnel Bear VPN', 'Tunnel Bear VPN', '102801', '', '', '1');
INSERT INTO `code_app_dic` VALUES ('35', 'HotSpot Shield VPN', 'HotSpot Shield VPN', '102901', '', '', '1');
INSERT INTO `code_app_dic` VALUES ('36', 'VPN Master', 'VPN Master', '103001', '', '', '1');
INSERT INTO `code_app_dic` VALUES ('37', 'Hola VPN', 'Hola VPN', '103101', '', '', '1');
INSERT INTO `code_app_dic` VALUES ('38', 'HideMyAss VPN', 'HideMyAss VPN', '103201', '', '', '1');
INSERT INTO `code_app_dic` VALUES ('39', 'Zenmate VPN', 'Zenmate VPN', '103301', '', '', '1');
INSERT INTO `code_app_dic` VALUES ('40', 'Unspecified', 'Unspecified', '268435455', '', '', '1');
INSERT INTO `code_app_dic` VALUES ('41', 'RFA', '', '200001', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('42', 'VOA', '', '200002', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('43', 'lantern', '', '200003', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('44', 'Line', '', '200004', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('45', 'voxer', '', '200005', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('46', 'ZeroNet', '', '200006', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('47', 'Zalo', '', '200007', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('48', 'free_browser', '', '200008', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('49', 'Diaspora', '', '200009', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('50', 'browsec', '', '200010', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('51', 'Chrome_plugin_chuguo', '', '200011', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('52', 'GoGo_SocksHttp', '', '200012', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('53', 'ultrasurf_chrome', '', '200013', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('54', 'pinterest', '', '200014', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('55', 'Swisscom', '', '200015', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('56', 'Threema', '', '200016', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('57', 'Circuit_iOS', '', '200017', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('58', 'Chrome_plugin_FastsTunnel', '', '200018', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('59', 'OoVoo', '', '200019', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('60', 'Vine', '', '200020', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('61', 'Keek', '', '200021', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('62', 'Hi', '', '200022', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('63', 'HeyTell', '', '200023', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('64', 'HikeMessenger', '', '200024', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('65', 'Nonoh', '', '200025', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('66', 'Path', '', '200026', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('67', 'Kik', '', '200027', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('68', 'BBM', '', '200028', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('69', 'Talkray', '', '200029', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('70', 'HelloTalk', '', '200030', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('71', 'Skout', '', '200031', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('72', 'MeetMe', '', '200032', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('73', 'Nextdoor', '', '200033', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('74', 'Shots', '', '200034', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('75', 'Zoosk', '', '200035', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('76', 'TextPlus', '', '200036', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('77', 'Maaii', '', '200037', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('78', 'Ustream', '', '200038', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('79', 'Yikyak', '', '200039', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('80', 'chatsecure', '', '200040', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('81', 'freecalls', '', '200041', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('82', 'SignalPrivateMessenger', '', '200042', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('83', 'USGOV_CIA', '', '200043', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('84', 'USGOV_FBI', '', '200044', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('85', 'USGOV_DHS', '', '200045', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('86', 'shenxun_Secure_Call_android', '', '200046', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('87', 'shenxun_Secure_Call_web', '', '200047', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('88', 'UProxy', '', '200048', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('89', 'soma', '', '200049', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('90', 'PlayerUnknown\'s_Battlegrounds', '', '200050', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('91', 'STEAM', '', '200051', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('92', 'dalai_lama', '', '200052', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('93', 'Steam_forum', '', '200053', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('94', 'Microsoft Online Certificate', '微软在线证书', '200054', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('95', 'sogou_spider', 'sogou_spider', '200055', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('96', 'baidu_mobie', 'baidu_mobie', '200056', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('97', 'Apple Push Service', '苹果推送服务', '200057', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('98', 'IQIYI', '爱奇艺', '200058', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('99', 'baidu_spider', 'baidu_spider', '200059', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('100', 'Weibo', '微博', '200060', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('101', 'Apple captive service', '苹果captive服务', '200061', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('102', 'Microsoft wns', '微软wns', '200062', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('103', '360_safe_pc', '360_safe_pc', '200063', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('104', 'Sogou Public Service', '搜狗公有服务', '200064', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('105', 'Lenovo app_POST service', '联想app_POST服务', '200065', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('106', 'Apple securityd service', '苹果securityd服务', '200066', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('107', 'Weather pass', '天气通', '200067', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('108', 'Apple Service Pack', '苹果服务包', '200068', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('109', 'Fast pressure', '快压', '200069', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('110', 'SemrushBot', 'SemrushBot', '200070', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('111', 'Adrefs_spider', 'Adrefs_spider', '200071', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('112', 'AndroidDownloadManager', 'AndroidDownloadManager', '200072', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('113', 'XenderTop', 'XenderTop', '200073', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('114', 'microsoft_update', 'microsoft_update', '200074', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('115', 'Youku', '优酷', '200075', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('116', 'Google_spider', 'goolge_spider', '200076', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('117', 'Microsoft User Experience and Telemetry Components', '微软用户体验与遥测组件', '200077', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('118', 'MICROSOFT_DEVICE_METADATA_RETRIEVAL_CLIENT', 'MICROSOFT_DEVICE_METADATA_RETRIEVAL_CLIENT', '200078', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('119', 'Microsoft ncsi', '微软ncsi', '200079', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('120', 'Microsoft Privacy Collection Service', '微软隐私收集服务', '200080', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('121', 'uTorrent', 'uTorrent', '200081', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('122', 'Edge', 'Edge', '200082', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('123', 'Lenovo Music Store', '联想乐商店', '200083', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('124', 'LieBao', 'LieBao', '200084', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('125', 'majesti_spider', 'majesti_spider', '200085', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('126', 'DotBot', 'DotBot', '200086', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('127', 'Millet Update', '小米Update', '200087', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('128', 'Today’s headlines', '今日头条', '200088', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('129', 'Microsoft live', '微软live', '200089', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('130', 'Microsoft OneNote', '微软OneNote', '200090', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('131', 'Bing_spider', 'bing_spider', '200091', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('132', 'Apple location service', '苹果位置服务', '200092', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('133', 'Kuaishou', '快手', '200093', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('134', 'LeTV video', '乐视视频', '200094', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('135', 'QQ', 'QQ', '200095', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('136', 'Wps', 'wps', '200096', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('137', 'kugou music', '酷狗音乐', '200097', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('138', 'kuwo music', '酷我音乐', '200098', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('139', '360_common_service', '360_common_service', '200099', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('140', 'Camera360', 'Camera360', '200100', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('141', 'Outlook', 'outlook', '200101', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('142', 'zhihu', '知乎', '200102', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('143', 'Tencent video', '腾讯视频', '200103', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('144', 'funshion', '风行', '200104', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('145', 'QQ shared service', 'QQ共有服务', '200105', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('146', 'Microsoft wdcp service', '微软wdcp服务', '200106', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('147', 'Microsoft Media_center_pc', '微软Media_center_pc', '200107', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('148', '360 phone helper', '360手机助手', '200108', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('149', 'Kingsoft Public Service', '金山公有服务', '200109', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('150', 'Ios_wifi connection', 'ios_wifi连接', '200110', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('151', 'Teamviewer', 'teamviewer', '200111', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('152', 'Baidu cloud disk', '百度云盘', '200112', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('153', 'Tencent News', '腾讯新闻', '200113', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('154', 'Baidu map', '百度地图', '200114', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('155', 'Panda TV', '熊猫TV', '200115', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('156', 'Thunder', '迅雷', '200116', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('157', 'NetEase cloud music', '网易云音乐', '200117', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('158', 'QQ music', 'QQ音乐', '200118', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('159', 'Itools', 'itools', '200119', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('160', 'Jingdong Finance', '京东金融', '200120', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('161', 'PycURL', 'PycURL', '200121', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('162', 'Yahoo_Spider', 'Yahoo_Spider', '200122', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('163', 'Himalayan FM', '喜马拉雅FM', '200123', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('164', 'Youdao_dict', 'youdao_dict', '200124', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('165', 'Microsoft_spynet', 'microsoft_spynet', '200125', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('166', 'proximicBot', 'proximicBot', '200126', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('167', 'Foxit_reader', 'foxit_reader', '200127', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('168', 'Sohu Video', '搜狐视频', '200128', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('169', 'BlueStacks', 'BlueStacks', '200129', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('170', 'Beauty group', '美团', '200130', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('171', 'Kmplayer', '暴风影音', '200131', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('172', 'TaoBao', '淘宝', '200132', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('173', 'Exabot', 'Exabot', '200133', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('174', 'Ubuntu_apt_get', 'ubuntu_apt_get', '200134', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('175', 'Oriental Fortune', '东方财富通', '200135', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('176', 'JikeSpider', 'JikeSpider', '200136', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('177', 'Itunesstored', 'itunesstored', '200137', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('178', 'GAD map', '高德地图', '200138', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('179', 'ifeng News', '凤凰新闻', '200139', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('180', 'BoardReaderBot', 'BoardReaderBot', '200140', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('181', 'AmazonBot', 'AmazonBot', '200141', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('182', 'Alipay', '支付宝', '200142', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('183', 'Alipay security controls', '支付宝安全控件', '200143', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('184', '360 security guards (antivirus)', '360安全卫士(杀毒)', '200144', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('185', '360 logon service', '360登录服务', '200145', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('186', 'BLP_bbot', 'BLP_bbot', '200146', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('187', 'Keep', 'keep', '200147', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('188', 'WiFi universal key', 'WiFi万能钥匙', '200148', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('189', 'Baidu Music', '百度音乐', '200149', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('190', 'Vip.com', '唯品会', '200150', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('191', 'JD.COM', '京东', '200151', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('192', 'sina', '新浪新闻', '200152', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('193', 'youdao Public service', '有道公有服务', '200153', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('194', 'Jinshan public service', '金山公有服务', '200154', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('195', 'Baidu guard', '百度卫士', '200155', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('196', 'Apple system update service', '苹果系统更新服务', '200156', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('197', 'momo', '陌陌', '200157', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('198', 'CNZZ statistics', 'CNZZ统计', '200158', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('199', 'Microsoft one_drive service', '微软one_drive服务', '200159', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('200', 'WindowsStore', 'WindowsStore', '200160', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('201', 'duba', '毒霸', '200161', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('202', 'Baidu HD', '百度HD', '200162', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('203', 'qunar', '去哪儿旅行', '200163', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('204', 'qianxunyingshi', '千寻影视', '200164', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('205', 'KuaiyanKanShuSpider', 'kuaiyanKanShuSpider', '200165', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('206', 'Mail.RU_Bot_Robot', 'Mail.RU_Bot_Robot', '200166', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('207', 'MpCommunication', 'MpCommunication', '200167', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('208', 'Weather forecast', '天气预报', '200168', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('209', 'Ltx71_spider', 'ltx71_spider', '200169', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('210', 'Ia_archiver', 'ia_archiver', '200170', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('211', 'Ali Wangwang', '阿里旺旺', '200171', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('212', 'UC home page (information)', 'UC首页(资讯)', '200172', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('213', 'Yeti', 'Yeti', '200173', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('214', 'Rtorrent', 'rtorrent', '200174', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('215', 'QQPim', 'QQ同步助手', '200175', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('216', 'NetEase News', '网易新闻', '200176', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('217', 'sina sports', '新浪体育', '200177', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('218', 'Exchange of foreign exchange', '和讯外汇', '200178', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('219', 'BlueStacks update', 'BlueStacks更新', '200179', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('220', 'youdao', '有道云笔记', '200180', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('221', 'QQ computer housekeeper', 'QQ电脑管家', '200181', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('222', 'UltraEdit', 'UltraEdit', '200182', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('223', 'EasouSpider', 'EasouSpider', '200183', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('224', 'QQ Zone', 'QQ空间', '200184', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('225', 'Foxmail', 'foxmail', '200185', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('226', 'Pingdom.com', 'Pingdom.com', '200186', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('227', 'Sina Finance', '新浪财经', '200187', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('228', 'QQ one key login', 'QQ一键登录', '200188', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('229', 'Norton', '诺顿', '200189', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('230', 'Baidu waimai', '百度外卖', '200190', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('231', 'YaCy', 'yacy', '200191', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('232', 'Haitong e Haitong', '海通e海通财', '200192', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('233', 'AdsBot-Google', 'AdsBot-Google', '200193', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('234', 'Guangfa Yijin', '广发易淘金', '200194', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('235', 'Ali security program', '阿里安全程序', '200195', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('236', 'Cliqzbot', 'Cliqzbot', '200196', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('237', 'Mobike', '摩拜单车', '200197', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('238', 'Kingsoft PowerWord', '金山词霸', '200198', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('239', 'meishij', '美食杰', '200199', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('240', 'CCB mobile bank', '建行手机银行', '200200', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('241', 'Mega SkyDrive', 'Mega网盘', '200201', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('242', 'douyu', '斗鱼', '200202', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('243', 'Flamingo_SearchEngine', 'Flamingo_SearchEngine', '200203', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('244', 'Lenovo shop', '联想乐商店', '200204', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('245', 'Uc news', 'Uc新闻', '200205', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('246', 'Volcano small video', '火山小视频', '200206', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('247', 'Meitu Xiu Xiu', '美图秀秀', '200207', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('248', 'Kaspersky', 'Kaspersky', '200208', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('249', 'AcFun', 'AcFun', '200209', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('250', 'Slackbot', 'Slackbot', '200210', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('251', 'Snowball', '雪球', '200211', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('252', 'SafeDNSBot', 'SafeDNSBot', '200212', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('253', 'Great wisdom', '大智慧', '200213', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('254', 'Mailbox of NetEase', '网易邮箱大师', '200214', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('255', 'peasecod', '豌豆荚', '200215', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('256', 'Gold ten data', '金十数据', '200216', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('257', 'Linkdexbot', 'linkdexbot', '200217', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('258', 'Sina blog', '新浪博客', '200218', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('259', 'Ginger', 'Ginger', '200219', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('260', 'Microsoft activation verification', '微软激活验证', '200220', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('261', 'Youdao Dictionary', '有道词典', '200221', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('262', 'Facebot', 'Facebot', '200222', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('263', 'Dangdang', '当当', '200223', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('264', 'QQ cyclone', 'QQ旋风', '200224', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('265', 'Wp_feedbot', 'wp_feedbot', '200225', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('266', 'ICBC', '中国工商银行', '200226', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('267', 'Public service', '有道公有服务', '200227', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('268', 'Magpie-crawler', 'magpie-crawler', '200228', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('269', 'xianyu', '闲鱼', '200229', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('270', 'GetIntentBot', 'GetIntentBot', '200230', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('271', 'Papers', 'Papers', '200231', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('272', 'NewsGator', 'NewsGator', '200232', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('273', 'RankFlexBot', 'RankFlexBot', '200233', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('274', 'rogerbot', 'rogerbot', '200234', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('275', 'SurdotlyBot', 'SurdotlyBot', '200235', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('276', 'CheckMarkNetworkBot', 'CheckMarkNetworkBot', '200236', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('277', 'pyspider', 'pyspider', '200237', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('278', 'spbot', 'spbot', '200238', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('279', 'Huaxia Bank', '华夏银行', '200239', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('280', 'glory of king', '王者荣耀', '200240', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('281', 'msnbot-Products', 'msnbot-Products', '200241', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('282', 'archive.org', 'archive.org', '200242', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('283', 'applebot', 'applebot', '200243', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('284', 'alicall', '阿里通电话', '200244', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('285', 'Superfeedrbot', 'Superfeedrbot', '200245', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('286', 'QQ mailbox', 'QQ邮箱', '200246', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('287', 'coccocbot-webBot', 'coccocbot-webBot', '200247', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('288', 'dingtalk', '钉钉', '200248', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('289', 'Dragonbot', 'Dragonbot', '200249', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('290', 'Mediatoolkitbot', 'Mediatoolkitbot', '200250', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('291', 'bitcomet', '', '200251', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('292', 'filetopia', '', '200252', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('293', 'flashget', '', '200253', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('294', 'oracle', '', '200254', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('295', 'yyvoice', '', '200255', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('296', 'imesh', '', '200256', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('297', 'citrix', '', '200257', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('298', 'corba', '', '200258', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('299', 'dameware', '', '200259', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('300', 'pcanywhere', '', '200260', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('301', 'qq_rdp', '', '200261', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('302', 'poco', '', '200262', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('303', 'qqdownload', '', '200263', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('304', 'applejuice', '', '200264', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('305', 'stealthnet', '', '200265', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('306', 'winmx', '', '200266', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('307', 'emule', '', '200267', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('308', 'emule_decrypt', '', '200268', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('309', 'kuwo', '', '200269', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('310', 'qvod', '', '200270', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('311', 'mysql', '', '200271', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('312', 'icecast', '', '200272', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('313', 'mms', '', '200273', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('314', 'pplive', '', '200274', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('315', 'ppstream', '', '200275', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('316', 'sopcast', '', '200276', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('317', 'tvants', '', '200277', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('318', 'sohutv', '', '200278', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('319', 'iku', '', '200279', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('320', 'qqlive', '', '200280', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('321', 'rsync', '', '200281', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('322', 'smb', '', '200282', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('323', 'xunyou_speedup', '', '200283', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('324', 'msn', '', '200284', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('325', 'popo', '', '200285', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('326', 'sinauc', '', '200286', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('327', 'qqbattle', '', '200287', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('328', 'quake', '', '200288', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('329', 'xbox', '', '200289', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('330', 'wow', '', '200290', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('331', 'lol', '', '200291', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('332', 'PostgreSQL', '', '200292', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('333', 'TDS', '', '200293', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('334', 'VMware', '', '200294', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('335', 'Kontiki', '', '200295', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('336', 'OpenFT', '', '200296', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('337', 'Gnutella', '', '200297', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('338', 'eDonkey', '', '200298', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('339', 'AVI', '', '200299', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('340', 'RealMedia', '', '200300', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('341', 'Windowsmedia', '', '200301', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('342', 'MOVE', '', '200302', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('343', 'Feidian', '', '200303', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('344', 'Zattoo', '', '200304', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('345', 'SHOUTCast', '', '200305', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('346', 'TVUplayer', '', '200306', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('347', 'VeohTV', '', '200307', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('348', 'Soulseek', '', '200308', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('349', 'GaduGadu', '', '200309', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('350', 'Oscar', '', '200310', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('351', 'Yahoo', '', '200311', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('352', 'Battlefield', '', '200312', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('353', 'Halflife2', '', '200313', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('354', 'World of Warcraft', '', '200314', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('355', 'USENET', '', '200315', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('356', 'AFP', '', '200316', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('357', 'Truphone', '', '200317', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('358', 'Armagetron', '', '200318', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('359', 'CrossFire', '', '200319', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('360', 'Dofus', '', '200320', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('361', 'Fiesta', '', '200321', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('362', 'Florensia', '', '200322', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('363', 'Guildwars', '', '200323', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('364', 'HTTP Application Activesync', '', '200324', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('365', 'MapleStory', '', '200325', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('366', 'msSQL', '', '200326', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('367', 'WARCRAFT3', '', '200327', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('368', 'World of Kung Fu', '', '200328', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('369', 'MEEBO', '', '200329', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('370', 'DropBox', '', '200330', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('371', 'HTTP Proxy', '', '200331', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('372', 'Netflix', '', '200332', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('373', 'CitrixOnline/GotoMeeting', '', '200333', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('374', 'Webex', '', '200334', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('375', 'Apple iCloud', '', '200335', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('376', 'Apple iTunes', '', '200336', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('377', 'WindowsUpdate', '', '200337', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('378', 'Tuenti', '', '200338', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('379', 'LotusNotes', '', '200339', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('380', 'RemoteScan', '', '200340', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('381', 'Spotify', '', '200341', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('382', 'CiscoVPN', '', '200342', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('383', 'TeamSpeak', '', '200343', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('384', 'UbuntuONE', '', '200344', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('385', 'CNN', '', '200345', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('386', 'Wikipedia', '', '200346', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('387', 'Collectd', '', '200347', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('388', 'Redis', '', '200348', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('389', 'ZeroMQ', '', '200349', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('390', 'Stracraft', '', '200350', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('391', 'Simet', '', '200351', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('392', 'OpenSignal', '', '200352', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('393', '99Taxi', '', '200353', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('394', 'GloboTV', '', '200354', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('395', 'Deezer', '', '200355', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('396', 'Microsoft cloud services', '', '200356', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('397', 'Twitch', '', '200357', '', '', '0');
INSERT INTO `code_app_dic` VALUES ('398', 'KakaoTalk Voice and Chat', '', '200358', '', '', '0');

-- ----------------------------
-- Table structure for `code_behavior_type_dic`
-- ----------------------------
DROP TABLE IF EXISTS `code_behavior_type_dic`;
CREATE TABLE `code_behavior_type_dic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `behavior_type` varchar(255) DEFAULT NULL,
  `view_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of code_behavior_type_dic
-- ----------------------------
INSERT INTO `code_behavior_type_dic` VALUES ('1', 'voice', '1');
INSERT INTO `code_behavior_type_dic` VALUES ('2', 'video', '2');
INSERT INTO `code_behavior_type_dic` VALUES ('3', 'calls', '3');
INSERT INTO `code_behavior_type_dic` VALUES ('4', 'transfer', '4');
INSERT INTO `code_behavior_type_dic` VALUES ('5', 'message', '5');
INSERT INTO `code_behavior_type_dic` VALUES ('6', 'live', '6');
INSERT INTO `code_behavior_type_dic` VALUES ('7', 'upload', '7');
INSERT INTO `code_behavior_type_dic` VALUES ('8', 'download', '8');
INSERT INTO `code_behavior_type_dic` VALUES ('9', 'backpass', '255');
INSERT INTO `code_behavior_type_dic` VALUES ('10', 'unspecified', '268435455');

-- ----------------------------
-- Table structure for `code_browser_type_dic`
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
INSERT INTO `code_browser_type_dic` VALUES ('1', 'Unkonw', '0', '');
INSERT INTO `code_browser_type_dic` VALUES ('2', 'Google Chrome', '1', '');
INSERT INTO `code_browser_type_dic` VALUES ('3', 'Mozilla Firefox', '101', '');
INSERT INTO `code_browser_type_dic` VALUES ('4', 'Safari', '201', '');
INSERT INTO `code_browser_type_dic` VALUES ('5', 'Opera', '301', '');
INSERT INTO `code_browser_type_dic` VALUES ('6', 'Yandex', '401', '');
INSERT INTO `code_browser_type_dic` VALUES ('7', 'MSIE', '501', '');
INSERT INTO `code_browser_type_dic` VALUES ('8', 'Android brower', '601', '');
INSERT INTO `code_browser_type_dic` VALUES ('9', 'Konqueror', '701', '');
INSERT INTO `code_browser_type_dic` VALUES ('10', 'Bing', '801', '');
INSERT INTO `code_browser_type_dic` VALUES ('11', 'Unspecified', '268435455', '');
INSERT INTO `code_browser_type_dic` VALUES ('14', 'Others', '-1', null);
INSERT INTO `code_browser_type_dic` VALUES ('15', 'SogouMobileBrowser', '901', null);
INSERT INTO `code_browser_type_dic` VALUES ('16', 'SogouBrowser', '1001', null);
INSERT INTO `code_browser_type_dic` VALUES ('17', 'OPPO', '1101', null);
INSERT INTO `code_browser_type_dic` VALUES ('18', 'UCBrowser', '1201', null);
INSERT INTO `code_browser_type_dic` VALUES ('19', '2345Browser', '1301', null);
INSERT INTO `code_browser_type_dic` VALUES ('20', 'QQMobileBrowser', '1401', null);
INSERT INTO `code_browser_type_dic` VALUES ('21', 'QQBrowser', '1501', null);
INSERT INTO `code_browser_type_dic` VALUES ('22', 'baidubrowser', '1601', null);
INSERT INTO `code_browser_type_dic` VALUES ('23', 'LieBao', '1701', null);
INSERT INTO `code_browser_type_dic` VALUES ('24', 'MiuiBrowser', '1801', null);
INSERT INTO `code_browser_type_dic` VALUES ('25', 'SamsungBrowser', '1901', null);
INSERT INTO `code_browser_type_dic` VALUES ('26', 'IE', '2001', null);
INSERT INTO `code_browser_type_dic` VALUES ('27', 'GriOS', '2101', null);
INSERT INTO `code_browser_type_dic` VALUES ('28', '360SE', '2201', null);
INSERT INTO `code_browser_type_dic` VALUES ('29', 'Maxthon', '2301', null);
INSERT INTO `code_browser_type_dic` VALUES ('30', 'The World', '2401', null);
INSERT INTO `code_browser_type_dic` VALUES ('31', 'JuziBrowser', '2501', null);
INSERT INTO `code_browser_type_dic` VALUES ('32', 'TencentTraveler', '2601', null);

-- ----------------------------
-- Table structure for `code_os_type_dic`
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
INSERT INTO `code_os_type_dic` VALUES ('2', 'Windows', '1', '');
INSERT INTO `code_os_type_dic` VALUES ('3', 'Linux', '101', '');
INSERT INTO `code_os_type_dic` VALUES ('4', 'Mac OS X', '201', '');
INSERT INTO `code_os_type_dic` VALUES ('5', 'OpenBSD', '301', '');
INSERT INTO `code_os_type_dic` VALUES ('6', 'FreeBSD', '401', '');
INSERT INTO `code_os_type_dic` VALUES ('7', 'Solaris', '501', '');
INSERT INTO `code_os_type_dic` VALUES ('8', 'OpenVMS', '601', '');
INSERT INTO `code_os_type_dic` VALUES ('9', 'NeXTSTEP', '701', '');
INSERT INTO `code_os_type_dic` VALUES ('10', 'HP-UX', '801', '');
INSERT INTO `code_os_type_dic` VALUES ('11', 'Tru64', '901', '');
INSERT INTO `code_os_type_dic` VALUES ('12', 'iOS ', '1001', '');
INSERT INTO `code_os_type_dic` VALUES ('14', 'Unspecified', '268435455', '');
INSERT INTO `code_os_type_dic` VALUES ('15', 'Others', '-1', null);
INSERT INTO `code_os_type_dic` VALUES ('16', 'Android\r\n', '1301\r\n', null);
INSERT INTO `code_os_type_dic` VALUES ('17', 'Windows Phone', '1201', null);
INSERT INTO `code_os_type_dic` VALUES ('18', 'Symbian\r\n', '1401\r\n', null);

-- ----------------------------
-- Table structure for `code_protocol_type_dic`
-- ----------------------------
DROP TABLE IF EXISTS `code_protocol_type_dic`;
CREATE TABLE `code_protocol_type_dic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `protocol_type` varchar(255) DEFAULT '',
  `view_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=95 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of code_protocol_type_dic
-- ----------------------------
INSERT INTO `code_protocol_type_dic` VALUES ('1', 'Unknow', '0');
INSERT INTO `code_protocol_type_dic` VALUES ('2', 'TCP', '1');
INSERT INTO `code_protocol_type_dic` VALUES ('3', 'UDP', '2');
INSERT INTO `code_protocol_type_dic` VALUES ('4', 'DNS', '3');
INSERT INTO `code_protocol_type_dic` VALUES ('5', 'FTP', '4');
INSERT INTO `code_protocol_type_dic` VALUES ('6', 'FTPS', '5');
INSERT INTO `code_protocol_type_dic` VALUES ('7', 'GRE', '6');
INSERT INTO `code_protocol_type_dic` VALUES ('8', 'HTTP', '7');
INSERT INTO `code_protocol_type_dic` VALUES ('9', 'HTTPS', '8');
INSERT INTO `code_protocol_type_dic` VALUES ('10', 'ICMP', '9');
INSERT INTO `code_protocol_type_dic` VALUES ('11', 'IKE', '10');
INSERT INTO `code_protocol_type_dic` VALUES ('12', 'IMAP', '11');
INSERT INTO `code_protocol_type_dic` VALUES ('13', 'IMAPS', '12');
INSERT INTO `code_protocol_type_dic` VALUES ('14', 'IPSEC', '13');
INSERT INTO `code_protocol_type_dic` VALUES ('15', 'XMPP', '14');
INSERT INTO `code_protocol_type_dic` VALUES ('16', 'L2TP', '15');
INSERT INTO `code_protocol_type_dic` VALUES ('17', 'NTP', '16');
INSERT INTO `code_protocol_type_dic` VALUES ('18', 'POP3', '17');
INSERT INTO `code_protocol_type_dic` VALUES ('19', 'POP3 SSL', '18');
INSERT INTO `code_protocol_type_dic` VALUES ('20', 'PPTP', '19');
INSERT INTO `code_protocol_type_dic` VALUES ('21', 'Quic', '20');
INSERT INTO `code_protocol_type_dic` VALUES ('22', 'SIP', '21');
INSERT INTO `code_protocol_type_dic` VALUES ('23', 'SMP', '22');
INSERT INTO `code_protocol_type_dic` VALUES ('24', 'SMTP', '23');
INSERT INTO `code_protocol_type_dic` VALUES ('25', 'SMTPS', '24');
INSERT INTO `code_protocol_type_dic` VALUES ('26', 'SPDY', '25');
INSERT INTO `code_protocol_type_dic` VALUES ('27', 'SSH-V1', '26');
INSERT INTO `code_protocol_type_dic` VALUES ('28', 'SSH-V2', '27');
INSERT INTO `code_protocol_type_dic` VALUES ('29', 'SSH-other', '28');
INSERT INTO `code_protocol_type_dic` VALUES ('30', 'SSL', '29');
INSERT INTO `code_protocol_type_dic` VALUES ('31', 'SOCKS', '30');
INSERT INTO `code_protocol_type_dic` VALUES ('32', 'BITTORRENT', '31');
INSERT INTO `code_protocol_type_dic` VALUES ('33', 'RTSP', '32');
INSERT INTO `code_protocol_type_dic` VALUES ('34', 'TELNET', '33');
INSERT INTO `code_protocol_type_dic` VALUES ('35', 'UTP', '34');
INSERT INTO `code_protocol_type_dic` VALUES ('36', 'FASTTRACK', '35');
INSERT INTO `code_protocol_type_dic` VALUES ('37', 'SKINNY', '36');
INSERT INTO `code_protocol_type_dic` VALUES ('38', 'IAX', '37');
INSERT INTO `code_protocol_type_dic` VALUES ('39', 'MGCP', '38');
INSERT INTO `code_protocol_type_dic` VALUES ('40', 'RTCP', '39');
INSERT INTO `code_protocol_type_dic` VALUES ('41', 'RTP', '40');
INSERT INTO `code_protocol_type_dic` VALUES ('42', 'VNC', '41');
INSERT INTO `code_protocol_type_dic` VALUES ('43', 'XDMCP', '42');
INSERT INTO `code_protocol_type_dic` VALUES ('44', 'RDP', '43');
INSERT INTO `code_protocol_type_dic` VALUES ('45', 'AIMINI', '44');
INSERT INTO `code_protocol_type_dic` VALUES ('46', 'IPP', '45');
INSERT INTO `code_protocol_type_dic` VALUES ('47', 'NFS', '46');
INSERT INTO `code_protocol_type_dic` VALUES ('48', 'TFTP', '47');
INSERT INTO `code_protocol_type_dic` VALUES ('49', 'BGP', '48');
INSERT INTO `code_protocol_type_dic` VALUES ('50', 'DHCP', '49');
INSERT INTO `code_protocol_type_dic` VALUES ('51', 'DHCPV6', '50');
INSERT INTO `code_protocol_type_dic` VALUES ('52', 'FINGER', '51');
INSERT INTO `code_protocol_type_dic` VALUES ('53', 'KERBEROS', '52');
INSERT INTO `code_protocol_type_dic` VALUES ('54', 'LDAP', '53');
INSERT INTO `code_protocol_type_dic` VALUES ('55', 'MDNS', '54');
INSERT INTO `code_protocol_type_dic` VALUES ('56', 'NNTP', '55');
INSERT INTO `code_protocol_type_dic` VALUES ('57', 'SNMP', '56');
INSERT INTO `code_protocol_type_dic` VALUES ('58', 'SSDP', '57');
INSERT INTO `code_protocol_type_dic` VALUES ('59', 'SYSLOG', '58');
INSERT INTO `code_protocol_type_dic` VALUES ('60', 'WHOIS', '59');
INSERT INTO `code_protocol_type_dic` VALUES ('61', 'RADIUS', '60');
INSERT INTO `code_protocol_type_dic` VALUES ('62', 'OPENVPN', '61');
INSERT INTO `code_protocol_type_dic` VALUES ('63', 'STUN', '62');
INSERT INTO `code_protocol_type_dic` VALUES ('64', 'TEREDO', '63');
INSERT INTO `code_protocol_type_dic` VALUES ('65', 'GTP', '64');
INSERT INTO `code_protocol_type_dic` VALUES ('66', 'RTMP', '65');
INSERT INTO `code_protocol_type_dic` VALUES ('67', 'chargen_tcp_udp', '66');
INSERT INTO `code_protocol_type_dic` VALUES ('68', 'llmnr_udp', '67');
INSERT INTO `code_protocol_type_dic` VALUES ('69', 'nbns_udp', '68');
INSERT INTO `code_protocol_type_dic` VALUES ('70', 'NETBIOS', '69');
INSERT INTO `code_protocol_type_dic` VALUES ('71', 'DirectDownloadLink', '70');
INSERT INTO `code_protocol_type_dic` VALUES ('72', 'I23V5', '71');
INSERT INTO `code_protocol_type_dic` VALUES ('73', 'DirectConnect', '72');
INSERT INTO `code_protocol_type_dic` VALUES ('74', 'OFF', '73');
INSERT INTO `code_protocol_type_dic` VALUES ('75', 'OGG', '74');
INSERT INTO `code_protocol_type_dic` VALUES ('76', 'MPEG', '75');
INSERT INTO `code_protocol_type_dic` VALUES ('77', 'IRC', '76');
INSERT INTO `code_protocol_type_dic` VALUES ('78', 'Jabber', '77');
INSERT INTO `code_protocol_type_dic` VALUES ('79', 'VRRP', '78');
INSERT INTO `code_protocol_type_dic` VALUES ('80', 'IGMP', '79');
INSERT INTO `code_protocol_type_dic` VALUES ('81', 'EGP', '80');
INSERT INTO `code_protocol_type_dic` VALUES ('82', 'SCTP', '81');
INSERT INTO `code_protocol_type_dic` VALUES ('83', 'OSPF', '82');
INSERT INTO `code_protocol_type_dic` VALUES ('84', 'IP in IP', '83');
INSERT INTO `code_protocol_type_dic` VALUES ('85', 'ICMPv6', '84');
INSERT INTO `code_protocol_type_dic` VALUES ('86', 'DCE RPC', '85');
INSERT INTO `code_protocol_type_dic` VALUES ('87', 'NetFlow_IPFIX', '86');
INSERT INTO `code_protocol_type_dic` VALUES ('88', 'sFlow', '87');
INSERT INTO `code_protocol_type_dic` VALUES ('89', 'HTTP Connect (SSL over HTTP)', '88');
INSERT INTO `code_protocol_type_dic` VALUES ('90', 'SAP', '89');
INSERT INTO `code_protocol_type_dic` VALUES ('91', 'UPnP', '90');
INSERT INTO `code_protocol_type_dic` VALUES ('92', 'H323', '91');
INSERT INTO `code_protocol_type_dic` VALUES ('93', 'Megaco', '92');
INSERT INTO `code_protocol_type_dic` VALUES ('94', 'Unspecified', '268435455');

-- ----------------------------
-- Table structure for `code_service_type_dic`
-- ----------------------------
DROP TABLE IF EXISTS `code_service_type_dic`;
CREATE TABLE `code_service_type_dic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_type` varchar(255) DEFAULT '' COMMENT '业务类型',
  `view_code` varchar(255) DEFAULT NULL COMMENT '界面编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of code_service_type_dic
-- ----------------------------
INSERT INTO `code_service_type_dic` VALUES ('1', 'Finance', '1');
INSERT INTO `code_service_type_dic` VALUES ('2', 'Chat', '2');
INSERT INTO `code_service_type_dic` VALUES ('3', 'Cloud', '3');
INSERT INTO `code_service_type_dic` VALUES ('4', 'Web', '4');
INSERT INTO `code_service_type_dic` VALUES ('5', 'DataTransfer', '5');
INSERT INTO `code_service_type_dic` VALUES ('6', 'Database', '6');
INSERT INTO `code_service_type_dic` VALUES ('7', 'Download-FileTransfer-FileSharing', '7');
INSERT INTO `code_service_type_dic` VALUES ('8', 'Email', '8');
INSERT INTO `code_service_type_dic` VALUES ('9', 'Game', '9');
INSERT INTO `code_service_type_dic` VALUES ('10', 'Media', '10');
INSERT INTO `code_service_type_dic` VALUES ('11', 'Network', '11');
INSERT INTO `code_service_type_dic` VALUES ('12', 'RPC', '12');
INSERT INTO `code_service_type_dic` VALUES ('13', 'RemoteAccess', '13');
INSERT INTO `code_service_type_dic` VALUES ('14', 'SocilNetwork', '14');
INSERT INTO `code_service_type_dic` VALUES ('15', 'SoftwareUpdate', '15');
INSERT INTO `code_service_type_dic` VALUES ('16', 'Streaming', '16');
INSERT INTO `code_service_type_dic` VALUES ('17', 'Browser', '17');
INSERT INTO `code_service_type_dic` VALUES ('18', 'System', '18');
INSERT INTO `code_service_type_dic` VALUES ('19', 'Collaborative', '19');
INSERT INTO `code_service_type_dic` VALUES ('20', 'VPN', '20');
INSERT INTO `code_service_type_dic` VALUES ('21', 'VoIP', '21');
INSERT INTO `code_service_type_dic` VALUES ('22', 'EncryptedTraffic', '22');
INSERT INTO `code_service_type_dic` VALUES ('23', 'Unspecified', '268435455');

-- ----------------------------
-- Table structure for `code_web_service_dic`
-- ----------------------------
DROP TABLE IF EXISTS `code_web_service_dic`;
CREATE TABLE `code_web_service_dic` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `website` varchar(255) DEFAULT '',
  `domain_name` varchar(255) DEFAULT '' COMMENT '域名',
  `view_code` varchar(255) DEFAULT '',
  `icon` varchar(255) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=244 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of code_web_service_dic
-- ----------------------------
INSERT INTO `code_web_service_dic` VALUES ('1', 'Unknow', 'Unknow', '0', '');
INSERT INTO `code_web_service_dic` VALUES ('2', 'Google', 'Google.com', '1', '');
INSERT INTO `code_web_service_dic` VALUES ('3', 'Youtube', 'Youtube.com', '2', '');
INSERT INTO `code_web_service_dic` VALUES ('4', 'Facebook', 'Facebook.com', '3', '');
INSERT INTO `code_web_service_dic` VALUES ('5', 'Baidu', 'Baidu.com', '4', '');
INSERT INTO `code_web_service_dic` VALUES ('6', 'Wikipedia', 'Wikipedia.org', '5', '');
INSERT INTO `code_web_service_dic` VALUES ('7', 'Reddit', 'Reddit.com', '6', '');
INSERT INTO `code_web_service_dic` VALUES ('8', 'Yahoo', 'Yahoo.com', '7', '');
INSERT INTO `code_web_service_dic` VALUES ('9', 'Qq', 'Qq.com', '8', '');
INSERT INTO `code_web_service_dic` VALUES ('10', 'Taobao', 'Taobao.com', '9', '');
INSERT INTO `code_web_service_dic` VALUES ('11', 'Amazon', 'Amazon.com', '10', '');
INSERT INTO `code_web_service_dic` VALUES ('12', 'Tmall', 'Tmall.com', '11', '');
INSERT INTO `code_web_service_dic` VALUES ('13', 'Twitter', 'Twitter.com', '12', '');
INSERT INTO `code_web_service_dic` VALUES ('14', 'Sohu', 'Sohu.com', '13', '');
INSERT INTO `code_web_service_dic` VALUES ('15', 'Instagram', 'Instagram.com', '14', '');
INSERT INTO `code_web_service_dic` VALUES ('16', 'Vk', 'Vk.com', '15', '');
INSERT INTO `code_web_service_dic` VALUES ('17', 'Live', 'Live.com', '16', '');
INSERT INTO `code_web_service_dic` VALUES ('18', 'Jd', 'Jd.com', '17', '');
INSERT INTO `code_web_service_dic` VALUES ('19', 'Sina', 'Sina.com.cn', '18', '');
INSERT INTO `code_web_service_dic` VALUES ('20', 'Weibo', 'Weibo.com', '19', '');
INSERT INTO `code_web_service_dic` VALUES ('21', 'Yandex', 'Yandex.ru', '20', '');
INSERT INTO `code_web_service_dic` VALUES ('22', '360', '360.cn', '21', '');
INSERT INTO `code_web_service_dic` VALUES ('23', 'Login', 'Login.tmall.com', '22', '');
INSERT INTO `code_web_service_dic` VALUES ('24', 'Netflix', 'Netflix.com', '23', '');
INSERT INTO `code_web_service_dic` VALUES ('25', 'Pornhub', 'Pornhub.com', '24', '');
INSERT INTO `code_web_service_dic` VALUES ('26', 'Twitch', 'Twitch.tv', '25', '');
INSERT INTO `code_web_service_dic` VALUES ('27', 'Linkedin', 'Linkedin.com', '26', '');
INSERT INTO `code_web_service_dic` VALUES ('28', 'Csdn', 'Csdn.net', '27', '');
INSERT INTO `code_web_service_dic` VALUES ('29', 'T', 'T.co', '28', '');
INSERT INTO `code_web_service_dic` VALUES ('30', 'Microsoft', 'Microsoft.com', '29', '');
INSERT INTO `code_web_service_dic` VALUES ('31', 'Office', 'Office.com', '30', '');
INSERT INTO `code_web_service_dic` VALUES ('32', 'Bing', 'Bing.com', '31', '');
INSERT INTO `code_web_service_dic` VALUES ('33', 'Ebay', 'Ebay.com', '32', '');
INSERT INTO `code_web_service_dic` VALUES ('34', 'Alipay', 'Alipay.com', '33', '');
INSERT INTO `code_web_service_dic` VALUES ('35', 'Xvideos', 'Xvideos.com', '34', '');
INSERT INTO `code_web_service_dic` VALUES ('36', 'Mail', 'Mail.ru', '35', '');
INSERT INTO `code_web_service_dic` VALUES ('37', 'Ok', 'Ok.ru', '36', '');
INSERT INTO `code_web_service_dic` VALUES ('38', 'Msn', 'Msn.com', '37', '');
INSERT INTO `code_web_service_dic` VALUES ('39', 'Microsoftonline', 'Microsoftonline.com', '38', '');
INSERT INTO `code_web_service_dic` VALUES ('40', 'Pages', 'Pages.tmall.com', '39', '');
INSERT INTO `code_web_service_dic` VALUES ('41', 'Imgur', 'Imgur.com', '40', '');
INSERT INTO `code_web_service_dic` VALUES ('42', 'Aliexpress', 'Aliexpress.com', '41', '');
INSERT INTO `code_web_service_dic` VALUES ('43', 'Tumblr', 'Tumblr.com', '42', '');
INSERT INTO `code_web_service_dic` VALUES ('44', 'Wordpress', 'Wordpress.com', '43', '');
INSERT INTO `code_web_service_dic` VALUES ('45', 'Wikia', 'Wikia.com', '44', '');
INSERT INTO `code_web_service_dic` VALUES ('46', 'Imdb', 'Imdb.com', '45', '');
INSERT INTO `code_web_service_dic` VALUES ('47', 'Whatsapp', 'Whatsapp.com', '46', '');
INSERT INTO `code_web_service_dic` VALUES ('48', 'Stackoverflow', 'Stackoverflow.com', '47', '');
INSERT INTO `code_web_service_dic` VALUES ('49', 'Github', 'Github.com', '48', '');
INSERT INTO `code_web_service_dic` VALUES ('50', 'Livejasmin', 'Livejasmin.com', '49', '');
INSERT INTO `code_web_service_dic` VALUES ('51', 'Xhamster', 'Xhamster.com', '50', '');
INSERT INTO `code_web_service_dic` VALUES ('52', 'Hao123', 'Hao123.com', '51', '');
INSERT INTO `code_web_service_dic` VALUES ('53', 'Blogspot', 'Blogspot.com', '52', '');
INSERT INTO `code_web_service_dic` VALUES ('54', 'Deloton', 'Deloton.com', '53', '');
INSERT INTO `code_web_service_dic` VALUES ('55', 'Paypal', 'Paypal.com', '54', '');
INSERT INTO `code_web_service_dic` VALUES ('56', 'Tribunnews', 'Tribunnews.com', '55', '');
INSERT INTO `code_web_service_dic` VALUES ('57', 'Popads', 'Popads.net', '56', '');
INSERT INTO `code_web_service_dic` VALUES ('58', 'Apple', 'Apple.com', '57', '');
INSERT INTO `code_web_service_dic` VALUES ('59', 'Diply', 'Diply.com', '58', '');
INSERT INTO `code_web_service_dic` VALUES ('60', 'Bongacams', 'Bongacams.com', '59', '');
INSERT INTO `code_web_service_dic` VALUES ('61', 'Googleusercontent', 'Googleusercontent.com', '60', '');
INSERT INTO `code_web_service_dic` VALUES ('62', 'Pinterest', 'Pinterest.com', '61', '');
INSERT INTO `code_web_service_dic` VALUES ('63', 'Xnxx', 'Xnxx.com', '62', '');
INSERT INTO `code_web_service_dic` VALUES ('64', 'Adobe', 'Adobe.com', '63', '');
INSERT INTO `code_web_service_dic` VALUES ('65', 'Providr', 'Providr.com', '64', '');
INSERT INTO `code_web_service_dic` VALUES ('66', 'Fbcdn', 'Fbcdn.net', '65', '');
INSERT INTO `code_web_service_dic` VALUES ('67', 'Coccoc', 'Coccoc.com', '66', '');
INSERT INTO `code_web_service_dic` VALUES ('68', 'Txxx', 'Txxx.com', '67', '');
INSERT INTO `code_web_service_dic` VALUES ('69', 'Dropbox', 'Dropbox.com', '68', '');
INSERT INTO `code_web_service_dic` VALUES ('70', 'Detail', 'Detail.tmall.com', '69', '');
INSERT INTO `code_web_service_dic` VALUES ('71', 'Savefrom', 'Savefrom.net', '70', '');
INSERT INTO `code_web_service_dic` VALUES ('72', 'Pixnet', 'Pixnet.net', '71', '');
INSERT INTO `code_web_service_dic` VALUES ('73', 'Bbc', 'Bbc.com', '72', '');
INSERT INTO `code_web_service_dic` VALUES ('74', 'Thestartmagazine', 'Thestartmagazine.com', '73', '');
INSERT INTO `code_web_service_dic` VALUES ('75', 'Quora', 'Quora.com', '74', '');
INSERT INTO `code_web_service_dic` VALUES ('76', 'Tianya', 'Tianya.cn', '75', '');
INSERT INTO `code_web_service_dic` VALUES ('77', 'Soso', 'Soso.com', '76', '');
INSERT INTO `code_web_service_dic` VALUES ('78', 'Chaturbate', 'Chaturbate.com', '77', '');
INSERT INTO `code_web_service_dic` VALUES ('79', 'Amazonaws', 'Amazonaws.com', '78', '');
INSERT INTO `code_web_service_dic` VALUES ('80', 'Booking', 'Booking.com', '79', '');
INSERT INTO `code_web_service_dic` VALUES ('81', 'Espn', 'Espn.com', '80', '');
INSERT INTO `code_web_service_dic` VALUES ('82', 'So', 'So.com', '81', '');
INSERT INTO `code_web_service_dic` VALUES ('83', 'Cnn', 'Cnn.com', '82', '');
INSERT INTO `code_web_service_dic` VALUES ('84', 'Craigslist', 'Craigslist.org', '83', '');
INSERT INTO `code_web_service_dic` VALUES ('85', 'Nicovideo', 'Nicovideo.jp', '84', '');
INSERT INTO `code_web_service_dic` VALUES ('86', 'Xinhuanet', 'Xinhuanet.com', '85', '');
INSERT INTO `code_web_service_dic` VALUES ('87', 'Soundcloud', 'Soundcloud.com', '86', '');
INSERT INTO `code_web_service_dic` VALUES ('88', 'Tokopedia', 'Tokopedia.com', '87', '');
INSERT INTO `code_web_service_dic` VALUES ('89', 'Dailymotion', 'Dailymotion.com', '88', '');
INSERT INTO `code_web_service_dic` VALUES ('90', 'Youth', 'Youth.cn', '89', '');
INSERT INTO `code_web_service_dic` VALUES ('91', 'Nytimes', 'Nytimes.com', '90', '');
INSERT INTO `code_web_service_dic` VALUES ('92', 'Detik', 'Detik.com', '91', '');
INSERT INTO `code_web_service_dic` VALUES ('93', 'Ask', 'Ask.com', '92', '');
INSERT INTO `code_web_service_dic` VALUES ('94', 'Naver', 'Naver.com', '93', '');
INSERT INTO `code_web_service_dic` VALUES ('95', 'Restorecosm', 'Restorecosm.bid', '94', '');
INSERT INTO `code_web_service_dic` VALUES ('96', 'Stackexchange', 'Stackexchange.com', '95', '');
INSERT INTO `code_web_service_dic` VALUES ('97', 'Dkn', 'Dkn.tv', '96', '');
INSERT INTO `code_web_service_dic` VALUES ('98', 'Thepiratebay', 'Thepiratebay.org', '97', '');
INSERT INTO `code_web_service_dic` VALUES ('99', 'Roblox', 'Roblox.com', '98', '');
INSERT INTO `code_web_service_dic` VALUES ('100', 'Rakuten', 'Rakuten.co.jp', '99', '');
INSERT INTO `code_web_service_dic` VALUES ('101', 'Onlinesbi', 'Onlinesbi.com', '100', '');
INSERT INTO `code_web_service_dic` VALUES ('102', 'Openload', 'Openload.co', '101', '');
INSERT INTO `code_web_service_dic` VALUES ('103', 'Spotify', 'Spotify.com', '102', '');
INSERT INTO `code_web_service_dic` VALUES ('104', 'Nih', 'Nih.gov', '103', '');
INSERT INTO `code_web_service_dic` VALUES ('105', 'Vimeo', 'Vimeo.com', '104', '');
INSERT INTO `code_web_service_dic` VALUES ('106', 'Fc2', 'Fc2.com', '105', '');
INSERT INTO `code_web_service_dic` VALUES ('107', 'Aparat', 'Aparat.com', '106', '');
INSERT INTO `code_web_service_dic` VALUES ('108', 'Ettoday', 'Ettoday.net', '107', '');
INSERT INTO `code_web_service_dic` VALUES ('109', 'Mozilla', 'Mozilla.org', '108', '');
INSERT INTO `code_web_service_dic` VALUES ('110', '1688', '1688.com', '109', '');
INSERT INTO `code_web_service_dic` VALUES ('111', 'K618', 'K618.cn', '110', '');
INSERT INTO `code_web_service_dic` VALUES ('112', 'Exdynsrv', 'Exdynsrv.com', '111', '');
INSERT INTO `code_web_service_dic` VALUES ('113', 'Oath', 'Oath.com', '112', '');
INSERT INTO `code_web_service_dic` VALUES ('114', 'Ntd', 'Ntd.tv', '113', '');
INSERT INTO `code_web_service_dic` VALUES ('115', 'Theguardian', 'Theguardian.com', '114', '');
INSERT INTO `code_web_service_dic` VALUES ('116', 'Mediafire', 'Mediafire.com', '115', '');
INSERT INTO `code_web_service_dic` VALUES ('117', 'Chase', 'Chase.com', '116', '');
INSERT INTO `code_web_service_dic` VALUES ('118', 'Flipkart', 'Flipkart.com', '117', '');
INSERT INTO `code_web_service_dic` VALUES ('119', 'Exosrv', 'Exosrv.com', '118', '恶意网站-无图标');
INSERT INTO `code_web_service_dic` VALUES ('120', 'Avito', 'Avito.ru', '119', '');
INSERT INTO `code_web_service_dic` VALUES ('121', 'Slideshare', 'Slideshare.net', '120', '');
INSERT INTO `code_web_service_dic` VALUES ('122', 'Dailymail', 'Dailymail.co.uk', '121', '');
INSERT INTO `code_web_service_dic` VALUES ('123', 'Discordapp', 'Discordapp.com', '122', '');
INSERT INTO `code_web_service_dic` VALUES ('124', 'Zhihu', 'Zhihu.com', '123', '');
INSERT INTO `code_web_service_dic` VALUES ('125', 'Salesforce', 'Salesforce.com', '124', '');
INSERT INTO `code_web_service_dic` VALUES ('126', 'Theepochtimes', 'Theepochtimes.com', '125', '');
INSERT INTO `code_web_service_dic` VALUES ('127', 'Softonic', 'Softonic.com', '126', '');
INSERT INTO `code_web_service_dic` VALUES ('128', 'Globo', 'Globo.com', '127', '');
INSERT INTO `code_web_service_dic` VALUES ('129', 'Bukalapak', 'Bukalapak.com', '128', '');
INSERT INTO `code_web_service_dic` VALUES ('130', 'Douyu', 'Douyu.com', '129', '');
INSERT INTO `code_web_service_dic` VALUES ('131', 'Nextoptim', 'Nextoptim.com', '130', '恶意网站-无图标');
INSERT INTO `code_web_service_dic` VALUES ('132', 'Deviantart', 'Deviantart.com', '131', '');
INSERT INTO `code_web_service_dic` VALUES ('133', 'Indeed', 'Indeed.com', '132', '');
INSERT INTO `code_web_service_dic` VALUES ('134', 'Lolsided', 'Lolsided.com', '133', '');
INSERT INTO `code_web_service_dic` VALUES ('135', 'Bles', 'Bles.com', '134', '');
INSERT INTO `code_web_service_dic` VALUES ('136', 'Cnet', 'Cnet.com', '135', '');
INSERT INTO `code_web_service_dic` VALUES ('137', 'Alibaba', 'Alibaba.com', '136', '');
INSERT INTO `code_web_service_dic` VALUES ('138', 'Bet9ja', 'Bet9ja.com', '137', '');
INSERT INTO `code_web_service_dic` VALUES ('139', 'Vice', 'Vice.com', '138', '');
INSERT INTO `code_web_service_dic` VALUES ('140', 'Lifedaily', 'Lifedaily.com', '139', '');
INSERT INTO `code_web_service_dic` VALUES ('141', 'Hotmovs', 'Hotmovs.com', '140', '');
INSERT INTO `code_web_service_dic` VALUES ('142', 'Sogou', 'Sogou.com', '141', '');
INSERT INTO `code_web_service_dic` VALUES ('143', 'Uol', 'Uol.com.br', '142', '');
INSERT INTO `code_web_service_dic` VALUES ('144', 'Doubleclick', 'Doubleclick.net', '143', '');
INSERT INTO `code_web_service_dic` VALUES ('145', 'Mercadolivre', 'Mercadolivre.com.br', '144', '');
INSERT INTO `code_web_service_dic` VALUES ('146', 'Speakol', 'Speakol.com', '145', '');
INSERT INTO `code_web_service_dic` VALUES ('147', 'Babytree', 'Babytree.com', '146', '');
INSERT INTO `code_web_service_dic` VALUES ('148', 'Liputan6', 'Liputan6.com', '147', '');
INSERT INTO `code_web_service_dic` VALUES ('149', 'Wikihow', 'Wikihow.com', '148', '');
INSERT INTO `code_web_service_dic` VALUES ('150', 'China', 'China.com.cn', '149', '');
INSERT INTO `code_web_service_dic` VALUES ('151', 'Etsy', 'Etsy.com', '150', '');
INSERT INTO `code_web_service_dic` VALUES ('152', 'W3schools', 'W3schools.com', '151', '');
INSERT INTO `code_web_service_dic` VALUES ('153', 'Youm7', 'Youm7.com', '152', '');
INSERT INTO `code_web_service_dic` VALUES ('154', 'Scribd', 'Scribd.com', '153', '');
INSERT INTO `code_web_service_dic` VALUES ('155', 'Rumble', 'Rumble.com', '154', '');
INSERT INTO `code_web_service_dic` VALUES ('156', 'Walmart', 'Walmart.com', '155', '');
INSERT INTO `code_web_service_dic` VALUES ('157', 'Twimg', 'Twimg.com', '156', '同twitter');
INSERT INTO `code_web_service_dic` VALUES ('158', 'Yelp', 'Yelp.com', '157', '');
INSERT INTO `code_web_service_dic` VALUES ('159', 'Kompas', 'Kompas.com', '158', '');
INSERT INTO `code_web_service_dic` VALUES ('160', 'Messenger', 'Messenger.com', '159', '');
INSERT INTO `code_web_service_dic` VALUES ('161', 'Force', 'Force.com', '160', '');
INSERT INTO `code_web_service_dic` VALUES ('162', 'Onlinevideoconverter', 'Onlinevideoconverter.com', '161', '');
INSERT INTO `code_web_service_dic` VALUES ('163', 'Redtube', 'Redtube.com', '162', '');
INSERT INTO `code_web_service_dic` VALUES ('164', 'Gmw', 'Gmw.cn', '163', '');
INSERT INTO `code_web_service_dic` VALUES ('165', 'Caijing', 'Caijing.com.cn', '164', '');
INSERT INTO `code_web_service_dic` VALUES ('166', 'Mama', 'Mama.cn', '165', '');
INSERT INTO `code_web_service_dic` VALUES ('167', 'Chinadaily', 'Chinadaily.com.cn', '166', '');
INSERT INTO `code_web_service_dic` VALUES ('168', 'Cricbuzz', 'Cricbuzz.com', '167', '');
INSERT INTO `code_web_service_dic` VALUES ('169', 'Ladbible', 'Ladbible.com', '168', '');
INSERT INTO `code_web_service_dic` VALUES ('170', 'Doublepimpssl', 'Doublepimpssl.com', '169', '');
INSERT INTO `code_web_service_dic` VALUES ('171', 'Steampowered', 'Steampowered.com', '170', '');
INSERT INTO `code_web_service_dic` VALUES ('172', 'Mega', 'Mega.nz', '171', '');
INSERT INTO `code_web_service_dic` VALUES ('173', 'Yts', 'Yts.am', '172', '');
INSERT INTO `code_web_service_dic` VALUES ('174', 'Youporn', 'Youporn.com', '173', '');
INSERT INTO `code_web_service_dic` VALUES ('175', 'Buzzfeed', 'Buzzfeed.com', '174', '');
INSERT INTO `code_web_service_dic` VALUES ('176', 'Indiatimes', 'Indiatimes.com', '175', '');
INSERT INTO `code_web_service_dic` VALUES ('177', 'Hotstar', 'Hotstar.com', '176', '');
INSERT INTO `code_web_service_dic` VALUES ('178', 'Cnblogs', 'Cnblogs.com', '177', '');
INSERT INTO `code_web_service_dic` VALUES ('179', 'Bet365', 'Bet365.com', '178', '');
INSERT INTO `code_web_service_dic` VALUES ('180', 'Wetransfer', 'Wetransfer.com', '179', '');
INSERT INTO `code_web_service_dic` VALUES ('181', 'Eastday', 'Eastday.com', '180', '');
INSERT INTO `code_web_service_dic` VALUES ('182', 'Rambler', 'Rambler.ru', '181', '');
INSERT INTO `code_web_service_dic` VALUES ('183', 'Coinmarketcap', 'Coinmarketcap.com', '182', '');
INSERT INTO `code_web_service_dic` VALUES ('184', 'Huanqiu', 'Huanqiu.com', '183', '');
INSERT INTO `code_web_service_dic` VALUES ('185', 'Hulu', 'Hulu.com', '184', '');
INSERT INTO `code_web_service_dic` VALUES ('186', 'Abs-cbn', 'Abs-cbn.com', '185', '');
INSERT INTO `code_web_service_dic` VALUES ('187', 'Steamcommunity', 'Steamcommunity.com', '186', '');
INSERT INTO `code_web_service_dic` VALUES ('188', '4chan', '4chan.org', '187', '');
INSERT INTO `code_web_service_dic` VALUES ('189', 'Metropcs', 'Metropcs.mobi', '188', '');
INSERT INTO `code_web_service_dic` VALUES ('190', 'Zillow', 'Zillow.com', '189', '');
INSERT INTO `code_web_service_dic` VALUES ('191', 'Godaddy', 'Godaddy.com', '190', '');
INSERT INTO `code_web_service_dic` VALUES ('192', 'Blastingnews', 'Blastingnews.com', '191', '');
INSERT INTO `code_web_service_dic` VALUES ('193', 'Washingtonpost', 'Washingtonpost.com', '192', '');
INSERT INTO `code_web_service_dic` VALUES ('194', 'Upornia', 'Upornia.com', '193', '');
INSERT INTO `code_web_service_dic` VALUES ('195', 'Nownews', 'Nownews.com', '194', '');
INSERT INTO `code_web_service_dic` VALUES ('196', 'Hibids10', 'Hibids10.com', '195', '广告网站，无图标');
INSERT INTO `code_web_service_dic` VALUES ('197', 'Aliyun', 'Aliyun.com', '196', '');
INSERT INTO `code_web_service_dic` VALUES ('198', 'Fapmeth', 'Fapmeth.com', '197', '广告网站，无图标');
INSERT INTO `code_web_service_dic` VALUES ('199', 'Genius', 'Genius.com', '198', '');
INSERT INTO `code_web_service_dic` VALUES ('200', 'Tripadvisor', 'Tripadvisor.com', '199', '');
INSERT INTO `code_web_service_dic` VALUES ('201', 'Bilibili', 'Bilibili.com', '200', '');
INSERT INTO `code_web_service_dic` VALUES ('202', 'Thewhizmarketing', 'Thewhizmarketing.com', '201', '');
INSERT INTO `code_web_service_dic` VALUES ('203', 'Livejournal', 'Livejournal.com', '202', '');
INSERT INTO `code_web_service_dic` VALUES ('204', 'Irctc', 'Irctc.co.in', '203', '');
INSERT INTO `code_web_service_dic` VALUES ('205', 'Slack', 'Slack.com', '204', '');
INSERT INTO `code_web_service_dic` VALUES ('206', 'Bankofamerica', 'Bankofamerica.com', '205', '');
INSERT INTO `code_web_service_dic` VALUES ('207', 'Forbes', 'Forbes.com', '206', '');
INSERT INTO `code_web_service_dic` VALUES ('208', 'Archive', 'Archive.org', '207', '');
INSERT INTO `code_web_service_dic` VALUES ('209', 'Digitaldsp', 'Digitaldsp.com', '208', '');
INSERT INTO `code_web_service_dic` VALUES ('210', 'Ebay-kleinanzeigen', 'Ebay-kleinanzeigen.de', '209', '');
INSERT INTO `code_web_service_dic` VALUES ('211', 'Doublepimp', 'Doublepimp.com', '210', '');
INSERT INTO `code_web_service_dic` VALUES ('212', 'Foxnews', 'Foxnews.com', '211', '');
INSERT INTO `code_web_service_dic` VALUES ('213', 'Wellsfargo', 'Wellsfargo.com', '212', '');
INSERT INTO `code_web_service_dic` VALUES ('214', 'Speedtest', 'Speedtest.net', '213', '');
INSERT INTO `code_web_service_dic` VALUES ('215', 'Researchgate', 'Researchgate.net', '214', '');
INSERT INTO `code_web_service_dic` VALUES ('216', 'Medium', 'Medium.com', '215', '');
INSERT INTO `code_web_service_dic` VALUES ('217', 'Gfycat', 'Gfycat.com', '216', '');
INSERT INTO `code_web_service_dic` VALUES ('218', 'Exoclick', 'Exoclick.com', '217', '');
INSERT INTO `code_web_service_dic` VALUES ('219', '1337x', '1337x.to', '218', '');
INSERT INTO `code_web_service_dic` VALUES ('220', 'Ltn', 'Ltn.com.tw', '219', '');
INSERT INTO `code_web_service_dic` VALUES ('221', 'Hclips', 'Hclips.com', '220', '');
INSERT INTO `code_web_service_dic` VALUES ('222', 'Redd', 'Redd.it', '221', '');
INSERT INTO `code_web_service_dic` VALUES ('223', 'Trello', 'Trello.com', '222', '');
INSERT INTO `code_web_service_dic` VALUES ('224', 'Duckduckgo', 'Duckduckgo.com', '223', '');
INSERT INTO `code_web_service_dic` VALUES ('225', 'Xfinity', 'Xfinity.com', '224', '');
INSERT INTO `code_web_service_dic` VALUES ('226', 'Cloudfront', 'Cloudfront.net', '225', 'NOTICE！！！：无独立图标');
INSERT INTO `code_web_service_dic` VALUES ('227', 'Blackboard', 'Blackboard.com', '226', '');
INSERT INTO `code_web_service_dic` VALUES ('228', 'Elfagr', 'Elfagr.com', '227', '');
INSERT INTO `code_web_service_dic` VALUES ('229', 'Weather', 'Weather.com', '228', '');
INSERT INTO `code_web_service_dic` VALUES ('230', 'Youdao', 'Youdao.com', '229', '');
INSERT INTO `code_web_service_dic` VALUES ('231', 'Sberbank', 'Sberbank.ru', '230', '');
INSERT INTO `code_web_service_dic` VALUES ('232', 'Rednet', 'Rednet.cn', '231', '');
INSERT INTO `code_web_service_dic` VALUES ('233', 'Iqiyi', 'Iqiyi.com', '232', '');
INSERT INTO `code_web_service_dic` VALUES ('234', 'Setn', 'Setn.com', '233', '');
INSERT INTO `code_web_service_dic` VALUES ('235', 'People', 'People.com.cn', '234', '');
INSERT INTO `code_web_service_dic` VALUES ('236', 'Leboncoin', 'Leboncoin.fr', '235', '');
INSERT INTO `code_web_service_dic` VALUES ('237', 'Gmx', 'Gmx.net', '236', '');
INSERT INTO `code_web_service_dic` VALUES ('238', 'Shutterstock', 'Shutterstock.com', '237', '');
INSERT INTO `code_web_service_dic` VALUES ('239', 'Blogger', 'Blogger.com', '238', '');
INSERT INTO `code_web_service_dic` VALUES ('240', 'Ikea', 'Ikea.com', '239', '');
INSERT INTO `code_web_service_dic` VALUES ('241', 'Kakaku', 'Kakaku.com', '240', '');
INSERT INTO `code_web_service_dic` VALUES ('242', 'Hdzog', 'Hdzog.com', '241', '');
INSERT INTO `code_web_service_dic` VALUES ('243', '其他', '', '268435455', '');
