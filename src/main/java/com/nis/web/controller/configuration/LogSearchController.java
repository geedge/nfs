package com.nis.web.controller.configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nis.domain.log.BaseLogEntity;
import com.nis.web.controller.BaseController;

@Controller
@RequestMapping("${adminPath}/toLogSearch")
public class LogSearchController extends BaseController{
	
	@RequestMapping(value = {"list",""})
	public String LogSearch(BaseLogEntity<Object> entity, RedirectAttributes attr, HttpServletRequest request, 
			HttpServletResponse response) {
		/**
		 * url: logUrl
		 * searchCfgId: compileId
		 * searchService: serviceId
		 */
		// 获取相应日志检索菜单URL
		String logUrl = menuService.getLogUrl(entity.getFunctionId());
//		Integer serviceId = menuService.getServiceId(entity.getFunctionId(),entity.getAction());
		
		attr.addAttribute("service", entity.getService().intValue());
		attr.addAttribute("cfgId", entity.getCfgId());
		attr.addAttribute("functionId", entity.getFunctionId());
		attr.addAttribute("date", entity.getDate());
		attr.addAttribute("isLogTotalSearch", entity.getIsLogTotalSearch());
		return "redirect:"+adminPath+logUrl;
	}
}
