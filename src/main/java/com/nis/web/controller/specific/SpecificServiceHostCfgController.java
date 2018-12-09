package com.nis.web.controller.specific;

import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.nis.domain.ImportErrorInfo;
import com.nis.domain.Page;
import com.nis.domain.specific.SpecificServiceCfg;
import com.nis.domain.specific.SpecificServiceHostCfg;
import com.nis.util.Configurations;
import com.nis.util.StringUtil;
import com.nis.util.excel.ExportExcel;
import com.nis.util.excel.ImportExcel;
import com.nis.web.controller.BaseController;

@Controller
@RequestMapping(value = "${adminPath}/specific/specificServiceHostCfg")
public class SpecificServiceHostCfgController extends BaseController {
	@ModelAttribute
	public SpecificServiceHostCfg get(@RequestParam(required=false) Integer hostId) {
		if (!StringUtil.isEmpty(hostId)){
			return specificServiceHostCfgService.getDictByHostId(hostId);
		}else{
			return new SpecificServiceHostCfg();
		}
	}
	
	/**
	 * 分页查询
	 * @param specificServiceHostCfg
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("specific:serviceIp:view")
	@RequestMapping(value = "list")
	public String list(SpecificServiceHostCfg specificServiceHostCfg, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		
		//查出分页数据
		Page<SpecificServiceHostCfg> page = specificServiceHostCfgService.findSpecHostList(new Page<SpecificServiceHostCfg>(request, response), specificServiceHostCfg);
        model.addAttribute("page", page);
        //查询特定服务管理（specific_service_cfg）协议id供下拉选择
        List<SpecificServiceCfg> listSpecService = specificServiceCfgService.findAllSpecificServiceCfg(new SpecificServiceCfg(), "spec_service_id DESC");
        model.addAttribute("listSpecService", listSpecService);
		return "/specific/specificServiceHostCfgList";
	}
	
	/**
	 * 进入查看、修改或新增页面
	 * @param specificServiceHostCfg
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("specific:serviceIp:edit")
	@RequestMapping(value = "form")
	public String form(SpecificServiceHostCfg specificServiceHostCfg, HttpServletRequest request, HttpServletResponse response, Model model) {
		//查询协议id供下拉选择
        List<SpecificServiceCfg> listSpecService = specificServiceCfgService.findAllSpecificServiceCfg(new SpecificServiceCfg(), "spec_service_id DESC");
        model.addAttribute("listSpecService", listSpecService);
		return "/specific/specificServiceHostCfgForm";
	}
	
	/**
	 * 新增或修改
	 * @param specificServiceHostCfg
	 * @param model
	 * @param redirectAttributes
	 * @param mulitId
	 * @return
	 */
	@RequiresPermissions("specific:serviceIp:edit")
	@RequestMapping(value = "saveOrUpdate")
	public String saveOrUpdate(SpecificServiceHostCfg specificServiceHostCfg,Model model,RedirectAttributes redirectAttributes){
		try {
			specificServiceHostCfgService.saveOrUpdate(specificServiceHostCfg);
			addMessage(redirectAttributes, "save_success");
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(redirectAttributes, "save_failed");
		}
		return "redirect:"+adminPath+"/specific/specificServiceHostCfg/list";
	}
	/**
	 * 删除
	 * @param specificServiceHostCfg
	 * @param redirectAttributes
	 * @param mulitId
	 * @return
	 */
	@RequiresPermissions("specific:serviceIp:edit")
	@RequestMapping(value="delete")
	public String delete(SpecificServiceHostCfg specificServiceHostCfg, RedirectAttributes redirectAttributes, String mulitId){
		try{
			specificServiceHostCfgService.delete(mulitId);
			addMessage(redirectAttributes,"delete_success");
		}catch (Exception e){
			e.printStackTrace();
			addMessage(redirectAttributes,"delete_failed");
		}
		
		return "redirect:"+adminPath+"/specific/specificServiceHostCfg/list";
	}
	@RequiresPermissions("specific:serviceIp:edit")
	@RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletRequest request,HttpServletResponse response, RedirectAttributes redirectAttributes) {
		Properties msgProp = new Properties();
		try {
			String language = LocaleContextHolder.getLocale().getLanguage();
			if(language.equals("zh_cn")||language.equals("zh")){
				msgProp.load(Configurations.class.getResourceAsStream("/messages/message_zh_CN.properties"));
			}else if(language.equals("ru")){
				msgProp.load(Configurations.class.getResourceAsStream("/messages/message_ru.properties"));
			}else{
				msgProp.load(Configurations.class.getResourceAsStream("/messages/message_en.properties"));
			}
			
		} catch (Exception e) {
			msgProp = null;
			System.err.println("未知i18n消息配置文件,请确定文件是否存在!");
		}
		try {
	        String fileName = msgProp.getProperty("agreement_ip_configuration").replaceAll(" ", "_")+".xlsx";
	        List<SpecificServiceHostCfg> list = Lists.newArrayList();
	        list.add(new SpecificServiceHostCfg());
	        new ExportExcel(msgProp,msgProp.getProperty("agreement_ip_configuration"), SpecificServiceHostCfg.class, 2).setDataList(msgProp,list,null).
	        	write(request,response, fileName).dispose();
	        return null;
	    } catch (Exception e) {
	    	addMessage(redirectAttributes, msgProp.getProperty("import_template_failed")+e.getMessage());
	    }
	    return "redirect:"+adminPath+"/specific/specificServiceHostCfg/list";
	}
	/*@RequiresPermissions("specific:serviceIp:edit")
	@RequestMapping(value = "import", method=RequestMethod.POST)
	public String importFile(HttpServletRequest request,Model model,HttpServletResponse response, RedirectAttributes redirectAttributes,
			@RequestParam("file") MultipartFile file) {
		Properties msgProp = new Properties();
		try {
			String language = LocaleContextHolder.getLocale().getLanguage();
			if(language.equals("zh_cn")||language.equals("zh")){
				msgProp.load(Configurations.class.getResourceAsStream("/messages/message_zh_CN.properties"));
			}else if(language.equals("ru")){
				msgProp.load(Configurations.class.getResourceAsStream("/messages/message_ru.properties"));
			}else{
				msgProp.load(Configurations.class.getResourceAsStream("/messages/message_en.properties"));
			}
			
		} catch (Exception e) {
			msgProp = null;
			System.err.println("未知i18n消息配置文件,请确定文件是否存在!");
		}
		 List<ImportErrorInfo> errorInfos = null;
		try {
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<SpecificServiceHostCfg> list = ei.getDataList(SpecificServiceHostCfg.class);
			//查询特定服务管理（specific_service_cfg）协议id供下拉选择
	        List<SpecificServiceCfg> listSpecService = specificServiceCfgService.findAllSpecificServiceCfg(new SpecificServiceCfg(), "spec_service_id DESC");
			
	        errorInfos = specificServiceHostCfgService.importFile(list,listSpecService,msgProp);
//			if(result){
//				addMessage(redirectAttributes,  msgProp.getProperty("import_success"));
//			}else{
//				addMessage(redirectAttributes,  msgProp.getProperty("import_error"));
//			}
		} catch (Exception e) {
			addMessage(redirectAttributes,  msgProp.getProperty("import_failed")+errorInfos);
			e.printStackTrace();
		}
		if(errorInfos!=null && errorInfos.size()>0){
			addMessage(redirectAttributes,  msgProp.getProperty("import_failed")+errorInfos);
		}else{
			addMessage(redirectAttributes,  "import_success");
		}
		return "redirect:"+adminPath+"/specific/specificServiceHostCfg/list";
//		return renderString(response,errorInfos);
	}*/
	/**
	 * 校验spec_service_id重复
	 * @param newId
	 * @param oldId
	 */
/*	@ResponseBody
	@RequestMapping(value = "isSpecServiceIdRepeat")
	public boolean isSpecServiceIdRepeat(String newId,String oldId){
		if(oldId!=null){
			oldId.trim().equals(newId);
			return true;
		}else{
			SpecificServiceHostCfg sshc = specificServiceHostCfgService.getBySpecServiceId(Integer.valueOf(newId));
			if(sshc==null){
				return true;
			}
		}
		return false;
	}*/
}
