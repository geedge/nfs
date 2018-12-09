package com.nis.domain.maat;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;

public class ToMaatDnsIpBean implements Serializable {

	private static final long serialVersionUID = 5124353996884030213L;
	
	@Expose
	private List<MaatDnsIpCfg> serviceCfg;
	@Expose
	private String attribute;
	@Expose
	private String classify;
	@Expose
	private Integer isValid;
	@Expose
	private String lable;
	@Expose
	private Long requestId;
	
	public List<MaatDnsIpCfg> getServiceCfg() {
		return serviceCfg;
	}
	public void setServiceCfg(List<MaatDnsIpCfg> serviceCfg) {
		this.serviceCfg = serviceCfg;
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
	public Integer getIsValid() {
		return isValid;
	}
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}
	public String getLable() {
		return lable;
	}
	public void setLable(String lable) {
		this.lable = lable;
	}
	public Long getRequestId() {
		return requestId;
	}
	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

}
