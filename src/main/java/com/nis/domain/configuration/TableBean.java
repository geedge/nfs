/**
 *@Title: TableBean.java
 *@Package com.nis.domain.configuration 
 *@Description TODO
 *@author dell
 *@date 2018年3月24日 下午5:20:51
 *@version  版本号
 */
package com.nis.domain.configuration;

import java.io.Serializable;

/**
 * @ClassName: TableBean.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年3月24日 下午5:20:51
 * @version V1.0
 */
public class TableBean implements Serializable{
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -7276874824312210708L;
	private String tableName;
	private String tableType;
	private String tableDesc;
	private boolean canEmpty;
	public TableBean(){
		
	}
	public TableBean(String tableName,String tableType,String tableDesc){
		this.tableName=tableName;
		this.tableType=tableType;
		this.tableDesc=tableDesc;
	}
	/**
	 * tableName
	 * @return  tableName
	*/
	
	public String getTableName() {
		return tableName;
	}
	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	/**
	 * tableType
	 * @return  tableType
	*/
	
	public String getTableType() {
		return tableType;
	}
	/**
	 * @param tableType the tableType to set
	 */
	public void setTableType(String tableType) {
		this.tableType = tableType;
	}
	/**
	 * canEmpty
	 * @return  canEmpty
	*/
	
	public boolean isCanEmpty() {
		return canEmpty;
	}
	/**
	 * @param canEmpty the canEmpty to set
	 */
	public void setCanEmpty(boolean canEmpty) {
		this.canEmpty = canEmpty;
	}
	/**
	 * tableDesc
	 * @return  tableDesc
	*/
	
	public String getTableDesc() {
		return tableDesc;
	}
	/**
	 * @param tableDesc the tableDesc to set
	 */
	public void setTableDesc(String tableDesc) {
		this.tableDesc = tableDesc;
	}
	
}
