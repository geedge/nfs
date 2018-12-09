#修改Proxy 控制策略中IP协议类型
update function_region_dict set config_protocol='0,6,17'  where dict_id in(84,90,96,105,101)