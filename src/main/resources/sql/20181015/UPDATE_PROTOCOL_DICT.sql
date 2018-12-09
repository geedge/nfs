INSERT INTO sys_data_dictionary_item VALUES(2725,50,'ESP','ESP',0,1,1,63),(2726,51,'AH','AH',0,1,1,63)

UPDATE function_region_dict SET config_protocol='6,17,1,15,50,51' WHERE function_id=5 AND config_region_value='INLINE_IP_CB';