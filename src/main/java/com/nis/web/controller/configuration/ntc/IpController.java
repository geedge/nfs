package com.nis.web.controller.configuration.ntc;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nis.domain.FunctionRegionDict;
import com.nis.domain.FunctionServiceDict;
import com.nis.domain.Page;
import com.nis.domain.SysDataDictionaryItem;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.configuration.NtcSubscribeIdCfg;
import com.nis.domain.configuration.template.AppDomainTemplate;
import com.nis.domain.configuration.template.AsnIpTemplate;
import com.nis.domain.configuration.template.ComplexStringAllTemplate;
import com.nis.domain.configuration.template.DdosIpTemplate;
import com.nis.domain.configuration.template.DnsComplexStringTemplate;
import com.nis.domain.configuration.template.DnsIpTemplate;
import com.nis.domain.configuration.template.DnsResStrategyTemplate;
import com.nis.domain.configuration.template.IpAllTemplate;
import com.nis.domain.configuration.template.IpPayloadTemplate;
import com.nis.domain.configuration.template.IpRateLimitTemplate;
import com.nis.domain.configuration.template.IpWhitelistTemplate;
import com.nis.domain.configuration.template.P2pHashStringTemplate;
import com.nis.domain.configuration.template.P2pIpTemplate;
import com.nis.domain.configuration.template.SnatTemplate;
import com.nis.domain.configuration.template.StringAllTemplate;
import com.nis.domain.specific.ConfigGroupInfo;
import com.nis.exceptions.MaatConvertException;
import com.nis.index.Configurations;
import com.nis.util.Constants;
import com.nis.util.DictUtils;
import com.nis.util.StringUtil;
import com.nis.util.excel.ExportExcel;
import com.nis.web.controller.BaseController;
import com.nis.web.security.UserUtils;

/**
 * IP相关配置控制类
 * @author dell
 *
 */
