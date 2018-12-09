/**
 *@Title: IpPortConfig.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2018年2月5日 下午3:48:25
 *@version  版本号
 */
package com.nis.domain.configuration;

/**
 * @ClassName: IpPortConfig.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年2月5日 下午3:48:25
 * @version V1.0
 */
public class IpPortCfg extends BaseIpCfg {
	private static final String tableName="ip_port_cfg";
	private String indexTable="ip_port_cfg";
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 4664942095843594575L;
	//仅用作导入时copy属性使用
	private String antiddosProtocol;
	//仅用作导入时copy属性使用
	private Long bpsThreadshold;
	//仅用作导入时copy属性使用
	private Long ppsThreadshold;
	//仅用作导入时copy属性使用
	private Integer policyGroup;
	
	public Integer getPolicyGroup() {
		return policyGroup;
	}
	public void setPolicyGroup(Integer policyGroup) {
		this.policyGroup = policyGroup;
	}
	public String getAntiddosProtocol() {
		return antiddosProtocol;
	}
	public void setAntiddosProtocol(String antiddosProtocol) {
		this.antiddosProtocol = antiddosProtocol;
	}
	public Long getBpsThreadshold() {
		return bpsThreadshold;
	}
	public void setBpsThreadshold(Long bpsThreadshold) {
		this.bpsThreadshold = bpsThreadshold;
	}
	public Long getPpsThreadshold() {
		return ppsThreadshold;
	}
	public void setPpsThreadshold(Long ppsThreadshold) {
		this.ppsThreadshold = ppsThreadshold;
	}
	/* (non-Javadoc)
	 * @see com.nis.domain.configuration.BaseIpCfg#initDefaultValue()
	 */
	@Override
	public void initDefaultValue() {
		// TODO Auto-generated method stub
		super.initDefaultValue();
//		this.protocolId = 0;
	}
	public void initDefaultValueImpl(){
		initDefaultValue();
	}
	/**
	 * 此表固定写0
	 */
	/*@Override
	public void setProtocolId(Integer protocolId) {
		// TODO Auto-generated method stub
		super.setProtocolId(0);
	}
	*//**
	 * tablename
	 * @return  tablename
	*/
	
	public static String getTablename() {
		return tableName;
	}
	public String getIndexTable() {
		return indexTable;
	}
	public void setIndexTable(String indexTable) {
		this.indexTable = indexTable;
	}
	
	
}
