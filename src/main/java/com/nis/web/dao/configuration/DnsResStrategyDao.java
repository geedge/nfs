package com.nis.web.dao.configuration;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.configuration.DnsResStrategy;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;

@MyBatisDao
public interface DnsResStrategyDao extends CrudDao<DnsResStrategy> {
	List<DnsResStrategy> findPage(DnsResStrategy dnsResStrategy);
	List<DnsResStrategy> findList(@Param("cfgId")Long cfgId
			,@Param("isAudit")Integer isAudit
			,@Param("isValid")Integer isValid);
	List<DnsResStrategy> findhasValidIpList(@Param("cfgId")Long cfgId
			,@Param("isAudit")Integer isAudit
			,@Param("isValid")Integer isValid);
	
}