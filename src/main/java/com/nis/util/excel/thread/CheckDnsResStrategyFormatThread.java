package com.nis.util.excel.thread;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jets3t.service.ServiceException;
import org.springframework.beans.BeanUtils;

import com.beust.jcommander.internal.Lists;
import com.nis.domain.FunctionRegionDict;
import com.nis.domain.FunctionServiceDict;
import com.nis.domain.basics.PolicyGroupInfo;
import com.nis.domain.configuration.DnsResStrategy;
import com.nis.util.Constants;
import com.nis.util.DictUtils;

public class CheckDnsResStrategyFormatThread implements Callable<String>{
	private Logger logger=Logger.getLogger(CheckDnsResStrategyFormatThread.class);
	private BlockingQueue<? extends Object> srcQueue;
	private BlockingQueue<DnsResStrategy> destQueue;
	private Properties prop;
	private FunctionServiceDict serviceDict;
	private FunctionRegionDict regionDict;
//	private PolicyGroupInfoDao policyGroupInfoDao;
//	private DnsResStrategyDao dnsResStrategyDao;
	public CheckDnsResStrategyFormatThread(FunctionServiceDict serviceDict,FunctionRegionDict regionDict,Properties prop,BlockingQueue<? extends Object> srcQueue,BlockingQueue<DnsResStrategy> destQueue) {
		this.serviceDict=serviceDict;
		this.regionDict=regionDict;
		this.srcQueue=srcQueue;
		this.destQueue=destQueue;
		this.prop=prop;
//		this.dnsResStrategyDao=SpringContextHolder.getBean(DnsResStrategyDao.class);
//		this.policyGroupInfoDao=SpringContextHolder.getBean(PolicyGroupInfoDao.class);
	}
	@Override
	public String call() throws Exception {
		List<Object> dataList=Lists.newArrayList(Constants.MAAT_JSON_SEND_SIZE);
		String msg=null;
		while(!srcQueue.isEmpty()) {
			int size=srcQueue.drainTo(dataList,Constants.MAAT_JSON_SEND_SIZE);
			if(regionDict.getRegionType().intValue()==6) {
				try {
					List<DnsResStrategy> cfgs=this.checkDnsResStrategyCfg(dataList);
						destQueue.addAll(cfgs);
				}catch (Exception e) {
					// TODO: handle exception
					msg=e.getMessage();
					break;
				}
			}
			dataList.clear();
		}
		return msg;
	}
	public List<DnsResStrategy> checkDnsResStrategyCfg(List<?> list)
			throws ServiceException {
		List<DnsResStrategy> dnsResStrategies=Lists.newArrayList();
		StringBuffer errTip = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			StringBuffer errInfo = new StringBuffer();
			DnsResStrategy dnsResStrategy=new DnsResStrategy();
			BeanUtils.copyProperties(list.get(i), dnsResStrategy);
			String groupName=dnsResStrategy.getCfgDesc();
			if(StringUtils.isBlank(groupName)) {
				errInfo.append(String.format(prop.getProperty("can_not_null"),
						prop.getProperty("policy_name")) + ";");
			}
			Integer resGroup1Id=dnsResStrategy.getResGroup1Id();
			if(resGroup1Id==null) {
				errInfo.append(String.format(prop.getProperty("can_not_null"),
						prop.getProperty("group")) + ";");
			}else {
				PolicyGroupInfo info=DictUtils.getDnsGroups(resGroup1Id);
//				PolicyGroupInfo info=policyGroupInfoDao.getById(resGroup1Id);
				if(info==null) {
					errInfo.append(String.format(prop.getProperty("is_incorrect"),
							prop.getProperty("group")) + ";");
				}
			}
			Integer resGroup1Num=dnsResStrategy.getResGroup1Num();
			if(resGroup1Num==null) {
				errInfo.append(String.format(prop.getProperty("can_not_null"),
						prop.getProperty("res_group_num")) + ";");
			}
			String ttl=dnsResStrategy.getTtl();
			if(StringUtils.isBlank(ttl)) {
				errInfo.append(String.format(prop.getProperty("can_not_null"),
						prop.getProperty("min_ttl")) + ";");
			}else {
				Pattern p=Constants.RANGE_PATTERN;
				Matcher m=p.matcher(ttl);
				if(!m.matches()) {
					errInfo.append(String.format(prop.getProperty("is_incorrect"),
							prop.getProperty("min_ttl")) + ";");
				}else {
					String minTtl=ttl.split("-")[0];
					String maxTtl=ttl.split("-")[1];
					Integer min=null,max=null;
					try {
						min=Integer.parseInt(minTtl);
					} catch (Exception e) {
						// TODO: handle exception
						errInfo.append(String.format(prop.getProperty("is_incorrect"),
								prop.getProperty("min_ttl")) + ";");
					}
					try {
						max=Integer.parseInt(maxTtl);
					} catch (Exception e) {
						// TODO: handle exception
						errInfo.append(String.format(prop.getProperty("is_incorrect"),
								prop.getProperty("min_ttl")) + ";");
					}
					if(min.intValue()>max.intValue())
					{errInfo.append(String.format(prop.getProperty("is_incorrect"),
							prop.getProperty("min_ttl")) + ";");
					}else {
						dnsResStrategy.setMinTtl(min);
						dnsResStrategy.setMaxTtl(max);
					}
				}
			}
			if (errInfo.toString().length() > 0) {//
				errTip.append(String.format(prop.getProperty("line"), dnsResStrategy.getIndex()) + ",");
				errTip.append(errInfo);
				errTip.append("<br>");
			}
			dnsResStrategies.add(dnsResStrategy);
		}
		if (errTip.toString().length() > 0) {
			throw new ServiceException(errTip.toString());
		}
		return dnsResStrategies;
	}
	
}
