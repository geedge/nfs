ALTER TABLE function_region_dict ADD  is_import int(2) DEFAULT 0 COMMENT '域是否是否支持导出';
ALTER TABLE function_service_dict ADD  is_import int(2) DEFAULT 0 COMMENT '业务是否支持导出';