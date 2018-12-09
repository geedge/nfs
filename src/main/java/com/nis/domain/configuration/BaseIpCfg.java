/**
 *@Title: BaseIpConfig.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2018年2月5日 下午5:02:24
 *@version  版本号
 */
package com.nis.domain.configuration;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nis.util.excel.ExcelField;

/**
 * @ClassName: BaseIpConfig.java 
 * @Description: 基础IP配置
 * @author (dell)
 * @date 2018年2月5日 下午5:02:24
 * @version V1.0
 */
public class BaseIpCfg extends BaseCfg<BaseIpCfg> {
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	private static final long serialVersionUID = -1939538118388300002L;
	
	/**
	 * 创建一个新的实例 BaseIpCfg.
	 *
	 */
	public BaseIpCfg() {
		super();
		// TODO Auto-generated constructor stub
	}
	private String indexTable="ip_port_cfg";
	/**
	 * ip类型
	 */
	@Expose
	@SerializedName("ipType")
	@ExcelField(title="ip_type",dictType="IP_TYPE",sort=44)
	protected Integer ipType;
	@ExcelField(title="ip_pattern",dictType="IP_PATTERN",sort=45)
	protected Integer ipPattern; 
	@ExcelField(title="client_ip",sort=47)
	protected String srcIpAddress; 
	@ExcelField(title="server_ip",sort=46)
	protected String destIpAddress;
	@ExcelField(title="port_pattern",dictType="PORT_PATTERN",sort=48)
	protected Integer portPattern;
	@ExcelField(title="client_port",sort=49)
	protected String srcPort;
	@ExcelField(title="server_port",sort=50)
	protected String destPort; 
	protected Integer dnsStrategyId; 
	@ExcelField(title="ir_type",dictType="IR_TYPE",sort=51)
	protected Integer irType; 
	@ExcelField(title="group_name",sort=52)
	protected String groupName; 
	
	private List<NtcSubscribeIdCfg> ntcSubscribeIdCfgList;
	private NtcSubscribeIdCfg ntcSubscribeIdCfg;
	protected Integer asnIpGroup;//asn组号，仅用于copy属性使用
	protected Integer appCode;//仅用于copy属性使用
	protected Integer behavCode;//仅用于copy属性使用
	protected Integer specServiceId;//仅用于copy属性使用
	
	public Integer getAppCode() {
		return appCode;
	}
	public void setAppCode(Integer appCode) {
		this.appCode = appCode;
	}
	public Integer getBehavCode() {
		return behavCode;
	}
	public void setBehavCode(Integer behavCode) {
		this.behavCode = behavCode;
	}
	public Integer getSpecServiceId() {
		return specServiceId;
	}
	public void setSpecServiceId(Integer specServiceId) {
		this.specServiceId = specServiceId;
	}
	public Integer getAsnIpGroup() {
		return asnIpGroup;
	}
	public void setAsnIpGroup(Integer asnIpGroup) {
		this.asnIpGroup = asnIpGroup;
	}
	/**
	 * irType
	 * @return  irType
	*/
	
	public Integer getIrType() {
		return irType;
	}
	/**
	 * @param irType the irType to set
	 */
	public void setIrType(Integer irType) {
		this.irType = irType;
	}
	/**
	 * groupName
	 * @return  groupName
	*/
	
	public String getGroupName() {
		return groupName;
	}
	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	/**
	 * 方向
	 */
	@Expose
	@SerializedName("direction")
	@ExcelField(title="direction",dictType="DIRECTION",sort=53)
	protected Integer direction ;
	/**
	 * 协议
	 */
	@Expose
	@SerializedName("protocol")
	@ExcelField(title="protocol",dictType="PROTOCOL",sort=54)
	protected Integer protocol ;
	/**
	 * 协议ID
	 */
	@Expose
	@SerializedName("protocolId")
	protected Integer protocolId ;
	/**
	 * 限速比例
	 */
	protected String ratelimit ;
	
	/**
	 * ratelimit
	 * @return  ratelimit
	*/
	
	public String getRatelimit() {
		return ratelimit;
	}
	/**
	 * @param ratelimit the ratelimit to set
	 */
	public void setRatelimit(String ratelimit) {
		this.ratelimit = ratelimit;
	}
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
	
	public Integer getProtocolId() {
		return protocolId;
	}
	/**
	 * @param protocolId the protocolId to set
	 */
	public void setProtocolId(Integer protocolId) {
		this.protocolId = protocolId;
	}
	
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
	
	/**
	 * dnsStrategyId
	 * @return  dnsStrategyId
	*/
	
	public Integer getDnsStrategyId() {
		return dnsStrategyId;
	}
	/**
	 * @param dnsStrategyId the dnsStrategyId to set
	 */
	public void setDnsStrategyId(Integer dnsStrategyId) {
		this.dnsStrategyId = dnsStrategyId;
	}
	@Override
	public void initDefaultValue(){
		super.initDefaultValue();
		this.direction = 0;
	}
	
	public List<NtcSubscribeIdCfg> getNtcSubscribeIdCfgList() {
		return ntcSubscribeIdCfgList;
	}
	public void setNtcSubscribeIdCfgList(List<NtcSubscribeIdCfg> ntcSubscribeIdCfgList) {
		this.ntcSubscribeIdCfgList = ntcSubscribeIdCfgList;
	}
	public NtcSubscribeIdCfg getNtcSubscribeIdCfg() {
		return ntcSubscribeIdCfg;
	}
	public void setNtcSubscribeIdCfg(NtcSubscribeIdCfg ntcsSubscribeIdCfg) {
		this.ntcSubscribeIdCfg = ntcsSubscribeIdCfg;
	}
	public String getIndexTable() {
		return indexTable;
	}
	public void setIndexTable(String indexTable) {
		this.indexTable = indexTable;
	}
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
}
