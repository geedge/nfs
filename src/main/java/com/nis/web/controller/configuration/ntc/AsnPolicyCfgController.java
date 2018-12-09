package com.nis.web.controller.configuration.ntc;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nis.domain.Page;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.specific.ConfigGroupInfo;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseController;
@Controller
@RequestMapping("${adminPath}/cfg/asnPolicy")
@Deprecated
public class AsnPolicyCfgController extends BaseController {
	@RequestMapping(value = {"/list"})
	public String list(Model model,HttpServletRequest  request
			,HttpServletResponse response,@ModelAttribute("cfg")CfgIndexInfo entity
			,RedirectAttributes redirectAttributes){
		Page<CfgIndexInfo> page = asnPolicyCfgService.findPage(new Page<CfgIndexInfo>(request, response,"r"), entity);
		model.addAttribute("page", page);
		initPageCondition(model,entity);
		return "/cfg/asnPolicyCfgList";
	}
	@RequestMapping(value = {"/form"})
	@RequiresPermissions(value={"asn:policy:config"})
	public String form(Model model,HttpServletRequest request
			,HttpServletResponse response,String ids,@ModelAttribute("cfg")CfgIndexInfo entity
			,RedirectAttributes redirectAttributes){
		if(!StringUtil.isEmpty(ids)){
			entity = asnPolicyCfgService.get(Long.parseLong(ids));
			initUpdateFormCondition(model, entity);
		}else{
			initFormCondition(model,entity);
		}
		List<ConfigGroupInfo> policyGroups=asnPolicyCfgService.getConfigGroupInfoList(4);
		model.addAttribute("policyGroups", policyGroups);
		model.addAttribute("_cfg", entity);
		return "/cfg/asnPolicyCfgForm";
	}
	@RequestMapping(value = {"/saveOrUpdate"})
	@RequiresPermissions(value={"asn:policy:config"})
	public String saveOrUpdate(Model model,HttpServletRequest request
			,HttpServletResponse response,
			@ModelAttribute("cfg")CfgIndexInfo cfg
			,RedirectAttributes redirectAttributes){
		try{
			asnPolicyCfgService.saveOrUpdate(cfg);
			addMessage(redirectAttributes,"success","save_success");
		}catch(Exception e){
			logger.error("信息保存失败",e);
			e.printStackTrace();
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,"error", "request_service_failed");
			}else {
				addMessage(redirectAttributes,"error","save_failed");
			}
		}
		
		return "redirect:" + adminPath +"/cfg/asnPolicy/list?functionId="+cfg.getFunctionId();
	}
	@RequestMapping(value = {"/delete"})
	@RequiresPermissions(value={"asn:policy:config"})
	public String delete(Integer isAudit,Integer isValid
			,String ids,Integer functionId
			,RedirectAttributes redirectAttributes){
		asnPolicyCfgService.delete(functionId, isValid, ids);
		return "redirect:" + adminPath +"/cfg/asnPolicy/list?functionId="+functionId;
	}
//	@RequestMapping(value = {"/audit"})
//	@RequiresPermissions(value={"asn:policy:confirm"})
//	public String audit(Integer isAudit,Integer isValid,String ids
//			,Integer functionId, RedirectAttributes redirectAttributes) {
//		if(!StringUtil.isEmpty(ids)){
//			String[] idArray = ids.split(",");
//			Date auditTime=new Date();
//			for(String id :idArray){
//				try {
//					asnPolicyCfgService.audit(isAudit,isValid,functionId,id,auditTime);
//				} catch (MaatConvertException e) {
//					e.printStackTrace();
//					logger.error("dns fake ip配置下发失败："+e.getMessage());
//					addMessage(redirectAttributes,"error","request_service_failed");
//				} catch (Exception e) {
//					e.printStackTrace();
//					logger.error("dns fake ip配置下发失败："+e.getMessage());
//					addMessage(redirectAttributes,"error","audit_failed");
//				}
//				
//			}
//			
//		}
//		return "redirect:" + adminPath +"/cfg/asnPolicy/list?functionId="+functionId;
//	}
}
