/**
 *@Title: RestfulResult.java
 *@Package com.nis.domain 
 *@Description TODO
 *@author dell
 *@date 2018年6月8日 下午3:22:26
 *@version  版本号
 */
package com.nis.domain;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONObject;

/**
 * @ClassName: RestfulResult.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年6月8日 下午3:22:26
 * @version V1.0
 */
public class RestfulResult {
	protected Integer status=0;
	protected Integer businessCode=0;
	protected String reason="";
	protected String msg="";
	private String traceCode="";
	protected String fromuri="";
	public RestfulResult(){
		
	}
	public RestfulResult(String content){
		try{
			if(StringUtils.isNotBlank(content)){
				JSONObject resObject = JSONObject.fromObject(content) ;
				this.status=resObject.getInt("status");
				this.businessCode=resObject.getInt("businessCode");
				this.reason=resObject.getString("reason");
				this.msg=resObject.getString("msg");
				this.traceCode=resObject.getString("traceCode");
				this.fromuri=resObject.getString("fromuri");
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	/**
	 * status
	 * @return  status
	*/
	
	public Integer getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * businessCode
	 * @return  businessCode
	*/
	
	public Integer getBusinessCode() {
		return businessCode;
	}
	/**
	 * @param businessCode the businessCode to set
	 */
	public void setBusinessCode(Integer businessCode) {
		this.businessCode = businessCode;
	}
	/**
	 * reason
	 * @return  reason
	*/
	
	public String getReason() {
		return reason;
	}
	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	/**
	 * msg
	 * @return  msg
	*/
	
	public String getMsg() {
		return msg;
	}
	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	/**
	 * traceCode
	 * @return  traceCode
	*/
	
	public String getTraceCode() {
		return traceCode;
	}
	/**
	 * @param traceCode the traceCode to set
	 */
	public void setTraceCode(String traceCode) {
		this.traceCode = traceCode;
	}
	/**
	 * fromuri
	 * @return  fromuri
	*/
	
	public String getFromuri() {
		return fromuri;
	}
	/**
	 * @param fromuri the fromuri to set
	 */
	public void setFromuri(String fromuri) {
		this.fromuri = fromuri;
	}
	
}
