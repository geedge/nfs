package com.nis.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nis.domain.FunctionRegionDict;
import com.nis.domain.FunctionServiceDict;
import com.nis.domain.SysDataDictionaryItem;
import com.nis.domain.SysDataDictionaryName;
import com.nis.domain.basics.PolicyGroupInfo;
import com.nis.domain.basics.SysDictInfo;
import com.nis.domain.configuration.DnsResStrategy;
import com.nis.web.dao.FunctionRegionDictDao;
import com.nis.web.dao.FunctionServiceDictDao;
import com.nis.web.dao.SysDictDao;
import com.nis.web.dao.basics.PolicyGroupInfoDao;
import com.nis.web.dao.basics.SysDictInfoDao;
import com.nis.web.dao.configuration.DnsResStrategyDao;
import com.nis.web.service.SpringContextHolder;





/**
 * 系统数据字典工具类
 * @author Administrator
 *
 */
public class DictUtils {
	
	private final static SysDictDao dictDao = SpringContextHolder.getBean(SysDictDao.class);
	private final static SysDictInfoDao sysDictInfoDao = SpringContextHolder.getBean(SysDictInfoDao.class);
	private final static FunctionRegionDictDao functionRegionDictDao = SpringContextHolder.getBean(FunctionRegionDictDao.class);
	private final static FunctionServiceDictDao functionServiceDictDao = SpringContextHolder.getBean(FunctionServiceDictDao.class);
	private final static DnsResStrategyDao dnsResStrategyDao = SpringContextHolder.getBean(DnsResStrategyDao.class);
	private final static PolicyGroupInfoDao policyGroupInfoDao = SpringContextHolder.getBean(PolicyGroupInfoDao.class);
	
	public static Map<String, List<SysDataDictionaryItem>> getDictData() {
		
		 Map<String, List<SysDataDictionaryItem>> dictMap = (Map<String, List<SysDataDictionaryItem>>)CacheUtils.get(Constants.CACHE_DICT_MAP);
		 
		 if (StringUtil.isEmpty(dictMap)) {
			 dictMap = Maps.newHashMap();
			 List<SysDataDictionaryName> dicList = dictDao.findAllList(new SysDataDictionaryName());
			 for (SysDataDictionaryName dict : dicList) {
				 dictMap.put(dict.getMark(), dict.getDictItemList());
			 }
			 CacheUtils.put(Constants.CACHE_DICT_MAP, dictMap);
		 }
		
		return dictMap;
	}
	
	/**
	 * 获取词典对应key的所有词条，code:value
	 * @param key
	 * @return
	 */
	public static Map<String,String> getDictOption(String key) {
		List<SysDataDictionaryItem> itemList = getDictData().get(key);
		Map<String, String> itemMap = Maps.newHashMap();
		for (SysDataDictionaryItem item : itemList) {
			itemMap.put(item.getItemCode(), item.getItemValue());
		}
		
		return itemMap;
	}
	
	/**
	 * 获取词典对应key的所有词条，value:code
	 * @param key
	 * @return
	 */
	public static Map<String,String> getDictOptionInReversion(String key) {
		List<SysDataDictionaryItem> itemList = getDictData().get(key);
		Map<String, String> itemMap = Maps.newHashMap();
		for (SysDataDictionaryItem item : itemList) {
			itemMap.put(item.getItemValue(), item.getItemCode());
		}
		
		return itemMap;
	}
	
	
	
	public static List<SysDataDictionaryItem> getDictList(String key){
		 List<SysDataDictionaryItem>  itemList = getDictData().get(key);
		 if (StringUtil.isEmpty(itemList)) {
			 itemList = Lists.newArrayList();
		 }
		return itemList;
	}
	
	public static String getDictLabels(String dictKey, String itemCodes, String defaultValue){
		
		Map<String, String> itemMap = getDictOption(dictKey);
		
		if (!StringUtil.isEmpty(itemMap)) {
			List<String> valueList = Lists.newArrayList();
			for (String itemCode : StringUtils.split(itemCodes, ",")){
				valueList.add(itemMap.get(itemCode));
			}
			return StringUtils.join(valueList, ",");
			
		}
		
		return defaultValue;
	}
	
	
	/**
	 * 获取字典词条标签
	 * @param value
	 * @param type
	 * @param defaultValue
	 * @return
	 */
	public static String getDictLabel(String dictKey, String itemCode, String defaultValue){
		String itemLabel = getDictOption(dictKey).get(itemCode);
		
		return StringUtil.isBlank(itemLabel) ? defaultValue : itemLabel;
	}
	
	public static String getDictLabel(String dictKey, String itemCode){
		
		return getDictLabel(dictKey, itemCode, "默认");
	}
	
	/*public static String getDictLabel(String dictKey, String itemCode, String itemValue){
		String itemLabel = getDictOption(dictKey).get(itemCode);
		
		return StringUtil.isBlank(itemLabel) ? defaultValue : itemLabel;
	}*/
	
	
	
