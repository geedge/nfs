/**
 *@Title: XmppController.java
 *@Package com.nis.web.controller.configuration.ntc 
 *@Description TODO
 *@author dell
 *@date 2018年6月12日 下午3:04:17
 *@version  版本号
 */
package com.nis.web.controller.configuration.ntc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nis.domain.Page;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.configuration.NtcSubscribeIdCfg;
import com.nis.exceptions.MaatConvertException;
import com.nis.web.controller.BaseController;
import com.nis.web.security.UserUtils;

/**
 * @ClassName: XmppController.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年6月12日 下午3:04:17
 * @version V1.0
 */
@Controller
@RequestMapping("${adminPath}/ntc/other")
public class XmppController extends BaseController {
	@RequestMapping(value = {"xmppList"})
	//@RequiresPermissions(value={"other:xmpp:config","other:xmpp:confirm"},logical=Logical.OR)
	public String list(Model model,@ModelAttribute("cfg")CfgIndexInfo cfg,HttpServletRequest request,HttpServletResponse response) {
		Page<CfgIndexInfo> searchPage=new Page<CfgIndexInfo>(request,response,"a");
		Page<CfgIndexInfo> page = xmppCfgService.getXmppList(searchPage, cfg);
		model.addAttribute("page", page);
		initPageCondition(model,cfg);
		return "/cfg/other/xmppList";
	}
	@RequestMapping(value = {"xmppForm"})
	@RequiresPermissions(value={"other:xmpp:config"})
	public String form(Model model,String ids,CfgIndexInfo entity) {
		if(StringUtils.isNotBlank(ids)){
			entity = xmppCfgService.getXmppCfg(Long.parseLong(ids));
			initUpdateFormCondition(model,entity);
		}else{
			initFormCondition(model,entity);
		}
		
		model.addAttribute("_cfg", entity);
		return "/cfg/other/xmppForm";
	}
	@RequestMapping(value = {"updateXmppCfgValid"})
	@RequiresPermissions(value={"other:xmpp:config"})
	public String updateXmppCfgValid(Integer isValid,String ids,Integer functionId) {
		xmppCfgService.updateXmppCfgValid(isValid,ids,functionId);
		return "redirect:" + adminPath +"/ntc/other/xmppList?functionId="+functionId;
	}
	@RequestMapping(value = {"auditXmppCfg"})
	@RequiresPermissions(value={"other:xmpp:confirm"})
	public String auditXmppCfg(Integer isAudit,Integer isValid,String ids,Integer functionId, RedirectAttributes redirectAttributes) {
		CfgIndexInfo entity = new CfgIndexInfo();
		String[] idArray = ids.split(",");
		for(String id :idArray){
			entity = xmppCfgService.getXmppCfg(Long.parseLong(id));
			entity.setIsAudit(isAudit);
			entity.setIsValid(isValid);
			entity.setAuditorId(UserUtils.getUser().getId());
			entity.setAuditTime(new Date());
			entity.setFunctionId(functionId);
			try {
				xmppCfgService.auditXmppCfg(entity,isAudit);
			} catch ( Exception e) {
				e.printStackTrace();
				logger.info("http配置下发失败："+e.getMessage());
				if(e instanceof MaatConvertException) {
					addMessage(redirectAttributes,"error","request_service_failed");
				}else {
					addMessage(redirectAttributes,"error","audit_failed");
				}
			}
		}
		return "redirect:" + adminPath +"/ntc/other/xmppList?functionId="+functionId;
	}
	@RequestMapping(value = {"ajaxXmppSubList"})
	public String ajaxXmppSubList(Model model,Long cfgId,Integer index) {
		CfgIndexInfo cfg = xmppCfgService.getXmppCfg(cfgId);
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
		model.addAttribute("_cfg", cfg);	
		model.addAttribute("index", index);
		model.addAttribute("tabList", tabList);
		return "/cfg/other/xmppSubList";
	}
	@RequestMapping(value = {"saveXmppCfg"})
	@RequiresPermissions(value={"other:xmpp:config"})
	public String saveXmppCfg(Model model,HttpServletRequest request,HttpServletResponse response,String ids,CfgIndexInfo entity) {
		xmppCfgService.saveXmppCfg(entity);
		return "redirect:" + adminPath +"/ntc/other/xmppList?functionId="+entity.getFunctionId();
	}
}
