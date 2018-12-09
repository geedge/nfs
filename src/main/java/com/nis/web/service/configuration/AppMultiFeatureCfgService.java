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
import org.springframework.transaction.annotation.Transactional;

import com.nis.domain.Page;
import com.nis.domain.configuration.AppComplexFeatureCfg;
import com.nis.domain.configuration.AppFeatureIndex;
import com.nis.domain.configuration.AppHttpCfg;
import com.nis.domain.configuration.AppIpCfg;
import com.nis.domain.configuration.AppStringFeatureCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.HttpUrlCfg;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.configuration.NtcSubscribeIdCfg;
import com.nis.domain.configuration.P2pHashCfg;
import com.nis.domain.configuration.P2pKeywordCfg;
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
import com.nis.web.dao.configuration.AppMultiFeatureCfgDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.BaseService;

/**
 * 特定协议相关配置事务类
 * 
 * @author dell
 *
 */
@Service
public class AppMultiFeatureCfgService extends BaseService {
	@Autowired
	protected AppMultiFeatureCfgDao appMultiFeatureCfgDao;

	//分页查询
	public Page<AppFeatureIndex> findAppFeatureIndexList(Page<AppFeatureIndex> page, AppFeatureIndex entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(), "r"));
		entity.setPage(page);
		List<AppFeatureIndex> list = appMultiFeatureCfgDao.findAppFeatureIndexList(entity);
		page.setList(list);
		return page;
	}
	public AppFeatureIndex getAppFeatureIndex(Long cfgId) {
		return appMultiFeatureCfgDao.getAppFeatureIndex(cfgId);
	}
	//新增或保存复杂特征配置
	@Transactional(readOnly = false, rollbackFor = RuntimeException.class)
	public void saveOrUpdateAppFeatureCfg(AppFeatureIndex entity) throws Exception {
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
				appMultiFeatureCfgDao.insertAppFeatureIndex(entity);
				if(entity.getIpPortList()!=null){
					for(AppIpCfg cfg:entity.getIpPortList()){
						BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
						appMultiFeatureCfgDao.insertAppIpRangeCfg(cfg);
					}
				}
				if(entity.getStrList()!=null){
					for(AppStringFeatureCfg cfg:entity.getStrList()){
						if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
							BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
							appMultiFeatureCfgDao.insertAppStringFeatureCfg(cfg);
						}
						
					}
				}
				if(entity.getComplexList()!=null){
					for(AppComplexFeatureCfg cfg:entity.getComplexList()){
						if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
							BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
							appMultiFeatureCfgDao.insertAppComplexFeatureCfg(cfg);
						}
						
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
			appMultiFeatureCfgDao.updateAppFeatureIndex(entity);
			appMultiFeatureCfgDao.deleteAppIpRangeCfg(entity);
			appMultiFeatureCfgDao.deleteAppStringFeatureCfg(entity);
			appMultiFeatureCfgDao.deleteAppComplexFeatureCfg(entity);
			entity.setCreateTime(new Date());
			entity.setCreatorId(entity.getCurrentUser().getId());
			if(entity.getIpPortList()!=null){
				for(AppIpCfg cfg:entity.getIpPortList()){
					BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
					appMultiFeatureCfgDao.insertAppIpRangeCfg(cfg);
				}
			}
			if(entity.getStrList()!=null){
				for(AppStringFeatureCfg cfg:entity.getStrList()){
					if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
					BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
					appMultiFeatureCfgDao.insertAppStringFeatureCfg(cfg);
					}
				}
			}
			if(entity.getComplexList()!=null){
				for(AppComplexFeatureCfg cfg:entity.getComplexList()){
					if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
					BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
					appMultiFeatureCfgDao.insertAppComplexFeatureCfg(cfg);
					}
				}
			}
		}
	}
	public void auditAppFeatureCfg(AppFeatureIndex entity, Integer isAudit) {
		ToMaatBean maatBean = new ToMaatBean();
		MaatCfg maatCfg = new MaatCfg();
		List<MaatCfg> configCompileList = new ArrayList();
		List<GroupCfg> groupRelationList = new ArrayList();
		List<IpCfg> ipRegionList = new ArrayList();
		List<StringCfg> strRegionList = new ArrayList();
		List<NumBoundaryCfg> numRegionList = new ArrayList();
		List<DigestCfg> digestRegionList = new ArrayList();
		List<IpCfg> areaIpRegionList = new ArrayList();
		entity.setTableName(AppFeatureIndex.getTablename());
		appMultiFeatureCfgDao.auditCfg(entity);

		/*if (isAudit == 1) {
			List<AppFeatureIndex> list = new ArrayList();
			list.add(entity);
			Map<String, List> map = cfgConvert(strRegionList, list, 2, entity, groupRelationList);
			groupRelationList = map.get("groupList");
			strRegionList = map.get("dstList");
		}*/
		//查询APP_IP_RANGE子配置并修改审核状态
		List ipRangeList = appMultiFeatureCfgDao.getAppIpRangeCfg(entity.getCompileId(),entity.getFunctionId());
		if(ipRangeList!=null && ipRangeList.size()>0){
			AppIpCfg cfg = new AppIpCfg();
			BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
			cfg.setTableName("APP_IP_RANGE_CFG");
			appMultiFeatureCfgDao.auditCfg(cfg);
			if(isAudit==1){
				Map<String,List> map = cfgConvert(ipRegionList,ipRangeList,1,entity,groupRelationList);
				groupRelationList=map.get("groupList");
				ipRegionList=map.get("dstList");
				if(map.get("numRegionList")!=null){
					numRegionList.addAll(map.get("numRegionList"));
				}
			}
		}
		List<AppStringFeatureCfg> strList = appMultiFeatureCfgDao.getAppStringFeatureCfg(entity.getCompileId(),entity.getFunctionId(),null);
		if(strList!=null && strList.size()>0){
			AppStringFeatureCfg cfg = new AppStringFeatureCfg();
			BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
			cfg.setTableName(AppStringFeatureCfg.getTablename());
			appMultiFeatureCfgDao.auditCfg(cfg);
			if(isAudit==1){
				Map<String,List> map = cfgConvert(strRegionList,strList,2,entity,groupRelationList);
				groupRelationList=map.get("groupList");
				strRegionList=map.get("dstList");
			}
		}
		List<AppComplexFeatureCfg> complexList = appMultiFeatureCfgDao.getAppComplexFeatureCfg(entity.getCompileId(),entity.getFunctionId(),null);
		if(complexList!=null && complexList.size()>0){
			AppComplexFeatureCfg cfg = new AppComplexFeatureCfg();
			BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
			cfg.setTableName(AppComplexFeatureCfg.getTablename());
			appMultiFeatureCfgDao.auditCfg(cfg);
			if(isAudit==1){
				Map<String,List> map = cfgConvert(strRegionList,complexList,2,entity,groupRelationList);
				groupRelationList=map.get("groupList");
				strRegionList=map.get("dstList");
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
			//只有用户自定义域1字段为空，才设置maat的自定义域字段信息为APP_ID
			if(StringUtil.isEmpty(entity.getUserRegion1())){
				// 设置APP自定义域
				String userRegion = "APP_ID=" + entity.getAppCode();
				maatCfg.setUserRegion(userRegion);
			}else{
				maatCfg.setUserRegion(entity.getUserRegion1());
			}
			
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(entity.getAuditTime());
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.INSERT_ACTION);
			// 调用服务接口下发配置数据
			String json = gsonToJson(maatBean);
			logger.info("app 特征配置下发配置参数：" + json);
			// 调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
			logger.info("app 特征配置下发响应信息：" + result.getMsg());

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
			logger.info("app 特征配置下发配置参数：" + json);
			// 调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json, 1);
			logger.info("app 特征配置取消配置响应信息：" + result.getMsg());
		}
	}

	public void updateAppFeatureCfgValid(Integer isValid, String ids, Integer functionId) {
		String[] idArray = ids.split(",");
		for (String id : idArray) {
			AppFeatureIndex entity = new AppFeatureIndex();
			entity.setCfgId(Long.parseLong(id));
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			entity.setTableName(AppFeatureIndex.getTablename());
			entity.setFunctionId(functionId);
			appMultiFeatureCfgDao.updateCfgValid(entity);
			//修改IP范围配置表状态
			if (!StringUtil.isEmpty(entity.getIpPortList())) {
				AppIpCfg cfg = new AppIpCfg();
				BeanUtils.copyProperties(entity, cfg, new String[] { "cfgId" });
				cfg.setTableName("APP_IP_RANGE_CFG");
				appMultiFeatureCfgDao.updateCfgValid(cfg);
			}
			//修改字符串类配置表状态
			if (!StringUtil.isEmpty(entity.getStrList())) {
				AppStringFeatureCfg cfg = new AppStringFeatureCfg();
				BeanUtils.copyProperties(entity, cfg, new String[] { "cfgId" });
				cfg.setTableName(AppStringFeatureCfg.getTablename());
				appMultiFeatureCfgDao.updateCfgValid(cfg);
			}
			//修改增强字符串类配置表状态
			if (!StringUtil.isEmpty(entity.getComplexList())) {
				AppComplexFeatureCfg cfg = new AppComplexFeatureCfg();
				BeanUtils.copyProperties(entity, cfg, new String[] { "cfgId" });
				cfg.setTableName(AppComplexFeatureCfg.getTablename());
				appMultiFeatureCfgDao.updateCfgValid(cfg);
			}
		}

	}

	//字符串类特征配置列表
	public List<AppStringFeatureCfg> getAppStringFeatureCfg(Integer compileId,Integer functionId,Integer cfgRegionCode){
		return appMultiFeatureCfgDao.getAppStringFeatureCfg(compileId,functionId,cfgRegionCode);
	}
	//增强字符串类特征配置列表
	public List<AppComplexFeatureCfg> getAppComplexFeatureCfg(Integer compileId,Integer functionId,Integer cfgRegionCode){
		return appMultiFeatureCfgDao.getAppComplexFeatureCfg(compileId,functionId,cfgRegionCode);
	}
	//IP RANGE配置
	public List<AppIpCfg> getAppIpRangeCfg(Integer compileId,Integer functionId){
		return appMultiFeatureCfgDao.getAppIpRangeCfg(compileId,functionId);
	}
	
	public Map<String, List>  exportFeature(AppFeatureIndex entity){
		Map<String, List> dataMap=new HashMap<String, List>();
		List<AppIpCfg> ipRangeList = appMultiFeatureCfgDao.getAppIpRangeCfg(entity.getCompileId(),entity.getFunctionId());
		if(entity.getFunctionId()!=567){
			List<AppComplexFeatureCfg> appKeyList = appMultiFeatureCfgDao.getAppComplexFeatureCfg(entity.getCompileId(),entity.getFunctionId(),null);
			dataMap.put("APP_KEYWORDS", appKeyList);
		}else{
			List<AppStringFeatureCfg> appKeyList = appMultiFeatureCfgDao.getAppStringFeatureCfg(entity.getCompileId(),entity.getFunctionId(),null);
			dataMap.put("APP_DK_GL", appKeyList);
		}
		dataMap.put("APP_IP_RANGE", ipRangeList);
		return dataMap;
	}
	
}
