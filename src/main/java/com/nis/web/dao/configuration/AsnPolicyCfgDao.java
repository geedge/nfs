package com.nis.web.dao.configuration;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.specific.ConfigGroupInfo;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;
@MyBatisDao
public interface AsnPolicyCfgDao extends CrudDao<CfgIndexInfo>{
	public List<CfgIndexInfo> findPage(CfgIndexInfo cfg);
	public void updateValid(@Param("isValid")Integer isValid,@Param("ids")String ids);
	public List<CfgIndexInfo> getByIds(@Param("ids")String ids);
	public void save(CfgIndexInfo entity);
	public void updateCfgIndexInfo(CfgIndexInfo entity);
	public List<ConfigGroupInfo> getConfigGroupInfoList(@Param("groupType")Integer groupType);
}
