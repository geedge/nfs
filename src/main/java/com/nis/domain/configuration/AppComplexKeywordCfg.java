/**
 *@Title: BaseEnhancedStringConfig.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2018年2月5日 下午5:42:52
 *@version  版本号
 */
package com.nis.domain.configuration;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @ClassName: BaseEnhancedStringConfig.java 
 * @Description: APP增强字符串类配置
 * @author (zhangwei)
 * @date 2018年4月3日 下午5:42:52
 * @version V1.0
 */
public class AppComplexKeywordCfg extends ComplexkeywordCfg{
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8058690163147881374L;
	@Expose
	private Integer compileId;
	
	public Integer getCompileId() {
		return compileId;
	}
	public void setCompileId(Integer compileId) {
		this.compileId = compileId;
	}
	/* (non-Javadoc)
	 * @see com.nis.domain.configuration.BaseCfg#initDefaultValue()
	 */
	@Override
	public void initDefaultValue() {
		// TODO Auto-generated method stub
		super.initDefaultValue();
		this.exprType=0;
		this.isHexbin = 0 ;
		this.matchMethod=3;
	}
}
