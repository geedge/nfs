INSERT INTO sys_data_dictionary_name
   (`id`, `module_name`, `mark`, `remark`, `revision`, `create_time`, `modify_time`, `status`)
VALUES
   (127, 'ANTIDDOS协议', 'ANTIDDOS_PROTOCOL', '', '', "2018/11/1 10:34:00", "2018/11/1 10:34:00", 1);
INSERT INTO sys_data_dictionary_item
   (`id`, `item_code`, `item_value`, `item_desc`, `item_sort`, `status`, `type`, `dictionary_id`)
VALUES
   (2979, 'TCP_SYN', 'TCP_SYN', 'TCP_SYN', 0, 1, 1, 127);

INSERT INTO sys_data_dictionary_item
   (`id`, `item_code`, `item_value`, `item_desc`, `item_sort`, `status`, `type`, `dictionary_id`)
VALUES
   (2980, 'UDP_DNS', 'UDP_DNS', 'UDP_DNS', 0, 1, 1, 127);

INSERT INTO sys_data_dictionary_item
   (`id`, `item_code`, `item_value`, `item_desc`, `item_sort`, `status`, `type`, `dictionary_id`)
VALUES
   (2981, 'UDP_NTP', 'UDP_NTP', 'UDP_NTP', 0, 1, 1, 127);
