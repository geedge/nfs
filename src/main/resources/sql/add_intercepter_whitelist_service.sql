  INSERT INTO function_service_dict
   ( `function_id`, `protocol_id`, `action`, `action_code`, `service_id`, `service_name`, `service_desc`, `is_valid`, `creator_id`, `create_time`, `editor_id`, `edit_time`)
VALUES
   ( 200, 0, 128, 'whitelist', 521, 'ip_intercepter_whitelist', '', 1,NULL , NULL,NULL , NULL);

INSERT INTO function_service_dict
   ( `function_id`, `protocol_id`, `action`, `action_code`, `service_id`, `service_name`, `service_desc`, `is_valid`, `creator_id`, `create_time`, `editor_id`, `edit_time`)
VALUES
   ( 201, 0, 128, 'whitelist', 521, 'domain_whitelist', '', 1,NULL , NULL, NULL, NULL);

 update function_service_dict set is_valid=0 where dict_id in(66,67);