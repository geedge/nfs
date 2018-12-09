package com.nis.web.service.configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.configuration.AreaIpCfg;
import com.nis.domain.configuration.CfgIndexInfo;
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
import com.nis.web.dao.configuration.StringCfgDao;
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
public class HttpRedirectCfgService extends CrudService<WebsiteCfgDao,CfgIndexInfo> {
	@Autowired
	protected WebsiteCfgDao websiteCfgDao;
	@Autowired
	protected AreaIpCfgDao areaIpCfgDao;
	@Autowired
	protected StringCfgDao stringCfgDao;
	
	public CfgIndexInfo getHttpCfg(Long cfgId,Integer compileId){
		CfgIndexInfo entity = websiteCfgDao.getCfgIndexInfo(cfgId,compileId);
		List<HttpUrlCfg> httpUrlList = websiteCfgDao.getHttpUrlList(entity);
		List<HttpReqHeadCfg> httpReqHdrList = websiteCfgDao.getHttpReqHdrList(entity);
		List<HttpResHeadCfg> httpResHdrList = websiteCfgDao.getHttpResHdrList(entity);
		List<IpPortCfg> ipPortCfgList = websiteCfgDao.getIpPortList(entity);
		entity.setCfgType(Constants.HTTP_REDIRECT_REQ_BODY_REGION);
		List<HttpBodyCfg> httpReqBodyList = websiteCfgDao.getHttpReqBodyList(entity);
		entity.setCfgType(Constants.HTTP_REDIRECT_RES_BODY_REGION);
		List<HttpBodyCfg> httpResBodyList = websiteCfgDao.getHttpResBodyList(entity);
		List<NtcSubscribeIdCfg> subscribeIdList = stringCfgDao.findSubscribeIdCfgListByCfgIndexInfo(entity);
		entity.setHttpReqBodyList(httpReqBodyList);
		entity.setHttpReqHdrList(httpReqHdrList);
		entity.setHttpResBodyList(httpResBodyList);
		entity.setHttpResHdrList(httpResHdrList);
		entity.setHttpUrlList(httpUrlList);
		entity.setIpPortList(ipPortCfgList);
		entity.setNtcSubscribeIdCfgList(subscribeIdList);
		return entity;
	}
	
	public Map<String, List> exportHttpCfg(CfgIndexInfo entity){
		Map<String, List> dataMap=new HashMap<String, List>();
		List<HttpUrlCfg> httpUrlList = websiteCfgDao.getHttpUrlList(entity);
		List<HttpReqHeadCfg> httpReqHdrList = websiteCfgDao.getHttpReqHdrList(entity);
		List<HttpResHeadCfg> httpResHdrList = websiteCfgDao.getHttpResHdrList(entity);
		List<IpPortCfg> ipPortCfgList = websiteCfgDao.getIpPortList(entity);
		entity.setCfgType(Constants.HTTP_REDIRECT_REQ_BODY_REGION);
		List<HttpBodyCfg> httpReqBodyList = websiteCfgDao.getHttpReqBodyList(entity);
		entity.setCfgType(Constants.HTTP_REDIRECT_RES_BODY_REGION);
		List<HttpBodyCfg> httpResBodyList = websiteCfgDao.getHttpResBodyList(entity);
		List<NtcSubscribeIdCfg> subscribeIdList = stringCfgDao.findSubscribeIdCfgListByCfgIndexInfo(entity);
		dataMap.put("NTC_UNIVERSAL_IP", ipPortCfgList);
		dataMap.put("NTC_HTTP_URL", httpUrlList);
		dataMap.put("NTC_HTTP_REQ_HDR", httpReqHdrList);
		dataMap.put("NTC_HTTP_RES_HDR", httpResHdrList);
		dataMap.put("NTC_HTTP_REQ_BODY", httpReqBodyList);
		dataMap.put("NTC_HTTP_RES_BODY", httpResBodyList);
		dataMap.put("NTC_SUBSCRIBE_ID", subscribeIdList);
		return dataMap;
	}
	
