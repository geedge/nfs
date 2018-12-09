package com.nis.web.dao.specific;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.Cacheable;

import com.nis.domain.specific.ConfigGroupInfo;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;
@MyBatisDao
public interface ConfigGroupInfoDao extends CrudDao<ConfigGroupInfo>{
	List<ConfigGroupInfo> findAllList(int groupType);
	List<ConfigGroupInfo> findAllListByGroupInfo(ConfigGroupInfo entity);
	void insertConfigGroupInfo(ConfigGroupInfo entity);
	int insertBatch(List<ConfigGroupInfo> list);
	void updateConfigGroupInfobyGroupId(ConfigGroupInfo entity);
	ConfigGroupInfo getConfigGroupInfoByGroupId(Integer groupId);
	@Cacheable(value="asnNoCache",key="#asnNo")
	ConfigGroupInfo getInfoByAsnNo(@Param("asnId")Long asnNo);
	Integer getIssuedConfigGroupInfoByGroupIds(@Param("groupIds")String groupIds);
	Long getCountByType(@Param("groupType")Integer groupType);
}
