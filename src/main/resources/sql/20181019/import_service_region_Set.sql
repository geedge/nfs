#ip addr
update function_service_dict set is_import=1 where function_id=5 and is_valid=1 and dict_id in(23,24,25,108);
update function_region_dict set is_import=1 where function_id=5 and is_valid=1 and dict_id in(27,35,228);
#basic
update function_service_dict set is_import=1 where function_id=407 and is_valid=1 ;
update function_region_dict set is_import=1 where function_id=407 and is_valid=1  and dict_id in(107);
#tunnel
update function_service_dict set is_import=1 where function_id=408 and is_valid=1 ;
update function_region_dict set is_import=1 where function_id=408 and is_valid=1  and dict_id in(157);
#ip payload
update function_service_dict set is_import=1 where function_id=212 and is_valid=1 ;
update function_region_dict set is_import=1 where function_id=212 and is_valid=1  and dict_id in(230);
#app
update function_service_dict set is_import=1 where function_id=63 and is_valid=1 ;
update function_region_dict set is_import=1 where function_id=63 and is_valid=1  and dict_id in(78);
#dns
update function_service_dict set is_import=1 where function_id=7 and is_valid=1 ;
update function_region_dict set is_import=1 where function_id=7 and is_valid=1  and dict_id in(28,29);
#url
update function_service_dict set is_import=1 where function_id=6 and is_valid=1 ;
update function_region_dict set is_import=1 where function_id=6 and is_valid=1  ;
#url advanced
update function_service_dict set is_import=1 where function_id=8 and is_valid=1 ;
update function_region_dict set is_import=1 where function_id=8 and is_valid=1  and dict_id in(9,10,11,12,13,14);
#mail
update function_service_dict set is_import=1 where function_id=37 and is_valid=1 ;
update function_region_dict set is_import=1 where function_id=37 and is_valid=1  and dict_id in(599);
#mail advanced
update function_service_dict set is_import=1 where function_id=35 and is_valid=1 ;
update function_region_dict set is_import=1 where function_id=35 and is_valid=1  and dict_id in(30,31,33);
#ftp
update function_service_dict set is_import=1 where function_id=51 and is_valid=1 ;
update function_region_dict set is_import=1 where function_id=51 and is_valid=1  and dict_id in(36,37,38);