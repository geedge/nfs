package com.nis.domain.log;

public class NtcSslLog extends BaseLogEntity<NtcSslLog>{
	
	private static final long serialVersionUID = 533266057780162781L;
	
	private String version;// 版本号
	private String sni;// SNI 
	private String san;// SAN 
	private String cn;// CN
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getSni() {
		return sni;
	}
	public void setSni(String sni) {
		this.sni = sni;
	}
	public String getSan() {
		return san;
	}
	public void setSan(String san) {
		this.san = san;
	}
	public String getCn() {
		return cn;
	}
	public void setCn(String cn) {
		this.cn = cn;
	}
	
}

