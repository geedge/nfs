#增加网页关键字请求跟应答
INSERT INTO function_region_dict
   (`config_service_type`, `config_multi_keywords`, `config_expr_type`, `config_hex`, `config_match_method`, `dict_id`, `function_id`, `config_region_code`, `config_region_value`, `config_district`, `config_desc`, `is_valid`, `creator_id`, `create_time`, `editor_id`, `edit_time`, `is_maat`, `region_type`, `config_ip_type`, `config_ip_pattern`, `config_port_pattern`, `config_direction`, `config_protocol`, `config_ip_port_show`, `config_region_sort`)
VALUES
   ('', '1', '0,1', '0,1,2', '0,1,2,3', 601, 635, 1, 'NTC_HTTP_REQ_BODY', '', '网站关键字请求内容配置', 1, NULL, NULL, NULL, NULL, 1, 2, '', '', '', '', '', '', 1);

   INSERT INTO function_region_dict
   (`config_service_type`, `config_multi_keywords`, `config_expr_type`, `config_hex`, `config_match_method`, `dict_id`, `function_id`, `config_region_code`, `config_region_value`, `config_district`, `config_desc`, `is_valid`, `creator_id`, `create_time`, `editor_id`, `edit_time`, `is_maat`, `region_type`, `config_ip_type`, `config_ip_pattern`, `config_port_pattern`, `config_direction`, `config_protocol`, `config_ip_port_show`, `config_region_sort`)
VALUES
   ('', '1', '0,1', '0,1,2', '0,1,2,3', 602, 635, 2, 'NTC_HTTP_RES_BODY', '', '网站关键字应答内容配置', 1, NULL, NULL, NULL, NULL, 1, 2, '', '', '', '', '', '', 2);
