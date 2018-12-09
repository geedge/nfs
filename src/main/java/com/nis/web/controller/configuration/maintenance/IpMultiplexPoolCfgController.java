package com.nis.web.controller.configuration.maintenance;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nis.domain.Page;
import com.nis.domain.basics.PolicyGroupInfo;
import com.nis.domain.basics.SysDictInfo;
import com.nis.domain.configuration.IpMultiplexPoolCfg;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.Constants;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseController;
import com.nis.web.service.basics.SysDictInfoService;

/**
 * IP复用地址池配置
 *
 */
@Controller
@RequestMapping("${adminPath}/maintenance/ipMultiplexPoolCfg")
public class IpMultiplexPoolCfgController extends BaseController  {
	
	@RequestMapping(value = {"/form"})
	@RequiresPermissions(value={"ip:mulitiplex:pool:config"})
	public String from(Model model,
							HttpServletRequest request,
							HttpServletResponse response,
							String ids,
							@ModelAttribute("cfg")IpMultiplexPoolCfg cfg){
		if(cfg == null){
			cfg=new IpMultiplexPoolCfg();
		}
		if(!StringUtil.isEmpty(ids)){
			cfg = ipMultiplexPoolCfgService.getIpMultiplexPoolCfg(Long.valueOf(ids),null);
			initFormCondition(model, cfg);
		}else{
			initFormCondition(model, cfg);
		}
		
		//查询ip复用地址池配置的policyGroup列表
		List<PolicyGroupInfo> policyGroups=policyGroupInfoService.getHasAreaPolicyGroups(3);
		model.addAttribute("policyGroups", policyGroups);
		model.addAttribute("_cfg", cfg);
		return "/cfg/maintenance/ipMultiplexPool/form";
	}
	
