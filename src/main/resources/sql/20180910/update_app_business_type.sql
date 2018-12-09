#删除旧的字典
DELETE FROM sys_data_dictionary_item WHERE dictionary_id=(SELECT id FROM sys_data_dictionary_name WHERE mark='APP_BUSINESS_TYPE');
#添加新的字典 dictionary_id需要根据查询结果替换
INSERT INTO sys_data_dictionary_item (item_code,item_value,item_desc,item_sort,STATUS,TYPE,dictionary_id) VALUES(2,'IM','通信',0,1,1,105);
INSERT INTO sys_data_dictionary_item (item_code,item_value,item_desc,item_sort,STATUS,TYPE,dictionary_id) VALUES(4,'Web','web',0,1,1,105);
INSERT INTO sys_data_dictionary_item (item_code,item_value,item_desc,item_sort,STATUS,TYPE,dictionary_id) VALUES(10,'Media','多媒体如youtube',0,1,1,105);
INSERT INTO sys_data_dictionary_item (item_code,item_value,item_desc,item_sort,STATUS,TYPE,dictionary_id) VALUES(14,'SocilNetwork','社交网络',0,1,1,105);
INSERT INTO sys_data_dictionary_item (item_code,item_value,item_desc,item_sort,STATUS,TYPE,dictionary_id) VALUES(15,'ApplicationMarket','软件更新，应用商城',0,1,1,105);
INSERT INTO sys_data_dictionary_item (item_code,item_value,item_desc,item_sort,STATUS,TYPE,dictionary_id) VALUES(17,'Browser','浏览器',0,1,1,105);
INSERT INTO sys_data_dictionary_item (item_code,item_value,item_desc,item_sort,STATUS,TYPE,dictionary_id) VALUES(20,'VPN','',0,1,1,105);
INSERT INTO sys_data_dictionary_item (item_code,item_value,item_desc,item_sort,STATUS,TYPE,dictionary_id) VALUES(268435455,'Unspecified','',0,1,1,105);
#删除旧的字典
DELETE FROM sys_data_dictionary_item WHERE dictionary_id=(SELECT id FROM sys_data_dictionary_name WHERE mark='BASIC_PROTOCOL_BUSINESS_TYPE');
#添加新的字典 dictionary_id需要根据查询结果替换
INSERT INTO sys_data_dictionary_item (item_code,item_value,item_desc,item_sort,STATUS,TYPE,dictionary_id) VALUES(101,'Communication traffic','通信流量',0,1,1,106);
INSERT INTO sys_data_dictionary_item (item_code,item_value,item_desc,item_sort,STATUS,TYPE,dictionary_id) VALUES(102,'EncryptedTraffic','加密流量',0,1,1,106);
INSERT INTO sys_data_dictionary_item (item_code,item_value,item_desc,item_sort,STATUS,TYPE,dictionary_id) VALUES(103,'Tunnel Traffic','隧道流量',0,1,1,106);
INSERT INTO sys_data_dictionary_item (item_code,item_value,item_desc,item_sort,STATUS,TYPE,dictionary_id) VALUES(104,'Web','web',0,1,1,106);
INSERT INTO sys_data_dictionary_item (item_code,item_value,item_desc,item_sort,STATUS,TYPE,dictionary_id) VALUES(105,'DataTransfer','数据传输 如迅雷',0,1,1,106);
INSERT INTO sys_data_dictionary_item (item_code,item_value,item_desc,item_sort,STATUS,TYPE,dictionary_id) VALUES(106,'Email','邮箱',0,1,1,106);
INSERT INTO sys_data_dictionary_item (item_code,item_value,item_desc,item_sort,STATUS,TYPE,dictionary_id) VALUES(107,'Network','网络控制 代理',0,1,1,106);
INSERT INTO sys_data_dictionary_item (item_code,item_value,item_desc,item_sort,STATUS,TYPE,dictionary_id) VALUES(108,'Procy','',0,1,1,106);
INSERT INTO sys_data_dictionary_item (item_code,item_value,item_desc,item_sort,STATUS,TYPE,dictionary_id) VALUES(109,'RemoteAccess','teamviewer',0,1,1,106);
INSERT INTO sys_data_dictionary_item (item_code,item_value,item_desc,item_sort,STATUS,TYPE,dictionary_id) VALUES(268435455,'Unspecified','',0,1,1,106);