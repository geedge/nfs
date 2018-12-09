package com.nis.domain.maat;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.nis.domain.configuration.DnsIpCfg;

public class MaatDnsIpCfg implements Serializable {

	private static final long serialVersionUID = 2407058503197501569L;
	
	@Expose
	private List<DnsIpCfg> ipCfg;

	public List<DnsIpCfg> getIpCfg() {
		return ipCfg;
	}

	public void setIpCfg(List<DnsIpCfg> ipCfg) {
		this.ipCfg = ipCfg;
	}
	
}
