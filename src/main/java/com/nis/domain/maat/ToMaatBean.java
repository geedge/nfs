/**
 *@Title: ToMaatBean.java
 *@Package com.nis.domain.configuration 
 *@Description TODO
 *@author dell
 *@date 2018年2月28日 下午2:03:08
 *@version  版本号
 */
package com.nis.domain.maat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @ClassName: ToMaatBean.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年2月28日 下午2:03:08
 * @version V1.0
 */
public class ToMaatBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3468862666445104490L;
	/**
	 * @Fields serialVersionUID:TODO（转换为maat格式的java bean）
	 *
	 * @since 1.0.0
	 */
	@Expose
	private String version;	
	@Expose
	@SerializedName("operator")
	private String creatorName;
	@Expose
	@SerializedName("opTime")
	private Date auditTime;
	@Expose
	private Integer opAction;
	@Expose
	private List<MaatCfg> configCompileList;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public List<MaatCfg> getConfigCompileList() {
		return configCompileList;
	}

	public void setConfigCompileList(List<MaatCfg> configCompileList) {
		this.configCompileList = configCompileList;
	}
	public Integer getOpAction() {
		return opAction;
	}

	public void setOpAction(Integer opAction) {
		this.opAction = opAction;
	}

	public static void main(String[] args) {
		
	}
}
