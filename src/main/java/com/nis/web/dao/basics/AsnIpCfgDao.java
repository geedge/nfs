package com.nis.web.dao.basics;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.basics.AsnIpCfg;
import com.nis.domain.basics.Varibles;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;
@MyBatisDao
public interface AsnIpCfgDao extends CrudDao<AsnIpCfg>{
	public List<AsnIpCfg> findPage(AsnIpCfg cfg);
	//public void updateValid(@Param("isValid")Integer isValid,@Param("ids")String ids);
	public void delete(@Param("ids")String ids);
	public void updateIssued(AsnIpCfg cfg);
	public List<AsnIpCfg> getByIds(@Param("ids")String ids);
	public List<Integer> hasGroupIds(@Param("ids")String ids);
	public List<AsnIpCfg> findAllList(AsnIpCfg cfg);
//	public List<ConfigGroupInfo> findPolicyGroupInfosByType(@Param("groupId")Integer groupId);
	public List<Integer>  findOtherIps(@Param("groupId")Integer groupId,@Param("cfgId")Integer cfgId);
	public List<Integer> countValidIPs(@Param("groups")String groups,@Param("ids")String ids);
	
	public List<AsnIpCfg> getByAsnNo(@Param("asnId")long asnNo);
	public int deleteByAsnGroup(@Param("asnId")String asnNo);
	public int insertBatch(List<AsnIpCfg> list);
	public Varibles getVaribles(@Param("name")String name);
}
