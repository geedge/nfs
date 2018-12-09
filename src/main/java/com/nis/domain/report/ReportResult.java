/**
 *@Title: ReportResultLog.java
 *@Package com.nis.domain.log 
 *@Description TODO
 *@author dell
 *@date 2018年6月11日 下午2:04:09
 *@version  版本号
 */
package com.nis.domain.report;

import com.nis.domain.Page;
import com.nis.domain.RestfulResult;

/**
 * @ClassName: ReportResultLog.java 
 * @Description: TODO
 * @author (wx)
 * @date 2018年6月11日 下午2:04:09
 * @version V1.0
 */
public class ReportResult extends RestfulResult{
	private Integer logSource;
	private Page data;
	/**
	 * logSource
	 * @return  logSource
	*/
	
	public Integer getLogSource() {
		return logSource;
	}
	/**
	 * @param logSource the logSource to set
	 */
	public void setLogSource(Integer logSource) {
		this.logSource = logSource;
	}
	/**
	 * data
	 * @return  data
	*/
	
	public Page getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(Page data) {
		this.data = data;
	}
}