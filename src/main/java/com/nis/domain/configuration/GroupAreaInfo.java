package com.nis.domain.configuration;

import java.io.Serializable;
import java.util.Date;

/**
 * 分组地理信息
 */
public class GroupAreaInfo extends BaseCfg<GroupAreaInfo> implements Serializable{

	private static final long serialVersionUID = 3312689200711339511L;
	
	private Long id;
	private Integer groupId;
	private Integer areaCode;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public Integer getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(Integer areaCode) {
		this.areaCode = areaCode;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getEditTime() {
		return editTime;
	}
	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}
}
