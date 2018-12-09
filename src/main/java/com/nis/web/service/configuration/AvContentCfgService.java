package com.nis.web.service.configuration;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nis.domain.Page;
import com.nis.domain.configuration.AreaBean;
import com.nis.domain.configuration.AreaIpCfg;
import com.nis.domain.configuration.AvContUrlCfg;
import com.nis.domain.configuration.AvVoipAccountCfg;
import com.nis.domain.configuration.AvVoipIpCfg;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.HttpUrlCfg;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.configuration.NtcSubscribeIdCfg;
import com.nis.domain.configuration.P2pHashCfg;
import com.nis.domain.configuration.P2pKeywordCfg;
import com.nis.domain.maat.MaatCfg;
import com.nis.domain.maat.ToMaatBean;
import com.nis.domain.maat.ToMaatResult;
import com.nis.domain.maat.MaatCfg.DigestCfg;
import com.nis.domain.maat.MaatCfg.GroupCfg;
import com.nis.domain.maat.MaatCfg.IpCfg;
import com.nis.domain.maat.MaatCfg.NumBoundaryCfg;
import com.nis.domain.maat.MaatCfg.StringCfg;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.Constants;
import com.nis.util.DictUtils;
import com.nis.util.StringUtil;
import com.nis.util.StringUtils;
import com.nis.web.dao.configuration.AreaIpCfgDao;
import com.nis.web.dao.configuration.AvContentCfgDao;
import com.nis.web.dao.configuration.StringCfgDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.BaseService;



/**
 * 音视频文本
 * @author dell
 *
 */
@Service
public class AvContentCfgService extends BaseService{
	@Autowired
	protected AvContentCfgDao avContentCfgDao;
	@Autowired
	protected AreaIpCfgDao areaIpCfgDao;
	@Autowired
	protected StringCfgDao stringCfgDao;
	
	
	/********************************voip业务*********************************/
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<CfgIndexInfo> findPage(Page<CfgIndexInfo> page, CfgIndexInfo entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"r"));
		entity.setPage(page);
		List<CfgIndexInfo> list=avContentCfgDao.findVoipList(entity);
		page.setList(list);
		return page;
	}
	/**
	 * 查询account分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	/*public Page<CfgIndexInfo> findAccountPage(Page<CfgIndexInfo> page, CfgIndexInfo entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"r"));
		entity.setPage(page);
		List<CfgIndexInfo> list=avContentCfgDao.findAccountList(entity);
		page.setList(list);
		return page;
	}*/
	/**
	 * 根据索引表信息获取voipIpCfg信息
	 * @param entity
	 * @return
	 */
	public List<AvVoipIpCfg> getVoipIpCfgList(CfgIndexInfo entity) {
		return avContentCfgDao.findVoipIpCfgList(entity);
	}
	/**
	 * 根据索引表信息获取voipAccountCfg信息
	 * @param entity
	 * @return
	 */
	public List<AvVoipAccountCfg> getVoipAccountCfgList(CfgIndexInfo entity) {
		return avContentCfgDao.findVoipAccountCfgList(entity);
	}
	/**
	 * 公共订阅关键字SubscribeId
	 * @param cfgIndexInfo
	 * @return
	 */
	public List<NtcSubscribeIdCfg> getSubscribeIdCfgList(CfgIndexInfo entity) {
		return stringCfgDao.findSubscribeIdCfgListByCfgIndexInfo(entity);
	}
	/**
	 * 根据索引表信息获取voipAccountCfg信息
	 * @param entity
	 * @return
	 */
	public CfgIndexInfo getCfgIndexInfo(CfgIndexInfo entity) {
		CfgIndexInfo cfg=avContentCfgDao.getCfgIndexInfo(entity);
		List<NtcSubscribeIdCfg>  ntcSubscribeList=getSubscribeIdCfgList(cfg);
		List<IpPortCfg> ipPortList = avContentCfgDao.getIpPortList(cfg);
		List<AvVoipAccountCfg>  voipAccounts=getVoipAccountCfgList(cfg);
		cfg.setNtcSubscribeIdCfgList(ntcSubscribeList);
		cfg.setVoipAccounts(voipAccounts);
		cfg.setIpPortList(ipPortList);
		return cfg;
	}
	
	
	public Map<String, List>  exportvoip(CfgIndexInfo entity){
		Map<String, List> dataMap=new HashMap<String, List>();
		List<NtcSubscribeIdCfg>  ntcSubscribeList=getSubscribeIdCfgList(entity);
		List<IpPortCfg> ipPortList = avContentCfgDao.getIpPortList(entity);
		List<AvVoipAccountCfg>  voipAccounts=getVoipAccountCfgList(entity);
		dataMap.put("NTC_SUBSCRIBE_ID", ntcSubscribeList);
		dataMap.put("NTC_VOIP_ACCOUNT", voipAccounts);
		dataMap.put("NTC_UNIVERSAL_IP", ipPortList);
		return dataMap;
	}
	
	public CfgIndexInfo getUrlCfgIndexInfo(CfgIndexInfo entity) {
		CfgIndexInfo cfg=avContentCfgDao.getCfgIndexInfo(entity);
		List<NtcSubscribeIdCfg>  ntcSubscribeList=getSubscribeIdCfgList(cfg);
		List<AvContUrlCfg>  avContUrlList=avContentCfgDao.getAvContUrlList(cfg);
		List<IpPortCfg> ipPortList = avContentCfgDao.getIpPortList(cfg);
		cfg.setNtcSubscribeIdCfgList(ntcSubscribeList);
		cfg.setIpPortList(ipPortList);
		cfg.setAvContUrlCfgList(avContUrlList);
		return cfg;
	}
	
	public Map<String, List>  exportstream(CfgIndexInfo entity){
		Map<String, List> dataMap=new HashMap<String, List>();
		List<NtcSubscribeIdCfg>  ntcSubscribeList=getSubscribeIdCfgList(entity);
		List<AvContUrlCfg>  avContUrlList=avContentCfgDao.getAvContUrlList(entity);
		List<IpPortCfg> ipPortList = avContentCfgDao.getIpPortList(entity);
		dataMap.put("NTC_SUBSCRIBE_ID", ntcSubscribeList);
		dataMap.put("NTC_STREAMING_MEDIA_URL", avContUrlList);
		dataMap.put("NTC_UNIVERSAL_IP", ipPortList);
		return dataMap;
	}
	
	
