package com.nis.domain.configuration.template;

import com.nis.util.excel.ExcelField;

/**
 * EXCEL IP导入限速模板
 *
 */
public class IpRateLimitTemplate extends IpAllTemplate {
	public static String userRegion1="0";
	private String userRegion2;
	@ExcelField(title="ratelimit",dictType="RATE_LIMIT",align=2,sort=2)
	public String getUserRegion2() {
		return userRegion2;
	}
	public void setUserRegion2(String userRegion2) {
		this.userRegion2 = userRegion2;
	}
}
