package com.nis.domain.log;

public class NtcL2tpLog extends BaseLogEntity<NtcL2tpLog> {
	
	private static final long serialVersionUID = -3547128625966615793L;
	
	private Integer tunnelType;// 通道类型
	private Integer encryptMode;// 加密方式
	private String chapName; // 用户名称
	
	public String getChapName() {
		return chapName;
	}
	public void setChapName(String chapName) {
		this.chapName = chapName;
	}
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
