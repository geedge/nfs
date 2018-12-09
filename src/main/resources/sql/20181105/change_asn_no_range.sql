ALTER TABLE config_group_info MODIFY COLUMN asn_id BIGINT(20) NULL COMMENT 'asn 组对应的asn号';
ALTER TABLE policy_group_info MODIFY COLUMN asn_no BIGINT(20) NULL COMMENT 'asn号';