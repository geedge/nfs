/**
 *@Title: MaatCfg.java
 *@Package com.nis.domain.maat 
 *@Description TODO
 *@author dell
 *@date 2018年3月1日 上午10:29:29
 *@version  版本号
 */
package com.nis.domain.maat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nis.util.Constants;
import com.nis.util.DateUtils;
import com.nis.util.StringUtils;
import com.nis.web.service.BaseService;
import com.nis.web.service.SpringContextHolder;
import com.nis.web.service.basics.SysDictInfoService;

/**
 * @ClassName: MaatCfg.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年3月1日 上午10:29:29
 * @version V1.0
 */
public class MaatCfg implements Serializable {
	@Expose
	private Integer compileId;
	@Expose
	private Integer action;	
	@Expose
	@SerializedName("service")
	private Integer serviceId;	
	@Expose
	@SerializedName("contType")
	private String classify;//分类
	@Expose
	@SerializedName("attrType")
	private String attribute;//性质
	@Expose
	@SerializedName("contLabel")
	private String lable;//标签	
	@Expose
	private Integer taskId;		
	@Expose
	@SerializedName("guaranteeId")
	private Integer functionId;		
	@Expose
	@SerializedName("effectiveRange")
	private String areaEffectiveIds ;
	@Expose
	@SerializedName("doBlacklist")
	private Integer doBlackList;	
	@Expose
	private Integer doLog;
	@Expose
	private Integer activeSys;	
	@Expose
	private Integer configPercent;	
	@Expose
	private Integer configOption;	
	@Expose
	private Date startTime;	
	@Expose
	private Date endTime;	
	@Expose
	private String userRegion;
	@Expose
	private Integer groupNum;
	@Expose
	private Integer fatherCfgId;
	@Expose
	@SerializedName("affairId")
	private Integer requestId;
	@Expose
	@SerializedName("opTime")
	private Date auditTime;		
	@Expose
	private Integer isValid;
	@Expose
	private List<GroupCfg> groupRelationList;	
	@Expose
	private List<StringCfg> strRegionList;	
	@Expose
	private List<IpCfg> ipRegionList;	
	@Expose
	private List<NumBoundaryCfg> numRegionList;	
	@Expose
	private List<DigestCfg> digestRegionList;
	@Expose
	@SerializedName("ipClientRangeList")
	private List<IpCfg> areaIpRegionList;
	
