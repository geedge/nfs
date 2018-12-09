package com.nis.web.service.specific;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.specific.ConfigGroupInfo;
import com.nis.web.dao.specific.ConfigGroupInfoDao;
import com.nis.web.service.BaseService;
@Service
public class ConfigGroupInfoService extends BaseService{
	@Autowired
	private ConfigGroupInfoDao configGroupInfoDao;
	
	public List<ConfigGroupInfo> findAllList(int groupType){
		return configGroupInfoDao.findAllList(groupType);
	}
	public ConfigGroupInfo insertConfigGroupInfo(ConfigGroupInfo entity) {
		configGroupInfoDao.insertConfigGroupInfo(entity);
		return entity;
	}
	public ConfigGroupInfo updateConfigGroupInfobyGroupId(ConfigGroupInfo entity) {
		configGroupInfoDao.updateConfigGroupInfobyGroupId(entity);
		return entity;
	}
	public ConfigGroupInfo getConfigGroupInfoByGroupId(Integer groupId) {
		return configGroupInfoDao.getConfigGroupInfoByGroupId(groupId);
	}
	public ConfigGroupInfo getInfoByAsnNo(Long asnNo) {
		return configGroupInfoDao.getInfoByAsnNo(asnNo);
	}
	public Integer getIssuedConfigGroupInfoByGroupIds(String groupIds) {
		return configGroupInfoDao.getIssuedConfigGroupInfoByGroupIds(groupIds);
	}
	public List<ConfigGroupInfo> findAllList(ConfigGroupInfo entity){
		return configGroupInfoDao.findAllListByGroupInfo(entity);
	}
}
