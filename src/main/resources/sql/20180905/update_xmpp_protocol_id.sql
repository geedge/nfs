#xmpp的protocol_id由18改为17，与文档一致
UPDATE function_service_dict SET protocol_id=17 WHERE service_id IN(30,142);