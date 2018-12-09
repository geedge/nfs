UPDATE function_service_dict SET region_code=1 WHERE function_id=5 AND region_code='1,3';
UPDATE function_service_dict SET region_code=2 WHERE function_id=5 AND region_code='2,3';
UPDATE function_service_dict SET is_valid=1 WHERE function_id=5 AND ACTION=1;
UPDATE function_service_dict SET is_valid=0 WHERE function_id=5 AND ACTION=96;

INSERT INTO function_service_dict (function_id,protocol_id,ACTION,action_code,service_id,service_name,service_desc,is_valid,region_code) VALUES(5,0,64,'ratelimit',1057,'ip_ratelimit','ip地址限速',1,1);

#根据PROTOCOL字典查出的值替换63
SELECT id FROM sys_data_dictionary_name WHERE mark= 'PROTOCOL';
INSERT INTO sys_data_dictionary_item (item_code,item_value,item_desc,item_sort,STATUS,TYPE,dictionary_id) VALUES(8,'IPSEC','IPSEC',0,1,1,63);
INSERT INTO sys_data_dictionary_item (item_code,item_value,item_desc,item_sort,STATUS,TYPE,dictionary_id) VALUES(15,'GRE','GRE',0,1,1,63);

UPDATE function_region_dict SET is_valid=0 WHERE function_id=5 AND config_region_value ='NTC_SUBSCRIBE_ID';
UPDATE function_region_dict SET config_protocol='0,6,17,1,8,15' WHERE function_id=5 AND config_region_value ='INLINE_IP_CB';
UPDATE function_region_dict SET config_protocol='0,6,17,1,8,15' WHERE function_id=5 AND config_region_value ='INLINE_IP_CB';
update function_region_dict set  config_ip_type='4,6' where region_type=1 and config_ip_type like '%,46,64,10%';
UPDATE function_service_dict SET region_code=4 WHERE function_id=5 AND action_code='ratelimit';
INSERT INTO function_region_dict (function_id,config_region_code,config_region_value,config_desc,is_valid,is_maat,region_type,config_ip_type,config_ip_pattern,config_port_pattern,config_direction,config_protocol,config_ip_port_show,config_region_sort)VALUES(5,4,'LIMIT_IP','IP限流',1,1,1,'4,6','1,2,3','1,2','0,1','0,6,17','1,2',4);



#asn region
INSERT INTO function_region_dict (config_service_type,function_id,config_region_code,config_region_value,config_desc,is_valid,is_maat,region_type,config_region_sort)
VALUES ('asn',5,5,'NTC_ASN_IP','asn',1,1,1,5);
# asn service
INSERT INTO function_service_dict (function_id,protocol_id,ACTION,action_code,service_id,service_name,service_desc,is_valid,region_code)
VALUES (5,0,16,'reject',37,'ans_reject','ANS_REJECT',1,5);
INSERT INTO function_service_dict (function_id,protocol_id,ACTION,action_code,service_id,service_name,service_desc,is_valid,region_code)
VALUES (5,0,1,'monit',149,'ans_monit','ANS_MONIT',1,5);