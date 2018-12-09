-- 地址池
UPDATE function_service_dict SET service_id = 834 WHERE dict_id = 142 AND function_id = 633;

-- SNAT策略
UPDATE function_service_dict SET service_id = 835,`action` = 80 WHERE dict_id = 80 AND function_id = 300;

-- DNAT策略
UPDATE function_service_dict SET service_id = 836,`action` = 80 WHERE dict_id = 143 AND function_id = 304;

ALTER TABLE ip_reuse_policy_cfg ADD do_log INT(11) COMMENT '是否记录结构化日志：1是 2否';