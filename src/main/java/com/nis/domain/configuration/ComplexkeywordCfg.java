/**
 *@Title: BaseEnhancedStringConfig.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2018年2月5日 下午5:42:52
 *@version  版本号
 */
package com.nis.domain.configuration;

import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.taglibs.standard.functions.Functions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nis.util.excel.ExcelField;

/**
 * @ClassName: BaseEnhancedStringConfig.java 
 * @Description: 基础增强字符串类配置
 * @author (wx)
 * @date 2018年2月5日 下午5:42:52
 * @version V1.0
 */
public class ComplexkeywordCfg extends BaseCfg<ComplexkeywordCfg>{
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -1417089397345367232L;
	/**
	 * 匹配区域
	 */
	@Expose
	@ExcelField(title="district",sort=301)
	@SerializedName("district")
	protected String district ;
	protected String districtShowName;
	/**
	 * 关键字
	 */
	@Expose
	@SerializedName("keywords")
	@ExcelField(title="key_word",sort=302)
	protected String cfgKeywords;
	protected String keywordsShowName;
	/**
	 * 表达式类型
	 */
	@Expose
	@SerializedName("exprType")
	@ExcelField(title="expression_type",dictType="EXPRESSION_TYPE",sort=303)
	protected Integer exprType ;
	/**
	 * 匹配方式
	 */
	@Expose
	@ExcelField(title="match_method",dictType="MATCH_METHOD",sort=304)
	@SerializedName("matchMethod")
	protected Integer matchMethod ;
	
	/**
	 * 是否hex
	 */
	@ExcelField(title="is_hex",sort=305)
	protected Integer isHex;
	/**
	 * 是否hex
	 */
	@ExcelField(title="is_case_insenstive",sort=306)
	protected Integer isCaseInsenstive;
	
	/**
	 * 是否hex二进制
	 */
	@Expose
	@SerializedName("isHexbin")
	protected Integer isHexbin ;
	/**
	 * district
	 * @return  district
	*/
	
	public String getDistrict() {
		return district;
	}
	/**
	 * copy属性使用
	 */
	protected Long dnsStrategyId;
	protected Integer appCode;//仅用于copy属性使用
	protected Integer behavCode;//仅用于copy属性使用
	protected Integer specServiceId;//仅用于copy属性使用
	
	public Integer getAppCode() {
		return appCode;
	}
	public void setAppCode(Integer appCode) {
		this.appCode = appCode;
	}
	public Integer getBehavCode() {
		return behavCode;
	}
	public void setBehavCode(Integer behavCode) {
		this.behavCode = behavCode;
	}
	public Integer getSpecServiceId() {
		return specServiceId;
	}
	public void setSpecServiceId(Integer specServiceId) {
		this.specServiceId = specServiceId;
	}
	public Long getDnsStrategyId() {
		return dnsStrategyId;
	}
	public void setDnsStrategyId(Long dnsStrategyId) {
		this.dnsStrategyId = dnsStrategyId;
	}
	/**
	 * @param district the district to set
	 */
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getCfgKeywords() {
//		cfgKeywords = StringEscapeUtils.unescapeHtml4(cfgKeywords);
		return cfgKeywords;
	}
	public void setCfgKeywords(String cfgKeywords) {
		this.cfgKeywords = cfgKeywords;
	}
	public String getDistrictShowName() {
		return districtShowName;
	}
	public void setDistrictShowName(String districtShowName) {
		this.districtShowName = districtShowName;
	}
	/**
	 * exprType
	 * 继承此类的类会覆盖该方法写自己的缺省值
	 * @return  exprType
	*/
	
	public Integer getExprType() {
		return this.exprType;
	}
	/**
	 * @param exprType the exprType to set
	 */
	public void setExprType(Integer exprType) {
		this.exprType = exprType;
	}
	/**
	 * matchMethod
	 * 继承此类的类会覆盖该方法写自己的缺省值
	 * @return  matchMethod
	*/
	
	public Integer getMatchMethod() {
		return this.matchMethod;
	}
	/**
	 * @param matchMethod the matchMethod to set
	 */
	public void setMatchMethod(Integer matchMethod) {
		this.matchMethod = matchMethod;
	}
	/**
	 * isHexbin
	 * @return  isHexbin
	*/
	
	public Integer getIsHexbin() {
		return isHexbin;
	}
	/**
	 * @param isHexbin the isHexbin to set
	 */
	public void setIsHexbin(Integer isHexbin) {
		this.isHexbin = isHexbin;
	}
	public Integer getIsHex() {
		return isHex;
	}
	public void setIsHex(Integer isHex) {
		this.isHex =isHex;
	}
	public Integer getIsCaseInsenstive() {
		return isCaseInsenstive;
	}
	public void setIsCaseInsenstive(Integer isCaseInsenstive) {
		this.isCaseInsenstive =isCaseInsenstive;
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
	
	public static List<ComplexkeywordCfg> replaceComplexKeyList(List<ComplexkeywordCfg> list){
		for (int i = 0; i < list.size(); i++) {
			ComplexkeywordCfg  base=(ComplexkeywordCfg)list.get(i);
			base.setIsHex(base.getIsHexbin());
			base.setIsCaseInsenstive(base.getIsHexbin());
			base.setCfgKeywords(Functions.replace(base.getCfgKeywords(), "***and***"," "));
	 }
		return list;
	}
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
}
