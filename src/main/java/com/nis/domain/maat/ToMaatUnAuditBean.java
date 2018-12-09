/**
 *@Title: ToMaatBean.java
 *@Package com.nis.domain.configuration 
 *@Description TODO
 *@author dell
 *@date 2018年2月28日 下午2:03:08
 *@version  版本号
 */
package com.nis.domain.maat;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @ClassName: ToMaatBean.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年2月28日 下午2:03:08
 * @version V1.0
 */
public class ToMaatUnAuditBean implements Serializable{

	/**
	 * @Fields serialVersionUID:TODO（转换为maat格式的java bean）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 5123156423588372849L;
	@Expose
	@SerializedName("serviceCfg")
	private List<UnAuditBean> serviceCfg;
	/**
	 * serviceCfg
	 * @return  serviceCfg
	*/
	
	public List<UnAuditBean> getServiceCfg() {
		return serviceCfg;
	}
	/**
	 * @param serviceCfg the serviceCfg to set
	 */
	public void setServiceCfg(List<UnAuditBean> serviceCfg) {
		this.serviceCfg = serviceCfg;
	}
	
}
