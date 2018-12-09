package com.nis.domain.log;

public class NtcHttpLog extends BaseLogEntity<NtcHttpLog> {

	private static final long serialVersionUID = -7697668215327123848L;

	private String url;
	private String reqHdrKey;//请求头转储文件key
	private String reqHdrFile;
	private String reqBodyKey;//请求体转储文件key
	private String reqBodyFile;
	private String resHdrKey;//应答头转储文件key
	private String resHdrFile;
	private String resBodyKey;//应答体转储文件key
	private String resBodyFile;
	private String website;//域名
	
	
	/*以下字段无需反馈到界面*/
	private Integer c2sIsn;
	private Integer pxyFlag;
	private Integer httpSeq;
	private String reqLine;
	private String resLine;
	private String cookie;
	private String refefer;
	private String userAgent;
	private String userDefineKey;
	private String userDefineValue;
	private String contentLen;
	private String contentType;
	private String setCookie;
	
	
	public String getReqHdrFile() {
		return reqHdrFile;
	}
	public void setReqHdrFile(String reqHdrFile) {
		this.reqHdrFile = reqHdrFile;
	}
	public String getReqBodyFile() {
		return reqBodyFile;
	}
	public void setReqBodyFile(String reqBodyFile) {
		this.reqBodyFile = reqBodyFile;
	}
	public String getResHdrFile() {
		return resHdrFile;
	}
	public void setResHdrFile(String resHdrFile) {
		this.resHdrFile = resHdrFile;
	}
	public String getResBodyFile() {
		return resBodyFile;
	}
	public void setResBodyFile(String resBodyFile) {
		this.resBodyFile = resBodyFile;
	}
	public Integer getPxyFlag() {
		return pxyFlag;
	}
	public void setPxyFlag(Integer pxyFlag) {
		this.pxyFlag = pxyFlag;
	}
	public String getCookie() {
		return cookie;
	}
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
	public String getRefefer() {
		return refefer;
	}
	public void setRefefer(String refefer) {
		this.refefer = refefer;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public String getUserDefineKey() {
		return userDefineKey;
	}
	public void setUserDefineKey(String userDefineKey) {
		this.userDefineKey = userDefineKey;
	}
	public String getUserDefineValue() {
		return userDefineValue;
	}
	public void setUserDefineValue(String userDefineValue) {
		this.userDefineValue = userDefineValue;
	}
	public String getContentLen() {
		return contentLen;
	}
	public void setContentLen(String contentLen) {
		this.contentLen = contentLen;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getSetCookie() {
		return setCookie;
	}
	public void setSetCookie(String setCookie) {
		this.setCookie = setCookie;
	}
	public Integer getC2sIsn() {
		return c2sIsn;
	}
	public void setC2sIsn(Integer c2sIsn) {
		this.c2sIsn = c2sIsn;
	}
	public Integer getHttpSeq() {
		return httpSeq;
	}
	public void setHttpSeq(Integer httpSeq) {
		this.httpSeq = httpSeq;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getReqLine() {
		return reqLine;
	}
	public void setReqLine(String reqLine) {
		this.reqLine = reqLine;
	}
	public String getResLine() {
		return resLine;
	}
	public void setResLine(String resLine) {
		this.resLine = resLine;
	}
	public String getReqHdrKey() {
		return reqHdrKey;
	}
	public void setReqHdrKey(String reqHdrKey) {
		this.reqHdrKey = reqHdrKey;
	}
	public String getReqBodyKey() {
		return reqBodyKey;
	}
	public void setReqBodyKey(String reqBodyKey) {
		this.reqBodyKey = reqBodyKey;
	}
	public String getResHdrKey() {
		return resHdrKey;
	}
	public void setResHdrKey(String resHdrKey) {
		this.resHdrKey = resHdrKey;
	}
	public String getResBodyKey() {
		return resBodyKey;
	}
	public void setResBodyKey(String resBodyKey) {
		this.resBodyKey = resBodyKey;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
}
