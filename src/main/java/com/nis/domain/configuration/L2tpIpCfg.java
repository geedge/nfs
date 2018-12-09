/**
 *@Title: L2tpIpConfig.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2018年2月6日 上午8:30:46
 *@version  版本号
 */
package com.nis.domain.configuration;

/**
 * @ClassName: L2tpIpConfig.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年2月6日 上午8:30:46
 * @version V1.0
 */
public class L2tpIpCfg extends BaseIpCfg {
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 2559118696091896747L;
	
	/* (non-Javadoc)
	 * @see com.nis.domain.configuration.BaseIpCfg#initDefaultValue()
	 */
	@Override
	public void initDefaultValue() {
		// TODO Auto-generated method stub
		super.initDefaultValue();
		this.protocolId=12;
	}
	
}
