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
 * @Description: excel导入DNS字符串类配置
 */
public class DnsComplexStringTemplate extends ComplexStringAllTemplate {
	private Long dnsStrategyId;
	
	@ExcelField(title="policy_name",align=2,sort=2)
	public Long getDnsStrategyId() {
		return dnsStrategyId;
	}
	public void setDnsStrategyId(Long dnsStrategyId) {
		this.dnsStrategyId = dnsStrategyId;
	}
}
