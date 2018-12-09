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
 * @Description: 主题网站配置
 * @author (dell)
 * @date 2018年2月5日 下午5:26:02
 * @version V1.0
 */
public class AppTopicDomainCfg extends BaseCfg<AppTopicDomainCfg> {

	private static final long serialVersionUID = -442502179959427340L;
	
	private static final String tableName="app_topic_domain_cfg";
	@Expose
	@ExcelField(title="cfg_id",sort=0)
	private Integer compileId;
	@Expose
	private String ratelimit;
//	private Integer appCode;//specific_service_cfg表一级节点的spec_service_code
	@ExcelField(title="website_server",dictType="WEBSITE_SERVER",sort=3)
	private Long websiteServiceId;//specific_service_cfg表二级节点的spec_service_code
	private Integer specServiceId;
	
	@ExcelField(title="domain",sort=4)
	private String cfgKeywords;
	private String appName;
	@ExcelField(title="topic",dictType="TOPIC",sort=2)
	private Long topicId;
	
	/**
	 * 表达式类型
	 */
	@Expose
	@ExcelField(title="expression_type",dictType="EXPRESSION_TYPE",sort=7)
	@SerializedName("exprType")
	protected Integer exprType ;
	/**
	 * 匹配方式
	 */
	@Expose
	@ExcelField(title="match_method",dictType="MATCH_METHOD",sort=8)
	@SerializedName("matchMethod")
	protected Integer matchMethod ;

	@Expose
	@SerializedName("isHexbin")
	protected Integer isHexbin;
	
	/**
	 * 是否hex
	 */
	@ExcelField(title="is_hex",sort=5)
	protected Integer isHex;
	/**
	 * 是否hex
	 */
	@ExcelField(title="is_case_insenstive",sort=6)
	protected Integer isCaseInsenstive;

	

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

	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
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
		super.initDefaultValue();
		this.isHexbin = 0;
		this.doLog = 0;//app特征域不记录日志
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
//	public Integer getAppCode() {
//		return appCode;
//	}
//	public void setAppCode(Integer appCode) {
//		this.appCode = appCode;
//	}
	public Integer getSpecServiceId() {
		return specServiceId;
	}
	public void setSpecServiceId(Integer specServiceId) {
		this.specServiceId = specServiceId;
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

	public Long getWebsiteServiceId() {
		return websiteServiceId;
	}

	public void setWebsiteServiceId(Long websiteServiceId) {
		this.websiteServiceId = websiteServiceId;
	}

}
