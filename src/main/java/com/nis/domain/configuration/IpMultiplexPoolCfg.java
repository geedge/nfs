package com.nis.domain.configuration;

import java.util.Date;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nis.util.excel.ExcelField;
import com.nis.web.service.BaseService;

/**
 * IP复用地址池配置
 * @author dell
 *
 */
public class IpMultiplexPoolCfg extends BaseCfg<IpMultiplexPoolCfg> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5902468971425910528L;
	private String indexTable="ip_multiplex_pool_cfg";
	@Expose
	@ExcelField(title="cfg_id",sort=0)
	@SerializedName("cfgId")
	private Integer compileId;
	@Expose
	@SerializedName("addrType")
	private String ipType;
	@Expose
	@ExcelField(title="server_ip",sort=6)
	@SerializedName("ip")
	private String destIpAddress;
	@Expose
	@ExcelField(title="server_port",sort=9)
	private String port;
	@Expose
	private String userRegion;
	@Expose
	private Integer location;
	@Expose
	private Integer policyGroup;
	@ExcelField(title="group",sort=2)
	private String  groupName;
	/**
	 * 方向
	 */
	@Expose
	@SerializedName("direction")
	@ExcelField(title="direction",dictType="DIRECTION",sort=13)
	protected Integer direction ;
	/**
	 * 协议
	 */
	@Expose
	@SerializedName("protocol")
	@ExcelField(title="protocol",dictType="PROTOCOL",sort=12)
	protected Integer protocol ;
	
	@ExcelField(title="isp" ,sort=3)
	private String ispName;
	
	
	public String getIspName() {
		return ispName;
	}
	public void setIspName(String ispName) {
		this.ispName = ispName;
	}
	public Integer getCompileId() {
		return compileId;
	}
	public void setCompileId(Integer compileId) {
		this.compileId = compileId;
	}
	public String getIpType() {
		return ipType;
	}
	public void setIpType(String ipType) {
		this.ipType = ipType;
	}
	public Integer getProtocol() {
		return protocol;
	}
	public void setProtocol(Integer protocol) {
		this.protocol = protocol;
	}
	

	public String getDestIpAddress() {
		return destIpAddress;
	}
	public void setDestIpAddress(String destIpAddress) {
		this.destIpAddress = destIpAddress;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public Integer getDirection() {
		return direction;
	}
	public void setDirection(Integer direction) {
		this.direction = direction;
	}
	public String getUserRegion() {
		userRegion=BaseService.keywordsEscape(userRegion);
		return userRegion;
	}
	public void setUserRegion(String userRegion) {
		this.userRegion = userRegion;
	}
	public Integer getLocation() {
		return location;
	}
	public void setLocation(Integer location) {
		this.location = location;
	}
	public Integer getPolicyGroup() {
		return policyGroup;
	}
	public void setPolicyGroup(Integer policyGroup) {
		this.policyGroup = policyGroup;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getIndexTable() {
		return indexTable;
	}
	public void setIndexTable(String indexTable) {
		this.indexTable = indexTable;
	}

}
