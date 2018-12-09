#ip白名单导入
update function_region_dict set is_import=1 where function_id=3 and dict_id=7;
UPDATE function_service_dict SET is_import=1 WHERE function_id=3 AND dict_id=15;
#bgp导入
update function_region_dict set is_import=1 where function_id=61 and dict_id=48;
update function_region_dict set is_import=1 where function_id=61 and dict_id=168;
UPDATE function_service_dict SET is_import=1 WHERE function_id=61 ;
#ssl 导入
UPDATE function_region_dict SET is_import=1 WHERE function_id=34 and  dict_id in(20,21,22,23);
UPDATE function_service_dict SET is_import=1 WHERE function_id=34;
#网页关键字导入
update function_region_dict set is_import=1 where function_id=635;
#邮件地址
UPDATE function_region_dict SET is_import=1 WHERE function_id=37 AND dict_id=599;
UPDATE function_region_dict SET is_import=0 WHERE function_id=37 AND dict_id=600;

#http 高级
UPDATE function_region_dict SET is_import=1 WHERE function_id=8 AND  dict_id IN(9,10,11,12,13);
#流媒体
UPDATE function_region_dict SET is_import=1 WHERE function_id=22 AND  dict_id IN(25,597);
#DDOS IP
UPDATE function_region_dict SET is_import=1,config_ip_port_show='3,4' WHERE function_id=301 AND  dict_id IN(74);
UPDATE function_service_dict SET is_import=1 WHERE function_id=301;
#SNAT 导入先不做
UPDATE function_region_dict SET is_import=0,region_type=1,config_ip_port_show='3' WHERE function_id=633;
UPDATE function_service_dict SET is_import=0 WHERE function_id=633;
#DNS欺骗IP
UPDATE function_region_dict SET is_import=1 WHERE function_id=401;
UPDATE function_service_dict SET is_import=1 WHERE function_id=401;
#DNS策略
UPDATE function_region_dict SET is_import=1 WHERE function_id=400;
UPDATE function_service_dict SET is_import=1 WHERE function_id=400;
#app ip
UPDATE function_region_dict SET is_import=1 WHERE function_id=405;
UPDATE function_service_dict SET is_import=1 WHERE function_id=405;
#app http
UPDATE function_region_dict SET is_import=1 WHERE function_id=402 AND is_valid=1;
UPDATE function_service_dict SET is_import=1 WHERE function_id=402 AND is_valid=1;