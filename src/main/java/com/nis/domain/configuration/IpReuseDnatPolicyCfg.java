package com.nis.domain.configuration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nis.util.Constants;

/**
 * IP复用DNAT策略实体
 * @author dell
 *
 */
public class IpReuseDnatPolicyCfg extends IpPortCfg{

	private static final long serialVersionUID = 3965263675989864475L;
	private String indexTable="ip_reuse_dnat_policy";
	
	@Expose
	@SerializedName("configId")
	private Integer compileId;
	
	@Expose
	@SerializedName("originalAddrType")
	private Integer ipType;
	
	@Expose
	@SerializedName("originalDestIp")
	private String srcIpAddress;
	
	@Expose
	@SerializedName("originalDestPort")
	private String srcPort;
	
	@Expose
	@SerializedName("originalProtocol")
	private Integer protocol;
	
	@Expose
	@SerializedName("translatedDestIp")
	private String destIpAddress;
	
	@Expose
	@SerializedName("translatedDestPort")
	private String destPort;
	
	@Expose
	private Integer doLog = Constants.MAAT_CFG_DOLOG_DEFAULT;
	
	@Expose
	@SerializedName("effectiveRange")
	private String areaEffectiveIds;
	
	
	public String getIndexTable() {
		return indexTable;
	}

	public void setIndexTable(String indexTable) {
		this.indexTable = indexTable;
	}

	public Integer getIpType() {
		return ipType;
	}
	
	public void setIpType(Integer ipType) {
		this.ipType = ipType;
	}
	
	public Integer getProtocol() {
		return protocol;
	}
	
	public void setProtocol(Integer protocol) {
		this.protocol = protocol;
	}
	
	public Integer getCompileId() {
		return compileId;
	}
	
	public void setCompileId(Integer compileId) {
		this.compileId = compileId;
	}
	
	public String getSrcIpAddress() {
		return srcIpAddress;
	}
	
	public void setSrcIpAddress(String srcIpAddress) {
		this.srcIpAddress = srcIpAddress;
	}
	
	public String getSrcPort() {
		return srcPort;
	}

	public void setSrcPort(String srcPort) {
		this.srcPort = srcPort;
	}
	
	public String getDestIpAddress() {
		return destIpAddress;
	}
	
	public void setDestIpAddress(String destIpAddress) {
		this.destIpAddress = destIpAddress;
	}
	
	public String getDestPort() {
		return destPort;
	}
	
	public void setDestPort(String destPort) {
		this.destPort = destPort;
	}
	
	public Integer getDoLog() {
		return doLog;
	}
	
	public void setDoLog(Integer doLog) {
		this.doLog = doLog;
	}
	
	public String getAreaEffectiveIds() {
		return areaEffectiveIds;
	}
	
	public void setAreaEffectiveIds(String areaEffectiveIds) {
		this.areaEffectiveIds = areaEffectiveIds;
	}
	
}
