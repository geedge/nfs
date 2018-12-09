/**
 *@Title: HttpDomainConfig.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2018年2月5日 下午5:33:05
 *@version  版本号
 */
package com.nis.domain.configuration;

/**
 * @ClassName: HttpDomainConfig.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年2月5日 下午5:33:05
 * @version V1.0
 */
public class HttpBodyCfg extends BaseStringCfg<HttpBodyCfg> {
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	private static final String tableName="http_body_cfg";	
	private static final long serialVersionUID = 8231984530204284334L;
	/* (non-Javadoc)
	 * @see com.nis.domain.configuration.BaseStringCfg#initDefaultValue()
	 */
	@Override
	public void initDefaultValue() {
		// TODO Auto-generated method stub
		super.initDefaultValue();
		this.matchMethod=0;
	}
	public static String getTablename() {
		return tableName;
	}
	private String replaceContent;
	public String getReplaceContent() {
		return replaceContent;
	}
	public void setReplaceContent(String replaceContent) {
		this.replaceContent = replaceContent;
	}
	
}
