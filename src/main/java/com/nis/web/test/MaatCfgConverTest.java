package com.nis.web.test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.ComplexkeywordCfg;
import com.nis.domain.maat.MaatCfg;
import com.nis.domain.maat.MaatCfg.GroupCfg;
import com.nis.domain.maat.MaatCfg.IpCfg;
import com.nis.domain.maat.MaatCfg.StringCfg;
import com.nis.domain.maat.ToMaatBean;
import com.nis.util.IpUtil;

public class MaatCfgConverTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<BaseIpCfg> ipList = new ArrayList();
		List<IpCfg> ipRegion = new ArrayList();
		List<StringCfg> strRegion = new ArrayList();
		List<MaatCfg> maatList = new ArrayList();
		List<GroupCfg> groupList = new ArrayList();
		ToMaatBean maat = new ToMaatBean();
		BaseStringCfg str = new BaseStringCfg();
		BaseIpCfg ip = new BaseIpCfg();
		ip.setIpPattern(1);
		ip.setIpType(4);
		ip.setSrcIpAddress("192.168.10.0/24");
		ip.setPortPattern(2);
		ip.setSrcPort("8080-8089");
		ip.setCompileId(123);
		ip.setProtocol(6);
		ip.setDirection(2);
		ipList.add(ip);
		ip = new BaseIpCfg();
		ip.setIpPattern(2);
		ip.setIpType(4);
		ip.setSrcIpAddress("10.0.6.0-10.0.6.100");
		ip.setPortPattern(1);
		ip.setSrcPort("7080/65534");
		ip.setCompileId(124);
		ip.setDirection(1);
		ip.setProtocol(17);
		ipList.add(ip);
		
		GroupCfg group = new GroupCfg();
		int groupId = 0;
		if(ipList.size()>0){
			groupId++;
			group.setGroupId(groupId);
			group.setCompileId(1);
			group.setAuditTime(new Date());
			group.setIsValid(1);
			groupList.add(group);
		}
		
		int regionId = 1;
		
		for(BaseIpCfg c:ipList){
			IpCfg ipCfg = new IpCfg();
			ipCfg.setRegionId(regionId);
			if(c.getIpPattern()==1){
				ipCfg.setSrcIp(c.getSrcIpAddress().split("/")[0]);
				ipCfg.setSrcIpMask(IpUtil.convertMask(Integer.parseInt(c.getSrcIpAddress().split("/")[1])));
			}else if(c.getIpPattern()==2){
				ipCfg.setSrcIp(c.getSrcIpAddress().split("-")[0]);
				ipCfg.setSrcIpMask(IpUtil.getMask(c.getSrcIpAddress().split("-")[0], c.getSrcIpAddress().split("-")[1]));
			}
			if(c.getPortPattern()==1){
				ipCfg.setSrcPort(c.getSrcPort().split("/")[0]);
				ipCfg.setSrcPortMask(c.getSrcPort().split("/")[1]);
			}else if(c.getPortPattern()==2){
				ipCfg.setSrcPort(c.getSrcPort().split("-")[0]);
				Integer portMask = Integer.parseInt(c.getSrcPort().split("-")[1])-Integer.parseInt(c.getSrcPort().split("-")[0]);
				ipCfg.setSrcPortMask(portMask.toString());
			}
			ipCfg.setDirection(c.getDirection());
			ipCfg.setProtocol(c.getProtocol());
			ipCfg.setGroupId(groupId);
			ipRegion.add(ipCfg);
			regionId++;
		}
		List<BaseStringCfg> list1 = new ArrayList();
		str.setAction(1);
		str.setCfgKeywords("测试1");
		list1.add(str);
		str = new BaseStringCfg();
		str.setAction(2);
		str.setCfgKeywords("测试2");
		list1.add(str);
		if(list1.size()>0){
			groupId++;
			group.setGroupId(groupId);
			group.setCompileId(1);
			group.setAuditTime(new Date());
			group.setIsValid(1);
			groupList.add(group);
		}
		for(BaseStringCfg c:list1){
			StringCfg strCfg = new StringCfg();
			try {
				BeanUtils.copyProperties(strCfg, c);
				strCfg.setRegionId(regionId);
				strCfg.setGroupId(groupId);
				strRegion.add(strCfg);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			regionId++;
		}
		ComplexkeywordCfg complex = new ComplexkeywordCfg();
		List<ComplexkeywordCfg> list2 = new ArrayList();
		complex.setAction(1);
		complex.setDistrict("UA");
		complex.setCfgKeywords("测试1");
		list2.add(complex);
		str = new BaseStringCfg();
		complex.setAction(1);
		complex.setDistrict("cookie");
		complex.setCfgKeywords("测试2");
		list2.add(complex);
		if(list2.size()>0){
			groupId++;
			group.setGroupId(groupId);
			group.setCompileId(1);
			group.setAuditTime(new Date());
			group.setIsValid(1);
			groupList.add(group);
		}
		for(ComplexkeywordCfg c:list2){
			StringCfg strCfg = new StringCfg();
			try {
				BeanUtils.copyProperties(strCfg, c);
				strCfg.setRegionId(regionId);
				strCfg.setGroupId(groupId);
				strRegion.add(strCfg);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			regionId++;
		}
		MaatCfg c = new MaatCfg();
		c.setCompileId(1);
		c.setGroupNum(groupList.size());
		c.setAction(1);
		c.setAttribute("11,12,13");
		c.setClassify("22,23,24");
		c.setLable("17,18,19");
		c.setFunctionId(30);
		c.setIpRegionList(ipRegion);
		c.setStrRegionList(strRegion);
		
		maatList.add(c);
		maat.setAuditTime(new Date());
		maat.setVersion("1.0");
		maat.setCreatorName("test");
		maat.setConfigCompileList(maatList);
		Gson gson=new GsonBuilder().disableHtmlEscaping()
				.create();
		System.out.println(gson.toJson(maat));
	}

}
