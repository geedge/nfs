UPDATE sys_menu set permission='avVoipIp:config',href='/ntc/av/voipList' where id=629;
UPDATE sys_menu set permission='avVoipAccount:config',href='/ntc/av/voipAccountList' where id=630;
UPDATE sys_menu set permission='avVoipIp:confirm',href='/ntc/av/voipList'  where id=662;
UPDATE sys_menu set permission='avVoipAccount:confirm',href='/ntc/av/voipAccountList' where id=663;
UPDATE sys_menu set permission='avVoipIp:audit',href='/ntc/av/voipList'  where id=832;
UPDATE sys_menu set permission='avVoipAccount:audit',href='/ntc/av/voipAccountList' where id=833;
#修改来函和专项权限标识
update sys_menu set permission='cfg:requestInfo:confirm',name='审核',code='confirm' where id=222;
update sys_menu set permission='cfg:requestInfo:config',name='操作',code='config' where id=221;
update sys_menu set permission='basics:taskInfo:config',name='操作',code='config' where id=223;
update sys_menu set permission='basics:taskInfo:confirm',name='审核',code='confirm' where id=224;