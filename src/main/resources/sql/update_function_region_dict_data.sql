#ip地址白名单
INSERT INTO function_region_dict (config_service_type,config_multi_keywords,config_expr_type,config_hex,config_match_method
,function_id,config_region_code,config_region_value,config_desc,is_valid,is_maat,region_type,config_region_sort)
VALUES('subscribe_id',0,'0,1','0,1,2','0,1,2,3',3,2,'WHITE_LIST_SUBSCRIBE_ID','全局IP白名单SUBSCRIBE配置',1,1,2,2);
UPDATE function_service_dict SET region_code='1,2' WHERE function_id=3 AND ACTION=128
UPDATE function_region_dict SET config_ip_port_show='1,2' ,config_ip_type='4,6',config_ip_pattern='1,2,3',config_port_pattern='1,2',config_direction='0',config_protocol='0' WHERE dict_id=7;
#域名白名单
INSERT INTO function_region_dict (config_service_type,config_multi_keywords,config_expr_type,config_hex,config_match_method
,function_id,config_region_code,config_region_value,config_desc,is_valid,is_maat,region_type,config_region_sort)
VALUES('subscribe_id',0,'0,1','0,1,2','0,1,2,3',4,2,'WHITE_LIST_SUBSCRIBE_ID','全局域名白名单SUBSCRIBE配置',1,1,2,2);
UPDATE function_service_dict SET region_code='1,2' WHERE function_id=4 AND ACTION=128
#ip地址
INSERT INTO function_region_dict (config_service_type,config_multi_keywords,config_expr_type,config_hex,config_match_method
,function_id,config_region_code,config_region_value,config_desc,is_valid,is_maat,region_type,config_region_sort)
VALUES('subscribe_id',0,'0,1','0,1,2','0,1,2,3',5,3,'NTC_SUBSCRIBE_ID','IP地址SUBSCRIBE配置',1,1,2,2);
UPDATE function_service_dict SET region_code='1,3' WHERE function_id=5 AND ACTION IN(16,1);
UPDATE function_service_dict SET region_code='2,3' WHERE function_id=5 AND ACTION IN(32,96);
UPDATE function_region_dict SET config_ip_port_show='1,2' ,config_ip_type='4,6,46,64,10',config_ip_pattern='1,2,3',config_port_pattern='1,2',config_direction='0,1',config_protocol='0,6,17' WHERE dict_id=27;
UPDATE function_region_dict SET config_service_type='ipaddr',config_ip_port_show='1,2' ,config_ip_type='4,6,46,64,10',config_ip_pattern='1,2,3',config_port_pattern='1,2',config_direction='0,1',config_protocol='0,6,17,1' WHERE dict_id=35;
#ip限流
UPDATE function_service_dict SET region_code=1 WHERE function_id=302 AND ACTION IN(64);
UPDATE function_region_dict SET config_service_type='ratelimit',config_ip_port_show='1,2' ,config_ip_type='4,6,46,64,10',config_ip_pattern='1,2,3',config_port_pattern='1,2',config_direction='0,1',config_protocol='0,6,17' WHERE dict_id=67;
#IP复用策略
UPDATE function_service_dict SET region_code=1 WHERE function_id=300 AND ACTION IN(96);
UPDATE function_region_dict SET config_service_type='ipmulitiplex',config_ip_port_show='1' ,config_ip_type='4,6',config_ip_pattern='1,2,3',config_port_pattern='1,2',config_direction='0,1',config_protocol='0,6,17' WHERE dict_id=68;
#IP拦截
UPDATE function_service_dict SET region_code=1 WHERE function_id=200 AND ACTION IN(1,128,64,80);
UPDATE function_region_dict SET config_service_type='ratelimit',config_ip_port_show='1,2' ,config_ip_type='4,6,46,64,10',config_ip_pattern='1,2,3',config_port_pattern='1,2',config_direction='0',config_protocol='0' WHERE dict_id=53;
#音视频IP
UPDATE function_service_dict SET region_code=2 WHERE function_id=20 AND ACTION IN(1,16);
UPDATE function_region_dict SET config_ip_port_show='1,2' ,config_ip_type='4,6,46,64,10',config_ip_pattern='1,2,3',config_port_pattern='1,2',config_direction='0,1',config_protocol='0,6,17' WHERE dict_id=17;
#图片IP
UPDATE function_service_dict SET region_code=1 WHERE function_id=21 AND ACTION IN(1,16);
UPDATE function_region_dict SET config_ip_port_show='1,2' ,config_ip_type='4,6,46,64,10',config_ip_pattern='1,2,3',config_port_pattern='1,2',config_direction='0,1',config_protocol='0,6,17' WHERE dict_id=24;
#APP IP
UPDATE function_service_dict SET region_code=1 WHERE function_id=405 AND ACTION IN(1);
UPDATE function_region_dict SET config_ip_port_show='1,2' ,config_ip_type='4,6',config_ip_pattern='1,2,3',config_port_pattern='1,2',config_direction='0',config_protocol='0,6,17' WHERE dict_id=62;
#http ip
UPDATE function_service_dict SET region_code='1,2,3,4,5,6' WHERE function_id=8 AND ACTION IN(16,1);
UPDATE function_region_dict SET config_ip_port_show='1,2' ,config_ip_type='4,6,46,64,10',config_ip_pattern='1,2,3',config_port_pattern='1,2',config_direction='0,1',config_protocol='0,6,17' WHERE dict_id=14;
#ssl ip
UPDATE function_service_dict SET region_code='1,2,3,4' WHERE function_id=34 AND ACTION IN(16,1);
UPDATE function_region_dict SET config_ip_port_show='1,2' ,config_ip_type='4,6,46,64,10',config_ip_pattern='1,2,3',config_port_pattern='1,2',config_direction='0,1',config_protocol='0,6,17' WHERE dict_id=23;
# mail ip
UPDATE function_service_dict SET region_code='1,2,3,4,5,6' WHERE function_id=35 AND ACTION IN(16,1);
UPDATE function_region_dict SET config_ip_port_show='1,2' ,config_ip_type='4,6,46,64,10',config_ip_pattern='1,2,3',config_port_pattern='1,2',config_direction='0,1',config_protocol='0,6,17' WHERE dict_id=33;
# ddos ip
UPDATE function_service_dict SET region_code='1,2,3,4,5,6' WHERE function_id=301 AND ACTION IN(32);
UPDATE function_region_dict SET config_ip_port_show='2' ,config_ip_type='4,6,46,64,10',config_ip_pattern='1,2,3',config_port_pattern='1,2',config_direction='0,1',config_protocol='0,6,17' WHERE dict_id=74;
# ftp ip
UPDATE function_service_dict SET region_code='1,2,3' WHERE function_id=51 AND ACTION IN(1,16);
UPDATE function_region_dict SET config_ip_port_show='1,2' ,config_ip_type='4,6,46,64,10',config_ip_pattern='1,2,3',config_port_pattern='1,2',config_direction='0,1',config_protocol='0,6,17' WHERE dict_id=38;
# pxy control
UPDATE function_service_dict SET region_code='1,2,3,4,5,6' WHERE function_id=207 AND ACTION IN(16);
UPDATE function_region_dict SET config_ip_port_show='1,2' ,config_ip_type='4,6,46,64,10',config_ip_pattern='1,2,3',config_port_pattern='1,2',config_direction='0',config_protocol='0' WHERE dict_id=84;
#pxy redirect
UPDATE function_service_dict SET region_code='1,2,3,4,5,6' WHERE function_id=208 AND ACTION IN(48);
UPDATE function_region_dict SET config_ip_port_show='1,2' ,config_ip_type='4,6,46,64,10',config_ip_pattern='1,2,3',config_port_pattern='1,2',config_direction='0',config_protocol='0' WHERE dict_id=90;
#pxy replace
UPDATE function_service_dict SET region_code='1,2,3,4,5,6' WHERE function_id=209 AND ACTION IN(80);
UPDATE function_region_dict SET config_ip_port_show='1,2' ,config_ip_type='4,6,46,64,10',config_ip_pattern='1,2,3',config_port_pattern='1,2',config_direction='0',config_protocol='0' WHERE dict_id=96;
#pxy monit
UPDATE function_service_dict SET region_code='1,2,3,4,5,6' WHERE function_id=210 AND ACTION IN(1);
UPDATE function_region_dict SET config_ip_port_show='1,2' ,config_ip_type='4,6,46,64,10',config_ip_pattern='1,2,3',config_port_pattern='1,2',config_direction='0',config_protocol='0' WHERE dict_id=105;
#pxy whitelist
UPDATE function_service_dict SET region_code='1,2,3' WHERE function_id=211 AND ACTION IN(128);
UPDATE function_region_dict SET config_ip_port_show='1,2' ,config_ip_type='4,6,46,64,10',config_ip_pattern='1,2,3',config_port_pattern='1,2',config_direction='0',config_protocol='0' WHERE dict_id=101;
#voip ip
UPDATE function_service_dict SET region_code='1,2' WHERE function_id=24 AND ACTION IN(1,16);
UPDATE function_region_dict SET config_ip_port_show='1,2' ,config_ip_type='4,6,46,64,10',config_ip_pattern='1,2,3',config_port_pattern='1,2',config_direction='0,1',config_protocol='0,6,17' WHERE dict_id=8;
#asn ip
INSERT INTO function_region_dict 
(function_id,config_region_code,config_region_value,config_desc,is_maat,region_type,config_ip_type,config_ip_pattern,config_port_pattern,config_direction,config_protocol,config_ip_port_show,config_region_sort,is_valid) VALUE 
(600,1,'NTC_ASN_IP','asn ip池',1,1,'4,6','1,2,3','1,2','0','0','2',1,1)
#asn 策略
INSERT INTO function_region_dict 
(function_id,config_region_code,config_region_value,config_desc,is_maat,region_type,config_region_sort,is_valid) VALUE 
(601,1,'NTC_ASN_IP','asn策略',1,1,1,1)
INSERT INTO	function_service_dict (function_id,protocol_id,ACTION,action_code,service_id,service_name,service_desc,is_valid,region_code)
VALUES(601,0,1,'monit',149,'ANS_POLICY_REJECT','ANS_POLICY_REJECT',1,1)
INSERT INTO	function_service_dict (function_id,protocol_id,ACTION,action_code,service_id,service_name,service_desc,is_valid,region_code)
VALUES(601,0,16,'reject',37,'ANS_POLICY_REJECT','ANS_POLICY_REJECT',1,1)

