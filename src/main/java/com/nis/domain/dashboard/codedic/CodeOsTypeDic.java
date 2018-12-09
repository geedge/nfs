package com.nis.domain.dashboard.codedic;

import java.io.Serializable;

public class CodeOsTypeDic implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3130292317922505634L;

	private Integer id;

    private String osType;

    private String viewCode;

    private String icon;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType == null ? null : osType.trim();
    }

    public String getViewCode() {
        return viewCode;
    }

    public void setViewCode(String viewCode) {
        this.viewCode = viewCode == null ? null : viewCode.trim();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }
}