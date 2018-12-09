package com.nis.web.controller.configuration;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nis.domain.Page;
import com.nis.domain.basics.PolicyGroupInfo;
import com.nis.domain.configuration.GroupAreaInfo;
import com.nis.exceptions.CallExternalProceduresException;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseController;
import com.nis.web.service.configuration.GroupAreaService;

/**
 * 分组地理信息管理
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/cfg/groupArea")
public class GroupAreaController extends BaseController {

	@Autowired
	private GroupAreaService groupAreaService;

	@RequestMapping(value = { "/groupAreaList", "" })
	public String policyGroupList(GroupAreaInfo cfg, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		if (cfg == null)cfg = new GroupAreaInfo();
		Page<GroupAreaInfo> pageCondition = new Page<GroupAreaInfo>(request, response, "r");
		Page page = groupAreaService.findGroupAreaInfoList(pageCondition, cfg);
		// 获取策略分组信息
		List<PolicyGroupInfo> policyGroups = policyGroupInfoService.findNatPolicyGroups();
		// 获取区域信息
		initFormCondition(model);
		model.addAttribute("cfg", cfg);
		model.addAttribute("page", page);
		model.addAttribute("policyGroups", policyGroups);
		return "/cfg/manipulation/ipmulitiplex/groupAreaList";
	}
	
	@RequestMapping(value={"/groupAreaForm"})
	public String form(String ids,Model model,String doAction,RedirectAttributes redirectAttributes) {
		GroupAreaInfo groupArea=new GroupAreaInfo();
		// 获取未使用的策略分组
		List<PolicyGroupInfo> groups = policyGroupInfoService.findNatPolicyGroups();
		groups = groupAreaService.getNotUsedPolicyGroups(groups);
		if(!StringUtil.isEmpty(ids)){
			groupArea=groupAreaService.getInfoById(Integer.parseInt(ids));
			groups.add(policyGroupInfoService.getById(groupArea.getGroupId()));
		}
		// 获取区域信息
		initFormCondition(model);
		model.addAttribute("_cfg", groupArea);
		model.addAttribute("policyGroups", groups);
		return "/cfg/manipulation/ipmulitiplex/groupAreaForm";
	}
	
	@RequestMapping(value = "saveOrUpdate")
	public String saveOrUpdate(GroupAreaInfo cfg,Model model,RedirectAttributes redirectAttributes) {
		try {
			groupAreaService.saveOrUpdate(cfg);
			addMessage(redirectAttributes,"success","save_success");
		} catch (Exception e) {
			logger.error("新增失败",e);
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,"error","request_service_failed");
			}else if(e instanceof CallExternalProceduresException) {
				addMessage(redirectAttributes,"error","call_external_procedures_failed");
			}else {
				addMessage(redirectAttributes,"error","save_failed");
			}
		}
		return "redirect:" + adminPath + "/cfg/groupArea/groupAreaList";
	}
	
	@RequestMapping(value={"delete"})
	public String delete(RedirectAttributes redirectAttributes, String ids,int isValid) {
		try {
			groupAreaService.deldete(ids,isValid);
			addMessage(redirectAttributes,"success","delete_success");
		} catch (Exception e) {
			logger.error("删除失败",e);
			addMessage(redirectAttributes,"error","delete_failed");
		}
		return "redirect:" + adminPath + "/cfg/groupArea/groupAreaList";
	}
	
}
