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
import com.nis.domain.log.MmFileDigestLog;
import com.nis.domain.log.NtcStreamMediaLog;
import com.nis.domain.maat.LogRecvData;
import com.nis.util.Constants;
import com.nis.util.DictUtils;
import com.nis.util.httpclient.HttpClientUtil;
import com.nis.web.controller.BaseController;

@Controller
@RequestMapping(value = "${adminPath}/log/ntc/mmFileDigestLogs")
public class MmFileDigestLogController extends BaseController{

	/**
	 * 音视频url日志
	 * @param model
	 * @param entry
	 * @param request
	 * @param response
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping(value={"list",""})
	public String list(Model model,@ModelAttribute("log")MmFileDigestLog entry,HttpServletRequest request, HttpServletResponse response) throws ClientProtocolException, IOException {
		
		try {
			
			PageLog<MmFileDigestLog> page = new PageLog<MmFileDigestLog>(request, response);
			Map<String, Object> params=new HashMap<>();
			params.put("pageSize", page.getPageSize());
			params.put("pageNo", page.getPageNo());
			//查询值判断
			initLogSearchValue(entry,params);
			
			List<FunctionServiceDict> serviceList = DictUtils.getFunctionServiceDictList(entry.getFunctionId());
			model.addAttribute("serviceList", serviceList);
			
			String url = "";
			url = Constants.LOG_BASE_URL+Constants.NTC_MMFILEDIGEST_LOG;
			String jsonString = HttpClientUtil.getMsg(url,params,request);
			
			Gson gson = new GsonBuilder().create();
			//gson泛型支持
			LogRecvData<MmFileDigestLog> fromJson = gson.fromJson(jsonString, new TypeToken<LogRecvData<MmFileDigestLog>>(){}.getType());
			
			if (fromJson.getStatus().intValue() == 200) {
				Page<MmFileDigestLog> data = fromJson.getData();
				page.setCount(data.getCount());
				page.setLast(data.getLast());
				page.setList(data.getList());
				List<MmFileDigestLog> list = page.getList();
				for (MmFileDigestLog l : list) {
					l.setFunctionId(entry.getFunctionId());
					setLogAction(l,serviceList);
				}
				model.addAttribute("page", page);
				logger.info("查询文件摘要日志成功");
			}
		} catch (Exception e) {
			logger.error("查询文件摘要日志失败", e);
			addMessageLog(model, e.getMessage());
		}
		return "/log/ntc/mmFileDigestList";
	}
	
}
