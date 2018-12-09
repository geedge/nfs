package com.nis.web.controller.configuration.ntc;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.taglibs.standard.functions.Functions;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nis.domain.FunctionRegionDict;
import com.nis.domain.Page;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.ComplexkeywordCfg;
import com.nis.domain.configuration.DnsResStrategy;
import com.nis.domain.configuration.ComplexStringCfgTemplate;
import com.nis.domain.configuration.template.IpCfgTemplate;
import com.nis.domain.specific.ConfigGroupInfo;
import com.nis.domain.configuration.StringCfgTemplate;
import com.nis.domain.configuration.HttpBodyCfg;
import com.nis.domain.configuration.HttpReqHeadCfg;
import com.nis.domain.configuration.HttpResHeadCfg;
import com.nis.domain.configuration.HttpUrlCfg;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.configuration.NtcSubscribeIdCfg;
import com.nis.exceptions.CallExternalProceduresException;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.Constants;
import com.nis.util.DictUtils;
import com.nis.util.StringUtil;
import com.nis.util.excel.ImportExcel;
import com.nis.web.controller.BaseController;
import com.nis.web.security.UserUtils;

/**
 * 网站配置
 * @author dell
 *
 */
@Controller
@RequestMapping("${adminPath}/ntc/website/")
public class WebsiteController extends BaseController{
	
	@RequestMapping(value = {"httpList"})
//	@RequiresPermissions(value={"website:http:config"})
	public String httpList(Model model,@ModelAttribute("cfg")CfgIndexInfo cfg,HttpServletRequest request,HttpServletResponse response) {
		Page<CfgIndexInfo> searchPage=new Page<CfgIndexInfo>(request,response,"a");
		Page<CfgIndexInfo> page = websiteCfgService.getWebsiteList(searchPage, cfg);
		model.addAttribute("page", page);
		initPageCondition(model,cfg);
		return "/cfg/website/httpList";
	}
	@RequestMapping(value = {"dnsList"})
//	@RequiresPermissions(value={"website:dns:config"})
	public String dnsList(Model model,@ModelAttribute("cfg")CfgIndexInfo cfg,HttpServletRequest request,HttpServletResponse response) {
		Page<CfgIndexInfo> searchPage=new Page<CfgIndexInfo>(request,response,"a");
		Page<CfgIndexInfo> page = websiteCfgService.getWebsiteList(searchPage, cfg);
		model.addAttribute("page", page);
		initPageCondition(model,cfg);
		return "/cfg/website/dnsList";
	}
	
