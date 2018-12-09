package com.nis.web.service.configuration;

import java.util.ArrayList;
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
import com.nis.domain.callback.IpAddrPool;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.IpAddrPoolCfg;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.configuration.UserManage;
import com.nis.domain.maat.MaatCfg.IpCfg;
import com.nis.domain.maat.ToMaatResult;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.Constants;
import com.nis.web.dao.configuration.IpAddrPoolCfgDao;
import com.nis.web.dao.configuration.IpCfgDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.BaseService;
import com.nis.web.service.SpringContextHolder;
import com.nis.web.service.basics.SysDictInfoService;

@Service
public class IpAddrPoolCfgService extends BaseService{
	
	@Autowired
	private IpAddrPoolCfgDao ipAddrPoolCfgDao;
	
	/**
	 * 列表分页查询
	 * @param page
	 * @param entity
	 * @return
	 */
	public Page<IpAddrPoolCfg> findPage(Page<IpAddrPoolCfg> page, IpAddrPoolCfg entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"r"));
		entity.setPage(page);
		List<IpAddrPoolCfg> list=ipAddrPoolCfgDao.findPage(entity);
		page.setList(list);
		return page;
	}
	
	/**
	 * 获取数据跳转表单
	 * @param id
	 * @param isValid
	 * @return
	 */
	public IpAddrPoolCfg getIpAddrPoolCfg(Long cfgId, Integer isValid) {
		// 1.获取地址池配置信息
		List<IpAddrPoolCfg> list=ipAddrPoolCfgDao.findList(cfgId,isValid,null);
		IpAddrPoolCfg addrPoolCfg=null;
		if(list != null && list.size()>0){
			addrPoolCfg=list.get(0);
		}
		
		// 2.获取地址池内IP信息
		List<BaseIpCfg> ipCfgs = ipAddrPoolCfgDao.getReuseIpCfgs(addrPoolCfg.getCfgId());
		if(ipCfgs != null && ipCfgs.size()>0){
			addrPoolCfg.setIpCfgs(ipCfgs);
		}
		return addrPoolCfg;
	}
	
	/**
	 * 更新配置数据
	 * @param res 
	 * @param req 
	 * @param cfg
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveOrUpdate(IpAddrPoolCfg entity, HttpServletRequest req, HttpServletResponse res) {
		
		// 1.更新地址池信息
		Date time = new Date();
		entity.setIsValid(0);
		entity.setIsAudit(0);
		setAreaEffectiveIds(entity);
		entity.setIpTotal(entity.getIpCfgs().size());
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
			ipAddrPoolCfgDao.saveAddrPoolCfg(entity);
			
		}else{
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(time);
			ipAddrPoolCfgDao.updateAddrPoolCfg(entity);
			entity.setCreateTime(new Date());
			entity.setCreatorId(entity.getCurrentUser().getId());
		}
		Long addrPoolId = ipAddrPoolCfgDao.getAddrPoolIdByName(entity.getAddrPoolName());
		
		// 2.更新地址池IP信息 
		ipAddrPoolCfgDao.deleteReuseIpCfgs(addrPoolId);
		for (BaseIpCfg IpCfg : entity.getIpCfgs()) {
			try {
				List<Integer> compileIds = ConfigServiceUtil.getId(1,1);
				if(compileIds != null && compileIds.size() > 0 ){
					IpCfg.setCompileId(compileIds.get(0));
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("获取编译ID出错");
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+e.getMessage());
			}
			BeanUtils.copyProperties(entity, IpCfg, new String[]{"cfgId","compileId"});
			IpCfg.setUserRegion1(addrPoolId+"");
			ipAddrPoolCfgDao.saveReuseIpCfgs(IpCfg);
		}
		
	}
	
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void delete(Integer isAudit, Integer isValid, String ids, Integer functionId) {
		String[] idArray = ids.split(",");
		for(String id :idArray){
			IpAddrPoolCfg entity = new IpAddrPoolCfg();
			entity.setCfgId(Long.valueOf(id));
			entity.setFunctionId(functionId);
			entity.setIsAudit(isAudit);
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			ipAddrPoolCfgDao.updateAddrPoolCfg(entity);
			ipAddrPoolCfgDao.updateReuseIpCfgs(entity);
			
		}
		
	}
	
	/**
	 * 配置审核
	 * @param isAudit
	 * @param isValid
	 * @param functionId
	 * @param id
	 * @param auditTime
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void audit(Integer isAudit, Integer isValid, Integer functionId, String id, Date auditTime) {
		IpAddrPoolCfg cfg=new IpAddrPoolCfg();
		cfg.setCfgId(Long.valueOf(id));
		cfg.setIsValid(isValid);
		cfg.setIsAudit(isAudit);
		cfg.setAuditorId(UserUtils.getUser().getId());
		cfg.setAuditTime(auditTime);
		ipAddrPoolCfgDao.updateAddrPoolCfg(cfg);// 更新配置审核状态
		ipAddrPoolCfgDao.updateReuseIpCfgs(cfg);
		
		cfg = this.getIpAddrPoolCfg(cfg.getCfgId(), null);
		String json="";
		if(cfg.getIsAudit()==1){
			List<IpAddrPool> resStrategyList=new ArrayList<IpAddrPool>();
			for(BaseIpCfg ipcfg : cfg.getIpCfgs()) {
				IpAddrPool ip = new IpAddrPool();
				BeanUtils.copyProperties(cfg, ipcfg, new String[]{"cfgId","compileId"});
				List<IpCfg> cfgs = BaseService.ipConvert(new IpCfg(), ipcfg);
				if (cfgs.size() > 1) {
					throw new RuntimeException("CallBack IP did not support IP range!");
				}
				IpCfg c = cfgs.get(0);
				ip.setRegionId(ipcfg.getCompileId());
				ip.setGroupId(cfg.getCfgId());
				ip.setAddrType(ipcfg.getIpType());
				
				ip.setSrcIp(c.getSrcIp());
				ip.setMaskSrcIp(c.getSrcIpMask());
				ip.setSrcPort(c.getSrcPort());
				ip.setMaskSrcPort(c.getSrcPortMask());
				ip.setDstIp(c.getDstIp());
				ip.setMaskDstIp(c.getDstIpMask());
				ip.setDstPort(c.getDstPort());
				ip.setMaskDstPort(c.getDstPortMask());
				ip.setProtocol(ipcfg.getProtocol());
				ip.setDirection(ipcfg.getDirection());
				
				ip.setAddrPoolId(cfg.getCfgId());
				ip.setIsValid(cfg.getIsValid());
				ip.setAction(ipcfg.getAction());
				ip.setService(ipcfg.getServiceId());
				ip.setAreaEffectiveIds(cfg.getAreaEffectiveIds());//添加区域管控
				ip.setOpTime(auditTime);
				resStrategyList.add(ip);
			}
			//调用服务接口下发配置数据
			json=gsonToJson(resStrategyList);
			logger.info("SNAT地址池配置下发配置参数："+json);
			//调用服务接口下发配置
			try {
				ToMaatResult result = ConfigServiceUtil.postCallbackCfg(json);
				if(result!=null){
					logger.info("SNAT地址池配置下发响应信息："+result.getMsg());
				}
			} catch (Exception e) {
				logger.error("SNAT地址池配置下发失败",e);
				throw e;
			}
			
		}else if(cfg.getIsAudit()==3){
			List<IpAddrPool> resStrategyList=new ArrayList<IpAddrPool>();
			for(BaseIpCfg ipcfg : cfg.getIpCfgs()) {
				IpAddrPool ip = new IpAddrPool();
				BeanUtils.copyProperties(cfg, ipcfg, new String[]{"cfgId","compileId"});
				List<IpCfg> cfgs = BaseService.ipConvert(new IpCfg(), ipcfg);
				if (cfgs.size() > 1) {
					throw new RuntimeException("CallBack IP did not support IP range!");
				}
				IpCfg c = cfgs.get(0);
				ip.setRegionId(ipcfg.getCompileId());
				ip.setGroupId(cfg.getCfgId());
				ip.setAddrType(ipcfg.getIpType());
				
				ip.setSrcIp(c.getSrcIp());
				ip.setMaskSrcIp(c.getSrcIpMask());
				ip.setSrcPort(c.getSrcPort());
				ip.setMaskSrcPort(c.getSrcPortMask());
				ip.setDstIp(c.getDstIp());
				ip.setMaskDstIp(c.getDstIpMask());
				ip.setDstPort(c.getDstPort());
				ip.setMaskDstPort(c.getDstPortMask());
				ip.setProtocol(ipcfg.getProtocol());
				ip.setDirection(ipcfg.getDirection());
				
				ip.setAddrPoolId(cfg.getCfgId());
				ip.setIsValid(cfg.getIsValid());
				ip.setAction(ipcfg.getAction());
				ip.setService(ipcfg.getServiceId());
				ip.setAreaEffectiveIds(cfg.getAreaEffectiveIds());
				ip.setOpTime(auditTime);
				resStrategyList.add(ip);
			}
			//调用服务接口取消配置
			json=gsonToJson(resStrategyList);
			logger.info("SNAT地址池配置配置参数："+json);
			//调用服务接口取消配置
			try {
				ToMaatResult result = ConfigServiceUtil.put(json, 2);
				logger.info("SNAT地址池配置响应信息："+result.getMsg());
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("SNAT地址池配置配置失败");
				throw e;
			}
			
		}
		
	}

	public IpAddrPoolCfg getCfgInfo(IpAddrPoolCfg cfg) {
		return ipAddrPoolCfgDao.getCfgInfo(cfg);
	}
	
	
	/**
	 * 策略管理获取地址池信息（至少有1个IP地址有效状态该地址池才能被使用）
	 * @param response 
	 * @param request 
	 * @return
	 */
	public List<IpAddrPoolCfg> getEffectiveAddrPool() {
		return ipAddrPoolCfgDao.findAddrPoolCfg();
	}
	
}
