INSERT INTO `function_service_dict` ( `function_id`, `protocol_id`, `action`, `action_code`, `service_id`, `service_name`, `service_desc`, `is_valid`, `creator_id`, `create_time`, `editor_id`, `edit_time`, `region_code`) VALUES ( '6', '4', '16', 'reject', '17', 'website_http_reject', NULL, '1', NULL, NULL, NULL, NULL, '1,2,3,4,5,6');
INSERT INTO `function_service_dict` ( `function_id`, `protocol_id`, `action`, `action_code`, `service_id`, `service_name`, `service_desc`, `is_valid`, `creator_id`, `create_time`, `editor_id`, `edit_time`, `region_code`) VALUES ( '6', '4', '1', 'monit', '129', 'website_http_monit', NULL, '1', NULL, NULL, NULL, NULL, '1,2,3,4,5,6');

update  function_region_dict set function_id='6' , config_service_type='http_url'  where  dict_id='15';

#ip复用地址池
INSERT INTO `function_service_dict` ( `function_id`, `protocol_id`, `action`, `action_code`, `service_id`, `service_name`, `service_desc`, `is_valid`, `creator_id`, `create_time`, `editor_id`, `edit_time`, `region_code`) VALUES ( '633', '0', '96', 'loop', '832', 'ip_reuse_adress_pool_loop', NULL, '1', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `function_region_dict` (`config_service_type`, `config_multi_keywords`, `config_expr_type`, `config_hex`, `config_match_method`,  `function_id`, `config_region_code`, `config_region_value`, `config_district`, `config_desc`, `is_valid`, `creator_id`, `create_time`, `editor_id`, `edit_time`, `is_maat`, `region_type`, `config_ip_type`, `config_ip_pattern`, `config_port_pattern`, `config_direction`, `config_protocol`, `config_ip_port_show`, `config_region_sort`) VALUES (NULL, NULL, NULL, NULL, NULL, '633', '1', 'IR_STATIC_IP_POOL_CB', NULL, 'IP复用地址池配置', '1', NULL, NULL, NULL, NULL, '2', '2', '4,6', '1,2,3', '1,2', '0', '0', '1', '1');

#http_url白名单
update function_region_dict set function_id='6' where dict_id='15';