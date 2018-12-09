package com.nis.web.service.basics;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beust.jcommander.internal.Lists;
import com.nis.domain.Page;
import com.nis.domain.basics.AsnIpCfg;
import com.nis.domain.basics.PolicyGroupInfo;
import com.nis.domain.basics.Varibles;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.maat.GroupReuseAddBean;
import com.nis.domain.maat.GroupReuseCfg;
import com.nis.domain.maat.MaatCfg.IpCfg;
import com.nis.domain.maat.MaatCfg.NumBoundaryCfg;
import com.nis.domain.maat.MaatCfg.StringCfg;
import com.nis.domain.maat.ToMaatResult;
import com.nis.domain.specific.ConfigGroupInfo;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.AsnCacheUtils;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.Constants;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.basics.AsnIpCfgDao;
import com.nis.web.dao.basics.PolicyGroupInfoDao;
import com.nis.web.dao.specific.ConfigGroupInfoDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.CrudService;
@Service
public class AsnIpCfgService extends CrudService<CrudDao<AsnIpCfg>, AsnIpCfg> {
	@Autowired
	private AsnIpCfgDao asnIpCfgDao;
//	@Autowired
//	private SpecificServiceCfgDao specificServiceCfgDao;
	@Autowired
	private ConfigGroupInfoDao configGroupInfoDao;
	@Autowired
	private PolicyGroupInfoDao policyGroupInfoDao;
	/**
	 * @param page
	 * @param entity
	 * @return
	 */
	public Page<AsnIpCfg> findPage(Page<AsnIpCfg> page, AsnIpCfg entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"r"));
		entity.setPage(page);
		List<AsnIpCfg> list=asnIpCfgDao.findPage(entity);
		page.setList(list);
		return page;
	}
	public AsnIpCfg get(Long id ) {
		return asnIpCfgDao.get(id);
	}
	
