package com.nis.web.controller.configuration;

import org.springframework.stereotype.Controller;

/**
 * 隧道协议相关配置控制类
 * @author dell
 *
 */
@Controller
public class TunnelCfgController {

	/**
	 * 
	 * @return
	 */
	public String l2tpList(){
		return "/configuration/tunnel/l2tpList";
	}
}
