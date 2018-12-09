/**
 *@Title: ControlController.java
 *@Package com.nis.web.controller.configuration.proxy 
 *@Description TODO
 *@author dell
 *@date 2018年6月22日 下午4:35:42
 *@version  版本号
 */
package com.nis.web.controller.configuration.manipulation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nis.domain.Page;
import com.nis.domain.SysDataDictionaryItem;
import com.nis.domain.basics.PolicyGroupInfo;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.IpAddrPoolCfg;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.configuration.IpReuseDnatPolicyCfg;
import com.nis.domain.configuration.IpReusePolicyCfg;
import com.nis.domain.configuration.UserManage;
import com.nis.domain.configuration.template.IpMultiplexPolicyTemplate;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.Constants;
import com.nis.util.DictUtils;
import com.nis.util.StringUtil;
import com.nis.web.controller.configuration.CommonController;
import com.nis.web.service.configuration.IpMultiplexService;

/**
 * @ClassName: ControlController.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年6月22日 下午4:35:42
 * @version V1.0
 */
@Controller 
@RequestMapping("${adminPath}/manipulation/ipmulitiplex")
public class IpMultiplexController extends CommonController {
	
	@Autowired
	private IpMultiplexService ipMultiplexService;
	
	@RequestMapping(value = {"/list"})
//	@RequiresPermissions(value={"ip:mulitiplex:config","ip:mulitiplex:confirm"},logical=Logical.OR)
	public String ipList(String cfgName,Model model,@ModelAttribute("cfg")IpPortCfg cfg,HttpServletRequest request,HttpServletResponse response) {
		this._ipList(cfgName,model, cfg, request, response);
		model.addAttribute("urlPrefix","/manipulation/ipmulitiplex");
		model.addAttribute("requiresPermissionPrefix","ip:mulitiplex");
		return "/cfg/common/ipList";
	}
	@RequestMapping(value = {"/form"})
	@RequiresPermissions(value={"ip:mulitiplex:config"})
	public String ipForm(String cfgName,Model model,String ids,Integer functionId,BaseIpCfg entity) {
		this._ipForm(cfgName,model, ids, functionId, entity);
		SysDataDictionaryItem dict=null;
		List<SysDataDictionaryItem> dictList= DictUtils.getDictList("GROUP_TYPE");
		for(SysDataDictionaryItem di:dictList){
			if("IP".equals(di.getItemValue())){
				dict=di;
				break;
			}
		}
		if(dict!=null){
			List<PolicyGroupInfo> policyGroups=policyGroupInfoService.findPolicyGroupInfosByType(Integer.parseInt(dict.getItemCode()));
			model.addAttribute("policyGroups", policyGroups);
		}
		model.addAttribute("urlPrefix","/manipulation/ipmulitiplex");
		model.addAttribute("requiresPermissionPrefix","ip:mulitiplex");
		return "/cfg/manipulation/ipmulitiplex/form";
	}
	@RequestMapping(value = {"/saveOrUpdate"})
	public String saveOrUpdateIp(String cfgName,RedirectAttributes model, IpPortCfg cfg) {
		this._saveOrUpdateIp(cfgName,model, cfg);
		return "redirect:" + adminPath +"/manipulation/ipmulitiplex/list?functionId="+cfg.getFunctionId();
	}
	@RequestMapping(value = {"/delete"})
	@RequiresPermissions("ip:mulitiplex:config")
	public String deleteIp(String cfgName,String ids,String compileIds,Integer functionId,RedirectAttributes model) {
		this._deleteIp(cfgName,ids, compileIds, functionId, model);
		model.addAttribute("urlPrefix","/proxy/ipmulitiplex");
		model.addAttribute("requiresPermissionPrefix","ip:mulitiplex");
		return "redirect:" + adminPath +"/manipulation/ipmulitiplex/list?functionId="+functionId;
	}
	@RequestMapping(value = {"/audit"})
	@RequiresPermissions("ip:mulitiplex:confirm")
	public String auditIp(String cfgName,String ids,IpPortCfg cfg,RedirectAttributes redirectAttributes) {
		this._auditIp(cfgName,ids, cfg, redirectAttributes);
		redirectAttributes.addAttribute("urlPrefix","/manipulation/ipmulitiplex");
		redirectAttributes.addAttribute("requiresPermissionPrefix","ip:mulitiplex");
		return "redirect:" + adminPath +"/manipulation/ipmulitiplex/list?functionId="+cfg.getFunctionId();
	}
	//ip配置导入
	/*@RequestMapping(value = "/import", method=RequestMethod.POST)
	public String importIp(String cfgName,RedirectAttributes redirectAttributes,
			@RequestParam("file") MultipartFile file,IpPortCfg cfg) {
		this._importIp(cfgName,redirectAttributes, file,cfg,IpMultiplexPolicyTemplate.class);
		redirectAttributes.addAttribute("urlPrefix","/manipulation/ipmulitiplex");
		redirectAttributes.addAttribute("requiresPermissionPrefix","ip:mulitiplex");
		return "redirect:" + adminPath +"/manipulation/ipmulitiplex/list?functionId="+cfg.getFunctionId();
	}*/
	//ip模板下载
	@RequestMapping(value = "import/template")
	public void importFileTemplate(HttpServletRequest request,HttpServletResponse response, 
    		RedirectAttributes redirectAttributes,Integer functionId,Integer cfgRegionCode) {
		this._importFileTemplate(request, response, redirectAttributes, functionId, cfgRegionCode,IpMultiplexPolicyTemplate.class);
	}
	//ip配置导出
	@RequestMapping(value = "export")
	public void exportIp(String columns,Model model,HttpServletRequest  request,HttpServletResponse response,
			@ModelAttribute("cfg")IpPortCfg entity,String ids,RedirectAttributes redirectAttributes){
		this._exportIp(columns,model, request, response, entity, ids, redirectAttributes);
	}
	
