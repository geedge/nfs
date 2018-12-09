 #更改协议菜单跟 anti ddos菜单的顺序
 UPDATE sys_menu SET sort = 10 WHERE id = 943;
 #dns菜单从website移动到文本下
 UPDATE sys_menu SET parent_id = 944, parent_ids = '0,1,780,944,', sort = 40 WHERE id = 786; 
 #修改voip ip菜单国际化
 UPDATE sys_menu SET CODE='av_voip_ip_control', NAME = 'VoIP IP 管控' WHERE id =975;