package com.nis.domain.configuration;

public class P2pHashCfg extends BaseStringCfg<P2pHashCfg>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7072353181349049294L;
	private static final String tableName="p2p_hash_cfg";
	
	@Override
	public void initDefaultValue() {
		// TODO Auto-generated method stub
		super.initDefaultValue();
		this.exprType=0;
		this.matchMethod=0;
	}
	
	public static String getTablename() {
		return tableName;
	}
	
	
}
