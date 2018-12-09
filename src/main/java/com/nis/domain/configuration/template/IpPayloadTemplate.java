package com.nis.domain.configuration.template;

import com.nis.util.excel.ExcelField;

/**
 * EXCEL IPPayload导入替换模板
 *
 */
public class IpPayloadTemplate extends IpAllTemplate {
	
	private String userRegion1;
	private String userRegion2;
	private String userRegion3;
	
	@ExcelField(title="replace_zone",dictType="INTERCEPT_REPLACE_ZONE",align=2,sort=2)
	public String getUserRegion1() {
		return userRegion1;
	}
	public void setUserRegion1(String userRegion1) {
		this.userRegion1 = userRegion1;
	}
	@ExcelField(title="replaced_content",align=2,sort=3)
	public String getUserRegion2() {
		return userRegion2;
	}
	public void setUserRegion2(String userRegion2) {
		this.userRegion2 = userRegion2;
	}
	@ExcelField(title="replace_content",align=2,sort=4)
	public String getUserRegion3() {
		return userRegion3;
	}
	public void setUserRegion3(String userRegion3) {
		this.userRegion3 = userRegion3;
	}
}
