/**
 *@Title: CfgIndexInfo.java
 *@Package com.nis.domain.restful 
 *@Description 索引公共表
 *@author dell
 *@date 2018年5月17日 下午16:59:17
 *@version  版本号
 */
package com.nis.domain.configuration;

import java.util.List;

import com.nis.util.excel.ExcelField;


/**
 * 文件摘要配置
 * @ClassName: FileDigestCfg.java 
 * @author (dell)
 * @date 2018年5月17日 下午16:59:17
 * @version V1.0
 */
public class FileDigestCfg extends BaseCfg<FileDigestCfg> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2796500715438264119L;
	private static final String tableName="file_digest_cfg";
	private String indexTable="file_digest_cfg";
	@ExcelField(title="raw_len",sort=3)
	private Long rawLen;
	@ExcelField(title="digest",sort=2)
	private String digest;
	@ExcelField(title="cfds_level",sort=4)
	private Integer cfdsLevel;
	@ExcelField(title="file_url",sort=5)
	private String fileUrl;
	
	private NtcSubscribeIdCfg ntcSubscribeIdCfg;
	private List<NtcSubscribeIdCfg> ntcSubscribeIdCfgList;
	
	public static String getTablename() {
		return tableName;
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
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public NtcSubscribeIdCfg getNtcSubscribeIdCfg() {
		return ntcSubscribeIdCfg;
	}
	public void setNtcSubscribeIdCfg(NtcSubscribeIdCfg ntcSubscribeIdCfg) {
		this.ntcSubscribeIdCfg = ntcSubscribeIdCfg;
	}
	public List<NtcSubscribeIdCfg> getNtcSubscribeIdCfgList() {
		return ntcSubscribeIdCfgList;
	}
	public void setNtcSubscribeIdCfgList(List<NtcSubscribeIdCfg> ntcSubscribeIdCfgList) {
		this.ntcSubscribeIdCfgList = ntcSubscribeIdCfgList;
	}
	public String getIndexTable() {
		return indexTable;
	}
	public void setIndexTable(String indexTable) {
		this.indexTable = indexTable;
	}

}
