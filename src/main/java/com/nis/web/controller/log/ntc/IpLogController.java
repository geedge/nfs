package com.nis.web.controller.log.ntc;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.util.FileUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nis.domain.FunctionServiceDict;
import com.nis.domain.Page;
import com.nis.domain.PageLog;
import com.nis.domain.log.NtcHttpLog;
import com.nis.domain.log.NtcIpLog;
import com.nis.domain.maat.LogRecvData;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.Constants;
import com.nis.util.DictUtils;
import com.nis.util.httpclient.HttpClientUtil;
import com.nis.web.controller.BaseController;

@Controller
@RequestMapping("${adminPath}/log/ntc/ip")
public class IpLogController extends BaseController {
	
	@RequestMapping("list")
	public String list(@ModelAttribute("log") NtcIpLog log, Model model, HttpServletRequest request, HttpServletResponse response) {
		try {
			PageLog<NtcIpLog> page = new PageLog<NtcIpLog>(request, response);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("pageSize", page.getPageSize());
			params.put("pageNo", page.getPageNo());
			initLogSearchValue(log, params);
			
			List<FunctionServiceDict> serviceList = DictUtils.getFunctionServiceDictList(log.getFunctionId());
			model.addAttribute("serviceList", serviceList);
			
			String url = Constants.LOG_BASE_URL + Constants.NTC_IP_LOG;
			String recv = HttpClientUtil.getMsg(url, params, request);
			
			if (StringUtils.isNotBlank(recv)) {
				Gson gson = new GsonBuilder().create();
				LogRecvData<NtcIpLog> fromJson = gson.fromJson(recv, new TypeToken<LogRecvData<NtcIpLog>>(){}.getType());
				if (fromJson.getStatus().intValue() == 200) {
					Page<NtcIpLog> data = fromJson.getData();
					page.setCount(data.getCount());
					page.setLast(data.getLast());
					page.setList(data.getList());
					List<NtcIpLog> list = page.getList();
					for (NtcIpLog l : list) {
						l.setFunctionId(log.getFunctionId());
						setLogAction(l,serviceList);
					}
					model.addAttribute("page", page);
				}
			}
		} catch (Exception e) {
			logger.error("查询失败", e);
			addMessageLog(model, e.getMessage());
		}
		
		return "/log/ntc/ipList";
	}
	
	@RequestMapping("testlist")
	public String testlist(@ModelAttribute("log") NtcIpLog log, Model model, HttpServletRequest request, HttpServletResponse response) {
		Page<NtcIpLog> page = new Page<NtcIpLog>(request, response);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pageSize", page.getPageSize());
		params.put("pageNo", page.getPageNo());
		initLogSearchValue(log, params);
		
		List<FunctionServiceDict> serviceList = DictUtils.getFunctionServiceDictList(log.getFunctionId());
		model.addAttribute("serviceList", serviceList);
		
		String url = "http://10.0.6.121:8080/gwall/test/logTest";
		try {
			String recv = HttpClientUtil.getMsg(url, params, request);
			
			Gson gson = new GsonBuilder().create();
		
			LogRecvData<NtcIpLog> fromJson = gson.fromJson(recv, new TypeToken<LogRecvData<NtcIpLog>>(){}.getType());
			if (fromJson.getStatus().intValue() == 200) {
				BeanUtils.copyProperties(fromJson.getData(), page);
				List<NtcIpLog> list = page.getList();
				for (NtcIpLog l : list) {
					l.setFunctionId(log.getFunctionId());
					setLogAction(l,serviceList);
				}
				model.addAttribute("page", page);
			}
		} catch (Exception e) {
			logger.error("查询失败", e);
			addMessageLog(model, e.getMessage());
		}
		
		return "/log/ntc/ipList";
	}
	
}
