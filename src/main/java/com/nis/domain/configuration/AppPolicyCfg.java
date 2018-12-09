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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nis.util.excel.ExcelField;

/**
 * @Description: APP策略配置
 * @author (dell)
 * @date 2018年2月5日 下午5:26:02
 * @version V1.0
 */
/**
 * @author Administrator
 *
 */
public class AppPolicyCfg extends BaseCfg<AppPolicyCfg> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5679998383250783571L;
	private static final String tableName="app_policy_cfg";
	private String indexTable="app_policy_cfg";
	@Expose
	@ExcelField(title="cfg_id",sort=0)
	private Integer compileId;
	@Expose
	private String ratelimit;
	private Integer appCode;//specific_service_cfg表一级节点的spec_service_code
	private Integer behavCode;//specific_service_cfg表二级节点的spec_service_code
	private Integer specServiceId;
	@ExcelField(title="basic_protocol",sort=5)
	private String appName;
	@ExcelField(title="behaviour_type",sort=6)
	private String behavName;
	private String cfgKeywords;
	private String userRegion1;
	@ExcelField(title="ratelimit",dictType="APP_RATE_LIMIT",sort=3)
	private String userRegion2;
	private String userRegion3;
	private String userRegion4;
	private String userRegion5;
	private String configType;
	
	@ExcelField(title="encrypted_tunnel_behavior",sort=5)
	private String behaviorName;
	@ExcelField(title="social_app",sort=5)
	private String socialName;
	
	
	

	public String getBehaviorName() {
		return behaviorName;
	}

	public void setBehaviorName(String behaviorName) {
		this.behaviorName = behaviorName;
	}

	public String getSocialName() {
		return socialName;
	}

	public void setSocialName(String socialName) {
		this.socialName = socialName;
	}

	public String getConfigType() {
		return configType;
	}

	public void setConfigType(String configType) {
		this.configType = configType;
	}

	public String getBehavName() {
		return behavName;
	}

	public void setBehavName(String behavName) {
		this.behavName = behavName;
	}

	@Expose
	@ExcelField(title="expression_type",dictType="EXPRESSION_TYPE",sort=202)
	@SerializedName("exprType")
	private Integer exprType ;

	@Expose
	@ExcelField(title="match_method",dictType="MATCH_METHOD",sort=203)
	@SerializedName("matchMethod")
	private Integer matchMethod ;

	@Expose
	@ExcelField(title="whether_hexbinary")
	@SerializedName("isHexbin")
	private Integer isHexbin;
	
	private List<IpPortCfg> ipPortList;
	private IpPortCfg ipPort;
	private NtcSubscribeIdCfg ntcSubscribeIdCfg;
	private List<NtcSubscribeIdCfg> ntcSubscribeIdCfgList;

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
		this.exprType=0;
		this.matchMethod=0;
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

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getCfgKeywords() {
		return cfgKeywords;
	}

	public void setCfgKeywords(String cfgKeywords) {
		this.cfgKeywords = cfgKeywords;
	}

	public Integer getBehavCode() {
		return behavCode;
	}

	public void setBehavCode(Integer behavCode) {
		this.behavCode = behavCode;
	}

	public List<IpPortCfg> getIpPortList() {
		return ipPortList;
	}

	public void setIpPortList(List<IpPortCfg> ipPortList) {
		this.ipPortList = ipPortList;
	}

	public IpPortCfg getIpPort() {
		return ipPort;
	}

	public void setIpPort(IpPortCfg ipPort) {
		this.ipPort = ipPort;
	}

	public String getUserRegion1() {
		return userRegion1;
	}

	public void setUserRegion1(String userRegion1) {
		this.userRegion1 = userRegion1;
	}

	public String getUserRegion2() {
		return userRegion2;
	}

	public void setUserRegion2(String userRegion2) {
		this.userRegion2 = userRegion2;
	}

	public String getUserRegion3() {
		return userRegion3;
	}

	public void setUserRegion3(String userRegion3) {
		this.userRegion3 = userRegion3;
	}

	public String getUserRegion4() {
		return userRegion4;
	}

	public void setUserRegion4(String userRegion4) {
		this.userRegion4 = userRegion4;
	}

	public String getUserRegion5() {
		return userRegion5;
	}

	public void setUserRegion5(String userRegion5) {
		this.userRegion5 = userRegion5;
	}

	public String getIndexTable() {
		return indexTable;
	}

	public void setIndexTable(String indexTable) {
		this.indexTable = indexTable;
	}
	
}
