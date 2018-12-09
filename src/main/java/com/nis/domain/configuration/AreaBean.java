/**
 *@Title: AreaBean.java
 *@Package com.nis.domain.configuration 
 *@Description TODO
 *@author dell
 *@date 2018年4月8日 上午10:06:11
 *@version  版本号
 */
package com.nis.domain.configuration;

/**
 * @ClassName: AreaBean.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年4月8日 上午10:06:11
 * @version V1.0
 */
public class AreaBean {
	private String area;
	private String isp;
	private String areaName;
	private String ispName;
	/**
	 * area
	 * @return  area
	*/
	
	public String getArea() {
		return area;
	}
	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}
	/**
	 * isp
	 * @return  isp
	*/
	
	public String getIsp() {
		return isp;
	}
	/**
	 * @param isp the isp to set
	 */
	public void setIsp(String isp) {
		this.isp = isp;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public void setIspName(String ispName) {
		this.ispName = ispName;
	}
	public String getIspName() {
		return ispName;
	}
	
}
