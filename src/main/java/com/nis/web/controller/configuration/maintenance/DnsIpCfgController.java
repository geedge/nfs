package com.nis.web.controller.configuration.maintenance;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.cxf.common.util.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nis.domain.Page;
import com.nis.domain.basics.PolicyGroupInfo;
import com.nis.domain.basics.ServiceDictInfo;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.DnsIpCfg;
import com.nis.domain.configuration.DnsResStrategy;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.configuration.DnsIpCfg;
import com.nis.domain.configuration.RequestInfo;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.Constants;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseController;
import com.nis.web.security.UserUtils;
import com.nis.web.service.configuration.DnsIpCfgService;

@Controller
@RequestMapping("${adminPath}/cfg/dnsIp")
public class DnsIpCfgController extends BaseController {
	
	@RequestMapping(value = {"/list"})
	public String list(Model model,HttpServletRequest  request
			,HttpServletResponse response,@ModelAttribute("cfg")DnsIpCfg entity
			,RedirectAttributes redirectAttributes){
		Page<DnsIpCfg> page = dnsIpCfgService.findPage(new Page<DnsIpCfg>(request, response,"r"), entity);
		model.addAttribute("page", page);
		initPageCondition(model,entity);
		return "/cfg/dnsIpCfgList";
	}
	@RequestMapping(value = {"/form"})
	@RequiresPermissions(value={"dns:fake:ip:config"})
	public String form(Model model,HttpServletRequest request
			,HttpServletResponse response,String ids,@ModelAttribute("cfg")DnsIpCfg cfg
			,RedirectAttributes redirectAttributes){
		if(!StringUtil.isEmpty(ids)){
			cfg = dnsIpCfgService.getDnsIpCfg(Long.parseLong(ids));
			initUpdateFormCondition(model, cfg);
		}else{
			initFormCondition(model,cfg);
		}
		/*//获取所有响应策略信息
		List<DnsResStrategy> resStrategys=dnsResStrategyService.findDnsResStrategys(null, 1,1);
		model.addAttribute("dnsResStrategys", resStrategys);*/
		//查询policyGroup列表，供响应策略选择 2018-07-24 经确定IP欺骗只能选择非0号分组
		List<PolicyGroupInfo> policyGroups=policyGroupInfoService.findPolicyGroupInfosByType(1);
		model.addAttribute("policyGroups", policyGroups);
		model.addAttribute("_cfg", cfg);
		return "/cfg/dnsIpCfgForm";
	}
	
	@RequestMapping(value = {"/saveOrUpdate"})
	@RequiresPermissions(value={"dns:fake:ip:config"})
	public String saveOrUpdate(Model model,HttpServletRequest request
			,HttpServletResponse response,
			@ModelAttribute("cfg")DnsIpCfg cfg
			,RedirectAttributes redirectAttributes){
		try{
			dnsIpCfgService.saveOrUpdate(cfg);
			addMessage(redirectAttributes,"success","save_success");
		}catch(Exception e){
			logger.error("信息保存失败",e);
			e.printStackTrace();
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,"error","request_service_failed");
			}else {
				addMessage(redirectAttributes,"error","save_failed");
			}
		}
		
		return "redirect:" + adminPath +"/cfg/dnsIp/list?functionId="+cfg.getFunctionId();
	}
	
	@RequestMapping(value = {"/delete"})
	@RequiresPermissions(value={"dns:fake:ip:config"})
	public String delete(Integer isAudit,Integer isValid
			,String ids,Integer functionId
			,RedirectAttributes redirectAttributes){
		dnsIpCfgService.delete(isAudit,isValid,ids,functionId);
		return "redirect:" + adminPath +"/cfg/dnsIp/list?functionId="+functionId;
	}
	@RequestMapping(value = {"/audit"})
	@RequiresPermissions(value={"dns:fake:ip:confirm"})
	public String audit(Model model,@ModelAttribute("cfg")DnsIpCfg cfg,
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
					dnsIpCfgService.audit(isAudit,isValid,functionId,id,auditTime);
				} catch (MaatConvertException e) {
					e.printStackTrace();
					logger.info("dns fake ip配置下发失败："+e.getMessage());
					addMessage(redirectAttributes,"error","request_service_failed");
				} catch (Exception e) {
					e.printStackTrace();
					logger.info("dns fake ip配置下发失败："+e.getMessage());
					addMessage(redirectAttributes,"error", "audit_failed");
				}
				
			}
			
		}else {
			Page<DnsIpCfg> searchPage=new Page<DnsIpCfg>(request,response,"r");
			Page<DnsIpCfg> auditPage=new Page<DnsIpCfg>(request,response,"r");

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
			
			return list(model, request, response,cfg, redirectAttributes);
		}
		return "redirect:" + adminPath +"/cfg/dnsIp/list?functionId="+functionId;
	}
	
	
	//dnsIp配置导出
		@RequestMapping(value = "exportDnsIp")
		public void exportDns(Model model,HttpServletRequest  request,HttpServletResponse response,
				@ModelAttribute("cfg")DnsIpCfg entity,String ids,RedirectAttributes redirectAttributes){
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
					Page<DnsIpCfg> pageInfo=new Page<DnsIpCfg>(request, response,"r");
					pageInfo.setPageNo(1);
					pageInfo.setPageSize(Constants.MAX_EXPORT_SIZE);
					Page<DnsIpCfg> page = dnsIpCfgService.findPage(pageInfo, entity);
					for (int i = 0; i < page.getList().size(); i++) {
					    if(StringUtil.isEmpty(page.getList().get(i).getDnsStrategyName())){
					    	Properties msgProp = getMsgProp();
					    	page.getList().get(i).setDnsStrategyName(msgProp.getProperty("default_group"));
					    }
					}
					model.addAttribute("page", page);
					titleList.add(entity.getMenuNameCode());
					classMap.put(entity.getMenuNameCode(), DnsIpCfg.class);
					String cfgIndexInfoNoExport=",whether_area_block,client_ip,port_pattern,client_port,server_port,ir_type,direction,protocol,do_log,client_port,ir_type,group_name,userregion1,userregion2,userregion3,userregion4,userregion5,";
					noExportMap.put(entity.getMenuNameCode(),cfgIndexInfoNoExport);
					dataMap.put(entity.getMenuNameCode(), page.getList());
				/*}*/
				this._export(model, request, response, redirectAttributes,entity.getMenuNameCode(),titleList,classMap,dataMap,noExportMap);
			} catch (Exception e) {
				logger.error("DnsIp export failed",e);
				if(e instanceof MaatConvertException) {
					addMessage(redirectAttributes,"error", "request_service_failed");
				}else {
					addMessage(redirectAttributes,"error", "export_failed");
				}
			}
			//return "redirect:" + adminPath +"/ntc/iplist/list?functionId="+entity.getFunctionId();
		}
}
