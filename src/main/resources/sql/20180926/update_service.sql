#update national code
update function_service_dict set service_name='APP_PAYLOAD' where dict_id in (127,132);
update function_service_dict set service_name='APP_DNS' where dict_id in (129,133);