package com.nis.web.controller.configuration.proxy;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nis.domain.FunctionRegionDict;
import com.nis.domain.FunctionServiceDict;
import com.nis.domain.Page;
import com.nis.domain.callback.ProxyFileStrategyCfg;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.ComplexkeywordCfg;
import com.nis.domain.configuration.FileDigestCfg;
import com.nis.domain.configuration.HttpBodyCfg;
import com.nis.domain.configuration.HttpReqHeadCfg;
import com.nis.domain.configuration.HttpResHeadCfg;
import com.nis.domain.configuration.HttpUrlCfg;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.configuration.NtcSubscribeIdCfg;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.Constants;
import com.nis.util.DictUtils;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseController;
import com.nis.web.security.UserUtils;

import jersey.repackaged.com.google.common.collect.Lists;

/**
 * http重定向策略
 * @author zhangwei
 *
 */
@Controller
@RequestMapping("${adminPath}/proxy/cache")
public class CachePolicyController extends BaseController{
	@RequestMapping(value = {"list"})
	public String list(Model model,HttpServletRequest  request,HttpServletResponse response,@ModelAttribute("cfg")CfgIndexInfo cfg){
		Page<CfgIndexInfo> searchPage=new Page<CfgIndexInfo>(request,response,"a");
		Page<CfgIndexInfo> page = cachePolicyService.getCachePolicyList(searchPage, cfg);
		model.addAttribute("page", page);
		initPageCondition(model,cfg);
		return "/cfg/proxy/cache/list";
	}
	@RequestMapping(value = {"form"})
	@RequiresPermissions(value={"cache:policy:config"})
	public String form(Model model,HttpServletRequest request,HttpServletResponse response,String ids,CfgIndexInfo entity){
		if(StringUtils.isNotBlank(ids)){
			entity = cachePolicyService.getCachePolicy(Long.parseLong(ids));
			initUpdateFormCondition(model,entity);
		}else{
			initFormCondition(model,entity);
		}
		model.addAttribute("_cfg", entity);
		return "/cfg/proxy/cache/form";
	}
	
