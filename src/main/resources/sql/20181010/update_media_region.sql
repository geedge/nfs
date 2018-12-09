#流媒体协议配置
update function_service_dict set service_id='39',protocol_id='23' where dict_id='30';
update function_service_dict set service_id='151',protocol_id='23' where dict_id='31';
#流媒体协议 增加IP配置
INSERT INTO `function_region_dict` (`function_id`, `config_district`, `config_region_code`, `config_region_value`, `config_desc`, `is_valid`, `is_maat`, `region_type`, `creator_id`, `create_time`, `editor_id`, `edit_time`, `config_service_type`, `config_expr_type`, `config_match_method`, `config_ip_type`, `config_hex`, `config_multi_keywords`, `config_ip_pattern`, `config_port_pattern`, `config_direction`, `config_protocol`, `config_ip_port_show`, `config_region_sort`) VALUES ('22', '', '1', 'NTC_UNIVERSAL_IP', '流媒体协议 IP配置', '1', '1', '1', NULL, '0000-00-00 00:00:00', NULL, '0000-00-00 00:00:00', '', '', '', '4,6', '', '', '1,2,3', '1,2', '0,1', '0,6,17', '1,2,3,4', '1');
update function_region_dict set config_region_value='NTC_STREAMING_MEDIA_URL' where dict_id='25';

#voip配置
update function_service_dict set service_id='38',protocol_id='22' where dict_id='16';
update function_service_dict set service_id='150',protocol_id='22' where dict_id='17';
#voip 配置
update function_region_dict set config_region_value='NTC_UNIVERSAL_IP' where dict_id='8';
update function_region_dict set config_region_value='NTC_VOIP_ACCOUNT' where dict_id='16';
update function_region_dict set is_valid=0 where dict_id='208';

#文件摘要配置
update function_service_dict set service_id='399' where dict_id='59';
update function_service_dict set service_id='273' where dict_id='60';

update function_region_dict set config_region_value ='MM_FILE_DIGEST' where dict_id='50';
update function_region_dict set is_valid='0' where dict_id='187';

