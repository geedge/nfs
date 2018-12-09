package com.nis.web.service.configuration;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nis.domain.Page;
import com.nis.domain.basics.SysDictInfo;
import com.nis.domain.callback.NtcDnsResStrategy;
import com.nis.domain.configuration.DnsResStrategy;
import com.nis.domain.configuration.IpMultiplexPoolCfg;
import com.nis.domain.maat.ToMaatResult;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.StringUtil;
import com.nis.web.dao.basics.SysDictInfoDao;
import com.nis.web.dao.configuration.IpMultiplexPoolCfgDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.BaseService;



/**
 * 音视频文本
 * @author dell
 *
 */
@Service
public class IpMultiplexPoolCfgService extends BaseService{
	@Autowired
	protected IpMultiplexPoolCfgDao ipMultiplexStrategyDao;
	
	@Autowired
    private SysDictInfoDao sysDictInfoDao;
	
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<IpMultiplexPoolCfg> findPage(Page<IpMultiplexPoolCfg> page, IpMultiplexPoolCfg entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"r"));
		entity.setPage(page);
		List<IpMultiplexPoolCfg> list=ipMultiplexStrategyDao.findPage(entity);
		page.setList(list);
		return page;
	}
	public List<IpMultiplexPoolCfg> findIpMultiplexPoolCfgs(Long cfgId,Integer isValid,Integer isAudit) {
		List<IpMultiplexPoolCfg> list=ipMultiplexStrategyDao.findList(cfgId,isValid,isAudit);
		return list;
	}
	
	public IpMultiplexPoolCfg getIpMultiplexPoolCfg(Long id,Integer isValid) {
		List<IpMultiplexPoolCfg> list=ipMultiplexStrategyDao.findList(id,isValid,null);
		IpMultiplexPoolCfg dnsResStrategy=null;
		if(list != null && list.size()>0){
			dnsResStrategy=list.get(0);
		}
		return dnsResStrategy;
	}
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveOrUpdate(IpMultiplexPoolCfg entity){
		Date createTime=new Date();
		setAreaEffectiveIds(entity);
		if(entity.getCfgId()==null){
			entity.initDefaultValue();
			entity.setCreatorId(UserUtils.getUser().getId());
			entity.setCreateTime(createTime);
			entity.setIsValid(0);
			entity.setIsAudit(0);
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
			ipMultiplexStrategyDao.insert(entity);
			//修改
		}else{
			Date editTime=new Date();
			entity.setIsValid(0);
			entity.setIsAudit(0);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(editTime);
			
			ipMultiplexStrategyDao.update(entity);
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
			IpMultiplexPoolCfg entity = new IpMultiplexPoolCfg();
			entity.setCfgId(Long.valueOf(id));
			entity.setFunctionId(functionId);
			entity.setIsAudit(isAudit);
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			ipMultiplexStrategyDao.update(entity);
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
		IpMultiplexPoolCfg cfg=new IpMultiplexPoolCfg();
		cfg.setCfgId(Long.valueOf(id));
		cfg.setIsValid(isValid);
		cfg.setIsAudit(isAudit);
		cfg.setAuditorId(UserUtils.getUser().getId());
		cfg.setAuditTime(auditTime);
		ipMultiplexStrategyDao.update(cfg);
		cfg=getIpMultiplexPoolCfg(cfg.getCfgId(), null);
		String json="";
		if(cfg.getIsAudit()==1){
			List<IpMultiplexPoolCfg> resStrategyList=new ArrayList<IpMultiplexPoolCfg>();
			resStrategyList.add(cfg);
			//调用服务接口下发配置数据
			json=gsonToJson(resStrategyList);
			logger.info("IP复用地址池配置下发配置参数："+json);
			//调用服务接口下发配置
			try {
				ToMaatResult result = ConfigServiceUtil.postCallbackCfg(json);
				if(result!=null){
					logger.info("IP复用地址池配置下发响应信息："+result.getMsg());
				}
			} catch (Exception e) {
				logger.error("IP复用地址池配置配置下发失败",e);
				throw e;
			}
		}else if(cfg.getIsAudit()==3){
			List<IpMultiplexPoolCfg> resStrategyList=new ArrayList<>();
			resStrategyList.add(cfg);
			//调用服务接口取消配置
			json=gsonToJson(resStrategyList);
			logger.info("IP复用地址池配置配置参数："+json);
			//调用服务接口取消配置
			try {
				ToMaatResult result = ConfigServiceUtil.put(json, 2);
				logger.info("IP复用地址池配置响应信息："+result.getMsg());
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("IP复用地址池配置配置失败");
				throw e;
			}
		}
	}
	
	/**
	 * 获取配置信息
	 * @param cfg
	 * @return
	 */
	public IpMultiplexPoolCfg getCfgInfo(IpMultiplexPoolCfg cfg){
		return ipMultiplexStrategyDao.getCfgInfo(cfg);
	}
	
	/**
	 * 获取该分组下的ISP信息
	 * @param groupId
	 * @return
	 */
	public List<IpMultiplexPoolCfg> getIspByGroupId(Integer groupId){
		return ipMultiplexStrategyDao.getIspByGroupId(groupId);
	};
	
	public List<IpMultiplexPoolCfg> getIsp(List<IpMultiplexPoolCfg> ispList){
		for (IpMultiplexPoolCfg snat : ispList) {
			if(!StringUtil.isEmpty(snat.getAreaEffectiveIds())){
			if(snat.getAreaEffectiveIds().contains(":")){
				int index=snat.getAreaEffectiveIds().indexOf(":");
				String ispCode =snat.getAreaEffectiveIds().substring(index+1);
				SysDictInfo  dict=  sysDictInfoDao.getDictByItemCode(ispCode);
				if(dict !=null){
					snat.setIspName(dict.getItemValue());
				}
			}else{
				SysDictInfo  dict=  sysDictInfoDao.getDictByItemCode(snat.getAreaEffectiveIds());
				if(dict !=null){
					snat.setIspName(dict.getItemValue());
				 }
			   }
			}
		}
		return ispList;
	}
}
	
	