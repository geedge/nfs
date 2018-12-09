package com.nis.web.service.configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.configuration.AreaIpCfg;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.HttpUrlCfg;
import com.nis.domain.configuration.InterceptPktBin;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.maat.MaatCfg;
import com.nis.domain.maat.MaatCfg.NumBoundaryCfg;
import com.nis.domain.maat.ToMaatBean;
import com.nis.domain.maat.ToMaatResult;
import com.nis.domain.maat.MaatCfg.DigestCfg;
import com.nis.domain.maat.MaatCfg.GroupCfg;
import com.nis.domain.maat.MaatCfg.IpCfg;
import com.nis.domain.maat.MaatCfg.StringCfg;
import com.nis.domain.specific.ConfigGroupInfo;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.Constants;
import com.nis.util.StringUtil;
import com.nis.web.dao.configuration.AreaIpCfgDao;
import com.nis.web.dao.configuration.InterceptCfgDao;
import com.nis.web.dao.configuration.WebsiteCfgDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.BaseService;
import com.nis.web.service.CrudService;

/**
 * 网站相关配置事务类
 * @author dell
 *
 */
@Service
public class InterceptCfgService extends CrudService<WebsiteCfgDao,CfgIndexInfo> {
	@Autowired
	protected WebsiteCfgDao websiteCfgDao;
	@Autowired
	protected InterceptCfgDao interceptCfgDao;
	@Autowired
	protected AreaIpCfgDao areaIpCfgDao;
	