	@RequestMapping(value = {"httpForm"})
	@RequiresPermissions(value={"website:http:config"})
	public String httpForm(Model model,String ids,CfgIndexInfo entity) {
		if(StringUtils.isNotBlank(ids)){
			entity = websiteCfgService.getHttpCfg(Long.parseLong(ids),null);
			
			//设置http各类配置的配置域类型
			IpPortCfg ipCfg = new IpPortCfg();
			ipCfg.setCfgType(Constants.HTTP_IP_REGION);
			entity.setIpPort(ipCfg);
			HttpUrlCfg urlCfg = new HttpUrlCfg();
			urlCfg.setCfgType(Constants.HTTP_URL_REGION);
			entity.setHttpUrl(urlCfg);
			HttpReqHeadCfg reqHdrCfg = new HttpReqHeadCfg();
			reqHdrCfg.setCfgType(Constants.HTTP_REQ_HEAD_REGION);
			entity.setHttpReqHdr(reqHdrCfg);
			HttpResHeadCfg resHdrCfg = new HttpResHeadCfg();
			resHdrCfg.setCfgType(Constants.HTTP_RES_HEAD_REGION);
			entity.setHttpResHdr(resHdrCfg);
			HttpBodyCfg reqBodyCfg = new HttpBodyCfg();
			reqBodyCfg.setCfgType(Constants.HTTP_REQ_BODY_REGION);
			entity.setHttpReqBody(reqBodyCfg);
			HttpBodyCfg resBodyCfg = new HttpBodyCfg();
			resBodyCfg.setCfgType(Constants.HTTP_RES_BODY_REGION);
			entity.setHttpResBody(resBodyCfg);
			NtcSubscribeIdCfg subscribeId = new NtcSubscribeIdCfg();
			subscribeId.setCfgType(Constants.NTC_SUBSCRIBE_ID);
			entity.setNtcSubscribeIdCfg(subscribeId);
			
			int httpReqCfgNum = entity.getIpPortList().size()+
								entity.getHttpUrlList().size()+
								entity.getHttpReqBodyList().size()+
								entity.getHttpReqHdrList().size();
			int httpResCfgNum = entity.getHttpResHdrList().size()+
								entity.getHttpResBodyList().size();
			model.addAttribute("httpReqCfgNum", httpReqCfgNum);
			model.addAttribute("httpResCfgNum", httpResCfgNum);
			if(entity.getIpPortList().size()==0){
				entity.getIpPortList().add(ipCfg);
			}
			if(entity.getHttpUrlList().size()==0){
				entity.getHttpUrlList().add(urlCfg);
			}
			if(entity.getHttpReqHdrList().size()==0){
				entity.getHttpReqHdrList().add(reqHdrCfg);
			}
			if(entity.getHttpResHdrList().size()==0){
				entity.getHttpResHdrList().add(resHdrCfg);
			}
			if(entity.getHttpReqBodyList().size()==0){
				entity.getHttpReqBodyList().add(reqBodyCfg);
			}
			if(entity.getHttpResBodyList().size()==0){
				entity.getHttpResBodyList().add(resBodyCfg);
			}
			if(entity.getNtcSubscribeIdCfgList().size()==0){
				entity.getNtcSubscribeIdCfgList().add(subscribeId);
			}
			initUpdateFormCondition(model,entity);
		}else{
			//设置http各类配置的配置域类型
			IpPortCfg ipCfg = new IpPortCfg();
			ipCfg.setCfgType(Constants.HTTP_IP_REGION);
			entity.setIpPort(ipCfg);
			HttpUrlCfg urlCfg = new HttpUrlCfg();
			urlCfg.setCfgType(Constants.HTTP_URL_REGION);
			entity.setHttpUrl(urlCfg);
			HttpReqHeadCfg reqHdrCfg = new HttpReqHeadCfg();
			reqHdrCfg.setCfgType(Constants.HTTP_REQ_HEAD_REGION);
			entity.setHttpReqHdr(reqHdrCfg);
			HttpResHeadCfg resHdrCfg = new HttpResHeadCfg();
			resHdrCfg.setCfgType(Constants.HTTP_RES_HEAD_REGION);
			entity.setHttpResHdr(resHdrCfg);
			HttpBodyCfg reqBodyCfg = new HttpBodyCfg();
			reqBodyCfg.setCfgType(Constants.HTTP_REQ_BODY_REGION);
			entity.setHttpReqBody(reqBodyCfg);
			HttpBodyCfg resBodyCfg = new HttpBodyCfg();
			resBodyCfg.setCfgType(Constants.HTTP_RES_BODY_REGION);
			entity.setHttpResBody(resBodyCfg);
			NtcSubscribeIdCfg subscribeIdCfg = new NtcSubscribeIdCfg();
			subscribeIdCfg.setCfgType(Constants.NTC_SUBSCRIBE_ID);
			entity.setNtcSubscribeIdCfg(subscribeIdCfg);
			
			List<IpPortCfg> ipList=new ArrayList<IpPortCfg>();
			ipList.add(ipCfg);
			entity.setIpPortList(ipList);
			List<HttpUrlCfg> urlList=new ArrayList<HttpUrlCfg>();
			urlList.add(urlCfg);
			entity.setHttpUrlList(urlList);
			List<HttpReqHeadCfg> reqHdrList=new ArrayList<HttpReqHeadCfg>();
			reqHdrList.add(reqHdrCfg);
			entity.setHttpReqHdrList(reqHdrList);
			List<HttpResHeadCfg> resHdrList=new ArrayList<HttpResHeadCfg>();
			resHdrList.add(resHdrCfg);
			entity.setHttpResHdrList(resHdrList);
			List<HttpBodyCfg> reqBodyList=new ArrayList<HttpBodyCfg>();
			reqBodyList.add(reqBodyCfg);
			entity.setHttpReqBodyList(reqBodyList);
			List<HttpBodyCfg> resBodyList=new ArrayList<HttpBodyCfg>();
			resBodyList.add(resBodyCfg);
			entity.setHttpResBodyList(resBodyList);
			List<NtcSubscribeIdCfg> subscribeIdList=new ArrayList<NtcSubscribeIdCfg>();
			subscribeIdList.add(subscribeIdCfg);
			entity.setNtcSubscribeIdCfgList(subscribeIdList);
			initFormCondition(model,entity);
		}
		
		model.addAttribute("_cfg", entity);
		return "/cfg/website/httpForm";
	}
	
