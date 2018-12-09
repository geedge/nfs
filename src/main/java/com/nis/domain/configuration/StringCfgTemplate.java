/**
 *@Title: BaseStringConfig.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2018年2月5日 下午5:26:02
 *@version  版本号
 */
package com.nis.domain.configuration;

import java.util.Date;

import org.apache.commons.lang3.StringEscapeUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nis.util.excel.ExcelField;

/**
 * @Description: excel导入字符串类配置
 */
public class StringCfgTemplate {

	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	

	private String cfgKeywords;
	private Integer exprType ;
	private Integer matchMethod ;
	private Integer isHexbin;
	private Long cfgId;
	private String cfgDesc;
	private Integer requestId;
	private String requestName;
	private Integer isAreaEffective;
	private String classify;
	private String attribute;
	private String lable;
	private String classifyName;
	private String attributeName;
	private String lableName;
	private String areaEffectiveIds ;

	@ExcelField(title="expression_type",align=2,sort=20)
	public Integer getExprType() {
		return exprType;
	}
	public void setExprType(Integer exprType) {
		this.exprType = exprType;
	}
	@ExcelField(title="match_method",align=2,sort=30)
	public Integer getMatchMethod() {
		return matchMethod;
	}
	public void setMatchMethod(Integer matchMethod) {
		this.matchMethod = matchMethod;
	}
	@ExcelField(title="whether_hexbinary",align=2,sort=40)
	public Integer getIsHexbin() {
		return isHexbin;
	}
	public void setIsHexbin(Integer isHexbin) {
		this.isHexbin = isHexbin;
	}
	@ExcelField(title="keywords",align=2,sort=11)
	public String getCfgKeywords() {
		return cfgKeywords;
	}
	public void setCfgKeywords(String cfgKeywords) {
		this.cfgKeywords = cfgKeywords;
	}
	public Long getCfgId() {
		return cfgId;
	}
	public void setCfgId(Long cfgId) {
		this.cfgId = cfgId;
	}
	@ExcelField(title="config_describe",align=2,sort=10)
	public String getCfgDesc() {
		return cfgDesc;
	}
	public void setCfgDesc(String cfgDesc) {
		this.cfgDesc = cfgDesc;
	}
	@ExcelField(title="letter",align=2,sort=12)
	public Integer getRequestId() {
		return requestId;
	}
	public void setRequestId(Integer requestId) {
		this.requestId = requestId;
	}
	public String getRequestName() {
		return requestName;
	}
	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}
	public Integer getIsAreaEffective() {
		return isAreaEffective;
	}
	public void setIsAreaEffective(Integer isAreaEffective) {
		this.isAreaEffective = isAreaEffective;
	}
	@ExcelField(title="classification",align=2,sort=71)
	public String getClassify() {
		return classify;
	}
	public void setClassify(String classify) {
		this.classify = classify;
	}
	@ExcelField(title="attribute",align=2,sort=72)
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	@ExcelField(title="label",align=2,sort=73)
	public String getLable() {
		return lable;
	}
	public void setLable(String lable) {
		this.lable = lable;
	}
	public String getClassifyName() {
		return classifyName;
	}
	public void setClassifyName(String classifyName) {
		this.classifyName = classifyName;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public String getLableName() {
		return lableName;
	}
	public void setLableName(String lableName) {
		this.lableName = lableName;
	}
//	@ExcelField(title="area_effective",align=2,sort=70)
	public String getAreaEffectiveIds() {
		return areaEffectiveIds;
	}
	public void setAreaEffectiveIds(String areaEffectiveIds) {
		this.areaEffectiveIds = areaEffectiveIds;
	}
}
