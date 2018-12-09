/**
 *@Title: SipIpConfig.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2018年2月6日 上午9:12:51
 *@version  版本号
 */
package com.nis.domain.configuration;

/**
 * @ClassName: SipIpConfig.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年2月6日 上午9:12:51
 * @version V1.0
 */
public class SipIpCfg extends BaseIpCfg {
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 8707774919555732957L;
	
	/* (non-Javadoc)
	 * @see com.nis.domain.configuration.BaseIpCfg#initDefaultValue()
	 */
	@Override
	public void initDefaultValue() {
		// TODO Auto-generated method stub
		super.initDefaultValue();
		this.protocolId=17;
	}
	
}
