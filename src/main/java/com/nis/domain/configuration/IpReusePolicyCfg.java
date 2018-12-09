package com.nis.domain.configuration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nis.util.Constants;

/**
 * IP复用策略
 * @author dell
 *
 */
/**
 * @author DuanDongmei
 *
 */
public class IpReusePolicyCfg extends BaseCfg<IpReusePolicyCfg>{

	private static final long serialVersionUID = 2904010760648378142L;
	private String indexTable="ip_reuse_policy_cfg";
	
	private Integer userId;			// 用户标识
	@Expose
	protected String userType;		// 用户类型
	@Expose
	protected Integer addrPoolId;		// 地址池ID
	@Expose
	protected String translateParam;	// 地址翻译参数
	@Expose
	@SerializedName("userId")
	private String userName;		
	private String addrPoolName;	// 仅用于列表条件检索
	
	private Integer ipType;
	private Integer ipPattern;
	private String srcIpAddress;
	private String destIpAddress;
	@Expose
	@SerializedName("configId")
	private Integer compileId;
	@Expose
	@SerializedName("effectiveRange")
	private String areaEffectiveIds ;
	@Expose
	private Integer doLog = Constants.MAAT_CFG_DOLOG_DEFAULT;
	
	public void setDoLog(Integer doLog) {
		this.doLog = doLog;
	}
	public Integer getDoLog() {
		return doLog;
	}
	public Integer getCompileId() {
		return compileId;
	}
	public void setCompileId(Integer compileId) {
		this.compileId = compileId;
	}
	public String getAreaEffectiveIds() {
		return areaEffectiveIds;
	}
	public void setAreaEffectiveIds(String areaEffectiveIds) {
		this.areaEffectiveIds = areaEffectiveIds;
	}
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public Integer getAddrPoolId() {
		return addrPoolId;
	}
	public void setAddrPoolId(Integer addrPoolId) {
		this.addrPoolId = addrPoolId;
	}
	public String getTranslateParam() {
		return translateParam;
	}
	public void setTranslateParam(String translateParam) {
		this.translateParam = translateParam;
	}
	public String getIndexTable() {
		return indexTable;
	}
	public void setIndexTable(String indexTable) {
		this.indexTable = indexTable;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAddrPoolName() {
		return addrPoolName;
	}
	public void setAddrPoolName(String addrPoolName) {
		this.addrPoolName = addrPoolName;
	}
	public Integer getIpType() {
		return ipType;
	}
	public void setIpType(Integer ipType) {
		this.ipType = ipType;
	}
	public String getSrcIpAddress() {
		return srcIpAddress;
	}
	public void setSrcIpAddress(String srcIpAddress) {
		this.srcIpAddress = srcIpAddress;
	}
	public Integer getIpPattern() {
		return ipPattern;
	}
	public void setIpPattern(Integer ipPattern) {
		this.ipPattern = ipPattern;
	}
	public String getDestIpAddress() {
		return destIpAddress;
	}
	public void setDestIpAddress(String destIpAddress) {
		this.destIpAddress = destIpAddress;
	}
	
}
