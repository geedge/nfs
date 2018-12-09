package com.nis.web.controller.dashboard;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.beust.jcommander.internal.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.reflect.TypeToken;
import com.nis.domain.PageLog;
import com.nis.domain.dashboard.TrafficIpActiveStatistic;
import com.nis.util.Constants;
import com.nis.util.StringUtil;
import com.nis.util.httpclient.HttpClientUtil;
import com.nis.web.controller.BaseController;

@Controller 
@RequestMapping("${adminPath}/dashboard/traffic")
public class TrafficStatisticsInfoController extends BaseController {
	/**
	 * rejectNum 封堵数量
	 * newUniConn 新建连接数
	 * liveConnNum 活跃链接数
	 * dropConnNum 丢弃链接数
	 * 
	 */
	@RequestMapping(value="info")
	@ResponseBody
	public Map ipActive(){
		Map<String, Object> fromJsonList = new HashMap<String, Object>();
		HashMap<String, Object> m = new HashMap<String, Object>();
		List list = new ArrayList();
		try {
			String string = HttpClientUtil.get(Constants.DASHBOARD_URL+Constants.NTC_TOTAL_REPORT);
			Gson gson = new GsonBuilder().create();
			fromJsonList = gson.fromJson(string, new TypeToken<Map>(){}.getType());
			list = (ArrayList) fromJsonList.get("data");
			Random r = new Random();
			Double rejectNum=0d;
			Double dropConnNum=0d;
			Double monitorNum=0d;
			Double loopConnNum=0d;
			Double liveConnNum=0d;
			Double newUniConnNum=0d;
			Double inoctetsNum=0d;
			Double outoctetsNum=0d;
			Double bandwidth=0d;
			if(list!=null&&list.size()>0){
				Map map = (Map) list.get(0);
				rejectNum =	(Double) map.get("rejectNum") ;
				monitorNum = (Double) map.get("monitorNum");
				dropConnNum =  (Double) map.get("dropConnNum");
				loopConnNum = (Double) map.get("loopConnNum");
				liveConnNum = (Double) map.get("liveConnNum");
				newUniConnNum = (Double) map.get("newUniConnNum");
				inoctetsNum = (Double) map.get("inoctets");
				outoctetsNum = (Double) map.get("outoctets");
				//单位换算 byte->bit/s
				inoctetsNum=(inoctetsNum*8)/(5*60);
				outoctetsNum=(outoctetsNum*8)/(5*60);
				
			}
			bandwidth=inoctetsNum+outoctetsNum;
			m.put("rejectNum", rejectNum);
			m.put("monitorNum", monitorNum);
			m.put("loopConnNum", loopConnNum);
			m.put("dropConnNum", dropConnNum);
			m.put("liveConnNum", liveConnNum);
			m.put("newUniConnNum", newUniConnNum);
			
			m.put("bandwidth", bandwidth);
			m.put("inoctetsNum", inoctetsNum);
			m.put("outoctetsNum", outoctetsNum);
			
			logger.info("统计流量信息数据"+fromJsonList);
			return m;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("统计流量信息数据错误"+e);
		}
		return m;
	}
	
