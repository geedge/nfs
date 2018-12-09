package com.nis.web.dao.configuration;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.callback.ProxyFileStrategyCfg;
import com.nis.web.dao.MyBatisDao;


/**
 * 特定协议相关配置数据处理类
 * @author dell
 *
 */
@MyBatisDao
public interface ProxyFileStrategyDao {
	
	public List<ProxyFileStrategyCfg> findPage(ProxyFileStrategyCfg entity) ;
	public ProxyFileStrategyCfg getCfgById(@Param("cfgId")Long cfgId);
	public void insert(ProxyFileStrategyCfg entity);
	public void update(ProxyFileStrategyCfg entity);
	
	
}
