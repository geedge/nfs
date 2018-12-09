package com.nis.domain.configuration;

import java.util.Date;

import com.nis.util.excel.ExcelField;

/**
 * 吊销证书列表
 * @author ddm
 *
 */
public class PxyObjTrustedCaCrl extends BaseCfg<PxyObjTrustedCaCrl> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2720862431960415564L;
	private String indexTable="pxy_obj_trusted_ca_crl";
	private String issuer; //颁发者
	private Integer certId; //证书Id 默认证书为空
	private String crlFile; //crlFile
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	
	public String getCrlFile() {
		return crlFile;
	}
	public void setCrlFile(String crlFile) {
		this.crlFile = crlFile;
	}
	public Integer getCertId() {
		return certId;
	}
	public void setCertId(Integer certId) {
		this.certId = certId;
	}
	public String getIndexTable() {
		return indexTable;
	}
	public void setIndexTable(String indexTable) {
		this.indexTable = indexTable;
	}
	
	
}
