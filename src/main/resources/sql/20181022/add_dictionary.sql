#取消审核时增加来函信息 数据库增加字段
ALTER TABLE dns_ip_cfg 		   ADD cancel_request_id int(11) DEFAULT NULL COMMENT '取消审核来函';
ALTER TABLE dns_res_strategy   ADD cancel_request_id int(11) DEFAULT NULL COMMENT '取消审核来函';
ALTER TABLE ip_port_cfg        ADD cancel_request_id int(11) DEFAULT NULL COMMENT '取消审核来函';
ALTER TABLE ddos_ip_cfg        ADD cancel_request_id int(11) DEFAULT NULL COMMENT '取消审核来函';
ALTER TABLE av_file_sample_cfg ADD cancel_request_id int(11) DEFAULT NULL COMMENT '取消审核来函';
ALTER TABLE cfg_index_info     ADD cancel_request_id int(11) DEFAULT NULL COMMENT '取消审核来函';
ALTER TABLE app_policy_cfg     ADD cancel_request_id int(11) DEFAULT NULL COMMENT '取消审核来函';
ALTER TABLE pxy_obj_keyring    ADD cancel_request_id int(11) DEFAULT NULL COMMENT '取消审核来函';
ALTER TABLE file_digest_cfg    ADD cancel_request_id int(11) DEFAULT NULL COMMENT '取消审核来函';
ALTER TABLE ip_multiplex_pool_cfg    ADD cancel_request_id int(11) DEFAULT NULL COMMENT '取消审核来函';