	/**
	 * IP复用策略拆分为SNAT/DNAT复用策略
	 */
	@RequestMapping(value = {"/snatPolicyList"})
	public String snatPolicyList(Model model,@ModelAttribute("cfg")IpReusePolicyCfg cfg,
			HttpServletRequest request,HttpServletResponse response) {
		
		Page<IpReusePolicyCfg> page = ipMultiplexService.findPage(new Page<IpReusePolicyCfg>(request, response,"r"), cfg);
		model.addAttribute("page", page);
		initFormCondition(model,cfg);
		
		// 获取地址池、用户信息
		List<UserManage> users = userManageService.findUsers();
		List<IpAddrPoolCfg> addrPools = ipAddrPoolCfgService.getEffectiveAddrPool();
		model.addAttribute("users", users);
		model.addAttribute("addrPools", addrPools);
		return "/cfg/manipulation/ipmulitiplex/snatPolicyList2";
	}
	
	@RequestMapping(value = {"/snatPolicyForm"})
	@RequiresPermissions(value={"snat_policy:config"})
	public String snatPolicyForm(Model model, String ids, IpReusePolicyCfg cfg,
			HttpServletRequest request,HttpServletResponse response) {
		if(cfg == null){
			cfg=new IpReusePolicyCfg();
		}
		if(!StringUtil.isEmpty(ids)){
			cfg = ipMultiplexService.getPolicyCfg(Long.valueOf(ids),null);
			initUpdateFormCondition(model, cfg);
		}else{
			initFormCondition(model, cfg);
		}
		
		// 获取用户信息 地址池信息
		List<UserManage> users = userManageService.findUsers();
		
		model.addAttribute("_cfg", cfg);
		model.addAttribute("users", users);
		model.addAttribute("urlPrefix","/manipulation/ipmulitiplex");
		return "/cfg/manipulation/ipmulitiplex/snatPolicyForm2";
	}
	
