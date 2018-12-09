#add app payload and app dns
INSERT INTO `function_service_dict` (`dict_id`, `function_id`, `protocol_id`, `action`, `action_code`, `service_id`, `service_name`, `service_desc`, `is_valid`, `creator_id`, `create_time`, `editor_id`, `edit_time`, `region_code`) VALUES ('127', '561', '0', '1', 'monit', '1025', 'app_payload', NULL, '1', '1', NULL, NULL, NULL, '1');
INSERT INTO `function_service_dict` (`dict_id`, `function_id`, `protocol_id`, `action`, `action_code`, `service_id`, `service_name`, `service_desc`, `is_valid`, `creator_id`, `create_time`, `editor_id`, `edit_time`, `region_code`) VALUES ('129', '562', '0', '1', 'monit', '1027', 'app_dns', NULL, '1', NULL, NULL, NULL, NULL, '1');
INSERT INTO `function_region_dict` (`config_service_type`, `config_multi_keywords`, `config_expr_type`, `config_hex`, `config_match_method`, `dict_id`, `function_id`, `config_region_code`, `config_region_value`, `config_district`, `config_desc`, `is_valid`, `creator_id`, `create_time`, `editor_id`, `edit_time`, `is_maat`, `region_type`, `config_ip_type`, `config_ip_pattern`, `config_port_pattern`, `config_direction`, `config_protocol`, `config_ip_port_show`, `config_region_sort`) VALUES ('', '0', '0,1,2', '0,1', '0,1,2,3', '203', '561', '1', 'APP_PAYLOAD', 'C2S_session_size,S2C_session_size,L2_header,L3_header', 'APP载荷特征', '1', NULL, NULL, NULL, NULL, '1', '3', NULL, NULL, NULL, NULL, NULL, NULL, '1');
INSERT INTO `function_region_dict` (`config_service_type`, `config_multi_keywords`, `config_expr_type`, `config_hex`, `config_match_method`, `dict_id`, `function_id`, `config_region_code`, `config_region_value`, `config_district`, `config_desc`, `is_valid`, `creator_id`, `create_time`, `editor_id`, `edit_time`, `is_maat`, `region_type`, `config_ip_type`, `config_ip_pattern`, `config_port_pattern`, `config_direction`, `config_protocol`, `config_ip_port_show`, `config_region_sort`) VALUES ('', '0', '0,1,2', '0,1', '0,1,2,3', '204', '562', '1', 'APP_DNS', 'DNS_Name', 'APP DNS域名', '1', NULL, '0000-00-00 00:00:00', NULL, '0000-00-00 00:00:00', '1', '3', '', '', '', '', '', '', '1');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `code`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `menu_bg`, `quick_action`, `is_top`, `function_id`) VALUES ('1075', '735', '0,1,86,717,735,', 'APP_PAYLOAD', 'APP PAYLOAD', '260', '/app/headerCfgList', '', '', '1', 'app:header:config', '1', '2018-09-16 21:40:49', '1', '2018-09-16 21:40:49', '', '1', NULL, '0', '0', '561');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `code`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `menu_bg`, `quick_action`, `is_top`, `function_id`) VALUES ('1072', '751', '0,1,150,750,751,', 'APP_PAYLOAD', 'APP PAYLOAD', '270', '/app/headerCfgList', '', '', '1', 'app:header:confirm', '1', '2018-09-16 21:43:11', '1', '2018-09-16 21:43:11', '', '1', NULL, '0', '0', '561');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `code`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `menu_bg`, `quick_action`, `is_top`, `function_id`) VALUES ('1073', '751', '0,1,150,750,751,', 'APP_DNS', 'APP DNS', '300', '/app/headerCfgList', '', '', '1', 'app:header:confirm', '1', '2018-09-16 21:43:42', '1', '2018-09-16 21:54:08', '', '1', NULL, '0', '0', '562');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `code`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `menu_bg`, `quick_action`, `is_top`, `function_id`) VALUES ('1074', '735', '0,1,86,717,735,', 'APP_DNS', 'APP DNS', '290', '/app/headerCfgList', '', '', '1', 'app:header:config', '1', '2018-09-16 21:54:57', '1', '2018-09-16 21:54:57', '', '1', NULL, '0', '0', '562');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `code`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `menu_bg`, `quick_action`, `is_top`, `function_id`) VALUES ('1076', '866', '0,1,86,717,866,', 'APP_PAYLOAD', 'APP PAYLOAD', '260', '/app/headerCfgList', '', '', '1', 'app:header:audit', '1', '2018-09-16 21:40:49', '1', '2018-09-16 21:40:49', '', '1', NULL, '0', '0', '561');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `code`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `menu_bg`, `quick_action`, `is_top`, `function_id`) VALUES ('1077', '866', '0,1,86,717,866,', 'APP_DNS', 'APP DNS', '290', '/app/headerCfgList', '', '', '1', 'app:header:audit', '1', '2018-09-16 21:54:57', '1', '2018-09-16 21:54:57', '', '1', NULL, '0', '0', '562');

