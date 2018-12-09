package com.nis.domain.log;

public class NtcSshLog extends BaseLogEntity<NtcSshLog> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5291611528905986066L;
	
	private String version;	//版本信息 
	private String hostKey;//主机密钥 
	private String hostCookie;	//主机Cookie 
	private String encryptMode;//加密方式 具体的加密算法
	private String mac;//消息认证码 HMAC、CRC等
	private Integer tunnelType;//;通道类型:1-SSH, 2-SSHD, 3-SFTP
	
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getHostKey() {
		return hostKey;
	}
	public void setHostKey(String hostKey) {
		this.hostKey = hostKey;
	}
	public String getHostCookie() {
		return hostCookie;
	}
	public void setHostCookie(String hostCookie) {
		this.hostCookie = hostCookie;
	}
	public String getEncryptMode() {
		return encryptMode;
	}
	public void setEncryptMode(String encryptMode) {
		this.encryptMode = encryptMode;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public Integer getTunnelType() {
		return tunnelType;
	}
	public void setTunnelType(Integer tunnelType) {
		this.tunnelType = tunnelType;
	}
	
}
