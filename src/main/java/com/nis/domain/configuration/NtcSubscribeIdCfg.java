package com.nis.domain.configuration;

public class NtcSubscribeIdCfg extends BaseStringCfg<NtcSubscribeIdCfg> {
	private static final String tableName="ntc_subscribe_id_cfg";
	/**
	 * 
	 */
	private static final long serialVersionUID = 9137401459733286997L;
	/**
	 * 配置关键字
	 */
	
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
