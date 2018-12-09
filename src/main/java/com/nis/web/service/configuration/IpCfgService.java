package com.nis.web.service.configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nis.domain.FunctionRegionDict;
import com.nis.domain.Page;
import com.nis.domain.basics.AsnIpCfg;
import com.nis.domain.callback.InlineIp;
import com.nis.domain.configuration.AreaIpCfg;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.CfgIndexInfo;
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
import com.nis.domain.specific.ConfigGroupInfo;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.AsnCacheUtils;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.Configurations;
import com.nis.util.Constants;
import com.nis.util.DictUtils;
import com.nis.util.StringUtil;
import com.nis.web.dao.basics.AsnIpCfgDao;
import com.nis.web.dao.configuration.AreaIpCfgDao;
import com.nis.web.dao.configuration.IpCfgDao;
import com.nis.web.dao.configuration.StringCfgDao;
import com.nis.web.dao.specific.ConfigGroupInfoDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.CrudService;
import com.nis.web.service.SpringContextHolder;

/**
 * IP相关配置事务类
 * @author dell
 *
 */
@Service
public class IpCfgService extends CrudService<IpCfgDao,BaseIpCfg> {
	@Autowired
	protected IpCfgDao ipCfgDao;
	@Autowired
	protected StringCfgDao stringCfgDao;
	@Autowired
	protected AreaIpCfgDao areaIpCfgDao;
	@Autowired
	//protected SpecificServiceCfgDao specificServiceCfgDao;
	protected ConfigGroupInfoDao configGroupInfoDao;
	@Autowired
	protected AsnIpCfgDao asnIpCfgDao;
	/**
	 * 
	 * addIpCfg(新增IP类配置)
	 * (继承BaseIpCfg这个类方可使用)
	 * @param baseIpCfg
	 * @return 
	 *int
	 * @exception 
	 * @since  1.0.0
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void addIpCfg(BaseIpCfg cfg){
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
			ipCfgDao.insert(cfg);
		}else{
			throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>: compileId is 0");
		}
		
	} 
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveIpCfg(CfgIndexInfo entity){
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
			ipCfgDao.saveCfgIndex(entity);
			if(entity.getIpPortList()!=null){
				for(IpPortCfg cfg:entity.getIpPortList()){
					BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
					ipCfgDao.saveIpPortCfg(cfg);
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
			ipCfgDao.updateCfgIndex(entity);
			//无效子配置后，再新增子配置
			ipCfgDao.deleteIpCfg(entity);
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
					ipCfgDao.saveIpPortCfg(cfg);
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
	public void updateIpCfgValid(Integer isValid,String ids,Integer functionId){
		String[] idArray = ids.split(",");
		for(String id :idArray){
			CfgIndexInfo entity = new CfgIndexInfo();
			entity.setCfgId(Long.parseLong(id));
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			entity.setTableName(CfgIndexInfo.getTablename());
			entity.setFunctionId(functionId);
			ipCfgDao.updateCfgValid(entity);
			//查询子配置
			entity = this.getIpPortCfg(Long.parseLong(id),entity.getCompileId());
			if(entity.getIpPortList()!=null && entity.getIpPortList().size()>0){
				IpPortCfg cfg = new IpPortCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(IpPortCfg.getTablename());
				ipCfgDao.updateCfgValid(cfg);
			}
			if(entity.getNtcSubscribeIdCfgList()!=null && entity.getNtcSubscribeIdCfgList().size()>0)
			{
				NtcSubscribeIdCfg cfg = new NtcSubscribeIdCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(NtcSubscribeIdCfg.getTablename());
				ipCfgDao.updateCfgValid(cfg);
			}
			//保存区域IP信息
			if(entity.getAreaCfg()!=null && entity.getAreaCfg().size()>0){
				AreaIpCfg cfg = new AreaIpCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(AreaIpCfg.getTablename());
				ipCfgDao.updateCfgValid(cfg);
			}
			
		}
		
	}
	/**
	 * 
	 * updateIpCfg(更新IP类配置)
	 * (继承BaseIpCfg这个类方可使用)
	 * @param baseIpCfg
	 * @return 
	 *int
	 * @exception 
	 * @since  1.0.0
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void updateIpCfg(BaseIpCfg cfg){
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
		ipCfgDao.update(cfg);
	}
	@Deprecated
	public void audit(BaseIpCfg cfg) throws Exception{
		//更新IP配置与区域IP的状态
		List<BaseIpCfg> beans=new ArrayList<>();
		beans.add(cfg);
		ipCfgDao.audit(cfg);
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
		String json="";
		//获取region
		List<FunctionRegionDict> dictList =  DictUtils.getFunctionRegionDictList(cfg.getFunctionId());
		int maatType=0;
		//判断下发类型是走maat还是callback
		String regionValue=cfg.getCfgType();
		if(regionValue!=null) {
			for(FunctionRegionDict region:dictList) {
				if(regionValue.equals(region.getConfigRegionValue())) {
					maatType=region.getIsMaat();
					break;
				}
			}
		}
		Properties props=this.getMsgProp();
		if(cfg.getIsAudit()==Constants.AUDIT_YES){
			if(maatType==Constants.CALLBACK_TYPE){
				List<InlineIp> ipList=new ArrayList<>();
				InlineIp ip=convertCallBackIp(cfg,null);
				ipList.add(ip);
				//调用服务接口下发配置数据
				json=gsonToJson(ipList);
				logger.info("IP配置下发配置参数："+json);
				//调用服务接口下发配置
				try {
					ToMaatResult result = ConfigServiceUtil.postCallbackCfg(json);
					if(result!=null){
						logger.info("IP配置配置下发响应信息："+result.getMsg());
					}
				} catch (Exception e) {
					logger.error("IP配置配置下发失败",e);
					throw e;
				}
			}else if(maatType==Constants.MAAT_TYPE){
				Map<String,List> map = cfgConvert(ipRegionList,beans,1,cfg,groupRelationList);
				ipRegionList=map.get("dstList");
				groupRelationList=map.get("groupList");
				numRegionList=map.get("numRegionList")==null?new ArrayList<>():map.get("numRegionList");
				Map<String,List> areaMap = cfgConvert(areaIpRegionList,beans1,1,cfg,groupRelationList);
				groupRelationList=areaMap.get("groupList");
				areaIpRegionList=areaMap.get("dstList");
				//maatCfg.setAreaEffectiveIds(StringUtils.isBlank(cfg.getAreaEffectiveIds())?"0":cfg.getAreaEffectiveIds());
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
				if(Constants.SERVICE_IP_MULITIPLEX==cfg.getServiceId().intValue()){
					String region=Constants.USERREGION_IR_STRATEGY+"="+cfg.getDnsStrategyId()+Constants.USER_REGION_SPLIT
							+Constants.USERREGION_IR_TYPE+"="+cfg.getIrType();
					maatCfg.setUserRegion(region);
				}else if(Constants.SERVICE_IP_RATELIMIT==cfg.getServiceId().intValue()){
					maatCfg.setUserRegion(Constants.USERREGION_RATE_LIMIT+"="+cfg.getRatelimit());
				}
				//限速需要发Droprate=0.001 ，暂不支持Bandwidth=200kbps
				if(cfg.getAction().equals(Constants.RATELIMIT_ACTION)){
					if(cfg.getUserRegion1().equals("0")){//丢包率
						cfg.setUserRegion2(StringUtil.isEmpty(cfg.getUserRegion2()) ? "":cfg.getUserRegion2());
						maatCfg.setUserRegion(Constants.INTERCEPT_IP_RATELIMIT_DROPRATE_USER_REGION_KEY+"="+cfg.getUserRegion2());
					}else if(cfg.getUserRegion1().equals("1")){//带宽
						cfg.setUserRegion3(StringUtil.isEmpty(cfg.getUserRegion3()) ? "":cfg.getUserRegion3());
						maatCfg.setUserRegion(Constants.INTERCEPT_IP_RATELIMIT_BANDWITH_USER_REGION_KEY+"="+cfg.getUserRegion3());
					}
				}else {
					if(!StringUtil.isEmpty(cfg.getUserRegion1())){
						String userRegion = "";
						if(cfg.getUserRegion1().startsWith(Constants.REDIRECT_RESPONSE_CODE_STARTWITH)){
							userRegion = Constants.REDIRECT_RESPONSE_CODE_KEY+"="+cfg.getUserRegion1()+
										 Constants.USER_REGION_SPLIT+
										 Constants.REDIRECT_URL_KEY+"="+cfg.getUserRegion2();
						}else{
							userRegion = Constants.REDIRECT_RESPONSE_CODE_KEY+"="+cfg.getUserRegion1()+
									 Constants.USER_REGION_SPLIT+
									 Constants.REDIRECT_CONTENT_KEY+"="+cfg.getUserRegion2();
						}
						maatCfg.setUserRegion(userRegion);
					}
				}
				configCompileList.add(maatCfg);
				maatBean.setOpAction(Constants.INSERT_ACTION);
				maatBean.setConfigCompileList(configCompileList);
				maatBean.setAuditTime(cfg.getAuditTime());
				maatBean.setCreatorName(cfg.getCurrentUser().getName());
				maatBean.setVersion(Constants.MAAT_VERSION);
				//调用服务接口下发配置数据
				json=gsonToJson(maatBean);
				logger.info("IP配置下发配置参数："+json);
				//调用服务接口下发配置
				try {
					ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
					if(result!=null){
						logger.info("IP配置配置下发响应信息："+result.getMsg());
					}
				} catch (Exception e) {
					logger.error("IP配置配置下发失败",e);
					throw e;
				}
			}else {
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+props.getProperty("unknown_cfg_type"));
			}
		}else if(cfg.getIsAudit()==Constants.AUDIT_NOT_YES){
			if(maatType==Constants.CALLBACK_TYPE){
				List<InlineIp> ipList=new ArrayList<>();
				InlineIp ip=convertCallBackIp(cfg,null);
				ipList.add(ip);
				//调用服务接口取消配置
				json=gsonToJson(ipList);
				logger.info("IP管控配置参数："+json);
				//调用服务接口取消配置
				try {
					ToMaatResult result = ConfigServiceUtil.put(json, 2);
					logger.info("IP配置取消配置响应信息："+result.getMsg());
				} catch (Exception e) {
					e.printStackTrace();
					logger.info("IP配置取消配置失败");
					throw e;
				}
			}else if(maatType==Constants.MAAT_TYPE){
				maatCfg.setCompileId(cfg.getCompileId());
				maatCfg.setServiceId(cfg.getServiceId());
				maatCfg.setIsValid(0);//无效
				configCompileList.add(maatCfg);
				maatBean.setConfigCompileList(configCompileList);
				maatBean.setAuditTime(cfg.getAuditTime());
				maatBean.setCreatorName(cfg.getCurrentUser().getName());
				maatBean.setVersion(Constants.MAAT_VERSION);
				maatBean.setOpAction(Constants.UPDATE_ACTION);
				//调用服务接口取消配置
				json=gsonToJson(maatBean);
				logger.info("IP管控配置参数："+json);
				//调用服务接口取消配置
				try {
					ToMaatResult result = ConfigServiceUtil.put(json, 1);
					logger.info("IP管控取消配置响应信息："+result.getMsg());
				} catch (Exception e) {
					e.printStackTrace();
					logger.info("IP管控取消配置失败");
					throw e;
				}
			}else {
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+props.getProperty("unknown_cfg_type"));
			}
		}
	}
	public void auditIpCfg(CfgIndexInfo entity,Integer isAudit) throws MaatConvertException{
		Properties props=this.getMsgProp();
		//修改数据库审核状态信息
		entity.setTableName(CfgIndexInfo.getTablename());
		entity.setIsAudit(isAudit);
		ipCfgDao.auditCfg(entity);
		ToMaatBean maatBean = new ToMaatBean();		
		MaatCfg maatCfg = new MaatCfg();
		List<MaatCfg> configCompileList = new ArrayList();
		List<GroupCfg> groupRelationList = new ArrayList();
		List<IpCfg> ipRegionList = new ArrayList();
		List<StringCfg> strRegionList = new ArrayList();
		List<NumBoundaryCfg> numRegionList = new ArrayList();
		List<DigestCfg> digestRegionList = new ArrayList();
		List<IpCfg> areaIpRegionList = new ArrayList();
		//获取region
		List<FunctionRegionDict> dictList =  DictUtils.getFunctionRegionDictList(entity.getFunctionId());
		int maatType=0;
		//查询子配置并修改审核状态
		entity = this.getIpPortCfg(entity.getCfgId(),entity.getCompileId());
		if(entity.getIpPortList()!=null && entity.getIpPortList().size()>0){
			//判断下发类型是走maat还是callback
			String regionValue=entity.getIpPortList().get(0).getCfgType();
			if(regionValue!=null) {
				for(FunctionRegionDict region:dictList) {
					if(regionValue.equals(region.getConfigRegionValue())) {
						maatType=region.getIsMaat();
						break;
					}
				}
			}
			for(IpPortCfg cfg:entity.getIpPortList()) {
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId","cfgType","userRegion1","userRegion2","userRegion3","userRegion4","userRegion5"});
				cfg.setTableName(IpPortCfg.getTablename());
				ipCfgDao.auditCfg(cfg);
				/*BeanUtils.copyProperties(entity, cfg, new String[]{"userRegion1","userRegion2","userRegion3","userRegion4","userRegion5","ipType","direction",
						"protocol","protocolId","areaEffectiveIds","cfgRegionCode",
						"cfgType","ipPattern","srcIpAddress","portPattern","srcPort","destIpAddress","destPort"});
				cfg.setTableName(IpPortCfg.getTablename());
				ipCfgDao.auditCfg(cfg);*/
			}
			if(isAudit==1&&maatType==Constants.MAAT_TYPE){
				Map<String,List> map = cfgConvert(ipRegionList,entity.getIpPortList(),1,entity,groupRelationList);
				groupRelationList=map.get("groupList");
				ipRegionList=map.get("dstList");
				if(map.get("numRegionList")!=null){
					numRegionList.addAll(map.get("numRegionList"));
				}
			}
		}
		if(entity.getNtcSubscribeIdCfgList()!=null && entity.getNtcSubscribeIdCfgList().size()>0){
			NtcSubscribeIdCfg cfg = new NtcSubscribeIdCfg();
			BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
			cfg.setTableName(NtcSubscribeIdCfg.getTablename());
			ipCfgDao.auditCfg(cfg);
			if(isAudit==1&&maatType==Constants.MAAT_TYPE){
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
			ipCfgDao.auditCfg(cfg);
			if(isAudit==1&&maatType==Constants.MAAT_TYPE){
				Map<String,List> map = cfgConvert(areaIpRegionList,areaIpCfgList,1,entity,groupRelationList);
				groupRelationList=map.get("groupList");
				areaIpRegionList=map.get("dstList");
			}
		}
		//构造提交综合服务参数格式，一条配置提交一次综合服务
		if(isAudit==1){
			if(maatType==Constants.CALLBACK_TYPE){
				List<InlineIp> ipList=new ArrayList<>();
				for(IpPortCfg cfg :entity.getIpPortList()) {
					Integer ipsecProtocol=null;
					BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
					InlineIp ip=convertCallBackIp(cfg,null);
					if(ipsecProtocol!=null) {
						ip.setProtocol(ipsecProtocol);
					}
					ipList.add(ip);
				}
				//调用服务接口下发配置数据
				String json=gsonToJson(ipList);
				logger.info("IP配置下发配置参数："+json);
				//调用服务接口下发配置
				try {
					ToMaatResult result = ConfigServiceUtil.postCallbackCfg(json);
					if(result!=null){
						logger.info("IP配置配置下发响应信息："+result.getMsg());
					}
				} catch (Exception e) {
					logger.error("IP配置配置下发失败",e);
					throw e;
				}
			}else if(maatType==Constants.MAAT_TYPE){
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
				//限速需要发Droprate=0.001 ，暂不支持Bandwidth=200kbps
				if(entity.getAction().equals(Constants.RATELIMIT_ACTION)){
					if(entity.getUserRegion1().equals("0")){//丢包率
						entity.setUserRegion2(StringUtil.isEmpty(entity.getUserRegion2()) ? "":entity.getUserRegion2());
						maatCfg.setUserRegion(Constants.INTERCEPT_IP_RATELIMIT_DROPRATE_USER_REGION_KEY+"="+entity.getUserRegion2());
					}else if(entity.getUserRegion1().equals("1")){//带宽
						entity.setUserRegion3(StringUtil.isEmpty(entity.getUserRegion3()) ? "":entity.getUserRegion3());
						maatCfg.setUserRegion(Constants.INTERCEPT_IP_RATELIMIT_BANDWITH_USER_REGION_KEY+"="+entity.getUserRegion3());
					}
				}
				//调用服务接口下发配置数据
				String json=gsonToJson(maatBean);
				logger.info("ip配置下发配置参数："+json);
				//调用服务接口下发配置
				ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
				logger.info("ip配置下发响应信息："+result.getMsg());
			}else {
				throw new RuntimeException("<spring:message code=\"request_service_failed\"/>:"+props.getProperty("unknown_cfg_type"));
			}
		}else if(isAudit==3){
			if(maatType==Constants.CALLBACK_TYPE){
				List<InlineIp> ipList=new ArrayList<>();
				for(IpPortCfg cfg :entity.getIpPortList()) {
					Integer ipsecProtocol=null;
					BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
					InlineIp ip=convertCallBackIp(cfg,null);
					if(ipsecProtocol!=null) {
						ip.setProtocol(ipsecProtocol);
					}
					ipList.add(ip);
				}
				//调用服务接口下发配置数据
				String json=gsonToJson(ipList);
				logger.info("IP配置下发配置参数："+json);
				//调用服务接口下发配置
				try {
					ToMaatResult result = ConfigServiceUtil.put(json, 2);
					if(result!=null){
						logger.info("IP配置配置下发响应信息："+result.getMsg());
					}
				} catch (Exception e) {
					logger.error("IP配置配置下发失败",e);
					throw e;
				}
			}else if(maatType==Constants.MAAT_TYPE){
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
				logger.info("ip配置下发配置参数："+json);
				//调用服务接口下发配置
				ToMaatResult result = ConfigServiceUtil.put(json,1);
				logger.info("ip配置取消配置响应信息："+result.getMsg());
			}else {
				throw new RuntimeException("<spring:message code=\"request_service_failed\"/>:"+props.getProperty("unknown_cfg_type"));
			}
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
	public void auditAsnCfg(CfgIndexInfo entity,Integer isAudit){
		entity.setTableName(CfgIndexInfo.getTablename());
		entity.setIsAudit(isAudit);
		ipCfgDao.auditCfg(entity);
		ToMaatBean maatBean = new ToMaatBean();		
		MaatCfg maatCfg = new MaatCfg();
		List<MaatCfg> configCompileList = new ArrayList<>();
		List<GroupCfg> groupRelationList = new ArrayList<>();
		List<IpCfg> ipRegionList = new ArrayList<>();
		List<StringCfg> strRegionList = new ArrayList<>();
		List<NumBoundaryCfg> numRegionList = new ArrayList<>();
		List<DigestCfg> digestRegionList = new ArrayList<>();
		List<IpCfg> areaIpRegionList = new ArrayList<>();
		if(isAudit==Constants.AUDIT_YES) {
			ConfigGroupInfo group=configGroupInfoDao.getConfigGroupInfoByGroupId(Integer.parseInt(entity.getUserRegion4()));
			if(group.getIsIssued()==1) {//复用，只下编译跟分组
				maatCfg.initDefaultValue();
				BeanUtils.copyProperties(entity, maatCfg);
				maatCfg.setAction(entity.getAction());
				maatCfg.setAuditTime(entity.getAuditTime());
				maatCfg.setIpRegionList(ipRegionList);
				maatCfg.setStrRegionList(strRegionList);
				maatCfg.setNumRegionList(numRegionList);
				maatCfg.setDigestRegionList(digestRegionList);
				maatCfg.setGroupRelationList(groupRelationList);
				maatCfg.setAreaIpRegionList(areaIpRegionList);
				maatCfg.setIsValid(entity.getIsValid());
				//group
				GroupCfg groupCfg=new GroupCfg();
				groupCfg.setCompileId(entity.getCompileId());
				groupCfg.setGroupId(Integer.parseInt(entity.getUserRegion4()));
				groupCfg.setIsValid(Constants.VALID_YES);
				groupCfg.setAuditTime(entity.getAuditTime());
				groupRelationList.add(groupCfg);
				maatCfg.setGroupNum(groupRelationList.size());
				configCompileList.add(maatCfg);
				maatBean.setConfigCompileList(configCompileList);
				maatBean.setAuditTime(entity.getAuditTime());
				maatBean.setCreatorName(entity.getCurrentUser().getName());
				maatBean.setVersion(Constants.MAAT_VERSION);
				maatBean.setOpAction(Constants.INSERT_ACTION);
				if(group.getAsnId()!=null) {
					maatCfg.setUserRegion("ASN_ID="+group.getAsnId());
				}
				//调用服务接口下发配置数据
				String json=gsonToJson(maatBean);
				logger.info("ipaddr asn组复用配置下发配置参数："+json);
				//调用服务接口下发配置
				ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
				logger.info("ipaddr asn组复用配置下发响应信息："+result.getMsg());
			}else {//首次下发
				maatCfg.initDefaultValue();
				BeanUtils.copyProperties(entity, maatCfg);
				maatCfg.setAction(entity.getAction());
				maatCfg.setAuditTime(entity.getAuditTime());
				maatCfg.setStrRegionList(strRegionList);
				maatCfg.setNumRegionList(numRegionList);
				maatCfg.setDigestRegionList(digestRegionList);
				maatCfg.setGroupRelationList(groupRelationList);
				maatCfg.setAreaIpRegionList(areaIpRegionList);
				maatCfg.setIsValid(entity.getIsValid());
				if(group.getAsnId()!=null) {
					maatCfg.setUserRegion("ASN_ID="+group.getAsnId());
				}
				//group
				GroupCfg groupCfg=new GroupCfg();
				groupCfg.setCompileId(entity.getCompileId());
				groupCfg.setGroupId(Integer.parseInt(entity.getUserRegion4()));
				groupCfg.setIsValid(Constants.VALID_YES);
				groupCfg.setAuditTime(entity.getAuditTime());
				groupRelationList.add(groupCfg);
				maatCfg.setGroupNum(groupRelationList.size());
				//region
				//查询asn group id下所有的 ip
				AsnIpCfg asnIpCfg=new AsnIpCfg();
				asnIpCfg.setAsnIpGroup(Integer.parseInt(entity.getUserRegion4()));
				List<AsnIpCfg> allAsnIpCfgs=asnIpCfgDao.findAllList(asnIpCfg);
				ipRegionList.addAll(groupReuseCfgAddRemoveConvert(allAsnIpCfgs,Constants.VALID_YES,null));
				maatCfg.setIpRegionList(ipRegionList);
				configCompileList.add(maatCfg);
				maatBean.setConfigCompileList(configCompileList);
				maatBean.setAuditTime(entity.getAuditTime());
				maatBean.setCreatorName(entity.getCurrentUser().getName());
				maatBean.setVersion(Constants.MAAT_VERSION);
				maatBean.setOpAction(Constants.INSERT_ACTION);
				
				//调用服务接口下发配置数据
				String json=gsonToJson(maatBean);
				logger.info("ipaddr asn配置下发配置参数："+json);
				//调用服务接口下发配置
				ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
				logger.info("ipaddr asn配置下发响应信息："+result.getMsg());
				ConfigGroupInfo info=new ConfigGroupInfo();
				BeanUtils.copyProperties(group, info);
				info.setIsIssued(1);
				//info.setGroupId(Integer.parseInt(entity.getUserRegion4()));
				configGroupInfoDao.updateConfigGroupInfobyGroupId(info);
				AsnCacheUtils.put(info.getAsnId(), info);
				AsnIpCfg cfg=new AsnIpCfg();
				cfg.setIsValid(Constants.VALID_YES);
				cfg.setAsnIpGroup(Integer.parseInt(entity.getUserRegion4()));
				asnIpCfgDao.updateIssued(cfg);
			}
		}else if(isAudit==Constants.AUDIT_NOT_YES) {//取消审核通过
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
			logger.info("ipaddr asn取消下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json,1);
			logger.info("ipaddr asn取消配置响应信息："+result.getMsg());
		}else {
			throw new RuntimeException("unknown isAudit value "+isAudit);
		}
		
	}
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void deleteIp(String ids,String compileIds,int functionId){
		if(StringUtils.isNotBlank(compileIds)){
			for(String compileId:compileIds.split(",")){//强转数字，防止注入
				Integer.parseInt(compileId);
			}
			ipCfgDao.deleteByCompileIds(new BaseIpCfg().getCurrentUser().getId(),AreaIpCfg.getTablename(),compileIds);
		}
		List<BaseIpCfg> ipCfgs=new ArrayList<BaseIpCfg>();
		Date date =new Date();
		if(StringUtils.isNotBlank(ids)){
			for(String idStr:ids.split(",")){
				if(StringUtils.isNotBlank(idStr)){
					BaseIpCfg cfg=new BaseIpCfg();
					cfg.setCfgId(Long.parseLong(idStr));
					cfg.setTableName(IpPortCfg.getTablename());
					cfg.setEditorId(cfg.getCurrentUser().getId());
					cfg.setEditTime(date);
					cfg.setIsValid(Constants.VALID_DEL);
					ipCfgs.add(cfg);
				}
			}
		}
		this.deleteBatch(ipCfgs, IpCfgDao.class);
	} 
	/**
	 * 
	 * deleteIpCfg(删除IP类配置)
	 * (继承BaseIpCfg这个类方可使用)
	 * @param baseIpCfg
	 * @return 
	 *int
	 * @exception 
	 * @since  1.0.0
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void deleteIpCfg(List<BaseIpCfg> baseIpCfg, List<AreaIpCfg> areaCfg){
		List<BaseIpCfg> cfgs=new ArrayList<>();
		if(areaCfg!=null&&areaCfg.size()>0){
			cfgs.addAll(areaCfg);
			this.deleteBatch(cfgs,IpCfgDao.class);
		}
		if(baseIpCfg!=null&&baseIpCfg.size()>0){
			this.deleteBatch(baseIpCfg, IpCfgDao.class);
		}
		
	} 
	/**
	 * 
	 * getIpCfg(根据IP与类名获取IP配置)
	 * (继承BaseIpCfg这个类方可使用)
	 * @param clazz
	 * @param id
	 * @return 
	 *BaseIpCfg
	 * @exception 
	 * @since  1.0.0
	 */
	public BaseIpCfg getIpCfgById(BaseIpCfg baseIpCfg){
		return ipCfgDao.getById(baseIpCfg.getTableName(), baseIpCfg.getCfgId());
	}
	public CfgIndexInfo getIpPortCfg(Long cfgId,Integer compileId){
		CfgIndexInfo entity = ipCfgDao.getCfgIndexInfo(cfgId,compileId);
		List<IpPortCfg> ipPortList = ipCfgDao.getIpPortList(entity);
		List<NtcSubscribeIdCfg> subscribeIdList = stringCfgDao.findSubscribeIdCfgListByCfgIndexInfo(entity);
		entity.setIpPortList(ipPortList);
		entity.setNtcSubscribeIdCfgList(subscribeIdList);
		if(StringUtils.isNotBlank(entity.getUserRegion4())) {
			ConfigGroupInfo info=configGroupInfoDao.getConfigGroupInfoByGroupId(Integer.parseInt(entity.getUserRegion4().trim()));
			entity.setAsnIpGroupName(info.getGroupName());
		}
		return entity;
	}
	public CfgIndexInfo exportIpInfo(CfgIndexInfo entity){
		List<IpPortCfg> ipPortList = ipCfgDao.getIpPortList(entity);
		entity.setIpPortList(ipPortList);
		if(StringUtils.isNotBlank(entity.getUserRegion4())) {
			ConfigGroupInfo info=configGroupInfoDao.getConfigGroupInfoByGroupId(Integer.parseInt(entity.getUserRegion4().trim()));
			entity.setAsnIpGroupName(info.getGroupName());
		}
		return entity;
	}
	public BaseIpCfg getIpCfgById(String tableName,long id){
		return ipCfgDao.getById(tableName, id);
	}
	public Integer getIsValid(BaseIpCfg baseIpCfg){
		return ipCfgDao.getIsValid(baseIpCfg);
	}
	public Integer getIsValid(String tableName, long id){
		return ipCfgDao.getIsValid(tableName,id);
	}
	public Integer getIsAudit(BaseIpCfg baseIpCfg){
		return ipCfgDao.getIsAudit(baseIpCfg);
	}
	public Integer getIsAudit(String tableName, long id){
		return ipCfgDao.getIsAudit(tableName,id);
	}
	public List<AreaIpCfg> getAreaCfgByCompileId(/*int functionId,*/int compileId){
		return areaIpCfgDao.getByCompileId(compileId);
	}
	public List<BaseIpCfg> getListByComileId(String tableName,int functionId,String ids){
		return ipCfgDao.getListByComileId(tableName,functionId,ids);
	}
	public List<BaseIpCfg> getListByCfgId(String tableName,int functionId,String ids){
		return ipCfgDao.getListByCfgId(tableName,functionId,ids);
	}
	public Integer getCompileId(){
		//调用服务接口获取compileId
		Integer compileId = 0;
		try {
			List<Integer> compileIds = ConfigServiceUtil.getId(1,1);
			if(!StringUtil.isEmpty(compileIds)){
				compileId = compileIds.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("获取编译ID出错");
			throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+e.getMessage());
		}
		return compileId;
	}
	/**
	 * getListByCfgIdWithName(这里用一句话描述这个方法的作用)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param tablename
	 * @param functionId
	 * @param ids
	 * @return 
	 *List<BaseIpCfg>
	 * @exception 
	 * @since  1.0.0
	*/
	public List<BaseIpCfg> getListByCfgIdWithName(String tablename, Integer functionId, String ids) {
		// TODO Auto-generated method stub
		return ipCfgDao.getListByCfgIdWithName(tablename,functionId,ids);
	}
	/**
	 * 获取国际化配置文件
	 * @return
	 */
	public Properties getMsgProp(){
		Properties msgProp = new Properties();
		try {
			String language = LocaleContextHolder.getLocale().getLanguage();
			if(language.equals("zh_cn")||language.equals("zh")){
				msgProp.load(Configurations.class.getResourceAsStream("/messages/message_zh_CN.properties"));
			}else if(language.equals("ru")){
				msgProp.load(Configurations.class.getResourceAsStream("/messages/message_ru.properties"));
			}else{
				msgProp.load(Configurations.class.getResourceAsStream("/messages/message_en.properties"));
			}
			
		} catch (Exception e) {
			msgProp = null;
			logger.error("未知i18n消息配置文件,请确定文件是否存在!",e);
		}
		return msgProp;
	}
	public Page<CfgIndexInfo> getIpCfgList(Page<CfgIndexInfo> page, CfgIndexInfo entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"a"));
		entity.setPage(page);
		List<CfgIndexInfo> list = ipCfgDao.getIpCfgList(entity);
		page.setList(list);
		return page;
	}
	
}
