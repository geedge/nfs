/**
 *@Title: FtpAddrConfig.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2018年2月6日 上午8:18:55
 *@version  版本号
 */
package com.nis.domain.configuration;

/**
 * @ClassName: FtpKeywordCfg.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年2月6日 上午8:18:55
 * @version V1.0
 */
public class FtpKeywordCfg extends BaseStringCfg<FtpKeywordCfg>{
	private static final String tableName="ftp_keyword_cfg";
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -7252491991496038882L;
	/* (non-Javadoc)
	 * @see com.nis.domain.configuration.BaseStringCfg#initDefaultValue()
	 */
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
