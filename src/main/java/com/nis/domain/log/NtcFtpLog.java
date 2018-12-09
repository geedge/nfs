package com.nis.domain.log;

public class NtcFtpLog extends BaseLogEntity<NtcFtpLog> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 348767275603485504L;
	
	private String ftpUrl;//FTP链接
	private String ftpContent;//FTP内容
	
	public String getFtpUrl() {
		return ftpUrl;
	}
	public void setFtpUrl(String ftpUrl) {
		this.ftpUrl = ftpUrl;
	}
	public String getFtpContent() {
		return ftpContent;
	}
	public void setFtpContent(String ftpContent) {
		this.ftpContent = ftpContent;
	}
	
	
	
	

}
