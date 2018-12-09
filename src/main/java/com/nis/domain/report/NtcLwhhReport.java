/**
 *@Title: NtcServiceReportLog.java
 *@Package com.nis.domain.log 
 *@Description TODO
 *@author dell
 *@date 2018年7月9日 上午8:57:01
 *@version  版本号
 */
package com.nis.domain.report;

/**
 * @ClassName: NtcServiceReportLog.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年7月9日 上午8:57:01
 * @version V1.0
 */
public class NtcLwhhReport extends BaseReport<NtcLwhhReport> {

	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 2245672277797057001L;
	private Integer lwhh;
	/**
	 * lwhh
	 * @return  lwhh
	*/
	
	public Integer getLwhh() {
		return lwhh;
	}
	/**
	 * @param lwhh the lwhh to set
	 */
	public void setLwhh(Integer lwhh) {
		this.lwhh = lwhh;
	}
	
}
