package com.nis.domain.specific;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nis.domain.BaseEntity;
import com.nis.domain.SysUser;
import com.nis.util.excel.ExcelField;

/**
 * 系统特定服务信息表-记录特定服务信息(specific_service_cfg)
 * @author zsl
 *
 */
public class SpecificServiceHostCfg extends BaseEntity<SpecificServiceHostCfg>{

	private static final long serialVersionUID = -301627652860717175L;
	@Expose
	@SerializedName("cfgId")
	private Integer hostId; //host_id	配置ID	bigint	N	主键，自增
	private String specServiceName;//协议名称，用于导入导出
	private Integer specServiceId; //spec_service_id	协议id	int	N	protocol_info_cfg.protocol_id
	@Expose
	private Integer ipType; //ip地址类型 ipV4=4 ipV6=6
	@Expose
	private Integer direction;; //direction	方向	int	N	0双向，1单向，默认缺省为双向。
	@Expose
	private Integer protocol; //protocol	协议	int	N	6表示tcp,17表示udp，0表示任意
	@Expose
	private Integer ipPattern; //ip格式
	@Expose
	private String srcIpAddress; //源（客户端）IP地址
	@Expose
	private String destIpAddress; //目的（服务器）IP地址
	@Expose
	private Integer portPattern; //端口格式
	@Expose
	private String srcPort; //源（客户端）端口
	@Expose
	private String destPort;//目的（服务器）端口
	
	private Integer isValid; //is_valid	有效标识	int	N	0无效，1有效
    private Integer isAudit; //is_audit	是否审核	int	N	0未审核，1审核通过，2审核未通过，3取消审核通过（即删除）
	private SysUser creator; //creator_id	创建人员	int	N	取自sys_user.id
	private Date createTime; //create_time	配置时间	date	N	
	private SysUser editor; //editor_id	修改人员	int	Y	取自sys_user.id
	private Date editTime; //edit_time	修改时间	date	Y	
	private SysUser auditor; //auditor_id	审核人员	int	Y	取自sys_user.id
	private Date auditTime; //audit_time	审核时间	date	Y	
	@Expose
	private Integer protocolId;//配置转json下发时使用
	@Expose
	private String maatTable;//配置转json下发时使用
	@Expose
	private Integer groupId;//配置转json下发时使用
	
	private Date beginDate;
	private Date endDate;
	private Date editBeginDate;
	private Date editEndDate;
	private Date auditBeginDate;
	private Date auditEndDate;
	
	
	public Integer getHostId() {
		return hostId;
	}
	public void setHostId(Integer hostId) {
		this.hostId = hostId;
	}
	public Integer getSpecServiceId() {
		return specServiceId;
	}
	public void setSpecServiceId(Integer specServiceId) {
		this.specServiceId = specServiceId;
	}
	@ExcelField(title="ip_type",align=2,sort=20,fieldType=Integer.class)
	public Integer getIpType() {
		return ipType;
	}
	public void setIpType(Integer ipType) {
		this.ipType = ipType;
	}
	@ExcelField(title="client_ip",align=2,sort=30,fieldType=String.class)
	public String getSrcIpAddress() {
		return srcIpAddress;
	}
	public void setSrcIpAddress(String srcIpAddress) {
		this.srcIpAddress = srcIpAddress;
	}
	
	@ExcelField(title="client_port",align=2,sort=50,fieldType=String.class)
	public String getSrcPort() {
		return srcPort;
	}
	public void setSrcPort(String srcPort) {
		this.srcPort = srcPort;
	}
	
	@ExcelField(title="server_ip",align=2,sort=70,fieldType=String.class)
	public String getDestIpAddress() {
		return destIpAddress;
	}
	public void setDestIpAddress(String destIpAddress) {
		this.destIpAddress = destIpAddress;
	}
	@ExcelField(title="server_port",align=2,sort=90,fieldType=String.class)
	public String getDestPort() {
		return destPort;
	}
	public void setDestPort(String destPort) {
		this.destPort = destPort;
	}
	@ExcelField(title="ip_pattern",align=2,sort=21,fieldType=Integer.class)
	public Integer getIpPattern() {
		return ipPattern;
	}
	public void setIpPattern(Integer ipPattern) {
		this.ipPattern = ipPattern;
	}
	@ExcelField(title="port_pattern",align=2,sort=40,fieldType=Integer.class)
	public Integer getPortPattern() {
		return portPattern;
	}
	public void setPortPattern(Integer portPattern) {
		this.portPattern = portPattern;
	}
	@ExcelField(title="direction",align=2,sort=110)
	public Integer getDirection() {
		return direction;
	}
	public void setDirection(Integer direction) {
		this.direction = direction;
	}
	@ExcelField(title="protocol",align=2,sort=120)
	public Integer getProtocol() {
		return protocol;
	}
	public void setProtocol(Integer protocol) {
		this.protocol = protocol;
	}
	@ExcelField(title="valid_identifier",type=1,align=2,sort=130,fieldType=Integer.class)
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
	public SysUser getCreator() {
		return creator;
	}
	public void setCreator(SysUser creator) {
		this.creator = creator;
	}
	@ExcelField(title="创建时间",type=1,align=2,sort=140)
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
	public SysUser getAuditor() {
		return auditor;
	}
	public void setAuditor(SysUser auditor) {
		this.auditor = auditor;
	}
	public Date getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
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
	public Date getEditBeginDate() {
		return editBeginDate;
	}
	public void setEditBeginDate(Date editBeginDate) {
		this.editBeginDate = editBeginDate;
	}
	public Date getEditEndDate() {
		return editEndDate;
	}
	public void setEditEndDate(Date editEndDate) {
		this.editEndDate = editEndDate;
	}
	public Date getAuditBeginDate() {
		return auditBeginDate;
	}
	public void setAuditBeginDate(Date auditBeginDate) {
		this.auditBeginDate = auditBeginDate;
	}
	public Date getAuditEndDate() {
		return auditEndDate;
	}
	public void setAuditEndDate(Date auditEndDate) {
		this.auditEndDate = auditEndDate;
	}
	@ExcelField(title="protocol_name",align=2,sort=10)
	public String getSpecServiceName() {
		return specServiceName;
	}
	public void setSpecServiceName(String specServiceName) {
		this.specServiceName = specServiceName;
	}
	/**
	 * 处理数据
	 * @param list
	 * @param sourceList
	 * @param ParentId
	 * @param cascade
	 */
	@JsonIgnore
	public static void sort(List<SpecificServiceHostCfg> list, List<SpecificServiceHostCfg> sourceList, Integer ParentId, boolean cascade){
		
	}
	public Integer getProtocolId() {
		return protocolId;
	}
	public void setProtocolId(Integer protocolId) {
		this.protocolId = protocolId;
	}
	public String getMaatTable() {
		return maatTable;
	}
	public void setMaatTable(String maatTable) {
		this.maatTable = maatTable;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

}
