package com.nis.web.dao.configuration;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.configuration.DnsIpCfg;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;

@MyBatisDao
public interface DnsIpCfgDao extends CrudDao<DnsIpCfg> {
	List<DnsIpCfg> findPage(DnsIpCfg dnsIpCfg);
	DnsIpCfg getDnsIpCfg(Long cfgId);
}
