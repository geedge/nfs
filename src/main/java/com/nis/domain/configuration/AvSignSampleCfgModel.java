package com.nis.domain.configuration;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AvSignSampleCfgModel extends BaseCfg<AvSignSampleCfgModel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7579758192797537509L;
	private List<AvSignSampleCfg> cfgs;
	public List<AvSignSampleCfg> getCfgs() {
		return cfgs;
	}
	public void setCfgs(List<AvSignSampleCfg> cfgs) {
		this.cfgs = cfgs;
	}
	
	
}
