alter table request_info modify column request_title VARCHAR(512) not NULL COMMENT 'request_number字段与request_title互换使用，request_title当来函函号使用';
alter table request_info modify column request_number VARCHAR(512)  COMMENT 'request_number字段与request_title互换使用，request_number当来函标题使用';
