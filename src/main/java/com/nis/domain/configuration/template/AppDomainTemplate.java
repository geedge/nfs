/**
 *@Title: BaseStringConfig.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2018年2月5日 下午5:26:02
 *@version  版本号
 */
package com.nis.domain.configuration.template;

import com.nis.util.excel.ExcelField;

/**
 * @Description: excel导入字符串类配置
 */
public class AppDomainTemplate extends BasicTemplate{
	private String cfgDesc;
	private String cfgKeywords;
	private Integer matchMethod ;
	private Integer isHex;
	private Integer isCaseInsenstive;
	
	@ExcelField(title="config_describe",align=2,sort=1)
	public String getCfgDesc() {
		return cfgDesc;
	}
	public void setCfgDesc(String cfgDesc) {
		this.cfgDesc = cfgDesc;
	}
	@ExcelField(title="domain_name",sort=2)
	public String getCfgKeywords() {
		return cfgKeywords;
	}
	public void setCfgKeywords(String cfgKeywords) {
		this.cfgKeywords = cfgKeywords;
	}
	@ExcelField(title="match_method",dictType="MATCH_METHOD",sort=13)
	public Integer getMatchMethod() {
		return matchMethod;
	}
	public void setMatchMethod(Integer matchMethod) {
		this.matchMethod = matchMethod;
	}
	@ExcelField(title="is_hex",dictType="IS_HEX",sort=14)
	public Integer getIsHex() {
		return isHex;
	}
	public void setIsHex(Integer isHex) {
		this.isHex = isHex;
	}
	@ExcelField(title="is_case_insenstive",dictType="CASE_INSENSTIVE",sort=15)
	public Integer getIsCaseInsenstive() {
		return isCaseInsenstive;
	}
	public void setIsCaseInsenstive(Integer isCaseInsenstive) {
		this.isCaseInsenstive = isCaseInsenstive;
	}
}
