/**
 *@Title: DnsIpConfig.java
 *@Package com.nis.domain.restful 
 *@Description 欺骗IP实体类
 *@author dell
 *@date 2018年2月5日 下午2:57:17
 *@version  版本号
 */
package com.nis.domain.basics;

import java.util.Date;

import com.google.gson.annotations.Expose;
import com.nis.domain.BaseEntity;
import com.nis.domain.SysUser;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.util.excel.ExcelField;

/**
 * @ClassName: DnsIpConfig.java 
 * @author (dell)
 * @date 2018年2月5日 下午2:57:17
 * @version V1.0
 */
public class IpReuseIpCfg extends BaseIpCfg {

	private static final long serialVersionUID = 5956694477140186483L;
	private String tableName="user_ip_cfg";
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
}
