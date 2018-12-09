package com.nis.web.controller.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nis.domain.Page;
import com.nis.domain.SysDataDictionaryItem;
import com.nis.domain.configuration.AppBuiltInFeatureFile;
import com.nis.domain.configuration.AppByteCfg;
import com.nis.domain.configuration.AppDomainCfg;
import com.nis.domain.configuration.AppHeaderCfg;
import com.nis.domain.configuration.AppHttpCfg;
import com.nis.domain.configuration.AppIpCfg;
import com.nis.domain.configuration.AppPolicyCfg;
import com.nis.domain.configuration.AppSslCertCfg;
import com.nis.domain.configuration.AppTcpCfg;
import com.nis.domain.configuration.AppTopicDomainCfg;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.configuration.NtcSubscribeIdCfg;
import com.nis.domain.configuration.WebsiteDomainTopic;
import com.nis.domain.maat.ToMaatResult;
import com.nis.domain.maat.ToMaatResult.ResponseData;
import com.nis.domain.specific.SpecificServiceCfg;
import com.nis.exceptions.CallExternalProceduresException;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.Constants;
import com.nis.util.DictUtils;
import com.nis.util.FileUtils;
import com.nis.util.JsonMapper;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseController;
import com.nis.web.security.UserUtils;

/**
 * 特定协议相关配置控制类
 * @author zhangwei
 *
 */
