package com.nis.domain.callback;

import java.util.Date;

import com.google.gson.annotations.Expose;
public class ProxyObjKeyring {
	@Expose
	private Long id; //compileId
	@Expose
	private Integer cfgId; //compileId
	@Expose
	private Integer keyringId;
	@Expose
	private Integer action;
	@Expose
	private Integer service;
	@Expose
	private Integer isValid;
	@Expose
	private Date opTime;
	@Expose
	private String keyringName;
	@Expose
	private String keyringType;
	@Expose
	private String privateKeyFile;
	@Expose
	private String publicKeyFile;
	@Expose
	private Integer expireAfter;
	@Expose
	private String publicKeyAlgo;
	@Expose
	private String crl;
	
	
	
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
	public void setKeyringId(Integer keyringId) {
		this.keyringId = keyringId;
	}
	public Integer getKeyringId() {
		return keyringId;
	}

	public String getKeyringName() {
		return keyringName;
	}
	public void setKeyringName(String keyringName) {
		this.keyringName = keyringName;
	}
	public String getKeyringType() {
		return keyringType;
	}
	public void setKeyringType(String keyringType) {
		this.keyringType = keyringType;
	}
	public String getPrivateKeyFile() {
		return privateKeyFile;
	}
	public void setPrivateKeyFile(String privateKeyFile) {
		this.privateKeyFile = privateKeyFile;
	}
	public String getPublicKeyFile() {
		return publicKeyFile;
	}
	public void setPublicKeyFile(String publicKeyFile) {
		this.publicKeyFile = publicKeyFile;
	}
	public Integer getExpireAfter() {
		return expireAfter;
	}
	public void setExpireAfter(Integer expireAfter) {
		this.expireAfter = expireAfter;
	}
	public String getPublicKeyAlgo() {
		return publicKeyAlgo;
	}
	public void setPublicKeyAlgo(String publicKeyAlgo) {
		this.publicKeyAlgo = publicKeyAlgo;
	}
	public String getCrl() {
		return crl;
	}
	public void setCrl(String crl) {
		this.crl = crl;
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
