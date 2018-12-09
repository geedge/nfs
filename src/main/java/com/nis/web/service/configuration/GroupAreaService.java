package com.nis.web.service.configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.basics.PolicyGroupInfo;
import com.nis.domain.configuration.AreaBean;
import com.nis.domain.configuration.GroupAreaInfo;
import com.nis.domain.configuration.IpMultiplexPoolCfg;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.util.StringUtil;
import com.nis.web.dao.configuration.GroupAreaDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.BaseService;
import com.nis.web.service.SpringContextHolder;

@Service
public class GroupAreaService extends BaseService{
	
	@Autowired
	private GroupAreaDao groupAreaDao;
	
	public Page<GroupAreaInfo> findGroupAreaInfoList(Page<GroupAreaInfo> page, GroupAreaInfo entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"r"));
		entity.setPage(page);
		List<GroupAreaInfo> list=groupAreaDao.findGroupAreaInfoList(entity);
		page.setList(list);
		return page;
	}

	public GroupAreaInfo getInfoById(int id) {
		GroupAreaInfo GroupAreaInfo=groupAreaDao.getInfoById(id);
		return GroupAreaInfo;
	}
	
	public void saveOrUpdate(GroupAreaInfo entity) {
		entity.setIsValid(1);
		if(entity.getId()==null){//新增
			Date createTime=new Date();
			entity.setCreatorId(UserUtils.getUser().getId());
			entity.setCreateTime(createTime);
			groupAreaDao.insert(entity);
		}else{//修改
			Date editTime=new Date();
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(editTime);
			groupAreaDao.update(entity);
		}
		
	}

	public void deldete(String ids, int isValid) {
		if(!StringUtil.isEmpty(ids)){
			GroupAreaInfo entity=new GroupAreaInfo();
			Date editTime=new Date();
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(editTime);
			entity.setIsValid(isValid);
			for (String id : ids.split(",")) {
				if(!StringUtil.isEmpty(id)){
					entity.setId(Long.parseLong(id));
					groupAreaDao.update(entity);
				}
			}
		}
	}
	
	// 获取未使用的策略分组
	public List<PolicyGroupInfo> getNotUsedPolicyGroups(List<PolicyGroupInfo> policyGroups) {
		List<GroupAreaInfo> groupAreas=groupAreaDao.findGroupAreaInfoList(new GroupAreaInfo());
		for (GroupAreaInfo groupArea : groupAreas) {
			for (int i = 0; i < policyGroups.size(); i++) {
				Integer groupId = policyGroups.get(i).getGroupId();
				if(groupArea.getGroupId().equals(groupId)){
					policyGroups.remove(i);
					i--;
				}
			}
		}
		return policyGroups;
	}
	
	public void setAreaCodeByGroupId(IpPortCfg cfg){
		Integer areaCode = groupAreaDao.getAreaCodeByGroupId(cfg.getDnsStrategyId());
		cfg.setAreaEffectiveIds(areaCode+"");
	}
	
	// snat策略 设置生效区域信息
	public void setAreaEffective(IpPortCfg cfg) {
		String areaEffectiveIds = "";
		if(cfg.getDnsStrategyId() != 0){// 已选择分组
			// 1.设置省份信息
			this.setAreaCodeByGroupId(cfg);
			areaEffectiveIds = cfg.getAreaEffectiveIds();
			
			// 2.获取该分组下的IP复用地址池配置的ISP
			IpMultiplexPoolCfgService poolCfgService = SpringContextHolder.getBean(IpMultiplexPoolCfgService.class);
			List<IpMultiplexPoolCfg> cfgsList = poolCfgService.getIspByGroupId(cfg.getDnsStrategyId());
			String newAreaEffectiveIds = "";
			List<String> ispList = new ArrayList<>();
			if(cfgsList.size() != 0){
				for (IpMultiplexPoolCfg poolCfg : cfgsList) {
					String areaIsp = poolCfg.getAreaEffectiveIds();
					if(areaIsp.contains(":")){
						areaIsp = areaIsp.substring(areaIsp.indexOf(":")+1);
					}
					ispList.add(areaIsp);
				}
			}
			// 3.组织格式
			if(ispList.size() != 0){
				for (int i = 0; i < ispList.size(); i++) {
					if(i < ispList.size()-1){
						newAreaEffectiveIds += cfg.getAreaEffectiveIds()+":"+ispList.get(i)+",";
					}else{
						newAreaEffectiveIds += cfg.getAreaEffectiveIds()+":"+ispList.get(i);
					}
				}
				areaEffectiveIds = newAreaEffectiveIds;
			}
		}else{
			areaEffectiveIds = cfg.getAreaEffectiveIds();
		}
		cfg.setAreaEffectiveIds(areaEffectiveIds);
		
	}
	
	// IP复用地址池 设置省份信息
	public void setAreaCodeByGroupId(IpMultiplexPoolCfg cfg){
		Integer areaCode = groupAreaDao.getAreaCodeByGroupId(cfg.getPolicyGroup());
		if(!StringUtil.isEmpty(areaCode)){
			// 判断是否有ISP
			if(StringUtil.isEmpty(cfg.getAreaIsp())){
				List<AreaBean> areaIsp = new ArrayList<>();
				AreaBean areaBean = new AreaBean();
				areaBean.setArea(areaCode+"");
				areaIsp.add(areaBean);
				cfg.setAreaIsp(areaIsp);
			}else{
				cfg.getAreaIsp().get(0).setArea(areaCode+"");
				
			}
		}
	}
	
}
