package com.nis.domain.configuration.template;

import com.nis.util.excel.ExcelField;

/**
 * EXCEL P2pIP导入替换模板
 *
 */
public class P2pIpTemplate extends IpAllTemplate {
	
	private String userRegion2;
	
	@ExcelField(title="p2p_ip_config_type",dictType="P2P_IP_TYPE",align=2,sort=2)
	public String getUserRegion2() {
		return userRegion2;
	}
	public void setUserRegion2(String userRegion2) {
		this.userRegion2 = userRegion2;
	}
}
