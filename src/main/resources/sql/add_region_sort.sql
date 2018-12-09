ALTER TABLE function_region_dict ADD config_region_sort INT(11)  COMMENT 'region排序';
UPDATE function_region_dict SET config_region_sort=1 WHERE config_region_code=1;
UPDATE function_region_dict SET config_region_sort=2 WHERE config_region_code=2;
UPDATE function_region_dict SET config_region_sort=3 WHERE config_region_code=3;
UPDATE function_region_dict SET config_region_sort=4 WHERE config_region_code=4;
UPDATE function_region_dict SET config_region_sort=5 WHERE config_region_code=5;
UPDATE function_region_dict SET config_region_sort=6 WHERE config_region_code=6;
UPDATE function_region_dict SET config_region_sort=7 WHERE config_region_code=7;
