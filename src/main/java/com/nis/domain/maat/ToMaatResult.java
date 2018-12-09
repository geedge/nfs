/**
 *@Title: ToMaatResult.java
 *@Package com.nis.domain.maat 
 *@Description TODO
 *@author dell
 *@date 2018年3月27日 下午5:10:01
 *@version  版本号
 */
package com.nis.domain.maat;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: ToMaatResult.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年3月27日 下午5:10:01
 * @version V1.0
 */
public class ToMaatResult implements Serializable{
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 4244178887886507606L;
	private Integer status;
	private Integer businessCode;
	private String reason;
	private String msg;
	private String fromuri;
	private ResponseData data;
	public static class ResponseData{
		private String sourceName;
		private Integer num;
		private List<Integer> pzIdList;
		private String accessUrl;
		private Long rawLen;
		private String digest;
		private List configCompileList;
		private List updateStatCfgList;
		private List groupReuseList;
		
		public List getGroupReuseList() {
			return groupReuseList;
		}
		public void setGroupReuseList(List groupReuseList) {
			this.groupReuseList = groupReuseList;
		}
		public List getUpdateStatCfgList() {
			return updateStatCfgList;
		}
		public void setUpdateStatCfgList(List updateStatCfgList) {
			this.updateStatCfgList = updateStatCfgList;
		}
		public String getSourceName() {
			return sourceName;
		}
		public void setSourceName(String sourceName) {
			this.sourceName = sourceName;
		}
		public Integer getNum() {
			return num;
		}
		public void setNum(Integer num) {
			this.num = num;
		}
		public List<Integer> getPzIdList() {
			return pzIdList;
		}
		public void setPzIdList(List<Integer> pzIdList) {
			this.pzIdList = pzIdList;
		}
		public String getAccessUrl() {
			return accessUrl;
		}
		public void setAccessUrl(String accessUrl) {
			this.accessUrl = accessUrl;
		}
		public Long getRawLen() {
			return rawLen;
		}
		public void setRawLen(Long rawLen) {
			this.rawLen = rawLen;
		}
		public String getDigest() {
			return digest;
		}
		public void setDigest(String digest) {
			this.digest = digest;
		}
		public List getConfigCompileList() {
			return configCompileList;
		}
		public void setConfigCompileList(List configCompileList) {
			this.configCompileList = configCompileList;
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
	public ResponseData getData() {
		return data;
	}
	public void setData(ResponseData data) {
		this.data = data;
	}
}
