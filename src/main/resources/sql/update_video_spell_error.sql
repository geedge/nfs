update function_region_dict t set t.config_region_value=insert(t.config_region_value,POSITION('vedio' in t.config_region_value),5,'video') where t.config_region_value like '%vedio%';
update sys_menu s set s.`code`=insert(s.`code`,POSITION('vedio' in s.`code`),5,'video') where s.`code` like '%vedio%';
