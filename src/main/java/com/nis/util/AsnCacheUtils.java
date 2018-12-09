package com.nis.util;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.beust.jcommander.internal.Lists;
import com.nis.domain.specific.ConfigGroupInfo;
import com.nis.web.dao.specific.ConfigGroupInfoDao;
import com.nis.web.service.SpringContextHolder;

import jersey.repackaged.com.google.common.collect.Maps;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

/**
 * asn no缓存工具类
 * @author Administrator
 *
 */
public class AsnCacheUtils{
	private static Logger logger=Logger.getLogger(AsnCacheUtils.class);
	/**
	 * ASN号缓存
	 */
	private static final String ASN_NO_CACHE = "asnNoCache";
	private static final int cache_rage = 1000;
	private final static ConfigGroupInfoDao configGroupInfoDao = SpringContextHolder.getBean(ConfigGroupInfoDao.class);
	/**
	 * 获取缓存
	 * @param cacheName
	 * @param key
	 * @return
	 */
	public static ConfigGroupInfo get(Long key) {
		Element element = getCache(ASN_NO_CACHE).get(key/cache_rage);
		if(element!=null) {
			Map<Long,ConfigGroupInfo> map=(Map<Long,ConfigGroupInfo>)element.getObjectValue();
			if(map.containsKey(key)) {
				return map.get(key);
			}
		}
		return null;
	}
	public static Map<Long,ConfigGroupInfo> getMap(Object key) {
		Element element = getCache(ASN_NO_CACHE).get(key);
		return (Map<Long,ConfigGroupInfo>)element.getObjectValue();
	}
	public static void clearCache() {
		logger.warn("clear cache!");
		CacheUtils.getCacheManager().removeCache(ASN_NO_CACHE);
	}
	public static List<ConfigGroupInfo> getAllAsnGroup(){
		List<ConfigGroupInfo> configGroupInfos=Lists.newArrayList();
		Cache cache=getCache(ASN_NO_CACHE);
		for(Object key:cache.getKeys()) {
			Element element = getCache(ASN_NO_CACHE).get(key);
			if(element!=null) {
				Map<Long,ConfigGroupInfo> map=(Map<Long,ConfigGroupInfo>)element.getObjectValue();
				configGroupInfos.addAll(map.values());
			}
		}
		return configGroupInfos;
	}
	/**
	 * 
	 * 初始化缓存
	 *
	 * @param force是否强制刷新
	 */
	//@PostConstruct
	public synchronized static void init(boolean force) {
			long start=System.currentTimeMillis();
			logger.warn("AsnCacheUtils init start...");
			Cache cache=getCache(ASN_NO_CACHE);
			if(force) {
				List<ConfigGroupInfo> list=configGroupInfoDao.findAllList(4);
				Map<Long,Map<Long,ConfigGroupInfo>> groupMap=Maps.newHashMap();
				 for(ConfigGroupInfo configGroupInfo:list) {
					 if(groupMap.containsKey(configGroupInfo.getAsnId()/cache_rage)) {
						 groupMap.get(configGroupInfo.getAsnId()/cache_rage)
						 .put(configGroupInfo.getAsnId(), configGroupInfo);
					 }else {
						 Map<Long,ConfigGroupInfo> m=Maps.newHashMap();
						 m.put(configGroupInfo.getAsnId(), configGroupInfo);
						 groupMap.put(configGroupInfo.getAsnId()/cache_rage, m);
					 }
				 }
				 for(Entry<Long, Map<Long, ConfigGroupInfo>> e:groupMap.entrySet()) {
					 Element element = new Element(e.getKey(), e.getValue());
					 cache.put(element);
				 }
			}else {
				//查询总量
				Long count=configGroupInfoDao.getCountByType(4);
				boolean loadDatabase=false;
				if(cache.getKeys().size()==0) {
					loadDatabase=true;
				}else {
					long c=0l;
					for(Object key:cache.getKeys()) {
						c+=getMap(key).size();
					}
					if(c!=count) {
						loadDatabase=true;
					}
				}
				if(loadDatabase) {
					List<ConfigGroupInfo> list=configGroupInfoDao.findAllList(4);
					Map<Long,Map<Long,ConfigGroupInfo>> groupMap=Maps.newHashMap();
					 for(ConfigGroupInfo configGroupInfo:list) {
						 if(groupMap.containsKey(configGroupInfo.getAsnId()/cache_rage)) {
							 groupMap.get(configGroupInfo.getAsnId()/cache_rage)
							 .put(configGroupInfo.getAsnId(), configGroupInfo);
						 }else {
							 Map<Long,ConfigGroupInfo> m=Maps.newHashMap();
							 m.put(configGroupInfo.getAsnId(), configGroupInfo);
							 groupMap.put(configGroupInfo.getAsnId()/cache_rage, m);
						 }
					 }
					 for(Entry<Long, Map<Long, ConfigGroupInfo>> e:groupMap.entrySet()) {
						 Element element = new Element(e.getKey(), e.getValue());
						 cache.put(element);
					 }
				}
			}
			
			long end=System.currentTimeMillis();
			logger.warn("AsnCacheUtils init finish,cost:"+(end-start));
	}
	/**
	 * 写入ASN_NO_CACHE缓存
	 * @param key
	 * @return
	 */
	public static void put(Long key, ConfigGroupInfo value) {
		Long _key=key/cache_rage;
		Element element = getCache(ASN_NO_CACHE).get(_key);
		if(element==null) {
			Map<Long,ConfigGroupInfo> map=Maps.newHashMap();
			map.put(key, value);
			element = new Element(_key, map);
		}else {
			Map<Long,ConfigGroupInfo> map=(Map<Long,ConfigGroupInfo>)element.getObjectValue();
			map.put(key, value);
			element = new Element(_key, map);
		}
		getCache(ASN_NO_CACHE).put(element);
	}
	/**
	 * 从缓存中移除
	 * @param cacheName
	 * @param key
	 */
	public static void remove(String key) {
		getCache(ASN_NO_CACHE).remove(key);
	}
	public static void remove(Long key) {
		Long _key=key/cache_rage;
		Element element = getCache(ASN_NO_CACHE).get(_key);
		if(element!=null) {
			Map<Long,ConfigGroupInfo> map=(Map<Long,ConfigGroupInfo>)element.getObjectValue();
			if(map.containsKey(key)) {
				map.remove(key);
			}
			if(map.isEmpty()) {
				getCache(ASN_NO_CACHE).remove(_key);
			}else {
				element=new Element(_key,map);
				getCache(ASN_NO_CACHE).put(element);
			}
			
		}
	}
	private static Cache getCache(String cacheName){
		Cache cache = CacheUtils.getCacheManager().getCache(cacheName);
		if (cache == null){
			CacheUtils.getCacheManager().addCache(cacheName);
			cache = CacheUtils.getCacheManager().getCache(cacheName);
			cache.getCacheConfiguration().setEternal(true);
		}
		return cache;
	}
	public static String getCacheName() {
		return ASN_NO_CACHE;
	}
	
	
}