	@RequestMapping(value="bandwidthList")
	public String bandwidthList(  HttpServletRequest request, HttpServletResponse response, Model model){
		return "/dashboard/trafficBandwidthList";
	}
	/**
	 * 流量统计滚动动作查看详情页面
	 */
	@RequestMapping(value={"trafficBlockList","trafficMonitorList"})
	public String serviceBlockList(@RequestParam("searchAction")String searchAction,HttpServletRequest request, HttpServletResponse response, Model model){
		model.addAttribute("searchAction", searchAction);
		return "/dashboard/trafficActionTransList";
	}
	/**
	 * 根据动作查询entrance趋势 间隔5分钟数据
	 */
	@RequestMapping(value="actionEntranceTrans")
	@ResponseBody
	public List actionTrans(@RequestParam("searchAction")String searchAction){
		Map<String, Object> fromJsonList = new HashMap<String, Object>();
		List resultList = new ArrayList();
		String url = Constants.DASHBOARD_URL+Constants.NTC_ACTION_ENTRANCE_REPORT;
			url=url+"?searchAction="+searchAction;
		try {
			String string = HttpClientUtil.get(url);
			Gson gson = new GsonBuilder().create();
			fromJsonList = gson.fromJson(string, new TypeToken<Map>(){}.getType());
			logger.info(searchAction+"动作间隔5分钟数据"+fromJsonList);
			resultList = (ArrayList) fromJsonList.get("data");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(searchAction+"动作详情数据获取错误"+e);
			resultList.add(Maps.newHashMap("error","request_service_failed"));
		}
		return resultList;
	}
	/**
	 * 根据ip46,协议tcp,udp查询带宽 间隔5分钟数据
	 */
	@RequestMapping(value="bandwidthTrans")
	@ResponseBody
	public Map ipActiveMinuteList(@RequestParam("addrType")String addrType,@RequestParam("transType")Integer transType){
		Map<String, Object> fromJsonList = new HashMap<String, Object>();
		Map map = new HashMap();
		String url = Constants.DASHBOARD_URL+Constants.TRAFFIC_BANDWIDTH_TRANS;
		if(!StringUtil.isBlank(addrType)){
			url=url+"?addrType="+addrType;
		}
		if(transType!=null){
			url=url+"?transType="+transType;
		}
		try {
			String string = HttpClientUtil.get(url);
			Gson gson = new GsonBuilder().create();
			fromJsonList = gson.fromJson(string, new TypeToken<Map>(){}.getType());
			logger.info("带宽1小时,间隔5分钟数据"+fromJsonList);
			List list = (ArrayList) fromJsonList.get("data");
			if(!StringUtil.isEmpty(list)){
				map=(Map) list.get(0);
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("带宽详情数据获取错误"+e);
		}
		return map;
	}
	/**
	 * 协议类型详细列表
	 */
	@RequestMapping(value="protocolTypeList")
	public String protocolTypeList(){
		return "/dashboard/trafficProtocolTypeList";
	}
	/**
	 * 协议详情统计图跟表
	 */
	@RequestMapping(value="protocolList")
	@ResponseBody
	public List protocolList(){
		Map<String, Object> fromJsonList = new HashMap<String, Object>();
		List list = new ArrayList();
		String url = Constants.DASHBOARD_URL+Constants.TRAFFIC_PROTOCOL_LIST;
		try {
			String string = HttpClientUtil.get(url);
			Gson gson = new GsonBuilder().create();
			fromJsonList = gson.fromJson(string, new TypeToken<Map>(){}.getType());
			logger.info("协议详情5分钟数据"+fromJsonList);
			list = (ArrayList) fromJsonList.get("data");
			Long totalLink=0l;
			Long totalPackets=0l;
			Double totalGByte=0d;
			DecimalFormat lf = new DecimalFormat("0");
			DecimalFormat df = new DecimalFormat("0.00000000");
			if(!StringUtil.isEmpty(list)){
				for (Object object : list) {
					Map m=(Map) object;
					totalGByte+=Double.parseDouble(m.get("GByte").toString());
//					String linkNum = df.format(m.get("linkNum"));
		            m.put("GByte", df.format(m.get("GByte")));
		            m.put("packets", lf.format(m.get("packets")));
		            m.put("linkNum", lf.format(m.get("linkNum")));
		            totalLink+=Long.parseLong( m.get("linkNum").toString());
		            totalPackets+=Long.parseLong(m.get("packets").toString());
				}
				for (Object object : list) {
					Map m=(Map) object;
					m.put("totalLink", totalLink);
					m.put("totalPackets", totalPackets);
					m.put("totalGByte", totalGByte);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("协议详情数据获取错误"+e);
			list.add(Maps.newHashMap("error","request_service_failed"));
		}
		return list;
	}
	/**
	 * App类型详细列表
	 */
	@RequestMapping(value="appTypeList")
	public String appTypeList(){
		return "/dashboard/trafficAppTypeList";
	}
	/**
	 * 协议详情统计图跟表
	 */
	@RequestMapping(value="appList")
	@ResponseBody
	public List appList(){
		Map<String, Object> fromJsonList = new HashMap<String, Object>();
		List list = new ArrayList();
		String url = Constants.DASHBOARD_URL+Constants.TRAFFIC_APP_LIST;
		try {
			String string = HttpClientUtil.get(url);
			Gson gson = new GsonBuilder().create();
			fromJsonList = gson.fromJson(string, new TypeToken<Map>(){}.getType());
			logger.info("app详情5分钟数据"+fromJsonList);
			list = (ArrayList) fromJsonList.get("data");
			Long totalLink=0l;
			Long totalPackets=0l;
			Double totalGByte=0d;
			DecimalFormat lf = new DecimalFormat("0");
			DecimalFormat df = new DecimalFormat("0.00000000");
			if(!StringUtil.isEmpty(list)){
				for (Object object : list) {
					Map m=(Map) object;
					totalGByte+=Double.parseDouble(m.get("GByte").toString());
//					String linkNum = df.format(m.get("linkNum"));
		            m.put("GByte", df.format(m.get("GByte")));
		            m.put("packets", lf.format(m.get("packets")));
		            m.put("linkNum", lf.format(m.get("linkNum")));
		            totalLink+=Long.parseLong( m.get("linkNum").toString());
		            totalPackets+=Long.parseLong(m.get("packets").toString());
				}
				for (Object object : list) {
					Map m=(Map) object;
					m.put("totalLink", totalLink);
					m.put("totalPackets", totalPackets);
					m.put("totalGByte", totalGByte);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("app详情数据获取错误"+e);
			list.add(Maps.newHashMap("error","request_service_failed"));
		}
		return list;
	}
}
