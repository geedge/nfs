UPDATE 
	specific_service_cfg c
SET 
	c.spec_service_code = c.spec_service_code + 10000
WHERE
	c.cfg_type IN (2, 3) AND c.parent_id = 0;