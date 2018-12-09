package com.nis.web.controller.configuration.proxy;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nis.domain.Page;
import com.nis.domain.configuration.AreaIpCfg;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.HttpBodyCfg;
import com.nis.domain.configuration.HttpUrlCfg;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.Constants;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseController;

/**
 * 控制策略
 * @author ddm
 *
 */
@Controller
@RequestMapping("${adminPath}/proxy/control")
public class ControlPolicyController extends BaseController{
	@RequestMapping(value = {"httpReqReplace/list"})
	public String reqList(Model model,HttpServletRequest  request
			,HttpServletResponse response
			,@ModelAttribute("cfg")HttpBodyCfg entity
			,RedirectAttributes redirectAttributes){
		Page<HttpBodyCfg> page = controlPolicyService.findPage(new Page<HttpBodyCfg>(request, response,"r"), entity);
		model.addAttribute("page", page);
		initPageCondition(model);
		return "/cfg/proxy/control/httpReqReplaceList";
	}
	@RequestMapping(value = {"httpReqReplace/form"})
	@RequiresPermissions(value={"proxy:contol:httpReqReplace:config"})
	public String reqForm(Model model,HttpServletRequest request
			,HttpServletResponse response,String ids
			,@ModelAttribute("cfg")HttpBodyCfg cfg
			,RedirectAttributes redirectAttributes){
		if(!StringUtil.isEmpty(ids)){
			cfg = controlPolicyService.getHttpBodyCfgById(Long.valueOf(ids));
			initUpdateFormCondition(model, cfg);
		}else{
			initFormCondition(model,cfg);
		}
		model.addAttribute("_cfg", cfg);
		return "/cfg/proxy/control/httpReqReplaceForm";
	}
	
	@RequestMapping(value = {"httpReqReplace/saveOrUpdate"})
	public String reqSaveOrUpdate(Model model,HttpServletRequest request
			,HttpServletResponse response,
			@ModelAttribute("cfg")HttpBodyCfg cfg,
			@ModelAttribute("areaCfgIds")String areaCfgIds
			,RedirectAttributes redirectAttributes){
		try{
			controlPolicyService.saveOrUpdate(cfg,areaCfgIds);
			addMessage(redirectAttributes,"success","save_success");
		}catch(Exception e){
			logger.error("信息保存失败",e);
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,"error", "request_service_failed");
			}else {
				addMessage(redirectAttributes,"error","save_failed");
			}
		}
		
		return "redirect:" + adminPath +"/proxy/control/httpReqReplace/list?functionId="+cfg.getFunctionId();
	}
	
	@RequestMapping(value = {"httpReqReplace/delete"})
	@RequiresPermissions(value={"proxy:contol:httpReqReplace:config"})
	public String reqDelete(Integer isAudit,Integer isValid,String ids
			,Integer functionId
			,RedirectAttributes redirectAttributes){
		controlPolicyService.update(isAudit,isValid,ids,functionId);
		return "redirect:" + adminPath +"/proxy/control/httpReqReplace/list?functionId="+functionId;
	}
	@RequestMapping(value = {"httpReqReplace/audit"})
	public String reqAudit(Integer isAudit,Integer isValid,String ids
			,Integer functionId, RedirectAttributes redirectAttributes) {
		if(!StringUtil.isEmpty(ids)){
			String[] idArray = ids.split(",");
			Date auditTime=new Date();
			for(String id :idArray){
				try {
					controlPolicyService.audit(isAudit,isValid,functionId,id,auditTime,Constants.REPLACE_REQ_KEY_VALUE);
				} catch ( Exception e) {
					logger.error("配置下发失败",e);
					if(e instanceof MaatConvertException) {
						addMessage(redirectAttributes,"error","request_service_failed");
					}else {
						addMessage(redirectAttributes,"error","audit_failed");
					}
				}
			}
			
		}
		return "redirect:" + adminPath +"/proxy/control/httpReqReplace/list?functionId="+functionId;
	}
	@RequestMapping(value = {"httpResReplace/list"})
	public String reslist(Model model,HttpServletRequest  request
			,HttpServletResponse response,@ModelAttribute("cfg")HttpBodyCfg entity
			,RedirectAttributes redirectAttributes){
		Page<HttpBodyCfg> page = controlPolicyService.findPage(new Page<HttpBodyCfg>(request, response,"r"), entity);
		model.addAttribute("page", page);
		initPageCondition(model);
		return "/cfg/proxy/control/httpResReplaceList";
	}
	@RequestMapping(value = {"httpResReplace/form"})
	@RequiresPermissions(value={"proxy:contol:httpResReplace:config"})
	public String resForm(Model model,HttpServletRequest request
			,HttpServletResponse response,String ids
			,@ModelAttribute("cfg")HttpBodyCfg cfg
			,RedirectAttributes redirectAttributes){
		if(!StringUtil.isEmpty(ids)){
			cfg = controlPolicyService.getHttpBodyCfgById(Long.valueOf(ids));
			initUpdateFormCondition(model, cfg);
		}else{
			initFormCondition(model,cfg);
		}
		model.addAttribute("_cfg", cfg);
		return "/cfg/proxy/control/httpResReplaceForm";
	}
	
	@RequestMapping(value = {"httpResReplace/saveOrUpdate"})
	public String resSaveOrUpdate(Model model,HttpServletRequest request
			,HttpServletResponse response,
			@ModelAttribute("cfg")HttpBodyCfg cfg,
			@ModelAttribute("areaCfgIds")String areaCfgIds
			,RedirectAttributes redirectAttributes){
		try{
			controlPolicyService.saveOrUpdate(cfg,areaCfgIds);
			addMessage(redirectAttributes,"success","save_success");
		}catch(Exception e){
			logger.error("信息保存失败",e);
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,"error", "request_service_failed");
			}else {
				addMessage(redirectAttributes,"error","save_failed");
			}
		}
		
		return "redirect:" + adminPath +"/proxy/control/httpResReplace/list?functionId="+cfg.getFunctionId();
	}
	
	@RequestMapping(value = {"httpResReplace/delete"})
	@RequiresPermissions(value={"proxy:contol:httpResReplace:config"})
	public String resDelete(Integer isAudit,Integer isValid
			,String ids,Integer functionId
			,RedirectAttributes redirectAttributes){
		controlPolicyService.update(isAudit,isValid,ids,functionId);
		return "redirect:" + adminPath +"/proxy/control/httpResReplace/list?functionId="+functionId;
	}
	@RequestMapping(value = {"httpResReplace/audit"})
	public String resAudit(Integer isAudit,Integer isValid,String ids
			,Integer functionId, RedirectAttributes redirectAttributes) {
		if(!StringUtil.isEmpty(ids)){
			String[] idArray = ids.split(",");
			Date auditTime=new Date();
			for(String id :idArray){
				try {
					controlPolicyService.audit(isAudit,isValid,functionId,id,auditTime,Constants.REPLACE_RES_KEY_VALUE);
				} catch ( Exception e) {
					e.printStackTrace();
					logger.error("配置下发失败",e);
					if(e instanceof MaatConvertException) {
						addMessage(redirectAttributes,"error", "request_service_failed");
					}else {
						addMessage(redirectAttributes,"error","audit_failed");
					}
				}
			}
			
		}
		return "redirect:" + adminPath +"/proxy/control/httpResReplace/list?functionId="+functionId;
	}
}
