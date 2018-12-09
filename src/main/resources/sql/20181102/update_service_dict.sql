#修改advanced的service_name
UPDATE `function_service_dict` SET `service_name`='website_advanced_reject' WHERE (`dict_id`='18');
UPDATE `function_service_dict` SET `service_name`='website_advanced_monit' WHERE (`dict_id`='19');
#修改APP PAYLOAD ADMIN的service_name
UPDATE `function_service_dict` SET `service_name`='APP_PAYLOAD_ADMIN' WHERE (`dict_id`='132');
#修改APP HTTP ADMIN的service_name
UPDATE `function_service_dict` SET `service_name`='APP_HTTP_ADMIN' WHERE (`dict_id`='134');
#修改APP DNS ADMIN的service_name
UPDATE `function_service_dict` SET `service_name`='APP_DNS_ADMIN' WHERE (`dict_id`='133');
#修改APP SSL ADMIN的service_name
UPDATE `function_service_dict` SET `service_name`='APP_SSL_ADMIN' WHERE (`dict_id`='135');
#修改拦截证书策略的service_name
UPDATE `function_service_dict` SET `service_name`='intercept_file_strategy' WHERE (`dict_id`='116');

#增加日志查询-原始二层封装格式字典
INSERT INTO `sys_data_dictionary_item` (`id`, `item_code`, `item_value`, `item_desc`, `item_sort`, `status`, `type`, `dictionary_id`) VALUES ('2976', '0', 'ethernet', '以太网', '0', '1', '1', '126');
INSERT INTO `sys_data_dictionary_item` (`id`, `item_code`, `item_value`, `item_desc`, `item_sort`, `status`, `type`, `dictionary_id`) VALUES ('2977', '8', 'PPP', '', '0', '1', '1', '126');
INSERT INTO `sys_data_dictionary_item` (`id`, `item_code`, `item_value`, `item_desc`, `item_sort`, `status`, `type`, `dictionary_id`) VALUES ('2978', '12', 'Cisco HDLC', '', '0', '1', '1', '126');

INSERT INTO `sys_data_dictionary_name` (`id`, `module_name`, `mark`, `remark`, `revision`, `create_time`, `modify_time`, `status`) VALUES ('126', '日志查询-原始二层封装格式', 'ENCAP_TYPE', '', '', '2018-10-31 19:18:11', '2018-10-31 19:18:11', '1');
