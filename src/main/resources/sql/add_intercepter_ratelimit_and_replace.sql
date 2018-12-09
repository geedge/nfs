INSERT INTO function_service_dict
   ()
VALUES
   (108, 200, 0, 64, 'ratelimit', 531, 'ip_intercepter_ratelimit', '', 1,null , null,null , null,null );

INSERT INTO function_service_dict
   ()
VALUES
   (109, 201, 0, 64, 'ratelimit', 541, 'domain_intercepter_ratelimit', '', 1,null , null,null ,null,null );
   
   
   INSERT INTO function_service_dict
   ()
VALUES
   (110, 200, 0, 80, 'replace', 551, 'ip_intercepter_replace', '', 1,null , null,null , null,null );

INSERT INTO function_service_dict
   ()
VALUES
   (111, 201, 0, 80, 'replace', 561, 'domain_intercepter_replace', '', 1,null , null,null ,null,null );

   
   INSERT INTO function_region_dict
   (`config_service_type`, `config_multi_keywords`, `config_expr_type`, `config_hex`, `config_match_method`, `dict_id`, `function_id`, `config_region_code`, `config_region_value`, `config_district`, `config_desc`, `is_valid`, `creator_id`, `create_time`, `editor_id`, `edit_time`, `is_maat`, `region_type`, `config_ip_type`, `config_ip_pattern`, `config_port_pattern`, `config_direction`, `config_protocol`, `config_ip_port_show`)
VALUES
   ('', '0', '0,1', '0,1,2', '0,1,2,3', 167, 200, 2, 'PXY_INTERCEPT_PKT_BIN', '', 'PXY±¨ÎÄ', 1, NULL, NULL, NULL, NULL, 1, 2, '', '', '', '', '', '');