	public static class GroupCfg{
		@Expose
		private Integer groupId;
		@Expose
		private Integer compileId;
		@Expose
		private Integer isValid;
		@Expose
		@SerializedName("opTime")
		private Date auditTime;
		public Integer getGroupId() {
			return groupId;
		}
		public void setGroupId(Integer groupId) {
			this.groupId = groupId;
		}
		public Integer getCompileId() {
			return compileId;
		}
		public void setCompileId(Integer compileId) {
			this.compileId = compileId;
		}
		public Integer getIsValid() {
			return isValid;
		}
		public void setIsValid(Integer isValid) {
			this.isValid = isValid;
		}
		public Date getAuditTime() {
			return auditTime;
		}
		public void setAuditTime(Date auditTime) {
			this.auditTime = auditTime;
		}
		
	}
	public static class StringCfg{	
		@Expose
		private Integer regionId;
		@Expose
		private Integer groupId;
		@Expose
		@SerializedName("tableName")
		private String cfgType;
		@Expose
		private String district;
		@Expose
		@SerializedName("keywords")
		private String cfgKeywords;
		@Expose
		private Integer exprType;
		@Expose
		private Integer matchMethod;
		@Expose
		private Integer isHexbin;
		@Expose
		private Integer isValid;
		@Expose
		@SerializedName("opTime")
		private Date auditTime;
		public Integer getRegionId() {
			return regionId;
		}
		public void setRegionId(Integer regionId) {
			this.regionId = regionId;
		}
		public Integer getGroupId() {
			return groupId;
		}
		public void setGroupId(Integer groupId) {
			this.groupId = groupId;
		}
		public String getCfgType() {
			return cfgType;
		}
		public void setCfgType(String cfgType) {
			this.cfgType = cfgType;
		}
		public String getDistrict() {
			return district;
		}
		public void setDistrict(String district) {
			this.district = district;
		}
		public String getCfgKeywords() {
			return cfgKeywords;
		}
		public void setCfgKeywords(String cfgKeywords) {
			this.cfgKeywords = cfgKeywords;
		}
		public Integer getExprType() {
			return exprType;
		}
		public void setExprType(Integer exprType) {
			this.exprType = exprType;
		}
		public Integer getMatchMethod() {
			return matchMethod;
		}
		public void setMatchMethod(Integer matchMethod) {
			this.matchMethod = matchMethod;
		}
		public Integer getIsHexbin() {
			return isHexbin;
		}
		public void setIsHexbin(Integer isHexbin) {
			this.isHexbin = isHexbin;
		}
		public Integer getIsValid() {
			return isValid;
		}
		public void setIsValid(Integer isValid) {
			this.isValid = isValid;
		}
		public Date getAuditTime() {
			return auditTime;
		}
		public void setAuditTime(Date auditTime) {
			this.auditTime = auditTime;
		}
		
	}
	public static class IpCfg{	
		@Expose
		private Integer regionId;
		@Expose
		private Integer groupId;
		@Expose
		@SerializedName("tableName")
		private String cfgType;
		@Expose
		@SerializedName("addrType")
		private Integer ipType;
		@Expose
		private String srcIp;
		@Expose
		@SerializedName("maskSrcIp")
		private String srcIpMask ;
		@Expose
		private String srcPort ;
		@Expose
		@SerializedName("maskSrcPort")
		private String srcPortMask ;
		@Expose
		private String dstIp ;
		@Expose
		@SerializedName("maskDstIp")
		private String dstIpMask ;
		@Expose
		private String dstPort ;
		@Expose
		@SerializedName("maskDstPort")
		private String dstPortMask ;
		@Expose
		private Integer direction ;
		@Expose
		private Integer protocol ;
		@Expose
		private Integer isValid;
		@Expose
		@SerializedName("opTime")
		private Date auditTime;
		@Expose
		private String userRegion;
		
