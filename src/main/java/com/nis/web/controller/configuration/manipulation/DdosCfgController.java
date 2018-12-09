package com.nis.web.controller.configuration.manipulation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.cxf.common.util.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nis.domain.Page;
import com.nis.domain.basics.ServiceDictInfo;
import com.nis.domain.configuration.AvFileSampleCfg;
import com.nis.domain.configuration.AvVoipAccountCfg;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.DdosIpCfg;
import com.nis.domain.configuration.DnsResStrategy;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.configuration.NtcSubscribeIdCfg;
import com.nis.domain.configuration.DdosIpCfg;
import com.nis.domain.configuration.RequestInfo;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.Constants;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseController;
import com.nis.web.security.UserUtils;

@Controller
@RequestMapping("${adminPath}/manipulation/ddos")
public class DdosCfgController extends BaseController {
	
	@RequestMapping(value = {"/list"})
	public String list(Model model,HttpServletRequest  request,HttpServletResponse response,@ModelAttribute("cfg")DdosIpCfg entity){
		Page<DdosIpCfg> page = ddosCfgService.findPage(new Page<DdosIpCfg>(request, response,"r"), entity);
		model.addAttribute("page", page);
		initPageCondition(model,entity);
		return "/cfg/ddosIpCfgList";
	}
	@RequestMapping(value = {"/form"})
	@RequiresPermissions(value={"ddos:ip:config"})
	public String form(Model model,HttpServletRequest request,HttpServletResponse response,String ids,@ModelAttribute("cfg")DdosIpCfg cfg){
		if(!StringUtil.isEmpty(ids)){
			cfg = ddosCfgService.getDdosIpCfg(Long.parseLong(ids),null);
			initUpdateFormCondition(model, cfg);
		}else{
			initFormCondition(model,cfg);
		}
		model.addAttribute("_cfg", cfg);
		return "/cfg/ddosIpCfgForm";
	}
	
	@RequestMapping(value = {"/saveOrUpdate"})
	@RequiresPermissions(value={"ddos:ip:config"})
	public String saveOrUpdate(Model model,HttpServletRequest request,HttpServletResponse response,
			@ModelAttribute("cfg")DdosIpCfg cfg, RedirectAttributes redirectAttributes){
		try{
			ddosCfgService.saveOrUpdate(cfg);
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
		
		return "redirect:" + adminPath +"/manipulation/ddos/list?functionId="+cfg.getFunctionId();
	}
	
	@RequestMapping(value = {"/delete"})
	@RequiresPermissions(value={"ddos:ip:config"})
	public String delete(Integer isAudit,Integer isValid,String ids,Integer functionId){
		ddosCfgService.delete(isAudit,isValid,ids,functionId);
		return "redirect:" + adminPath +"/manipulation/ddos/list?functionId="+functionId;
	}
	@RequestMapping(value = {"/audit"})
	//@RequiresPermissions(value={"ddos:ip:confirm"})
	public String audit(Model model,@ModelAttribute("cfg")DdosIpCfg cfg,
							Integer isValid,
							Integer isAudit,
							String ids,
							Integer functionId, 
							RedirectAttributes redirectAttributes,
							HttpServletResponse response,
							HttpServletRequest request) {
		if(!StringUtil.isEmpty(ids)){
			String[] idArray = ids.split(",");
			Date auditTime=new Date();
			for(String id :idArray){
				try {
					ddosCfgService.audit(isAudit,isValid,functionId,id,auditTime);
				} catch (MaatConvertException e) {
					e.printStackTrace();
					logger.info("dns fake ip配置下发失败："+e.getMessage());
					addMessage(redirectAttributes,"error","request_service_failed");
				} catch (Exception e) {
					e.printStackTrace();
					logger.info("dns fake ip配置下发失败："+e.getMessage());
					addMessage(redirectAttributes,"error","audit_failed");
				}
				
			}
		}else {
			Page<DdosIpCfg> searchPage=new Page<DdosIpCfg>(request,response,"r");
			Page<DdosIpCfg> auditPage=new Page<DdosIpCfg>(request,response,"r");
			
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
		return "redirect:" + adminPath +"/manipulation/ddos/list?functionId="+functionId;
	}
	
	//ddos配置导出
	@RequestMapping(value = "exportDdos")
	public void exportddos(Model model,HttpServletRequest  request,HttpServletResponse response,
			@ModelAttribute("cfg")DdosIpCfg entity,String ids,RedirectAttributes redirectAttributes){
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
				Page<DdosIpCfg> pageInfo=new Page<DdosIpCfg>(request, response,"r");
				pageInfo.setPageNo(1);
				pageInfo.setPageSize(Constants.MAX_EXPORT_SIZE);
				Page<DdosIpCfg> page = ddosCfgService.findPage(pageInfo, entity);
				titleList.add(entity.getMenuNameCode());
				classMap.put(entity.getMenuNameCode(), DdosIpCfg.class);
				String cfgIndexInfoNoExport=",do_log,client_ip,client_port,ir_type,group_name,userregion1,userregion2,userregion3,userregion4,userregion5,";
				noExportMap.put(entity.getMenuNameCode(),cfgIndexInfoNoExport);
				dataMap.put(entity.getMenuNameCode(), page.getList());
			/*}*/
			this._export(model, request, response, redirectAttributes,entity.getMenuNameCode(),titleList,classMap,dataMap,noExportMap);
		} catch (Exception e) {
			logger.error("ddos export failed",e);
			addMessage(redirectAttributes,"error","export_failed");
		}
		//return "redirect:" + adminPath +"/ntc/iplist/list?functionId="+entity.getFunctionId();
	}
	/**
	 * 根据索引表信息异步获取子表信息
	 * @return
	 */
	@RequestMapping(value = {"/ajaxDdosInfo"})
	public String getDdosInfo(Model model,Long cfgId,Integer index,Integer compileId) {
		DdosIpCfg cfg = ddosCfgService.getDdosIpCfg(cfgId, compileId);
		List<String[]> tabList = new ArrayList();
		//获取DdosIpCfg信息
			String cfgType = null;
				if(!cfg.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"1",cfg.getCfgType()});
					cfgType = cfg.getCfgType();
				}
		
		model.addAttribute("_cfg", cfg);	
		model.addAttribute("index", index);
		model.addAttribute("tabList", tabList);
		return "/cfg/ddosSubList";
	}
}
