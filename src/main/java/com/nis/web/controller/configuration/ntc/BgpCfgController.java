package com.nis.web.controller.configuration.ntc;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.nis.domain.configuration.ComplexkeywordCfg;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.configuration.NtcBgpAsCfg;
import com.nis.domain.configuration.NtcSubscribeIdCfg;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.Constants;
import com.nis.util.StringUtil;
import com.nis.util.StringUtils;
import com.nis.web.controller.BaseController;
import com.nis.web.security.UserUtils;

/**
 * BGP配置
 * @author dell
 *
 */
@Controller
@RequestMapping("${adminPath}/ntc/other/")
public class BgpCfgController extends BaseController{
	
	@RequestMapping(value = {"bgpList"})
//	@RequiresPermissions(value={"other:bgp:config"})
	public String bgpList(Model model,@ModelAttribute("cfg")CfgIndexInfo cfg
			,HttpServletRequest request,HttpServletResponse response) {
		Page<CfgIndexInfo> searchPage=new Page<CfgIndexInfo>(request,response,"a");
		Page<CfgIndexInfo> page = bgpCfgService.getBgpList(searchPage, cfg);
		model.addAttribute("page", page);
		initPageCondition(model,cfg);
		return "/cfg/other/bgpList";
	}
	
	@RequestMapping(value = {"bgpForm"})
	@RequiresPermissions(value={"other:bgp:config"})
	public String bgpForm(Model model,String ids,CfgIndexInfo entity,RedirectAttributes redirectAttributes) {
		if(StringUtils.isNotBlank(ids)){
			entity = bgpCfgService.getBgpCfg(Long.parseLong(ids),null);
			initUpdateFormCondition(model,entity);
		}else{
			initFormCondition(model,entity);
		}
		model.addAttribute("_cfg", entity);
		return "/cfg/other/bgpForm";
	}
	
	@RequestMapping(value = {"saveBgpCfg"})
	@RequiresPermissions(value={"other:bgp:config"})
	public String saveBgpCfg(Model model,HttpServletRequest request,HttpServletResponse response
			,String ids,CfgIndexInfo entity
			,RedirectAttributes redirectAttributes) {
		bgpCfgService.saveBgpCfg(entity);
		return "redirect:" + adminPath +"/ntc/other/bgpList?functionId="+entity.getFunctionId();
	}
	
