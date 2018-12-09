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
 * @Description: excel导入P2Phash字符串类配置
 */
public class P2pHashStringTemplate extends StringAllTemplate {
	private String userRegion1;
	
	@ExcelField(title="p2p_hash_type",dictType="P2P_HASH_TYPE",align=2,sort=2)
	public String getUserRegion1() {
		return userRegion1;
	}
	public void setUserRegion1(String userRegion1) {
		this.userRegion1 = userRegion1;
	}
}
