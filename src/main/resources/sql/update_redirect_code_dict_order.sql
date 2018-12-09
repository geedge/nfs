update sys_data_dictionary_item set item_sort=1 where dictionary_id=(select id from sys_data_dictionary_name where mark='REDIRECT_RESPONSE_CODE') and item_code='302';
update sys_data_dictionary_item set item_sort=2 where dictionary_id=(select id from sys_data_dictionary_name where mark='REDIRECT_RESPONSE_CODE') and item_code='301';
