#增加ddos相关菜单
INSERT INTO `sys_menu` VALUES ('1138', '801', '0,1,152,801,', 'protected_ddos', '保护ddos攻击', '10', '/log/ntc/ntcDdosLogs/list?isBlock=1', '', '', '1', '', '1', '2018-09-17 16:38:57', '1', '2018-09-17 16:38:57', '', '0', null, '0', '0', '301');
INSERT INTO `sys_menu` VALUES ('1139', '801', '0,1,152,801,', 'usual_ddos', '常用ddos攻击', '20', '/log/ntc/ntcDdosLogs/list?isBlock=0', '', '', '1', '', '1', '2018-09-17 16:38:57', '1', '2018-09-17 16:38:57', '', '0', null, '0', '0', '305');
UPDATE sys_menu set href='',function_id=null where id=801;
#增加邮件地址菜单
INSERT INTO `sys_menu` VALUES ('1140', '931', '0,1,86,931,', 'mail_address', '邮件地址配置', '5', '/ntc/mail/mailList', '', '', '1', 'mail:config', '1', '2018-09-10 14:31:09', '1', '2018-09-10 14:31:09', '', '1', null, '0', '0', '37');
INSERT INTO `sys_menu` VALUES ('1141', '934', '0,1,150,934,', 'mail_address', '邮件地址审核', '5', '/ntc/mail/mailList', '', '', '1', 'mail:confirm', '1', '2018-09-10 14:31:09', '1', '2018-09-10 14:31:09', '', '1', null, '0', '0', '37');
INSERT INTO `sys_menu` VALUES ('1142', '937', '0,1,151,937,', 'mail_address', '邮件地址审计', '5', '/ntc/mail/mailList', '', '', '1', 'mail:audit', '1', '2018-09-10 14:31:09', '1', '2018-09-10 14:31:09', '', '1', null, '0', '0', '37');


