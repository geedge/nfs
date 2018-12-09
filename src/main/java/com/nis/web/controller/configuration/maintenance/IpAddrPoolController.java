package com.nis.web.controller.configuration.maintenance;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nis.domain.Page;
import com.nis.domain.basics.IpReuseIpCfg;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.IpAddrPoolCfg;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.Constants;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseController;
import com.nis.web.service.configuration.IpAddrPoolCfgService;


/**
 * IP复用地址池配置
 * @author dell
 *
 */
@Controller
@RequestMapping("${adminPath}/maintenance/ipMultiplexPoolCfg")
public class IpAddrPoolController extends BaseController{
	
	@Autowired
	private IpAddrPoolCfgService ipAddrPoolCfgService;
	
	@RequestMapping(value = {"/snatlist"})
	public String snatlist(Model model,HttpServletRequest request,HttpServletResponse response,
			@ModelAttribute("cfg")IpAddrPoolCfg entity){
		Page<IpAddrPoolCfg> page = ipAddrPoolCfgService.findPage(new Page<IpAddrPoolCfg>(request, response,"r"), entity);
		model.addAttribute("page", page);
		initFormCondition(model,entity);
		return "/cfg/maintenance/ipMultiplexPool/snatlist2";
	}
	
	@RequestMapping(value = {"/snatform"})
	@RequiresPermissions(value={"ip:mulitiplex:pool:config"})
	public String snatfrom(Model model, String ids, HttpServletRequest request, HttpServletResponse response,
							@ModelAttribute("cfg")IpAddrPoolCfg cfg){
		if(cfg == null){
			cfg=new IpAddrPoolCfg();
		}
		if(!StringUtil.isEmpty(ids)){
			cfg = ipAddrPoolCfgService.getIpAddrPoolCfg(Long.valueOf(ids),null);
			initUpdateFormCondition(model, cfg);
		}else{
			initFormCondition(model, cfg);
		}
		
		model.addAttribute("_cfg", cfg);
		return "/cfg/maintenance/ipMultiplexPool/snatform2";
	}
	
	@RequestMapping(value = {"/snatSaveOrUpdate"})
	@RequiresPermissions(value={"ip:mulitiplex:pool:config"})
	public String snatSaveOrUpdate(Model model,HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes,
									@ModelAttribute("cfg")IpAddrPoolCfg cfg){
		try{
			// 添加地址池配置信息
			ipAddrPoolCfgService.saveOrUpdate(cfg, request, response);
			addMessage(redirectAttributes,"success","save_success");
		}catch(Exception e) {
			e.printStackTrace();
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,"error", "request_service_failed");
			}else {
				addMessage(redirectAttributes,"error", "save_failed");
			}
		}
		
		return "redirect:" + adminPath +"/maintenance/ipMultiplexPoolCfg/snatlist?functionId="+cfg.getFunctionId();
	}
	
	@RequestMapping(value = {"/snatAudit"})
	@RequiresPermissions(value={"ip:mulitiplex:pool:confirm"})
	public String snataudit(Integer isAudit,Integer isValid,String ids,Integer functionId,RedirectAttributes redirectAttributes) {
		if(!StringUtil.isEmpty(ids)){
			String[] idArray = ids.split(",");
			Date auditTime=new Date();
			for(String id :idArray){
				try {
					ipAddrPoolCfgService.audit(isAudit,isValid,functionId,id,auditTime);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("SNAT地址池配置下发失败："+e.getMessage());
					if(e instanceof MaatConvertException) {
						addMessage(redirectAttributes,"error", "request_service_failed");
					}else {
						addMessage(redirectAttributes,"error", "audit_failed");
					}
				}
			}
			
		}
		return "redirect:" + adminPath +"/maintenance/ipMultiplexPoolCfg/snatlist?functionId="+functionId;
	}
	
	/**
	 * 配置删除
	 * @param isAudit
	 * @param isValid
	 * @param ids
	 * @param functionId
	 * @param model
	 * @param redirectAttributes
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = {"/snatDelete"})
	@RequiresPermissions(value={"ip:mulitiplex:pool:config"})
	public String snatdelete(Integer isAudit,Integer isValid,String ids,Integer functionId,Model model,RedirectAttributes redirectAttributes
			,HttpServletRequest request,HttpServletResponse response){
		try{
			if(!StringUtil.isEmpty(ids)){
				ipAddrPoolCfgService.delete(isAudit,isValid,ids,functionId);
			}
			addMessage(redirectAttributes,"success","delete_success");
		}catch(Exception e){
			logger.error(e);
			addMessage(redirectAttributes,"error","delete_failed");
		}
		return "redirect:" + adminPath +"/maintenance/ipMultiplexPoolCfg/snatlist?functionId="+functionId;
	}
	
	/**
	 * 校验地址池
	 * @param cfg
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = {"/checkAddrPool"})
	public boolean checkAddrPool(IpAddrPoolCfg cfg, HttpServletRequest request, HttpServletResponse response){
		// 修改
		if(!StringUtil.isEmpty(cfg.getCfgId())){
			IpAddrPoolCfg poolCfg = ipAddrPoolCfgService.getCfgInfo(cfg);
			if(poolCfg != null && poolCfg.getAddrPoolName().equals(cfg.getAddrPoolName())){
				return true;
			}
		}
		cfg.setCfgId(null);
		IpAddrPoolCfg poolCfg = ipAddrPoolCfgService.getCfgInfo(cfg);
		if(poolCfg != null){
			return false;
		}
		return true;
	}
	
	@ResponseBody
	@RequestMapping(value = {"/getCGIInfo"})
	public Map<String,Object> getUserInfo(Integer addrPoolId,String cmd,HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", cmd);
		params.put("addr_pool_id", addrPoolId);
		String url =Constants.IP_REUSE_CALL_CGI_URL ;
		resultMap=ConfigServiceUtil.getCGIInfo(url, cmd, params);
		return resultMap;
	}
	
	@ResponseBody
	@RequestMapping(value = {"/getAllIpInfo"})
	public List<IpReuseIpCfg> getAllIpInfo(Integer addrPoolId,HttpServletRequest request,HttpServletResponse response){
		List<IpReuseIpCfg> ipList = ipReuseIpCfgService.getPolicyIpInfo(addrPoolId);
		return ipList;
	}

}
