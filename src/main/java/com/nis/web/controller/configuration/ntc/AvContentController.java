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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nis.domain.Page;
import com.nis.domain.basics.ServiceDictInfo;
import com.nis.domain.basics.SysDictInfo;
import com.nis.domain.configuration.AreaBean;
import com.nis.domain.configuration.AreaIpCfg;
import com.nis.domain.configuration.AvContUrlCfg;
import com.nis.domain.configuration.AvVoipAccountCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.configuration.NtcSubscribeIdCfg;
import com.nis.exceptions.MaatConvertException;
import com.nis.domain.configuration.AvVoipIpCfg;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.util.Constants;
import com.nis.util.DateUtils;
import com.nis.util.StringUtil;
import com.nis.util.excel.ExportExcel;
import com.nis.web.controller.BaseController;

/**
 * 处理音视频文本业务
 * @author ddm
 *
 */
@Controller
@RequestMapping("${adminPath}/ntc/av")
public class AvContentController extends BaseController  {
	/*****************************voip业务***************/
	//音视频VOIP IP配置新增界面
		@RequestMapping(value = {"/voipForm"})
		@RequiresPermissions(value={"avVoip:config"})
		public String voipFrom(Model model,HttpServletRequest request ,HttpServletResponse response
				,String compileIds,String ids, CfgIndexInfo cfg ,RedirectAttributes redirectAttributes){
			if(!StringUtil.isEmpty(ids)){
				cfg.setCfgId(Long.parseLong(ids));
				cfg = avContentCfgService.getCfgIndexInfo(cfg);
				initUpdateFormCondition(model, cfg);
			}else{
				initFormCondition(model,cfg);
			}
			
			model.addAttribute("_cfg", cfg);
			return "/cfg/av/voip/voipForm";
		}
		//音视频VOIP Account配置新增界面
/*		@RequestMapping(value = {"/voipAccountForm"})
		@RequiresPermissions(value={"avVoipAccount:config"})
		public String voipAccountForm(Model model,HttpServletRequest request ,HttpServletResponse response
				,String compileIds,String ids, CfgIndexInfo cfg ,RedirectAttributes redirectAttributes){
			//修改
			if(!StringUtil.isEmpty(compileIds)){
				cfg.setCompileId(Integer.parseInt(compileIds));
				cfg = avContentCfgService.getCfgIndexInfo2(cfg);
				initUpdateFormCondition(model, cfg);
			}else{
				initFormCondition(model,cfg);//新增
			}
			
			model.addAttribute("_cfg", cfg);
			return "/cfg/av/voip/voipAccountForm";
		}*/
	
