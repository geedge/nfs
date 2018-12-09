package com.nis.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nis.domain.SysDataDictionaryItem;
import com.nis.domain.SysDataDictionaryName;
import com.nis.domain.basics.ServiceDictInfo;
import com.nis.domain.basics.SysDictInfo;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.PxyObjTrustedCaCrl;
import com.nis.domain.specific.SpecificServiceCfg;
import com.nis.web.dao.SysDictDao;
import com.nis.web.dao.basics.ServiceDictInfoDao;
import com.nis.web.dao.basics.SysDictInfoDao;
import com.nis.web.dao.configuration.PxyObjKeyringDao;
import com.nis.web.dao.configuration.WebsiteCfgDao;
import com.nis.web.dao.specific.SpecificServiceCfgDao;
import com.nis.web.service.SpringContextHolder;





/**
 * 系统数据字典工具类
 * @author Administrator
 *
 */
public class ConfigDictUtils {
	
	private final static ServiceDictInfoDao serviceDictInfoDao = SpringContextHolder.getBean(ServiceDictInfoDao.class);
	private final static SysDictInfoDao sysDictInfoDao = SpringContextHolder.getBean(SysDictInfoDao.class);
	private final static SpecificServiceCfgDao specificServiceCfgDao = SpringContextHolder.getBean(SpecificServiceCfgDao.class);
	private final static WebsiteCfgDao websiteCfgDao = SpringContextHolder.getBean(WebsiteCfgDao.class);
	private final static PxyObjKeyringDao pxyObjKeyringDao = SpringContextHolder.getBean(PxyObjKeyringDao.class);

	/**
	 * 根据主键查询配置详情
	 * @param serviceDictId
	 * @return
	 */
	public static ServiceDictInfo getServiceDictInfoById(Integer serviceDictId){
		return serviceDictInfoDao.getDictById(serviceDictId);
	}
	
	/**
	 * 根据主键查询配置详情
	 * @param sysDictId
	 * @return
	 */
	public static SysDictInfo getSysDictInfoById(Integer sysDictId){
		return sysDictInfoDao.getDictById(sysDictId);
	}
	
	/**
	 * 根据主键查询特定服务协议对象
	 * @param specServiceId
	 * @return
	 */
	public static SpecificServiceCfg getBySpecServiceId(Integer specServiceId){
		return specificServiceCfgDao.getBySpecServiceId(specServiceId);
	}
	/**
	 * 根据计算公式计算数据结果
	 * @param sysDictId
	 * @return
	 */
	public static Integer getNumberResult(Integer number1, Integer number2, Integer number3){
		
		return number1+(number2-1)*number3;
	}
	/**
	 * 根据数据类型编码取出数据类型标题国际化
	 * (2018-03-30修订获取的国际化改为类型的国际化)
	 * @param dictKey 数据的字典标识
	 * @param intArr
	 * @return
	 */
	public static List<String> getItemTypeByNo(String dictKey, List intArr){
		List<String> itemValueList = Lists.newArrayList();
		if(intArr==null||intArr.size()==0){
			List<SysDataDictionaryItem>  itemList = DictUtils.getDictList(dictKey);
			if(!StringUtil.isEmpty(itemList)){
				for(SysDataDictionaryItem sd:itemList){
					itemValueList.add(sd.getItemValue());
				}
			}
		}else{
			for(Object itemCode:intArr){
				itemValueList.add(DictUtils.getDictLabel(dictKey,itemCode.toString()));
			}
		}
		
		return itemValueList;
	}
	
	/**
	 * 根据数据类型编码取出权限标识码
	 * @param dictKey 数据的字典标识
	 * @param intArr
	 * @return
	 */
	public static String getPermissionByNo(String dictKey, List intArr){
		List<String> itemValueList = Lists.newArrayList();
		StringBuffer sb = new StringBuffer("");
		itemValueList=getItemTypeByNo(dictKey,intArr);
		if(!StringUtil.isEmpty(itemValueList)){
			for(String itermvalue:itemValueList){
				sb.append(itermvalue);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 根据数据类型标识取出数据类型的itemCode
	 * @param dictKey 数据的字典标识
	 * @param intArr
	 * @return
	 */
	public static List<Integer> getAllType(String dictKey){
		List<Integer> itemCodeList = Lists.newArrayList();
			List<SysDataDictionaryItem>  itemList = DictUtils.getDictList(dictKey);
			if(!StringUtil.isEmpty(itemList)){
				for(SysDataDictionaryItem sd:itemList){
					itemCodeList.add(Integer.valueOf(sd.getItemCode()));
				}
			}
		return itemCodeList;
	}

	/**
	 * 根据传入数据拆分数据类型参数
	 * @param dictKey 数据的字典标识
	 * @param itType 传入参数
	 * @return
	 */
	public static List<Integer> dealTypeCondition(String dictKey,String itType ){
		List<Integer> intArr = Lists.newArrayList();
		if(StringUtil.isEmpty(itType)){
			intArr = ConfigDictUtils.getAllType(dictKey);
		}else{
			 String[] strArr = itType.split("-");
			 Integer[] tempArr = new Integer[strArr.length];
			 for(int i=0;i<strArr.length;i++){
				tempArr[i] = Integer.valueOf(strArr[i]); 
			 }
			 intArr = Arrays.asList(tempArr);
		}
		return intArr;
	}
	/**
	 * 根据compileId获取配置内容
	 * @param compileId
	 * @return
	 */
	public static String getHttpBodyKeyword(Integer compileId ) {
		String httpBodyKeyword="";
		try {
			if(!StringUtil.isEmpty(compileId)){
				CfgIndexInfo entity=new CfgIndexInfo();
				entity.setCompileId(compileId);
				List httpBodyList=websiteCfgDao.getHttpBodyList(entity);
				if(!StringUtil.isEmpty(httpBodyList)){
					BaseStringCfg cfg=(BaseStringCfg)httpBodyList.get(0);
					httpBodyKeyword=StringUtil.isEmpty(cfg.getCfgKeywords()) ? "":cfg.getCfgKeywords()
													.replace(Constants.KEYWORD_EXPR, " ");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return httpBodyKeyword;
	}
	/**
	 * 根据compileId获取配置内容
	 * @param compileId
	 * @return
	 */
	public static String getTrustedCrlByCerId(Integer certId ) {
		String crlFIle="";
		try {
			if(!StringUtil.isEmpty(certId)){
				PxyObjTrustedCaCrl crl=new PxyObjTrustedCaCrl();
				crl.setCertId(certId);
				crl=pxyObjKeyringDao.getPxyObjTrustedCaCrl(crl);
				if(crl != null ){
					crlFIle=crl.getCrlFile();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return crlFIle;
	}
}
