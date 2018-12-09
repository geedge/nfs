ALTER TABLE function_region_dict ADD config_ip_port_show VARCHAR(10) NULL COMMENT '1,展示源IP/端口2;展示目目的IP/端口;使用逗号分隔';
ALTER TABLE function_region_dict ADD config_ip_type VARCHAR(20) NULL COMMENT 'ip的类型，有4,6,46,64,10几种类型,使用逗号分隔';
ALTER TABLE function_region_dict ADD config_ip_pattern VARCHAR(10) NULL COMMENT 'ip的格式 1:ip掩码;2:IP范围;3:IP;使用逗号分隔';
ALTER TABLE function_region_dict ADD config_port_pattern VARCHAR(10) NULL COMMENT '端口的格式，1:port;2:port_mask;使用逗号分隔';
ALTER TABLE function_region_dict ADD config_direction VARCHAR(10) NULL COMMENT 'IP方向0,双向;1,单向.使用逗号分隔';
ALTER TABLE function_service_dict ADD region_code VARCHAR(20) NULL COMMENT 'function_region_dict 表中的config_region_code字段值';
ALTER TABLE function_region_dict ADD config_protocol VARCHAR(20) NULL COMMENT '协议 0:任意;6:TCP;17:UDP;1:ICMP;50:ESP;51:AH';
