package com.nis.domain.configuration;

import javax.net.ssl.KeyManager;

import com.nis.util.excel.ExcelField;

/**
 * dns响应策略配置
 * @author dell
 *
 */
public class DnsResStrategy extends BaseCfg<DnsResStrategy> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2720862431960415564L;
	private String indexTable="dns_res_strategy";
	private Integer resGroup1Id;
	@ExcelField(title="res_group_num",sort=3)
	private Integer resGroup1Num;
	private Integer resGroup2Id;
	private Integer resGroup2Num;
	private Integer resGroup3Id;
	private Integer resGroup3Num;
	private Integer resGroup4Id;
	private Integer resGroup4Num;
	private Integer resGroup5Id;
	private Integer resGroup5Num;
	private Integer minTtl;
	private Integer maxTtl;
	@ExcelField(title="group",sort=2)
	private String  group1Name;
	@ExcelField(title="min_ttl",sort=4)
	private String miTtlmax;
	//仅作导入使用
	private String ttl;
	
	public String getTtl() {
		return ttl;
	}


	public void setTtl(String ttl) {
		this.ttl = ttl;
	}


	@Override
	public void initDefaultValue() {
		this.resGroup2Id=0;
		this.resGroup2Num=0;
		this.resGroup3Id=0;
		this.resGroup3Num=0;
		this.resGroup4Id=0;
		this.resGroup4Num=0;
		this.resGroup5Id=0;
		this.resGroup5Num=0;
	}
	
	
	public String getMiTtlmax() {
		return miTtlmax;
	}


	public void setMiTtlmax(String miTtlmax) {
		this.miTtlmax = miTtlmax;
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
	public Integer getResGroup2Id() {
		return resGroup2Id;
	}
	public void setResGroup2Id(Integer resGroup2Id) {
		this.resGroup2Id = resGroup2Id;
	}
	public Integer getResGroup2Num() {
		return resGroup2Num;
	}
	public void setResGroup2Num(Integer resGroup2Num) {
		this.resGroup2Num = resGroup2Num;
	}
	public Integer getResGroup3Id() {
		return resGroup3Id;
	}
	public void setResGroup3Id(Integer resGroup3Id) {
		this.resGroup3Id = resGroup3Id;
	}
	public Integer getResGroup3Num() {
		return resGroup3Num;
	}
	public void setResGroup3Num(Integer resGroup3Num) {
		this.resGroup3Num = resGroup3Num;
	}
	public Integer getResGroup4Id() {
		return resGroup4Id;
	}
	public void setResGroup4Id(Integer resGroup4Id) {
		this.resGroup4Id = resGroup4Id;
	}
	public Integer getResGroup4Num() {
		return resGroup4Num;
	}
	public void setResGroup4Num(Integer resGroup4Num) {
		this.resGroup4Num = resGroup4Num;
	}
	public Integer getResGroup5Id() {
		return resGroup5Id;
	}
	public void setResGroup5Id(Integer resGroup5Id) {
		this.resGroup5Id = resGroup5Id;
	}
	public Integer getResGroup5Num() {
		return resGroup5Num;
	}
	public void setResGroup5Num(Integer resGroup5Num) {
		this.resGroup5Num = resGroup5Num;
	}
	public Integer getMinTtl() {
		return minTtl;
	}
	public void setMinTtl(Integer minTtl) {
		this.minTtl = minTtl;
	}
	public Integer getMaxTtl() {
		return maxTtl;
	}
	public void setMaxTtl(Integer maxTtl) {
		this.maxTtl = maxTtl;
	}
	public String getGroup1Name() {
		return group1Name;
	}
	public void setGroup1Name(String group1Name) {
		this.group1Name = group1Name;
	}
	public String getIndexTable() {
		return indexTable;
	}
	public void setIndexTable(String indexTable) {
		this.indexTable = indexTable;
	}
	
}
