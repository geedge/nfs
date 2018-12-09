package com.nis.web.controller.log.ntc;

import java.io.IOException;
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
import com.google.gson.reflect.TypeToken;
import com.nis.domain.FunctionServiceDict;
import com.nis.domain.Page;
import com.nis.domain.PageLog;
import com.nis.domain.log.NtcPptpLog;
import com.nis.domain.log.NtcSshLog;
import com.nis.domain.log.NtcSshLog;
import com.nis.domain.maat.LogRecvData;
import com.nis.util.Constants;
import com.nis.util.DictUtils;
import com.nis.util.httpclient.HttpClientUtil;
import com.nis.web.controller.BaseController;

@Controller
@RequestMapping(value = "${adminPath}/log/ntc/ntcSshLogs")
public class SshLogController extends BaseController{

	/**
	 * @param model
	 * @param entry
	 * @param request
	 * @param response
	 * @return
	 * @throws ClientProtocolException
	 * @throws Exception
	 */
	@RequestMapping(value={"list",""})
	public String list(Model model,@ModelAttribute("log")NtcSshLog entry,HttpServletRequest request, HttpServletResponse response) throws ClientProtocolException, IOException {
		
		try {
			
			PageLog<NtcSshLog> page = new PageLog<NtcSshLog>(request, response);
			Map<String, Object> params=new HashMap<>();
			params.put("pageSize", page.getPageSize());
			params.put("pageNo", page.getPageNo());
			//查询值判断
			initLogSearchValue(entry,params);
			
			List<FunctionServiceDict> serviceList = DictUtils.getFunctionServiceDictList(entry.getFunctionId());
			model.addAttribute("serviceList", serviceList);
			
			String url = "";
			url = Constants.LOG_BASE_URL+Constants.NTC_SSH_LOG;
			String jsonString = HttpClientUtil.getMsg(url,params,request);
			
			Gson gson = new GsonBuilder().create();
			//gson泛型支持
			LogRecvData<NtcSshLog> fromJson = gson.fromJson(jsonString, new TypeToken<LogRecvData<NtcSshLog>>(){}.getType());
			
			if (fromJson.getStatus().intValue() == 200) {
				Page<NtcSshLog> data = fromJson.getData();
				page.setCount(data.getCount());
				page.setLast(data.getLast());
				page.setList(data.getList());
				List<NtcSshLog> list = page.getList();
				for (NtcSshLog l : list) {
					l.setFunctionId(entry.getFunctionId());
					setLogAction(l,serviceList);
				}
				model.addAttribute("page", page);
				logger.info("查询Ssh日志成功");
			}
		} catch (Exception e) {
			logger.error("查询Ssh日志失败", e);
			addMessageLog(model, e.getMessage());
		}
		return "/log/ntc/sshList";
	}
	
}
