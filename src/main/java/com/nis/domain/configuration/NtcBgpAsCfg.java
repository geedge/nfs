package com.nis.domain.configuration;

public class NtcBgpAsCfg extends BaseStringCfg<NtcBgpAsCfg> {

	private static final String tableName="ntc_bgp_as_cfg";
	private static final long serialVersionUID = -3581535510691651087L;

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
