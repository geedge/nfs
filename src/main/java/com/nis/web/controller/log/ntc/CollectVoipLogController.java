package com.nis.web.controller.log.ntc;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.nis.domain.FunctionServiceDict;
import com.nis.domain.Page;
import com.nis.domain.PageLog;
import com.nis.domain.log.NtcBGPLog;
import com.nis.domain.log.NtcCollectVoipLog;
import com.nis.domain.maat.LogRecvData;
import com.nis.util.CodeDicUtils;
import com.nis.util.Constants;
import com.nis.util.DateUtils;
import com.nis.util.DictUtils;
import com.nis.util.httpclient.HttpClientUtil;
import com.nis.web.controller.BaseController;
import com.nis.web.dao.dashboard.codedic.CodeResult;

@Controller
@RequestMapping(value = "${adminPath}/log/ntc/ntcCollectVoipLogs")
public class CollectVoipLogController extends BaseController{

	/**
	 * voip泛收日志
	 * @param model
	 * @param entry
	 * @param request
	 * @param response
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping(value={"list",""})
	public String list(Model model,@ModelAttribute("log")NtcCollectVoipLog entry,HttpServletRequest request, HttpServletResponse response) throws ClientProtocolException, IOException {
		
		try {
			
			PageLog<NtcCollectVoipLog> page = new PageLog<NtcCollectVoipLog>(request, response);
			Map<String, Object> params=new HashMap<>();
			params.put("pageSize", page.getPageSize());
			params.put("pageNo", page.getPageNo());
			//查询值判断
			if (StringUtils.isNotBlank(entry.getSearchFoundStartTime())
					&& StringUtils.isNotBlank(entry.getSearchFoundEndTime())) {
				params.put("searchFoundStartTime", entry.getSearchFoundStartTime());
				params.put("searchFoundEndTime", entry.getSearchFoundEndTime());
			} else {
				// 判断是否是从配置界面过来的日志查询
				if (StringUtils.isNotBlank(entry.getIsLogTotalSearch())) {
					Calendar time = Calendar.getInstance();
					time.add(Calendar.MINUTE, -5);
					String searchEndTime = DateUtils.formatDateTime(time.getTime());
					params.put("searchFoundEndTime", searchEndTime);
					entry.setSearchFoundEndTime(searchEndTime);

					time.add(Calendar.MINUTE, -5);
					String searchStartTime = DateUtils.formatDateTime(time.getTime());
					params.put("searchFoundStartTime", searchStartTime);
					entry.setSearchFoundStartTime(searchStartTime);

				} else {
					// 设置默认查询当前时间及前五分钟
					String endTime = DateUtils.getDateTime();
					Date dateStart = new Date(new Date().getTime() - Constants.LOG_TIME_INTERVAL);
					String startTime = DateUtils.formatDateTime(dateStart);
					params.put("searchFoundStartTime", startTime);
					params.put("searchFoundEndTime", endTime);
					entry.setSearchFoundStartTime(startTime);
					entry.setSearchFoundEndTime(endTime);
				}
				logger.info("searchFoundStartTime：" + params.get("searchFoundStartTime"));
				logger.info("searchFoundEndTime：" + params.get("searchFoundEndTime"));
			}

			if (StringUtils.isNotBlank(entry.getVoipProtocol())) {
				params.put("searchVoipProtocol", entry.getVoipProtocol());
			}
			if (StringUtils.isNotBlank(entry.getRtpDIp())) {
				params.put("searchRtpDIp", entry.getRtpDIp());
			}
			if (StringUtils.isNotBlank(entry.getCapIp())) {
				params.put("searchCapIp", entry.getCapIp());
			}
			if (StringUtils.isNotBlank(entry.getRtpSIp())) {
				params.put("searchRtpSIp", entry.getRtpSIp());
			}
			if (StringUtils.isNotBlank(entry.getSipDIp())) {
				params.put("searchSipDIp", entry.getSipDIp());
			}
			if (StringUtils.isNotBlank(entry.getSipSIp())) {
				params.put("searchSipSIp", entry.getSipSIp());
			}
			if (StringUtils.isNotBlank(entry.getOrderBy())) {
				params.put("orderBy", entry.getOrderBy());
			}
			List<FunctionServiceDict> serviceList = DictUtils.getFunctionServiceDictList(entry.getFunctionId());
			model.addAttribute("serviceList", serviceList);
			
			//获取字典信息
			List<CodeResult> protocolList=CodeDicUtils.getCodeList("protocolCode");
			List<CodeResult> behaviorList=CodeDicUtils.getCodeList("behaviorCode");
			List<CodeResult> appList=CodeDicUtils.getCodeList("appCode");
			model.addAttribute("protocolList", protocolList);
			model.addAttribute("behaviorList", behaviorList);
			model.addAttribute("appList", appList);
			
			String url = "";
			url = Constants.LOG_BASE_URL+Constants.NTC_COLLECT_VOIP_LOG;
			String jsonString = HttpClientUtil.getMsg(url,params,request);
			
			GsonBuilder builder = new GsonBuilder();
			builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {

				public Date deserialize(JsonElement json, java.lang.reflect.Type typeOfT,
						JsonDeserializationContext context) throws JsonParseException {
					return new Date(json.getAsJsonPrimitive().getAsLong());
				}
			});
			Gson gson = builder.setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			//gson泛型支持
			LogRecvData<NtcCollectVoipLog> fromJson = gson.fromJson(jsonString, new TypeToken<LogRecvData<NtcCollectVoipLog>>(){}.getType());
			
			if (fromJson.getStatus().intValue() == 200) {
				Page<NtcCollectVoipLog> data = fromJson.getData();
				page.setCount(data.getCount());
				page.setLast(data.getLast());
				page.setList(data.getList());
				model.addAttribute("page", page);
				logger.info("查询VoIP泛收日志成功");
			}
		} catch (Exception e) {
			logger.error("查询VoIP泛收日志失败", e);
			addMessageLog(model, e.getMessage());
		}
		return "/log/ntc/collectVoipList";
	}
	
}
