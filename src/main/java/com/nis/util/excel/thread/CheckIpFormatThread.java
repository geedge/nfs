package com.nis.util.excel.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import com.nis.domain.SysDataDictionaryItem;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.DnsResStrategy;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.configuration.template.IpRateLimitTemplate;
import com.nis.domain.specific.ConfigGroupInfo;
import com.nis.util.AsnCacheUtils;
import com.nis.util.Constants;
import com.nis.util.DictUtils;

public class CheckIpFormatThread implements Callable<String>{
	private Logger logger=Logger.getLogger(CheckIpFormatThread.class);
	private BlockingQueue<? extends Object> srcQueue;
	private BlockingQueue<BaseIpCfg> destQueue;
	private Properties prop;
//	private DnsResStrategyService dnsResStrategyService;
	private FunctionServiceDict serviceDict;
	private FunctionRegionDict regionDict;
	private List<Map<Long,Integer>> asnNoMaps;
//	private DnsResStrategyDao dnsResStrategyDao;
	public CheckIpFormatThread(FunctionServiceDict serviceDict,FunctionRegionDict regionDict,Properties prop,BlockingQueue<? extends Object> srcQueue,BlockingQueue<BaseIpCfg> destQueue) {
		this.serviceDict=serviceDict;
		this.regionDict=regionDict;
		this.srcQueue=srcQueue;
		this.destQueue=destQueue;
		this.prop=prop;
//		this.dnsResStrategyDao=SpringContextHolder.getBean(DnsResStrategyDao.class);
	}
	@Override
	public String call() throws Exception {
		List<Object> dataList=Lists.newArrayList(Constants.MAAT_JSON_SEND_SIZE);
		String msg=null;
		while(!srcQueue.isEmpty()) {
			int size=srcQueue.drainTo(dataList,Constants.MAAT_JSON_SEND_SIZE);
			if(regionDict.getRegionType().intValue()==1) {
				try {
					List<BaseIpCfg> ipcfgs=this.checkIpCfg(asnNoMaps, dataList);
					boolean success=destQueue.addAll(ipcfgs);
				}catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
					msg=e.getMessage();
					break;
				}
			}
			dataList.clear();
		}
		return msg;
	}
	public List<BaseIpCfg> checkIpCfg(List<Map<Long,Integer>> asnNos, List<Object> list)
			throws ServiceException {
		logger.warn("start to validate ip...");
		long start=System.currentTimeMillis();
		List<BaseIpCfg> ipList = new ArrayList<BaseIpCfg>();
		// ip必填一个，configIpPortShow不能为空，否则字典配错；
		String configIpPortShow = regionDict.getConfigIpPortShow();
		if (StringUtils.isBlank(configIpPortShow)) {
			throw new RuntimeException("Found IP region,but configIpPortShow is Empty");
		}
		String ipType = regionDict.getConfigIpType();
		if (StringUtils.isBlank(ipType)) {
			throw new RuntimeException("Found IP region,but ipType is Empty");
		}
		boolean canIPv4 = ipType.indexOf("4") > -1;
		boolean canIPv6 = ipType.indexOf("6") > -1;
		String ipPattern = regionDict.getConfigIpPattern();
		if (StringUtils.isBlank(ipPattern)) {
			throw new RuntimeException("Found IP region,but ipPattern is Empty");
		}
		boolean canIPFormat = ipType.indexOf("3") > -1;
		boolean canIPSubnetFormat = ipType.indexOf("1") > -1;
		boolean canIPRangeFormat = ipType.indexOf("2") > -1;
		String portPattern = regionDict.getConfigPortPattern();
		String protocol = regionDict.getConfigProtocol();
		String direction = regionDict.getConfigDirection();
		StringBuffer errTip = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			StringBuffer errInfo = new StringBuffer();
			IpPortCfg baseIpCfg = new IpPortCfg();
			BeanUtils.copyProperties(list.get(i), baseIpCfg);
			// 特殊字段验证
			// packet ip ratelimit
			if (serviceDict!=null&&serviceDict.getFunctionId().intValue() == 5 && serviceDict.getAction().intValue() == 64) {
				String userRegion1 = IpRateLimitTemplate.userRegion1;
				baseIpCfg.setUserRegion1(userRegion1);
				if("0".equals(userRegion1)) {
					String userRegion2 = baseIpCfg.getUserRegion2();
					try {
						// Double ratelimt= Double.parseDouble(userRegion2);
						List<SysDataDictionaryItem> ratelimtList = DictUtils.getDictList("RATE_LIMIT");
						boolean has = false;
						for (SysDataDictionaryItem ratelimit : ratelimtList) {
							if (ratelimit.getItemCode().equals(userRegion2)) {
								has = true;
								break;
							}
						}
						if (!has) {
							errInfo.append(prop.getProperty("ratelimit") + "."
									+ String.format(prop.getProperty("must_between"), 0.1, 0.9) + ";");
						}
					} catch (Exception e) {
						// TODO: handle exception
						errInfo.append(String.format(prop.getProperty("not_number"), prop.getProperty("ratelimit")) + ";");
					}
				}else if("1".equals(userRegion1)) {
					String userRegion3 = baseIpCfg.getUserRegion3();
					if (StringUtils.isNotBlank(userRegion3)) {
						try {
							 Integer ratelimt=Integer.parseInt(userRegion3);
						} catch (Exception e) {
							// TODO: handle exception
							errInfo.append(String.format(prop.getProperty("not_number"), prop.getProperty("bandwith")) + ";");
						}
					}else {
						errInfo.append(
								String.format(prop.getProperty("can_not_null"), prop.getProperty("bandwith")) + ";");
					}
					
				}
				
			}
			// payload ip
			if (regionDict.getFunctionId().equals(212)) {
				// replace_zone
				String userRegion1 = baseIpCfg.getUserRegion1();
				if (StringUtils.isNotBlank(userRegion1)) {
					Object val = DictUtils.getDictLabel("INTERCEPT_REPLACE_ZONE", userRegion1);
					if (StringUtils.isBlank(val.toString())) {
						errInfo.append(String.format(prop.getProperty("is_incorrect"), prop.getProperty("replace_zone"))
								+ ";");
					}
				} else {
					errInfo.append(
							String.format(prop.getProperty("can_not_null"), prop.getProperty("replace_zone")) + ";");
				}

				// replaced_content
				String userRegion2 = baseIpCfg.getUserRegion2();
				if (StringUtils.isBlank(userRegion2)) {
					errInfo.append(String.format(prop.getProperty("can_not_null"), prop.getProperty("replaced_content"))
							+ ";");
				}
				// replace_content
				String userRegion3 = baseIpCfg.getUserRegion3();
				if (StringUtils.isBlank(userRegion3)) {
					errInfo.append(
							String.format(prop.getProperty("can_not_null"), prop.getProperty("replace_content")) + ";");
				}
			}
			// dns ip
			if (regionDict.getFunctionId().equals(7)) {
				Integer dnsStrategyId = baseIpCfg.getDnsStrategyId();
				if (dnsStrategyId != null&&dnsStrategyId>0) {
					List<DnsResStrategy> dnsStrategys = DictUtils.getDnsResStrategyList(Long.valueOf(dnsStrategyId));
					if (dnsStrategys == null || dnsStrategys.size() == 0) {
						errInfo.append(
								String.format(prop.getProperty("is_incorrect"), prop.getProperty("dns_res_strategy"))
										+ ";");
					}
				}
			}
			// dns fake ip
			if (regionDict.getFunctionId().equals(401)) {
				Integer dnsStrategyId = baseIpCfg.getDnsStrategyId();
				if (dnsStrategyId != null) {
					if(dnsStrategyId>0) {
						List<DnsResStrategy> dnsStrategys = DictUtils.getDnsResStrategyList(Long.valueOf(dnsStrategyId));
						if (dnsStrategys == null || dnsStrategys.size() == 0) {
							errInfo.append(
									String.format(prop.getProperty("is_incorrect"), prop.getProperty("dns_res_strategy"))
									+ ";");
						}
					}
				}else {
					baseIpCfg.setDnsStrategyId(0);
				}
			}
			//p2p ip
			if (regionDict.getFunctionId().equals(510) && "p2p_ip".equals(regionDict.getConfigServiceType())) { // P2p
																												// IP
				String userRegion2 = baseIpCfg.getUserRegion2();
				List<SysDataDictionaryItem> ipTypes = DictUtils.getDictList("P2P_IP_TYPE");
				boolean has = false;
				for (SysDataDictionaryItem type : ipTypes) {
					if (type.getItemCode().equals(userRegion2)) {
						has = true;
						break;
					}
				}
				if (!has) {
					errInfo.append(
							String.format(prop.getProperty("is_incorrect"), prop.getProperty("p2p_ip_config_type"))
									+ ";");
				}
			}
			if (regionDict.getFunctionId().equals(600)) {//asn
				//asn id
				String userRegion1 = baseIpCfg.getUserRegion1();
				if(StringUtils.isBlank(userRegion1)) {
					errInfo.append(
							String.format(prop.getProperty("can_not_null"), prop.getProperty("asn_no"))
									+ ";");
				}else {
					try {
						Long asnNo=Long.parseLong(userRegion1);
						if(asnNo.longValue()>4294967295l||asnNo.longValue()<1) {
							errInfo.append(prop.getProperty("asn_no")+" "+
									String.format(prop.getProperty("must_between"),"1","4294967295" )
											+ ";");
						}else {
//							ConfigGroupInfo configGroupInfo=asnIpCfgService.getConfigGroupInfoByAsnNo(asnNo);
							ConfigGroupInfo configGroupInfo=AsnCacheUtils.get(asnNo);
							if(asnNos!=null) {
								//缓存中没有
								if(configGroupInfo==null) {
									asnNos.get(0).put(asnNo,-1);
								}else {
									asnNos.get(1).put(asnNo,configGroupInfo.getGroupId());
								}
							}
						}
						
					}catch (Exception e) {
						e.printStackTrace();
						// TODO: handle exception
						errInfo.append(
								String.format(prop.getProperty("not_number"), prop.getProperty("asn_no"))
										+ ";");
					}
				}
			}
			if (regionDict.getFunctionId().equals(301)) {
				String antiddosProtocol=baseIpCfg.getAntiddosProtocol();
				Long bpsThreadshold= baseIpCfg.getBpsThreadshold();
				Long ppsThreadshold= baseIpCfg.getPpsThreadshold();
				if(StringUtils.isNotBlank(antiddosProtocol)) {
					List<SysDataDictionaryItem> antiddosProtocolDicts=DictUtils.getDictList("ANTIDDOS_PROTOCOL");
					boolean has=false;
					for(SysDataDictionaryItem ditc:antiddosProtocolDicts) {
						if(ditc.getItemCode().equals(antiddosProtocol)) {
							has=true;
							break;
						}
					}
					if(!has) {
						errInfo.append(
								String.format(prop.getProperty("is_incorrect"), prop.getProperty("antiddos_protocol"))
										+ ";");
					}
					if(bpsThreadshold==null) {
						errInfo.append(
								String.format(prop.getProperty("can_not_null"), prop.getProperty("bps_threadshold"))
										+ ";");
					}
					if(ppsThreadshold==null) {
						errInfo.append(
								String.format(prop.getProperty("can_not_null"), prop.getProperty("pps_threadshold"))
										+ ";");
					}
				}else {
					baseIpCfg.setAntiddosProtocol("");
				}
			}
			if (regionDict.getRegionType().equals(1)) {
				boolean srcIpEmpty = false;
				boolean destIpEmpty = false;
				boolean srcPortEmpty = false;
				boolean destPortEmpty = false;
				Pattern ipv4IpRangeRegexpNew = Constants.IPV4_IP_RANGE_REGEXP_NEW_PATTERN;
				if (StringUtils.isBlank(baseIpCfg.getSrcIpAddress())) {
					srcIpEmpty = true;
				}else {
					//处理0.0.0.1-2这种格式，转换为0.0.0.1-0.0.0.2
					Matcher m=ipv4IpRangeRegexpNew.matcher(baseIpCfg.getSrcIpAddress());
					if(m.matches()) {
						String prefix=baseIpCfg.getSrcIpAddress().split("-")[0];
						String subfix=baseIpCfg.getSrcIpAddress().split("-")[1];
						baseIpCfg.setSrcIpAddress(prefix+"-"+prefix.substring(0, prefix.lastIndexOf(".")+1)+subfix);
					}
				}
				if (StringUtils.isBlank(baseIpCfg.getDestIpAddress())) {
					destIpEmpty = true;
				}else {
					//处理0.0.0.1-2这种格式，转换为0.0.0.1-0.0.0.2
					Matcher m=ipv4IpRangeRegexpNew.matcher(baseIpCfg.getDestIpAddress());
					if(m.matches()) {
						String prefix=baseIpCfg.getDestIpAddress().split("-")[0];
						String subfix=baseIpCfg.getDestIpAddress().split("-")[1];
						baseIpCfg.setDestIpAddress(prefix+"-"+prefix.substring(0, prefix.lastIndexOf(".")+1)+subfix);
					}
				}
				if (StringUtils.isBlank(baseIpCfg.getSrcPort())) {
					srcPortEmpty = true;
				}
				if (StringUtils.isBlank(baseIpCfg.getDestPort())) {
					destPortEmpty = true;
				}
				if (srcIpEmpty && destIpEmpty) {
					if (configIpPortShow.indexOf("1") > -1 && configIpPortShow.indexOf("3") > -1) {
						errInfo.append(String.format(prop.getProperty("can_not_null"),
								prop.getProperty("client_ip", "Client IP")) + ";");
					} else if (configIpPortShow.indexOf("1") > -1) {
						errInfo.append(String.format(prop.getProperty("can_not_null"),
								prop.getProperty("client_ip", "Client IP")) + ";");
					} else if (configIpPortShow.indexOf("3") > -1) {
						errInfo.append(String.format(prop.getProperty("can_not_null"),
								prop.getProperty("server_ip", "Server IP")) + ";");
					}
				} else if (destIpEmpty) {// 目的IP为空
					if (configIpPortShow.indexOf("3") > -1 && configIpPortShow.indexOf("1") == -1) {// 目的IP必填
						errInfo.append(String.format(prop.getProperty("can_not_null"),
								prop.getProperty("server_ip", "Server IP")) + ";");
					} else if (configIpPortShow.indexOf("1") > -1) {
						String matchType = this.validIPAddress("client_ip", errInfo, baseIpCfg.getSrcIpAddress(),
								ipType, ipPattern);
						if (matchType.startsWith("IPV6")) {
							if (matchType.endsWith("RANGE")) {
								// baseIpCfg.setSrcIpAddress(baseIpCfg.getSrcIpAddress());
								baseIpCfg.setDestIpAddress(Constants.IPV6_DEFAULT_IP_RANGE_VALUE);
								baseIpCfg.setIpPattern(2);
								if (baseIpCfg.getSrcIpAddress()
										.startsWith(Constants.IPV6_DEFAULT_IP_RANGE_VALUE.split("-")[0])) {
									errInfo.append(prop.getProperty("client_ip")+" "
											+ String.format(prop.getProperty("can_not_be"),
													Constants.IPV6_DEFAULT_IP_RANGE_VALUE.split("-")[0])
											+ ";");
								}
							} else if (matchType.endsWith("SUBNET")) {
								// baseIpCfg.setSrcIpAddress(baseIpCfg.getSrcIpAddress());
								baseIpCfg.setDestIpAddress(Constants.IPV6_DEFAULT_IP_SUBNET_VALUE);
								baseIpCfg.setIpPattern(1);
								if (baseIpCfg.getSrcIpAddress()
										.startsWith(Constants.IPV6_DEFAULT_IP_SUBNET_VALUE.split("/")[0])) {
									errInfo.append(
											prop.getProperty("client_ip")+" "
													+ String.format(prop.getProperty("can_not_be"),
															Constants.IPV6_DEFAULT_IP_SUBNET_VALUE.split("/")[0])
													+ ";");
								}
							} else {
								// baseIpCfg.setSrcIpAddress(baseIpCfg.getSrcIpAddress());
								baseIpCfg.setDestIpAddress(Constants.IPV6_DEFAULT_IP_VALUE);
								baseIpCfg.setIpPattern(3);
								if (baseIpCfg.getSrcIpAddress().equals(Constants.IPV6_DEFAULT_IP_VALUE)) {
									errInfo.append(prop.getProperty("client_ip")+" " + String.format(
											prop.getProperty("can_not_be"), Constants.IPV6_DEFAULT_IP_VALUE) + ";");
								}
							}
							baseIpCfg.setIpType(6);
						} else {
							if (matchType.endsWith("RANGE")) {
								this.validIPv4Range("client_ip", errInfo, baseIpCfg.getSrcIpAddress());
								// baseIpCfg.setSrcIpAddress("0.0.0.0-"+baseIpCfg.getSrcIpAddress().split("-")[0].substring(0,baseIpCfg.getSrcIpAddress().indexOf("-")+1)+baseIpCfg.getSrcIpAddress().split("-")[1]);
								baseIpCfg.setDestIpAddress(Constants.IPV4_DEFAULT_IP_RANGE_VALUE);
								baseIpCfg.setIpPattern(2);
								if (baseIpCfg.getSrcIpAddress()
										.startsWith(Constants.IPV4_DEFAULT_IP_RANGE_VALUE.split("-")[0])) {
									errInfo.append(prop.getProperty("client_ip")+" "
											+ String.format(prop.getProperty("can_not_be"),
													Constants.IPV4_DEFAULT_IP_RANGE_VALUE.split("-")[0])
											+ ";");
								}
							} else if (matchType.endsWith("SUBNET")) {
								// baseIpCfg.setSrcIpAddress(baseIpCfg.getSrcIpAddress());
								baseIpCfg.setDestIpAddress(Constants.IPV4_DEFAULT_IP_SUBNET_VALUE);
								baseIpCfg.setIpPattern(1);
								if (baseIpCfg.getSrcIpAddress()
										.startsWith(Constants.IPV4_DEFAULT_IP_SUBNET_VALUE.split("/")[0])) {
									errInfo.append(
											prop.getProperty("client_ip")+" "
													+ String.format(prop.getProperty("can_not_be"),
															Constants.IPV4_DEFAULT_IP_SUBNET_VALUE.split("/")[0])
													+ ";");
								}
							} else {
								baseIpCfg.setSrcIpAddress(baseIpCfg.getSrcIpAddress());
								baseIpCfg.setDestIpAddress(Constants.IPV4_DEFAULT_IP_VALUE);
								baseIpCfg.setIpPattern(3);
								if (baseIpCfg.getSrcIpAddress().equals(Constants.IPV4_DEFAULT_IP_VALUE)) {
									errInfo.append(prop.getProperty("client_ip")+" " + String.format(
											prop.getProperty("can_not_be"), Constants.IPV4_DEFAULT_IP_VALUE) + ";");
								}
							}
							baseIpCfg.setIpType(4);
						}
					}

				} else if (srcIpEmpty) {// 源IP为空
					if (configIpPortShow.indexOf("1") > -1 && configIpPortShow.indexOf("3") == -1) {// 源IP必填
						errInfo.append(String.format(prop.getProperty("can_not_null"),
								prop.getProperty("client_ip", "Client IP")) + ";");
					} else if (configIpPortShow.indexOf("3") > -1) {
						String matchType = this.validIPAddress("server_ip", errInfo, baseIpCfg.getDestIpAddress(),
								ipType, ipPattern);
						if (matchType.startsWith("IPV6")) {
							if (matchType.endsWith("RANGE")) {
								// baseIpCfg.setDestIpAddress(baseIpCfg.getDestIpAddress());
								baseIpCfg.setSrcIpAddress(Constants.IPV6_DEFAULT_IP_RANGE_VALUE);
								baseIpCfg.setIpPattern(2);
								if (baseIpCfg.getDestIpAddress()
										.startsWith(Constants.IPV6_DEFAULT_IP_RANGE_VALUE.split("-")[0])) {
									errInfo.append(prop.getProperty("server_ip")+" "
											+ String.format(prop.getProperty("can_not_be"),
													Constants.IPV6_DEFAULT_IP_RANGE_VALUE.split("-")[0])
											+ ";");
								}
							} else if (matchType.endsWith("SUBNET")) {
								// baseIpCfg.setDestIpAddress(baseIpCfg.getDestIpAddress());
								baseIpCfg.setSrcIpAddress(Constants.IPV6_DEFAULT_IP_SUBNET_VALUE);
								baseIpCfg.setIpPattern(1);
								if (baseIpCfg.getDestIpAddress()
										.startsWith(Constants.IPV6_DEFAULT_IP_SUBNET_VALUE.split("/")[0])) {
									errInfo.append(
											prop.getProperty("server_ip")+" "
													+ String.format(prop.getProperty("can_not_be"),
															Constants.IPV6_DEFAULT_IP_SUBNET_VALUE.split("/")[0])
													+ ";");
								}
							} else {
								// baseIpCfg.setDestIpAddress(baseIpCfg.getDestIpAddress());
								baseIpCfg.setSrcIpAddress(Constants.IPV6_DEFAULT_IP_VALUE);
								baseIpCfg.setIpPattern(3);
								if (baseIpCfg.getDestIpAddress().equals(Constants.IPV6_DEFAULT_IP_VALUE)) {
									errInfo.append(prop.getProperty("server_ip")+" " + String.format(
											prop.getProperty("can_not_be"), Constants.IPV6_DEFAULT_IP_VALUE) + ";");
								}
							}
							baseIpCfg.setIpType(6);
						} else {
							if (matchType.endsWith("RANGE")) {
								this.validIPv4Range("server_ip", errInfo, baseIpCfg.getDestIpAddress());
								// baseIpCfg.setDestIpAddress("0.0.0.0-"+baseIpCfg.getDestIpAddress().split("-")[0]
								// .substring(0,baseIpCfg.getDestIpAddress().indexOf("-")+1)
								// +baseIpCfg.getDestIpAddress().split("-")[1]);
								baseIpCfg.setSrcIpAddress(Constants.IPV4_DEFAULT_IP_RANGE_VALUE);
								baseIpCfg.setIpPattern(2);
								if (baseIpCfg.getDestIpAddress()
										.startsWith(Constants.IPV4_DEFAULT_IP_RANGE_VALUE.split("-")[0])) {
									errInfo.append(prop.getProperty("server_ip")+" "
											+ String.format(prop.getProperty("can_not_be"),
													Constants.IPV4_DEFAULT_IP_RANGE_VALUE.split("-")[0])
											+ ";");
								}
							} else if (matchType.endsWith("SUBNET")) {
								// baseIpCfg.setDestIpAddress(baseIpCfg.getDestIpAddress());
								baseIpCfg.setSrcIpAddress(Constants.IPV4_DEFAULT_IP_SUBNET_VALUE);
								baseIpCfg.setIpPattern(1);
								if (baseIpCfg.getDestIpAddress()
										.startsWith(Constants.IPV4_DEFAULT_IP_SUBNET_VALUE.split("/")[0])) {
									errInfo.append(
											prop.getProperty("server_ip")+" "
													+ String.format(prop.getProperty("can_not_be"),
															Constants.IPV4_DEFAULT_IP_SUBNET_VALUE.split("/")[0])
													+ ";");
								}
							} else {
								// baseIpCfg.setDestIpAddress(baseIpCfg.getDestIpAddress());
								baseIpCfg.setSrcIpAddress(Constants.IPV4_DEFAULT_IP_VALUE);
								baseIpCfg.setIpPattern(3);
								if (baseIpCfg.getDestIpAddress().equals(Constants.IPV4_DEFAULT_IP_VALUE)) {
									errInfo.append(prop.getProperty("server_ip")+" " + String.format(
											prop.getProperty("can_not_be"), Constants.IPV4_DEFAULT_IP_VALUE) + ";");
								}
							}
							baseIpCfg.setIpType(4);
						}
					}
				} else {// 全不为空
					String matchType = this.validIPAddress("client_ip", errInfo, baseIpCfg.getSrcIpAddress(), ipType,
							ipPattern);
					String matchType1 = this.validIPAddress("server_ip", errInfo, baseIpCfg.getDestIpAddress(), ipType,
							ipPattern);
					if (StringUtils.isNotBlank(matchType) && StringUtils.isNotBlank(matchType1)
							&& !matchType.equals(matchType1)) {
						errInfo.append(prop.get("the_same_ip_type_pattern") + ";");
					}
					// baseIpCfg.setSrcIpAddress(baseIpCfg.getSrcIpAddress());
					// baseIpCfg.setDestIpAddress(baseIpCfg.getDestIpAddress());
					if (matchType.startsWith("IPV6")) {
						baseIpCfg.setIpType(6);
					} else {
						baseIpCfg.setIpType(4);
					}
					if (matchType.endsWith("RANGE")) {
						if (matchType.startsWith("IPV4")) {
							this.validIPv4Range("client_ip", errInfo, baseIpCfg.getSrcIpAddress());
							this.validIPv4Range("server_ip", errInfo, baseIpCfg.getDestIpAddress());
						}

						if (baseIpCfg.getSrcIpAddress().split("-")[0]
								.equals(baseIpCfg.getDestIpAddress().split("-")[0])) {
							errInfo.append(String.format(prop.getProperty("are_the_same"),
									baseIpCfg.getSrcIpAddress().split("-")[0],
									baseIpCfg.getDestIpAddress().split("-")[0]) + ";");
						}
						baseIpCfg.setIpPattern(2);
					} else if (matchType.endsWith("SUBNET")) {
						if (baseIpCfg.getSrcIpAddress().split("/")[0]
								.equals(baseIpCfg.getDestIpAddress().split("/")[0])) {
							errInfo.append(String.format(prop.getProperty("are_the_same"),
									baseIpCfg.getSrcIpAddress().split("/")[0],
									baseIpCfg.getDestIpAddress().split("/")[0]) + ";");
						}
						baseIpCfg.setIpPattern(1);
					} else {
						if (baseIpCfg.getSrcIpAddress().equals(baseIpCfg.getDestIpAddress())) {
							errInfo.append(String.format(prop.getProperty("are_the_same"),
									prop.getProperty("client_ip"), prop.getProperty("server_ip")) + ";");
						}
						baseIpCfg.setIpPattern(3);
					}
				}

				// TODO 判断源IP和目的IP的值
				// TODO 判断源IP和目的IP格式
				if (destPortEmpty) {
					if (srcPortEmpty) {
						baseIpCfg.setSrcPort("0");
						baseIpCfg.setDestPort("0");
						baseIpCfg.setPortPattern(1);
					} else {
						if (baseIpCfg.getSrcPort().indexOf("/") > -1) {
							baseIpCfg.setDestPort("0/0");
							baseIpCfg.setPortPattern(2);
						} else {
							baseIpCfg.setDestPort("0");
							baseIpCfg.setPortPattern(1);
						}
						baseIpCfg.setSrcPort(baseIpCfg.getSrcPort().trim());
					}
				} else {
					if (srcPortEmpty) {
						if (baseIpCfg.getDestPort().indexOf("/") > -1) {
							baseIpCfg.setSrcPort("0/0");
							baseIpCfg.setPortPattern(2);
						} else {
							baseIpCfg.setSrcPort("0");
							baseIpCfg.setPortPattern(1);
						}
						baseIpCfg.setDestPort(baseIpCfg.getSrcPort().trim());
					} else {
						if (baseIpCfg.getSrcPort().indexOf("/") > -1) {
							baseIpCfg.setPortPattern(2);
						} else {
							baseIpCfg.setPortPattern(1);
						}
						baseIpCfg.setSrcPort(baseIpCfg.getSrcPort().trim());
						baseIpCfg.setDestPort(baseIpCfg.getDestPort().trim());

					}
				}
				// TODO 判断源端口和目的端口格式
				// TODO 判断源和目的端口的值
				boolean validPort = this.validPort(errInfo, baseIpCfg.getSrcPort(), baseIpCfg.getDestPort(),
						portPattern);
				if (baseIpCfg.getProtocol()==null) {
					//packet ip reject
					if(regionDict.getFunctionId().equals(5)&&serviceDict!=null&&serviceDict.getServiceId().equals(16)) {
						baseIpCfg.setProtocol(6);
					}else {
						if (StringUtils.isBlank(regionDict.getConfigProtocol())) {
							baseIpCfg.setProtocol(0);
						} else {
							baseIpCfg.setProtocol(Integer.parseInt(regionDict.getConfigProtocol().split(",")[0]));
						}
						
					}
					
					baseIpCfg.setProtocolId(serviceDict==null?0:serviceDict.getProtocolId());
				} else {
					baseIpCfg.setProtocol(baseIpCfg.getProtocol());
					baseIpCfg.setProtocolId(serviceDict==null?0:serviceDict.getProtocolId());
				}
				// TODO 判断Protocol的值
				boolean validProtocol = this.validProtocol(errInfo, baseIpCfg.getProtocol(), protocol);
				// 验证protocol与port是否符合逻辑
				if (validProtocol && validPort) {
					validProtocolAndPort(errInfo, baseIpCfg.getProtocol(), baseIpCfg.getSrcPort(),
							baseIpCfg.getDestPort());
				}
				if (baseIpCfg.getDirection()==null) {
					if (StringUtils.isBlank(regionDict.getConfigDirection())) {
						baseIpCfg.setDirection(0);
					} else {
						baseIpCfg.setDirection(Integer.parseInt(regionDict.getConfigDirection().split(",")[0]));
					}
				} else {
					baseIpCfg.setDirection(baseIpCfg.getDirection());
				}

				// TODO 判断Direction的值
				this.validDirection(errInfo, baseIpCfg.getDirection(), direction);
			}
			if (errInfo.toString().length() > 0) {//
				errTip.append(String.format(prop.getProperty("line"), baseIpCfg.getIndex()) + ",");
				errTip.append(errInfo);
				errTip.append("<br>");
			}
			ipList.add(baseIpCfg);
		}
		if (errTip.toString().length() > 0) {
			throw new ServiceException(errTip.toString());
		}
		long end=System.currentTimeMillis();
		logger.warn("validate ip finish,cost:"+(end-start));
		return ipList;
	}
	/**
	 * 协议与端口验证规则
	 * 
	 * @param errInfo
	 * @param protocol
	 * @param srcPort
	 * @param destPort
	 */
	public void validProtocolAndPort(StringBuffer errInfo, Integer protocol, String srcPort, String destPort) {
		if (srcPort.indexOf("/") > -1) {
			srcPort = srcPort.split("/")[0];
		}
		if (destPort.indexOf("/") > -1) {
			destPort = destPort.split("/")[0];
		}
		if (protocol == 0) {
			return;
		} else if (protocol != 6 && protocol != 17) {
			if (!"0".equals(srcPort) || !"0".equals(destPort)) {
				errInfo.append(prop.get("protocol_and_port") + ";");
			}
		}

	}

	/**
	 * 验证方向
	 * 
	 * @param errInfo
	 * @param direction
	 * @param directionPattern
	 * @throws ServiceException
	 */
	public boolean validDirection(StringBuffer errInfo, Integer direction, String directionPattern)
			throws ServiceException {
		if (direction == null) {
			errInfo.append(String.format(prop.getProperty("can_not_null"), prop.getProperty("direction")) + ";");
			return false;
		}
		boolean has = false;
		for (String p : directionPattern.split(",")) {
			if (p.equals(direction.toString())) {
				has = true;
				break;
			}
		}
		if (!has) {
			errInfo.append(String.format(prop.getProperty("is_incorrect"), prop.getProperty("direction")) + "."
					+ String.format(prop.getProperty("alternative_values"), directionPattern) + ";");
		}
		return has;
	}

	/**
	 * 验证协议
	 * 
	 * @param errInfo
	 * @param protocol
	 * @param protocolPattern
	 * @return
	 * @throws ServiceException
	 */
	public boolean validProtocol(StringBuffer errInfo, Integer protocol, String protocolPattern)
			throws ServiceException {
		if (protocol == null) {
			errInfo.append(String.format(prop.getProperty("can_not_null"), prop.getProperty("protocol")) + ";");
			return false;
		}
		boolean has = false;
		for (String p : protocolPattern.split(",")) {
			if (p.equals(protocol.toString())) {
				has = true;
				break;
			}
		}
		if (!has) {
			errInfo.append(String.format(prop.getProperty("is_incorrect"), prop.getProperty("protocol")) + "."
					+ String.format(prop.getProperty("alternative_values"), protocolPattern) + ";");
		}
		return has;
	}

	/**
	 * 验证端口格式
	 * 
	 * @param srcPortStr
	 * @param destPortStr
	 * @param portPattern
	 * @throws ServiceException
	 */
	public boolean validPort(StringBuffer errInfo, String srcPortStr, String destPortStr, String portPattern)
			throws ServiceException {
		boolean valid = true;
		if (portPattern.equals("1")) {
			try {
				Integer srcPort = Integer.parseInt(srcPortStr);
				if (srcPort < 0 || srcPort > 65535) {
					errInfo.append(prop.getProperty("client_port")
							+ String.format(prop.getProperty("must_between"), 0, 65535) + ";");
					valid = false;
				}
			} catch (Exception e) {
				// TODO: handle exception
				errInfo.append(String.format(prop.getProperty("not_number"), prop.getProperty("client_port")) + ";");
				valid = false;
			}
			try {
				Integer destPort = Integer.parseInt(destPortStr);
				if (destPort < 0 || destPort > 65535) {
					errInfo.append(prop.getProperty("server_port")
							+ String.format(prop.getProperty("must_between"), 0, 65535) + ";");
					valid = false;
				}
			} catch (Exception e) {
				// TODO: handle exception
				errInfo.append(String.format(prop.getProperty("not_number"), prop.getProperty("server_port")) + ";");
				valid = false;
			}
		} else if (portPattern.equals("2")) {
			Pattern p = Constants.PORT_MASK_PATTERN;
			Matcher m = p.matcher(srcPortStr);
			if (!m.matches()) {
				errInfo.append(
						String.format(prop.getProperty("is_in_wrong_format"), prop.getProperty("client_port")) + ";");
				valid = false;
			}
			m = p.matcher(destPortStr);
			if (!m.matches()) {
				errInfo.append(
						String.format(prop.getProperty("is_in_wrong_format"), prop.getProperty("server_port")) + ";");
				valid = false;
			}
			Integer srcPort = Integer.parseInt(srcPortStr.split("/")[0]);
			Integer srcPortMask = Integer.parseInt(srcPortStr.split("/")[1]);
			Integer destPort = Integer.parseInt(destPortStr.split("/")[0]);
			Integer destPortMask = Integer.parseInt(destPortStr.split("/")[1]);
			if (srcPort < 0 || srcPort > 65535) {
				errInfo.append(prop.getProperty("client_port")
						+ String.format(prop.getProperty("must_between"), 0, 65535) + ";");
				valid = false;
			}
			if (srcPortMask < 0 || srcPortMask > 65535) {
				errInfo.append(prop.getProperty("client_port_mask")
						+ String.format(prop.getProperty("must_between"), 0, 65535) + ";");
				valid = false;
			}
			if (destPort < 0 || destPort > 65535) {
				errInfo.append(prop.getProperty("server_port")
						+ String.format(prop.getProperty("must_between"), 0, 65535) + ";");
				valid = false;
			}
			if (destPortMask < 0 || destPortMask > 65535) {
				errInfo.append(prop.getProperty("server_port_mask")
						+ String.format(prop.getProperty("must_between"), 0, 65535) + ";");
				valid = false;
			}
		} else if (portPattern.indexOf("1") > -1 && portPattern.indexOf("2") > -1) {
			Pattern p = Constants.PORT_MASK_PATTERN;
			Pattern p1 = Constants.PORT_PATTERN;
			Matcher m = p.matcher(srcPortStr);// 源端口是端口掩码格式
			Matcher m1 = p.matcher(destPortStr);// 目的端口是端口掩码格式
			Matcher m2 = p1.matcher(srcPortStr);// 源端口是端口格式
			Matcher m3 = p1.matcher(destPortStr);// 目的端口是端口格式
			if (m.matches()) {
				Integer srcPort = Integer.parseInt(srcPortStr.split("/")[0]);
				Integer srcPortMask = Integer.parseInt(srcPortStr.split("/")[1]);
				if (srcPort < 0 || srcPort > 65535) {
					errInfo.append(prop.getProperty("client_port")
							+ String.format(prop.getProperty("must_between"), 0, 65535) + ";");
					valid = false;
				}
				if (srcPortMask < 0 || srcPortMask > 65535) {
					errInfo.append(prop.getProperty("client_port_mask")
							+ String.format(prop.getProperty("must_between"), 0, 65535) + ";");
					valid = false;
				}
			}
			if (m1.matches()) {
				Integer destPort = Integer.parseInt(destPortStr.split("/")[0]);
				Integer destPortMask = Integer.parseInt(destPortStr.split("/")[1]);

				if (destPort < 0 || destPort > 65535) {
					errInfo.append(prop.getProperty("server_port")
							+ String.format(prop.getProperty("must_between"), 0, 65535) + ";");
					valid = false;
				}
				if (destPortMask < 0 || destPortMask > 65535) {
					errInfo.append(prop.getProperty("server_port_mask")
							+ String.format(prop.getProperty("must_between"), 0, 65535) + ";");
					valid = false;
				}
			}
			if (m2.matches()) {
				try {
					Integer srcPort = Integer.parseInt(srcPortStr);
					if (srcPort < 0 || srcPort > 65535) {
						errInfo.append(prop.getProperty("client_port")
								+ String.format(prop.getProperty("must_between"), 0, 65535) + ";");
						valid = false;
					}
				} catch (Exception e) {
					// TODO: handle exception
					errInfo.append(
							String.format(prop.getProperty("not_number"), prop.getProperty("client_port")) + ";");
					valid = false;
				}
			}
			if (m3.matches()) {
				try {
					Integer destPort = Integer.parseInt(destPortStr);
					if (destPort < 0 || destPort > 65535) {
						errInfo.append(prop.getProperty("server_port")
								+ String.format(prop.getProperty("must_between"), 0, 65535) + ";");
						valid = false;
					}
				} catch (Exception e) {
					// TODO: handle exception
					errInfo.append(
							String.format(prop.getProperty("not_number"), prop.getProperty("server_port")) + ";");
					valid = false;
				}
			}
			if ((m.matches() && m3.matches()) || (m2.matches() && m1.matches())) {
				errInfo.append(prop.getProperty("the_same_port_pattern") + ";");
				valid = false;
			}
			if (!m.matches() && !m2.matches()) {
				errInfo.append(
						String.format(prop.getProperty("is_in_wrong_format"), prop.getProperty("client_port")) + ";");
				valid = false;
			}
			if (!m1.matches() && !m3.matches()) {
				errInfo.append(
						String.format(prop.getProperty("is_in_wrong_format"), prop.getProperty("server_port")) + ";");
				valid = false;
			}

		}
		return valid;
	}
	/**
	 * IPv4 范围验证
	 * 
	 * @param ipName
	 * @param errInfo
	 * @param ip
	 */
	public void validIPv4Range(String ipName, StringBuffer errInfo, String ip) {
		String prefix = ip.split("-")[0];
		String subfix = ip.split("-")[1];
		if ((Integer.parseInt(prefix.split("\\.")[0])<128&&Integer.parseInt(subfix.split("\\.")[0])<128)
		||(Integer.parseInt(prefix.split("\\.")[0])>127&&Integer.parseInt(subfix.split("\\.")[0])>127&&Integer.parseInt(prefix.split("\\.")[0])<192&&Integer.parseInt(subfix.split("\\.")[0])<192)
		||(Integer.parseInt(prefix.split("\\.")[0])>191&&Integer.parseInt(subfix.split("\\.")[0])>191&&Integer.parseInt(prefix.split("\\.")[0])<224&&Integer.parseInt(subfix.split("\\.")[0])<224)
		||(Integer.parseInt(prefix.split("\\.")[0])>223&&Integer.parseInt(subfix.split("\\.")[0])>223&&Integer.parseInt(prefix.split("\\.")[0])<240&&Integer.parseInt(subfix.split("\\.")[0])<240)
		||(Integer.parseInt(prefix.split("\\.")[0])>239&&Integer.parseInt(subfix.split("\\.")[0])>239&&Integer.parseInt(prefix.split("\\.")[0])<256&&Integer.parseInt(subfix.split("\\.")[0])<256)) {
		}else {
			errInfo.append(prop.get(ipName) + " " + prop.get("ip_range_in_the_same") + ";");
		}
//		if (Integer.parseInt(prefix.split("\\.")[0]) < 192 || Integer.parseInt(prefix.split("\\.")[0]) > 223
//				|| Integer.parseInt(subfix.split("\\.")[0]) < 192 || Integer.parseInt(subfix.split("\\.")[0]) > 223) {
//			errInfo.append(prop.get(ipName) + " " + prop.get("ip_range_in_c") + ";");
//		}
		if (!prefix.split("\\.")[0].equals(subfix.split("\\.")[0])
				|| !prefix.split("\\.")[1].equals(subfix.split("\\.")[1])
				|| !prefix.split("\\.")[2].equals(subfix.split("\\.")[2])) {
			errInfo.append(prop.get(ipName) + " " + prop.get("ip_range_bit_field") + ";");
		}
		if (ipToInt(prefix) >= ipToInt(subfix)) {
			errInfo.append(prop.get(ipName) + " " + prop.get("ip_range_smaller") + ";");
		}
	}

	/**
	 * 根据位运算把 byte[] -> int
	 * 
	 * @param bytes
	 * @return int
	 */
	public static int bytesToInt(byte[] bytes) {
		int addr = bytes[3] & 0xFF;
		addr |= ((bytes[2] << 8) & 0xFF00);
		addr |= ((bytes[1] << 16) & 0xFF0000);
		addr |= ((bytes[0] << 24) & 0xFF000000);
		return addr;
	}

	public static int ipToInt(String ipAddr) {
		try {
			return bytesToInt(ipToBytesByReg(ipAddr));
		} catch (Exception e) {
			throw new IllegalArgumentException(ipAddr + " is invalid IP");
		}
	}

	/**
	 * 把IP地址转化为int
	 * 
	 * @param ipAddr
	 * @return int
	 */
	public static byte[] ipToBytesByReg(String ipAddr) {
		byte[] ret = new byte[4];
		try {
			String[] ipArr = ipAddr.split("\\.");
			ret[0] = (byte) (Integer.parseInt(ipArr[0]) & 0xFF);
			ret[1] = (byte) (Integer.parseInt(ipArr[1]) & 0xFF);
			ret[2] = (byte) (Integer.parseInt(ipArr[2]) & 0xFF);
			ret[3] = (byte) (Integer.parseInt(ipArr[3]) & 0xFF);
			return ret;
		} catch (Exception e) {
			throw new IllegalArgumentException(ipAddr + " is invalid IP");
		}

	}

	/**
	 * 校验ip
	 * 
	 * @param ipName
	 * @param errInfo
	 * @param ip
	 * @param ipType
	 * @param ipPattern
	 * @return
	 * @throws ServiceException
	 */
	public String validIPAddress(String ipName, StringBuffer errInfo, String ip, String ipType, String ipPattern)
			throws ServiceException {
		String matchType = "";
		Pattern ipv4IpSubnetOriginalRegexp = Constants.IPV4_IP_SUBNET_ORIGINAL_PATTERN;
		Pattern ipv4IpSubnetRegexp = Constants.IPV4_IP_SUBNET_PATTERN;
		Pattern ipv6IpSubnetRegexp = Constants.IPV6_IP_SUBNET_PATTERN;
		Pattern ipv4IpRangeRegexp = Constants.IPV4_IP_RANGE_PATTERN;
		Pattern ipv6IpRangeRegexp = Constants.IPV6_IP_RANGE_PATTERN;
		Pattern ipv4IpRegexp = Constants.IPV4_IP_PATTERN;
		Pattern ipv6IpRegexp = Constants.IPV6_IP_PATTERN;
		//减少正则匹配次数
		if(ip.indexOf("-")==-1&&ip.indexOf("/")==-1) {
			Matcher matcher = ipv4IpRegexp.matcher(ip);
			if (matcher.matches()) {
				matchType = "IPV4_IP";
			}else {
				matcher = ipv6IpRegexp.matcher(ip);
				if (matcher.matches()) {
					matchType = "IPV6_IP";
				} else {
					errInfo.append(String.format(prop.getProperty("is_in_wrong_format"), prop.getProperty(ipName))
									+ ";");
				}
			}
		}else if(ip.indexOf("/")!=-1) {
			Matcher matcher = ipv4IpSubnetRegexp.matcher(ip);
			if (matcher.matches()) {
				matchType = "IPV4_IP_SUBNET";
			} else {
				matcher = ipv6IpSubnetRegexp.matcher(ip);
				if (matcher.matches()) {
					matchType = "IPV6_IP_SUBNET";
				} else {
					errInfo.append(String.format(prop.getProperty("is_in_wrong_format"), prop.getProperty(ipName))
							+ ";");
				}
			}
		}else if(ip.indexOf("-")!=-1) {
			Matcher matcher = ipv4IpRangeRegexp.matcher(ip);
			if (matcher.matches()) {
				matchType = "IPV4_IP_RANGE";
			} else {
				matcher = ipv6IpRangeRegexp.matcher(ip);
				if (matcher.matches()) {
					matchType = "IPV6_IP_RANGE";
				} else {
					matcher = ipv4IpSubnetOriginalRegexp.matcher(ip);
					if(matcher.matches()) {
						errInfo.append(prop.getProperty(ipName)+" "+prop.getProperty("ipv4_mask_range_tip")+ ";");
					}else {
						errInfo.append(
								String.format(prop.getProperty("is_in_wrong_format"), prop.getProperty(ipName))
										+ ";");
					}
				}
			}
		}
		if (ipType.indexOf("4") == -1 && matchType.startsWith("IPV4")) {
			errInfo.append(
					prop.getProperty("ip_type") + " " + String.format(prop.getProperty("can_not_be"), "IPv4") + ",4;");
		}
		if (ipType.indexOf("6") == -1 && matchType.startsWith("IPV6")) {
			errInfo.append(
					prop.getProperty("ip_type") + " " + String.format(prop.getProperty("can_not_be"), "IPv6") + ",6;");
		}
		if (ipPattern.indexOf("3") == -1 && matchType.endsWith("IP")) {
			errInfo.append(
					prop.getProperty("ip_pattern") + " " + String.format(prop.getProperty("can_not_be"), "IP") + ",3;");
		}
		if (ipPattern.indexOf("1") == -1 && matchType.endsWith("SUBNET")) {
			errInfo.append(prop.getProperty("ip_pattern") + " "
					+ String.format(prop.getProperty("can_not_be"), prop.getProperty("ip_subnet")) + ",1;");
		}
		if (ipPattern.indexOf("2") == -1 && matchType.endsWith("RANGE")) {
			errInfo.append(prop.getProperty("ip_pattern") + " "
					+ String.format(prop.getProperty("can_not_be"), prop.getProperty("ip_range")) + ",2;");
		}
		return matchType;
	}
	
	public List<Map<Long, Integer>> getAsnNoMaps() {
		return asnNoMaps;
	}
	public void setAsnNoMaps(List<Map<Long, Integer>> asnNoMaps) {
		this.asnNoMaps = asnNoMaps;
	}
	
}
