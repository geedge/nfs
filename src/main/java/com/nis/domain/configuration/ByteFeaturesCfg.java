/**
 *@Title: ByteFeaturesConfig.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2018年2月6日 上午10:36:22
 *@version  版本号
 */
package com.nis.domain.configuration;

/**
 * @ClassName: ByteFeaturesConfig.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年2月6日 上午10:36:22
 * @version V1.0
 */
public class ByteFeaturesCfg extends BaseStringCfg<ByteFeaturesCfg> {
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 7790855206924149377L;
	
	/* (non-Javadoc)
	 * @see com.nis.domain.configuration.BaseStringCfg#initDefaultValue()
	 */
	@Override
	public void initDefaultValue() {
		// TODO Auto-generated method stub
		super.initDefaultValue();
		this.exprType=3;
		this.matchMethod=0;
	}
	
}
