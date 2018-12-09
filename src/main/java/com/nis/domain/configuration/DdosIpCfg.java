/**
 *@Title: DnsIpConfig.java
 *@Package com.nis.domain.restful 
 *@Description 欺骗IP实体类
 *@author dell
 *@date 2018年2月5日 下午2:57:17
 *@version  版本号
 */
package com.nis.domain.configuration;

import com.nis.util.excel.ExcelField;

/**
 * @ClassName: DnsIpConfig.java 
 * @author (dell)
 * @date 2018年2月5日 下午2:57:17
 * @version V1.0
 */
public class DdosIpCfg extends BaseIpCfg {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5446903784736960824L;
	private String indexTable="ddos_ip_cfg";
	@ExcelField(title="antiddos_protocol",sort=41)
	private String antiddosProtocol;//目前支持TCP_SYN, DNS, NTP，
	@ExcelField(title="bps_threadshold",sort=42)
	private Long bpsThreadshold;// 即DDoS攻击保护动作触发阈值，每秒Bit数和每秒包数
	@ExcelField(title="pps_threadshold",sort=43)
	private Long ppsThreadshold;

	public String getAntiddosProtocol() {
		return antiddosProtocol;
	}
	public void setAntiddosProtocol(String antiddosProtocol) {
		this.antiddosProtocol = antiddosProtocol;
	}
	public Long getBpsThreadshold() {
		return bpsThreadshold;
	}
	public void setBpsThreadshold(Long bpsThreadshold) {
		this.bpsThreadshold = bpsThreadshold;
	}
	public Long getPpsThreadshold() {
		return ppsThreadshold;
	}
	public void setPpsThreadshold(Long ppsThreadshold) {
		this.ppsThreadshold = ppsThreadshold;
	}
	public String getIndexTable() {
		return indexTable;
	}
	public void setIndexTable(String indexTable) {
		this.indexTable = indexTable;
	}
	
	
}
