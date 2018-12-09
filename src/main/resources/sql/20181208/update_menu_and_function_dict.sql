#修改协议的code为国际化key
update sys_menu set code='protocol' where id in (931,934,937,1131,1134);
#删除APP HTTP Feature APP SSL Cert Feature APP DK GL Admin业务
update sys_menu set is_show=0,del_flag=0  where function_id in (402,520,567);
UPDATE function_region_dict set is_valid=0 where function_id in (402,520,567);
update function_service_dict  set is_valid=0 where function_id in (402,520,567);
#APP的http admin，ssladmin 及payload admin业务变更district
update function_region_dict set config_district='Host,URI,User-Agent,Content-Type,Content-Encoding,Refer,Cookie,Set-cookie' where dict_id in(220,221,222);
update function_region_dict set config_district='SSL_SNI,SSL_FP,serilNumber,issuer_commonName,issuer_organizationName,issuer_countryName,subject_commonName,subject_organizationName,subject_countryName,notBefore,notAfter,Algorithm_id' where dict_id in(224,225,226);
update function_region_dict set config_district='Payload,L2_header,L3_header,C2S_session_size,S2C_session_size' where dict_id in(212,213,215);