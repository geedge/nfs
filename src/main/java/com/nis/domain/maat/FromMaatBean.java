package com.nis.domain.maat;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FromMaatBean implements Serializable {

	private static final long serialVersionUID = 1277140754421705676L;
	@Expose
	@SerializedName("data")
	private List<DnsIpRecvData> data;

	public List<DnsIpRecvData> getData() {
		return data;
	}

	public void setData(List<DnsIpRecvData> data) {
		this.data = data;
	}

}
