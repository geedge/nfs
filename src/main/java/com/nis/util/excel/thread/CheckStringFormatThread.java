package com.nis.util.excel.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jets3t.service.ServiceException;
import org.springframework.beans.BeanUtils;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Sets;
import com.nis.domain.FunctionRegionDict;
import com.nis.domain.FunctionServiceDict;
import com.nis.domain.SysDataDictionaryItem;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.util.Constants;
import com.nis.util.DictUtils;

public class CheckStringFormatThread implements Callable<String>{
	private Logger logger=Logger.getLogger(CheckStringFormatThread.class);
	private BlockingQueue<? extends Object> srcQueue;
	private BlockingQueue<BaseStringCfg<?>> destQueue;
	private Properties prop;
//	private DnsResStrategyDao dnsResStrategyDao;
	private FunctionServiceDict serviceDict;
	private FunctionRegionDict regionDict;
	public CheckStringFormatThread(FunctionServiceDict serviceDict,FunctionRegionDict regionDict,Properties prop,BlockingQueue<? extends Object> srcQueue,BlockingQueue<BaseStringCfg<?>> destQueue) {
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
			if(regionDict.getRegionType().intValue()==2) {
				try {
					List<BaseStringCfg<?>> cfgs=this.checkStringCfg(dataList);
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
	
	public List<BaseStringCfg<?>> checkStringCfg(List<?> list) throws ServiceException {
		logger.warn("start to validate stringCfg data...");
		long start=System.currentTimeMillis();
		List<BaseStringCfg<?>> stringList = new ArrayList<BaseStringCfg<?>>();
		String exprTypeP = regionDict.getConfigExprType();
		if (StringUtils.isBlank(exprTypeP)) {
			throw new RuntimeException("Found String region,but exprType is Empty");
		}
		String matchMethodP = regionDict.getConfigMatchMethod();
		if (StringUtils.isBlank(matchMethodP)) {
			throw new RuntimeException("Found String region,but matchMethod is Empty");
		}
		String hexP = regionDict.getConfigHex();
		if (StringUtils.isBlank(hexP)) {
			throw new RuntimeException("Found String region,but hex is Empty");
		}
		String mulityKeywordsP = regionDict.getConfigMultiKeywords();
		if (StringUtils.isBlank(mulityKeywordsP)) {
			throw new RuntimeException("Found String region,but mulityKeywords is Empty");
		}
		StringBuffer errTip = new StringBuffer();
		Pattern pattern = Pattern.compile("\t|\r|\n|\b|\f");
		Pattern domainPattern = Pattern.compile(Constants.DOMAIN_REGEXP);
		for (int i = 0; i < list.size(); i++) {
			StringBuffer errInfo = new StringBuffer();
			BaseStringCfg baseStringCfg = new BaseStringCfg();
			BeanUtils.copyProperties(list.get(i), baseStringCfg);
			if (regionDict.getRegionType().equals(2)) {
				if (regionDict.getFunctionId().equals(510) && "p2p_hash".equals(regionDict.getConfigServiceType())) {
					String userRegion1 = baseStringCfg.getUserRegion1();
					if (StringUtils.isNotBlank(userRegion1)) {
						List<SysDataDictionaryItem> hashs = DictUtils.getDictList("P2P_HASH_TYPE");
						boolean has = false;
						for (SysDataDictionaryItem hash : hashs) {
							if (hash.getItemCode().equals(userRegion1)) {
								has = true;
								break;
							}
						}
						if (!has) {
							errInfo.append(String.format(prop.getProperty("is_incorrect"),
									prop.getProperty("p2p_hash_type") + " ") + ";");
						}
					}
				}
				String keyword = baseStringCfg.getCfgKeywords();
				if (!regionDict.getFunctionId().equals(403)) {
					if (StringUtils.isBlank(keyword)) {
						errInfo.append(
								String.format(prop.getProperty("can_not_null"), prop.getProperty("key_word") + " ") + ";");
					}
					if (mulityKeywordsP.equals("0")) {
						if (keyword.indexOf("\n") > -1) {
							errInfo.append(
									String.format(prop.getProperty("not_multiple"), prop.getProperty("key_word")) + ";");
						}
						Matcher m = pattern.matcher(keyword);
						if (m.find()) {
							errInfo.append(String.format(prop.getProperty("has_invisible_char"),
									prop.getProperty("key_word") + " '" + keyword + "'") + ";");
						}
					} else {
						boolean has = false;
						Set<String> keywordSet=Sets.newHashSet();
						for (String key : keyword.split("\n")) {
							Matcher m = pattern.matcher(key);
							if (m.find()) {
								has = true;
								errInfo.append(String.format(prop.getProperty("has_invisible_char"),
										prop.getProperty("key_word") + " '" + key + "'") + ";");
								break;
							}
							if(!keywordSet.contains(key)) {
								keywordSet.add(key);
							}else {
								errInfo.append(prop.getProperty("key_word") + " '" + key + "'"+" "+prop.getProperty("repeat") + ";");
							}
						}
						if (!has) {
							if(keyword.replaceAll("\n","").length()>1024) {
								errInfo.append(String.format(prop.getProperty("most_keywords"),
										prop.getProperty("key_word")) + ";");
							}else {
								String reWord = keyword.replaceAll("\n", Constants.KEYWORD_EXPR);
								baseStringCfg.setCfgKeywords(reWord);
							}
						}
					}
				}else {
					if (StringUtils.isBlank(keyword)) {
						errInfo.append(
								String.format(prop.getProperty("can_not_null"), prop.getProperty("domain_name") + " ") + ";");
					}else {
						Matcher m = pattern.matcher(keyword);
						if (m.find()) {
							errInfo.append(String.format(prop.getProperty("has_invisible_char"),
									prop.getProperty("domain_name") + " '" + keyword + "'") + ";");
						}else {
							m = domainPattern.matcher(keyword);
							if(!m.matches()) {
								errInfo.append(String.format(prop.getProperty("not_valid_domain"),
										prop.getProperty("domain_name") + " '" + keyword + "'") + ";");
							}else {
								baseStringCfg.setDomain(keyword);
							}
							
						}
					}
					
				}
				
				Integer exprType = baseStringCfg.getExprType();
				boolean has = false;
				if (exprType == null) {
					if (exprTypeP.indexOf(",") == -1) {
						if (mulityKeywordsP.equals("0") && exprTypeP.equals("1")) {
							throw new RuntimeException("region dict config error,dict id is " + regionDict.getDictId());
						}
						baseStringCfg.setExprType(Integer.parseInt(exprTypeP));
					} else if (exprTypeP.indexOf("0") > -1 && mulityKeywordsP.equals("0")) {
						baseStringCfg.setExprType(0);
					} else if (exprTypeP.indexOf("1") > -1 && mulityKeywordsP.equals("1")
							&& keyword.indexOf("\n") > -1) {
						baseStringCfg.setExprType(1);
					} else if (exprTypeP.indexOf("0") > -1 && mulityKeywordsP.equals("1")
							&& keyword.indexOf("\n") == -1) {
						baseStringCfg.setExprType(0);
					} else {
						baseStringCfg.setExprType(Integer.parseInt(exprTypeP.split(",")[0]));
					}
					// errInfo.append(String.format(prop.getProperty("can_not_null"),
					// prop.getProperty("expression_type"))+";");
				} else {
					for (String exp : exprTypeP.split(",")) {
						if (exp.equals(exprType.toString())) {
							has = true;
							break;
						}
					}
					if (!has) {
						errInfo.append(
								String.format(prop.getProperty("is_incorrect"), prop.getProperty("expression_type"))
										+ ";");
					}
					has = false;
				}
				exprType = baseStringCfg.getExprType();
				Integer matchMethod = baseStringCfg.getMatchMethod();
				if (matchMethod == null) {
					if (matchMethodP.indexOf(",") == -1) {
						if (exprTypeP.equals("1") && !matchMethodP.equals("0")) {
							throw new RuntimeException("region dict config error,dict id is " + regionDict.getDictId());
						}
						baseStringCfg.setMatchMethod(Integer.parseInt(matchMethodP));
					} else if (exprType != null && exprType.intValue() == 1) {
						if (matchMethodP.indexOf("0") > -1) {
							baseStringCfg.setMatchMethod(0);
						} else {
							throw new RuntimeException("region dict config error,dict id is " + regionDict.getDictId());
						}

					} else {
						baseStringCfg.setMatchMethod(Integer.parseInt(matchMethodP.split(",")[0]));
					}
					// errInfo.append(String.format(prop.getProperty("can_not_null"),
					// prop.getProperty("match_method"))+";");
				} else {
					for (String exp : matchMethodP.split(",")) {
						if (exp.equals(matchMethod.toString())) {
							has = true;
							break;
						}
					}
					if (!has) {
						errInfo.append(String.format(prop.getProperty("is_incorrect"), prop.getProperty("match_method"))
								+ ";");
					}
				}

				Integer isHex = baseStringCfg.getIsHex();
				Integer isCaseInsenstive = baseStringCfg.getIsCaseInsenstive();
				if (isHex == null || isCaseInsenstive == null) {
					if (isHex == null) {
						if (hexP.equals("0") || hexP.equals("2")) {
							baseStringCfg.setIsHex(0);
						} else if (hexP.equals("1")) {
							baseStringCfg.setIsHex(1);
						} else {
							errInfo.append(
									String.format(prop.getProperty("can_not_null"), prop.getProperty("is_hex")) + ";");
						}
					}
					if (isCaseInsenstive == null) {
						if (hexP.equals("0") || hexP.equals("1")) {
							baseStringCfg.setIsCaseInsenstive(0);
						} else if (hexP.equals("2")) {
							baseStringCfg.setIsCaseInsenstive(1);
						} else {
							errInfo.append(String.format(prop.getProperty("can_not_null"),
									prop.getProperty("is_case_insenstive")) + ";");
						}
					}
				} else {
					if (isHex.intValue() != 0 && isHex.intValue() != 1) {
						errInfo.append(
								String.format(prop.getProperty("is_incorrect"), prop.getProperty("is_hex")) + ";");
					}
					if (isCaseInsenstive.intValue() != 0 && isCaseInsenstive.intValue() != 1) {
						errInfo.append(
								String.format(prop.getProperty("is_incorrect"), prop.getProperty("is_case_insenstive"))
										+ ";");
					}
					if (hexP.indexOf("1") == -1 && isHex.intValue() == 1) {
						errInfo.append(
								String.format(prop.getProperty("is_incorrect"), prop.getProperty("is_hex")) + ";");
					}
					if (hexP.equals("1") && isHex.intValue() == 0) {
						errInfo.append(
								String.format(prop.getProperty("is_incorrect"), prop.getProperty("is_hex")) + ";");
					}
					if (hexP.indexOf("2") == -1 && isCaseInsenstive.intValue() == 1) {
						errInfo.append(
								String.format(prop.getProperty("is_incorrect"), prop.getProperty("is_case_insenstive"))
										+ ";");
					}
					if (hexP.equals("2") && isCaseInsenstive.intValue() == 0) {
						errInfo.append(
								String.format(prop.getProperty("is_incorrect"), prop.getProperty("is_case_insenstive"))
										+ ";");
					}
				}
				isHex = baseStringCfg.getIsHex();
				isCaseInsenstive = baseStringCfg.getIsCaseInsenstive();
				if (isHex != null && isCaseInsenstive != null) {
					if (isHex.intValue() == 0 && isCaseInsenstive.intValue() == 0) {
						baseStringCfg.setIsHexbin(0);
					} else if (isHex.intValue() == 1 && isCaseInsenstive.intValue() == 0) {
						baseStringCfg.setIsHexbin(1);
					} else if (isHex.intValue() == 0 && isCaseInsenstive.intValue() == 1) {
						baseStringCfg.setIsHexbin(2);
					}else {
						errInfo.append(prop.getProperty("hex_case_insensitive")+ ";");
					}
				}

			}
			if (errInfo.toString().length() > 0) {//
				errTip.append(String.format(prop.getProperty("line"), baseStringCfg.getIndex()) + ",");
				errTip.append(errInfo);
				errTip.append("<br>");
			}
			stringList.add(baseStringCfg);
		}
		if (errTip.toString().length() > 0) {
			throw new ServiceException(errTip.toString());
		}
		long end=System.currentTimeMillis();
		logger.warn("validate stringCfg data finish,cost:"+(end-start));
		return stringList;
	}
}
