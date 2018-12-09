package com.nis.web.controller.dashboard;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nis.domain.dashboard.NtcRadiusReport;
import com.nis.util.Constants;
import com.nis.util.DateUtils;
import com.nis.util.StringUtil;
import com.nis.util.httpclient.HttpClientUtil;
import com.nis.web.controller.BaseController;

@Controller 
@RequestMapping("${adminPath}/traffic")
public class TrafficStatisticsReportController extends BaseController {
	
	
	/**
	 * 用户行为查询
	 * @param bean
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("userBehavior")
	public String list(@ModelAttribute("log") NtcRadiusReport bean,Model model, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> fromJsonList = new HashMap<String, Object>();
		Map<String, Object> userJsonList = new HashMap<String, Object>();
		Map<String, Object> ipJsonList = new HashMap<String, Object>();
		String searchBusinessType = bean.getSearchBusinessType();
		if("2".endsWith(searchBusinessType)) {
			model.addAttribute("searchBusinessType", 2);// 用户查询
		}
		if("3".endsWith(searchBusinessType)) {
			model.addAttribute("searchBusinessType", 3);// IP查询
		}
		String url = Constants.DASHBOARD_URL+Constants.NTC_RADIUS_REPORT;
		String statTime = bean.getSearchFoundStartTime();
		String endTime = bean.getSearchFoundEndTime();
		String account = bean.getAccount();
		String nasIp = bean.getNasIp();
		String destUrl = url+ "?searchBusinessType=1";
		String userUrl = url+ "?searchBusinessType=2";
		String ipUrl =url + "?searchBusinessType=3";
		List list = new ArrayList();
		List userList = new ArrayList();
		List ipList = new ArrayList();
		try {
			if(StringUtil.isBlank(statTime)&&StringUtil.isBlank(endTime)) {
				statTime=DateUtils.getDate()+" 00:00:00"; // 默认今天
				endTime=DateUtils.getDateTime();
				bean.setSearchFoundStartTime(statTime);
				bean.setSearchFoundEndTime(endTime);
				statTime=URLEncoder.encode(statTime, "UTF-8");
				endTime=URLEncoder.encode(endTime, "UTF-8");
				String paramUrl="&searchReportStartTime="+statTime+"&searchReportEndTime="+endTime;
				destUrl=destUrl+paramUrl;
				userUrl=userUrl+paramUrl;
				ipUrl=ipUrl+paramUrl;
			}else {
				statTime=URLEncoder.encode(statTime, "UTF-8");
				endTime=URLEncoder.encode(endTime, "UTF-8");
				String paramUrl="&searchReportStartTime="+statTime+"&searchReportEndTime="+endTime;
				destUrl=destUrl+paramUrl;
				userUrl=userUrl+paramUrl;
				ipUrl=ipUrl+paramUrl;
			}
			// 用户及IP数据
			String string = HttpClientUtil.get(destUrl);
			Gson gson = new GsonBuilder().create();
			fromJsonList = gson.fromJson(string, new TypeToken<Map>(){}.getType());
			list = (ArrayList) fromJsonList.get("data");
			Map map = (Map) list.get(0);
			List nasIpList=(List) map.get("nasIpList");
			List accountList=(List) map.get("accountList");
			model.addAttribute("nasIpList", nasIpList);
			model.addAttribute("accountList", accountList);
			if(StringUtil.isBlank(account)&&accountList!=null&&accountList.size()>0&&!"3".equals(searchBusinessType)) {
				Map accountMap = (Map) accountList.get(0);
				String firstAccount=(String) accountMap.get("account");
				String userString = HttpClientUtil.get(userUrl+"&searchAccount="+firstAccount);
				// 指定用户 查询
				Gson usergson = new GsonBuilder().create();
				userJsonList = usergson.fromJson(userString, new TypeToken<Map>(){}.getType());
				userList = (ArrayList) userJsonList.get("data");
				model.addAttribute("userList", userList);
				model.addAttribute("searchAccount", firstAccount);
			}
			if(StringUtil.isBlank(nasIp)&&nasIpList!=null&&nasIpList.size()>0&&!"2".equals(searchBusinessType)) {
				Map IpMap = (Map) nasIpList.get(0);
				String firstIp=(String) IpMap.get("nasIp");
				String ipString = HttpClientUtil.get(ipUrl+"&searchNasIp="+firstIp);
				// 指定IP查询
				Gson ipgson = new GsonBuilder().create();
				ipJsonList = ipgson.fromJson(ipString, new TypeToken<Map>(){}.getType());
				ipList = (ArrayList) ipJsonList.get("data");
				model.addAttribute("ipList", ipList);
				model.addAttribute("searchNasIp", firstIp);
				
			}
			if("2".endsWith(searchBusinessType)&&!StringUtil.isBlank(account)) {
				
				String userString = HttpClientUtil.get(userUrl+"&searchAccount="+account);
				// 指定用户 查询
				Gson usergson = new GsonBuilder().create();
				userJsonList = usergson.fromJson(userString, new TypeToken<Map>(){}.getType());
				userList = (ArrayList) userJsonList.get("data");
				model.addAttribute("userList", userList);
				model.addAttribute("searchAccount", account);
			}
			if ("3".endsWith(searchBusinessType)&&!StringUtil.isBlank(nasIp)) {
				String ipString = HttpClientUtil.get(ipUrl+"&searchNasIp="+nasIp);
				// 指定IP查询
				Gson ipgson = new GsonBuilder().create();
				ipJsonList = ipgson.fromJson(ipString, new TypeToken<Map>(){}.getType());
				ipList = (ArrayList) ipJsonList.get("data");
				model.addAttribute("ipList", ipList);
				model.addAttribute("searchNasIp", nasIp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/dashboard/trafficUserBehavior";
	}
/*	public static void main(String[] args) {
		Map<String, Object> fromJsonList = new HashMap<String, Object>();
		HashMap<String, Object> m = new HashMap<String, Object>();
		List list = new ArrayList();
		String string;
		try {
			string = HttpClientUtil.get("http://localhost:8082/galaxy-service/service/log/v1/ntcRadiusReport?"
		+ "searchBusinessType=1&searchReportStartTime=2018-06-01+15%3A41%3A21&searchReportEndTime=2018-10-30+15%3A46%3A21");
			Gson gson = new GsonBuilder().create();
			fromJsonList = gson.fromJson(string, new TypeToken<Map>(){}.getType());
			list = (ArrayList) fromJsonList.get("data");
			Map map = (Map) list.get(0);
			List nasIpList=(List) map.get("nasIpList");
			List accountList=(List) map.get("accountList");
			System.err.println(map.get("nasIpList"));
			System.err.println(map.get("accountList"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
}
