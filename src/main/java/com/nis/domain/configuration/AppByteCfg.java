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
import com.nis.util.excel.ExcelField;

/**
 * @Description: APP策略配置
 * @author (dell)
 * @date 2018年2月5日 下午5:26:02
 * @version V1.0
 */
public class AppByteCfg extends BaseCfg<AppByteCfg> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3350695484085989968L;
	private static final String tableName="app_byte_cfg";
	@Expose
	private Integer compileId;
	@Expose
	private String ratelimit;
	private Integer appCode;//specific_service_cfg表一级节点的spec_service_code
	private Integer behavCode;//specific_service_cfg表二级节点的spec_service_code
	private Integer specServiceId;
	private String bytes;
	private String cfgKeywords;
	private String appName;

	@Expose
	@ExcelField(title="expression_type")
	@SerializedName("exprType")
	protected Integer exprType ;

	@Expose
	@ExcelField(title="match_method")
	@SerializedName("matchMethod")
	protected Integer matchMethod ;

	@Expose
	@ExcelField(title="whether_hexbinary")
	@SerializedName("isHexbin")
	protected Integer isHexbin;

	public Integer getExprType() {
		return exprType;
	}

	public void setExprType(Integer exprType) {
		this.exprType = exprType;
	}

	public Integer getMatchMethod() {
		return matchMethod;
	}

	public void setMatchMethod(Integer matchMethod) {
		this.matchMethod = matchMethod;
	}

	public Integer getIsHexbin() {
		return isHexbin;
	}

	public void setIsHexbin(Integer isHexbin) {
		this.isHexbin = isHexbin;
	}
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
	public Integer getAppCode() {
		return appCode;
	}
	public void setAppCode(Integer appCode) {
		this.appCode = appCode;
	}
	public Integer getSpecServiceId() {
		return specServiceId;
	}
	public void setSpecServiceId(Integer specServiceId) {
		this.specServiceId = specServiceId;
	}

	public String getBytes() {
		return bytes;
	}

	public void setBytes(String bytes) {
		this.bytes = bytes;
	}

	public String getCfgKeywords() {
		return cfgKeywords;
	}

	public void setCfgKeywords(String cfgKeywords) {
		this.cfgKeywords = cfgKeywords;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Integer getBehavCode() {
		return behavCode;
	}

	public void setBehavCode(Integer behavCode) {
		this.behavCode = behavCode;
	}
	
}
