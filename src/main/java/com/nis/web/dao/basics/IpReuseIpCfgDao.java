package com.nis.web.dao.basics;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.basics.IpReuseIpCfg;
import com.nis.domain.configuration.DnsIpCfg;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;

@MyBatisDao
public interface IpReuseIpCfgDao extends CrudDao<IpReuseIpCfg> {
	List<IpReuseIpCfg> findPage(IpReuseIpCfg userIpCfg);
	IpReuseIpCfg getUserIpCfg(Long cfgId);
	IpReuseIpCfg getIpByIp(String destIpAddress);
	List<IpReuseIpCfg> getPolicyIpInfo(@Param("addrPoolId")Integer addrPoolId);
}
