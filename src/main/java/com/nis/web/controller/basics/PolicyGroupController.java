package com.nis.web.controller.basics;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nis.domain.Page;
import com.nis.domain.basics.PolicyGroupInfo;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseController;
/**
 * 协议分组
 * @author dell
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/basics/policyGroup")
public class PolicyGroupController extends BaseController {
	
	//@RequiresPermissions(value={"basics:policyGroup:view"},logical=Logical.OR)
	@RequestMapping(value = {"/policyGroupList", ""})
	public String policyGroupList(PolicyGroupInfo cfg,HttpServletRequest request
			, HttpServletResponse response, Model model
			,RedirectAttributes redirectAttributes) {
		if(cfg == null)cfg=new PolicyGroupInfo();
		Page<PolicyGroupInfo> pageCondition = new Page<PolicyGroupInfo>(request, response,"r");
		
		Page page = policyGroupInfoService.findPolicyGroupInfoList(pageCondition,cfg);
		
		model.addAttribute("cfg", cfg);
		model.addAttribute("page", page);
		return "/basics/policyGroupList";
	}
	
	
	/**
	 * 进入添加或修改页面
	 * @param serviceDictInfo
	 * @param model
	 * @return
	 */
	//@RequiresPermissions(value={"basics:classification:add","basics:attribute:add","basics:label:add","basics:classification:edit","basics:attribute:edit","basics:label:edit","basics:classification:view","basics:attribute:view","basics:label:view"},logical=Logical.OR)
	@RequestMapping(value={"/policyGroupForm"})
	public String form(String ids,Model model,String doAction,RedirectAttributes redirectAttributes) {
		PolicyGroupInfo policyGroup=new PolicyGroupInfo();
		if(!StringUtil.isEmpty(ids)){
			policyGroup=policyGroupInfoService.getById(Integer.parseInt(ids));
		}
		model.addAttribute("_cfg", policyGroup);
		return "/basics/policyGroupForm";
	}
	/**
	 * 新增或修改
	 * @return
	 */
	//@RequiresPermissions(value={"basics:classification:add","basics:attribute:add","basics:label:add","basics:classification:edit","basics:attribute:edit","basics:label:edit"},logical=Logical.OR)
	@RequestMapping(value = "saveOrUpdate")
	public String saveOrUpdate(PolicyGroupInfo cfg,Model model,
			RedirectAttributes redirectAttributes,String itType) {
		
		try {
			policyGroupInfoService.saveOrUpdate(cfg);
			addMessage(redirectAttributes, "save_success");
		} catch (Exception e) {
			logger.error("新增失败",e);
			addMessage(redirectAttributes, "save_failed");
		}
		
		return "redirect:" + adminPath + "/basics/policyGroup/policyGroupList";
		
		
	}
	
	
	/**
	 * 删除
	 * @return
	 */
	//@RequiresPermissions(value={"basics:classification:del","basics:attribute:del","basics:label:del"},logical=Logical.OR)
	@RequestMapping(value={"delete"})
	public String delete(RedirectAttributes redirectAttributes, String ids,int isValid) {
		try {
			policyGroupInfoService.deldete(ids,isValid);
			addMessage(redirectAttributes, "delete_success");
		} catch (Exception e) {
			logger.error("删除失败",e);
			addMessage(redirectAttributes, "delete_failed");
		}
		return "redirect:" + adminPath + "/basics/policyGroup/policyGroupList";
	}
	@RequestMapping(value="ajaxServiceIdState",method=RequestMethod.POST)
	@ResponseBody
	public boolean ajaxServiceIdState(Model model,@RequestParam(required=true,value="serviceGroupIds")String serviceGroupIds){
		if(StringUtils.isNotBlank(serviceGroupIds)) {
			return policyGroupInfoService.checkIsIssued(serviceGroupIds);
		}
		return false;
	}
	@RequestMapping(value="ajaxHasAsnIPs",method=RequestMethod.POST)
	@ResponseBody
	public boolean ajaxHasAsnIPs(Model model,@RequestParam(required=true,value="serviceGroupIds")String serviceGroupIds){
		if(StringUtils.isNotBlank(serviceGroupIds)) {
			return policyGroupInfoService.checkIps(serviceGroupIds);
		}
		return false;
	}
	/**
	 * 校验asn号是否已存在
	 */
	@ResponseBody
	@RequestMapping(value = {"/checkAsnNo"})
	public boolean checkIp(PolicyGroupInfo cfg, HttpServletRequest request, HttpServletResponse response){
		
		if(cfg.getGroupId() != null){
			PolicyGroupInfo policyGroup = policyGroupInfoService.getInfoByAsnNo(cfg);
			if(policyGroup != null && policyGroup.getAsnNo().equals(cfg.getAsnNo()) ){
				return true;
			}
		}
		cfg.setGroupId(null);
		PolicyGroupInfo policyGroup = policyGroupInfoService.getInfoByAsnNo(cfg);
		if(policyGroup != null){
			return false;
		}
		
		return true;
	}
}
