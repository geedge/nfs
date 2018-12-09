package com.nis.domain.configuration.template;

import com.nis.util.excel.ExcelField;

public class SnatTemplate extends IpAllTemplate{
	
	@ExcelField(title="group",align=2,sort=2)
	private Integer policyGroup;
	@ExcelField(title="isp",align=2,sort=3)
	private String isp;
}
