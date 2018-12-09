#修改日志ddos 为一个菜单
UPDATE sys_menu SET href='/log/ntc/ntcDdosLogs/list' WHERE id=1138;
UPDATE sys_menu SET is_show=0 WHERE id=1139;

