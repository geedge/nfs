package com.nis.web.controller.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.taglibs.standard.functions.Functions;
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
import com.nis.domain.FunctionRegionDict;
import com.nis.domain.Page;
import com.nis.domain.SysDataDictionaryItem;
import com.nis.domain.configuration.AppBuiltInFeatureFile;
import com.nis.domain.configuration.AppByteCfg;
import com.nis.domain.configuration.AppComplexFeatureCfg;
import com.nis.domain.configuration.AppDomainCfg;
import com.nis.domain.configuration.AppFeatureIndex;
import com.nis.domain.configuration.AppHeaderCfg;
import com.nis.domain.configuration.AppHttpCfg;
import com.nis.domain.configuration.AppIpCfg;
import com.nis.domain.configuration.AppPolicyCfg;
import com.nis.domain.configuration.AppSslCertCfg;
import com.nis.domain.configuration.AppStringFeatureCfg;
import com.nis.domain.configuration.AppTcpCfg;
import com.nis.domain.configuration.AppTopicDomainCfg;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.CfgIndexInfo;
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
@RequestMapping("${adminPath}/app/feature/")
public class AppFeatureCfgController extends BaseController {
	/**
	 * app协议复杂特征配置列表
	 * @param model
	 * @param cfg
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = {"multiFeatureCfgList"})
	public String multiFeatureCfgList(Model model,@ModelAttribute("cfg")AppFeatureIndex cfg,HttpServletRequest request,HttpServletResponse response) {
		Page<AppFeatureIndex> searchPage=new Page<AppFeatureIndex>(request,response,"r");
		Page<AppFeatureIndex> page = appMultiFeatureCfgService.findAppFeatureIndexList(searchPage, cfg);
		for(AppFeatureIndex entity:page.getList()){
			SpecificServiceCfg app = specificServiceCfgService.getBySpecServiceId(entity.getSpecServiceId());
			entity.setAppName(app.getSpecServiceName());
		}
		model.addAttribute("page", page);
		initPageCondition(model,cfg);
		return "/cfg/app/appMultiFeatureCfgList";
	}
	/**
	 * 特征配置表单
	 * @param model
	 * @param ids
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = {"multiFeatureCfgForm"})
	public String multiFeatureCfgForm(Model model,String ids,AppFeatureIndex entity) {
		if(StringUtils.isNotBlank(ids)){
			entity = appMultiFeatureCfgService.getAppFeatureIndex(Long.parseLong(ids));
			entity.setIpPortList(appMultiFeatureCfgService.getAppIpRangeCfg(entity.getCompileId(), entity.getFunctionId()));
			entity.setStrList(appMultiFeatureCfgService.getAppStringFeatureCfg(entity.getCompileId(), entity.getFunctionId(),null));
			entity.setComplexList(appMultiFeatureCfgService.getAppComplexFeatureCfg(entity.getCompileId(), entity.getFunctionId(),null));
			initUpdateFormCondition(model,entity);
		}else{
			initFormCondition(model,entity);
		}
		model.addAttribute("_cfg", entity);
		return "/cfg/app/appMultiFeatureCfgForm";
	}
	/**
	 * 特征配置新增修改
	 * @param model
	 * @param request
	 * @param response
	 * @param entity
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"saveAppMultiFeatureCfg"})
	public String saveAppMultiFeatureCfg(Model model,HttpServletRequest request,HttpServletResponse response,
			AppFeatureIndex entity,RedirectAttributes redirectAttributes) {
		try {
			SpecificServiceCfg specificService = specificServiceCfgService.getBySpecServiceId(entity.getSpecServiceId());
			if(specificService!=null){
				entity.setAppCode(specificService.getSpecServiceCode());
			}
			appMultiFeatureCfgService.saveOrUpdateAppFeatureCfg(entity);
		} catch (Exception e) {
			e.printStackTrace();
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,"error", "request_service_failed");
			}else {
				addMessage(redirectAttributes,"error", "save_failed");
			}
		}
		
		return "redirect:" + adminPath +"/app/feature/multiFeatureCfgList?functionId="+entity.getFunctionId();
	}
	/**
	 * 特征配置审核
	 * @param isAudit
	 * @param isValid
	 * @param ids
	 * @param functionId
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"auditAppFeatureCfg"})
	public String auditAppFeatureCfg(Model model,@ModelAttribute("cfg")AppFeatureIndex cfg,
										Integer isValid,
										Integer isAudit,
										String ids,
										Integer functionId, 
										RedirectAttributes redirectAttributes,
										HttpServletResponse response,
										HttpServletRequest request) {
		if(!StringUtil.isEmpty(ids)){
			AppFeatureIndex entity = new AppFeatureIndex();
			String[] idArray = ids.split(",");
			for(String id :idArray){
				entity = appMultiFeatureCfgService.getAppFeatureIndex(Long.parseLong(id));
				entity.setIsAudit(isAudit);
				entity.setIsValid(isValid);
				entity.setAuditorId(UserUtils.getUser().getId());
				entity.setAuditTime(new Date());
				entity.setFunctionId(functionId);
				try {
					appMultiFeatureCfgService.auditAppFeatureCfg(entity,isAudit);
				} catch (MaatConvertException e) {
					e.printStackTrace();
					logger.error("app 特征配置下发失败："+e.getMessage());
					addMessage(redirectAttributes,"error", "request_service_failed");
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("app 特征配置下发失败："+e.getMessage());
					addMessage(redirectAttributes,"error", "audit_failed");
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
			
			return multiFeatureCfgList(model, cfg, request, response);
		}
		
		return "redirect:" + adminPath +"/app/feature/multiFeatureCfgList?functionId="+functionId;
	}
	/**
	 * 特征配置删除
	 * @param isValid
	 * @param ids
	 * @param functionId
	 * @return
	 */
	@RequestMapping(value = {"updateAppFeatureCfgValid"})
	public String updateAppFeatureCfgValid(Integer isValid,String ids,Integer functionId) {
		appMultiFeatureCfgService.updateAppFeatureCfgValid(isValid,ids,functionId);
		return "redirect:" + adminPath +"/app/feature/multiFeatureCfgList?functionId="+functionId;
	}
	
