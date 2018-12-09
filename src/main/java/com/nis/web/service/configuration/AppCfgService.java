package com.nis.web.service.configuration;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.nis.domain.Page;
import com.nis.domain.basics.AsnIpCfg;
import com.nis.domain.basics.Varibles;
import com.nis.domain.configuration.AppByteCfg;
import com.nis.domain.configuration.AppDomainCfg;
import com.nis.domain.configuration.AppHeaderCfg;
import com.nis.domain.configuration.AppHttpCfg;
import com.nis.domain.configuration.AppIpCfg;
import com.nis.domain.configuration.AppPolicyCfg;
import com.nis.domain.configuration.AppSslCertCfg;
import com.nis.domain.configuration.AppTcpCfg;
import com.nis.domain.configuration.AppTopicDomainCfg;
import com.nis.domain.configuration.AreaIpCfg;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.ComplexkeywordCfg;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.configuration.NtcSubscribeIdCfg;
import com.nis.domain.configuration.WebsiteDomainTopic;
import com.nis.domain.maat.GroupReuseAddBean;
import com.nis.domain.maat.GroupReuseCfg;
import com.nis.domain.maat.MaatCfg;
import com.nis.domain.maat.MaatCfg.DigestCfg;
import com.nis.domain.maat.MaatCfg.GroupCfg;
import com.nis.domain.maat.MaatCfg.IpCfg;
import com.nis.domain.maat.MaatCfg.NumBoundaryCfg;
import com.nis.domain.maat.MaatCfg.StringCfg;
import com.nis.domain.maat.ToMaatBean;
import com.nis.domain.maat.ToMaatResult;
import com.nis.domain.specific.ConfigGroupInfo;
import com.nis.domain.specific.SpecificServiceCfg;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.Constants;
import com.nis.util.DictUtils;
import com.nis.util.StringUtil;
import com.nis.util.StringUtils;
import com.nis.web.dao.basics.AsnIpCfgDao;
import com.nis.web.dao.configuration.AppCfgDao;
import com.nis.web.dao.configuration.AreaIpCfgDao;
import com.nis.web.dao.configuration.IpCfgDao;
import com.nis.web.dao.configuration.StringCfgDao;
import com.nis.web.dao.specific.ConfigGroupInfoDao;
import com.nis.web.dao.specific.SpecificServiceCfgDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.BaseService;
import com.nis.web.service.SpringContextHolder;

/**
 * 特定协议相关配置事务类
 * 
 * @author dell
 *
 */
@Service
public class AppCfgService extends BaseService {
	@Autowired
	protected AppCfgDao appCfgDao;
	@Autowired
	protected AreaIpCfgDao areaIpCfgDao;
	@Autowired
	protected StringCfgDao stringcfgDao;
	@Autowired
	protected IpCfgDao ipCfgDao;
	@Autowired
	protected SpecificServiceCfgDao specificServiceCfgDao;
	@Autowired
	protected ConfigGroupInfoDao configGroupInfoDao;
	@Autowired
	protected AsnIpCfgDao asnIpCfgDao;

