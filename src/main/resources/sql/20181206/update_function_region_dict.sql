UPDATE function_region_dict SET config_ip_pattern = '1,3' WHERE dict_id = 68 AND function_id = 300;

ALTER TABLE ip_reuse_policy_cfg ADD ip_type INT(11) DEFAULT NULL COMMENT 'IPV4=4,IPV6=6';
ALTER TABLE ip_reuse_policy_cfg ADD ip_pattern INT(11) DEFAULT NULL;
ALTER TABLE ip_reuse_policy_cfg ADD src_ip_address VARCHAR(128) DEFAULT NULL;