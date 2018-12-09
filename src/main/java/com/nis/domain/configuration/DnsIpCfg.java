/**
 *@Title: DnsIpConfig.java
 *@Package com.nis.domain.restful 
 *@Description 欺骗IP实体类
 *@author dell
 *@date 2018年2月5日 下午2:57:17
 *@version  版本号
 */
package com.nis.domain.configuration;

import java.util.Date;

import com.google.gson.annotations.Expose;
import com.nis.domain.BaseEntity;
import com.nis.domain.SysUser;
import com.nis.util.excel.ExcelField;

/**
 * @ClassName: DnsIpConfig.java 
 * @author (dell)
 * @date 2018年2月5日 下午2:57:17
 * @version V1.0
 */
public class DnsIpCfg extends BaseIpCfg {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8149437730819674317L;
	private String indexTable="dns_ip_cfg";
	@ExcelField(title="group",sort=3)
	private String dnsStrategyName;
	private Integer dnsStrategyId;
	
	public Integer getDnsStrategyId() {
		return dnsStrategyId;
	}
	public void setDnsStrategyId(Integer dnsStrategyId) {
		this.dnsStrategyId = dnsStrategyId;
	}
	public String getDnsStrategyName() {
		return dnsStrategyName;
	}
	public void setDnsStrategyName(String dnsStrategyName) {
		this.dnsStrategyName = dnsStrategyName;
	}
	public String getIndexTable() {
		return indexTable;
	}
	public void setIndexTable(String indexTable) {
		this.indexTable = indexTable;
	}
	
}
