package com.nis.web.service.configuration;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beust.jcommander.internal.Lists;
import com.nis.domain.Page;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.IpReuseDnatPolicyCfg;
import com.nis.domain.configuration.IpReusePolicyCfg;
import com.nis.domain.maat.ToMaatResult;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.Constants;
import com.nis.util.StringUtils;
import com.nis.web.dao.configuration.IpMultiplexDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.BaseService;
import com.nis.web.service.SpringContextHolder;
import com.nis.web.service.basics.SysDictInfoService;

@Service
public class IpMultiplexService extends BaseService{
	
	@Autowired
	private IpMultiplexDao ipMultiplexDao;

	public Page<IpReusePolicyCfg> findPage(Page<IpReusePolicyCfg> page, IpReusePolicyCfg entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"r"));
		entity.setPage(page);
		List<IpReusePolicyCfg> list=ipMultiplexDao.findPage(entity);
		page.setList(list);
		return page;
	}

	public IpReusePolicyCfg getPolicyCfg(Long cfgId, Integer isValid) {
		List<IpReusePolicyCfg> list=ipMultiplexDao.findList(cfgId,isValid);
		IpReusePolicyCfg policyCfg=null;
		if(list != null && list.size()>0){
			policyCfg=list.get(0);
		}
		return policyCfg;
	}
	
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveOrUpdate(IpReusePolicyCfg entity, HttpServletRequest request, HttpServletResponse response) {
		Date time = new Date();
		entity.setIsValid(0);
		entity.setIsAudit(0);
		setAreaEffectiveIds(entity);
		
		if("VPN".equals(entity.getUserType())){
			entity.setIpType(null);
			entity.setIpPattern(null);
			entity.setSrcIpAddress("");
		}else{
			entity.setUserName(entity.getSrcIpAddress());
		}
		
		// 更新策略信息
		if(entity.getCfgId() == null){
			entity.initDefaultValue();
			entity.setCreateTime(time);
			entity.setCreatorId(UserUtils.getUser().getId());
			//调用服务接口获取compileId
			try {
				List<Integer> compileIds = ConfigServiceUtil.getId(1,1);
				if(compileIds != null && compileIds.size() > 0 ){
					entity.setCompileId(compileIds.get(0));
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("获取编译ID出错");
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+e.getMessage());
			}
			ipMultiplexDao.savePolicyCfg(entity);
			
		}else{
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(time);
			ipMultiplexDao.updatePolicyCfg(entity);
			
		}
		
	}
	
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void delete(Integer isValid, String ids, Integer functionId) {
		String[] idArray = ids.split(",");
		for(String id :idArray){
			IpReusePolicyCfg entity = new IpReusePolicyCfg();
			entity.setCfgId(Long.valueOf(id));
			entity.setFunctionId(functionId);
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			ipMultiplexDao.updatePolicyCfg(entity);
			
		}
		
	}
	
	/**
	 * 策略配置审核
	 * @param ids
	 * @param cfg
	 * @param auditTime 
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void auditSnatPolicy(Integer isAudit, Integer isValid, Integer functionId, String id, Date auditTime) {
		// 更新配置审核状态
		IpReusePolicyCfg cfg = new IpReusePolicyCfg();
		cfg.setCfgId(Long.valueOf(id));
		cfg.setIsValid(isValid);
		cfg.setIsAudit(isAudit);
		cfg.setAuditorId(UserUtils.getUser().getId());
		cfg.setAuditTime(auditTime);
		ipMultiplexDao.updatePolicyAuditStatus(cfg);
		
		SysDictInfoService sysDictInfoService = SpringContextHolder.getBean(SysDictInfoService.class);
		// 获取配置信息
		cfg = this.getPolicyCfg(cfg.getCfgId(), null);
		String json="";
		if(cfg.getIsAudit() == Constants.AUDIT_YES){
			cfg=convertCallBackSnatPolicy(cfg);
			List list = Lists.newArrayList();
			list.add(cfg);
			//调用服务接口下发配置数据
			json=gsonToJson(list);
			logger.info("SNAT策略配置下发参数："+json);
			//调用服务接口下发配置
			try {
				ToMaatResult result = ConfigServiceUtil.postCallbackCfg(json);
				if(result!=null){
					logger.info("SNAT策略配置下发响应信息："+result.getMsg());
				}
			} catch (Exception e) {
				logger.error("SNAT策略配置下发失败",e);
				throw e;
			}
			
		}else if(cfg.getIsAudit() == Constants.AUDIT_NOT_YES){
			cfg=convertCallBackSnatPolicy(cfg);
			List list = Lists.newArrayList();
			list.add(cfg);
			//调用服务接口取消配置
			json=gsonToJson(list);
			logger.info("SNAT策略配置下发参数："+json);
			//调用服务接口取消配置
			try {
				ToMaatResult result = ConfigServiceUtil.put(json, 2);
				logger.info("SNAT策略配置响应信息："+result.getMsg());
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("SNAT策略配置配置失败");
				throw e;
			}
			
		}
	}
	
	/**
	 * 校验地址池是否被策略引用
	 * @param addrPoolId
	 * @return
	 */
	public List<IpReusePolicyCfg> checkAddrPoolIsUsed(String addrPoolId) {
		return ipMultiplexDao.checkAddrPoolIsUsed(addrPoolId);
	}
	
	/**
	 * DNAT策略配置审核
	 * @param isAudit
	 * @param isValid
	 * @param functionId
	 * @param id
	 * @param auditTime
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void auditDnatPolicy(Integer isAudit, Integer isValid, Integer functionId, String id, Date auditTime) {
		// 更新配置审核状态
		IpReuseDnatPolicyCfg cfg = new IpReuseDnatPolicyCfg();
		cfg.setCfgId(Long.valueOf(id));
		cfg.setIsValid(isValid);
		cfg.setIsAudit(isAudit);
		cfg.setAuditorId(UserUtils.getUser().getId());
		cfg.setAuditTime(auditTime);
		ipMultiplexDao.updateDnatPolicyStatus(cfg);
		
		// 获取配置信息
		cfg = this.getDnatPolicyCfg(cfg.getCfgId(), null);
		
		SysDictInfoService sysDictInfoService = SpringContextHolder.getBean(SysDictInfoService.class);
		cfg.setAreaEffectiveIds(cfg.getIsAreaEffective() == 1?sysDictInfoService.setEffectiveRange(cfg.getAreaEffectiveIds()):"0");
		
		String json="";
		if(cfg.getIsAudit() == Constants.AUDIT_YES){
			List list = Lists.newArrayList();
			list.add(cfg);
			json=gsonToJson(list);
			logger.info("DNAT策略配置下发参数："+json);
			//调用服务接口下发配置
			try {
				ToMaatResult result = ConfigServiceUtil.postCallbackCfg(json);
				if(result!=null){
					logger.info("DNAT策略配置下发响应信息："+result.getMsg());
				}
			} catch (Exception e) {
				logger.error("DNAT策略配置下发失败",e);
				throw e;
			}
			
		}else if(cfg.getIsAudit() == Constants.AUDIT_NOT_YES){
			List list = Lists.newArrayList();
			list.add(cfg);
			json=gsonToJson(list);
			logger.info("DNAT策略配置下发参数："+json);
			//调用服务接口取消配置
			try {
				ToMaatResult result = ConfigServiceUtil.put(json, 2);
				logger.info("DNAT策略配置响应信息："+result.getMsg());
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("DNAT策略配置配置失败");
				throw e;
			}
			
		}
	}
	
	/**
	 * DNAT策略新增
	 * @param entity
	 * @param request
	 * @param response
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveOrUpdateDnat(IpReuseDnatPolicyCfg entity, HttpServletRequest request, HttpServletResponse response) {
		Date time = new Date();
		entity.setIsValid(0);
		entity.setIsAudit(0);
		setAreaEffectiveIds(entity);
		
		if(entity.getCfgId() == null){
			entity.initDefaultValue();
			entity.setCreateTime(time);
			entity.setCreatorId(UserUtils.getUser().getId());
			//调用服务接口获取compileId
			try {
				List<Integer> compileIds = ConfigServiceUtil.getId(1,1);
				if(compileIds != null && compileIds.size() > 0 ){
					entity.setCompileId(compileIds.get(0));
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("获取编译ID出错");
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+e.getMessage());
			}
			ipMultiplexDao.saveDnatPolicyCfg(entity);
			
		}else{
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(time);
			ipMultiplexDao.updateDnatPolicyCfg(entity);
			
		}
	}

	public Page<BaseIpCfg> findPageDnat(Page<BaseIpCfg> page, IpReuseDnatPolicyCfg entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"a"));
		entity.setPage(page);
		List<BaseIpCfg> list=ipMultiplexDao.findPageDnat(entity);
		page.setList(list);
		return page;
	}

	public IpReuseDnatPolicyCfg getDnatPolicyCfg(Long cfgId, Integer isValid) {
		List<IpReuseDnatPolicyCfg> list=ipMultiplexDao.findDnatList(cfgId,isValid);
		IpReuseDnatPolicyCfg policyCfg=null;
		if(list != null && list.size()>0){
			policyCfg=list.get(0);
		}
		return policyCfg;
	}
	
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void deleteDnat(Integer isValid, String ids, Integer functionId) {
		String[] idArray = ids.split(",");
		for(String id :idArray){
			IpReuseDnatPolicyCfg entity = new IpReuseDnatPolicyCfg();
			entity.setCfgId(Long.valueOf(id));
			entity.setFunctionId(functionId);
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			ipMultiplexDao.updateDnatPolicyCfg(entity);
			
		}
	}
	
}
