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
import com.google.gson.reflect.TypeToken;
import com.nis.domain.FunctionServiceDict;
import com.nis.domain.Page;
import com.nis.domain.PageLog;
import com.nis.domain.log.MmPicIpLog;
import com.nis.domain.log.MmPicUrlLog;
import com.nis.domain.maat.LogRecvData;
import com.nis.util.Constants;
import com.nis.util.DictUtils;
import com.nis.util.httpclient.HttpClientUtil;
import com.nis.web.controller.BaseController;

@Controller
@RequestMapping(value = "${adminPath}/log/ntc/mmPicUrlLogs")
public class MmPicUrlController extends BaseController{
	
	@RequestMapping(value = {"/list",""})
	public String list(HttpServletRequest request, HttpServletResponse response, Model model, @ModelAttribute("log")MmPicUrlLog mmPicUrlLog) {
		
		PageLog<MmPicUrlLog> page = new PageLog<MmPicUrlLog>(request,response);
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("pageSize", page.getPageSize());
		params.put("pageNo", page.getPageNo());
		
		// 请求参数判断
		initLogSearchValue(mmPicUrlLog, params);
		
		List<FunctionServiceDict> serviceList = DictUtils.getFunctionServiceDictList(mmPicUrlLog.getFunctionId());
		model.addAttribute("serviceList", serviceList);
		
		try {
			// 请求接口
			String url = Constants.LOG_BASE_URL + Constants.NTC_MMPICURL_LOG;
			String resJson = HttpClientUtil.getMsg(url, params, request);
			Gson gson = new GsonBuilder().create();
			LogRecvData<MmPicUrlLog> fromJson = gson.fromJson(resJson, new TypeToken<LogRecvData<MmPicUrlLog>>() {}.getType());
			if(fromJson.getStatus().intValue() == 200) {
				Page<MmPicUrlLog> data = fromJson.getData();
				page.setCount(data.getCount());
				page.setLast(data.getLast());
				page.setList(data.getList());
				List<MmPicUrlLog> list = page.getList();
				for (MmPicUrlLog log : list) {
					log.setFunctionId(mmPicUrlLog.getFunctionId());
					setLogAction(log,serviceList);
				}
				model.addAttribute("page", page);
				logger.info("音视频图片URL日志查询成功！");
			}
		} catch (Exception e) {
			logger.error("音视频图片URL日志查询失败！", e);
			addMessageLog(model, e.getMessage());
		}
		
		return "/log/ntc/mmPicUrlList";
	}
}
