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
public class NtcSrcipDomesticReport extends BaseReport<NtcSrcipDomesticReport> {

	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 2245672277797057001L;
	private String srcProvince;
	private String srcCity;
	/**
	 * srcProvince
	 * @return  srcProvince
	*/
	
	public String getSrcProvince() {
		return srcProvince;
	}
	/**
	 * @param srcProvince the srcProvince to set
	 */
	public void setSrcProvince(String srcProvince) {
		this.srcProvince = srcProvince;
	}
	/**
	 * srcCity
	 * @return  srcCity
	*/
	
	public String getSrcCity() {
		return srcCity;
	}
	/**
	 * @param srcCity the srcCity to set
	 */
	public void setSrcCity(String srcCity) {
		this.srcCity = srcCity;
	}
	
}
