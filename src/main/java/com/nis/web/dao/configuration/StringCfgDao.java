package com.nis.web.dao.configuration;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.ComplexkeywordCfg;
import com.nis.domain.configuration.NtcSubscribeIdCfg;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;

/**
 * 字符串相关配置数据处理类
 * @author dell
 *
 */
@MyBatisDao
public interface StringCfgDao extends CrudDao<BaseStringCfg>{
	public BaseStringCfg getById(@Param("tableName")String tableName,@Param("cfgId")Long id) ;
	public BaseStringCfg get(BaseStringCfg entity) ;
	public List<BaseStringCfg> findList(BaseStringCfg entity) ;
	public int insert(BaseStringCfg entity) ;
	public int update(BaseStringCfg entity) ;
	public int updateValid(BaseStringCfg entity) ;
	public int audit(BaseStringCfg entity) ;
	public int getIsValid(@Param("tableName")String tableName,@Param("cfgId")Long id);
	public int getIsValid(BaseStringCfg entity);
	public int getIsAudit(@Param("tableName")String tableName,@Param("cfgId")Long id);
	public int getIsAudit(BaseStringCfg entity);
	public int deleteByCompileId(BaseStringCfg entity);
	public List<BaseStringCfg> getListByCfgId(@Param("tableName")String tableName, @Param("ids")String ids);
	public List<BaseStringCfg> getListByCompileId(@Param("tableName")String tableName, @Param("ids")String ids);
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
	public void deleteByCompileIds(@Param("user")long user,@Param("tableName")String tableName,@Param("compileIds")String compileIds);
	public List<NtcSubscribeIdCfg> findSubscribeIdCfgListByCfgIndexInfo(CfgIndexInfo entity);
	public void deleteSubscribeIdCfgByCfgIndexInfo(CfgIndexInfo entity);
	public List<NtcSubscribeIdCfg> findSubscribeIdCfgList(NtcSubscribeIdCfg ntcSubscribeIdCfg);
	public void saveSubscribeIdCfg(NtcSubscribeIdCfg ntcSubscribeIdCfg);
	public void updateSubscribeIdCfg(NtcSubscribeIdCfg ntcSubscribeIdCfg);
	public void deleteSubscribeIdCfg(NtcSubscribeIdCfg ntcSubscribeIdCfg);
	
	// 导入配置时数据批量入库使用
	public void saveStringCfgBatch(BaseStringCfg cfg);
	public void saveComplexkeywordCfgBatch(ComplexkeywordCfg cfg);
}
