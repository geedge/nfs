/**
 *@Title: UnAuditBean.java
 *@Package com.nis.domain.maat 
 *@Description TODO
 *@author dell
 *@date 2018年3月26日 下午1:30:07
 *@version  版本号
 */
package com.nis.domain.maat;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @ClassName: UnAuditBean.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年3月26日 下午1:30:07
 * @version V1.0
 */
public class UnAuditBean implements Serializable{

	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -3497633085090859526L;
	@Expose
	@SerializedName("compileId")
	private Integer compileId;
	@Expose
	@SerializedName("maatTable")
	private String maatTable;
	/**
	 * compileId
	 * @return  compileId
	*/
	
	public Integer getCompileId() {
		return compileId;
	}
	/**
	 * @param compileId the compileId to set
	 */
	public void setCompileId(Integer compileId) {
		this.compileId = compileId;
	}
	/**
	 * maatTable
	 * @return  maatTable
	*/
	
	public String getMaatTable() {
		return maatTable;
	}
	/**
	 * @param maatTable the maatTable to set
	 */
	public void setMaatTable(String maatTable) {
		this.maatTable = maatTable;
	}
}
