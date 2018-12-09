package com.nis.web.service.configuration;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nis.domain.Page;
import com.nis.domain.configuration.AppBuiltInFeatureFile;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.StringUtil;
import com.nis.web.dao.configuration.AppBuiltInFeatureDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.BaseService;



/**
 * 内置文件
 * @author dell
 *
 */
@Service
public class AppBuiltInFeatureService extends BaseService{
	@Autowired
	protected AppBuiltInFeatureDao appBuiltInFeatureDao;
	
	
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<AppBuiltInFeatureFile> findPage(Page<AppBuiltInFeatureFile> page, AppBuiltInFeatureFile entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"r"));
		entity.setPage(page);
		List<AppBuiltInFeatureFile> list=appBuiltInFeatureDao.findPage(entity);
		page.setList(list);
		return page;
	}
	public List<AppBuiltInFeatureFile> findAppBuiltInFeatureFiles(Long cfgId,Integer isValid,Integer isAudit,String cfgType) {
		List<AppBuiltInFeatureFile> list=appBuiltInFeatureDao.findList(cfgId,isValid,isAudit);
		return list;
	}
	
	public AppBuiltInFeatureFile getAppBuiltInFeatureFile(Long id,Integer isValid) {
		List<AppBuiltInFeatureFile> list=appBuiltInFeatureDao.findList(id,isValid,null);
		AppBuiltInFeatureFile dnsResStrategy=null;
		if(list != null && list.size()>0){
			dnsResStrategy=list.get(0);
		}
		return dnsResStrategy;
	}
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveOrUpdate(AppBuiltInFeatureFile entity){
		Date createTime=new Date();
		setAreaEffectiveIds(entity);
		entity.setIsValid(1);
		entity.setIsAudit(1);
		//新增
		if(StringUtil.isEmpty(entity.getCfgId())){
			entity.initDefaultValue();
			entity.setCreatorId(UserUtils.getUser().getId());
			entity.setCreateTime(createTime);
			/*//调用服务接口获取compileId
			List<Integer> compileIds = new ArrayList<Integer>();
			try {
				compileIds = ConfigServiceUtil.getId(1,1);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("获取编译ID出错");
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+e.getMessage());
			}
			if(compileIds != null && compileIds.size() >0  && compileIds.get(0) != 0){
				entity.setCompileId(compileIds.get(0));
			}*/
			appBuiltInFeatureDao.insert(entity);
			//修改
		}else{
			Date editTime=new Date();
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(editTime);
			
			appBuiltInFeatureDao.update(entity);
		}
	}
	
	/**
	 * 
	 * @param isAudit
	 * @param isValid
	 * @param ids compileIds
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void delete(Integer isAudit,Integer isValid,String ids,Integer functionId){
		AppBuiltInFeatureFile entity = new AppBuiltInFeatureFile();
		String[] idArray = ids.split(",");
		for(String id :idArray){
			entity.setCfgId(Long.valueOf(id));
			entity.setFunctionId(functionId);
			entity.setIsAudit(isAudit);
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			appBuiltInFeatureDao.update(entity);
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
	public void audit(Integer isAudit,Integer isValid,Integer functionId,String id,Date auditTime){
		AppBuiltInFeatureFile cfg=new AppBuiltInFeatureFile();
		cfg.setCfgId(Long.valueOf(id));
		cfg.setIsValid(isValid);
		cfg.setIsAudit(isAudit);
		cfg.setEditTime(auditTime);
		cfg.setEditorId(UserUtils.getUser().getId());
		cfg.setAuditorId(UserUtils.getUser().getId());
		cfg.setAuditTime(auditTime);
		appBuiltInFeatureDao.update(cfg);
		/*cfg=getAppBuiltInFeatureFile(cfg.getCfgId(), null);
		String json="";
		if(cfg.getIsAudit()==1){
			List<ProxyObjKeyring> resStrategyList=new ArrayList<ProxyObjKeyring>();
			ProxyObjKeyring resStrategy=convertCallBackProxyObjKeyring(cfg);
			resStrategyList.add(resStrategy);
			//调用服务接口下发配置数据
			json=gsonToJson(resStrategyList);
			logger.info("拦截策略配置下发配置参数："+json);
			//调用服务接口下发配置
			try {
				ToMaatResult result = ConfigServiceUtil.postCallbackCfg(json);
				if(result!=null){
					logger.info("拦截策略配置下发响应信息："+result.getMsg());
				}
			} catch (Exception e) {
				logger.error("拦截策略配置配置下发失败",e);
				throw e;
			}
		}else if(cfg.getIsAudit()==3){
			List<ProxyObjKeyring> resStrategyList=new ArrayList<>();
			ProxyObjKeyring ntcAppBuiltInFeatureFile=convertCallBackProxyObjKeyring(cfg);
			resStrategyList.add(ntcAppBuiltInFeatureFile);
			//调用服务接口取消配置
			json=gsonToJson(resStrategyList);
			logger.info("拦截策略配置配置参数："+json);
			//调用服务接口取消配置
			try {
				ToMaatResult result = ConfigServiceUtil.put(json, 2);
				logger.info("拦截策略配置响应信息："+result.getMsg());
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("拦截策略配置配置失败");
				throw e;
			}
		}*/
	}
}
	
	