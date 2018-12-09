/**
 *@Title: BaseReportLog.java
 *@Package com.nis.domain.log 
 *@Description TODO
 *@author dell
 *@date 2018年7月5日 上午11:12:48
 *@version  版本号
 */
package com.nis.domain.report;

import com.nis.domain.BaseEntity;

/**
 * @ClassName: BaseReportLog.java 
 * @Description: 报表接口返回的公共字段
 * @author (wx)
 * @date 2018年7月5日 上午11:12:48
 * @version V1.0
 */
public class BaseReport<T> extends BaseEntity<T>{
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -6190203013788730697L;
	protected Long sum;
	protected Integer service;
	protected String reportTime;
	protected String time;
	/**
	 * sum
	 * @return  sum
	*/
	
	public Long getSum() {
		return sum;
	}
	/**
	 * @param sum the sum to set
	 */
	public void setSum(Long sum) {
		this.sum = sum;
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
	/**
	 * reportTime
	 * @return  reportTime
	*/
	
	public String getReportTime() {
		return reportTime;
	}
	/**
	 * @param reportTime the reportTime to set
	 */
	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
}
