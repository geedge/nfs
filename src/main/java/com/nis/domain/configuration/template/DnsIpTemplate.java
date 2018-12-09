/**
 *@Title: BaseStringConfig.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2018年2月5日 下午5:26:02
 *@version  版本号
 */
package com.nis.domain.configuration.template;

import com.nis.util.excel.ExcelField;

/**
 * @Description: excel导入DNS IP类配置
 */
public class DnsIpTemplate extends IpAllTemplate {

	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	
	private Integer dnsStrategyId;
	
	@ExcelField(title="policy_name",align=2,sort=2)
	public Integer getDnsStrategyId() {
		return dnsStrategyId;
	}
	public void setDnsStrategyId(Integer dnsStrategyId) {
		this.dnsStrategyId = dnsStrategyId;
	}
	
}