		public String getUserRegion() {
			return userRegion;
		}
		public void setUserRegion(String userRegion) {
			this.userRegion = userRegion;
		}
		public Integer getRegionId() {
			return regionId;
		}
		public void setRegionId(Integer regionId) {
			this.regionId = regionId;
		}
		public Integer getGroupId() {
			return groupId;
		}
		public void setGroupId(Integer groupId) {
			this.groupId = groupId;
		}
		public String getCfgType() {
			return cfgType;
		}
		public void setCfgType(String cfgType) {
			this.cfgType = cfgType;
		}
		public Integer getIpType() {
			return ipType;
		}
		public void setIpType(Integer ipType) {
			this.ipType = ipType;
		}
		public String getSrcIp() {
			return srcIp;
		}
		public void setSrcIp(String srcIp) {
			this.srcIp = srcIp;
		}
		public String getSrcIpMask() {
			return srcIpMask;
		}
		public void setSrcIpMask(String srcIpMask) {
			this.srcIpMask = srcIpMask;
		}
		public String getSrcPort() {
			return srcPort;
		}
		public void setSrcPort(String srcPort) {
			this.srcPort = srcPort;
		}
		public String getSrcPortMask() {
			return srcPortMask;
		}
		public void setSrcPortMask(String srcPortMask) {
			this.srcPortMask = srcPortMask;
		}
		public String getDstIp() {
			return dstIp;
		}
		public void setDstIp(String dstIp) {
			this.dstIp = dstIp;
		}
		public String getDstIpMask() {
			return dstIpMask;
		}
		public void setDstIpMask(String dstIpMask) {
			this.dstIpMask = dstIpMask;
		}
		public String getDstPort() {
			return dstPort;
		}
		public void setDstPort(String dstPort) {
			this.dstPort = dstPort;
		}
		public String getDstPortMask() {
			return dstPortMask;
		}
		public void setDstPortMask(String dstPortMask) {
			this.dstPortMask = dstPortMask;
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
		public Integer getIsValid() {
			return isValid;
		}
		public void setIsValid(Integer isValid) {
			this.isValid = isValid;
		}
		public Date getAuditTime() {
			return auditTime;
		}
		public void setAuditTime(Date auditTime) {
			this.auditTime = auditTime;
		}
	}
	public static class NumBoundaryCfg{	
		@Expose
		private Integer regionId;
		@Expose
		private Integer groupId;
		@Expose
		@SerializedName("tableName")
		private String cfgType="";
		@Expose
		private Integer lowBoundary;
		@Expose
		private Integer upBoundary;
		@Expose
		private Integer isValid;
		@Expose
		@SerializedName("opTime")
		private Date auditTime;
		public Integer getRegionId() {
			return regionId;
		}
		public void setRegionId(Integer regionId) {
			this.regionId = regionId;
		}
		public Integer getGroupId() {
			return groupId;
		}
		public void setGroupId(Integer groupId) {
			this.groupId = groupId;
		}
		public String getCfgType() {
			return cfgType;
		}
		public void setCfgType(String cfgType) {
			this.cfgType = cfgType;
		}
		public Integer getLowBoundary() {
			return lowBoundary;
		}
		public void setLowBoundary(Integer lowBoundary) {
			this.lowBoundary = lowBoundary;
		}
		public Integer getUpBoundary() {
			return upBoundary;
		}
		public void setUpBoundary(Integer upBoundary) {
			this.upBoundary = upBoundary;
		}
		public Integer getIsValid() {
			return isValid;
		}
		public void setIsValid(Integer isValid) {
			this.isValid = isValid;
		}
		public Date getAuditTime() {
			return auditTime;
		}
		public void setAuditTime(Date auditTime) {
			this.auditTime = auditTime;
		}
		public void initDefaultValue() {
			this.cfgType=Constants.PROTOCOL_NUM_REGION;
		}
	}public static class DigestCfg{	
		@Expose
		private Integer regionId;
		@Expose
		private Integer groupId;
		@Expose
		@SerializedName("tableName")
		private String cfgType;
		@Expose
		private Long rawLen;
		@Expose
		private String digest;
		@Expose
		private Integer cfdsLevel;
		@Expose
		private Integer isValid;
		@Expose
		private Date auditTime;
		public Integer getRegionId() {
			return regionId;
		}
		public void setRegionId(Integer regionId) {
			this.regionId = regionId;
		}
		public Integer getGroupId() {
			return groupId;
		}
		public void setGroupId(Integer groupId) {
			this.groupId = groupId;
		}
		public String getCfgType() {
			return cfgType;
		}
		public void setCfgType(String cfgType) {
			this.cfgType = cfgType;
		}
		public Long getRawLen() {
			return rawLen;
		}
		public void setRawLen(Long rawLen) {
			this.rawLen = rawLen;
		}
		public String getDigest() {
			return digest;
		}
		public void setDigest(String digest) {
			this.digest = digest;
		}
		public Integer getCfdsLevel() {
			return cfdsLevel;
		}
		public void setCfdsLevel(Integer cfdsLevel) {
			this.cfdsLevel = cfdsLevel;
		}
		public Integer getIsValid() {
			return isValid;
		}
		public void setIsValid(Integer isValid) {
			this.isValid = isValid;
		}
		public Date getAuditTime() {
			return auditTime;
		}
		public void setAuditTime(Date auditTime) {
			this.auditTime = auditTime;
		}
	}
	public Integer getCompileId() {
		return compileId;
	}
	public void setCompileId(Integer compileId) {
		this.compileId = compileId;
	}
	public Integer getAction() {
		return action;
	}
	public void setAction(Integer action) {
		this.action = action;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public String getClassify() {
		return classify;
	}
	public void setClassify(String classify) {
		this.classify = classify;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getLable() {
		return lable;
	}
	public void setLable(String lable) {
		this.lable = lable;
	}
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	public Integer getFunctionId() {
		return functionId;
	}
	public void setFunctionId(Integer functionId) {
		this.functionId = functionId;
	}
	public String getAreaEffectiveIds() {
		return areaEffectiveIds;
	}
	/**
	 * areaEffectiveIds 
	 * areaEffectiveIds=7242:5,7212:4,7122:3
	 * 接口要求格式为：72425,72124,71223
	 * @param areaEffectiveIds
	 */
	public void setAreaEffectiveIds(String areaEffectiveIds) {
		if(StringUtils.isEmpty(areaEffectiveIds)){
			areaEffectiveIds="0";
		}else {
			// 组织区域、运营商配置下发格式
			SysDictInfoService service = SpringContextHolder.getBean(SysDictInfoService.class);
			areaEffectiveIds = service.setEffectiveRange(areaEffectiveIds);
		}
		this.areaEffectiveIds = areaEffectiveIds;
	}
	public Integer getDoBlackList() {
		return doBlackList;
	}
	public void setDoBlackList(Integer doBlackList) {
		this.doBlackList = doBlackList;
	}
	public Integer getDoLog() {
		return doLog;
	}
	public void setDoLog(Integer doLog) {
		this.doLog = doLog;
	}
	public Integer getActiveSys() {
		return activeSys;
	}
	public void setActiveSys(Integer activeSys) {
		this.activeSys = activeSys;
	}
	public Integer getConfigPercent() {
		return configPercent;
	}
	public void setConfigPercent(Integer configPercent) {
		this.configPercent = configPercent;
	}
	public Integer getConfigOption() {
		return configOption;
	}
	public void setConfigOption(Integer configOption) {
		this.configOption = configOption;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getUserRegion() {
		return userRegion;
	}
	public void setUserRegion(String userRegion) {
		userRegion=BaseService.keywordsEscape(userRegion);
		this.userRegion = userRegion;
	}
	public Integer getGroupNum() {
		return groupNum;
	}
	public void setGroupNum(Integer groupNum) {
		this.groupNum = groupNum;
	}
	public Integer getFatherCfgId() {
		return fatherCfgId;
	}
	public void setFatherCfgId(Integer fatherCfgId) {
		this.fatherCfgId = fatherCfgId;
	}
	public Date getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	public List<GroupCfg> getGroupRelationList() {
		return groupRelationList;
	}
	public void setGroupRelationList(List<GroupCfg> groupRelationList) {
		this.groupRelationList = groupRelationList;
	}
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
	public List<DigestCfg> getDigestRegionList() {
		return digestRegionList;
	}
	public void setDigestRegionList(List<DigestCfg> digestRegionList) {
		this.digestRegionList = digestRegionList;
	}
	public List<IpCfg> getAreaIpRegionList() {
		return areaIpRegionList;
	}
	public void setAreaIpRegionList(List<IpCfg> areaIpRegionList) {
		this.areaIpRegionList = areaIpRegionList;
	}
	public Integer getIsValid() {
		return isValid;
	}
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}
	
	public Integer getRequestId() {
		return requestId;
	}
	public void setRequestId(Integer requestId) {
		this.requestId = requestId;
	}
	public void initDefaultValue() {
		this.doLog=Constants.MAAT_CFG_DOLOG_DEFAULT;
		this.doBlackList=Constants.MAAT_CFG_DOLOG_DOBLACKLIST_DEFAULT;
		this.configPercent=Constants.MAAT_CFG_DOLOG_CONFIGPERCENT_DEFAULT;
		this.configOption=Constants.MAAT_CFG_DOLOG_CONFIGOPTION_DEFAULT;
		this.startTime=new Date();
		this.endTime=DateUtils.addYears(this.startTime, 2099-Integer.parseInt(DateUtils.getYear()));
		this.fatherCfgId=0;
		this.taskId=0;
		//初始化用户定义域占位符，对用户定义域有特殊格式的业务，根据分隔符拆分占位符，在相应位置填写相应参数值
		this.userRegion = Constants.USER_REGION_PLACEHOLDER;
	}
}
