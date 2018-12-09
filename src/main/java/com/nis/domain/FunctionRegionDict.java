package com.nis.domain;

import com.nis.domain.configuration.BaseCfg;

public class FunctionRegionDict extends BaseCfg<FunctionRegionDict> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8813625022212298550L;
	private Integer dictId;
	private Integer functionId;
	private Integer configRegionCode;
	private String configRegionValue;
	private String configDistrict;
	private String configDesc;
	private Integer isMaat;
	private Integer regionType;
	private String configMultiKeywords;
	private String configHex;
	private String configExprType;
	private String configMatchMethod;
	private String configServiceType;
	private String configIpPortShow;
	private String configIpType;
	private String configIpPattern;
	private String configPortPattern;
	private String configDirection;
	private String configProtocol;
	private Integer configRegionSort;
	private Integer isImport;
	
	public Integer getIsImport() {
		return isImport;
	}
	public void setIsImport(Integer isImport) {
		this.isImport = isImport;
	}
	
	public Integer getConfigRegionSort() {
		return configRegionSort;
	}
	public void setConfigRegionSort(Integer configRegionSort) {
		this.configRegionSort = configRegionSort;
	}
	public String getConfigIpPortShow() {
		return configIpPortShow;
	}
	public void setConfigIpPortShow(String configIpPortShow) {
		this.configIpPortShow = configIpPortShow;
	}
	public String getConfigIpType() {
		return configIpType;
	}
	public void setConfigIpType(String configIpType) {
		this.configIpType = configIpType;
	}
	public String getConfigIpPattern() {
		return configIpPattern;
	}
	public void setConfigIpPattern(String configIpPattern) {
		this.configIpPattern = configIpPattern;
	}
	public String getConfigPortPattern() {
		return configPortPattern;
	}
	public void setConfigPortPattern(String configPortPattern) {
		this.configPortPattern = configPortPattern;
	}
	public String getConfigDirection() {
		return configDirection;
	}
	public void setConfigDirection(String configDirection) {
		this.configDirection = configDirection;
	}
	public String getConfigProtocol() {
		return configProtocol;
	}
	public void setConfigProtocol(String configProtocol) {
		this.configProtocol = configProtocol;
	}
	public String getConfigServiceType() {
		return configServiceType;
	}
	public void setConfigServiceType(String configServiceType) {
		this.configServiceType = configServiceType;
	}
	public Integer getDictId() {
		return dictId;
	}
	public void setDictId(Integer dictId) {
		this.dictId = dictId;
	}
	public Integer getFunctionId() {
		return functionId;
	}
	public void setFunctionId(Integer functionId) {
		this.functionId = functionId;
	}
	public Integer getConfigRegionCode() {
		return configRegionCode;
	}
	public void setConfigRegionCode(Integer configRegionCode) {
		this.configRegionCode = configRegionCode;
	}
	public String getConfigRegionValue() {
		return configRegionValue;
	}
	public void setConfigRegionValue(String configRegionValue) {
		this.configRegionValue = configRegionValue;
	}
	public String getConfigDistrict() {
		return configDistrict;
	}
	public void setConfigDistrict(String configDistrict) {
		this.configDistrict = configDistrict;
	}
	public String getConfigDesc() {
		return configDesc;
	}
	public void setConfigDesc(String configDesc) {
		this.configDesc = configDesc;
	}
	public Integer getIsMaat() {
		return isMaat;
	}
	public void setIsMaat(Integer isMaat) {
		this.isMaat = isMaat;
	}
	public Integer getRegionType() {
		return regionType;
	}
	public void setRegionType(Integer regionType) {
		this.regionType = regionType;
	}
	public String getConfigMultiKeywords() {
		return configMultiKeywords;
	}
	public void setConfigMultiKeywords(String configMultiKeywords) {
		this.configMultiKeywords = configMultiKeywords;
	}
	public String getConfigHex() {
		return configHex;
	}
	public void setConfigHex(String configHex) {
		this.configHex = configHex;
	}
	public String getConfigExprType() {
		return configExprType;
	}
	public void setConfigExprType(String configExprType) {
		this.configExprType = configExprType;
	}
	public String getConfigMatchMethod() {
		return configMatchMethod;
	}
	public void setConfigMatchMethod(String configMatchMethod) {
		this.configMatchMethod = configMatchMethod;
	}
	

}
