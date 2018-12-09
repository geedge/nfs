/**
 *@Title: MmsIpConfig.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2018年2月6日 上午9:47:49
 *@version  版本号
 */
package com.nis.domain.configuration;

/**
 * @ClassName: MmsIpConfig.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年2月6日 上午9:47:49
 * @version V1.0
 */
public class MmsIpCfg extends BaseIpCfg {
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 321679812572480924L;
	
	/* (non-Javadoc)
	 * @see com.nis.domain.configuration.BaseIpCfg#initDefaultValue()
	 */
	@Override
	public void initDefaultValue() {
		// TODO Auto-generated method stub
		super.initDefaultValue();
		this.protocolId=14;
	}
	
}
