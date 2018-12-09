INSERT INTO sys_data_dictionary_name
   (`id`, `module_name`, `mark`, `remark`, `revision`, `create_time`, `modify_time`, `status`)
VALUES
   (116, '域名拦截强度', 'INTERCEPT_DOMAIN_INTENSITY', '', 'user:管理员,2018-10-16 18:01:23edit|user:管理员,2018-10-16 18:00:52edit|user:管理员,2018-10-15 17:52:08edit|user:管理员,2018-10-15 17:37:44edit|user:管理员,2018-10-15 15:24:55edit|user:管理员,2018-10-15 15:24:37edit', "2018/10/15 15:24:04", "2018/10/16 18:01:23", 1);

INSERT INTO sys_data_dictionary_name
   (`id`, `module_name`, `mark`, `remark`, `revision`, `create_time`, `modify_time`, `status`)
VALUES
   (117, '丢包率', 'RATE_LIMIT', '', '', "2018/10/16 18:30:43", "2018/10/16 18:30:43", 1);

INSERT INTO sys_data_dictionary_name
   (`id`, `module_name`, `mark`, `remark`, `revision`, `create_time`, `modify_time`, `status`)
VALUES
   (118, '置信度', 'CONFIDENCE_INTERVAL', '', '', "2018/10/16 19:13:15", "2018/10/16 19:13:15", 1);

INSERT INTO sys_data_dictionary_name
   (`id`, `module_name`, `mark`, `remark`, `revision`, `create_time`, `modify_time`, `status`)
VALUES
   (119, '十六进制', 'IS_HEX', '', '', "2018/10/18 11:41:24", "2018/10/18 11:41:24", 1);

INSERT INTO sys_data_dictionary_name
   (`id`, `module_name`, `mark`, `remark`, `revision`, `create_time`, `modify_time`, `status`)
VALUES
   (120, '大小写敏感', 'CASE_INSENSTIVE', '', '', "2018/10/18 11:44:58", "2018/10/18 11:44:58", 1);

   INSERT INTO sys_data_dictionary_item
   (`id`, `item_code`, `item_value`, `item_desc`, `item_sort`, `status`, `type`, `dictionary_id`)
VALUES
   (2729, '1', 'no', 'NO', 0, 1, 1, 116);

INSERT INTO sys_data_dictionary_item
   (`id`, `item_code`, `item_value`, `item_desc`, `item_sort`, `status`, `type`, `dictionary_id`)
VALUES
   (2730, '2', 'yes', 'YES', 0, 1, 1, 116);

INSERT INTO sys_data_dictionary_item
   (`id`, `item_code`, `item_value`, `item_desc`, `item_sort`, `status`, `type`, `dictionary_id`)
VALUES
   (2731, '0.001', '0.001', '', 0, 1, 1, 117);

INSERT INTO sys_data_dictionary_item
   (`id`, `item_code`, `item_value`, `item_desc`, `item_sort`, `status`, `type`, `dictionary_id`)
VALUES
   (2732, '0.002', '0.002', '', 0, 1, 1, 117);

INSERT INTO sys_data_dictionary_item
   (`id`, `item_code`, `item_value`, `item_desc`, `item_sort`, `status`, `type`, `dictionary_id`)
VALUES
   (2733, '0.003', '0.003', '', 0, 1, 1, 117);

INSERT INTO sys_data_dictionary_item
   (`id`, `item_code`, `item_value`, `item_desc`, `item_sort`, `status`, `type`, `dictionary_id`)
VALUES
   (2734, '0.004', '0.004', '', 0, 1, 1, 117);

INSERT INTO sys_data_dictionary_item
   (`id`, `item_code`, `item_value`, `item_desc`, `item_sort`, `status`, `type`, `dictionary_id`)
VALUES
   (2735, '0.005', '0.005', '', 0, 1, 1, 117);

INSERT INTO sys_data_dictionary_item
   (`id`, `item_code`, `item_value`, `item_desc`, `item_sort`, `status`, `type`, `dictionary_id`)
VALUES
   (2736, '0.006', '0.006', '', 0, 1, 1, 117);

INSERT INTO sys_data_dictionary_item
   (`id`, `item_code`, `item_value`, `item_desc`, `item_sort`, `status`, `type`, `dictionary_id`)
VALUES
   (2737, '0.007', '0.007', '', 0, 1, 1, 117);

INSERT INTO sys_data_dictionary_item
   (`id`, `item_code`, `item_value`, `item_desc`, `item_sort`, `status`, `type`, `dictionary_id`)
VALUES
   (2738, '0.008', '0.008', '', 0, 1, 1, 117);

INSERT INTO sys_data_dictionary_item
   (`id`, `item_code`, `item_value`, `item_desc`, `item_sort`, `status`, `type`, `dictionary_id`)
VALUES
   (2739, '0.009', '0.009', '', 0, 1, 1, 117);

INSERT INTO sys_data_dictionary_item
   (`id`, `item_code`, `item_value`, `item_desc`, `item_sort`, `status`, `type`, `dictionary_id`)
VALUES
   (2740, '75', '75', '', 0, 1, 1, 118);

INSERT INTO sys_data_dictionary_item
   (`id`, `item_code`, `item_value`, `item_desc`, `item_sort`, `status`, `type`, `dictionary_id`)
VALUES
   (2741, '80', '80', '', 0, 1, 1, 118);

INSERT INTO sys_data_dictionary_item
   (`id`, `item_code`, `item_value`, `item_desc`, `item_sort`, `status`, `type`, `dictionary_id`)
VALUES
   (2742, '85', '85', '', 0, 1, 1, 118);

INSERT INTO sys_data_dictionary_item
   (`id`, `item_code`, `item_value`, `item_desc`, `item_sort`, `status`, `type`, `dictionary_id`)
VALUES
   (2743, '90', '90', '', 0, 1, 1, 118);

INSERT INTO sys_data_dictionary_item
   (`id`, `item_code`, `item_value`, `item_desc`, `item_sort`, `status`, `type`, `dictionary_id`)
VALUES
   (2744, '95', '95', '', 0, 1, 1, 118);

INSERT INTO sys_data_dictionary_item
   (`id`, `item_code`, `item_value`, `item_desc`, `item_sort`, `status`, `type`, `dictionary_id`)
VALUES
   (2745, '100', '100', '', 0, 1, 1, 118);

INSERT INTO sys_data_dictionary_item
   (`id`, `item_code`, `item_value`, `item_desc`, `item_sort`, `status`, `type`, `dictionary_id`)
VALUES
   (2746, '1', 'hex', '十六进制', 0, 1, 1, 119);

INSERT INTO sys_data_dictionary_item
   (`id`, `item_code`, `item_value`, `item_desc`, `item_sort`, `status`, `type`, `dictionary_id`)
VALUES
   (2747, '0', 'not_hex', '非十六进制', 0, 1, 1, 119);

INSERT INTO sys_data_dictionary_item
   (`id`, `item_code`, `item_value`, `item_desc`, `item_sort`, `status`, `type`, `dictionary_id`)
VALUES
   (2748, '1', 'case_senstive', '敏感', 0, 1, 1, 120);

INSERT INTO sys_data_dictionary_item
   (`id`, `item_code`, `item_value`, `item_desc`, `item_sort`, `status`, `type`, `dictionary_id`)
VALUES
   (2749, '0', 'case_insenstive', '不敏感', 0, 1, 1, 120);


