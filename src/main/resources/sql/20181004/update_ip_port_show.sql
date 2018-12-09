ALTER TABLE function_region_dict MODIFY config_ip_port_show VARCHAR(10) NULL COMMENT '源ip展示、源端口展示、目的ip展示、目的端口展示1、2、3、4';
UPDATE function_region_dict SET config_ip_port_show ='1,2,3,4' WHERE config_ip_port_show ='1,2';
UPDATE function_region_dict SET config_ip_port_show ='1,2' WHERE config_ip_port_show ='1';
UPDATE function_region_dict SET config_ip_port_show ='3,4' WHERE config_ip_port_show ='2';