#修改asn ip cfg 的asn id所在列 user_region1的大小,以便能在其上创建索引
ALTER TABLE asn_ip_cfg MODIFY COLUMN user_region1 VARCHAR(20) DEFAULT NULL;