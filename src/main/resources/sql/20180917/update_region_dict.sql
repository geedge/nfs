#update account and DNS match_method=0,1,2,3
update function_region_dict set config_match_method='0,1,2,3' where dict_id in (16,28);
