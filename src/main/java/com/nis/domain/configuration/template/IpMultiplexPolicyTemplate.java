package com.nis.domain.configuration.template;

import com.nis.util.excel.ExcelField;
/**
 * wx 将部分字段的标题改变，或者不需要的字段隐藏
 * 隐藏方法，对于不需要的字段或者方法，Override该字段方法，但是@ExcelField注解不需要加上了
 * @author dell
 *
 */
@Deprecated
public class IpMultiplexPolicyTemplate extends IpCfgTemplate {
	@Override
	@ExcelField(title="IP",align=2,sort=3)
	public String getSrcIpAddress() {
		return super.getSrcIpAddress();
	}
	@ExcelField(title="port",align=2,sort=6)
	@Override
	public String getSrcPort() {
		// TODO Auto-generated method stub
		return super.getSrcPort();
	}
	
	@Override
	@ExcelField(title="group_name",align=2,sort=75)
	public Integer getDnsStrategyId() {
		// TODO Auto-generated method stub
		return super.getDnsStrategyId();
	}
	
	@Override
	public String getRatelimit() {
		// TODO Auto-generated method stub
		return super.getRatelimit();
	}
	@Override
	public String getDestPort() {
		// TODO Auto-generated method stub
		return super.getDestPort();
	}
	@Override
	public String getDestIpAddress() {
		// TODO Auto-generated method stub
		return super.getDestIpAddress();
	}

}