#delete 550 app tcp session config
update function_region_dict set is_valid=0 where function_id=550;
update function_service_dict set is_valid=0 where function_id=550;
update sys_menu set del_flag=0 where function_id=550;
update statistics_tables set is_valid=0 where id=44;
#delete 404 app byte config
update function_region_dict set is_valid=0 where function_id=404;
update function_service_dict set is_valid=0 where function_id=404;
update sys_menu set del_flag=0 where function_id=404;
update statistics_tables set is_valid=0 where id=1;
#delete 530 app header config
update function_region_dict set is_valid=0 where function_id=530;
update function_service_dict set is_valid=0 where function_id=530;
update sys_menu set del_flag=0 where function_id=530;
update statistics_tables set is_valid=0 where id=42;

#update app ssl regionValue
update function_region_dict set config_region_value=APP_SSL where function_id=520;
#update app ssl old value
update app_ssl_cert_cfg set cfg_type='APP_SSL' where service_id=1029;

#update ssl disctrict new info
DELETE from sys_data_dictionary_item where dictionary_id=110;
INSERT INTO sys_data_dictionary_item
   ()
VALUES
   (2204, '1', 'SSL_SNI', 'SSL_SNI', 0, 1, 1, 110);

INSERT INTO sys_data_dictionary_item
   ()
VALUES
   (2205, '2', 'SSL_FP', 'SSL_FP', 0, 0, 0, 110);

INSERT INTO sys_data_dictionary_item
   ()
VALUES
   (2206, '3', 'serialNumber', 'serialNumber', 0, 1, 1, 110);

INSERT INTO sys_data_dictionary_item
   ()
VALUES
   (2207, '4', 'issuer_commonName', 'issuer_commonName', 0, 1, 1, 110);

INSERT INTO sys_data_dictionary_item
   ()
VALUES
   (2208, '5', 'issuer_organizationName', 'issuer_organizationName', 0, 1, 1, 110);

INSERT INTO sys_data_dictionary_item
   ()
VALUES
   (2209, '6', 'issuer_countryName', 'issuer_countryName', 0, 1, 1, 110);

INSERT INTO sys_data_dictionary_item
   ()
VALUES
   (2210, '7', 'subject_commonName', 'subject_commonName', 0, 1, 1, 110);

INSERT INTO sys_data_dictionary_item
   ()
VALUES
   (2211, '8', 'subject_organizationName', 'subject_organizationName', 0, 1, 1, 110);

INSERT INTO sys_data_dictionary_item
   ()
VALUES
   (2212, '9', 'subject_countryName', 'subject_countryName', 0, 1, 1, 110);

INSERT INTO sys_data_dictionary_item
   ()
VALUES
   (2213, '10', 'notBefore', 'notBefore', 0, 1, 1, 110);

INSERT INTO sys_data_dictionary_item
   ()
VALUES
   (2214, '11', 'notAfter', 'notAfter', 0, 1, 1, 110);

INSERT INTO sys_data_dictionary_item
   ()
VALUES
   (2215, '12', 'Algorithm_Id', 'Algorithm_Id', 0, 1, 1, 110);



