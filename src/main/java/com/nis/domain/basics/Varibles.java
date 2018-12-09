package com.nis.domain.basics;

import java.io.Serializable;

public class Varibles implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5747624301389103730L;
	private String variableName;
	private Long value;
	public String getVariableName() {
		return variableName;
	}
	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
	public Long getValue() {
		return value;
	}
	public void setValue(Long value) {
		this.value = value;
	}
	
}
