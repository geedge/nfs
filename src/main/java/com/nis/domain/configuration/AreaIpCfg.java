/**
 *@Title: AreaIpCfg.java
 *@Package com.nis.domain.configuration 
 *@Description TODO
 *@author dell
 *@date 2018年2月8日 上午9:47:29
 *@version  版本号
 */
package com.nis.domain.configuration;

import com.nis.util.Constants;

/**
 * @ClassName: AreaIpCfg.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年2月8日 上午9:47:29
 * @version V1.0
 */
public class AreaIpCfg extends BaseIpCfg {
	private static final String tableName="area_ip_cfg";
	private static final String maatTable="DF_IP_RANGE";
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 2367503808235405524L;
	
	/* (是否区域gk对此表无意义，直接设置0值)
	 * @see com.nis.domain.configuration.BaseCfg#setIsAreaEffective(java.lang.Integer)
	 */
	@Override
	public void setIsAreaEffective(Integer isAreaEffective) {
		// TODO Auto-generated method stub
		this.isAreaEffective=0;
	}
	
	/* (区域生效id对此表无意义，直接设置null值)
	 * @see com.nis.domain.configuration.BaseCfg#setAreaEffectiveIds(java.lang.String)
	 */
	@Override
	public void setAreaEffectiveIds(String areaEffectiveIds) {
		// TODO Auto-generated method stub
		this.areaEffectiveIds=null;
	}

	/* (non-Javadoc)
	 * @see com.nis.domain.configuration.BaseIpCfg#initDefaultValue()
	 */
	@Override
	public void initDefaultValue() {
		// TODO Auto-generated method stub
		super.initDefaultValue();
		this.protocolId=0;
		this.isAreaEffective=0;
		this.areaEffectiveIds=null;
		this.cfgType=Constants.AREA_REGION;
		this.cfgRegionCode=1;
		this.protocol=0;
		this.portPattern=1;
		this.srcPort="0";
		this.destPort="0";
		this.direction=0;
		if(this.ipType!=null && (this.ipType==4||this.ipType==46)){
			this.destIpAddress="0.0.0.0";
		}else if(this.ipType!=null && (this.ipType==6 || this.ipType==64)){
			this.destIpAddress="::";
		}else{
			this.destIpAddress="0.0.0.0";
		}
		
	}

	/**
	 * tablename
	 * @return  tablename
	*/
	
	public static String getTablename() {
		return tableName;
	}

	/**
	 * maattable
	 * @return  maattable
	*/
	
	public static String getMaattable() {
		return maatTable;
	}
	
}
