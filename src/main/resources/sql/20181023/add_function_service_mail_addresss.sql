#更新邮件地址白名单服务id
UPDATE function_service_dict set service_id=66  where dict_id=147;

INSERT INTO function_service_dict 
	(`function_id`, `protocol_id`, `action`, `action_code`, `service_id`, `service_name`, `service_desc`, `is_valid`, `creator_id`, `create_time`, `editor_id`, `edit_time`, `region_code`,`is_import`) 
VALUES 
	('635', '4', '1', 'monit', '152', 'http_keyword_monit', NULL, '1', NULL, NULL, NULL, NULL, '1,2',1);
INSERT INTO function_service_dict 
	(`function_id`, `protocol_id`, `action`, `action_code`, `service_id`, `service_name`, `service_desc`, `is_valid`, `creator_id`, `create_time`, `editor_id`, `edit_time`, `region_code`,`is_import`) 
VALUES 
	('635', '4', '16', 'reject', '40', 'http_keyword_reject', NULL, '1', NULL, NULL, NULL, NULL, '1,2',1);
