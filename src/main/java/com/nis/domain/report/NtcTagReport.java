/**
 *@Title: NtcServiceReportLog.java
 *@Package com.nis.domain.log 
 *@Description TODO
 *@author dell
 *@date 2018年7月9日 上午8:57:01
 *@version  版本号
 */
package com.nis.domain.report;

/**
 * @ClassName: NtcServiceReportLog.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年7月9日 上午8:57:01
 * @version V1.0
 */
public class NtcTagReport extends BaseReport<NtcTagReport> {

	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 2245672277797057001L;
	private Integer tag;
	/**
	 * tag
	 * @return  tag
	*/
	
	public Integer getTag() {
		return tag;
	}
	/**
	 * @param tag the tag to set
	 */
	public void setTag(Integer tag) {
		this.tag = tag;
	}
	
}
