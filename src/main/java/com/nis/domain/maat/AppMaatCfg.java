/**
 *@Title: MaatCfg.java
 *@Package com.nis.domain.maat 
 *@Description TODO
 *@author dell
 *@date 2018年3月1日 上午10:29:29
 *@version  版本号
 */
package com.nis.domain.maat;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nis.domain.configuration.AppComplexKeywordCfg;
import com.nis.domain.configuration.AppStringCfg;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.specific.SpecificServiceHostCfg;

/**
 * @ClassName: MaatCfg.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年3月1日 上午10:29:29
 * @version V1.0
 */
public class AppMaatCfg implements Serializable {

	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -7745084076394247318L;
	/**
	 * APP字符串特征配置
	 */
	@Expose
	@SerializedName("strFeaturesCfg")
	private List<AppStringCfg> strCfg;
	/**
	 * APP服务器IP配置
	 */
	@Expose
	@SerializedName("specificServiceIpCfg")
	private List<SpecificServiceHostCfg> ipCfg;
	/**
	 * APP服务器IP生效区域配置
	 */
	@Expose
	@SerializedName("areaCfg")
	private List<BaseIpCfg> areaCfg;
	/**
	 * APP增强字符串特征配置
	 */
	@Expose
	@SerializedName("complexStrFeaturesCfg")
	private List<AppComplexKeywordCfg> complexStrCfg;
	
	@Expose
	@SerializedName("action")
	private Integer action;
	@Expose
	@SerializedName("areaEffectiveIds")
	private String areaEffectiveIds;
	@Expose
	@SerializedName("attribute")
	private String attribute;
	@Expose
	@SerializedName("classify")
	private String classify;
	@Expose
	@SerializedName("lable")
	private String lable;
	@Expose
	@SerializedName("compileId")
	private Integer compileId;
	@Expose
	@SerializedName("isAreaEffective")
	private Integer isAreaEffective;
	@Expose
	@SerializedName("isValid")
	private Integer isValid;
	@Expose
	@SerializedName("requestId")
	private Integer requestId;
	@Expose
	@SerializedName("serviceId")
	private Integer serviceId; 
	@Expose
	@SerializedName("specificServiceId")
	private Integer specificServiceId;
	
	@Expose
	private String maatTable;
	
	public List<AppStringCfg> getStrCfg() {
		return strCfg;
	}
	public void setStrCfg(List<AppStringCfg> strCfg) {
		this.strCfg = strCfg;
	}
	public List<SpecificServiceHostCfg> getIpCfg() {
		return ipCfg;
	}
	public void setIpCfg(List<SpecificServiceHostCfg> ipCfg) {
		this.ipCfg = ipCfg;
	}
	public List<AppComplexKeywordCfg> getComplexStrCfg() {
		return complexStrCfg;
	}
	public void setComplexStrCfg(List<AppComplexKeywordCfg> complexStrCfg) {
		this.complexStrCfg = complexStrCfg;
	}
	public List<BaseIpCfg> getAreaCfg() {
		return areaCfg;
	}
	public void setAreaCfg(List<BaseIpCfg> areaCfg) {
		this.areaCfg = areaCfg;
	}
	public Integer getAction() {
		return action;
	}
	public void setAction(Integer action) {
		this.action = action;
	}
	public String getAreaEffectiveIds() {
		return areaEffectiveIds;
	}
	public void setAreaEffectiveIds(String areaEffectiveIds) {
		this.areaEffectiveIds = areaEffectiveIds;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getClassify() {
		return classify;
	}
	public void setClassify(String classify) {
		this.classify = classify;
	}
	public String getLable() {
		return lable;
	}
	public void setLable(String lable) {
		this.lable = lable;
	}
	public Integer getCompileId() {
		return compileId;
	}
	public void setCompileId(Integer compileId) {
		this.compileId = compileId;
	}
	public Integer getIsAreaEffective() {
		return isAreaEffective;
	}
	public void setIsAreaEffective(Integer isAreaEffective) {
		this.isAreaEffective = isAreaEffective;
	}
	public Integer getIsValid() {
		return isValid;
	}
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}
	public Integer getRequestId() {
		return requestId;
	}
	public void setRequestId(Integer requestId) {
		this.requestId = requestId;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public Integer getSpecificServiceId() {
		return specificServiceId;
	}
	public void setSpecificServiceId(Integer specificServiceId) {
		this.specificServiceId = specificServiceId;
	}
	public String getMaatTable() {
		return maatTable;
	}
	public void setMaatTable(String maatTable) {
		this.maatTable = maatTable;
	}
	
}
