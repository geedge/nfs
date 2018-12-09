package com.nis.web.dao.configuration;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.configuration.AreaIpCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;

/**
 * IP相关配置数据处理类
 * @author dell
 *
 */
@MyBatisDao
public interface AreaIpCfgDao extends CrudDao<AreaIpCfg>{
	public List<AreaIpCfg> getByCompileId(@Param("compileId") int compileId) ;
	public void saveAreaIpCfg(AreaIpCfg entity);
	public void updateAreaIpCfg(AreaIpCfg entity);
	public void updateAreaIpCfgValid(AreaIpCfg entity);
	public void deleteAreaIpCfg(AreaIpCfg entity);
	public void deleteAreaIpCfgByCfgId(AreaIpCfg entity);
}