/*	public CfgIndexInfo getCfgIndexInfo2(CfgIndexInfo entity) {
		CfgIndexInfo cfg=avContentCfgDao.getCfgIndexInfo(entity);
		List<AvVoipAccountCfg>  voipAccounts=new ArrayList<>();
		voipAccounts=getVoipAccountCfgList(entity);
		if(voipAccounts ==null || voipAccounts.size() <=0){
			voipAccounts.add(new AvVoipAccountCfg());
		}
		List<NtcSubscribeIdCfg>  ntcSubscribeList=getSubscribeIdCfgList(entity);
		if(ntcSubscribeList ==null || ntcSubscribeList.size() <=0){
			ntcSubscribeList.add(new NtcSubscribeIdCfg());
		}
		
		cfg.setVoipAccounts(voipAccounts);
		cfg.setNtcSubscribeIdCfgList(ntcSubscribeList);
		
		return cfg;
	}*/
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveVoIpAccountCfg(List<BaseStringCfg<?>> cfgs) {
		for (BaseStringCfg voipAccount : cfgs) {
			AvVoipAccountCfg cfg=new AvVoipAccountCfg();
			BeanUtils.copyProperties(voipAccount, cfg);
			avContentCfgDao.saveAvVoipAccountCfg(cfg);
		}
	}
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveOrUpdateAvVoip(CfgIndexInfo entity){
		//设置区域运营商信息
				setAreaEffectiveIds(entity);
				entity.setIsValid(0);//无效
				entity.setIsAudit(0);//未审核
				if(entity.getCfgId()==null){
					Integer compileId = 0;
					try {
						List<Integer> idList = ConfigServiceUtil.getId(1, 1);
						if(idList!=null && idList.size()>0){
							compileId = idList.get(0);
						}
						entity.setCompileId(compileId);
						entity.setCreateTime(new Date());
						entity.setCreatorId(entity.getCurrentUser().getId());
						avContentCfgDao.insertCfgIndexInfo(entity);
/*						if(entity.getVoipIps()!=null){
							for (AvVoipIpCfg voipIp : entity.getVoipIps()) {
								entity.setVoipIp(voipIp);
								avContentCfgDao.insertAvVoipIp(entity);
							}
						}*/
						if(entity.getIpPortList()!=null){
							for(IpPortCfg cfg:entity.getIpPortList()){
								BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
								avContentCfgDao.saveIpPortCfg(cfg);
							}
						}
						if(entity.getNtcSubscribeIdCfgList()!=null){
							for(NtcSubscribeIdCfg cfg:entity.getNtcSubscribeIdCfgList()){
								if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
									entity.setNtcSubscribeIdCfg(cfg);
									avContentCfgDao.insertAvVoipKeywordCfg(entity);
								}
							}
						}
						if(entity.getVoipAccounts()!=null&&entity.getVoipAccounts().size()>0){
							for(AvVoipAccountCfg voipAccount : entity.getVoipAccounts()){
								entity.setVoipAccount(voipAccount);
								avContentCfgDao.insertAvVoipAccountCfg(entity);
							}
						}
						//保存区域IP信息
						if(entity.getAreaCfg()!=null){
							for(AreaIpCfg cfg:entity.getAreaCfg()){
								cfg.initDefaultValue();
								BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
								areaIpCfgDao.saveAreaIpCfg(cfg);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.info("获取编译ID出错");
						throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+e.getMessage());
					}
					
				}else{
					entity.setEditTime(new Date());
					entity.setEditorId(entity.getCurrentUser().getId());
					avContentCfgDao.updateCfgIndexInfo(entity);
					//无效子配置后，再新增子配置
					//avContentCfgDao.deleteAvVoipIp(entity);
					avContentCfgDao.deleteIpCfg(entity);
					stringCfgDao.deleteSubscribeIdCfgByCfgIndexInfo(entity);
					avContentCfgDao.deleteAvVoipAccountCfg(entity);
					AreaIpCfg area = new AreaIpCfg();
					area.setCompileId(entity.getCompileId());
					area.setFunctionId(entity.getFunctionId());
					areaIpCfgDao.deleteAreaIpCfg(area);
					entity.setCreateTime(new Date());
					entity.setCreatorId(entity.getCurrentUser().getId());
					/*if(entity.getVoipIps()!=null&&entity.getVoipIps().size()>0){
						for (AvVoipIpCfg voipIp : entity.getVoipIps()) {
							entity.setVoipIp(voipIp);
							avContentCfgDao.insertAvVoipIp(entity);
						}
					}*/
					if(entity.getIpPortList()!=null){
						for(IpPortCfg cfg:entity.getIpPortList()){
							BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
							avContentCfgDao.saveIpPortCfg(cfg);
						}
					}
					if(entity.getNtcSubscribeIdCfgList()!=null){
						for(NtcSubscribeIdCfg cfg:entity.getNtcSubscribeIdCfgList()){
							if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
								entity.setNtcSubscribeIdCfg(cfg);
								avContentCfgDao.insertAvVoipKeywordCfg(entity);
							}
						}
					}
					if(entity.getVoipAccounts()!=null&&entity.getVoipAccounts().size()>0){
						for (AvVoipAccountCfg voipAccount : entity.getVoipAccounts()) {
							entity.setVoipAccount(voipAccount);
							avContentCfgDao.insertAvVoipAccountCfg(entity);
						}
					}
					//保存区域IP信息
					if(entity.getAreaCfg()!=null){
						for(AreaIpCfg cfg:entity.getAreaCfg()){
							cfg.initDefaultValue();
							BeanUtils.copyProperties(entity, cfg,new String[]{"cfgDesc","cfgRegionCode","cfgType"});
							areaIpCfgDao.saveAreaIpCfg(cfg);
						}
					}
				}
	}
	
	/*@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveOrUpdateAvVoipAccount(CfgIndexInfo entity){
		//设置区域运营商信息
		setAreaEffectiveIds(entity);
		entity.setIsValid(0);//无效
		entity.setIsAudit(0);//未审核
		if(entity.getCfgId()==null){
			Integer compileId = 0;
			try {
				List<Integer> idList = ConfigServiceUtil.getId(1, 1);
				if(idList!=null && idList.size()>0){
					compileId = idList.get(0);
				}
				entity.setCompileId(compileId);
				entity.setCreateTime(new Date());
				entity.setCreatorId(entity.getCurrentUser().getId());
				avContentCfgDao.insertCfgIndexInfo(entity);
				if(entity.getVoipAccounts()!=null&&entity.getVoipAccounts().size()>0){
					for(AvVoipAccountCfg voipAccount : entity.getVoipAccounts()){
						entity.setVoipAccount(voipAccount);
						avContentCfgDao.insertAvVoipAccountCfg(entity);
					}
				}
				if(entity.getNtcSubscribeIdCfgList()!=null){
					for(NtcSubscribeIdCfg cfg:entity.getNtcSubscribeIdCfgList()){
						if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
							entity.setNtcSubscribeIdCfg(cfg);
							avContentCfgDao.insertAvVoipKeywordCfg(entity);
						}
					}
				}
				//保存区域IP信息
				if(entity.getAreaCfg()!=null){
					for(AreaIpCfg cfg:entity.getAreaCfg()){
						cfg.initDefaultValue();
						BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
						areaIpCfgDao.saveAreaIpCfg(cfg);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("获取编译ID出错");
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+e.getMessage());
			}
			
		}else{
			entity.setEditTime(new Date());
			entity.setEditorId(entity.getCurrentUser().getId());
			avContentCfgDao.updateCfgIndexInfo(entity);
			//无效子配置后，再新增子配置
			stringCfgDao.deleteSubscribeIdCfgByCfgIndexInfo(entity);
			avContentCfgDao.deleteAvVoipAccountCfg(entity);
			AreaIpCfg area = new AreaIpCfg();
			area.setCompileId(entity.getCompileId());
			area.setFunctionId(entity.getFunctionId());
			areaIpCfgDao.deleteAreaIpCfg(area);
			entity.setCreateTime(new Date());
			entity.setCreatorId(entity.getCurrentUser().getId());
			if(entity.getVoipAccounts()!=null&&entity.getVoipAccounts().size()>0){
				for (AvVoipAccountCfg voipAccount : entity.getVoipAccounts()) {
					entity.setVoipAccount(voipAccount);
					avContentCfgDao.insertAvVoipAccountCfg(entity);
				}
			}
			if(entity.getNtcSubscribeIdCfgList()!=null){
				for(NtcSubscribeIdCfg cfg:entity.getNtcSubscribeIdCfgList()){
					if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
						entity.setNtcSubscribeIdCfg(cfg);
						avContentCfgDao.insertAvVoipKeywordCfg(entity);
					}
				}
			}
			//保存区域IP信息
			if(entity.getAreaCfg()!=null){
				for(AreaIpCfg cfg:entity.getAreaCfg()){
					cfg.initDefaultValue();
					BeanUtils.copyProperties(entity, cfg,new String[]{"cfgDesc","cfgRegionCode","cfgType"});
					areaIpCfgDao.saveAreaIpCfg(cfg);
				}
			}
		}
	}*/
	
	
	/**
	 * 
	 * @param isAudit
	 * @param isValid
	 * @param ids compileIds
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void updateAvVoipValid(Integer isAudit,Integer isValid,String compileIds,Integer functionId){
		String[] idArray = compileIds.split(",");
		for(String id :idArray){
			CfgIndexInfo entity = new CfgIndexInfo();
			entity.setCompileId(Integer.parseInt(id));
			entity.setFunctionId(functionId);
			entity.setIsAudit(isAudit);
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			entity.setTableName(CfgIndexInfo.getTablename());
			avContentCfgDao.updateCfgValid(entity);
			entity = getCfgIndexInfo(entity);
			if(entity.getIpPortList()!=null && entity.getIpPortList().size()>0){
				IpPortCfg cfg = new IpPortCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(IpPortCfg.getTablename());
				avContentCfgDao.updateCfgValid(cfg);
			}
			avContentCfgDao.updateAvVoipKeywordCfgt(entity);
			avContentCfgDao.updateAvVoipAccount(entity);
			avContentCfgDao.updateCfgIndexInfo(entity);
			AreaIpCfg areaIpCfg=new AreaIpCfg();
			BeanUtils.copyProperties(entity, areaIpCfg);
			areaIpCfgDao.updateAreaIpCfgValid(areaIpCfg);
		}
	}
	/**
	 * 
	 * @param isAudit
	 * @param isValid
	 * @param ids compileIds
	 */
