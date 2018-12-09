/**
 *@Title: DnsDomainConfig.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2018年2月5日 下午3:34:14
 *@version  版本号
 */
package com.nis.domain.configuration;

/**
 * @ClassName: DnsDomainConfig.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年2月5日 下午3:34:14
 * @version V1.0
 */
public class DnsDomainCfg extends ComplexkeywordCfg {
	private static final String tableName="dns_domain_cfg";
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -2761078174512000577L;
	
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
		this.district="QNAME";
	}

	/**
	 * tablename
	 * @return  tablename
	*/
	
	public static String getTablename() {
		return tableName;
	}
	
}
