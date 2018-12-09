--修改topic_domain默认无表达式
update function_region_dict set config_expr_type=0,config_hex=0 where dict_id=179;