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
 * APP TCP会话字节数特征
 * @author dell
 *
 */
public class AppTcpCfg extends BaseCfg<AppTcpCfg> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4366457794248757698L;
	private static final String tableName="app_session_cfg";
	@Expose
	private Integer compileId;
	@Expose
	private String ratelimit;
	private Integer appCode;//specific_service_cfg表一级节点的spec_service_code
	private Integer behavCode;//specific_service_cfg表二级节点的spec_service_code
	private Integer specServiceId;
	private String district;
	private String cfgKeywords;
	private String appName;
	
	/**
	 * 数值下界
	 */
	protected Integer lowBoundary;
	/**
	 * 数值上界
	 */
	protected Integer upBoundary;

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


	


	public Integer getLowBoundary() {
		return lowBoundary;
	}

	public void setLowBoundary(Integer lowBoundary) {
		this.lowBoundary = lowBoundary;
	}

	public Integer getUpBoundary() {
		return upBoundary;
	}

	public void setUpBoundary(Integer upBoundary) {
		this.upBoundary = upBoundary;
	}

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

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
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