	public static String getDictCode(String dictKey, String itemValue, String defaultValue){
		String itemCode = getDictOptionInReversion(dictKey).get(itemValue);
		
		return StringUtil.isBlank(itemCode) ? defaultValue : itemCode;
	}
	
	
	public static String getDictCode(String dictKey, String itemValue){
		return getDictCode(dictKey, itemValue, "默认");
	}
	
	
	/**
	 * 返回字典列表（JSON）
	 * @param type
	 * @return
	 */
	public static String getDictListJson(String key){
		return JsonMapper.toJsonString(getDictList(key));
	}
	/**
	 * 增强字符串配置特征域字典
	 * @return
	 */
	public static List<SysDictInfo> getFeaturesDictData(String itemCode) {
		Map<String, List<SysDictInfo>> dictMap = (Map<String, List<SysDictInfo>>)CacheUtils.get(Constants.CACHE_FEATURES_DICT_MAP);
		if(StringUtil.isEmpty(dictMap)){
			dictMap = Maps.newHashMap();
			SysDictInfo dict = new SysDictInfo();
			dict.setItemType(3);
			dict.setItemCode(itemCode);
			List<SysDictInfo> dicList = sysDictInfoDao.findAllSysDictInfo(dict,"");
			dictMap.put(itemCode, dicList);
			CacheUtils.put(Constants.CACHE_FEATURES_DICT_MAP, dictMap);
		}else{
			if(StringUtil.isEmpty(dictMap.get(itemCode))){
				SysDictInfo dict = new SysDictInfo();
				dict.setItemType(3);
				dict.setItemCode(itemCode);
				List<SysDictInfo> dicList = sysDictInfoDao.findAllSysDictInfo(dict,"");
				dictMap.put(itemCode, dicList);
				CacheUtils.put(Constants.CACHE_FEATURES_DICT_MAP, dictMap);
			}
			
		}
		
		return dictMap.get(itemCode);
	}
	/**
	 * 功能业务字典，获取相应功能菜单对应的业务信息
	 * @param functionId
	 * @return
	 */
	public static List<FunctionServiceDict> getFunctionServiceDictList(Integer functionId){
		List<FunctionServiceDict> allDictList = (List<FunctionServiceDict>)CacheUtils.get(Constants.CACHE_FUNCTION_SERVICE_DICT);
		List<FunctionServiceDict> dictList = new ArrayList();
		if(StringUtil.isEmpty(allDictList)){
			FunctionServiceDict entity = new FunctionServiceDict();
			allDictList = functionServiceDictDao.getList(entity);
			CacheUtils.put(Constants.CACHE_FUNCTION_SERVICE_DICT, allDictList);
		}
		for(FunctionServiceDict entity:allDictList){
			if(entity.getFunctionId().equals(functionId)){
				dictList.add(entity);
			}
		}
		return dictList;		
	}
	/**
	 * 功能业务字典，业务信息
	 * @param dictId
	 * @return
	 */
	public static FunctionServiceDict getFunctionServiceDict(Integer dictId){
		FunctionServiceDict serviceDict=null;
		List<FunctionServiceDict> allDictList = (List<FunctionServiceDict>)CacheUtils.get(Constants.CACHE_FUNCTION_SERVICE_DICT);
		List<FunctionServiceDict> dictList = new ArrayList();
		if(StringUtil.isEmpty(allDictList)){
			FunctionServiceDict entity = new FunctionServiceDict();
			allDictList = functionServiceDictDao.getList(entity);
			CacheUtils.put(Constants.CACHE_FUNCTION_SERVICE_DICT, allDictList);
		}
		for(FunctionServiceDict entity:allDictList){
			if(entity.getDictId().equals(dictId)){
				serviceDict=entity;
				break;
			}
		}
		return serviceDict;
	}
	/**
	 * 功能业务字典，获取相应功能菜单对应的业务信息
	 * @param functionId
	 * @return
	 */
	public static List<FunctionServiceDict> getFunctionServiceDictList(){
		List<FunctionServiceDict> allDictList = (List<FunctionServiceDict>)CacheUtils.get(Constants.CACHE_FUNCTION_SERVICE_DICT);
		List<FunctionServiceDict> dictList = new ArrayList();
		if(StringUtil.isEmpty(allDictList)){
			FunctionServiceDict entity = new FunctionServiceDict();
			allDictList = functionServiceDictDao.getList(entity);
			CacheUtils.put(Constants.CACHE_FUNCTION_SERVICE_DICT, allDictList);
		}
		
		return allDictList;		
	}
	/**
	 * 功能配置域字典，获取相应功能菜单对应的配置域信息
	 * @param functionId
	 * @return
	 */
	public static List<FunctionRegionDict> getFunctionRegionDictList(Integer functionId){
		List<FunctionRegionDict> allDictList = (List<FunctionRegionDict>)CacheUtils.get(Constants.CACHE_FUNCTION_REGION_DICT);
		List<FunctionRegionDict> dictList = new ArrayList();
		if(StringUtil.isEmpty(allDictList)){
			FunctionRegionDict entity = new FunctionRegionDict();
			allDictList = functionRegionDictDao.getList(entity);
			CacheUtils.put(Constants.CACHE_FUNCTION_REGION_DICT, allDictList);
		}
		for(FunctionRegionDict entity:allDictList){
			if(entity.getFunctionId().equals(functionId)){
				dictList.add(entity);
			}
		}
		return dictList;		
	}
	/**
	 * 功能配置域字典，获取配置域信息
	 * @param dictId
	 * @return
	 */
	public static FunctionRegionDict getFunctionRegionDict(Integer dictId){
		FunctionRegionDict regionDict=null;
		List<FunctionRegionDict> allDictList = (List<FunctionRegionDict>)CacheUtils.get(Constants.CACHE_FUNCTION_REGION_DICT);
		List<FunctionRegionDict> dictList = new ArrayList();
		if(StringUtil.isEmpty(allDictList)){
			FunctionRegionDict entity = new FunctionRegionDict();
			allDictList = functionRegionDictDao.getList(entity);
			CacheUtils.put(Constants.CACHE_FUNCTION_REGION_DICT, allDictList);
		}
		for(FunctionRegionDict entity:allDictList){
			if(entity.getDictId().equals(dictId)){
				regionDict=entity;
				break;
			}
		}
		return regionDict;		
	}
	/**
	 * 功能配置域字典，获取配置域信息
	 * @param dictId
	 * @return
	 */
	public static FunctionRegionDict getFunctionRegionDictByRegionCode(Integer functionId,Integer regionCode){
		FunctionRegionDict regionDict=null;
		List<FunctionRegionDict> allDictList = (List<FunctionRegionDict>)CacheUtils.get(Constants.CACHE_FUNCTION_REGION_DICT);
		List<FunctionRegionDict> dictList = new ArrayList();
		if(StringUtil.isEmpty(allDictList)){
			FunctionRegionDict entity = new FunctionRegionDict();
			allDictList = functionRegionDictDao.getList(entity);
			CacheUtils.put(Constants.CACHE_FUNCTION_REGION_DICT, allDictList);
		}
		for(FunctionRegionDict entity:allDictList){
			if(entity.getConfigRegionCode().equals(regionCode)&&entity.getFunctionId().equals(functionId)){
				regionDict=entity;
				break;
			}
		}
		return regionDict;		
	}
	/**
	 * 功能配置域字典，获取相应功能菜单对应的配置域信息
	 * @param functionId
	 * @return
	 */
	public static List<FunctionRegionDict> getFunctionRegionDictList(){
		List<FunctionRegionDict> allDictList = (List<FunctionRegionDict>)CacheUtils.get(Constants.CACHE_FUNCTION_REGION_DICT);
		List<FunctionRegionDict> dictList = new ArrayList();
		if(StringUtil.isEmpty(allDictList)){
			FunctionRegionDict entity = new FunctionRegionDict();
			allDictList = functionRegionDictDao.getList(entity);
			CacheUtils.put(Constants.CACHE_FUNCTION_REGION_DICT, allDictList);
		}
		
		return allDictList;		
	}
	public static List<DnsResStrategy> getDnsResStrategyList(){
		List<DnsResStrategy> allDictList = (List<DnsResStrategy>)CacheUtils.get(Constants.CACHE_DNSRESSTRATEGY_DICT);
		if(StringUtil.isEmpty(allDictList)){
			allDictList = dnsResStrategyDao.findList(null, Constants.VALID_YES, Constants.AUDIT_YES);
			CacheUtils.put(Constants.CACHE_DNSRESSTRATEGY_DICT, allDictList);
		}
		
		return allDictList;		
	}
	public static List<DnsResStrategy> getDnsResStrategyList(Long cfgId){
		List<DnsResStrategy> dnsResStrategies=Lists.newArrayList();
		List<DnsResStrategy> allDictList = getDnsResStrategyList();
		if(StringUtil.isEmpty(allDictList)){
		}else {
			for(DnsResStrategy dnsResStrategy:allDictList) {
				if(dnsResStrategy.getCfgId().longValue()==cfgId.longValue()) {
					dnsResStrategies.add(dnsResStrategy);
				}
			}
		}
		return dnsResStrategies;		
	}
	public static List<PolicyGroupInfo> getDnsGroups(){
		List<PolicyGroupInfo> allDictList = (List<PolicyGroupInfo>)CacheUtils.get(Constants.CACHE_DNSGROUP_DICT);
		if(StringUtil.isEmpty(allDictList)){
			allDictList = policyGroupInfoDao.findPolicyGroupInfosByType(1);
			CacheUtils.put(Constants.CACHE_DNSGROUP_DICT, allDictList);
		}
		
		return allDictList;		
	}
	public static PolicyGroupInfo getDnsGroups(int groupId){
		List<PolicyGroupInfo> allDictList = getDnsGroups();
		if(StringUtil.isEmpty(allDictList)){
		}else {
			for(PolicyGroupInfo group:allDictList) {
				if(group.getGroupId().intValue()==groupId) {
					return group;
				}
			}
		}
		return null;		
	}
}
