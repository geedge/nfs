package com.nis.domain.configuration;

import java.util.Date;

/**
 * 内置文件上传
 * @author dell
 *
 */
public class AppBuiltInFeatureFile extends BaseCfg<AppBuiltInFeatureFile> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2720862431960415564L;
	private String filePath;
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
