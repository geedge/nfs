package com.nis.web.controller.configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nis.domain.Page;
import com.nis.domain.configuration.AppPolicyCfg;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.configuration.NtcSubscribeIdCfg;
import com.nis.domain.specific.ConfigGroupInfo;
import com.nis.domain.specific.SpecificServiceCfg;
import com.nis.exceptions.CallExternalProceduresException;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.Constants;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseController;
import com.nis.web.security.UserUtils;

/**
 * 基础协议控制类
 * @author wx
 *
 */
@Controller
@RequestMapping("${adminPath}/basicprotocol")
public class BasicProtocolController extends BaseController {
	/**
	 * app策略列表
	 * @param model
	 * @param cfg
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = {"list"})
	public String policyCfgList(Model model,@ModelAttribute("cfg")AppPolicyCfg cfg,HttpServletRequest request,HttpServletResponse response) {
		Page<AppPolicyCfg> searchPage=new Page<AppPolicyCfg>(request,response,"r");
		Page<AppPolicyCfg> page = appCfgService.findAppPolicyList(searchPage, cfg);
//		for(AppPolicyCfg entity:page.getList()){
//			SpecificServiceCfg app = specificServiceCfgService.getBySpecServiceId(entity.getSpecServiceId());
//			if(app!=null) {
//				entity.setAppName(app.getSpecServiceName());
//			}
//			
//		}
		model.addAttribute("page", page);
		initPageCondition(model,cfg);
		return "/cfg/basicprotocol/list";
	}
	/**
	 * 查询APP策略IP子配置
	 * @param model
	 * @param cfgId
	 * @param index
	 * @return
	 */
	@RequestMapping(value = {"ajaxIpList"})
	public String ajaxSslSubList(Model model,Long cfgId,Integer index,Integer compileId) {
		AppPolicyCfg cfg = appCfgService.getAppPolicyCfg(cfgId,compileId);
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
		if(cfg.getNtcSubscribeIdCfgList()!=null){
			String cfgType = null;
			for(NtcSubscribeIdCfg ntc:cfg.getNtcSubscribeIdCfgList()){
				if(!ntc.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"2",ntc.getCfgType()});
					cfgType = ntc.getCfgType();
				}
			}
		}
		model.addAttribute("_cfg", cfg);
		model.addAttribute("index", index);
		model.addAttribute("tabList", tabList);
		return "/cfg/basicprotocol/basicProtocolSubList";
	}
	/**
	 * 策略配置表单
	 * @param model
	 * @param ids
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = {"form"})
	@RequiresPermissions(value={"basicprotocol:config"})
	public String policyCfgForm(Model model,String ids,AppPolicyCfg entity) {
		if(StringUtils.isNotBlank(ids)){
			entity = appCfgService.getAppPolicyCfg(Long.parseLong(ids),null);
			initUpdateFormCondition(model,entity);
		}else{
			initFormCondition(model,entity);
		}
		model.addAttribute("_cfg", entity);
		return "/cfg/basicprotocol/form";
	}
	/**
	 * 策略配置新增修改
	 * @param model
	 * @param request
	 * @param response
	 * @param entity
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"save"})
	@RequiresPermissions(value={"basicprotocol:config"})
	public String saveAppPolicyCfg(Model model,HttpServletRequest request,HttpServletResponse response,
			AppPolicyCfg entity,RedirectAttributes redirectAttributes) {
		try {
			SpecificServiceCfg specificService = specificServiceCfgService.getBySpecServiceId(entity.getSpecServiceId());
			if(specificService!=null){
				entity.setAppCode(specificService.getSpecServiceCode());
			}
			appCfgService.saveOrUpdateAppPolicyCfg(entity);
			addMessage(redirectAttributes,"success","save_success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("基础协议信息保存失败",e);
			if(e instanceof MaatConvertException) {
//				addMessage(redirectAttributes,e.getMessage());
				addMessage(redirectAttributes,"error","request_service_failed");
			}else if(e instanceof CallExternalProceduresException) {
				addMessage(redirectAttributes,"error","call_external_procedures_failed");
			}else {
				addMessage(redirectAttributes,"error","save_failed");
			}
		}
		
		return "redirect:" + adminPath +"/basicprotocol/list?functionId="+entity.getFunctionId();
	}
	/**
	 * 策略配置审核
	 * @param isAudit
	 * @param isValid
	 * @param ids
	 * @param functionId
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"audit"})
	@RequiresPermissions(value={"basicprotocol:confirm"})
	public String auditAppPolicyCfg(Model model,@ModelAttribute("cfg")AppPolicyCfg cfg,
										Integer isValid,
										Integer isAudit,
										String ids,
										Integer functionId, 
										RedirectAttributes redirectAttributes,
										HttpServletResponse response,
										HttpServletRequest request) {
		if(!StringUtil.isEmpty(ids)){
			AppPolicyCfg entity = new AppPolicyCfg();
			String[] idArray = ids.split(",");
			for(String id :idArray){
				entity = appCfgService.getAppPolicyCfg(Long.parseLong(id),null);
				entity.setIsAudit(isAudit);
				entity.setIsValid(isValid);
				entity.setAuditorId(UserUtils.getUser().getId());
				entity.setAuditTime(new Date());
				entity.setFunctionId(functionId);
				entity.setConfigType(Constants.SPECIFIC_SERVICE_CFG_TYPE_BASIC_PROTOCOL);
				try {
					appCfgService.auditAppPolicyCfg(entity,isAudit);
					addMessage(redirectAttributes,"success","audit_success");
				} catch (Exception e) {
					if(e instanceof MaatConvertException) {
						e.printStackTrace();
						logger.info("app策略配置下发失败："+e.getMessage());;
						addMessage(redirectAttributes,"error","request_service_failed");
					}else {
						e.printStackTrace();
						logger.error("app策略配置下发失败",e);
						addMessage(redirectAttributes,"error","audit_failed");
					}
					
				}
			}
		}else {
			Page<AppPolicyCfg> searchPage=new Page<AppPolicyCfg>(request,response,"r");
			Page<AppPolicyCfg> auditPage=new Page<AppPolicyCfg>(request,response,"r");
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
			
			return policyCfgList(model, cfg, request, response);
		}
		return "redirect:" + adminPath +"/basicprotocol/list?functionId="+functionId;
	}
	/**
	 * 策略配置删除
	 * @param isValid
	 * @param ids
	 * @param functionId
	 * @return
	 */
	@RequestMapping(value = {"updateValid"})
	@RequiresPermissions(value={"basicprotocol:config"})
	public String updateAppPolicyCfgValid(Integer isValid,String ids,Integer functionId,RedirectAttributes redirectAttributes) {
		try {
			appCfgService.updateAppPolicyCfgValid(isValid,ids,functionId);
			addMessage(redirectAttributes,"success","delete_success");
		} catch (Exception e) {
			logger.error("基础协议信息保存失败",e);
			if(e instanceof MaatConvertException) {
				e.printStackTrace();
				addMessage(redirectAttributes,"error","request_service_failed");
			}else if(e instanceof CallExternalProceduresException) {
				addMessage(redirectAttributes,"error","call_external_procedures_failed");
			}else {
				e.printStackTrace();
				addMessage(redirectAttributes,"error","delete_failed");
			}
		}
		return "redirect:" + adminPath +"/basicprotocol/list?functionId="+functionId;
	}
	
	//ip配置导出
		@RequestMapping(value = "exportIpAddr")
		public void exportIpAddr(Model model,HttpServletRequest  request,HttpServletResponse response,
				@ModelAttribute("cfg")AppPolicyCfg entity,String ids,RedirectAttributes redirectAttributes){
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
					Page<AppPolicyCfg> pageInfo=new Page<AppPolicyCfg>(request, response,"a");
					pageInfo.setPageNo(1);
					pageInfo.setPageSize(Constants.MAX_EXPORT_SIZE);
					Page<AppPolicyCfg> page = appCfgService.findAppPolicyList(pageInfo, entity);
					for(AppPolicyCfg policy:page.getList()){
						SpecificServiceCfg app = specificServiceCfgService.getBySpecServiceId(policy.getSpecServiceId());
						if(app!=null) {
							policy.setAppName(app.getSpecServiceName());
						}
						
					}
					titleList.add(entity.getMenuNameCode());
					titleList.add("NTC_IP");
					titleList.add("NTC_SUBSCRIBE_ID");
					classMap.put(entity.getMenuNameCode(), AppPolicyCfg.class);
					classMap.put("NTC_IP", IpPortCfg.class);
					classMap.put("NTC_SUBSCRIBE_ID", NtcSubscribeIdCfg.class);
					String cfgIndexInfoNoExport=",encrypted_tunnel_behavior,behaviour_type,social_app,behaviour_type,expression_type,match_method,whether_hexbinary,group_name,userregion1,userregion2,userregion3,userregion4,userregion5,";
					String ipPortInfoNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
											+ ",config_time,editor,edit_time,auditor,audit_time"
											+",letter,whether_area_block,classification,attribute,label"
											+",userregion1,userregion2,userregion3,userregion4,userregion5,ir_type,group_name,";
					String subscribeInfoNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
							+ ",config_time,editor,edit_time,auditor,audit_time"
							+",letter,whether_area_block,classification,attribute,label"
							+",userregion1,userregion2,userregion3,userregion4,userregion5,ir_type,group_name,";
					noExportMap.put(entity.getMenuNameCode(),cfgIndexInfoNoExport);
					noExportMap.put("NTC_IP", ipPortInfoNoExport);
					noExportMap.put("NTC_SUBSCRIBE_ID", subscribeInfoNoExport);
					List<IpPortCfg> ipList=new ArrayList<IpPortCfg>();
					List<BaseStringCfg> subscribeInfoList=new ArrayList<BaseStringCfg>();
					for (AppPolicyCfg cfg : page.getList()) {
						AppPolicyCfg cfgIndexInfo=appCfgService.exportIpInfo(cfg);
						ipList.addAll(cfgIndexInfo.getIpPortList());
						subscribeInfoList.addAll(cfgIndexInfo.getNtcSubscribeIdCfgList());
					}
					subscribeInfoList=BaseStringCfg.baseHexList(subscribeInfoList);
					dataMap.put(entity.getMenuNameCode(), page.getList());
					dataMap.put("NTC_IP", ipList);
					dataMap.put("NTC_SUBSCRIBE_ID", subscribeInfoList);
					
				/*}*/
				this._export(model, request, response, redirectAttributes,entity.getMenuNameCode(),titleList,classMap,dataMap,noExportMap);
			} catch (Exception e) {
				logger.error("ip addr export failed",e);
				addMessage(redirectAttributes,"error","export_failed");
			}
			//return "redirect:" + adminPath +"/ntc/iplist/list?functionId="+entity.getFunctionId();
		}
	
}
