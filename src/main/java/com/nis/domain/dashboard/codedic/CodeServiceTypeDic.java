package com.nis.domain.dashboard.codedic;

import java.io.Serializable;

public class CodeServiceTypeDic implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4684291961768554944L;

	private Integer id;

    private String serviceType;

    private String viewCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType == null ? null : serviceType.trim();
    }

    public String getViewCode() {
        return viewCode;
    }

    public void setViewCode(String viewCode) {
        this.viewCode = viewCode == null ? null : viewCode.trim();
    }
}