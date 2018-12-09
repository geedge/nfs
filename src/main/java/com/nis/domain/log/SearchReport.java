/**
 *@Title: SearchReport.java
 *@Package com.nis.domain.log 
 *@Description TODO
 *@author dell
 *@date 2018年7月5日 下午12:30:44
 *@version  版本号
 */
package com.nis.domain.log;

import java.util.Date;
import java.util.HashMap;

import com.nis.domain.BaseEntity;

/**
 * @ClassName: SearchReport.java 
 * @Description: TODO
 * @author (wx)
 * @date 2018年7月5日 下午12:30:44
 * @version V1.0
 */
public class SearchReport extends BaseEntity<SearchReport>{
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 1L;
	private Integer functionId;
	private String cfgName;
	private Integer action;
	private Integer serviceId;
	private String services;//service日志总量统计使用
	private Integer reportType;//小时报，日报，月报
	//标签，性质，来文函号，境内源Ip,各国目的IP，运营商局点
	private String reportBusinessType;
	private String searchBusinessType;
	private String searchReportStartTime;
	private Date reportStartTime;
	private String searchReportEndTime;
	private Date reportEndTime;
	private String searchService;
	private HashMap<String,Object> searchCondition;
	public static final String searchConditionSplitor=",";
	public int searchConditionLimit;
	private Integer pageNo;
	private Integer pageSize;
	private String orderBy;
	private String fields;
	//界面查询的时间
	private String reportTime;
	
	
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public String getServices() {
		return services;
	}
	public void setServices(String services) {
		this.services = services;
	}
	/**
	 * reportStartTime
	 * @return  reportStartTime
	*/
	
	public Date getReportStartTime() {
		return reportStartTime;
	}
	/**
	 * @param reportStartTime the reportStartTime to set
	 */
	public void setReportStartTime(Date reportStartTime) {
		this.reportStartTime = reportStartTime;
	}
	/**
	 * reportEndTime
	 * @return  reportEndTime
	*/
	
	public Date getReportEndTime() {
		return reportEndTime;
	}
	/**
	 * @param reportEndTime the reportEndTime to set
	 */
	public void setReportEndTime(Date reportEndTime) {
		this.reportEndTime = reportEndTime;
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
	/**
	 * pageNo
	 * @return  pageNo
	*/
	
	public Integer getPageNo() {
		return pageNo;
	}
	/**
	 * @param pageNo the pageNo to set
	 */
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	/**
	 * pageSize
	 * @return  pageSize
	*/
	
	public Integer getPageSize() {
		return pageSize;
	}
	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	/**
	 * orderBy
	 * @return  orderBy
	*/
	
	public String getOrderBy() {
		return orderBy;
	}
	/**
	 * @param orderBy the orderBy to set
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	/**
	 * fields
	 * @return  fields
	*/
	
	public String getFields() {
		return fields;
	}
	/**
	 * @param fields the fields to set
	 */
	public void setFields(String fields) {
		this.fields = fields;
	}
	/**
	 * reportBusinessType
	 * @return  reportBusinessType
	*/
	
	public String getReportBusinessType() {
		return reportBusinessType;
	}
	/**
	 * @param reportBusinessType the reportBusinessType to set
	 */
	public void setReportBusinessType(String reportBusinessType) {
		this.reportBusinessType = reportBusinessType;
	}
	/**
	 * reportType
	 * @return  reportType
	*/
	
	public Integer getReportType() {
		return reportType;
	}
	/**
	 * @param reportType the reportType to set
	 */
	public void setReportType(Integer reportType) {
		this.reportType = reportType;
	}
	/**
	 * functionId
	 * @return  functionId
	*/
	
	public Integer getFunctionId() {
		return functionId;
	}
	/**
	 * @param functionId the functionId to set
	 */
	public void setFunctionId(Integer functionId) {
		this.functionId = functionId;
	}
	/**
	 * cfgName
	 * @return  cfgName
	*/
	
	public String getCfgName() {
		return cfgName;
	}
	/**
	 * @param cfgName the cfgName to set
	 */
	public void setCfgName(String cfgName) {
		this.cfgName = cfgName;
	}
	/**
	 * action
	 * @return  action
	*/
	
	public Integer getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(Integer action) {
		this.action = action;
	}
	/**
	 * searchBusinessType
	 * @return  searchBusinessType
	*/
	
	public String getSearchBusinessType() {
		return searchBusinessType;
	}
	/**
	 * @param searchBusinessType the searchBusinessType to set
	 */
	public void setSearchBusinessType(String searchBusinessType) {
		this.searchBusinessType = searchBusinessType;
	}
	/**
	 * searchReportStartTime
	 * @return  searchReportStartTime
	*/
	
	public String getSearchReportStartTime() {
		return searchReportStartTime;
	}
	/**
	 * @param searchReportStartTime the searchReportStartTime to set
	 */
	public void setSearchReportStartTime(String searchReportStartTime) {
		this.searchReportStartTime = searchReportStartTime;
	}
	/**
	 * searchReportEndTime
	 * @return  searchReportEndTime
	*/
	
	public String getSearchReportEndTime() {
		return searchReportEndTime;
	}
	/**
	 * @param searchReportEndTime the searchReportEndTime to set
	 */
	public void setSearchReportEndTime(String searchReportEndTime) {
		this.searchReportEndTime = searchReportEndTime;
	}
	/**
	 * searchService
	 * @return  searchService
	*/
	
	public String getSearchService() {
		return searchService;
	}
	/**
	 * @param searchService the searchService to set
	 */
	public void setSearchService(String searchService) {
		this.searchService = searchService;
	}
	/**
	 * searchCondition
	 * @return  searchCondition
	*/
	
	public HashMap<String, Object> getSearchCondition() {
		return searchCondition;
	}
	/**
	 * @param searchCondition the searchCondition to set
	 */
	public void setSearchCondition(HashMap<String, Object> searchCondition) {
		this.searchCondition = searchCondition;
	}
	/**
	 * searchConditionLimit
	 * @return  searchConditionLimit
	*/
	
	public int getSearchConditionLimit() {
		return searchConditionLimit;
	}
	/**
	 * @param searchConditionLimit the searchConditionLimit to set
	 */
	public void setSearchConditionLimit(int searchConditionLimit) {
		this.searchConditionLimit = searchConditionLimit;
	}
	/**
	 * searchconditionsplitor
	 * @return  searchconditionsplitor
	*/
	
	public static String getSearchconditionsplitor() {
		return searchConditionSplitor;
	}
	
}
