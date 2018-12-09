/**
 *@Title: BaseStringConfig.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2018年2月5日 下午5:26:02
 *@version  版本号
 */
package com.nis.domain.configuration;

import java.util.List;

import org.apache.taglibs.standard.functions.Functions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nis.util.excel.ExcelField;

/**
 * @ClassName: BaseStringConfig.java 
 * @Description: 基础字符串类配置
 * @author (dell)
 * @date 2018年2月5日 下午5:26:02
 * @version V1.0
 */
public class BaseStringCfg<T> extends BaseCfg<T> {

	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -2040598274024301785L;
	/**
	 * 配置关键字
	 */
	@Expose
	@SerializedName("keywords")
	@ExcelField(title="key_word",sort=201)
	protected String cfgKeywords;
	protected String cfgKeywordsShowName;
	protected Integer appCode;//仅用于copy属性使用
	protected Integer behavCode;//仅用于copy属性使用
	protected Integer specServiceId;//仅用于copy属性使用
	protected String domain;//仅用于copy属性使用
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
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
	/**
	 * @param cfgKeywords the cfgKeywords to set
	 */
	public void setCfgKeywords(String cfgKeywords) {
		this.cfgKeywords = cfgKeywords;
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
	
	private List<NtcSubscribeIdCfg> ntcSubscribeIdCfgList;
	private NtcSubscribeIdCfg ntcSubscribeIdCfg;
	private IpPortCfg ipPort;
	
	//http url来源于http关键字的标识
	private Integer sourceCompileId;
	
	
	public IpPortCfg getIpPort() {
		return ipPort;
	}
	public void setIpPort(IpPortCfg ipPort) {
		this.ipPort = ipPort;
	}
	/**
	 * exprType
	 * @return  exprType
	*/
	public Integer getExprType() {
		return exprType;
	}
	/**
	 * @param exprType the exprType to set
	 */
	public void setExprType(Integer exprType) {
		this.exprType = exprType;
	}
	/**
	 * matchMethod
	 * @return  matchMethod
	*/
	
	public Integer getMatchMethod() {
		return matchMethod;
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
	/* (non-Javadoc)
	 * @see com.nis.domain.configuration.BaseCfg#initDefaultValue()
	 */
	@Override
	public void initDefaultValue() {
		// TODO Auto-generated method stub
		super.initDefaultValue();
		this.isHexbin = 0;
	}
	/**
	 * cfgKeywordsShowName
	 * @return  cfgKeywordsShowName
	*/
	
	public String getCfgKeywordsShowName() {
		return cfgKeywordsShowName;
	}
	/**
	 * @param cfgKeywordsShowName the cfgKeywordsShowName to set
	 */
	public void setCfgKeywordsShowName(String cfgKeywordsShowName) {
		this.cfgKeywordsShowName = cfgKeywordsShowName;
	}
	/**
	 * cfgKeywords
	 * @return  cfgKeywords
	*/
	
	public String getCfgKeywords() {
//		cfgKeywords = StringEscapeUtils.unescapeHtml4(cfgKeywords);
		/*cfgKeywords = cfgKeywords.replace("&", "\\&");
		cfgKeywords = cfgKeywords.replace(" ", "\\b");
		cfgKeywords = cfgKeywords.replace("\\", "\\\\");
		cfgKeywords = cfgKeywords.replace(",", "&");*/
		return cfgKeywords;
	}
	public List<NtcSubscribeIdCfg> getNtcSubscribeIdCfgList() {
		return ntcSubscribeIdCfgList;
	}
	public void setNtcSubscribeIdCfgList(List<NtcSubscribeIdCfg> ntcSubscribeIdCfgList) {
		this.ntcSubscribeIdCfgList = ntcSubscribeIdCfgList;
	}
	public NtcSubscribeIdCfg getNtcSubscribeIdCfg() {
		return ntcSubscribeIdCfg;
	}
	public void setNtcSubscribeIdCfg(NtcSubscribeIdCfg ntcSubscribeIdCfg) {
		this.ntcSubscribeIdCfg = ntcSubscribeIdCfg;
	}
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
		this.isCaseInsenstive =isCaseInsenstive;
	}
	public Integer getSourceCompileId() {
		return sourceCompileId;
	}
	public void setSourceCompileId(Integer sourceCompileId) {
		this.sourceCompileId = sourceCompileId;
	}
	public static List<BaseStringCfg> replaceBaseKeyList(List<BaseStringCfg> list){
		for (int i = 0; i < list.size(); i++) {
			BaseStringCfg  base=(BaseStringCfg)list.get(i);
			base.setIsHex(base.getIsHexbin());
			base.setIsCaseInsenstive(base.getIsHexbin());
			base.setCfgKeywords(Functions.replace(base.getCfgKeywords(), "***and***"," "));
	 }
		return list;
	}
	
	public static List<BaseStringCfg> baseHexList(List<BaseStringCfg> list){
		for (int i = 0; i < list.size(); i++) {
			BaseStringCfg  base=(BaseStringCfg)list.get(i);
			base.setIsHex(base.getIsHexbin());
			base.setIsCaseInsenstive(base.getIsHexbin());
		}
		return list;
	}
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public Integer getRegionId() {
		return regionId;
	}
	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}
	
}
