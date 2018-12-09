package com.nis.domain.callback;

import com.google.gson.annotations.Expose;

public class IpAddrPool extends InlineIp{
	
	@Expose
	private Integer regionId;
	@Expose
	private Long groupId;
	@Expose
	private Long addrPoolId;

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getAddrPoolId() {
		return addrPoolId;
	}

	public void setAddrPoolId(Long addrPoolId) {
		this.addrPoolId = addrPoolId;
	}

}
