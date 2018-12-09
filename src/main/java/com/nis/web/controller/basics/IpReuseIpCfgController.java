package com.nis.web.controller.basics;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.nis.domain.FunctionRegionDict;
import com.nis.domain.FunctionServiceDict;
import com.nis.domain.Page;
import com.nis.domain.basics.IpReuseIpCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.DictUtils;
import com.nis.web.controller.BaseController;
@Controller
@RequestMapping(value = "${adminPath}/basics/vpn")
public class IpReuseIpCfgController extends BaseController{
	@RequestMapping(value = {"/list"})
	public String list(Model model,HttpServletRequest  request
			,HttpServletResponse response,@ModelAttribute("cfg")IpReuseIpCfg entity
			,RedirectAttributes redirectAttributes){
		Page<IpReuseIpCfg> page = ipReuseIpCfgService.findPage(new Page<IpReuseIpCfg>(request, response,"r"), entity);
		model.addAttribute("page", page);
		
		List<FunctionRegionDict> regionList = DictUtils.getFunctionRegionDictList(entity.getFunctionId());
		model.addAttribute("regionList", regionList);
		List<FunctionServiceDict> serviceList = DictUtils.getFunctionServiceDictList(entity.getFunctionId());
		model.addAttribute("serviceList", serviceList);
		
		return "/basics/userIpCfgList";
	}
	@RequestMapping(value = {"/addForm"})
	public String addForm(Model model,HttpServletRequest request
			,HttpServletResponse response,@ModelAttribute("cfg")CfgIndexInfo cfg
			,RedirectAttributes redirectAttributes){
		initFormCondition(model,cfg);
		model.addAttribute("_cfg", cfg);
		return "/basics/userIpCfgFormAdd";
	}
	@RequestMapping(value = {"/updateForm"})
	@RequiresPermissions(value={"other:bgp:config"})
	public String updateForm(Model model,HttpServletRequest request
			,HttpServletResponse response,String ids,@ModelAttribute("cfg")IpReuseIpCfg cfg
			,RedirectAttributes redirectAttributes){
		if(StringUtils.isNotBlank(ids)){
			cfg = ipReuseIpCfgService.getUserIpCfg(Long.parseLong(ids));
			initUpdateFormCondition(model,cfg);
		}else{
			initFormCondition(model,cfg);
		}
		model.addAttribute("_cfg", cfg);
		return "/basics/userIpCfgFormUpdate";
	}
	

	
	@RequestMapping(value = {"save"})
	@RequiresPermissions(value={"other:bgp:config"})
	public String saveBgpCfg(Model model,HttpServletRequest request,HttpServletResponse response
			,String ids,CfgIndexInfo entity
			,RedirectAttributes redirectAttributes) {
		try{
		ipReuseIpCfgService.save(entity);;
		addMessage(redirectAttributes,"save_success");
		}catch(Exception e){
			logger.error("信息保存失败",e);
			e.printStackTrace();
			addMessage(redirectAttributes,"save_failed");
		}
		return "redirect:" + adminPath +"/basics/vpn/list?functionId="+entity.getFunctionId();
	}
	
	
	
	@RequestMapping(value = {"/update"})
	@RequiresPermissions(value={"asn:ip:config"})
	public String update(Model model,HttpServletRequest request,HttpServletResponse response,@ModelAttribute("cfg")IpReuseIpCfg cfg,RedirectAttributes redirectAttributes){
		try{
			ipReuseIpCfgService.update(cfg);
			addMessage(redirectAttributes,"save_success");
		}catch(Exception e){
			logger.error("信息保存失败",e);
			e.printStackTrace();
			addMessage(redirectAttributes,"save_failed");
		}
		return "redirect:" + adminPath +"/basics/vpn/list?functionId="+cfg.getFunctionId();
	}
	@RequestMapping(value = {"/delete"})
	@RequiresPermissions(value={"asn:ip:config"})
	public String delete(Integer isValid
			,String ids,Integer functionId
			,RedirectAttributes redirectAttributes){
			try{
				ipReuseIpCfgService.delete(isValid,ids);
				addMessage(redirectAttributes,"delete_success");
			}catch(Exception e){
				logger.error("Delete failed",e);
				if(e instanceof MaatConvertException) {
					addMessage(redirectAttributes,e.getMessage());
				}else {
					addMessage(redirectAttributes,"delete_failed");
				}
			}
			
		return "redirect:" + adminPath +"/basics/vpn/list?functionId="+functionId;
	}
	
	/**
	 * 验证登录名是否有效
	 * @param oldLoginName
	 * @param loginName
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("user:manage:edit")
	@RequestMapping(value = "checkIp")
	public String checkIp(String oldDestIpAddress, String destIpAddress) {
		if (destIpAddress !=null && destIpAddress.equals(oldDestIpAddress)) {
			return "true";
		} else if (destIpAddress !=null && ipReuseIpCfgService.getIpByIp(destIpAddress) == null) {
			return "true";
		}
		return "false";
	}
	
}
