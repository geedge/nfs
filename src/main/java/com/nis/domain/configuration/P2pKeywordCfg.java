package com.nis.domain.configuration;

public class P2pKeywordCfg extends BaseStringCfg<P2pKeywordCfg>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1308668362397070497L;
	private static final String tableName="p2p_keyword_cfg";
	
	@Override
	public void initDefaultValue() {
		// TODO Auto-generated method stub
		super.initDefaultValue();
		this.exprType=0;
		this.matchMethod=0;
		this.isHexbin=0;
	}

	public static String getTablename() {
		return tableName;
	}
}
