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
import com.nis.domain.configuration.ComplexkeywordCfg;
import com.nis.domain.configuration.DnsDomainCfg;
import com.nis.domain.configuration.HttpBodyCfg;
import com.nis.domain.configuration.HttpReqHeadCfg;
import com.nis.domain.configuration.HttpResHeadCfg;
import com.nis.domain.configuration.HttpUrlCfg;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.configuration.NtcSubscribeIdCfg;
import com.nis.domain.configuration.SslKeywordCfg;
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
import com.nis.util.DictUtils;
import com.nis.util.StringUtil;
import com.nis.web.dao.basics.ServiceDictInfoDao;
import com.nis.web.dao.configuration.AreaIpCfgDao;
import com.nis.web.dao.configuration.StringCfgDao;
import com.nis.web.dao.configuration.WebsiteCfgDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.CrudService;

/**
 * 网站相关配置事务类
 * @author dell
 *
 */
@Service
public class WebsiteCfgService extends CrudService<WebsiteCfgDao,CfgIndexInfo> {
	@Autowired
	protected WebsiteCfgDao websiteCfgDao;
	@Autowired
	protected AreaIpCfgDao areaIpCfgDao;
	@Autowired
	protected StringCfgDao stringCfgDao;
	@Autowired
    private ServiceDictInfoDao serviceDictInfoDao;
	
	public CfgIndexInfo getCfgIndexInfo(Integer compileId){
		CfgIndexInfo entity=new CfgIndexInfo();
		entity.setCompileId(compileId);
		List<CfgIndexInfo> cfgs=websiteCfgDao.getCfgIndexInfos(entity);
		if(!StringUtil.isEmpty(cfgs)){
			entity=cfgs.get(0);
		}
		return entity;
	}
	public CfgIndexInfo getHttpCfg(Long cfgId,Integer compileId){
		CfgIndexInfo entity = websiteCfgDao.getCfgIndexInfo(cfgId,compileId);
		List<IpPortCfg> ipPortList = websiteCfgDao.getIpPortList(entity);
		List<HttpUrlCfg> httpUrlList = websiteCfgDao.getHttpUrlList(entity);
		List<HttpReqHeadCfg> httpReqHdrList = websiteCfgDao.getHttpReqHdrList(entity);
		List<HttpResHeadCfg> httpResHdrList = websiteCfgDao.getHttpResHdrList(entity);
		List<NtcSubscribeIdCfg> subscribeIdList = stringCfgDao.findSubscribeIdCfgListByCfgIndexInfo(entity);
		entity.setCfgType(Constants.HTTP_REQ_BODY_REGION);
		List<HttpBodyCfg> httpReqBodyList = websiteCfgDao.getHttpReqBodyList(entity);
		entity.setCfgType(Constants.HTTP_RES_BODY_REGION);
		List<HttpBodyCfg> httpResBodyList = websiteCfgDao.getHttpResBodyList(entity);
		entity.setIpPortList(ipPortList);
		entity.setHttpReqBodyList(httpReqBodyList);
		entity.setHttpReqHdrList(httpReqHdrList);
		entity.setHttpResBodyList(httpResBodyList);
		entity.setHttpResHdrList(httpResHdrList);
		entity.setHttpUrlList(httpUrlList);
		entity.setNtcSubscribeIdCfgList(subscribeIdList);
		return entity;
	}
	public Map<String, List> exportHttpCfg(CfgIndexInfo entity){
		Map<String, List> dataMap=new HashMap<String, List>();
		List<IpPortCfg> ipPortList = websiteCfgDao.getIpPortList(entity);
		List<HttpUrlCfg> httpUrlList = websiteCfgDao.getHttpUrlList(entity);
		List<HttpReqHeadCfg> httpReqHdrList = websiteCfgDao.getHttpReqHdrList(entity);
		List<HttpResHeadCfg> httpResHdrList = websiteCfgDao.getHttpResHdrList(entity);
		List<NtcSubscribeIdCfg> subscribeIdList = stringCfgDao.findSubscribeIdCfgListByCfgIndexInfo(entity);
		entity.setCfgType(Constants.HTTP_REQ_BODY_REGION);
		List<HttpBodyCfg> httpReqBodyList = websiteCfgDao.getHttpReqBodyList(entity);
		entity.setCfgType(Constants.HTTP_RES_BODY_REGION);
		List<HttpBodyCfg> httpResBodyList = websiteCfgDao.getHttpResBodyList(entity);
		dataMap.put("NTC_UNIVERSAL_IP", ipPortList);
		dataMap.put("NTC_HTTP_URL", httpUrlList);
		dataMap.put("NTC_HTTP_REQ_HDR", httpReqHdrList);
		dataMap.put("NTC_HTTP_RES_HDR", httpResHdrList);
		dataMap.put("NTC_HTTP_REQ_BODY", httpReqBodyList);
		dataMap.put("NTC_HTTP_RES_BODY", httpResBodyList);
		dataMap.put("NTC_SUBSCRIBE_ID", subscribeIdList);
		
		return dataMap;
	}
	
	
	public CfgIndexInfo exportdns(CfgIndexInfo entity){
		List<IpPortCfg> ipPortList = websiteCfgDao.getIpPortList(entity);
		List<ComplexkeywordCfg> domainList = websiteCfgDao.getDnsDomainList(entity);
		List<NtcSubscribeIdCfg> subscribeIdList = stringCfgDao.findSubscribeIdCfgListByCfgIndexInfo(entity);
		entity.setIpPortList(ipPortList);
		entity.setDomainList(domainList);
		entity.setNtcSubscribeIdCfgList(subscribeIdList);
		return entity;
	}
	
	
	
