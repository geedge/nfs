package com.nis.web.controller.basics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.nis.domain.basics.AsnIpCfg;
import com.nis.domain.basics.PolicyGroupInfo;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.specific.ConfigGroupInfo;
import com.nis.domain.specific.SpecificServiceCfg;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.AsnCacheUtils;
import com.nis.util.Constants;
import com.nis.util.DictUtils;
import com.nis.web.controller.BaseController;
@Controller
@RequestMapping(value = "${adminPath}/basics/asn")
public class AsnIpController extends BaseController{
	@RequestMapping(value = {"/list"})
	public String list(Model model,HttpServletRequest  request
			,HttpServletResponse response,@ModelAttribute("cfg")AsnIpCfg entity
			,RedirectAttributes redirectAttributes){
		Page<AsnIpCfg> page = asnIpCfgService.findPage(new Page<AsnIpCfg>(request, response,"r"), entity);
		model.addAttribute("page", page);
//		initPageCondition(model);
		List<FunctionRegionDict> regionList = DictUtils.getFunctionRegionDictList(entity.getFunctionId());
		model.addAttribute("regionList", regionList);
		List<FunctionServiceDict> serviceList = DictUtils.getFunctionServiceDictList(entity.getFunctionId());
		model.addAttribute("serviceList", serviceList);
		
//		List<PolicyGroupInfo> policyGroups=policyGroupInfoService.findPolicyGroupInfosByType(4);
//		model.addAttribute("policyGroups", policyGroups);
		AsnCacheUtils.init(false);
		List<ConfigGroupInfo> policyGroups=AsnCacheUtils.getAllAsnGroup();
		model.addAttribute("policyGroups", policyGroups);
		return "/basics/asnIpCfgList";
	}
	@RequestMapping(value = {"/addForm"})
	public String addForm(Model model,HttpServletRequest request
			,HttpServletResponse response,@ModelAttribute("cfg")CfgIndexInfo cfg
			,RedirectAttributes redirectAttributes){
		initFormCondition(model,cfg);
		List<PolicyGroupInfo> policyGroups=policyGroupInfoService.findPolicyGroupInfosByType(4);
		model.addAttribute("policyGroups", policyGroups);
		model.addAttribute("_cfg", cfg);
		return "/basics/asnIpCfgFormAdd";
	}
	@RequestMapping(value = {"/updateForm"})
	public String updateForm(Model model,HttpServletRequest request
			,HttpServletResponse response,String ids,@ModelAttribute("cfg")AsnIpCfg cfg
			,RedirectAttributes redirectAttributes){
		cfg = asnIpCfgService.get(Long.parseLong(ids));
		initUpdateFormCondition(model, cfg);
		List<PolicyGroupInfo> policyGroups=policyGroupInfoService.findPolicyGroupInfosByType(4);
		model.addAttribute("policyGroups", policyGroups);
		model.addAttribute("_cfg", cfg);
		return "/basics/asnIpCfgFormUpdate";
	}
	@RequestMapping(value = {"/save"})
	@RequiresPermissions(value={"asn:ip:config"})
	public String save(Model model,HttpServletRequest request,HttpServletResponse response,@ModelAttribute("cfg")CfgIndexInfo cfg,RedirectAttributes redirectAttributes){
		try{
			asnIpCfgService.saveAsnIpCfg(cfg);
			addMessage(redirectAttributes,"save_success");
		}catch(Exception e){
			logger.error("信息保存失败",e);
			e.printStackTrace();
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,e.getMessage());
			}else {
				addMessage(redirectAttributes,"save_failed");
			}
		}
		
		return "redirect:" + adminPath +"/basics/asn/list?functionId="+cfg.getFunctionId();
	}
	@RequestMapping(value = {"/update"})
	@RequiresPermissions(value={"asn:ip:config"})
	public String update(Model model,HttpServletRequest request,HttpServletResponse response,@ModelAttribute("cfg")AsnIpCfg cfg,RedirectAttributes redirectAttributes){
		try{
			
			asnIpCfgService.update(cfg);
			addMessage(redirectAttributes,"save_success");
		}catch(Exception e){
			logger.error("信息保存失败",e);
			e.printStackTrace();
			addMessage(redirectAttributes,"save_failed");
		}
		
		return "redirect:" + adminPath +"/basics/asn/list?functionId="+cfg.getFunctionId();
	}
	@RequestMapping(value = {"/delete"})
	@RequiresPermissions(value={"asn:ip:config"})
	public String delete(Integer isValid
			,String ids,Integer functionId
			,RedirectAttributes redirectAttributes){
			try{
				asnIpCfgService.delete(isValid,ids);
				addMessage(redirectAttributes,"delete_success");
			}catch(Exception e){
				logger.error("Delete failed",e);
				if(e instanceof MaatConvertException) {
					addMessage(redirectAttributes,e.getMessage());
				}else {
					addMessage(redirectAttributes,"delete_failed");
				}
			}
			
		return "redirect:" + adminPath +"/basics/asn/list?functionId="+functionId;
	}
	/**
	 * ajax设置Name
	 * @param model
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="ajaxGroupName",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> ajaxGroupName(Model model,String ids){
		AsnCacheUtils.init(false);
		Map<String, String> groupMap=new HashMap<>();
		for(String id:ids.split(",")) {
			ConfigGroupInfo info=AsnCacheUtils.get(Long.parseLong(id));
			if(info!=null) {
				groupMap.put(id, info.getGroupName());
			}else {
				info=asnIpCfgService.getConfigGroupInfoByAsnNo(Long.parseLong(id));
				groupMap.put(id, info.getGroupName());
			}
		}
		return groupMap;
	}
	@RequestMapping(value="ajaxServiceIdState",method=RequestMethod.POST)
	@ResponseBody
	public boolean ajaxServiceIdState(Model model,@RequestParam(required=true,value="serviceGroupIds")String serviceGroupIds){
		if(StringUtils.isNotBlank(serviceGroupIds)) {
			return policyGroupInfoService.checkIsIssued(serviceGroupIds);
		}
		return false;
	}
	@RequestMapping(value="ajaxIsLast",method=RequestMethod.POST)
	@ResponseBody
	public boolean ajaxIsLast(Model model,@RequestParam(required=true,value="serviceGroupIds")String serviceGroupIds,@RequestParam(required=true,value="ids")String ids){
		if(StringUtils.isNotBlank(serviceGroupIds)&&StringUtils.isNotBlank(ids)) {
			return asnIpCfgService.hasLastIp(serviceGroupIds,ids);
		}
		return false;
	}
	
	//asnIp配置导出
	@RequestMapping(value = "exportAsnIp")
	public void exportAsnIp(Model model,HttpServletRequest  request,HttpServletResponse response,
			@ModelAttribute("cfg")AsnIpCfg entity,String ids,RedirectAttributes redirectAttributes){
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
				Page<AsnIpCfg> pageInfo=new Page<AsnIpCfg>(request, response,"r");
				pageInfo.setPageNo(1);
				pageInfo.setPageSize(Constants.MAX_EXPORT_SIZE);
				Page<AsnIpCfg> page = asnIpCfgService.findPage(pageInfo, entity);
				for (AsnIpCfg asnIp : page.getList()) {
					asnIp.setIsIssued(String.valueOf(asnIp.getIsValid()));
				}
				titleList.add(entity.getMenuNameCode());
				classMap.put(entity.getMenuNameCode(), AsnIpCfg.class);
				String ipPortInfoNoExport=",do_log,action,valid_identifier,is_audit"
						+ ",auditor,audit_time,letter,whether_area_block,classification,attribute,label"
						+",userregion1,userregion2,userregion3,userregion4,userregion5,ir_type,group_name,";
				noExportMap.put(entity.getMenuNameCode(),ipPortInfoNoExport);
				dataMap.put(entity.getMenuNameCode(), page.getList());
			/*}*/
			this._export(model, request, response, redirectAttributes,entity.getMenuNameCode(),titleList,classMap,dataMap,noExportMap);
		} catch (Exception e) {
			logger.error("asnIp export failed",e);
			addMessage(redirectAttributes,  "export_failed");
		}
		//return "redirect:" + adminPath +"/ntc/iplist/list?functionId="+entity.getFunctionId();
	}
}
