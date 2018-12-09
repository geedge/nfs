package com.nis.domain.configuration.template;

import com.nis.util.excel.ExcelField;

/**
 * @Description: excel导入ASN IP类配置
 */
public class AsnIpTemplate extends BasicTemplate{

	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private String cfgDesc;
	private String userRegion1;
	private String destIpAddress; 
	
	@ExcelField(title="config_describe",align=2,sort=1)
	public String getCfgDesc() {
		return cfgDesc;
	}
	public void setCfgDesc(String cfgDesc) {
		this.cfgDesc = cfgDesc;
	}
	@ExcelField(title="asn_no",align=2,sort=11)
	public String getUserRegion1() {
		return userRegion1;
	}
	public void setUserRegion1(String userRegion1) {
		this.userRegion1 = userRegion1;
	}
	@ExcelField(title="server_ip",align=2,sort=12)
	public String getDestIpAddress() {
		return destIpAddress;
	}
	
	public void setDestIpAddress(String destIpAddress) {
		this.destIpAddress = destIpAddress;
	}
}
