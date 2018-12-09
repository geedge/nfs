package com.nis.domain.maat;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DnsIpRecvData implements Serializable {
	
	private static final long serialVersionUID = -3438469380427993582L;
	@Expose
	@SerializedName("cfgId")
	private Long cfgId;
	@Expose
	@SerializedName("fakeId")
	private Integer fakeId;
	
	public Long getCfgId() {
		return cfgId;
	}
	public void setCfgId(Long cfgId) {
		this.cfgId = cfgId;
	}
	public Integer getFakeId() {
		return fakeId;
	}
	public void setFakeId(Integer fakeId) {
		this.fakeId = fakeId;
	}
}
