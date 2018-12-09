package com.nis.web.dao.basics;

import java.util.List;


import com.nis.domain.basics.PolicyGroupInfo;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;

@MyBatisDao
public interface PolicyGroupInfoDao extends CrudDao<PolicyGroupInfo> {
	List<PolicyGroupInfo> findPolicyGroupInfoList(PolicyGroupInfo policyGroupInfo);
	List<PolicyGroupInfo> findPolicyGroupInfos();
	List<PolicyGroupInfo> findPolicyGroupInfosByType(int groupType);
	List<PolicyGroupInfo> findHasIPPolicyGroupInfosByType(int groupType);
	List<PolicyGroupInfo> findPolicyGroupInfosHasIpRegionByType(int groupType);
	PolicyGroupInfo getById(int id);
	List<PolicyGroupInfo> findNatPolicyGroups();
	
	PolicyGroupInfo getInfoByAsnNo(PolicyGroupInfo policyGroupInfo);
	List<PolicyGroupInfo> getHasAreaPolicyGroups(int groupType);
	int insertBatch(List<PolicyGroupInfo> list);
	
	Integer getGroupIdByGroupName(String groupName);
	PolicyGroupInfo getGroupInfo(PolicyGroupInfo policyGroupInfo);
}