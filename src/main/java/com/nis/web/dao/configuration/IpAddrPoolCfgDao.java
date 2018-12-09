package com.nis.web.dao.configuration;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.IpAddrPoolCfg;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;

@MyBatisDao
public interface IpAddrPoolCfgDao extends CrudDao<IpAddrPoolCfgDao>{

	List<IpAddrPoolCfg> findPage(IpAddrPoolCfg entity);

	List<IpAddrPoolCfg> findList(@Param("cfgId")Long cfgId
								,@Param("isAudit")Integer isAudit
								,@Param("isValid")Integer isValid);

	List<BaseIpCfg> getReuseIpCfgs(@Param("cfgId")Long cfgId);

	IpAddrPoolCfg getCfgInfo(IpAddrPoolCfg cfg);
	
	void saveAddrPoolCfg(IpAddrPoolCfg cfg);
	void saveReuseIpCfgs(BaseIpCfg ipCfg);
	
	void updateAddrPoolCfg(IpAddrPoolCfg entity);
	void updateReuseIpCfgs(IpAddrPoolCfg entity);

	void deleteReuseIpCfgs(@Param("addrPoolId")Long addrPoolId);

	List<IpAddrPoolCfg> findAddrPoolCfg();

	Long getAddrPoolIdByName(String addrPoolName);



}
