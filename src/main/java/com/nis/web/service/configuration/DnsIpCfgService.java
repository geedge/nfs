package com.nis.web.service.configuration;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beust.jcommander.internal.Lists;
import com.nis.domain.Page;
import com.nis.domain.callback.InlineIp;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.DnsIpCfg;
import com.nis.domain.maat.ToMaatResult;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.ConfigServiceUtil;
import com.nis.web.dao.configuration.DnsIpCfgDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.BaseService;
import com.nis.web.service.SpringContextHolder;



/**
 * 音视频文本
 * @author dell
 *
 */
@Service
public class DnsIpCfgService extends BaseService{
	@Autowired
	protected DnsIpCfgDao dnsIpCfgDao;
	
	
	public Page<DnsIpCfg> findPage(Page page, DnsIpCfg entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"r"));
		entity.setPage(page);
		List<DnsIpCfg> list=dnsIpCfgDao.findPage(entity);
		page.setList(list);
		return page;
	}
	
	public DnsIpCfg getDnsIpCfg(Long cfgId) {
		return dnsIpCfgDao.getDnsIpCfg(cfgId);
	}
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveOrUpdate(DnsIpCfg entity){
		Date createTime=new Date();
		//设置区域运营商信息
		setAreaEffectiveIds(entity);
		//新增
		if(entity.getCfgId()==null){
			entity.setCreatorId(UserUtils.getUser().getId());
			entity.setCreateTime(createTime);
			entity.setIsValid(0);
			entity.setIsAudit(0);
			if(entity.getCompileId()==null||entity.getCompileId()==0) {
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
					
				}else{
					throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>");
				}
			}
			if(entity.getCompileId()!=null&&entity.getCompileId()>0) {
				dnsIpCfgDao.insert(entity);
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
			dnsIpCfgDao.update(entity);
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
			DnsIpCfg entity = new DnsIpCfg();
			entity.setCfgId(Long.valueOf(id));
			entity.setFunctionId(functionId);
			entity.setIsAudit(isAudit);
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			dnsIpCfgDao.update(entity);
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
		DnsIpCfg entity = new DnsIpCfg();
		
		List<DnsIpCfg> list = new ArrayList();
		
		entity.setCfgId(Long.valueOf(id));
		entity.setIsAudit(isAudit);
		entity.setIsValid(isValid);
		entity.setAuditorId(UserUtils.getUser().getId());
		entity.setAuditTime(auditTime);
		
		dnsIpCfgDao.update(entity);
		
		entity=dnsIpCfgDao.getDnsIpCfg(Long.valueOf(id));
		
		String json="";
		if(entity.getIsAudit()==1){
			List<InlineIp> resStrategyList=new ArrayList<InlineIp>();
			InlineIp resStrategy=convertCallBackIp(entity,entity.getDnsStrategyId());
			resStrategyList.add(resStrategy);
			//调用服务接口下发配置数据
			json=gsonToJson(resStrategyList);
			logger.info("DNS欺骗ip配置下发配置参数："+json);
			//调用服务接口下发配置
			try {
				ToMaatResult result = ConfigServiceUtil.postCallbackCfg(json);
				if(result!=null){
					logger.info("DNS欺骗ip配置下发响应信息："+result.getMsg());
				}
			} catch (Exception e) {
				logger.error("DNS欺骗ip配置下发失败",e);
				throw e;
			}
		}else if(entity.getIsAudit()==3){
			List<InlineIp> resStrategyList=new ArrayList<>();
			InlineIp resStrategy=convertCallBackIp(entity,entity.getDnsStrategyId());
			resStrategyList.add(resStrategy);
			//调用服务接口取消配置
			json=gsonToJson(resStrategyList);
			logger.info("DNS欺骗ip配置参数："+json);
			//调用服务接口取消配置
			try {
				ToMaatResult result = ConfigServiceUtil.put(json, 2);
				logger.info("DNS欺骗ip配置响应信息："+result.getMsg());
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("DNS欺骗ip配置配置失败");
				throw e;
			}
		}
		
	}
}
