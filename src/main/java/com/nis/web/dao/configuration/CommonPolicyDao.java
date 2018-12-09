package com.nis.web.dao.configuration;


import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.configuration.BaseCfg;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.ComplexkeywordCfg;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.web.dao.MyBatisDao;


@MyBatisDao
public interface CommonPolicyDao {
	public List<CfgIndexInfo> getPolicyList(CfgIndexInfo entity);
	public CfgIndexInfo getPolicyById(Long cfgId);
	public List<IpPortCfg> getIpPortList(CfgIndexInfo entity);
	public List<BaseStringCfg> getCommonStringList(CfgIndexInfo entity);
	public List<ComplexkeywordCfg> getComplexStringList(CfgIndexInfo entity);
	public void saveCfgIndex(CfgIndexInfo entity);
	public void saveIpPortCfg(IpPortCfg entity);
	public void saveCommonStringCfg(BaseStringCfg entity);
	public void saveComplexStringCfg(ComplexkeywordCfg entity);
	public void updateCfgIndex(CfgIndexInfo entity);
	public void deleteIpPortCfg(CfgIndexInfo entity);
	public void deleteCommonStringCfg(CfgIndexInfo entity);
	public void deleteComplexStringCfg(CfgIndexInfo entity);
	public void updateCfgValid(BaseCfg entity);
	public void auditCfg(BaseCfg entity);
	public void auditCfgBatch(@Param("tableName")String tableName,
								@Param("entity")BaseCfg baseCfg,
								@Param("compileIds")List compileIds,
								@Param("requestId")Integer requestId);
}