	@RequestMapping(value = {"saveHttpCfg"})
	@RequiresPermissions(value={"website:http:config"})
	public String saveHttpCfg(RedirectAttributes model,HttpServletRequest request,HttpServletResponse response,String ids,CfgIndexInfo entity) {
		websiteCfgService.saveHttpCfg(entity);
		return "redirect:" + adminPath +"/ntc/website/httpList?functionId="+entity.getFunctionId();
	}
	@RequestMapping(value = {"ajaxHttpSubList"})
	public String ajaxHttpSubList(Model model,Long cfgId,Integer index,Integer compileId) {
		CfgIndexInfo cfg = websiteCfgService.getHttpCfg(cfgId,compileId);
		model.addAttribute("_cfg", cfg);	
		model.addAttribute("index", index);
		return "/cfg/website/httpSubList";
	}
	@RequestMapping(value = {"updateHttpCfgValid"})
	@RequiresPermissions(value={"website:http:config"})
	public String updateHttpCfgValid(Integer isValid,String ids,Integer functionId) {
		websiteCfgService.updateHttpCfgValid(isValid,ids,functionId);
		return "redirect:" + adminPath +"/ntc/website/httpList?functionId="+functionId;
	}
	@RequestMapping(value = {"auditHttpCfg"})
	@RequiresPermissions(value={"website:http:confirm"})
	public String auditHttpCfg(Model model,@ModelAttribute("cfg")CfgIndexInfo cfg,
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
				entity = websiteCfgService.getHttpCfg(Long.parseLong(id),null);
				entity.setIsAudit(isAudit);
				entity.setIsValid(isValid);
				entity.setAuditorId(UserUtils.getUser().getId());
				entity.setAuditTime(new Date());
				entity.setFunctionId(functionId);
				try {
					websiteCfgService.auditHttpCfg(entity,isAudit);
				} catch (MaatConvertException e) {
					logger.error("http配置下发失败："+e.getMessage());
					addMessage(redirectAttributes,"error", "request_service_failed");
				}catch (Exception e) {
					logger.error("http配置下发失败："+e.getMessage());
					addMessage(redirectAttributes,"error", "audit_failed");
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
			
			return httpList(model, cfg, request, response);
		}
		return "redirect:" + adminPath +"/ntc/website/httpList?functionId="+functionId;
	}
	@RequestMapping(value = {"sslList"})
//	@RequiresPermissions(value={"website:ssl:config"})
	public String sslList(Model model,@ModelAttribute("cfg")CfgIndexInfo cfg,HttpServletRequest request,HttpServletResponse response) {
		Page<CfgIndexInfo> searchPage=new Page<CfgIndexInfo>(request,response,"a");
		Page<CfgIndexInfo> page = websiteCfgService.getWebsiteList(searchPage, cfg);
		model.addAttribute("page", page);
		initPageCondition(model,cfg);
		return "/cfg/website/sslList";
	}
	@RequestMapping(value = {"sslForm"})
	@RequiresPermissions(value={"website:ssl:config"})
	public String sslForm(Model model,String ids,CfgIndexInfo entity) {
		if(StringUtils.isNotBlank(ids)){
			entity = websiteCfgService.getSslCfg(Long.parseLong(ids),null);
			initUpdateFormCondition(model,entity);
		}else{
			initFormCondition(model,entity);
		}
		model.addAttribute("_cfg", entity);
		return "/cfg/website/sslForm";
	}
	@RequestMapping(value = {"saveSslCfg"})
	@RequiresPermissions(value={"website:ssl:config"})
	public String saveSslCfg(RedirectAttributes model,HttpServletRequest request,HttpServletResponse response,String ids,CfgIndexInfo entity) {
		websiteCfgService.saveSslCfg(entity);
		return "redirect:" + adminPath +"/ntc/website/sslList?functionId="+entity.getFunctionId();
	}
	@RequestMapping(value = {"ajaxSslSubList"})
	public String ajaxSslSubList(Model model,Long cfgId,Integer index,Integer compileId) {
		CfgIndexInfo cfg = websiteCfgService.getSslCfg(cfgId,compileId);
		/*List<FunctionRegionDict> regionList = DictUtils.getFunctionRegionDictList(cfg.getFunctionId());
		model.addAttribute("regionList", regionList);*/
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
		if(cfg.getSslList()!=null){
			String cfgType = null;
			for(BaseStringCfg keyword:cfg.getSslList()){
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
		return "/cfg/website/sslSubList";
	}
	@RequestMapping(value = {"updateSslCfgValid"})
	@RequiresPermissions(value={"website:ssl:config"})
	public String updateSslCfgValid(Integer isValid,String ids,Integer functionId) {
		websiteCfgService.updateSslCfgValid(isValid,ids,functionId);
		return "redirect:" + adminPath +"/ntc/website/sslList?functionId="+functionId;
	}
	@RequestMapping(value = {"auditSslCfg"})
	@RequiresPermissions(value={"website:ssl:confirm"})
	public String auditSslCfg(Model model,@ModelAttribute("cfg")CfgIndexInfo cfg,
										Integer isAudit,
										Integer isValid,
										String ids,
										Integer functionId, 
										RedirectAttributes redirectAttributes,
										HttpServletRequest request,
										HttpServletResponse response) {
		if(!StringUtil.isEmpty(ids)) {
			CfgIndexInfo entity = new CfgIndexInfo();
			String[] idArray = ids.split(",");
			for(String id :idArray){
				entity = websiteCfgService.getSslCfg(Long.parseLong(id),null);
				entity.setIsAudit(isAudit);
				entity.setIsValid(isValid);
				entity.setAuditorId(UserUtils.getUser().getId());
				entity.setAuditTime(new Date());
				entity.setFunctionId(functionId);
				try {
					websiteCfgService.auditSslCfg(entity,isAudit);
				} catch ( Exception e) {
					e.printStackTrace();
					logger.error("ssl配置下发失败："+e.getMessage());
					if(e instanceof MaatConvertException) {
						addMessage(redirectAttributes,"error","request_service_failed");
						
					}else if(e instanceof CallExternalProceduresException) {
						addMessage(redirectAttributes,"error","call_external_procedures_failed");
					}else {
						addMessage(redirectAttributes,"error","save_failed");
					}
				}
			}
		}else{//条件下所有配置审核
			try {
				Page<CfgIndexInfo> searchPage=new Page<CfgIndexInfo>(request,response,"a");
				Page<CfgIndexInfo> auditPage=new Page<CfgIndexInfo>(request,response,"a");
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
			
			return sslList(model, cfg, request, response);
		}
		return "redirect:" + adminPath +"/ntc/website/sslList?functionId="+functionId;
	}
	
	@RequestMapping(value = {"dnsForm"})
	@RequiresPermissions(value={"website:dns:config"})
	public String dnsForm(Model model,String ids,CfgIndexInfo entity) {
		if(StringUtils.isNotBlank(ids)){
			entity = websiteCfgService.getDnsCfg(Long.parseLong(ids),null);
			initUpdateFormCondition(model,entity);
		}else{
			initFormCondition(model,entity);
		}
		//获取所有响应策略信息
		List<DnsResStrategy> resStrategys=dnsResStrategyService.findDnsResStrategys(null, 1,1);
		model.addAttribute("dnsResStrategys", resStrategys);
		model.addAttribute("_cfg", entity);
		return "/cfg/website/dnsForm";
	}
	@RequestMapping(value = {"saveDnsCfg"})
	@RequiresPermissions(value={"website:dns:config"})
	public String saveDnsCfg(RedirectAttributes model,HttpServletRequest request,HttpServletResponse response,String ids,CfgIndexInfo entity) {
		websiteCfgService.saveDnsCfg(entity);
		return "redirect:" + adminPath +"/ntc/website/dnsList?functionId="+entity.getFunctionId();
	}
	@RequestMapping(value = {"ajaxDnsSubList"})
	public String ajaxDnsSubList(Model model,Long cfgId,Integer index,Integer compileId) {
		CfgIndexInfo cfg = websiteCfgService.getDnsCfg(cfgId,compileId);
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
		if(cfg.getDomainList()!=null){
			String cfgType = null;
			for(ComplexkeywordCfg keyword:cfg.getDomainList()){
				if(!keyword.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"3",keyword.getCfgType()});
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
		return "/cfg/website/dnsSubList";
	}
	@RequestMapping(value = {"updateDnsCfgValid"})
	@RequiresPermissions(value={"website:dns:config"})
	public String updateDnsCfgValid(Integer isValid,String ids,Integer functionId) {
		websiteCfgService.updateDnsCfgValid(isValid,ids,functionId);
		return "redirect:" + adminPath +"/ntc/website/dnsList?functionId="+functionId;
	}
	@RequestMapping(value = {"auditDnsCfg"})
	@RequiresPermissions(value={"website:dns:confirm"})
	public String auditDnsCfg(Model model,@ModelAttribute("cfg")CfgIndexInfo cfg,
									Integer isAudit,
									Integer isValid,
									String ids,
									Integer functionId,
									HttpServletRequest request,
									HttpServletResponse response,
									RedirectAttributes redirectAttributes) {
		if(!StringUtil.isEmpty(ids)) {
			CfgIndexInfo entity = new CfgIndexInfo();
			String[] idArray = ids.split(",");
			for(String id :idArray){
				entity = websiteCfgService.getDnsCfg(Long.parseLong(id),null);
				entity.setIsAudit(isAudit);
				entity.setIsValid(isValid);
				entity.setAuditorId(UserUtils.getUser().getId());
				entity.setAuditTime(new Date());
				entity.setFunctionId(functionId);
				try {
					websiteCfgService.auditDnsCfg(entity,isAudit);
				} catch (MaatConvertException e) {
					e.printStackTrace();
					logger.info("dns配置下发失败："+e.getMessage());
					addMessage(redirectAttributes,"error", "request_service_failed");
				}catch (Exception e) {
					e.printStackTrace();
					logger.info("dns配置下发失败："+e.getMessage());
					addMessage(redirectAttributes,"error", "audit_failed");
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
			
			return dnsList(model, cfg, request, response);
		}
		return "redirect:" + adminPath +"/ntc/website/dnsList?functionId="+functionId;
	}
	@Deprecated
	//下载导入模板
	@RequestMapping(value = "import/template")
    public void importFileTemplate(HttpServletRequest request,HttpServletResponse response, 
    		RedirectAttributes redirectAttributes,Integer functionId,Integer cfgRegionCode) {
		this.importCfgTemplate(request, response, redirectAttributes, functionId, cfgRegionCode);
	}
	//http配置导入
	/*@RequestMapping(value = "importHttp", method=RequestMethod.POST)
	public String importHttpFile(HttpServletRequest request,Model model,HttpServletResponse response, RedirectAttributes redirectAttributes,
			@RequestParam("file") MultipartFile file,CfgIndexInfo cfgIndex,Integer protocolId) {
		try {
			ImportExcel ei = new ImportExcel(file, 0, 0);
			if(cfgIndex.getCfgType().equals(Constants.HTTP_IP_REGION)){
				List<IpCfgTemplate> list = ei.getDataList(IpCfgTemplate.class);
				for(IpCfgTemplate cfg : list){
					List<IpPortCfg> ipList = new ArrayList();
					BeanUtils.copyProperties(cfg, cfgIndex);
					IpPortCfg ip = new IpPortCfg();
		        	BeanUtils.copyProperties(cfg, ip);
		        	ip.setProtocolId(protocolId);
		        	ip.setCfgRegionCode(cfgIndex.getCfgRegionCode());
		        	ip.setCfgType(cfgIndex.getCfgType());
		        	ipList.add(ip);
		        	cfgIndex.setIpPortList(ipList);
		        	cfgIndex.setIsAreaEffective(0);
		        	websiteCfgService.saveHttpCfg(cfgIndex);
				}
				
			}else if(cfgIndex.getCfgType().equals(Constants.HTTP_URL_REGION)){
				List<StringCfgTemplate> list = ei.getDataList(StringCfgTemplate.class);
				for(StringCfgTemplate cfg : list){
					List<HttpUrlCfg> urlList = new ArrayList();
					BeanUtils.copyProperties(cfg, cfgIndex);
					HttpUrlCfg url = new HttpUrlCfg();
		        	BeanUtils.copyProperties(cfg, url);
		        	url.setCfgRegionCode(cfgIndex.getCfgRegionCode());
		        	url.setCfgType(cfgIndex.getCfgType());
		        	urlList.add(url);
		        	cfgIndex.setHttpUrlList(urlList);
		        	cfgIndex.setIsAreaEffective(0);
		        	websiteCfgService.saveHttpCfg(cfgIndex);
				}
				
        	}else if(cfgIndex.getCfgType().equals(Constants.HTTP_REQ_HEAD_REGION)){
        		List<ComplexStringCfgTemplate> list = ei.getDataList(ComplexStringCfgTemplate.class);
				for(ComplexStringCfgTemplate cfg : list){
					List<HttpReqHeadCfg> headList = new ArrayList();
					BeanUtils.copyProperties(cfg, cfgIndex);
					HttpReqHeadCfg head = new HttpReqHeadCfg();
		        	BeanUtils.copyProperties(cfg, head);
		        	head.setCfgRegionCode(cfgIndex.getCfgRegionCode());
		        	head.setCfgType(cfgIndex.getCfgType());
		        	headList.add(head);
		        	cfgIndex.setHttpReqHdrList(headList);
		        	cfgIndex.setIsAreaEffective(0);
		        	websiteCfgService.saveHttpCfg(cfgIndex);
				}
				
        	}else if(cfgIndex.getCfgType().equals(Constants.HTTP_REQ_BODY_REGION)){
        		List<StringCfgTemplate> list = ei.getDataList(StringCfgTemplate.class);
				for(StringCfgTemplate cfg : list){
					List<HttpBodyCfg> bodyList = new ArrayList();
					BeanUtils.copyProperties(cfg, cfgIndex);
					HttpBodyCfg body = new HttpBodyCfg();
		        	BeanUtils.copyProperties(cfg, body);
		        	body.setCfgRegionCode(cfgIndex.getCfgRegionCode());
		        	body.setCfgType(cfgIndex.getCfgType());
		        	bodyList.add(body);
		        	cfgIndex.setHttpReqBodyList(bodyList);
		        	cfgIndex.setIsAreaEffective(0);
		        	websiteCfgService.saveHttpCfg(cfgIndex);
				}
				
        	}else if(cfgIndex.getCfgType().equals(Constants.HTTP_RES_HEAD_REGION)){
        		List<ComplexStringCfgTemplate> list = ei.getDataList(ComplexStringCfgTemplate.class);
				for(ComplexStringCfgTemplate cfg : list){
					List<HttpResHeadCfg> headList = new ArrayList();
					BeanUtils.copyProperties(cfg, cfgIndex);
					HttpResHeadCfg head = new HttpResHeadCfg();
		        	BeanUtils.copyProperties(cfg, head);
		        	head.setCfgRegionCode(cfgIndex.getCfgRegionCode());
		        	head.setCfgType(cfgIndex.getCfgType());
		        	headList.add(head);
		        	cfgIndex.setHttpResHdrList(headList);
		        	cfgIndex.setIsAreaEffective(0);
		        	websiteCfgService.saveHttpCfg(cfgIndex);
				}
				
        	}else if(cfgIndex.getCfgType().equals(Constants.HTTP_RES_BODY_REGION)){
        		List<StringCfgTemplate> list = ei.getDataList(StringCfgTemplate.class);
				for(StringCfgTemplate cfg : list){
					List<HttpBodyCfg> bodyList = new ArrayList();
					BeanUtils.copyProperties(cfg, cfgIndex);
					HttpBodyCfg body = new HttpBodyCfg();
		        	BeanUtils.copyProperties(cfg, body);
		        	body.setCfgRegionCode(cfgIndex.getCfgRegionCode());
		        	body.setCfgType(cfgIndex.getCfgType());
		        	bodyList.add(body);
		        	cfgIndex.setHttpResBodyList(bodyList);
					cfgIndex.setIsAreaEffective(0);
		        	websiteCfgService.saveHttpCfg(cfgIndex);
				}
				
        	}
			
	       
		} catch (Exception e) {
			addMessage(redirectAttributes,  e.getMessage());
			e.printStackTrace();
		}
		return "redirect:" + adminPath +"/ntc/website/httpList?functionId="+cfgIndex.getFunctionId();
		
	}*/
	
	
	

	//http配置导出
			@RequestMapping(value = "exportHttp")
			public void exportHttp(Model model,HttpServletRequest  request,HttpServletResponse response,
					@ModelAttribute("cfg")CfgIndexInfo entity,String ids,RedirectAttributes redirectAttributes){
				try {
					//export data info
					List<String> titleList=new ArrayList<String>();
					Map<String, Class<?>> classMap=new HashMap<String, Class<?>>();
					Map<String, List> dataMap=new HashMap<String, List>();
					Map<String, String> noExportMap=new HashMap<String, String>();
					
					List<FunctionRegionDict> regionList = DictUtils.getFunctionRegionDictList(entity.getFunctionId());
					
					/*//导出选中记录
					if(!StringUtil.isEmpty(ids)){
						for(String id:ids.split(",")){
							Long.parseLong(id);
						}
						//List<CfgIndexInfo> list=ipCfgService.getListByCfgIdWithName(IpPortCfg.getTablename(), entity.getFunctionId(), ids);
					}else{*/
						//条件导出数据大于最大导出数，只导出最大导出条数
						Page<CfgIndexInfo> pageInfo=new Page<CfgIndexInfo>(request, response,"a");
						pageInfo.setPageNo(1);
						pageInfo.setPageSize(Constants.MAX_EXPORT_SIZE);
						Page<CfgIndexInfo> page = httpRedirectCfgService.getHttpRedirectList(pageInfo, entity);
						String cfgIndexInfoNoExport=",policy_name,group_name,userregion1,userregion2,userregion3,userregion4,userregion5,";
						String httpUrlCfgNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
													+ ",config_time,editor,edit_time,auditor,audit_time"
													+",letter,whether_area_block,classification,attribute,label"
													+",userregion1,userregion2,userregion3,userregion4,userregion5,";
						String httpReqHeadCfgNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
													+ ",config_time,editor,edit_time,auditor,audit_time"
													+",letter,whether_area_block,classification,attribute,label"
													+",userregion1,userregion2,userregion3,userregion4,userregion5,";
						String httpResHeadCfgNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
													+ ",config_time,editor,edit_time,auditor,audit_time"
													+",letter,whether_area_block,classification,attribute,label"
													+",userregion1,userregion2,userregion3,userregion4,userregion5,";
						String httpReqBodyCfgNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
													+ ",config_time,editor,edit_time,auditor,audit_time"
													+",letter,whether_area_block,classification,attribute,label"
													+",userregion1,userregion2,userregion3,userregion4,userregion5,";
						String httpResBodyCfgNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
													+ ",config_time,editor,edit_time,auditor,audit_time"
													+",letter,whether_area_block,classification,attribute,label"
													+",userregion1,userregion2,userregion3,userregion4,userregion5,";
						String ipPortCfgNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
													+ ",config_time,editor,edit_time,auditor,audit_time"
													+",letter,whether_area_block,classification,attribute,label"
													+",userregion1,userregion2,userregion3,userregion4,userregion5,ir_type,group_name,";
						String ntcSubscribeIdCfgNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
													+ ",config_time,editor,edit_time,auditor,audit_time"
													+",letter,whether_area_block,classification,attribute,label"
													+",userregion1,userregion2,userregion3,userregion4,userregion5,ir_type,group_name,";
						List<BaseStringCfg> httpUrlList = new ArrayList<>();
						List<ComplexkeywordCfg> httpReqHdrList = new ArrayList<>();
						List<ComplexkeywordCfg> httpResHdrList = new ArrayList<>();
						List<BaseStringCfg> httpReqBodyList = new ArrayList<>();
						List<BaseStringCfg> httpResBodyList = new ArrayList<>();
						List<IpPortCfg> ipPortList = new ArrayList<>();
						List<BaseStringCfg> subscribeIdList = new ArrayList<>();
						for (CfgIndexInfo cfg : page.getList()) {
							 Map<String, List> maps=websiteCfgService.exportHttpCfg(cfg);
							 httpUrlList.addAll(maps.get("NTC_HTTP_URL"));
							 httpReqHdrList.addAll(maps.get("NTC_HTTP_REQ_HDR"));
							 httpResHdrList.addAll(maps.get("NTC_HTTP_RES_HDR"));
							 httpReqBodyList.addAll(maps.get("NTC_HTTP_REQ_BODY"));
							 httpResBodyList.addAll(maps.get("NTC_HTTP_RES_BODY"));
							 ipPortList.addAll(maps.get("NTC_UNIVERSAL_IP"));
							 subscribeIdList.addAll(maps.get("NTC_SUBSCRIBE_ID"));
						}
						httpUrlList=BaseStringCfg.replaceBaseKeyList(httpUrlList);
						httpReqHdrList=ComplexkeywordCfg.replaceComplexKeyList(httpReqHdrList);
						httpResHdrList=ComplexkeywordCfg.replaceComplexKeyList(httpResHdrList);
						httpReqBodyList=BaseStringCfg.replaceBaseKeyList(httpReqBodyList);
						httpResBodyList=BaseStringCfg.replaceBaseKeyList(httpResBodyList);
						subscribeIdList=BaseStringCfg.baseHexList(subscribeIdList);
						titleList.add(entity.getMenuNameCode());
						classMap.put(entity.getMenuNameCode(), CfgIndexInfo.class);
						noExportMap.put(entity.getMenuNameCode(),cfgIndexInfoNoExport);
						dataMap.put(entity.getMenuNameCode(), page.getList());
						
						if(entity.getFunctionId()!=635){
							titleList.add("NTC_HTTP_URL");
							titleList.add("NTC_HTTP_REQ_HDR");
							titleList.add("NTC_HTTP_RES_HDR");
							classMap.put("NTC_HTTP_URL", HttpUrlCfg.class);
							classMap.put("NTC_HTTP_REQ_HDR", HttpReqHeadCfg.class);
							classMap.put("NTC_HTTP_RES_HDR", HttpResHeadCfg.class);
							noExportMap.put("NTC_HTTP_URL", httpUrlCfgNoExport);
							noExportMap.put("NTC_HTTP_REQ_HDR", httpReqHeadCfgNoExport);
							noExportMap.put("NTC_HTTP_RES_HDR", httpResHeadCfgNoExport);
							dataMap.put("NTC_HTTP_URL", httpUrlList);
							dataMap.put("NTC_HTTP_REQ_HDR", httpReqHdrList);
							dataMap.put("NTC_HTTP_RES_HDR", httpResHdrList);
						}
						titleList.add("NTC_HTTP_REQ_BODY");
						titleList.add("NTC_HTTP_RES_BODY");
						classMap.put("NTC_HTTP_REQ_BODY", HttpBodyCfg.class);
						classMap.put("NTC_HTTP_RES_BODY", HttpBodyCfg.class);
						noExportMap.put("NTC_HTTP_REQ_BODY", httpReqBodyCfgNoExport);
						noExportMap.put("NTC_HTTP_RES_BODY", httpResBodyCfgNoExport);
						dataMap.put("NTC_HTTP_REQ_BODY", httpReqBodyList);
						dataMap.put("NTC_HTTP_RES_BODY", httpResBodyList);
						if(entity.getFunctionId()!=635){
						titleList.add("NTC_UNIVERSAL_IP");
						titleList.add("NTC_SUBSCRIBE_ID");
						classMap.put("NTC_UNIVERSAL_IP", IpPortCfg.class);
						classMap.put("NTC_SUBSCRIBE_ID", NtcSubscribeIdCfg.class);
						noExportMap.put("NTC_UNIVERSAL_IP", ipPortCfgNoExport);
						noExportMap.put("NTC_SUBSCRIBE_ID", ntcSubscribeIdCfgNoExport);
						dataMap.put("NTC_UNIVERSAL_IP", ipPortList);
						dataMap.put("NTC_SUBSCRIBE_ID",subscribeIdList);
						}
					/*}*/
					this._export(model, request, response, redirectAttributes,entity.getMenuNameCode(),titleList,classMap,dataMap,noExportMap);
				} catch (Exception e) {
					logger.error("http export failed",e);
					addMessage(redirectAttributes,"error",  "export_failed");
				}
				//return "redirect:" + adminPath +"/ntc/website/httpList?functionId="+entity.getFunctionId();
			}

	//dns配置导出
	@RequestMapping(value = "exportdns")
	public void exportdns(Model model,HttpServletRequest  request,HttpServletResponse response,
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
				Page<CfgIndexInfo> page = websiteCfgService.getWebsiteList(pageInfo, entity);
				for (int i = 0; i < page.getList().size(); i++) {
					Properties msgProp = getMsgProp();
					if(page.getList().get(i).getDnsStrategyId()!=null && page.getList().get(i).getDnsStrategyId()!=0){
						List<DnsResStrategy> resStrategys=dnsResStrategyService.findDnsResStrategys(page.getList().get(i).getDnsStrategyId(), 1,1);
						if(resStrategys!=null && resStrategys.size()>0){
							String msg= msgProp.getProperty(resStrategys.get(0).getCfgDesc());
						 if(!StringUtil.isEmpty(msg)){
							 page.getList().get(i).setDnsStrategyName(msg);
						 }else{
							 page.getList().get(i).setDnsStrategyName(resStrategys.get(0).getCfgDesc());
						 }
						}else{
							page.getList().get(i).setDnsStrategyName(msgProp.getProperty("no_strategy"));
						}
					}else{
						page.getList().get(i).setDnsStrategyName(msgProp.getProperty("no_strategy"));
					}
				}
				titleList.add(entity.getMenuNameCode());
				titleList.add("NTC_IP");
				titleList.add("NTC_DNS_REGION");
				titleList.add("NTC_SUBSCRIBE_ID");
				classMap.put(entity.getMenuNameCode(), CfgIndexInfo.class);
				classMap.put("NTC_IP", IpPortCfg.class);
				classMap.put("NTC_DNS_REGION", ComplexkeywordCfg.class);
				classMap.put("NTC_SUBSCRIBE_ID", NtcSubscribeIdCfg.class);
				String cfgIndexInfoNoExport=",group_name,userregion1,userregion2,userregion3,userregion4,userregion5,";
				String ipPortInfoNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
										+ ",config_time,editor,edit_time,auditor,audit_time"
										+",letter,whether_area_block,classification,attribute,label"
										+",userregion1,userregion2,userregion3,userregion4,userregion5,ir_type,group_name,";
				String ntcSubscribeIdCfgNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
						+ ",config_time,editor,edit_time,auditor,audit_time"
						+",letter,whether_area_block,classification,attribute,label"
						+",userregion1,userregion2,userregion3,userregion4,userregion5,";
				String regionCfgNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
						+ ",config_time,editor,edit_time,auditor,audit_time"
						+",letter,whether_area_block,classification,attribute,label"
						+",userregion1,userregion2,userregion3,userregion4,userregion5,";
				noExportMap.put(entity.getMenuNameCode(),cfgIndexInfoNoExport);
				noExportMap.put("NTC_IP", ipPortInfoNoExport);
				noExportMap.put("NTC_DNS_REGION", regionCfgNoExport);
				noExportMap.put("NTC_SUBSCRIBE_ID", ntcSubscribeIdCfgNoExport);
				List<IpPortCfg> ipList=new ArrayList<IpPortCfg>();
				List<BaseStringCfg> subscribeInfoList=new ArrayList<BaseStringCfg>();
				List<ComplexkeywordCfg> regionInfoList=new ArrayList<ComplexkeywordCfg>();
				for (CfgIndexInfo cfg : page.getList()) {
					CfgIndexInfo cfgIndexInfo=websiteCfgService.exportdns(cfg);
					ipList.addAll(cfgIndexInfo.getIpPortList());
					subscribeInfoList.addAll(cfgIndexInfo.getNtcSubscribeIdCfgList());
					regionInfoList.addAll(cfgIndexInfo.getDomainList());
				}
				subscribeInfoList=BaseStringCfg.baseHexList(subscribeInfoList);
				regionInfoList=ComplexkeywordCfg.replaceComplexKeyList(regionInfoList);
				dataMap.put(entity.getMenuNameCode(), page.getList());
				dataMap.put("NTC_IP", ipList);
				dataMap.put("NTC_DNS_REGION", regionInfoList);
				dataMap.put("NTC_SUBSCRIBE_ID", subscribeInfoList);
				
			/*}*/
			this._export(model, request, response, redirectAttributes,entity.getMenuNameCode(),titleList,classMap,dataMap,noExportMap);
		} catch (Exception e) {
			logger.error("dns export failed",e);
			addMessage(redirectAttributes,"error",  "export_failed");
		}
		//return "redirect:" + adminPath +"/ntc/iplist/list?functionId="+entity.getFunctionId();
	}
	
	//ssl配置导出
		@RequestMapping(value = "exportSsl")
		public void exportSsl(Model model,HttpServletRequest  request,HttpServletResponse response,
				@ModelAttribute("cfg")CfgIndexInfo entity,String ids,RedirectAttributes redirectAttributes){
			try {
				//export data info
				List<String> titleList=new ArrayList<String>();
				Map<String, Class<?>> classMap=new HashMap<String, Class<?>>();
				Map<String, List> dataMap=new HashMap<String, List>();
				Map<String, String> noExportMap=new HashMap<String, String>();
				
				List<FunctionRegionDict> regionList = DictUtils.getFunctionRegionDictList(entity.getFunctionId());
				
				/*//导出选中记录
				if(!StringUtil.isEmpty(ids)){
					for(String id:ids.split(",")){
						Long.parseLong(id);
					}
					//List<CfgIndexInfo> list=ipCfgService.getListByCfgIdWithName(IpPortCfg.getTablename(), entity.getFunctionId(), ids);
				}else{*/
					//条件导出数据大于最大导出数，只导出最大导出条数
					Page<CfgIndexInfo> pageInfo=new Page<CfgIndexInfo>(request, response,"a");
					pageInfo.setPageNo(1);
					pageInfo.setPageSize(Constants.MAX_EXPORT_SIZE);
					Page<CfgIndexInfo> page = websiteCfgService.getWebsiteList(pageInfo, entity);
					titleList.add(entity.getMenuNameCode());
					titleList.add("NTC_UNIVERSAL_IP");
					titleList.add("NTC_SSL_SNI");
					titleList.add("NTC_SSL_SAN");
					titleList.add("NTC_SSL_CN");
					titleList.add("NTC_SUBSCRIBE_ID");
					classMap.put(entity.getMenuNameCode(), CfgIndexInfo.class);
					classMap.put("NTC_UNIVERSAL_IP", IpPortCfg.class);
					classMap.put("NTC_SSL_SNI", BaseStringCfg.class);
					classMap.put("NTC_SSL_SAN", BaseStringCfg.class);
					classMap.put("NTC_SSL_CN", BaseStringCfg.class);
					classMap.put("NTC_SUBSCRIBE_ID", NtcSubscribeIdCfg.class);
					String cfgIndexInfoNoExport=",policy_name,group_name,userregion1,userregion2,userregion3,userregion4,userregion5,";
					String sslNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
												+ ",config_time,editor,edit_time,auditor,audit_time"
												+",letter,whether_area_block,classification,attribute,label"
												+",userregion1,userregion2,userregion3,userregion4,userregion5,";
					String ipPortCfgNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
												+ ",config_time,editor,edit_time,auditor,audit_time"
												+",letter,whether_area_block,classification,attribute,label"
												+",userregion1,userregion2,userregion3,userregion4,userregion5,ir_type,group_name,";
					String ntcSubscribeIdCfgNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
												+ ",config_time,editor,edit_time,auditor,audit_time"
												+",letter,whether_area_block,classification,attribute,label"
												+",userregion1,userregion2,userregion3,userregion4,userregion5,";
					noExportMap.put(entity.getMenuNameCode(),cfgIndexInfoNoExport);
					noExportMap.put("NTC_UNIVERSAL_IP", ipPortCfgNoExport);
					noExportMap.put("NTC_SSL_SNI", sslNoExport);
					noExportMap.put("NTC_SSL_SAN", sslNoExport);
					noExportMap.put("NTC_SSL_CN", sslNoExport);
					noExportMap.put("NTC_SUBSCRIBE_ID", ntcSubscribeIdCfgNoExport);
					
					List<BaseStringCfg> sslSniList = new ArrayList<>();
					List<BaseStringCfg> sslSnaList = new ArrayList<>();
					List<BaseStringCfg> sslCnList = new ArrayList<>();
					List<IpPortCfg> ipPortList = new ArrayList<>();
					List<BaseStringCfg> subscribeIdList = new ArrayList<BaseStringCfg>();
					for (CfgIndexInfo cfg : page.getList()) {
						 Map<String, List> maps=websiteCfgService.exportssl(cfg);
						 sslSniList.addAll(maps.get("NTC_SSL_SNI"));
						 sslSnaList.addAll(maps.get("NTC_SSL_SAN"));
						 sslCnList.addAll(maps.get("NTC_SSL_CN"));
						 ipPortList.addAll(maps.get("NTC_UNIVERSAL_IP"));
						 subscribeIdList.addAll(maps.get("NTC_SUBSCRIBE_ID"));
					}
					sslSniList=BaseStringCfg.replaceBaseKeyList(sslSniList);
					sslSnaList=BaseStringCfg.replaceBaseKeyList(sslSnaList);
					sslCnList=BaseStringCfg.replaceBaseKeyList(sslCnList);
					subscribeIdList=BaseStringCfg.baseHexList(subscribeIdList);
					dataMap.put(entity.getMenuNameCode(), page.getList());
					dataMap.put("NTC_UNIVERSAL_IP", ipPortList);
					dataMap.put("NTC_SSL_SNI", sslSniList);
					dataMap.put("NTC_SSL_SAN", sslSnaList);
					dataMap.put("NTC_SSL_CN", sslCnList);
					dataMap.put("NTC_SUBSCRIBE_ID",subscribeIdList);
				/*}*/
				this._export(model, request, response, redirectAttributes,entity.getMenuNameCode(),titleList,classMap,dataMap,noExportMap);
			} catch (Exception e) {
				logger.error("http export failed",e);
				addMessage(redirectAttributes,"error",  "export_failed");
			}
			//return "redirect:" + adminPath +"/ntc/website/httpList?functionId="+entity.getFunctionId();
		}
}