@Controller
@RequestMapping("${adminPath}/ntc/iplist")
public class IpController extends BaseController{
	@RequestMapping(value = {"list"})
//	@RequiresPermissions(value={"iplist:config","iplist:confirm"},logical=Logical.OR)
	public String list(Model model,@ModelAttribute("cfg")CfgIndexInfo cfg,HttpServletRequest request,HttpServletResponse response) {
		Page<CfgIndexInfo> searchPage=new Page<CfgIndexInfo>(request,response,"a");
		Page<CfgIndexInfo> page = ipCfgService.getIpCfgList(searchPage, cfg);
		model.addAttribute("page", page);
		initPageCondition(model,cfg);
		return "/cfg/ipaddr/ipList";
	}
	@RequestMapping(value = {"form"})
	@RequiresPermissions(value={"iplist:config"})
	public String form(Model model,String ids,CfgIndexInfo entity) {
		if(StringUtils.isNotBlank(ids)){
			entity = ipCfgService.getIpPortCfg(Long.parseLong(ids),null);
			initUpdateFormCondition(model,entity);
		}else{
			initFormCondition(model,entity);
		}
		List<ConfigGroupInfo> policyGroups=asnPolicyCfgService.getConfigGroupInfoList(4);
		model.addAttribute("policyGroups", policyGroups);
		model.addAttribute("_cfg", entity);
		return "/cfg/ipaddr/ipForm";
	}
	@RequestMapping(value = {"saveOrUpdate"})
	public String saveOrUpdate(RedirectAttributes model,HttpServletRequest request,HttpServletResponse response,String ids,CfgIndexInfo entity) {
		ipCfgService.saveIpCfg(entity);
		return "redirect:" + adminPath +"/ntc/iplist/list?functionId="+entity.getFunctionId();
	}
	@RequestMapping(value = {"ajaxSubList"})
	public String ajaxIpPortSubList(Model model,Long cfgId,Integer index,Integer compileId) {
		CfgIndexInfo cfg = ipCfgService.getIpPortCfg(cfgId,compileId);
		/*List<FunctionRegionDict> regionList = DictUtils.getFunctionRegionDictList(cfg.getFunctionId());
		model.addAttribute("regionList", regionList);*/
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
			for(NtcSubscribeIdCfg keyword:cfg.getNtcSubscribeIdCfgList()){
				if(!keyword.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"2",keyword.getCfgType()});
					cfgType = keyword.getCfgType();
				}
			}
		}
		if(StringUtils.isNotBlank(cfg.getUserRegion4())) {
			tabList.add(new String[]{"3","ASN"});
		}
		model.addAttribute("_cfg", cfg);	
		model.addAttribute("index", index);
		model.addAttribute("tabList", tabList);
		return "/cfg/ipaddr/ipSubList";
	}
	@RequestMapping(value = {"updateValid"})
	@RequiresPermissions("iplist:config")
	public String updateIpPortCfgValid(Integer isValid,String ids,Integer functionId) {
		ipCfgService.updateIpCfgValid(isValid, ids, functionId);
		return "redirect:" + adminPath +"/ntc/iplist/list?functionId="+functionId;
	}
	/**
	 * 
	 * @param model
	 * @param cfg
	 * @param auditType 审核类型 ：批量审核时使用，1批量生效，0批量失效
	 * @param isAudit
	 * @param isValid
	 * @param ids
	 * @param functionId
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"audit"})
	@RequiresPermissions("iplist:confirm")
	public String audit(Model model,@ModelAttribute("cfg")CfgIndexInfo cfg
										,Integer isAudit
										,Integer isValid
										,String ids
										,Integer functionId
										, HttpServletRequest request
										,HttpServletResponse response
										,RedirectAttributes redirectAttributes) {
		//选中配置审核
		if(!StringUtil.isEmpty(ids)) {
			CfgIndexInfo entity = new CfgIndexInfo();
			String[] idArray = ids.split(",");
			for(String id :idArray){
				entity = ipCfgService.getIpPortCfg(Long.parseLong(id),null);
				entity.setIsAudit(isAudit);
				entity.setIsValid(isValid);
				entity.setAuditorId(UserUtils.getUser().getId());
				entity.setAuditTime(new Date());
				entity.setFunctionId(functionId);
				try {
					if(StringUtils.isNoneBlank(entity.getUserRegion4())) {
						ipCfgService.auditAsnCfg(entity, isAudit);
					}else {
						ipCfgService.auditIpCfg(entity,isAudit);
					}
					
				} catch (Exception e) {
					logger.info("ip配置下发失败："+e.getMessage());
					if(e instanceof MaatConvertException) {
						addMessage(redirectAttributes,"error", "request_service_failed");
					}else {
						addMessage(redirectAttributes,"error", "audit_failed");
					}
					
				}
			}
		}else{//条件下所有配置审核
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
			
			return list(model, cfg, request, response);
		}
		
		return "redirect:" + adminPath +"/ntc/iplist/list?functionId="+functionId;
	}
	/*//ip配置导入
	@RequestMapping(value = "import", method=RequestMethod.POST)
	public String importIp(String cfgName,RedirectAttributes redirectAttributes,
			@RequestParam("file") MultipartFile file,IpPortCfg cfg) {
		redirectAttributes.addAttribute("urlPrefix","/ntc/iplist");
		redirectAttributes.addAttribute("requiresPermissionPrefix","iplist");
		List<SysDataDictionaryItem>  specialFunctionIdList = DictUtils.getDictList("SPECIAL_FUNCTION_ID");
		String specialFunctionId=null;
		for(SysDataDictionaryItem item:specialFunctionIdList) {
			if(Integer.parseInt(item.getItemCode())==cfg.getFunctionId().intValue()) {
				specialFunctionId=	item.getItemValue();
				break;
			}
		}
		if(specialFunctionId==null) {
			this._importIp(cfgName,redirectAttributes, file,cfg,IpAddrTemplate.class);
		}else if("tunnel".equals(specialFunctionId)) {
				this._importIp(cfgName,redirectAttributes, file,cfg,TunnelIpTemplate.class);
		}else if("ipsec".equals(specialFunctionId)) {
			this._importIp(cfgName,redirectAttributes, file,cfg,IpsecTemplate.class);
		}else if("ipaddr".equals(specialFunctionId)) {
			this._importIp(cfgName,redirectAttributes, file,cfg,IpAddrTemplate.class);
		}else {
			this._importIp(cfgName,redirectAttributes, file,cfg);
		}
		
		
		return "redirect:" + adminPath +"/ntc/iplist/list?functionId="+cfg.getFunctionId();
	}
	//ip模板下载
	@RequestMapping(value = "import/template")
	public void importFileTemplate(HttpServletRequest request,HttpServletResponse response, 
    		RedirectAttributes redirectAttributes,Integer functionId,Integer cfgRegionCode) {
		List<SysDataDictionaryItem>  specialFunctionIdList = DictUtils.getDictList("SPECIAL_FUNCTION_ID");
		String specialFunctionId=null;
		for(SysDataDictionaryItem item:specialFunctionIdList) {
			if(Integer.parseInt(item.getItemCode())==functionId.intValue()) {
				specialFunctionId=	item.getItemValue();
				break;
			}
		}
		if(specialFunctionId==null) {
			this._importFileTemplate(request, response, redirectAttributes, functionId, cfgRegionCode,IpAddrTemplate.class);
		}else if("tunnel".equals(specialFunctionId)) {
			this._importFileTemplate(request, response, redirectAttributes, functionId, cfgRegionCode,TunnelIpTemplate.class);
		}else if("ipsec".equals(specialFunctionId)) {
			this._importFileTemplate(request, response, redirectAttributes, functionId, cfgRegionCode,IpsecTemplate.class);
		}else if("ipaddr".equals(specialFunctionId)) {
			this._importFileTemplate(request, response, redirectAttributes, functionId, cfgRegionCode,IpAddrTemplate.class);
		}else {
			this._importFileTemplate(request, response, redirectAttributes, functionId, cfgRegionCode);
		}
	}
	//ip配置导出
	@RequestMapping(value = "export")
	public void exportIp(String columns,Model model,HttpServletRequest  request,HttpServletResponse response,
			@ModelAttribute("cfg")IpPortCfg entity,String ids,RedirectAttributes redirectAttributes){
		this._exportIp(columns,model, request, response, entity, ids, redirectAttributes);
	}*/
	
	//模板下载
	@RequestMapping(value = "import/template")
	public void importFileTemplate(HttpServletRequest request,HttpServletResponse response, 
			RedirectAttributes redirectAttributes
			,Integer serviceDictId
			,Integer regionDictId
			,Integer requestId
			) {
		try {
			FunctionRegionDict regionDict=DictUtils.getFunctionRegionDict(regionDictId);
			FunctionServiceDict serviceDict=DictUtils.getFunctionServiceDict(serviceDictId);
			exportTemplate(request,response,redirectAttributes,regionDict,serviceDict,requestId);
		} catch (Exception e) {
			logger.error("export_failed", e);
			addMessage(redirectAttributes,"error", "export_failed");
		}
		
	}
	public void exportTemplate(HttpServletRequest request,HttpServletResponse response, 
			RedirectAttributes redirectAttributes
			,FunctionRegionDict regionDict
			,FunctionServiceDict serviceDict
			,Integer requestId) throws Exception{
		Properties pro=getMsgProp();
		//String serviceName=pro.getProperty(serviceDict.getServiceName());
		String regionName=pro.getProperty(regionDict.getConfigRegionValue());
		//serviceName=StringUtil.isEmpty(serviceName) ?serviceDict.getServiceName():serviceName;
		regionName=StringUtil.isEmpty(regionName) ?regionDict.getConfigRegionValue():regionName;
		String fileName = regionName+"_Template.xlsx";
		//ip类模板
		if(regionDict.getRegionType().equals(1)){
			if(regionDict.getFunctionId().equals(5)){// IP Address
				if(serviceDict.getAction().equals(64)){//ip限速
					List<IpRateLimitTemplate> classList=new ArrayList<IpRateLimitTemplate>();
					ExportExcel excel=new ExportExcel(serviceDict,regionDict,this.getMsgProp(),null, IpRateLimitTemplate.class, 2);
					excel.setDataList(this.getMsgProp(),classList,null).
			    	write(request,response, fileName).dispose();
				}else{
					List<IpAllTemplate> classList=new ArrayList<IpAllTemplate>();
					ExportExcel excel=new ExportExcel(serviceDict,regionDict,this.getMsgProp(),null, IpAllTemplate.class, 2);
					excel.setDataList(this.getMsgProp(),classList,null).
			    	write(request,response, fileName).dispose();
				}
			}else if(regionDict.getFunctionId().equals(212)){ //IP Payload
				List<IpPayloadTemplate> classList=new ArrayList<IpPayloadTemplate>();
				ExportExcel excel=new ExportExcel(serviceDict,regionDict,this.getMsgProp(),null, IpPayloadTemplate.class, 2);
				excel.setDataList(this.getMsgProp(),classList,null).
		    	write(request,response, fileName).dispose();
			}else if(regionDict.getFunctionId().equals(7)||regionDict.getFunctionId().equals(401)){ //Dns IP，DNS FAKE IP
				List<DnsIpTemplate> classList=new ArrayList<DnsIpTemplate>();
				ExportExcel excel=new ExportExcel(serviceDict,regionDict,this.getMsgProp(),null, DnsIpTemplate.class, 2);
				excel.setDataList(this.getMsgProp(),classList,null).
		    	write(request,response, fileName).dispose();
			}else if(regionDict.getFunctionId().equals(510) && "p2p_ip".equals(regionDict.getConfigServiceType())){ //P2p IP
				List<P2pIpTemplate> classList=new ArrayList<P2pIpTemplate>();
				ExportExcel excel=new ExportExcel(serviceDict,regionDict,this.getMsgProp(),null, P2pIpTemplate.class, 2);
				excel.setDataList(this.getMsgProp(),classList,null).
		    	write(request,response, fileName).dispose();
			}else if(regionDict.getFunctionId().equals(600)){ //asnip
				List<AsnIpTemplate> classList=new ArrayList<AsnIpTemplate>();
				ExportExcel excel=new ExportExcel(serviceDict,regionDict,this.getMsgProp(),null, AsnIpTemplate.class, 2);
				excel.setDataList(this.getMsgProp(),classList,null).
		    	write(request,response, fileName).dispose();
			}else if(regionDict.getFunctionId().equals(3)){ //ip whitelist
				List<IpWhitelistTemplate> classList=new ArrayList<IpWhitelistTemplate>();
				ExportExcel excel=new ExportExcel(serviceDict,regionDict,this.getMsgProp(),null, IpWhitelistTemplate.class, 2);
				excel.setDataList(this.getMsgProp(),classList,null).
		    	write(request,response, fileName).dispose();
			}else if(regionDict.getFunctionId().equals(301)){ //ddos ip
				List<DdosIpTemplate> classList=new ArrayList<DdosIpTemplate>();
				ExportExcel excel=new ExportExcel(serviceDict,regionDict,this.getMsgProp(),null, DdosIpTemplate.class, 2);
				excel.setDataList(this.getMsgProp(),classList,null).
		    	write(request,response, fileName).dispose();
			}else if(regionDict.getFunctionId().equals(633)){ //snat ip池
				List<SnatTemplate> classList=new ArrayList<SnatTemplate>();
				ExportExcel excel=new ExportExcel(serviceDict,regionDict,this.getMsgProp(),null, SnatTemplate.class, 2);
				excel.setDataList(this.getMsgProp(),classList,null).
		    	write(request,response, fileName).dispose();
			}else{
				List<IpAllTemplate> classList=new ArrayList<IpAllTemplate>();
				ExportExcel excel=new ExportExcel(serviceDict,regionDict,this.getMsgProp(),null, IpAllTemplate.class, 2);
				excel.setDataList(this.getMsgProp(),classList,null).
		    	write(request,response, fileName).dispose();
			}
		}
		//str类模板
		if(regionDict.getRegionType().equals(2)){
			if(regionDict.getFunctionId().equals(510) && "p2p_hash".equals(regionDict.getConfigServiceType())){ //P2p hash
				List<P2pHashStringTemplate> classList=new ArrayList<P2pHashStringTemplate>();
				ExportExcel excel=new ExportExcel(serviceDict,regionDict,this.getMsgProp(),null, P2pHashStringTemplate.class, 2);
				excel.setDataList(this.getMsgProp(),classList,null).
		    	write(request,response, fileName).dispose();
			}else if(regionDict.getFunctionId().equals(403)) {//app domain
				List<AppDomainTemplate> classList=new ArrayList<AppDomainTemplate>();
				ExportExcel excel=new ExportExcel(serviceDict,regionDict,this.getMsgProp(),null, AppDomainTemplate.class, 2);
				excel.setDataList(this.getMsgProp(),classList,null).
		    	write(request,response, fileName).dispose();
			}else{
				List<StringAllTemplate> classList=new ArrayList<StringAllTemplate>();
				ExportExcel excel=new ExportExcel(serviceDict,regionDict,this.getMsgProp(),null, StringAllTemplate.class, 2);
				excel.setDataList(this.getMsgProp(),classList,null).
		    	write(request,response, fileName).dispose();
			}
			
		}
		//district类模板
		if(regionDict.getRegionType().equals(3)){
			if(regionDict.getFunctionId().equals(7)){//DNS DISTRICT STRING
				List<DnsComplexStringTemplate> classList=new ArrayList<DnsComplexStringTemplate>();
				ExportExcel excel=new ExportExcel(serviceDict,regionDict,this.getMsgProp(),null, DnsComplexStringTemplate.class, 2);
				excel.setDataList(this.getMsgProp(),classList,null).
		    	write(request,response, fileName).dispose();
			}else{
				List<ComplexStringAllTemplate> classList=new ArrayList<ComplexStringAllTemplate>();
				ExportExcel excel=new ExportExcel(serviceDict,regionDict,this.getMsgProp(),null, ComplexStringAllTemplate.class, 2);
				excel.setDataList(this.getMsgProp(),classList,null).
		    	write(request,response, fileName).dispose();
			}
		}
		if(regionDict.getRegionType().equals(6)){
			if(regionDict.getFunctionId().equals(400)){
				List<DnsResStrategyTemplate> classList=new ArrayList<DnsResStrategyTemplate>();
				ExportExcel excel=new ExportExcel(serviceDict,regionDict,this.getMsgProp(),null, DnsResStrategyTemplate.class, 2);
				excel.setDataList(this.getMsgProp(),classList,null).
		    	write(request,response, fileName).dispose();
			}
		}
	}
	
	//ip配置导入
		@RequestMapping(value = "import", method=RequestMethod.POST)
		public String importIp(HttpServletRequest  request,HttpServletResponse response,RedirectAttributes redirectAttributes,
				@RequestParam("files") MultipartFile[] files
				,Integer serviceDictId
				,Integer requestId
				,String regionDictIds
				,String importPath) {
				this._import(request,response,redirectAttributes, files,serviceDictId,regionDictIds,requestId);
				return "redirect:" + adminPath+ importPath;
		}
		//ip配置导出
		@RequestMapping(value = "exportIpAddr")
		public void exportIpAddr(Model model,HttpServletRequest  request,HttpServletResponse response,
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
					Page<CfgIndexInfo> page = ipCfgService.getIpCfgList(pageInfo, entity);
					titleList.add(entity.getMenuNameCode());
					titleList.add("NTC_IP");
					titleList.add("asn_policy");
					classMap.put(entity.getMenuNameCode(), CfgIndexInfo.class);
					classMap.put("NTC_IP", IpPortCfg.class);
					classMap.put("asn_policy", ConfigGroupInfo.class);
					String cfgIndexInfoNoExport=",policy_name,group_name,userregion1,userregion3,userregion4,userregion5,&userregion2:ratelimit-";
					String ipPortInfoNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
											+ ",config_time,editor,edit_time,auditor,audit_time"
											+",letter,whether_area_block,classification,attribute,label"
											+",userregion1,userregion2,userregion3,userregion4,userregion5,ir_type,group_name,";
					String asnGroupInfoNoExport="";
					noExportMap.put(entity.getMenuNameCode(),cfgIndexInfoNoExport);
					noExportMap.put("NTC_IP", ipPortInfoNoExport);
					noExportMap.put("asn_policy", asnGroupInfoNoExport);
					List<IpPortCfg> ipList=new ArrayList<IpPortCfg>();
					List<ConfigGroupInfo> groupInfoList=new ArrayList<ConfigGroupInfo>();
					for (CfgIndexInfo cfg : page.getList()) {
						CfgIndexInfo cfgIndexInfo=ipCfgService.exportIpInfo(cfg);
						ipList.addAll(cfgIndexInfo.getIpPortList());
						if(!StringUtil.isEmpty(cfgIndexInfo.getAsnIpGroupName())){
							ConfigGroupInfo group=new ConfigGroupInfo();
							group.setCompileId(cfgIndexInfo.getCompileId());
							group.setGroupName(cfgIndexInfo.getAsnIpGroupName());
							groupInfoList.add(group);
						}
					}
					dataMap.put(entity.getMenuNameCode(), page.getList());
					dataMap.put("NTC_IP", ipList);
					dataMap.put("asn_policy", groupInfoList);
					
				/*}*/
				this._export(model, request, response, redirectAttributes,entity.getMenuNameCode(),titleList,classMap,dataMap,noExportMap);
			} catch (Exception e) {
				logger.error("ip addr export failed",e);
				addMessage(redirectAttributes,"error",  "export_failed");
			}
			//return "redirect:" + adminPath +"/ntc/iplist/list?functionId="+entity.getFunctionId();
		}
}
