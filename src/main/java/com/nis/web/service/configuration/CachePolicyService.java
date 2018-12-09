package com.nis.web.service.configuration;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.configuration.AreaIpCfg;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.CommonStringCfg;
import com.nis.domain.configuration.HttpBodyCfg;
import com.nis.domain.configuration.HttpReqHeadCfg;
import com.nis.domain.configuration.HttpResHeadCfg;
import com.nis.domain.configuration.HttpUrlCfg;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.configuration.NtcSubscribeIdCfg;
import com.nis.domain.maat.MaatCfg;
import com.nis.domain.maat.MaatCfg.DigestCfg;
import com.nis.domain.maat.MaatCfg.GroupCfg;
import com.nis.domain.maat.MaatCfg.IpCfg;
import com.nis.domain.maat.MaatCfg.NumBoundaryCfg;
import com.nis.domain.maat.MaatCfg.StringCfg;
import com.nis.domain.maat.ToMaatBean;
import com.nis.domain.maat.ToMaatResult;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.Constants;
import com.nis.util.StringUtil;
import com.nis.web.dao.configuration.AreaIpCfgDao;
import com.nis.web.dao.configuration.CommonPolicyDao;
import com.nis.web.dao.configuration.StringCfgDao;
import com.nis.web.dao.configuration.WebsiteCfgDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.BaseService;
import com.nis.web.service.CrudService;

/**
 * 代理缓存相关配置事务类
 * @author zhangwei
 *
 */
@Service
public class CachePolicyService extends CrudService<WebsiteCfgDao,CfgIndexInfo> {
	@Autowired
	protected CommonPolicyDao commonPolicyDao;
	@Autowired
	protected AreaIpCfgDao areaIpCfgDao;
	@Autowired
	protected StringCfgDao stringCfgDao;
	
	public CfgIndexInfo getCachePolicy(Long cfgId){
		CfgIndexInfo entity = commonPolicyDao.getPolicyById(cfgId);
		entity.setUserRegion((Map<String,Object>)this.gsonFromJson(entity.getUserRegion1(),Map.class));
		List<BaseStringCfg> stringList = commonPolicyDao.getCommonStringList(entity);
		entity.setStringList(stringList);
		return entity;
	}
	
	/*public Map<String, List> exportCachePolicy(CfgIndexInfo entity){
		Map<String, List> dataMap=new HashMap<String, List>();
		List<HttpUrlCfg> httpUrlList = commonPolicyDao.getHttpUrlList(entity);
		List<HttpReqHeadCfg> httpReqHdrList = commonPolicyDao.getHttpReqHdrList(entity);
		List<HttpResHeadCfg> httpResHdrList = commonPolicyDao.getHttpResHdrList(entity);
		List<IpPortCfg> ipPortCfgList = commonPolicyDao.getIpPortList(entity);
		entity.setCfgType(Constants.HTTP_REDIRECT_REQ_BODY_REGION);
		List<HttpBodyCfg> httpReqBodyList = commonPolicyDao.getHttpReqBodyList(entity);
		entity.setCfgType(Constants.HTTP_REDIRECT_RES_BODY_REGION);
		List<HttpBodyCfg> httpResBodyList = commonPolicyDao.getHttpResBodyList(entity);
		List<NtcSubscribeIdCfg> subscribeIdList = stringCfgDao.findSubscribeIdCfgListByCfgIndexInfo(entity);
		dataMap.put("NTC_UNIVERSAL_IP", ipPortCfgList);
		dataMap.put("NTC_CACHE_URL", httpUrlList);
		dataMap.put("NTC_HTTP_REQ_HDR", httpReqHdrList);
		dataMap.put("NTC_HTTP_RES_HDR", httpResHdrList);
		dataMap.put("NTC_HTTP_REQ_BODY", httpReqBodyList);
		dataMap.put("NTC_HTTP_RES_BODY", httpResBodyList);
		dataMap.put("NTC_SUBSCRIBE_ID", subscribeIdList);
		return dataMap;
	}*/
	
