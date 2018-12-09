package com.nis.web.dao.configuration;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.configuration.NumBoundaryCfg;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;

/**
 * IP相关配置数据处理类
 * @author dell
 *
 */
@MyBatisDao
public interface NumCfgDao extends CrudDao<NumBoundaryCfg>{
	public NumBoundaryCfg getById(@Param("cfgId")Long id) ;
	public NumBoundaryCfg get(NumBoundaryCfg entity) ;
	public List<NumBoundaryCfg> findList(NumBoundaryCfg entity) ;
	public int insert(NumBoundaryCfg entity) ;
	public int updateByPrimaryKeySelective(NumBoundaryCfg entity) ;
	public int updateValid(NumBoundaryCfg entity) ;
	public int audit(NumBoundaryCfg entity) ;
	public int getIsValid(@Param("cfgId")Long id);
	public int getIsValid(NumBoundaryCfg entity);
	public int getIsAudit(@Param("cfgId")Long id);
	public int getIsAudit(NumBoundaryCfg entity);
	public int deleteByCompileId(NumBoundaryCfg entity);
}
