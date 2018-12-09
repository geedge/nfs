package com.nis.domain.basics;

import java.io.Serializable;
import java.util.Date;

import com.nis.domain.configuration.BaseCfg;
import com.nis.domain.configuration.CfgIndexInfo;

/**
 * @ClassName: PolicyGroupInfo.java 
 * @Description: 策略分组
 * @version V1.0
 */
public class PolicyGroupInfo extends BaseCfg<PolicyGroupInfo> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7931466570918016654L;
	
	private Integer groupId;
	private String groupName;
	private Integer groupType;
	private Integer serviceGroupId;
	private String description;
	private Long asnNo;
	
	public Integer getServiceGroupId() {
		return serviceGroupId;
	}
	public void setServiceGroupId(Integer serviceGroupId) {
		this.serviceGroupId = serviceGroupId;
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
	public Integer getGroupType() {
		return groupType;
	}
	public void setGroupType(Integer groupType) {
		this.groupType = groupType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getAsnNo() {
		return asnNo;
	}
	public void setAsnNo(Long asnNo) {
		this.asnNo = asnNo;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
}