	public Page<CfgIndexInfo> getCachePolicyList(Page<CfgIndexInfo> page, CfgIndexInfo entity){
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"a"));
		entity.setPage(page);
		List<CfgIndexInfo> list = commonPolicyDao.getPolicyList(entity);
		for(CfgIndexInfo c:list){
			c.setUserRegion((Map<String,Object>)this.gsonFromJson(c.getUserRegion1(),Map.class));
		}
		page.setList(list);
		return page;
	}
	public void saveCachePolicy(CfgIndexInfo entity){
		//设置区域运营商信息
		setAreaEffectiveIds(entity);
		if(entity.getCfgId()==null){
			Integer compileId = 0;
			try {
				//处理复杂用户自定义域
				if(!StringUtil.isEmpty(entity.getUserRegion())){
					entity.setUserRegion1(gsonToJson(entity.getUserRegion()));
				}
				
				System.out.println(entity.getUserRegion1());
				List<Integer> idList = ConfigServiceUtil.getId(1, 1);
				if(idList!=null && idList.size()>0){
					compileId = idList.get(0);
				}
				entity.setCompileId(compileId);
				entity.setCreateTime(new Date());
				entity.setCreatorId(entity.getCurrentUser().getId());
				commonPolicyDao.saveCfgIndex(entity);
				if(entity.getStringList()!=null){
					for(BaseStringCfg cfg:entity.getStringList()){
						if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
							BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
							commonPolicyDao.saveCommonStringCfg(cfg);
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
				logger.info("数据保存出错");
				throw e;
			}
			
		}else{
			//处理复杂用户自定义域
			if(!StringUtil.isEmpty(entity.getUserRegion())){
				entity.setUserRegion1(gsonToJson(entity.getUserRegion()));
			}
			System.out.println(entity.getUserRegion1());
			// 审核未通过状态的配置 修改后状态改为未审核
			entity.setIsValid(0);
			entity.setIsAudit(0);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			commonPolicyDao.updateCfgIndex(entity);
			//无效子配置后，再新增子配置
			commonPolicyDao.deleteCommonStringCfg(entity);

			AreaIpCfg area = new AreaIpCfg();
			area.setCompileId(entity.getCompileId());
			area.setFunctionId(entity.getFunctionId());
			areaIpCfgDao.deleteAreaIpCfg(area);
			entity.setCreateTime(new Date());
			entity.setCreatorId(entity.getCurrentUser().getId());
			if(entity.getStringList()!=null){
				for(BaseStringCfg cfg:entity.getStringList()){
					if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
						BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
						commonPolicyDao.saveCommonStringCfg(cfg);
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
	public void updateCachePolicyValid(Integer isValid,String ids,Integer functionId){
		String[] idArray = ids.split(",");
		for(String id :idArray){
			CfgIndexInfo entity = new CfgIndexInfo();
			entity.setCfgId(Long.parseLong(id));
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			entity.setTableName(CfgIndexInfo.getTablename());
			entity.setFunctionId(functionId);
			commonPolicyDao.updateCfgValid(entity);
			//查询子配置
			entity = this.getCachePolicy(Long.parseLong(id));

			if(entity.getStringList()!=null && entity.getStringList().size()>0){
				CommonStringCfg cfg = new CommonStringCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(CommonStringCfg.getTablename());
				commonPolicyDao.updateCfgValid(cfg);
			}
			//保存区域IP信息
			if(entity.getAreaCfg()!=null && entity.getAreaCfg().size()>0){
				AreaIpCfg cfg = new AreaIpCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(AreaIpCfg.getTablename());
				commonPolicyDao.updateCfgValid(cfg);
			}
			
		}
		
	}
	public void auditCachePolicy(CfgIndexInfo entity,Integer isAudit) throws MaatConvertException{
		//修改数据库审核状态信息
		entity.setTableName(CfgIndexInfo.getTablename());
		commonPolicyDao.auditCfg(entity);
		
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
		entity = this.getCachePolicy(entity.getCfgId());

		if(entity.getIpPortList()!=null && entity.getIpPortList().size()>0){
			IpPortCfg cfg = new IpPortCfg();
			BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
			cfg.setTableName(IpPortCfg.getTablename());
			commonPolicyDao.auditCfg(cfg);
			if(isAudit==1){
				Map<String,List> map = cfgConvert(ipRegionList,entity.getIpPortList(),1,entity,groupRelationList);
				groupRelationList=map.get("groupList");
				ipRegionList=map.get("dstList");
				if(map.get("numRegionList")!=null){
					numRegionList.addAll(map.get("numRegionList"));
				}
			}
		}
		if(entity.getStringList()!=null && entity.getStringList().size()>0){
			CommonStringCfg cfg = new CommonStringCfg();
			BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
			cfg.setTableName(CommonStringCfg.getTablename());
			commonPolicyDao.auditCfg(cfg);
			if(isAudit==1){
				Map<String,List> map = cfgConvert(strRegionList,entity.getStringList(),2,entity,groupRelationList);
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
			commonPolicyDao.auditCfg(cfg);
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
			
			//判断代理缓存策略是否需要设置自定义域参数
			/*String userRegion = "";
			
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
			if(StringUtil.isEmpty(userRegion)){
				userRegion=Constants.USER_REGION_PLACEHOLDER;
			}*/
			maatCfg.setUserRegion(entity.getUserRegion1());
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(entity.getAuditTime());
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.INSERT_ACTION);
			//调用服务接口下发配置数据
			String json=gsonToJson(maatBean);
			logger.info("代理缓存策略下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
			logger.info("代理缓存策略下发响应信息："+result.getMsg());

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
			logger.info("代理缓存策略下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json,1);
			logger.info("代理缓存策略取消配置响应信息："+result.getMsg());
		}
	}
	
}
