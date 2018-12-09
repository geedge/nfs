/**
 *@Title: SipTerminalConfig.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2018年2月6日 上午9:42:15
 *@version  版本号
 */
package com.nis.domain.configuration;

/**
 * @ClassName: SipTerminalConfig.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年2月6日 上午9:42:15
 * @version V1.0
 */
public class SipTerminalCfg extends ComplexkeywordCfg{
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 7164399463108973251L;

	/* (non-Javadoc)
	 * @see com.nis.domain.configuration.ComplexkeywordCfg#initDefaultValue()
	 */
	@Override
	public void initDefaultValue() {
		// TODO Auto-generated method stub
		super.initDefaultValue();
		this.district="TERMINAL_CALL";
		this.exprType=0;
		this.matchMethod=3;
		this.isHexbin=0;
	}
	
}
