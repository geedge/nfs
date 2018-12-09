package com.nis.web.controller.dashboard;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.beust.jcommander.internal.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.nis.domain.PageLog;
import com.nis.domain.SysDataDictionaryItem;
import com.nis.domain.configuration.WebsiteDomainTopic;
import com.nis.domain.dashboard.TrafficIpActiveStatistic;
import com.nis.util.CodeDicUtils;
import com.nis.util.Constants;
import com.nis.util.DateUtils;
import com.nis.util.DictUtils;
import com.nis.util.StringUtil;
import com.nis.util.excel.ExportExcel;
import com.nis.util.httpclient.HttpClientUtil;
import com.nis.web.controller.BaseController;
import com.nis.web.dao.dashboard.codedic.CodeResult;
@SuppressWarnings("rawtypes")
@Controller
@RequestMapping(value = "${adminPath}/dashboard")
public class DashboardController extends BaseController{
	
	
	/**
	 * 跳转到图表界面
	 * @return
	 */
	@RequestMapping(value="logChart")
	public String logChart(){
		
		return "/dashboard/dashBoardIndex";
	}

	/**
	 * 活跃端口 TOP10
	 */
	@RequestMapping(value="portActive")
	@ResponseBody
	public List portActive(){
		Map<String, Object> fromJsonList = new HashMap<String, Object>();
		List list = new ArrayList();
		try {
			String string = HttpClientUtil.get(Constants.DASHBOARD_URL+Constants.TRAFFIC_PORT_ACTIVE);
			Gson gson = new GsonBuilder().create();
			fromJsonList = gson.fromJson(string, new TypeToken<Map>(){}.getType());
			list = (ArrayList) fromJsonList.get("data");
			logger.info("活跃端口数据"+fromJsonList);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("活跃端口数据错误"+e);
			list.add(Maps.newHashMap("error","request_service_failed"));
		}
		return list;
	}
	/**
	 * 活跃IP TOP10
	 */
	@RequestMapping(value="ipActive")
	@ResponseBody
	public List ipActive(){
		Map<String, Object> fromJsonList = new HashMap<String, Object>();
		List list = new ArrayList();
		try {
			String string = HttpClientUtil.get(Constants.DASHBOARD_URL+Constants.TRAFFIC_IPACTIVE);
			Gson gson = new GsonBuilder().create();
			fromJsonList = gson.fromJson(string, new TypeToken<Map>(){}.getType());
			logger.info("活跃IP数据"+fromJsonList);
				list = (ArrayList) fromJsonList.get("data");
				return list;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("活跃IP错误"+e);
			list.add(Maps.newHashMap("error","request_service_failed"));
		}
		return list;
	}
	/**
	 * 活跃IP TOP10 一小时 间隔5分钟数据
	 */
	@RequestMapping(value="ipActiveMinuteList")
	@ResponseBody
	public List ipActiveMinuteList(){
		Map<String, Object> fromJsonList = new HashMap<String, Object>();
		List list = new ArrayList();
		try {
			String string = HttpClientUtil.get(Constants.DASHBOARD_URL+Constants.TRAFFIC_IPACTIVE_FIVEMINUTE);
			Gson gson = new GsonBuilder().create();
			fromJsonList = gson.fromJson(string, new TypeToken<Map>(){}.getType());
			logger.info("活跃IP1小时,间隔5分钟数据"+fromJsonList);
			list = (ArrayList) fromJsonList.get("data");
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("活跃IP数据获取错误"+e);
			list.add(Maps.newHashMap("error","request_service_failed"));
		}
		return list;
	}
	/**
	 * 活跃IP详情
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	
	  public static Gson getIntGson() {
	       Gson gson = new GsonBuilder()
	                .registerTypeAdapter(
	                        new TypeToken<Map<String, Object>>() {
	                        }.getType(),
	                        new JsonDeserializer<Map<String, Object>>() {
	                            @Override
	                            public Map<String, Object> deserialize(
	                                    JsonElement json, Type typeOfT,
	                                    JsonDeserializationContext context) throws JsonParseException {
	 
	                                Map<String, Object> treeMap = new HashMap<String, Object>();
	                                JsonObject jsonObject = json.getAsJsonObject();
	                                Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
	                                for (Map.Entry<String, JsonElement> entry : entrySet) {
	                                    treeMap.put(entry.getKey(), entry.getValue());
	                                }
	                                return treeMap;
	                            }
	                        }).create();
	        return gson;
	    }
	@RequestMapping(value="ipActiveList")
	public String ipActiveList(  HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes){
		Map<String, Object> fromJsonList = new HashMap<String, Object>();
		List<TrafficIpActiveStatistic> ipActiveList = new ArrayList<TrafficIpActiveStatistic>();
			try {
			String string = HttpClientUtil.get(Constants.DASHBOARD_URL+Constants.TRAFFIC_IPACTIVE_ONEHOUR);
				
				Gson gson = getIntGson();
				fromJsonList = gson.fromJson(string, new TypeToken<Map<String, Object>>(){}.getType());
				logger.info("活跃IP1小时"+fromJsonList);
				ipActiveList = gson.fromJson(fromJsonList.get("data").toString(), new TypeToken<List<TrafficIpActiveStatistic>>() {}.getType()) ;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("活跃IP统计错误"+e);
				addMessage(redirectAttributes,"request_service_failed");
			}
		
        model.addAttribute("ipActiveList", ipActiveList);
		return "/dashboard/trafficIpActiveList";
	}
	
	//IP活跃导出
	@RequestMapping(value = "export")
	public void exportddos(Model model,HttpServletRequest request,HttpServletResponse response,
			@ModelAttribute("entry")TrafficIpActiveStatistic entity,String ids,RedirectAttributes redirectAttributes){
		try {
			//export data info
			List<String> titleList=new ArrayList<String>();
			Map<String, Class<?>> classMap=new HashMap<String, Class<?>>();
			Map<String, List> dataMap=new HashMap<String, List>();
			Map<String, String> noExportMap=new HashMap<String, String>();
			Map<String, Object> fromJsonList = new HashMap<String, Object>();
			List<TrafficIpActiveStatistic> ipActiveList = new ArrayList<TrafficIpActiveStatistic>();
				String string = HttpClientUtil.get(Constants.DASHBOARD_URL+Constants.TRAFFIC_IPACTIVE_ONEHOUR);
					
					Gson gson = getIntGson();
					fromJsonList = gson.fromJson(string, new TypeToken<Map<String, Object>>(){}.getType());
					ipActiveList = gson.fromJson(fromJsonList.get("data").toString(), new TypeToken<List<TrafficIpActiveStatistic>>() {}.getType()) ;
					
				titleList.add("traffic_ipactive_hour_max");
				classMap.put(null, TrafficIpActiveStatistic.class);
				String cfgIndexInfoNoExport=",id,";
				noExportMap.put("traffic_ipactive_hour_max",cfgIndexInfoNoExport);
				dataMap.put(null, ipActiveList);
			
			
			// 获取国际化配置
			Properties msgProp = getMsgProp();
			Map<Object, Object> map = new HashMap<Object, Object>();

			String fileName = "IP-Active-Hour-Max" + "-" + DateUtils.getDate("yyyyMMddHHmmss")
					+ ".xlsx";
			new ExportExcel("", TrafficIpActiveStatistic.class, 1).setDataList(msgProp,ipActiveList,new HashMap())
					.write(request,response, fileName).dispose();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("IPActive export failed",e);
			addMessage(redirectAttributes,"error","export_failed");
		}
//		return "/dashboard/trafficIpActiveList";
	}
	/**
	 * 协议统计
	 */
	@RequestMapping(value="protocol")
	@ResponseBody
	public List protocolChart(){
		Map<String, Object> fromJsonList = new HashMap<String, Object>();
		List<Map> list = new ArrayList<Map>();
		List<Map> resultList = new ArrayList<Map>();
		try {
			String string = HttpClientUtil.get(Constants.DASHBOARD_URL+Constants.TRAFFIC_PROTOCOL);
			Gson gson = new GsonBuilder().create();
			fromJsonList = gson.fromJson(string, new TypeToken<Map>(){}.getType());
			list = (List<Map>) fromJsonList.get("data");
			//标签集合
			List<CodeResult> codeList = CodeDicUtils.getCodeList("protocolCode");
			//将数字替换为标签文字
			if(list!=null&&!list.isEmpty()){
			for (Map map : list) {
				Double value1 = Double.parseDouble(map.get("protoType").toString());
				for (CodeResult code : codeList) {
					Double value2 = Double.valueOf(code.getCode());
					if(value1.equals(value2)){
						map.put("protoType", code.getItem());
						break;
					}
				}
				resultList.add(map);
			}
			logger.info("协议统计"+fromJsonList);
			return resultList;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("协议统计错误"+e);
			resultList.add(Maps.newHashMap("error","request_service_failed"));
		}
		return resultList;
	}
	/**
	 * app流量分析
	 */
	@RequestMapping(value="app")
	@ResponseBody
	public List appChart(){
		Map<String, Object> fromJsonList = new HashMap<String, Object>();
		List<Map> list = new ArrayList<Map>();
		List<Map> resultList = new ArrayList<Map>();
		try {
			String string = HttpClientUtil.get(Constants.DASHBOARD_URL+Constants.TRAFFIC_APP);
			Gson gson = new GsonBuilder().create();
			fromJsonList = gson.fromJson(string, new TypeToken<Map>(){}.getType());
			list = (List<Map>) fromJsonList.get("data");
			//标签集合
			List<CodeResult> codeList = CodeDicUtils.getCodeList("appCode");
			//将数字替换为标签文字
			if(list!=null&&!list.isEmpty()){
			for (Map map : list) {
				Double value1 = Double.parseDouble(map.get("appType").toString());
				for (CodeResult code : codeList) {
					Double value2 = Double.valueOf(code.getCode());
					if(value1.equals(value2)){
						map.put("appType", code.getItem());
						break;
					}
				}
				resultList.add(map);
			}
			logger.info("app流量分析"+fromJsonList);
			return resultList;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("app流量分析错误"+e);
			resultList.add(Maps.newHashMap("error","request_service_failed"));
		}
		return resultList;
	}
	/**
	 * 终端用户-操作系统列表
	 */
	@RequestMapping(value="osList")
	@ResponseBody
	public List osList(){
		Map<String, Object> fromJsonList = new HashMap<String, Object>();
		List<Map> list = new ArrayList<Map>();
		List<Map> resultList = new ArrayList<Map>();
		try {
			String string = HttpClientUtil.get(Constants.DASHBOARD_URL+Constants.TRAFFIC_OSLIST);
			Gson gson = new GsonBuilder().create();
			fromJsonList = gson.fromJson(string, new TypeToken<Map>(){}.getType());
			list = (List<Map>) fromJsonList.get("data");
			//标签集合
			List<CodeResult> codeList = CodeDicUtils.getCodeList("osCode");
			//将数字替换为标签文字
			if(list!=null&&!list.isEmpty()){
			for (Map map : list) {
				Double value1 = Double.parseDouble(map.get("osType").toString());
				for (CodeResult code : codeList) {
					Double value2 = Double.valueOf(code.getCode());
					if(value1.equals(value2)){
						map.put("osType", code.getItem());
						break;
					}
				}
				resultList.add(map);
			}
			logger.info("终端用户-获取操作系统列表"+fromJsonList);
			return resultList;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("终端用户-获取操作系统列表错误"+e);
			resultList.add(Maps.newHashMap("error","request_service_failed"));
		}
		return resultList;
	}
	/**
	 * 终端用户-指定操作系统下浏览器分类流量统计
	 */
	@RequestMapping(value="browserChart")
	@ResponseBody
	public List browserChart(@RequestParam("osType") String osType){
		Map<String, Object> fromJsonList = new HashMap<String, Object>();
		List<Map> list = new ArrayList<Map>();
		List<Map> resultList = new ArrayList<Map>();
		//标签集合
		List<CodeResult> codeList1 = CodeDicUtils.getCodeList("osCode");
		//将标签文字转为数字
		Integer os=null;
		for (CodeResult codeResult : codeList1) {
			if(osType.equalsIgnoreCase(codeResult.getItem())){
				os=Integer.parseInt(codeResult.getCode());
				break;
			}
		}
		if(os==null){
			os=Integer.parseInt(osType);//处理为匹配项编码
		}
		try {
			String string = HttpClientUtil.get(Constants.DASHBOARD_URL+Constants.TRAFFIC_BROWSER_CHART+"?osType="+os);
			Gson gson = new GsonBuilder().create();
			fromJsonList = gson.fromJson(string, new TypeToken<Map>(){}.getType());
			list = (List<Map>) fromJsonList.get("data");
			//标签集合
			List<CodeResult> codeList2 = CodeDicUtils.getCodeList("browserCode");
			//将数字替换为标签文字
			if(null!=list&&!list.isEmpty()){
				for (Map map : list) {
					Double value1 = Double.parseDouble(map.get("bsType").toString());
					for (CodeResult code : codeList2) {
						Double value2 = Double.valueOf(code.getCode());
						if(value1.equals(value2)){
							map.put("bsType", code.getItem());
							break;
						}
						//10个域名之外的为others  设为-1外
						if(map.get("bsType").toString().equals("-1")){
							map.put("bsType", "Others");
							break;
						}
					}
					resultList.add(map);
				}
			}
			logger.info("终端用户-获取操作系统下浏览器类型"+fromJsonList);
			return resultList;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("终端用户-获取操作系统下浏览器类型错误"+e);
			resultList.add(Maps.newHashMap("error","request_service_failed"));
		}
		return resultList;
	}
	/**
	 * 终端用户-浏览器列表
	 */
	@RequestMapping(value="bsList")
	@ResponseBody
	public List bsList(){
		Map<String, Object> fromJsonList = new HashMap<String, Object>();
		List<Map> list = new ArrayList<Map>();
		List<Map> resultList = new ArrayList<Map>();
		try {
			String string = HttpClientUtil.get(Constants.DASHBOARD_URL+Constants.TRAFFIC_BSLISTL);
			Gson gson = new GsonBuilder().create();
			fromJsonList = gson.fromJson(string, new TypeToken<Map>(){}.getType());
			list = (List<Map>) fromJsonList.get("data");
			//标签集合
			List<CodeResult> codeList = CodeDicUtils.getCodeList("browserCode");
			//将数字替换为标签文字
			if(list!=null&&!list.isEmpty()){
			for (Map map : list) {
				Double value1 = Double.parseDouble(map.get("bsType").toString());
				for (CodeResult code : codeList) {
					Double value2 = Double.valueOf(code.getCode());
					if(value1.equals(value2)){
						map.put("bsType", code.getItem());
						break;
					}
				}
				resultList.add(map);
				}
			}
			logger.info("终端用户-获取浏览器列表"+fromJsonList);
			return resultList;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("终端用户-获取浏览器列表错误"+e);
			resultList.add(Maps.newHashMap("error","request_service_failed"));
		}
		return resultList;
	}
	/**
	 * 终端用户-指定操作系统下浏览器分类流量统计
	 */
	@RequestMapping(value="systemChart")
	@ResponseBody
	public List systemChart(@RequestParam("bsType") String bsType){
		Map<String, Object> fromJsonList = new HashMap<String, Object>();
		List<Map> list = new ArrayList<Map>();
		List<Map> resultList = new ArrayList<Map>();
		//标签集合
		List<CodeResult> codeList1 = CodeDicUtils.getCodeList("browserCode");
		//将标签文字转为数字
		Integer bs=null;
		for (CodeResult codeResult : codeList1) {
			if(bsType.equalsIgnoreCase(codeResult.getItem())){
				bs=Integer.parseInt(codeResult.getCode());
				break;
			}
		}
		if(bs==null){
			bs=Integer.parseInt(bsType);//处理为匹配项编码
		}
		try {
			String string = HttpClientUtil.get(Constants.DASHBOARD_URL+Constants.TRAFFIC_SYSTEM_CHART+"?bsType="+bs);
			Gson gson = new GsonBuilder().create();
			fromJsonList = gson.fromJson(string, new TypeToken<Map>(){}.getType());
			list = (List<Map>) fromJsonList.get("data");
			//标签集合
			List<CodeResult> codeList2 = CodeDicUtils.getCodeList("osCode");
			//将数字替换为标签文字
			if(null!=list&&!list.isEmpty()){
				for (Map map : list) {
					Double value1 = Double.parseDouble(map.get("osType").toString());
					for (CodeResult code : codeList2) {
						Double value2 = Double.valueOf(code.getCode());
						//10个域名之外的为others webId设为-1外
						if(map.get("osType").toString().equals("-1")){
							map.put("osType", "Others");
							break;
						}
						if(value1.equals(value2)){
							map.put("osType", code.getItem());
							break;
						}
					}
					resultList.add(map);
				}
			}
			logger.info("终端用户-获取浏览器下操作系统类型"+fromJsonList);
			return resultList;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("终端用户-获取浏览器下操作系统类型错误"+e);
			resultList.add(Maps.newHashMap("error","request_service_failed"));
		}
		return resultList;
	}
	/**
	 * http网站统计
	 */
	@RequestMapping(value="websiteList")
	@ResponseBody
	public List websiteList(){
		Map<String, Object> fromJsonList = new HashMap<String, Object>();
		List<Map> list = new ArrayList<Map>();
		List<Map> resultList = new ArrayList<Map>();
		try {
			String string = HttpClientUtil.get(Constants.DASHBOARD_URL+Constants.TRAFFIC_WEBSITELIST);
			Gson gson = new GsonBuilder().create();
			fromJsonList = gson.fromJson(string, new TypeToken<Map>(){}.getType());
			list = (List<Map>) fromJsonList.get("data");
			//标签集合
//			List<CodeResult> codeList = CodeDicUtils.getCodeList("webCode");
			List<SysDataDictionaryItem> codeList = DictUtils.getDictList("WEBSITE_SERVER");
			//将数字替换为标签文字
			if(list!=null&&!list.isEmpty()){
				for (Map map : list) {
					Double value1=0d;
					if(StringUtil.isBlank(map.get("websiteServiceId").toString())){
						value1=268435455d;
					}else{
						value1 = Double.parseDouble(map.get("websiteServiceId").toString());
					}
					for (SysDataDictionaryItem code : codeList) {
						Double value2 = Double.valueOf(code.getItemCode());
						if(value1.equals(value2)){
							map.put("websiteService", code.getItemValue());
							break;
						}
					}
					resultList.add(map);
				}
			}
			logger.info("http网站流量分析数据："+fromJsonList);
			return resultList;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("http网站流量分析数据错误："+e);
			resultList.add(Maps.newHashMap("error","request_service_failed"));
		}
		return resultList;
	}
	/**
	 * http网站-某个网站的子域名统计
	 */
	@RequestMapping(value="webTypeChart")
	@ResponseBody
	public List webTypeChart(@RequestParam("websiteServiceId") String websiteServiceId){
		Map<String, Object> fromJsonList = new HashMap<String, Object>();
		List<Map> list = new ArrayList<Map>();
		List<Map> resultList = new ArrayList<Map>();
		try {
			String string = HttpClientUtil.get(Constants.DASHBOARD_URL+Constants.TRAFFIC_WEBTYPECHART+"?websiteServiceId="+websiteServiceId);
			Gson gson = new GsonBuilder().create();
			fromJsonList = gson.fromJson(string, new TypeToken<Map>(){}.getType());
			list = (List<Map>) fromJsonList.get("data");
			//标签集合
//			List<CodeResult> codeList2 = CodeDicUtils.getCodeList("webCode");//网站子域名表获取域名domain
			List<WebsiteDomainTopic> domainList = appCfgService.getDomainDict(new WebsiteDomainTopic());
			if(list!=null&&list.size()>0&&domainList!=null&&domainList.size()>0){
				for (Map dataMap : list) {
				for (WebsiteDomainTopic wdt : domainList) {
//					Map<String, Object> map = Maps.newHashMap();
					if(wdt!=null){
						String domain = wdt.getDomain();
						Double domainId = wdt.getId().doubleValue();
							Double webId = Double.parseDouble(dataMap.get("webId").toString());
							//10个域名之外的为others webId设为-1外
							if(dataMap.get("webId").toString().equals("-1")){
								dataMap.put("domain", "Others");
								break;
							}
							if(domainId.equals(webId)){
								dataMap.put("domain", domain);
								break;
							}
						}
					}
				resultList.add(dataMap);
					
				}
			}
			
			logger.info("http某个网站子域名流量统计"+fromJsonList);
			return resultList;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("http某个网站子域名流量统计错误"+e);
			resultList.add(Maps.newHashMap("error","request_service_failed"));
		}
		return resultList;
	}
	
	/**
	 * http网站主题跟域名统计
	 */
	@RequestMapping(value="topicAndDomainList")
	@ResponseBody
	public List topicAndDomainList(){
		Map<String, Object> fromJsonList = new HashMap<String, Object>();
		List<Map> list = new ArrayList<Map>();
		List<Map> resultList = new ArrayList<Map>();
		try {
			String string = HttpClientUtil.get(Constants.DASHBOARD_URL+Constants.TRAFFIC_TOPIC_AND_DOMAIN_CHART);
			Gson gson = new GsonBuilder().create();
			fromJsonList = gson.fromJson(string, new TypeToken<Map>(){}.getType());
			list = (List<Map>) fromJsonList.get("data");
			List<SysDataDictionaryItem> codeList = DictUtils.getDictList("TOPIC");
			//将数字替换为标签文字
			List<WebsiteDomainTopic> domainList = appCfgService.getDomainDict(new WebsiteDomainTopic());
			if(list!=null&&!list.isEmpty()){
				for (Map map : list) {
					Double value1=0d;
					if(StringUtil.isBlank(map.get("topicId").toString())){
						value1=268435455d;
					}else{
						value1 = Double.parseDouble(map.get("topicId").toString());
					}
					for (SysDataDictionaryItem code : codeList) {
						Double value2 = Double.valueOf(code.getItemCode());
						if(value1.equals(value2)){
							map.put("topic", code.getItemValue());
							break;
						}
					}
					//将domain添加名称				
					List<Map> dataDomainList = (List<Map>)map.get("domainData");
					if(dataDomainList!=null&&dataDomainList.size()>0&&domainList!=null&&domainList.size()>0){
						for (WebsiteDomainTopic wdt : domainList) {
							for (Map dataMap : dataDomainList) {
							if(wdt!=null){
								String domain = wdt.getDomain();
								Double domainId = wdt.getId().doubleValue();
									Double webId = Double.parseDouble(dataMap.get("webId").toString());
									//10个域名之外的为others webId设为-1
//									if(dataMap.get("webId").toString().equals("-1")){
//										dataMap.put("domain", "Others");
//										break;
//									}
									if(domainId.equals(webId)){
										dataMap.put("domain", domain);
										break;
									}
								}
							}
						}
					}
					resultList.add(map);
				}
			}
			logger.info("http网站主题流量分析数据："+fromJsonList);
			return resultList;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("http网站主题流量分析数据错误："+e);
			resultList.add(Maps.newHashMap("error","request_service_failed"));
		}
		return resultList;
	}
	
	
	@RequestMapping(value="webTypeList")
	public String webTypeList(  HttpServletRequest request, HttpServletResponse response, Model model){
		PageLog<TrafficIpActiveStatistic> page = new PageLog<TrafficIpActiveStatistic>(request, response);
		List list = new ArrayList();
		for (int i = 1; i < 10; i++) {
			TrafficIpActiveStatistic ip = new TrafficIpActiveStatistic();
			ip.setId(i);
			ip.setIpAddr(3+i+".1.1."+i);
			ip.setAreaId("11");
			ip.setLinkNum(1212l);
			list.add(ip);
		}
		page.setList(list);
		model.addAttribute("page", page);
		return "/dashboard/trafficWebTypeList";
	}
	
////////////////////////////////////////test
	@RequestMapping(value="showChart")
	public String showChart(@ModelAttribute("entry") TrafficIpActiveStatistic entry, Model model){
		String ipAddr="1.1.1.1";
		TrafficIpActiveStatistic ip=new TrafficIpActiveStatistic();
		ArrayList list = new ArrayList();
//		chartMap.put("ipAddr", ip.getIpAddr());
			HashMap map1 = new HashMap();
			HashMap map2 = new HashMap();
			HashMap map3 = new HashMap();
			HashMap map4 = new HashMap();
			map1.put("name", "c2sPktNum");
			map1.put("y", ip.getC2sPktNum());
			map2.put("name", "s2cPktNum");
			map2.put("y", ip.getS2cPktNum());
			map3.put("name", "c2sByteLen");
			map3.put("y", ip.getC2sByteLen());
			map4.put("name", "s2cByteLen");
			map4.put("y", ip.getS2cByteLen());
			list.add(ip.getC2sPktNum());
			list.add(ip.getS2cPktNum());
			list.add(ip.getC2sByteLen());
			list.add(ip.getS2cByteLen());
		
//		String json = new Gson().toJson();
		model.addAttribute("ipAddr",ipAddr);
		model.addAttribute("chartMap",list);
		return "/dashboard/trafficWebTypeList";
	}
	
	
	  public static void main(String[] args) {
	        Map<String, Object> map = new HashMap<String, Object>();
	        map.put("a", 10);
	        map.put("b", "10");
	        map.put("c", 10L);
	        map.put("d", "fsaf");
	        //Gson gson = new Gson();
	        Gson gson = new GsonBuilder()
	               /* .registerTypeAdapter(
	                        new TypeToken<Map<String, Object>>() {
	                        }.getType(),
	                        new JsonDeserializer<Map<String, Object>>() {
	                            @Override
	                            public Map<String, Object> deserialize(
	                                    JsonElement json, Type typeOfT,
	                                    JsonDeserializationContext context) throws JsonParseException {
	 
	                                Map<String, Object> treeMap = new HashMap<String, Object>();
	                                JsonObject jsonObject = json.getAsJsonObject();
	                                Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
	                                for (Map.Entry<String, JsonElement> entry : entrySet) {
	                                    treeMap.put(entry.getKey(), entry.getValue());
	                                }
	                                return treeMap;
	                            }
	                        })*/.create();
	 
	        String json = gson.toJson(map);
	        System.out.println(json);
	 
	        Map map1 = gson.fromJson(json, Map.class);
	        Map map2 = gson.fromJson(json, new TypeToken<Map<String, Object>>() {
	        }.getType());
	 
	        System.out.println(map2.get("c"));
	    }

}
