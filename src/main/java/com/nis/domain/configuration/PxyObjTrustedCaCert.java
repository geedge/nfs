package com.nis.domain.configuration;

import com.nis.util.excel.ExcelField;

/**
 * 可信证书
 * @author ddm
 *
 */
public class PxyObjTrustedCaCert extends BaseCfg<PxyObjTrustedCaCert> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2720862431960415564L;
	private String indexTable="pxy_obj_trusted_ca_cert";
	@ExcelField(title="issuer",sort=3)
	private String issuer; //颁发者
	@ExcelField(title="certificate_file",sort=2)
	private String certFile; 
	@ExcelField(title="crl_file",sort=4)
	private String crlFile;
	
	public String getCrlFile() {
		return crlFile;
	}
	public void setCrlFile(String crlFile) {
		this.crlFile = crlFile;
	}
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	
	public String getCertFile() {
		return certFile;
	}
	public void setCertFile(String certFile) {
		this.certFile = certFile;
	}
	
	public String getIndexTable() {
		return indexTable;
	}
	public void setIndexTable(String indexTable) {
		this.indexTable = indexTable;
	}
	
	
}
