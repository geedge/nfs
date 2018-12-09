package com.nis.web.service.configuration;


import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beust.jcommander.internal.Lists;
import com.nis.domain.Page;
import com.nis.domain.callback.ProxyFileStrategyCfg;
import com.nis.domain.maat.ToMaatResult;
import com.nis.util.ConfigServiceUtil;
import com.nis.web.dao.configuration.ProxyFileStrategyDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.BaseService;

import jersey.repackaged.com.google.common.collect.Maps;


@Service
public class ProxyFileStrategyService extends BaseService{
	@Autowired
	protected ProxyFileStrategyDao proxyFileDao;
	
	
	/**
	 * 分页查询
	 * @param page
	 * @param entity
	 * @return
	 */
	public Page<ProxyFileStrategyCfg> findPage(Page page, ProxyFileStrategyCfg entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"a"));
		entity.setPage(page);
		List<ProxyFileStrategyCfg> list=proxyFileDao.findPage(entity);
		page.setList(list);
		return page;
	}
	
	public ProxyFileStrategyCfg getCfgById(Long cfgId) {
		return proxyFileDao.getCfgById(cfgId);
	}
	
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveOrUpdate(ProxyFileStrategyCfg entity){
		Date createTime=new Date();
		//设置区域运营商信息
		setAreaEffectiveIds(entity);
		//新增
		if(entity.getCfgId()==null){
			Integer compileId = ConfigServiceUtil.getId(1, 1).get(0);//获取编译id
			entity.setCompileId(compileId);
			entity.setCreatorId(UserUtils.getUser().getId());
			entity.setCreateTime(createTime);
			entity.setIsValid(0);
			entity.setIsAudit(0);
			proxyFileDao.insert(entity);//新增
		}else{
			Date editTime=new Date();
			entity.setIsValid(0);
			entity.setIsAudit(0);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(editTime);
			proxyFileDao.update(entity);//更新
		}
	}
	
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void update(Integer isAudit,Integer isValid,String ids,Integer functionId){
		ProxyFileStrategyCfg entity = new ProxyFileStrategyCfg();
		String[] idArray = ids.split(",");
		for(String id :idArray){
			entity.setCfgId(Long.parseLong(id));
			entity.setFunctionId(functionId);
			entity.setIsAudit(isAudit);
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			proxyFileDao.update(entity);
		}
	}
	
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void audit(Integer isAudit,Integer isValid,Integer functionId,String ids){
		Date auditTime = new Date();//审核时间
		String[] idArray = ids.split(",");
		for(String id : idArray) {
			ProxyFileStrategyCfg entity = proxyFileDao.getCfgById(Long.parseLong(id));
			entity.setIsAudit(isAudit);
			entity.setIsValid(isValid);
			entity.setAuditorId(UserUtils.getUser().getId());
			entity.setAuditTime(auditTime);
			proxyFileDao.update(entity);
			if(isAudit == 1) {//审核通过，下发配置回调配置信息
				Map<String,Object> params = Maps.newHashMap();
				params.put("fileId", entity.getCompileId());//文件ID
				params.put("service", entity.getServiceId());//业务ID
				params.put("fileDesc", entity.getFileDesc());//文件描述
				params.put("contentType", entity.getContentType());//内容类型
				params.put("contentLength", entity.getContentLength());//文件长度
				params.put("filePath", entity.getUrl());//文件路径
				params.put("isValid", 1);//有效标志，有效
				params.put("opTime", new Date());
				List list = Lists.newArrayList();
				list.add(params);
				String json = gsonToJson(list);
				logger.debug("params:" + json);
				ToMaatResult result = ConfigServiceUtil.postCallbackCfg(json);
				logger.debug("响应："+gsonToJson(result));
			}else if(isAudit == 3) {//取消审核通过，将回调配置信息置为无效
				Map<String,Object> params = Maps.newHashMap();
				params.put("fileId", entity.getCompileId());//文件ID
				params.put("service", entity.getServiceId());//业务ID
				params.put("isValid", 0);//有效标志，无效
				params.put("opTime", new Date());
				List list = Lists.newArrayList();
				list.add(params);
				String json = gsonToJson(list);
				logger.debug("params:" + json);
				ToMaatResult result = ConfigServiceUtil.put(json,2);
				logger.debug("响应："+gsonToJson(result));
			}
		}
		
	}
	
	/**
	 * 获取所有的 响应文件 策略
	 * @return
	 */
	public List<ProxyFileStrategyCfg> getProxyFileStrategyCfgList(ProxyFileStrategyCfg entity){
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"a"));
		List<ProxyFileStrategyCfg> list=proxyFileDao.findPage(entity);
		return list;
	}
	
}
