package com.nis.web.controller.configuration.proxy;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import com.nis.domain.configuration.InterceptPktBin;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.configuration.PxyObjKeyring;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.Constants;
import com.nis.util.DictUtils;
import com.nis.util.StringUtil;
import com.nis.web.controller.configuration.CommonController;
import com.nis.web.security.UserUtils;

/**
 * IP相关配置控制类
 * @author dell
 *
 */
@Controller
@RequestMapping(value={"${adminPath}/proxy/intercept"})
public class InterceptController extends CommonController{
	
	@RequestMapping(value = {"/ip/list","/domain/list","/ippayload/list"})
	public String ipList(Model model,@ModelAttribute("cfg")CfgIndexInfo cfg,HttpServletRequest request,HttpServletResponse response) {
		Page<CfgIndexInfo> searchPage=new Page<CfgIndexInfo>(request,response,"a");
		Page<CfgIndexInfo> page = websiteCfgService.getWebsiteList(searchPage, cfg);
		model.addAttribute("page", page);
		initPageCondition(model,cfg);
		
		//获取证书信息
		List<PxyObjKeyring> certificateList=new ArrayList<PxyObjKeyring>();
		if(cfg.getFunctionId().equals(200)){
			certificateList=pxyObjKeyringService.findPxyObjKeyrings(null, 1, 1, "ip");
		}
		if(cfg.getFunctionId().equals(201)){
			certificateList=pxyObjKeyringService.findPxyObjKeyrings(null, 1, 1, "domain");
		}
		model.addAttribute("certificateList", certificateList);
		
		return "/cfg/intercept/interceptList";
	}
	@RequestMapping(value = {"/interceptIpForm","interceptDomainForm","interceptIpPayloadForm"})
	public String interceptIpForm(Model model,String ids,CfgIndexInfo entity) {
		if(StringUtils.isNotBlank(ids)){
			entity = interceptCfgService.getInterceptCfg(Long.parseLong(ids),null);
			initUpdateFormCondition(model,entity);
		}else{
			initFormCondition(model,entity);
		}
		//获取证书信息
		List<PxyObjKeyring> certificateList=new ArrayList<PxyObjKeyring>();
		if(entity.getFunctionId().equals(200)){
			certificateList=pxyObjKeyringService.findPxyObjKeyrings(null, 1, 1, "ip");
		}
		if(entity.getFunctionId().equals(201)){
			certificateList=pxyObjKeyringService.findPxyObjKeyrings(null, 1, 1, "domain");
		}
		model.addAttribute("certificateList", certificateList);
		
		model.addAttribute("_cfg", entity);
		return "/cfg/intercept/interceptForm";
	}
	@RequestMapping(value = {"/ip/saveOrUpdate","/domain/saveOrUpdate","/ippayload/SaveOrUpdate"})
	public String saveInterceptIpCfg(RedirectAttributes redirectAttributes,Model model,HttpServletRequest request,HttpServletResponse response,String ids,CfgIndexInfo entity) {
		try {
			interceptCfgService.saveInterceptCfg(entity);
			addMessage(redirectAttributes,"success","save_success");
		} catch (Exception e) {
			logger.error("save_failed", e);
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,"error","request_service_failed");
			}else {
				addMessage(redirectAttributes,"error","save_failed");
			}
		}
		return "redirect:" + adminPath +"/proxy/intercept/ip/list?functionId="+entity.getFunctionId();
	}
	
	@RequestMapping(value = {"/ajaxInterceptIpSubList","/ajaxInterceptDomainSubList","/ajaxInterceptIpPayloadSubList"})
	public String ajaxInterceptIpSubList(Model model,Long cfgId,Integer index,Integer compileId) {
		CfgIndexInfo cfg = interceptCfgService.getInterceptCfg(cfgId,compileId);
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
		if(cfg.getInterceptPktBinList()!=null){
			String cfgType = null;
			for(BaseStringCfg keyword:cfg.getInterceptPktBinList()){
				if(!keyword.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"2",keyword.getCfgType()});
					cfgType = keyword.getCfgType();
				}
			}
		}
		if(cfg.getHttpUrlList()!=null){
			String cfgType = null;
			for(BaseStringCfg keyword:cfg.getHttpUrlList()){
				if(!keyword.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"2",keyword.getCfgType()});
					cfgType = keyword.getCfgType();
				}
			}
		}
		model.addAttribute("_cfg", cfg);	
		model.addAttribute("index", index);
		model.addAttribute("tabList", tabList);
		return "/cfg/intercept/interceptSubList";
	}
	
	@RequestMapping(value = {"auditInterceptIpCfg","auditInterceptDomainCfg","auditInterceptIpPayloadCfg"})
	public String auditInterceptIpCfg(Model model,@ModelAttribute("cfg")CfgIndexInfo cfg,
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
				entity = interceptCfgService.getInterceptCfg(Long.parseLong(id),null);
				entity.setIsAudit(isAudit);
				entity.setIsValid(isValid);
				entity.setAuditorId(UserUtils.getUser().getId());
				entity.setAuditTime(new Date());
				entity.setFunctionId(functionId);
				try {
					interceptCfgService.auditInterceptIpCfg(entity,isAudit);
					addMessage(redirectAttributes,"success","audit_success");
				} catch ( Exception e) {
					logger.error("intercept ip audit failed",e);
					if(e instanceof MaatConvertException) {
						addMessage(redirectAttributes,"error", "request_service_failed");
					}else {
						addMessage(redirectAttributes,"error", "audit_failed");
					}
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
			
			return ipList(model, cfg, request, response);
		}
		return "redirect:" + adminPath +"/proxy/intercept/ip/list?functionId="+functionId;
	}
	
	@RequestMapping(value = {"updateInterceptIpValid","updateInterceptDomainValid","updateInterceptIpPayloadValid"})
	public String updateInterceptIpValid(Integer isValid,String ids,Integer functionId, RedirectAttributes redirectAttributes) {
		try {
			interceptCfgService.updatInterceptValid(isValid,ids,functionId);
			addMessage(redirectAttributes,"error","delete_success");
		} catch (Exception e) {
			logger.error("intercept ip delete failed",e);
			addMessage(redirectAttributes,"error","delete_failed");
		}
		return "redirect:" + adminPath +"/proxy/intercept/ip/list?functionId="+functionId;
	}
	
	/*
	//ip配置导入
	@RequestMapping(value = "/ip/import", method=RequestMethod.POST)
	public String importIp(String cfgName,RedirectAttributes redirectAttributes,
			@RequestParam("file") MultipartFile file,IpPortCfg cfg) {
		this._importIp(cfgName,redirectAttributes, file,cfg,IpAddrTemplate.class);
		redirectAttributes.addAttribute("urlPrefix","/proxy/intercept/ip");
		redirectAttributes.addAttribute("requiresPermissionPrefix","intercept:ip");
		return "redirect:" + adminPath +"/proxy/intercept/ip/list?functionId="+cfg.getFunctionId();
	}
	@RequestMapping(value = "/ip/import/template")
	public void importFileTemplate(HttpServletRequest request,HttpServletResponse response, 
    		RedirectAttributes redirectAttributes,Integer functionId,Integer cfgRegionCode) {
		this._importFileTemplate(request, response, redirectAttributes, functionId, cfgRegionCode,IpAddrTemplate.class);
	}
	//ip配置导出
	@RequestMapping(value = "/ip/export")
	public void exportIp(String columns,Model model,HttpServletRequest  request,HttpServletResponse response,
			@ModelAttribute("cfg")IpPortCfg entity,String ids,RedirectAttributes redirectAttributes){
		this._exportIp(columns,model, request, response, entity, ids, redirectAttributes);
	}*/
	
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
				Map<String, String> replaceExportMap=new HashMap<String, String>();
				
				
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
					
					//获取证书信息
					List<PxyObjKeyring> certificateList=new ArrayList<PxyObjKeyring>();
					if(entity.getFunctionId().equals(200)){
						certificateList=pxyObjKeyringService.findPxyObjKeyrings(null, 1, 1, "ip");
					}
					if(entity.getFunctionId().equals(201)){
						certificateList=pxyObjKeyringService.findPxyObjKeyrings(null, 1, 1, "domain");
					}
					for (CfgIndexInfo str : page.getList()) {
						if(!StringUtil.isEmpty(str.getUserRegion5())){
							Properties prop = getMsgProp();
							 String cs= DictUtils.getDictLabel("INTERCEPT_DOMAIN_INTENSITY", str.getUserRegion5());
							 str.setUserRegion5(prop.getProperty(cs));
						}
						if(certificateList!=null){
						if(entity.getFunctionId()==200|| entity.getFunctionId()==201){
							 if(!StringUtil.isEmpty(str.getUserRegion1())){
								 boolean flag=false;
						     for (PxyObjKeyring pxyObjKeyring : certificateList) {
								 if(str.getUserRegion1().equals(String.valueOf(pxyObjKeyring.getCompileId()))){
									 str.setUserRegion1(pxyObjKeyring.getCfgDesc());
									 flag=true;
									 break;
								   }
						    	 }
						        if(!flag){
						        	str.setUserRegion1(null);
						        }
						       } 
							}
						
						}
					}
					titleList.add(entity.getMenuNameCode());
					classMap.put(entity.getMenuNameCode(), CfgIndexInfo.class);
					String cfgIndexInfoNoExport="";
					String ipPortInfoNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
											+ ",config_time,editor,edit_time,auditor,audit_time"
											+",letter,whether_area_block,classification,attribute,label"
											+",userregion1,userregion2,userregion3,userregion4,userregion5,ir_type,group_name,";
					String interceptNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
							+ ",config_time,editor,edit_time,auditor,audit_time"
							+",letter,whether_area_block,classification,attribute,label"
							+",userregion1,userregion2,userregion3,userregion4,userregion5,ir_type,group_name,";
					String httpUrlCfgNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
							+ ",config_time,editor,edit_time,auditor,audit_time"
							+",letter,whether_area_block,classification,attribute,label"
							+",userregion1,userregion2,userregion3,userregion4,userregion5,";
					
					List<IpPortCfg> ipList=new ArrayList<IpPortCfg>();
					List<BaseStringCfg> httpUrlList=new ArrayList<BaseStringCfg>();
					List<BaseStringCfg> pktBinList=new ArrayList<BaseStringCfg>();
					for (CfgIndexInfo cfg : page.getList()) {
						 Map<String, List> maps=interceptCfgService.exportIpInfo(cfg);
						 httpUrlList.addAll(maps.get("NTC_HTTP_URL"));
						 ipList.addAll(maps.get("PXY_INTERCEPT_IP"));
						 pktBinList.addAll(maps.get("PXY_INTERCEPT_PKT_BIN"));
					}
					pktBinList=BaseStringCfg.replaceBaseKeyList(pktBinList);
					httpUrlList=BaseStringCfg.baseHexList(httpUrlList);
					dataMap.put(entity.getMenuNameCode(), page.getList());
					if(entity.getFunctionId()==212){  //IP Payload
						cfgIndexInfoNoExport=",do_log,log_total,policy_name,group_name,userregion4,userregion5,&userregion1:replace_zone-userregion2:replaced_content-userregion3:replace_content-";
						titleList.add("PXY_INTERCEPT_IP");
						titleList.add("PXY_INTERCEPT_PKT_BIN");
						classMap.put("PXY_INTERCEPT_IP", IpPortCfg.class);
						classMap.put("PXY_INTERCEPT_PKT_BIN", InterceptPktBin.class);
						noExportMap.put("PXY_INTERCEPT_IP", ipPortInfoNoExport);
						noExportMap.put("PXY_INTERCEPT_PKT_BIN", interceptNoExport);
						dataMap.put("PXY_INTERCEPT_IP", ipList);
						dataMap.put("PXY_INTERCEPT_PKT_BIN", pktBinList);
					}else if(entity.getFunctionId()==200){//IP拦截
						cfgIndexInfoNoExport=",policy_name,group_name,userregion3,userregion4,userregion5,&userregion2:ratelimit-userregion1:certificate-";
						titleList.add("PXY_INTERCEPT_IP");
						classMap.put("PXY_INTERCEPT_IP", IpPortCfg.class);
						noExportMap.put("PXY_INTERCEPT_IP", ipPortInfoNoExport);
						dataMap.put("PXY_INTERCEPT_IP", ipList);
					}else if(entity.getFunctionId()==201){//域名拦截
						cfgIndexInfoNoExport=",policy_name,group_name,userregion3,userregion4,&userregion2:ratelimit-userregion1:certificate-userregion5:intercept_intensity-";
						titleList.add("NTC_HTTP_URL");
						classMap.put("NTC_HTTP_URL", HttpUrlCfg.class);
						noExportMap.put("NTC_HTTP_URL", httpUrlCfgNoExport);
						dataMap.put("NTC_HTTP_URL", httpUrlList);
					}
					noExportMap.put(entity.getMenuNameCode(),cfgIndexInfoNoExport);
					
				/*}*/
				this._export(model, request, response, redirectAttributes,entity.getMenuNameCode(),titleList,classMap,dataMap,noExportMap);
			} catch (Exception e) {
				logger.error("ip addr export failed",e);
				addMessage(redirectAttributes,"error","export_failed");
			}
			//return "redirect:" + adminPath +"/ntc/iplist/list?functionId="+entity.getFunctionId();
		}
		
}
