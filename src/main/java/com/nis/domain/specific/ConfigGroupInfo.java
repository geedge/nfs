package com.nis.domain.specific;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.nis.util.excel.ExcelField;

public class ConfigGroupInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4539188977409108742L;
	private Integer id;
	private Integer groupId;
	@ExcelField(title="group_name",sort=302)
	private String groupName;
	private Integer isIssued;
	private Date insertTime;
	private Date updateTime;
	private Integer groupType;
	@ExcelField(title="cfg_id",sort=301)
	private Integer compileId;
	private Long asnId;
	
	public Long getAsnId() {
		return asnId;
	}
	public void setAsnId(Long asnId) {
		this.asnId = asnId;
	}
	public Integer getCompileId() {
		return compileId;
	}
	public void setCompileId(Integer compileId) {
		this.compileId = compileId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public Integer getIsIssued() {
		return isIssued;
	}
	public void setIsIssued(Integer isIssued) {
		this.isIssued = isIssued;
	}
	public Date getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getGroupType() {
		return groupType;
	}
	public void setGroupType(Integer groupType) {
		this.groupType = groupType;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ReflectionToStringBuilder.toString(this);
	}
	
}
