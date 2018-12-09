/**
 *@Title: BaseStringConfig.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2018年2月5日 下午5:26:02
 *@version  版本号
 */
package com.nis.domain.configuration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @ClassName: BaseStringConfig.java 
 * @Description: 基础字符串类配置
 * @author (dell)
 * @date 2018年2月5日 下午5:26:02
 * @version V1.0
 */
public class AppStringCfg extends BaseStringCfg<AppStringCfg> {

	private static final String tableName="app_string_cfg";
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	private static final long serialVersionUID = 4938724489988587634L;
	@Expose
	private Integer compileId;
	@Expose
	private String ratelimit;
	
	/* (non-Javadoc)
	 * @see com.nis.domain.configuration.BaseCfg#initDefaultValue()
	 */
	@Override
	public void initDefaultValue() {
		// TODO Auto-generated method stub
		super.initDefaultValue();
		this.isHexbin = 0;
	}
	
	public Integer getCompileId() {
		return compileId;
	}
	public void setCompileId(Integer compileId) {
		this.compileId = compileId;
	}

	public static String getTablename() {
		return tableName;
	}

	public String getRatelimit() {
		return ratelimit;
	}

	public void setRatelimit(String ratelimit) {
		this.ratelimit = ratelimit;
	}
	
}
