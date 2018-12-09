package com.nis.web.controller.log.ntc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nis.domain.FunctionServiceDict;
import com.nis.domain.Page;
import com.nis.domain.PageLog;
import com.nis.domain.log.NtcFtpLog;
import com.nis.domain.log.NtcHttpLog;
import com.nis.domain.log.NtcKeywordsUrlLog;
import com.nis.domain.maat.LogRecvData;
import com.nis.util.Constants;
import com.nis.util.DictUtils;
import com.nis.util.httpclient.HttpClientUtil;
import com.nis.web.controller.BaseController;

@Controller
@RequestMapping("${adminPath}/log/ntc/httpkey")
public class HttpKeyLogController extends BaseController {
	
	@RequestMapping("list")
	public String list(@ModelAttribute("log") NtcKeywordsUrlLog log, Model model, HttpServletRequest request, HttpServletResponse response) {
		try {
			PageLog<NtcKeywordsUrlLog> page = new PageLog<NtcKeywordsUrlLog>(request, response);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("pageSize", page.getPageSize());
			params.put("pageNo", page.getPageNo());
			if (StringUtils.isNotBlank(log.getUrl())) {
				params.put("searchUrl", log.getUrl());
			}
			initLogSearchValue(log, params);
		
			List<FunctionServiceDict> serviceList = DictUtils.getFunctionServiceDictList(log.getFunctionId());
			model.addAttribute("serviceList", serviceList);
			
			String url = Constants.LOG_BASE_URL + Constants.NTC_KEYWORDS_URL_LOG;
			String recv = HttpClientUtil.getMsg(url, params, request);
			
			Gson gson = new GsonBuilder().create();
		
			LogRecvData<NtcKeywordsUrlLog> fromJson = gson.fromJson(recv, new TypeToken<LogRecvData<NtcKeywordsUrlLog>>(){}.getType());
			if (fromJson.getStatus().intValue() == 200) {
				Page<NtcKeywordsUrlLog> data = fromJson.getData();
				page.setCount(data.getCount());
				page.setLast(data.getLast());
				page.setList(data.getList());
				List<NtcKeywordsUrlLog> list = page.getList();
				for (NtcKeywordsUrlLog l : list) {
					l.setFunctionId(log.getFunctionId());
					setLogAction(l,serviceList);
				}
				model.addAttribute("page", page);
			}
		} catch (Exception e) {
			logger.error("查询失败", e);
			addMessageLog(model, e.getMessage());
		}
		
		return "/log/ntc/httpKeyList";
	}
}
