package com.nis.domain.log;

public class NtcKeywordsUrlLog extends BaseLogEntity<NtcKeywordsUrlLog> {

	private static final long serialVersionUID = -7697668215327123848L;

	private String url;
	private String website;//域名

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
}
