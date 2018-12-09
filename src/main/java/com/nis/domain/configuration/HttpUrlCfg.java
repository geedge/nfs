/**
 *@Title: HttpDomainConfig.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2018年2月5日 下午5:33:05
 *@version  版本号
 */
package com.nis.domain.configuration;

import com.nis.domain.Page;
import com.nis.util.Constants;

/**
 * @ClassName: HttpDomainConfig.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年2月5日 下午5:33:05
 * @version V1.0
 */
public class HttpUrlCfg extends BaseStringCfg<HttpUrlCfg> {
	private static final String tableName="http_url_cfg";
	private String ratelimit;
	/**
	 * ratelimit
	 * @return  ratelimit
	*/
	
	public String getRatelimit() {
		return ratelimit;
	}
	/**
	 * @param ratelimit the ratelimit to set
	 */
	public void setRatelimit(String ratelimit) {
		this.ratelimit = ratelimit;
	}
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 8231984530204284334L;
	/* (non-Javadoc)
	 * @see com.nis.domain.configuration.BaseStringCfg#initDefaultValue()
	 */
	@Override
	public void initDefaultValue() {
		// TODO Auto-generated method stub
		super.initDefaultValue();
		this.exprType=0;
		this.matchMethod=0;
	}
	/**
	 * tablename
	 * @return  tablename
	*/
	
	public static String getTablename() {
		return tableName;
	}
}
