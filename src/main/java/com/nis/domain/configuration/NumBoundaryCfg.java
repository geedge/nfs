/**
 *@Title: NumBoundaryConfig.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2018年2月6日 上午10:37:45
 *@version  版本号
 */
package com.nis.domain.configuration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @ClassName: NumBoundaryConfig.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年2月6日 上午10:37:45
 * @version V1.0
 */
public class NumBoundaryCfg extends BaseCfg<NumBoundaryCfg> {

	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -7655744880438502818L;
	/**
	 * 数值下界
	 */
	@Expose
	@SerializedName("lowBounadry")
	protected Long lowBounadry;
	/**
	 * 数值上界
	 */
	@Expose
	@SerializedName("upBoundary")
	protected Long upBoundary;
	/* (non-Javadoc)
	 * @see com.nis.domain.configuration.BaseCfg#initDefaultValue()
	 */
	@Override
	public void initDefaultValue() {
		// TODO Auto-generated method stub
		super.initDefaultValue();
	}
	/**
	 * lowBounadry
	 * @return  lowBounadry
	*/
	
	public Long getLowBounadry() {
		return lowBounadry;
	}
	/**
	 * @param lowBounadry the lowBounadry to set
	 */
	public void setLowBounadry(Long lowBounadry) {
		this.lowBounadry = lowBounadry;
	}
	/**
	 * upBoundary
	 * @return  upBoundary
	*/
	
	public Long getUpBoundary() {
		return upBoundary;
	}
	/**
	 * @param upBoundary the upBoundary to set
	 */
	public void setUpBoundary(Long upBoundary) {
		this.upBoundary = upBoundary;
	}
}
