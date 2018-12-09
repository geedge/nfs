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
 * @ClassName: ComplexStringCfg.java 
 * @Description: 通用增强字符串类配置[后期业务扩展使用]
 * @author (zhangwei)
 * @date 2018年11月8日 下午2:00:52
 * @version V1.0
 */
public class ComplexStringCfg extends ComplexkeywordCfg {
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	private static final long serialVersionUID = -2248641720522865575L;
	private static final String tableName="complex_keyword_cfg";
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
