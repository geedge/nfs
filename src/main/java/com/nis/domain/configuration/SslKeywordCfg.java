/**
 *@Title: SslCertConfig.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2018年2月6日 上午8:59:51
 *@version  版本号
 */
package com.nis.domain.configuration;

/**
 * @ClassName: SslCertConfig.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年2月6日 上午8:59:51
 * @version V1.0
 */
public class SslKeywordCfg extends BaseStringCfg<SslKeywordCfg>{
	public static final String tableName="ssl_keyword_cfg";
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -6121146112218278084L;

	/* (non-Javadoc)
	 * @see com.nis.domain.configuration.ComplexkeywordCfg#initDefaultValue()
	 */
	@Override
	public void initDefaultValue() {
		// TODO Auto-generated method stub
		super.initDefaultValue();
		this.exprType=0;
		this.matchMethod=3;
		this.isHexbin=0;
	}

	public static String getTablename() {
		return tableName;
	}
	
}
