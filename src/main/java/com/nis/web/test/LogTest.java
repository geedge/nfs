package com.nis.web.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.util.FileUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nis.domain.Page;
import com.nis.domain.log.NtcIpLog;
import com.nis.domain.maat.LogRecvData;

@Controller
@RequestMapping("test")
public class LogTest {
	
	@ResponseBody
	@RequestMapping("logTest")
	public String testservice(HttpServletRequest request) {
		String recv = "";
		try {
			String path = request.getClass().getClassLoader().getResource("").getPath();
			recv = FileUtil.readAsString(new File(path + "com/nis/web/test/logTest.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(61000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static void main(String[] args) throws IOException {
		String readAsString = FileUtil.readAsString(new File("d:/test/logTest.txt"));
		
		Gson gson = new GsonBuilder().create();
		//gson泛型支持
		LogRecvData<NtcIpLog> fromJson = gson.fromJson(readAsString, new TypeToken<LogRecvData<NtcIpLog>>(){}.getType());
		
		Page<NtcIpLog> data = fromJson.getData();
		System.out.println(data.getPageNo());
		List<NtcIpLog> list = data.getList();
		NtcIpLog object = data.getList().get(0);
		System.out.print(object.getsPort());
	}
}