	public Page<AppPolicyCfg> findAppPolicyList(Page<AppPolicyCfg> page, AppPolicyCfg entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(), "r"));
		entity.setPage(page);
		List<AppPolicyCfg> list = appCfgDao.findAppPolicyList(entity);
		page.setList(list);
		return page;
	}

	public Page<AppIpCfg> findAppIpList(Page<AppIpCfg> page, AppIpCfg entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(), "r"));
		entity.setPage(page);
		List<AppIpCfg> list = appCfgDao.findAppIpList(entity);
		page.setList(list);
		return page;
	}

	public Page<AppHttpCfg> findAppHttpList(Page<AppHttpCfg> page, AppHttpCfg entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(), "r"));
		entity.setPage(page);
		List<AppHttpCfg> list = appCfgDao.findAppHttpList(entity);
		page.setList(list);
		return page;
	}

	public Page<AppDomainCfg> findAppDomainList(Page<AppDomainCfg> page, AppDomainCfg entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(), "r"));
		entity.setPage(page);
		List<AppDomainCfg> list = appCfgDao.findAppDomainList(entity);
		page.setList(list);
		return page;
	}
	
	public AppPolicyCfg exportIpInfo(AppPolicyCfg policy){
		List<IpPortCfg> ipPortList = appCfgDao.getAppPolicyIpList(policy);
		policy.setIpPortList(ipPortList);
		NtcSubscribeIdCfg ntcSubscribeIdCfg = new NtcSubscribeIdCfg();
		ntcSubscribeIdCfg.setCompileId(policy.getCompileId());
		List<NtcSubscribeIdCfg> ntcList = stringcfgDao.findSubscribeIdCfgList(ntcSubscribeIdCfg);
		policy.setNtcSubscribeIdCfgList(ntcList);
		return policy;
	}

	// 主题网站列表
	public Page<AppTopicDomainCfg> findAppTopicDomainList(Page<AppTopicDomainCfg> page, AppTopicDomainCfg entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(), "r"));
		entity.setPage(page);
		List<AppTopicDomainCfg> list = appCfgDao.findAppTopicDomainList(entity);
		page.setList(list);
		return page;
	}

	public Page<AppByteCfg> findAppByteList(Page<AppByteCfg> page, AppByteCfg entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(), "r"));
		entity.setPage(page);
		List<AppByteCfg> list = appCfgDao.findAppByteList(entity);
		page.setList(list);
		return page;
	}

	public Page<AppSslCertCfg> findAppSslList(Page<AppSslCertCfg> page, AppSslCertCfg entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(), "r"));
		entity.setPage(page);
		List<AppSslCertCfg> list = appCfgDao.findAppSslList(entity);
		page.setList(list);
		return page;
	}

	public Page<AppTcpCfg> findAppTcpList(Page<AppTcpCfg> page, AppTcpCfg entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(), "r"));
		entity.setPage(page);
		List<AppTcpCfg> list = appCfgDao.findAppTcpList(entity);
		page.setList(list);
		return page;
	}

	public AppTcpCfg getAppTcpCfg(Long cfgId) {
		return appCfgDao.getAppTcpCfg(cfgId);
	}

	public AppPolicyCfg getAppPolicyCfg(Long cfgId,Integer compileId) {
		AppPolicyCfg policy = appCfgDao.getAppPolicyCfg(cfgId,compileId);
		List<IpPortCfg> ipPortList = appCfgDao.getAppPolicyIpList(policy);
		// 查询关键字
		if (policy.getCompileId() != null) {
			NtcSubscribeIdCfg ntcSubscribeIdCfg = new NtcSubscribeIdCfg();
			ntcSubscribeIdCfg.setCompileId(policy.getCompileId());
			List<NtcSubscribeIdCfg> ntcList = stringcfgDao.findSubscribeIdCfgList(ntcSubscribeIdCfg);
			policy.setNtcSubscribeIdCfgList(ntcList);
		}

		policy.setIpPortList(ipPortList);
		return policy;
	}

	public AppIpCfg getAppIpCfg(Long cfgId) {
		return appCfgDao.getAppIpCfg(cfgId);
	}

	public AppHttpCfg getAppHttpCfg(Long cfgId) {
		return appCfgDao.getAppHttpCfg(cfgId);
	}

	public AppDomainCfg getAppDomainCfg(Long cfgId) {
		return appCfgDao.getAppDomainCfg(cfgId);
	}

	// 主题网站form
	public AppTopicDomainCfg getAppTopicDomainCfg(Long cfgId) {
		return appCfgDao.getAppTopicDomainCfg(cfgId);
	}

	public AppByteCfg getAppByteCfg(Long cfgId) {
		return appCfgDao.getAppByteCfg(cfgId);
	}

	public AppSslCertCfg getAppSslCfg(Long cfgId) {
		return appCfgDao.getAppSslCfg(cfgId);
	}

	@Transactional(readOnly = false, rollbackFor = RuntimeException.class)
	public void saveOrUpdateAppPolicyCfg(AppPolicyCfg entity) throws Exception {
		// 设置区域运营商信息
		setAreaEffectiveIds(entity);

		if (entity.getCfgId() == null) {
			Integer compileId = 0;
			try {
				List<Integer> idList = ConfigServiceUtil.getId(1, 1);
				if (idList != null && idList.size() > 0) {
					compileId = idList.get(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("获取编译ID出错");
				throw new MaatConvertException(e.getMessage());
			}
			entity.setCompileId(compileId);
			entity.setCreateTime(new Date());
			entity.setCreatorId(entity.getCurrentUser().getId());
			entity.setIsValid(0);
			entity.setIsAudit(0);
			// 设置SubscribeID域配置参数
//			if (entity != null && entity.getNtcSubscribeIdCfgList() != null) {
//				for (NtcSubscribeIdCfg cfg : entity.getNtcSubscribeIdCfgList()) {
//					entity.setExprType(cfg.getExprType());
//					entity.setIsHexbin(cfg.getIsHexbin());
//					entity.setMatchMethod(cfg.getMatchMethod());
//					entity.setCfgKeywords(cfg.getCfgKeywords());
//				}
//			}
//			entity.initDefaultValue();
			appCfgDao.insertAppPolicyCfg(entity);
			// 保存策略IP配置
			if (entity != null && entity.getIpPortList() != null) {
				for (IpPortCfg cfg : entity.getIpPortList()) {
					BeanUtils.copyProperties(entity, cfg, new String[] { "cfgRegionCode", "cfgType" });
					appCfgDao.insertAppPolicyIpCfg(cfg);
				}
			}
			// 保存关键字配置
			if (entity != null && entity.getNtcSubscribeIdCfgList() != null) {
				for (NtcSubscribeIdCfg cfg : entity.getNtcSubscribeIdCfgList()) {
					if (StringUtils.isNotBlank(cfg.getCfgKeywords())) {
						entity.setCfgKeywords(cfg.getCfgKeywords());
						BeanUtils.copyProperties(entity, cfg, new String[] { "cfgRegionCode", "cfgType", "exprType", "matchMethod", "isHexbin" });
						stringcfgDao.saveSubscribeIdCfg(cfg);
					}
				}
			}
			// 保存区域IP信息
			if (entity.getAreaCfg() != null) {
				for (AreaIpCfg cfg : entity.getAreaCfg()) {
					cfg.initDefaultValue();
					BeanUtils.copyProperties(entity, cfg, new String[] { "cfgDesc", "cfgRegionCode", "cfgType" });
					areaIpCfgDao.saveAreaIpCfg(cfg);
				}
			}

		} else {
			entity.setEditorId(entity.getCurrentUser().getId());
			entity.setEditTime(new Date());
			entity.setIsValid(0);
			entity.setIsAudit(0);
			// 设置SubscribeID域配置参数
//			if (entity != null && entity.getNtcSubscribeIdCfgList() != null) {
//				for (NtcSubscribeIdCfg cfg : entity.getNtcSubscribeIdCfgList()) {
//					entity.setExprType(cfg.getExprType());
//					entity.setIsHexbin(cfg.getIsHexbin());
//					entity.setMatchMethod(cfg.getMatchMethod());
//				}
//			}
			appCfgDao.updateAppPolicyCfg(entity);
			
			// 先删后加 各域配置
			appCfgDao.deleteAppPolicyIpCfg(entity);
			appCfgDao.deleteSubscribeIdCfg(entity);
			
			// 删除旧的区域IP，新增新的区域IP
			AreaIpCfg area = new AreaIpCfg();
			area.setCompileId(entity.getCompileId());
			area.setFunctionId(entity.getFunctionId());
			areaIpCfgDao.deleteAreaIpCfg(area);

			entity.setCreateTime(new Date());
			entity.setCreatorId(entity.getCurrentUser().getId());
			// 保存策略IP配置
			if (entity.getIpPortList() != null) {
				for (IpPortCfg cfg : entity.getIpPortList()) {
					BeanUtils.copyProperties(entity, cfg, new String[] { "cfgRegionCode", "cfgType" });
					appCfgDao.insertAppPolicyIpCfg(cfg);
				}
			}
			if (entity != null && entity.getNtcSubscribeIdCfgList() != null) {
				for (NtcSubscribeIdCfg cfg : entity.getNtcSubscribeIdCfgList()) {
					if (StringUtils.isNotBlank(cfg.getCfgKeywords())) {
						entity.setCfgKeywords(cfg.getCfgKeywords());
						BeanUtils.copyProperties(entity, cfg,
								new String[] { "cfgRegionCode", "cfgType", "exprType", "matchMethod", "isHexbin" });
						stringcfgDao.saveSubscribeIdCfg(cfg);
					}
				}
			}

			// 保存区域IP信息
			if (entity.getAreaCfg() != null) {
				for (AreaIpCfg cfg : entity.getAreaCfg()) {
					cfg.initDefaultValue();
					BeanUtils.copyProperties(entity, cfg, new String[] { "cfgDesc", "cfgRegionCode", "cfgType" });
					areaIpCfgDao.saveAreaIpCfg(cfg);
				}
			}
		}
	}
	
	@Transactional(readOnly = false, rollbackFor = RuntimeException.class)
	public void saveOrUpdateAppIpCfg(AppIpCfg entity) throws Exception {
		// 设置区域运营商信息
		setAreaEffectiveIds(entity);

		if (entity.getCfgId() == null) {
			
			SpecificServiceCfg specificServiceCfg=specificServiceCfgDao.getBySpecServiceId(entity.getSpecServiceId());
			ConfigGroupInfo configGroupInfo=configGroupInfoDao.getConfigGroupInfoByGroupId(specificServiceCfg.getGroupId());
			if(configGroupInfo==null) {//一般特定服务的组号是新增之后就有的，不会进入这一步。
				Integer groupId = ConfigServiceUtil.getId(2, 1).get(0);
				Integer compileId=ConfigServiceUtil.getId(1, 1).get(0);
				specificServiceCfg.setGroupId(groupId);
				configGroupInfo = new ConfigGroupInfo();
				configGroupInfo.setGroupId(specificServiceCfg.getGroupId());
				configGroupInfo.setGroupName(specificServiceCfg.getSpecServiceName());
				configGroupInfo.setIsIssued(0);
				configGroupInfo.setGroupType(1);
				configGroupInfo.setCompileId(compileId);
				configGroupInfoDao.insertConfigGroupInfo(configGroupInfo);
				//更新group_id
				specificServiceCfg.setOpTime(new Date());
				specificServiceCfgDao.update(specificServiceCfg);
			}
			Integer compileId = configGroupInfo.getCompileId();
			/*if(configGroupInfo.getIsIssued().intValue()==0) {//没有下发过
			//查询AppIpCfg对应 的未下发IP的编译ID
				List<AppIpCfg> beforeCfg=appCfgDao.getAppIpCfgBySpecServiceId(entity.getSpecServiceId());
				if(beforeCfg==null||beforeCfg.size()==0) {//第一条，获取编译id
					try {
						List<Integer> idList = ConfigServiceUtil.getId(1, 1);
						if (idList != null && idList.size() > 0) {
							compileId = idList.get(0);
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.info("获取编译ID出错");
						throw new MaatConvertException(e.getMessage());
					}
				}else {
					compileId=beforeCfg.get(0).getCompileId();
				}
			}else if(configGroupInfo.getIsIssued().intValue()==1){//下发过了
//				List<AppIpCfg> beforeCfg=appCfgDao.getAppIpCfgBySpecServiceId(entity.getSpecServiceId());
//				if(beforeCfg==null||beforeCfg.size()==0) {
//					throw new RuntimeException("Can't get compileId, there is not app ip in an issued group");
//				}
				compileId=configGroupInfo.getCompileId();
			}else {
				throw new RuntimeException("Unknown configGroupInfo isIssued value");
			}*/
			entity.setUserRegion1(String.valueOf(ConfigServiceUtil.getId(3, 1).get(0)));
			entity.setCompileId(compileId);
			entity.setCreateTime(new Date());
			entity.setCreatorId(entity.getCurrentUser().getId());
			entity.setIsValid(0);
			entity.setIsAudit(0);
			appCfgDao.insertAppIpCfg(entity);
			//app ip的编译ID存于configGroupInfo中
			if((configGroupInfo.getCompileId()==null)||(compileId.intValue()!=configGroupInfo.getCompileId().intValue())) {
				configGroupInfo.setCompileId(entity.getCompileId());
				configGroupInfoDao.updateConfigGroupInfobyGroupId(configGroupInfo);
			}
			// 保存区域IP信息
			if (entity.getAreaCfg() != null) {
				for (AreaIpCfg cfg : entity.getAreaCfg()) {
					cfg.initDefaultValue();
					BeanUtils.copyProperties(entity, cfg,
							new String[] { "cfgRegionCode", "cfgType", "ipType", "isAreaEffective",
									"areaEffectiveIds", "protocol", "portPattern", "srcPort", "destPort",
									"direction", "destIpAddress", "srcIpAddress", "ipPattern" });
					areaIpCfgDao.saveAreaIpCfg(cfg);
				}
			}
		} else {
			entity.setEditorId(entity.getCurrentUser().getId());
			entity.setEditTime(new Date());
			entity.setIsValid(0);
			entity.setIsAudit(0);
			appCfgDao.updateAppIpCfg(entity);
			// 删除旧的区域IP，新增新的区域IP
			AreaIpCfg area = new AreaIpCfg();
			area.setCompileId(entity.getCompileId());
			area.setFunctionId(entity.getFunctionId());
			areaIpCfgDao.deleteAreaIpCfg(area);
			entity.setCreateTime(new Date());
			entity.setCreatorId(entity.getCurrentUser().getId());
			// 保存区域IP信息
			if (entity.getAreaCfg() != null) {
				for (AreaIpCfg cfg : entity.getAreaCfg()) {
					cfg.initDefaultValue();
					BeanUtils.copyProperties(entity, cfg,
							new String[] { "cfgRegionCode", "cfgType", "ipType", "isAreaEffective", "areaEffectiveIds",
									"protocol", "portPattern", "srcPort", "destPort", "direction", "destIpAddress",
									"srcIpAddress", "ipPattern" });
					areaIpCfgDao.saveAreaIpCfg(cfg);
				}
			}
		}
	}
	public void saveAppHttpCfg(List<ComplexkeywordCfg> cfgs) {
		for(ComplexkeywordCfg cfg:cfgs) {
			AppHttpCfg appHttpCfg=new AppHttpCfg();
			BeanUtils.copyProperties(cfg, appHttpCfg);
			appCfgDao.insertAppHttpCfg(appHttpCfg);
		}
		
	}
	@Transactional(readOnly = false, rollbackFor = RuntimeException.class)
	public void saveOrUpdateAppHttpCfg(AppHttpCfg entity) throws Exception {
		// 设置区域运营商信息
		setAreaEffectiveIds(entity);
		if (entity.getCfgId() == null) {
			Integer compileId = 0;
			try {
				List<Integer> idList = ConfigServiceUtil.getId(1, 1);
				if (idList != null && idList.size() > 0) {
					compileId = idList.get(0);
				}
				entity.setCompileId(compileId);
				entity.setCreateTime(new Date());
				entity.setCreatorId(entity.getCurrentUser().getId());
				entity.setIsValid(0);
				entity.setIsAudit(0);
				appCfgDao.insertAppHttpCfg(entity);
				// 保存区域IP信息
				if (entity.getAreaCfg() != null) {
					for (AreaIpCfg cfg : entity.getAreaCfg()) {
						cfg.initDefaultValue();
						BeanUtils.copyProperties(entity, cfg, new String[] { "cfgRegionCode", "cfgType" });

						areaIpCfgDao.saveAreaIpCfg(cfg);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				logger.info("获取编译ID出错");
				throw new MaatConvertException(e.getMessage());
			}
		} else {
			entity.setEditorId(entity.getCurrentUser().getId());
			entity.setEditTime(new Date());
			entity.setIsValid(0);
			entity.setIsAudit(0);
			appCfgDao.updateAppHttpCfg(entity);

			// 删除旧的区域IP，新增新的区域IP
			AreaIpCfg area = new AreaIpCfg();
			area.setCompileId(entity.getCompileId());
			area.setFunctionId(entity.getFunctionId());
			areaIpCfgDao.deleteAreaIpCfg(area);
			entity.setCreateTime(new Date());
			entity.setCreatorId(entity.getCurrentUser().getId());
			// 保存区域IP信息
			if (entity.getAreaCfg() != null) {
				for (AreaIpCfg cfg : entity.getAreaCfg()) {
					cfg.initDefaultValue();
					BeanUtils.copyProperties(entity, cfg, new String[] { "cfgDesc", "cfgRegionCode", "cfgType" });
					areaIpCfgDao.saveAreaIpCfg(cfg);
				}
			}

		}

	}
	@Transactional(readOnly = false, rollbackFor = RuntimeException.class)
	public void saveAppDomainCfg(List<BaseStringCfg<?>> cfgs){
		for(BaseStringCfg cfg:cfgs) {
			AppDomainCfg entity=new AppDomainCfg();
			BeanUtils.copyProperties(cfg, entity);
			appCfgDao.insertAppDomainCfg(entity);
		}
		
	}
	@Transactional(readOnly = false, rollbackFor = RuntimeException.class)
	public void saveOrUpdateAppDomainCfg(AppDomainCfg entity) throws Exception {
		// 设置区域运营商信息
		setAreaEffectiveIds(entity);

		if (entity.getCfgId() == null) {
			Integer compileId = 0;
			try {
				List<Integer> idList = ConfigServiceUtil.getId(1, 1);
				if (idList != null && idList.size() > 0) {
					compileId = idList.get(0);
				}
				entity.setCompileId(compileId);
				entity.setCreateTime(new Date());
				entity.setCreatorId(entity.getCurrentUser().getId());
				entity.setIsValid(0);
				entity.setIsAudit(0);
				appCfgDao.insertAppDomainCfg(entity);
				// 保存区域IP信息
				if (entity.getAreaCfg() != null) {
					for (AreaIpCfg cfg : entity.getAreaCfg()) {
						cfg.initDefaultValue();
						BeanUtils.copyProperties(entity, cfg, new String[] { "cfgDesc", "cfgRegionCode", "cfgType" });
						areaIpCfgDao.saveAreaIpCfg(cfg);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("获取编译ID出错");
				throw new MaatConvertException(e.getMessage());
			}
		} else {
			entity.setEditorId(entity.getCurrentUser().getId());
			entity.setEditTime(new Date());
			entity.setIsValid(0);
			entity.setIsAudit(0);
			appCfgDao.updateAppDomainCfg(entity);
			// 删除旧的区域IP，新增新的区域IP
			AreaIpCfg area = new AreaIpCfg();
			area.setCompileId(entity.getCompileId());
			area.setFunctionId(entity.getFunctionId());
			areaIpCfgDao.deleteAreaIpCfg(area);
			entity.setCreateTime(new Date());
			entity.setCreatorId(entity.getCurrentUser().getId());
			// 保存区域IP信息
			if (entity.getAreaCfg() != null) {
				for (AreaIpCfg cfg : entity.getAreaCfg()) {
					cfg.initDefaultValue();
					BeanUtils.copyProperties(entity, cfg, new String[] { "cfgDesc", "cfgRegionCode", "cfgType" });
					areaIpCfgDao.saveAreaIpCfg(cfg);
				}
			}
		}
	}

	// 主题网站新增 修改
	public void saveOrUpdateAppTopicDomainCfg(AppTopicDomainCfg entity) throws Exception {
		// 设置区域运营商信息
		setAreaEffectiveIds(entity);

		if (entity.getCfgId() == null) {
			Integer compileId = 0;
			try {
				List<Integer> idList = ConfigServiceUtil.getId(1, 1);
				if (idList != null && idList.size() > 0) {
					compileId = idList.get(0);
				}
				entity.setCompileId(compileId);
				entity.setCreateTime(new Date());
				entity.setCreatorId(entity.getCurrentUser().getId());
				entity.setIsValid(0);
				entity.setIsAudit(0);
				appCfgDao.insertAppTopicDomainCfg(entity);
				// 保存区域IP信息
				if (entity.getAreaCfg() != null) {
					for (AreaIpCfg cfg : entity.getAreaCfg()) {
						cfg.initDefaultValue();
						BeanUtils.copyProperties(entity, cfg, new String[] { "cfgDesc", "cfgRegionCode", "cfgType" });
						areaIpCfgDao.saveAreaIpCfg(cfg);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("获取编译ID出错");
				throw new MaatConvertException(e.getMessage());
			}
		} else {
			entity.setEditorId(entity.getCurrentUser().getId());
			entity.setEditTime(new Date());
			entity.setIsValid(0);
			entity.setIsAudit(0);
			appCfgDao.updateAppTopicDomainCfg(entity);
			// 删除旧的区域IP，新增新的区域IP
			AreaIpCfg area = new AreaIpCfg();
			area.setCompileId(entity.getCompileId());
			area.setFunctionId(entity.getFunctionId());
			areaIpCfgDao.deleteAreaIpCfg(area);
			entity.setCreateTime(new Date());
			entity.setCreatorId(entity.getCurrentUser().getId());
			// 保存区域IP信息
			if (entity.getAreaCfg() != null) {
				for (AreaIpCfg cfg : entity.getAreaCfg()) {
					cfg.initDefaultValue();
					BeanUtils.copyProperties(entity, cfg, new String[] { "cfgDesc", "cfgRegionCode", "cfgType" });
					areaIpCfgDao.saveAreaIpCfg(cfg);
				}
			}
		}
	}

	@Transactional(readOnly = false, rollbackFor = RuntimeException.class)
	public void saveOrUpdateAppByteCfg(AppByteCfg entity) throws Exception {
		// 设置区域运营商信息
		setAreaEffectiveIds(entity);
		if (entity.getCfgId() == null) {
			Integer compileId = 0;
			try {
				List<Integer> idList = ConfigServiceUtil.getId(1, 1);
				if (idList != null && idList.size() > 0) {
					compileId = idList.get(0);
				}
				entity.setCompileId(compileId);
				entity.setCreateTime(new Date());
				entity.setCreatorId(entity.getCurrentUser().getId());
				entity.setIsValid(0);
				entity.setIsAudit(0);
				appCfgDao.insertAppByteCfg(entity);
				// 保存区域IP信息
				if (entity.getAreaCfg() != null) {
					for (AreaIpCfg cfg : entity.getAreaCfg()) {
						cfg.initDefaultValue();
						BeanUtils.copyProperties(entity, cfg, new String[] { "cfgDesc", "cfgRegionCode", "cfgType" });
						areaIpCfgDao.saveAreaIpCfg(cfg);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("获取编译ID出错");
				throw new MaatConvertException(e.getMessage());
			}
		} else {
			entity.setEditorId(entity.getCurrentUser().getId());
			entity.setEditTime(new Date());
			entity.setIsValid(0);
			entity.setIsAudit(0);
			appCfgDao.updateAppByteCfg(entity);
			// 删除旧的区域IP，新增新的区域IP
			AreaIpCfg area = new AreaIpCfg();
			area.setCompileId(entity.getCompileId());
			area.setFunctionId(entity.getFunctionId());
			areaIpCfgDao.deleteAreaIpCfg(area);
			entity.setCreateTime(new Date());
			entity.setCreatorId(entity.getCurrentUser().getId());
			// 保存区域IP信息
			if (entity.getAreaCfg() != null) {
				for (AreaIpCfg cfg : entity.getAreaCfg()) {
					cfg.initDefaultValue();
					BeanUtils.copyProperties(entity, cfg, new String[] { "cfgDesc", "cfgRegionCode", "cfgType" });
					areaIpCfgDao.saveAreaIpCfg(cfg);
				}
			}
		}
	}

	@Transactional(readOnly = false, rollbackFor = RuntimeException.class)
	public void saveOrUpdateAppSslCfg(AppSslCertCfg entity) throws Exception {
		// 设置区域运营商信息
		setAreaEffectiveIds(entity);
		if (entity.getCfgId() == null) {
			Integer compileId = 0;
			try {
				List<Integer> idList = ConfigServiceUtil.getId(1, 1);
				if (idList != null && idList.size() > 0) {
					compileId = idList.get(0);
				}
				entity.setCompileId(compileId);
				entity.setCreateTime(new Date());
				entity.setCreatorId(entity.getCurrentUser().getId());
				entity.setIsValid(0);
				entity.setIsAudit(0);
				appCfgDao.insertAppSslCfg(entity);
				// 保存区域IP信息
				if (entity.getAreaCfg() != null) {
					for (AreaIpCfg cfg : entity.getAreaCfg()) {
						cfg.initDefaultValue();
						BeanUtils.copyProperties(entity, cfg, new String[] { "cfgRegionCode", "cfgType" });
						areaIpCfgDao.saveAreaIpCfg(cfg);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				logger.info("获取编译ID出错");
				throw new MaatConvertException(e.getMessage());
			}
		} else {
			entity.setEditorId(entity.getCurrentUser().getId());
			entity.setEditTime(new Date());
			entity.setIsValid(0);
			entity.setIsAudit(0);
			appCfgDao.updateAppSslCfg(entity);

			// 删除旧的区域IP，新增新的区域IP
			AreaIpCfg area = new AreaIpCfg();
			area.setCompileId(entity.getCompileId());
			area.setFunctionId(entity.getFunctionId());
			areaIpCfgDao.deleteAreaIpCfg(area);
			entity.setCreateTime(new Date());
			entity.setCreatorId(entity.getCurrentUser().getId());
			// 保存区域IP信息
			if (entity.getAreaCfg() != null) {
				for (AreaIpCfg cfg : entity.getAreaCfg()) {
					cfg.initDefaultValue();
					BeanUtils.copyProperties(entity, cfg, new String[] { "cfgDesc", "cfgRegionCode", "cfgType" });
					areaIpCfgDao.saveAreaIpCfg(cfg);
				}
			}

		}

	}

	@Transactional(readOnly = false, rollbackFor = RuntimeException.class)
	public void saveOrUpdateAppTcpeCfg(AppTcpCfg entity) throws Exception {
		// 设置区域运营商信息
		setAreaEffectiveIds(entity);
		if (entity.getCfgId() == null) {
			Integer compileId = 0;
			try {
				List<Integer> idList = ConfigServiceUtil.getId(1, 1);
				if (idList != null && idList.size() > 0) {
					compileId = idList.get(0);
				}
				entity.setCompileId(compileId);
				entity.setCreateTime(new Date());
				entity.setCreatorId(entity.getCurrentUser().getId());
				entity.setIsValid(0);
				entity.setIsAudit(0);
				appCfgDao.insertAppTcpCfg(entity);
				// 保存区域IP信息
				if (entity.getAreaCfg() != null) {
					for (AreaIpCfg cfg : entity.getAreaCfg()) {
						cfg.initDefaultValue();
						BeanUtils.copyProperties(entity, cfg, new String[] { "cfgDesc", "cfgRegionCode", "cfgType" });
						areaIpCfgDao.saveAreaIpCfg(cfg);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("获取编译ID出错");
				throw new MaatConvertException(e.getMessage());
			}
		} else {
			entity.setEditorId(entity.getCurrentUser().getId());
			entity.setEditTime(new Date());
			entity.setIsValid(0);
			entity.setIsAudit(0);
			appCfgDao.updateAppTcpCfg(entity);
			// 删除旧的区域IP，新增新的区域IP
			AreaIpCfg area = new AreaIpCfg();
			area.setCompileId(entity.getCompileId());
			area.setFunctionId(entity.getFunctionId());
			areaIpCfgDao.deleteAreaIpCfg(area);
			entity.setCreateTime(new Date());
			entity.setCreatorId(entity.getCurrentUser().getId());
			// 保存区域IP信息
			if (entity.getAreaCfg() != null) {
				for (AreaIpCfg cfg : entity.getAreaCfg()) {
					cfg.initDefaultValue();
					BeanUtils.copyProperties(entity, cfg, new String[] { "cfgDesc", "cfgRegionCode", "cfgType" });
					areaIpCfgDao.saveAreaIpCfg(cfg);
				}
			}
		}
	}

	public void auditAppTcpCfg(AppTcpCfg entity, Integer isAudit) {
		ToMaatBean maatBean = new ToMaatBean();
		MaatCfg maatCfg = new MaatCfg();
		List<MaatCfg> configCompileList = new ArrayList();
		List<GroupCfg> groupRelationList = new ArrayList();
		List<IpCfg> ipRegionList = new ArrayList();
		List<StringCfg> strRegionList = new ArrayList();
		List<NumBoundaryCfg> numRegionList = new ArrayList();
		List<DigestCfg> digestRegionList = new ArrayList();
		List<IpCfg> areaIpRegionList = new ArrayList();
		entity.setTableName(AppTcpCfg.getTablename());
		appCfgDao.auditCfg(entity);

		if (isAudit == 1) {
			List<AppTcpCfg> list = new ArrayList();
			list.add(entity);
			Map<String, List> map = cfgConvert(numRegionList, list, 4, entity, groupRelationList);
			groupRelationList = map.get("groupList");
			numRegionList = map.get("dstList");
		}
		/*
		 * //保存区域IP信息 List<AreaIpCfg>
		 * areaIpCfgList=areaIpCfgDao.getByCompileId(entity.getCompileId());
		 * if(!StringUtil.isEmpty(areaIpCfgList)){ AreaIpCfg cfg = new AreaIpCfg();
		 * BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
		 * cfg.setTableName(AreaIpCfg.getTablename()); appCfgDao.auditCfg(cfg);
		 * if(isAudit==1){ Map<String,List> map =
		 * cfgConvert(areaIpRegionList,areaIpCfgList,4,entity,groupRelationList);
		 * groupRelationList=map.get("groupList"); areaIpRegionList=map.get("dstList");
		 * } }
		 */
		// 构造提交综合服务参数格式，一条配置提交一次综合服务
		if (isAudit == 1) {
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
			// 设置APP自定义域
			String userRegion = "APP_ID=" + entity.getAppCode();
			maatCfg.setUserRegion(userRegion);
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(entity.getAuditTime());
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.INSERT_ACTION);
			// 调用服务接口下发配置数据
			String json = gsonToJson(maatBean);
			logger.info("app SSL配置下发配置参数：" + json);
			// 调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
			logger.info("app TCP配置下发响应信息：" + result.getMsg());

		} else if (isAudit == 3) {
			maatCfg.setCompileId(entity.getCompileId());
			maatCfg.setServiceId(entity.getServiceId());
			maatCfg.setIsValid(0);// 无效
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(entity.getAuditTime());
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.UPDATE_ACTION);
			// 调用服务接口取消配置
			String json = gsonToJson(maatBean);
			logger.info("app TCP配置下发配置参数：" + json);
			// 调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json, 1);
			logger.info("app TCP配置取消配置响应信息：" + result.getMsg());
		}
	}

	public void auditAppPolicyCfg(AppPolicyCfg entity, Integer isAudit) {
		String configType = entity.getConfigType();
		ToMaatBean maatBean = new ToMaatBean();
		MaatCfg maatCfg = new MaatCfg();
		List<MaatCfg> configCompileList = new ArrayList();
		List<GroupCfg> groupRelationList = new ArrayList();
		List<IpCfg> ipRegionList = new ArrayList();
		List<StringCfg> strRegionList = new ArrayList();
		List<NumBoundaryCfg> numRegionList = new ArrayList();
		List<DigestCfg> digestRegionList = new ArrayList();
		List<IpCfg> areaIpRegionList = new ArrayList();
		entity.setTableName(AppPolicyCfg.getTablename());
		List<NtcSubscribeIdCfg> ntcList = new ArrayList();
		appCfgDao.auditCfg(entity);
		// 更新关键字表状态
		if (entity.getCompileId() != null) {
			NtcSubscribeIdCfg ntc = new NtcSubscribeIdCfg();
			ntc.setCompileId(entity.getCompileId());
			ntc.setIsAudit(entity.getIsAudit());
			ntc.setIsValid(entity.getIsValid());
			ntc.setAuditorId(UserUtils.getUser().getId());
			ntc.setAuditTime(entity.getAuditTime());
			stringcfgDao.updateSubscribeIdCfg(ntc);
		}

		if (isAudit == 1) {
			StringBuffer cfgKeywords = new StringBuffer();
			if (entity.getBehavCode() != null) {
				if (Constants.SPECIFIC_SERVICE_CFG_TYPE_APP.equalsIgnoreCase(configType)) {
					cfgKeywords.append(Constants.APP_ID_REGION + "=" + entity.getAppCode());
				} else if (Constants.SPECIFIC_SERVICE_CFG_TYPE_ENCRYPTED_TUNNEL_BEHAVIOR.equalsIgnoreCase(configType)
						|| Constants.SPECIFIC_SERVICE_CFG_TYPE_BASIC_PROTOCOL.equalsIgnoreCase(configType)) {
					cfgKeywords.append(Constants.PROTO_ID_REGION + "=" + entity.getAppCode());
				}
				cfgKeywords.append(Constants.KEYWORD_EXPR);
				cfgKeywords.append(Constants.BEHAV_ID_REGION + "=" + entity.getBehavCode());
				// entity.setCfgKeywords(entity.getAppCode()+Constants.KEYWORD_EXPR+entity.getBehavCode());
			} else {
				if (Constants.SPECIFIC_SERVICE_CFG_TYPE_APP.equalsIgnoreCase(configType)) {
					cfgKeywords.append(Constants.APP_ID_REGION + "=" + entity.getAppCode());
				} else if (Constants.SPECIFIC_SERVICE_CFG_TYPE_ENCRYPTED_TUNNEL_BEHAVIOR.equalsIgnoreCase(configType)
						|| Constants.SPECIFIC_SERVICE_CFG_TYPE_BASIC_PROTOCOL.equalsIgnoreCase(configType)) {
					cfgKeywords.append(Constants.PROTO_ID_REGION + "=" + entity.getAppCode());
				}
				// entity.setCfgKeywords(entity.getAppCode()+"");
			}
			entity.setCfgKeywords(cfgKeywords.toString());
			List<AppPolicyCfg> list = new ArrayList();
			list.add(entity);
			Map<String, List> map = cfgConvert(strRegionList, list, 2, entity, groupRelationList);
			groupRelationList = map.get("groupList");
			strRegionList = map.get("dstList");
		}

		// 查询子配置并修改审核状态
		entity = this.getAppPolicyCfg(entity.getCfgId(),entity.getCompileId());
		if (entity.getIpPortList() != null && entity.getIpPortList().size() > 0) {
			IpPortCfg cfg = new IpPortCfg();
			BeanUtils.copyProperties(entity, cfg, new String[] { "cfgId" });
			cfg.setTableName(IpPortCfg.getTablename());
			appCfgDao.auditCfg(cfg);
			if (isAudit == 1) {
				Map<String, List> map = cfgConvert(ipRegionList, entity.getIpPortList(), 1, entity, groupRelationList);
				groupRelationList = map.get("groupList");
				ipRegionList = map.get("dstList");
				if (map.get("numRegionList") != null) {
					numRegionList.addAll(map.get("numRegionList"));
				}
			}
		}
		if (entity.getNtcSubscribeIdCfgList() != null && entity.getNtcSubscribeIdCfgList().size() > 0) {
			if (isAudit == 1) {
				Map<String, List> ntcMap = cfgConvert(strRegionList, entity.getNtcSubscribeIdCfgList(), 2, entity,
						groupRelationList);
				groupRelationList = ntcMap.get("groupList");
				strRegionList = ntcMap.get("dstList");
			}
		}

		// 保存区域IP信息
		List<AreaIpCfg> areaIpCfgList = areaIpCfgDao.getByCompileId(entity.getCompileId());
		if (!StringUtil.isEmpty(areaIpCfgList)) {
			AreaIpCfg cfg = new AreaIpCfg();
			BeanUtils.copyProperties(entity, cfg, new String[] { "cfgId" });
			cfg.setTableName(AreaIpCfg.getTablename());
			appCfgDao.auditCfg(cfg);
			if (isAudit == 1) {
				Map<String, List> map = cfgConvert(areaIpRegionList, areaIpCfgList, 1, entity, groupRelationList);
				groupRelationList = map.get("groupList");
				areaIpRegionList = map.get("dstList");
			}
		}
		// 构造提交综合服务参数格式，一条配置提交一次综合服务
		if (isAudit == 1) {
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
			String userRegion = null;
			if (Constants.SPECIFIC_SERVICE_CFG_TYPE_APP.equalsIgnoreCase(configType)) {
				// 设置APP自定义域
				userRegion = Constants.APP_ID_REGION + "=" + entity.getAppCode();
				if (entity.getBehavCode() != null) {
					userRegion += Constants.USER_REGION_SPLIT + Constants.BEHAV_ID_REGION + "=" + entity.getBehavCode();
				}
			} else if (Constants.SPECIFIC_SERVICE_CFG_TYPE_BASIC_PROTOCOL.equalsIgnoreCase(configType)) {
				userRegion = Constants.PROTO_ID_REGION + "=" + entity.getAppCode();
			} else if (Constants.SPECIFIC_SERVICE_CFG_TYPE_ENCRYPTED_TUNNEL_BEHAVIOR.equalsIgnoreCase(configType)) {
				userRegion = Constants.PROTO_ID_REGION + "=" + entity.getAppCode();
				if (entity.getBehavCode() != null) {
					userRegion += Constants.USER_REGION_SPLIT + Constants.BEHAV_ID_REGION + "=" + entity.getBehavCode();
				}

			}
			
			// 限速业务需要设置
			if (Constants.RATELIMIT_ACTION.equals(entity.getAction())) {
				entity.setUserRegion1(StringUtil.isEmpty(entity.getUserRegion1()) ? "0":entity.getUserRegion1());
				//限速 需要发Droprate=0.001 ，暂不支持Bandwidth=200kbps
				if(entity.getUserRegion1().equals("0")){//丢包率
					entity.setUserRegion2(StringUtil.isEmpty(entity.getUserRegion2()) ? "":entity.getUserRegion2());
					userRegion+=Constants.USER_REGION_SPLIT +Constants.INTERCEPT_IP_RATELIMIT_DROPRATE_USER_REGION_KEY+"="+entity.getUserRegion2();
				}else if(entity.getUserRegion1().equals("1")){//带宽
					entity.setUserRegion3(StringUtil.isEmpty(entity.getUserRegion3()) ? "":entity.getUserRegion3());
					userRegion+=Constants.USER_REGION_SPLIT +Constants.INTERCEPT_IP_RATELIMIT_BANDWITH_USER_REGION_KEY+"="+entity.getUserRegion3();
				}
			}
			//VIGNORE=1
			if(StringUtil.isEmpty(entity.getUserRegion4())){
				userRegion+=Constants.USER_REGION_SPLIT +Constants.APP_VIGNORE_USER_REGION_KEY+"=1";
			}
			
			maatCfg.setUserRegion(userRegion);
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(entity.getAuditTime());
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.INSERT_ACTION);
			// 调用服务接口下发配置数据
			String json = gsonToJson(maatBean);
			logger.info("app策略配置下发配置参数：" + json);
			// 调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
			logger.info("app策略配置下发响应信息：" + result.getMsg());

		} else if (isAudit == 3) {
			maatCfg.setCompileId(entity.getCompileId());
			maatCfg.setServiceId(entity.getServiceId());
			maatCfg.setIsValid(0);// 无效
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(entity.getAuditTime());
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.UPDATE_ACTION);
			// 调用服务接口取消配置
			String json = gsonToJson(maatBean);
			logger.info("app策略配置下发配置参数：" + json);
			// 调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json, 1);
			logger.info("app策略配置取消配置响应信息：" + result.getMsg());
		}
	}

	public void auditAppIpCfg(AppIpCfg entity, Integer isAudit) {
		ToMaatBean maatBean = new ToMaatBean();
		MaatCfg maatCfg = new MaatCfg();
		List<MaatCfg> configCompileList = new ArrayList();
		List<GroupCfg> groupRelationList = new ArrayList();
		List<IpCfg> ipRegionList = new ArrayList();
		List<StringCfg> strRegionList = new ArrayList();
		List<NumBoundaryCfg> numRegionList = new ArrayList();
		List<DigestCfg> digestRegionList = new ArrayList();
		List<IpCfg> areaIpRegionList = new ArrayList();
		entity.setTableName(AppIpCfg.getTablename());
		appCfgDao.auditCfg(entity);
		if (isAudit == 1) {
			List<AppIpCfg> list = new ArrayList();
			list.add(entity);
			Map<String, List> map = cfgConvert(ipRegionList, list, 1, entity, groupRelationList);
			groupRelationList = map.get("groupList");
			ipRegionList = map.get("dstList");
		}
		// 保存区域IP信息
		List<AreaIpCfg> areaIpCfgList = areaIpCfgDao.getByCompileId(entity.getCompileId());
		if (!StringUtil.isEmpty(areaIpCfgList)) {
			AreaIpCfg cfg = new AreaIpCfg();
			BeanUtils.copyProperties(entity, cfg, new String[] { "cfgId" });
			cfg.setTableName(AreaIpCfg.getTablename());
			appCfgDao.auditCfg(cfg);
			if (isAudit == 1) {
				Map<String, List> map = cfgConvert(areaIpRegionList, areaIpCfgList, 1, entity, groupRelationList);
				groupRelationList = map.get("groupList");
				areaIpRegionList = map.get("dstList");
			}
		}
		// 构造提交综合服务参数格式，一条配置提交一次综合服务
		if (isAudit == 1) {
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
			// 设置APP自定义域
			String userRegion = "APP_ID=" + entity.getAppCode();
			maatCfg.setUserRegion(userRegion);
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(entity.getAuditTime());
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.INSERT_ACTION);
			// 调用服务接口下发配置数据
			String json = gsonToJson(maatBean);
			logger.info("app协议IP配置下发配置参数：" + json);
			// 调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
			logger.info("app协议IP配置下发响应信息：" + result.getMsg());

		} else if (isAudit == 3) {
			maatCfg.setCompileId(entity.getCompileId());
			maatCfg.setServiceId(entity.getServiceId());
			maatCfg.setIsValid(0);// 无效
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(entity.getAuditTime());
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.UPDATE_ACTION);
			// 调用服务接口取消配置
			String json = gsonToJson(maatBean);
			logger.info("app协议IP配置下发配置参数：" + json);
			// 调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json, 1);
			logger.info("app协议IP配置取消配置响应信息：" + result.getMsg());
		}
	}
	/**
	 * ip复用批量审核
	 * @param data
	 */
	public void auditAppIpCfg(List<AppIpCfg> data) {
		SqlSessionFactory sqlSessionFactory=SpringContextHolder.getBean(SqlSessionFactory.class);
        SqlSession batchSqlSession = null;
        try{
            batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
            for(int index = 0; index < data.size();index++){
            	AppIpCfg t = data.get(index);
                ((AppCfgDao) batchSqlSession.getMapper(AppCfgDao.class)).auditCfg(t);
            }
            batchSqlSession.commit();
        }finally {
            if(batchSqlSession != null){
                batchSqlSession.close();
            }
        }
    }
	public void auditAppIpCfg(List<AppIpCfg> entitys, Integer isAudit) {
		MaatCfg maatCfg = new MaatCfg();
		List<MaatCfg> configCompileList = new ArrayList();
		List<GroupCfg> groupRelationList = new ArrayList();
		List<IpCfg> ipRegionList = new ArrayList();
		List<StringCfg> strRegionList = new ArrayList();
		List<NumBoundaryCfg> numRegionList = new ArrayList();
		List<DigestCfg> digestRegionList = new ArrayList();
		List<IpCfg> areaIpRegionList = new ArrayList();
		this.auditAppIpCfg(entitys);
//		for(AppIpCfg entity:entitys) {
//			// 保存区域IP信息
//			List<AreaIpCfg> areaIpCfgList = areaIpCfgDao.getByCompileId(entitys.get(0).getCompileId());
//			if (!StringUtil.isEmpty(areaIpCfgList)) {
//				AreaIpCfg cfg = new AreaIpCfg();
//				BeanUtils.copyProperties(entity, cfg, new String[] { "cfgId" });
//				cfg.setTableName(AreaIpCfg.getTablename());
//				appCfgDao.auditCfg(cfg);
//				if (isAudit == 1) {
//					Map<String, List> map = cfgConvert(areaIpRegionList, areaIpCfgList, 1, entity, groupRelationList);
//					groupRelationList = map.get("groupList");
//					areaIpRegionList = map.get("dstList");
//				}
//			}
//		}
		Integer specServiceId= entitys.get(0).getSpecServiceId();
		SpecificServiceCfg specificServiceCfg=specificServiceCfgDao.getBySpecServiceId(specServiceId);
		//String businessType=specificServiceCfg.getBusinessType();
		ConfigGroupInfo configGroupInfo=configGroupInfoDao.getConfigGroupInfoByGroupId(specificServiceCfg.getGroupId()); 
		if(configGroupInfo==null) {
			Integer groupId = ConfigServiceUtil.getId(2, 1).get(0);
			specificServiceCfg.setGroupId(groupId);
			configGroupInfo = new ConfigGroupInfo();
			configGroupInfo.setGroupId(specificServiceCfg.getGroupId());
			configGroupInfo.setGroupName(specificServiceCfg.getSpecServiceName());
			configGroupInfo.setIsIssued(0);
			configGroupInfo.setCompileId(entitys.get(0).getCompileId());
			configGroupInfo.setGroupType(1);
			configGroupInfoDao.insertConfigGroupInfo(configGroupInfo);
			//更新group_id
			specificServiceCfg.setOpTime(new Date());
			specificServiceCfgDao.update(specificServiceCfg);
		}
		Integer groupId=configGroupInfo.getGroupId();
		// 构造提交综合服务参数格式，一条配置提交一次综合服务
		if (isAudit == 1) {
			if(configGroupInfo.getIsIssued()==0) {//未下发，下发region compile,group.
				ToMaatBean maatBean = new ToMaatBean();
				maatCfg.initDefaultValue();
				//group
				GroupCfg groupCfg=new GroupCfg();
				groupCfg.setCompileId(entitys.get(0).getCompileId());
				groupCfg.setGroupId(groupId);
				groupCfg.setIsValid(Constants.VALID_YES);
				groupCfg.setAuditTime(entitys.get(0).getAuditTime());
				groupRelationList.add(groupCfg);
				BeanUtils.copyProperties(entitys.get(0), maatCfg);
				ipRegionList.addAll(groupReuseCfgAddRemoveConvert(entitys,Constants.VALID_YES,groupId));
				maatCfg.setAction(entitys.get(0).getAction());
				maatCfg.setAuditTime(entitys.get(0).getAuditTime());
				maatCfg.setIpRegionList(ipRegionList);
				maatCfg.setStrRegionList(strRegionList);
				maatCfg.setNumRegionList(numRegionList);
				maatCfg.setDigestRegionList(digestRegionList);
				maatCfg.setGroupRelationList(groupRelationList);
				maatCfg.setGroupNum(groupRelationList.size());
				maatCfg.setAreaIpRegionList(areaIpRegionList);
				maatCfg.setIsValid(entitys.get(0).getIsValid());
				// 设置APP自定义域
				String userRegion = "APP_ID=" + entitys.get(0).getAppCode()/*+Constants.USER_REGION_SPLIT+"ASEV_ID="+businessType*/;
				maatCfg.setUserRegion(userRegion);
				configCompileList.add(maatCfg);
				maatBean.setConfigCompileList(configCompileList);
				maatBean.setAuditTime(entitys.get(0).getAuditTime());
				maatBean.setCreatorName(entitys.get(0).getCurrentUser().getName());
				maatBean.setVersion(Constants.MAAT_VERSION);
				maatBean.setOpAction(Constants.INSERT_ACTION);
				// 调用服务接口下发配置数据
				String json = gsonToJson(maatBean);
				logger.info("app协议IP配置下发配置参数：" + json);
				// 调用服务接口下发配置
				ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
				logger.info("app协议IP配置下发响应信息：" + result.getMsg());
				configGroupInfo.setIsIssued(1);
				configGroupInfo.setUpdateTime(new Date());
				configGroupInfoDao.updateConfigGroupInfobyGroupId(configGroupInfo);
			}else if(configGroupInfo.getIsIssued()==1) {//已经下发，分组复用下发
				GroupReuseAddBean maatBean = new GroupReuseAddBean();		
				List<GroupReuseCfg> groupReuseList=new ArrayList<>();
				GroupReuseCfg groupReuseCfg=new GroupReuseCfg();
				/*groupReuseCfg.setUserRegion(userRegion);*/
				groupReuseCfg.setServiceId(entitys.get(0).getServiceId());
				ipRegionList.addAll(groupReuseCfgAddRemoveConvert(entitys,Constants.VALID_YES,groupId));
				groupReuseCfg.setIpRegionList(ipRegionList);
				groupReuseCfg.setStrRegionList(strRegionList);
				groupReuseCfg.setNumRegionList(numRegionList);
				groupReuseList.add(groupReuseCfg);
				maatBean.setGroupReuseCfgList(groupReuseList);
				maatBean.setAuditTime(entitys.get(0).getAuditTime());
				maatBean.setCreatorName(entitys.get(0).getCurrentUser().getName());
				maatBean.setVersion(Constants.MAAT_VERSION);
				maatBean.setOpAction(Constants.INSERT_ACTION);
				//调用服务接口下发配置数据
				String json=gsonToJson(maatBean);
				logger.info("app协议IP域新增配置下发配置参数："+json);
				//调用服务接口下发配置
				ToMaatResult result = ConfigServiceUtil.postGroupReuseSources(json);
				logger.info("app协议IP域新增配置响应信息："+result.getMsg());
			}else {
				throw new RuntimeException("Unknown configGroupInfo isIssued value");
			}
		} else if (isAudit == 3) {
			//已经下发过的,调用分组复用配置删除接口
			GroupReuseAddBean maatBean = new GroupReuseAddBean();		
			List<GroupReuseCfg> groupReuseList=new ArrayList<>();
			GroupReuseCfg groupReuseCfg=new GroupReuseCfg();
			ipRegionList.addAll(groupReuseCfgAddRemoveConvert(entitys,Constants.VALID_NO,groupId));
			groupReuseCfg.setIpRegionList(ipRegionList);
			groupReuseCfg.setStrRegionList(strRegionList);
			groupReuseCfg.setNumRegionList(numRegionList);
			groupReuseList.add(groupReuseCfg);
			maatBean.setGroupReuseCfgList(groupReuseList);
			maatBean.setAuditTime(entitys.get(0).getAuditTime());
			maatBean.setCreatorName(entitys.get(0).getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.UPDATE_ACTION);
			//调用服务接口下发配置数据
			String json=gsonToJson(maatBean);
			logger.info("app协议IP域删除配置下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json,3);
			logger.info("app协议IP域删除配置响应信息："+result.getMsg());
		}
	}

	public void auditAppHttpCfg(AppHttpCfg entity, Integer isAudit) {
		ToMaatBean maatBean = new ToMaatBean();
		MaatCfg maatCfg = new MaatCfg();
		List<MaatCfg> configCompileList = new ArrayList();
		List<GroupCfg> groupRelationList = new ArrayList();
		List<IpCfg> ipRegionList = new ArrayList();
		List<StringCfg> strRegionList = new ArrayList();
		List<NumBoundaryCfg> numRegionList = new ArrayList();
		List<DigestCfg> digestRegionList = new ArrayList();
		List<IpCfg> areaIpRegionList = new ArrayList();
		entity.setTableName(AppHttpCfg.getTablename());
		appCfgDao.auditCfg(entity);

		if (isAudit == 1) {
			List<AppHttpCfg> list = new ArrayList();
			list.add(entity);
			Map<String, List> map = cfgConvert(strRegionList, list, 2, entity, groupRelationList);
			groupRelationList = map.get("groupList");
			strRegionList = map.get("dstList");
		}
		// 保存区域IP信息
		List<AreaIpCfg> areaIpCfgList = areaIpCfgDao.getByCompileId(entity.getCompileId());
		if (!StringUtil.isEmpty(areaIpCfgList)) {
			AreaIpCfg cfg = new AreaIpCfg();
			BeanUtils.copyProperties(entity, cfg, new String[] { "cfgId" });
			cfg.setTableName(AreaIpCfg.getTablename());
			appCfgDao.auditCfg(cfg);
			if (isAudit == 1) {
				Map<String, List> map = cfgConvert(areaIpRegionList, areaIpCfgList, 1, entity, groupRelationList);
				groupRelationList = map.get("groupList");
				areaIpRegionList = map.get("dstList");
			}
		}
		// 构造提交综合服务参数格式，一条配置提交一次综合服务
		if (isAudit == 1) {
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
			// 设置APP自定义域
			String userRegion = "APP_ID=" + entity.getAppCode();
			maatCfg.setUserRegion(userRegion);
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(entity.getAuditTime());
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.INSERT_ACTION);
			// 调用服务接口下发配置数据
			String json = gsonToJson(maatBean);
			logger.info("app HTTP配置下发配置参数：" + json);
			// 调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
			logger.info("app HTTP配置下发响应信息：" + result.getMsg());

		} else if (isAudit == 3) {
			maatCfg.setCompileId(entity.getCompileId());
			maatCfg.setServiceId(entity.getServiceId());
			maatCfg.setIsValid(0);// 无效
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(entity.getAuditTime());
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.UPDATE_ACTION);
			// 调用服务接口取消配置
			String json = gsonToJson(maatBean);
			logger.info("app HTTP配置下发配置参数：" + json);
			// 调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json, 1);
			logger.info("app HTTP配置取消配置响应信息：" + result.getMsg());
		}
	}

	public void auditAppDomainCfg(AppDomainCfg entity, Integer isAudit) {
		ToMaatBean maatBean = new ToMaatBean();
		MaatCfg maatCfg = new MaatCfg();
		List<MaatCfg> configCompileList = new ArrayList();
		List<GroupCfg> groupRelationList = new ArrayList();
		List<IpCfg> ipRegionList = new ArrayList();
		List<StringCfg> strRegionList = new ArrayList();
		List<NumBoundaryCfg> numRegionList = new ArrayList();
		List<DigestCfg> digestRegionList = new ArrayList();
		List<IpCfg> areaIpRegionList = new ArrayList();
		entity.setTableName(AppDomainCfg.getTablename());
		appCfgDao.auditCfg(entity);
		if (isAudit == 1) {
			entity.setCfgKeywords(entity.getCfgKeywords());
			List<AppDomainCfg> list = new ArrayList();
			list.add(entity);
			Map<String, List> map = cfgConvert(strRegionList, list, 2, entity, groupRelationList);
			groupRelationList = map.get("groupList");
			strRegionList = map.get("dstList");
		}
		// 保存区域IP信息
		List<AreaIpCfg> areaIpCfgList = areaIpCfgDao.getByCompileId(entity.getCompileId());
		if (!StringUtil.isEmpty(areaIpCfgList)) {
			AreaIpCfg cfg = new AreaIpCfg();
			BeanUtils.copyProperties(entity, cfg, new String[] { "cfgId" });
			cfg.setTableName(AreaIpCfg.getTablename());
			appCfgDao.auditCfg(cfg);
			if (isAudit == 1) {
				Map<String, List> map = cfgConvert(areaIpRegionList, areaIpCfgList, 1, entity, groupRelationList);
				groupRelationList = map.get("groupList");
				areaIpRegionList = map.get("dstList");
			}
		}
		// 构造提交综合服务参数格式，一条配置提交一次综合服务
		if (isAudit == 1) {
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
			// 设置APP自定义域
			String userRegion = "APP_ID=" + entity.getAppCode() + Constants.USER_REGION_SPLIT + "DOMAIN_ID="
					+ entity.getCompileId() + Constants.USER_REGION_SPLIT + "DOMAIN_STR="
					+ keywordsEscape(entity.getCfgKeywords());
			maatCfg.setUserRegion(userRegion);
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(entity.getAuditTime());
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.INSERT_ACTION);
			// 调用服务接口下发配置数据
			String json = gsonToJson(maatBean);
			logger.info("app域名配置下发配置参数：" + json);
			// 调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
			logger.info("app域名配置下发响应信息：" + result.getMsg());

		} else if (isAudit == 3) {
			maatCfg.setCompileId(entity.getCompileId());
			maatCfg.setServiceId(entity.getServiceId());
			maatCfg.setIsValid(0);// 无效
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(entity.getAuditTime());
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.UPDATE_ACTION);
			// 调用服务接口取消配置
			String json = gsonToJson(maatBean);
			logger.info("app域名配置下发配置参数：" + json);
			// 调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json, 1);
			logger.info("app域名配置取消配置响应信息：" + result.getMsg());
		}
	}

	// app主题网站配置审核
	public void auditAppTopicDomainCfg(AppTopicDomainCfg entity, Integer isAudit) {
		ToMaatBean maatBean = new ToMaatBean();
		MaatCfg maatCfg = new MaatCfg();
		List<MaatCfg> configCompileList = new ArrayList();
		List<GroupCfg> groupRelationList = new ArrayList();
		List<IpCfg> ipRegionList = new ArrayList();
		List<StringCfg> strRegionList = new ArrayList();
		List<NumBoundaryCfg> numRegionList = new ArrayList();
		List<DigestCfg> digestRegionList = new ArrayList();
		List<IpCfg> areaIpRegionList = new ArrayList();
		entity.setTableName(AppTopicDomainCfg.getTablename());
		appCfgDao.auditCfg(entity);
		// 域名为关键字
		if (isAudit == 1) {
			entity.setCfgKeywords(entity.getCfgKeywords());
			List<AppTopicDomainCfg> list = new ArrayList();
			list.add(entity);
			Map<String, List> map = cfgConvert(strRegionList, list, 2, entity, groupRelationList);
			groupRelationList = map.get("groupList");
			strRegionList = map.get("dstList");
		}
		// 保存区域IP信息
		List<AreaIpCfg> areaIpCfgList = areaIpCfgDao.getByCompileId(entity.getCompileId());
		if (!StringUtil.isEmpty(areaIpCfgList)) {
			AreaIpCfg cfg = new AreaIpCfg();
			BeanUtils.copyProperties(entity, cfg, new String[] { "cfgId" });
			cfg.setTableName(AreaIpCfg.getTablename());
			appCfgDao.auditCfg(cfg);
			if (isAudit == 1) {
				Map<String, List> map = cfgConvert(areaIpRegionList, areaIpCfgList, 1, entity, groupRelationList);
				groupRelationList = map.get("groupList");
				areaIpRegionList = map.get("dstList");
			}
		}
		// 构造提交综合服务参数格式，一条配置提交一次综合服务
		if (isAudit == 1) {
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

			// 设置APP自定义域 
			String domain = entity.getCfgKeywords();
			WebsiteDomainTopic websiteDomainTopic = new WebsiteDomainTopic();
			websiteDomainTopic.setDomain(domain);
			//查询domainId
			List<WebsiteDomainTopic> domainDict = appCfgDao.getDomainDict(websiteDomainTopic);
			Long domainId = domainDict.get(0).getId();
			// "APP_ID="+entity.getAppCode()+Constants.USER_REGION_SPLIT+
			String userRegion = "WEB_ID=" + domainId /*+ Constants.USER_REGION_SPLIT + "DOMAIN_STR="
					+ keywordsEscape(entity.getDomain())*/;

			maatCfg.setUserRegion(userRegion);

			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(entity.getAuditTime());
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.INSERT_ACTION);
			// 调用服务接口下发配置数据
			String json = gsonToJson(maatBean);
			logger.info("app主题网站配置下发配置参数：" + json);
			// 调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
			logger.info("app主题网站配置下发响应信息：" + result.getMsg());

		} else if (isAudit == 3) {
			maatCfg.setCompileId(entity.getCompileId());
			maatCfg.setServiceId(entity.getServiceId());
			maatCfg.setIsValid(0);// 无效
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(entity.getAuditTime());
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.UPDATE_ACTION);
			// 调用服务接口取消配置
			String json = gsonToJson(maatBean);
			logger.info("app主题网站配置下发配置参数：" + json);
			// 调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json, 1);
			logger.info("app主题网站配置取消配置响应信息：" + result.getMsg());
		}
	}

	public void auditAppByteCfg(AppByteCfg entity, Integer isAudit) {
		ToMaatBean maatBean = new ToMaatBean();
		MaatCfg maatCfg = new MaatCfg();
		List<MaatCfg> configCompileList = new ArrayList();
		List<GroupCfg> groupRelationList = new ArrayList();
		List<IpCfg> ipRegionList = new ArrayList();
		List<StringCfg> strRegionList = new ArrayList();
		List<NumBoundaryCfg> numRegionList = new ArrayList();
		List<DigestCfg> digestRegionList = new ArrayList();
		List<IpCfg> areaIpRegionList = new ArrayList();
		entity.setTableName(AppByteCfg.getTablename());
		appCfgDao.auditCfg(entity);

		if (isAudit == 1) {
			entity.setCfgKeywords(entity.getBytes());
			List<AppByteCfg> list = new ArrayList();
			list.add(entity);
			Map<String, List> map = cfgConvert(strRegionList, list, 2, entity, groupRelationList);
			groupRelationList = map.get("groupList");
			strRegionList = map.get("dstList");
		}
		// 保存区域IP信息
		List<AreaIpCfg> areaIpCfgList = areaIpCfgDao.getByCompileId(entity.getCompileId());
		if (!StringUtil.isEmpty(areaIpCfgList)) {
			AreaIpCfg cfg = new AreaIpCfg();
			BeanUtils.copyProperties(entity, cfg, new String[] { "cfgId" });
			cfg.setTableName(AreaIpCfg.getTablename());
			appCfgDao.auditCfg(cfg);
			if (isAudit == 1) {
				Map<String, List> map = cfgConvert(areaIpRegionList, areaIpCfgList, 1, entity, groupRelationList);
				groupRelationList = map.get("groupList");
				areaIpRegionList = map.get("dstList");
			}
		}
		// 构造提交综合服务参数格式，一条配置提交一次综合服务
		if (isAudit == 1) {
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
			// 设置用户自定义域
			String userRegion = "APP_ID=" + entity.getAppCode();
			maatCfg.setUserRegion(userRegion);
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(entity.getAuditTime());
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.INSERT_ACTION);
			// 调用服务接口下发配置数据
			String json = gsonToJson(maatBean);
			logger.info("app字节配置下发配置参数：" + json);
			// 调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
			logger.info("app字节配置下发响应信息：" + result.getMsg());

		} else if (isAudit == 3) {
			maatCfg.setCompileId(entity.getCompileId());
			maatCfg.setServiceId(entity.getServiceId());
			maatCfg.setIsValid(0);// 无效
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(entity.getAuditTime());
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.UPDATE_ACTION);
			// 调用服务接口取消配置
			String json = gsonToJson(maatBean);
			logger.info("app字节配置下发配置参数：" + json);
			// 调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json, 1);
			logger.info("app字节配置取消配置响应信息：" + result.getMsg());
		}
	}

	public void auditAppSslCfg(AppSslCertCfg entity, Integer isAudit) {
		ToMaatBean maatBean = new ToMaatBean();
		MaatCfg maatCfg = new MaatCfg();
		List<MaatCfg> configCompileList = new ArrayList();
		List<GroupCfg> groupRelationList = new ArrayList();
		List<IpCfg> ipRegionList = new ArrayList();
		List<StringCfg> strRegionList = new ArrayList();
		List<NumBoundaryCfg> numRegionList = new ArrayList();
		List<DigestCfg> digestRegionList = new ArrayList();
		List<IpCfg> areaIpRegionList = new ArrayList();
		entity.setTableName(AppSslCertCfg.getTablename());
		appCfgDao.auditCfg(entity);

		if (isAudit == 1) {
			List<AppSslCertCfg> list = new ArrayList();
			list.add(entity);
			Map<String, List> map = cfgConvert(strRegionList, list, 3, entity, groupRelationList);
			groupRelationList = map.get("groupList");
			strRegionList = map.get("dstList");
		}
		// 保存区域IP信息
		List<AreaIpCfg> areaIpCfgList = areaIpCfgDao.getByCompileId(entity.getCompileId());
		if (!StringUtil.isEmpty(areaIpCfgList)) {
			AreaIpCfg cfg = new AreaIpCfg();
			BeanUtils.copyProperties(entity, cfg, new String[] { "cfgId" });
			cfg.setTableName(AreaIpCfg.getTablename());
			appCfgDao.auditCfg(cfg);
			if (isAudit == 1) {
				Map<String, List> map = cfgConvert(areaIpRegionList, areaIpCfgList, 1, entity, groupRelationList);
				groupRelationList = map.get("groupList");
				areaIpRegionList = map.get("dstList");
			}
		}
		// 构造提交综合服务参数格式，一条配置提交一次综合服务
		if (isAudit == 1) {
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
			// 设置APP自定义域
			String userRegion = "APP_ID=" + entity.getAppCode();
			maatCfg.setUserRegion(userRegion);
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(entity.getAuditTime());
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.INSERT_ACTION);
			// 调用服务接口下发配置数据
			String json = gsonToJson(maatBean);
			logger.info("app SSL配置下发配置参数：" + json);
			// 调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
			logger.info("app SSL配置下发响应信息：" + result.getMsg());

		} else if (isAudit == 3) {
			maatCfg.setCompileId(entity.getCompileId());
			maatCfg.setServiceId(entity.getServiceId());
			maatCfg.setIsValid(0);// 无效
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(entity.getAuditTime());
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.UPDATE_ACTION);
			// 调用服务接口取消配置
			String json = gsonToJson(maatBean);
			logger.info("app SSL配置下发配置参数：" + json);
			// 调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json, 1);
			logger.info("app SSL配置取消配置响应信息：" + result.getMsg());
		}
	}

	public void updateAppPolicyCfgValid(Integer isValid, String ids, Integer functionId) {
		String[] idArray = ids.split(",");
		for (String id : idArray) {
			AppPolicyCfg entity = new AppPolicyCfg();
			entity.setCfgId(Long.parseLong(id));
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			entity.setTableName(AppPolicyCfg.getTablename());
			entity.setFunctionId(functionId);
			appCfgDao.updateCfgValid(entity);

			// 查询子配置
			entity = this.getAppPolicyCfg(Long.parseLong(id),entity.getCompileId());
			if (entity.getIpPortList() != null && entity.getIpPortList().size() > 0) {
				IpPortCfg cfg = new IpPortCfg();
				BeanUtils.copyProperties(entity, cfg, new String[] { "cfgId" });
				cfg.setTableName(IpPortCfg.getTablename());
				appCfgDao.updateCfgValid(cfg);
			}
			// 删除关键字
			if (entity.getNtcSubscribeIdCfgList() != null && entity.getNtcSubscribeIdCfgList().size() > 0) {
				NtcSubscribeIdCfg cfg = new NtcSubscribeIdCfg();
				BeanUtils.copyProperties(entity, cfg, new String[] { "cfgId" });
				stringcfgDao.updateSubscribeIdCfg(cfg);
			}
			// 保存区域IP信息
			if (entity.getAreaCfg() != null && entity.getAreaCfg().size() > 0) {
				AreaIpCfg cfg = new AreaIpCfg();
				BeanUtils.copyProperties(entity, cfg, new String[] { "cfgId" });
				cfg.setTableName(AreaIpCfg.getTablename());
				appCfgDao.updateCfgValid(cfg);
			}

		}

	}

	public void updateAppIpCfgValid(Integer isValid, String ids, Integer functionId) {
		String[] idArray = ids.split(",");
		for (String id : idArray) {
			AppIpCfg entity = new AppIpCfg();
			entity.setCfgId(Long.parseLong(id));
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			entity.setTableName(AppIpCfg.getTablename());
			entity.setFunctionId(functionId);
			appCfgDao.updateCfgValid(entity);
			// 保存区域IP信息
			if (entity.getAreaCfg() != null && entity.getAreaCfg().size() > 0) {
				AreaIpCfg cfg = new AreaIpCfg();
				BeanUtils.copyProperties(entity, cfg, new String[] { "cfgId" });
				cfg.setTableName(AreaIpCfg.getTablename());
				appCfgDao.updateCfgValid(cfg);
			}

		}

	}

	public void updateAppTcpCfgValid(Integer isValid, String ids, Integer functionId) {
		String[] idArray = ids.split(",");
		for (String id : idArray) {
			AppTcpCfg entity = new AppTcpCfg();
			entity.setCfgId(Long.parseLong(id));
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			entity.setTableName(AppTcpCfg.getTablename());
			entity.setFunctionId(functionId);
			appCfgDao.updateCfgValid(entity);
			// 保存区域IP信息
			if (entity.getAreaCfg() != null && entity.getAreaCfg().size() > 0) {
				AreaIpCfg cfg = new AreaIpCfg();
				BeanUtils.copyProperties(entity, cfg, new String[] { "cfgId" });
				cfg.setTableName(AreaIpCfg.getTablename());
				appCfgDao.updateCfgValid(cfg);
			}

		}

	}

	public void updateAppHttpCfgValid(Integer isValid, String ids, Integer functionId) {
		String[] idArray = ids.split(",");
		for (String id : idArray) {
			AppHttpCfg entity = new AppHttpCfg();
			entity.setCfgId(Long.parseLong(id));
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			entity.setTableName(AppHttpCfg.getTablename());
			entity.setFunctionId(functionId);
			appCfgDao.updateCfgValid(entity);
			// 保存区域IP信息
			if (entity.getAreaCfg() != null && entity.getAreaCfg().size() > 0) {
				AreaIpCfg cfg = new AreaIpCfg();
				BeanUtils.copyProperties(entity, cfg, new String[] { "cfgId" });
				cfg.setTableName(AreaIpCfg.getTablename());
				appCfgDao.updateCfgValid(cfg);
			}

		}

	}

	public void updateAppDomainCfgValid(Integer isValid, String ids, Integer functionId) {
		String[] idArray = ids.split(",");
		for (String id : idArray) {
			AppDomainCfg entity = new AppDomainCfg();
			entity.setCfgId(Long.parseLong(id));
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			entity.setTableName(AppDomainCfg.getTablename());
			entity.setFunctionId(functionId);
			appCfgDao.updateCfgValid(entity);
			// 保存区域IP信息
			if (entity.getAreaCfg() != null && entity.getAreaCfg().size() > 0) {
				AreaIpCfg cfg = new AreaIpCfg();
				BeanUtils.copyProperties(entity, cfg, new String[] { "cfgId" });
				cfg.setTableName(AreaIpCfg.getTablename());
				appCfgDao.updateCfgValid(cfg);
			}

		}

	}

	// 主题网站配置删除
	public void updateAppTopicDomainCfgValid(Integer isValid, String ids, Integer functionId) {
		String[] idArray = ids.split(",");
		for (String id : idArray) {
			AppTopicDomainCfg entity = new AppTopicDomainCfg();
			entity.setCfgId(Long.parseLong(id));
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			entity.setTableName(AppTopicDomainCfg.getTablename());
			entity.setFunctionId(functionId);
			appCfgDao.updateCfgValid(entity);
			// 保存区域IP信息
			if (entity.getAreaCfg() != null && entity.getAreaCfg().size() > 0) {
				AreaIpCfg cfg = new AreaIpCfg();
				BeanUtils.copyProperties(entity, cfg, new String[] { "cfgId" });
				cfg.setTableName(AreaIpCfg.getTablename());
				appCfgDao.updateCfgValid(cfg);
			}

		}

	}

	public void updateAppByteCfgValid(Integer isValid, String ids, Integer functionId) {
		String[] idArray = ids.split(",");
		for (String id : idArray) {
			AppByteCfg entity = new AppByteCfg();
			entity.setCfgId(Long.parseLong(id));
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			entity.setTableName(AppByteCfg.getTablename());
			entity.setFunctionId(functionId);
			appCfgDao.updateCfgValid(entity);
			// 保存区域IP信息
			if (entity.getAreaCfg() != null && entity.getAreaCfg().size() > 0) {
				AreaIpCfg cfg = new AreaIpCfg();
				BeanUtils.copyProperties(entity, cfg, new String[] { "cfgId" });
				cfg.setTableName(AreaIpCfg.getTablename());
				appCfgDao.updateCfgValid(cfg);
			}

		}

	}

	public void updateAppSslCfgValid(Integer isValid, String ids, Integer functionId) {
		String[] idArray = ids.split(",");
		for (String id : idArray) {
			AppSslCertCfg entity = new AppSslCertCfg();
			entity.setCfgId(Long.parseLong(id));
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			entity.setTableName(AppSslCertCfg.getTablename());
			entity.setFunctionId(functionId);
			appCfgDao.updateCfgValid(entity);
			// 保存区域IP信息
			if (entity.getAreaCfg() != null && entity.getAreaCfg().size() > 0) {
				AreaIpCfg cfg = new AreaIpCfg();
				BeanUtils.copyProperties(entity, cfg, new String[] { "cfgId" });
				cfg.setTableName(AreaIpCfg.getTablename());
				appCfgDao.updateCfgValid(cfg);
			}

		}

	}

	public void updateAppHeaderCfgValid(Integer isValid, String ids, Integer functionId) {
		String[] idArray = ids.split(",");
		for (String id : idArray) {
			AppHeaderCfg entity = new AppHeaderCfg();
			entity.setCfgId(Long.parseLong(id));
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			entity.setTableName(AppHeaderCfg.getTablename());
			entity.setFunctionId(functionId);
			appCfgDao.updateCfgValid(entity);
			// 保存区域IP信息
			if (entity.getAreaCfg() != null && entity.getAreaCfg().size() > 0) {
				AreaIpCfg cfg = new AreaIpCfg();
				BeanUtils.copyProperties(entity, cfg, new String[] { "cfgId" });
				cfg.setTableName(AreaIpCfg.getTablename());
				appCfgDao.updateCfgValid(cfg);
			}

		}

	}

	public Page<AppHeaderCfg> findAppHeaderList(Page<AppHeaderCfg> page, AppHeaderCfg entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(), "r"));
		entity.setPage(page);
		List<AppHeaderCfg> list = appCfgDao.findAppHeaderList(entity);
		page.setList(list);
		return page;
	}

	public AppHeaderCfg getAppHeaderCfg(Long cfgId) {
		return appCfgDao.getAppHeaderCfg(cfgId);
	}

	@Transactional(readOnly = false, rollbackFor = RuntimeException.class)
	public void saveOrUpdateAppHeaderCfg(AppHeaderCfg entity) throws Exception {
		// 设置区域运营商信息
		setAreaEffectiveIds(entity);
		if (entity.getCfgId() == null) {
			Integer compileId = 0;
			try {
				List<Integer> idList = ConfigServiceUtil.getId(1, 1);
				if (idList != null && idList.size() > 0) {
					compileId = idList.get(0);
				}
				entity.setCompileId(compileId);
				entity.setCreateTime(new Date());
				entity.setCreatorId(entity.getCurrentUser().getId());
				entity.setIsValid(0);
				entity.setIsAudit(0);
				appCfgDao.insertAppHeaderCfg(entity);
				// 保存区域IP信息
				if (entity.getAreaCfg() != null) {
					for (AreaIpCfg cfg : entity.getAreaCfg()) {
						cfg.initDefaultValue();
						BeanUtils.copyProperties(entity, cfg, new String[] { "cfgRegionCode", "cfgType" });
						areaIpCfgDao.saveAreaIpCfg(cfg);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				logger.info("获取编译ID出错");
				throw new MaatConvertException(e.getMessage());
			}
		} else {
			entity.setEditorId(entity.getCurrentUser().getId());
			entity.setEditTime(new Date());
			entity.setIsValid(0);
			entity.setIsAudit(0);
			appCfgDao.updateAppHeaderCfg(entity);

			// 删除旧的区域IP，新增新的区域IP
			AreaIpCfg area = new AreaIpCfg();
			area.setCompileId(entity.getCompileId());
			area.setFunctionId(entity.getFunctionId());
			areaIpCfgDao.deleteAreaIpCfg(area);
			entity.setCreateTime(new Date());
			entity.setCreatorId(entity.getCurrentUser().getId());
			// 保存区域IP信息
			if (entity.getAreaCfg() != null) {
				for (AreaIpCfg cfg : entity.getAreaCfg()) {
					cfg.initDefaultValue();
					BeanUtils.copyProperties(entity, cfg, new String[] { "cfgDesc", "cfgRegionCode", "cfgType" });
					areaIpCfgDao.saveAreaIpCfg(cfg);
				}
			}

		}

	}

	public void auditAppHeaderCfg(AppHeaderCfg entity, Integer isAudit) {
		ToMaatBean maatBean = new ToMaatBean();
		MaatCfg maatCfg = new MaatCfg();
		List<MaatCfg> configCompileList = new ArrayList();
		List<GroupCfg> groupRelationList = new ArrayList();
		List<IpCfg> ipRegionList = new ArrayList();
		List<StringCfg> strRegionList = new ArrayList();
		List<NumBoundaryCfg> numRegionList = new ArrayList();
		List<DigestCfg> digestRegionList = new ArrayList();
		List<IpCfg> areaIpRegionList = new ArrayList();
		entity.setTableName(AppHeaderCfg.getTablename());
		appCfgDao.auditCfg(entity);

		if (isAudit == 1) {
			List<AppHeaderCfg> list = new ArrayList();
			list.add(entity);
			Map<String, List> map = cfgConvert(strRegionList, list, 3, entity, groupRelationList);
			groupRelationList = map.get("groupList");
			strRegionList = map.get("dstList");
		}
		// 保存区域IP信息
		List<AreaIpCfg> areaIpCfgList = areaIpCfgDao.getByCompileId(entity.getCompileId());
		if (!StringUtil.isEmpty(areaIpCfgList)) {
			AreaIpCfg cfg = new AreaIpCfg();
			BeanUtils.copyProperties(entity, cfg, new String[] { "cfgId" });
			cfg.setTableName(AreaIpCfg.getTablename());
			appCfgDao.auditCfg(cfg);
			if (isAudit == 1) {
				Map<String, List> map = cfgConvert(areaIpRegionList, areaIpCfgList, 1, entity, groupRelationList);
				groupRelationList = map.get("groupList");
				areaIpRegionList = map.get("dstList");
			}
		}
		// 构造提交综合服务参数格式，一条配置提交一次综合服务
		if (isAudit == 1) {
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
			// 设置APP自定义域
			String userRegion = "APP_ID=" + entity.getAppCode();
			maatCfg.setUserRegion(userRegion);
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(entity.getAuditTime());
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.INSERT_ACTION);
			// 调用服务接口下发配置数据
			String json = gsonToJson(maatBean);
			logger.info("app Header配置下发配置参数：" + json);
			// 调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
			logger.info("app Header配置下发响应信息：" + result.getMsg());

		} else if (isAudit == 3) {
			maatCfg.setCompileId(entity.getCompileId());
			maatCfg.setServiceId(entity.getServiceId());
			maatCfg.setIsValid(0);// 无效
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(entity.getAuditTime());
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.UPDATE_ACTION);
			// 调用服务接口取消配置
			String json = gsonToJson(maatBean);
			logger.info("app Header配置下发配置参数：" + json);
			// 调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json, 1);
			logger.info("app Header配置取消配置响应信息：" + result.getMsg());
		}
	}

	/**
	 * 获取主题跟服务表中的域名
	 * @param websiteDomainTopic
	 * @return
	 */
	public List<WebsiteDomainTopic> getDomainDict(WebsiteDomainTopic websiteDomainTopic) {
		return appCfgDao.getDomainDict(websiteDomainTopic);
	}

	/**
	 * 保存自定义的域名
	 * @param websiteDomainTopic
	 */
	@Transactional(readOnly = false, rollbackFor = RuntimeException.class)
	public void saveDomainDict(WebsiteDomainTopic websiteDomainTopic) {
		
		appCfgDao.saveDomainDict(websiteDomainTopic);
		
	}
	
}
