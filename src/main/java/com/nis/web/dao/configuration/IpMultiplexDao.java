package com.nis.web.dao.configuration;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.IpReuseDnatPolicyCfg;
import com.nis.domain.configuration.IpReusePolicyCfg;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;

@MyBatisDao
public interface IpMultiplexDao extends CrudDao<IpMultiplexDao>{

	List<IpReusePolicyCfg> findPage(IpReusePolicyCfg entity);

	List<IpReusePolicyCfg> findList(@Param("cfgId")Long cfgId
									,@Param("isValid")Integer isValid);

	void savePolicyCfg(IpReusePolicyCfg entity);

	void updatePolicyCfg(IpReusePolicyCfg entity);

	String getUserType(@Param("userName")String userName);
	
	// 校验地址池是否被引用
	List<IpReusePolicyCfg> checkAddrPoolIsUsed(@Param("addrPoolId")String addrPoolId);
	
	// SNAT策略更新审核状态
	void updatePolicyAuditStatus(IpReusePolicyCfg cfg);
	
	// DNAT策略更新审核状态
	void updateDnatPolicyStatus(IpReuseDnatPolicyCfg cfg);

	void saveDnatPolicyCfg(IpReuseDnatPolicyCfg entity);

	void updateDnatPolicyCfg(IpReuseDnatPolicyCfg entity);

	List<BaseIpCfg> findPageDnat(IpReuseDnatPolicyCfg entity);

	List<IpReuseDnatPolicyCfg> findDnatList(@Param("cfgId")Long cfgId, @Param("isValid")Integer isValid);

}
