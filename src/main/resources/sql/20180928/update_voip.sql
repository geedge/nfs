 #voip ip跟voip account 合二为一
update sys_menu set href = "/ntc/av/voipList" where id = 831;
update function_service_dict set service_name='av_voip_monit' where dict_id=17
update function_service_dict set service_name='av_voip_reject' where dict_id=16
update function_region_dict set config_region_value='MM_SUBSCRIBE_ID',function_id=24,config_region_code=2 where dict_id=208
