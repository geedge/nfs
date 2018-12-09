package com.nis.web.dao.configuration;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.configuration.BaseCfg;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;

/**
 * IP相关配置数据处理类
 * @author dell
 *
 */
@MyBatisDao
public interface IpCfgDao extends CrudDao<BaseIpCfg>{
	public BaseIpCfg getById(@Param("tableName")String tableName,@Param("cfgId")Long id) ;
	public BaseIpCfg get(BaseIpCfg entity) ;
	public List<BaseIpCfg> findList(BaseIpCfg entity) ;
	public List<BaseIpCfg> getListByCfgId(@Param("tableName")String tableName,@Param("functionId")int functionId,@Param("ids")String ids) ;
	public List<BaseIpCfg> getListByCfgIdWithName(@Param("tableName")String tableName,@Param("functionId")int functionId,@Param("ids")String ids) ;
	public List<BaseIpCfg> getListByComileId(@Param("tableName")String tableName,@Param("functionId")int functionId,@Param("ids")String ids) ;
	public int insert(BaseIpCfg entity) ;
	public int update(BaseIpCfg entity) ;
	public int updateValid(BaseIpCfg entity) ;
	public int audit(BaseIpCfg entity) ;
	public int getIsValid(@Param("tableName")String tableName,@Param("cfgId")Long id);
	public int getIsValid(BaseIpCfg entity);
	public int getIsAudit(@Param("tableName")String tableName,@Param("cfgId")Long id);
	public int getIsAudit(BaseIpCfg entity);
	public int deleteByCompileId(BaseIpCfg entity);
	public int deleteByCompileIds(@Param("user")long user,@Param("tableName")String tableName,@Param("compileIds")String compileIds);
	/******************************new ***********************************/
	
	public CfgIndexInfo getCfgIndexInfo(@Param("cfgId")Long id,@Param("compileId")Integer compileId);
	public List<IpPortCfg> getIpPortList(CfgIndexInfo entity);
	public void saveCfgIndex(CfgIndexInfo entity);
	public void saveCfgIndexForBatch(CfgIndexInfo entity);
	public void saveIpPortCfg(IpPortCfg entity);
	public void updateCfgIndex(CfgIndexInfo entity);
	public void deleteIpCfg(CfgIndexInfo entity);
	public void updateCfgValid(BaseCfg entity);
	public void auditCfg(BaseCfg entity);
	public List<CfgIndexInfo> getIpCfgList(CfgIndexInfo entity);
		
}
