package com.nis.domain.maat;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EffectiveRangeCfg implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4503782443649076836L;
	@Expose
	@SerializedName("tag_sets")
	private List<List<AreaIsp>> effectiveRangeList;
	
	public List<List<AreaIsp>> getEffectiveRangeList() {
		return effectiveRangeList;
	}

	public void setEffectiveRangeList(List<List<AreaIsp>> effectiveRangeList) {
		this.effectiveRangeList = effectiveRangeList;
	}

	public static class AreaIsp{
		@Expose
		private String tag;
		@Expose
		private List<String> value;
		
		public String getTag() {
			return tag;
		}
		public void setTag(String tag) {
			this.tag = tag;
		}
		public List<String> getValue() {
			return value;
		}
		public void setValue(List<String> value) {
			this.value = value;
		}
		@Override
		public String toString() {
			return "AreaIsp [tag=" + tag + ", value=" + value + "]";
		}
	}
}
