package com.nis.domain.dashboard.codedic;

import java.io.Serializable;

public class CodeBehaviorTypeDic implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3093079166837898180L;

	private Integer id;

    private String behaviorType;

    private String viewCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBehaviorType() {
        return behaviorType;
    }

    public void setBehaviorType(String behaviorType) {
        this.behaviorType = behaviorType == null ? null : behaviorType.trim();
    }

    public String getViewCode() {
        return viewCode;
    }

    public void setViewCode(String viewCode) {
        this.viewCode = viewCode == null ? null : viewCode.trim();
    }
}