	@RequestMapping(value = {"saveOrUpdate"})
	@RequiresPermissions(value={"cache:policy:config"})
	public String saveOrUpdate(Model model,HttpServletRequest request,HttpServletResponse response,
			@ModelAttribute("cfg")CfgIndexInfo cfg,RedirectAttributes redirectAttributes){
		try{
			if(cfg.getAction().equals(128)){//不为白名单时保存自定义域
				cfg.setUserRegion(null);
			}else{
				String ignore_qs = request.getParameter("ignore_qs");
				String hdrs = request.getParameter("hdrs");
				String cookie = request.getParameter("cookie");
				Map<String,Object> map = new HashMap();
				if(!StringUtil.isBlank(ignore_qs)){
					map.put("ignore_qs", ignore_qs.split(","));
				}
				if(!StringUtil.isBlank(hdrs)){
					map.put("hdrs",hdrs.trim());
				}
				if(!StringUtil.isBlank(cookie)){
					map.put("cookie", cookie.trim());
				}
				cfg.getUserRegion().put("cache_key", map);
				if(!StringUtil.isEmpty(cfg.getUserRegion().get("no_revalidate"))){
					cfg.getUserRegion().put("no_revalidate",Integer.parseInt(cfg.getUserRegion().get("no_revalidate").toString()));
				}
				if(!StringUtil.isEmpty(cfg.getUserRegion().get("cache_dyn_url"))){
					cfg.getUserRegion().put("cache_dyn_url",Integer.parseInt(cfg.getUserRegion().get("cache_dyn_url").toString()));
				}
				if(!StringUtil.isEmpty(cfg.getUserRegion().get("cache_cookied_cont"))){
					cfg.getUserRegion().put("cache_cookied_cont",Integer.parseInt(cfg.getUserRegion().get("cache_cookied_cont").toString()));
				}
				if(!StringUtil.isEmpty(cfg.getUserRegion().get("ignore_req_nocache"))){
					cfg.getUserRegion().put("ignore_req_nocache",Integer.parseInt(cfg.getUserRegion().get("ignore_req_nocache").toString()));
				}
				if(!StringUtil.isEmpty(cfg.getUserRegion().get("ignore_res_nocache"))){
					cfg.getUserRegion().put("ignore_res_nocache",Integer.parseInt(cfg.getUserRegion().get("ignore_res_nocache").toString()));
				}
				if(!StringUtil.isEmpty(cfg.getUserRegion().get("force_caching"))){
					cfg.getUserRegion().put("force_caching",Integer.parseInt(cfg.getUserRegion().get("force_caching").toString()));
				}
				if(!StringUtil.isEmpty(cfg.getUserRegion().get("min_use"))){
					cfg.getUserRegion().put("min_use",Integer.parseInt(cfg.getUserRegion().get("min_use").toString()));
				}
			}
			cachePolicyService.saveCachePolicy(cfg);
			addMessage(redirectAttributes,"success","save_success");
		}catch(Exception e){
			logger.error("信息保存失败",e);
			e.printStackTrace();
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,"error","request_service_failed");
			}else {
				addMessage(redirectAttributes,"error","save_failed");
			}
		}
		
		return "redirect:" + adminPath +"/proxy/cache/list?functionId="+cfg.getFunctionId();
	}
	@RequestMapping(value = {"ajaxCacheSubList"})
	public String ajaxCacheSubList(Model model,Long cfgId,Integer index) {
		CfgIndexInfo cfg = cachePolicyService.getCachePolicy(cfgId);
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
		if(cfg.getComplexList()!=null){
			String cfgType = null;
			for(ComplexkeywordCfg keyword:cfg.getComplexList()){
				if(!keyword.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"3",keyword.getCfgType()});
					cfgType = keyword.getCfgType();
				}
			}
		}
		if(cfg.getStringList()!=null){
			String cfgType = null;
			for(BaseStringCfg strCfg:cfg.getStringList()){
				if(!strCfg.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"2",strCfg.getCfgType()});
					cfgType = strCfg.getCfgType();
				}
			}
		}
		model.addAttribute("_cfg", cfg);	
		model.addAttribute("index", index);
		model.addAttribute("tabList", tabList);
		return "/cfg/proxy/cache/cachePolicySubList";
	}
	@RequestMapping(value = {"delete"})
	@RequiresPermissions(value={"cache:policy:config"})
	public String delete(Integer isAudit,Integer isValid,String ids,Integer functionId){
		cachePolicyService.updateCachePolicyValid(isValid,ids,functionId);
		return "redirect:" + adminPath +"/proxy/cache/list?functionId="+functionId;
	}
	@RequestMapping(value = {"audit"})
	@RequiresPermissions(value={"cache:policy:confirm"})
	public String audit(Model model,@ModelAttribute("cfg") CfgIndexInfo cfg,
							Integer isValid,
							Integer isAudit,
							String ids,
							Integer functionId, 
							RedirectAttributes redirectAttributes,
							HttpServletResponse response,
							HttpServletRequest request) {
		
		if(!StringUtil.isEmpty(ids)){
			CfgIndexInfo entity = new CfgIndexInfo();
			String[] idArray = ids.split(",");
			for(String id :idArray){
				entity = cachePolicyService.getCachePolicy(Long.parseLong(id));
				entity.setIsAudit(isAudit);
				entity.setIsValid(isValid);
				entity.setAuditorId(UserUtils.getUser().getId());
				entity.setAuditTime(new Date());
				entity.setFunctionId(functionId);
				try {
					cachePolicyService.auditCachePolicy(entity,isAudit);
				} catch ( Exception e) {
					e.printStackTrace();
					logger.info("代理缓存策略下发失败："+e.getMessage());
					if(e instanceof MaatConvertException) {
						addMessage(redirectAttributes,"error","request_service_failed");
					}else {
						addMessage(redirectAttributes,"error","audit_failed");
					}
				}
			}
		}else {
			Page<CfgIndexInfo> searchPage=new Page<CfgIndexInfo>(request,response,"r");
			Page<CfgIndexInfo> auditPage=new Page<CfgIndexInfo>(request,response,"r");

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
			
			return list(model, request, response, cfg);
		}
		return "redirect:" + adminPath +"/proxy/cache/list?functionId="+functionId;
	}
	
	
	//http配置导出
		/*@RequestMapping(value = "exportHttp")
		public String exportHttp(Model model,HttpServletRequest  request,HttpServletResponse response,
				@ModelAttribute("cfg")CfgIndexInfo entity,String ids,RedirectAttributes redirectAttributes){
			try {
				//export data info
				List<String> titleList=new ArrayList<String>();
				Map<String, Class<?>> classMap=new HashMap<String, Class<?>>();
				Map<String, List> dataMap=new HashMap<String, List>();
				Map<String, String> noExportMap=new HashMap<String, String>();
				
				List<FunctionRegionDict> regionList = DictUtils.getFunctionRegionDictList(entity.getFunctionId());
				
				//导出选中记录
				if(!StringUtil.isEmpty(ids)){
					for(String id:ids.split(",")){
						Long.parseLong(id);
					}
					//List<CfgIndexInfo> list=ipCfgService.getListByCfgIdWithName(IpPortCfg.getTablename(), entity.getFunctionId(), ids);
				}else{
					//条件导出数据大于最大导出数，只导出最大导出条数
					Page<CfgIndexInfo> pageInfo=new Page<CfgIndexInfo>(request, response,"a");
					if(pageInfo.getCount()>Constants.MAX_EXPORT_SIZE){
						pageInfo.setPageNo(1);
						pageInfo.setPageSize(Constants.MAX_EXPORT_SIZE);
					}else{
						pageInfo.setPageNo(1);
						pageInfo.setPageSize(-1);
					}
					
					Page<CfgIndexInfo> page = cachePolicyService.getCachePolicyList(pageInfo, entity);
					Properties prop = getMsgProp();
					for (CfgIndexInfo str : page.getList()) {
						if(entity.getFunctionId()!=210 && entity.getFunctionId()!=211){
							String type="RESPONSE_CODE";
							if(entity.getFunctionId()==207){
								type="RESPONSE_CODE";
							}else  if(entity.getFunctionId()==208){
								type="REDIRECT_RESPONSE_CODE";
							}else if(entity.getFunctionId()==209){
								type="CONTROL_REPLACE_ZONE";
							}
							String dictlabel= DictUtils.getDictLabel(type, str.getUserRegion1());
							str.setUserRegion1(prop.getProperty(dictlabel));
							if(entity.getFunctionId()==207 || entity.getFunctionId()==208){
							if(StringUtil.isEmpty(str.getUserRegion2())){
								if(!StringUtil.isEmpty(str.getUserRegion3())){
									ProxyFileStrategyCfg  file= new ProxyFileStrategyCfg();
									file.setCompileId(Integer.valueOf(str.getUserRegion3()));
									List<ProxyFileStrategyCfg> allProxyFileStrategys = proxyFileStrategyService.getProxyFileStrategyCfgList(file);
									if(allProxyFileStrategys!=null && allProxyFileStrategys.size()>0){
										str.setUserRegion2(allProxyFileStrategys.get(0).getCfgDesc());	
									}
								}
							}
						  }
						}
					}
					String cfgIndexInfoNoExport=",policy_name,group_name,userregion1,userregion2,userregion3,userregion4,userregion5,";
					if(entity.getFunctionId()==207){
						cfgIndexInfoNoExport=",policy_name,group_name,userregion3,userregion4,userregion5,&userregion1:response_code-userregion2:response_content-";
					}else if(entity.getFunctionId()==208){
						cfgIndexInfoNoExport=",policy_name,group_name,userregion3,userregion4,userregion5,&userregion1:redirect_response_code-userregion2:redirect_url-";
					}else if(entity.getFunctionId()==209){
						cfgIndexInfoNoExport=",policy_name,group_name,userregion3,userregion4,userregion5,&userregion1:replace_zone-userregion2:replaced_content-userregion3:replace_content-";
					}else if(entity.getFunctionId()==211){
						cfgIndexInfoNoExport=",do_log,log_total,policy_name,group_name,userregion1,userregion2,userregion3,userregion4,userregion5,";
					 }
					String httpUrlCfgNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
												+ ",config_time,editor,edit_time,auditor,audit_time"
												+",letter,whether_area_block,classification,attribute,label"
												+",userregion1,userregion2,userregion3,userregion4,userregion5,";
					String httpReqHeadCfgNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
												+ ",config_time,editor,edit_time,auditor,audit_time"
												+",letter,whether_area_block,classification,attribute,label"
												+",userregion1,userregion2,userregion3,userregion4,userregion5,";
					String httpResHeadCfgNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
												+ ",config_time,editor,edit_time,auditor,audit_time"
												+",letter,whether_area_block,classification,attribute,label"
												+",userregion1,userregion2,userregion3,userregion4,userregion5,";
					String httpReqBodyCfgNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
												+ ",config_time,editor,edit_time,auditor,audit_time"
												+",letter,whether_area_block,classification,attribute,label"
												+",userregion1,userregion2,userregion3,userregion4,userregion5,";
					String httpResBodyCfgNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
												+ ",config_time,editor,edit_time,auditor,audit_time"
												+",letter,whether_area_block,classification,attribute,label"
												+",userregion1,userregion2,userregion3,userregion4,userregion5,";
					String ipPortCfgNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
												+ ",config_time,editor,edit_time,auditor,audit_time"
												+",letter,whether_area_block,classification,attribute,label"
												+",userregion1,userregion2,userregion3,userregion4,userregion5,ir_type,group_name,";
					String ntcSubscribeIdCfgNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
												+ ",config_time,editor,edit_time,auditor,audit_time"
												+",letter,whether_area_block,classification,attribute,label"
												+",userregion1,userregion2,userregion3,userregion4,userregion5,";
					List<BaseStringCfg> httpUrlList = new ArrayList<>();
					List<ComplexkeywordCfg> httpReqHdrList = new ArrayList<>();
					List<ComplexkeywordCfg> httpResHdrList = new ArrayList<>();
					List<BaseStringCfg> httpReqBodyList = new ArrayList<>();
					List<BaseStringCfg> httpResBodyList = new ArrayList<>();
					List<IpPortCfg> ipPortList = new ArrayList<>();
					List<BaseStringCfg> subscribeIdList = new ArrayList<>();
					for (CfgIndexInfo cfg : page.getList()) {
						 Map<String, List> maps=cachePolicyService.exportCachePolicy(cfg);
						 httpUrlList.addAll(maps.get("NTC_HTTP_URL"));
						 httpReqHdrList.addAll(maps.get("NTC_HTTP_REQ_HDR"));
						 httpResHdrList.addAll(maps.get("NTC_HTTP_RES_HDR"));
						 httpReqBodyList.addAll(maps.get("NTC_HTTP_REQ_BODY"));
						 httpResBodyList.addAll(maps.get("NTC_HTTP_RES_BODY"));
						 ipPortList.addAll(maps.get("NTC_UNIVERSAL_IP"));
						 subscribeIdList.addAll(maps.get("NTC_SUBSCRIBE_ID"));
					}
					httpUrlList=BaseStringCfg.replaceBaseKeyList(httpUrlList);
					httpReqHdrList=ComplexkeywordCfg.replaceComplexKeyList(httpReqHdrList);
					httpResHdrList=ComplexkeywordCfg.replaceComplexKeyList(httpResHdrList);
					httpReqBodyList=BaseStringCfg.replaceBaseKeyList(httpReqBodyList);
					httpResBodyList=BaseStringCfg.replaceBaseKeyList(httpResBodyList);
					subscribeIdList=BaseStringCfg.baseHexList(subscribeIdList);
					titleList.add(entity.getMenuNameCode());
					titleList.add("NTC_HTTP_URL");
					titleList.add("NTC_HTTP_REQ_HDR");
					classMap.put(entity.getMenuNameCode(), CfgIndexInfo.class);
					classMap.put("NTC_HTTP_URL", HttpUrlCfg.class);
					classMap.put("NTC_HTTP_REQ_HDR", HttpReqHeadCfg.class);
					noExportMap.put(entity.getMenuNameCode(),cfgIndexInfoNoExport);
					noExportMap.put("NTC_HTTP_URL", httpUrlCfgNoExport);
					noExportMap.put("NTC_HTTP_REQ_HDR", httpReqHeadCfgNoExport);
					dataMap.put(entity.getMenuNameCode(), page.getList());
					dataMap.put("NTC_HTTP_URL", httpUrlList);
					dataMap.put("NTC_HTTP_REQ_HDR", httpReqHdrList);
					if(entity.getFunctionId()!=208 && entity.getFunctionId()!=211){
						titleList.add("NTC_HTTP_RES_BODY");
						classMap.put("NTC_HTTP_RES_BODY", HttpBodyCfg.class);
						noExportMap.put("NTC_HTTP_RES_BODY", httpResBodyCfgNoExport);
						dataMap.put("NTC_HTTP_RES_BODY", httpResBodyList);
					}else if(entity.getFunctionId()!=211){
						titleList.add("NTC_HTTP_RES_HDR");
						titleList.add("NTC_HTTP_REQ_BODY");
						classMap.put("NTC_HTTP_RES_HDR", HttpResHeadCfg.class);
						classMap.put("NTC_HTTP_REQ_BODY", HttpBodyCfg.class);
						noExportMap.put("NTC_HTTP_RES_HDR", httpResHeadCfgNoExport);
						noExportMap.put("NTC_HTTP_REQ_BODY", httpReqBodyCfgNoExport);
						dataMap.put("NTC_HTTP_RES_HDR", httpResHdrList);
						dataMap.put("NTC_HTTP_REQ_BODY", httpReqBodyList);
					}
					titleList.add("NTC_UNIVERSAL_IP");
					titleList.add("NTC_SUBSCRIBE_ID");
					classMap.put("NTC_UNIVERSAL_IP", IpPortCfg.class);
					classMap.put("NTC_SUBSCRIBE_ID", NtcSubscribeIdCfg.class);
					noExportMap.put("NTC_UNIVERSAL_IP", ipPortCfgNoExport);
					noExportMap.put("NTC_SUBSCRIBE_ID", ntcSubscribeIdCfgNoExport);
					dataMap.put("NTC_UNIVERSAL_IP", ipPortList);
					dataMap.put("NTC_SUBSCRIBE_ID",subscribeIdList);
					
				}
				this._export(model, request, response, redirectAttributes,entity.getMenuNameCode(),titleList,classMap,dataMap,noExportMap);
			} catch (Exception e) {
				logger.error("http export failed",e);
				addMessage(redirectAttributes,  "export_failed");
			}
			return "redirect:" + adminPath +"/ntc/website/httpList?functionId="+entity.getFunctionId();
		}*/
}
