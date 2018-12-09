package com.nis.domain;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServiceConfigInfo {
	
	private Long id;
	private String tableName;//配置表名
	private String tableDesc;//表描述
	private Integer tableType;//表类型  1:ip类配置表;2:字符串类配置表;3:数值类配置表;4:增强字符串配置表;
	private String maatTable;//maat中对应的表名
	private Integer serviceId;//业务id,对应SystemServiceInfo.serviceId
	private Integer isValid;//有效标识  0:无效;1:有效;
	private Integer isInitianlize;//有效标识  0:无效;1:有效;
	
	
	public Integer getIsInitianlize() {
		return isInitianlize;
	}
	public void setIsInitianlize(Integer isInitianlize) {
		this.isInitianlize = isInitianlize;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTableDesc() {
		return tableDesc;
	}
	public void setTableDesc(String tableDesc) {
		this.tableDesc = tableDesc;
	}
	public Integer getTableType() {
		return tableType;
	}
	public void setTableType(Integer tableType) {
		this.tableType = tableType;
	}
	public String getMaatTable() {
		return maatTable;
	}
	public void setMaatTable(String maatTable) {
		this.maatTable = maatTable;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public Integer getIsValid() {
		return isValid;
	}
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}
	
	@Override
	public String toString() {
		return this.toJson();
	}
	
	public String toJson() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return e.getMessage();
		}
	}
}
