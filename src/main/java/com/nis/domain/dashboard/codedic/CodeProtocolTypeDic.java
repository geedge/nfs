package com.nis.domain.dashboard.codedic;

import java.io.Serializable;

public class CodeProtocolTypeDic implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4918506981012927864L;

	private Integer id;

    private String protocolType;

    private String viewCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType == null ? null : protocolType.trim();
    }

    public String getViewCode() {
        return viewCode;
    }

    public void setViewCode(String viewCode) {
        this.viewCode = viewCode == null ? null : viewCode.trim();
    }
}