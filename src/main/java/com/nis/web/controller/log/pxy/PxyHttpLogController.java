package com.nis.web.controller.log.pxy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.RequestContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nis.domain.FunctionServiceDict;
import com.nis.domain.Page;
import com.nis.domain.PageLog;
import com.nis.domain.log.NtcSslLog;
import com.nis.domain.log.PxyHttpLog;
import com.nis.domain.maat.LogRecvData;
import com.nis.util.Constants;
import com.nis.util.DictUtils;
import com.nis.util.httpclient.HttpClientUtil;
import com.nis.web.controller.BaseController;

@Controller
@RequestMapping("${adminPath}/log/pxy")
public class PxyHttpLogController extends BaseController {

	@RequestMapping(value = {"list", "ipInterceptlogs", "domainInterceptlogs", "httpBlocklogs", 
			"httpRedirectlogs", "httpReplacelogs", "httpMonitogs"})
	public String list(@ModelAttribute("log") PxyHttpLog log, Model model, HttpServletRequest request, HttpServletResponse response) {
		try {
			setLogInfo(request, model, log);
			
			PageLog<PxyHttpLog> page = new PageLog<PxyHttpLog>(request, response);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("pageSize", page.getPageSize());
			params.put("pageNo", page.getPageNo());
		
			initLogSearchValue(log, params);
		
			List<FunctionServiceDict> serviceList = DictUtils.getFunctionServiceDictList(log.getFunctionId());
			model.addAttribute("serviceList", serviceList);
			
			String url = Constants.LOG_BASE_URL + Constants.PXY_HTTP_LOG;
			String recv = HttpClientUtil.getMsg(url, params, request);
			
			Gson gson = new GsonBuilder().create();
		
			LogRecvData<PxyHttpLog> fromJson = gson.fromJson(recv, new TypeToken<LogRecvData<PxyHttpLog>>(){}.getType());
			if (fromJson.getStatus().intValue() == 200) {
				Page<PxyHttpLog> data = fromJson.getData();
				page.setCount(data.getCount());
				page.setLast(data.getLast());
				page.setList(data.getList());
				List<PxyHttpLog> list = page.getList();
				for (PxyHttpLog l : list) {
					l.setFunctionId(log.getFunctionId());
					setLogAction(l,serviceList);
				}
				model.addAttribute("page", page);
			}
			
		} catch (Exception e) {
			logger.error("查询失败", e);
			addMessageLog(model, e.getMessage());
		}
		
		return "/log/pxy/pxyHttpList";
	}
	
	private void setLogInfo(HttpServletRequest request, Model model, PxyHttpLog log) {
		int functionId = log.getFunctionId();
		
		RequestContext requestContext = new RequestContext(request);
		
		if (functionId == 200) {
			model.addAttribute("logTitle", requestContext.getMessage("ip_intercept"));
			log.setAction(1);
		} else if (functionId == 201) {
			model.addAttribute("logTitle", requestContext.getMessage("domain_intercept"));
			log.setAction(1);
		} else if (functionId == 207) {
			model.addAttribute("logTitle", requestContext.getMessage("http_block"));
			log.setAction(16);
		} else if (functionId == 208) {
			model.addAttribute("logTitle", requestContext.getMessage("http_redirect"));
			log.setAction(48);
		} else if (functionId == 209) {
			model.addAttribute("logTitle", requestContext.getMessage("http_replace"));
			log.setAction(80);
		} else if (functionId == 210) {
			model.addAttribute("logTitle", requestContext.getMessage("http_monit"));
			log.setAction(1);
		}
	}
}
