package com.nis.web.dao.configuration;

import java.util.List;

import com.nis.domain.configuration.GroupAreaInfo;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;

@MyBatisDao
public interface GroupAreaDao extends CrudDao<GroupAreaInfo>{

	List<GroupAreaInfo> findGroupAreaInfoList(GroupAreaInfo entity);

	GroupAreaInfo getInfoById(int id);

	Integer getAreaCodeByGroupId(Integer groupId);


}
