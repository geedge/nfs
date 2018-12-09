package com.nis.domain.log;

public class NtcP2pLog extends BaseLogEntity<NtcP2pLog>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -826089417186930465L;
	private String p2pProto;
	private String p2pFileId;
	private String p2pKeyword;
	
	public String getP2pProto() {
		return p2pProto;
	}
	public void setP2pProto(String p2pProto) {
		this.p2pProto = p2pProto;
	}
	public String getP2pFileId() {
		return p2pFileId;
	}
	public void setP2pFileId(String p2pFileId) {
		this.p2pFileId = p2pFileId;
	}
	public String getP2pKeyword() {
		return p2pKeyword;
	}
	public void setP2pKeyword(String p2pKeyword) {
		this.p2pKeyword = p2pKeyword;
	}
		
}
