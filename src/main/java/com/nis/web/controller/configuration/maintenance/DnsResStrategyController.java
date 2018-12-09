package com.nis.web.controller.configuration.maintenance;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.taglibs.standard.functions.Functions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nis.domain.Page;
import com.nis.domain.basics.PolicyGroupInfo;
import com.nis.domain.configuration.AppSslCertCfg;
import com.nis.domain.configuration.DnsResStrategy;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.specific.SpecificServiceCfg;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.Constants;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseController;

/**
 * dns响应策略
 * @author ddm
 *
 */
@Controller
@RequestMapping("${adminPath}/maintenance/dnsResStrategy")
public class DnsResStrategyController extends BaseController  {
	
	@RequestMapping(value = {"/form"})
	@RequiresPermissions(value={"dns:res:strategy:config"})
	public String from(Model model,
							HttpServletRequest request,
							HttpServletResponse response,
							String ids,
							@ModelAttribute("cfg")DnsResStrategy cfg
							,RedirectAttributes redirectAttributes){
		if(cfg == null){
			cfg=new DnsResStrategy();
		}
		if(!StringUtil.isEmpty(ids)){
			cfg = dnsResStrategyService.getDnsResStrategy(Long.valueOf(ids),-1);
			initFormCondition(model, cfg);
			model.addAttribute("isAdd", false);
		}else{
			/*//不存在响应策略为0的配置，则新策略时，cfgId默认为0
			DnsResStrategy dns0=dnsResStrategyService.getDnsResStrategy(0l,null);
			if(dns0 == null){
				cfg.setCfgId(0l);
				cfg.setCfgDesc("无策略");
			}*/
			initFormCondition(model, cfg);
			model.addAttribute("isAdd", true);
		}
		
		//查询policyGroup列表，供响应策略选择
//		List<PolicyGroupInfo> policyGroups=policyGroupInfoService.findPolicyGroupInfosByType(1);
		List<PolicyGroupInfo> policyGroups=policyGroupInfoService.findHasIpPolicyGroupInfosByType(1);
		model.addAttribute("policyGroups", policyGroups);
		
		model.addAttribute("_cfg", cfg);
		return "/cfg/maintenance/dnsResStrategy/form";
	}
	
	@RequestMapping(value = {"/saveOrUpdate"})
	@RequiresPermissions(value={"dns:res:strategy:config"})
	public String saveOrUpdate(Model model,HttpServletRequest request,HttpServletResponse response,
			@ModelAttribute("cfg")DnsResStrategy cfg
			,RedirectAttributes redirectAttributes){
		try{
			
			dnsResStrategyService.saveOrUpdate(cfg);
			
			addMessage(redirectAttributes,"success","save_success");
		}catch (MaatConvertException e) {
			addMessage(redirectAttributes,"error", "request_service_failed");
		} catch (Exception e) {
			addMessage(redirectAttributes,"error", "save_failed");
		}
		
		return "redirect:" + adminPath +"/maintenance/dnsResStrategy/list?functionId="+cfg.getFunctionId();
	}
	
	@RequestMapping(value = {"/list"})
	public String list(Model model,HttpServletRequest  request,HttpServletResponse response
			,@ModelAttribute("cfg")DnsResStrategy entity
			,RedirectAttributes redirectAttributes){
		//查询时left join policyGroup
		Page<DnsResStrategy> page = dnsResStrategyService.findPage(new Page<DnsResStrategy>(request, response,"r"), entity);
		model.addAttribute("page", page);
		initPageCondition(model,entity);
		return "/cfg/maintenance/dnsResStrategy/list";
	}
	
	@RequestMapping(value = {"/delete"})
	@RequiresPermissions(value={"dns:res:strategy:config"})
	public String delete(Integer isAudit,Integer isValid,String ids,Integer functionId
			,Model model,HttpServletRequest request
			,HttpServletResponse response
			,RedirectAttributes redirectAttributes){
		if(!StringUtil.isEmpty(ids)){
			dnsResStrategyService.delete(isAudit,isValid,ids,functionId);
		}
		
		return "redirect:" + adminPath +"/maintenance/dnsResStrategy/list?functionId="+functionId;
	}
	
