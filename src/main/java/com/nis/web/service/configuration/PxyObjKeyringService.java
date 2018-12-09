package com.nis.web.service.configuration;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nis.domain.Page;
import com.nis.domain.callback.ProxyObjKeyring;
import com.nis.domain.callback.ProxyObjTrustedCa;
import com.nis.domain.callback.ProxyObjTrustedCaCrl;
import com.nis.domain.configuration.PxyObjKeyring;
import com.nis.domain.configuration.PxyObjTrustedCaCert;
import com.nis.domain.configuration.PxyObjTrustedCaCrl;
import com.nis.domain.maat.ToMaatResult;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.StringUtil;
import com.nis.web.dao.basics.ServiceDictInfoDao;
import com.nis.web.dao.configuration.PxyObjKeyringDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.BaseService;

import oracle.sql.ARRAY;



/**
 * 拦截证书管理
 * @author dell
 *
 */
@Service
public class PxyObjKeyringService extends BaseService{
	@Autowired
	protected PxyObjKeyringDao pxyObjKeyringDao;
	@Autowired
    private ServiceDictInfoDao serviceDictInfoDao;
	
	
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<PxyObjKeyring> findPage(Page<PxyObjKeyring> page, PxyObjKeyring entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"r"));
		entity.setPage(page);
		List<PxyObjKeyring> list=pxyObjKeyringDao.findPage(entity);
		page.setList(list);
		return page;
	}
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<PxyObjTrustedCaCert> findTrustedCertPage(Page<PxyObjTrustedCaCert> page, PxyObjTrustedCaCert entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"r"));
		entity.setPage(page);
		List<PxyObjTrustedCaCert> list=pxyObjKeyringDao.findTrustedCertPage(entity);
		page.setList(list);
		return page;
	}
	public List<PxyObjKeyring> findPxyObjKeyrings(Long cfgId,Integer isValid,Integer isAudit,String cfgType) {
		List<PxyObjKeyring> list=pxyObjKeyringDao.findList(cfgId,isValid,isAudit,cfgType);
		return list;
	}
	
	public PxyObjKeyring getPxyObjKeyring(Long id,Integer isValid) {
		List<PxyObjKeyring> list=pxyObjKeyringDao.findList(id,isValid,null,null);
		PxyObjKeyring dnsResStrategy=null;
		if(list != null && list.size()>0){
			dnsResStrategy=list.get(0);
		}
		return dnsResStrategy;
	}
	public PxyObjTrustedCaCert getPxyObjTrustedCaCert(Long id) {
		return pxyObjKeyringDao.getPxyObjTrustedCaCert(id);
	}
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveOrUpdate(PxyObjKeyring entity){
		Date createTime=new Date();
		setAreaEffectiveIds(entity);
		entity.setIsValid(0);
		entity.setIsAudit(0);
		//新增
		if(StringUtil.isEmpty(entity.getCfgId())){
			entity.initDefaultValue();
			entity.setCreatorId(UserUtils.getUser().getId());
			entity.setCreateTime(createTime);
			//调用服务接口获取compileId
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
			}
			pxyObjKeyringDao.insert(entity);
			//修改
		}else{
			Date editTime=new Date();
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(editTime);
			
			pxyObjKeyringDao.update(entity);
		}
	}
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void trustedCertsaveOrUpdate(PxyObjTrustedCaCert entity){
		Date createTime=new Date();
		setAreaEffectiveIds(entity);
		entity.setIsValid(0);
		entity.setIsAudit(0);
		//新增
		if(StringUtil.isEmpty(entity.getCfgId())){
			entity.initDefaultValue();
			entity.setCreatorId(UserUtils.getUser().getId());
			entity.setCreateTime(createTime);
			//调用服务接口获取compileId
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
			}
			pxyObjKeyringDao.insertPxyObjTrustedCaCert(entity);
			//修改
		}else{
			Date editTime=new Date();
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(editTime);
			pxyObjKeyringDao.updatePxyObjTrustedCaCert(entity);
			//修改主表cert配置时，需要修改子表crl配置信息
			PxyObjTrustedCaCrl crlCfg =new PxyObjTrustedCaCrl();
			BeanUtils.copyProperties(entity, crlCfg, new String[]{"cfgId","serviceId","compileId","cfgType"});
			entity=pxyObjKeyringDao.getPxyObjTrustedCaCert(entity.getCfgId());
			crlCfg.setCertId(entity.getCompileId());
			pxyObjKeyringDao.updatePxyObjTrustedCaCrl(crlCfg);
			
		}
	}
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void trustedCrlsaveOrUpdate(PxyObjTrustedCaCrl entity){
		if(StringUtil.isEmpty(entity.getCertId())){
			entity.setCertId(0);
		}
		Date createTime=new Date();
		Integer oldCrlAudit=0;
		Integer oldCompileId=0;
		PxyObjTrustedCaCrl  oldCrl=pxyObjKeyringDao.getPxyObjTrustedCaCrl(entity);
		
		if(oldCrl != null){
			entity.setCfgId(oldCrl.getCfgId());
			entity.setCompileId(oldCrl.getCompileId());
			oldCrlAudit=oldCrl.getIsAudit();
			oldCompileId=oldCrl.getCompileId();
		}
		
		//新增
		if(StringUtil.isEmpty(entity.getCfgId())){
			entity.initDefaultValue();
			entity.setCreatorId(UserUtils.getUser().getId());
			entity.setCreateTime(createTime);
			//调用服务接口获取compileId
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
			}
			entity.setCreateTime(createTime);
			entity.setCreatorId(UserUtils.getUser().getId());
			//主表为审核通过，则直接修改审核信息
			if(entity.getIsAudit().equals(1)){
				entity.setAuditTime(createTime);
				entity.setAuditorId(UserUtils.getUser().getId());
			}
			pxyObjKeyringDao.insertPxyObjTrustedCaCrl(entity);
		}else{
			Date editTime=new Date();
			//主表为审核通过，则直接修改审核信息
			if(entity.getIsAudit() == 1){
				entity.setAuditTime(editTime);
				entity.setAuditorId(UserUtils.getUser().getId());
			//主表为未审核，直接修改修改人员信息
			}else{
				entity.setEditorId(UserUtils.getUser().getId());
				entity.setEditTime(editTime);
			}
			//如之前crl为生效，则需要重新获取编译id
			if(oldCrlAudit==1){
				//调用服务接口获取compileId
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
				}
			}
			pxyObjKeyringDao.updatePxyObjTrustedCaCrl(entity);
		}
		
		
		//主表cert审核通过，crl配置需下发
		if(entity.getIsAudit() == 1){
			//crl之前为生效状态，需先失效
			if(oldCrlAudit==1){
				String json="";
				List<ProxyObjTrustedCa> resStrategyList=new ArrayList<>();
				resStrategyList=convertCallBackProxyObjTrustedCa(null,entity);
				if( !StringUtil.isEmpty(resStrategyList)){
					resStrategyList.get(0).setCrlId(oldCompileId);
					resStrategyList.get(0).setId(Long.valueOf(oldCompileId));
					resStrategyList.get(0).setCfgId(oldCompileId);
					resStrategyList.get(0).setIsValid(0);
				}
				//调用服务接口取消配置
				json=gsonToJson(resStrategyList);
				logger.info("crl配置参数："+json);
				//调用服务接口取消配置
				try {
					ToMaatResult result = ConfigServiceUtil.put(json, 2);
					logger.info("crl配置响应信息："+result.getMsg());
				} catch (Exception e) {
					e.printStackTrace();
					logger.info("crl配置失败");
					throw e;
				}
			}
			
			String json="";
			List<ProxyObjTrustedCa> ProxyObjTrustedCaList=new ArrayList<ProxyObjTrustedCa>();
			ProxyObjTrustedCaList=convertCallBackProxyObjTrustedCa(null,entity);
			//调用服务接口下发配置数据
			json=gsonToJson(ProxyObjTrustedCaList);
			logger.info("crl配置参数："+json);
			//调用服务接口下发配置
			try {
				ToMaatResult result = ConfigServiceUtil.postCallbackCfg(json);
				if(result!=null){
					logger.info("crl配置下发响应信息："+result.getMsg());
				}
			} catch (Exception e) {
				logger.error("crl配置下发失败",e);
				throw e;
			}
			
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
		String[] idArray = ids.split(",");
		for(String id :idArray){
			PxyObjKeyring entity = new PxyObjKeyring();
			entity.setCfgId(Long.valueOf(id));
			entity.setFunctionId(functionId);
			entity.setIsAudit(isAudit);
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			pxyObjKeyringDao.update(entity);
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
		PxyObjKeyring cfg=new PxyObjKeyring();
		cfg.setCfgId(Long.valueOf(id));
		cfg.setIsValid(isValid);
		cfg.setIsAudit(isAudit);
		cfg.setEditTime(auditTime);
		cfg.setEditorId(UserUtils.getUser().getId());
		cfg.setAuditorId(UserUtils.getUser().getId());
		cfg.setAuditTime(auditTime);
		pxyObjKeyringDao.update(cfg);
		cfg=getPxyObjKeyring(cfg.getCfgId(), null);
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
			ProxyObjKeyring ntcPxyObjKeyring=convertCallBackProxyObjKeyring(cfg);
			resStrategyList.add(ntcPxyObjKeyring);
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
		}
	}
	/**
	 * 
	 * @param isAudit
	 * @param isValid
	 * @param ids compileIds
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void trustedCertDelete(Integer isAudit,Integer isValid,String ids,Integer functionId){
		String[] idArray = ids.split(",");
		for(String id :idArray){
			PxyObjTrustedCaCert entity = new PxyObjTrustedCaCert();
			entity.setCfgId(Long.valueOf(id));
			entity.setFunctionId(functionId);
			entity.setIsAudit(isAudit);
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			pxyObjKeyringDao.updatePxyObjTrustedCaCert(entity);
			//主表cert配置删除后，需删除crl子表配置
			PxyObjTrustedCaCrl crlCfg =new PxyObjTrustedCaCrl();
			BeanUtils.copyProperties(entity, crlCfg, new String[]{"cfgId","serviceId","compileId","cfgType"});
			entity=pxyObjKeyringDao.getPxyObjTrustedCaCert(entity.getCfgId());
			crlCfg.setCertId(entity.getCompileId());
			pxyObjKeyringDao.updatePxyObjTrustedCaCrl(crlCfg);
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
	public void trustedCertAudit(Integer isAudit,Integer isValid,Integer functionId,String id,Date auditTime){
		PxyObjTrustedCaCrl crlCfg=new PxyObjTrustedCaCrl();
		PxyObjTrustedCaCert cfg=new PxyObjTrustedCaCert();
		cfg.setCfgId(Long.valueOf(id));
		cfg.setIsValid(isValid);
		cfg.setIsAudit(isAudit);
		cfg.setEditTime(auditTime);
		cfg.setEditorId(UserUtils.getUser().getId());
		cfg.setAuditorId(UserUtils.getUser().getId());
		cfg.setAuditTime(auditTime);
		//修改主表cert 配置状态
		pxyObjKeyringDao.updatePxyObjTrustedCaCert(cfg);
		cfg=getPxyObjTrustedCaCert(cfg.getCfgId());
		//修改子表crl 配置状态
		BeanUtils.copyProperties(cfg, crlCfg, new String[]{"cfgId","serviceId","compileId","cfgType"});
		crlCfg.setCertId(cfg.getCompileId());
		pxyObjKeyringDao.updatePxyObjTrustedCaCrl(crlCfg);
		crlCfg=pxyObjKeyringDao.getPxyObjTrustedCaCrl(crlCfg);
		
		//失效配置，将子表的失效来函设置与主表相同
		if(cfg.getIsAudit()==3 && crlCfg != null){
			//设置配置取消的来函信息
			serviceDictInfoDao.auditCancleRequestInfo(cfg.getCancelRequestId(), 
					"pxy_obj_trusted_ca_crl",
					crlCfg.getCfgId().toString());
		}
		
		String json="";
		if(cfg.getIsAudit()==1){
			List<ProxyObjTrustedCa> trustedCertList=new ArrayList<ProxyObjTrustedCa>();
			//可信证书cert回调配置转换
			trustedCertList=convertCallBackProxyObjTrustedCa(cfg,crlCfg);
			//调用服务接口下发配置数据
			json=gsonToJson(trustedCertList);
			logger.info("可信证书（cert+crl）配置下发配置参数："+json);
			//调用服务接口下发配置
			try {
				ToMaatResult result = ConfigServiceUtil.postCallbackCfg(json);
				if(result!=null){
					logger.info("可信证书（cert+crl）配置下发响应信息："+result.getMsg());
				}
			} catch (Exception e) {
				logger.error("可信证书（cert+crl）配置下发失败",e);
				throw e;
			}
		}else if(cfg.getIsAudit()==3){
			List<ProxyObjTrustedCa> trustedCertList=new ArrayList<ProxyObjTrustedCa>();
			//可信证书cert回调配置转换
			trustedCertList=convertCallBackProxyObjTrustedCa(cfg,crlCfg);
			//调用服务接口取消配置
			json=gsonToJson(trustedCertList);
			logger.info("可信证书（cert+crl）配置参数："+json);
			//调用服务接口取消配置
			try {
				ToMaatResult result = ConfigServiceUtil.put(json, 2);
				logger.info("可信证书配置响应信息："+result.getMsg());
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("可信证书配置失败");
				throw e;
			}
		}
	}
}
	
	