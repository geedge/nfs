UPDATE 
	function_region_dict
SET 
	config_region_value = 'IR_STATIC_NOMINEE_IP',
	config_desc = 'SNAT地址池管理',
	config_ip_port_show = '1'
WHERE
	function_id = 633 AND dict_id = 231;

UPDATE
	function_region_dict
SET
	config_region_value = 'IR_STATIC_NOMINEE_IP',
	config_desc = 'DNAT地址池管理'
WHERE
	function_id = 406 AND dict_id = 75;