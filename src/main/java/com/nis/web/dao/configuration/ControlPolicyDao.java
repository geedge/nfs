package com.nis.web.dao.configuration;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.configuration.HttpBodyCfg;
import com.nis.web.dao.MyBatisDao;


@MyBatisDao
public interface ControlPolicyDao {
	public List<HttpBodyCfg> findPage(HttpBodyCfg entity) ;
	public HttpBodyCfg getHttpBodyCfgById(@Param("cfgId")Long cfgId);
	public void insert(HttpBodyCfg entity);
	public void update(HttpBodyCfg entity);
}
