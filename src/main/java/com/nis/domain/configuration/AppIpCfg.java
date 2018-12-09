
package com.nis.domain.configuration;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nis.util.excel.ExcelField;

/**
 * @Description: APP策略配置
 * @author (dell)
 * @date 2018年2月5日 下午5:26:02
 * @version V1.0
 */
public class AppIpCfg extends BaseCfg<AppIpCfg> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4758285716968228089L;
	private static final String tableName="app_ip_cfg";
	@Expose
	@ExcelField(title="cfg_id",sort=0)
	private Integer compileId;
	@Expose
	private String ratelimit;
	private Integer appCode;//specific_service_cfg表一级节点的spec_service_code
	private Integer behavCode;//specific_service_cfg表二级节点的spec_service_code
	private Integer specServiceId;
	@ExcelField(title="social_app",sort=2)
	private String appName;
	@Expose
	@ExcelField(title="ip_type",dictType="IP_TYPE",sort=3)
	@SerializedName("ipType")
	protected Integer ipType;
	protected Integer ipPattern; 
	protected String srcIpAddress; 
	@ExcelField(title="server_ip",sort=4)
	protected String destIpAddress; 
	protected Integer portPattern; 
	protected String srcPort; 
	@ExcelField(title="server_port",sort=5)
	protected String destPort; 
	
	/**
	 * 方向
	 */
	@Expose
	@SerializedName("direction")
	protected Integer direction ;
	/**
	 * 方向
	 */
	@Expose
	@ExcelField(title="protocol",dictType="PROTOCOL",sort=6)
	@SerializedName("protocol")
	protected Integer protocol ;
	/**
	 * 协议ID
	 */
//	@Expose
//	@SerializedName("protocolId")
//	protected Integer protocolId ;

	/**
	 * ipType
	 * @return  ipType
	*/
	
	public Integer getIpType() {
		return ipType;
	}
	/**
	 * @param ipType the ipType to set
	 */
	public void setIpType(Integer ipType) {
		this.ipType = ipType;
	}
	
	/**
	 * direction
	 * @return  direction
	*/
	
	public Integer getDirection() {
		return direction;
	}
	/**
	 * @param direction the direction to set
	 */
	public void setDirection(Integer direction) {
		this.direction = direction;
	}
	/**
	 * protocol
	 * @return  protocol
	*/
	
	public Integer getProtocol() {
		return protocol;
	}
	/**
	 * @param protocol the protocol to set
	 */
	public void setProtocol(Integer protocol) {
		this.protocol = protocol;
	}
	/**
	 * protocolId
	 * @return  protocolId
	*/
	
//	public Integer getProtocolId() {
//		return protocolId;
//	}
//	/**
//	 * @param protocolId the protocolId to set
//	 */
//	public void setProtocolId(Integer protocolId) {
//		this.protocolId = protocolId;
//	}
	
	/**
	 * ipPattern
	 * @return  ipPattern
	*/
	
	public Integer getIpPattern() {
		return ipPattern;
	}
	/**
	 * @param ipPattern the ipPattern to set
	 */
	public void setIpPattern(Integer ipPattern) {
		this.ipPattern = ipPattern;
	}
	/**
	 * portPattern
	 * @return  portPattern
	*/
	
	public Integer getPortPattern() {
		return portPattern;
	}
	/**
	 * @param portPattern the portPattern to set
	 */
	public void setPortPattern(Integer portPattern) {
		this.portPattern = portPattern;
	}
	
	/**
	 * srcIpAddress
	 * @return  srcIpAddress
	*/
	
	public String getSrcIpAddress() {
		return srcIpAddress;
	}
	/**
	 * @param srcIpAddress the srcIpAddress to set
	 */
	public void setSrcIpAddress(String srcIpAddress) {
		this.srcIpAddress = srcIpAddress;
	}
	/**
	 * destIpAddress
	 * @return  destIpAddress
	*/
	
	public String getDestIpAddress() {
		return destIpAddress;
	}
	/**
	 * @param destIpAddress the destIpAddress to set
	 */
	public void setDestIpAddress(String destIpAddress) {
		this.destIpAddress = destIpAddress;
	}
	/**
	 * srcPort
	 * @return  srcPort
	*/
	
	public String getSrcPort() {
		return srcPort;
	}
	/**
	 * @param srcPort the srcPort to set
	 */
	public void setSrcPort(String srcPort) {
		this.srcPort = srcPort;
	}
	/**
	 * destPort
	 * @return  destPort
	*/
	
	public String getDestPort() {
		return destPort;
	}
	/**
	 * @param destPort the destPort to set
	 */
	public void setDestPort(String destPort) {
		this.destPort = destPort;
	}
	@Override
	public void initDefaultValue(){
		super.initDefaultValue();
		this.direction = 0;
	}
	public Integer getCompileId() {
		return compileId;
	}
	public void setCompileId(Integer compileId) {
		this.compileId = compileId;
	}

	public static String getTablename() {
		return tableName;
	}

	public String getRatelimit() {
		return ratelimit;
	}

	public void setRatelimit(String ratelimit) {
		this.ratelimit = ratelimit;
	}
	public Integer getAppCode() {
		return appCode;
	}
	public void setAppCode(Integer appCode) {
		this.appCode = appCode;
	}
	public Integer getSpecServiceId() {
		return specServiceId;
	}
	public void setSpecServiceId(Integer specServiceId) {
		this.specServiceId = specServiceId;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public Integer getBehavCode() {
		return behavCode;
	}
	public void setBehavCode(Integer behavCode) {
		this.behavCode = behavCode;
	}
	@Override
	public String toString() {
		return "AppIpCfg [compileId=" + compileId + ", ratelimit=" + ratelimit + ", appCode=" + appCode + ", behavCode="
				+ behavCode + ", specServiceId=" + specServiceId + ", appName=" + appName + ", ipType=" + ipType
				+ ", ipPattern=" + ipPattern + ", srcIpAddress=" + srcIpAddress + ", destIpAddress=" + destIpAddress
				+ ", portPattern=" + portPattern + ", srcPort=" + srcPort + ", destPort=" + destPort + ", direction="
				+ direction + ", protocol=" + protocol + ", userRegion1=" + userRegion1 + ", userRegion2=" + userRegion2
				+ ", userRegion3=" + userRegion3 + ", userRegion4=" + userRegion4 + ", userRegion5=" + userRegion5
				+ ", cfgId=" + cfgId + ", cfgDesc=" + cfgDesc + ", action=" + action + ", isValid=" + isValid
				+ ", isAudit=" + isAudit + ", creatorId=" + creatorId + ", createTime=" + createTime + ", editorId="
				+ editorId + ", editTime=" + editTime + ", auditorId=" + auditorId + ", auditTime=" + auditTime
				+ ", serviceId=" + serviceId + ", requestId=" + requestId + ", cancelRequestId=" + cancelRequestId
				+ ", isAreaEffective=" + isAreaEffective + ", classify=" + classify + ", attribute=" + attribute
				+ ", lable=" + lable + ", areaEffectiveIds=" + areaEffectiveIds + ", cfgRegionCode=" + cfgRegionCode
				+ ", cfgType=" + cfgType + ", functionId=" + functionId + ", doLog=" + doLog + "]";
	}
}
