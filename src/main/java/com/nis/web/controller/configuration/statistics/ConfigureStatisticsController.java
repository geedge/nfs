package com.nis.web.controller.configuration.statistics;




import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mchange.v1.util.MapUtils;
import com.nis.domain.FunctionServiceDict;
import com.nis.domain.Page;
import com.nis.domain.SysArea;
import com.nis.domain.SysMenu;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.RequestInfo;
import com.nis.util.CacheUtils;
import com.nis.util.Constants;
import com.nis.util.DictUtils;
import com.nis.util.StringUtil;
import com.nis.util.StringUtils;
import com.nis.web.controller.BaseController;
import com.nis.web.security.UserUtils;
import com.nis.web.service.BaseService;
import com.nis.web.service.CommonService;

/**
 * 配置统计
 * @author ddm
 *
 */
@Controller
@RequestMapping("${adminPath}/configure/statistics")
public class ConfigureStatisticsController extends BaseController{
	
	
	@RequestMapping(value = {"/configureStateStatistics"})
	public String configStateStatistics(Model model,HttpServletRequest request
			,HttpServletResponse response
			,RedirectAttributes redirectAttributes){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		/****************************Request Info Statistics*****************************/
		//1、查询所有有效的service
		List<FunctionServiceDict> serviceDictList = DictUtils.getFunctionServiceDictList();
		//2、查询所有菜单
		List<SysMenu> menuList = UserUtils.getMenuCfgList();
		//3、根据来函分页
		RequestInfo requestInfo=new RequestInfo();
		requestInfo.setIsAudit(1);
		requestInfo.setIsValid(1);
		Page requestPage = requestInfoService.findRequestInfo(new Page(request, response,"r"),requestInfo);
		List<RequestInfo> requestInfos=requestPage.getList();
		//4、根据当前页的requestInfo信息查询request统计信息
		List<Object[]> requestStatisticList=new ArrayList<Object[]>();
		if(!StringUtil.isEmpty(requestInfos)){
			Date requestStatisticTime=configureStatisticsService.getRequestStatisticTime();
			String requestStatisticTimeStr="";
			if(!StringUtil.isEmpty(requestStatisticTime)){
				requestStatisticTimeStr=sdf.format(requestStatisticTime);
			}
			requestStatisticList=configureStatisticsService.getRequestStateStatistics(requestInfos,menuList,requestStatisticTimeStr);
			if(!StringUtil.isEmpty(requestStatisticList)){
					for (Iterator iterator = requestStatisticList.iterator(); iterator.hasNext();) {
						Map map = (Map) iterator.next();
						for (RequestInfo requestInfoO : requestInfos) {
							if(requestInfoO.getId().toString().equals(map.get("request").toString())){
								map.put("request", requestInfoO.getRequestTitle());
							}
						}
					}
			}
			model.addAttribute("requestStatisticTime", requestStatisticTimeStr);
		}
		requestPage.setList(requestStatisticList);
		model.addAttribute("requestInfos", requestInfos);
		model.addAttribute("serviceList", serviceDictList);
		
		model.addAttribute("page", requestPage);
		/****************************Config Status Info Statistics*****************************/
		Date cfgStatisticTime=configureStatisticsService.getConfigStatisticTime();
		String cfgStatisticTimeStr="";
		if(!StringUtil.isEmpty(cfgStatisticTime)){
			cfgStatisticTimeStr=sdf.format(cfgStatisticTime);
		}
		List<Object[]> list = configureStatisticsService.getConfigStateStatistics(cfgStatisticTimeStr);
		model.addAttribute("configStatistics", list);
		model.addAttribute("configStatisticTime", cfgStatisticTimeStr);
		return "/index";
	}
	
	/**
	 * 根据业务ID查询当前业务配置是否可继续新增，如果达到规则限制量则不允许新增。
	 * @param serviceId
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getEffectiveCfgNum")
	public Map getEffectiveCfgNum(Integer serviceId, HttpServletResponse response) {
		Map map= new HashMap();
		Integer cfgNum = configureStatisticsService.getEffectiveCfgNum(serviceId);
		String dictValue = DictUtils.getDictLabel("SERVICE_RULE_LIMIT", serviceId+"","default").trim();
		Integer ruleLimit = 0;
		if(!"default".equals(dictValue)){
			ruleLimit = Integer.parseInt(dictValue);
		}
		if(ruleLimit==0){
			map.put("status", 1);//如果为0说明没有限制
		}else if(cfgNum<ruleLimit){
			map.put("status", 1);//当前配置量小于限制
		}else{
			map.put("status", 0);//当前配置量大于或等于限制，则不允许添加
			map.put("msg", String.format(this.getMsgProp().getProperty("ruleLimitTip"),ruleLimit));
		}
		map.put("cfgNum", cfgNum);				
		map.put("ruleLimit", ruleLimit);		
		return map;
	}
}
