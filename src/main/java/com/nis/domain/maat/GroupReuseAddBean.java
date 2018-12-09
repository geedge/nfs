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
public class GroupReuseAddBean implements Serializable{

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
	@SerializedName("groupReuseList")
	private List<GroupReuseCfg> groupReuseCfgList;

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
	
	public List<GroupReuseCfg> getGroupReuseCfgList() {
		return groupReuseCfgList;
	}

	public void setGroupReuseCfgList(List<GroupReuseCfg> groupReuseCfgList) {
		this.groupReuseCfgList = groupReuseCfgList;
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
