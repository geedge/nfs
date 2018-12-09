/**
 *@Title: NtcPzReportController.java
 *@Package com.nis.web.controller.log.ntc 
 *@Description TODO
 *@author dell
 *@date 2018年6月12日 下午2:09:41
 *@version  版本号
 */
package com.nis.web.controller.report;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nis.domain.FunctionServiceDict;
import com.nis.domain.basics.ServiceDictInfo;
import com.nis.domain.log.SearchReport;
import com.nis.domain.report.NtcPzReport;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.Constants;
import com.nis.util.DictUtils;
import com.nis.web.controller.BaseController;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * @ClassName: NtcPzReportController.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年6月12日 下午2:09:41
 * @version V1.0
 */
@Controller
@RequestMapping("${adminPath}/report")
public class NtcPzReportController extends BaseController{
	@RequestMapping(value="ajaxGetLogTotal",method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String, Object>> ajaxGetLogTotal(Model model,Long endTime,String actions,String serviceIds,Integer functionId,String compileIds){
		List<Map<String, Object>> dataList=new ArrayList<>();
		SimpleDateFormat sdf=new SimpleDateFormat(Constants.SEARCH_DATEFORMAT);
		Date endDate=null;
		Date startDate=null;
		if(endTime!=null) {
			if(StringUtils.isNotBlank(Constants.LOG_TIME_START)) {
				try {
					startDate=sdf.parse(Constants.LOG_TIME_START);
					if(StringUtils.isNotBlank(Constants.LOG_TIME_END)) {
						endDate=sdf.parse(Constants.LOG_TIME_END);
					}else {
						endDate=new Date(endTime);
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else {
				Calendar cal=Calendar.getInstance();
				//分钟间隔
				int interval=Constants.LOG_TIME_RANGE/1000/60;
				cal.setTimeInMillis(endTime);
				if(interval<60) {//5分钟
					cal.set(Calendar.SECOND, 0);
					int minute=cal.get(Calendar.MINUTE);
					cal.set(Calendar.MINUTE,minute/interval*interval);
				}else {//1小时
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MINUTE, 0);
				}
				Calendar endTimeCal=Calendar.getInstance();
				endTimeCal.setTimeInMillis(cal.getTimeInMillis());
				//endTimeCal.add(Calendar.SECOND, -1);
				endDate=endTimeCal.getTime();
				cal.add(Calendar.MINUTE, -interval);
				startDate=cal.getTime();
			}
			logger.info("log search end time:"+sdf.format(endDate));
			logger.info("log search start time:"+sdf.format(startDate));
		}
//		String serviceIdsStr="";
//		if(StringUtils.isBlank(serviceIds)) {
//			StringBuffer _serviceIds=new StringBuffer();
//			List<FunctionServiceDict> serviceList=DictUtils.getFunctionServiceDictList(functionId.intValue()); 
//			for(String action:actions.split(",")) {
//				for(FunctionServiceDict service:serviceList){
//					if(service.getAction().intValue()==Integer.parseInt(action)){
//						_serviceIds.append(service.getServiceId().intValue());
//						_serviceIds.append(",");
//					}
//				}
//			}
//			if(_serviceIds.toString().endsWith(",")) {
//				serviceIdsStr=_serviceIds.toString().substring(0,_serviceIds.toString().lastIndexOf(","));
//			}
//		}else {
//			serviceIdsStr=serviceIds;
//		}
		String json=ConfigServiceUtil.getReport(Constants.BUSINESSTYPE_CONFIG, compileIds, serviceIds, startDate==null?null:sdf.format(startDate), endDate==null?null:sdf.format(endDate));
		List<NtcPzReport> list=getList(json);
		String[] idArr=compileIds.split(",");
		if(list!=null&&(list.size()>0)){
			for(NtcPzReport report:list) {
				Map<String, Object> data=new HashMap<String, Object>();
				data.put("compileId", report.getCfgId().longValue());
				data.put("sum",report.getSum().longValue());
				dataList.add(data);
			}
		}else {
			for(String id:idArr) {
				Map<String, Object> data=new HashMap<String, Object>();
				data.put("compileId", id);
				data.put("sum",0);
				dataList.add(data);
			}
		}
		
		return dataList;
	}
	@RequestMapping("/ntcPzReport")
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
		StringBuffer url=new StringBuffer(Constants.LOG_BASE_URL+Constants.NTC_PZ_REPORT);
		
		List<String> titles=this.getDateTitiles(bean);
		model.addAttribute("titles", titles);
		try {
			String json=ConfigServiceUtil.getReport(url.toString(), bean);
			model.addAttribute("datas", getList(json));
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("查询失败", e);
			addMessageLog(model, null);
		}
		return "/report/pz";
	}
	public List<NtcPzReport> getList(String json){
		JSONObject obj=JSONObject.fromObject(json);
		if(obj.containsKey("data")) {
			JSONObject data= obj.getJSONObject("data");
			if(data.containsKey("list")) {
				Object list=data.get("list");
				JSONArray array=JSONArray.fromObject(list);
				List<NtcPzReport> result=JSONArray.toList(array, new NtcPzReport(),new JsonConfig());
				return result;
			}
			
		}
		return new ArrayList<NtcPzReport>();
	}
}
