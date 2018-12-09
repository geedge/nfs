package com.nis.domain.configuration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nis.util.excel.ExcelField;

public class AvFileSampleCfg extends BaseCfg<AvFileSampleCfg> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2356472662189941874L;
	private String indexTable="av_file_sample_cfg";
	@Expose
	@SerializedName("srcFile")
	@ExcelField(title="src_file",sort=2)
	private String srcUrl;
	@Expose
	@SerializedName("dstFile")
	@ExcelField(title="sample_file",sort=3)
	private String sampleUrl;
	@Expose
	@SerializedName("srcFileMd5")
	private String srcMd5;
	@Expose
	@SerializedName("dstFileMd5")
	private String sampleMd5;
	@Expose
	@ExcelField(title="harm_level",sort=21)
	private Integer level;
	@Expose
	@SerializedName("cfgId")
	private Integer compileId;
	private String srcPath;
	private String samplePath;
	private Integer isSampleCreated;
	private String resultPath;

	public String getSrcPath() {
		return srcPath;
	}
	public void setSrcPath(String srcPath) {
		this.srcPath = srcPath;
	}
	public String getSamplePath() {
		return samplePath;
	}
	public void setSamplePath(String samplePath) {
		this.samplePath = samplePath;
	}
	public Integer getCompileId() {
		return compileId;
	}
	public void setCompileId(Integer compileId) {
		this.compileId = compileId;
	}
	public String getSrcUrl() {
		return srcUrl;
	}
	public void setSrcUrl(String srcUrl) {
		this.srcUrl = srcUrl;
	}
	public String getSampleUrl() {
		return sampleUrl;
	}
	public void setSampleUrl(String sampleUrl) {
		this.sampleUrl = sampleUrl;
	}
	public String getSrcMd5() {
		return srcMd5;
	}
	public void setSrcMd5(String srcMd5) {
		this.srcMd5 = srcMd5;
	}
	public String getSampleMd5() {
		return sampleMd5;
	}
	public void setSampleMd5(String sampleMd5) {
		this.sampleMd5 = sampleMd5;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getIsSampleCreated() {
		return isSampleCreated;
	}
	public void setIsSampleCreated(Integer isSampleCreated) {
		this.isSampleCreated = isSampleCreated;
	}
	public String getResultPath() {
		return resultPath;
	}
	public void setResultPath(String resultPath) {
		this.resultPath = resultPath;
	}
	public String getIndexTable() {
		return indexTable;
	}
	public void setIndexTable(String indexTable) {
		this.indexTable = indexTable;
	}
	
}
