package com.nis.domain.configuration.template;

import com.nis.util.excel.ExcelField;

public class DnsResStrategyTemplate extends BasicTemplate{
	@ExcelField(title="policy_name",align=2,sort=1)
	private String cfgDesc;
	@ExcelField(title="group",align=2,sort=2)
	private Integer resGroup1Id;
	@ExcelField(title="res_group_num",align=2,sort=3)
	private Integer resGroup1Num;
	@ExcelField(title="min_ttl",align=2,sort=4)
	private String ttl;
	public String getCfgDesc() {
		return cfgDesc;
	}
	public void setCfgDesc(String cfgDesc) {
		this.cfgDesc = cfgDesc;
	}
	public Integer getResGroup1Id() {
		return resGroup1Id;
	}
	public void setResGroup1Id(Integer resGroup1Id) {
		this.resGroup1Id = resGroup1Id;
	}
	public Integer getResGroup1Num() {
		return resGroup1Num;
	}
	public void setResGroup1Num(Integer resGroup1Num) {
		this.resGroup1Num = resGroup1Num;
	}
	public String getTtl() {
		return ttl;
	}
	public void setTtl(String ttl) {
		this.ttl = ttl;
	}
}
