/**
 *@Title: BaseStringConfig.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2018年2月5日 下午5:26:02
 *@version  版本号
 */
package com.nis.domain.configuration.template;

import com.google.gson.JsonObject;
import com.nis.util.excel.ExcelField;

/**
 * @Description: excel导入IP类配置
 */
public class IpAllTemplate extends BasicTemplate{

	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	

	
	private String cfgDesc;
	private String srcIpAddress; 
	private String destIpAddress; 
	private String srcPort; 
	private String destPort;
	private Integer protocol;
	private Integer direction;
	
	@ExcelField(title="config_describe",align=2,sort=1)
	public String getCfgDesc() {
		return cfgDesc;
	}
	public void setCfgDesc(String cfgDesc) {
		this.cfgDesc = cfgDesc;
	}
	@ExcelField(title="client_ip",align=2,sort=11)
	public String getSrcIpAddress() {
		return srcIpAddress;
	}
	public void setSrcIpAddress(String srcIpAddress) {
		this.srcIpAddress = srcIpAddress;
	}
	@ExcelField(title="server_ip",align=2,sort=12)
	public String getDestIpAddress() {
		return destIpAddress;
	}
	public void setDestIpAddress(String destIpAddress) {
		this.destIpAddress = destIpAddress;
	}
	
	@ExcelField(title="client_port",align=2,sort=13)
	public String getSrcPort() {
		return srcPort;
	}
	public void setSrcPort(String srcPort) {
		this.srcPort = srcPort;
	}
	@ExcelField(title="server_port",align=2,sort=14)
	public String getDestPort() {
		return destPort;
	}
	public void setDestPort(String destPort) {
		this.destPort = destPort;
	}
	
	@ExcelField(title="protocol",align=2,sort=15)
	public Integer getProtocol() {
		return protocol;
	}
	public void setProtocol(Integer protocol) {
		this.protocol = protocol;
	}
	@ExcelField(title="direction",align=2,sort=16)
	public Integer getDirection() {
		return direction;
	}
	public void setDirection(Integer direction) {
		this.direction = direction;
	}
	
}
