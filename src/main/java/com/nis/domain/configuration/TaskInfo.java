package com.nis.domain.configuration;

import java.util.Date;

import com.nis.domain.BaseEntity;

public class TaskInfo extends BaseEntity<TaskInfo> {
	private static final long serialVersionUID = 1L;

	private String taskName;

	private String taskOrg;

	private Date taskTime;

	private String taskDesc;

	private Integer isValid;

	private Integer isAudit;

	private Integer creatorId;

	private Date createTime;

	private Integer editorId;

	private Date editTime;

	private Integer auditorId;

	private Date auditTime;
	
	//自定义 创建人员 修改人员 审核人员
    private String creatorName;
    private String editorName;
    private String auditorName;
    private Date beginDate;//来函开始时间
    private Date endDate;//来函结束时间
    private String seltype;//选中类型
    private Date dobeginDate;//操作开始时间
    private Date doendDate;//操作结束时间

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskOrg() {
		return taskOrg;
	}

	public void setTaskOrg(String taskOrg) {
		this.taskOrg = taskOrg;
	}

	public Date getTaskTime() {
		return taskTime;
	}

	public void setTaskTime(Date taskTime) {
		this.taskTime = taskTime;
	}

	public String getTaskDesc() {
		return taskDesc;
	}

	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}

	public Integer getIsValid() {
		return isValid;
	}

	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	public Integer getIsAudit() {
		return isAudit;
	}

	public void setIsAudit(Integer isAudit) {
		this.isAudit = isAudit;
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getEditorId() {
		return editorId;
	}

	public void setEditorId(Integer editorId) {
		this.editorId = editorId;
	}

	public Date getEditTime() {
		return editTime;
	}

	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}

	public Integer getAuditorId() {
		return auditorId;
	}

	public void setAuditorId(Integer auditorId) {
		this.auditorId = auditorId;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getEditorName() {
		return editorName;
	}

	public void setEditorName(String editorName) {
		this.editorName = editorName;
	}

	public String getAuditorName() {
		return auditorName;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getSeltype() {
		return seltype;
	}

	public void setSeltype(String seltype) {
		this.seltype = seltype;
	}

	public Date getDobeginDate() {
		return dobeginDate;
	}

	public void setDobeginDate(Date dobeginDate) {
		this.dobeginDate = dobeginDate;
	}

	public Date getDoendDate() {
		return doendDate;
	}

	public void setDoendDate(Date doendDate) {
		this.doendDate = doendDate;
	}

	
}