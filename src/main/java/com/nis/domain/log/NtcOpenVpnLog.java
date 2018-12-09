package com.nis.domain.log;

public class NtcOpenVpnLog extends BaseLogEntity<NtcOpenVpnLog> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3197594223008465746L;
	private String version;//版本号
	private String encryptMode;//加密方式
	private Integer hmac;//是否有HMAC
	private Integer tunnelType;//通道类型
	
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getEncryptMode() {
		return encryptMode;
	}
	public void setEncryptMode(String encryptMode) {
		this.encryptMode = encryptMode;
	}
	public Integer getHmac() {
		return hmac;
	}
	public void setHmac(Integer hmac) {
		this.hmac = hmac;
	}
	public Integer getTunnelType() {
		return tunnelType;
	}
	public void setTunnelType(Integer tunnelType) {
		this.tunnelType = tunnelType;
	}
	
}
