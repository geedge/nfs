INSERT INTO sys_data_dictionary_item(item_code,item_value,item_sort,STATUS,TYPE,dictionary_id) VALUES(521,'PXY_INTERCEPT_IP',0,1,1,89);

UPDATE function_service_dict SET service_name='basic_protocol_reject' WHERE action_code='reject' AND service_name='basic_protocol' AND function_id=407;
UPDATE function_service_dict SET service_name='basic_protocol_ratelimit' WHERE action_code='ratelimit' AND service_name='basic_protocol'  AND function_id=407;
UPDATE function_service_dict SET service_name='basic_protocol_monit' WHERE action_code='monit' AND service_name='basic_protocol'  AND function_id=407;
UPDATE function_service_dict SET service_name='basic_protocol_drop' WHERE action_code='drop' AND service_name='basic_protocol'  AND function_id=407;

UPDATE function_service_dict SET service_name='encrypted_tunnel_behavior_reject' WHERE action_code='reject' AND service_name='encrypted_tunnel_behavior' AND function_id=408;
UPDATE function_service_dict SET service_name='encrypted_tunnel_behavior_ratelimit' WHERE action_code='ratelimit' AND service_name='encrypted_tunnel_behavior'  AND function_id=408;
UPDATE function_service_dict SET service_name='encrypted_tunnel_behavior_monit' WHERE action_code='monit' AND service_name='encrypted_tunnel_behavior'  AND function_id=408;
UPDATE function_service_dict SET service_name='encrypted_tunnel_behavior_drop' WHERE action_code='drop' AND service_name='encrypted_tunnel_behavior'  AND function_id=408;