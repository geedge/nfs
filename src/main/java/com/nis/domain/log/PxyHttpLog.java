package com.nis.domain.log;

public class PxyHttpLog extends BaseLogEntity<NtcIpLog> {

	private static final long serialVersionUID = -3046458130302949428L;
	
	private String url;
	private String reqLine;
	private String resLine;
	private String cookie;
	private String referer;
	private String userAgent;
	private String contentLen;
	private String contentType;
	private String setCookie;
	private String reqHeader;
	private String respHeader;
	private String reqBody;
	private String resBody;
	private String website;
	
	

	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
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
	public String getCookie() {
		return cookie;
	}
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
	public String getReferer() {
		return referer;
	}
	public void setReferer(String referer) {
		this.referer = referer;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
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
	public String getReqHeader() {
		return reqHeader;
	}
	public void setReqHeader(String reqHeader) {
		this.reqHeader = reqHeader;
	}
	public String getRespHeader() {
		return respHeader;
	}
	public void setRespHeader(String respHeader) {
		this.respHeader = respHeader;
	}
	public String getReqBody() {
		return reqBody;
	}
	public void setReqBody(String reqBody) {
		this.reqBody = reqBody;
	}
	public String getResBody() {
		return resBody;
	}
	public void setResBody(String resBody) {
		this.resBody = resBody;
	}
	@Override
	public String toString() {
		return "{\"url\"=\"" + url + "\", \"reqLine\"=\"" + reqLine
				+ "\", \"resLine\"=\"" + resLine + "\", \"cookie\"=\"" + cookie
				+ "\", \"referer\"=\"" + referer + "\", \"userAgent\"=\""
				+ userAgent + "\", \"contentLen\"=\"" + contentLen
				+ "\", \"contentType\"=\"" + contentType
				+ "\", \"setCookie\"=\"" + setCookie + "\", \"reqHeader\"=\""
				+ reqHeader + "\", \"respHeader\"=\"" + respHeader
				+ "\", \"reqBody\"=\"" + reqBody + "\", \"resBody\"=\""
				+ resBody + "\"}";
	}
	
}
