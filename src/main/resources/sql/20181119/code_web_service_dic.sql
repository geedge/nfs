/*
Navicat MySQL Data Transfer

Source Server         : 192.168.10.204_3306
Source Server Version : 50505
Source Host           : 192.168.10.204:3306
Source Database       : gwall_dev

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2018-11-19 16:52:49
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for code_web_service_dic
-- ----------------------------
DROP TABLE IF EXISTS `code_web_service_dic`;
CREATE TABLE `code_web_service_dic` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `website` varchar(255) DEFAULT '',
  `domain_name` varchar(255) DEFAULT '' COMMENT '域名',
  `view_code` varchar(255) DEFAULT '',
  `icon` varchar(255) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=245 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of code_web_service_dic
-- ----------------------------
INSERT INTO `code_web_service_dic` VALUES ('1', 'Unknow', 'Unknow', '0', '');
INSERT INTO `code_web_service_dic` VALUES ('2', 'Google', 'Google.com', '1', '');
INSERT INTO `code_web_service_dic` VALUES ('3', 'Youtube', 'Youtube.com', '1001', '');
INSERT INTO `code_web_service_dic` VALUES ('4', 'Facebook', 'Facebook.com', '2001', '');
INSERT INTO `code_web_service_dic` VALUES ('5', 'Baidu', 'Baidu.com', '3001', '');
INSERT INTO `code_web_service_dic` VALUES ('6', 'Wikipedia', 'Wikipedia.org', '4001', '');
INSERT INTO `code_web_service_dic` VALUES ('7', 'Reddit', 'Reddit.com', '5001', '');
INSERT INTO `code_web_service_dic` VALUES ('8', 'Yahoo', 'Yahoo.com', '6001', '');
INSERT INTO `code_web_service_dic` VALUES ('9', 'Qq', 'Qq.com', '7001', '');
INSERT INTO `code_web_service_dic` VALUES ('10', 'Taobao', 'Taobao.com', '8001', '');
INSERT INTO `code_web_service_dic` VALUES ('11', 'Amazon', 'Amazon.com', '9001', '');
INSERT INTO `code_web_service_dic` VALUES ('12', 'Tmall', 'Tmall.com', '10001', '');
INSERT INTO `code_web_service_dic` VALUES ('13', 'Twitter', 'Twitter.com', '11001', '');
INSERT INTO `code_web_service_dic` VALUES ('14', 'Sohu', 'Sohu.com', '12001', '');
INSERT INTO `code_web_service_dic` VALUES ('15', 'Instagram', 'Instagram.com', '13001', '');
INSERT INTO `code_web_service_dic` VALUES ('16', 'Vk', 'Vk.com', '14001', '');
INSERT INTO `code_web_service_dic` VALUES ('17', 'Live', 'Live.com', '15001', '');
INSERT INTO `code_web_service_dic` VALUES ('18', 'Jd', 'Jd.com', '16001', '');
INSERT INTO `code_web_service_dic` VALUES ('19', 'Sina', 'Sina.com.cn', '17001', '');
INSERT INTO `code_web_service_dic` VALUES ('20', 'Weibo', 'Weibo.com', '18001', '');
INSERT INTO `code_web_service_dic` VALUES ('21', 'Yandex', 'Yandex.ru', '19001', '');
INSERT INTO `code_web_service_dic` VALUES ('22', '360', '360.cn', '20001', '');
INSERT INTO `code_web_service_dic` VALUES ('23', 'Netflix', 'Netflix.com', '22001', '');
INSERT INTO `code_web_service_dic` VALUES ('24', 'Pornhub', 'Pornhub.com', '23001', '');
INSERT INTO `code_web_service_dic` VALUES ('25', 'Twitch', 'Twitch.tv', '24001', '');
INSERT INTO `code_web_service_dic` VALUES ('26', 'Linkedin', 'Linkedin.com', '25001', '');
INSERT INTO `code_web_service_dic` VALUES ('27', 'Csdn', 'Csdn.net', '26001', '');
INSERT INTO `code_web_service_dic` VALUES ('28', 'Microsoft', 'Microsoft.com', '28001', '');
INSERT INTO `code_web_service_dic` VALUES ('29', 'Office', 'Office.com', '29001', '');
INSERT INTO `code_web_service_dic` VALUES ('30', 'Bing', 'Bing.com', '30001', '');
INSERT INTO `code_web_service_dic` VALUES ('31', 'Ebay', 'Ebay.com', '31001', '');
INSERT INTO `code_web_service_dic` VALUES ('32', 'Alipay', 'Alipay.com', '32001', '');
INSERT INTO `code_web_service_dic` VALUES ('33', 'Xvideos', 'Xvideos.com', '33001', '');
INSERT INTO `code_web_service_dic` VALUES ('34', 'Mail', 'Mail.ru', '34001', '');
INSERT INTO `code_web_service_dic` VALUES ('35', 'Ok', 'Ok.ru', '35001', '');
INSERT INTO `code_web_service_dic` VALUES ('36', 'Msn', 'Msn.com', '36001', '');
INSERT INTO `code_web_service_dic` VALUES ('37', 'Imgur', 'Imgur.com', '39001', '');
INSERT INTO `code_web_service_dic` VALUES ('38', 'Aliexpress', 'Aliexpress.com', '40001', '');
INSERT INTO `code_web_service_dic` VALUES ('39', 'Tumblr', 'Tumblr.com', '41001', '');
INSERT INTO `code_web_service_dic` VALUES ('40', 'Wordpress', 'Wordpress.com', '42001', '');
INSERT INTO `code_web_service_dic` VALUES ('41', 'Wikia', 'Wikia.com', '43001', '');
INSERT INTO `code_web_service_dic` VALUES ('42', 'Imdb', 'Imdb.com', '44001', '');
INSERT INTO `code_web_service_dic` VALUES ('43', 'Whatsapp', 'Whatsapp.com', '45001', '');
INSERT INTO `code_web_service_dic` VALUES ('44', 'Stackoverflow', 'Stackoverflow.com', '46001', '');
INSERT INTO `code_web_service_dic` VALUES ('45', 'Github', 'Github.com', '47001', '');
INSERT INTO `code_web_service_dic` VALUES ('46', 'Livejasmin', 'Livejasmin.com', '48001', '');
INSERT INTO `code_web_service_dic` VALUES ('47', 'Xhamster', 'Xhamster.com', '49001', '');
INSERT INTO `code_web_service_dic` VALUES ('48', 'Hao123', 'Hao123.com', '50001', '');
INSERT INTO `code_web_service_dic` VALUES ('49', 'Blogspot', 'Blogspot.com', '51001', '');
INSERT INTO `code_web_service_dic` VALUES ('50', 'Deloton', 'Deloton.com', '52001', '');
INSERT INTO `code_web_service_dic` VALUES ('51', 'Paypal', 'Paypal.com', '53001', '');
INSERT INTO `code_web_service_dic` VALUES ('52', 'Tribunnews', 'Tribunnews.com', '54001', '');
INSERT INTO `code_web_service_dic` VALUES ('53', 'Popads', 'Popads.net', '55001', '');
INSERT INTO `code_web_service_dic` VALUES ('54', 'Apple', 'Apple.com', '56001', '');
INSERT INTO `code_web_service_dic` VALUES ('55', 'Diply', 'Diply.com', '57001', '');
INSERT INTO `code_web_service_dic` VALUES ('56', 'Bongacams', 'Bongacams.com', '58001', '');
INSERT INTO `code_web_service_dic` VALUES ('57', 'Pinterest', 'Pinterest.com', '60001', '');
INSERT INTO `code_web_service_dic` VALUES ('58', 'Xnxx', 'Xnxx.com', '61001', '');
INSERT INTO `code_web_service_dic` VALUES ('59', 'Adobe', 'Adobe.com', '62001', '');
INSERT INTO `code_web_service_dic` VALUES ('60', 'Providr', 'Providr.com', '63001', '');
INSERT INTO `code_web_service_dic` VALUES ('61', 'Coccoc', 'Coccoc.com', '65001', '');
INSERT INTO `code_web_service_dic` VALUES ('62', 'Txxx', 'Txxx.com', '66001', '');
INSERT INTO `code_web_service_dic` VALUES ('63', 'Dropbox', 'Dropbox.com', '67001', '');
INSERT INTO `code_web_service_dic` VALUES ('64', 'Savefrom', 'Savefrom.net', '69001', '');
INSERT INTO `code_web_service_dic` VALUES ('65', 'Pixnet', 'Pixnet.net', '70001', '');
INSERT INTO `code_web_service_dic` VALUES ('66', 'Bbc', 'Bbc.com', '71001', '');
INSERT INTO `code_web_service_dic` VALUES ('67', 'Thestartmagazine', 'Thestartmagazine.com', '72001', '');
INSERT INTO `code_web_service_dic` VALUES ('68', 'Quora', 'Quora.com', '73001', '');
INSERT INTO `code_web_service_dic` VALUES ('69', 'Tianya', 'Tianya.cn', '74001', '');
INSERT INTO `code_web_service_dic` VALUES ('70', 'Soso', 'Soso.com', '75001', '');
INSERT INTO `code_web_service_dic` VALUES ('71', 'Chaturbate', 'Chaturbate.com', '76001', '');
INSERT INTO `code_web_service_dic` VALUES ('72', 'Amazonaws', 'Amazonaws.com', '77001', '');
INSERT INTO `code_web_service_dic` VALUES ('73', 'Booking', 'Booking.com', '78001', '');
INSERT INTO `code_web_service_dic` VALUES ('74', 'Espn', 'Espn.com', '79001', '');
INSERT INTO `code_web_service_dic` VALUES ('75', 'haosou', 'So.com', '80001', '');
INSERT INTO `code_web_service_dic` VALUES ('76', 'Cnn', 'Cnn.com', '81001', '');
INSERT INTO `code_web_service_dic` VALUES ('77', 'Craigslist', 'Craigslist.org', '82001', '');
INSERT INTO `code_web_service_dic` VALUES ('78', 'Nicovideo', 'Nicovideo.jp', '83001', '');
INSERT INTO `code_web_service_dic` VALUES ('79', 'Xinhuanet', 'Xinhuanet.com', '84001', '');
INSERT INTO `code_web_service_dic` VALUES ('80', 'Soundcloud', 'Soundcloud.com', '85001', '');
INSERT INTO `code_web_service_dic` VALUES ('81', 'Tokopedia', 'Tokopedia.com', '86001', '');
INSERT INTO `code_web_service_dic` VALUES ('82', 'Dailymotion', 'Dailymotion.com', '87001', '');
INSERT INTO `code_web_service_dic` VALUES ('83', 'Youth', 'Youth.cn', '88001', '');
INSERT INTO `code_web_service_dic` VALUES ('84', 'Nytimes', 'Nytimes.com', '89001', '');
INSERT INTO `code_web_service_dic` VALUES ('85', 'Detik', 'Detik.com', '90001', '');
INSERT INTO `code_web_service_dic` VALUES ('86', 'Ask', 'Ask.com', '91001', '');
INSERT INTO `code_web_service_dic` VALUES ('87', 'Naver', 'Naver.com', '92001', '');
INSERT INTO `code_web_service_dic` VALUES ('88', 'Stackexchange', 'Stackexchange.com', '93001', '');
INSERT INTO `code_web_service_dic` VALUES ('89', 'Dkn', 'Dkn.tv', '94001', '');
INSERT INTO `code_web_service_dic` VALUES ('90', 'Thepiratebay', 'Thepiratebay.org', '95001', '');
INSERT INTO `code_web_service_dic` VALUES ('91', 'Roblox', 'Roblox.com', '96001', '');
INSERT INTO `code_web_service_dic` VALUES ('92', 'Rakuten', 'Rakuten.co.jp', '97001', '');
INSERT INTO `code_web_service_dic` VALUES ('93', 'Onlinesbi', 'Onlinesbi.com', '98001', '');
INSERT INTO `code_web_service_dic` VALUES ('94', 'Openload', 'Openload.co', '99001', '');
INSERT INTO `code_web_service_dic` VALUES ('95', 'Spotify', 'Spotify.com', '100001', '');
INSERT INTO `code_web_service_dic` VALUES ('96', 'Nih', 'Nih.gov', '101001', '');
INSERT INTO `code_web_service_dic` VALUES ('97', 'Vimeo', 'Vimeo.com', '102001', '');
INSERT INTO `code_web_service_dic` VALUES ('98', 'Fc2', 'Fc2.com', '103001', '');
INSERT INTO `code_web_service_dic` VALUES ('99', 'Aparat', 'Aparat.com', '104001', '');
INSERT INTO `code_web_service_dic` VALUES ('100', 'Ettoday', 'Ettoday.net', '105001', '');
INSERT INTO `code_web_service_dic` VALUES ('101', 'Mozilla', 'Mozilla.org', '106001', '');
INSERT INTO `code_web_service_dic` VALUES ('102', '1688', '1688.com', '107001', '');
INSERT INTO `code_web_service_dic` VALUES ('103', 'K618', 'K618.cn', '108001', '');
INSERT INTO `code_web_service_dic` VALUES ('104', 'Exdynsrv', 'Exdynsrv.com', '109001', '');
INSERT INTO `code_web_service_dic` VALUES ('105', 'Oath', 'Oath.com', '110001', '');
INSERT INTO `code_web_service_dic` VALUES ('106', 'Ntd', 'Ntd.tv', '111001', '');
INSERT INTO `code_web_service_dic` VALUES ('107', 'Theguardian', 'Theguardian.com', '112001', '');
INSERT INTO `code_web_service_dic` VALUES ('108', 'Mediafire', 'Mediafire.com', '113001', '');
INSERT INTO `code_web_service_dic` VALUES ('109', 'Chase', 'Chase.com', '114001', '');
INSERT INTO `code_web_service_dic` VALUES ('110', 'Flipkart', 'Flipkart.com', '115001', '');
INSERT INTO `code_web_service_dic` VALUES ('111', 'Exosrv', 'Exosrv.com', '116001', '');
INSERT INTO `code_web_service_dic` VALUES ('112', 'Avito', 'Avito.ru', '117001', '');
INSERT INTO `code_web_service_dic` VALUES ('113', 'Slideshare', 'Slideshare.net', '118001', '');
INSERT INTO `code_web_service_dic` VALUES ('114', 'Dailymail', 'Dailymail.co.uk', '119001', '');
INSERT INTO `code_web_service_dic` VALUES ('115', 'Discordapp', 'Discordapp.com', '120001', '');
INSERT INTO `code_web_service_dic` VALUES ('116', 'Zhihu', 'Zhihu.com', '121001', '');
INSERT INTO `code_web_service_dic` VALUES ('117', 'Salesforce', 'Salesforce.com', '122001', '');
INSERT INTO `code_web_service_dic` VALUES ('118', 'Theepochtimes', 'Theepochtimes.com', '123001', '');
INSERT INTO `code_web_service_dic` VALUES ('119', 'Softonic', 'Softonic.com', '124001', '恶意网站-无图标');
INSERT INTO `code_web_service_dic` VALUES ('120', 'Globo', 'Globo.com', '125001', '');
INSERT INTO `code_web_service_dic` VALUES ('121', 'Bukalapak', 'Bukalapak.com', '126001', '');
INSERT INTO `code_web_service_dic` VALUES ('122', 'Douyu', 'Douyu.com', '127001', '');
INSERT INTO `code_web_service_dic` VALUES ('123', 'Nextoptim', 'Nextoptim.com', '128001', '');
INSERT INTO `code_web_service_dic` VALUES ('124', 'Deviantart', 'Deviantart.com', '129001', '');
INSERT INTO `code_web_service_dic` VALUES ('125', 'Indeed', 'Indeed.com', '130001', '');
INSERT INTO `code_web_service_dic` VALUES ('126', 'Lolsided', 'Lolsided.com', '131001', '');
INSERT INTO `code_web_service_dic` VALUES ('127', 'Bles', 'Bles.com', '132001', '');
INSERT INTO `code_web_service_dic` VALUES ('128', 'Cnet', 'Cnet.com', '133001', '');
INSERT INTO `code_web_service_dic` VALUES ('129', 'Alibaba', 'Alibaba.com', '134001', '');
INSERT INTO `code_web_service_dic` VALUES ('130', 'Bet9ja', 'Bet9ja.com', '135001', '');
INSERT INTO `code_web_service_dic` VALUES ('131', 'Vice', 'Vice.com', '136001', '恶意网站-无图标');
INSERT INTO `code_web_service_dic` VALUES ('132', 'Lifedaily', 'Lifedaily.com', '137001', '');
INSERT INTO `code_web_service_dic` VALUES ('133', 'Hotmovs', 'Hotmovs.com', '138001', '');
INSERT INTO `code_web_service_dic` VALUES ('134', 'Sogou', 'Sogou.com', '139001', '');
INSERT INTO `code_web_service_dic` VALUES ('135', 'Uol', 'Uol.com.br', '140001', '');
INSERT INTO `code_web_service_dic` VALUES ('136', 'Doubleclick', 'Doubleclick.net', '141001', '');
INSERT INTO `code_web_service_dic` VALUES ('137', 'Mercadolivre', 'Mercadolivre.com.br', '142001', '');
INSERT INTO `code_web_service_dic` VALUES ('138', 'Speakol', 'Speakol.com', '143001', '');
INSERT INTO `code_web_service_dic` VALUES ('139', 'Babytree', 'Babytree.com', '144001', '');
INSERT INTO `code_web_service_dic` VALUES ('140', 'Liputan6', 'Liputan6.com', '145001', '');
INSERT INTO `code_web_service_dic` VALUES ('141', 'Wikihow', 'Wikihow.com', '146001', '');
INSERT INTO `code_web_service_dic` VALUES ('142', 'China', 'China.com.cn', '147001', '');
INSERT INTO `code_web_service_dic` VALUES ('143', 'Etsy', 'Etsy.com', '148001', '');
INSERT INTO `code_web_service_dic` VALUES ('144', 'W3schools', 'W3schools.com', '149001', '');
INSERT INTO `code_web_service_dic` VALUES ('145', 'Youm7', 'Youm7.com', '150001', '');
INSERT INTO `code_web_service_dic` VALUES ('146', 'Scribd', 'Scribd.com', '151001', '');
INSERT INTO `code_web_service_dic` VALUES ('147', 'Rumble', 'Rumble.com', '152001', '');
INSERT INTO `code_web_service_dic` VALUES ('148', 'Walmart', 'Walmart.com', '153001', '');
INSERT INTO `code_web_service_dic` VALUES ('149', 'Twimg', 'Twimg.com', '154001', '');
INSERT INTO `code_web_service_dic` VALUES ('150', 'Yelp', 'Yelp.com', '155001', '');
INSERT INTO `code_web_service_dic` VALUES ('151', 'Kompas', 'Kompas.com', '156001', '');
INSERT INTO `code_web_service_dic` VALUES ('152', 'Messenger', 'Messenger.com', '157001', '');
INSERT INTO `code_web_service_dic` VALUES ('153', 'Onlinevideoconverter', 'Onlinevideoconverter.com', '159001', '');
INSERT INTO `code_web_service_dic` VALUES ('154', 'Redtube', 'Redtube.com', '160001', '');
INSERT INTO `code_web_service_dic` VALUES ('155', 'Gmw', 'Gmw.cn', '161001', '');
INSERT INTO `code_web_service_dic` VALUES ('156', 'Caijing', 'Caijing.com.cn', '162001', '');
INSERT INTO `code_web_service_dic` VALUES ('157', 'Mama', 'Mama.cn', '163001', '同twitter');
INSERT INTO `code_web_service_dic` VALUES ('158', 'Chinadaily', 'Chinadaily.com.cn', '164001', '');
INSERT INTO `code_web_service_dic` VALUES ('159', 'Cricbuzz', 'Cricbuzz.com', '165001', '');
INSERT INTO `code_web_service_dic` VALUES ('160', 'Ladbible', 'Ladbible.com', '166001', '');
INSERT INTO `code_web_service_dic` VALUES ('161', 'Doublepimpssl', 'Doublepimpssl.com', '167001', '');
INSERT INTO `code_web_service_dic` VALUES ('162', 'Steampowered', 'Steampowered.com', '168001', '');
INSERT INTO `code_web_service_dic` VALUES ('163', 'Mega', 'Mega.nz', '169001', '');
INSERT INTO `code_web_service_dic` VALUES ('164', 'Yts', 'Yts.am', '170001', '');
INSERT INTO `code_web_service_dic` VALUES ('165', 'Youporn', 'Youporn.com', '171001', '');
INSERT INTO `code_web_service_dic` VALUES ('166', 'Buzzfeed', 'Buzzfeed.com', '172001', '');
INSERT INTO `code_web_service_dic` VALUES ('167', 'Indiatimes', 'Indiatimes.com', '173001', '');
INSERT INTO `code_web_service_dic` VALUES ('168', 'Hotstar', 'Hotstar.com', '174001', '');
INSERT INTO `code_web_service_dic` VALUES ('169', 'Cnblogs', 'Cnblogs.com', '175001', '');
INSERT INTO `code_web_service_dic` VALUES ('170', 'Bet365', 'Bet365.com', '176001', '');
INSERT INTO `code_web_service_dic` VALUES ('171', 'Wetransfer', 'Wetransfer.com', '177001', '');
INSERT INTO `code_web_service_dic` VALUES ('172', 'Eastday', 'Eastday.com', '178001', '');
INSERT INTO `code_web_service_dic` VALUES ('173', 'Rambler', 'Rambler.ru', '179001', '');
INSERT INTO `code_web_service_dic` VALUES ('174', 'Coinmarketcap', 'Coinmarketcap.com', '180001', '');
INSERT INTO `code_web_service_dic` VALUES ('175', 'Huanqiu', 'Huanqiu.com', '181001', '');
INSERT INTO `code_web_service_dic` VALUES ('176', 'Hulu', 'Hulu.com', '182001', '');
INSERT INTO `code_web_service_dic` VALUES ('177', 'Abs-cbn', 'Abs-cbn.com', '183001', '');
INSERT INTO `code_web_service_dic` VALUES ('178', 'Steamcommunity', 'Steamcommunity.com', '184001', '');
INSERT INTO `code_web_service_dic` VALUES ('179', '4chan', '4chan.org', '185001', '');
INSERT INTO `code_web_service_dic` VALUES ('180', 'Metropcs', 'Metropcs.mobi', '186001', '');
INSERT INTO `code_web_service_dic` VALUES ('181', 'Zillow', 'Zillow.com', '187001', '');
INSERT INTO `code_web_service_dic` VALUES ('182', 'Godaddy', 'Godaddy.com', '188001', '');
INSERT INTO `code_web_service_dic` VALUES ('183', 'Blastingnews', 'Blastingnews.com', '189001', '');
INSERT INTO `code_web_service_dic` VALUES ('184', 'Washingtonpost', 'Washingtonpost.com', '190001', '');
INSERT INTO `code_web_service_dic` VALUES ('185', 'Upornia', 'Upornia.com', '191001', '');
INSERT INTO `code_web_service_dic` VALUES ('186', 'Nownews', 'Nownews.com', '192001', '');
INSERT INTO `code_web_service_dic` VALUES ('187', 'Hibids10', 'Hibids10.com', '193001', '');
INSERT INTO `code_web_service_dic` VALUES ('188', 'Aliyun', 'Aliyun.com', '194001', '');
INSERT INTO `code_web_service_dic` VALUES ('189', 'Fapmeth', 'Fapmeth.com', '195001', '');
INSERT INTO `code_web_service_dic` VALUES ('190', 'Genius', 'Genius.com', '196001', '');
INSERT INTO `code_web_service_dic` VALUES ('191', 'Tripadvisor', 'Tripadvisor.com', '197001', '');
INSERT INTO `code_web_service_dic` VALUES ('192', 'Bilibili', 'Bilibili.com', '198001', '');
INSERT INTO `code_web_service_dic` VALUES ('193', 'Livejournal', 'Livejournal.com', '199001', '');
INSERT INTO `code_web_service_dic` VALUES ('194', 'Irctc', 'Irctc.co.in', '200001', '');
INSERT INTO `code_web_service_dic` VALUES ('195', 'Slack', 'Slack.com', '201001', '');
INSERT INTO `code_web_service_dic` VALUES ('196', 'Bankofamerica', 'Bankofamerica.com', '202001', '广告网站，无图标');
INSERT INTO `code_web_service_dic` VALUES ('197', 'Forbes', 'Forbes.com', '203001', '');
INSERT INTO `code_web_service_dic` VALUES ('198', 'Archive', 'Archive.org', '204001', '广告网站，无图标');
INSERT INTO `code_web_service_dic` VALUES ('199', 'Ebay-kleinanzeigen', 'Ebay-kleinanzeigen.de', '205001', '');
INSERT INTO `code_web_service_dic` VALUES ('200', 'Doublepimp', 'Doublepimp.com', '206001', '');
INSERT INTO `code_web_service_dic` VALUES ('201', 'Foxnews', 'Foxnews.com', '207001', '');
INSERT INTO `code_web_service_dic` VALUES ('202', 'Wellsfargo', 'Wellsfargo.com', '208001', '');
INSERT INTO `code_web_service_dic` VALUES ('203', 'Speedtest', 'Speedtest.net', '209001', '');
INSERT INTO `code_web_service_dic` VALUES ('204', 'Researchgate', 'Researchgate.net', '210001', '');
INSERT INTO `code_web_service_dic` VALUES ('205', 'Medium', 'Medium.com', '211001', '');
INSERT INTO `code_web_service_dic` VALUES ('206', 'Gfycat', 'Gfycat.com', '212001', '');
INSERT INTO `code_web_service_dic` VALUES ('207', 'Exoclick', 'Exoclick.com', '213001', '');
INSERT INTO `code_web_service_dic` VALUES ('208', '1337x', '1337x.to', '214001', '');
INSERT INTO `code_web_service_dic` VALUES ('209', 'Ltn', 'Ltn.com.tw', '215001', '');
INSERT INTO `code_web_service_dic` VALUES ('210', 'Hclips', 'Hclips.com', '216001', '');
INSERT INTO `code_web_service_dic` VALUES ('211', 'Redd', 'Redd.it', '217001', '');
INSERT INTO `code_web_service_dic` VALUES ('212', 'Trello', 'Trello.com', '218001', '');
INSERT INTO `code_web_service_dic` VALUES ('213', 'Duckduckgo', 'Duckduckgo.com', '219001', '');
INSERT INTO `code_web_service_dic` VALUES ('214', 'Xfinity', 'Xfinity.com', '220001', '');
INSERT INTO `code_web_service_dic` VALUES ('215', 'Cloudfront', 'Cloudfront.net', '221001', '');
INSERT INTO `code_web_service_dic` VALUES ('216', 'Blackboard', 'Blackboard.com', '222001', '');
INSERT INTO `code_web_service_dic` VALUES ('217', 'Elfagr', 'Elfagr.com', '223001', '');
INSERT INTO `code_web_service_dic` VALUES ('218', 'Weather', 'Weather.com', '224001', '');
INSERT INTO `code_web_service_dic` VALUES ('219', 'Youdao', 'Youdao.com', '225001', '');
INSERT INTO `code_web_service_dic` VALUES ('220', 'Sberbank', 'Sberbank.ru', '226001', '');
INSERT INTO `code_web_service_dic` VALUES ('221', 'Rednet', 'Rednet.cn', '227001', '');
INSERT INTO `code_web_service_dic` VALUES ('222', 'Iqiyi', 'Iqiyi.com', '228001', '');
INSERT INTO `code_web_service_dic` VALUES ('223', 'Setn', 'Setn.com', '229001', '');
INSERT INTO `code_web_service_dic` VALUES ('224', 'People', 'People.com.cn', '230001', '');
INSERT INTO `code_web_service_dic` VALUES ('225', 'Leboncoin', null, '231001', '');
INSERT INTO `code_web_service_dic` VALUES ('226', 'Gmx', null, '232001', 'NOTICE！！！：无独立图标');
INSERT INTO `code_web_service_dic` VALUES ('227', 'Shutterstock', null, '233001', '');
INSERT INTO `code_web_service_dic` VALUES ('228', 'Blogger', null, '234001', '');
INSERT INTO `code_web_service_dic` VALUES ('229', 'Ikea', null, '235001', '');
INSERT INTO `code_web_service_dic` VALUES ('230', 'Kakaku', null, '236001', '');
INSERT INTO `code_web_service_dic` VALUES ('231', 'Hdzog', null, '237001', '');
INSERT INTO `code_web_service_dic` VALUES ('232', 'Unspecified', null, '268435455', '');
