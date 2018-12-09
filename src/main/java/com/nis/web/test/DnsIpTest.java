package com.nis.web.test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nis.domain.configuration.DnsIpCfg;
import com.nis.domain.maat.DnsIpRecvData;
import com.nis.domain.maat.FromMaatBean;
import com.nis.domain.maat.MaatDnsIpCfg;
import com.nis.domain.maat.ToMaatDnsIpBean;

public class DnsIpTest {
	
	/**
	 * 模拟审核返回结果
	 * @param recv
	 */
	public static String examine(String recv) {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		Type type = new TypeToken<List<ToMaatDnsIpBean>>(){}.getType();
		List<ToMaatDnsIpBean> fromJson = gson.fromJson(recv, type);
		List<MaatDnsIpCfg> serviceCfg = fromJson.get(0).getServiceCfg();
		
		FromMaatBean fBean = new FromMaatBean();
		List<DnsIpRecvData> data = new ArrayList<DnsIpRecvData>();
		
		for (MaatDnsIpCfg mCfg : serviceCfg) {
			List<DnsIpCfg> ipCfg = mCfg.getIpCfg();
			for (DnsIpCfg dnsIpCfg : ipCfg) {
				DnsIpRecvData recvData = new DnsIpRecvData();
				recvData.setCfgId(dnsIpCfg.getCfgId());
				recvData.setFakeId(Integer.valueOf(dnsIpCfg.getCfgId().toString()));
				data.add(recvData);
			}
		}
		
		fBean.setData(data);
		String json = gson.toJson(fBean);
		return json;
	}
	
}
