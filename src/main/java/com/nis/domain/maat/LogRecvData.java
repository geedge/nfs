package com.nis.domain.maat;

import com.nis.domain.Page;

public class LogRecvData<T> {
	
	private Integer status;
	private Integer businessCode;
	private String reason;
	private String msg;
	private String fromuri;
	private String logSource;
	private Page<T> data;
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getBusinessCode() {
		return businessCode;
	}
	public void setBusinessCode(Integer businessCode) {
		this.businessCode = businessCode;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getFromuri() {
		return fromuri;
	}
	public void setFromuri(String fromuri) {
		this.fromuri = fromuri;
	}
	public String getLogSource() {
		return logSource;
	}
	public void setLogSource(String logSource) {
		this.logSource = logSource;
	}
	public Page<T> getData() {
		return data;
	}
	public void setData(Page<T> data) {
		this.data = data;
	}
	
}
