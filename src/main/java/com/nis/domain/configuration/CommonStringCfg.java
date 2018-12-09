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
 * @ClassName: CommonStringConfig.java 
 * @Description: 通用字符串配置对象类[后期业务扩展使用]
 * @author (zhangwei)
 * @date 2018年11月8日 下午1:58:05
 * @version V1.0
 */
public class CommonStringCfg extends BaseStringCfg<CommonStringCfg> {
	
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）	 *
	 * @since 1.0.0
	 */
	private static final long serialVersionUID = -5912532490412412005L;
	private static final String tableName="common_keyword_cfg";	
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