	public CfgIndexInfo getInterceptCfg(Long cfgId,Integer compileId){
		CfgIndexInfo entity = websiteCfgDao.getCfgIndexInfo(cfgId,compileId);
		List<IpPortCfg> ipPortList = websiteCfgDao.getIpPortList(entity);
		List<InterceptPktBin> pktBinList = interceptCfgDao.getInterceptPktBin(entity);
		List<HttpUrlCfg> httpUrlList = websiteCfgDao.getHttpUrlList(entity);
		entity.setIpPortList(ipPortList);
		entity.setInterceptPktBinList(pktBinList);
		entity.setHttpUrlList(httpUrlList);
		return entity;
	}
	public Page<CfgIndexInfo> getWebsiteList(Page<CfgIndexInfo> page, CfgIndexInfo entity){
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"a"));
		entity.setPage(page);
		List<CfgIndexInfo> list = websiteCfgDao.getWebsiteList(entity);
		page.setList(list);
		return page;
	}
	/**
	 * 保存ip拦截
	 * @param entity
	 */
	
	public Map<String, List> exportIpInfo(CfgIndexInfo entity){
		Map<String, List> dataMap=new HashMap<String, List>();
		List<IpPortCfg> ipPortList = websiteCfgDao.getIpPortList(entity);
		List<InterceptPktBin> info = interceptCfgDao.getInterceptPktBin(entity);
		List<HttpUrlCfg> httpUrlList = websiteCfgDao.getHttpUrlList(entity);
		dataMap.put("PXY_INTERCEPT_IP", ipPortList);
		dataMap.put("NTC_HTTP_URL", httpUrlList);
		dataMap.put("PXY_INTERCEPT_PKT_BIN", info);
		return dataMap;
	}
	
	public void saveInterceptCfg(List<BaseStringCfg<?>> interceptPktBins){
		for(BaseStringCfg cfg:interceptPktBins){
			InterceptPktBin _cfg=new InterceptPktBin();
			BeanUtils.copyProperties(cfg, _cfg);
			if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
				interceptCfgDao.saveInterceptPktBin(_cfg);
			}
		}
	}
	public void saveInterceptCfg(CfgIndexInfo entity){
		if(!entity.getAction().equals(Constants.REPLACE_ACTION)){
			entity.setInterceptPktBinList(null);
		}
		
		entity.setIsValid(0);
		entity.setIsAudit(0);
		
		//设置区域运营商信息
		setAreaEffectiveIds(entity);
		if(entity.getCfgId()==null){
			Integer compileId = 0;
			try {
				List<Integer> idList = ConfigServiceUtil.getId(1, 1);
				if(idList!=null && idList.size()>0){
					compileId = idList.get(0);
			}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("获取编译ID出错");
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+e.getMessage());
			}
			entity.setCompileId(compileId);
			entity.setCreateTime(new Date());
			entity.setCreatorId(entity.getCurrentUser().getId());
			websiteCfgDao.saveCfgIndex(entity);
			if(entity.getIpPortList()!=null){
				for(IpPortCfg cfg:entity.getIpPortList()){
					BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
					websiteCfgDao.saveIpPortCfg(cfg);
				}
			}
			
			if(entity.getInterceptPktBinList()!=null ){
				for(InterceptPktBin cfg:entity.getInterceptPktBinList()){
					if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
						BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
						interceptCfgDao.saveInterceptPktBin(cfg);
					}
				}
			}
			if(entity.getHttpUrlList()!=null ){
				for(HttpUrlCfg cfg:entity.getHttpUrlList()){
					if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
						BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
						websiteCfgDao.saveHttpUrlCfg(cfg);
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
			interceptCfgDao.deleteInterceptIpCfg(entity);
			interceptCfgDao.deleteInterceptPktBin(entity);
			websiteCfgDao.deleteHttpUrlCfg(entity);
			AreaIpCfg area = new AreaIpCfg();
			area.setCompileId(entity.getCompileId());
			area.setFunctionId(entity.getFunctionId());
			areaIpCfgDao.deleteAreaIpCfg(area);
			entity.setCreateTime(new Date());
			entity.setCreatorId(entity.getCurrentUser().getId());
			if(entity.getIpPortList()!=null){
				for(IpPortCfg cfg:entity.getIpPortList()){
					BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
					websiteCfgDao.saveIpPortCfg(cfg);
				}
			}
			if(entity.getInterceptPktBinList()!=null){
				for(InterceptPktBin cfg:entity.getInterceptPktBinList()){
					if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
						BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
						interceptCfgDao.saveInterceptPktBin(cfg);
					}
				}
			}
			if(entity.getHttpUrlList()!=null){
				for(HttpUrlCfg cfg:entity.getHttpUrlList()){
					if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
						BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
						websiteCfgDao.saveHttpUrlCfg(cfg);
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
	/**
	 * dns配置删除
	 * @param isValid
	 * @param ids
	 * @param functionId
	 */
	public void updatInterceptValid(Integer isValid,String ids,Integer functionId){
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
			entity = this.getInterceptCfg(Long.parseLong(id),entity.getCompileId());
			if(entity.getIpPortList()!=null && entity.getIpPortList().size()>0){
				IpPortCfg cfg = new IpPortCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(IpPortCfg.getTablename());
				websiteCfgDao.updateCfgValid(cfg);
			}
			if(entity.getInterceptPktBinList()!=null && entity.getInterceptPktBinList().size()>0)
			{
				InterceptPktBin cfg = new InterceptPktBin();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(InterceptPktBin.getTablename());
				websiteCfgDao.updateCfgValid(cfg);
			}
			if(entity.getHttpUrlList()!=null && entity.getHttpUrlList().size()>0)
			{
				HttpUrlCfg cfg = new HttpUrlCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(HttpUrlCfg.getTablename());
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
	/**
	 * 配置审核
	 * @param entity
	 * @param isAudit
	 * @throws MaatConvertException
	 */
	public void auditInterceptIpCfg(CfgIndexInfo entity,Integer isAudit) throws MaatConvertException{
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
		entity = this.getInterceptCfg(entity.getCfgId(),entity.getCompileId());
		if(entity.getIpPortList()!=null && entity.getIpPortList().size()>0){
			IpPortCfg cfg = new IpPortCfg();
			BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
			cfg.setTableName(IpPortCfg.getTablename());
			websiteCfgDao.auditCfg(cfg);
			if(isAudit==1){
				Map<String,List> map = cfgConvert(ipRegionList,entity.getIpPortList(),1,entity,groupRelationList);
				groupRelationList=map.get("groupList");
				ipRegionList=map.get("dstList");
				if(map.get("numRegionList")!=null){
					numRegionList.addAll(map.get("numRegionList"));
				}
			}
		}
		if(entity.getInterceptPktBinList()!=null && entity.getInterceptPktBinList().size()>0){
			InterceptPktBin cfg = new InterceptPktBin();
			BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
			cfg.setTableName(InterceptPktBin.getTablename());
			websiteCfgDao.auditCfg(cfg);
			if(isAudit==1){
				Map<String,List> map = cfgConvert(strRegionList,entity.getInterceptPktBinList(),2,entity,groupRelationList);
				groupRelationList=map.get("groupList");
				strRegionList=map.get("dstList");
			}
		}
		//域名信息
		boolean doaminFlag=false;
		String domainUserRegion="";
		if(entity.getHttpUrlList()!=null && entity.getHttpUrlList().size()>0){
			doaminFlag=true;
			domainUserRegion=Constants.USERREGION_DOMAIN_ID+"="+entity.getCompileId();
			String domainStr="";
			if(entity != null && entity.getHttpUrlList() != null && entity.getHttpUrlList().size() > 0){
				BaseStringCfg httpUrl= entity.getHttpUrlList().get(0);
				domainStr=httpUrl.getCfgKeywords();
			}
			//entity.getHttpUrlList().get(0).getCfgKeywords();
			
			domainUserRegion=domainUserRegion+";"+Constants.USERREGION_DOMAIN_STR+"="+domainStr;
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
			
			//监测 需要发keyring_id
			if(entity.getAction().equals(Constants.MONIT_ACTION)){
				entity.setUserRegion1(StringUtil.isEmpty(entity.getUserRegion1()) ? "0":entity.getUserRegion1());
				maatCfg.setUserRegion(Constants.INTERCEPT_IP_MONIT_USER_REGION_KEY+"="+entity.getUserRegion1());
				//监测的域名需下发拦截强度
				if(entity.getFunctionId().equals(201)){
					entity.setUserRegion5(StringUtil.isEmpty(entity.getUserRegion5()) ? "1":entity.getUserRegion5());
					if(StringUtil.isEmpty(maatCfg.getUserRegion())) {
						maatCfg.setUserRegion(Constants.INTERCEPT_DOMAN_INTENSITY_USER_REGION_KEY+"="+entity.getUserRegion5());
					}else {
						maatCfg.setUserRegion(maatCfg.getUserRegion()+";"+Constants.INTERCEPT_DOMAN_INTENSITY_USER_REGION_KEY+"="+entity.getUserRegion5());
					}
				}
			}
			//限速 需要发Droprate=0.001 ，暂不支持Bandwidth=200kbps
			if(entity.getAction().equals(Constants.RATELIMIT_ACTION)){
				if(entity.getUserRegion1().equals("0")){//丢包率
					entity.setUserRegion2(StringUtil.isEmpty(entity.getUserRegion2()) ? "":entity.getUserRegion2());
					maatCfg.setUserRegion(Constants.INTERCEPT_IP_RATELIMIT_DROPRATE_USER_REGION_KEY+"="+entity.getUserRegion2());
				}else if(entity.getUserRegion1().equals("1")){//带宽
					entity.setUserRegion3(StringUtil.isEmpty(entity.getUserRegion3()) ? "":entity.getUserRegion3());
					maatCfg.setUserRegion(Constants.INTERCEPT_IP_RATELIMIT_BANDWITH_USER_REGION_KEY+"="+entity.getUserRegion3());
				}
			}
			String userRegion="";
			//替换需要发zone
			if(entity.getAction().equals(Constants.REPLACE_ACTION)){
					//HTTP replace： replace type is not null ;find is not null;replace with is not null(userRegion is not null)
					userRegion=Constants.REPLACE_ZONE_KEY+"="+entity.getUserRegion1();
					String substitute="";
					String userRegion2=StringUtil.isEmpty(entity.getUserRegion2()) ? "":entity.getUserRegion2();
					substitute="/";
					userRegion2 = BaseService.replaceContentEscape(userRegion2);
					substitute=substitute+userRegion2;
					
					String userRegion3=StringUtil.isEmpty(entity.getUserRegion3()) ? "":entity.getUserRegion3();
					userRegion3 = BaseService.replaceContentEscape(userRegion3);
					substitute=substitute+"/"+userRegion3;
					
					userRegion=userRegion+";"+Constants.REPLACE_SUBSTITUTE_KEY+"="+substitute;
					maatCfg.setUserRegion(userRegion);
			}
			
			//域名拦截需要增加域名id和域名字符串自定义配置
			if(StringUtil.isEmpty(maatCfg.getUserRegion()) 
					|| maatCfg.getUserRegion().equals(Constants.USER_REGION_PLACEHOLDER)){
				if(doaminFlag){
					maatCfg.setUserRegion(domainUserRegion);
				}else{
					maatCfg.setUserRegion(Constants.USER_REGION_PLACEHOLDER);
				}
			}else{
				if(doaminFlag){
					maatCfg.setUserRegion(maatCfg.getUserRegion()+";"+domainUserRegion);
				}
			}
			
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(entity.getAuditTime());
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.INSERT_ACTION);
			//调用服务接口下发配置数据
			String json=gsonToJson(maatBean);
			logger.info("intercept IP/DNS 配置下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
			logger.info("intercept IP/DNS 配置下发响应信息："+result.getMsg());

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
			logger.info("intercept IP/DNS 配置下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json,1);
			logger.info("intercept IP/DNS 配置取消配置响应信息："+result.getMsg());
		}
	}
}
