package com.nis.domain.log;

public class MmSampleVideoLog extends BaseLogEntity<MmSampleVideoLog>{

	private static final long serialVersionUID = -1160853324359310439L;
	
	private String pid;// 节目ID
	private String url;// 节目访问地址
	private String logUri;// fd现场片段路径
	private String referer;// 入口页面
	private Integer level;// 有害级别
	private Integer fdType;// fd类型
	private String protocol;// 传输协议
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLogUri() {
		return logUri;
	}
	public void setLogUri(String logUri) {
		this.logUri = logUri;
	}
	public String getReferer() {
		return referer;
	}
	public void setReferer(String referer) {
		this.referer = referer;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getFdType() {
		return fdType;
	}
	public void setFdType(Integer fdType) {
		this.fdType = fdType;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	@Override
	public String toString() {
		return "MmSampleVideoLog [pid=" + pid + ", url=" + url + ", logUri=" + logUri + ", referer=" + referer + ", level="
				+ level + ", fdType=" + fdType + ", protocol=" + protocol + "]";
	}
	
}