/*	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void updateAvVoipAccountValid(Integer isAudit,Integer isValid,String compileIds,Integer functionId){
		String[] idArray = compileIds.split(",");
		for(String id :idArray){
			CfgIndexInfo entity = new CfgIndexInfo();
			entity.setCompileId(Integer.parseInt(id));
			entity.setFunctionId(functionId);
			entity.setIsAudit(isAudit);
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			avContentCfgDao.updateAvVoipAccount(entity);
			avContentCfgDao.updateAvVoipKeywordCfgt(entity);
			avContentCfgDao.updateCfgIndexInfo(entity);
			AreaIpCfg areaIpCfg=new AreaIpCfg();
			BeanUtils.copyProperties(entity, areaIpCfg);
			areaIpCfgDao.updateAreaIpCfgValid(areaIpCfg);
		}
	}*/
	/**
	 * 
	 * @param isAudit
	 * @param isValid
	 * @param ids 编译Id
	 * @param functionId
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void auditAvVoip(Integer isAudit,Integer isValid,Integer functionId,String id,Date auditTime){
		CfgIndexInfo entity = new CfgIndexInfo();
		
		List<CfgIndexInfo> list = new ArrayList();
		//List<AvVoipIpCfg> voipIpList = new ArrayList();
		List<IpPortCfg> ipPortList =new ArrayList();
		List <NtcSubscribeIdCfg> ntcList = new ArrayList();
		List<AvVoipAccountCfg> accountList = new ArrayList();
		List<AreaIpCfg> areaIpCfgList = new ArrayList();
		
		CfgIndexInfo searchCfg=new CfgIndexInfo();
		searchCfg.setCfgId(Long.parseLong(id));
		
		entity = avContentCfgDao.getCfgIndexInfo(searchCfg);
		entity.setIsAudit(isAudit);
		entity.setIsValid(isValid);
		entity.setAuditorId(UserUtils.getUser().getId());
		entity.setAuditTime(auditTime);
		
		avContentCfgDao.updateAvVoipIp(entity);
		avContentCfgDao.updateAvVoipAccount(entity);
		avContentCfgDao.updateAvVoipKeywordCfgt(entity);
		
		avContentCfgDao.updateCfgIndexInfo(entity);
		AreaIpCfg areaIpCfg=new AreaIpCfg();
		BeanUtils.copyProperties(entity, areaIpCfg,new String[]{"cfgRegionCode","cfgType","cfgId"});
		areaIpCfgDao.updateAreaIpCfg(areaIpCfg);
		
		//voipIpList=avContentCfgDao.findVoipIpCfgList(entity);
		ipPortList = avContentCfgDao.getIpPortList(entity);
		accountList=avContentCfgDao.findVoipAccountCfgList(entity);
		ntcList=stringCfgDao.findSubscribeIdCfgListByCfgIndexInfo(entity);
		areaIpCfgList=areaIpCfgDao.getByCompileId(entity.getCompileId());
		
		ToMaatBean maatBean = new ToMaatBean();		
		MaatCfg maatCfg = new MaatCfg();
		List<MaatCfg> configCompileList = new ArrayList();
		List<GroupCfg> groupRelationList = new ArrayList();
		List<IpCfg> ipRegionList = new ArrayList();
		List<StringCfg> strRegionList = new ArrayList();
		List<NumBoundaryCfg> numRegionList = new ArrayList();
		List<DigestCfg> digestRegionList = new ArrayList();
		List<IpCfg> areaIpRegionList = new ArrayList();
		
		
		if(isAudit==1){
	/*		if(!StringUtil.isEmpty(voipIpList)){
				Map<String,List> ipMap = cfgConvert(ipRegionList,voipIpList,1,entity,groupRelationList);
				groupRelationList=ipMap.get("groupList");
				ipRegionList=ipMap.get("dstList");
			}*/
			if(!StringUtil.isEmpty(ipPortList)){
				Map<String,List> ipMap = cfgConvert(ipRegionList,ipPortList,1,entity,groupRelationList);
				groupRelationList=ipMap.get("groupList");
				ipRegionList=ipMap.get("dstList");
				if(ipMap.get("numRegionList")!=null){
					numRegionList.addAll(ipMap.get("numRegionList"));

				}
			}
			
			if(!StringUtil.isEmpty(ntcList)){
				Map<String,List> ntcMap = cfgConvert(strRegionList,ntcList,2,entity,groupRelationList);
				groupRelationList=ntcMap.get("groupList");
				strRegionList=ntcMap.get("dstList");
			}
			if(!StringUtil.isEmpty(accountList)){
				Map<String,List> accountMap = cfgConvert(strRegionList,accountList,2,entity,groupRelationList);
				groupRelationList=accountMap.get("groupList");
				strRegionList=accountMap.get("dstList");
			}
			if(!StringUtil.isEmpty(areaIpCfgList)){
				Map<String,List> areaMap = cfgConvert(areaIpRegionList,areaIpCfgList,1,entity,groupRelationList);
				groupRelationList=areaMap.get("groupList");
				areaIpRegionList=areaMap.get("dstList");
			}
		}
		
		//构造提交综合服务参数格式，一条配置提交一次综合服务
		if(isAudit==1){
			maatCfg.initDefaultValue();
			BeanUtils.copyProperties(entity, maatCfg);
			maatCfg.setAction(entity.getAction());
			maatCfg.setAuditTime(auditTime);
			maatCfg.setIpRegionList(ipRegionList);
			maatCfg.setStrRegionList(strRegionList);
			maatCfg.setNumRegionList(numRegionList);
			maatCfg.setDigestRegionList(digestRegionList);
			maatCfg.setGroupRelationList(groupRelationList);
			maatCfg.setGroupNum(groupRelationList.size());
			maatCfg.setAreaIpRegionList(areaIpRegionList);
			maatCfg.setIsValid(entity.getIsValid());
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(auditTime);
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.INSERT_ACTION);
			//调用服务接口下发配置数据
			String json=gsonToJson(maatBean);
			logger.info("voip 配置下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
			logger.info("voip 配置下发响应信息："+result.getMsg());

		}else if(isAudit==3){
			maatCfg.setCompileId(entity.getCompileId());
			maatCfg.setServiceId(entity.getServiceId());
			maatCfg.setIsValid(0);//无效
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(auditTime);
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.UPDATE_ACTION);
			//调用服务接口取消配置
			String json=gsonToJson(maatBean);
			logger.info("voip 配置下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json,1);
			logger.info("voip 配置取消配置响应信息："+result.getMsg());
		}
		
		
	}
	/********************************voip account业务*********************************/
	/**
	 * 
	 * @param isAudit
	 * @param isValid
	 * @param ids 编译Id
	 * @param functionId
	 */