	/**
	 * 查询特征子配置
	 * @param model
	 * @param cfgId
	 * @param index
	 * @return
	 */
	@RequestMapping(value = {"ajaxAppFeatureList"})
	public String ajaxAppFeatureList(Model model,Long cfgId,Integer compileId,Integer functionId,Integer index) {
		List<AppIpCfg> ipRangeList = appMultiFeatureCfgService.getAppIpRangeCfg(compileId,functionId);
		AppFeatureIndex cfg = appMultiFeatureCfgService.getAppFeatureIndex(cfgId);
		List<Map<String,Object>> tabList = new ArrayList();
		String[] cfgRegionValue = cfg.getCfgRegionValue().split(",");
		String[] cfgRegionType = cfg.getCfgRegionType().split(",");
		String[] cfgRegionCode = cfg.getCfgRegionCode1().split(",");
		if(cfgRegionValue.length == cfgRegionType.length && cfgRegionValue.length==cfgRegionCode.length){
			for(int i=0;i<cfgRegionValue.length;i++){
				Map<String,Object> map = new HashMap();
				map.put("regionType", cfgRegionType[i]);
				map.put("regionValue", cfgRegionValue[i]);
				if("1".equals(cfgRegionType[i])){
					map.put("ipRangeList", ipRangeList);
				}
				if("2".equals(cfgRegionType[i])){
					List<AppStringFeatureCfg> strList = appMultiFeatureCfgService.getAppStringFeatureCfg(compileId,functionId,Integer.valueOf(cfgRegionCode[i]));
					map.put("strList", strList);
				}
				if("3".equals(cfgRegionType[i])){
					List<AppComplexFeatureCfg> complexList = appMultiFeatureCfgService.getAppComplexFeatureCfg(compileId,functionId,Integer.valueOf(cfgRegionCode[i]));
					map.put("complexList", complexList);
				}
				tabList.add(map);
			}
		}else{
			Map<String,Object> map = new HashMap();
			map.put("regionType", "0");
			map.put("regionValue", "query config detail error");
			tabList.add(map);
		}
		
		/*if(ipRangeList!=null){
			String cfgType = null;
			for(AppIpCfg ip:ipRangeList){
				if(!ip.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"1",ip.getCfgType()});
					cfgType = ip.getCfgType();
				}
			}
		}
		if(strList!=null){
			String cfgType = null;
			for(AppStringFeatureCfg cfg:strList){
				if(!cfg.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"2",cfg.getCfgType()});
					cfgType = cfg.getCfgType();
				}
			}
		}
		if(complexList!=null){
			String cfgType = null;
			for(AppComplexFeatureCfg cfg:complexList){
				if(!cfg.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"3",cfg.getCfgType()});
					cfgType = cfg.getCfgType();0.
					
				}
			}
		}*/
		model.addAttribute("index", index);
		model.addAttribute("tabList", tabList);
		return "/cfg/app/appSubFeatureList";
	}
	
