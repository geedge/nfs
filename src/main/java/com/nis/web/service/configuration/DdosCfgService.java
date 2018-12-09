package com.nis.web.service.configuration;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beust.jcommander.internal.Lists;
import com.google.gson.Gson;
import com.nis.domain.Page;
import com.nis.domain.callback.InlineIp;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.DdosIpCfg;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.maat.MaatCfg;
import com.nis.domain.maat.ToMaatBean;
import com.nis.domain.maat.ToMaatResult;
import com.nis.domain.maat.MaatCfg.DigestCfg;
import com.nis.domain.maat.MaatCfg.GroupCfg;
import com.nis.domain.maat.MaatCfg.IpCfg;
import com.nis.domain.maat.MaatCfg.NumBoundaryCfg;
import com.nis.domain.maat.MaatCfg.StringCfg;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.Constants;
import com.nis.util.StringUtil;
import com.nis.web.dao.configuration.DdosCfgDao;
import com.nis.web.dao.configuration.IpCfgDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.BaseService;
import com.nis.web.service.SpringContextHolder;



/**
 * ddos配置业务
 * @author dell
 *
 */
@Service
public class DdosCfgService extends BaseService{
	@Autowired
	protected DdosCfgDao ddosCfgDao;
	
	
	public Page<DdosIpCfg> findPage(Page page, DdosIpCfg entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"r"));
		entity.setPage(page);
		List<DdosIpCfg> list=ddosCfgDao.findPage(entity);
		page.setList(list);
		return page;
	}
	
	public DdosIpCfg getDdosIpCfg(Long cfgId,Integer compileId) {
		return ddosCfgDao.getDdosIpCfg(cfgId,compileId);
	}
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveOrUpdate(DdosIpCfg entity){
		Date createTime=new Date();
		//设置区域运营商信息
		setAreaEffectiveIds(entity);
		String antiddosProtocol = entity.getAntiddosProtocol();
		Long bpsThreadshold = entity.getBpsThreadshold();
		Long ppsThreadshold = entity.getPpsThreadshold();
		if(StringUtil.isBlank(antiddosProtocol)) {
			entity.setAntiddosProtocol("");
			entity.setBpsThreadshold(0l);
			entity.setBpsThreadshold(0l);
		}
		if(bpsThreadshold==null) {
			entity.setBpsThreadshold(0l);
		}
		if(ppsThreadshold==null) {
			entity.setBpsThreadshold(0l);
		}
		//新增
		if(entity.getCfgId()==null){
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
					throw new MaatConvertException(e.getMessage());
				}
				if(compileIds != null && compileIds.size() >0  && compileIds.get(0) != 0){
					entity.setCompileId(compileIds.get(0));
				}else{
					throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>");
				}
			}
			if(entity.getCompileId()!=null&&entity.getCompileId().intValue()>0) {
				ddosCfgDao.insert(entity);
			}else {
				throw new RuntimeException("Could not get compileId!");
			}
			
			//修改
		}else{
			Date editTime=new Date();
			entity.setIsValid(0);
			entity.setIsAudit(0);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(editTime);
			
			ddosCfgDao.update(entity);
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
			DdosIpCfg entity = new DdosIpCfg();
			entity.setCfgId(Long.valueOf(id));
			entity.setFunctionId(functionId);
			entity.setIsAudit(isAudit);
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			ddosCfgDao.update(entity);
		}
	}
	/**
	 * 
	 * @param isAudit
	 * @param isValid
	 * @param ids cfgId
	 * @param functionId
	 */
	/*	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void audit(Integer isAudit,Integer isValid,Integer functionId,String id,Date auditTime){
		DdosIpCfg entity = new DdosIpCfg();
		
		entity=ddosCfgDao.getDdosIpCfg(Long.valueOf(id));
		List<DdosIpCfg> list = new ArrayList();
		
		entity.setCfgId(Long.valueOf(id));
		entity.setIsAudit(isAudit);
		entity.setIsValid(isValid);
		entity.setAuditorId(UserUtils.getUser().getId());
		entity.setAuditTime(auditTime);
		
		ddosCfgDao.update(entity);
		
		String antiddosProtocol = entity.getAntiddosProtocol();
		Long bpsThreadshold = entity.getBpsThreadshold();
		Long ppsThreadshold = entity.getPpsThreadshold();
		Map map= new HashMap();
		if(StringUtil.isBlank(antiddosProtocol)) {
			antiddosProtocol="";
		}
		if(bpsThreadshold==null) {
			bpsThreadshold=0l;
		}
		if(ppsThreadshold==null) {
			ppsThreadshold=0l;
		}
		map.put("protocol", antiddosProtocol);
		map.put("bps_threadshold", bpsThreadshold);
		map.put("pps_threadshold", ppsThreadshold);
		
		String json="";
		if(entity.getIsAudit()==1){
			List<InlineIp> ipList=new ArrayList<InlineIp>();
			InlineIp resStrategy=convertCallBackIp(entity,null);
			resStrategy.setUserRegion(new Gson().toJson(map));
			ipList.add(resStrategy);
			//调用服务接口下发配置数据
			json=gsonToJson(ipList);
			logger.info("DDOS IP配置下发配置参数："+json);
			//调用服务接口下发配置
			try {
				ToMaatResult result = ConfigServiceUtil.postCallbackCfg(json);
				if(result!=null){
					logger.info("DDOS IP配置下发响应信息："+result.getMsg());
				}
			} catch (Exception e) {
				logger.error("DDOS IP配置下发失败",e);
				throw e;
			}
		}else if(entity.getIsAudit()==3){
			List<InlineIp> resStrategyList=new ArrayList<>();
			InlineIp resStrategy=convertCallBackIp(entity,entity.getDnsStrategyId());
			resStrategy.setUserRegion(new Gson().toJson(map));
			resStrategyList.add(resStrategy);
			//调用服务接口取消配置
			json=gsonToJson(resStrategyList);
			logger.info("DDOS IP配置参数："+json);
			//调用服务接口取消配置
			try {
				ToMaatResult result = ConfigServiceUtil.put(json, 2);
				logger.info("DDOS IP配置响应信息："+result.getMsg());
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("DDOS IP配置配置失败");
				throw e;
			}
		}
		
	}*/
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void audit(Integer isAudit,Integer isValid,Integer functionId,String id,Date auditTime) throws Exception{
		DdosIpCfg entity = new DdosIpCfg();
		
		entity=ddosCfgDao.getDdosIpCfg(Long.valueOf(id),entity.getCompileId());
		List<DdosIpCfg> list = new ArrayList();
		
		entity.setCfgId(Long.valueOf(id));
		entity.setIsAudit(isAudit);
		entity.setIsValid(isValid);
		entity.setAuditorId(UserUtils.getUser().getId());
		entity.setAuditTime(auditTime);
		
		ddosCfgDao.update(entity);
		
		
		String antiddosProtocol = entity.getAntiddosProtocol();
		Long bpsThreadshold = entity.getBpsThreadshold();
		Long ppsThreadshold = entity.getPpsThreadshold();
		Map umap= new HashMap();
		if(StringUtil.isBlank(antiddosProtocol)) {
			antiddosProtocol="";
			bpsThreadshold=0l;
			bpsThreadshold=0l;
		}
		if(bpsThreadshold==null) {
			bpsThreadshold=0l;
		}
		if(ppsThreadshold==null) {
			bpsThreadshold=0l;
		}
		umap.put("protocol", antiddosProtocol);
		umap.put("bps_threadshold", bpsThreadshold);
		umap.put("pps_threadshold", ppsThreadshold);
		String userRegion = new Gson().toJson(umap);
		
		ToMaatBean maatBean = new ToMaatBean();		
		MaatCfg maatCfg = new MaatCfg();
		List<MaatCfg> configCompileList = new ArrayList();
		List<GroupCfg> groupRelationList = new ArrayList();
		List<IpCfg> ipRegionList = new ArrayList();
		List<StringCfg> strRegionList = new ArrayList();
		List<NumBoundaryCfg> numRegionList = new ArrayList();
		List<DigestCfg> digestRegionList = new ArrayList();
		List<IpCfg> areaIpRegionList = new ArrayList();		
		
		
		entity=this.getDdosIpCfg(entity.getCfgId(),entity.getCompileId());
		
		if(entity.getIsAudit()==1){
			List<DdosIpCfg> ipList=new ArrayList<DdosIpCfg>();
			ipList.add(entity);
			if(!StringUtil.isEmpty(ipList)){
				Map<String,List> ipMap = cfgConvert(ipRegionList,ipList,1,entity,groupRelationList);
				groupRelationList=ipMap.get("groupList");
				ipRegionList=ipMap.get("dstList");
				if(ipMap.get("numRegionList")!=null){
					numRegionList.addAll(ipMap.get("numRegionList"));

				}
			}
			
			//调用服务接口下发配置数据
			maatCfg.initDefaultValue();
			BeanUtils.copyProperties(entity, maatCfg);
			maatCfg.setAction(entity.getAction());
			maatCfg.setAuditTime(auditTime);
			maatCfg.setIpRegionList(ipRegionList);
			maatCfg.setStrRegionList(strRegionList);
			maatCfg.setNumRegionList(numRegionList);
			maatCfg.setDigestRegionList(digestRegionList);
			maatCfg.setGroupRelationList(groupRelationList);
			maatCfg.setGroupNum(groupRelationList.size());
			maatCfg.setAreaIpRegionList(areaIpRegionList);
			maatCfg.setIsValid(entity.getIsValid());
			maatCfg.setUserRegion(userRegion);// 自定义域
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(auditTime);
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.INSERT_ACTION);
			//调用服务接口下发配置数据
			String json=gsonToJson(maatBean);
			logger.info("ddos 配置下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
			logger.info("ddos 配置下发响应信息："+result.getMsg());
			
		}else if(entity.getIsAudit()==3){
			maatCfg.setCompileId(entity.getCompileId());
			maatCfg.setServiceId(entity.getServiceId());
			maatCfg.setIsValid(0);//无效
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(auditTime);
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.UPDATE_ACTION);
			//调用服务接口取消配置
			String json=gsonToJson(maatBean);
			logger.info("ddos 配置下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json,1);
			logger.info("ddos 配置取消配置响应信息："+result.getMsg());
		}
		
	}
}
