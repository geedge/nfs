package com.nis.web.controller.configuration.ntc;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nis.domain.Page;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.HttpUrlCfg;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.configuration.NtcSubscribeIdCfg;
import com.nis.domain.configuration.template.WhiteListIpTemplate;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.Constants;
import com.nis.util.StringUtil;
import com.nis.web.controller.configuration.CommonController;
import com.nis.web.security.UserUtils;

/**
 * 白名单
 * @author dell
 *
 */
@Controller
@RequestMapping("${adminPath}/ntc/whitelist")
public class WhiteListController extends CommonController{
	
	@RequestMapping(value = {"ip/list"})
	//@RequiresPermissions(value={"whitelist:ip:config","whitelist:ip:confirm","whitelist:ip:aduit"},logical=Logical.OR)
	public String ipList(Model model,@ModelAttribute("cfg")CfgIndexInfo cfg,HttpServletRequest request,HttpServletResponse response) {
		Page<CfgIndexInfo> searchPage=new Page<CfgIndexInfo>(request,response,"a");
		Page<CfgIndexInfo> page = ipCfgService.getIpCfgList(searchPage, cfg);
		model.addAttribute("page", page);
		initPageCondition(model,cfg);
		return "/cfg/whitelist/ipList";
	}
	@RequestMapping(value = {"ajaxIpSubList"})
	public String ajaxIpSubList(Model model,Long cfgId,Integer index,Integer compileId) {
		CfgIndexInfo cfg = ipCfgService.getIpPortCfg(cfgId,compileId);
		List<String[]> tabList = new ArrayList();
		if(cfg.getIpPortList()!=null){
			String cfgType = null;
			for(IpPortCfg ip:cfg.getIpPortList()){
				if(!ip.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"1",ip.getCfgType()});
					cfgType = ip.getCfgType();
				}
			}
		}
		if(cfg.getNtcSubscribeIdCfgList()!=null){
			String cfgType = null;
			for(NtcSubscribeIdCfg keyword:cfg.getNtcSubscribeIdCfgList()){
				if(!keyword.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"2",keyword.getCfgType()});
					cfgType = keyword.getCfgType();
				}
			}
		}
		model.addAttribute("_cfg", cfg);	
		model.addAttribute("index", index);
		model.addAttribute("tabList", tabList);
		return "/cfg/whitelist/ipSubList";
	}
	@RequestMapping(value = {"ip/form"})
	@RequiresPermissions(value={"whitelist:ip:config"})
	public String ipForm(Model model,String ids,CfgIndexInfo entity) {
		if(StringUtils.isNotBlank(ids)){
			
			entity = this.ipCfgService.getIpPortCfg(Long.parseLong(ids),null);
			initUpdateFormCondition(model,entity);
		}else{
			initFormCondition(model,entity);
		}
		model.addAttribute("_cfg", entity);
		return "/cfg/whitelist/ipForm";
	}
	@RequestMapping(value = {"ip/save"})
	public String saveIpCfg(RedirectAttributes model,HttpServletRequest request,HttpServletResponse response,String ids,CfgIndexInfo entity) {
		ipCfgService.saveIpCfg(entity);
		return "redirect:" + adminPath +"/ntc/whitelist/ip/list?functionId="+entity.getFunctionId();
	}
	@RequestMapping(value = {"ip/audit"})
	@RequiresPermissions("whitelist:ip:confirm")
	public String auditIp(Model model,@ModelAttribute("cfg")CfgIndexInfo cfg,
							Integer isValid,
							Integer isAudit,
							String ids,
							Integer functionId, 
							RedirectAttributes redirectAttributes,
							HttpServletResponse response,
							HttpServletRequest request
							) {
		if(!StringUtil.isEmpty(ids)){
			CfgIndexInfo entity = new CfgIndexInfo();
			String[] idArray = ids.split(",");
			for(String id :idArray){
				entity = ipCfgService.getIpPortCfg(Long.parseLong(id),null);
				entity.setIsAudit(isAudit);
				entity.setIsValid(isValid);
				entity.setAuditorId(UserUtils.getUser().getId());
				entity.setAuditTime(new Date());
				entity.setFunctionId(functionId);
				try {
					ipCfgService.auditIpCfg(entity,isAudit);
					addMessage(redirectAttributes,"success", "audit_success");
				} catch (MaatConvertException e) {
					logger.info("ip配置下发失败："+e.getMessage());
					addMessage(redirectAttributes,"error", "request_service_failed");
				}catch (Exception e) {
					logger.info("ip配置下发失败："+e.getMessage());
					addMessage(redirectAttributes,"error", "audit_failed");
				}
			}
		}else{
			Page<CfgIndexInfo> searchPage=new Page<CfgIndexInfo>(request,response,"a");
			Page<CfgIndexInfo> auditPage=new Page<CfgIndexInfo>(request,response,"a");
			BeanUtils.copyProperties(searchPage, auditPage);
			
			try {
				auditAll(auditPage,isValid , cfg);
			} catch (Exception e) {
				logger.error("配置下发失败：",e);
				if(e instanceof MaatConvertException) {
					addMessage(redirectAttributes,"error", "request_service_failed");
				}else {
					addMessage(redirectAttributes,"error", "audit_failed");
				}
				
			}
			
			return ipList(model, cfg, request, response);
		}
		return "redirect:" + adminPath +"/ntc/whitelist/ip/list?functionId="+functionId;
	}
	@RequestMapping(value = {"updateIpValid"})
	@RequiresPermissions(value={"whitelist:ip:config"})
	public String updateIpCfgValid(Integer isValid,String ids,Integer functionId) {
		ipCfgService.updateIpCfgValid(isValid,ids,functionId);
		return "redirect:" + adminPath +"/ntc/whitelist/ip/list?functionId="+functionId;
	}
	@RequestMapping(value = {"domain/list"})
	//@RequiresPermissions(value={"whitelist:domain:config","whitelist:domain:confirm","whitelist:domain:aduit"},logical=Logical.OR)
	public String domainList(Model model,@ModelAttribute("cfg")CfgIndexInfo cfg,HttpServletRequest request,HttpServletResponse response) {
		Page<CfgIndexInfo> searchPage=new Page<CfgIndexInfo>(request,response,"a");
		Page<CfgIndexInfo> page = domainService.getDomainList(searchPage, cfg);
		model.addAttribute("page", page);
		initPageCondition(model,cfg);
		return "/cfg/whitelist/domainList";
	}
	
	@RequestMapping(value = {"saveHttpUrlCfgs"})
	public String saveHttpUrlCfgs(RedirectAttributes model,HttpServletRequest request,HttpServletResponse response,CfgIndexInfo entity) {
		if(!StringUtil.isEmpty(entity) && !StringUtil.isEmpty(entity.getHttpUrlList())){
			
			CfgIndexInfo sourceCfg=websiteCfgService.getCfgIndexInfo(entity.getHttpUrlList().get(0).getSourceCompileId());
			for (HttpUrlCfg httpUrlCfg : entity.getHttpUrlList()) {
				CfgIndexInfo cfg=new CfgIndexInfo();
				List httpList=new ArrayList<>();
				httpUrlCfg.setRequestId(sourceCfg.getRequestId());
				BeanUtils.copyProperties(httpUrlCfg, cfg);
				httpList.add(httpUrlCfg);
				cfg.setHttpUrlList(httpList);
				websiteCfgService.saveHttpCfg(cfg);
			}
		}
		return "redirect:" + adminPath +"/ntc/whitelist/domain/list?functionId="+entity.getFunctionId();
	}
	
	@RequestMapping(value = {"domain/form"})
	@RequiresPermissions(value={"whitelist:domain:config"})
	public String domainForm(Model model,String ids,CfgIndexInfo entity) {
		if(StringUtils.isNotBlank(ids)){
			entity = domainService.getDomainCfg(Long.parseLong(ids),null);
			HttpUrlCfg urlCfg = new HttpUrlCfg();
			urlCfg.setCfgType(Constants.HTTP_URL_REGION);
			entity.setHttpUrl(urlCfg);
			if(entity.getHttpUrlList().size()==0){
				entity.getHttpUrlList().add(urlCfg);
			}
			initUpdateFormCondition(model,entity);
		}else{
			HttpUrlCfg urlCfg = new HttpUrlCfg();
			urlCfg.setCfgType(Constants.HTTP_URL_REGION);
			entity.setHttpUrl(urlCfg);
			List<HttpUrlCfg> urlList=new ArrayList<HttpUrlCfg>();
			urlList.add(urlCfg);
			entity.setHttpUrlList(urlList);
			initFormCondition(model,entity);
		}
		model.addAttribute("_cfg", entity);
		return "/cfg/whitelist/domainForm";
	}
	@RequestMapping(value = {"domain/save"})
	public String saveOrUpdateDomain(RedirectAttributes model,HttpServletRequest request,HttpServletResponse response,String ids,CfgIndexInfo entity) {
		domainService.saveDomainCfg(entity);
		return "redirect:" + adminPath +"/ntc/whitelist/domain/list?functionId="+entity.getFunctionId();
	}
	@RequestMapping(value = {"domain/ajaxSubList"})
	public String ajaxDomainSubList(Model model,Long cfgId,Integer index,Integer compileId) {
		CfgIndexInfo cfg = domainService.getDomainCfg(cfgId,compileId);
		List<String[]> tabList = new ArrayList();
		if(cfg.getHttpUrlList()!=null){
			String cfgType = null;
			for(BaseStringCfg keyword:cfg.getHttpUrlList()){
				if(!keyword.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"2",keyword.getCfgType()});
					cfgType = keyword.getCfgType();
				}
			}
		}
		if(cfg.getNtcSubscribeIdCfgList()!=null){
			String cfgType = null;
			for(NtcSubscribeIdCfg keyword:cfg.getNtcSubscribeIdCfgList()){
				if(!keyword.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"2",keyword.getCfgType()});
					cfgType = keyword.getCfgType();
				}
			}
		}
		model.addAttribute("_cfg", cfg);	
		model.addAttribute("index", index);
		model.addAttribute("tabList", tabList);
		return "/cfg/whitelist/domianSubList";
	}
	@RequestMapping(value = {"domain/updateValid"})
	@RequiresPermissions(value={"whitelist:domain:config"})
	public String updateDomainValid(Integer isValid,String ids,Integer functionId) {
		domainService.updateDomainCfgValid(isValid,ids,functionId);
		return "redirect:" + adminPath +"/ntc/whitelist/domain/list?functionId="+functionId;
	}
	@RequestMapping(value = {"domain/delete"})
	@RequiresPermissions("whitelist:domain:config")
	public String deleteDomain(String ids,Integer functionId,RedirectAttributes model) {
		try{
			domainService.deleteWhiteDomain(ids);
			addMessage(model,"success","delete_success");
		}catch(Exception e){
			logger.error("删除失败", e);
			addMessage(model,"error","delete_failed");
		}
		return "redirect:" + adminPath +"/ntc/whitelist/domain/list?functionId="+functionId;
	}
	
	@RequestMapping(value = {"domain/audit"})
	@RequiresPermissions("whitelist:domain:confirm")
	public String auditDomain(Model model,@ModelAttribute("cfg")CfgIndexInfo cfg,
										Integer isValid,
										Integer isAudit,
										String ids,
										Integer functionId, 
										RedirectAttributes redirectAttributes,
										HttpServletResponse response,
										HttpServletRequest request) {
		if(!StringUtil.isEmpty(ids)) {
			CfgIndexInfo entity = new CfgIndexInfo();
			String[] idArray = ids.split(",");
			for(String id :idArray){
				entity = domainService.getDomainCfg(Long.parseLong(id),null);
				entity.setIsAudit(isAudit);
				entity.setIsValid(isValid);
				entity.setAuditorId(UserUtils.getUser().getId());
				entity.setAuditTime(new Date());
				entity.setFunctionId(functionId);
				try {
					domainService.auditDomainCfg(entity,isAudit);
				} catch (MaatConvertException e) {
					e.printStackTrace();
					logger.error("domain配置下发失败："+e.getMessage());
					addMessage(redirectAttributes,"error","request_service_failed");
				}catch (Exception e) {
					e.printStackTrace();
					logger.error("domain配置下发失败："+e.getMessage());
					addMessage(redirectAttributes,"error","audit_failed");
				}
			}
		}else{//条件下所有配置审核
			Page<CfgIndexInfo> searchPage=new Page<CfgIndexInfo>(request,response,"a");
			Page<CfgIndexInfo> auditPage=new Page<CfgIndexInfo>(request,response,"a");
			BeanUtils.copyProperties(searchPage, auditPage);
			
			try {
				auditAll(auditPage,isValid , cfg);
			} catch (Exception e) {
				logger.error("配置下发失败：",e);
				if(e instanceof MaatConvertException) {
					addMessage(redirectAttributes,"error", "request_service_failed");
				}else {
					addMessage(redirectAttributes,"error", "audit_failed");
				}
				
			}
			
			return domainList(model, cfg, request, response);
		}
		return "redirect:" + adminPath +"/ntc/whitelist/domain/list?functionId="+functionId;
	}
	//ip配置导入
	/*@RequestMapping(value = "ip/import", method=RequestMethod.POST)
	public String importIp(String cfgName,RedirectAttributes redirectAttributes,
			@RequestParam("file") MultipartFile file,IpPortCfg cfg) {
		this._importIp(cfgName,redirectAttributes, file,cfg,WhiteListIpTemplate.class);
		return "redirect:" + adminPath +"/ntc/whitelist/ip/list?functionId="+cfg.getFunctionId();
	}*/
	//ip模板下载
	@RequestMapping(value = "ip/import/template")
	public void importFileTemplate(HttpServletRequest request,HttpServletResponse response, 
    		RedirectAttributes redirectAttributes,Integer functionId,Integer cfgRegionCode) {
		this._importFileTemplate(request, response, redirectAttributes, functionId, cfgRegionCode,WhiteListIpTemplate.class);
	}
	//ip配置导出
	@RequestMapping(value = "ip/export")
	public void exportIp(String columns,Model model,HttpServletRequest  request,HttpServletResponse response,
			@ModelAttribute("cfg")IpPortCfg entity,String ids,RedirectAttributes redirectAttributes){
		this._exportIp(columns,model, request, response, entity, ids, redirectAttributes);
	}
	
	//ip配置导出
			@RequestMapping(value = "exportIpAddr")
			public void exportIpAddr(Model model,HttpServletRequest  request,HttpServletResponse response,
					@ModelAttribute("cfg")CfgIndexInfo entity,String ids,RedirectAttributes redirectAttributes){
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
						Page<CfgIndexInfo> pageInfo=new Page<CfgIndexInfo>(request, response,"a");
						pageInfo.setPageNo(1);
						pageInfo.setPageSize(Constants.MAX_EXPORT_SIZE);
						Page<CfgIndexInfo> page = ipCfgService.getIpCfgList(pageInfo, entity);
						titleList.add(entity.getMenuNameCode());
						titleList.add("NTC_IP");
						classMap.put(entity.getMenuNameCode(), CfgIndexInfo.class);
						classMap.put("NTC_IP", IpPortCfg.class);
						String cfgIndexInfoNoExport=",policy_name,do_log,letter,whether_area_block,classification,attribute,label,group_name,userregion1,userregion2,userregion3,userregion4,userregion5,";
						String ipPortInfoNoExport=",protocol,direction,do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
												+ ",config_time,editor,edit_time,auditor,audit_time"
												+",letter,whether_area_block,classification,attribute,label"
												+",userregion1,userregion2,userregion3,userregion4,userregion5,ir_type,group_name,";
						noExportMap.put(entity.getMenuNameCode(),cfgIndexInfoNoExport);
						noExportMap.put("NTC_IP", ipPortInfoNoExport);
						List<IpPortCfg> ipList=new ArrayList<IpPortCfg>();
						for (CfgIndexInfo cfg : page.getList()) {
							CfgIndexInfo cfgIndexInfo=ipCfgService.exportIpInfo(cfg);
							ipList.addAll(cfgIndexInfo.getIpPortList());
						}
						dataMap.put(entity.getMenuNameCode(), page.getList());
						dataMap.put("NTC_IP", ipList);
					/*}*/
					this._export(model, request, response, redirectAttributes,entity.getMenuNameCode(),titleList,classMap,dataMap,noExportMap);
				} catch (Exception e) {
					logger.error("ip white export failed",e);
					addMessage(redirectAttributes,"error",  "export_failed");
				}
				//return "redirect:" + adminPath +"/ntc/iplist/list?functionId="+entity.getFunctionId();
			}
			//ip配置导出
					@RequestMapping(value = "exportdomain")
					public void exportdomain(Model model,HttpServletRequest  request,HttpServletResponse response,
							@ModelAttribute("cfg")CfgIndexInfo entity,String ids,RedirectAttributes redirectAttributes){
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
								Page<CfgIndexInfo> pageInfo=new Page<CfgIndexInfo>(request, response,"a");
								pageInfo.setPageNo(1);
								pageInfo.setPageSize(Constants.MAX_EXPORT_SIZE);
								Page<CfgIndexInfo> page = domainService.getDomainList(pageInfo, entity);
								titleList.add(entity.getMenuNameCode());
								titleList.add("NTC_HTTP_URL");
								classMap.put(entity.getMenuNameCode(), CfgIndexInfo.class);
								classMap.put("NTC_HTTP_URL", HttpUrlCfg.class);
								String cfgIndexInfoNoExport=",policy_name,group_name,userregion1,userregion2,userregion3,userregion4,userregion5,";
								String httpUrlInfoNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
										+ ",config_time,editor,edit_time,auditor,audit_time"
										+",letter,whether_area_block,classification,attribute,label"
										+",userregion1,userregion2,userregion3,userregion4,userregion5,";
								noExportMap.put(entity.getMenuNameCode(),cfgIndexInfoNoExport);
								noExportMap.put("NTC_HTTP_URL", httpUrlInfoNoExport);
								List<BaseStringCfg> httpurlList=new ArrayList<BaseStringCfg>();
								for (CfgIndexInfo cfg : page.getList()) {
									CfgIndexInfo cfgIndexInfo=domainService.exportDomainInfo(cfg);
									httpurlList.addAll(cfgIndexInfo.getHttpUrlList());
								}
								httpurlList=BaseStringCfg.replaceBaseKeyList(httpurlList);
								dataMap.put(entity.getMenuNameCode(), page.getList());
								dataMap.put("NTC_HTTP_URL", httpurlList);
							/*}*/
							this._export(model, request, response, redirectAttributes,entity.getMenuNameCode(),titleList,classMap,dataMap,noExportMap);
						} catch (Exception e) {
							logger.error("ip white export failed",e);
							addMessage(redirectAttributes,"error",  "export_failed");
						}
						//return "redirect:" + adminPath +"/ntc/iplist/list?functionId="+entity.getFunctionId();
					}
	
}