/*	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void auditAvVoipAccount(Integer isAudit,Integer isValid,Integer functionId,String compileId,Date auditTime){
		CfgIndexInfo entity = new CfgIndexInfo();
		
		List<CfgIndexInfo> list = new ArrayList();
		List<AvVoipAccountCfg> accountList = new ArrayList();
		List <NtcSubscribeIdCfg> ntcList = new ArrayList();
		List<AreaIpCfg> areaIpCfgList = new ArrayList();
		
		CfgIndexInfo searchCfg=new CfgIndexInfo();
		searchCfg.setCompileId(Integer.parseInt(compileId));
		
		entity = avContentCfgDao.getCfgIndexInfo(searchCfg);
		entity.setIsAudit(isAudit);
		entity.setIsValid(isValid);
		entity.setAuditorId(UserUtils.getUser().getId());
		entity.setAuditTime(auditTime);
		
		avContentCfgDao.updateAvVoipAccount(entity);
		avContentCfgDao.updateAvVoipKeywordCfgt(entity);
		
		avContentCfgDao.updateCfgIndexInfo(entity);
		AreaIpCfg areaIpCfg=new AreaIpCfg();
		BeanUtils.copyProperties(entity, areaIpCfg,new String[]{"cfgRegionCode","cfgType","cfgId"});
		areaIpCfgDao.updateAreaIpCfg(areaIpCfg);
		
		ntcList=stringCfgDao.findSubscribeIdCfgListByCfgIndexInfo(entity);
		accountList=avContentCfgDao.findVoipAccountCfgList(entity);
		areaIpCfgList=areaIpCfgDao.getByCompileId(entity.getCompileId());
		
		ToMaatBean maatBean = new ToMaatBean();		
		MaatCfg maatCfg = new MaatCfg();
		List<MaatCfg> configCompileList = new ArrayList();
		List<GroupCfg> groupRelationList = new ArrayList();
		List<IpCfg> ipRegionList = new ArrayList();
		List<StringCfg> strRegionList = new ArrayList();
		List<NumBoundaryCfg> numRegionList = new ArrayList();
		List<DigestCfg> digestRegionList = new ArrayList();
		List<IpCfg> areaIpRegionList = new ArrayList();
		
		if(isAudit==1){
			
			if(!StringUtil.isEmpty(ntcList)){
				Map<String,List> accountMap = cfgConvert(strRegionList,ntcList,2,entity,groupRelationList);
				groupRelationList=accountMap.get("groupList");
				strRegionList=accountMap.get("dstList");
			}
			if(!StringUtil.isEmpty(accountList)){
				Map<String,List> accountMap = cfgConvert(strRegionList,accountList,2,entity,groupRelationList);
				groupRelationList=accountMap.get("groupList");
				strRegionList=accountMap.get("dstList");
			}
			if(!StringUtil.isEmpty(areaIpCfgList)){
				Map<String,List> areaMap = cfgConvert(areaIpRegionList,areaIpCfgList,1,entity,groupRelationList);
				groupRelationList=areaMap.get("groupList");
				areaIpRegionList=areaMap.get("dstList");
			}
		}
		
		//构造提交综合服务参数格式，一条配置提交一次综合服务
		if(isAudit==1){
			maatCfg.initDefaultValue();
			BeanUtils.copyProperties(entity, maatCfg);
			maatCfg.setAction(entity.getAction());
			maatCfg.setAuditTime(auditTime);
			maatCfg.setIpRegionList(ipRegionList);
			maatCfg.setStrRegionList(strRegionList);
			maatCfg.setNumRegionList(numRegionList);
			maatCfg.setDigestRegionList(digestRegionList);
			maatCfg.setGroupRelationList(groupRelationList);
			maatCfg.setGroupNum(groupRelationList.size());
			maatCfg.setAreaIpRegionList(areaIpRegionList);
			maatCfg.setIsValid(entity.getIsValid());
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(auditTime);
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.INSERT_ACTION);
			//调用服务接口下发配置数据
			String json=gsonToJson(maatBean);
			logger.info("voip Account配置下发配置参数："+json);
			System.err.println(json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
			logger.info("voip Account配置下发响应信息："+result.getMsg());
			
		}else if(isAudit==3){
			maatCfg.setCompileId(entity.getCompileId());
			maatCfg.setServiceId(entity.getServiceId());
			maatCfg.setIsValid(0);//无效
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(auditTime);
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.UPDATE_ACTION);
			//调用服务接口取消配置
			String json=gsonToJson(maatBean);
			logger.info("voip Account配置下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json,1);
			logger.info("voip Account配置取消配置响应信息："+result.getMsg());
		}
		
		
	}*/
	/********************************voip业务*********************************/
	
	
	/********************************contIp业务*********************************/
	/**
	 * contIp查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<BaseIpCfg> findContIpPage(Page page, BaseIpCfg entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"r"));
		entity.setPage(page);
		entity.setTableName("av_cont_ip_cfg");
		List<BaseIpCfg> list=avContentCfgDao.findIpList(entity);
		page.setList(list);
		return page;
	}
	
	/**
	 * contUp根据id查询contIpCfg信息
	 * @param entity
	 * @return
	 */
	public BaseIpCfg getContIpCfgById(BaseIpCfg entity) {
		entity.setTableName("av_cont_ip_cfg");
		BaseIpCfg cfg = avContentCfgDao.findIpCfgById(entity);
		NtcSubscribeIdCfg subscribeIdCfg = new NtcSubscribeIdCfg();
		BeanUtils.copyProperties(cfg, subscribeIdCfg, new String[] {"cfgId","isValid"});
		List<NtcSubscribeIdCfg> list = stringCfgDao.findSubscribeIdCfgList(subscribeIdCfg);
		cfg.setNtcSubscribeIdCfgList(list);
		return cfg;
	}
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveOrUpdateContIp(BaseIpCfg entity,String areaCfgIds){
		Date createTime=new Date();
		entity.setTableName("av_cont_ip_cfg");
		//设置区域运营商信息
		setAreaEffectiveIds(entity);
		//新增
		if(entity.getCfgId()==null){
			entity.setCreatorId(UserUtils.getUser().getId());
			entity.setCreateTime(createTime);
			entity.setIsValid(0);
			entity.setIsAudit(0);
			//调用服务接口获取compileId
			List<Integer> compileIds = new ArrayList<Integer>();
			try {
				compileIds = ConfigServiceUtil.getId(1,1);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("获取编译ID出错");
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+e.getMessage());
			}
			
			// 添加subscribeId
			if(entity.getNtcSubscribeIdCfgList()!=null){
				for(NtcSubscribeIdCfg cfg:entity.getNtcSubscribeIdCfgList()){
					if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
						entity.setCompileId(compileIds.get(0));
						BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
						stringCfgDao.saveSubscribeIdCfg(cfg);
					}
				}
			}
			
			if(compileIds != null && compileIds.size() >0  && compileIds.get(0) != 0){
				entity.setCompileId(compileIds.get(0));
				
				//保存区域IP信息
				if(entity.getAreaCfg()!=null&&entity.getAreaCfg().size()>0){
					for (AreaIpCfg areaIpCfg : entity.getAreaCfg()) {
						BeanUtils.copyProperties(entity, areaIpCfg,new String[]{"cfgRegionCode"
																				,"cfgType"
																				,"ipType"
																				,"ipPattern"
																				,"srcIpAddress"
																				,"portPattern"
																				,"srcPort"
																				,"destPort"
																				,"protocol"
																				,"direction"
																				,"protocolId"
																				});
						areaIpCfgDao.saveAreaIpCfg(areaIpCfg);
					}
				}
				
				//保存cfgIndexInfo
				avContentCfgDao.insertIp(entity);
			}else{
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>");
			}
			
			//修改
		}else{
			Date editTime=new Date();
			entity.setIsValid(0);
			entity.setIsAudit(0);
			
			areaCfgIds=!StringUtil.isEmpty(areaCfgIds)? ","+areaCfgIds:"";
			
			if(!StringUtil.isEmpty(entity.getAreaCfg())){
				for (AreaIpCfg areaIpCfg : entity.getAreaCfg()) {
					if(!StringUtil.isEmpty(areaIpCfg.getCfgId())){
						if(areaCfgIds.contains(","+areaIpCfg.getCfgId()+",")){
							areaCfgIds=areaCfgIds.replace(areaIpCfg.getCfgId()+",", "");
						}
						//修改
						entity.setEditorId(UserUtils.getUser().getId());
						entity.setEditTime(editTime);
						BeanUtils.copyProperties(entity, areaIpCfg,new String[]{"cfgRegionCode"
																				,"cfgType"
																				,"cfgId"
																				,"ipType"
																				,"ipPattern"
																				,"srcIpAddress"
																				,"portPattern"
																				,"srcPort"
																				,"destPort"
																				,"protocol"
																				,"direction"
																				,"protocolId"
																				});
						areaIpCfgDao.updateAreaIpCfg(areaIpCfg);
					}else{
						//新增
						entity.setCreatorId(UserUtils.getUser().getId());
						entity.setCreateTime(createTime);
						BeanUtils.copyProperties(entity, areaIpCfg,new String[]{"cfgRegionCode"
								,"cfgType"
								,"ipType"
								,"ipPattern"
								,"srcIpAddress"
								,"portPattern"
								,"srcPort"
								,"destPort"
								,"protocol"
								,"direction"
								,"protocolId"
								});
						areaIpCfgDao.saveAreaIpCfg(areaIpCfg);
					}
				}
			}
			//delete 真是删除areaIpCfg信息
			if(!StringUtil.isEmpty(areaCfgIds.replaceAll(",", ""))){
				areaCfgIds=areaCfgIds.substring(1,areaCfgIds.length());
				for (String cfgId : areaCfgIds.split(",")) {
					AreaIpCfg areaIpCfg=new AreaIpCfg();
					areaIpCfg.setCfgId(Long.parseLong(cfgId));
					areaIpCfgDao.deleteAreaIpCfgByCfgId(areaIpCfg);
				}
			}
			
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(editTime);
			avContentCfgDao.updateIp(entity);
			
			entity.setCreateTime(new Date());
			entity.setCreatorId(entity.getCurrentUser().getId());
			// 删除SubscribeId配置再进行添加
			NtcSubscribeIdCfg subscribeIdCfg = new NtcSubscribeIdCfg();
			BeanUtils.copyProperties(entity, subscribeIdCfg,new String[]{"cfgId","cfgType"});
			stringCfgDao.deleteSubscribeIdCfg(subscribeIdCfg);
			if(entity.getNtcSubscribeIdCfgList()!=null){
				for(NtcSubscribeIdCfg cfg:entity.getNtcSubscribeIdCfgList()){
					if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
						BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
						stringCfgDao.saveSubscribeIdCfg(cfg);
					}
				}
			}			
		}
	}
	
	/**
	 * 
	 * @param isAudit
	 * @param isValid
	 * @param ids compileIds
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void updateContIpValid(Integer isAudit,Integer isValid,String ids,Integer functionId){
		String[] idArray = ids.split(",");
		for(String id :idArray){
			BaseIpCfg entity = new BaseIpCfg();
			entity.setCfgId(Long.parseLong(id));
			BaseIpCfg ipCfg = this.getContIpCfgById(entity);// 获取CompileId
			entity.setCompileId(ipCfg.getCompileId());
			entity.setTableName("av_cont_ip_cfg");
			entity.setFunctionId(functionId);
			entity.setIsAudit(isAudit);
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			avContentCfgDao.updateIp(entity);
			// 查询域配置信息
			BaseIpCfg cfg = this.getContIpCfgById(entity);
			if(cfg.getNtcSubscribeIdCfgList() != null && cfg.getNtcSubscribeIdCfgList().size()>0) {
				NtcSubscribeIdCfg subscribeIdCfg = new NtcSubscribeIdCfg();
				BeanUtils.copyProperties(cfg, subscribeIdCfg, new String[] {"cfgId","cfgType","tableName"});
				stringCfgDao.updateSubscribeIdCfg(subscribeIdCfg);
			}
			
			AreaIpCfg areaIpCfg=new AreaIpCfg();
			BeanUtils.copyProperties(entity, areaIpCfg);
			areaIpCfgDao.updateAreaIpCfgValid(areaIpCfg);
		}
	}
	/**
	 * 
	 * @param isAudit
	 * @param isValid
	 * @param ids cfgId
	 * @param functionId
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void auditContIp(Integer isAudit,Integer isValid,Integer functionId,String id,Date auditTime){
		BaseIpCfg entity = new BaseIpCfg();
		entity.setTableName("av_cont_ip_cfg");
		
		List<BaseIpCfg> list = new ArrayList();
		List<AreaIpCfg> areaIpCfgList = new ArrayList();
		
		entity.setCompileId(Integer.parseInt(id));
		entity = avContentCfgDao.findIpCfgById(entity);
		entity.setTableName("av_cont_ip_cfg");
		entity.setIsAudit(isAudit);
		entity.setIsValid(isValid);
		entity.setAuditorId(UserUtils.getUser().getId());
		entity.setAuditTime(auditTime);
		
		avContentCfgDao.updateIp(entity);
		AreaIpCfg areaIpCfg=new AreaIpCfg();
		BeanUtils.copyProperties(entity, areaIpCfg,new String[]{"cfgRegionCode"
				,"cfgType"
				,"cfgId"
				,"ipType"
				,"ipPattern"
				,"srcIpAddress"
				,"portPattern"
				,"srcPort"
				,"destPort"
				,"protocol"
				,"direction"
				,"protocolId"
				});
		
		areaIpCfgDao.updateAreaIpCfg(areaIpCfg);
		
		list=avContentCfgDao.getIpList(entity);
		areaIpCfgList=areaIpCfgDao.getByCompileId(entity.getCompileId());
		
		ToMaatBean maatBean = new ToMaatBean();		
		MaatCfg maatCfg = new MaatCfg();
		List<MaatCfg> configCompileList = new ArrayList();
		List<GroupCfg> groupRelationList = new ArrayList();
		List<IpCfg> ipRegionList = new ArrayList();
		List<StringCfg> strRegionList = new ArrayList();
		List<NumBoundaryCfg> numRegionList = new ArrayList();
		List<DigestCfg> digestRegionList = new ArrayList();
		List<IpCfg> areaIpRegionList = new ArrayList();
		
		if(isAudit==1){
			if(!StringUtil.isEmpty(list)){
				Map<String,List> ipMap = cfgConvert(ipRegionList,list,1,entity,groupRelationList);
				groupRelationList=ipMap.get("groupList");
				ipRegionList=ipMap.get("dstList");
				if (ipMap.get("numRegionList") != null) {
					numRegionList.addAll(ipMap.get("numRegionList"));
				}
				
			}
			if(!StringUtil.isEmpty(areaIpCfgList)){
				Map<String,List> areaMap = cfgConvert(areaIpRegionList,areaIpCfgList,1,entity,groupRelationList);
				groupRelationList=areaMap.get("groupList");
				areaIpRegionList=areaMap.get("dstList");
			}
		}
		
		// 修改审核状态
		BaseIpCfg baseIpCfg = this.getContIpCfgById(entity);
		if(!StringUtil.isEmpty(baseIpCfg.getNtcSubscribeIdCfgList())) {
			NtcSubscribeIdCfg cfg = new NtcSubscribeIdCfg();
			BeanUtils.copyProperties(entity, cfg, new String[] {"cfgId","cfgType"});
			stringCfgDao.updateSubscribeIdCfg(cfg);
			if(isAudit==1) {
				Map<String,List> map = cfgConvert(strRegionList,baseIpCfg.getNtcSubscribeIdCfgList(),2,entity,groupRelationList);
				groupRelationList=map.get("groupList");
				strRegionList=map.get("dstList");
			}
		}
		
		//构造提交综合服务参数格式，一条配置提交一次综合服务
		if(isAudit==1){
			maatCfg.initDefaultValue();
			BeanUtils.copyProperties(entity, maatCfg);
			maatCfg.setAction(entity.getAction());
			maatCfg.setAuditTime(auditTime);
			maatCfg.setIpRegionList(ipRegionList);
			maatCfg.setStrRegionList(strRegionList);
			maatCfg.setNumRegionList(numRegionList);
			maatCfg.setDigestRegionList(digestRegionList);
			maatCfg.setGroupRelationList(groupRelationList);
			maatCfg.setGroupNum(groupRelationList.size());
			maatCfg.setAreaIpRegionList(areaIpRegionList);
			maatCfg.setIsValid(entity.getIsValid());
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(auditTime);
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.INSERT_ACTION);
			//调用服务接口下发配置数据
			String json=gsonToJson(maatBean);
			logger.info("contIp配置下发配置参数："+json);
			// 调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
			logger.info("contIp配置下发响应信息："+result.getMsg());

		}else if(isAudit==3){
			maatCfg.setCompileId(entity.getCompileId());
			maatCfg.setServiceId(entity.getServiceId());
			maatCfg.setIsValid(0);//无效
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(auditTime);
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.UPDATE_ACTION);
			//调用服务接口取消配置
			String json=gsonToJson(maatBean);
			logger.info("contIp配置下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json,1);
			logger.info("contIp配置取消配置响应信息："+result.getMsg());
		}
	}
	/********************************contIp业务*********************************/
	
	
	/********************************picIp业务*********************************/
	/**
	 * picIp查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<BaseIpCfg> findPicIpPage(Page page, BaseIpCfg entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"r"));
		entity.setPage(page);
		entity.setTableName("av_pic_ip_cfg");
		List<BaseIpCfg> list=avContentCfgDao.findIpList(entity);
		page.setList(list);
		return page;
	}
	
	/**
	 * picIp根据id查询contIpCfg信息
	 * @param entity
	 * @return
	 */
	public BaseIpCfg getPicIpCfgById(BaseIpCfg entity) {
		entity.setTableName("av_pic_ip_cfg");
		BaseIpCfg cfg = avContentCfgDao.findIpCfgById(entity);
		NtcSubscribeIdCfg subscribeIdCfg = new NtcSubscribeIdCfg();
		BeanUtils.copyProperties(cfg, subscribeIdCfg,new String[] {"cfgId","isValid"});
		List<NtcSubscribeIdCfg> list = stringCfgDao.findSubscribeIdCfgList(subscribeIdCfg);
		cfg.setNtcSubscribeIdCfgList(list);
		return cfg;
	}
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveOrUpdatePicIp(BaseIpCfg entity,String areaCfgIds){
		Date createTime=new Date();
		entity.setTableName("av_pic_ip_cfg");
		//设置区域运营商信息
		setAreaEffectiveIds(entity);
		//新增
		if(entity.getCfgId()==null){
			entity.setCreatorId(UserUtils.getUser().getId());
			entity.setCreateTime(createTime);
			entity.setIsValid(0);
			entity.setIsAudit(0);
			//调用服务接口获取compileId
			List<Integer> compileIds = new ArrayList<Integer>();
			try {
				compileIds = ConfigServiceUtil.getId(1,1);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("获取编译ID出错");
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+e.getMessage());
			}
			
			// 添加subscribeId
			if(entity.getNtcSubscribeIdCfgList()!=null){
				for(NtcSubscribeIdCfg cfg:entity.getNtcSubscribeIdCfgList()){
					if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
						 entity.setCompileId(compileIds.get(0));
						 BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
						 stringCfgDao.saveSubscribeIdCfg(cfg);
					}
				}
			}			
			
			if(compileIds != null && compileIds.size() >0  && compileIds.get(0) != 0){
				entity.setCompileId(compileIds.get(0));
				
				//保存区域IP信息
				if(entity.getAreaCfg()!=null&&entity.getAreaCfg().size()>0){
					for (AreaIpCfg areaIpCfg : entity.getAreaCfg()) {
						BeanUtils.copyProperties(entity, areaIpCfg,new String[]{"cfgRegionCode"
								,"cfgType"
								,"ipType"
								,"ipPattern"
								,"srcIpAddress"
								,"portPattern"
								,"srcPort"
								,"destPort"
								,"protocol"
								,"direction"
								,"protocolId"
								});
						areaIpCfgDao.saveAreaIpCfg(areaIpCfg);
					}
				}
				
				//保存cfgIndexInfo
				avContentCfgDao.insertIp(entity);
			}else{
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>");
			}
			
			//修改
		}else{
			Date editTime=new Date();
			entity.setIsValid(0);
			entity.setIsAudit(0);
			
			areaCfgIds=!StringUtil.isEmpty(areaCfgIds)? ","+areaCfgIds:"";
			
			if(!StringUtil.isEmpty(entity.getAreaCfg())){
				for (AreaIpCfg areaIpCfg : entity.getAreaCfg()) {
					if(!StringUtil.isEmpty(areaIpCfg.getCfgId())){
						if(areaCfgIds.contains(","+areaIpCfg.getCfgId()+",")){
							areaCfgIds=areaCfgIds.replace(areaIpCfg.getCfgId()+",", "");
						}
						//修改
						entity.setEditorId(UserUtils.getUser().getId());
						entity.setEditTime(editTime);
						BeanUtils.copyProperties(entity, areaIpCfg,new String[]{"cfgRegionCode"
								,"cfgType"
								,"cfgId"
								,"ipType"
								,"ipPattern"
								,"srcIpAddress"
								,"portPattern"
								,"srcPort"
								,"destPort"
								,"protocol"
								,"direction"
								,"protocolId"
								});
						areaIpCfgDao.updateAreaIpCfg(areaIpCfg);
					}else{
						//新增
						entity.setCreatorId(UserUtils.getUser().getId());
						entity.setCreateTime(createTime);
						BeanUtils.copyProperties(entity, areaIpCfg,new String[]{"cfgRegionCode"
								,"cfgType"
								,"ipType"
								,"ipPattern"
								,"srcIpAddress"
								,"portPattern"
								,"srcPort"
								,"destPort"
								,"protocol"
								,"direction"
								,"protocolId"
								});
						areaIpCfgDao.saveAreaIpCfg(areaIpCfg);
					}
				}
			}
			//delete 真是删除areaIpCfg信息
			if(!StringUtil.isEmpty(areaCfgIds.replaceAll(",", ""))){
				areaCfgIds=areaCfgIds.substring(1,areaCfgIds.length());
				for (String cfgId : areaCfgIds.split(",")) {
					AreaIpCfg areaIpCfg=new AreaIpCfg();
					areaIpCfg.setCfgId(Long.parseLong(cfgId));
					areaIpCfgDao.deleteAreaIpCfgByCfgId(areaIpCfg);
				}
			}
			
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(editTime);
			avContentCfgDao.updateIp(entity);
			
			entity.setCreateTime(new Date());
			entity.setCreatorId(entity.getCurrentUser().getId());
			// 删除SubscribeId配置再进行添加
			NtcSubscribeIdCfg subscribeIdCfg = new NtcSubscribeIdCfg();
			BeanUtils.copyProperties(entity, subscribeIdCfg,new String[]{"cfgId","cfgType"});
			stringCfgDao.deleteSubscribeIdCfg(subscribeIdCfg);
			if(entity.getNtcSubscribeIdCfgList()!=null){
				for(NtcSubscribeIdCfg cfg:entity.getNtcSubscribeIdCfgList()){
					if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
						BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
						stringCfgDao.saveSubscribeIdCfg(cfg);
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param isAudit
	 * @param isValid
	 * @param ids compileIds
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void updatePicIpValid(Integer isAudit,Integer isValid,String ids,Integer functionId){
		String[] idArray = ids.split(",");
		for(String id :idArray){
			BaseIpCfg entity = new BaseIpCfg();
			entity.setCfgId(Long.parseLong(id));
			BaseIpCfg picIpCfg = this.getPicIpCfgById(entity);// 获取CompileId
			entity.setCompileId(picIpCfg.getCompileId());
			entity.setTableName("av_pic_ip_cfg");
			entity.setFunctionId(functionId);
			entity.setIsAudit(isAudit);
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			avContentCfgDao.updateIp(entity);
			// 查询域配置信息
			BaseIpCfg cfg = this.getPicIpCfgById(entity);
			if(cfg.getNtcSubscribeIdCfgList() != null && cfg.getNtcSubscribeIdCfgList().size()>0) {
				NtcSubscribeIdCfg subscribeIdCfg = new NtcSubscribeIdCfg();
				BeanUtils.copyProperties(cfg, subscribeIdCfg, new String[] {"cfgId","cfgTyep","tableName"});
				stringCfgDao.updateSubscribeIdCfg(subscribeIdCfg);
			}
			
			AreaIpCfg areaIpCfg=new AreaIpCfg();
			BeanUtils.copyProperties(entity, areaIpCfg);
			areaIpCfgDao.updateAreaIpCfgValid(areaIpCfg);
		}
	}
	/**
	 * 
	 * @param isAudit
	 * @param isValid
	 * @param ids cfgId
	 * @param functionId
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void auditPicIp(Integer isAudit,Integer isValid,Integer functionId,String id,Date auditTime){
		BaseIpCfg entity = new BaseIpCfg();
		entity.setTableName("av_pic_ip_cfg");
		List<BaseIpCfg> list = new ArrayList();
		List<AreaIpCfg> areaIpCfgList = new ArrayList();
		
		entity.setCompileId(Integer.parseInt(id));
		entity = avContentCfgDao.findIpCfgById(entity);
		entity.setTableName("av_pic_ip_cfg");
		entity.setIsAudit(isAudit);
		entity.setIsValid(isValid);
		entity.setAuditorId(UserUtils.getUser().getId());
		entity.setAuditTime(auditTime);
		
		
		avContentCfgDao.updateIp(entity);
		AreaIpCfg areaIpCfg=new AreaIpCfg();
		BeanUtils.copyProperties(entity, areaIpCfg,new String[]{"cfgRegionCode"
				,"cfgType"
				,"cfgId"
				,"ipType"
				,"ipPattern"
				,"srcIpAddress"
				,"portPattern"
				,"srcPort"
				,"destPort"
				,"protocol"
				,"direction"
				,"protocolId"
				});
		areaIpCfgDao.updateAreaIpCfg(areaIpCfg);
		
		list=avContentCfgDao.getIpList(entity);
		areaIpCfgList=areaIpCfgDao.getByCompileId(entity.getCompileId());
		
		ToMaatBean maatBean = new ToMaatBean();		
		MaatCfg maatCfg = new MaatCfg();
		List<MaatCfg> configCompileList = new ArrayList();
		List<GroupCfg> groupRelationList = new ArrayList();
		List<IpCfg> ipRegionList = new ArrayList();
		List<StringCfg> strRegionList = new ArrayList();
		List<NumBoundaryCfg> numRegionList = new ArrayList();
		List<DigestCfg> digestRegionList = new ArrayList();
		List<IpCfg> areaIpRegionList = new ArrayList();
		
		if(isAudit==1){
			if(!StringUtil.isEmpty(list)){
				
				Map<String,List> ipMap = cfgConvert(ipRegionList,list,1,entity,groupRelationList);
				groupRelationList=ipMap.get("groupList");
				ipRegionList=ipMap.get("dstList");
				if (ipMap.get("numRegionList") != null) {
					numRegionList.addAll(ipMap.get("numRegionList"));
				}
			}
			
			if(!StringUtil.isEmpty(areaIpCfgList)){
				Map<String,List> areaMap = cfgConvert(areaIpRegionList,areaIpCfgList,1,entity,groupRelationList);
				groupRelationList=areaMap.get("groupList");
				areaIpRegionList=areaMap.get("dstList");
			}
		}
		
		// 修改审核状态
		BaseIpCfg baseIpCfg = this.getPicIpCfgById(entity);
		if(!StringUtil.isEmpty(baseIpCfg.getNtcSubscribeIdCfgList())) {
			NtcSubscribeIdCfg cfg = new NtcSubscribeIdCfg();
			BeanUtils.copyProperties(entity, cfg, new String[] {"cfgId","cfgType"});
			stringCfgDao.updateSubscribeIdCfg(cfg);
			if(isAudit==1) {
				Map<String,List> map = cfgConvert(strRegionList,baseIpCfg.getNtcSubscribeIdCfgList(),2,entity,groupRelationList);
				groupRelationList=map.get("groupList");
				strRegionList=map.get("dstList");
			}
		}
		
		//构造提交综合服务参数格式，一条配置提交一次综合服务
		if(isAudit==1){
			maatCfg.initDefaultValue();
			BeanUtils.copyProperties(entity, maatCfg);
			maatCfg.setAction(entity.getAction());
			maatCfg.setAuditTime(auditTime);
			maatCfg.setIpRegionList(ipRegionList);
			maatCfg.setStrRegionList(strRegionList);
			maatCfg.setNumRegionList(numRegionList);
			maatCfg.setDigestRegionList(digestRegionList);
			maatCfg.setGroupRelationList(groupRelationList);
			maatCfg.setGroupNum(groupRelationList.size());
			maatCfg.setAreaIpRegionList(areaIpRegionList);
			maatCfg.setIsValid(entity.getIsValid());
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(auditTime);
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.INSERT_ACTION);
			//调用服务接口下发配置数据
			String json=gsonToJson(maatBean);
			logger.info("picIp配置下发配置参数："+json);
			//TODO 调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
			logger.info("picIp配置下发响应信息："+result.getMsg());
			
		}else if(isAudit==3){
			maatCfg.setCompileId(entity.getCompileId());
			maatCfg.setServiceId(entity.getServiceId());
			maatCfg.setIsValid(0);//无效
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(auditTime);
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.UPDATE_ACTION);
			//调用服务接口取消配置
			String json=gsonToJson(maatBean);
			logger.info("picIp配置下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json,1);
			logger.info("picIp配置取消配置响应信息："+result.getMsg());
		}
	}
	/********************************picIp业务*********************************/
	
	
	
	/********************************contUrl业务*********************************/
	/**
	 * picIp查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<BaseStringCfg> findContUrlPage(Page page, BaseStringCfg entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"r"));
		entity.setPage(page);
		entity.setTableName("av_cont_url_cfg");
		List<BaseStringCfg> list=avContentCfgDao.findStringList(entity);
		page.setList(list);
		return page;
	}
	
	/**
	 * ContUrl根据id查询contIpCfg信息
	 * @param entity
	 * @return
	 */
	public BaseStringCfg<NtcSubscribeIdCfg> getContUrlCfgById(BaseStringCfg entity) {
		entity.setTableName("av_cont_url_cfg");
		BaseStringCfg cfg = avContentCfgDao.findStringCfgById(entity);
		NtcSubscribeIdCfg subscribeIdCfg = new NtcSubscribeIdCfg();
		BeanUtils.copyProperties(cfg, subscribeIdCfg, new String[] {"cfgId","isValid"});
		List<NtcSubscribeIdCfg> list = stringCfgDao.findSubscribeIdCfgList(subscribeIdCfg);
		CfgIndexInfo cfgInfo=new CfgIndexInfo();
		cfgInfo.setCompileId(cfg.getCompileId());
		List<IpPortCfg> ipPortList=avContentCfgDao.getIpPortList(cfgInfo);
		cfg.setNtcSubscribeIdCfgList(list);
		
		return cfg;
	}
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveContUrlCfg(List<BaseStringCfg<?>> cfgs){
		for(BaseStringCfg cfg : cfgs){
			AvContUrlCfg _cfg=new AvContUrlCfg();
			BeanUtils.copyProperties(cfg, _cfg);
			avContentCfgDao.saveAvContUrlCfgNew(_cfg);
		}
	}
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveOrUpdateContUrl(CfgIndexInfo entity){
		//设置区域运营商信息
		setAreaEffectiveIds(entity);
		entity.setIsValid(0);//无效
		entity.setIsAudit(0);//未审核
		if(entity.getCfgId()==null){
			Integer compileId = 0;
			try {
				List<Integer> idList = ConfigServiceUtil.getId(1, 1);
				if(idList!=null && idList.size()>0){
					compileId = idList.get(0);
				}
				entity.setCompileId(compileId);
				entity.setCreateTime(new Date());
				entity.setCreatorId(entity.getCurrentUser().getId());
				avContentCfgDao.insertCfgIndexInfo(entity);
				if(entity.getIpPortList()!=null){
					for(IpPortCfg cfg:entity.getIpPortList()){
						BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
						avContentCfgDao.saveIpPortCfg(cfg);
					}
				}
				if(entity.getNtcSubscribeIdCfgList()!=null){
					for(NtcSubscribeIdCfg cfg:entity.getNtcSubscribeIdCfgList()){
						if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
							entity.setNtcSubscribeIdCfg(cfg);
							avContentCfgDao.insertAvVoipKeywordCfg(entity);
						}
					}
				}
				if(entity.getAvContUrlCfgList()!=null&&entity.getAvContUrlCfgList().size()>0){
					for(AvContUrlCfg avContUrlCfg : entity.getAvContUrlCfgList()){
						entity.setAvContUrlCfg(avContUrlCfg);
						avContentCfgDao.saveAvContUrlCfg(entity);
					}
				}
				//保存区域IP信息
				if(entity.getAreaCfg()!=null){
					for(AreaIpCfg cfg:entity.getAreaCfg()){
						cfg.initDefaultValue();
						BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
						areaIpCfgDao.saveAreaIpCfg(cfg);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("获取编译ID出错");
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+e.getMessage());
			}
			
		}else{
			entity.setEditTime(new Date());
			entity.setEditorId(entity.getCurrentUser().getId());
			avContentCfgDao.updateCfgIndexInfo(entity);
			//无效子配置后，再新增子配置
			avContentCfgDao.deleteIpCfg(entity);
			stringCfgDao.deleteSubscribeIdCfgByCfgIndexInfo(entity);
			avContentCfgDao.deleteAvContUrlCfg(entity);
			AreaIpCfg area = new AreaIpCfg();
			area.setCompileId(entity.getCompileId());
			area.setFunctionId(entity.getFunctionId());
			areaIpCfgDao.deleteAreaIpCfg(area);
			entity.setCreateTime(new Date());
			entity.setCreatorId(entity.getCurrentUser().getId());
			if(entity.getIpPortList()!=null){
				for(IpPortCfg cfg:entity.getIpPortList()){
					BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
					avContentCfgDao.saveIpPortCfg(cfg);
				}
			}
			if(entity.getNtcSubscribeIdCfgList()!=null){
				for(NtcSubscribeIdCfg cfg:entity.getNtcSubscribeIdCfgList()){
					if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
						entity.setNtcSubscribeIdCfg(cfg);
						avContentCfgDao.insertAvVoipKeywordCfg(entity);
					}
				}
			}
			if(entity.getAvContUrlCfgList()!=null&&entity.getAvContUrlCfgList().size()>0){
				for(AvContUrlCfg avContUrlCfg : entity.getAvContUrlCfgList()){
					entity.setAvContUrlCfg(avContUrlCfg);
					avContentCfgDao.saveAvContUrlCfg(entity);
				}
			}
			//保存区域IP信息
			if(entity.getAreaCfg()!=null){
				for(AreaIpCfg cfg:entity.getAreaCfg()){
					cfg.initDefaultValue();
					BeanUtils.copyProperties(entity, cfg,new String[]{"cfgDesc","cfgRegionCode","cfgType"});
					areaIpCfgDao.saveAreaIpCfg(cfg);
				}
			}
		}
}
	
	/**
	 * 
	 * @param isAudit
	 * @param isValid
	 * @param ids compileIds
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void updateContUrlValid(Integer isAudit,Integer isValid,String ids,Integer functionId){
		String[] idArray = ids.split(",");
		for(String id :idArray){
			CfgIndexInfo entity = new CfgIndexInfo();
			entity.setCfgId(Long.parseLong(id));
			entity.setFunctionId(functionId);
			entity.setIsAudit(isAudit);
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			entity.setTableName(CfgIndexInfo.getTablename());
			avContentCfgDao.updateCfgValid(entity);
			entity = getUrlCfgIndexInfo(entity);
			if(entity.getIpPortList()!=null && entity.getIpPortList().size()>0){
				IpPortCfg cfg = new IpPortCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(IpPortCfg.getTablename());
				avContentCfgDao.updateCfgValid(cfg);
			}
			if(entity.getAvContUrlCfgList()!=null && entity.getAvContUrlCfgList().size()>0){
				AvContUrlCfg cfg = new AvContUrlCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(AvContUrlCfg.getTablename());
				avContentCfgDao.updateCfgValid(cfg);
			}
			avContentCfgDao.updateAvVoipKeywordCfgt(entity);
			avContentCfgDao.updateCfgIndexInfo(entity);
			AreaIpCfg areaIpCfg=new AreaIpCfg();
			BeanUtils.copyProperties(entity, areaIpCfg);
			areaIpCfgDao.updateAreaIpCfgValid(areaIpCfg);
		}
	}
	/**
	 * 
	 * @param isAudit
	 * @param isValid
	 * @param ids cfgId
	 * @param functionId
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void auditContUrl(Integer isAudit,Integer isValid,Integer functionId,String id,Date auditTime){
		CfgIndexInfo entity = new CfgIndexInfo();
		List<CfgIndexInfo> list = new ArrayList();
		List<IpPortCfg> ipPortList =new ArrayList();
		List <NtcSubscribeIdCfg> ntcList = new ArrayList();
		List<AreaIpCfg> areaIpCfgList = new ArrayList();
		List<AvContUrlCfg> avContUrlCfgList = new ArrayList();
		CfgIndexInfo searchCfg=new CfgIndexInfo();
		searchCfg.setCfgId(Long.parseLong(id));
		
		entity = avContentCfgDao.getCfgIndexInfo(searchCfg);
		entity.setIsAudit(isAudit);
		entity.setIsValid(isValid);
		entity.setAuditorId(UserUtils.getUser().getId());
		entity.setAuditTime(auditTime);
		avContentCfgDao.updateAvVoipIp(entity);
		avContentCfgDao.updateAvVoipKeywordCfgt(entity);
		avContentCfgDao.updateCfgIndexInfo(entity);
		AreaIpCfg areaIpCfg=new AreaIpCfg();
		BeanUtils.copyProperties(entity, areaIpCfg,new String[]{"cfgRegionCode","cfgType","cfgId"});
		areaIpCfgDao.updateAreaIpCfg(areaIpCfg);
		
		ipPortList = avContentCfgDao.getIpPortList(entity);
		ntcList=stringCfgDao.findSubscribeIdCfgListByCfgIndexInfo(entity);
		areaIpCfgList=areaIpCfgDao.getByCompileId(entity.getCompileId());
		avContUrlCfgList=avContentCfgDao.getAvContUrlList(entity);
		ToMaatBean maatBean = new ToMaatBean();		
		MaatCfg maatCfg = new MaatCfg();
		List<MaatCfg> configCompileList = new ArrayList();
		List<GroupCfg> groupRelationList = new ArrayList();
		List<IpCfg> ipRegionList = new ArrayList();
		List<StringCfg> strRegionList = new ArrayList();
		List<NumBoundaryCfg> numRegionList = new ArrayList();
		List<DigestCfg> digestRegionList = new ArrayList();
		List<IpCfg> areaIpRegionList = new ArrayList();
		
		if(avContUrlCfgList!=null && avContUrlCfgList.size()>0){
			entity.setAvContUrlCfgList(avContUrlCfgList);
			AvContUrlCfg cfg = new AvContUrlCfg();
			BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
			cfg.setTableName(AvContUrlCfg.getTablename());
			avContentCfgDao.updateCfgValid(cfg);
		}
		
		if(isAudit==1){
			if(!StringUtil.isEmpty(ipPortList)){
				IpPortCfg cfg = new IpPortCfg();
				BeanUtils.copyProperties(entity, cfg, new String[] { "cfgId" });
				cfg.setTableName(IpPortCfg.getTablename());
				avContentCfgDao.updateCfgValid(cfg);
				Map<String,List> ipMap = cfgConvert(ipRegionList,ipPortList,1,entity,groupRelationList);
				groupRelationList=ipMap.get("groupList");
				ipRegionList=ipMap.get("dstList");
				if(ipMap.get("numRegionList")!=null){
					numRegionList.addAll(ipMap.get("numRegionList"));

				}
			}
			if(!StringUtil.isEmpty(avContUrlCfgList)){
				Map<String,List> urlMap = cfgConvert(strRegionList,avContUrlCfgList,2,entity,groupRelationList);
				groupRelationList=urlMap.get("groupList");
				strRegionList=urlMap.get("dstList");
			}
			
			if(!StringUtil.isEmpty(ntcList)){
				Map<String,List> ntcMap = cfgConvert(strRegionList,ntcList,2,entity,groupRelationList);
				groupRelationList=ntcMap.get("groupList");
				strRegionList=ntcMap.get("dstList");
			}

			if(!StringUtil.isEmpty(areaIpCfgList)){
				Map<String,List> areaMap = cfgConvert(areaIpRegionList,areaIpCfgList,1,entity,groupRelationList);
				groupRelationList=areaMap.get("groupList");
				areaIpRegionList=areaMap.get("dstList");
			}
		}
		
		//构造提交综合服务参数格式，一条配置提交一次综合服务
		if(isAudit==1){
			maatCfg.initDefaultValue();
			BeanUtils.copyProperties(entity, maatCfg);
			maatCfg.setAction(entity.getAction());
			maatCfg.setAuditTime(auditTime);
			maatCfg.setIpRegionList(ipRegionList);
			maatCfg.setStrRegionList(strRegionList);
			maatCfg.setNumRegionList(numRegionList);
			maatCfg.setDigestRegionList(digestRegionList);
			maatCfg.setGroupRelationList(groupRelationList);
			maatCfg.setGroupNum(groupRelationList.size());
			maatCfg.setAreaIpRegionList(areaIpRegionList);
			maatCfg.setIsValid(entity.getIsValid());
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(auditTime);
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.INSERT_ACTION);
			//调用服务接口下发配置数据
			String json=gsonToJson(maatBean);
			logger.info("contUrl 配置下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
			logger.info("contUrl 配置下发响应信息："+result.getMsg());

		}else if(isAudit==3){
			maatCfg.setCompileId(entity.getCompileId());
			maatCfg.setServiceId(entity.getServiceId());
			maatCfg.setIsValid(0);//无效
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(auditTime);
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.UPDATE_ACTION);
			//调用服务接口取消配置
			String json=gsonToJson(maatBean);
			logger.info("contUrl 配置下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json,1);
			logger.info("contUrl 配置取消配置响应信息："+result.getMsg());
		}
	}	
	/********************************contUrl业务*********************************/
	
	
	/********************************picUrl业务*********************************/
	/**
	 * picIp查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<BaseStringCfg> findPicUrlPage(Page page, BaseStringCfg entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"r"));
		entity.setPage(page);
		entity.setTableName("av_pic_url_cfg");
		List<BaseStringCfg> list=avContentCfgDao.findStringList(entity);
		page.setList(list);
		return page;
	}
	
	/**
	 * PicUrl根据id查询picIpCfg信息
	 * @param entity
	 * @return
	 */
	public BaseStringCfg<NtcSubscribeIdCfg> getPicUrlCfgById(BaseStringCfg entity) {
		entity.setTableName("av_pic_url_cfg");
		BaseStringCfg cfg = avContentCfgDao.findStringCfgById(entity);
		NtcSubscribeIdCfg subscribeIdCfg = new NtcSubscribeIdCfg();
		BeanUtils.copyProperties(cfg, subscribeIdCfg, new String[] {"cfgId","isValid"});
		List<NtcSubscribeIdCfg> list = stringCfgDao.findSubscribeIdCfgList(subscribeIdCfg);
		cfg.setNtcSubscribeIdCfgList(list);
		return cfg;
	}
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveOrUpdatePicUrl(BaseStringCfg<BaseStringCfg> entity,String areaCfgIds){
		Date createTime=new Date();
		entity.setTableName("av_pic_url_cfg");
		//设置区域运营商信息
		setAreaEffectiveIds(entity);
		//新增
		if(entity.getCfgId()==null){
			entity.setCreatorId(UserUtils.getUser().getId());
			entity.setCreateTime(createTime);
			entity.setIsValid(0);
			entity.setIsAudit(0);
			//调用服务接口获取compileId
			List<Integer> compileIds = new ArrayList<Integer>();
			try {
				compileIds = ConfigServiceUtil.getId(1,1);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("获取编译ID出错");
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+e.getMessage());
			}
			
			// 添加subscribeId
			if(entity.getNtcSubscribeIdCfgList()!=null){
				for(NtcSubscribeIdCfg cfg:entity.getNtcSubscribeIdCfgList()){
					if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
						 entity.setCompileId(compileIds.get(0));
						 BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType",
								 										   "cfgKeywords","exprType",
								 										   "matchMethod","isHexbin"});
						 stringCfgDao.saveSubscribeIdCfg(cfg);
					}
				}
			}
			
			if(compileIds != null && compileIds.size() >0  && compileIds.get(0) != 0){
				entity.setCompileId(compileIds.get(0));
				
				//保存区域IP信息
				if(entity.getAreaCfg()!=null&&entity.getAreaCfg().size()>0){
					for (AreaIpCfg areaIpCfg : entity.getAreaCfg()) {
						BeanUtils.copyProperties(entity, areaIpCfg,new String[]{"cfgRegionCode"
								,"cfgType"
						});
						areaIpCfgDao.saveAreaIpCfg(areaIpCfg);
					}
				}
				
				//保存cfgIndexInfo
				avContentCfgDao.insertString(entity);
			}else{
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>");
			}
			
			//修改
		}else{
			Date editTime=new Date();
			entity.setIsValid(0);
			entity.setIsAudit(0);
			
			areaCfgIds=!StringUtil.isEmpty(areaCfgIds)? ","+areaCfgIds:"";
			
			if(!StringUtil.isEmpty(entity.getAreaCfg())){
				for (AreaIpCfg areaIpCfg : entity.getAreaCfg()) {
					if(!StringUtil.isEmpty(areaIpCfg.getCfgId())){
						if(areaCfgIds.contains(","+areaIpCfg.getCfgId()+",")){
							areaCfgIds=areaCfgIds.replace(areaIpCfg.getCfgId()+",", "");
						}
						//修改
						entity.setEditorId(UserUtils.getUser().getId());
						entity.setEditTime(editTime);
						BeanUtils.copyProperties(entity, areaIpCfg,new String[]{"cfgRegionCode"
								,"cfgType"
								,"cfgId"
						});
						areaIpCfgDao.updateAreaIpCfg(areaIpCfg);
					}else{
						//新增
						entity.setCreatorId(UserUtils.getUser().getId());
						entity.setCreateTime(createTime);
						BeanUtils.copyProperties(entity, areaIpCfg,new String[]{"cfgRegionCode"
								,"cfgType"
						});
						areaIpCfgDao.saveAreaIpCfg(areaIpCfg);
					}
				}
			}
			//delete 真是删除areaIpCfg信息
			if(!StringUtil.isEmpty(areaCfgIds.replaceAll(",", ""))){
				areaCfgIds=areaCfgIds.substring(1,areaCfgIds.length());
				for (String cfgId : areaCfgIds.split(",")) {
					AreaIpCfg areaIpCfg=new AreaIpCfg();
					areaIpCfg.setCfgId(Long.parseLong(cfgId));
					areaIpCfgDao.deleteAreaIpCfgByCfgId(areaIpCfg);
				}
			}
			
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(editTime);
			entity.setTableName("av_pic_url_cfg");
			avContentCfgDao.updateString(entity);
			
			entity.setCreateTime(new Date());
			entity.setCreatorId(entity.getCurrentUser().getId());
			// 删除SubscribeId配置再进行添加
			NtcSubscribeIdCfg subscribeIdCfg = new NtcSubscribeIdCfg();
			BeanUtils.copyProperties(entity, subscribeIdCfg,new String[]{"cfgId","cfgType"});
			stringCfgDao.deleteSubscribeIdCfg(subscribeIdCfg);
			if(entity.getNtcSubscribeIdCfgList()!=null){
				for(NtcSubscribeIdCfg cfg:entity.getNtcSubscribeIdCfgList()){
					if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
						BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType",
								   										  "cfgKeywords","exprType",
								   										  "matchMethod","isHexbin"});
						stringCfgDao.saveSubscribeIdCfg(cfg);
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param isAudit
	 * @param isValid
	 * @param ids compileIds
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void updatePicUrlValid(Integer isAudit,Integer isValid,String ids,Integer functionId){
		String[] idArray = ids.split(",");
		for(String id :idArray){
			BaseStringCfg entity = new BaseStringCfg();
			entity.setCfgId(Long.parseLong(id));
			BaseStringCfg picUrlCfg = this.getPicUrlCfgById(entity);// 获取CompileId
			entity.setCompileId(picUrlCfg.getCompileId());
			entity.setTableName("av_pic_url_cfg");
			entity.setFunctionId(functionId);
			entity.setIsAudit(isAudit);
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			avContentCfgDao.updateString(entity);
			// 查询域配置信息
			BaseStringCfg cfg = this.getContUrlCfgById(entity);
			if(cfg.getNtcSubscribeIdCfgList() != null && cfg.getNtcSubscribeIdCfgList().size()>0) {
				NtcSubscribeIdCfg subscribeIdCfg = new NtcSubscribeIdCfg();
				BeanUtils.copyProperties(cfg, subscribeIdCfg, new String[] {"cfgId","cfgType","tableName"});
				stringCfgDao.updateSubscribeIdCfg(subscribeIdCfg);
			}
			AreaIpCfg areaIpCfg=new AreaIpCfg();
			BeanUtils.copyProperties(entity, areaIpCfg);
			areaIpCfgDao.updateAreaIpCfgValid(areaIpCfg);
		}
	}
	/**
	 * 
	 * @param isAudit
	 * @param isValid
	 * @param ids cfgId
	 * @param functionId
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void auditPicUrl(Integer isAudit,Integer isValid,Integer functionId,String id,Date auditTime){
		BaseStringCfg entity = new BaseStringCfg();
		entity.setTableName("av_pic_url_cfg");
		List<BaseStringCfg> list = new ArrayList();
		List<AreaIpCfg> areaIpCfgList = new ArrayList();
		
		entity.setCompileId(Integer.parseInt(id));
		entity = avContentCfgDao.findStringCfgById(entity);
		entity.setTableName("av_pic_url_cfg");
		entity.setIsAudit(isAudit);
		entity.setIsValid(isValid);
		entity.setAuditorId(UserUtils.getUser().getId());
		entity.setAuditTime(auditTime);
		
		
		avContentCfgDao.updateString(entity);
		AreaIpCfg areaIpCfg=new AreaIpCfg();
		BeanUtils.copyProperties(entity, areaIpCfg,new String[]{"cfgRegionCode"
				,"cfgType"
				,"cfgId"
		});
		areaIpCfgDao.updateAreaIpCfg(areaIpCfg);
		
		list=avContentCfgDao.getStringList(entity);
		areaIpCfgList=areaIpCfgDao.getByCompileId(entity.getCompileId());
		
		ToMaatBean maatBean = new ToMaatBean();		
		MaatCfg maatCfg = new MaatCfg();
		List<MaatCfg> configCompileList = new ArrayList();
		List<GroupCfg> groupRelationList = new ArrayList();
		List<IpCfg> ipRegionList = new ArrayList();
		List<StringCfg> strRegionList = new ArrayList();
		List<NumBoundaryCfg> numRegionList = new ArrayList();
		List<DigestCfg> digestRegionList = new ArrayList();
		List<IpCfg> areaIpRegionList = new ArrayList();
		
		if(isAudit==1){
			if(!StringUtil.isEmpty(list)){
				Map<String,List> strMap = cfgConvert(strRegionList,list,2,entity,groupRelationList);
				groupRelationList=strMap.get("groupList");
				strRegionList=strMap.get("dstList");
			}
			
			if(!StringUtil.isEmpty(areaIpCfgList)){
				Map<String,List> areaMap = cfgConvert(areaIpRegionList,areaIpCfgList,1,entity,groupRelationList);
				groupRelationList=areaMap.get("groupList");
				areaIpRegionList=areaMap.get("dstList");
			}
		}
		
		// 修改审核状态
		BaseStringCfg baseStrCfg = this.getPicUrlCfgById(entity);
		if(!StringUtil.isEmpty(baseStrCfg.getNtcSubscribeIdCfgList())) {
			NtcSubscribeIdCfg cfg = new NtcSubscribeIdCfg();
			BeanUtils.copyProperties(entity, cfg, new String[] {"cfgId","cfgType","cfgKeywords",
																"exprType","matchMethod","isHexbin"});
			stringCfgDao.updateSubscribeIdCfg(cfg);
			if(isAudit==1) {
				Map<String,List> map = cfgConvert(strRegionList,baseStrCfg.getNtcSubscribeIdCfgList(),2,entity,groupRelationList);
				groupRelationList=map.get("groupList");
				strRegionList=map.get("dstList");
			}
		}
		
		//构造提交综合服务参数格式，一条配置提交一次综合服务
		if(isAudit==1){
			maatCfg.initDefaultValue();
			BeanUtils.copyProperties(entity, maatCfg);
			maatCfg.setAction(entity.getAction());
			maatCfg.setAuditTime(auditTime);
			maatCfg.setIpRegionList(ipRegionList);
			maatCfg.setStrRegionList(strRegionList);
			maatCfg.setNumRegionList(numRegionList);
			maatCfg.setDigestRegionList(digestRegionList);
			maatCfg.setGroupRelationList(groupRelationList);
			maatCfg.setGroupNum(groupRelationList.size());
			maatCfg.setAreaIpRegionList(areaIpRegionList);
			maatCfg.setIsValid(entity.getIsValid());
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(auditTime);
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.INSERT_ACTION);
			//调用服务接口下发配置数据
			String json=gsonToJson(maatBean);
			logger.info("picUrl配置下发配置参数："+json);
			// 调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
			logger.info("picUrl配置下发响应信息："+result.getMsg());
			
		}else if(isAudit==3){
			maatCfg.setCompileId(entity.getCompileId());
			maatCfg.setServiceId(entity.getServiceId());
			maatCfg.setIsValid(0);//无效
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(auditTime);
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.UPDATE_ACTION);
			//调用服务接口取消配置
			String json=gsonToJson(maatBean);
			logger.info("picUrl配置下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json,1);
			logger.info("picUrl配置取消配置响应信息："+result.getMsg());
		}
	}
	/********************************picUrl业务*********************************/
}
