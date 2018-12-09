/**
 *@Title: MmsUrlConfig.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2018年2月6日 上午9:48:36
 *@version  版本号
 */
package com.nis.domain.configuration;

/**
 * @ClassName: MmsUrlConfig.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年2月6日 上午9:48:36
 * @version V1.0
 */
public class MmsUrlCfg extends BaseStringCfg<MmsUrlCfg>{
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 1057620148828322308L;
	
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
