package com.nis.web.controller.log.ntc;

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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.nis.domain.FunctionServiceDict;
import com.nis.domain.Page;
import com.nis.domain.PageLog;
import com.nis.domain.log.IrSnatLog;
import com.nis.domain.log.NtcL2tpLog;
import com.nis.domain.maat.LogRecvData;
import com.nis.util.Constants;
import com.nis.util.DictUtils;
import com.nis.util.httpclient.HttpClientUtil;
import com.nis.web.controller.BaseController;

@Controller
@RequestMapping("${adminPath}/log/ntc/l2tpLogs")
public class L2tpLogController extends BaseController {

	@RequestMapping(value = {"/list"})
	public String list(HttpServletRequest request, HttpServletResponse response, Model model, @ModelAttribute("log")NtcL2tpLog ntcL2tpLog) {
		
		PageLog<NtcL2tpLog> page = new PageLog<NtcL2tpLog>(request,response);
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("pageSize", page.getPageSize());
		params.put("pageNo", page.getPageNo());
		
		// 请求参数判断
		initLogSearchValue(ntcL2tpLog, params);
		
		List<FunctionServiceDict> serviceList = DictUtils.getFunctionServiceDictList(ntcL2tpLog.getFunctionId());
		model.addAttribute("serviceList", serviceList);
		
		try {
			// 请求接口
			String url = Constants.LOG_BASE_URL + Constants.NTC_L2TP_LOG;
			String resJson = HttpClientUtil.getMsg(url, params, request);
			Gson gson = new GsonBuilder().create();
			LogRecvData<NtcL2tpLog> fromJson = gson.fromJson(resJson, new TypeToken<LogRecvData<NtcL2tpLog>>() {}.getType());
			if(fromJson.getStatus().intValue() == 200) {
				Page<NtcL2tpLog> data = fromJson.getData();
				page.setCount(data.getCount());
				page.setLast(data.getLast());
				page.setList(data.getList());
				List<NtcL2tpLog> list = page.getList();
				for (NtcL2tpLog log : list) {
					log.setFunctionId(ntcL2tpLog.getFunctionId());
					setLogAction(log,serviceList);
				}
				model.addAttribute("page", page);
			}
		} catch (Exception e) {
			logger.error("L2TP日志查询失败", e);
			addMessageLog(model, e.getMessage());
		}
		
		return "/log/ntc/l2tpLogList";
	}
}
