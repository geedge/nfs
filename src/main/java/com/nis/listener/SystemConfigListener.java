package com.nis.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.nis.datasource.CustomerContextHolder;

/**
 * 项目启动时加载数据字典
 * 
 * @author RenKaiGe-Office
 * @Date:2016-11-03
 */
public class SystemConfigListener extends ContextLoaderListener {
	private static WebApplicationContext webApplicationContext;

	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_B);
			webApplicationContext = WebApplicationContextUtils
					.getRequiredWebApplicationContext(event.getServletContext());
			CustomerContextHolder.clearCustomerType();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