	public Map<String, List> exportssl(CfgIndexInfo entity){
		Map<String, List> dataMap=new HashMap<String, List>();
		List<IpPortCfg> ipPortList = websiteCfgDao.getIpPortList(entity);
		List<NtcSubscribeIdCfg> subscribeIdList = stringCfgDao.findSubscribeIdCfgListByCfgIndexInfo(entity);
		entity.setCfgType("NTC_SSL_SNI");
		List<BaseStringCfg> sslSniList = websiteCfgDao.getSslKewordList(entity);
		entity.setCfgType("NTC_SSL_SAN");
		List<BaseStringCfg> sslSanList = websiteCfgDao.getSslKewordList(entity);
		entity.setCfgType("NTC_SSL_CN");
		List<BaseStringCfg> sslCnList = websiteCfgDao.getSslKewordList(entity);
		dataMap.put("NTC_UNIVERSAL_IP", ipPortList);
		dataMap.put("NTC_SSL_SNI", sslSniList);
		dataMap.put("NTC_SSL_SAN", sslSanList);
		dataMap.put("NTC_SSL_CN", sslCnList);
		dataMap.put("NTC_SUBSCRIBE_ID", subscribeIdList);
		return dataMap;
	}
	
	
	
	public CfgIndexInfo getSslCfg(Long cfgId,Integer compileId){
		CfgIndexInfo entity = websiteCfgDao.getCfgIndexInfo(cfgId,compileId);
		List<IpPortCfg> ipPortList = websiteCfgDao.getIpPortList(entity);
		List<BaseStringCfg> sslList = websiteCfgDao.getSslKewordList(entity);
		List<NtcSubscribeIdCfg> subscribeIdList = stringCfgDao.findSubscribeIdCfgListByCfgIndexInfo(entity);
		entity.setIpPortList(ipPortList);
		entity.setNtcSubscribeIdCfgList(subscribeIdList);
		entity.setSslList(sslList);
		return entity;
	}
	public CfgIndexInfo getDnsCfg(Long cfgId,Integer compileId){
		CfgIndexInfo entity = websiteCfgDao.getCfgIndexInfo(cfgId,compileId);
		List<IpPortCfg> ipPortList = websiteCfgDao.getIpPortList(entity);
		List<ComplexkeywordCfg> domainList = websiteCfgDao.getDnsDomainList(entity);
		List<NtcSubscribeIdCfg> subscribeIdList = stringCfgDao.findSubscribeIdCfgListByCfgIndexInfo(entity);
		entity.setIpPortList(ipPortList);
		entity.setDomainList(domainList);
		entity.setNtcSubscribeIdCfgList(subscribeIdList);
		return entity;
	}
	public Page<CfgIndexInfo> getWebsiteList(Page<CfgIndexInfo> page, CfgIndexInfo entity){
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"a"));
		entity.setPage(page);
		// 设置cfg_type,用于列表检索时区分
		if(entity.getHttpReqHdr() != null){
			entity.getHttpReqHdr().setCfgType(Constants.HTTP_REQ_HEAD_REGION);
		}
		if(entity.getHttpReqBody() != null){
			entity.getHttpReqBody().setCfgType(Constants.HTTP_REQ_BODY_REGION);
		}
		if(entity.getHttpResHdr() != null){
			entity.getHttpResHdr().setCfgType(Constants.HTTP_RES_HEAD_REGION);
		}
		if(entity.getHttpResBody() != null){
			entity.getHttpResBody().setCfgType(Constants.HTTP_RES_BODY_REGION);
		}
		List<CfgIndexInfo> list = websiteCfgDao.getWebsiteList(entity);
		page.setList(list);
		return page;
	}
	public void saveHttpBodyCfg(List<BaseStringCfg<?>> cfgs) {
		for(BaseStringCfg cfg:cfgs){
			HttpBodyCfg _cfg=new HttpBodyCfg();
			BeanUtils.copyProperties(cfg,_cfg);
			websiteCfgDao.saveHttpBodyCfg(_cfg);
		}
	}
	public void saveHttpReqHeadCfg(List<ComplexkeywordCfg> cfgs) {
		for(ComplexkeywordCfg cfg:cfgs){
			HttpReqHeadCfg _cfg=new HttpReqHeadCfg();
			BeanUtils.copyProperties(cfg,_cfg);
			websiteCfgDao.saveHttpReqHdrCfg(_cfg);
		}
	}
	public void saveHttpResHeadCfg(List<ComplexkeywordCfg> cfgs) {
		for(ComplexkeywordCfg cfg:cfgs){
			HttpResHeadCfg _cfg=new HttpResHeadCfg();
			BeanUtils.copyProperties(cfg,_cfg);
			websiteCfgDao.saveHttpResHdrCfg(_cfg);
		}
	}
	public void saveHttpCfg(CfgIndexInfo entity){
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
			if(entity.getIpPortList()!=null){
				for(IpPortCfg cfg:entity.getIpPortList()){
					BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
					websiteCfgDao.saveIpPortCfg(cfg);
				}
			}
			if(entity.getHttpUrlList()!=null){
				for(HttpUrlCfg cfg:entity.getHttpUrlList()){
					BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
					websiteCfgDao.saveHttpUrlCfg(cfg);
				}
			}
			if(entity.getHttpReqHdrList()!=null){
				for(HttpReqHeadCfg cfg:entity.getHttpReqHdrList()){
					BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
					if(StringUtils.isNoneBlank(cfg.getDistrictShowName()) && cfg.getDistrict().equals("others")){
						cfg.setDistrict(cfg.getDistrictShowName());
					}
					websiteCfgDao.saveHttpReqHdrCfg(cfg);
				}
			}
			if(entity.getHttpResHdrList()!=null){
				for(HttpResHeadCfg cfg:entity.getHttpResHdrList()){
					BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
					if(StringUtils.isNoneBlank(cfg.getDistrictShowName()) && cfg.getDistrict().equals("others")){
						cfg.setDistrict(cfg.getDistrictShowName());
					}
					websiteCfgDao.saveHttpResHdrCfg(cfg);
				}
			}
			if(entity.getHttpReqBodyList()!=null){
				for(HttpBodyCfg cfg:entity.getHttpReqBodyList()){
					BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
					websiteCfgDao.saveHttpBodyCfg(cfg);
				}
			}
			if(entity.getHttpResBodyList()!=null){
				for(HttpBodyCfg cfg:entity.getHttpResBodyList()){
					BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
					websiteCfgDao.saveHttpBodyCfg(cfg);
				}
			}
			if(entity.getNtcSubscribeIdCfgList()!=null){
				for(NtcSubscribeIdCfg cfg:entity.getNtcSubscribeIdCfgList()){
					BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
					stringCfgDao.saveSubscribeIdCfg(cfg);
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
			entity.setEditorId(entity.getCurrentUser().getId());
			entity.setEditTime(new Date());
			websiteCfgDao.updateCfgIndex(entity);
			//无效子配置后，再新增子配置
			websiteCfgDao.deleteHttpIpCfg(entity);
			websiteCfgDao.deleteHttpUrlCfg(entity);
			websiteCfgDao.deleteHttpReqHdrCfg(entity);
			websiteCfgDao.deleteHttpResHdrCfg(entity);
			websiteCfgDao.deleteHttpBodyCfg(entity);
			stringCfgDao.deleteSubscribeIdCfgByCfgIndexInfo(entity);
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
			if(entity.getHttpUrlList()!=null){
				for(HttpUrlCfg cfg:entity.getHttpUrlList()){
					BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
					websiteCfgDao.saveHttpUrlCfg(cfg);
				}
			}
			if(entity.getHttpReqHdrList()!=null){
				for(HttpReqHeadCfg cfg:entity.getHttpReqHdrList()){
					BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
					if(StringUtils.isNoneBlank(cfg.getDistrictShowName()) && cfg.getDistrict().equals("others")){
						cfg.setDistrict(cfg.getDistrictShowName());
					}
					websiteCfgDao.saveHttpReqHdrCfg(cfg);
				}
			}
			if(entity.getHttpResHdrList()!=null){
				for(HttpResHeadCfg cfg:entity.getHttpResHdrList()){
					BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
					if(StringUtils.isNoneBlank(cfg.getDistrictShowName()) && cfg.getDistrict().equals("others")){
						cfg.setDistrict(cfg.getDistrictShowName());
					}
					websiteCfgDao.saveHttpResHdrCfg(cfg);
				}
			}
			if(entity.getHttpReqBodyList()!=null){
				for(HttpBodyCfg cfg:entity.getHttpReqBodyList()){
					BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
					websiteCfgDao.saveHttpBodyCfg(cfg);
				}
			}
			if(entity.getHttpResBodyList()!=null){
				for(HttpBodyCfg cfg:entity.getHttpResBodyList()){
					BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
					websiteCfgDao.saveHttpBodyCfg(cfg);
				}
			}
			if(entity.getNtcSubscribeIdCfgList()!=null){
				for(NtcSubscribeIdCfg cfg:entity.getNtcSubscribeIdCfgList()){
					BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
					stringCfgDao.saveSubscribeIdCfg(cfg);
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
	public void updateHttpCfgValid(Integer isValid,String ids,Integer functionId){
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
			entity = this.getHttpCfg(Long.parseLong(id),entity.getCompileId());
			if(entity.getIpPortList()!=null && entity.getIpPortList().size()>0){
				IpPortCfg cfg = new IpPortCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(IpPortCfg.getTablename());
				websiteCfgDao.updateCfgValid(cfg);
			}
			if(entity.getHttpUrlList()!=null && entity.getHttpUrlList().size()>0){
				HttpUrlCfg cfg = new HttpUrlCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(HttpUrlCfg.getTablename());
				websiteCfgDao.updateCfgValid(cfg);
			}
			if(entity.getHttpReqHdrList()!=null && entity.getHttpReqHdrList().size()>0){
				HttpReqHeadCfg cfg = new HttpReqHeadCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(HttpReqHeadCfg.getTablename());
				websiteCfgDao.updateCfgValid(cfg);
			}
			if(entity.getHttpResHdrList()!=null && entity.getHttpResHdrList().size()>0){
				HttpResHeadCfg cfg = new HttpResHeadCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(HttpResHeadCfg.getTablename());
				websiteCfgDao.updateCfgValid(cfg);
			}
			if((entity.getHttpReqBodyList()!=null && entity.getHttpReqBodyList().size()>0) ||
					(entity.getHttpResBodyList()!=null && entity.getHttpResBodyList().size()>0)){
				HttpBodyCfg cfg = new HttpBodyCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(HttpBodyCfg.getTablename());
				websiteCfgDao.updateCfgValid(cfg);
			}
			if(entity.getNtcSubscribeIdCfgList()!=null && entity.getNtcSubscribeIdCfgList().size()>0){
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
	public void auditHttpCfg(CfgIndexInfo entity,Integer isAudit) throws Exception{
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
		entity = this.getHttpCfg(entity.getCfgId(),entity.getCompileId());
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
		if(entity.getHttpReqHdrList()!=null && entity.getHttpReqHdrList().size()>0){
			HttpReqHeadCfg cfg = new HttpReqHeadCfg();
			BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
			cfg.setTableName(HttpReqHeadCfg.getTablename());
			websiteCfgDao.auditCfg(cfg);
			if(isAudit==1){
				Map<String,List> map = cfgConvert(strRegionList,entity.getHttpReqHdrList(),2,entity,groupRelationList);
				groupRelationList=map.get("groupList");
				strRegionList=map.get("dstList");
			}
		}
		if(entity.getHttpResHdrList()!=null && entity.getHttpResHdrList().size()>0){
			HttpResHeadCfg cfg = new HttpResHeadCfg();
			BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
			cfg.setTableName(HttpResHeadCfg.getTablename());
			websiteCfgDao.auditCfg(cfg);
			if(isAudit==1){
				Map<String,List> map = cfgConvert(strRegionList,entity.getHttpResHdrList(),2,entity,groupRelationList);
				groupRelationList=map.get("groupList");
				strRegionList=map.get("dstList");
			}
		}
		if((entity.getHttpReqBodyList()!=null && entity.getHttpReqBodyList().size()>0) ||
				(entity.getHttpResBodyList()!=null && entity.getHttpResBodyList().size()>0)){
			HttpBodyCfg cfg = new HttpBodyCfg();
			BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
			cfg.setTableName(HttpBodyCfg.getTablename());
			websiteCfgDao.auditCfg(cfg);
			if(isAudit==1){
				if(entity.getHttpReqBodyList()!=null && entity.getHttpReqBodyList().size()>0){
					Map<String,List> map = cfgConvert(strRegionList,entity.getHttpReqBodyList(),2,entity,groupRelationList);
					groupRelationList=map.get("groupList");
					strRegionList=map.get("dstList");
				}
				if(entity.getHttpResBodyList()!=null && entity.getHttpResBodyList().size()>0){
					Map<String,List> map = cfgConvert(strRegionList,entity.getHttpResBodyList(),2,entity,groupRelationList);
					groupRelationList=map.get("groupList");
					strRegionList=map.get("dstList");
				}
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
			
			//判断http配置是否需要设置自定义域参数
			String actionValue = DictUtils.getDictCode("SERVICE_ACTION", "action_monit");
			String userRegion = "";
			//只有监测业务，并且头域的匹配区域不匹配字典中的信息时需要设置
			if(!actionValue.equals("默认") && entity.getAction().equals(Integer.parseInt(actionValue))){
				//请求头域
				if(entity.getHttpReqHdrList()!=null && entity.getHttpReqHdrList().size()>0){
					for(ComplexkeywordCfg head:entity.getHttpReqHdrList()){
						String dictValue = DictUtils.getDictCode(Constants.HTTP_HEADER_DICT_MODULE, head.getDistrict());
						if(StringUtil.isEmpty(dictValue) || dictValue.equals("默认")){
							if(userRegion.equals("")){
								userRegion += Constants.HTTP_HEADER_USER_REGION_KEY+"="+head.getDistrict();
							}else{
								userRegion += Constants.USER_REGION_SPLIT+Constants.HTTP_HEADER_USER_REGION_KEY+"="+head.getDistrict();
							}
							
						}
					}
				}
				//应答头域
				if(entity.getHttpResHdrList()!=null && entity.getHttpResHdrList().size()>0){
					for(ComplexkeywordCfg head:entity.getHttpResHdrList()){
						String dictValue = DictUtils.getDictCode(Constants.HTTP_HEADER_DICT_MODULE, head.getDistrict());
						if(StringUtil.isEmpty(dictValue) || dictValue.equals("默认")){
							if(userRegion.equals("")){
								userRegion += Constants.HTTP_HEADER_USER_REGION_KEY+"="+head.getDistrict();
							}else{
								userRegion += Constants.USER_REGION_SPLIT+Constants.HTTP_HEADER_USER_REGION_KEY+"="+head.getDistrict();
							}
							
						}
					}
				}
				if(!StringUtil.isEmpty(userRegion)){
					maatCfg.setUserRegion(userRegion);
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
			logger.info("http配置下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
			logger.info("http配置下发响应信息："+result.getMsg());

		}else if(isAudit==3){
			maatCfg.setCompileId(entity.getCompileId());
			maatCfg.setServiceId(entity.getServiceId());
			maatCfg.setIsValid(0);//无效
			configCompileList.add(maatCfg);
			//http关键字配置失效时，需确认是否存在与之关联的HTTPURL配置
			List<MaatCfg> httpUrlCompile=new ArrayList<>();
			if(entity.getFunctionId().equals(635)){
				httpUrlCompile=auditHttpUrlByBodyId(maatCfg,httpUrlCompile,entity);
			}
			if(!StringUtil.isEmpty(httpUrlCompile)){
				configCompileList.addAll(httpUrlCompile);
			}
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(entity.getAuditTime());
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.UPDATE_ACTION);
			//调用服务接口取消配置
			String json=gsonToJson(maatBean);
			logger.info("http配置下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json,1);
			logger.info("http配置取消配置响应信息："+result.getMsg());
			
		}
	}
	public List<MaatCfg> auditHttpUrlByBodyId(MaatCfg maatCfgBody,List<MaatCfg> httpUrlCompile,CfgIndexInfo cfgHttpBody) throws Exception{
		ToMaatBean maatBean = new ToMaatBean();		
		MaatCfg maatCfg = new MaatCfg();
		
		CfgIndexInfo cfg=new CfgIndexInfo();
		cfg.setSourceCompileId(maatCfgBody.getCompileId());
		cfg.setFunctionId(6);//http url
		List<CfgIndexInfo> cfgs=websiteCfgDao.getCfgIndexInfos(cfg);
		//关键字包含的所有url配置
		for (CfgIndexInfo cfgIndexInfo : cfgs) {
			//查询所有httpurl配置
			List<HttpUrlCfg> httpUrlList=websiteCfgDao.getHttpUrlList(cfgIndexInfo);
			
			//审核通过的，失效  3 0
			if(cfgIndexInfo.getIsAudit().equals(1)){
				cfgIndexInfo.setTableName(CfgIndexInfo.getTablename());
				cfgIndexInfo.setAuditorId(UserUtils.getUser().getId());
				cfgIndexInfo.setAuditTime(cfgHttpBody.getAuditTime());
				cfgIndexInfo.setIsAudit(3);
				cfgIndexInfo.setIsValid(0);
				//设置配置取消的来函信息
				serviceDictInfoDao.auditCancleRequestInfo(cfgHttpBody.getCancelRequestId(), 
															"cfg_index_info",
															cfgIndexInfo.getCfgId().toString());
				websiteCfgDao.auditCfg(cfgIndexInfo);
				for (BaseStringCfg httpUrlCfg : httpUrlList) {
					httpUrlCfg.setTableName(HttpUrlCfg.getTablename());
					httpUrlCfg.setAuditorId(UserUtils.getUser().getId());
					httpUrlCfg.setAuditTime(cfgHttpBody.getAuditTime());
					httpUrlCfg.setIsAudit(3);
					httpUrlCfg.setIsValid(0);
					websiteCfgDao.auditCfg(httpUrlCfg);
				}
				
			}else if(!(cfgIndexInfo.getIsAudit().equals(3) || cfgIndexInfo.getIsValid().equals(-1))){
				//未审核和审核未通过，删除  0 -1
				cfgIndexInfo.setTableName(CfgIndexInfo.getTablename());
				cfgIndexInfo.setEditorId(UserUtils.getUser().getId());
				cfgIndexInfo.setEditTime(maatCfgBody.getAuditTime());
				cfgIndexInfo.setIsAudit(0);
				cfgIndexInfo.setIsValid(-1);
				websiteCfgDao.updateCfgValid(cfgIndexInfo);
				
				for (BaseStringCfg httpUrlCfg : httpUrlList) {
					httpUrlCfg.setTableName(HttpUrlCfg.getTablename());
					httpUrlCfg.setEditorId(UserUtils.getUser().getId());
					httpUrlCfg.setEditTime(maatCfgBody.getAuditTime());
					httpUrlCfg.setIsAudit(0);
					httpUrlCfg.setIsValid(-1);
					websiteCfgDao.updateCfgValid(httpUrlCfg);
				}
			}
			//只有审核通过的配置需要下发失效动作
			if(cfgIndexInfo.getIsAudit().equals(3)){
				maatCfg.setCompileId(cfgIndexInfo.getCompileId());
				maatCfg.setServiceId(cfgIndexInfo.getServiceId());
				maatCfg.setIsValid(0);//无效
				httpUrlCompile.add(maatCfg);
			}
		}
		
		return httpUrlCompile;
	}
	public void saveSslCfg(List<BaseStringCfg<?>> cfgs){
		for(BaseStringCfg cfg:cfgs){
			if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
				websiteCfgDao.saveSslKeywordCfg(cfg);
			}
		}
	}
	public void saveSslCfg(CfgIndexInfo entity){
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
			if(entity.getIpPortList()!=null){
				for(IpPortCfg cfg:entity.getIpPortList()){
					BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
					websiteCfgDao.saveIpPortCfg(cfg);
				}
			}
			if(entity.getSslList()!=null){
				for(BaseStringCfg cfg:entity.getSslList()){
					if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
						BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
						websiteCfgDao.saveSslKeywordCfg(cfg);
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
			entity.setEditorId(entity.getCurrentUser().getId());
			entity.setEditTime(new Date());
			websiteCfgDao.updateCfgIndex(entity);
			//无效子配置后，再新增子配置
			websiteCfgDao.deleteSslIpCfg(entity);
			websiteCfgDao.deleteSslKeywordCfg(entity);
			stringCfgDao.deleteSubscribeIdCfgByCfgIndexInfo(entity);
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
			if(entity.getSslList()!=null){
				for(BaseStringCfg cfg:entity.getSslList()){
					if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
						BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
						websiteCfgDao.saveSslKeywordCfg(cfg);
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
	public void updateSslCfgValid(Integer isValid,String ids,Integer functionId){
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
			entity = this.getSslCfg(Long.parseLong(id),entity.getCompileId());
			if(entity.getIpPortList()!=null && entity.getIpPortList().size()>0){
				IpPortCfg cfg = new IpPortCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(IpPortCfg.getTablename());
				websiteCfgDao.updateCfgValid(cfg);
			}
			if(entity.getSslList()!=null && entity.getSslList().size()>0)
			{
				SslKeywordCfg cfg = new SslKeywordCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(SslKeywordCfg.getTablename());
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
	public void auditSslCfg(CfgIndexInfo entity,Integer isAudit) throws MaatConvertException{
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
		entity = this.getSslCfg(entity.getCfgId(),entity.getCompileId());
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
		if(entity.getSslList()!=null && entity.getSslList().size()>0){
			SslKeywordCfg cfg = new SslKeywordCfg();
			BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
			cfg.setTableName(SslKeywordCfg.getTablename());
			websiteCfgDao.auditCfg(cfg);
			if(isAudit==1){
				Map<String,List> map = cfgConvert(strRegionList,entity.getSslList(),2,entity,groupRelationList);
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
			logger.info("ssl配置下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
			logger.info("ssl配置下发响应信息："+result.getMsg());

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
			logger.info("ssl配置下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json,1);
			logger.info("ssl配置取消配置响应信息："+result.getMsg());
		}
	}
	public void saveDnsCfg(List<ComplexkeywordCfg> complexkeywordCfgs){
		for(ComplexkeywordCfg cfg:complexkeywordCfgs){
			websiteCfgDao.saveDnsDomainCfg(cfg);
		}
	}
	public void saveHttpUrlCfg(List<BaseStringCfg<?>> cfgs){
		for(BaseStringCfg cfg:cfgs){
			HttpUrlCfg _cfg=new HttpUrlCfg();
			BeanUtils.copyProperties(cfg, _cfg);
			websiteCfgDao.saveHttpUrlCfg(_cfg);
		}
	}
	/**
	 * 保存dns配置
	 * @param entity
	 */
	
	public void saveDnsCfg(CfgIndexInfo entity){
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
			if(entity.getIpPortList()!=null){
				for(IpPortCfg cfg:entity.getIpPortList()){
					BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
					websiteCfgDao.saveIpPortCfg(cfg);
				}
			}
			if(entity.getDomainList()!=null){
				for(ComplexkeywordCfg cfg:entity.getDomainList()){
					if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
						BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
						if(StringUtils.isNoneBlank(cfg.getDistrictShowName()) && cfg.getDistrict().equals("others")){
							cfg.setDistrict(cfg.getDistrictShowName());
						}
						websiteCfgDao.saveDnsDomainCfg(cfg);
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
			entity.setEditorId(entity.getCurrentUser().getId());
			entity.setEditTime(new Date());
			websiteCfgDao.updateCfgIndex(entity);
			//无效子配置后，再新增子配置
			websiteCfgDao.deleteDnsIpCfg(entity);
			websiteCfgDao.deleteDnsDomainCfg(entity);
			stringCfgDao.deleteSubscribeIdCfgByCfgIndexInfo(entity);
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
			if(entity.getDomainList()!=null){
				for(ComplexkeywordCfg cfg:entity.getDomainList()){
					if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
						BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
						if(StringUtils.isNoneBlank(cfg.getDistrictShowName()) && cfg.getDistrict().equals("others")){
							cfg.setDistrict(cfg.getDistrictShowName());
						}
						websiteCfgDao.saveDnsDomainCfg(cfg);
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
	/**
	 * dns配置删除
	 * @param isValid
	 * @param ids
	 * @param functionId
	 */
	public void updateDnsCfgValid(Integer isValid,String ids,Integer functionId){
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
			entity = this.getDnsCfg(Long.parseLong(id),entity.getCompileId());
			if(entity.getIpPortList()!=null && entity.getIpPortList().size()>0){
				IpPortCfg cfg = new IpPortCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(IpPortCfg.getTablename());
				websiteCfgDao.updateCfgValid(cfg);
			}
			if(entity.getDomainList()!=null && entity.getDomainList().size()>0)
			{
				DnsDomainCfg cfg = new DnsDomainCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(DnsDomainCfg.getTablename());
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
	/**
	 * dns配置审核
	 * @param entity
	 * @param isAudit
	 * @throws MaatConvertException
	 */
	public void auditDnsCfg(CfgIndexInfo entity,Integer isAudit) throws MaatConvertException{
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
		entity = this.getDnsCfg(entity.getCfgId(),entity.getCompileId());
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
		if(entity.getDomainList()!=null && entity.getDomainList().size()>0){
			DnsDomainCfg cfg = new DnsDomainCfg();
			BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
			cfg.setTableName(DnsDomainCfg.getTablename());
			websiteCfgDao.auditCfg(cfg);
			if(isAudit==1){
				Map<String,List> map = cfgConvert(strRegionList,entity.getDomainList(),2,entity,groupRelationList);
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
			// dns reject userRegion=;dnsStrategyId;
			if(!StringUtil.isEmpty(entity.getDnsStrategyId())){
//				maatCfg.setUserRegion(Constants.USER_REGION_SPLIT+entity.getDnsStrategyId()+Constants.USER_REGION_SPLIT);
				maatCfg.setUserRegion(Constants.DNS_STRATEGY_USER_REGION_KEY+"="+entity.getDnsStrategyId());
			}
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(entity.getAuditTime());
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.INSERT_ACTION);
			//调用服务接口下发配置数据
			String json=gsonToJson(maatBean);
			logger.info("dns配置下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
			logger.info("dns配置下发响应信息："+result.getMsg());

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
			logger.info("dns配置下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json,1);
			logger.info("dns配置取消配置响应信息："+result.getMsg());
		}
	}
}
