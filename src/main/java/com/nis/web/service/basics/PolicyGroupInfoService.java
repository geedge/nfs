package com.nis.web.service.basics;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nis.domain.Page;
import com.nis.domain.basics.PolicyGroupInfo;
import com.nis.domain.specific.ConfigGroupInfo;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.AsnCacheUtils;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.StringUtil;
import com.nis.web.dao.basics.AsnIpCfgDao;
import com.nis.web.dao.basics.PolicyGroupInfoDao;
import com.nis.web.dao.specific.ConfigGroupInfoDao;
import com.nis.web.dao.specific.SpecificServiceCfgDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.BaseService;

@Service
public class PolicyGroupInfoService extends BaseService{
	
	@Autowired
    private PolicyGroupInfoDao policyGroupInfoDao;
	@Autowired
//	private SpecificServiceCfgDao specificServiceCfgDao;
	private ConfigGroupInfoDao configGroupInfoDao;
	@Autowired
	private AsnIpCfgDao asnIpCfgDao;
	
	/**
	 * @param page
	 * @param entity
	 * @return
	 */
	public Page<PolicyGroupInfo> findPolicyGroupInfoList(Page<PolicyGroupInfo> page, PolicyGroupInfo entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"r"));
		entity.setPage(page);
		List<PolicyGroupInfo> list=policyGroupInfoDao.findPolicyGroupInfoList(entity);
		page.setList(list);
		return page;
	}
	public List<PolicyGroupInfo> findPolicyGroupInfos() {
		List<PolicyGroupInfo> list=policyGroupInfoDao.findPolicyGroupInfos();
		return list;
	}
	public List<PolicyGroupInfo> findNatPolicyGroups() {
		List<PolicyGroupInfo> list=policyGroupInfoDao.findNatPolicyGroups();
		return list;
	}
	public List<PolicyGroupInfo> findPolicyGroupInfosByType(int type) {
		List<PolicyGroupInfo> list=policyGroupInfoDao.findPolicyGroupInfosByType(type);
		return list;
	}
	public List<PolicyGroupInfo> findHasIpPolicyGroupInfosByType(int type) {
		List<PolicyGroupInfo> list=policyGroupInfoDao.findHasIPPolicyGroupInfosByType(type);
		return list;
	}
	public List<PolicyGroupInfo> findPolicyGroupInfosHasIpRegionByType(int type) {
		List<PolicyGroupInfo> list=policyGroupInfoDao.findPolicyGroupInfosByType(type);
		return list;
	}
	
	public PolicyGroupInfo getById(int id) {
		PolicyGroupInfo policyGroupInfo=policyGroupInfoDao.getById(id);
		return policyGroupInfo;
	}
	
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveOrUpdate(PolicyGroupInfo entity) throws MaatConvertException{
		entity.setIsValid(1);
		//新增
		if(entity.getGroupId()==null){
			Date createTime=new Date();
			entity.setCreatorId(UserUtils.getUser().getId());
			entity.setCreateTime(createTime);
			//asn类型从综合服务获取groupId
			if(4==entity.getGroupType().intValue()) {
				int serviceGroupId=0;
				List<Integer> groupIds= ConfigServiceUtil.getId(2,1);
				if(groupIds.size()>0) {
					serviceGroupId=groupIds.get(0).intValue();
				}else {
					throw new MaatConvertException("Get asn group id failed");
				}
				entity.setServiceGroupId(serviceGroupId);
				//新增协议分组
				ConfigGroupInfo group = configGroupInfoDao.getConfigGroupInfoByGroupId(serviceGroupId);
				if(group==null){
					group = new ConfigGroupInfo();
					group.setGroupId(serviceGroupId);
					group.setGroupName(entity.getGroupName());
					group.setIsIssued(0);
					group.setGroupType(entity.getGroupType());
					group.setAsnId(entity.getAsnNo());
					configGroupInfoDao.insertConfigGroupInfo(group);
					if(group.getGroupType().intValue()==4) {
						AsnCacheUtils.put(entity.getAsnNo(), group);
					}
					
				}
			}
			policyGroupInfoDao.insert(entity);
		//修改
		}else{
			Date editTime=new Date();
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(editTime);
			policyGroupInfoDao.update(entity);
		}
	}
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void deldete(String ids, int isValid){
		PolicyGroupInfo entity=new PolicyGroupInfo();
		Date editTime=new Date();
		entity.setEditorId(UserUtils.getUser().getId());
		entity.setEditTime(editTime);
		entity.setIsValid(isValid);
		if(!StringUtil.isEmpty(ids)){
			for (String id : ids.split(",")) {
				if(!StringUtil.isEmpty(id)){
					entity.setGroupId(Integer.parseInt(id));
					policyGroupInfoDao.update(entity);
				}
			}
		}
	}
	public boolean checkIsIssued(String groupIds) {
		for(String groupId:groupIds.split(",")) {
			Integer.parseInt(groupId);
		}
		Integer count=configGroupInfoDao.getIssuedConfigGroupInfoByGroupIds(groupIds);
		if(count>0) return true;
		return false;
	}
	public boolean checkIps(String groupIds) {
		for(String groupId:groupIds.split(",")) {
			Integer.parseInt(groupId);
		}
		List<Integer> hasIpList=asnIpCfgDao.hasGroupIds(groupIds);
		if(hasIpList!=null&&hasIpList.size()>0) {
			return true;
		}
		return false;
	}
	
	public PolicyGroupInfo getInfoByAsnNo(PolicyGroupInfo policyGroupInfo){
		return policyGroupInfoDao.getInfoByAsnNo(policyGroupInfo);
	}
	
	/**
	 * IP复用配置表单-获取拥有区域信息的策略分组
	 * @param groupType
	 * @return
	 */
	public List<PolicyGroupInfo> getHasAreaPolicyGroups(int groupType) {
		return policyGroupInfoDao.getHasAreaPolicyGroups(groupType);
	}
	
	public Integer getGroupIdByGroupName(String groupName){
		return policyGroupInfoDao.getGroupIdByGroupName(groupName);
	}
	
	public PolicyGroupInfo getGroupInfo(PolicyGroupInfo policyGroupInfo){
		return policyGroupInfoDao.getGroupInfo(policyGroupInfo);
	}
	
}
