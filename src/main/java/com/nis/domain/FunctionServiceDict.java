package com.nis.domain;

import com.nis.domain.configuration.BaseCfg;

public class FunctionServiceDict extends BaseCfg<FunctionServiceDict> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1453401375628742549L;
	private Integer dictId;
	private Integer functionId;
	private Integer protocolId;
	private Integer action;
	private Integer serviceId;
	private String serviceName;
	private String serviceDesc;
	private String actionCode;
	private String regionCode;
	
	private Integer isImport;
	
	public Integer getIsImport() {
		return isImport;
	}
	public void setIsImport(Integer isImport) {
		this.isImport = isImport;
	}
	
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
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
	public Integer getProtocolId() {
		return protocolId;
	}
	public void setProtocolId(Integer protocolId) {
		this.protocolId = protocolId;
	}
	public Integer getAction() {
		return action;
	}
	public void setAction(Integer action) {
		this.action = action;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getServiceDesc() {
		return serviceDesc;
	}
	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}
	public Integer getIsValid() {
		return isValid;
	}
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}
	
	public String getActionCode() {
		return actionCode;
	}
	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}
	
}