//	public void save(List<AsnIpCfg> entitys){
//		this.saveBatch(entitys, AsnIpCfgDao.class);
//	}
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void save(List<AsnIpCfg> entitys) {
		logger.warn("Start to save IP,size:"+entitys.size());
		long start=System.currentTimeMillis();
		int len=0;
		List<AsnIpCfg> tempList=Lists.newArrayList();
//		Varibles maxPacket=asnIpCfgDao.getVaribles("max_allowed_packet");
		for(AsnIpCfg asnIpCfg:entitys) {
			int tempLen=asnIpCfg.toString().getBytes(Charset.forName("UTF-8")).length;
			if((len+tempLen)<Constants.MAX_ALLOWED_PACKET) {
				tempList.add(asnIpCfg);
				len+=tempLen;
			}else {
//				logger.warn("save ip size:"+tempList.size());
				asnIpCfgDao.insertBatch(tempList);
				tempList.clear();
				tempList.add(asnIpCfg);
				len=tempLen;
			}
		}
		if(tempList.size()>0) {
//			logger.warn("save ip size:"+tempList.size());
			asnIpCfgDao.insertBatch(tempList);
			tempList.clear();
		}
//		entitys.clear();
		long end=System.currentTimeMillis();
		logger.warn("Save IP finish,cost:"+(end-start));
	}
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void update(AsnIpCfg entity){
		Date editTime=new Date();
		entity.setEditTime(editTime);
		entity.setEditorId(entity.getCurrentUser().getId());
		if(entity.getAsnIpGroup()==null) {
			throw new RuntimeException("asn group name not found!");
		}
		ConfigGroupInfo groupInfo=configGroupInfoDao.getConfigGroupInfoByGroupId(entity.getAsnIpGroup());
		if(groupInfo.getIsIssued()==0) {//未下发,可修改
			asnIpCfgDao.update(entity);
		}else {
			throw new RuntimeException("asn group is issued, cannot update asn ip!");
		}
		
	}
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void processGroup(Map<Long,Integer> asnNoMap){
		logger.warn("Start to save group");
		long start=System.currentTimeMillis();
		List<PolicyGroupInfo> policyGroupInfos=Lists.newArrayList();
		List<ConfigGroupInfo> configGroupInfoS=Lists.newArrayList();
		Date createTime=new Date();
		int len=0,len1=0;
//		Varibles maxPacket=asnIpCfgDao.getVaribles("max_allowed_packet");
		for(Entry<Long, Integer> e:asnNoMap.entrySet()) {
			PolicyGroupInfo info=new PolicyGroupInfo();
			info.setAsnNo(e.getKey());
			info.setDescription("Create by import excel");
			info.setGroupName("Import"+e.getKey());
			info.setGroupType(4);
			info.setServiceGroupId(asnNoMap.get(e.getKey()));
			info.setIsValid(1);
			info.setCreatorId(UserUtils.getUser().getId());
			info.setCreateTime(createTime);
			int tempLen=info.toString().getBytes(Charset.forName("UTF-8")).length;
			if((len+tempLen)<Constants.MAX_ALLOWED_PACKET) {
				policyGroupInfos.add(info);
				len+=tempLen;
			}else {
				policyGroupInfoDao.insertBatch(policyGroupInfos);
				policyGroupInfos.clear();
				policyGroupInfos.add(info);
				len=tempLen;
			}
			ConfigGroupInfo group=new ConfigGroupInfo();
			group.setGroupId(info.getServiceGroupId());
			group.setGroupName(info.getGroupName());
			group.setIsIssued(0);
			group.setGroupType(info.getGroupType());
			group.setAsnId(info.getAsnNo());
			int tempLen1=group.toString().getBytes(Charset.forName("UTF-8")).length;
			if((len1+tempLen1)<Constants.MAX_ALLOWED_PACKET) {
				configGroupInfoS.add(group);
				len1+=tempLen1;
			}else {
				configGroupInfoDao.insertBatch(configGroupInfoS);
				configGroupInfoS.clear();
				configGroupInfoS.add(group);
				len1=tempLen1;
			}
		}
		if(policyGroupInfos.size()>0) {
			policyGroupInfoDao.insertBatch(policyGroupInfos);
			policyGroupInfos.clear();
		}
		if(configGroupInfoS.size()>0) {
			configGroupInfoDao.insertBatch(configGroupInfoS);
			configGroupInfoS.clear();
		}
		//刷新缓存
		AsnCacheUtils.init(true);
		long end=System.currentTimeMillis();
		logger.warn("Save group finish,cost:"+(end-start));
	}
	public void deleteIps(Map<Long,Integer> asnNoMap){
		List<AsnIpCfg> toDelAndSendAsnIpCfgs=Lists.newArrayList();
		List<Long> asnIds=Lists.newArrayList(asnNoMap.size());
		for(Entry<Long, Integer> e:asnNoMap.entrySet()) {
			ConfigGroupInfo configGroupInfo=AsnCacheUtils.get(e.getKey());
			if(configGroupInfo==null) {
				configGroupInfo=this.getConfigGroupInfoByAsnNo(e.getKey());
			}
			if(configGroupInfo.getIsIssued()==1) {//已下发
				List<AsnIpCfg> _toDelAsnIpCfgs=this.getByAsnNo(configGroupInfo.getAsnId());
				toDelAndSendAsnIpCfgs.addAll(_toDelAsnIpCfgs);
			}else {
				asnIds.add(e.getKey());
			}
		}
		if(asnIds.size()>0) {
			this.deleteByAsnNo(asnIds);
		}
		if(toDelAndSendAsnIpCfgs.size()>0) {
			int pointsDataLimit = Constants.MAAT_JSON_SEND_SIZE;//限制条数
			Integer size = toDelAndSendAsnIpCfgs.size();
			//判断是否有必要分批
			if(pointsDataLimit<size){
				int part = size/pointsDataLimit;//分批数
				for (int i = 0; i < part; i++) {
					//pointsDataLimit条
					List<AsnIpCfg> listPage = toDelAndSendAsnIpCfgs.subList(0, pointsDataLimit);
					delAndSend(listPage);
					//剔除
					toDelAndSendAsnIpCfgs.subList(0, pointsDataLimit).clear();
				}
				//最后剩下的
				if(!toDelAndSendAsnIpCfgs.isEmpty()){
					delAndSend(toDelAndSendAsnIpCfgs);
					
				}
			}else {
				delAndSend(toDelAndSendAsnIpCfgs);
			}
		}
	}
	/**
	 * 根据Id 批量删除
	 * @param entities
	 */
	public void deleteByIdsBatch(List<AsnIpCfg> entities) {
		StringBuilder sb=new StringBuilder();
		for(AsnIpCfg ip:entities) {
			sb.append(ip.getCfgId());
			sb.append(",");
		}
		sb.deleteCharAt(sb.toString().lastIndexOf(","));
		asnIpCfgDao.delete(sb.toString());
	}
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void delAndSend(List<AsnIpCfg> entities) {
		deleteByIdsBatch(entities);
		asnIPRegionSendToMaat(entities,Constants.VALID_NO);
	}
	/**
	 * 分割IP region分次下发
	 * 由于在Controller中分割，此方法不需要调用，直接使用asnIPRegionSendToMaat方法即可
	 * @param asnIpCfgs
	 * @param valid
	 */
	@Deprecated
	public void splitAndSend(List<AsnIpCfg> asnIpCfgs,Integer valid){
		if(asnIpCfgs!=null&&asnIpCfgs.size()>0) {
//			List<AsnIpCfg> asnIpCfgs=Lists.newArrayList(list.size()); 
//			asnIpCfgs.addAll(list);
			int pointsDataLimit = Constants.MAAT_JSON_SEND_SIZE;//限制条数
			Integer size = asnIpCfgs.size();
			//判断是否有必要分批
			if(pointsDataLimit<size){
				int part = size/pointsDataLimit;//分批数
				for (int i = 0; i < part; i++) {
					//pointsDataLimit条
					List<AsnIpCfg> listPage = asnIpCfgs.subList(0, pointsDataLimit);
					asnIPRegionSendToMaat(listPage,valid);
					//剔除
					asnIpCfgs.subList(0, pointsDataLimit).clear();
				}
				//最后剩下的
				if(!asnIpCfgs.isEmpty()){
					asnIPRegionSendToMaat(asnIpCfgs,valid);
				}
			}else {
				asnIPRegionSendToMaat(asnIpCfgs,valid);
			}
			
		}
	}
	
	/**
	 * 批量保存asn PolicyGroupInfo
	 * @param entities
	 * @return
	 * @throws MaatConvertException
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public Map<Long,ConfigGroupInfo> saveAsnGroupBatch(List<PolicyGroupInfo> entities) throws MaatConvertException{
		Map<Long,ConfigGroupInfo> configGroupInfoMap=new HashMap<>(entities.size());
		List<Integer> groupIds= ConfigServiceUtil.getId(2,entities.size());
		if(groupIds.size()!=entities.size()) {
			throw new MaatConvertException("Get asn group id failed");
		}
		int ind=0;
		for(PolicyGroupInfo entity:entities) {
			entity.setIsValid(1);
			ConfigGroupInfo group=new ConfigGroupInfo();
			//新增
			Date createTime=new Date();
			entity.setCreatorId(UserUtils.getUser().getId());
			entity.setCreateTime(createTime);
			//asn类型从综合服务获取groupId
			if(4!=entity.getGroupType().intValue()) {
				throw new RuntimeException("ConfigGroupInfo is not asn type 4!");
			}
			entity.setServiceGroupId(groupIds.get(ind).intValue());
			//新增协议分组
			group.setGroupId(groupIds.get(ind).intValue());
			group.setGroupName(entity.getGroupName());
			group.setIsIssued(0);
			group.setGroupType(entity.getGroupType());
			group.setAsnId(entity.getAsnNo());
			configGroupInfoDao.insertConfigGroupInfo(group);
			if(group.getGroupType().intValue()==4) {
				AsnCacheUtils.put(entity.getAsnNo(), group);
			}
			policyGroupInfoDao.insert(entity);
			configGroupInfoMap.put(group.getAsnId(), group);
			ind++;
		}
		return configGroupInfoMap;
	}
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveAsnIpCfg(CfgIndexInfo entity) {
		Date createTime=new Date();
		ConfigGroupInfo groupInfo=configGroupInfoDao.getConfigGroupInfoByGroupId(entity.getAsnIpGroup().intValue());
		if(groupInfo==null) {
			throw new RuntimeException("ConfigGroupInfo is null!");
		}
		List<Integer> idList=ConfigServiceUtil.getId(3, entity.getAsnIpCfgs().size());
		int index=0;
		if(groupInfo.getIsIssued()==1) {//如果已经下发，则需要下到综合服务中
			if(entity.getAsnIpCfgs()!=null) {
				for(AsnIpCfg cfg:entity.getAsnIpCfgs()) {
					if(StringUtils.isNotBlank(entity.getCfgDesc())) {
						BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
					}else {
						BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId","cfgDesc"});
					}
					
					cfg.setCreateTime(createTime);
					cfg.setCreatorId(entity.getCurrentUser().getId());
					cfg.setAsnIpGroup(entity.getAsnIpGroup());
					cfg.setIsValid(Constants.VALID_YES);
					cfg.setRegionId(idList.get(index));
					index++;
				}
			}
			this.save(entity.getAsnIpCfgs());
			asnIPRegionSendToMaat(entity.getAsnIpCfgs(),Constants.VALID_YES);
//			splitAndSend(entity.getAsnIpCfgs(),Constants.VALID_YES);
		}else {
			if(entity.getAsnIpCfgs()!=null) {
				for(AsnIpCfg cfg:entity.getAsnIpCfgs()) {
					BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
					cfg.setCreateTime(createTime);
					cfg.setCreatorId(entity.getCurrentUser().getId());
					cfg.setAsnIpGroup(entity.getAsnIpGroup().intValue());
					cfg.setIsValid(Constants.VALID_NO);
					cfg.setRegionId(idList.get(index));
					index++;
				}
			}
			this.save(entity.getAsnIpCfgs());
		}
	}
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void delete(Integer isValid,String ids) {
		for(String id:ids.split(",")) {
			Long.parseLong(id);
		}
		List<AsnIpCfg> issuedList=new ArrayList<>();
		List<AsnIpCfg> asnIpCfgList= asnIpCfgDao.getByIds(ids);
		
		for(AsnIpCfg asnIpCfg:asnIpCfgList) {
			if(asnIpCfg.getIsValid()==Constants.VALID_YES) {//代表下发过了
				asnIpCfg.setIsValid(isValid);
				issuedList.add(asnIpCfg);
				Integer groupId=asnIpCfg.getAsnIpGroup();
				List result=asnIpCfgDao.findOtherIps(groupId, asnIpCfg.getCfgId().intValue());
				if(result==null||result.size()==0) {
					throw new RuntimeException("Cant not delete ip from asn group, there is only one ip left in the group!");
				}
			}
		}
		asnIpCfgDao.delete(ids);
		//已经下发过的,调用分组复用配置删除接口
//		splitAndSend(issuedList,Constants.VALID_NO);
		asnIPRegionSendToMaat(issuedList,Constants.VALID_NO);
		
	}
//	public List<ConfigGroupInfo> findPolicyGroupInfosByType(Integer groupId) {
//		// TODO Auto-generated method stub
//		return asnIpCfgDao.findPolicyGroupInfosByType(groupId);
//	}
	/**
	 * 选中组中是否含有只剩一个未删除IP的组
	 * @param serviceGroupIds
	 * @return
	 */
	public boolean hasLastIp(String serviceGroupIds,String ids) {
		// TODO Auto-generated method stub
		for(String groupId:serviceGroupIds.split(",")) {
			Long.parseLong(groupId);
		}
		List<Integer> countList=asnIpCfgDao.countValidIPs(serviceGroupIds,ids);
		if(countList.size()==0) {
			return true;
		}
		for(Integer count:countList) {
			if(count==0) {
				return true;
			}
		}
		return false;
	}
	public ConfigGroupInfo getConfigGroupInfoByAsnNo(Long asnNo) {
		ConfigGroupInfo configGroupInfo=configGroupInfoDao.getInfoByAsnNo(asnNo);
		AsnCacheUtils.put(asnNo, configGroupInfo);
		return configGroupInfo;
	}
	public List<AsnIpCfg> getByAsnNo(Long asnNo) {
		if(asnNo==null) {
			throw new RuntimeException("groupId or asnNo is null!");
		}
		return asnIpCfgDao.getByAsnNo(asnNo.longValue());
	}
