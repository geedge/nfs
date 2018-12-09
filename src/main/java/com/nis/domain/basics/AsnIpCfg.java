package com.nis.domain.basics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nis.domain.configuration.BaseCfg;
import com.nis.util.excel.ExcelField;

public class AsnIpCfg extends BaseCfg<AsnIpCfg> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 398247881810945300L;
	private static final String tableName="asn_ip_cfg";
	@Expose
	private Integer regionId;
	@Expose
	private String ratelimit;
	@Expose
	@ExcelField(title="ip_type",dictType="IP_TYPE",sort=3)
	@SerializedName("ipType")
	private Integer ipType;
	@ExcelField(title="ip_pattern",dictType="IP_PATTERN",sort=6)
	private Integer ipPattern; 
	private String srcIpAddress; 
	@ExcelField(title="server_ip",sort=5)
	private String destIpAddress; 
	private Integer portPattern; 
	private String srcPort; 
	private String destPort; 
	@ExcelField(title="group",sort=2)
	private String asnIpGroupName;//asn组名
	private Integer asnIpGroup;//asn组号
	/**
	 * 方向
	 */
	@Expose
	@SerializedName("direction")
	private Integer direction ;
	/**
	 * 方向
	 */
	@Expose
	@SerializedName("protocol")
	private Integer protocol ;
	private Integer protocolId;
	private Integer compileId=0;
	
	@ExcelField(title="is_issued",dictType="VALID_IDENTIFIER",sort=90)
	private String isIssued;
	
	
	
	public String getIsIssued() {
		return isIssued;
	}
	public void setIsIssued(String isIssued) {
		this.isIssued = isIssued;
	}
	public Integer getCompileId() {
		return compileId;
	}
	public Integer getProtocolId() {
		return protocolId;
	}
	public void setProtocolId(Integer protocolId) {
		this.protocolId = protocolId;
	}
	
	public String getAsnIpGroupName() {
		return asnIpGroupName;
	}
	public void setAsnIpGroupName(String asnIpGroupName) {
		this.asnIpGroupName = asnIpGroupName;
	}
	public Integer getAsnIpGroup() {
		return asnIpGroup;
	}
	public void setAsnIpGroup(Integer asnIpGroup) {
		this.asnIpGroup = asnIpGroup;
	}
	public String getRatelimit() {
		return ratelimit;
	}
	public void setRatelimit(String ratelimit) {
		this.ratelimit = ratelimit;
	}
	public Integer getIpType() {
		return ipType;
	}
	public void setIpType(Integer ipType) {
		this.ipType = ipType;
	}
	public Integer getIpPattern() {
		return ipPattern;
	}
	public void setIpPattern(Integer ipPattern) {
		this.ipPattern = ipPattern;
	}
	public String getSrcIpAddress() {
		return srcIpAddress;
	}
	public void setSrcIpAddress(String srcIpAddress) {
		this.srcIpAddress = srcIpAddress;
	}
	public String getDestIpAddress() {
		return destIpAddress;
	}
	public void setDestIpAddress(String destIpAddress) {
		this.destIpAddress = destIpAddress;
	}
	public Integer getPortPattern() {
		return portPattern;
	}
	public void setPortPattern(Integer portPattern) {
		this.portPattern = portPattern;
	}
	public String getSrcPort() {
		return srcPort;
	}
	public void setSrcPort(String srcPort) {
		this.srcPort = srcPort;
	}
	public String getDestPort() {
		return destPort;
	}
	public void setDestPort(String destPort) {
		this.destPort = destPort;
	}
	public Integer getDirection() {
		return direction;
	}
	public void setDirection(Integer direction) {
		this.direction = direction;
	}
	public Integer getProtocol() {
		return protocol;
	}
	public void setProtocol(Integer protocol) {
		this.protocol = protocol;
	}
	public static String getTablename() {
		return tableName;
	}
	public Integer getRegionId() {
		return regionId;
	}
	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}
	@Override
	public String toString() {
		return "AsnIpCfg [regionId=" + regionId + ", ratelimit=" + ratelimit + ", ipType=" + ipType + ", ipPattern="
				+ ipPattern + ", srcIpAddress=" + srcIpAddress + ", destIpAddress=" + destIpAddress + ", portPattern="
				+ portPattern + ", srcPort=" + srcPort + ", destPort=" + destPort + ", asnIpGroupName=" + asnIpGroupName
				+ ", asnIpGroup=" + asnIpGroup + ", direction=" + direction + ", protocol=" + protocol + ", protocolId="
				+ protocolId + ", compileId=" + compileId + ", isIssued=" + isIssued + ", userRegion1=" + userRegion1
				+ ", userRegion2=" + userRegion2 + ", userRegion3=" + userRegion3 + ", userRegion4=" + userRegion4
				+ ", userRegion5=" + userRegion5 + ", cfgId=" + cfgId + ", cfgDesc=" + cfgDesc + ", action=" + action
				+ ", isValid=" + isValid + ", isAudit=" + isAudit + ", creatorId=" + creatorId + ", createTime="
				+ createTime + ", editorId=" + editorId + ", editTime=" + editTime + ", auditorId=" + auditorId
				+ ", auditTime=" + auditTime + ", serviceId=" + serviceId + ", requestId=" + requestId
				+ ", cancelRequestId=" + cancelRequestId + ", requestName=" + requestName + ", isAreaEffective="
				+ isAreaEffective + ", classify=" + classify + ", attribute=" + attribute + ", lable=" + lable
				+ ", cfgType=" + cfgType + ", functionId=" + functionId + ", doLog=" + doLog + "]";
	}
	
}
