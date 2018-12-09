#先插入字典名字
INSERT INTO sys_data_dictionary_name (module_name,mark,remark,revision,create_time,modify_time,STATUS) VALUES ('app，基础协议是否展示业务类型','IS_SHOW_BUSINESS_TYPE','','',NOW(),NOW(),1);
#再查询字典的id
SELECT * FROM sys_data_dictionary_name WHERE mark='IS_SHOW_BUSINESS_TYPE';
#添加字典值
INSERT INTO sys_data_dictionary_item (item_code,item_value,item_desc,item_sort,STATUS,TYPE,dictionary_id) VALUES(1,'yes_no','是否',0,1,1,113)