//	public void deleteByAsnGroup(Long asnNo) {
//		if(asnNo==null) {
//			throw new RuntimeException("asnNo is null!");
//		}
//		asnIpCfgDao.deleteByAsnGroup(asnNo.longValue());
//	}
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void deleteByAsnNo(List<Long> asnNoList) {
		int pointsDataLimit = 1000;//限制条数
		Integer size = asnNoList.size();
		if(pointsDataLimit<size){
			int part = size/pointsDataLimit;//分批数
			for (int i = 0; i < part; i++) {
				List<Long> listPage = asnNoList.subList(0, pointsDataLimit);
				StringBuilder asnNoStr=new StringBuilder();
				for(Long asnNo:asnNoList) {
					asnNoStr.append(asnNo);
					asnNoStr.append(",");
				}
				int result=0;
				do {
					result=asnIpCfgDao.deleteByAsnGroup(asnNoStr.toString().substring(0,asnNoStr.toString().length()-1));
				}while(result>0);
				//剔除
				asnNoList.subList(0, pointsDataLimit).clear();
			}
			if(!asnNoList.isEmpty()){
				StringBuilder asnNoStr=new StringBuilder();
				for(Long asnNo:asnNoList) {
					asnNoStr.append(asnNo);
					asnNoStr.append(",");
				}
				int result=0;
				do {
					result=asnIpCfgDao.deleteByAsnGroup(asnNoStr.toString().substring(0,asnNoStr.toString().length()-1));
				}while(result>0);
				asnNoList.clear();
			}
		}else {
			StringBuilder asnNoStr=new StringBuilder();
			for(Long asnNo:asnNoList) {
				asnNoStr.append(asnNo);
				asnNoStr.append(",");
			}
			int result=0;
			do {
				result=asnIpCfgDao.deleteByAsnGroup(asnNoStr.toString().substring(0,asnNoStr.toString().length()-1));
			}while(result>0);
			asnNoList.clear();
		}
	}
}