	public Page<CfgIndexInfo> getHttpRedirectList(Page<CfgIndexInfo> page, CfgIndexInfo entity){
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"a"));
		entity.setPage(page);
		// 设置cfg_type,用于列表检索时区分
		if(entity.getHttpReqHdr() != null){
			entity.getHttpReqHdr().setCfgType(Constants.HTTP_REDIRECT_REQ_HEAD_REGION);
		}
		if(entity.getHttpReqBody() != null){
			entity.getHttpReqBody().setCfgType(Constants.HTTP_REDIRECT_REQ_BODY_REGION);
		}
		if(entity.getHttpResHdr() != null){
			entity.getHttpResHdr().setCfgType(Constants.HTTP_REDIRECT_RES_HEAD_REGION);
		}
		if(entity.getHttpResBody() != null){
			entity.getHttpResBody().setCfgType(Constants.HTTP_REDIRECT_RES_BODY_REGION);
		}
		List<CfgIndexInfo> list = websiteCfgDao.getWebsiteList(entity);
		page.setList(list);
		return page;
	}
	public void saveHttpCfg(CfgIndexInfo entity){
		//设置区域运营商信息
		setAreaEffectiveIds(entity);
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
				websiteCfgDao.saveCfgIndex(entity);
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
				if(entity.getIpPortList()!=null){
					for(IpPortCfg cfg:entity.getIpPortList()){
						BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
						websiteCfgDao.saveIpPortCfg(cfg);
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
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("获取编译ID出错");
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+e.getMessage());
			}
			
		}else{
			// 审核未通过状态的配置 修改后状态改为未审核
			entity.setIsValid(0);
			entity.setIsAudit(0);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			websiteCfgDao.updateCfgIndex(entity);
			//无效子配置后，再新增子配置
			websiteCfgDao.deleteHttpUrlCfg(entity);
			websiteCfgDao.deleteHttpReqHdrCfg(entity);
			websiteCfgDao.deleteHttpResHdrCfg(entity);
			websiteCfgDao.deleteHttpBodyCfg(entity);
			websiteCfgDao.deleteHttpIpCfg(entity);
			stringCfgDao.deleteSubscribeIdCfgByCfgIndexInfo(entity);

			AreaIpCfg area = new AreaIpCfg();
			area.setCompileId(entity.getCompileId());
			area.setFunctionId(entity.getFunctionId());
			areaIpCfgDao.deleteAreaIpCfg(area);
			entity.setCreateTime(new Date());
			entity.setCreatorId(entity.getCurrentUser().getId());
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
			if(entity.getIpPortList()!=null){
				for(IpPortCfg cfg:entity.getIpPortList()){
					BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
					websiteCfgDao.saveIpPortCfg(cfg);
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
			if((entity.getNtcSubscribeIdCfgList()!=null && entity.getNtcSubscribeIdCfgList().size()>0)){
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
	public void auditHttpCfg(CfgIndexInfo entity,Integer isAudit) throws MaatConvertException{
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
			String userRegion = "";
			
			//HTTP block： response code is not null ;response content null(userRegion is not null)
			if(entity.getAction().equals(16)){
				String userRegion1=StringUtil.isEmpty(entity.getUserRegion1()) ? "":entity.getUserRegion1();
				userRegion=Constants.REDIRECT_RESPONSE_CODE_KEY+"="+userRegion1;
				String userRegion3=StringUtil.isEmpty(entity.getUserRegion3()) ? "":entity.getUserRegion3();//应答文件
				if(StringUtils.isNotBlank(userRegion3)) {
					userRegion=userRegion+";"+Constants.CONTENT_FILE_ID_KEY+"="+userRegion3;
				}else {//应答内容
					String userRegion2=StringUtil.isEmpty(entity.getUserRegion2()) ? "":entity.getUserRegion2();
					userRegion=userRegion+";"+Constants.REDIRECT_CONTENT_KEY+"="+userRegion2;
				}
			}
			//HTTP redirect： response code is not null ;response content is not null(userRegion is not null)
			if(entity.getAction().equals(48)){
				String userRegion1=StringUtil.isEmpty(entity.getUserRegion1()) ? "":entity.getUserRegion1();
				userRegion=Constants.REDIRECT_RESPONSE_CODE_KEY+"="+userRegion1;
				String userRegion3=StringUtil.isEmpty(entity.getUserRegion3()) ? "":entity.getUserRegion3();//应答文件
				if(StringUtils.isNotBlank(userRegion3)) {
					userRegion=userRegion+";"+Constants.CONTENT_FILE_ID_KEY+"="+userRegion3;
				}else {//应答内容
					String userRegion2=StringUtil.isEmpty(entity.getUserRegion2()) ? "":entity.getUserRegion2();
					userRegion=userRegion+";"+Constants.REDIRECT_URL_KEY+"="+userRegion2;
				}
			}
			//HTTP replace： replace type is not null ;find is not null;replace with is not null(userRegion is not null)
			if(entity.getAction().equals(80)){
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
			}
			if(StringUtil.isEmpty(userRegion)){
				userRegion=Constants.USER_REGION_PLACEHOLDER;
			}
			maatCfg.setUserRegion(userRegion);
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
	
}
