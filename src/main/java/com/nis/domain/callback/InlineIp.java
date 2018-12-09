/**
 *@Title: InlineIp.java
 *@Package com.nis.domain.callback 
 *@Description TODO
 *@author dell
 *@date 2018年6月5日 上午9:08:34
 *@version  版本号
 */
package com.nis.domain.callback;

import java.util.Date;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nis.util.StringUtils;
import com.nis.web.service.SpringContextHolder;
import com.nis.web.service.basics.SysDictInfoService;

/**
 * @ClassName: InlineIp.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年6月5日 上午9:08:34
 * @version V1.0
 */
public class InlineIp {
	@Expose
	private Integer id;
	@Expose
	private Integer cfgId;
	@Expose
	private Integer action;
	@Expose
	private Integer service;
	@Expose
	private Integer addrType;
	@Expose
	private String srcIp;
	@Expose
	private String maskSrcIp;
	@Expose
	private String srcPort;
	@Expose
	private String maskSrcPort;
	@Expose
	private String dstIp;
	@Expose
	private String maskDstIp;
	@Expose
	private String dstPort;
	@Expose
	private String maskDstPort;
	@Expose
	private Integer protocol;
	@Expose
	private Integer direction;
	@Expose
	private Integer isValid;
	@Expose
	private Date opTime;
	@Expose
	private Integer policyGroup;
	@Expose
	private String userRegion;//用于存储协议跟流量阀值
	@Expose
	@SerializedName("effectiveRange")
	private String areaEffectiveIds ;
	
	public String getUserRegion() {
		return userRegion;
	}
	public void setUserRegion(String userRegion) {
		this.userRegion = userRegion;
	}
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
	 * addrType
	 * @return  addrType
	*/
	
	public Integer getAddrType() {
		return addrType;
	}
	/**
	 * @param addrType the addrType to set
	 */
	public void setAddrType(Integer addrType) {
		this.addrType = addrType;
	}
	/**
	 * srcIp
	 * @return  srcIp
	*/
	
	public String getSrcIp() {
		return srcIp;
	}
	/**
	 * @param srcIp the srcIp to set
	 */
	public void setSrcIp(String srcIp) {
		this.srcIp = srcIp;
	}
	/**
	 * maskSrcIp
	 * @return  maskSrcIp
	*/
	
	public String getMaskSrcIp() {
		return maskSrcIp;
	}
	/**
	 * @param maskSrcIp the maskSrcIp to set
	 */
	public void setMaskSrcIp(String maskSrcIp) {
		this.maskSrcIp = maskSrcIp;
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
	 * maskSrcPort
	 * @return  maskSrcPort
	*/
	
	public String getMaskSrcPort() {
		return maskSrcPort;
	}
	/**
	 * @param maskSrcPort the maskSrcPort to set
	 */
	public void setMaskSrcPort(String maskSrcPort) {
		this.maskSrcPort = maskSrcPort;
	}
	/**
	 * dstIp
	 * @return  dstIp
	*/
	
	public String getDstIp() {
		return dstIp;
	}
	/**
	 * @param dstIp the dstIp to set
	 */
	public void setDstIp(String dstIp) {
		this.dstIp = dstIp;
	}
	/**
	 * maskDstIp
	 * @return  maskDstIp
	*/
	
	public String getMaskDstIp() {
		return maskDstIp;
	}
	/**
	 * @param maskDstIp the maskDstIp to set
	 */
	public void setMaskDstIp(String maskDstIp) {
		this.maskDstIp = maskDstIp;
	}
	/**
	 * dstPort
	 * @return  dstPort
	*/
	
	public String getDstPort() {
		return dstPort;
	}
	/**
	 * @param dstPort the dstPort to set
	 */
	public void setDstPort(String dstPort) {
		this.dstPort = dstPort;
	}
	/**
	 * maskDstPort
	 * @return  maskDstPort
	*/
	
	public String getMaskDstPort() {
		return maskDstPort;
	}
	/**
	 * @param maskDstPort the maskDstPort to set
	 */
	public void setMaskDstPort(String maskDstPort) {
		this.maskDstPort = maskDstPort;
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
	/**
	 * cfgId
	 * @return  cfgId
	*/
	
	public Integer getCfgId() {
		return cfgId;
	}
	/**
	 * @param cfgId the cfgId to set
	 */
	public void setCfgId(Integer cfgId) {
		this.cfgId = cfgId;
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
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPolicyGroup() {
		return policyGroup;
	}
	public void setPolicyGroup(Integer policyGroup) {
		this.policyGroup = policyGroup;
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
}