	@RequestMapping(value = {"/saveOrUpdateSnat"})
	public String saveOrUpdateSnat(Model model, HttpServletRequest request,HttpServletResponse response,
			RedirectAttributes redirectAttributes, IpReusePolicyCfg cfg) {
		try{
			// 添加策略配置信息
			ipMultiplexService.saveOrUpdate(cfg, request, response);
			addMessage(redirectAttributes,"success","save_success");
		}catch(Exception e) {
			e.printStackTrace();
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,"error", "request_service_failed");
			}else {
				addMessage(redirectAttributes,"error", "save_failed");
			}
		}
		
		return "redirect:" + adminPath +"/manipulation/ipmulitiplex/snatPolicyList?functionId="+cfg.getFunctionId();
	}
	
	@RequestMapping(value = {"/snatDelete"})
	@RequiresPermissions("snat_policy:config")
	public String snatDelete(String ids, Integer isValid, Integer functionId,RedirectAttributes redirectAttributes,
			HttpServletRequest request,HttpServletResponse response) {
		
		try{
			if(!StringUtil.isEmpty(ids)){
				ipMultiplexService.delete(isValid,ids,functionId);
			}
			addMessage(redirectAttributes,"success","delete_success");
		}catch(Exception e){
			logger.error(e);
			addMessage(redirectAttributes,"error","delete_failed");
		}
		
		return "redirect:" + adminPath +"/manipulation/ipmulitiplex/snatPolicyList?functionId="+functionId;
	}
	
	@RequestMapping(value = {"/auditSnat"})
	@RequiresPermissions("snat_policy:confirm")
	public String auditSnat(String ids,Integer isAudit,Integer isValid,Integer functionId,RedirectAttributes redirectAttributes) {
		
		if(!StringUtil.isEmpty(ids)){
			String[] idArray = ids.split(",");
			Date auditTime=new Date();
			for(String id :idArray){
				try {
					ipMultiplexService.auditSnatPolicy(isAudit,isValid,functionId,id,auditTime);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("SNAT策略配置下发失败："+e.getMessage());
					if(e instanceof MaatConvertException) {
						addMessage(redirectAttributes,"error", "request_service_failed");
					}else {
						addMessage(redirectAttributes,"error", "audit_failed");
					}
				}
			}
			
		}
		return "redirect:" + adminPath +"/manipulation/ipmulitiplex/snatPolicyList?functionId="+functionId;
	}
	
	/**
	 * 异步获取地址池信息
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = {"/ajaxGetAddrPools"})
	public List<IpAddrPoolCfg> ajaxGetAddrPools(HttpServletRequest request, HttpServletResponse response){
		List<IpAddrPoolCfg> addrPools = ipAddrPoolCfgService.getEffectiveAddrPool();
		List<IpAddrPoolCfg> addrPoolsNew=new ArrayList<>();
		for (IpAddrPoolCfg addrPool : addrPools) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("addr_pool_id", addrPool.getCfgId());
			String url =Constants.IP_REUSE_CALL_CGI_URL ;
			Map<String, Object> resultMap=ConfigServiceUtil.getCGIInfo(url, Constants.IP_NUM_GET, params);
			if(!StringUtil.isEmpty(resultMap) 
					&& !StringUtil.isEmpty(resultMap.get("num"))
					&& Integer.parseInt(resultMap.get("num").toString()) > 0) {
				addrPoolsNew.add(addrPool);
			}
		}
		return addrPoolsNew;
	}
	
	/**
	 * 校验地址池是否被策略引用
	 * @param addrPoolIds
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = {"/ajaxIsUsed"})
	public boolean ajaxIsUsed(String addrPoolIds, HttpServletRequest request, HttpServletResponse response){
		for (String addrPoolId : addrPoolIds.split(",")) {
			List<IpReusePolicyCfg> ipList = ipMultiplexService.checkAddrPoolIsUsed(addrPoolId);
			if(ipList != null && ipList.size() > 0){
				return false;
			}
		}
		return true;
		
	}
	
	
	@RequestMapping(value = {"/dnatPolicyList"})
	public String dnatPolicyList(String cfgName,Model model,@ModelAttribute("cfg")IpReuseDnatPolicyCfg cfg,
			HttpServletRequest request,HttpServletResponse response) {
		//this._ipList(cfgName,model, cfg, request, response);
		
		Page<BaseIpCfg> page = ipMultiplexService.findPageDnat(new Page<BaseIpCfg>(request, response,"r"), cfg);
		model.addAttribute("page", page);
		initFormCondition(model,cfg);
		
		return "/cfg/manipulation/ipmulitiplex/dnatPolicyList2";
	}
	
	@RequestMapping(value = {"/dnatPolicyForm"})
	@RequiresPermissions(value={"dnat_policy:config"})
	public String dnatPolicyForm(String cfgName,Model model,String ids,Integer functionId,IpReuseDnatPolicyCfg cfg) {
		//this._ipForm(cfgName,model, ids, functionId, entity);
		
		if(cfg == null){
			cfg=new IpReuseDnatPolicyCfg();
		}
		if(!StringUtil.isEmpty(ids)){
			cfg = ipMultiplexService.getDnatPolicyCfg(Long.valueOf(ids),null);
			initUpdateFormCondition(model, cfg);
		}else{
			initFormCondition(model, cfg);
		}
		model.addAttribute("_cfg", cfg);
		return "/cfg/manipulation/ipmulitiplex/dnatPolicyForm2";
	}
	
	@RequestMapping(value = {"/saveOrUpdateDnat"})
	public String saveOrUpdateDnat(Model model, RedirectAttributes redirectAttributes, IpReuseDnatPolicyCfg cfg,
			HttpServletRequest request,HttpServletResponse response) {
		try{
			// 添加策略配置信息
			ipMultiplexService.saveOrUpdateDnat(cfg, request, response);
			addMessage(redirectAttributes,"success","save_success");
		}catch(Exception e) {
			e.printStackTrace();
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,"error", "request_service_failed");
			}else {
				addMessage(redirectAttributes,"error", "save_failed");
			}
		}
		return "redirect:" + adminPath +"/manipulation/ipmulitiplex/dnatPolicyList?functionId="+cfg.getFunctionId();
	}
	
	@RequestMapping(value = {"/dnatDelete"})
	@RequiresPermissions("dnat_policy:config")
	public String dnatDelete(String ids, Integer isValid, Integer functionId,RedirectAttributes redirectAttributes) {
		//this._deleteIp(cfgName,ids, compileIds, functionId, model);
		
		try{
			if(!StringUtil.isEmpty(ids)){
				ipMultiplexService.deleteDnat(isValid,ids,functionId);
			}
			addMessage(redirectAttributes,"success","delete_success");
		}catch(Exception e){
			logger.error(e);
			addMessage(redirectAttributes,"error","delete_failed");
		}
		
		return "redirect:" + adminPath +"/manipulation/ipmulitiplex/dnatPolicyList?functionId="+functionId;
	}
	
	@RequestMapping(value = {"/auditDnat"})
	@RequiresPermissions("dnat_policy:confirm")
	public String auditDnat(String ids,Integer isAudit,Integer isValid,Integer functionId,RedirectAttributes redirectAttributes) {
		//this._auditIp(cfgName,ids, cfg, redirectAttributes);
		
		if(!StringUtil.isEmpty(ids)){
			String[] idArray = ids.split(",");
			Date auditTime=new Date();
			for(String id :idArray){
				try {
					ipMultiplexService.auditDnatPolicy(isAudit,isValid,functionId,id,auditTime);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("DNAT策略配置下发失败："+e.getMessage());
					if(e instanceof MaatConvertException) {
						addMessage(redirectAttributes,"error", "request_service_failed");
					}else {
						addMessage(redirectAttributes,"error", "audit_failed");
					}
				}
			}
			
		}
		return "redirect:" + adminPath +"/manipulation/ipmulitiplex/dnatPolicyList?functionId="+functionId;
	}
	
	//snat配置导出
			@RequestMapping(value = "exportSnat")
			public void exportSnat(Model model,HttpServletRequest  request,HttpServletResponse response,
					@ModelAttribute("cfg")BaseIpCfg entity,String ids,RedirectAttributes redirectAttributes){
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
						entity.setTableName(IpPortCfg.getTablename());
						Page<BaseIpCfg> pageInfo=new Page<BaseIpCfg>(request, response,"r");
						pageInfo.setPageNo(1);
						pageInfo.setPageSize(Constants.MAX_EXPORT_SIZE);
						Page<BaseIpCfg> page = ipCfgService.findPage(pageInfo, entity);//
						for (BaseIpCfg baseIp : page.getList()) {
							if(StringUtil.isEmpty(baseIp.getGroupName())){
								Properties msgProp = getMsgProp();
								baseIp.setGroupName(msgProp.getProperty("default_group"));
							}
						}
						titleList.add(entity.getMenuNameCode());
						classMap.put(entity.getMenuNameCode(), BaseIpCfg.class);
						String snatNoExport=",client_ip,group_name,server_ip,ip_type,ip_pattern,port_pattern,client_port,server_port,ir_type,direction,protocol,do_log,whether_area_block,userregion1,userregion2,userregion3,userregion4,userregion5,";
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
					@ModelAttribute("cfg")BaseIpCfg entity,String ids,RedirectAttributes redirectAttributes){
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
						entity.setTableName(IpPortCfg.getTablename());
						Page<BaseIpCfg> pageInfo=new Page<BaseIpCfg>(request, response,"r");
						pageInfo.setPageNo(1);
						pageInfo.setPageSize(Constants.MAX_EXPORT_SIZE);
						Page<BaseIpCfg> page = ipCfgService.findPage(pageInfo, entity);//
						for (BaseIpCfg baseIp : page.getList()) {
							if(StringUtil.isEmpty(baseIp.getGroupName())){
								Properties msgProp = getMsgProp();
								baseIp.setGroupName(msgProp.getProperty("default_group"));
							}
						}
						titleList.add(entity.getMenuNameCode());
						classMap.put(entity.getMenuNameCode(), BaseIpCfg.class);
						String snatNoExport=",direction,client_ip,ip_type,ip_pattern,port_pattern,client_port,ir_type,do_log,whether_area_block,userregion1,userregion2,userregion3,userregion4,userregion5,";
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