	//保存voip ip信息
	@RequestMapping(value = {"/saveVoip"})
	public String saveVoip(Model model,HttpServletRequest request,HttpServletResponse response,String ids,
			CfgIndexInfo cfg ,RedirectAttributes redirectAttributes){
		try{
			avContentCfgService.saveOrUpdateAvVoip(cfg);
			addMessage(redirectAttributes,"success","save_success");
		}catch(Exception e){
			if(e instanceof MaatConvertException) {
				e.printStackTrace();
				logger.error("voip 信息保存失败",e);
				addMessage(redirectAttributes,"error","request_service_failed");
			}else {
				e.printStackTrace();
				logger.error("voip 信息保存失败",e);
				addMessage(redirectAttributes,"error","save_failed");
			}
		}
		return "redirect:" + adminPath +"/ntc/av/voipList?functionId="+cfg.getFunctionId();
	}
	//保存voip Account信息
/*	@RequestMapping(value = {"/saveAccountVoip"})
	public String saveAccountVoip(Model model,HttpServletRequest request,HttpServletResponse response,String ids,
			CfgIndexInfo cfg ,RedirectAttributes redirectAttributes){
		try{
			avContentCfgService.saveOrUpdateAvVoipAccount(cfg);
			addMessage(redirectAttributes,"save_success");
		}catch(Exception e){
			if(e instanceof MaatConvertException) {
				e.printStackTrace();
				logger.error("voip Account信息保存失败",e);
				addMessage(redirectAttributes,"request_service_failed");
			}else {
				e.printStackTrace();
				logger.error("voip Account信息保存失败",e);
				addMessage(redirectAttributes,"save_failed");
			}
		}
		return "redirect:" + adminPath +"/ntc/av/voipAccountList?functionId="+cfg.getFunctionId();
	}*/
	//视频文本VOIP 配置列表
	@RequestMapping(value = {"/voipList"})
	public String voipCfgList(Model model,HttpServletRequest  request ,HttpServletResponse response ,@ModelAttribute("cfg")CfgIndexInfo entity
			,RedirectAttributes redirectAttributes){
		/*if(entity.getIpPort()== null){
			entity.setIpPort(new IpPortCfg());
		}
		if(entity.getVoipAccount()== null){
			entity.setVoipAccount(new AvVoipAccountCfg());
		}
		if(entity.getNtcSubscribeIdCfg()==null){
			entity.setNtcSubscribeIdCfg(new NtcSubscribeIdCfg());
		}*/
		Page<CfgIndexInfo> page = avContentCfgService.findPage(new Page<CfgIndexInfo>(request, response,"r"), entity);
		model.addAttribute("page", page);
		initPageCondition(model,entity);
		return "/cfg/av/voip/voipList";
	}
	//视频文本VOIP Account配置列表
/*	@RequestMapping(value = {"/voipAccountList"})
	public String voipCfgAccountList(Model model,HttpServletRequest request ,HttpServletResponse response
			,@ModelAttribute("cfg")CfgIndexInfo entity ,RedirectAttributes redirectAttributes){
		if(entity.getVoipAccount()== null){
			entity.setVoipAccount(new AvVoipAccountCfg());
		}
		if(entity.getNtcSubscribeIdCfg()==null){
			entity.setNtcSubscribeIdCfg(new NtcSubscribeIdCfg());
		}
		Page<CfgIndexInfo> page = avContentCfgService.findAccountPage(new Page<CfgIndexInfo>(request, response,"r"), entity);
		model.addAttribute("page", page);
		initPageCondition(model,entity);
		return "/cfg/av/voip/voipAccountList";
	}*/
	
	
	//修改VOIP IP配置状态
	@RequestMapping(value = {"/updateAvVoipValid"})
	@RequiresPermissions(value={"avVoip:config"})
	public String updateVoipValid(Integer isAudit,String compileIds,Integer isValid,String ids,Integer functionId
			,RedirectAttributes redirectAttributes
			) {
		try {
			avContentCfgService.updateAvVoipValid(isAudit,isValid,compileIds,functionId);
		} catch (Exception e) {
			if(e instanceof MaatConvertException) {
				e.printStackTrace();
				logger.error("voip 信息审核失败",e);
				addMessage(redirectAttributes,"error","request_service_failed");
			}else {
				e.printStackTrace();
				logger.error("voip 信息审核失败",e);
				addMessage(redirectAttributes,"error","delete_failed");
			}
		}
		return "redirect:" + adminPath +"/ntc/av/voipList?functionId="+functionId;
	}
	//修改VOIP account配置状态
/*	@RequestMapping(value = {"/updateAvVoipAccountValid"})
	@RequiresPermissions(value={"avVoipAccount:config"})
	public String updateVoipAccountValid(Integer isAudit,String compileIds,Integer isValid,String ids,Integer functionId
			,RedirectAttributes redirectAttributes
			) {
		try {
			avContentCfgService.updateAvVoipAccountValid(isAudit,isValid,compileIds,functionId);
		} catch (Exception e) {
			if(e instanceof MaatConvertException) {
				e.printStackTrace();
				logger.error("voip Account信息审核失败",e);
				addMessage(redirectAttributes,"request_service_failed");
			}else {
				e.printStackTrace();
				logger.error("voip Account信息审核失败",e);
				addMessage(redirectAttributes,"audit_failed");
			}
		}
		return "redirect:" + adminPath +"/ntc/av/voipAccountList?functionId="+functionId;
	}*/
	//修改VOIP IP配置审核状态
	@RequestMapping(value = {"/auditAvVoip"})
	@RequiresPermissions(value={"avVoip:confirm"})
	public String auditVoip(Model model,@ModelAttribute("cfg")CfgIndexInfo cfg,
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
					avContentCfgService.auditAvVoip(isAudit,isValid,functionId,id,auditTime);
				} catch ( Exception e) {
					if(e instanceof MaatConvertException) {
						e.printStackTrace();
						logger.error("VOIP配置下发失败："+e.getMessage());;
						addMessage(redirectAttributes,"error","request_service_failed");
					}else {
						e.printStackTrace();
						logger.error("VOIP配置下发失败",e);
						addMessage(redirectAttributes,"error","audit_failed");
					}
					
				}
			}
			
		}else {
			Page<CfgIndexInfo> searchPage=new Page<CfgIndexInfo>(request,response,"r");
			Page<CfgIndexInfo> auditPage=new Page<CfgIndexInfo>(request,response,"r");
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
			return voipCfgList(model, request, response, cfg, redirectAttributes);
		}
		return "redirect:" + adminPath +"/ntc/av/voipList?functionId="+functionId;
	}
	//修改VOIP IP配置审核状态
