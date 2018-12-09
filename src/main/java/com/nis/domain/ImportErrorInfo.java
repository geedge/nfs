/**
 * 
 */
package com.nis.domain;

import java.io.Serializable;

/**
 * @ClassName:ImportErrorInfo
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author (zdx)
 * @date 2018年4月25日 上午10:39:47
 * @version V1.0
 */
public class ImportErrorInfo implements Serializable{
	private static final long serialVersionUID = -8044793465646498657L;
	private String rowNum;
	private String fieldName;
	private String errorMessage;
	
	public ImportErrorInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param rowNum 行号
	 * @param fieldName 属性名称
	 * @param errorMessage 错误信息
	 */
	public ImportErrorInfo(String rowNum, String fieldName, String errorMessage) {
		super();
		this.rowNum = rowNum;
		this.fieldName = fieldName;
		this.errorMessage = errorMessage;
	}


	public String getRowNum() {
		return rowNum;
	}
	public void setRowNum(String rowNum) {
		this.rowNum = rowNum;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	@Override
	public String toString() {
		return "ImportErrorInfo [rowNum=" + rowNum + ", fieldName=" + fieldName
				+ ", errorMessage=" + errorMessage + "]";
	}
	
}
