package com.nis.web.controller.log.ntc;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.nis.domain.log.NtcCollectVoipLog;
import com.nis.domain.log.NtcDdosLog;
import com.nis.domain.maat.LogRecvData;
import com.nis.util.Constants;
import com.nis.util.DictUtils;
import com.nis.util.httpclient.HttpClientUtil;
import com.nis.web.controller.BaseController;

@Controller
@RequestMapping(value = "${adminPath}/log/ntc/ntcDdosLogs")
public class DdosLogController extends BaseController{

	/**
	 * @param model
	 * @param entry
	 * @param request
	 * @param response
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping(value={"list",""})
	public String list(Model model,@ModelAttribute("log")NtcDdosLog entry,HttpServletRequest request, HttpServletResponse response) throws ClientProtocolException, IOException {
		
		try {
			
			PageLog<NtcDdosLog> page = new PageLog<NtcDdosLog>(request, response);
			Map<String, Object> params=new HashMap<>();
			params.put("pageSize", page.getPageSize());
			params.put("pageNo", page.getPageNo());
			/*if(entry!=null&&entry.getIsBlock()!=null) {
				params.put("searchIsBlock", entry.getIsBlock());
			}else {
				entry.setIsBlock(1);//默认保护ddos日志
				params.put("searchIsBlock", entry.getIsBlock());
			}*/
			//查询值判断
			initLogSearchValue(entry,params);
			
			List<FunctionServiceDict> serviceList = DictUtils.getFunctionServiceDictList(entry.getFunctionId());
			model.addAttribute("serviceList", serviceList);
			
			String url = "";
			url = Constants.LOG_BASE_URL+Constants.NTC_DDOS_LOG;
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
			LogRecvData<NtcDdosLog> fromJson = gson.fromJson(jsonString, new TypeToken<LogRecvData<NtcDdosLog>>(){}.getType());
			
			if (fromJson.getStatus().intValue() == 200) {
				Page<NtcDdosLog> data = fromJson.getData();
				page.setCount(data.getCount());
				page.setLast(data.getLast());
				page.setList(data.getList());
				List<NtcDdosLog> list = page.getList();
				for (NtcDdosLog l : list) {
					l.setFunctionId(entry.getFunctionId());
					setLogAction(l,serviceList);
				}
				model.addAttribute("page", page);
				logger.info("查询Ddos日志成功");
			}
		} catch (Exception e) {
			logger.error("查询Ddos日志失败", e);
			addMessageLog(model, e.getMessage());
		}
		return "/log/ntc/ddosList";
	}
	
}