/*	@RequestMapping(value = {"/auditAvVoipAccount"})
	@RequiresPermissions(value={"avVoipAccount:confirm"})
	public String auditVoipAccount(Integer isAudit,Integer isValid,String ids
			,String compileIds,Integer functionId
			,RedirectAttributes redirectAttributes) {
		if(!StringUtil.isEmpty(compileIds)){
			String[] idArray = compileIds.split(",");
			Date auditTime=new Date();
			for(String id :idArray){
				try {
					avContentCfgService.auditAvVoipAccount(isAudit,isValid,functionId,id,auditTime);
				} catch (MaatConvertException e) {
					if(e instanceof MaatConvertException) {
						e.printStackTrace();
						logger.info("VOIPAccount配置下发失败："+e.getMessage());;
						addMessage(redirectAttributes,"request_service_failed");
					}else {
						e.printStackTrace();
						logger.error("VOIPAccount配置下发失败",e);
						addMessage(redirectAttributes,"audit_failed");
					}
				}
			}
			
		}
		return "redirect:" + adminPath +"/ntc/av/voipAccountList?functionId="+functionId;
	}*/
	
	/**
	 * 账号account
	 * 根据索引表信息异步获取子表信息
	 * @return
	 */
	/*@ResponseBody
	@RequestMapping(value = "ajaxVoipAccountInfo")
	public Map getVoipAccountInfo(CfgIndexInfo cfgIndexInfo) {
		Map<String, Object> voipAndAccountInfoMap=new HashMap<String, Object>();
		List<AvVoipAccountCfg> voipAccountCfgList=new ArrayList<AvVoipAccountCfg>();
		List<NtcSubscribeIdCfg> ntcSubscribeIdCfgList=new ArrayList<NtcSubscribeIdCfg>();
		
		voipAccountCfgList = avContentCfgService.getVoipAccountCfgList(cfgIndexInfo);
		ntcSubscribeIdCfgList=avContentCfgService.getSubscribeIdCfgList(cfgIndexInfo);
		
		voipAndAccountInfoMap.put("account", voipAccountCfgList);
		voipAndAccountInfoMap.put("subscribe", ntcSubscribeIdCfgList);
		return voipAndAccountInfoMap;
	}*/
	/**
	 * 根据索引表信息异步获取子表信息
	 * @return
	 */
	@RequestMapping(value = {"/ajaxVoipIpInfo"})
	public String getVoipIpInfo(Model model,Long cfgId,Integer index,Integer compileId) {
		CfgIndexInfo cfgIndexInfo = new CfgIndexInfo();
		cfgIndexInfo.setCfgId(cfgId);
		cfgIndexInfo.setCompileId(compileId);
		CfgIndexInfo cfg = avContentCfgService.getCfgIndexInfo(cfgIndexInfo);
		List<String[]> tabList = new ArrayList();
		//获取voipIpCfg信息
/*		if(cfg.getVoipIps()!=null){
			String cfgType = null;
			for(AvVoipIpCfg ip:cfg.getVoipIps()){
				if(!ip.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"1",ip.getCfgType()});
					cfgType = ip.getCfgType();
				}
			}
		}*/
		if(cfg.getIpPortList()!=null){
			String cfgType = null;
			for(IpPortCfg ip:cfg.getIpPortList()){
				if(!ip.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"1",ip.getCfgType()});
					cfgType = ip.getCfgType();
				}
			}
		}
		
		if(cfg.getVoipAccounts()!=null){
			String cfgType = null;
			for(AvVoipAccountCfg account:cfg.getVoipAccounts()){
				if(account!=null&&!account.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"2",account.getCfgType()});
					cfgType = account.getCfgType();
				}
			}
		}
		//查询关键字
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
		return "/cfg/av/voip/voipSubList";
	}
	/*****************************voip业务***************/
	
	/*****************************contIp业务***************/
	//视频文本内容配置列表
	@RequestMapping(value = {"/contIpList"})
	public String contIpList(Model model,HttpServletRequest  request
			,HttpServletResponse response
			,@ModelAttribute("cfg")BaseIpCfg entity
			,RedirectAttributes redirectAttributes){
		Page<BaseIpCfg> page = avContentCfgService.findContIpPage(new Page<BaseIpCfg>(request, response,"r"), entity);
		model.addAttribute("page", page);
		initPageCondition(model,entity);
		return "/cfg/av/contIp/contIpList";
	}
	//音视频CONTIP新增界面
	@RequestMapping(value = {"/contIpForm"})
	@RequiresPermissions(value={"avContIp:config"})
	public String contIpForm(Model model,HttpServletRequest request,HttpServletResponse response
			,String ids,String compileIds
			,BaseIpCfg cfg
			,RedirectAttributes redirectAttributes){
		if(!StringUtil.isEmpty(compileIds)){
			cfg.setCompileId(Integer.parseInt(compileIds));
			cfg = avContentCfgService.getContIpCfgById(cfg);
			initUpdateFormCondition(model, cfg);
		}else{
			initFormCondition(model,cfg);
		}
		model.addAttribute("_cfg", cfg);
		return "/cfg/av/contIp/contIpForm";
	}
	
	//保存CONTIP信息
	@RequestMapping(value = {"/saveContIp"})
	public String saveContIp(Model model,HttpServletRequest request
			,HttpServletResponse response,
			@ModelAttribute("cfg")BaseIpCfg cfg,
			@ModelAttribute("areaCfgIds")String areaCfgIds
			,RedirectAttributes redirectAttributes){
		try{
			avContentCfgService.saveOrUpdateContIp(cfg,areaCfgIds);
			addMessage(redirectAttributes,"success","save_success");
		}catch(Exception e){
			logger.error("信息保存失败",e);
			e.printStackTrace();
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,"error", "request_service_failed");
			}else {
				addMessage(redirectAttributes,"error","save_failed");
			}
		}
		
		return "redirect:" + adminPath +"/ntc/av/contIpList?functionId="+cfg.getFunctionId();
	}
	
	//修改CONTIP例配置状态
	@RequestMapping(value = {"/updateAvContIpValid"})
	@RequiresPermissions(value={"avContIp:config"})
	public String updateAvContIpValid(Integer isAudit,Integer isValid
			,String ids,Integer functionId
			,RedirectAttributes redirectAttributes){
		avContentCfgService.updateContIpValid(isAudit,isValid,ids,functionId);
		return "redirect:" + adminPath +"/ntc/av/contIpList?functionId="+functionId;
	}
	//修改CONTIP配置审核
	@RequestMapping(value = {"/auditAvContIp"})
	@RequiresPermissions(value={"avContIp:confirm"})
	public String auditContIp(Integer isAudit,Integer isValid
			,String ids,String compileIds,Integer functionId
			, RedirectAttributes redirectAttributes) {
		if(!StringUtil.isEmpty(compileIds)){
			String[] idArray = compileIds.split(",");
			Date auditTime=new Date();
			for(String id :idArray){
				try {
					avContentCfgService.auditContIp(isAudit,isValid,functionId,id,auditTime);
				} catch (MaatConvertException e) {
					logger.error("Cont Ip配置下发失败",e);
					addMessage(redirectAttributes,"error","request_service_failed");
				}catch (Exception e) {
					logger.error("Cont Ip配置下发失败",e);
					addMessage(redirectAttributes,"error","audit_failed");
				}
				
			}
			
		}
		return "redirect:" + adminPath +"/ntc/av/contIpList?functionId="+functionId;
	}
	// 获取域配置信息
	@RequestMapping(value = {"ajaxAvContIpList"})
	public String ajaxAvContIpList(Model model,Long cfgId,Integer index) {
		BaseIpCfg baseIpCfg = new BaseIpCfg();
		baseIpCfg.setCfgId(cfgId);
		BaseIpCfg cfg = avContentCfgService.getContIpCfgById(baseIpCfg);
		List<String[]> tabList = new ArrayList();
		
		tabList.add(new String[]{"1",cfg.getCfgType()});
		if(cfg.getNtcSubscribeIdCfgList()!=null){
			String cfgType = null;
			for(BaseStringCfg subscribeId:cfg.getNtcSubscribeIdCfgList()){
				if(!subscribeId.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"2",subscribeId.getCfgType()});
					cfgType = subscribeId.getCfgType();
				}
			}
		}
		model.addAttribute("_cfg", cfg);	
		model.addAttribute("index", index);
		model.addAttribute("tabList", tabList);
		return "/cfg/av/subList";
	}
	/*****************************contIp业务***************/
	
	/*****************************picIp业务***************/
	//视频文本内容配置列表
	@RequestMapping(value = {"/picIpList"})
	public String picIpList(Model model,HttpServletRequest  request
			,HttpServletResponse response
			,@ModelAttribute("cfg")BaseIpCfg entity
			,RedirectAttributes redirectAttributes){
		Page<BaseIpCfg> page = avContentCfgService.findPicIpPage(new Page<BaseIpCfg>(request, response,"r"), entity);
		model.addAttribute("page", page);
		initPageCondition(model,entity);
		return "/cfg/av/picIp/picIpList";
	}
	//音视频CONTIP新增界面
	@RequestMapping(value = {"/picIpForm"})
	@RequiresPermissions(value={"avPicIp:config"})
	public String picIpForm(Model model,HttpServletRequest request
			,HttpServletResponse response,String ids
			,String compileIds,BaseIpCfg cfg
			,RedirectAttributes redirectAttributes){
		if(!StringUtil.isEmpty(compileIds)){
			cfg.setCompileId(Integer.parseInt(compileIds));
			cfg = avContentCfgService.getPicIpCfgById(cfg);
			initUpdateFormCondition(model, cfg);
		}else{
			initFormCondition(model,cfg);
		}
		model.addAttribute("_cfg", cfg);
		return "/cfg/av/picIp/picIpForm";
	}
	
	//保存CONTIP信息
	@RequestMapping(value = {"/savePicIp"})
	public String savePicIp(Model model,HttpServletRequest request
			,HttpServletResponse response,
			@ModelAttribute("cfg")BaseIpCfg cfg,
			@ModelAttribute("areaCfgIds")String areaCfgIds
			,RedirectAttributes redirectAttributes){
		try{
			avContentCfgService.saveOrUpdatePicIp(cfg,areaCfgIds);
			addMessage(redirectAttributes,"success","save_success");
		}catch(Exception e){
			logger.error("信息保存失败",e);
			e.printStackTrace();
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,"error", "request_service_failed");
			}else {
				addMessage(redirectAttributes,"error","save_failed");
			}
		}
		
		return "redirect:" + adminPath +"/ntc/av/picIpList?functionId="+cfg.getFunctionId();
	}
	
	//修改CONTIP例配置状态
	@RequestMapping(value = {"/updatePicIpValid"})
	@RequiresPermissions(value={"avPicIp:config"})
	public String updateAvPicIpValid(Integer isAudit,Integer isValid
			,String ids,Integer functionId
			,RedirectAttributes redirectAttributes){
		avContentCfgService.updatePicIpValid(isAudit,isValid,ids,functionId);
		return "redirect:" + adminPath +"/ntc/av/picIpList?functionId="+functionId;
	}
	//修改CONTIP配置审核
	@RequestMapping(value = {"/auditPicIp"})
	@RequiresPermissions(value={"avPicIp:confirm"})
	public String auditPicIp(Integer isAudit,Integer isValid,String ids
			,String compileIds,Integer functionId
			, RedirectAttributes redirectAttributes) {
		if(!StringUtil.isEmpty(compileIds)){
			String[] idArray = compileIds.split(",");
			Date auditTime=new Date();
			for(String id :idArray){
				try {
					avContentCfgService.auditPicIp(isAudit,isValid,functionId,id,auditTime);
				} catch (MaatConvertException e) {
					e.printStackTrace();
					logger.error("Pic Ip配置下发失败",e);
					addMessage(redirectAttributes,"error","request_service_failed");
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Pic Ip配置下发失败",e);
						addMessage(redirectAttributes,"error","audit_failed");
				}
			}
			
		}
		return "redirect:" + adminPath +"/ntc/av/picIpList?functionId="+functionId;
	}
	// 获取域配置信息
	@RequestMapping(value = {"ajaxAvPicIpList"})
	public String ajaxAvPicIpList(Model model,Long cfgId,Integer index) {
		BaseIpCfg baseIpCfg = new BaseIpCfg();
		baseIpCfg.setCfgId(cfgId);
		BaseIpCfg cfg = avContentCfgService.getPicIpCfgById(baseIpCfg);
		List<String[]> tabList = new ArrayList();
		
		tabList.add(new String[]{"1",cfg.getCfgType()});
		if(cfg.getNtcSubscribeIdCfgList()!=null){
			String cfgType = null;
			for(BaseStringCfg subscribeId:cfg.getNtcSubscribeIdCfgList()){
				if(!subscribeId.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"2",subscribeId.getCfgType()});
					cfgType = subscribeId.getCfgType();
				}
			}
		}
		model.addAttribute("_cfg", cfg);	
		model.addAttribute("index", index);
		model.addAttribute("tabList", tabList);
		return "/cfg/av/subList";
	}	
	/*****************************picIp业务***************/
	
	
	/*****************************contUrl业务***************/
	//视频文本内容配置列表
	@RequestMapping(value = {"/contUrlList"})
	public String contUrlList(Model model,HttpServletRequest  request
			,HttpServletResponse response
			,@ModelAttribute("cfg")CfgIndexInfo entity
			,RedirectAttributes redirectAttributes){
		/*if(entity.getIpPort()== null){
			entity.setIpPort(new IpPortCfg());
		}
		if(entity.getAvContUrlCfgList()== null){
			entity.setAvContUrlCfg(new AvContUrlCfg());;
		}
		if(entity.getNtcSubscribeIdCfg()==null){
			entity.setNtcSubscribeIdCfg(new NtcSubscribeIdCfg());
		}*/
		Page<CfgIndexInfo> page = avContentCfgService.findPage(new Page<CfgIndexInfo>(request, response,"r"), entity);
		model.addAttribute("page", page);
		initPageCondition(model,entity);
		return "/cfg/av/contUrl/contUrlList";
	}
	//音视频CONTUrl新增界面
	@RequestMapping(value = {"/contUrlForm"})
	@RequiresPermissions(value={"avContUrl:config"})
	public String contUrlForm(Model model,HttpServletRequest request ,HttpServletResponse response
			,String compileIds,String ids, CfgIndexInfo cfg ,RedirectAttributes redirectAttributes){
		if(!StringUtil.isEmpty(ids)){
			cfg.setCfgId(Long.parseLong(ids));
			cfg = avContentCfgService.getUrlCfgIndexInfo(cfg);
			AvContUrlCfg urlCfg = new AvContUrlCfg();
			urlCfg.setCfgType("NTC_STREAMING_MEDIA_URL");
			cfg.setAvContUrlCfg(urlCfg);
			if(cfg.getAvContUrlCfgList().size()==0){
				cfg.getAvContUrlCfgList().add(urlCfg);
			}
			initUpdateFormCondition(model, cfg);
		}else{
			AvContUrlCfg urlCfg = new AvContUrlCfg();
			urlCfg.setCfgType("NTC_STREAMING_MEDIA_URL");
			cfg.setAvContUrlCfg(urlCfg);
			List<AvContUrlCfg> urlList=new ArrayList<AvContUrlCfg>();
			urlList.add(urlCfg);
			cfg.setAvContUrlCfgList(urlList);
			initFormCondition(model,cfg);
		}
		model.addAttribute("_cfg", cfg);
		return "/cfg/av/contUrl/contUrlForm";
	}
	
	//保存CONTUrl信息
	@RequestMapping(value = {"/saveContUrl"})
	public String saveContUrl(Model model,HttpServletRequest request,HttpServletResponse response,String ids,
			CfgIndexInfo cfg ,RedirectAttributes redirectAttributes){
		try{
			avContentCfgService.saveOrUpdateContUrl(cfg);
			addMessage(redirectAttributes,"success","save_success");
		}catch(Exception e){
			if(e instanceof MaatConvertException) {
				e.printStackTrace();
				logger.error("Stream Media 信息保存失败",e);
				addMessage(redirectAttributes,"error","request_service_failed");
			}else {
				e.printStackTrace();
				logger.error("Stream Media 信息保存失败",e);
				addMessage(redirectAttributes,"error","save_failed");
			}
		}
		return "redirect:" + adminPath +"/ntc/av/contUrlList?functionId="+cfg.getFunctionId();
	}
	
	//修改CONTUrl例配置状态
	@RequestMapping(value = {"/updateAvContUrlValid"})
	@RequiresPermissions(value={"avContUrl:config"})
	public String updateAvContUrlValid(Integer isAudit,Integer isValid
			,String ids,Integer functionId
			,RedirectAttributes redirectAttributes){
		avContentCfgService.updateContUrlValid(isAudit,isValid,ids,functionId);
		return "redirect:" + adminPath +"/ntc/av/contUrlList?functionId="+functionId;
	}
	//修改CONTUrl配置审核
	@RequestMapping(value = {"/auditAvContUrl"})
	@RequiresPermissions(value={"avContUrl:confirm"})
	public String auditContUrl(Model model,@ModelAttribute("cfg")CfgIndexInfo cfg,
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
					avContentCfgService.auditContUrl(isAudit,isValid,functionId,id,auditTime);
				} catch ( Exception e) {
					e.printStackTrace();
					logger.info("Cont Url配置下发失败",e);
					if(e instanceof MaatConvertException) {
						addMessage(redirectAttributes,"error", "request_service_failed");
					}else {
						addMessage(redirectAttributes,"error","audit_failed");
					}
				}
			}
			
		}else {
			Page<CfgIndexInfo> searchPage=new Page<CfgIndexInfo>(request,response,"r");
			Page<CfgIndexInfo> auditPage=new Page<CfgIndexInfo>(request,response,"r");
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
			return contUrlList(model, request, response, cfg, redirectAttributes);
		}
		return "redirect:" + adminPath +"/ntc/av/contUrlList?functionId="+functionId;
	}
	// 获取域配置信息
	@RequestMapping(value = {"ajaxAvContUrlList"})
	public String ajaxAvContUrlList(Model model,Long cfgId,Integer index,Integer compileId) {
		CfgIndexInfo cfgIndexInfo = new CfgIndexInfo();
		cfgIndexInfo.setCfgId(cfgId);
		cfgIndexInfo.setCompileId(compileId);
		CfgIndexInfo cfg = avContentCfgService.getUrlCfgIndexInfo(cfgIndexInfo);
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
		if(cfg.getAvContUrlCfgList()!=null){
			String cfgType = null;
			for(AvContUrlCfg avContUrlCfg:cfg.getAvContUrlCfgList()){
				if(!avContUrlCfg.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"2",avContUrlCfg.getCfgType()});
					cfgType = avContUrlCfg.getCfgType();
				}
			}
		}
		
		
		//查询关键字
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
		return "/cfg/av/contUrl/contSubList";
	}
	/*****************************contUrl业务***************/
	
	
	/*****************************picUrl业务***************/
	//视频文本内容配置列表
	@RequestMapping(value = {"/picUrlList"})
	public String picUrlList(Model model,HttpServletRequest  request
			,HttpServletResponse response
			,@ModelAttribute("cfg")BaseStringCfg entity
			,RedirectAttributes redirectAttributes){
		Page<BaseStringCfg> page = avContentCfgService.findPicUrlPage(new Page<BaseStringCfg>(request, response,"r"), entity);
		model.addAttribute("page", page);
		initPageCondition(model,entity);
		return "/cfg/av/picUrl/picUrlList";
	}
	//音视频CONTUrl新增界面
	@RequestMapping(value = {"/picUrlForm"})
	@RequiresPermissions(value={"avPicUrl:config"})
	public String picUrlForm(Model model,HttpServletRequest request
			,HttpServletResponse response,String ids
			,String compileIds,@ModelAttribute("cfg")BaseStringCfg cfg
			,RedirectAttributes redirectAttributes){
		if(!StringUtil.isEmpty(compileIds)){
			cfg.setCompileId(Integer.parseInt(compileIds));
			cfg = avContentCfgService.getPicUrlCfgById(cfg);
			initUpdateFormCondition(model, cfg);
		}else{
			initFormCondition(model,cfg);
		}
		model.addAttribute("_cfg", cfg);
		return "/cfg/av/picUrl/picUrlForm";
	}
	
	//保存CONTUrl信息
	@RequestMapping(value = {"/savePicUrl"})
	public String savePicUrl(Model model,HttpServletRequest request
			,HttpServletResponse response,
			@ModelAttribute("cfg")BaseStringCfg cfg,
			@ModelAttribute("areaCfgIds")String areaCfgIds
			,RedirectAttributes redirectAttributes){
		try{
			avContentCfgService.saveOrUpdatePicUrl(cfg,areaCfgIds);
			addMessage(redirectAttributes,"success","save_success");
		}catch(Exception e){
			logger.error("信息保存失败",e);
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,"error", "request_service_failed");
			}else {
				addMessage(redirectAttributes,"error","save_failed");
			}
		}
		
		return "redirect:" + adminPath +"/ntc/av/picUrlList?functionId="+cfg.getFunctionId();
	}
	
	//修改CONTUrl例配置状态
	@RequestMapping(value = {"/updateAvPicUrlValid"})
	@RequiresPermissions(value={"avPicUrl:config"})
	public String updateAvPicUrlValid(Integer isAudit,Integer isValid
			,String ids,Integer functionId
			,RedirectAttributes redirectAttributes){
		avContentCfgService.updatePicUrlValid(isAudit,isValid,ids,functionId);
		return "redirect:" + adminPath +"/ntc/av/picUrlList?functionId="+functionId;
	}
	//修改CONTUrl配置审核
	@RequestMapping(value = {"/auditAvPicUrl"})
	@RequiresPermissions(value={"avPicUrl:confirm"})
	public String auditPicUrl(Integer isAudit,Integer isValid
			,String ids,String compileIds,Integer functionId
			, RedirectAttributes redirectAttributes) {
		if(!StringUtil.isEmpty(compileIds)){
			String[] idArray = compileIds.split(",");
			Date auditTime=new Date();
			for(String id :idArray){
				try {
					avContentCfgService.auditPicUrl(isAudit,isValid,functionId,id,auditTime);
				} catch ( Exception e) {
					e.printStackTrace();
					logger.info("Pic Url配置下发失败",e);
					if(e instanceof MaatConvertException) {
						addMessage(redirectAttributes,"error", "request_service_failed");
					}else {
						addMessage(redirectAttributes,"error","audit_failed");
					}
				}
			}
			
		}
		return "redirect:" + adminPath +"/ntc/av/picUrlList?functionId="+functionId;
	}
	// 获取域配置信息
	@RequestMapping(value = {"ajaxAvPicUrlList"})
	public String ajaxAvPicUrlList(Model model,Long cfgId,Integer index) {
		BaseStringCfg baseStrCfg = new BaseStringCfg();
		baseStrCfg.setCfgId(cfgId);
		BaseStringCfg<NtcSubscribeIdCfg> cfg = avContentCfgService.getPicUrlCfgById(baseStrCfg);
		List<String[]> tabList = new ArrayList();
		
		tabList.add(new String[]{"2",cfg.getCfgType()});
		if(cfg.getNtcSubscribeIdCfgList()!=null){
			String cfgType = null;
			for(NtcSubscribeIdCfg subscribeId:cfg.getNtcSubscribeIdCfgList()){
				if(!subscribeId.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"2",subscribeId.getCfgType()});
					cfgType = subscribeId.getCfgType();
				}
			}
		}
		model.addAttribute("_cfg", cfg);	
		model.addAttribute("index", index);
		model.addAttribute("tabList", tabList);
		return "/cfg/av/subList";
	}
	/*****************************picUrl业务***************/
	/**
	 * 1、effectiveIds分组展示
	 * 2、无effectiveIds查询area_ip，根据compileId查询展示
	 */
	@ResponseBody
	@RequestMapping(value = "/area/ajaxAreaEffictiveInfo")
	public Map ajaxAreaEffictiveInfo(String areaEffectiveIds,Integer compileId) {
		Map<String, Object> data=new HashMap<String, Object>();
		
		List<AreaBean> areaIsps=new ArrayList<AreaBean>();
		List<AreaIpCfg> areaIps=new ArrayList<AreaIpCfg>();
		if(StringUtils.isEmpty(areaEffectiveIds)){
			areaIps=ipCfgService.getAreaCfgByCompileId(compileId);
		}else{
			List<Integer> itTypeList=new ArrayList<Integer>();
			itTypeList.add(Constants.ITEM_TYPE_AREA);
			List<SysDictInfo> areas = sysDictInfoService.findAllSysDictInfo(new SysDictInfo(),itTypeList,null);
			itTypeList.clear();
			itTypeList.add(Constants.ITEM_TYPE_ISP);
			List<SysDictInfo> isps = sysDictInfoService.findAllSysDictInfo(new SysDictInfo(),itTypeList,null);
			getAreaIsps(areaEffectiveIds, areaIsps, isps, areas);
			
		}
		data.put("areaIsps", areaIsps);
		data.put("areaIps", areaIps);
		return data;
	}
	//视频文本VOIP配置列表导出
	@RequestMapping(value = {"/voipExport"})
	public String voipExport(Model model,
			HttpServletRequest  request,
			HttpServletResponse response,
			@ModelAttribute("cfg")CfgIndexInfo entity,
			String ids,
			RedirectAttributes redirectAttributes){
		try {
			//获取国际化配置
			Properties msgProp = getMsgProp();
			//获取分类、性质、标签
			List<ServiceDictInfo> fls=serviceDictInfoService.findAllFlDict();
			List<ServiceDictInfo> xzs=serviceDictInfoService.findAllXzDict();
			List<ServiceDictInfo> labels=serviceDictInfoService.findAllLableDict();
			Map<Object, Object> map=new HashMap<Object, Object>();
			map.put("fls", fls);
			map.put("xzs", xzs);
			map.put("labels", labels);
			
            String fileName = "数据-"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            
            //导出选中记录
    		if(!StringUtil.isEmpty(ids)){
    			
    		}else{
    		//导出查询条件下的所有记录
    			
    			if(entity.getVoipIp()== null){
    				entity.setVoipIp(new AvVoipIpCfg());
    			}
    			if(entity.getVoipAccount()== null){
    				entity.setVoipAccount(new AvVoipAccountCfg());
    			}
    			//条件导出数据大于最大导出数，只导出最大导出条数
    			Page pageInfo=new Page<CfgIndexInfo>(request, response,"r");
    			if(pageInfo.getCount()>Constants.MAX_EXPORT_SIZE){
    				pageInfo.setPageNo(1);
    				pageInfo.setPageSize(Constants.MAX_EXPORT_SIZE);
    			}else{
    				pageInfo.setPageNo(1);
    				pageInfo.setPageSize(-1);
    			}
    			Page<CfgIndexInfo> page = avContentCfgService.findPage(pageInfo, entity);
    			new ExportExcel(msgProp,null, CfgIndexInfo.class,1).setDataList(msgProp,page.getList(),map).write(response, fileName).dispose();
    		}
            
    		return null;
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(redirectAttributes,"error", "export_failed");
		}
		
		return "redirect:" + adminPath +"/ntc/av/picUrlList?functionId="+entity.getFunctionId();
	}
	
	//stream配置导出
	@RequestMapping(value = "exportStream")
	public void exportStream(Model model,HttpServletRequest  request,HttpServletResponse response,
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
				Page<CfgIndexInfo> pageInfo=new Page<CfgIndexInfo>(request, response,"r");
				pageInfo.setPageNo(1);
				pageInfo.setPageSize(Constants.MAX_EXPORT_SIZE);
				if(entity.getIpPort()== null){
					entity.setIpPort(new IpPortCfg());
				}
				if(entity.getAvContUrlCfgList()== null){
					entity.setAvContUrlCfg(new AvContUrlCfg());;
				}
				if(entity.getNtcSubscribeIdCfg()==null){
					entity.setNtcSubscribeIdCfg(new NtcSubscribeIdCfg());
				}
				Page<CfgIndexInfo> page = avContentCfgService.findPage(pageInfo, entity);
				titleList.add(entity.getMenuNameCode());
				titleList.add("NTC_UNIVERSAL_IP");
				titleList.add("NTC_STREAMING_MEDIA_URL");								
				titleList.add("NTC_SUBSCRIBE_ID");
				classMap.put(entity.getMenuNameCode(), CfgIndexInfo.class);
				classMap.put("NTC_UNIVERSAL_IP", IpPortCfg.class);
				classMap.put("NTC_STREAMING_MEDIA_URL", BaseStringCfg.class);
				classMap.put("NTC_SUBSCRIBE_ID", NtcSubscribeIdCfg.class);
				
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
				noExportMap.put("NTC_UNIVERSAL_IP", ipPortInfoNoExport);
				noExportMap.put("NTC_STREAMING_MEDIA_URL", regionCfgNoExport);
				noExportMap.put("NTC_SUBSCRIBE_ID", ntcSubscribeIdCfgNoExport);
				List<IpPortCfg> ipList=new ArrayList<IpPortCfg>();
				List<BaseStringCfg> subscribeInfoList=new ArrayList<BaseStringCfg>();
				List<BaseStringCfg> streamurlList=new ArrayList<BaseStringCfg>();
				for (CfgIndexInfo cfg : page.getList()) {
					 Map<String, List> maps=avContentCfgService.exportstream(cfg);
					 ipList.addAll(maps.get("NTC_UNIVERSAL_IP"));
					 streamurlList.addAll(maps.get("NTC_STREAMING_MEDIA_URL"));
					 subscribeInfoList.addAll(maps.get("NTC_SUBSCRIBE_ID"));
				}
				subscribeInfoList=BaseStringCfg.baseHexList(subscribeInfoList);
				streamurlList=BaseStringCfg.replaceBaseKeyList(streamurlList);
				dataMap.put(entity.getMenuNameCode(), page.getList());
				dataMap.put("NTC_UNIVERSAL_IP", ipList);
				dataMap.put("NTC_STREAMING_MEDIA_URL", streamurlList);
				dataMap.put("NTC_SUBSCRIBE_ID", subscribeInfoList);
			/*}*/
			this._export(model, request, response, redirectAttributes,entity.getMenuNameCode(),titleList,classMap,dataMap,noExportMap);
		} catch (Exception e) {
			logger.error("stream export failed",e);
			addMessage(redirectAttributes,"error",  "export_failed");
		}
		//return "redirect:" + adminPath +"/ntc/iplist/list?functionId="+entity.getFunctionId();
	}
	
	//voip配置导出
		@RequestMapping(value = "exportVoip")
		public void exportVoip(Model model,HttpServletRequest  request,HttpServletResponse response,
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
					Page<CfgIndexInfo> pageInfo=new Page<CfgIndexInfo>(request, response,"r");
					pageInfo.setPageNo(1);
					pageInfo.setPageSize(Constants.MAX_EXPORT_SIZE);
					if(entity.getIpPort()== null){
						entity.setIpPort(new IpPortCfg());
					}
					if(entity.getVoipAccount()== null){
						entity.setVoipAccount(new AvVoipAccountCfg());
					}
					if(entity.getNtcSubscribeIdCfg()==null){
						entity.setNtcSubscribeIdCfg(new NtcSubscribeIdCfg());
					}
					Page<CfgIndexInfo> page = avContentCfgService.findPage(new Page<CfgIndexInfo>(request, response,"r"), entity);
					titleList.add(entity.getMenuNameCode());
					titleList.add("NTC_UNIVERSAL_IP");
					titleList.add("NTC_VOIP_ACCOUNT");								
					titleList.add("NTC_SUBSCRIBE_ID");
					classMap.put(entity.getMenuNameCode(), CfgIndexInfo.class);
					classMap.put("NTC_UNIVERSAL_IP", IpPortCfg.class);
					classMap.put("NTC_VOIP_ACCOUNT", BaseStringCfg.class);
					classMap.put("NTC_SUBSCRIBE_ID", NtcSubscribeIdCfg.class);
					
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
					noExportMap.put("NTC_UNIVERSAL_IP", ipPortInfoNoExport);
					noExportMap.put("NTC_VOIP_ACCOUNT", regionCfgNoExport);
					noExportMap.put("NTC_SUBSCRIBE_ID", ntcSubscribeIdCfgNoExport);
					List<IpPortCfg> ipList=new ArrayList<IpPortCfg>();
					List<BaseStringCfg> subscribeInfoList=new ArrayList<BaseStringCfg>();
					List<BaseStringCfg> countlList=new ArrayList<BaseStringCfg>();
					for (CfgIndexInfo cfg : page.getList()) {
						 Map<String, List> maps=avContentCfgService.exportvoip(cfg);
						 ipList.addAll(maps.get("NTC_UNIVERSAL_IP"));
						 countlList.addAll(maps.get("NTC_VOIP_ACCOUNT"));
						 subscribeInfoList.addAll(maps.get("NTC_SUBSCRIBE_ID"));
					}
					countlList=BaseStringCfg.replaceBaseKeyList(countlList);
					subscribeInfoList=BaseStringCfg.baseHexList(subscribeInfoList);
					dataMap.put(entity.getMenuNameCode(), page.getList());
					dataMap.put("NTC_UNIVERSAL_IP", ipList);
					dataMap.put("NTC_VOIP_ACCOUNT", countlList);
					dataMap.put("NTC_SUBSCRIBE_ID", subscribeInfoList);
				/*}*/
				this._export(model, request, response, redirectAttributes,entity.getMenuNameCode(),titleList,classMap,dataMap,noExportMap);
			} catch (Exception e) {
				logger.error("voip export failed",e);
				addMessage(redirectAttributes,"error",  "export_failed");
			}
			//return "redirect:" + adminPath +"/ntc/iplist/list?functionId="+entity.getFunctionId();
		}
}
