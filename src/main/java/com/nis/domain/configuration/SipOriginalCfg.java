/**
 *@Title: SipOriginalConfig.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2018年2月6日 上午9:25:49
 *@version  版本号
 */
package com.nis.domain.configuration;

import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName: SipOriginalConfig.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年2月6日 上午9:25:49
 * @version V1.0
 */
public class SipOriginalCfg extends ComplexkeywordCfg {
	public static final String tableName="sip_original_cfg";
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -8113106451373646902L;

	/* (non-Javadoc)
	 * @see com.nis.domain.restful.BaseEnhancedStringConfig#getDistrict()
	 */
	@Override
	public String getDistrict() {
		if(StringUtils.isBlank(this.district))
			return "ORIGINAL_CALL";
		else
			return this.district;
	}
	/* (non-Javadoc)
	 * @see com.nis.domain.restful.BaseStringConfig#getExprType()
	 */
	@Override
	public Integer getExprType() {
		if(this.exprType==null)
			return 0;
		else
			return this.exprType;
	}
	/* (non-Javadoc)
	 * @see com.nis.domain.restful.BaseStringConfig#getMatchMethod()
	 */
	@Override
	public Integer getMatchMethod() {
		if(this.matchMethod==null)
			return 3;
		else
			return this.matchMethod;
	}
	/* (non-Javadoc)
	 * @see com.nis.domain.configuration.ComplexkeywordCfg#initDefaultValue()
	 */
	@Override
	public void initDefaultValue() {
		// TODO Auto-generated method stub
		super.initDefaultValue();
		this.district="ORIGINAL_CALL";
		this.exprType=0;
		this.matchMethod=3;
		this.isHexbin=0;
	}
	
}
