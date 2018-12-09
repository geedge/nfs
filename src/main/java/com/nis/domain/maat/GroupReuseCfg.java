package com.nis.domain.maat;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nis.domain.maat.MaatCfg.IpCfg;
import com.nis.domain.maat.MaatCfg.NumBoundaryCfg;
import com.nis.domain.maat.MaatCfg.StringCfg;
/**
 * 组复用配置
 * @author dell
 *
 */
public class GroupReuseCfg implements Serializable {
	@Expose
	@SerializedName("service")
	private Integer serviceId;
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1830956100866313836L;
	@Expose
	private List<StringCfg> strRegionList;
	@Expose
	private List<IpCfg> ipRegionList;
	@Expose
	private List<NumBoundaryCfg> numRegionList;
	public List<StringCfg> getStrRegionList() {
		return strRegionList;
	}
	public void setStrRegionList(List<StringCfg> strRegionList) {
		this.strRegionList = strRegionList;
	}
	public List<IpCfg> getIpRegionList() {
		return ipRegionList;
	}
	public void setIpRegionList(List<IpCfg> ipRegionList) {
		this.ipRegionList = ipRegionList;
	}
	public List<NumBoundaryCfg> getNumRegionList() {
		return numRegionList;
	}
	public void setNumRegionList(List<NumBoundaryCfg> numRegionList) {
		this.numRegionList = numRegionList;
	}
	
}
