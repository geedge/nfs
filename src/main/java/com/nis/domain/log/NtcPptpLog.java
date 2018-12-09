package com.nis.domain.log;

public class NtcPptpLog extends BaseLogEntity<NtcPptpLog> {
	
	private static final long serialVersionUID = 6527360739786343374L;
	
	private Integer tunnelType;// 通道类型
	private Integer encryptMode;// 加密方式
	
	public Integer getTunnelType() {
		return tunnelType;
	}
	public void setTunnelType(Integer tunnelType) {
		this.tunnelType = tunnelType;
	}
	public Integer getEncryptMode() {
		return encryptMode;
	}
	public void setEncryptMode(Integer encryptMode) {
		this.encryptMode = encryptMode;
	}
	
}