	//appfeature配置导出
	@RequestMapping(value = "exportFeature")
	public void exportFeature(Model model,HttpServletRequest  request,HttpServletResponse response,
			@ModelAttribute("cfg")AppFeatureIndex entity,String ids,RedirectAttributes redirectAttributes){
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
				Page<AppFeatureIndex> pageInfo=new Page<AppFeatureIndex>(request, response,"r");
				pageInfo.setPageNo(1);
				pageInfo.setPageSize(Constants.MAX_EXPORT_SIZE);
				Page<AppFeatureIndex> page = appMultiFeatureCfgService.findAppFeatureIndexList(pageInfo, entity);
				for(AppFeatureIndex feature:page.getList()){
					SpecificServiceCfg app = specificServiceCfgService.getBySpecServiceId(feature.getSpecServiceId());
					feature.setAppName(app.getSpecServiceName());
				}
				String cfgIndexInfoNoExport=",letter,whether_area_block,classification,attribute,label,do_log,block_type,client_port,ir_type,group_name,userregion1,userregion2,userregion3,userregion4,userregion5,";
				String appComplexNoExport=",do_log,block_type,config_describe,valid_identifier,is_audit,creator,creator"
						+ ",config_time,editor,edit_time,auditor,audit_time"
						+",letter,whether_area_block,classification,attribute,label"
						+",userregion1,userregion2,userregion3,userregion4,userregion5,";
				String ipPortInfoNoExport=",do_log,block_type,config_describe,valid_identifier,is_audit,creator,creator"
						+ ",config_time,editor,edit_time,auditor,audit_time"
						+",letter,whether_area_block,classification,attribute,label"
						+",userregion1,userregion2,userregion3,userregion4,userregion5,ir_type,group_name,";
				titleList.add(entity.getMenuNameCode());
				classMap.put(entity.getMenuNameCode(), AppFeatureIndex.class);
				noExportMap.put(entity.getMenuNameCode(),cfgIndexInfoNoExport);
				List<IpPortCfg> ipList=new ArrayList<IpPortCfg>();
				List<AppComplexFeatureCfg> keywordList=new ArrayList<AppComplexFeatureCfg>();
				List<AppStringFeatureCfg> strList=new ArrayList<AppStringFeatureCfg>();
				for (AppFeatureIndex cfg : page.getList()) {
					 Map<String, List> maps=appMultiFeatureCfgService.exportFeature(cfg);
					 if(entity.getFunctionId()!=567){
						 keywordList.addAll(maps.get("APP_KEYWORDS"));
					 }else{
						 strList.addAll(maps.get("APP_DK_GL"));
					 }
					 ipList.addAll(maps.get("APP_IP_RANGE"));
				}
				dataMap.put(entity.getMenuNameCode(), page.getList());
				if(entity.getFunctionId()!=564 && entity.getFunctionId()!=567 ){
					keywordList=replaceKeyList(keywordList);
					titleList.add("APP_IP_RANGE");
					classMap.put("APP_IP_RANGE", IpPortCfg.class);
					noExportMap.put("APP_IP_RANGE",ipPortInfoNoExport);
					dataMap.put("APP_IP_RANGE", ipList);
				}
				if(entity.getFunctionId()==563){
						titleList.add("APP_PAYLOAD");
						classMap.put("APP_PAYLOAD", AppComplexFeatureCfg.class);
						noExportMap.put("APP_PAYLOAD",appComplexNoExport);
						dataMap.put("APP_PAYLOAD", keywordList);
				}else if(entity.getFunctionId()==565){
						titleList.add("APP_HTTP");
						classMap.put("APP_HTTP", AppComplexFeatureCfg.class);
						noExportMap.put("APP_HTTP",appComplexNoExport);
						dataMap.put("APP_HTTP", keywordList);
				}else if(entity.getFunctionId()==566){
						titleList.add("APP_SSL");
						classMap.put("APP_SSL", AppComplexFeatureCfg.class);
						noExportMap.put("APP_SSL",appComplexNoExport);
						dataMap.put("APP_SSL", keywordList);
				}else if(entity.getFunctionId()==564){
						keywordList=replaceKeyList(keywordList);
						titleList.add("APP_DNS");
						classMap.put("APP_DNS", AppComplexFeatureCfg.class);
						noExportMap.put("APP_DNS",appComplexNoExport);
						dataMap.put("APP_DNS", keywordList);
				}else if(entity.getFunctionId()==567){
						strList=replaceFeatureList(strList);
						appComplexNoExport=appComplexNoExport+"district,";
						titleList.add("APP_DK");
						classMap.put("APP_DK", AppStringFeatureCfg.class);
						noExportMap.put("APP_DK",appComplexNoExport);
						dataMap.put("APP_DK", strList);
				}
				
			/*}*/
			this._export(model, request, response, redirectAttributes,entity.getMenuNameCode(),titleList,classMap,dataMap,noExportMap);
		} catch (Exception e) {
			logger.error("Appfeature export failed",e);
			addMessage(redirectAttributes,"error","export_failed");
		}
		//return "redirect:" + adminPath +"/ntc/iplist/list?functionId="+entity.getFunctionId();
	}
	
	public static List<AppComplexFeatureCfg> replaceKeyList(List<AppComplexFeatureCfg> list){
		for (int i = 0; i < list.size(); i++) {
			AppComplexFeatureCfg  base=(AppComplexFeatureCfg)list.get(i);
			base.setIsHex(base.getIsHexbin());
			base.setIsCaseInsenstive(base.getIsHexbin());
			base.setCfgKeywords(Functions.replace(base.getCfgKeywords(), "***and***"," "));
	 }
		return list;
	}
	
	public static List<AppStringFeatureCfg> replaceFeatureList(List<AppStringFeatureCfg> list){
		for (int i = 0; i < list.size(); i++) {
			AppStringFeatureCfg  base=(AppStringFeatureCfg)list.get(i);
			base.setIsHex(base.getIsHexbin());
			base.setIsCaseInsenstive(base.getIsHexbin());
			base.setCfgKeywords(Functions.replace(base.getCfgKeywords(), "***and***"," "));
	 }
		return list;
	}
	
	
}
