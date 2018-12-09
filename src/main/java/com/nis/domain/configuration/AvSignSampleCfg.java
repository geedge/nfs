package com.nis.domain.configuration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AvSignSampleCfg extends BaseCfg<AvSignSampleCfg> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8002327858986390761L;
	@Expose
	private String description;
	@Expose
	private Integer level;
	@Expose
	@SerializedName("cfgId")
	private Integer compileId;
	
	private Integer isValidOld;
	private Integer actionOld;//判断动作是否变更
	
	public Integer getActionOld() {
		return actionOld;
	}
	public void setActionOld(Integer actionOld) {
		this.actionOld = actionOld;
	}
	public Integer getIsValidOld() {
		return isValidOld;
	}
	public void setIsValidOld(Integer isValidOld) {
		this.isValidOld = isValidOld;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getCompileId() {
		return compileId;
	}
	public void setCompileId(Integer compileId) {
		this.compileId = compileId;
	}
	
}