	@RequestMapping(value = {"/saveOrUpdate"})
	@RequiresPermissions(value={"ip:mulitiplex:pool:config"})
	public String saveOrUpdate(Model model,HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,
			@ModelAttribute("cfg")IpMultiplexPoolCfg cfg){
		try{
			// 设置省份信息
			groupAreaService.setAreaCodeByGroupId(cfg);
			
			ipMultiplexPoolCfgService.saveOrUpdate(cfg);
			
			addMessage(redirectAttributes,"success","save_success");
		}catch(Exception e){
			e.printStackTrace();
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,"error", "request_service_failed");
			}else {
				addMessage(redirectAttributes,"error", "save_failed");
			}
		}
		
		return "redirect:" + adminPath +"/maintenance/ipMultiplexPoolCfg/list?functionId="+cfg.getFunctionId();
	}
	
	@RequestMapping(value = {"/list"})
	public String list(Model model,HttpServletRequest  request,HttpServletResponse response,@ModelAttribute("cfg")IpMultiplexPoolCfg entity){
		//查询时left join policyGroup
		Page<IpMultiplexPoolCfg> page = ipMultiplexPoolCfgService.findPage(new Page<IpMultiplexPoolCfg>(request, response,"r"), entity);
		model.addAttribute("page", page);
		initPageCondition(model,entity);
		return "/cfg/maintenance/ipMultiplexPool/list";
	}
	
	@RequestMapping(value = {"/delete"})
	@RequiresPermissions(value={"ip:mulitiplex:pool:config"})
	public String delete(Integer isAudit,Integer isValid,String ids,Integer functionId,Model model,RedirectAttributes redirectAttributes
			,HttpServletRequest request,HttpServletResponse response){
		try{
			if(!StringUtil.isEmpty(ids)){
				ipMultiplexPoolCfgService.delete(isAudit,isValid,ids,functionId);
			}
			addMessage(redirectAttributes,"delete_success");
		}catch(Exception e){
				logger.error(e);
				addMessage(redirectAttributes,"error", "delete_failed");
		}
		return "redirect:" + adminPath +"/maintenance/ipMultiplexPoolCfg/list?functionId="+functionId;
	}
	
	@RequestMapping(value = {"/audit"})
	@RequiresPermissions(value={"ip:mulitiplex:pool:confirm"})
	public String audit(Integer isAudit,Integer isValid,String ids,Integer functionId,RedirectAttributes redirectAttributes) {
		if(!StringUtil.isEmpty(ids)){
			String[] idArray = ids.split(",");
			Date auditTime=new Date();
			for(String id :idArray){
				try {
					ipMultiplexPoolCfgService.audit(isAudit,isValid,functionId,id,auditTime);
				} catch (MaatConvertException e) {
					e.printStackTrace();
					logger.info("IP复用地址池配置下发失败："+e.getMessage());
					addMessage(redirectAttributes,"error", "request_service_failed");
				}catch (Exception e) {
					e.printStackTrace();
					logger.info("IP复用地址池配置下发失败："+e.getMessage());
					addMessage(redirectAttributes,"error", "audit_failed");
				}
			}
			
		}
		return "redirect:" + adminPath +"/maintenance/ipMultiplexPoolCfg/list?functionId="+functionId;
	}
	@ResponseBody
	@RequestMapping(value = "/validCfgId")
	public boolean validCfgId(Long cfgId) {
		IpMultiplexPoolCfg dns=ipMultiplexPoolCfgService.getIpMultiplexPoolCfg(cfgId,null);
		if(dns == null ){
			return false;
		}else{
			return true;
		}
	}
	
	/*@RequestMapping(value = {"/snatlist"})
	public String snatlist(Model model,HttpServletRequest  request,HttpServletResponse response,@ModelAttribute("cfg")IpMultiplexPoolCfg entity){
		//查询时left join policyGroup
		Page<IpMultiplexPoolCfg> page = ipMultiplexPoolCfgService.findPage(new Page<IpMultiplexPoolCfg>(request, response,"r"), entity);
		model.addAttribute("page", page);
		initFormCondition(model,entity);
		return "/cfg/maintenance/ipMultiplexPool/snatlist";
	}
	
	@RequestMapping(value = {"/snatform"})
	@RequiresPermissions(value={"ip:mulitiplex:pool:config"})
	public String snatfrom(Model model,
							HttpServletRequest request,
							HttpServletResponse response,
							String ids,
							@ModelAttribute("cfg")IpMultiplexPoolCfg cfg){
		if(cfg == null){
			cfg=new IpMultiplexPoolCfg();
		}
		if(!StringUtil.isEmpty(ids)){
			cfg = ipMultiplexPoolCfgService.getIpMultiplexPoolCfg(Long.valueOf(ids),null);
			initUpdateFormCondition(model, cfg);
		}else{
			initFormCondition(model, cfg);
		}
		
		//查询ip复用地址池配置的policyGroup列表
		List<PolicyGroupInfo> policyGroups=policyGroupInfoService.getHasAreaPolicyGroups(2);
		model.addAttribute("policyGroups", policyGroups);
		model.addAttribute("_cfg", cfg);
		return "/cfg/maintenance/ipMultiplexPool/snatform";
	}
	
	@RequestMapping(value = {"/snatsaveOrUpdate"})
	@RequiresPermissions(value={"ip:mulitiplex:pool:config"})
	public String snatsaveOrUpdate(Model model,HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,
			@ModelAttribute("cfg")IpMultiplexPoolCfg cfg){
		try{
			// 设置省份信息
			if(!StringUtil.isEmpty(cfg.getPolicyGroup())){
				groupAreaService.setAreaCodeByGroupId(cfg);
			}
			ipMultiplexPoolCfgService.saveOrUpdate(cfg);
			
			addMessage(redirectAttributes,"success","save_success");
		}catch(Exception e){
			e.printStackTrace();
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,"error", "request_service_failed");
			}else {
				addMessage(redirectAttributes,"error", "save_failed");
			}
		}
		
		return "redirect:" + adminPath +"/maintenance/ipMultiplexPoolCfg/snatlist?functionId="+cfg.getFunctionId();
	}
	
	@RequestMapping(value = {"/snataudit"})
	@RequiresPermissions(value={"ip:mulitiplex:pool:confirm"})
	public String snataudit(Integer isAudit,Integer isValid,String ids,Integer functionId,RedirectAttributes redirectAttributes) {
		if(!StringUtil.isEmpty(ids)){
			String[] idArray = ids.split(",");
			Date auditTime=new Date();
			for(String id :idArray){
				try {
					ipMultiplexPoolCfgService.audit(isAudit,isValid,functionId,id,auditTime);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("SNAT地址池管理配置下发失败："+e.getMessage());
					if(e instanceof MaatConvertException) {
						addMessage(redirectAttributes,"error", "request_service_failed");
					}else {
						addMessage(redirectAttributes,"error", "audit_failed");
					}
				}
			}
			
		}
		return "redirect:" + adminPath +"/maintenance/ipMultiplexPoolCfg/snatlist?functionId="+functionId;
	}
	@RequestMapping(value = {"/snatdelete"})
	@RequiresPermissions(value={"ip:mulitiplex:pool:config"})
	public String snatdelete(Integer isAudit,Integer isValid,String ids,Integer functionId,Model model,RedirectAttributes redirectAttributes
			,HttpServletRequest request,HttpServletResponse response){
		try{
			if(!StringUtil.isEmpty(ids)){
				ipMultiplexPoolCfgService.delete(isAudit,isValid,ids,functionId);
			}
			addMessage(redirectAttributes,"success","delete_success");
		}catch(Exception e){
			logger.error(e);
			addMessage(redirectAttributes,"error","delete_failed");
		}
		return "redirect:" + adminPath +"/maintenance/ipMultiplexPoolCfg/snatlist?functionId="+functionId;
	}*/
	
	/**
	 * 校验IP是否已存在
	 * @param cfg
	 * @param redirectAttributes
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = {"/checkIp"})
	public boolean checkIp(IpMultiplexPoolCfg cfg, HttpServletRequest request, HttpServletResponse response){
		// 修改
		if(!StringUtil.isEmpty(cfg.getCfgId())){
			IpMultiplexPoolCfg poolCfg = ipMultiplexPoolCfgService.getCfgInfo(cfg);
			if(poolCfg != null && poolCfg.getDestIpAddress().equals(cfg.getDestIpAddress())){
				return true;
			}
		}
		cfg.setCfgId(null);
		IpMultiplexPoolCfg poolCfg = ipMultiplexPoolCfgService.getCfgInfo(cfg);
		if(poolCfg != null){
			return false;
		}
		return true;
	}
	
	//snat配置导出
			@RequestMapping(value = "exportSnat")
			public void exportSnat(Model model,HttpServletRequest  request,HttpServletResponse response,
					@ModelAttribute("cfg")IpMultiplexPoolCfg entity,String ids,RedirectAttributes redirectAttributes){
				try {
					//export data info
					List<String> titleList=new ArrayList<String>();
					Map<String, Class<?>> classMap=new HashMap<String, Class<?>>();
					Map<String, List> dataMap=new HashMap<String, List>();
					Map<String, String> noExportMap=new HashMap<String, String>();
					
					
					/*//导出选中记录
					 * if(!StringUtil.isEmpty(ids)){
						for(String id:ids.split(",")){
							Long.parseLong(id);
						}
						//List<CfgIndexInfo> list=ipCfgService.getListByCfgIdWithName(IpPortCfg.getTablename(), entity.getFunctionId(), ids);
					}else{*/
						//条件导出数据大于最大导出数，只导出最大导出条数
						Page<IpMultiplexPoolCfg> pageInfo=new Page<IpMultiplexPoolCfg>(request, response,"r");
						pageInfo.setPageNo(1);
						pageInfo.setPageSize(Constants.MAX_EXPORT_SIZE);
						Page<IpMultiplexPoolCfg> page = ipMultiplexPoolCfgService.findPage(pageInfo, entity);
						page.setList(ipMultiplexPoolCfgService.getIsp(page.getList()));
						titleList.add(entity.getMenuNameCode());
						classMap.put(entity.getMenuNameCode(), IpMultiplexPoolCfg.class);
						String snatNoExport=",server_port,direction,protocol,do_log,whether_area_block,userregion1,userregion2,userregion3,userregion4,userregion5,group_name,";
						noExportMap.put(entity.getMenuNameCode(),snatNoExport);
						dataMap.put(entity.getMenuNameCode(), page.getList());
					/*}*/
					this._export(model, request, response, redirectAttributes,entity.getMenuNameCode(),titleList,classMap,dataMap,noExportMap);
				} catch (Exception e) {
					logger.error("snat export failed",e);
					addMessage(redirectAttributes,"error","export_failed");
				}
				//return "redirect:" + adminPath +"/ntc/iplist/list?functionId="+entity.getFunctionId();
			}
			
			//dnat配置导出
					@RequestMapping(value = "exportDnat")
					public void exportDnat(Model model,HttpServletRequest  request,HttpServletResponse response,
							@ModelAttribute("cfg")IpMultiplexPoolCfg entity,String ids,RedirectAttributes redirectAttributes){
						try {
							//export data info
							List<String> titleList=new ArrayList<String>();
							Map<String, Class<?>> classMap=new HashMap<String, Class<?>>();
							Map<String, List> dataMap=new HashMap<String, List>();
							Map<String, String> noExportMap=new HashMap<String, String>();
							
							
							/*//导出选中记录
							 * if(!StringUtil.isEmpty(ids)){
								for(String id:ids.split(",")){
									Long.parseLong(id);
								}
								//List<CfgIndexInfo> list=ipCfgService.getListByCfgIdWithName(IpPortCfg.getTablename(), entity.getFunctionId(), ids);
							}else{*/
								//条件导出数据大于最大导出数，只导出最大导出条数
								Page<IpMultiplexPoolCfg> pageInfo=new Page<IpMultiplexPoolCfg>(request, response,"r");
								pageInfo.setPageNo(1);
								pageInfo.setPageSize(Constants.MAX_EXPORT_SIZE);
								Page<IpMultiplexPoolCfg> page = ipMultiplexPoolCfgService.findPage(pageInfo, entity);
								titleList.add(entity.getMenuNameCode());
								classMap.put(entity.getMenuNameCode(), IpMultiplexPoolCfg.class);
								String snatNoExport=",isp,do_log,whether_area_block,userregion1,userregion2,userregion3,userregion4,userregion5,group_name,";
								noExportMap.put(entity.getMenuNameCode(),snatNoExport);
								dataMap.put(entity.getMenuNameCode(), page.getList());
							/*}*/
							this._export(model, request, response, redirectAttributes,entity.getMenuNameCode(),titleList,classMap,dataMap,noExportMap);
						} catch (Exception e) {
							logger.error("dnat export failed",e);
							addMessage(redirectAttributes,"error","export_failed");
						}
						//return "redirect:" + adminPath +"/ntc/iplist/list?functionId="+entity.getFunctionId();
					}
}
