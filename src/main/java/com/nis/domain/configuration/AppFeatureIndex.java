/**
 *@Title: CfgIndexInfo.java
 *@Package com.nis.domain.restful 
 *@Description 索引公共表
 *@author dell
 *@date 2018年5月17日 下午16:59:17
 *@version  版本号
 */
package com.nis.domain.configuration;

import java.util.ArrayList;
import java.util.List;

import com.nis.domain.basics.AsnIpCfg;
import com.nis.util.excel.ExcelField;


/**
 * @ClassName: CfgIndexInfo.java 
 * @author (dell)
 * @date 2018年5月17日 下午16:59:17
 * @version V1.0
 */
public class AppFeatureIndex extends BaseCfg<AppFeatureIndex> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8069201965300255275L;
	private static final String tableName="app_feature_index";
	private Integer appCode;//specific_service_cfg表一级节点的spec_service_code
	@ExcelField(title="social_app",sort=3)
	private String appName;
	private Integer specServiceId;
	private Integer behavCode;//specific_service_cfg表二级节点的spec_service_code
	private AppHttpCfg complexStrCfg;
	private AppByteCfg strCfg;
	private AppIpCfg ipCfg;
	private List<AppIpCfg> ipPortList = new ArrayList();
	private List<AppStringFeatureCfg> strList = new ArrayList();
	private List<AppComplexFeatureCfg> complexList = new ArrayList();
	protected Integer exprType ;
	protected Integer matchMethod ;
	protected Integer isHexbin;
	private String cfgRegionType;
	private String cfgRegionValue;
	private String cfgRegionCode1;
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
	public List<AppIpCfg> getIpPortList() {
		return ipPortList;
	}
	public void setIpPortList(List<AppIpCfg> ipPortList) {
		this.ipPortList = ipPortList;
	}
	public List<AppComplexFeatureCfg> getComplexList() {
		return complexList;
	}
	public void setComplexList(List<AppComplexFeatureCfg> complexList) {
		this.complexList = complexList;
	}
	public AppHttpCfg getComplexStrCfg() {
		return complexStrCfg;
	}
	public void setComplexStrCfg(AppHttpCfg complexStrCfg) {
		this.complexStrCfg = complexStrCfg;
	}
	public AppByteCfg getStrCfg() {
		return strCfg;
	}
	public void setStrCfg(AppByteCfg strCfg) {
		this.strCfg = strCfg;
	}
	public AppIpCfg getIpCfg() {
		return ipCfg;
	}
	public void setIpCfg(AppIpCfg ipCfg) {
		this.ipCfg = ipCfg;
	}
	public List<AppStringFeatureCfg> getStrList() {
		return strList;
	}
	public void setStrList(List<AppStringFeatureCfg> strList) {
		this.strList = strList;
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
	public String getCfgRegionType() {
		return cfgRegionType;
	}
	public void setCfgRegionType(String cfgRegionType) {
		this.cfgRegionType = cfgRegionType;
	}
	public String getCfgRegionValue() {
		return cfgRegionValue;
	}
	public void setCfgRegionValue(String cfgRegionValue) {
		this.cfgRegionValue = cfgRegionValue;
	}
	public String getCfgRegionCode1() {
		return cfgRegionCode1;
	}
	public void setCfgRegionCode1(String cfgRegionCode1) {
		this.cfgRegionCode1 = cfgRegionCode1;
	}

}