	@RequestMapping(value = {"ajaxBgpSubList"})
	public String ajaxBgpSubList(Model model,Long cfgId,Integer index,Integer compileId) {
		CfgIndexInfo cfg = bgpCfgService.getBgpCfg(cfgId,compileId);
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
		if(cfg.getNtcBgpAsCfgList()!=null){
			String cfgType = null;
			for(NtcBgpAsCfg ip:cfg.getNtcBgpAsCfgList()){
				if(!ip.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"2",ip.getCfgType()});
					cfgType = ip.getCfgType();
				}
			}
		}
		if(cfg.getNtcSubscribeIdCfgList()!=null){
			String cfgType = null;
			for(NtcSubscribeIdCfg ip:cfg.getNtcSubscribeIdCfgList()){
				if(!ip.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"2",ip.getCfgType()});
					cfgType = ip.getCfgType();
				}
			}
		}
		/*TODO 
		 * if(cfg.getDomainList()!=null){
			String cfgType = null;
			for(ComplexkeywordCfg keyword:cfg.getDomainList()){
				if(!keyword.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"3",keyword.getCfgType()});
					cfgType = keyword.getCfgType();
				}
			}
		}*/
		model.addAttribute("_cfg", cfg);	
		model.addAttribute("index", index);
		model.addAttribute("tabList", tabList);
		return "/cfg/other/bgpSubList";
	}
	
	@RequestMapping(value = {"updateBgpCfgValid"})
	@RequiresPermissions(value={"other:bgp:config"})
	public String updateBgpCfgValid(Integer isValid,String ids,Integer functionId,RedirectAttributes redirectAttributes) {
		bgpCfgService.updateBgpCfgValid(isValid,ids,functionId);
		return "redirect:" + adminPath +"/ntc/other/bgpList?functionId="+functionId;
	}
	
	@RequestMapping(value = {"auditBgpCfg"})
	@RequiresPermissions(value={"other:bgp:confirm"})
	public String auditBgpCfg(Model model,@ModelAttribute("cfg")CfgIndexInfo cfg,
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
				entity = bgpCfgService.getBgpCfg(Long.parseLong(id),null);
				entity.setIsAudit(isAudit);
				entity.setIsValid(isValid);
				entity.setAuditorId(UserUtils.getUser().getId());
				entity.setAuditTime(new Date());
				entity.setFunctionId(functionId);
				try {
					bgpCfgService.auditBgpCfg(entity,isAudit);
				} catch (MaatConvertException e) {
					e.printStackTrace();
					logger.error("bgp配置下发失败",e);
					addMessage(redirectAttributes,"error","request_service_failed");
				}catch (Exception e) {
					e.printStackTrace();
					logger.error("bgp配置下发失败",e);
					addMessage(redirectAttributes,"error","audit_failed");
				}
			}
		}else {
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
			
			return bgpList(model, cfg, request, response);
		}
		return "redirect:" + adminPath +"/ntc/other/bgpList?functionId="+functionId;
	}
	
	//bgp配置导出
		@RequestMapping(value = "exportbgp")
		public void exportbgp(Model model,HttpServletRequest  request,HttpServletResponse response,
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
					Page<CfgIndexInfo> page = bgpCfgService.getBgpList(pageInfo, entity);
					titleList.add(entity.getMenuNameCode());
					titleList.add("NTC_IP");
					titleList.add("NTC_BGP_AS");
//					titleList.add("NTC_SUBSCRIBE_ID");
					classMap.put(entity.getMenuNameCode(), CfgIndexInfo.class);
					classMap.put("NTC_IP", IpPortCfg.class);
					classMap.put("NTC_BGP_AS", BaseStringCfg.class);
//					classMap.put("NTC_SUBSCRIBE_ID", NtcSubscribeIdCfg.class);
					String cfgIndexInfoNoExport=",policy_name,group_name,userregion1,userregion2,userregion3,userregion4,userregion5,";
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
					noExportMap.put("NTC_BGP_AS", regionCfgNoExport);
//					noExportMap.put("NTC_SUBSCRIBE_ID", ntcSubscribeIdCfgNoExport);
					List<IpPortCfg> ipList=new ArrayList<IpPortCfg>();
//					List<BaseStringCfg> subscribeInfoList=new ArrayList<BaseStringCfg>();
					List<BaseStringCfg> asInfoList=new ArrayList<BaseStringCfg>();
					for (CfgIndexInfo cfg : page.getList()) {
						CfgIndexInfo cfgIndexInfo=bgpCfgService.exportbgp(cfg);
						ipList.addAll(cfgIndexInfo.getIpPortList());
//						subscribeInfoList.addAll(cfgIndexInfo.getNtcSubscribeIdCfgList());
						asInfoList.addAll(cfgIndexInfo.getNtcBgpAsCfgList());
					}
					asInfoList=BaseStringCfg.replaceBaseKeyList(asInfoList);
//					subscribeInfoList=BaseStringCfg.baseHexList(subscribeInfoList);
					dataMap.put(entity.getMenuNameCode(), page.getList());
					dataMap.put("NTC_IP", ipList);
					dataMap.put("NTC_BGP_AS", asInfoList);
//					dataMap.put("NTC_SUBSCRIBE_ID", subscribeInfoList);
					
				/*}*/
				this._export(model, request, response, redirectAttributes,entity.getMenuNameCode(),titleList,classMap,dataMap,noExportMap);
			} catch (Exception e) {
				logger.error("bgp export failed",e);
				addMessage(redirectAttributes,"error","export_failed");
			}
			//return "redirect:" + adminPath +"/ntc/iplist/list?functionId="+entity.getFunctionId();
		}
	
}
