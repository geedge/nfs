package com.nis.web.controller.report;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nis.domain.FunctionServiceDict;
import com.nis.domain.basics.ServiceDictInfo;
import com.nis.domain.log.SearchReport;
import com.nis.domain.report.NtcTagReport;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.Constants;
import com.nis.util.DictUtils;
import com.nis.web.controller.BaseController;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Controller
@RequestMapping("${adminPath}/report")
public class NtcTagReportController extends BaseController {
	@RequestMapping("/ajaxNtcTagReport")
	public String list(@ModelAttribute("bean") SearchReport bean,Model model, HttpServletRequest request, HttpServletResponse response) {
		List<FunctionServiceDict> serviceList = DictUtils.getFunctionServiceDictList(bean.getFunctionId());
		List<ServiceDictInfo> labels=serviceDictInfoService.findAllLableDict();
		model.addAttribute("labels", labels);
		if(bean.getServiceId()!=null){
			bean.setSearchService(bean.getServiceId().toString());
		}else{
			StringBuffer serviceId=new StringBuffer();
			for(int i=0;i<serviceList.size();i++){
				if(i==0){
					serviceId.append(serviceList.get(i).getServiceId().toString());
				}else{
					serviceId.append(","+serviceList.get(i).getServiceId().toString());
				}
			}
			bean.setSearchService(serviceId.toString());
		}
		//根据reportType获取小时、日报、月报
		if(bean.getReportType()==Constants.REPORT_TYPE_HOUR){
			bean.setSearchBusinessType("2");
		 }else if(bean.getReportType()==Constants.REPORT_TYPE_DAY){
		    bean.setSearchBusinessType("3");
		 }else  if(bean.getReportType()==Constants.REPORT_TYPE_MONTH){
			    bean.setSearchBusinessType("4");
		}
		bean.setPageSize(-1);
		bean.setFields("tag,sum,reportTime");
		try {
			this.setReportSearchTime(bean);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		List<String> titles=this.getDateTitiles(bean);
		model.addAttribute("titles", titles);
		try {
			StringBuffer url=new StringBuffer(Constants.LOG_BASE_URL+Constants.NTC_TAG_REPORT);
			String json=ConfigServiceUtil.getReport(url.toString(), bean);
			List<NtcTagReport> list=getList(json);
			HashMap<String,Long> trimData=new HashMap<>();//数据累加去重
			//SimpleDateFormat sdf=new SimpleDateFormat(Constants.SEARCH_DATEFORMAT);
			for(NtcTagReport report:list){
				String trimStr="";
				if(bean.getReportType()==Constants.REPORT_TYPE_HOUR){
					trimStr=report.getTag().intValue()+Constants.REPORT_TRIM_CONNECTOR+
							report.getReportTime().substring(0,13);
					
				}else if(bean.getReportType()==Constants.REPORT_TYPE_DAY){
					trimStr=report.getTag().intValue()+Constants.REPORT_TRIM_CONNECTOR+
							report.getReportTime().substring(0,10);
					
				}else if(bean.getReportType()==Constants.REPORT_TYPE_MONTH){
					 trimStr=report.getTag().intValue()+Constants.REPORT_TRIM_CONNECTOR+
							report.getReportTime().substring(0,7);
				}
				if(trimData.containsKey(trimStr)){	
					trimData.put(trimStr, trimData.get(trimStr)+report.getSum().longValue());		
				}else{
					trimData.put(trimStr, report.getSum().longValue());	
				}
			}
			HashMap<String,List<Long>> showData=new LinkedHashMap<>();//构造数据展示集合
			for(ServiceDictInfo label:labels){
				List<Long> line= new ArrayList<Long>();
				long total=0;
				for(String title:titles){
					if(trimData.containsKey(label.getServiceDictId().intValue()+Constants.REPORT_TRIM_CONNECTOR+title)){
						line.add(trimData.get(label.getServiceDictId().intValue()+Constants.REPORT_TRIM_CONNECTOR+title));
						total+=trimData.get(label.getServiceDictId().intValue()+Constants.REPORT_TRIM_CONNECTOR+title).longValue();
					}else{
						line.add(0l);
					}
				}
				List<Long> _line= new ArrayList<Long>();
				_line.add(total);
				_line.addAll(line);
				showData.put(label.getServiceDictId().toString(), _line);
			}
			model.addAttribute("datas", showData);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("查询失败", e);
			addMessageLog(model, null);
		}
//		HashMap<String,List<Long>> showData=new LinkedHashMap<>();//构造数据展示集合
//		for(ServiceDictInfo label:labels){
//			List<Long> line= new ArrayList<Long>();
//			long total=0l;
//			for(String title:titles){
//				line.add(10000l);
//				total+=10000l;
//			}
//			List<Long> _line= new ArrayList<Long>();
//			_line.add(total);
//			_line.addAll(line);
//			showData.put(label.getServiceDictId().toString(), _line);
//		}
//		model.addAttribute("datas", showData);
		return "/report/label";
	}
	public List<NtcTagReport> getList(String json){
		JSONObject obj=JSONObject.fromObject(json);
		if(obj.containsKey("data")) {
			JSONObject data= obj.getJSONObject("data");
			if(data.containsKey("list")) {
				Object list=data.get("list");
				JSONArray array=JSONArray.fromObject(list);
				List<NtcTagReport> result=JSONArray.toList(array, new NtcTagReport(),new JsonConfig());
				return result;
			}
			
		}
		return new ArrayList<NtcTagReport>();
	}
}
