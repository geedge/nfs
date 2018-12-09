#日志查询-串联设备链路号字典添加
INSERT INTO `sys_data_dictionary_name` (`id`, `module_name`, `mark`, `remark`, `revision`, `create_time`, `modify_time`, `status`) VALUES ('147', '串联设备链路号', 'LINK', '串联设备链路号', 'user:管理员,2018-12-03 18:39:24edit', '2018-12-03 16:13:24', '2018-12-03 18:39:24', '1');


#日志查询-出入口字典添加
INSERT INTO `sys_data_dictionary_item` (`id`, `item_code`, `item_value`, `item_desc`, `item_sort`, `status`, `type`, `dictionary_id`) VALUES ('3168', '1', 'astana', 'astana', '0', '1', '1', '81');
INSERT INTO `sys_data_dictionary_item` (`id`, `item_code`, `item_value`, `item_desc`, `item_sort`, `status`, `type`, `dictionary_id`) VALUES ('3169', '2', 'almaty', 'almaty', '0', '1', '1', '81');

#VPN服务地址-业务字典数据类型添加
INSERT INTO `sys_data_dictionary_item` (`id`, `item_code`, `item_value`, `item_desc`, `item_sort`, `status`, `type`, `dictionary_id`) VALUES ('3163', '5', 'vpn_ip', '数据类型：VPN 服务器IP', '0', '1', '1', '37');


