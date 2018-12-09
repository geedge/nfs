/**
 *@Title: L2tpUrlConfig.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2018年2月6日 上午8:42:42
 *@version  版本号
 */
package com.nis.domain.configuration;

/**
 * @ClassName: L2tpUrlConfig.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年2月6日 上午8:42:42
 * @version V1.0
 */
public class L2tpUrlCfg extends BaseStringCfg<L2tpUrlCfg> {
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -5003009573787886287L;
	
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
	
}
