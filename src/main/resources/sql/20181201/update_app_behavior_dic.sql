#APP_POLICY界面上的隧道类型multimedia video编码从1变为90001，other编码从6变为90006 
update specific_service_cfg a set a.spec_service_code=90006 where a.spec_service_code=6 and a.cfg_type=2 and a.spec_service_name='other';
update specific_service_cfg a set a.spec_service_code=90001 where a.spec_service_code=1 and a.cfg_type=2 and a.spec_service_name='multimedia_video';
#Tor mode、Tor broswer、Opera Turbo、Opera mini的行为类型见“行为类型” sheet
update specific_service_cfg set parent_id=36,is_leaf=1 where spec_service_id in(35,283);
update specific_service_cfg set parent_id=47,is_leaf=1 where spec_service_id in(45,46);
