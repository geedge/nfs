package com.nis.web.dao.configuration;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.configuration.DdosIpCfg;
import com.nis.domain.configuration.DnsIpCfg;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;

@MyBatisDao
public interface DdosCfgDao extends CrudDao<DdosIpCfg> {
	List<DdosIpCfg> findPage(DdosIpCfg cfg);
	DdosIpCfg getDdosIpCfg(@Param("cfgId")Long cfgId,@Param("compileId")Integer compileId);
}
