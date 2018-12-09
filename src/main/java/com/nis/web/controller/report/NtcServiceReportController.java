package com.nis.web.controller.report;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nis.domain.FunctionServiceDict;
import com.nis.domain.log.SearchReport;
import com.nis.domain.report.NtcServiceReport;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.Constants;
import com.nis.util.DictUtils;
import com.nis.web.controller.BaseController;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Controller
@RequestMapping("${adminPath}/report")
public class NtcServiceReportController extends BaseController {
	@RequestMapping("/ajaxNtcServiceReport")
	@ResponseBody
	public List<Map<String, Object>> report(@ModelAttribute("bean") SearchReport bean,Model model, HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> datas=new ArrayList<Map<String, Object>>();
		List<FunctionServiceDict> serviceList = DictUtils.getFunctionServiceDictList(bean.getFunctionId());
		StringBuffer serviceId=new StringBuffer();
		if(StringUtils.isNotBlank(bean.getServices())){
			serviceId.append(bean.getServices());
		}else{
			for(int i=0;i<serviceList.size();i++){
				if(i==0){ 
					serviceId.append(serviceList.get(i).getServiceId().toString());
				}else{
					serviceId.append(","+serviceList.get(i).getServiceId().toString());
				}
			}
		}
		bean.setSearchService(serviceId.toString());
		//根据reportType获取小时、日报、月报
		if(bean.getReportType()==Constants.REPORT_TYPE_HOUR){
			bean.setSearchBusinessType("2");
		 }else if(bean.getReportType()==Constants.REPORT_TYPE_DAY){
		    bean.setSearchBusinessType("3");
		 }else  if(bean.getReportType()==Constants.REPORT_TYPE_MONTH){
			    bean.setSearchBusinessType("4");
		}
		bean.setPageSize(-1);
		try {
			this.setReportSearchTime(bean);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		StringBuffer url=new StringBuffer(Constants.LOG_BASE_URL+Constants.NTC_SERVICE_REPORT);
		try {
			String json=ConfigServiceUtil.getReport(url.toString(), bean);
			List<NtcServiceReport> data=getList(json);
			Map<Integer, Long> dataMap=new HashMap<>();
			for(NtcServiceReport report:data){
				if(dataMap.containsKey(report.getService())){
					dataMap.put(report.getService(), dataMap.get(report.getService())+report.getSum().longValue());
				}else{
					dataMap.put(report.getService(),report.getSum().longValue());
				}
			}
			for(Entry<Integer, Long> e:dataMap.entrySet()) {
				Map<String, Object> _data=new HashMap<>();
				_data.put("service", e.getKey());
				_data.put("sum", e.getValue());
				datas.add(_data);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("查询失败", e);
			addMessageLog(model, null);
		} 
//		if(bean.getAction()!=null){
//			data.put(bean.getAction().toString(), 66666666l);
//		}
		return datas;
	}
	public List<NtcServiceReport> getList(String json){
		JSONObject obj=JSONObject.fromObject(json);
		if(obj.containsKey("data")) {
			JSONObject data= obj.getJSONObject("data");
			if(data.containsKey("list")) {
				Object list=data.get("list");
				JSONArray array=JSONArray.fromObject(list);
				List<NtcServiceReport> result=JSONArray.toList(array, new NtcServiceReport(),new JsonConfig());
				return result;
			}
			
		}
		return new ArrayList<NtcServiceReport>();
	}
}
