package com.nis.web.dao.configuration;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.IpMultiplexPoolCfg;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;

@MyBatisDao
public interface IpMultiplexPoolCfgDao extends CrudDao<IpMultiplexPoolCfg> {
	List<IpMultiplexPoolCfg> findPage(IpMultiplexPoolCfg cfg);
	List<IpMultiplexPoolCfg> findList(@Param("cfgId")Long cfgId
			,@Param("isAudit")Integer isAudit
			,@Param("isValid")Integer isValid);
	
	IpMultiplexPoolCfg getCfgInfo(IpMultiplexPoolCfg cfg);
	
	List<IpMultiplexPoolCfg> getIspByGroupId(Integer groupId);
	
	// 保存IP信息
	void saveReuseIps(BaseIpCfg ipCfg);
	
}