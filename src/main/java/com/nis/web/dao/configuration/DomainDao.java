package com.nis.web.dao.configuration;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.configuration.HttpUrlCfg;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;

/**
 * 字符串相关配置数据处理类
 * @author dell
 *
 */
@MyBatisDao
public interface DomainDao extends CrudDao<HttpUrlCfg>{
	public HttpUrlCfg getById(@Param("cfgId")Long id) ;
	public HttpUrlCfg get(HttpUrlCfg entity) ;
	public List<HttpUrlCfg> findList(HttpUrlCfg entity) ;
	public int insert(HttpUrlCfg entity) ;
	public int update(HttpUrlCfg entity) ;
	public int updateValid(HttpUrlCfg entity) ;
	public int audit(HttpUrlCfg entity) ;
	public int getIsValid(@Param("cfgId")Long id);
	public int getIsValid(HttpUrlCfg entity);
	public int getIsAudit(@Param("cfgId")Long id);
	public int getIsAudit(HttpUrlCfg entity);
	public int deleteByCompileId(HttpUrlCfg entity);
	public List<HttpUrlCfg> getListByCfgId(@Param("ids")String ids);
	public List<HttpUrlCfg> getListByCompileId(@Param("ids")String ids);
	/**
	 * deleteByCompileIds(这里用一句话描述这个方法的作用)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param id
	 * @param tablename
	 * @param compileIds 
	 *void
	 * @exception 
	 * @since  1.0.0
	*/
	public void deleteByCompileIds(@Param("user")long user,@Param("compileIds")String compileIds);
}