@Controller
@RequestMapping("${adminPath}/app")
public class AppCfgController extends BaseController {
	/**
	 * app策略列表
	 * @param model
	 * @param cfg
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = {"policyCfgList"})
	public String policyCfgList(Model model,@ModelAttribute("cfg")AppPolicyCfg cfg,HttpServletRequest request,HttpServletResponse response) {
		Page<AppPolicyCfg> searchPage=new Page<AppPolicyCfg>(request,response,"r");
		Page<AppPolicyCfg> page = appCfgService.findAppPolicyList(searchPage, cfg);
//		for(AppPolicyCfg entity:page.getList()){
//			SpecificServiceCfg app = specificServiceCfgService.getBySpecServiceId(entity.getSpecServiceId());
//			if(app!=null) {
//				entity.setAppName(app.getSpecServiceName());
//			}
//		}
		//查找社交应用的所有有效二级特定服务
//		SpecificServiceCfg second=new SpecificServiceCfg();
//		second.setCfgType(1);
//		second.setIsValid(Constants.VALID_YES);
//		second.setIsLeaf(1);
//		List<SpecificServiceCfg> secondList=specificServiceCfgService.findAllSpecificServiceCfg(second, null);
		
		//遍历，找到匹配项后将行为设置进去
//		for(AppPolicyCfg entity:page.getList()){
//			if(entity.getBehavCode()==null) continue;
//			for(SpecificServiceCfg secondCfg:secondList) {
//				if(secondCfg.getSpecServiceCode()==null) continue;
//				if(secondCfg.getSpecServiceCode().intValue()==entity.getBehavCode().intValue()) {
//					entity.setBehavName(secondCfg.getSpecServiceName());
//					break;
//				}
//			}
//		}
		model.addAttribute("page", page);
		initPageCondition(model,cfg);
		return "/cfg/app/appPolicyCfgList";
	}
	/**
	 * ajax设置Name
	 * @param model
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="ajaxAppName",method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> ajaxAppName(Model model,String ids){
		List<Map<String, String>> dataList=new ArrayList<>();
		List<SpecificServiceCfg> serviceList=specificServiceCfgService.getBySpecServiceCodes(ids);
		Map<String, String> appMap=new HashMap<>();
		Map<String, String> behavMap=new HashMap<>();
		for(SpecificServiceCfg cfg:serviceList) {
			if(cfg.getIsLeaf().intValue()==0) {
				appMap.put(cfg.getSpecServiceCode().toString(), cfg.getSpecServiceName());
			}else {
				behavMap.put(cfg.getSpecServiceCode().toString(), cfg.getSpecServiceName());
			}
		}
		dataList.add(appMap);
		dataList.add(behavMap);
		return dataList;
	}
	/**
	 * 查询APP策略IP子配置
	 * @param model
	 * @param cfgId
	 * @param index
	 * @return
	 */
	@RequestMapping(value = {"ajaxAppPolicyIpList"})
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
		return "/cfg/app/appPolicySubList";
	}
	/**
	 * 策略配置表单
	 * @param model
	 * @param ids
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = {"policyCfgForm"})
	@RequiresPermissions(value={"app:policy:config"})
	public String policyCfgForm(Model model,String ids,AppPolicyCfg entity) {
		if(StringUtils.isNotBlank(ids)){
			entity = appCfgService.getAppPolicyCfg(Long.parseLong(ids),null);
			initUpdateFormCondition(model,entity);
		}else{
			initFormCondition(model,entity);
		}
		model.addAttribute("_cfg", entity);
		return "/cfg/app/appPolicyCfgForm";
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
	@RequestMapping(value = {"saveAppPolicyCfg"})
	@RequiresPermissions(value={"app:policy:config"})
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
			logger.error("saveAppPolicyCfg failed",e);
			e.printStackTrace();
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,"error","request_service_failed");
			}else {
				addMessage(redirectAttributes,"error","save_failed");
			}
		}
		
		return "redirect:" + adminPath +"/app/policyCfgList?functionId="+entity.getFunctionId();
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
	@RequestMapping(value = {"auditAppPolicyCfg"})
	@RequiresPermissions(value={"app:policy:confirm"})
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
				entity.setConfigType(Constants.SPECIFIC_SERVICE_CFG_TYPE_APP);
				try {
					appCfgService.auditAppPolicyCfg(entity,isAudit);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("app策略配置下发失败",e);
					if(e instanceof MaatConvertException) {
						addMessage(redirectAttributes,"error","request_service_failed");
					}else {
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
		
		return "redirect:" + adminPath +"/app/policyCfgList?functionId="+functionId;
	}
	/**
	 * 策略配置删除
	 * @param isValid
	 * @param ids
	 * @param functionId
	 * @return
	 */
	@RequestMapping(value = {"updateAppPolicyCfgValid"})
	@RequiresPermissions(value={"app:policy:config"})
	public String updateAppPolicyCfgValid(Integer isValid,String ids,Integer functionId) {
		appCfgService.updateAppPolicyCfgValid(isValid,ids,functionId);
		return "redirect:" + adminPath +"/app/policyCfgList?functionId="+functionId;
	}
	
	/**
	 * app协议IP配置列表
	 * @param model
	 * @param cfg
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = {"ipCfgList"})
	public String ipCfgList(Model model,@ModelAttribute("cfg")AppIpCfg cfg,HttpServletRequest request,HttpServletResponse response) {
		Page<AppIpCfg> searchPage=new Page<AppIpCfg>(request,response,"r");
		Page<AppIpCfg> page = appCfgService.findAppIpList(searchPage, cfg);
		for(AppIpCfg entity:page.getList()){
			SpecificServiceCfg app = specificServiceCfgService.getBySpecServiceId(entity.getSpecServiceId());
			entity.setAppName(app.getSpecServiceName());
		}
		model.addAttribute("page", page);
		initPageCondition(model,cfg);
		return "/cfg/app/appIpCfgList";
	}
	/**
	 * 协议IP配置表单
	 * @param model
	 * @param ids
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = {"ipCfgForm"})
	@RequiresPermissions(value={"app:ip:config"})
	public String ipCfgForm(Model model,String ids,AppIpCfg entity) {
		if(StringUtils.isNotBlank(ids)){
			entity = appCfgService.getAppIpCfg(Long.parseLong(ids));
			initUpdateFormCondition(model,entity);
		}else{
			initFormCondition(model,entity);
		}
		model.addAttribute("_cfg", entity);
		return "/cfg/app/appIpCfgForm";
	}
	/**
	 * 协议IP配置新增修改
	 * @param model
	 * @param request
	 * @param response
	 * @param entity
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"saveAppIpCfg"})
	@RequiresPermissions(value={"app:ip:config"})
	public String saveAppIpCfg(Model model,HttpServletRequest request,HttpServletResponse response,
			AppIpCfg entity,RedirectAttributes redirectAttributes) {
		try {
			SpecificServiceCfg specificService = specificServiceCfgService.getBySpecServiceId(entity.getSpecServiceId());
			if(specificService!=null){
				entity.setAppCode(specificService.getSpecServiceCode());
			}
			appCfgService.saveOrUpdateAppIpCfg(entity);
			addMessage(redirectAttributes,"success","save_success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("saveAppIpCfg failed",e);
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,"error","request_service_failed");
			}else {
				addMessage(redirectAttributes,"error","save_failed");
			}
		}
		
		return "redirect:" + adminPath +"/app/ipCfgList?functionId="+entity.getFunctionId();
	}
	/**
	 * 协议IP配置审核
	 * @param isAudit
	 * @param isValid
	 * @param ids
	 * @param functionId
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"auditAppIpCfg"})
	@RequiresPermissions(value={"app:ip:confirm"})
	public String auditAppIpCfg(Model model,@ModelAttribute("cfg")AppIpCfg cfg,
							Integer isValid,
							Integer isAudit,
							String ids,
							Integer functionId, 
							RedirectAttributes redirectAttributes,
							HttpServletResponse response,
							HttpServletRequest request) {
		if(!StringUtil.isEmpty(ids)) {
			AppIpCfg entity = new AppIpCfg();
			String[] idArray = ids.split(",");
			//同一编译IP的分组
			Map<Integer,List<AppIpCfg>> dataMap=new HashMap<>();
			for(String id :idArray){
				entity = appCfgService.getAppIpCfg(Long.parseLong(id));
				entity.setIsAudit(isAudit);
				entity.setIsValid(isValid);
				entity.setAuditorId(UserUtils.getUser().getId());
				entity.setAuditTime(new Date());
				entity.setFunctionId(functionId);
				entity.setTableName(AppIpCfg.getTablename());
				if(dataMap.containsKey(entity.getCompileId())) {
					dataMap.get(entity.getCompileId()).add(entity);
				}else {
					List<AppIpCfg> ipList=new ArrayList<>();
					ipList.add(entity);
					dataMap.put(entity.getCompileId(), ipList);
				}
			}
			for(List<AppIpCfg> entitys:dataMap.values()) {
				try {
					appCfgService.auditAppIpCfg(entitys,isAudit);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("app协议IP配置下发失败",e);
					if(e instanceof MaatConvertException) {
						addMessage(redirectAttributes,"error","request_service_failed");
					}else {
						addMessage(redirectAttributes,"error","audit_failed");
					}
				}
			}
		}else {
			Page<AppIpCfg> searchPage=new Page<AppIpCfg>(request,response,"r");
			Page<AppIpCfg> auditPage=new Page<AppIpCfg>(request,response,"r");
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
			
			return ipCfgList(model, cfg, request, response);
		}
		
		return "redirect:" + adminPath +"/app/ipCfgList?functionId="+functionId;
	}
	/**
	 * IP配置删除
	 * @param isValid
	 * @param ids
	 * @param functionId
	 * @return
	 */
	@RequestMapping(value = {"updateAppIpCfgValid"})
	@RequiresPermissions(value={"app:ip:config"})
	public String updateAppIpCfgValid(Integer isValid,String ids,Integer functionId) {
		appCfgService.updateAppIpCfgValid(isValid,ids,functionId);
		return "redirect:" + adminPath +"/app/ipCfgList?functionId="+functionId;
	}
	/**
	 * app协议http配置列表
	 * @param model
	 * @param cfg
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = {"httpCfgList"})
	public String httpCfgList(Model model,@ModelAttribute("cfg")AppHttpCfg cfg,HttpServletRequest request,HttpServletResponse response) {
		Page<AppHttpCfg> searchPage=new Page<AppHttpCfg>(request,response,"r");
		Page<AppHttpCfg> page = appCfgService.findAppHttpList(searchPage, cfg);
		for(AppHttpCfg entity:page.getList()){
			SpecificServiceCfg app = specificServiceCfgService.getBySpecServiceId(entity.getSpecServiceId());
			entity.setAppName(app.getSpecServiceName());
//			entity.setCfgKeywords(entity.getCfgKeywords().replace(Constants.KEYWORD_EXPR, "&"));
		}
		model.addAttribute("page", page);
		initPageCondition(model,cfg);
		return "/cfg/app/appHttpCfgList";
	}
	/**
	 * 协议http配置表单
	 * @param model
	 * @param ids
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = {"httpCfgForm"})
	@RequiresPermissions(value={"app:http:config"})
	public String httpCfgForm(Model model,String ids,AppHttpCfg entity) {
		if(StringUtils.isNotBlank(ids)){
			entity = appCfgService.getAppHttpCfg(Long.parseLong(ids));
			initUpdateFormCondition(model,entity);
		}else{
			initFormCondition(model,entity);
		}
		model.addAttribute("_cfg", entity);
		return "/cfg/app/appHttpCfgForm";
	}
	/**
	 * http配置新增修改
	 * @param model
	 * @param request
	 * @param response
	 * @param entity
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"saveAppHttpCfg"})
	@RequiresPermissions(value={"app:http:config"})
	public String saveAppHttpCfg(Model model,HttpServletRequest request,HttpServletResponse response,
			AppHttpCfg entity,RedirectAttributes redirectAttributes) {
		try {
			SpecificServiceCfg specificService = specificServiceCfgService.getBySpecServiceId(entity.getSpecServiceId());
			if(specificService!=null){
				entity.setAppCode(specificService.getSpecServiceCode());
			}
			appCfgService.saveOrUpdateAppHttpCfg(entity);
			addMessage(redirectAttributes,"success","save_success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("saveAppHttpCfg failed", e);
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,"error","request_service_failed");
			}else {
				addMessage(redirectAttributes,"error","save_failed");
			}
		}
		
		return "redirect:" + adminPath +"/app/httpCfgList?functionId="+entity.getFunctionId();
	}
	/**
	 * http配置审核
	 * @param isAudit
	 * @param isValid
	 * @param ids
	 * @param functionId
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"auditAppHttpCfg"})
//	@RequiresPermissions(value={"app:http:confirm"})
	public String auditAppHttpCfg(Model model,@ModelAttribute("cfg")AppHttpCfg cfg,
								Integer isValid,
								Integer isAudit,
								String ids,
								Integer functionId, 
								RedirectAttributes redirectAttributes,
								HttpServletResponse response,
								HttpServletRequest request) {
		if(!StringUtil.isEmpty(ids)){
			AppHttpCfg entity = new AppHttpCfg();
			String[] idArray = ids.split(",");
			for(String id :idArray){
				entity = appCfgService.getAppHttpCfg(Long.parseLong(id));
				entity.setIsAudit(isAudit);
				entity.setIsValid(isValid);
				entity.setAuditorId(UserUtils.getUser().getId());
				entity.setAuditTime(new Date());
				entity.setFunctionId(functionId);
				try {
					appCfgService.auditAppHttpCfg(entity,isAudit);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("app http配置下发失败",e);
					if(e instanceof MaatConvertException) {
						addMessage(redirectAttributes,"error","request_service_failed");
					}else {
						addMessage(redirectAttributes,"error","audit_failed");
					}
				}
			}
		}else {
			Page<AppHttpCfg> searchPage=new Page<AppHttpCfg>(request,response,"r");
			Page<AppHttpCfg> auditPage=new Page<AppHttpCfg>(request,response,"r");

			try {
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
			
			return httpCfgList(model, cfg, request, response);
		}
		return "redirect:" + adminPath +"/app/httpCfgList?functionId="+functionId;
	}
	/**
	 * http配置删除
	 * @param isValid
	 * @param ids
	 * @param functionId
	 * @return
	 */
	@RequestMapping(value = {"updateAppHttpCfgValid"})
	@RequiresPermissions(value={"app:http:config"})
	public String updateAppHttpCfgValid(Integer isValid,String ids,Integer functionId) {
		appCfgService.updateAppHttpCfgValid(isValid,ids,functionId);
		return "redirect:" + adminPath +"/app/httpCfgList?functionId="+functionId;
	}
	/**
	 * app协议domain配置列表
	 * @param model
	 * @param cfg
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = {"domainCfgList"})
	public String domainCfgList(Model model,@ModelAttribute("cfg")AppDomainCfg cfg,HttpServletRequest request,HttpServletResponse response) {
		Page<AppDomainCfg> searchPage=new Page<AppDomainCfg>(request,response,"r");
		Page<AppDomainCfg> page = appCfgService.findAppDomainList(searchPage, cfg);
		for(AppDomainCfg entity:page.getList()){
			SpecificServiceCfg app = specificServiceCfgService.getBySpecServiceId(entity.getSpecServiceId());
			entity.setAppName(app.getSpecServiceName());
		}
		model.addAttribute("page", page);
		initPageCondition(model,cfg);
		return "/cfg/app/appDomainCfgList";
	}
	/**
	 * 协议domain配置表单
	 * @param model
	 * @param ids
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = {"domainCfgForm"})
	@RequiresPermissions(value={"app:domain:config"})
	public String domainCfgForm(Model model,String ids,AppDomainCfg entity) {
		if(StringUtils.isNotBlank(ids)){
			entity = appCfgService.getAppDomainCfg(Long.parseLong(ids));
			initUpdateFormCondition(model,entity);
		}else{
			initFormCondition(model,entity);
		}
		model.addAttribute("_cfg", entity);
		return "/cfg/app/appDomainCfgForm";
	}
/**
	 * domain配置新增修改
	 * @param model
	 * @param request
	 * @param response
	 * @param entity
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"saveAppDomainCfg"})
	@RequiresPermissions(value={"app:domain:config"})
	public String saveAppDomainCfg(Model model,HttpServletRequest request,HttpServletResponse response,
			AppDomainCfg entity,RedirectAttributes redirectAttributes) {
		try {
			SpecificServiceCfg specificService = specificServiceCfgService.getBySpecServiceId(entity.getSpecServiceId());
			if(specificService!=null){
				entity.setAppCode(specificService.getSpecServiceCode());
			}
			appCfgService.saveOrUpdateAppDomainCfg(entity);
			addMessage(redirectAttributes,"success","save_success");
		} catch (Exception e) {
			e.printStackTrace();
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,"error","request_service_failed");
			}else {
				addMessage(redirectAttributes,"error","save_failed");
			}
		}
		
		return "redirect:" + adminPath +"/app/domainCfgList?functionId="+entity.getFunctionId();
	}
	/**
	 * domain配置审核
	 * @param isAudit
	 * @param isValid
	 * @param ids
	 * @param functionId
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"auditAppDomainCfg"})
//	@RequiresPermissions(value={"app:domain:confirm"})
	public String auditAppDomainCfg(Model model,@ModelAttribute("cfg")AppDomainCfg cfg,
									Integer isValid,
									Integer isAudit,
									String ids,
									Integer functionId, 
									RedirectAttributes redirectAttributes,
									HttpServletResponse response,
									HttpServletRequest request) {
		if(!StringUtil.isEmpty(ids)){
			AppDomainCfg entity = new AppDomainCfg();
			String[] idArray = ids.split(",");
			for(String id :idArray){
				entity = appCfgService.getAppDomainCfg(Long.parseLong(id));
				entity.setIsAudit(isAudit);
				entity.setIsValid(isValid);
				entity.setAuditorId(UserUtils.getUser().getId());
				entity.setAuditTime(new Date());
				entity.setFunctionId(functionId);
				try {
					appCfgService.auditAppDomainCfg(entity,isAudit);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("app协议domain配置下发失败",e);
					if(e instanceof MaatConvertException) {
						addMessage(redirectAttributes,"error","request_service_failed");
					}else {
						addMessage(redirectAttributes,"error","audit_failed");
					}
				}
			}
		}else {
			Page<AppDomainCfg> searchPage=new Page<AppDomainCfg>(request,response,"r");
			Page<AppDomainCfg> auditPage=new Page<AppDomainCfg>(request,response,"r");

			try {
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
			
			return domainCfgList(model, cfg, request, response);
		}
		return "redirect:" + adminPath +"/app/domainCfgList?functionId="+functionId;
	}
	/**
	 * domain配置删除
	 * @param isValid
	 * @param ids
	 * @param functionId
	 * @return
	 */
	@RequestMapping(value = {"updateAppDomainCfgValid"})
	@RequiresPermissions(value={"app:domain:config"})
	public String updateAppDomainCfgValid(Integer isValid,String ids,Integer functionId) {
		appCfgService.updateAppDomainCfgValid(isValid,ids,functionId);
		return "redirect:" + adminPath +"/app/domainCfgList?functionId="+functionId;
	}
	/**
	 * app协议byte配置列表
	 * @param model
	 * @param cfg
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = {"byteCfgList"})
	public String byteCfgList(Model model,@ModelAttribute("cfg")AppByteCfg cfg,HttpServletRequest request,HttpServletResponse response) {
		Page<AppByteCfg> searchPage=new Page<AppByteCfg>(request,response,"r");
		Page<AppByteCfg> page = appCfgService.findAppByteList(searchPage, cfg);
		for(AppByteCfg entity:page.getList()){
			SpecificServiceCfg app = specificServiceCfgService.getBySpecServiceId(entity.getSpecServiceId());
			entity.setAppName(app.getSpecServiceName());
		}
		model.addAttribute("page", page);
		initPageCondition(model,cfg);
		return "/cfg/app/appByteCfgList";
	}
	/**
	 * 协议byte配置表单
	 * @param model
	 * @param ids
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = {"byteCfgForm"})
	@RequiresPermissions(value={"app:byte:config"})
	public String byteCfgForm(Model model,String ids,AppByteCfg entity) {
		if(StringUtils.isNotBlank(ids)){
			entity = appCfgService.getAppByteCfg(Long.parseLong(ids));
			initUpdateFormCondition(model,entity);
		}else{
			initFormCondition(model,entity);
		}
		model.addAttribute("_cfg", entity);
		return "/cfg/app/appByteCfgForm";
	}
/**
	 * byte配置新增修改
	 * @param model
	 * @param request
	 * @param response
	 * @param entity
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"saveAppByteCfg"})
	@RequiresPermissions(value={"app:byte:config"})
	public String saveAppByteCfg(Model model,HttpServletRequest request,HttpServletResponse response,
			AppByteCfg entity,RedirectAttributes redirectAttributes) {
		try {
			SpecificServiceCfg specificService = specificServiceCfgService.getBySpecServiceId(entity.getSpecServiceId());
			if(specificService!=null){
				entity.setAppCode(specificService.getSpecServiceCode());
			}
			appCfgService.saveOrUpdateAppByteCfg(entity);
			addMessage(redirectAttributes,"success","save_success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("saveAppByteCfg failed",e);
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,"error","request_service_failed");
			}else {
				addMessage(redirectAttributes,"error","save_failed");
			}
		}
		
		return "redirect:" + adminPath +"/app/byteCfgList?functionId="+entity.getFunctionId();
	}
	/**
	 * byte配置审核
	 * @param isAudit
	 * @param isValid
	 * @param ids
	 * @param functionId
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"auditAppByteCfg"})
//	@RequiresPermissions(value={"app:byte:confirm"})
	public String auditAppByteCfg(Integer isAudit,Integer isValid,String ids,Integer functionId, RedirectAttributes redirectAttributes) {
		AppByteCfg entity = new AppByteCfg();
		String[] idArray = ids.split(",");
		for(String id :idArray){
			entity = appCfgService.getAppByteCfg(Long.parseLong(id));
			entity.setIsAudit(isAudit);
			entity.setIsValid(isValid);
			entity.setAuditorId(UserUtils.getUser().getId());
			entity.setAuditTime(new Date());
			entity.setFunctionId(functionId);
			try {
				appCfgService.auditAppByteCfg(entity,isAudit);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("app协议byte配置下发失败",e);
				if(e instanceof MaatConvertException) {
					addMessage(redirectAttributes,"error","request_service_failed");
				}else {
					addMessage(redirectAttributes,"error","audit_failed");
				}
			}
		}
		return "redirect:" + adminPath +"/app/byteCfgList?functionId="+functionId;
	}
	/**
	 * byte配置删除
	 * @param isValid
	 * @param ids
	 * @param functionId
	 * @return
	 */
	@RequestMapping(value = {"updateAppByteCfgValid"})
	@RequiresPermissions(value={"app:byte:config"})
	public String updateAppByteCfgValid(Integer isValid,String ids,Integer functionId) {
		appCfgService.updateAppByteCfgValid(isValid,ids,functionId);
		return "redirect:" + adminPath +"/app/byteCfgList?functionId="+functionId;
	}
	
	/**
	 * APP ssl证书特征配置 列表
	 * @param model
	 * @param cfg
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = {"sslCfgList"})
	public String sslCfgList(Model model,@ModelAttribute("cfg")AppSslCertCfg cfg,HttpServletRequest request,HttpServletResponse response) {
		Page<AppSslCertCfg> searchPage=new Page<AppSslCertCfg>(request,response,"r");
		Page<AppSslCertCfg> page = appCfgService.findAppSslList(searchPage, cfg);
		for(AppSslCertCfg entity:page.getList()){
			SpecificServiceCfg app = specificServiceCfgService.getBySpecServiceId(entity.getSpecServiceId());
			entity.setAppName(app.getSpecServiceName());
		}
		model.addAttribute("page", page);
		initPageCondition(model,cfg);
		return "/cfg/app/appSslCfgList";
	}
	/**
	 * APP ssl证书特征配置表单（新增/修改）
	 * @param model
	 * @param ids
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = {"sslCfgForm"})
	@RequiresPermissions(value={"app:ssl:config"})
	public String sslCfgForm(Model model,String ids,AppSslCertCfg entity) {
		if(StringUtils.isNotBlank(ids)){
			entity = appCfgService.getAppSslCfg(Long.parseLong(ids));
			initUpdateFormCondition(model,entity);
		}else{
			initFormCondition(model,entity);
		}
		model.addAttribute("_cfg", entity);
		return "/cfg/app/appSslCfgForm";
	}
	/**
	 * APP ssl证书特征配置（新增/修改）提交
	 * @param model
	 * @param request
	 * @param response
	 * @param entity
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"saveAppSslCfg"})
	@RequiresPermissions(value={"app:ssl:config"})
	public String saveAppSslCfg(Model model,HttpServletRequest request,HttpServletResponse response,
			AppSslCertCfg entity,RedirectAttributes redirectAttributes) {
		try {
			SpecificServiceCfg specificService = specificServiceCfgService.getBySpecServiceId(entity.getSpecServiceId());
			if(specificService!=null){
				entity.setAppCode(specificService.getSpecServiceCode());
			}
			appCfgService.saveOrUpdateAppSslCfg(entity);
			addMessage(redirectAttributes,"success","save_success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("saveAppSslCfg failed", e);
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,"error","request_service_failed");
			}else {
				addMessage(redirectAttributes,"error","save_failed");
			}
		}
		
		return "redirect:" + adminPath +"/app/sslCfgList?functionId="+entity.getFunctionId();
	}
	/**
	 * APP ssl证书特征配置 审核
	 * @param isAudit
	 * @param isValid
	 * @param ids
	 * @param functionId
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"auditAppSslCfg"})
	@RequiresPermissions(value={"app:ssl:confirm"})
	public String auditAppSslCfg(Model model,@ModelAttribute("cfg")AppSslCertCfg cfg,
								Integer isValid,
								Integer isAudit,
								String ids,
								Integer functionId, 
								RedirectAttributes redirectAttributes,
								HttpServletResponse response,
								HttpServletRequest request) {
		if(!StringUtil.isEmpty(ids)){
			AppSslCertCfg entity = new AppSslCertCfg();
			String[] idArray = ids.split(",");
			for(String id :idArray){
				entity = appCfgService.getAppSslCfg(Long.parseLong(id));
				entity.setIsAudit(isAudit);
				entity.setIsValid(isValid);
				entity.setAuditorId(UserUtils.getUser().getId());
				entity.setAuditTime(new Date());
				entity.setFunctionId(functionId);
				try {
					appCfgService.auditAppSslCfg(entity,isAudit);
				} catch (Exception e) {
					logger.error("app SSL配置下发失败",e);
					if(e instanceof MaatConvertException) {
						addMessage(redirectAttributes,"error","request_service_failed");
					}else {
						addMessage(redirectAttributes,"error","audit_failed");
					}
				}
			}
		}else {
			Page<AppPolicyCfg> searchPage=new Page<AppPolicyCfg>(request,response,"r");
			Page<AppPolicyCfg> auditPage=new Page<AppPolicyCfg>(request,response,"r");

			try {
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
			
			return sslCfgList(model, cfg, request, response);
		}
		
		return "redirect:" + adminPath +"/app/sslCfgList?functionId="+functionId;
	}
	/**
	 * APP ssl证书特征配置 删除
	 * @param isValid
	 * @param ids
	 * @param functionId
	 * @return
	 */
	@RequestMapping(value = {"updateAppSslCfgValid"})
	@RequiresPermissions(value={"app:ssl:config"})
	public String updateAppSslCfgValid(Integer isValid,String ids,Integer functionId) {
		appCfgService.updateAppSslCfgValid(isValid,ids,functionId);
		return "redirect:" + adminPath +"/app/sslCfgList?functionId="+functionId;
	}
	

	/**
	 * APP TCP会话字节数特征 列表
	 * @param model
	 * @param cfg
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = {"tcpCfgList"})
	public String tcpCfgList(Model model,@ModelAttribute("cfg")AppTcpCfg cfg,HttpServletRequest request,HttpServletResponse response) {
		Page<AppTcpCfg> searchPage=new Page<AppTcpCfg>(request,response,"r");
		Page<AppTcpCfg> page = appCfgService.findAppTcpList(searchPage, cfg);
		for(AppTcpCfg entity:page.getList()){
			SpecificServiceCfg app = specificServiceCfgService.getBySpecServiceId(entity.getSpecServiceId());
			entity.setAppName(app.getSpecServiceName());
		}
		model.addAttribute("page", page);
		initPageCondition(model,cfg);
		return "/cfg/app/appTcpCfgList";
	}
	
	/**
	 * APP TCP会话字节数特征表单（新增/修改）
	 * @param model
	 * @param ids
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = {"tcpCfgForm"})
	@RequiresPermissions(value={"app:tcp:config"})
	public String tcpCfgForm(Model model,String ids,AppTcpCfg entity) {
		if(StringUtils.isNotBlank(ids)){
			entity = appCfgService.getAppTcpCfg(Long.parseLong(ids));
			initUpdateFormCondition(model,entity);
		}else{
			initFormCondition(model,entity);
		}
		model.addAttribute("_cfg", entity);
		return "/cfg/app/appTcpCfgForm";
	}
	
	/**
	 * APP TCP会话字节数特征配置（新增/修改）提交
	 * @param model
	 * @param request
	 * @param response
	 * @param entity
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"saveAppTcpCfg"})
	@RequiresPermissions(value={"app:tcp:config"})
	public String saveAppTcpCfg(Model model,HttpServletRequest request,HttpServletResponse response,
			AppTcpCfg entity,RedirectAttributes redirectAttributes) {
		try {
			SpecificServiceCfg specificService = specificServiceCfgService.getBySpecServiceId(entity.getSpecServiceId());
			if(specificService!=null){
				entity.setAppCode(specificService.getSpecServiceCode());
			}
			appCfgService.saveOrUpdateAppTcpeCfg(entity);
			addMessage(redirectAttributes,"success","save_success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("saveAppTcpCfg failed",e);
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,"error","request_service_failed");
			}else {
				addMessage(redirectAttributes,"save_failed");
			}
		}
		
		return "redirect:" + adminPath +"/app/tcpCfgList?functionId="+entity.getFunctionId();
	}
	
	/**
	 * APP TCP会话字节数特征配置删除
	 * @param isValid
	 * @param ids
	 * @param functionId
	 * @return
	 */
	@RequestMapping(value = {"updateAppTcpCfgValid"})
	@RequiresPermissions(value={"app:tcp:config"})
	public String updateAppTcpCfgValid(Integer isValid,String ids,Integer functionId) {
		appCfgService.updateAppTcpCfgValid(isValid,ids,functionId);
		return "redirect:" + adminPath +"/app/tcpCfgList?functionId="+functionId;
	}
	
	
	/**
	 * APP TCP会话字节数特征配置 审核
	 * @param isAudit
	 * @param isValid
	 * @param ids
	 * @param functionId
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"auditAppTcpCfg"})
	@RequiresPermissions(value={"app:tcp:confirm"})
	public String auditAppTcpCfg(Integer isAudit,Integer isValid,String ids,Integer functionId, RedirectAttributes redirectAttributes) {
		AppTcpCfg entity = new AppTcpCfg();
		String[] idArray = ids.split(",");
		for(String id :idArray){
			entity = appCfgService.getAppTcpCfg(Long.parseLong(id));
			entity.setIsAudit(isAudit);
			entity.setIsValid(isValid);
			entity.setAuditorId(UserUtils.getUser().getId());
			entity.setAuditTime(new Date());
			entity.setFunctionId(functionId);
			try {
				appCfgService.auditAppTcpCfg(entity,isAudit);
			} catch (Exception e) {
				logger.error("app SSL配置下发失败",e);
				if(e instanceof MaatConvertException) {
					addMessage(redirectAttributes,"error","request_service_failed");
				}else {
					addMessage(redirectAttributes,"error","audit_failed");
				}
			}
		}
		return "redirect:" + adminPath +"/app/tcpCfgList?functionId="+functionId;
	}
	
	
	
	
	/**
	 * APP header特征配置 列表
	 * @param model
	 * @param cfg
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = {"headerCfgList"})
	public String headerCfgList(Model model,@ModelAttribute("cfg")AppHeaderCfg cfg,HttpServletRequest request,HttpServletResponse response) {
		Page<AppHeaderCfg> searchPage=new Page<AppHeaderCfg>(request,response,"r");
		Page<AppHeaderCfg> page = appCfgService.findAppHeaderList(searchPage, cfg);
		for(AppHeaderCfg entity:page.getList()){
			SpecificServiceCfg app = specificServiceCfgService.getBySpecServiceId(entity.getSpecServiceId());
			entity.setAppName(app.getSpecServiceName());
		}
		model.addAttribute("page", page);
		initPageCondition(model,cfg);
		return "/cfg/app/appHeaderCfgList";
	}
	/**
	 * APP header特征配置表单 （修改/新增）
	 * @param model
	 * @param ids
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = {"headerCfgForm"})
	@RequiresPermissions(value={"app:header:config"})
	public String headerCfgForm(Model model,String ids,AppHeaderCfg entity) {
		if(StringUtils.isNotBlank(ids)){
			entity = appCfgService.getAppHeaderCfg(Long.parseLong(ids));
			initUpdateFormCondition(model,entity);
		}else{
			initFormCondition(model,entity);
		}
		model.addAttribute("_cfg", entity);
		return "/cfg/app/appHeaderCfgForm";
	}
	/**
	 * APP header特征配置 （新增/修改）表单提交
	 * @param model
	 * @param request
	 * @param response
	 * @param entity
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"saveAppHeaderCfg"})
	@RequiresPermissions(value={"app:header:config"})
	public String saveAppHeaderCfg(Model model,HttpServletRequest request,HttpServletResponse response,
			AppHeaderCfg entity,RedirectAttributes redirectAttributes) {
		try {
			SpecificServiceCfg specificService = specificServiceCfgService.getBySpecServiceId(entity.getSpecServiceId());
			if(specificService!=null){
				entity.setAppCode(specificService.getSpecServiceCode());
			}
			appCfgService.saveOrUpdateAppHeaderCfg(entity);
			addMessage(redirectAttributes,"success","save_success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("saveAppHeaderCfg failed",e);
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,"error","request_service_failed");
			}else {
				addMessage(redirectAttributes,"save_failed");
			}
		}
		
		return "redirect:" + adminPath +"/app/headerCfgList?functionId="+entity.getFunctionId();
	}
	/**
	 * APP header特征配置 审核
	 * @param isAudit
	 * @param isValid
	 * @param ids
	 * @param functionId
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"auditAppHeaderCfg"})
	@RequiresPermissions(value={"app:header:confirm"})
	public String auditAppHeaderCfg(Integer isAudit,Integer isValid,String ids,Integer functionId, RedirectAttributes redirectAttributes) {
		AppHeaderCfg entity = new AppHeaderCfg();
		String[] idArray = ids.split(",");
		for(String id :idArray){
			entity = appCfgService.getAppHeaderCfg(Long.parseLong(id));
			entity.setIsAudit(isAudit);
			entity.setIsValid(isValid);
			entity.setAuditorId(UserUtils.getUser().getId());
			entity.setAuditTime(new Date());
			entity.setFunctionId(functionId);
			try {
				appCfgService.auditAppHeaderCfg(entity,isAudit);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("app Header配置下发失败",e);
				if(e instanceof MaatConvertException) {
					addMessage(redirectAttributes,"error","request_service_failed");
				}else {
					addMessage(redirectAttributes,"error","audit_failed");
				}
			}
		}
		return "redirect:" + adminPath +"/app/headerCfgList?functionId="+functionId;
	}
	/**
	 * APP header特征配置 删除
	 * @param isValid
	 * @param ids
	 * @param functionId
	 * @return
	 */
	@RequestMapping(value = {"updateAppHeaderCfgValid"})
	@RequiresPermissions(value={"app:header:config"})
	public String updateAppHeaderCfgValid(Integer isValid,String ids,Integer functionId) {
		appCfgService.updateAppHeaderCfgValid(isValid,ids,functionId);
		return "redirect:" + adminPath +"/app/headerCfgList?functionId="+functionId;
	}
	
	/**
	 * 主题网站配置列表
	 * @param model
	 * @param cfg
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = {"topicDomainCfgList"})
	public String TopicDomainCfgList(Model model,@ModelAttribute("cfg")AppTopicDomainCfg cfg,HttpServletRequest request,HttpServletResponse response) {
		Page<AppTopicDomainCfg> searchPage=new Page<AppTopicDomainCfg>(request,response,"r");
		Page<AppTopicDomainCfg> page = appCfgService.findAppTopicDomainList(searchPage, cfg);
		model.addAttribute("page", page);
		initPageCondition(model,cfg);
		return "/cfg/app/appTopicDomainCfgList";
	}
	/**
	 * 
	 * 获取服务与主题关系表中的域名
	 * @param websiteDomainTopic
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "WebsiteDomainTopicList",method=RequestMethod.GET)
	public List<Map<String, Object>> WebsiteDomainTopicList(@ModelAttribute("websiteDomainTopic")WebsiteDomainTopic websiteDomainTopic,@RequestParam("websiteServiceId") Long websiteServiceId,@RequestParam("topicId") Long topicId,HttpServletResponse response){
		List<Map<String, Object>> mapList = Lists.newArrayList();
		if(websiteServiceId!=null&&topicId!=null){
			List<WebsiteDomainTopic> list = appCfgService.getDomainDict(websiteDomainTopic);
			if(list!=null&&list.size()>0){
				for (WebsiteDomainTopic domain : list) {
					Map<String, Object> map = Maps.newHashMap();
					map.put("domain",domain.getDomain());
					map.put("domainId",domain.getId());
					
					mapList.add(map);
				}
			}
		}
		return mapList;
	}
	/**
	 * app主题网站配置表单
	 * @param model
	 * @param ids
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = {"topicDomainCfgForm"})
	@RequiresPermissions(value={"app:topic:config"})
	public String topicDomainCfgForm(Model model,String ids,@ModelAttribute("_cfg")AppTopicDomainCfg entity) {
		if(StringUtils.isNotBlank(ids)){
			entity = appCfgService.getAppTopicDomainCfg(Long.parseLong(ids));
			initUpdateFormCondition(model,entity);
		}else{
			initFormCondition(model,entity);
		}
		model.addAttribute("_cfg", entity);
		return "/cfg/app/appTopicDomainCfgForm";
	}
/**
	 * app主题网站配置新增修改
	 * @param model
	 * @param request
	 * @param response
	 * @param entity
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"saveAppTopicDomainCfg"})
	@RequiresPermissions(value={"app:topic:config"})
	public String saveAppTopicDomainCfg(Model model,HttpServletRequest request,HttpServletResponse response,
			AppTopicDomainCfg entity,RedirectAttributes redirectAttributes) {
		try {
			//验证域名的重复行
			if(!StringUtil.isBlank(entity.getCfgKeywords())){
				WebsiteDomainTopic websiteDomainTopic = new WebsiteDomainTopic();
				websiteDomainTopic.setDomain(entity.getCfgKeywords());
				List<WebsiteDomainTopic> domainDict = appCfgService.getDomainDict(websiteDomainTopic);
				if((domainDict==null || domainDict.size()==0)){
					//保存到域名关联表中
					if(entity!=null&&entity.getWebsiteServiceId()!=null&&entity.getTopicId()!=null){
						websiteDomainTopic.setWebsiteServiceId(entity.getWebsiteServiceId());
						websiteDomainTopic.setTopicId(entity.getTopicId());
						websiteDomainTopic.setCreateTime(new Date());
						websiteDomainTopic.setCreatorId(Integer.valueOf(UserUtils.getUser().getId().toString()));
						websiteDomainTopic.setIsValid(Constants.VALID_YES);
						appCfgService.saveDomainDict(websiteDomainTopic);
					}else{
						addMessage(redirectAttributes,"save_failed");
						return "redirect:" + adminPath +"/app/topicDomainCfgList?functionId="+entity.getFunctionId();
					}
				}
			}
			if(entity!=null&&StringUtil.isBlank(entity.getCfgKeywords())&&!StringUtil.isBlank(entity.getCfgKeywords())){
				entity.setCfgKeywords(entity.getCfgKeywords());
			}
			appCfgService.saveOrUpdateAppTopicDomainCfg(entity);
			addMessage(redirectAttributes,"success","save_success");
		} catch (Exception e) {
			if(e instanceof MaatConvertException) {
				e.printStackTrace();
				logger.info("app主题网站配置下发失败："+e.getMessage());;
				addMessage(redirectAttributes,"error","request_service_failed");
			}else {
				e.printStackTrace();
				logger.error("app主题网站配置下发失败",e);
				addMessage(redirectAttributes,"error","save_failed");
			}
		}
		
		return "redirect:" + adminPath +"/app/topicDomainCfgList?functionId="+entity.getFunctionId();
	}
	/**
	 * domain配置审核
	 * @param isAudit
	 * @param isValid
	 * @param ids
	 * @param functionId
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"auditAppTopicDomainCfg"})
//	@RequiresPermissions(value={"app:domain:confirm"})
	public String auditAppTopicDomainCfg(Model model,@ModelAttribute("cfg")AppTopicDomainCfg cfg,
										Integer isValid,
										Integer isAudit,
										String ids,
										Integer functionId, 
										RedirectAttributes redirectAttributes,
										HttpServletResponse response,
										HttpServletRequest request) {
		if(!StringUtil.isEmpty(ids)){
			AppTopicDomainCfg entity = new AppTopicDomainCfg();
			String[] idArray = ids.split(",");
			for(String id :idArray){
				entity = appCfgService.getAppTopicDomainCfg(Long.parseLong(id));
				entity.setIsAudit(isAudit);
				entity.setIsValid(isValid);
				entity.setAuditorId(UserUtils.getUser().getId());
				entity.setAuditTime(new Date());
				entity.setFunctionId(functionId);
				try {
					appCfgService.auditAppTopicDomainCfg(entity,isAudit);
					addMessage(redirectAttributes,"success","audit_success");
				} catch (Exception e) {
					logger.error("app主题网站配置下发失败",e);
					if(e instanceof MaatConvertException) {
						addMessage(redirectAttributes,"error","request_service_failed");
					}else {
						addMessage(redirectAttributes,"error","audit_failed");
					}
				}
			}
		}else {
			Page<AppTopicDomainCfg> searchPage=new Page<AppTopicDomainCfg>(request,response,"r");
			Page<AppTopicDomainCfg> auditPage=new Page<AppTopicDomainCfg>(request,response,"r");

			try {
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
			
			return TopicDomainCfgList(model, cfg, request, response);
		}
		return "redirect:" + adminPath +"/app/topicDomainCfgList?functionId="+functionId;
	}
	/**
	 * 网站主题配置删除
	 * @param isValid
	 * @param ids
	 * @param functionId
	 * @return
	 */
	@RequestMapping(value = {"updateAppTopicDomainCfgValid"})
	@RequiresPermissions(value={"app:topic:config"})
	public String updateAppTopicDomainCfgValid(Integer isValid,String ids,Integer functionId,RedirectAttributes redirectAttributes) {
		try {
			appCfgService.updateAppTopicDomainCfgValid(isValid,ids,functionId);
			addMessage(redirectAttributes,"success","delete_success");
		} catch (Exception e) {
			logger.error("app主题网站删除失败",e);
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
		return "redirect:" + adminPath +"/app/topicDomainCfgList?functionId="+functionId;
	}
	
	
	/**********************内置APP特征文件上传*******************************/
	@RequestMapping(value = {"/appBuiltinFeatureFileForm"})
	@RequiresPermissions(value={"app:built_in_file:config"})
	public String from(Model model,
							HttpServletRequest request,
							HttpServletResponse response,
							String ids,
							@ModelAttribute("cfg")AppBuiltInFeatureFile cfg
							,RedirectAttributes redirectAttributes){
		if(cfg == null){
			cfg=new AppBuiltInFeatureFile();
		}
		if(!StringUtil.isEmpty(ids)){
			cfg = appBuiltInFeatureService.getAppBuiltInFeatureFile(Long.valueOf(ids),-1);
			initFormCondition(model, cfg);
			model.addAttribute("isAdd", false);
		}else{
			initFormCondition(model, cfg);
			model.addAttribute("isAdd", true);
		}
		
		model.addAttribute("_cfg", cfg);
		return "/cfg/app/appBuiltinFeatureFileForm";
	}
	
	@RequestMapping(value = {"/appBuiltinFeatureFileSaveOrUpdate"})
	@RequiresPermissions(value={"app:built_in_file:config"})
	public String saveOrUpdate(Model model,HttpServletRequest request,HttpServletResponse response,
			@ModelAttribute("cfg")AppBuiltInFeatureFile cfg,
			MultipartFile file,
			RedirectAttributes redirectAttributes){
		File fileD=null;
		try{
			
				if(file != null) {
					String filename = file.getOriginalFilename();
					String prefix = FileUtils.getPrefix(filename, false);
					String suffix = FileUtils.getSuffix(filename, false);
					fileD = File.createTempFile("file_"+ prefix, suffix);
					file.transferTo(fileD);//复制文件
					String md5 = FileUtils.getFileMD5(fileD);
					Map<String,Object> srcMap = Maps.newHashMap();
					srcMap.put("filetype", suffix);
					srcMap.put("datatype", "dbSystem");//源文件存入数据中心
					srcMap.put("createTime",new Date());
					srcMap.put("key",prefix);
					srcMap.put("fileName", filename);
					srcMap.put("checksum", md5);
					ToMaatResult result = ConfigServiceUtil.postFileCfg(null, fileD, JsonMapper.toJsonString(srcMap));
					logger.info("APP 内置特征 文件上传响应信息："+JsonMapper.toJsonString(result));
					String accessUrl = null;
					if(!StringUtil.isEmpty(result)){
						ResponseData data = result.getData();
						accessUrl=data.getAccessUrl();
						cfg.setFilePath(accessUrl);;
					}
	 			}
				appBuiltInFeatureService.saveOrUpdate(cfg);
			
			addMessage(redirectAttributes,"success","save_success");
		}catch(Exception e){
			e.printStackTrace();
			logger.error("appBuiltinFeatureFileSaveOrUpdate failed",e);
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,"error","request_service_failed");
			}else {
				addMessage(redirectAttributes,"error","save_failed");
			}
		}
		
		return "redirect:" + adminPath +"/app/appBuiltinFeatureFileList?functionId="+cfg.getFunctionId();
	}
	
	@RequestMapping(value = {"/appBuiltinFeatureFileList"})
	public String list(Model model,HttpServletRequest  request,HttpServletResponse response
			,@ModelAttribute("cfg")AppBuiltInFeatureFile entity
			,RedirectAttributes redirectAttributes){
		Page<AppBuiltInFeatureFile> page = appBuiltInFeatureService.findPage(new Page<AppBuiltInFeatureFile>(request, response,"r"), entity);
		model.addAttribute("page", page);
		initPageCondition(model);
		return "/cfg/app/appBuiltinFeatureFileList";
	}
	
	@RequestMapping(value = {"/appBuiltinFeatureFileDelete"})
	@RequiresPermissions(value={"app:built_in_file:config"})
	public String delete(Integer isAudit,Integer isValid,String ids,Integer functionId
			,Model model,HttpServletRequest request
			,HttpServletResponse response
			,RedirectAttributes redirectAttributes){
		if(!StringUtil.isEmpty(ids)){
			appBuiltInFeatureService.delete(isAudit,isValid,ids,functionId);
		}
		
		return "redirect:" + adminPath +"/app/appBuiltinFeatureFileList?functionId="+functionId;
	}
	
	@RequestMapping(value = {"/audit"})
	@RequiresPermissions(value={"app:built_in_file:confirm"})
	public String audit(Integer isAudit,Integer isValid,String ids,Integer functionId,
			RedirectAttributes redirectAttributes) {
		if(!StringUtil.isEmpty(ids)){
			String[] idArray = ids.split(",");
			Date auditTime=new Date();
			for(String id :idArray){
				try {
					appBuiltInFeatureService.audit(isAudit,isValid,functionId,id,auditTime);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("appBuiltinFeature audit failed", e);
					if(e instanceof MaatConvertException) {
						addMessage(redirectAttributes,"error","request_service_failed");
					}else {
						addMessage(redirectAttributes,"error","audit_failed");
					}
				}
			}
			
		}
		return "redirect:" + adminPath +"/app/appBuiltinFeatureFileList?functionId="+functionId;
	}
	@ResponseBody
	@RequestMapping(value = "/validCfgId")
	public boolean validCfgId(Long cfgId) {
		AppBuiltInFeatureFile dns=appBuiltInFeatureService.getAppBuiltInFeatureFile(cfgId,null);
		if(dns == null ){
			return false;
		}else{
			return true;
		}
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
								policy.setSocialName(app.getSpecServiceName());
							}
						}
						//查找社交应用的所有有效二级特定服务
						SpecificServiceCfg second=new SpecificServiceCfg();
						for(SysDataDictionaryItem dict:DictUtils.getDictList("SPECIFIC_SERVICE_CFG_TYPE")) {
							if(Constants.SPECIFIC_SERVICE_CFG_TYPE_APP.equals(dict.getItemValue())) {
								second.setCfgType(Integer.parseInt(dict.getItemCode()));
								break;
							}
						}
						second.setIsValid(Constants.VALID_YES);
						second.setIsLeaf(1);
						List<SpecificServiceCfg> secondList=specificServiceCfgService.findAllSpecificServiceCfg(second, null);
						//遍历，找到匹配项后将行为设置进去
						for(AppPolicyCfg policy:page.getList()){
							if(policy.getBehavCode()==null) continue;
							for(SpecificServiceCfg secondCfg:secondList) {
								if(secondCfg.getSpecServiceCode()==null) continue;
								if(secondCfg.getSpecServiceCode().intValue()==policy.getBehavCode().intValue()) {
									policy.setBehavName(secondCfg.getSpecServiceName());
									break;
								}
							}
						}
						titleList.add(entity.getMenuNameCode());
						titleList.add("NTC_IP");
						titleList.add("NTC_SUBSCRIBE_ID");
						classMap.put(entity.getMenuNameCode(), AppPolicyCfg.class);
						classMap.put("NTC_IP", IpPortCfg.class);
						classMap.put("NTC_SUBSCRIBE_ID", NtcSubscribeIdCfg.class);
						String cfgIndexInfoNoExport=",encrypted_tunnel_behavior,basic_protocol,expression_type,match_method,whether_hexbinary,group_name,userregion1,userregion2,userregion3,userregion4,userregion5,";
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
		
			//top
			@RequestMapping(value = "exportTop")
			public void exportTop(Model model,HttpServletRequest  request,HttpServletResponse response,
					@ModelAttribute("cfg")AppTopicDomainCfg entity,String ids,RedirectAttributes redirectAttributes){
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
						Page<AppTopicDomainCfg> pageInfo=new Page<AppTopicDomainCfg>(request, response,"r");
						pageInfo.setPageNo(1);
						pageInfo.setPageSize(Constants.MAX_EXPORT_SIZE);
						Page<AppTopicDomainCfg> page = appCfgService.findAppTopicDomainList(pageInfo, entity);
						for (int i = 0; i < page.getList().size(); i++) {
							AppTopicDomainCfg appTop=page.getList().get(i);
							appTop.setIsHex(appTop.getIsHexbin());
							appTop.setIsCaseInsenstive(appTop.getIsHexbin());
						}
						titleList.add(entity.getMenuNameCode());
						classMap.put(entity.getMenuNameCode(), AppTopicDomainCfg.class);
						String cfgIndexInfoNoExport=",letter,whether_area_block,classification,attribute,label,do_log,block_type,group_name,userregion1,userregion2,userregion3,userregion4,userregion5,";
						noExportMap.put(entity.getMenuNameCode(),cfgIndexInfoNoExport);
						dataMap.put(entity.getMenuNameCode(), page.getList());
					/*}*/
					this._export(model, request, response, redirectAttributes,entity.getMenuNameCode(),titleList,classMap,dataMap,noExportMap);
				} catch (Exception e) {
					logger.error("top export failed",e);
					addMessage(redirectAttributes,"error","export_failed");
				}
				//return "redirect:" + adminPath +"/ntc/iplist/list?functionId="+entity.getFunctionId();
			}
			
			//appIp配置导出
			@RequestMapping(value = "exportAppIp")
			public void exportAppIp(Model model,HttpServletRequest  request,HttpServletResponse response,
					@ModelAttribute("cfg")AppIpCfg entity,String ids,RedirectAttributes redirectAttributes){
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
						Page<AppIpCfg> pageInfo=new Page<AppIpCfg>(request, response,"r");
						pageInfo.setPageNo(1);
						pageInfo.setPageSize(Constants.MAX_EXPORT_SIZE);
						Page<AppIpCfg> page = appCfgService.findAppIpList(pageInfo, entity);
						for(AppIpCfg appIp:page.getList()){
							SpecificServiceCfg app = specificServiceCfgService.getBySpecServiceId(appIp.getSpecServiceId());
							appIp.setAppName(app.getSpecServiceName());
						}
						titleList.add(entity.getMenuNameCode());
						classMap.put(entity.getMenuNameCode(), AppIpCfg.class);
						String cfgIndexInfoNoExport=",letter,whether_area_block,classification,attribute,label,do_log,block_type,client_port,ir_type,group_name,userregion1,userregion2,userregion3,userregion4,userregion5,";
						noExportMap.put(entity.getMenuNameCode(),cfgIndexInfoNoExport);
						dataMap.put(entity.getMenuNameCode(), page.getList());
					/*}*/
					this._export(model, request, response, redirectAttributes,entity.getMenuNameCode(),titleList,classMap,dataMap,noExportMap);
				} catch (Exception e) {
					logger.error("appIp export failed",e);
					addMessage(redirectAttributes,"error","export_failed");
				}
				//return "redirect:" + adminPath +"/ntc/iplist/list?functionId="+entity.getFunctionId();
			}
			
			//appHttp配置导出
			@RequestMapping(value = "exportAppHttp")
			public void exportAppHttp(Model model,HttpServletRequest  request,HttpServletResponse response,
					@ModelAttribute("cfg")AppHttpCfg entity,String ids,RedirectAttributes redirectAttributes){
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
						Page<AppHttpCfg> pageInfo=new Page<AppHttpCfg>(request, response,"r");
						pageInfo.setPageNo(1);
						pageInfo.setPageSize(Constants.MAX_EXPORT_SIZE);
						Page<AppHttpCfg> page = appCfgService.findAppHttpList(pageInfo, entity);
						for(AppHttpCfg http:page.getList()){
							http.setIsHex(http.getIsHexbin());
							http.setIsCaseInsenstive(http.getIsHexbin());
							http.setCfgKeywords(Functions.replace(http.getCfgKeywords(), "***and***"," "));
							SpecificServiceCfg app = specificServiceCfgService.getBySpecServiceId(http.getSpecServiceId());
							http.setAppName(app.getSpecServiceName());
						}
						
						titleList.add(entity.getMenuNameCode());
						classMap.put(entity.getMenuNameCode(), AppHttpCfg.class);
						String cfgIndexInfoNoExport=",letter,whether_area_block,classification,attribute,label,do_log,block_type,client_port,ir_type,group_name,userregion1,userregion2,userregion3,userregion4,userregion5,";
						noExportMap.put(entity.getMenuNameCode(),cfgIndexInfoNoExport);
						dataMap.put(entity.getMenuNameCode(), page.getList());
					/*}*/
					this._export(model, request, response, redirectAttributes,entity.getMenuNameCode(),titleList,classMap,dataMap,noExportMap);
				} catch (Exception e) {
					logger.error("http export failed",e);
					addMessage(redirectAttributes,"error","export_failed");
				}
				//return "redirect:" + adminPath +"/ntc/iplist/list?functionId="+entity.getFunctionId();
			}
			
			//appdomain配置导出
			@RequestMapping(value = "exportDomain")
			public void exportDomain(Model model,HttpServletRequest  request,HttpServletResponse response,
					@ModelAttribute("cfg")AppDomainCfg entity,String ids,RedirectAttributes redirectAttributes){
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
						Page<AppDomainCfg> pageInfo=new Page<AppDomainCfg>(request, response,"r");
						pageInfo.setPageNo(1);
						pageInfo.setPageSize(Constants.MAX_EXPORT_SIZE);
						Page<AppDomainCfg> page = appCfgService.findAppDomainList(pageInfo, entity);
						for(AppDomainCfg domain:page.getList()){
							domain.setIsHex(domain.getIsHexbin());
							domain.setIsCaseInsenstive(domain.getIsHexbin());
							SpecificServiceCfg app = specificServiceCfgService.getBySpecServiceId(domain.getSpecServiceId());
							domain.setAppName(app.getSpecServiceName());
						}
						
						titleList.add(entity.getMenuNameCode());
						classMap.put(entity.getMenuNameCode(), AppDomainCfg.class);
						String cfgIndexInfoNoExport=",letter,whether_area_block,classification,attribute,label,do_log,block_type,client_port,ir_type,group_name,userregion1,userregion2,userregion3,userregion4,userregion5,";
						noExportMap.put(entity.getMenuNameCode(),cfgIndexInfoNoExport);
						dataMap.put(entity.getMenuNameCode(), page.getList());
					/*}*/
					this._export(model, request, response, redirectAttributes,entity.getMenuNameCode(),titleList,classMap,dataMap,noExportMap);
				} catch (Exception e) {
					logger.error("Domain export failed",e);
					addMessage(redirectAttributes,"error","export_failed");
				}
				//return "redirect:" + adminPath +"/ntc/iplist/list?functionId="+entity.getFunctionId();
			}
			
			//appssl配置导出
			@RequestMapping(value = "exportAppSsl")
			public void exportAppSsl(Model model,HttpServletRequest  request,HttpServletResponse response,
					@ModelAttribute("cfg")AppSslCertCfg entity,String ids,RedirectAttributes redirectAttributes){
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
						Page<AppSslCertCfg> pageInfo=new Page<AppSslCertCfg>(request, response,"r");
						pageInfo.setPageNo(1);
						pageInfo.setPageSize(Constants.MAX_EXPORT_SIZE);
						Page<AppSslCertCfg> page = appCfgService.findAppSslList(pageInfo, entity);
						for(AppSslCertCfg ssl:page.getList()){
							ssl.setIsHex(ssl.getIsHexbin());
							ssl.setIsCaseInsenstive(ssl.getIsHexbin());
							ssl.setCfgKeywords(Functions.replace(ssl.getCfgKeywords(), "***and***"," "));
							SpecificServiceCfg app = specificServiceCfgService.getBySpecServiceId(ssl.getSpecServiceId());
							ssl.setAppName(app.getSpecServiceName());
						}
						titleList.add(entity.getMenuNameCode());
						classMap.put(entity.getMenuNameCode(), AppSslCertCfg.class);
						String cfgIndexInfoNoExport=",letter,whether_area_block,classification,attribute,label,do_log,block_type,client_port,ir_type,group_name,userregion1,userregion2,userregion3,userregion4,userregion5,";
						noExportMap.put(entity.getMenuNameCode(),cfgIndexInfoNoExport);
						dataMap.put(entity.getMenuNameCode(), page.getList());
					/*}*/
					this._export(model, request, response, redirectAttributes,entity.getMenuNameCode(),titleList,classMap,dataMap,noExportMap);
				} catch (Exception e) {
					logger.error("App ssl export failed",e);
					addMessage(redirectAttributes,"error","export_failed");
				}
				//return "redirect:" + adminPath +"/ntc/iplist/list?functionId="+entity.getFunctionId();
			}
}
