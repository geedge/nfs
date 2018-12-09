package com.nis.domain.report;

public class NtcDestIpReport extends BaseReport<NtcDestIpReport>{

	private static final long serialVersionUID = 8905927100915123026L;
	
	private String destCountry;

	public String getDestCountry() {
		return destCountry;
	}

	public void setDestCountry(String destCountry) {
		this.destCountry = destCountry;
	}
}
