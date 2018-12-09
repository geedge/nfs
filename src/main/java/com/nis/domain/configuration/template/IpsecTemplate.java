package com.nis.domain.configuration.template;

/**
 * wx 将部分字段的标题改变，或者不需要的字段隐藏
 * 隐藏方法，对于不需要的字段或者方法，Override该字段方法，但是@ExcelField注解不需要加上了
 * @author dell
 *
 */
@Deprecated
public class IpsecTemplate extends IpCfgTemplate {
	

	@Override
	public Integer getIrType() {
		// TODO Auto-generated method stub
		return super.getIrType();
	}

	@Override
	public Integer getDnsStrategyId() {
		// TODO Auto-generated method stub
		return super.getDnsStrategyId();
	}

	@Override
	public String getRatelimit() {
		// TODO Auto-generated method stub
		return super.getRatelimit();
	}
	
}
