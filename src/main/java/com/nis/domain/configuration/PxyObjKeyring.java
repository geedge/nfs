package com.nis.domain.configuration;

import java.util.Date;

import com.nis.util.excel.ExcelField;

/**
 * 拦截证书策略
 * @author dell
 *
 */
public class PxyObjKeyring extends BaseCfg<PxyObjKeyring> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2720862431960415564L;
	private String indexTable="pxy_obj_keyring";
	@ExcelField(title="keyring_type",dictType="INTERCEPT_CERTIFICATE_TYPE",sort=2)
	private String keyringType;
	@ExcelField(title="private_key_file",sort=3)
	private String privateKeyFile;
	@ExcelField(title="public_key_file",sort=4)
	private String publicKeyFile;
	@ExcelField(title="expire_after",sort=5)
	private Integer expireAfter;
	private String publicKeyAlgo;
	private String crl;
	@ExcelField(title="issuer",sort=6)
	private String issuer;
	@ExcelField(title="not_before_time",sort=8)
	private String notBeforeTime;
	@ExcelField(title="not_after_time",sort=9)
	private String notAfterTime;
	@ExcelField(title="certificate_subject",sort=7)
	private String subject;
	@ExcelField(title="keyring_name",sort=1)
	private String keyringName;
	private String cn;
	private String altName;
	
	public String getCn() {
		return cn;
	}
	public String getAltName() {
		return altName;
	}
	public void setCn(String cn) {
		this.cn = cn;
	}
	public void setAltName(String altName) {
		this.altName = altName;
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
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	public String getNotAfterTime() {
		return notAfterTime;
	}
	public String getNotBeforeTime() {
		return notBeforeTime;
	}
	public void setNotAfterTime(String notAfterTime) {
		this.notAfterTime = notAfterTime;
	}
	public void setNotBeforeTime(String notBeforeTime) {
		this.notBeforeTime = notBeforeTime;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getIndexTable() {
		return indexTable;
	}
	public void setIndexTable(String indexTable) {
		this.indexTable = indexTable;
	}
	
	
}
