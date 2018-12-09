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
public class HttpResHeadCfg extends ComplexkeywordCfg {
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 1306491838846197119L;
	private static final String tableName="http_res_head_cfg";

	/* (non-Javadoc)
	 * @see com.nis.domain.configuration.ComplexkeywordCfg#initDefaultValue()
	 */
	@Override
	public void initDefaultValue() {
		// TODO Auto-generated method stub
		super.initDefaultValue();
		this.exprType=0;
		this.matchMethod=0;
		this.exprType=0;
	}

	public static String getTablename() {
		return tableName;
	}
	
}
