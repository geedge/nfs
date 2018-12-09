package com.nis.domain.callback;

import java.util.Date;

import com.google.gson.annotations.Expose;
import com.nis.web.service.BaseService;
public class ProxyObjTrustedCa {
	@Expose
	private Long id; //compileId
	@Expose
	private Integer cfgId; //compileId
	@Expose
	private Integer certId; //compileId
	@Expose
	private Integer action;
	@Expose
	private Integer service;
	@Expose
	private Integer isValid;
	@Expose
	private Date opTime;
	@Expose
	private String certFile;
	@Expose
	private String certName;
	@Expose
	private Integer crlId; //crlId compileId crl使用
	@Expose
	private String crlFile;//crlFile crl使用
	
	public String getCrlFile() {
		return crlFile;
	}
	public void setCrlFile(String crlFile) {
		this.crlFile = crlFile;
	}
	public Integer getCrlId() {
		return crlId;
	}
	public void setCrlId(Integer crlId) {
		this.crlId = crlId;
	}
	
	
	public Integer getCertId() {
		return certId;
	}
	public void setCertId(Integer certId) {
		this.certId = certId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setCfgId(Integer cfgId) {
		this.cfgId = cfgId;
	}
	public Integer getCfgId() {
		return cfgId;
	}
	
	public String getCertName() {
		return certName;
	}
	public void setCertName(String certName) {
		this.certName = certName;
	}
	public String getCertFile() {
		return certFile;
	}
	public void setCertFile(String certFile) {
		this.certFile = certFile;
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
}
