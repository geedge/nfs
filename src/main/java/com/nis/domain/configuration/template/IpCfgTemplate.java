/**
 *@Title: BaseStringConfig.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2018年2月5日 下午5:26:02
 *@version  版本号
 */
package com.nis.domain.configuration.template;

import com.nis.util.excel.ExcelField;

/**
 * @Description: excel导入IP类配置
 */
@Deprecated
public class IpCfgTemplate {

	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	

	
	private Long cfgId;
	private Integer ipType;//1
	private Integer ipPattern;//2 
	private String srcIpAddress; //3
	private String destIpAddress; //4
	private Integer portPattern; //5
	private String srcPort; //6
	private String destPort;//7
	private Integer protocol;//8
	protected Integer direction;//9
	private String cfgDesc;//10
	private Integer requestId;//11
	private String requestName;//12
	private Integer isAreaEffective;//13
	private String classify;//14
	private String attribute;//15
	private String lable;//16
	private String classifyName;//17
	private String attributeName;//18
	private String lableName;//19
	private String areaEffectiveIds;//20
	private String ratelimit;//21
	private Integer dnsStrategyId;//22
	private Integer irType;//23
	private String userRegion1;//77
	private String userRegion2;//78
	private String userRegion3;//79
	private String userRegion4;//80
	private String userRegion5;//81
	
	/**
	 * userRegion1
	 * @return  userRegion1
	*/
//	@ExcelField(title="userregion1",align=2,sort=77)
	public String getUserRegion1() {
		return userRegion1;
	}
	/**
	 * @param userRegion1 the userRegion1 to set
	 */
	public void setUserRegion1(String userRegion1) {
		this.userRegion1 = userRegion1;
	}
	/**
	 * userRegion2
	 * @return  userRegion2
	*/
//	@ExcelField(title="userregion2",align=2,sort=78)
	public String getUserRegion2() {
		return userRegion2;
	}
	/**
	 * @param userRegion2 the userRegion2 to set
	 */
	public void setUserRegion2(String userRegion2) {
		this.userRegion2 = userRegion2;
	}
	/**
	 * userRegion3
	 * @return  userRegion3
	*/
//	@ExcelField(title="userregion3",align=2,sort=79)
	public String getUserRegion3() {
		return userRegion3;
	}
	/**
	 * @param userRegion3 the userRegion3 to set
	 */
	public void setUserRegion3(String userRegion3) {
		this.userRegion3 = userRegion3;
	}
	/**
	 * userRegion4
	 * @return  userRegion4
	*/
//	@ExcelField(title="userregion4",align=2,sort=80)
	public String getUserRegion4() {
		return userRegion4;
	}
	/**
	 * @param userRegion4 the userRegion4 to set
	 */
	public void setUserRegion4(String userRegion4) {
		this.userRegion4 = userRegion4;
	}
	/**
	 * userRegion5
	 * @return  userRegion5
	*/
//	@ExcelField(title="userregion5",align=2,sort=81)
	public String getUserRegion5() {
		return userRegion5;
	}
	/**
	 * @param userRegion5 the userRegion5 to set
	 */
	public void setUserRegion5(String userRegion5) {
		this.userRegion5 = userRegion5;
	}
	/**
	 * ratelimit
	 * @return  ratelimit
	*/
	@ExcelField(title="ratelimit",align=2,sort=74)
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
	 * irType
	 * @return  irType
	*/
	@ExcelField(title="ir_type",align=2,sort=76)
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
	 * dnsStrategyId
	 * @return  dnsStrategyId
	*/
	@ExcelField(title="dns_strategy_id",align=2,sort=75)
	public Integer getDnsStrategyId() {
		return dnsStrategyId;
	}
	/**
	 * @param dnsStrategyId the dnsStrategyId to set
	 */
	public void setDnsStrategyId(Integer dnsStrategyId) {
		this.dnsStrategyId = dnsStrategyId;
	}
	@ExcelField(title="ip_type",align=2,sort=1)
	public Integer getIpType() {
		return ipType;
	}
	public void setIpType(Integer ipType) {
		this.ipType = ipType;
	}
	@ExcelField(title="ip_pattern",align=2,sort=2)
	public Integer getIpPattern() {
		return ipPattern;
	}
	public void setIpPattern(Integer ipPattern) {
		this.ipPattern = ipPattern;
	}
	@ExcelField(title="client_ip",align=2,sort=3)
	public String getSrcIpAddress() {
		return srcIpAddress;
	}
	public void setSrcIpAddress(String srcIpAddress) {
		this.srcIpAddress = srcIpAddress;
	}
	@ExcelField(title="server_ip",align=2,sort=4)
	public String getDestIpAddress() {
		return destIpAddress;
	}
	public void setDestIpAddress(String destIpAddress) {
		this.destIpAddress = destIpAddress;
	}
	@ExcelField(title="port_pattern",align=2,sort=5)
	public Integer getPortPattern() {
		return portPattern;
	}
	public void setPortPattern(Integer portPattern) {
		this.portPattern = portPattern;
	}
	@ExcelField(title="client_port",align=2,sort=6)
	public String getSrcPort() {
		return srcPort;
	}
	public void setSrcPort(String srcPort) {
		this.srcPort = srcPort;
	}
	@ExcelField(title="server_port",align=2,sort=7)
	public String getDestPort() {
		return destPort;
	}
	public void setDestPort(String destPort) {
		this.destPort = destPort;
	}
	public Long getCfgId() {
		return cfgId;
	}
	public void setCfgId(Long cfgId) {
		this.cfgId = cfgId;
	}
	@ExcelField(title="direction",align=2,sort=8)
	public Integer getDirection() {
		return direction;
	}
	public void setDirection(Integer direction) {
		this.direction = direction;
	}
	@ExcelField(title="config_describe",align=2,sort=11)
	public String getCfgDesc() {
		return cfgDesc;
	}
	public void setCfgDesc(String cfgDesc) {
		this.cfgDesc = cfgDesc;
	}
	@ExcelField(title="letter",align=2,sort=13)
	public Integer getRequestId() {
		return requestId;
	}
	public void setRequestId(Integer requestId) {
		this.requestId = requestId;
	}
	public String getRequestName() {
		return requestName;
	}
	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}
	public Integer getIsAreaEffective() {
		return isAreaEffective;
	}
	public void setIsAreaEffective(Integer isAreaEffective) {
		this.isAreaEffective = isAreaEffective;
	}
	@ExcelField(title="classification",align=2,sort=71)
	public String getClassify() {
		return classify;
	}
	public void setClassify(String classify) {
		this.classify = classify;
	}
	@ExcelField(title="attribute",align=2,sort=72)
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	@ExcelField(title="label",align=2,sort=73)
	public String getLable() {
		return lable;
	}
	public void setLable(String lable) {
		this.lable = lable;
	}
	public String getClassifyName() {
		return classifyName;
	}
	public void setClassifyName(String classifyName) {
		this.classifyName = classifyName;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public String getLableName() {
		return lableName;
	}
	public void setLableName(String lableName) {
		this.lableName = lableName;
	}
//	@ExcelField(title="area_effective",align=2,sort=70)
	public String getAreaEffectiveIds() {
		return areaEffectiveIds;
	}
	public void setAreaEffectiveIds(String areaEffectiveIds) {
		this.areaEffectiveIds = areaEffectiveIds;
	}
	@ExcelField(title="protocol",align=2,sort=9)
	public Integer getProtocol() {
		return protocol;
	}
	public void setProtocol(Integer protocol) {
		this.protocol = protocol;
	}
}
