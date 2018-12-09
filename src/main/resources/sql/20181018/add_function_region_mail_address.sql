#增加邮件地址头域，subscribe_id
INSERT INTO function_region_dict
   (`config_service_type`, `config_multi_keywords`, `config_expr_type`, `config_hex`, `config_match_method`, `dict_id`, `function_id`, `config_region_code`, `config_region_value`, `config_district`, `config_desc`, `is_valid`, `creator_id`, `create_time`, `editor_id`, `edit_time`, `is_maat`, `region_type`, `config_ip_type`, `config_ip_pattern`, `config_port_pattern`, `config_direction`, `config_protocol`, `config_ip_port_show`, `config_region_sort`)
VALUES
   ('', '1', '0,1', '0,1,2', '0,1,2,3', 599, 37, 1, 'NTC_MAIL_HDR', 'From,To', '邮件地址头域配置', 1, NULL, NULL, NULL, NULL, 1, 3, '', '', '', '', '', '', 1);
INSERT INTO function_region_dict
   (`config_service_type`, `config_multi_keywords`, `config_expr_type`, `config_hex`, `config_match_method`, `dict_id`, `function_id`, `config_region_code`, `config_region_value`, `config_district`, `config_desc`, `is_valid`, `creator_id`, `create_time`, `editor_id`, `edit_time`, `is_maat`, `region_type`, `config_ip_type`, `config_ip_pattern`, `config_port_pattern`, `config_direction`, `config_protocol`, `config_ip_port_show`, `config_region_sort`)
VALUES
   ('', '', '', '', '', 600, 37, 2, 'NTC_SUBSCRIBE_ID', '', '邮件地址subscribeId配置', 1, NULL, NULL, NULL, NULL, 1, 2, '', '', '', '', '', '', 2);
