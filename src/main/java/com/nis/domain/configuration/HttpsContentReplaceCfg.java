/**
 *@Title: HttpsContentReplaceConfig.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2018年2月6日 上午10:30:46
 *@version  版本号
 */
package com.nis.domain.configuration;

/**
 * @ClassName: HttpsContentReplaceConfig.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年2月6日 上午10:30:46
 * @version V1.0
 */
public class HttpsContentReplaceCfg extends BaseStringCfg<HttpsContentReplaceCfg> {

	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -116828220692786004L;
	/**
	 * 网站名称
	 */
	private String webName;
	/**
	 * 源内容
	 */
	private String srcContent;
	/**
	 * 替换内容
	 */
	private String destContent;
	/**
	 * webName
	 * @return  webName
	*/
	
	public String getWebName() {
		return webName;
	}
	/**
	 * @param webName the webName to set
	 */
	public void setWebName(String webName) {
		this.webName = webName;
	}
	/**
	 * srcContent
	 * @return  srcContent
	*/
	
	public String getSrcContent() {
		return srcContent;
	}
	/**
	 * @param srcContent the srcContent to set
	 */
	public void setSrcContent(String srcContent) {
		this.srcContent = srcContent;
	}
	/**
	 * destContent
	 * @return  destContent
	*/
	
	public String getDestContent() {
		return destContent;
	}
	/**
	 * @param destContent the destContent to set
	 */
	public void setDestContent(String destContent) {
		this.destContent = destContent;
	}
	
	/* (non-Javadoc)
	 * @see com.nis.domain.configuration.BaseStringCfg#initDefaultValue()
	 */
	@Override
	public void initDefaultValue() {
		// TODO Auto-generated method stub
		super.initDefaultValue();
		this.exprType=0;
		this.matchMethod=3;
	}
	
}
