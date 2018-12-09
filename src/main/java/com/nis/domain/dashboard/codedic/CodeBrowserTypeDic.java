package com.nis.domain.dashboard.codedic;

import java.io.Serializable;

public class CodeBrowserTypeDic implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2134192971712765277L;

	private Integer id;

    private String browserType;

    private String viewCode;

    private String icon;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBrowserType() {
        return browserType;
    }

    public void setBrowserType(String browserType) {
        this.browserType = browserType == null ? null : browserType.trim();
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