package com.nis.domain.callback;

import com.nis.domain.configuration.BaseCfg;
/**
 * http代理文件策略
 * @author nanfang
 * 
 */
public class ProxyFileStrategyCfg extends BaseCfg<ProxyFileStrategyCfg> {
	private static final long serialVersionUID = 4283944377949702481L;
	public final static String TABLE_NAME = "PROXY_FILE_STRATEGY_CFG";//对应表名
	private String fileDesc;//文件描述
	private String url;//文件保存的url
	private String contentType;//内容类型，如text/html，取字典表contentType
	private String md5;//文件md5值
	private Long contentLength;//文件长度
	
	public ProxyFileStrategyCfg() {
		super();
	}
	
	public String getFileDesc() {
		return fileDesc;
	}
	public void setFileDesc(String fileDesc) {
		this.fileDesc = fileDesc;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public Long getContentLength() {
		return contentLength;
	}
	public void setContentLength(Long contentLength) {
		this.contentLength = contentLength;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	
}
