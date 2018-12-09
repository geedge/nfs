package com.nis.domain.configuration;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * IP复用地址池实体
 * @author dell
 *
 */
public class IpAddrPoolCfg extends BaseCfg<IpAddrPoolCfg>{

	private static final long serialVersionUID = 1392197458744667669L;
	private String indexTable="ip_reuse_addr_pool";
	
	private String addrPoolName; 		// 地址池名称
	private Integer ipTotal;			// IP总数
	private Integer availableIpTotal;	// 可用IP总数
	private String description;			// 描述信息
	
	private List<BaseIpCfg> ipCfgs;		// 地址池IP信息
	
	@SerializedName("cfgId")
	private Integer compileId;
	
	public String getIndexTable() {
		return indexTable;
	}
	public void setIndexTable(String indexTable) {
		this.indexTable = indexTable;
	}
	public String getAddrPoolName() {
		return addrPoolName;
	}
	public void setAddrPoolName(String addrPoolName) {
		this.addrPoolName = addrPoolName;
	}
	public Integer getIpTotal() {
		return ipTotal;
	}
	public void setIpTotal(Integer ipTotal) {
		this.ipTotal = ipTotal;
	}
	public Integer getAvailableIpTotal() {
		return availableIpTotal;
	}
	public void setAvailableIpTotal(Integer availableIpTotal) {
		this.availableIpTotal = availableIpTotal;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<BaseIpCfg> getIpCfgs() {
		return ipCfgs;
	}
	public void setIpCfgs(List<BaseIpCfg> ipCfgs) {
		this.ipCfgs = ipCfgs;
	}
	public Integer getCompileId() {
		return compileId;
	}
	public void setCompileId(Integer compileId) {
		this.compileId = compileId;
	}
	
	
}
