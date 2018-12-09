package com.nis.domain.dashboard.codedic;

import java.io.Serializable;

public class CodeAppDic implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8878203808371459079L;

	private Integer id;

    private String appName;

    private String appNameZh;

    private String viewCode;

    private String icon;

    private String behavior;

    private Integer coreApp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName == null ? null : appName.trim();
    }

    public String getAppNameZh() {
        return appNameZh;
    }

    public void setAppNameZh(String appNameZh) {
        this.appNameZh = appNameZh == null ? null : appNameZh.trim();
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

    public String getBehavior() {
        return behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior == null ? null : behavior.trim();
    }

    public Integer getCoreApp() {
        return coreApp;
    }

    public void setCoreApp(Integer coreApp) {
        this.coreApp = coreApp;
    }
}