package com.nis.web.dao.configuration;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.configuration.ComplexkeywordCfg;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;

/**
 * 增强字符串相关配置数据处理类
 * @author dell
 *
 */
@MyBatisDao
public interface ComplexStringCfgDao extends CrudDao<ComplexkeywordCfg>{
	public ComplexkeywordCfg getById(@Param("tableName")String tableName,@Param("cfgId")Long id) ;
	public ComplexkeywordCfg get(ComplexkeywordCfg entity) ;
	public List<ComplexkeywordCfg> getList(@Param("tableName")String tableName,@Param("ids")String ids) ;
	public List<ComplexkeywordCfg> findList(ComplexkeywordCfg entity) ;
	public int insert(ComplexkeywordCfg entity) ;
	public int update(ComplexkeywordCfg entity) ;
	public int updateValid(ComplexkeywordCfg entity) ;
	public int audit(ComplexkeywordCfg entity) ;
	public int getIsValid(@Param("tableName")String tableName,@Param("cfgId")Long id);
	public int getIsValid(ComplexkeywordCfg entity);
	public int getIsAudit(@Param("tableName")String tableName,@Param("cfgId")Long id);
	public int getIsAudit(ComplexkeywordCfg entity);
	public int deleteByCompileId(ComplexkeywordCfg entity);
	public int delete(@Param("tableName")String tableName,@Param("ids")String ids);
}
