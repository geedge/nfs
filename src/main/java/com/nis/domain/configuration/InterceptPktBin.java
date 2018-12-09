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
public class InterceptPktBin extends BaseStringCfg<InterceptPktBin> {
	private static final String tableName="intercept_pkt_bin";
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
	}

	/**
	 * tablename
	 * @return  tablename
	*/
	
	public static String getTablename() {
		return tableName;
	}
	
}
