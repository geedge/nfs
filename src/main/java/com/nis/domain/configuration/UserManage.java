package com.nis.domain.configuration;

import java.util.Date;

import com.nis.domain.BaseEntity;

public class UserManage extends BaseEntity<UserManage>{

	private static final long serialVersionUID = -2749633756272829982L;
	
	private static final String tableName="user_manage";
	private String userName;
	private String userPwd;
	private String serverIp;
	private String authMethod;
	private Integer numLogins;
	private String lastLogin;
	private String transferBytes;
	private String transferPackets;
	private Long creatorId;
    private Date createTime;
	private Long editorId;
	private Date editTime;
	private Integer isValid; 
	private String remarks;
	private String userType;
	
	private String newUserPwd;
	private String oldUserName;//原用户名
	//用户名详细信息
/*	private Date createOn;
	private Date updatedOn;
	private String outgoingUnicastPackets;
	private String outgoingUnicastTotalSize;
	private String outgoingBroadcastPackets;
	private String outgoingBroadcastTotalSize;
	private String incomingUnicastPackets;
	private String incomingUnicastTotalSize;
	private String incomingBroadcastPackets;
	private String incomingBroadcastTotalSize;
	private Integer numberOfLogins;*/
	
	private String creatorName;
	private String editorName;
	private Date search_create_time_start;
	private Date search_create_time_end;
	private Date search_edit_time_start;
	private Date search_edit_time_end;
	
	private String seltype;
	private Integer functionId;
	private String isAudit;
	private String cgiError;
	private String errorType;
	
	
	public String getErrorType() {
		return errorType;
	}
	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}
	public String getCgiError() {
		return cgiError;
	}
	public void setCgiError(String cgiError) {
		this.cgiError = cgiError;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getIsAudit() {
		return isAudit;
	}
	public void setIsAudit(String isAudit) {
		this.isAudit = isAudit;
	}
	public String getOldUserName() {
		return oldUserName;
	}
	public void setOldUserName(String oldUserName) {
		this.oldUserName = oldUserName;
	}
	
	public String getNewUserPwd() {
		return newUserPwd;
	}
	public void setNewUserPwd(String newUserPwd) {
		this.newUserPwd = newUserPwd;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Integer getFunctionId() {
		return functionId;
	}
	public void setFunctionId(Integer functionId) {
		this.functionId = functionId;
	}
	public Integer getIsValid() {
		return isValid;
	}
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}
	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getEditorId() {
		return editorId;
	}
	public void setEditorId(Long editorId) {
		this.editorId = editorId;
	}
	public Date getEditTime() {
		return editTime;
	}
	public void setEditTime(Date editTime) {
		this.editTime = editTime;
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
	public Date getSearch_create_time_start() {
		return search_create_time_start;
	}
	public void setSearch_create_time_start(Date search_create_time_start) {
		this.search_create_time_start = search_create_time_start;
	}
	public Date getSearch_create_time_end() {
		return search_create_time_end;
	}
	public void setSearch_create_time_end(Date search_create_time_end) {
		this.search_create_time_end = search_create_time_end;
	}
	public Date getSearch_edit_time_start() {
		return search_edit_time_start;
	}
	public void setSearch_edit_time_start(Date search_edit_time_start) {
		this.search_edit_time_start = search_edit_time_start;
	}
	public Date getSearch_edit_time_end() {
		return search_edit_time_end;
	}
	public void setSearch_edit_time_end(Date search_edit_time_end) {
		this.search_edit_time_end = search_edit_time_end;
	}
	public String getSeltype() {
		return seltype;
	}
	public void setSeltype(String seltype) {
		this.seltype = seltype;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	public String getAuthMethod() {
		return authMethod;
	}
	public void setAuthMethod(String authMethod) {
		this.authMethod = authMethod;
	}
	public Integer getNumLogins() {
		return numLogins;
	}
	public void setNumLogins(Integer numLogins) {
		this.numLogins = numLogins;
	}
	public String getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}
	public String getTransferBytes() {
		return transferBytes;
	}
	public void setTransferBytes(String transferBytes) {
		this.transferBytes = transferBytes;
	}
	public String getTransferPackets() {
		return transferPackets;
	}
	public void setTransferPackets(String transferPackets) {
		this.transferPackets = transferPackets;
	}
	public static String getTablename() {
		return tableName;
	}

	
}
