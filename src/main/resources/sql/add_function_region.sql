INSERT INTO function_region_dict
   ()
VALUES
   ('subscribe_id', '0', '0,1', '0,1,2', '0,1,2,3', 174, 34, 2, 'NTC_SUBSCRIBE_ID', '', 'SSL SUBSCRIBE_ID配置', 1, NULL, NULL, NULL, NULL, 1, 2, '', '', '', '', '', '', 3);

INSERT INTO function_region_dict
   ()
VALUES
   ('subscribe_id', '0', '0,1', '0,1,2', '0,1,2,3', 175, 7, 2, 'NTC_SUBSCRIBE_ID', '', 'DNS SUBSCRIBE_ID配置', 1, NULL, NULL, NULL, NULL, 1, 2, '', '', '', '', '', '', 5);

INSERT INTO function_region_dict
   ()
VALUES
   ('subscribe_id', '0', '0,1', '0,1,2', '0,1,2,3', 176, 35, 3, 'NTC_SUBSCRIBE_ID', '', '邮件 SUBSCRIBE_ID配置', 1, NULL, NULL,NULL , NULL, 1, 2, '', '', '', '', '', '', 7);

INSERT INTO function_region_dict
   ()
VALUES
   ('subscribe_id', '0', '0,1', '0,1,2', '0,1,2,3', 177, 8, 3, 'NTC_SUBSCRIBE_ID', '', 'HTTP SUBSCRIBE_ID配置', 1, NULL, NULL,NULL , NULL, 1, 3, '', '', '', '', '', '', 7);
   
   
INSERT INTO function_region_dict
   (`config_service_type`, `config_multi_keywords`, `config_expr_type`, `config_hex`, `config_match_method`, `dict_id`, `function_id`, `config_region_code`, `config_region_value`, `config_district`, `config_desc`, `is_valid`, `creator_id`, `create_time`, `editor_id`, `edit_time`, `is_maat`, `region_type`, `config_ip_type`, `config_ip_pattern`, `config_port_pattern`, `config_direction`, `config_protocol`, `config_ip_port_show`, `config_region_sort`)
VALUES
   ('', '', '', '', '', 186, 570, 1, 'PXY_OBJ_KEYRING', '', '证书策略配置', 1, NULL, NULL, NULL, NULL, 2, 6, '', '', '', '', '', '', 1);
