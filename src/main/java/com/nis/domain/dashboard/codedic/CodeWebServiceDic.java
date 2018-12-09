package com.nis.domain.dashboard.codedic;

import java.io.Serializable;

public class CodeWebServiceDic implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3695848998373942822L;

	private Integer id;

    private String website;

    private String domainName;

    private String viewCode;

    private String icon;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website == null ? null : website.trim();
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName == null ? null : domainName.trim();
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