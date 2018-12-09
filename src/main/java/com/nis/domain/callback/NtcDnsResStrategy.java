package com.nis.domain.callback;

import java.util.Date;

import com.google.gson.annotations.Expose;
public class NtcDnsResStrategy {
	@Expose
	private Long id; //compileId
	@Expose
	private Integer cfgId; //compileId
	@Expose
	private Long reqStrateId;
	@Expose
	private String strateName;
	@Expose
	private Integer action;
	@Expose
	private Integer service;
	@Expose
	private Integer isValid;
	@Expose
	private Date opTime;
	@Expose
	private Integer resGroup1Id;
	@Expose
	private Integer resGroup1Num;
	@Expose
	private Integer resGroup2Id;
	@Expose
	private Integer resGroup2Num;
	@Expose
	private Integer resGroup3Id;
	@Expose
	private Integer resGroup3Num;
	@Expose
	private Integer resGroup4Id;
	@Expose
	private Integer resGroup4Num;
	@Expose
	private Integer resGroup5Id;
	@Expose
	private Integer resGroup5Num;
	@Expose
	private Integer minTtl;
	@Expose
	private Integer maxTtl;
	

	/**
	 * action
	 * @return  action
	*/
	
	public Integer getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(Integer action) {
		this.action = action;
	}
	/**
	 * isValid
	 * @return  isValid
	*/
	
	public Integer getIsValid() {
		return isValid;
	}
	/**
	 * @param isValid the isValid to set
	 */
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}
	/**
	 * opTime
	 * @return  opTime
	*/
	
	public Date getOpTime() {
		return opTime;
	}
	/**
	 * @param opTime the opTime to set
	 */
	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}
	public Long getReqStrateId() {
		return reqStrateId;
	}
	public void setReqStrateId(Long reqStrateId) {
		this.reqStrateId = reqStrateId;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStrateName() {
		return strateName;
	}
	public void setStrateName(String strateName) {
		this.strateName = strateName;
	}
	/**
	 * service
	 * @return  service
	*/
	
	public Integer getService() {
		return service;
	}
	/**
	 * @param service the service to set
	 */
	public void setService(Integer service) {
		this.service = service;
	}
	public Integer getResGroup1Id() {
		return resGroup1Id;
	}
	public void setResGroup1Id(Integer resGroup1Id) {
		this.resGroup1Id = resGroup1Id;
	}
	public Integer getResGroup1Num() {
		return resGroup1Num;
	}
	public void setResGroup1Num(Integer resGroup1Num) {
		this.resGroup1Num = resGroup1Num;
	}
	public Integer getResGroup2Id() {
		return resGroup2Id;
	}
	public void setResGroup2Id(Integer resGroup2Id) {
		this.resGroup2Id = resGroup2Id;
	}
	public Integer getResGroup2Num() {
		return resGroup2Num;
	}
	public void setResGroup2Num(Integer resGroup2Num) {
		this.resGroup2Num = resGroup2Num;
	}
	public Integer getResGroup3Id() {
		return resGroup3Id;
	}
	public void setResGroup3Id(Integer resGroup3Id) {
		this.resGroup3Id = resGroup3Id;
	}
	public Integer getResGroup3Num() {
		return resGroup3Num;
	}
	public void setResGroup3Num(Integer resGroup3Num) {
		this.resGroup3Num = resGroup3Num;
	}
	public Integer getResGroup4Id() {
		return resGroup4Id;
	}
	public void setResGroup4Id(Integer resGroup4Id) {
		this.resGroup4Id = resGroup4Id;
	}
	public Integer getResGroup4Num() {
		return resGroup4Num;
	}
	public void setResGroup4Num(Integer resGroup4Num) {
		this.resGroup4Num = resGroup4Num;
	}
	public Integer getResGroup5Id() {
		return resGroup5Id;
	}
	public void setResGroup5Id(Integer resGroup5Id) {
		this.resGroup5Id = resGroup5Id;
	}
	public Integer getResGroup5Num() {
		return resGroup5Num;
	}
	public void setResGroup5Num(Integer resGroup5Num) {
		this.resGroup5Num = resGroup5Num;
	}
	public Integer getMinTtl() {
		return minTtl;
	}
	public void setMinTtl(Integer minTtl) {
		this.minTtl = minTtl;
	}
	public Integer getMaxTtl() {
		return maxTtl;
	}
	public void setMaxTtl(Integer maxTtl) {
		this.maxTtl = maxTtl;
	}
	public Integer getCfgId() {
		return cfgId;
	}
	public void setCfgId(Integer cfgId) {
		this.cfgId = cfgId;
	}
	public void init() {
		resGroup2Id=0; 
		resGroup2Num=0;
		resGroup3Id=0;
		resGroup3Num=0;
		resGroup4Id=0;
		resGroup4Num=0;
		resGroup5Id=0;
		resGroup5Num=0;

	}
}
