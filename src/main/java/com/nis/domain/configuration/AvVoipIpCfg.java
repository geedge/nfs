package com.nis.domain.configuration;

/**
 * @ClassName: AvVoipIpCfg.java 
 * @Description: 音视频VOIP
 * @author (dell)
 * @date 2018年2月6日 上午8:29:26
 * @version V1.0
 */
public class AvVoipIpCfg extends BaseIpCfg {
	private static final String tableName="av_voip_ip_cfg";
	/**
	 * 
	 */
	private static final long serialVersionUID = -942354218504312548L;
	
	public static String getTablename() {
		return tableName;
	}
	
}
