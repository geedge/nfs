#增加邮件主菜单
INSERT INTO `sys_menu` VALUES ('1144', '931', '0,1,86,931,', 'mail', '邮件', '5', '', '', '', '1', '', '1', '2018-09-10 14:31:09', '1', '2018-09-10 14:31:09', '', '1', null, '0', '0', '');
INSERT INTO `sys_menu` VALUES ('1145', '934', '0,1,150,934,', 'mail', '邮件', '5', '', '', '', '1', '', '1', '2018-09-10 14:31:09', '1', '2018-09-10 14:31:09', '', '1', null, '0', '0', '');
INSERT INTO `sys_menu` VALUES ('1146', '937', '0,1,151,937,', 'mail', '邮件', '5', '', '', '', '1', '', '1', '2018-09-10 14:31:09', '1', '2018-09-10 14:31:09', '', '1', null, '0', '0', '');
#增加邮件地址菜单
INSERT INTO `sys_menu` VALUES ('1140', '1144', '0,1,86,931,1144,', 'mail_address', '邮件地址配置', '10', '/ntc/mail/mailList', '', '', '1', 'mail:config', '1', '2018-09-10 14:31:09', '1', '2018-09-10 14:31:09', '', '1', null, '0', '0', '37');
INSERT INTO `sys_menu` VALUES ('1141', '1145', '0,1,150,934,1145,', 'mail_address', '邮件地址审核', '10', '/ntc/mail/mailList', '', '', '1', 'mail:confirm', '1', '2018-09-10 14:31:09', '1', '2018-09-10 14:31:09', '', '1', null, '0', '0', '37');
INSERT INTO `sys_menu` VALUES ('1142', '1146', '0,1,151,937,1146,', 'mail_address', '邮件地址审计', '10', '/ntc/mail/mailList', '', '', '1', 'mail:audit', '1', '2018-09-10 14:31:09', '1', '2018-09-10 14:31:09', '', '1', null, '0', '0', '37');
#修改邮件菜单
UPDATE sys_menu set parent_id='1144',parent_ids='0,1,86,931,1144,',code='advanced',sort='30'  where id=682;
UPDATE sys_menu set parent_id='1145',parent_ids='0,1,150,934,1145,',code='advanced',sort='30'  where id=683;
UPDATE sys_menu set parent_id='1146',parent_ids='0,1,151,937,1146,',code='advanced',sort='30'  where id=815;


