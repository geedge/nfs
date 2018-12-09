/**
 *@Title: ControlController.java
 *@Package com.nis.web.controller.configuration.proxy 
 *@Description TODO
 *@author dell
 *@date 2018年6月22日 下午4:35:42
 *@version  版本号
 */
package com.nis.web.controller.configuration.proxy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.HttpUrlCfg;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.web.controller.configuration.CommonController;

/**
 * @ClassName: ControlController.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年6月22日 下午4:35:42
 * @version V1.0
 */
@Controller
@RequestMapping("${adminPath}/proxy/control")
@Deprecated
public class ControlController extends CommonController {
	@RequestMapping(value = {"/ip/list"})
//	@RequiresPermissions(value={"control:ip:config","control:ip:confirm"},logical=Logical.OR)
	public String ipList(String cfgName,Model model,@ModelAttribute("cfg")IpPortCfg cfg,HttpServletRequest request,HttpServletResponse response) {
		this._ipList(cfgName,model, cfg, request, response);
		model.addAttribute("urlPrefix","/proxy/control/ip");
		model.addAttribute("requiresPermissionPrefix","control:ip");
		return "/cfg/common/ipList";
	}
	@RequestMapping(value = {"/domain/list"})
//	@RequiresPermissions(value={"control:domain:config","control:domain:confirm"},logical=Logical.OR)
	public String domainList(String cfgName,Model model,@ModelAttribute("cfg")HttpUrlCfg cfg,HttpServletRequest request,HttpServletResponse response) {
		this._domainList(cfgName,model, cfg, request, response);
		model.addAttribute("urlPrefix","/proxy/control/domain");
		model.addAttribute("requiresPermissionPrefix","control:domain");
		return "/cfg/common/domainList";
	}
	@RequestMapping(value = {"/ip/form"})
	@RequiresPermissions(value={"control:ip:config"})
	public String ipForm(String cfgName,Model model,String ids,Integer functionId,BaseIpCfg entity) {
		this._ipForm(cfgName,model, ids, functionId, entity);
		model.addAttribute("urlPrefix","/proxy/control/ip");
		model.addAttribute("requiresPermissionPrefix","control:ip");
		return "/cfg/common/ipForm";
	}
	@RequestMapping(value = {"/domain/form"})
	@RequiresPermissions(value={"control:domain:config"})
	public String domainForm(String cfgName,Model model,String ids,Integer functionId,HttpUrlCfg entity) {
		this._domainForm(cfgName, model, ids, functionId, entity);
		model.addAttribute("urlPrefix","/proxy/control/domain");
		model.addAttribute("requiresPermissionPrefix","control:domain");
		return "/cfg/common/domainForm";
	}
	@RequestMapping(value = {"/ip/saveOrUpdate"})
	public String saveOrUpdateIp(String cfgName,RedirectAttributes model, IpPortCfg cfg) {
		this._saveOrUpdateIp(cfgName,model, cfg);
		return "redirect:" + adminPath +"/proxy/control/ip/list?functionId="+cfg.getFunctionId();
	}
	@RequestMapping(value = {"/domain/saveOrUpdate"})
	public String saveOrUpdateDomain(String cfgName,RedirectAttributes model, HttpUrlCfg cfg) {
		this._saveOrUpdateDomain(cfgName, model, cfg);
		return "redirect:" + adminPath +"/proxy/control/domain/list?functionId="+cfg.getFunctionId();
	}
	@RequestMapping(value = {"/ip/delete"})
	@RequiresPermissions("control:ip:config")
	public String deleteIp(String cfgName,String ids,String compileIds,Integer functionId,RedirectAttributes model) {
		this._deleteIp(cfgName,ids, compileIds, functionId, model);
		model.addAttribute("urlPrefix","/proxy/control/ip");
		model.addAttribute("requiresPermissionPrefix","control:ip");
		return "redirect:" + adminPath +"/proxy/control/ip/list?functionId="+functionId;
	}
	@RequestMapping(value = {"/domain/delete"})
	@RequiresPermissions("config:domain:config")
	public String deleteDomain(String cfgName,String ids,String compileIds,Integer functionId,RedirectAttributes model) {
		this._deleteDomain(cfgName, ids, compileIds, functionId, model);
		model.addAttribute("urlPrefix","/proxy/control/domain");
		model.addAttribute("requiresPermissionPrefix","control:domain");
		return "redirect:" + adminPath +"/proxy/control/domain/list?functionId="+functionId;
	}
	@RequestMapping(value = {"/ip/audit"})
	@RequiresPermissions("control:ip:confirm")
	public String auditIp(String cfgName,String ids,IpPortCfg cfg,RedirectAttributes redirectAttributes) {
		this._auditIp(cfgName,ids, cfg, redirectAttributes);
		redirectAttributes.addAttribute("urlPrefix","/proxy/control/ip");
		redirectAttributes.addAttribute("requiresPermissionPrefix","control:ip");
		return "redirect:" + adminPath +"/proxy/control/ip/list?functionId="+cfg.getFunctionId();
	}
	@RequestMapping(value = {"/domain/audit"})
	@RequiresPermissions("control:domain:confirm")
	public String auditDomain(String ids,HttpUrlCfg cfg,RedirectAttributes redirectAttributes) {
		this._auditDomain(ids, cfg, redirectAttributes);
		redirectAttributes.addAttribute("urlPrefix","/proxy/control/domain");
		redirectAttributes.addAttribute("requiresPermissionPrefix","control:domain");
		return "redirect:" + adminPath +"/proxy/control/domain/list?functionId="+cfg.getFunctionId();
	}
	//ip配置导入
	@RequestMapping(value = "/ip/import", method=RequestMethod.POST)
	public String importIp(String cfgName,RedirectAttributes redirectAttributes,
			@RequestParam("file") MultipartFile file,IpPortCfg cfg) {
		//this._importIp(cfgName,redirectAttributes, file,cfg);
		redirectAttributes.addAttribute("urlPrefix","/proxy/control/ip");
		redirectAttributes.addAttribute("requiresPermissionPrefix","control:ip");
		return "redirect:" + adminPath +"/proxy/control/ip/list?functionId="+cfg.getFunctionId();
	}
	@RequestMapping(value = "/ip/import/template")
	public void importFileTemplate(HttpServletRequest request,HttpServletResponse response, 
    		RedirectAttributes redirectAttributes,Integer functionId,Integer cfgRegionCode) {
		this._importFileTemplate(request, response, redirectAttributes, functionId, cfgRegionCode);
	}
	//ip配置导出
	@RequestMapping(value = "/ip/export")
	public void exportIp(String columns,Model model,HttpServletRequest  request,HttpServletResponse response,
			@ModelAttribute("cfg")IpPortCfg entity,String ids,RedirectAttributes redirectAttributes){
		this._exportIp(columns,model, request, response, entity, ids, redirectAttributes);
	}
}
