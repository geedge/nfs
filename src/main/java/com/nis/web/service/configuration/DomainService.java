package com.nis.web.service.configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nis.domain.Page;
import com.nis.domain.configuration.AreaIpCfg;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.HttpUrlCfg;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.configuration.NtcSubscribeIdCfg;
import com.nis.domain.maat.MaatCfg;
import com.nis.domain.maat.MaatCfg.DigestCfg;
import com.nis.domain.maat.MaatCfg.GroupCfg;
import com.nis.domain.maat.MaatCfg.IpCfg;
import com.nis.domain.maat.MaatCfg.NumBoundaryCfg;
import com.nis.domain.maat.MaatCfg.StringCfg;
import com.nis.domain.specific.ConfigGroupInfo;
import com.nis.domain.maat.ToMaatBean;
import com.nis.domain.maat.ToMaatResult;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.Constants;
import com.nis.util.StringUtil;
import com.nis.web.dao.configuration.AreaIpCfgDao;
import com.nis.web.dao.configuration.DomainDao;
import com.nis.web.dao.configuration.StringCfgDao;
import com.nis.web.dao.configuration.WebsiteCfgDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.CrudService;

/**
 * String相关配置事务类
 * @author dell
 *
 */
@Service
public class DomainService extends CrudService<DomainDao,HttpUrlCfg> {
	@Autowired
	DomainDao domainDao;
	@Autowired
	protected StringCfgDao stringCfgDao;
	@Autowired
	protected WebsiteCfgDao websiteCfgDao;
	@Autowired
	protected AreaIpCfgDao areaIpCfgDao;
	 
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void addStringCfg(HttpUrlCfg cfg){
		//调用服务接口获取compileId
		Integer compileId = 0;
		try {
			List<Integer> compileIds = ConfigServiceUtil.getId(1,1);
			if(!StringUtil.isEmpty(compileIds)){
				compileId = compileIds.get(0);
			}
		} catch (MaatConvertException e) {
			e.printStackTrace();
			logger.info("获取编译ID出错");
			throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+e.getMessage());
		}
		if(compileId!=0){
			cfg.setCompileId(compileId);
			setAreaEffectiveIds(cfg);
			if(cfg.getAreaCfg()!=null&&cfg.getAreaCfg().size()>0){
				for(AreaIpCfg c:cfg.getAreaCfg()){
					c.initDefaultValue();
					BeanUtils.copyProperties(cfg, c,new String[]{"cfgId","ipType","direction",
							"protocol","protocolId","areaEffectiveIds","cfgRegionCode",
							"cfgType","ipPattern","srcIpAddress","portPattern","srcPort","destIpAddress","destPort"});	
					c.setTableName(AreaIpCfg.getTablename());
				}
				this.saveIpBatch(cfg.getAreaCfg());
			}
			if(cfg instanceof HttpUrlCfg){
				domainDao.insert((HttpUrlCfg)cfg);
			}else{
				stringCfgDao.insert(cfg);
			}
			
		}else{
			throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>: compileId is 0");
		}
		
	} 
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void updateStringCfg(HttpUrlCfg cfg){
		AreaIpCfg area=new AreaIpCfg();
		area.setCompileId(cfg.getCompileId());
		area.setFunctionId(cfg.getFunctionId());
		areaIpCfgDao.deleteAreaIpCfg(area);
		//区域IPsetAreaEffectiveIds设置
		setAreaEffectiveIds(cfg);
		Date date=new Date();
		if(cfg.getAreaCfg()!=null&&cfg.getAreaCfg().size()>0){
			for(AreaIpCfg c:cfg.getAreaCfg()){
				c.initDefaultValue();
				BeanUtils.copyProperties(cfg, c,new String[]{"cfgId","ipType","direction",
						"protocol","protocolId","areaEffectiveIds","cfgRegionCode",
						"cfgType","ipPattern","srcIpAddress","portPattern","srcPort","destIpAddress","destPort"});	
				c.setTableName(AreaIpCfg.getTablename());
				c.setCreatorId(cfg.getCurrentUser().getId());
				c.setCreateTime(date);
			}
			this.saveIpBatch(cfg.getAreaCfg());
		}
		if(cfg instanceof HttpUrlCfg && cfg.getCfgId() == null){
			domainDao.insert((HttpUrlCfg)cfg);
		}else{
			stringCfgDao.update(cfg);
		}
		
	} 
	public void auditWhiteDomain(HttpUrlCfg cfg) throws Exception{
		List<HttpUrlCfg> beans=new ArrayList<>();
		beans.add(cfg);
		domainDao.audit(cfg);
		List<MaatCfg> configCompileList = new ArrayList<>();
		List<GroupCfg> groupRelationList = new ArrayList<>();
		List<IpCfg> ipRegionList = new ArrayList<>();
		List<StringCfg> strRegionList = new ArrayList<>();
		List<NumBoundaryCfg> numRegionList = new ArrayList<>();
		List<DigestCfg> digestRegionList = new ArrayList<>();
		List<IpCfg> areaIpRegionList = new ArrayList<>();
		ToMaatBean maatBean = new ToMaatBean();	
		MaatCfg maatCfg = new MaatCfg();
		maatCfg.initDefaultValue();
		BeanUtils.copyProperties(cfg, maatCfg);
		if(cfg.getIsAudit()==Constants.AUDIT_YES){			
			maatBean.setOpAction(Constants.INSERT_ACTION);
			Map<String,List> map = cfgConvert(strRegionList,beans,2,cfg,groupRelationList);
			groupRelationList=map.get("groupList");
			strRegionList=map.get("dstList");
			maatCfg.setAreaEffectiveIds("0");
			maatCfg.setAction(cfg.getAction());
			maatCfg.setAuditTime(cfg.getAuditTime());
			maatCfg.setIpRegionList(ipRegionList);
			maatCfg.setStrRegionList(strRegionList);
			maatCfg.setNumRegionList(numRegionList);
			maatCfg.setDigestRegionList(digestRegionList);
			maatCfg.setGroupRelationList(groupRelationList);
			maatCfg.setGroupNum(groupRelationList.size());
			maatCfg.setAreaIpRegionList(areaIpRegionList);
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(cfg.getAuditTime());
			maatBean.setCreatorName(cfg.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			//调用服务接口下发配置数据
			String json=gsonToJson(maatBean);
			logger.info("域名白名单配置："+json);
			//调用服务接口下发配置
			try {
				ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
				logger.info("域名白名单配置下发响应信息："+result.getMsg());
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("域名白名单配置下发失败");
				throw e;
			}
		}else if(cfg.getIsAudit()==Constants.AUDIT_NOT_YES){
			maatCfg.setCompileId(cfg.getCompileId());
			maatCfg.setServiceId(cfg.getServiceId());
			maatCfg.setIsValid(Constants.VALID_NO);//无效
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(cfg.getAuditTime());
			maatBean.setCreatorName(cfg.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.UPDATE_ACTION);
			//调用服务接口取消配置
			String json=gsonToJson(maatBean);
			logger.info("域名白名单配置参数："+json);
			//调用服务接口取消配置
			try {
				ToMaatResult result = ConfigServiceUtil.put(json, 1);
				logger.info("域名白名单取消配置响应信息："+result.getMsg());
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("域名白名单取消配置失败");
				throw e;
			}
		}
	}
	public void auditDomain(HttpUrlCfg cfg) throws Exception{
		List<HttpUrlCfg> beans=new ArrayList<>();
		beans.add(cfg);
		domainDao.audit(cfg);
		List<BaseIpCfg> beans1=new ArrayList<>();
		List<AreaIpCfg> areaIpCfgList=areaIpCfgDao.getByCompileId(cfg.getCompileId());
		for(AreaIpCfg area:areaIpCfgList){
			BeanUtils.copyProperties(cfg,area ,new String[]{"cfgRegionCode"
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
			beans1.add(area);
		}
		this.auditIpBatch(beans1);
		
		List<MaatCfg> configCompileList = new ArrayList<>();
		List<GroupCfg> groupRelationList = new ArrayList<>();
		List<IpCfg> ipRegionList = new ArrayList<>();
		List<StringCfg> strRegionList = new ArrayList<>();
		List<NumBoundaryCfg> numRegionList = new ArrayList<>();
		List<DigestCfg> digestRegionList = new ArrayList<>();
		List<IpCfg> areaIpRegionList = new ArrayList<>();
		ToMaatBean maatBean = new ToMaatBean();	
		MaatCfg maatCfg = new MaatCfg();
		maatCfg.initDefaultValue();
		BeanUtils.copyProperties(cfg, maatCfg);
		if(cfg.getIsAudit()==Constants.AUDIT_YES){			
			maatBean.setOpAction(Constants.INSERT_ACTION);
			Map<String,List> map = cfgConvert(strRegionList,beans,2,cfg,groupRelationList);
			groupRelationList=map.get("groupList");
			strRegionList=map.get("dstList");
			numRegionList=map.get("numRegionList")==null?new ArrayList<>():map.get("numRegionList");
			Map<String,List> areaMap = cfgConvert(areaIpRegionList,beans1,1,cfg,groupRelationList);
			groupRelationList=areaMap.get("groupList");
			areaIpRegionList=areaMap.get("dstList");
			maatCfg.setAreaEffectiveIds(StringUtils.isBlank(cfg.getAreaEffectiveIds())?"0":cfg.getAreaEffectiveIds());
			if(cfg.getServiceId().intValue()==Constants.SERVICE_PXY_DOMAIN_INTERCEPT){
				String region=Constants.USERREGION_DOMAIN_ID+"="+cfg.getCompileId().intValue()+
						Constants.USER_REGION_SPLIT+Constants.USERREGION_DOMAIN_STR+"="+this.keywordsEscape(cfg.getCfgKeywords());
				maatCfg.setUserRegion(region);
			}else if(Constants.SERVICE_DOMAIN_RATELIMIT==cfg.getServiceId().intValue()){
				String region=Constants.USERREGION_RATE_LIMIT+"="+cfg.getRatelimit()+
						Constants.USER_REGION_SPLIT+Constants.USERREGION_DOMAIN_ID+"="+cfg.getCompileId().intValue()+
						Constants.USER_REGION_SPLIT+Constants.USERREGION_DOMAIN_STR+"="+this.keywordsEscape(cfg.getCfgKeywords());
				maatCfg.setUserRegion(region);
			}
			maatCfg.setAction(cfg.getAction());
			maatCfg.setAuditTime(cfg.getAuditTime());
			maatCfg.setIpRegionList(ipRegionList);
			maatCfg.setStrRegionList(strRegionList);
			maatCfg.setNumRegionList(numRegionList);
			maatCfg.setAreaIpRegionList(areaIpRegionList);
			maatCfg.setDigestRegionList(digestRegionList);
			maatCfg.setGroupRelationList(groupRelationList);
			maatCfg.setGroupNum(groupRelationList.size());
			maatCfg.setAreaIpRegionList(areaIpRegionList);
			configCompileList.add(maatCfg);
			maatBean.setOpAction(Constants.INSERT_ACTION);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(cfg.getAuditTime());
			maatBean.setCreatorName(cfg.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			//调用服务接口下发配置数据
			String json=gsonToJson(maatBean);
			logger.info("域名配置："+json);
			//调用服务接口下发配置
			try {
				ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
				logger.info("域名配置下发响应信息："+result.getMsg());
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("域名配置下发失败");
				throw e;
			}
		}else if(cfg.getIsAudit()==Constants.AUDIT_NOT_YES){
			maatCfg.setCompileId(cfg.getCompileId());
			maatCfg.setServiceId(cfg.getServiceId());
			maatCfg.setIsValid(Constants.VALID_NO);//无效
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(cfg.getAuditTime());
			maatBean.setCreatorName(cfg.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.UPDATE_ACTION);
			//调用服务接口取消配置
			String json=gsonToJson(maatBean);
			logger.info("域名配置参数："+json);
			//调用服务接口取消配置
			try {
				ToMaatResult result = ConfigServiceUtil.put(json, 1);
				logger.info("域名取消配置响应信息："+result.getMsg());
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("域名取消配置失败");
				throw e;
			}
		}
	}
	
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void deleteWhiteDomain(String ids){
		List<HttpUrlCfg> cfgs=new ArrayList<HttpUrlCfg>();
		Date date =new Date();
		if(StringUtils.isNotBlank(ids)){
			for(String idStr:ids.split(",")){
				if(StringUtils.isNotBlank(idStr)){
					HttpUrlCfg cfg=new HttpUrlCfg();
					cfg.setCfgId(Long.parseLong(idStr));
					cfg.setTableName(HttpUrlCfg.getTablename());
					cfg.setEditorId(cfg.getCurrentUser().getId());
					cfg.setEditTime(date);
					cfg.setIsValid(Constants.VALID_DEL);
					cfgs.add(cfg);
				}
			}
		}
		this.deleteBatch(cfgs, DomainDao.class);
	}
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void deleteDomain(String ids,String compileIds,int functionId){
		if(StringUtils.isNotBlank(compileIds)){
			for(String compileId:compileIds.split(",")){//强转数字，防止注入
				Integer.parseInt(compileId);
			}
			stringCfgDao.deleteByCompileIds(new HttpUrlCfg().getCurrentUser().getId(),AreaIpCfg.getTablename(),compileIds);
		}
		List<HttpUrlCfg> stringCfgs=new ArrayList<HttpUrlCfg>();
		Date date =new Date();
		if(StringUtils.isNotBlank(ids)){
			for(String idStr:ids.split(",")){
				if(StringUtils.isNotBlank(idStr)){
					HttpUrlCfg cfg=new HttpUrlCfg();
					cfg.setCfgId(Long.parseLong(idStr));
					cfg.setTableName(HttpUrlCfg.getTablename());
					cfg.setEditorId(cfg.getCurrentUser().getId());
					cfg.setEditTime(date);
					cfg.setIsValid(Constants.VALID_DEL);
					stringCfgs.add(cfg);
				}
			}
		}
		this.deleteBatch(stringCfgs, StringCfgDao.class);
	} 
	/**
	 * 
	 * deleteStringCfg(删除IP类配置)
	 * (继承BaseStringCfg这个类方可使用)
	 * @param baseStringCfg
	 * @return 
	 *int
	 * @exception 
	 * @since  1.0.0
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void deleteStringCfg(List<HttpUrlCfg> stringCfg,List<BaseIpCfg> areaCfg){
		if(areaCfg!=null&&areaCfg.size()>0){
			this.deleteIpBatch(areaCfg);
		}
		this.deleteBatch(stringCfg, StringCfgDao.class);
	} 
	/**
	 * 
	 * getStringCfg(根据IP与类名获取IP配置)
	 * (继承BaseStringCfg这个类方可使用)
	 * @param clazz
	 * @param id
	 * @return 
	 *BaseStringCfg
	 * @exception 
	 * @since  1.0.0
	 */
	public HttpUrlCfg getStringCfgById(HttpUrlCfg baseStringCfg){
		return domainDao.getById(baseStringCfg.getCfgId());
	}
	public HttpUrlCfg getStringCfgById(long id){
		return domainDao.getById(id);
	}
	public Integer getIsValid(HttpUrlCfg baseStringCfg){
		return domainDao.getIsValid(baseStringCfg);
	}
	public Integer getIsValid(long id){
		return domainDao.getIsValid(id);
	}
	public Integer getIsAudit(HttpUrlCfg baseStringCfg){
		return domainDao.getIsAudit(baseStringCfg);
	}
	public Integer getIsAudit(long id){
		return domainDao.getIsAudit(id);
	}
	/**
	 * getList(这里用一句话描述这个方法的作用)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param tableName
	 * @param ids
	 * @return 
	 *List<BaseStringCfg>
	 * @exception 
	 * @since  1.0.0
	*/
	public List<HttpUrlCfg> getListByCompileId(String ids) {
		// TODO Auto-generated method stub
		return domainDao.getListByCompileId(ids);
	}
	/**
	 * getListByCfgId(这里用一句话描述这个方法的作用)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param tablename
	 * @param ids
	 * @return 
	 *List<BaseStringCfg>
	 * @exception 
	 * @since  1.0.0
	*/
	public List<HttpUrlCfg> getListByCfgId(String ids) {
		// TODO Auto-generated method stub
		return domainDao.getListByCfgId(ids);
	}
	public CfgIndexInfo getDomainCfg(Long cfgId,Integer compileId) {
		CfgIndexInfo entity = websiteCfgDao.getCfgIndexInfo(cfgId,compileId);
		List<HttpUrlCfg> httpUrlList = websiteCfgDao.getHttpUrlList(entity);
		List<NtcSubscribeIdCfg> subscribeIdList = stringCfgDao.findSubscribeIdCfgListByCfgIndexInfo(entity);
		entity.setHttpUrlList(httpUrlList);
		entity.setNtcSubscribeIdCfgList(subscribeIdList);
		return entity;
	}
	public void saveDomainCfg(CfgIndexInfo entity){
		if(entity.getAction()==128){
			entity.setCfgType("WHITE_LIST_DOMAIN");
			if(entity.getHttpUrlList().size()>0){
				for (int i = 0; i < entity.getHttpUrlList().size(); i++) {
					entity.getHttpUrlList().get(i).setCfgType("WHITE_LIST_DOMAIN");
				}
			}
		}
		//设置区域运营商信息
		setAreaEffectiveIds(entity);
		
		entity.setIsValid(0);
		entity.setIsAudit(0);
		
		if(entity.getCfgId()==null){
			Integer compileId = 0;
			try {
				List<Integer> idList = ConfigServiceUtil.getId(1, 1);
				if(idList!=null && idList.size()>0){
					compileId = idList.get(0);
				}
			} catch (MaatConvertException e) {
				logger.info("获取编译ID出错");
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>");
			}
			entity.setCompileId(compileId);
			entity.setCreateTime(new Date());
			entity.setCreatorId(entity.getCurrentUser().getId());
			websiteCfgDao.saveCfgIndex(entity);
			
			if(entity.getHttpUrlList()!=null){
				for(HttpUrlCfg cfg:entity.getHttpUrlList()){
					if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
						BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
						websiteCfgDao.saveHttpUrlCfg(cfg);
					}
				}
			}
			if(entity.getNtcSubscribeIdCfgList()!=null){
				for(NtcSubscribeIdCfg cfg:entity.getNtcSubscribeIdCfgList()){
					if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
						BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
						stringCfgDao.saveSubscribeIdCfg(cfg);
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
			
		}else{
			entity.setEditTime(new Date());
			entity.setEditorId(entity.getCurrentUser().getId());
			websiteCfgDao.updateCfgIndex(entity);
			//无效子配置后，再新增子配置
			websiteCfgDao.deleteHttpUrlCfg(entity);
			stringCfgDao.deleteSubscribeIdCfgByCfgIndexInfo(entity);
			AreaIpCfg area = new AreaIpCfg();
			area.setCompileId(entity.getCompileId());
			area.setFunctionId(entity.getFunctionId());
			areaIpCfgDao.deleteAreaIpCfg(area);
			entity.setCreateTime(new Date());
			entity.setCreatorId(entity.getCurrentUser().getId());
			if(entity.getHttpUrlList()!=null){
				for(HttpUrlCfg cfg:entity.getHttpUrlList()){
					if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
						BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
						websiteCfgDao.saveHttpUrlCfg(cfg);
					}
				}
			}
			if(entity.getNtcSubscribeIdCfgList()!=null){
				for(NtcSubscribeIdCfg cfg:entity.getNtcSubscribeIdCfgList()){
					if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
						BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
						stringCfgDao.saveSubscribeIdCfg(cfg);
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
	}
	public void updateDomainCfgValid(Integer isValid,String ids,Integer functionId){
		String[] idArray = ids.split(",");
		for(String id :idArray){
			CfgIndexInfo entity = new CfgIndexInfo();
			entity.setCfgId(Long.parseLong(id));
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			entity.setTableName(CfgIndexInfo.getTablename());
			entity.setFunctionId(functionId);
			websiteCfgDao.updateCfgValid(entity);
			//查询子配置
			entity = this.getDomainCfg(Long.parseLong(id),entity.getCompileId());
			
			if(entity.getHttpUrlList()!=null && entity.getHttpUrlList().size()>0)
			{
				HttpUrlCfg cfg = new HttpUrlCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(HttpUrlCfg.getTablename());
				websiteCfgDao.updateCfgValid(cfg);
			}
			if(entity.getNtcSubscribeIdCfgList()!=null && entity.getNtcSubscribeIdCfgList().size()>0)
			{
				NtcSubscribeIdCfg cfg = new NtcSubscribeIdCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(NtcSubscribeIdCfg.getTablename());
				websiteCfgDao.updateCfgValid(cfg);
			}
			//保存区域IP信息
			if(entity.getAreaCfg()!=null && entity.getAreaCfg().size()>0){
				AreaIpCfg cfg = new AreaIpCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(AreaIpCfg.getTablename());
				websiteCfgDao.updateCfgValid(cfg);
			}
			
		}
		
	}
	public void auditDomainCfg(CfgIndexInfo entity,Integer isAudit) throws MaatConvertException{
		// TODO Auto-generated method stub
		//修改数据库审核状态信息
				entity.setTableName(CfgIndexInfo.getTablename());
				websiteCfgDao.auditCfg(entity);
				
				ToMaatBean maatBean = new ToMaatBean();		
				MaatCfg maatCfg = new MaatCfg();
				List<MaatCfg> configCompileList = new ArrayList();
				List<GroupCfg> groupRelationList = new ArrayList();
				List<IpCfg> ipRegionList = new ArrayList();
				List<StringCfg> strRegionList = new ArrayList();
				List<NumBoundaryCfg> numRegionList = new ArrayList();
				List<DigestCfg> digestRegionList = new ArrayList();
				List<IpCfg> areaIpRegionList = new ArrayList();
				
				//查询子配置并修改审核状态
				entity = this.getDomainCfg(entity.getCfgId(),entity.getCompileId());
				
				if(entity.getHttpUrlList()!=null && entity.getHttpUrlList().size()>0){
					HttpUrlCfg cfg = new HttpUrlCfg();
					BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
					cfg.setTableName(HttpUrlCfg.getTablename());
					websiteCfgDao.auditCfg(cfg);
					if(isAudit==1){
						Map<String,List> map = cfgConvert(strRegionList,entity.getHttpUrlList(),2,entity,groupRelationList);
						groupRelationList=map.get("groupList");
						strRegionList=map.get("dstList");
					}
				}
				if(entity.getNtcSubscribeIdCfgList()!=null && entity.getNtcSubscribeIdCfgList().size()>0){
					NtcSubscribeIdCfg cfg = new NtcSubscribeIdCfg();
					BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
					cfg.setTableName(NtcSubscribeIdCfg.getTablename());
					websiteCfgDao.auditCfg(cfg);
					if(isAudit==1){
						Map<String,List> map = cfgConvert(strRegionList,entity.getNtcSubscribeIdCfgList(),2,entity,groupRelationList);
						groupRelationList=map.get("groupList");
						strRegionList=map.get("dstList");
					}
				}
				//保存区域IP信息
				List<AreaIpCfg> areaIpCfgList=areaIpCfgDao.getByCompileId(entity.getCompileId());
				if(!StringUtil.isEmpty(areaIpCfgList)){
					AreaIpCfg cfg = new AreaIpCfg();
					BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
					cfg.setTableName(AreaIpCfg.getTablename());
					websiteCfgDao.auditCfg(cfg);
					if(isAudit==1){
						Map<String,List> map = cfgConvert(areaIpRegionList,areaIpCfgList,1,entity,groupRelationList);
						groupRelationList=map.get("groupList");
						areaIpRegionList=map.get("dstList");
					}
				}
				
				//构造提交综合服务参数格式，一条配置提交一次综合服务
				if(isAudit==1){
					maatCfg.initDefaultValue();
					BeanUtils.copyProperties(entity, maatCfg);
					maatCfg.setAction(entity.getAction());
					maatCfg.setAuditTime(entity.getAuditTime());
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
					maatBean.setAuditTime(entity.getAuditTime());
					maatBean.setCreatorName(entity.getCurrentUser().getName());
					maatBean.setVersion(Constants.MAAT_VERSION);
					maatBean.setOpAction(Constants.INSERT_ACTION);
					//调用服务接口下发配置数据
					String json=gsonToJson(maatBean);
					logger.info("domain配置下发配置参数："+json);
					//调用服务接口下发配置
					ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
					logger.info("domain配置下发响应信息："+result.getMsg());

				}else if(isAudit==3){
					maatCfg.setCompileId(entity.getCompileId());
					maatCfg.setServiceId(entity.getServiceId());
					maatCfg.setIsValid(0);//无效
					configCompileList.add(maatCfg);
					maatBean.setConfigCompileList(configCompileList);
					maatBean.setAuditTime(entity.getAuditTime());
					maatBean.setCreatorName(entity.getCurrentUser().getName());
					maatBean.setVersion(Constants.MAAT_VERSION);
					maatBean.setOpAction(Constants.UPDATE_ACTION);
					//调用服务接口取消配置
					String json=gsonToJson(maatBean);
					logger.info("domain配置下发配置参数："+json);
					//调用服务接口下发配置
					ToMaatResult result = ConfigServiceUtil.put(json,1);
					logger.info("domain配置取消配置响应信息："+result.getMsg());
				}
	}
	public Page<CfgIndexInfo> getDomainList(Page<CfgIndexInfo> page, CfgIndexInfo entity){
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"a"));
		entity.setPage(page);
		List<CfgIndexInfo> list = websiteCfgDao.getWebsiteList(entity);
		page.setList(list);
		return page;
	}
	
	public CfgIndexInfo exportDomainInfo(CfgIndexInfo entity){
		List<HttpUrlCfg> httpUrlList = websiteCfgDao.getHttpUrlList(entity);
		entity.setHttpUrlList(httpUrlList);
		return entity;
	}
}
