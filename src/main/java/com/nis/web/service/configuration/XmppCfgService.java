package com.nis.web.service.configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.configuration.AreaIpCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.ComplexkeywordCfg;
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
import com.nis.web.dao.configuration.XmppCfgDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.CrudService;

/**
 * xmpp相关配置事务类
 * @author dell
 *
 */
@Service
public class XmppCfgService extends CrudService<XmppCfgDao,CfgIndexInfo> {
	@Autowired
	protected XmppCfgDao xmppCfgDao;
	@Autowired
	protected AreaIpCfgDao areaIpCfgDao;
	@Autowired
	protected StringCfgDao stringCfgDao;
	
	public CfgIndexInfo getXmppCfg(Long cfgId){
		CfgIndexInfo entity = xmppCfgDao.getCfgIndexInfo(cfgId);
		List<IpPortCfg> ipPortList = xmppCfgDao.getIpPortList(entity);
		//设置关键字
		if(entity.getCompileId()!=null){
		  NtcSubscribeIdCfg ntcSubscribeIdCfg = new NtcSubscribeIdCfg();
		  ntcSubscribeIdCfg.setCompileId(entity.getCompileId());
		  List<NtcSubscribeIdCfg> ntcList = stringCfgDao.findSubscribeIdCfgList(ntcSubscribeIdCfg);
		  entity.setNtcSubscribeIdCfgList(ntcList);
		}
		entity.setIpPortList(ipPortList);
		return entity;
	}
	public Page<CfgIndexInfo> getXmppList(Page<CfgIndexInfo> page, CfgIndexInfo entity){
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"a"));
		entity.setPage(page);
		List<CfgIndexInfo> list = xmppCfgDao.getXmppList(entity);
		page.setList(list);
		return page;
	}
	public void saveXmppCfg(CfgIndexInfo entity){
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
						xmppCfgDao.saveCfgIndex(entity);
						if(entity.getIpPortList()!=null){
							for(IpPortCfg cfg:entity.getIpPortList()){
								BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
								xmppCfgDao.saveIpPortCfg(cfg);
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
					xmppCfgDao.updateCfgIndex(entity);
					//无效子配置后，再新增子配置
					xmppCfgDao.deleteXmppIpCfg(entity);
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
							xmppCfgDao.saveIpPortCfg(cfg);
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
	public void updateXmppCfgValid(Integer isValid,String ids,Integer functionId){
		String[] idArray = ids.split(",");
		for(String id :idArray){
			CfgIndexInfo entity = new CfgIndexInfo();
			entity.setCfgId(Long.parseLong(id));
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			entity.setTableName(CfgIndexInfo.getTablename());
			entity.setFunctionId(functionId);
			xmppCfgDao.updateCfgValid(entity);
			//查询子配置
			entity = this.getXmppCfg(Long.parseLong(id));
			if(entity.getIpPortList()!=null && entity.getIpPortList().size()>0){
				IpPortCfg cfg = new IpPortCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(IpPortCfg.getTablename());
				xmppCfgDao.updateCfgValid(cfg);
			}
			if(entity.getNtcSubscribeIdCfgList()!=null && entity.getNtcSubscribeIdCfgList().size()>0)
			{
				NtcSubscribeIdCfg cfg = new NtcSubscribeIdCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(NtcSubscribeIdCfg.getTablename());
				xmppCfgDao.updateCfgValid(cfg);
			}
//			if(entity.getHttpUrlList()!=null && entity.getHttpUrlList().size()>0){
//				HttpUrlCfg cfg = new HttpUrlCfg();
//				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
//				cfg.setTableName(HttpUrlCfg.getTablename());
//				xmppCfgDao.updateCfgValid(cfg);
//			}
			//保存区域IP信息
			if(entity.getAreaCfg()!=null && entity.getAreaCfg().size()>0){
				AreaIpCfg cfg = new AreaIpCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(AreaIpCfg.getTablename());
				xmppCfgDao.updateCfgValid(cfg);
			}
			
		}
		
	}
	public void auditXmppCfg(CfgIndexInfo entity,Integer isAudit) throws MaatConvertException{
		//修改数据库审核状态信息
		entity.setTableName(CfgIndexInfo.getTablename());
		xmppCfgDao.auditCfg(entity);
		
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
		entity = this.getXmppCfg(entity.getCfgId());
		if(entity.getIpPortList()!=null && entity.getIpPortList().size()>0){
			IpPortCfg cfg = new IpPortCfg();
			BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
			cfg.setTableName(IpPortCfg.getTablename());
			xmppCfgDao.auditCfg(cfg);
			if(isAudit==1){
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
			xmppCfgDao.auditCfg(cfg);
			if(isAudit==1){
				Map<String,List> map = cfgConvert(strRegionList,entity.getNtcSubscribeIdCfgList(),2,entity,groupRelationList);
				groupRelationList=map.get("groupList");
				strRegionList=map.get("dstList");
			}
		}
//		if(entity.getHttpUrlList()!=null && entity.getHttpUrlList().size()>0){
//			HttpUrlCfg cfg = new HttpUrlCfg();
//			BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
//			cfg.setTableName(HttpUrlCfg.getTablename());
//			xmppCfgDao.auditCfg(cfg);
//			if(isAudit==1){
//				Map<String,List> map = cfgConvert(strRegionList,entity.getHttpUrlList(),2,entity,groupRelationList);
//				groupRelationList=map.get("groupList");
//				strRegionList=map.get("dstList");
//			}
//		}
		//保存区域IP信息
		List<AreaIpCfg> areaIpCfgList=areaIpCfgDao.getByCompileId(entity.getCompileId());
		if(!StringUtil.isEmpty(areaIpCfgList)){
			AreaIpCfg cfg = new AreaIpCfg();
			BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
			cfg.setTableName(AreaIpCfg.getTablename());
			xmppCfgDao.auditCfg(cfg);
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
			logger.info("xmpp配置下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
			logger.info("xmpp配置下发响应信息："+result.getMsg());

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
			logger.info("xmpp配置下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json,1);
			logger.info("xmpp配置取消配置响应信息："+result.getMsg());
		}
	}
	
}
