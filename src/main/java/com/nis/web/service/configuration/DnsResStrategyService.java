package com.nis.web.service.configuration;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nis.domain.Page;
import com.nis.domain.callback.NtcDnsResStrategy;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.DnsResStrategy;
import com.nis.domain.maat.ToMaatResult;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.StringUtil;
import com.nis.web.dao.configuration.DnsResStrategyDao;
import com.nis.web.dao.configuration.StringCfgDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.BaseService;
import com.nis.web.service.SpringContextHolder;



/**
 * 音视频文本
 * @author dell
 *
 */
@Service
public class DnsResStrategyService extends BaseService{
	@Autowired
	protected DnsResStrategyDao dnsResStrategyDao;
	
	
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DnsResStrategy> findPage(Page<DnsResStrategy> page, DnsResStrategy entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"r"));
		entity.setPage(page);
		List<DnsResStrategy> list=dnsResStrategyDao.findPage(entity);
		page.setList(list);
		return page;
	}
	public List<DnsResStrategy> findDnsResStrategys(Long cfgId,Integer isValid,Integer isAudit) {
		List<DnsResStrategy> list=dnsResStrategyDao.findList(cfgId,isValid,isAudit);
		return list;
	}
	public List<DnsResStrategy> findhasValidIpResStrategys(Long cfgId,Integer isValid,Integer isAudit) {
		List<DnsResStrategy> list=dnsResStrategyDao.findhasValidIpList(cfgId,isValid,isAudit);
		return list;
	}
	
	public DnsResStrategy getDnsResStrategy(Long id,Integer isValid) {
		List<DnsResStrategy> list=dnsResStrategyDao.findList(id,isValid,null);
		DnsResStrategy dnsResStrategy=null;
		if(list != null && list.size()>0){
			dnsResStrategy=list.get(0);
		}
		return dnsResStrategy;
	}
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveDnsResStrategies(List<DnsResStrategy> cfgs) {
		for(DnsResStrategy cfg: cfgs) {
			saveOrUpdate(cfg);
		}
	}
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveOrUpdate(DnsResStrategy entity){
		Date createTime=new Date();
		setAreaEffectiveIds(entity);
		List<DnsResStrategy> list = new ArrayList<DnsResStrategy>();
		if( entity.getCfgId()!=null && !"".equals(entity.getCfgId())){
			list=dnsResStrategyDao.findList(entity.getCfgId(),null,null);
		}
		//新增
		if(list==null || list.size()==0){
			entity.initDefaultValue();
			entity.setCreatorId(UserUtils.getUser().getId());
			entity.setCreateTime(createTime);
			entity.setIsValid(0);
			entity.setIsAudit(0);
			if(entity.getCompileId()==null||entity.getCompileId().intValue()==0) {
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
			dnsResStrategyDao.insert(entity);
			//修改
		}else{
			Date editTime=new Date();
			entity.setIsValid(0);
			entity.setIsAudit(0);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(editTime);
			
			dnsResStrategyDao.update(entity);
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
			DnsResStrategy entity = new DnsResStrategy();
			entity.setCfgId(Long.valueOf(id));
			entity.setFunctionId(functionId);
			entity.setIsAudit(isAudit);
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			dnsResStrategyDao.update(entity);
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
		DnsResStrategy cfg=new DnsResStrategy();
		cfg.setCfgId(Long.valueOf(id));
		cfg.setIsValid(isValid);
		cfg.setIsAudit(isAudit);
		cfg.setEditTime(auditTime);
		cfg.setEditorId(UserUtils.getUser().getId());
		cfg.setAuditorId(UserUtils.getUser().getId());
		cfg.setAuditTime(auditTime);
		dnsResStrategyDao.update(cfg);
		cfg=getDnsResStrategy(cfg.getCfgId(), null);
		String json="";
		if(cfg.getIsAudit()==1){
			List<NtcDnsResStrategy> resStrategyList=new ArrayList<NtcDnsResStrategy>();
			NtcDnsResStrategy resStrategy=convertCallBackDnsResStrategy(cfg);
			resStrategyList.add(resStrategy);
			//调用服务接口下发配置数据
			json=gsonToJson(resStrategyList);
			logger.info("DNS响应策略配置下发配置参数："+json);
			//调用服务接口下发配置
			try {
				ToMaatResult result = ConfigServiceUtil.postCallbackCfg(json);
				if(result!=null){
					logger.info("DNS响应策略配置下发响应信息："+result.getMsg());
				}
			} catch (Exception e) {
				logger.error("DNS响应策略配置配置下发失败",e);
				throw e;
			}
		}else if(cfg.getIsAudit()==3){
			List<NtcDnsResStrategy> resStrategyList=new ArrayList<>();
			NtcDnsResStrategy ntcDnsResStrategy=convertCallBackDnsResStrategy(cfg);
			resStrategyList.add(ntcDnsResStrategy);
			//调用服务接口取消配置
			json=gsonToJson(resStrategyList);
			logger.info("DNS响应策略配置配置参数："+json);
			//调用服务接口取消配置
			try {
				ToMaatResult result = ConfigServiceUtil.put(json, 2);
				logger.info("DNS响应策略配置响应信息："+result.getMsg());
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("DNS响应策略配置配置失败");
				throw e;
			}
		}
	}
	
}
	
	