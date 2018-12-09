package com.nis.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
@Deprecated
public class SystemServiceInfo extends BaseEntity<SystemServiceInfo> {
	
	private static final long serialVersionUID = -8919580011013718190L;

	private Integer serviceId;//业务id
	private String serviceName;//业务名称
	private String serviceDesc;//业务描述
	private Integer action;//业务动作  1:阻断;2:监测;5:FD白名单;6:监测白名单;7:FD监测白名单;8:灰名单;
	private Integer isValid;//有效标识  0:无效;1:有效;
	private Integer serviceType;//业务类型  1:常规(单域)业务配置;2:多域业务配置;3.特定服务配置;
	private SysUser creator;
	private Date createTime;
	private SysUser editor;
	private Date editTime;
	private List<ServiceConfigInfo> serviceConfigInfoList;//业务配置表
	private Integer isInitianlize;//是否是初始化数据
	
	
	public Integer getIsInitianlize() {
		return isInitianlize;
	}
	public void setIsInitianlize(Integer isInitianlize) {
		this.isInitianlize = isInitianlize;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getServiceDesc() {
		return serviceDesc;
	}
	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}
	public Integer getAction() {
		return action;
	}
	public void setAction(Integer action) {
		this.action = action;
	}
	public Integer getIsValid() {
		return isValid;
	}
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}
	public Integer getServiceType() {
		return serviceType;
	}
	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}
	public SysUser getCreator() {
		return creator;
	}
	public void setCreator(SysUser creator) {
		this.creator = creator;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public SysUser getEditor() {
		return editor;
	}
	public void setEditor(SysUser editor) {
		this.editor = editor;
	}
	public Date getEditTime() {
		return editTime;
	}
	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}
	
	public List<ServiceConfigInfo> getServiceConfigInfoList() {
		return serviceConfigInfoList;
	}
	public void setServiceConfigInfoList(List<ServiceConfigInfo> serviceConfigInfoList) {
		this.serviceConfigInfoList = serviceConfigInfoList;
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
	
	private Integer serviceIdPre;//辅助用，用来存放serviceId前四位以获得最新serviceId
	
	public Integer getServiceIdPre() {
		return serviceIdPre;
	}
	public void setServiceIdPre(Integer serviceIdPre) {
		this.serviceIdPre = serviceIdPre;
	}
}
