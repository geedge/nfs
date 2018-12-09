#修改基于音视频传输的IP PORT阻断业务的业务ID，由0x100改为0x10F
update function_service_dict set service_id='271' where dict_id='21';
#修改基于音视频传输的URL阻断业务的业务ID，由0x101改为0x110
update function_service_dict set service_id='272' where dict_id='30';
#音视频添加action白名单
INSERT INTO `function_service_dict` VALUES (130,20, 0, 128, 'whitelist', 256, 'av_content_ip_whitelist', '', 1, NULL, NULL, NULL, NULL, '2');
INSERT INTO `function_service_dict` VALUES (131,22, 0, 128, 'whitelist', 257, 'av_content_url_whitelist', '', 1, NULL, NULL, NULL, NULL, '2');