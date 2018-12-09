INSERT INTO function_service_dict
   (`dict_id`, `function_id`, `protocol_id`, `action`, `action_code`, `service_id`, `service_name`, `service_desc`, `is_valid`, `creator_id`, `create_time`, `editor_id`, `edit_time`, `region_code`)
VALUES
   (116, 570, 0, 1, 'monit', 520, 'pxy_intercept_monit_keyring', '', 1, null, null, null, null, '');
   
INSERT INTO function_service_dict 
	(`function_id`, `protocol_id`, `action`, `action_code`, `service_id`, `service_name`, `service_desc`, `is_valid`, `creator_id`, `create_time`, `editor_id`, `edit_time`, `region_code`) 
VALUES 
	('510', '0', '1', 'monit', '146', 'p2p_monit', NULL, '1', NULL, NULL, NULL, NULL, '1,2,3,4');
