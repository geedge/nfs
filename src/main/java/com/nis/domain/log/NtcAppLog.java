package com.nis.domain.log;

public class NtcAppLog extends BaseLogEntity<NtcAppLog> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6025543701060412591L;
	
	protected Integer labelProtoId; //协议类型id
	protected Integer labelProtoSource; //应用id
	protected Integer labelBehavId; //行为id
	protected Integer labelBehavSource; 
	protected Integer labelAppId; 
	protected Integer labelAppSource; 
	
	
	public Integer getLabelProtoId() {
		return labelProtoId;
	}
	public void setLabelProtoId(Integer labelProtoId) {
		this.labelProtoId = labelProtoId;
	}
	public Integer getLabelProtoSource() {
		return labelProtoSource;
	}
	public void setLabelProtoSource(Integer labelProtoSource) {
		this.labelProtoSource = labelProtoSource;
	}
	public Integer getLabelBehavId() {
		return labelBehavId;
	}
	public void setLabelBehavId(Integer labelBehavId) {
		this.labelBehavId = labelBehavId;
	}
	public Integer getLabelBehavSource() {
		return labelBehavSource;
	}
	public void setLabelBehavSource(Integer labelBehavSource) {
		this.labelBehavSource = labelBehavSource;
	}
	public Integer getLabelAppId() {
		return labelAppId;
	}
	public void setLabelAppId(Integer labelAppId) {
		this.labelAppId = labelAppId;
	}
	public Integer getLabelAppSource() {
		return labelAppSource;
	}
	public void setLabelAppSource(Integer labelAppSource) {
		this.labelAppSource = labelAppSource;
	}
	
	


}
