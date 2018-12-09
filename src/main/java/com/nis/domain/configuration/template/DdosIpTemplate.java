package com.nis.domain.configuration.template;

import com.nis.util.excel.ExcelField;

public class DdosIpTemplate extends IpAllTemplate {
	private String antiddosProtocol;
	private Long bpsThreadshold;
	private Long ppsThreadshold;
	@ExcelField(title="antiddos_protocol",align=2,sort=2)
	public String getAntiddosProtocol() {
		return antiddosProtocol;
	}
	public void setAntiddosProtocol(String antiddosProtocol) {
		this.antiddosProtocol = antiddosProtocol;
	}
	@ExcelField(title="bps_threadshold",align=2,sort=3)
	public Long getBpsThreadshold() {
		return bpsThreadshold;
	}
	public void setBpsThreadshold(Long bpsThreadshold) {
		this.bpsThreadshold = bpsThreadshold;
	}
	@ExcelField(title="pps_threadshold",align=2,sort=4)
	public Long getPpsThreadshold() {
		return ppsThreadshold;
	}
	public void setPpsThreadshold(Long ppsThreadshold) {
		this.ppsThreadshold = ppsThreadshold;
	}
	
}
