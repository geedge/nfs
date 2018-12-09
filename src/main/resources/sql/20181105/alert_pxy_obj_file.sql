ALTER TABLE pxy_obj_keyring ADD  cn varchar(400) DEFAULT null COMMENT 'CN信息';
ALTER TABLE pxy_obj_keyring ADD  alt_name varchar(4000) DEFAULT null COMMENT 'SAN信息';
alter table pxy_obj_keyring modify column not_before_time varchar(40);
alter table pxy_obj_keyring modify column not_after_time varchar(40);