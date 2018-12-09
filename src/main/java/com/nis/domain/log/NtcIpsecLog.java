package com.nis.domain.log;

public class NtcIpsecLog extends BaseLogEntity<NtcIpsecLog> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 351171176772839901L;
	
	private Integer exProtocol;//交换协议：1-ISAKMP(V1) 2-IKEv2 3-其它
	private Integer isakmpMode;//加密方式：0-其它1-IPSEC 2-无
	private String chapName;//用户名称
	
	public Integer getExProtocol() {
		return exProtocol;
	}
	public void setExProtocol(Integer exProtocol) {
		this.exProtocol = exProtocol;
	}
	public Integer getIsakmpMode() {
		return isakmpMode;
	}
	public void setIsakmpMode(Integer isakmpMode) {
		this.isakmpMode = isakmpMode;
	}
	public String getChapName() {
		return chapName;
	}
	public void setChapName(String chapName) {
		this.chapName = chapName;
	}
	
	
}
