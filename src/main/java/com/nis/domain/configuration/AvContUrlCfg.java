package com.nis.domain.configuration;

/**
 * @ClassName: AvContUrlCfg.java 
 * @Description: 音视频Cont URL
 * @author (dell)
 * @date 2018年2月6日 上午8:29:26
 * @version V1.0
 */
public class AvContUrlCfg extends BaseStringCfg<AvContUrlCfg> {
	private static final String tableName="av_cont_url_cfg";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1249923766365462700L;
	
	@Override
	public void initDefaultValue() {
		super.initDefaultValue();
		this.exprType=0;
		this.matchMethod=0;
	}
	
	public static String getTablename() {
		return tableName;
	}
	
	
}
