package com.nis.domain.log;

public class NtcBGPLog extends BaseLogEntity<NtcBGPLog> {

	private static final long serialVersionUID = 4597407682545926823L;
	private Integer type;
	private String asNum;
	private String route;
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getAsNum() {
		return asNum;
	}
	public void setAsNum(String asNum) {
		this.asNum = asNum;
	}
	public String getRoute() {
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
	}
	
}