	@RequestMapping(value = {"/audit"})
	@RequiresPermissions(value={"dns:res:strategy:confirm"})
	public String audit(Model model,@ModelAttribute("cfg")DnsResStrategy cfg,
							Integer isValid,
							Integer isAudit,
							String ids,
							Integer functionId, 
							RedirectAttributes redirectAttributes,
							HttpServletResponse response,
							HttpServletRequest request) {
		if(!StringUtil.isEmpty(ids)){
			String[] idArray = ids.split(",");
			Date auditTime=new Date();
			for(String id :idArray){
				try {
					dnsResStrategyService.audit(isAudit,isValid,functionId,id,auditTime);
				} catch (MaatConvertException e) {
					addMessage(redirectAttributes,"error", "request_service_failed");
				} catch (Exception e) {
					addMessage(redirectAttributes,"error", "audit_failed");
				}
			}
			
		}else {
			Page<DnsResStrategy> searchPage=new Page<DnsResStrategy>(request,response,"r");
			Page<DnsResStrategy> auditPage=new Page<DnsResStrategy>(request,response,"r");

			try {
				BeanUtils.copyProperties(searchPage, auditPage);
				auditAll(auditPage,isValid , cfg);
			} catch (Exception e) {
				logger.error("配置下发失败：",e);
				if(e instanceof MaatConvertException) {
					addMessage(redirectAttributes,"error", "request_service_failed");
				}else {
					addMessage(redirectAttributes,"error", "audit_failed");
				}
				
			}
			
			return list(model, request, response, cfg,redirectAttributes);
		}
		return "redirect:" + adminPath +"/maintenance/dnsResStrategy/list?functionId="+functionId;
	}
	@ResponseBody
	@RequestMapping(value = "/validCfgId")
	public boolean validCfgId(Long cfgId) {
		DnsResStrategy dns=dnsResStrategyService.getDnsResStrategy(cfgId,null);
		if(dns == null ){
			return false;
		}else{
			return true;
		}
	}
	
	//dns配置导出
	@RequestMapping(value = "exportDns")
	public void exportDns(Model model,HttpServletRequest  request,HttpServletResponse response,
			@ModelAttribute("cfg")DnsResStrategy entity,String ids,RedirectAttributes redirectAttributes){
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
				Page<DnsResStrategy> pageInfo=new Page<DnsResStrategy>(request, response,"r");
				pageInfo.setPageNo(1);
				pageInfo.setPageSize(Constants.MAX_EXPORT_SIZE);
				Page<DnsResStrategy> page = dnsResStrategyService.findPage(pageInfo, entity);
				for (int i = 0; i < page.getList().size(); i++) {
					page.getList().get(i).setMiTtlmax(String.valueOf(page.getList().get(i).getMinTtl())+"-"+String.valueOf(page.getList().get(i).getMaxTtl()));
				}
				titleList.add(entity.getMenuNameCode());
				classMap.put(entity.getMenuNameCode(), DnsResStrategy.class);
				String cfgIndexInfoNoExport=",valid_identifier,do_log,client_port,ir_type,group_name,userregion1,userregion2,userregion3,userregion4,userregion5,";
				noExportMap.put(entity.getMenuNameCode(),cfgIndexInfoNoExport);
				dataMap.put(entity.getMenuNameCode(), page.getList());
			/*}*/
			this._export(model, request, response, redirectAttributes,entity.getMenuNameCode(),titleList,classMap,dataMap,noExportMap);
		} catch (Exception e) {
			logger.error("Dns export failed",e);
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,"error", "request_service_failed");
			}else {
				addMessage(redirectAttributes,"error", "export_failed");
			}
		}
		//return "redirect:" + adminPath +"/ntc/iplist/list?functionId="+entity.getFunctionId();
	}
}
