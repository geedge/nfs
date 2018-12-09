/**
 *@Title: ReportLog.java
 *@Package com.nis.domain.log 
 *@Description TODO
 *@author dell
 *@date 2018年6月13日 下午2:21:04
 *@version  版本号
 */
package com.nis.domain.report;

import java.util.Date;

/**
 * @ClassName: ReportLog.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年6月13日 下午2:21:04
 * @version V1.0
 */
public class NtcPzReport extends BaseReport<NtcPzReport>{
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 7079305499220977782L;
	private Long cfgId;
	private Date statTime;
	
	public Long getCfgId() {
		return cfgId;
	}
	public void setCfgId(Long cfgId) {
		this.cfgId = cfgId;
	}
	/**
	 * statTime
	 * @return  statTime
	*/
	
	public Date getStatTime() {
		return statTime;
	}
	/**
	 * @param statTime the statTime to set
	 */
	public void setStatTime(Date statTime) {
		this.statTime = statTime;
	}
	
}
