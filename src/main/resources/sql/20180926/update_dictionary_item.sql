#配置是否记录日志修改
delete from sys_data_dictionary_item where dictionary_id = '103';
INSERT INTO `sys_data_dictionary_item` (`item_code`, `item_value`, `item_desc`, `item_sort`, `status`, `type`, `dictionary_id`) VALUES ('0', 'no', '否', '0', '1', '1', '103');
INSERT INTO `sys_data_dictionary_item` (`item_code`, `item_value`, `item_desc`, `item_sort`, `status`, `type`, `dictionary_id`) VALUES ('1', 'yes', '是', '0', '1', '1', '103');