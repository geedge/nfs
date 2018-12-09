package com.nis.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.SysDataDictionaryItem;
import com.nis.domain.SysDataDictionaryName;

@MyBatisDao
public interface SysDictDao extends CrudDao<SysDataDictionaryName> {
	
	
	List<SysDataDictionaryName> findDicByName(@Param("DEL_FLAG_NORMAL")String DEL_FLAG_NORMAL,@Param("modualName") String modualName,@Param("mark") String mark);
	
	List<SysDataDictionaryName> findDictList(SysDataDictionaryName sysDictName);
	
	SysDataDictionaryName getDictById(@Param("DEL_FLAG_NORMAL")Integer DEL_FLAG_NORMAL,@Param("id") Integer id);
  
	void insertDictName(SysDataDictionaryName sysDictName);
	
	void updateDictName(SysDataDictionaryName sysDictName);
	
	void insertDictItem(SysDataDictionaryItem sysDictitem);

	void deleteDictItem(@Param("dictId")Integer dictId);
	
	void deleteDictName(@Param("dictId")Integer dictId);
	
	void updateDictItem(SysDataDictionaryItem sysDictItem);
	
}