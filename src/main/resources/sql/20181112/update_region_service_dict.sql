#修改ddos动作为回流，配置表为NTC_DDOS_PROTECT_TARGET_IP
UPDATE function_region_dict SET config_region_value='NTC_DDOS_PROTECT_TARGET_IP' WHERE dict_id=74;
UPDATE function_service_dict SET `action`=96 , action_code='loop' WHERE dict_id=82;
