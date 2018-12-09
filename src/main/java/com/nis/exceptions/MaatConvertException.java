package com.nis.exceptions;

import com.nis.domain.RestfulResult;


public class MaatConvertException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final String prefix="<spring:message code=\"request_service_failed\"/>:";
	private RestfulResult result;
	public MaatConvertException(String message) {
		super(message);
		if(message.startsWith(prefix)){
			String returnValue=message.replaceFirst(prefix, "");
			result=new RestfulResult(returnValue);
		}else{
			result=new RestfulResult();
		}
	}

	public MaatConvertException(String message, Throwable cause) {
		super(message, cause);
		if(message.startsWith(prefix)){
			result=new RestfulResult(message.replaceFirst(prefix, ""));
		}else{
			result=new RestfulResult();
		}
	}
	
	/**
	 * result
	 * @return  result
	*/
	
	public RestfulResult getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(RestfulResult result) {
		this.result = result;
	}

	/**
	 * prefix
	 * @return  prefix
	*/
	
	public static String getPrefix() {
		return prefix;
	}
	
}
