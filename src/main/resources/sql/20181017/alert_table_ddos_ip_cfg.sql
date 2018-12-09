ALTER TABLE ddos_ip_cfg ADD  antiddos_protocol varchar(255) DEFAULT NULL COMMENT '目前支持TCP_SYN, DNS, NTP，单选。';
ALTER TABLE ddos_ip_cfg ADD  bps_threadshold bigint(20) DEFAULT NULL COMMENT ' 即DDoS攻击保护动作触发阈值，每秒Bit数';
ALTER TABLE ddos_ip_cfg ADD  pps_threadshold bigint(20) DEFAULT NULL COMMENT ' 即DDoS攻击保护动作触发阈值，每秒包数，';