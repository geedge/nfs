/**
 *@Title: MailAttachContentConfig.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2018年2月5日 下午6:40:26
 *@version  版本号
 */
package com.nis.domain.configuration;

/**
 * @ClassName: MailAttachContentConfig.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年2月5日 下午6:40:26
 * @version V1.0
 */
public class MailKeywordCfg extends ComplexkeywordCfg {
	private static final String tableName = "mail_keyword_cfg";
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -5535627006599408223L;
	/* (non-Javadoc)
	 * @see com.nis.domain.configuration.ComplexkeywordCfg#initDefaultValue()
	 */
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
