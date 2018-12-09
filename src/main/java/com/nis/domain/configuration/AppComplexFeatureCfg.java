/**
 *@Title: BaseStringConfig.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2018年2月5日 下午5:26:02
 *@version  版本号
 */
package com.nis.domain.configuration;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nis.util.excel.ExcelField;

/**
 * APP Header层头特征配置
 * @author dell
 *
 */
public class AppComplexFeatureCfg extends BaseCfg<AppComplexFeatureCfg> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5718725405113991364L;
	/**
	 * 
	 */
	private static final String tableName="app_complex_feature_cfg";
	@Expose
	@ExcelField(title="cfg_id",sort=0)
	private Integer compileId;
	private Integer appCode;//specific_service_cfg表一级节点的spec_service_code
	private Integer behavCode;//specific_service_cfg表二级节点的spec_service_code
	private Integer specServiceId;
	@ExcelField(title="district",sort=1)
	private String district;
	@ExcelField(title="key_word",sort=201)
	private String cfgKeywords;
	private String appName;
	private List<AppIpCfg> ipPortList = new ArrayList();
	private String cfgRegionCode1;
	public String getCfgRegionCode1() {
		return cfgRegionCode1;
	}

	public void setCfgRegionCode1(String cfgRegionCode1) {
		this.cfgRegionCode1 = cfgRegionCode1;
	}

	/**
	 * 表达式类型
	 */
	@Expose
	@ExcelField(title="expression_type",dictType="EXPRESSION_TYPE",sort=202)
	@SerializedName("exprType")
	protected Integer exprType ;
	/**
	 * 匹配方式
	 */
	@Expose
	@ExcelField(title="match_method",dictType="MATCH_METHOD",sort=203)
	@SerializedName("matchMethod")
	protected Integer matchMethod ;
	/**
	 * 是否hex
	 */
	@ExcelField(title="is_hex",sort=204)
	protected Integer isHex;
	/**
	 * 是否hex
	 */
	@ExcelField(title="is_case_insenstive",sort=205)
	protected Integer isCaseInsenstive;
	/**
	 * 是否hex二进制
	 */
	@Expose
	@SerializedName("isHexbin")
	protected Integer isHexbin;
	
	

	public Integer getIsHex() {
		return isHex;
	}

	public void setIsHex(Integer isHex) {
		this.isHex = isHex;
	}

	public Integer getIsCaseInsenstive() {
		return isCaseInsenstive;
	}

	public void setIsCaseInsenstive(Integer isCaseInsenstive) {
		this.isCaseInsenstive = isCaseInsenstive;
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

	public List<AppIpCfg> getIpPortList() {
		if(ipPortList.size()==0){
			ipPortList.add(new AppIpCfg());
		}
		return ipPortList;
	}

	public void setIpPortList(List<AppIpCfg> ipPortList) {
		this.ipPortList = ipPortList;
	}
	
}
