ALTER TABLE config_group_info MODIFY COLUMN group_type INT(11) NULL COMMENT '1:app ip，2：内容，3：区域 ,4 asn';
ALTER TABLE config_group_info ADD COLUMN compile_id INT(11) NULL COMMENT 'app ip 对应的编译ID';