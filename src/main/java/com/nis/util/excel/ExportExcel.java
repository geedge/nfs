/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.nis.util.excel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.nis.domain.FunctionRegionDict;
import com.nis.domain.FunctionServiceDict;
import com.nis.domain.SysDataDictionaryItem;
import com.nis.domain.basics.PolicyGroupInfo;
import com.nis.domain.basics.ServiceDictInfo;
import com.nis.domain.basics.SysDictInfo;
import com.nis.domain.configuration.DnsResStrategy;
import com.nis.util.Constants;
import com.nis.util.DictUtils;
import com.nis.util.Encodes;
import com.nis.util.Reflections;
import com.nis.util.StringUtil;
import com.nis.web.dao.basics.PolicyGroupInfoDao;
import com.nis.web.dao.basics.SysDictInfoDao;
import com.nis.web.dao.configuration.DnsResStrategyDao;
import com.nis.web.service.SpringContextHolder;

/**
 * 导出Excel文件（导出“XLSX”格式，支持大数据量导出   @see org.apache.poi.ss.SpreadsheetVersion）
 * @author ThinkGem
 * @version 2013-04-21
 */
public class ExportExcel {
	
	private static Logger log = LoggerFactory.getLogger(ExportExcel.class);
	
	private static DnsResStrategyDao dnsResStrategyDao = SpringContextHolder.getBean(DnsResStrategyDao.class);
	private static PolicyGroupInfoDao policyGroupInfoDao = SpringContextHolder.getBean(PolicyGroupInfoDao.class);
	private static SysDictInfoDao sysDictInfoDao = SpringContextHolder.getBean(SysDictInfoDao.class);
			
	/**
	 * 工作薄对象
	 */
	private SXSSFWorkbook wb;
	
	/**
	 * 工作表对象
	 */
	private Sheet sheet;
	
	/**
	 * 工作表对象
	 */
	private Map<String,Sheet> sheets;
	
	/**
	 * 样式列表
	 */
	private Map<String, CellStyle> styles;
	
	/**
	 * 当前行号
	 */
	private int rownum;
	
	/**
	 * 当前行号
	 */
	private Map<String, Integer> rownums;
	
	/**
	 * 注解列表（Object[]{ ExcelField, Field/Method }）
	 */
	List<Object[]> annotationList = Lists.newArrayList();
	Map<String, List<Object[]>> annotationMap=new HashMap<>(); 
	
	/**
	 * //递归获取cls实体对象及父级对象的属性
	 * wx:修改，子类覆盖父类的同名方法
	 * @param list
	 * @param cls
	 */
	public void getFields(List<Field> list,Class<?> cls) {
		Field[] fields=cls.getDeclaredFields();
		if(fields != null && fields.length > 0){
			List<Field> tempList=new ArrayList<>();
			for (Field field : fields) {
				if(list.size()==0) {
					tempList.add(field);
				}else {
					boolean has=false;
					for(Field checkF:list) {
						if(checkF.getName().equals(field.getName())) {
							has=true;
							break;
						}
					}
					if(!has) {
						tempList.add(field);
					}
				}
			}
			list.addAll(tempList);
		}
		if(cls.getSuperclass() != null){
			getFields(list,cls.getSuperclass());
		}
	}
	public String[] validRegionFieldAndSetComment(String headerStr,String commentStr,FunctionRegionDict region,FunctionServiceDict service,Properties msgProp){
		String defaultValue="";
		int index=0;
		String[] titleInfo=new String[3];
		//p2p_hash_type
		if("p2p_hash_type".equals(headerStr)){
			commentStr="";
			List<SysDataDictionaryItem> p2pHashType=DictUtils.getDictList("P2P_HASH_TYPE");
			if(p2pHashType !=null && p2pHashType.size()>0){
				for (SysDataDictionaryItem sysDataDictionaryItem : p2pHashType) {
					if(StringUtil.isEmpty(msgProp.getProperty(sysDataDictionaryItem.getItemValue()))){
						commentStr=commentStr+sysDataDictionaryItem.getItemCode()+"（"+sysDataDictionaryItem.getItemValue()+"）\n";
						index++;
					}else{
						commentStr=commentStr+sysDataDictionaryItem.getItemCode()+"（"+msgProp.getProperty(sysDataDictionaryItem.getItemValue())+"）\n";
						index++;
					}
				}
			}
			commentStr=msgProp.getProperty("select")+":\n"+commentStr;
			index++;
		}
		//p2p_ip_config_type
		if("p2p_ip_config_type".equals(headerStr)){
			commentStr="";
			List<SysDataDictionaryItem> p2pIpType=DictUtils.getDictList("P2P_IP_TYPE");
			if(p2pIpType !=null && p2pIpType.size()>0){
				for (SysDataDictionaryItem sysDataDictionaryItem : p2pIpType) {
					commentStr=commentStr+sysDataDictionaryItem.getItemCode()+"（"+msgProp.getProperty(sysDataDictionaryItem.getItemValue(),sysDataDictionaryItem.getItemValue())+"）\n";
					index++;
				}
				defaultValue=p2pIpType.get(0).getItemCode();
			}
			commentStr=msgProp.getProperty("select")+":\n"+commentStr;
			index++;
			index++;
			commentStr=commentStr+"\n"+msgProp.getProperty("rule_desc_tip")+":\n";
			index++;
			if(!StringUtil.isEmpty(defaultValue)){
				//1、默认值说明
				commentStr=commentStr+"▶"+msgProp.getProperty("default_value")+":"+defaultValue+"\n";
				index++;
			}

		}
		//ratelimit
		if("ratelimit".equals(headerStr)){
			commentStr="";
			List<SysDataDictionaryItem> ratelimit=DictUtils.getDictList("RATE_LIMIT");
			if(ratelimit !=null && ratelimit.size()>0){
				for (SysDataDictionaryItem sysDataDictionaryItem : ratelimit) {
						commentStr=commentStr+msgProp.getProperty(sysDataDictionaryItem.getItemValue(),sysDataDictionaryItem.getItemValue())+"\n";
						index++;
				}
				defaultValue=ratelimit.get(0).getItemCode();
			}
			commentStr=msgProp.getProperty("select")+":\n"+commentStr;
			index++;
			commentStr=commentStr+"\n"+msgProp.getProperty("rule_desc_tip")+":\n";
			index++;
			if(!StringUtil.isEmpty(defaultValue)){
				//1、默认值说明
				commentStr=commentStr+"▶"+msgProp.getProperty("default_value")+":"+defaultValue+"\n";
				index++;
				index++;
			}
		}
		/***发现内容***/
		if("replaced_content".equals(headerStr)){
			commentStr=commentStr+msgProp.getProperty("rule_desc_tip")+":\n";
			index++;
			//1、非空
			commentStr=commentStr+"▶"+msgProp.getProperty("required")+"\n";
			index++;
		}
		/***替换内容***/
		if("replace_content".equals(headerStr)){
			commentStr=commentStr+msgProp.getProperty("rule_desc_tip")+":\n";
			index++;
			//1、非空
			commentStr=commentStr+"▶"+msgProp.getProperty("required")+"\n";
			index++;
		}
		//intercept replace replace_zone
		if("replace_zone".equals(headerStr)){
			commentStr="";
			List<SysDataDictionaryItem> interceptReplaceZone=DictUtils.getDictList("INTERCEPT_REPLACE_ZONE");
			if(interceptReplaceZone !=null && interceptReplaceZone.size()>0){
				for (SysDataDictionaryItem sysDataDictionaryItem : interceptReplaceZone) {
					commentStr=commentStr+sysDataDictionaryItem.getItemCode()+"（"+msgProp.getProperty(sysDataDictionaryItem.getItemValue(),sysDataDictionaryItem.getItemValue())+"）\n";
					index++;
				}
				defaultValue=interceptReplaceZone.get(0).getItemCode();
			}
			commentStr=msgProp.getProperty("select")+":\n"+commentStr;
			index++;
			index++;
			commentStr=commentStr+"\n"+msgProp.getProperty("rule_desc_tip")+":\n";
			index++;
			if(!StringUtil.isEmpty(defaultValue)){
				//1、默认值说明
				commentStr=commentStr+"▶"+msgProp.getProperty("default_value")+":"+defaultValue+"\n";
				index++;
				index++;
			}
		}
		
		if("asn_no".equals(headerStr)){
			commentStr=commentStr+msgProp.getProperty("rule_desc_tip")+":\n";
			index++;
			//2、数值类型
			commentStr=commentStr+"▶"+msgProp.getProperty("input_integer")+"\n";
			index++;
		}
		if("policy_name".equals(headerStr)){
			if(region.getFunctionId().intValue()!=400) {//400 policy_name为DNS响应策略
				if(service.getAction().equals(1)){
					headerStr="";
					commentStr="";
				}else{
					List<DnsResStrategy> resStrategys=dnsResStrategyDao.findList(null, 1,1);
					commentStr=commentStr+"0"+"（"+msgProp.getProperty("no_strategy")+"）\n";
					index++;
					defaultValue="0";
					if(!StringUtil.isEmpty(resStrategys)){
						for (DnsResStrategy dnsResStrategy : resStrategys) {
							commentStr=commentStr+dnsResStrategy.getCfgId()+"（"+dnsResStrategy.getCfgDesc()+"）\n";
							index++;
						}
					}
				}
				if(StringUtil.isEmpty(headerStr)){
					headerStr="";
					commentStr="";
				}else{
					commentStr=msgProp.getProperty("select")+":\n"+commentStr;
					index++;
					index++;
					commentStr=commentStr+"\n"+msgProp.getProperty("rule_desc_tip")+":\n";
					index++;
					if(!StringUtil.isEmpty(defaultValue)){
						//2、默认值说明
						commentStr=commentStr+"▶"+msgProp.getProperty("default_value")+":"+defaultValue+"\n";
						index++;
					}
				}
			}
		}
		if("antiddos_protocol".equals(headerStr)) {
			commentStr="";
			List<SysDataDictionaryItem> antiddosProtocol=DictUtils.getDictList("ANTIDDOS_PROTOCOL");
			if(antiddosProtocol !=null && antiddosProtocol.size()>0){
				for (SysDataDictionaryItem sysDataDictionaryItem : antiddosProtocol) {
					if(StringUtil.isEmpty(msgProp.getProperty(sysDataDictionaryItem.getItemValue()))){
						commentStr=commentStr+sysDataDictionaryItem.getItemCode()+"（"+sysDataDictionaryItem.getItemValue()+"）\n";
						index++;
					}else{
						commentStr=commentStr+sysDataDictionaryItem.getItemCode()+"（"+msgProp.getProperty(sysDataDictionaryItem.getItemValue())+"）\n";
						index++;
					}
				}
			}
			commentStr=msgProp.getProperty("select")+":\n"+commentStr;
			index++;
		}
		if("bps_threadshold".equals(headerStr)){
			commentStr=commentStr+msgProp.getProperty("rule_desc_tip")+":\n";
			index++;
			commentStr=commentStr+msgProp.getProperty("input_integer")+"\n";
			index++;
		}
		if("pps_threadshold".equals(headerStr)){
			commentStr=commentStr+msgProp.getProperty("rule_desc_tip")+":\n";
			index++;
			commentStr=commentStr+msgProp.getProperty("input_integer")+"\n";
			index++;
		}
		if("group".equals(headerStr)) {
			commentStr="";
			//查询ip复用地址池配置的policyGroup列表
			List<PolicyGroupInfo> list=policyGroupInfoDao.findPolicyGroupInfosByType(2);
			if(!StringUtil.isEmpty(list)){
				for (PolicyGroupInfo policyGroupInfo : list) {
					commentStr=commentStr+policyGroupInfo.getGroupId()+"（"+policyGroupInfo.getGroupName()+"）\n";
					index++;
				}
			}
			commentStr=msgProp.getProperty("select")+":\n"+commentStr;
			index++;
		}
		if("isp".equals(headerStr)) {
			commentStr="";
			List<Integer> itTypeList = new ArrayList<Integer>();
			itTypeList.add(Constants.ITEM_TYPE_ISP);
			SysDictInfo sysDictInfo=new SysDictInfo();
			sysDictInfo.setConditionType(itTypeList);
			List<SysDictInfo> isps=sysDictInfoDao.findAllSysDictInfo(sysDictInfo,null);
			if(!StringUtil.isEmpty(isps)){
				for (SysDictInfo _sysDictInfo : isps) {
					commentStr=commentStr+_sysDictInfo.getItemCode()+"（"+_sysDictInfo.getItemValue()+"）\n";
					index++;
				}
			}
			commentStr=msgProp.getProperty("select")+":\n"+commentStr;
			index++;
		}
		if(region.getRegionType().equals(1)){//IP配置
			//ip配置需要导入的信息：srcIp srcPort destIp destPort Protocol Direction
			//确定需要导入的srcIp srcPort destIp destPort信息
			if(StringUtil.isEmpty(region.getConfigIpPortShow())){//无源目的ip和端口
				if("client_ip".equals(headerStr) 
						|| "server_ip".equals(headerStr)
						|| "client_port".equals(headerStr)
						|| "server_port".equals(headerStr)
						){
					headerStr="";
					commentStr="";
				}
			}else{
					if("client_ip".equals(headerStr)){
						if(((","+region.getConfigIpPortShow()+",").indexOf(",1,") > -1) ){
							commentStr="";
							if((","+region.getConfigIpType()+",").indexOf(",4,") > -1){
								if((","+region.getConfigIpPattern()+",").indexOf(",3,") > -1){
									commentStr=commentStr+Constants.IPV4_DEFAULT_IP_VALUE+"（IPv4）"+"\n";
									if(StringUtil.isEmpty(defaultValue)){
										defaultValue=Constants.IPV4_DEFAULT_IP_VALUE;
									}
									index++;
								}
								if((","+region.getConfigIpPattern()+",").indexOf(",2,") > -1){
									commentStr=commentStr+Constants.IPV4_DEFAULT_IP_RANGE_VALUE+"（"+msgProp.getProperty("ipv4_range_tip")+"）"+"\n";
									if(StringUtil.isEmpty(defaultValue)){
										defaultValue=Constants.IPV4_DEFAULT_IP_RANGE_VALUE;
									}
									index++;
								}
								if((","+region.getConfigIpPattern()+",").indexOf(",1,") > -1){
									commentStr=commentStr+Constants.IPV4_DEFAULT_IP_SUBNET_VALUE+"（"+msgProp.getProperty("ipv4_subnet_tip")+"）"+"\n";
									if(StringUtil.isEmpty(defaultValue)){
										defaultValue=Constants.IPV4_DEFAULT_IP_SUBNET_VALUE;
									}
									index++;
								}
							}
							if((","+region.getConfigIpType()+",").indexOf(",6,") > -1){
								if((","+region.getConfigIpPattern()+",").indexOf(",3,") > -1){
									commentStr=commentStr+Constants.IPV6_DEFAULT_IP_VALUE+"（IPv6）"+"\n";
									if(StringUtil.isEmpty(defaultValue)){
										defaultValue=Constants.IPV6_DEFAULT_IP_VALUE;
									}
									index++;
								}
								if((","+region.getConfigIpPattern()+",").indexOf(",2,") > -1){
									commentStr=commentStr+Constants.IPV6_DEFAULT_IP_RANGE_VALUE+"（"+msgProp.getProperty("ipv6_range_tip")+"）"+"\n";
									if(StringUtil.isEmpty(defaultValue)){
										defaultValue=Constants.IPV6_DEFAULT_IP_RANGE_VALUE;
									}
									index++;
								}
								if((","+region.getConfigIpPattern()+",").indexOf(",1,") > -1){
									commentStr=commentStr+Constants.IPV6_DEFAULT_IP_SUBNET_VALUE+"（"+msgProp.getProperty("ipv6_subnet_tip")+"）"+"\n\n";
									if(StringUtil.isEmpty(defaultValue)){
										defaultValue=Constants.IPV6_DEFAULT_IP_SUBNET_VALUE;
									}
									index++;
								}
							}
						}else{
							headerStr="";
							commentStr="";
						}
						if(StringUtil.isEmpty(commentStr)){
							headerStr="";
							commentStr="";
						}else{
							/**TODO
							 规则：
							index++;
							commentStr=msgProp.getProperty("example_tip")+":\n"+commentStr;
							index++;
							1、源ip和目的ip不能相等
							index++;
							2、源ip和目的ip格式必须一致
							index++;
							3、IPv4 Range 必须是C段IP
							index++;
							4、IPv4 Mask 掩码范围16-32
							index++;
							5、IPv6 Mask 掩码范围2-128
							index++;
							*/
							commentStr=msgProp.getProperty("example_tip")+":\n"+commentStr;
							index++;
							index++;
							commentStr=commentStr+msgProp.getProperty("rule_desc_tip")+":\n";
							index++;
							//1、默认值说明
							if(!StringUtil.isEmpty(defaultValue)){
								commentStr=commentStr+"▶"+msgProp.getProperty("default_value")+":"+defaultValue+"\n";
								index++;
							}
							//2、源IP和目的IP不能相同
							if(((","+region.getConfigIpPortShow()+",").indexOf(",1,") > -1) && ((","+region.getConfigIpPortShow()+",").indexOf(",3,") > -1)){
								commentStr=commentStr+"▶"+String.format(msgProp.getProperty("are_the_same")
										, msgProp.getProperty("clientip")
										,msgProp.getProperty("serverip"))+"\n";
								index++;
								index++;
								commentStr=commentStr+"▶"+String.format(msgProp.getProperty("the_same_ip_type_pattern"))+"\n";
								index++;
								index++;
							}
							//4、IP Range 开始IP和结束IP必须在同一网段
							if(((","+region.getConfigIpPattern()+",").indexOf(",2,") > -1)){
								commentStr=commentStr+"▶"+msgProp.getProperty("ip_range")+","+msgProp.getProperty("ip_range_bit_field")+"\n";
								index++;
								index++;
								//5、IP Range 开始IP必须小于结束IP
								commentStr=commentStr+"▶"+msgProp.getProperty("ip_range")+","+msgProp.getProperty("ip_range_smaller")+"\n";
								index++;
								index++;
							}
							
							//6、IPv4 Mask 掩码范围16-32
							if(((","+region.getConfigIpPattern()+",").indexOf(",1,") > -1)){
								if(((","+region.getConfigIpType()+",").indexOf(",4,") > -1)){
									commentStr=commentStr+"▶"+msgProp.getProperty("ipv4_subnet_tip")+","+msgProp.getProperty("ipv4_mask_range_tip")+"\n";
									index++;
									index++;
								}
								if(((","+region.getConfigIpType()+",").indexOf(",6,") > -1)){
									commentStr=commentStr+"▶"+msgProp.getProperty("ipv6_subnet_tip")+","+msgProp.getProperty("ipv6_mask_range_tip")+"\n";
									index++;
									index++;
								}
							}
						}
						
				}
				if("server_ip".equals(headerStr)){
					if((","+region.getConfigIpPortShow()+",").indexOf(",3,") > -1){
						commentStr="";
						if((","+region.getConfigIpType()+",").indexOf(",4,") > -1){
							if((","+region.getConfigIpPattern()+",").indexOf(",3,") > -1){
								commentStr=commentStr+Constants.IPV4_DEFAULT_IP_VALUE+"（IPv4）"+"\n";
								if(StringUtil.isEmpty(defaultValue)){
									defaultValue=Constants.IPV4_DEFAULT_IP_VALUE;
								}
								index++;
							}
							if((","+region.getConfigIpPattern()+",").indexOf(",2,") > -1){
								commentStr=commentStr+Constants.IPV4_DEFAULT_IP_RANGE_VALUE+"（"+msgProp.getProperty("ipv4_range_tip")+"）"+"\n";
								if(StringUtil.isEmpty(defaultValue)){
									defaultValue=Constants.IPV4_DEFAULT_IP_RANGE_VALUE;
								}
								index++;
							}
							if((","+region.getConfigIpPattern()+",").indexOf(",1,") > -1){
								commentStr=commentStr+Constants.IPV4_DEFAULT_IP_SUBNET_VALUE+"（"+msgProp.getProperty("ipv4_subnet_tip")+"）"+"\n";
								if(StringUtil.isEmpty(defaultValue)){
									defaultValue=Constants.IPV4_DEFAULT_IP_SUBNET_VALUE;
								}
								index++;
							}
						}
						if((","+region.getConfigIpType()+",").indexOf(",6,") > -1){
							if((","+region.getConfigIpPattern()+",").indexOf(",3,") > -1){
								commentStr=commentStr+Constants.IPV6_DEFAULT_IP_VALUE+"（IPv6）"+"\n";
								if(StringUtil.isEmpty(defaultValue)){
									defaultValue=Constants.IPV6_DEFAULT_IP_VALUE;
								}
								index++;
							}
							if((","+region.getConfigIpPattern()+",").indexOf(",2,") > -1){
								commentStr=commentStr+Constants.IPV6_DEFAULT_IP_RANGE_VALUE+"（"+msgProp.getProperty("ipv6_range_tip")+"）"+"\n";
								if(StringUtil.isEmpty(defaultValue)){
									defaultValue=Constants.IPV6_DEFAULT_IP_RANGE_VALUE;
								}
								index++;
							}
							if((","+region.getConfigIpPattern()+",").indexOf(",1,") > -1){
								commentStr=commentStr+Constants.IPV6_DEFAULT_IP_SUBNET_VALUE+"（"+msgProp.getProperty("ipv6_subnet_tip")+"）"+"\n\n";
								if(StringUtil.isEmpty(defaultValue)){
									defaultValue=Constants.IPV6_DEFAULT_IP_SUBNET_VALUE;
								}
								index++;
							}
						}
					}else{
						headerStr="";
						commentStr="";
					}
					if(StringUtil.isEmpty(commentStr)){
						headerStr="";
						commentStr="";
					}else{
						commentStr=msgProp.getProperty("example_tip")+":\n"+commentStr;
						index++;
						index++;
						commentStr=commentStr+msgProp.getProperty("rule_desc_tip")+":\n";
						index++;
						//1、默认值说明
						if(!StringUtil.isEmpty(defaultValue)){
							commentStr=commentStr+"▶"+msgProp.getProperty("default_value")+":"+defaultValue+"\n";
							index++;
						}
						//2、源IP和目的IP不能相同
						if(((","+region.getConfigIpPortShow()+",").indexOf(",1,") > -1) && ((","+region.getConfigIpPortShow()+",").indexOf(",3,") > -1)){
							commentStr=commentStr+"▶"+String.format(msgProp.getProperty("are_the_same")
									, msgProp.getProperty("clientip")
									,msgProp.getProperty("serverip"))+"\n";
							index++;
							index++;
							commentStr=commentStr+"▶"+String.format(msgProp.getProperty("the_same_ip_type_pattern"))+"\n";
							index++;
							index++;
						}
						//4、IP Range 开始IP和结束IP必须在同一网段
						if(((","+region.getConfigIpPattern()+",").indexOf(",2,") > -1)){
							commentStr=commentStr+"▶"+msgProp.getProperty("ip_range")+","+msgProp.getProperty("ip_range_bit_field")+"\n";
							index++;
							index++;
							//5、IP Range 开始IP必须小于结束IP
							commentStr=commentStr+"▶"+msgProp.getProperty("ip_range")+","+msgProp.getProperty("ip_range_smaller")+"\n";
							index++;
							index++;
						}
						
						//6、IPv4 Mask 掩码范围16-32
						if(((","+region.getConfigIpPattern()+",").indexOf(",1,") > -1)){
							if(((","+region.getConfigIpType()+",").indexOf(",4,") > -1)){
								commentStr=commentStr+"▶"+msgProp.getProperty("ipv4_subnet_tip")+","+msgProp.getProperty("ipv4_mask_range_tip")+"\n";
								index++;
								index++;
							}
							if(((","+region.getConfigIpType()+",").indexOf(",6,") > -1)){
								commentStr=commentStr+"▶"+msgProp.getProperty("ipv6_subnet_tip")+","+msgProp.getProperty("ipv6_mask_range_tip")+"\n";
								index++;
								index++;
							}
						}
					}
				}
				//4、协议
				String protocol=region.getConfigProtocol();
				if(region.getFunctionId().equals(5) ){
					//ip block tcp
					if(service.getAction().equals(16) ){
						protocol="6";
					}else{
						protocol=region.getConfigProtocol();
					}
				}else{
					protocol=region.getConfigProtocol();
				}
				if("client_port".equals(headerStr)){
					if(((","+region.getConfigIpPortShow()+",").indexOf(",2,") > -1)){
						commentStr="";
						if((","+region.getConfigPortPattern()+",").indexOf(",1,") > -1){
							commentStr=commentStr+Constants.PORT_DEFAULT+"（Port）"+"\n";
							if(StringUtil.isEmpty(defaultValue)){
								defaultValue=Constants.PORT_DEFAULT;
							}
							index++;
						}
						if((","+region.getConfigPortPattern()+",").indexOf(",2,") > -1){
							commentStr=commentStr+Constants.PORT_MASK_DEFAULT+"（Port/"+msgProp.getProperty("port_mask")+"）"+"\n\n";
							if(StringUtil.isEmpty(defaultValue)){
								defaultValue=Constants.PORT_MASK_DEFAULT;
							}
							index++;
						}
					}else{
						headerStr="";
						commentStr="";
					}
					if(StringUtil.isEmpty(commentStr)){
						headerStr="";
						commentStr="";
					}else{
						commentStr=msgProp.getProperty("example_tip")+":\n"+commentStr;
						index++;
						index++;
						commentStr=commentStr+msgProp.getProperty("rule_desc_tip")+":\n";
						index++;
						//1、默认值说明
						if(!StringUtil.isEmpty(defaultValue)){
							commentStr=commentStr+"▶"+msgProp.getProperty("default_value")+":"+defaultValue+"\n";
							index++;
						}
						//2、源端口、目的端口格式必须一致
						if(((","+region.getConfigIpPortShow()+",").indexOf(",2,") > -1) && ((","+region.getConfigIpPortShow()+",").indexOf(",4,") > -1)){
							commentStr=commentStr+"▶"+msgProp.getProperty("the_same_port_pattern")+"\n";
							index++;
							index++;
						}
						//3、端口和端口掩码范围0-65535
						commentStr=commentStr+"▶"+msgProp.getProperty("port_mask_comment_tip")+"\n";
						index++;
						index++;
						//protocol 非tcp 和 udp必须为o
						//协议大于1个，excel不会隐藏，需提示协议和端口的关系
						if(protocol.split(",").length > 1){
							if(((","+protocol+",").indexOf(",6,")>-1) ||((","+protocol+",").indexOf(",17,")>-1)){
								commentStr=commentStr+"▶"+msgProp.getProperty("port_comment_tip")+"\n";
								index++;
								index++;
								commentStr=commentStr+"▶"+msgProp.getProperty("protocol_and_port")+"\n";
								index++;
								index++;
							}else{
								commentStr=commentStr+"▶"+msgProp.getProperty("no_tc_udp_port_comment_tip")+"\n";
								index++;
								index++;
							}
						}else{
							if(((","+protocol+",").indexOf(",6,")>-1) ||((","+protocol+",").indexOf(",17,")>-1)){
								commentStr=commentStr+"▶"+msgProp.getProperty("port_comment_tip")+"\n";
								index++;
								index++;
							}else{
								commentStr=commentStr+"▶"+msgProp.getProperty("no_tc_udp_port_comment_tip")+"\n";
								index++;
								index++;
							}
						} 
						
					}
				}
				if("server_port".equals(headerStr)){
					if((","+region.getConfigIpPortShow()+",").indexOf(",4,") > -1){
						commentStr="";
						if((","+region.getConfigPortPattern()+",").indexOf(",1,") > -1){
							commentStr=commentStr+Constants.PORT_DEFAULT+"（Port）"+"\n";
							if(StringUtil.isEmpty(defaultValue)){
								defaultValue=Constants.PORT_DEFAULT;
							}
							index++;
						}
						if((","+region.getConfigPortPattern()+",").indexOf(",2,") > -1){
							commentStr=commentStr+Constants.PORT_MASK_DEFAULT+"（Port/"+msgProp.getProperty("port_mask")+"）"+"\n\n";
							if(StringUtil.isEmpty(defaultValue)){
								defaultValue=Constants.PORT_MASK_DEFAULT;
							}
							index++;
						}
					}else{
						headerStr="";
						commentStr="";
					}
					if(StringUtil.isEmpty(commentStr)){
						headerStr="";
						commentStr="";
					}else{
						commentStr=msgProp.getProperty("example_tip")+":\n"+commentStr;
						index++;
						index++;
						commentStr=commentStr+msgProp.getProperty("rule_desc_tip")+":\n";
						index++;
						//1、默认值说明
						if(!StringUtil.isEmpty(defaultValue)){
							commentStr=commentStr+"▶"+msgProp.getProperty("default_value")+":"+defaultValue+"\n";
							index++;
						}
						//2、源端口、目的端口格式必须一致
						if(((","+region.getConfigIpPortShow()+",").indexOf(",2,") > -1) && ((","+region.getConfigIpPortShow()+",").indexOf(",4,") > -1)){
							commentStr=commentStr+"▶"+msgProp.getProperty("the_same_port_pattern")+"\n";
							index++;
							index++;
						}
						//3、端口和端口掩码范围0-65535
						commentStr=commentStr+"▶"+msgProp.getProperty("port_mask_comment_tip")+"\n";
						index++;
						index++;
						//protocol 非tcp 和 udp必须为o
						//协议大于1个，excel不会隐藏，需提示协议和端口的关系
						if(protocol.split(",").length > 1){
							if(((","+protocol+",").indexOf(",6,")>-1) ||((","+protocol+",").indexOf(",17,")>-1)){
								commentStr=commentStr+"▶"+msgProp.getProperty("port_comment_tip")+"\n";
								index++;
								index++;
								commentStr=commentStr+"▶"+msgProp.getProperty("protocol_and_port")+"\n";
								index++;
								index++;
							}else{
								commentStr=commentStr+"▶"+msgProp.getProperty("no_tc_udp_port_comment_tip")+"\n";
								index++;
								index++;
							}
						}else{
							if(((","+protocol+",").indexOf(",6,")>-1) ||((","+protocol+",").indexOf(",17,")>-1)){
								commentStr=commentStr+"▶"+msgProp.getProperty("port_comment_tip")+"\n";
								index++;
								index++;
							}else{
								commentStr=commentStr+"▶"+msgProp.getProperty("no_tc_udp_port_comment_tip")+"\n";
								index++;
								index++;
							}
						} 
					}
				}
			} 
			//导入的Protocol
			if("protocol".equals(headerStr)){
				if(region.getFunctionId().equals(5) ){
					commentStr="";
					//ip block tcp
					if(service.getAction().equals(16) ){
						/**
						 *block 只有TCP不需要导入
						 * */
						headerStr="";
						commentStr="";
						/*List<SysDataDictionaryItem> protocol=DictUtils.getDictList("PROTOCOL");
						if(protocol !=null && protocol.size()>0){
							for (SysDataDictionaryItem sysDataDictionaryItem : protocol) {
								if(sysDataDictionaryItem.getItemCode().equals("6")){
									commentStr=commentStr+sysDataDictionaryItem.getItemCode()+"（"+sysDataDictionaryItem.getItemValue()+"）"+"\n";
									index++;
								}
							}
						}*/
						if(StringUtil.isEmpty(defaultValue)){
							defaultValue="6";
						}
					}else{
						commentStr="";
						List<SysDataDictionaryItem> protocol=DictUtils.getDictList("PROTOCOL");
						if(protocol !=null && protocol.size()>0){
							for (SysDataDictionaryItem sysDataDictionaryItem : protocol) {
								if((","+region.getConfigProtocol()+",").indexOf(","+sysDataDictionaryItem.getItemCode()+",") >-1){
									if(StringUtil.isEmpty(msgProp.getProperty(sysDataDictionaryItem.getItemValue()))){
										commentStr=commentStr+sysDataDictionaryItem.getItemCode()+"（"+sysDataDictionaryItem.getItemValue()+"）"+"\n";
										index++;
									}else{
										commentStr=commentStr+sysDataDictionaryItem.getItemCode()+"（"+msgProp.getProperty(sysDataDictionaryItem.getItemValue())+"）"+"\n";
										index++;
									}
									
								}
							}
							if(StringUtil.isEmpty(defaultValue)){
								if((","+region.getConfigProtocol()+",").indexOf("0") > -1){
									defaultValue="0";
								}else if((","+region.getConfigProtocol()+",").indexOf("6") > -1){
									defaultValue="6";
								}else if((","+region.getConfigProtocol()+",").indexOf("17") > -1){
									defaultValue="17";
								}else if((","+region.getConfigProtocol()+",").indexOf("17") > -1){
									defaultValue=region.getConfigProtocol().split(",")[0];
								}
								
								
							}
						}
					}
				}else{
					if(StringUtil.isEmpty(region.getConfigProtocol())){
						headerStr="";
						commentStr="";
					}else{
						commentStr="";
						List<SysDataDictionaryItem> protocol=DictUtils.getDictList("PROTOCOL");
						if(protocol !=null && protocol.size()>0){
							for (SysDataDictionaryItem sysDataDictionaryItem : protocol) {
								if((","+region.getConfigProtocol()+",").indexOf(","+sysDataDictionaryItem.getItemCode()+",") >-1){
									if(StringUtil.isEmpty(msgProp.getProperty(sysDataDictionaryItem.getItemValue()))){
										commentStr=commentStr+sysDataDictionaryItem.getItemCode()+"（"+sysDataDictionaryItem.getItemValue()+"）"+"\n";
										index++;
									}else{
										commentStr=commentStr+sysDataDictionaryItem.getItemCode()+"（"+msgProp.getProperty(sysDataDictionaryItem.getItemValue())+"）"+"\n";
										index++;
									}
									
								}
							}
						}
						if(StringUtil.isEmpty(defaultValue)){
							if((","+region.getConfigProtocol()+",").indexOf("0") > -1){
								defaultValue="0";
							}else if((","+region.getConfigProtocol()+",").indexOf("6") > -1){
								defaultValue="6";
							}else if((","+region.getConfigProtocol()+",").indexOf("17") > -1){
								defaultValue="17";
							}else if((","+region.getConfigProtocol()+",").indexOf("17") > -1){
								defaultValue=region.getConfigProtocol().split(",")[0];
							}
							
							
						}
					}
				}
				if(StringUtil.isEmpty(commentStr)
						||(region.getConfigProtocol().split(",").length==1)){
					headerStr="";
					commentStr="";
				}else{
					commentStr=msgProp.getProperty("select")+":\n"+commentStr;
					index++;
					index++;
					commentStr=commentStr+msgProp.getProperty("rule_desc_tip")+":\n";
					index++;
					//1、默认值说明
					if(!StringUtil.isEmpty(defaultValue)){
						commentStr=commentStr+"▶"+msgProp.getProperty("default_value")+":"+defaultValue+"\n";
						index++;
					}
				}
			}
			
			//Direction
			if("direction".equals(headerStr)){
				if(StringUtil.isEmpty(region.getConfigDirection())
						||(region.getConfigDirection().split(",").length==1)){
					headerStr="";
					commentStr="";
				}else{
					commentStr="";
					List<SysDataDictionaryItem> direction=DictUtils.getDictList("DIRECTION");
					if(direction !=null && direction.size()>0){
						for (SysDataDictionaryItem sysDataDictionaryItem : direction) {
							if(((","+region.getConfigDirection()+",").indexOf(sysDataDictionaryItem.getItemCode()) >-1) 
									&& StringUtil.isEmpty(msgProp.getProperty(sysDataDictionaryItem.getItemValue()))){
								commentStr=commentStr+sysDataDictionaryItem.getItemCode()+"（"+sysDataDictionaryItem.getItemValue()+"）"+"\n";
								index++;
							}else if((","+region.getConfigDirection()+",").indexOf(sysDataDictionaryItem.getItemCode()) >-1){
								commentStr=commentStr+sysDataDictionaryItem.getItemCode()+"（"+msgProp.getProperty(sysDataDictionaryItem.getItemValue())+"）"+"\n";
								index++;
							}
						}
						if(StringUtil.isEmpty(commentStr)){
							headerStr="";
							commentStr="";
						}
						if(StringUtil.isEmpty(defaultValue)){
							defaultValue="0";
						}
					}
				}
				if(StringUtil.isEmpty(commentStr)){
					headerStr="";
					commentStr="";
				}else{
					commentStr=msgProp.getProperty("select")+":\n"+commentStr;
					index++;
					index++;
					commentStr=commentStr+msgProp.getProperty("rule_desc_tip")+":\n";
					index++;
					//1、默认值说明
					if(!StringUtil.isEmpty(defaultValue)){
						commentStr=commentStr+"▶"+msgProp.getProperty("default_value")+":"+defaultValue+"\n";
						index++;
					}
				}
			}
			
		//字符串或摘要字符串
		}else if(region.getRegionType().equals(2) || region.getRegionType().equals(3)){
			if(region.getRegionType().equals(3)){
				if("district".equals(headerStr)){
					if(StringUtil.isEmpty(region.getConfigDistrict())
							||(region.getConfigDistrict().split(",").length==1)){
						headerStr="";
						commentStr="";
					}else{
						for (String districtStr : region.getConfigDistrict().split(",")) {
							commentStr=commentStr+districtStr+"\n";
							index++;
						}
						defaultValue=region.getConfigDistrict().split(",")[0];
					}
					if(StringUtil.isEmpty(commentStr)){
						headerStr="";
						commentStr="";
					}else{
						commentStr=msgProp.getProperty("select")+":\n"+commentStr;
						index++;
						index++;
						commentStr=commentStr+msgProp.getProperty("rule_desc_tip")+":\n";
						index++;
						//1、非空说明
						commentStr=commentStr+"▶"+msgProp.getProperty("required")+"\n";
						index++;
						//2、默认值说明
						if(!StringUtil.isEmpty(defaultValue)){
							commentStr=commentStr+"▶"+msgProp.getProperty("default_value")+":"+defaultValue+"\n";
							index++;
						}
					}
				}
			}
			//expr type
			if("key_word".equals(headerStr)){
				if(!StringUtil.isEmpty(region.getConfigMultiKeywords())){
					if(region.getConfigMultiKeywords().equals(1)){//多关键字输入TODO
						commentStr=commentStr+msgProp.getProperty("rule_desc_tip")+":\n";
						index++;
						//允许输入多关键字，多个关键字用换行表示
						commentStr=commentStr+"▶"+msgProp.getProperty("multi_keywords_tip")+"\n";
						index++;
						index++;
						//单个关键字不允许输入不可见字符
						commentStr=commentStr+"▶"+msgProp.getProperty("visible_keyword_tip")+"\n";
						index++;
						index++;
					}else{
						commentStr=commentStr+msgProp.getProperty("rule_desc_tip")+":\n";
						index++;
						index++;
						//只允许输入单个关键字
						commentStr=commentStr+"▶"+msgProp.getProperty("single_keyword_tip")+"\n";
						index++;
						index++;
						//单个关键字不允许输入不可见字符
						commentStr=commentStr+"▶"+msgProp.getProperty("visible_keyword_tip")+"\n";
						index++;
					}
					
				}
				
			}
			//expr type
			/*if("expression_type".equals(headerStr)){
				if(StringUtil.isEmpty(region.getConfigExprType())
						||(region.getConfigExprType().split(",").length==1)){
					headerStr="";
					commentStr="";
				}else{
					commentStr="";
					List<SysDataDictionaryItem> exprType=DictUtils.getDictList("EXPRESSION_TYPE");
					if(exprType !=null && exprType.size()>0){
						for (SysDataDictionaryItem sysDataDictionaryItem : exprType) {
							if(((","+region.getConfigExprType()+",").indexOf(sysDataDictionaryItem.getItemCode()) >-1) 
									&& StringUtil.isEmpty(msgProp.getProperty(sysDataDictionaryItem.getItemValue()))){
								commentStr=commentStr+sysDataDictionaryItem.getItemCode()+"（"+sysDataDictionaryItem.getItemValue()+"）"+"\n";
								index++;
							}else if((","+region.getConfigExprType()+",").indexOf(sysDataDictionaryItem.getItemCode()) >-1){
								commentStr=commentStr+sysDataDictionaryItem.getItemCode()+"（"+msgProp.getProperty(sysDataDictionaryItem.getItemValue())+"）"+"\n";
								index++;
							}
						}
						defaultValue=region.getConfigExprType().split(",")[0];
					}
				}
				if(StringUtil.isEmpty(commentStr)){
					headerStr="";
					commentStr="";
				}else{
					commentStr=msgProp.getProperty("select")+":\n"+commentStr;
					index++;
					index++;
					commentStr=commentStr+msgProp.getProperty("rule_desc_tip")+":\n";
					index++;
					//1、非空说明
					commentStr=commentStr+"▶"+msgProp.getProperty("required")+"\n";
					index++;
					//2、默认值说明
					if(!StringUtil.isEmpty(defaultValue)){
						commentStr=commentStr+"▶"+msgProp.getProperty("default_value")+":"+defaultValue+"\n";
						index++;
					}
				}
			}*/
			//match method
			if("match_method".equals(headerStr)){
				if(StringUtil.isEmpty(region.getConfigMatchMethod())
						||(region.getConfigMatchMethod().split(",").length==1)){
					headerStr="";
					commentStr="";
				}else{
					commentStr="";
					List<SysDataDictionaryItem> matchMethod=DictUtils.getDictList("MATCH_METHOD");
					if(matchMethod !=null && matchMethod.size()>0){
						for (SysDataDictionaryItem sysDataDictionaryItem : matchMethod) {
							if(((","+region.getConfigMatchMethod()+",").indexOf(sysDataDictionaryItem.getItemCode()) >-1) 
									&& StringUtil.isEmpty(msgProp.getProperty(sysDataDictionaryItem.getItemValue()))){
								commentStr=commentStr+sysDataDictionaryItem.getItemCode()+"（"+sysDataDictionaryItem.getItemValue()+"）"+"\n";
								index++;
							}else if((","+region.getConfigMatchMethod()+",").indexOf(sysDataDictionaryItem.getItemCode()) >-1){
								commentStr=commentStr+sysDataDictionaryItem.getItemCode()+"（"+msgProp.getProperty(sysDataDictionaryItem.getItemValue())+"）"+"\n";
								index++;
							}
						}
						if(StringUtil.isEmpty(defaultValue)){
							if((","+region.getConfigMatchMethod()+",").indexOf("0") > -1){
								defaultValue="0";
							}else{
								defaultValue=region.getConfigMatchMethod().split(",")[0];
							}
						}
					}
					if(StringUtil.isEmpty(commentStr)){
						headerStr="";
						commentStr="";
					}else{
						commentStr=msgProp.getProperty("select")+":\n"+commentStr;
						index++;
						index++;
						commentStr=commentStr+msgProp.getProperty("rule_desc_tip")+":\n";
						index++;
						//2、默认值说明
						if(!StringUtil.isEmpty(defaultValue)){
							commentStr=commentStr+"▶"+msgProp.getProperty("default_value")+":"+defaultValue+"\n";
							index++;
						}
					}
				}
			}
			//ishex
			if("is_hex".equals(headerStr)){
				List<String> list=new ArrayList<String>();
				if(StringUtil.isEmpty(region.getConfigHex())){
					headerStr="";
					commentStr="";
				}else{
					commentStr="";
					List<SysDataDictionaryItem> isHex=DictUtils.getDictList("IS_HEX");
					if(isHex !=null && isHex.size()>0){
						if(((","+region.getConfigHex()+",").indexOf(",1,") >-1)){
							for (SysDataDictionaryItem sysDataDictionaryItem : isHex) {
								if(sysDataDictionaryItem.getItemCode().equals("1")){
									commentStr=commentStr+sysDataDictionaryItem.getItemCode()+"（"+msgProp.getProperty(sysDataDictionaryItem.getItemValue())+"）"+"\n";
									list.add(sysDataDictionaryItem.getItemCode());
									index++;
								}
								if(region.getConfigHex().split(",").length>1){
									if(sysDataDictionaryItem.getItemCode().equals("0")){
										commentStr=commentStr+sysDataDictionaryItem.getItemCode()+"（"+msgProp.getProperty(sysDataDictionaryItem.getItemValue())+"）"+"\n";
										list.add(sysDataDictionaryItem.getItemCode());
										index++;
									}
								}
							}
						}else{
							for (SysDataDictionaryItem sysDataDictionaryItem : isHex) {
								if(sysDataDictionaryItem.getItemCode().equals("0")){
									commentStr=commentStr+sysDataDictionaryItem.getItemCode()+"（"+msgProp.getProperty(sysDataDictionaryItem.getItemValue())+"）"+"\n";
									list.add(sysDataDictionaryItem.getItemCode());
									index++;
								}
							}
						}
						
					}
				}
				if(StringUtil.isEmpty(commentStr) || (!StringUtil.isEmpty(list) && list.size()==1)){
					headerStr="";
					commentStr="";
				}else{
					commentStr=msgProp.getProperty("select")+":\n"+commentStr;
					index++;
					index++;
					commentStr=commentStr+msgProp.getProperty("rule_desc_tip")+":\n";
					index++;
					//2、默认值说明
					commentStr=commentStr+"▶"+msgProp.getProperty("default_value")+":0\n";
					index++;
				}
			}
			if("is_case_insenstive".equals(headerStr)){
				List<String> list=new ArrayList<String>();
				if(StringUtil.isEmpty(region.getConfigHex())){
					headerStr="";
					commentStr="";
				}else{
					commentStr="";
					List<SysDataDictionaryItem> isCaseSenstive=DictUtils.getDictList("CASE_INSENSTIVE");
					if(isCaseSenstive !=null && isCaseSenstive.size()>0){
						if(((","+region.getConfigHex()+",").indexOf(",0,") >-1)){
							for (SysDataDictionaryItem sysDataDictionaryItem : isCaseSenstive) {
								if(sysDataDictionaryItem.getItemCode().equals("0")){
									commentStr=commentStr+sysDataDictionaryItem.getItemCode()+"（"+msgProp.getProperty(sysDataDictionaryItem.getItemValue())+"）"+"\n";
									list.add(sysDataDictionaryItem.getItemCode());
									index++;
								}
								if(region.getConfigHex().split(",").length>1){
									if(sysDataDictionaryItem.getItemCode().equals("1")){
										commentStr=commentStr+sysDataDictionaryItem.getItemCode()+"（"+msgProp.getProperty(sysDataDictionaryItem.getItemValue())+"）"+"\n";
										list.add(sysDataDictionaryItem.getItemCode());
										index++;
									}
								}
							}
						}else{
							for (SysDataDictionaryItem sysDataDictionaryItem : isCaseSenstive) {
								if(sysDataDictionaryItem.getItemCode().equals("1")){
									commentStr=commentStr+sysDataDictionaryItem.getItemCode()+"（"+msgProp.getProperty(sysDataDictionaryItem.getItemValue())+"）"+"\n";
									list.add(sysDataDictionaryItem.getItemCode());
									index++;
								}
							}
						}
						
					}
				}
				if(StringUtil.isEmpty(commentStr) || (!StringUtil.isEmpty(list) && list.size()==1)){
					headerStr="";
					commentStr="";
				}else{
					commentStr=msgProp.getProperty("select")+":\n"+commentStr;
					index++;
					index++;
					commentStr=commentStr+msgProp.getProperty("rule_desc_tip")+":\n";
					index++;
					//2、默认值说明
					commentStr=commentStr+"▶"+msgProp.getProperty("default_value")+":0\n";
					index++;
				}
			}
		}else if(region.getRegionType().equals(6)) {
			if(region.getFunctionId().equals(400)) {
				if("group".equals(headerStr)){
					commentStr="";
					List<PolicyGroupInfo> list=policyGroupInfoDao.findPolicyGroupInfosByType(1);
					if(!StringUtil.isEmpty(list)){
						for (PolicyGroupInfo policyGroupInfo : list) {
							commentStr=commentStr+policyGroupInfo.getGroupId()+"（"+policyGroupInfo.getGroupName()+"）\n";
							index++;
						}
					}
					commentStr=msgProp.getProperty("select")+":\n"+commentStr;
					index++;
				}
				if("res_group_num".equals(headerStr)) {
					commentStr=msgProp.getProperty("input_integer")+":\n"+commentStr;
					index++;
				}
				if("min_ttl".equals(headerStr)) {
					commentStr="1-2";
					index++;
					commentStr=msgProp.getProperty("example_tip")+":\n"+commentStr;
					index++;
				}
			}
		}
		titleInfo[0]=headerStr;
		titleInfo[1]=commentStr;
		titleInfo[2]=index+"";
		return titleInfo;
	}
	/**
	 * //递归获取cls实体对象及父级对象的method
	 * @param list
	 * @param cls
	 */
	public void getMethods(List<Method> list,Class<?> cls) {
		Method[] methods=cls.getDeclaredMethods();
		if(methods != null && methods.length > 0){
			List<Method> tempList=new ArrayList<>();
			for (Method method : methods) {
				if(list.size()==0) {
					tempList.add(method);
				}else {
					boolean has=false;
					for(Method checkM:list) {
						if(checkM.getName().equals(method.getName())) {
							has=true;
							break;
						}
					}
					if(!has) {
						tempList.add(method);
					}
				}
			}
			list.addAll(tempList);
		}
		if(cls.getSuperclass() != null){
			getMethods(list,cls.getSuperclass());
		}
	}
	
	/**
	 * 构造函数
	 * @param title 表格标题，传“空值”，表示无标题
	 * @param cls 实体对象，通过annotation.ExportField获取标题
	 */
	public ExportExcel(String title, Class<?> cls){
		this(title, cls, 1);
	}
	/**
	 * 构造函数
	 * @param title 表格标题，传“空值”，表示无标题
	 * @param cls 实体对象，通过annotation.ExportField获取标题
	 * @param type 导出类型（1:导出数据；2：导出模板）
	 * @param groups 导入分组
	 */
	public ExportExcel(String title, Class<?> cls, int type, int... groups){
		List<Field> list=new ArrayList<Field>();
		// Get annotation field 
		//递归获取cls实体对象及父级对象的属性
		getFields(list, cls);
		
		if(!StringUtil.isEmpty(list)){
			for (Field f : list){
				ExcelField ef = f.getAnnotation(ExcelField.class);
				if (ef != null && (ef.type()==0 || ef.type()==type)){
					if (groups!=null && groups.length>0){
						boolean inGroup = false;
						for (int g : groups){
							if (inGroup){
								break;
							}
							for (int efg : ef.groups()){
								if (g == efg){
									inGroup = true;
									annotationList.add(new Object[]{ef, f});
									break;
								}
							}
						}
					}else{
						annotationList.add(new Object[]{ef, f});
					}
				}
			}
		}
		List<Method> ms=new ArrayList<Method>();
		// Get annotation method
		//递归获取cls实体对象及父级对象的属性
		getMethods(ms, cls);
		if(!StringUtil.isEmpty(ms)){
			for (Method m : ms){
				ExcelField ef = m.getAnnotation(ExcelField.class);
				if (ef != null && (ef.type()==0 || ef.type()==type)){
					if (groups!=null && groups.length>0){
						boolean inGroup = false;
						for (int g : groups){
							if (inGroup){
								break;
							}
							for (int efg : ef.groups()){
								if (g == efg){
									inGroup = true;
									annotationList.add(new Object[]{ef, m});
									break;
								}
							}
						}
					}else{
						annotationList.add(new Object[]{ef, m});
					}
				}
			}
		}
		// Field sorting
		Collections.sort(annotationList, new Comparator<Object[]>() {
			public int compare(Object[] o1, Object[] o2) {
				return new Integer(((ExcelField)o1[0]).sort()).compareTo(
						new Integer(((ExcelField)o2[0]).sort()));
			};
		});
		// Initialize
		List<String> headerList = Lists.newArrayList();
		List<String> commentList = Lists.newArrayList();
		for (Object[] os : annotationList){
			String titleStr = ((ExcelField)os[0]).title();
			String commentStr = ((ExcelField)os[0]).comment();
			// 如果是导出，则去掉注释
			if (type==1){
				commentStr="";
			}
			headerList.add(titleStr);
			commentList.add(commentStr);
		}
		initialize(title, headerList,commentList,null);
	}
	/**
	 * 构造函数
	 * @param FunctionRegionDict region信息
	 * @param msgProp 国际化配置
	 * @param title 表格标题，传“空值”，表示无标题
	 * @param cls 实体对象，通过annotation.ExportField获取标题
	 * @param type 导出类型（1:导出数据；2：导出模板）
	 * @param groups 导入分组
	 */
	public ExportExcel(FunctionServiceDict service,FunctionRegionDict region,Properties msgProp,String title, Class<?> cls, int type, int... groups){
		List<Field> list=new ArrayList<Field>();
		// Get annotation field 
		//递归获取cls实体对象及父级对象的属性
		getFields(list, cls);
		
		if(!StringUtil.isEmpty(list)){
			for (Field f : list){
				ExcelField ef = f.getAnnotation(ExcelField.class);
				if (ef != null && (ef.type()==0 || ef.type()==type)){
					if (groups!=null && groups.length>0){
						boolean inGroup = false;
						for (int g : groups){
							if (inGroup){
								break;
							}
							for (int efg : ef.groups()){
								if (g == efg){
									inGroup = true;
									annotationList.add(new Object[]{ef, f});
									break;
								}
							}
						}
					}else{
						annotationList.add(new Object[]{ef, f});
					}
				}
			}
		}
		List<Method> ms=new ArrayList<Method>();
		// Get annotation method
		//递归获取cls实体对象及父级对象的属性
		getMethods(ms, cls);
		if(!StringUtil.isEmpty(ms)){
			for (Method m : ms){
				ExcelField ef = m.getAnnotation(ExcelField.class);
				if (ef != null && (ef.type()==0 || ef.type()==type)){
					if (groups!=null && groups.length>0){
						boolean inGroup = false;
						for (int g : groups){
							if (inGroup){
								break;
							}
							for (int efg : ef.groups()){
								if (g == efg){
									inGroup = true;
									annotationList.add(new Object[]{ef, m});
									break;
								}
							}
						}
					}else{
						annotationList.add(new Object[]{ef, m});
					}
				}
			}
		}
		// Field sorting
		Collections.sort(annotationList, new Comparator<Object[]>() {
			public int compare(Object[] o1, Object[] o2) {
				return new Integer(((ExcelField)o1[0]).sort()).compareTo(
						new Integer(((ExcelField)o2[0]).sort()));
			};
		});
		// Initialize
		List<String> headerList = Lists.newArrayList();
		List<String> commentList = Lists.newArrayList();
		List<Integer> commentRowList = Lists.newArrayList();
		for (Object[] os : annotationList){
			String titleStr = ((ExcelField)os[0]).title();
			String commentStr = ((ExcelField)os[0]).comment();
			Integer commentRow = 0;
			
			// 如果是导出，则去掉注释
			if (type==1){
				commentStr="";
			}else{
				//判断此业务导出模板内容，设置导入提示信息
				String[] titleInfo=validRegionFieldAndSetComment(titleStr,commentStr,region,service,msgProp);
 				titleStr=titleInfo[0];
				commentStr=titleInfo[1];
				commentRow=StringUtil.isEmpty(titleInfo[2]) ? 0:Integer.parseInt(titleInfo[2]);
			}
			if(!StringUtil.isEmpty(titleStr)){
				headerList.add(msgProp.getProperty(titleStr)==null?titleStr:msgProp.getProperty(titleStr));
				commentList.add(commentStr);
				commentRowList.add(commentRow);
			}
			
		}
		initialize(title, headerList,commentList,commentRowList);
	}
	/**
	 * 构造函数
	 * @param msgProp 国际化配置
	 * @param title 表格标题，传“空值”，表示无标题
	 * @param cls 实体对象，通过annotation.ExportField获取标题
	 * @param type 导出类型（1:导出数据；2：导出模板）
	 * @param groups 导入分组
	 */
	public ExportExcel(Properties msgProp,String title, Class<?> cls, int type, int... groups){
		List<Field> list=new ArrayList<Field>();
		// Get annotation field 
		//递归获取cls实体对象及父级对象的属性
		getFields(list, cls);
		
		if(!StringUtil.isEmpty(list)){
			for (Field f : list){
				ExcelField ef = f.getAnnotation(ExcelField.class);
				if (ef != null && (ef.type()==0 || ef.type()==type)){
					if (groups!=null && groups.length>0){
						boolean inGroup = false;
						for (int g : groups){
							if (inGroup){
								break;
							}
							for (int efg : ef.groups()){
								if (g == efg){
									inGroup = true;
									annotationList.add(new Object[]{ef, f});
									break;
								}
							}
						}
					}else{
						annotationList.add(new Object[]{ef, f});
					}
				}
			}
		}
		List<Method> ms=new ArrayList<Method>();
		// Get annotation method
		//递归获取cls实体对象及父级对象的属性
		getMethods(ms, cls);
		if(!StringUtil.isEmpty(ms)){
			for (Method m : ms){
				ExcelField ef = m.getAnnotation(ExcelField.class);
				if (ef != null && (ef.type()==0 || ef.type()==type)){
					if (groups!=null && groups.length>0){
						boolean inGroup = false;
						for (int g : groups){
							if (inGroup){
								break;
							}
							for (int efg : ef.groups()){
								if (g == efg){
									inGroup = true;
									annotationList.add(new Object[]{ef, m});
									break;
								}
							}
						}
					}else{
						annotationList.add(new Object[]{ef, m});
					}
				}
			}
		}
		// Field sorting
		Collections.sort(annotationList, new Comparator<Object[]>() {
			public int compare(Object[] o1, Object[] o2) {
				return new Integer(((ExcelField)o1[0]).sort()).compareTo(
						new Integer(((ExcelField)o2[0]).sort()));
			};
		});
		// Initialize
		List<String> headerList = Lists.newArrayList();
		List<String> commentList = Lists.newArrayList();
		for (Object[] os : annotationList){
			String titleStr = ((ExcelField)os[0]).title();
			String commentStr = ((ExcelField)os[0]).comment();
			
			// 如果是导出，则去掉注释
			if (type==1){
				commentStr="";
			}
			if(!StringUtil.isEmpty(titleStr)){
				headerList.add(msgProp.getProperty(titleStr)==null?titleStr:msgProp.getProperty(titleStr));
				commentList.add(commentStr);
			}
			
		}
		initialize(title, headerList,commentList,null);
	}
	/**
	 * 构造函数
	 * @param msgProp 国际化配置
	 * @param title 表格标题，传“空值”，表示无标题
	 * @param cls 实体对象，通过annotation.ExportField获取标题
	 * @param type 导出类型（1:导出数据；2：导出模板）
	 * @param groups 导入分组
	 */
	public ExportExcel(String columns,Properties msgProp,String title, Class<?> cls, int type, int... groups){
		String[] cloumnArray=columns.split(",");
		List<Field> list=new ArrayList<Field>();
		// Get annotation field 
		//递归获取cls实体对象及父级对象的属性
		getFields(list, cls);
		
		if(!StringUtil.isEmpty(list)){
			for (Field f : list){
				ExcelField ef = f.getAnnotation(ExcelField.class);
				/*if(ef==null || StringUtil.isEmpty(ef.title())){
					break;
				}
				String titleP=StringUtil.isEmpty(msgProp.get(ef.title())) ? ef.title().toString():msgProp.get(ef.title()).toString();
				boolean export=false;
				for(String column:cloumnArray) {
					
					if(ef!=null&&column.equals(titleP)) {
						export=true;
						break;
					}
				}
				if(export) {*/
				if (ef != null && (ef.type()==0 || ef.type()==type)){
					if (groups!=null && groups.length>0){
						boolean inGroup = false;
						for (int g : groups){
							if (inGroup){
								break;
							}
							for (int efg : ef.groups()){
								if (g == efg){
									inGroup = true;
									annotationList.add(new Object[]{ef, f});
									break;
								}
							}
						}
					}else{
						annotationList.add(new Object[]{ef, f});
					}
				}
				/*}*/
			}
		}
		List<Method> ms=new ArrayList<Method>();
		// Get annotation method
		//递归获取cls实体对象及父级对象的属性
		getMethods(ms, cls);
		if(!StringUtil.isEmpty(ms)){
			for (Method m : ms){
				ExcelField ef = m.getAnnotation(ExcelField.class);
				boolean export=false;
				for(String column:cloumnArray) {
					if(ef!=null&&column.equals(ef.title())) {
						export=true;
						break;
					}
				}
				if(export) {
					if (ef != null && (ef.type()==0 || ef.type()==type)){
						if (groups!=null && groups.length>0){
							boolean inGroup = false;
							for (int g : groups){
								if (inGroup){
									break;
								}
								for (int efg : ef.groups()){
									if (g == efg){
										inGroup = true;
										annotationList.add(new Object[]{ef, m});
										break;
									}
								}
							}
						}else{
							annotationList.add(new Object[]{ef, m});
						}
					}
				}
			}
		}
		// Field sorting
		Collections.sort(annotationList, new Comparator<Object[]>() {
			public int compare(Object[] o1, Object[] o2) {
				return new Integer(((ExcelField)o1[0]).sort()).compareTo(
						new Integer(((ExcelField)o2[0]).sort()));
			};
		});
		// Initialize
		List<String> headerList = Lists.newArrayList();
		List<String> commentList = Lists.newArrayList();
		for (Object[] os : annotationList){
			String titleStr = ((ExcelField)os[0]).title();
			String commentStr = ((ExcelField)os[0]).comment();
			// 如果是导出，则去掉注释
			if (type==1){
				commentStr="";
			}
			headerList.add(msgProp.getProperty(titleStr)==null?titleStr:msgProp.getProperty(titleStr));
			commentList.add(commentStr);
		}
		initialize(title, headerList,commentList,null);
	}
	
	/**
	 * 构造函数
	 * @param title 表格标题，传“空值”，表示无标题
	 * @param headers 表头数组
	 */
	public ExportExcel(String title, String[] headers, String[] comments) {
		initialize(title, Lists.newArrayList(headers), Lists.newArrayList(comments),null);
	}
	
	/**
	 * 构造函数
	 * @param title 表格标题，传“空值”，表示无标题
	 * @param headerList 表头列表
	 * @param commentList 注释列表
	 */
	public ExportExcel(String title, List<String> headerList, List<String> commentList,List<Integer> commentRowList) {
		initialize(title, headerList,commentList,commentRowList);
	}
	
	
	/**
	 * 初始化函数
	 * @param title 表格标题，传“空值”，表示无标题
	 * @param headerList 表头列表
	 */
	private void initialize(String title, List<String> headerList, List<String> commentList,List<Integer> commentRowList) {
		this.wb = new SXSSFWorkbook(500);
		this.sheet = wb.createSheet("Export");
		this.styles = createStyles(wb);
		// Create title
		if (StringUtils.isNotBlank(title)){
			Row titleRow = sheet.createRow(rownum++);
			titleRow.setHeightInPoints(30);
			Cell titleCell = titleRow.createCell(0);
			titleCell.setCellStyle(styles.get("title"));
			titleCell.setCellValue(title);
			sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(),
					titleRow.getRowNum(), titleRow.getRowNum(), headerList.size()-1));
		}
		// Create header
		if (headerList == null){
			throw new RuntimeException("headerList not null!");
		}
		Row headerRow = sheet.createRow(rownum++);
		headerRow.setHeightInPoints(16);
		for (int i = 0; i < headerList.size(); i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellStyle(styles.get("header"));
			String  commentStr = commentList.get(i);
			Integer  commentRow =3;
			if(!StringUtil.isEmpty(commentRowList) 
					&& !StringUtil.isEmpty(commentRowList.get(i))
					&& commentRowList.get(i) > 0){
				commentRow=commentRowList.get(i);
			}
			if (!StringUtil.isEmpty(commentStr)){
				cell.setCellValue(headerList.get(i));
				Comment comment = this.sheet.createDrawingPatriarch().createCellComment(
						new XSSFClientAnchor(0, 0, 0, 0, (short) i, 0, (short) i+4, commentRow));
				comment.setString(new XSSFRichTextString(commentStr));
				cell.setCellComment(comment);
			}else{
				cell.setCellValue(headerList.get(i));
			}
			sheet.autoSizeColumn(i);
		}
		for (int i = 0; i < headerList.size(); i++) {  
			int colWidth = sheet.getColumnWidth(i)*2;
	        sheet.setColumnWidth(i, colWidth < 3000 ? 3000 : colWidth);  
		}
		log.debug("Initialize success.");
	}
	
	/**
	 * 创建表格样式
	 * @param wb 工作薄对象
	 * @return 样式列表
	 */
	private Map<String, CellStyle> createStyles(Workbook wb) {
		Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
		
		CellStyle style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		Font titleFont = wb.createFont();
		titleFont.setFontName("Arial");
		titleFont.setFontHeightInPoints((short) 16);
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(titleFont);
		styles.put("title", style);

		style = wb.createCellStyle();
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		Font dataFont = wb.createFont();
		dataFont.setFontName("Arial");
		dataFont.setFontHeightInPoints((short) 10);
		style.setFont(dataFont);
		styles.put("data", style);
		
		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		style.setAlignment(CellStyle.ALIGN_LEFT);
		styles.put("data1", style);

		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		style.setAlignment(CellStyle.ALIGN_CENTER);
		styles.put("data2", style);

		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		styles.put("data3", style);
		
		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
//		style.setWrapText(true);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Font headerFont = wb.createFont();
		headerFont.setFontName("Arial");
		headerFont.setFontHeightInPoints((short) 10);
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont.setColor(IndexedColors.WHITE.getIndex());
		style.setFont(headerFont);
		styles.put("header", style);
		
		return styles;
	}

	/**
	 * 添加一行
	 * @return 行对象
	 */
	public Row addRow(){
		return sheet.createRow(rownum++);
	}
	/**
	 * 添加一行
	 * @return 行对象
	 */
	public Row addRow(Sheet sheet,Integer rownum,String key){
		rownum++;
		rownums.put(key, rownum);
		return sheet.createRow(rownum);
	}
	

	/**
	 * 添加一个单元格
	 * @param row 添加的行
	 * @param column 添加列号
	 * @param val 添加值
	 * @return 单元格对象
	 */
	public Cell addCell(Row row, int column, Object val){
		return this.addCell(row, column, val, 0, Class.class);
	}
	
	/**
	 * 添加一个单元格
	 * @param row 添加的行
	 * @param column 添加列号
	 * @param val 添加值
	 * @param align 对齐方式（1：靠左；2：居中；3：靠右）
	 * @return 单元格对象
	 */
	public Cell addCell(Row row, int column, Object val, int align, Class<?> fieldType){
		Cell cell = row.createCell(column);
		CellStyle style = styles.get("data"+(align>=1&&align<=3?align:""));
		try {
			if (val == null){
				cell.setCellValue("");
			} else if (val instanceof String) {
				cell.setCellValue((String) val);
			} else if (val instanceof Integer) {
				cell.setCellValue((String) val);
			} else if (val instanceof Long) {
				cell.setCellValue((Long) val);
			} else if (val instanceof Double) {
				cell.setCellValue((Double) val);
			} else if (val instanceof Float) {
				cell.setCellValue((Float) val);
			} else if (val instanceof Date) {
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				cell.setCellValue(sdf.format(val));
			} else {
				if (fieldType != Class.class){
					cell.setCellValue((String)fieldType.getMethod("setValue", Object.class).invoke(null, val));
				}else{
					cell.setCellValue((String)Class.forName(this.getClass().getName().replaceAll(this.getClass().getSimpleName(), 
						"fieldtype."+val.getClass().getSimpleName()+"Type")).getMethod("setValue", Object.class).invoke(null, val));
				}
			}
		} catch (Exception ex) {
			log.info("Set cell value ["+row.getRowNum()+","+column+"] error: " + ex.toString());
			cell.setCellValue(val.toString());
		}
		cell.setCellStyle(style);
		return cell;
	}

	/**
	 * 添加数据（通过annotation.ExportField添加数据）
	 * @return list 数据列表
	 */
	public <E> ExportExcel setDataList(Properties msgProp,List<E> list,Map map){
			for (E e : list){
				int colunm = 0;
				Row row = this.addRow();
				StringBuilder sb = new StringBuilder();
				for (Object[] os : annotationList){
					ExcelField ef = (ExcelField)os[0];
					Object val = null;
					// Get entity value
					try{
						if (StringUtils.isNotBlank(ef.value())){
							val = Reflections.invokeGetter(e, ef.value());
						}else{
							if (os[1] instanceof Field){
								val = Reflections.invokeGetter(e, ((Field)os[1]).getName());
							}else if (os[1] instanceof Method){
								val = Reflections.invokeMethod(e, ((Method)os[1]).getName(), new Class[] {}, new Object[] {});
							}
						}
						// If is dict, get dict label 
						if (StringUtils.isNotBlank(ef.dictType())){
							String valStr=val==null?"":val.toString();
							if("type".equals(ef.dictType()) || "attribute".equals(ef.dictType()) 
									|| "label".equals(ef.dictType())){
								// Get basic info
								val = getBasicInfo(ef.dictType(),map,valStr);
							}else{
								//字典数据已做国际化处理
								String dict=DictUtils.getDictLabel(ef.dictType(), valStr, "");
								//如果找不到字典国际化值，把字典本身作为默认值放进去，不然导出就是空了
								val = msgProp.getProperty(dict,dict);
							}
							
						}
					}catch(Exception ex) {
						// Failure to ignore
						log.error("Get entity value failed",ex);
						val = "";
					}
					this.addCell(row, colunm++, val, ef.align(), ef.fieldType());
					sb.append(val + ", ");
				}
				log.debug("Write success: ["+row.getRowNum()+"] "+sb.toString());
			}
		return this;
	}
	/**
	 * 添加数据（通过annotation.ExportField添加数据）
	 * @return list 数据列表
	 */
	public <E> ExportExcel setDataList(Properties msgProp, Map<String,List> dataMap, Map<Object,Object> map){
		Set<String> keyList=dataMap.keySet();
		for (String key : keyList) {
			List<E> list=dataMap.get(key);
			for (E e : list){
				int colunm = 0;
				Row row = this.addRow(sheets.get(key),rownums.get(key),key);
				StringBuilder sb = new StringBuilder();
				for (Object[] os : annotationMap.get(key)){
					ExcelField ef = (ExcelField)os[0];
					Object val = null;
					// Get entity value
					try{
						if (StringUtils.isNotBlank(ef.value())){
							val = Reflections.invokeGetter(e, ef.value());
						}else{
							if (os[1] instanceof Field){
								val = Reflections.invokeGetter(e, ((Field)os[1]).getName());
							}else if (os[1] instanceof Method){
								val = Reflections.invokeMethod(e, ((Method)os[1]).getName(), new Class[] {}, new Object[] {});
							}
						}
						// If is dict, get dict label 
						if (StringUtils.isNotBlank(ef.dictType())){
							String valStr=val==null?"":val.toString();
							if("type".equals(ef.dictType()) || "attribute".equals(ef.dictType()) 
									|| "label".equals(ef.dictType())){
								// Get basic info
								val = getBasicInfo(ef.dictType(),map,valStr);
							}else{
								//字典数据已做国际化处理
								String dict=DictUtils.getDictLabel(ef.dictType(), valStr, "");
								//如果找不到字典国际化值，把字典本身作为默认值放进去，不然导出就是空了
								val = msgProp.getProperty(dict,dict);
							}
							
						}
						if(ef.title().equals("is_hex") && !StringUtil.isEmpty(val)){
							Integer isHex=Integer.parseInt(val.toString());
							if(isHex.equals(0) || isHex.equals(2)){
								val = msgProp.getProperty("not_hex","not_hex");
							}else if(isHex.equals(1)){
								val = msgProp.getProperty("hex","hex");
							}else{
								val ="";
							}
						}
						if(ef.title().equals("is_case_insenstive") && !StringUtil.isEmpty(val)){
							Integer isCaseSenstive=Integer.parseInt(val.toString());
							if(isCaseSenstive.equals(0) || isCaseSenstive.equals(1)){
								val = msgProp.getProperty("case_insenstive","case_insenstive");
							}else if(isCaseSenstive.equals(1)){
								val = msgProp.getProperty("case_senstive","case_senstive");
							}else{
								val ="";
							}
						}
					}catch(Exception ex) {
						// Failure to ignore
						log.error("Get entity value failed",ex);
						val = "";
					}
					this.addCell(row, colunm++, val, ef.align(), ef.fieldType());
					sb.append(val + ", ");
				}
				log.debug("Write success: ["+row.getRowNum()+"] "+sb.toString());
			}
		}
		return this;
	}
	/**
	 * 添加数据（通过annotation.ExportField添加数据）
	 * @return list 数据列表
	 */
	public <E> ExportExcel setDataList(String columns,Properties msgProp,List<E> list,Map map){
		if(StringUtils.isBlank(columns)) {
			return setDataList(msgProp,list,map);
		}else {
			Map<String,Boolean> exportMap=new HashMap<>();
			String[] columnArray=columns.split(",");
			for (Object[] os : annotationList){
				ExcelField ef = (ExcelField)os[0];
				for(String column:columnArray) {
					if(column.equals(ef.title())) {
						exportMap.put(column, true);
						break;
					}
				}
			}
			for (E e : list){
				int colunm = 0;
				Row row = this.addRow();
				StringBuilder sb = new StringBuilder();
				for (Object[] os : annotationList){
					ExcelField ef = (ExcelField)os[0];
					if(exportMap.containsKey(ef.title())) {
						Object val = null;
						// Get entity value
						try{
							
							if (StringUtils.isNotBlank(ef.value())){
								val = Reflections.invokeGetter(e, ef.value());
							}else{
								if (os[1] instanceof Field){
									val = Reflections.invokeGetter(e, ((Field)os[1]).getName());
								}else if (os[1] instanceof Method){
									val = Reflections.invokeMethod(e, ((Method)os[1]).getName(), new Class[] {}, new Object[] {});
								}
							}
							// If is dict, get dict label 
							if (StringUtils.isNotBlank(ef.dictType())){
								String valStr=val==null?"":val.toString();
								if("type".equals(ef.dictType()) || "attribute".equals(ef.dictType()) 
										|| "label".equals(ef.dictType())){
									// Get basic info
									val = getBasicInfo(ef.dictType(),map,valStr);
								}else{
									//字典数据已做国际化处理
									String dict=DictUtils.getDictLabel(ef.dictType(), valStr, "");
									//如果找不到字典国际化值，把字典本身作为默认值放进去，不然导出就是空了
									val = msgProp.getProperty(dict,dict);
								}
								
							}
						}catch(Exception ex) {
							// Failure to ignore
							log.error("Get entity value failed",ex);
							val = "";
						}
						this.addCell(row, colunm++, val, ef.align(), ef.fieldType());
						sb.append(val + ", ");
					}
				}
				log.debug("Write success: ["+row.getRowNum()+"] "+sb.toString());
			}
		}
		return this;
	}
	/**
	 * 设置性质、分类、标签等信息
	 * @param dictType
	 * @param map
	 * @param val
	 * @return
	 */
	public String getBasicInfo(String dictType,Map map,String val) {
		String basicInfo="";
		List<ServiceDictInfo> list=new ArrayList<ServiceDictInfo>();
		if("type".equals(dictType)){
			list=(List<ServiceDictInfo>) map.get("fls");
			for (String info : val.split(",")) {
				for (ServiceDictInfo dictInfo : list) {
					if(info.equals(dictInfo.getServiceDictId().toString())){
						basicInfo+=","+dictInfo.getItemValue();
					}
				}
			}
			
		}
		if("attribute".equals(dictType)){
			list=(List<ServiceDictInfo>) map.get("xzs");
			for (String info : val.split(",")) {
				for (ServiceDictInfo dictInfo : list) {
					if(info.equals(dictInfo.getServiceDictId().toString())){
						basicInfo+=","+dictInfo.getItemValue();
					}
				}
			}
		}
		if("label".equals(dictType)){
			list=(List<ServiceDictInfo>) map.get("labels");
			for (String info : val.split(",")) {
				for (ServiceDictInfo dictInfo : list) {
					if(info.equals(dictInfo.getServiceDictId().toString())){
						basicInfo+=","+dictInfo.getItemValue();
					}
				}
			}
		}
		if(!StringUtil.isEmpty(basicInfo)){
			basicInfo=basicInfo.substring(1);
		}
		return basicInfo;
	}
	
	
	
	
	
	/************************************************************/
	/**
	 * 构造函数
	 * @param msgProp 国际化配置
	 * @param title sheet名称
	 * @param cls 实体对象，通过annotation.ExportField获取标题
	 * @param type 导出类型（1:导出数据；2：导出模板）
	 * @param groups 导入分组
	 * @return 
	 */
	public ExportExcel(Properties msgProp,List<String> titleList,Map<String,String> noExportMap,Map<String, Class<?>> clsMap, int type, int... groups){
		Map<String, List<String>> headerMap=new HashMap<String, List<String>>();
		Map<String, List<String>> commentMap=new HashMap<String, List<String>>();
		for (String title : titleList) {
			String noExportField=noExportMap.get(title);
			List<Object[]> annotationList = Lists.newArrayList();
			List<Field> list=new ArrayList<Field>();
			// Get annotation field 
			//递归获取cls实体对象及父级对象的属性
			getFields(list, clsMap.get(title));
			if(!StringUtil.isEmpty(list)){
				for (Field f : list){
					ExcelField ef = f.getAnnotation(ExcelField.class);
					if (ef != null && (ef.type()==0 || ef.type()==type)){
						if (groups!=null && groups.length>0){
							boolean inGroup = false;
							for (int g : groups){
								if (inGroup){
									break;
								}
								for (int efg : ef.groups()){
									if (g == efg){
										inGroup = true;
										if(!StringUtil.isEmpty(ef.title()) && !(noExportField.indexOf(","+ef.title()+",") > -1)){
											annotationList.add(new Object[]{ef, f});
										}
										break;
									}
								}
							}
						}else{
							if(!StringUtil.isEmpty(ef.title()) && !(noExportField.indexOf(","+ef.title()+",") > -1)){
								annotationList.add(new Object[]{ef, f});
							}
						}
					}
				}
			}
			List<Method> ms=new ArrayList<Method>();
			// Get annotation method
			//递归获取cls实体对象及父级对象的属性
			getMethods(ms, clsMap.get(title));
			if(!StringUtil.isEmpty(ms)){
				for (Method m : ms){
					ExcelField ef = m.getAnnotation(ExcelField.class);
					if (ef != null && (ef.type()==0 || ef.type()==type)){
						if (groups!=null && groups.length>0){
							boolean inGroup = false;
							for (int g : groups){
								if (inGroup){
									break;
								}
								for (int efg : ef.groups()){
									if (g == efg){
										inGroup = true;
										if(!StringUtil.isEmpty(ef.title()) && !(noExportField.indexOf(","+ef.title()+",") > -1)){
											annotationList.add(new Object[]{ef, m});
										}
										break;
									}
								}
							}
						}else{
							if(!StringUtil.isEmpty(ef.title()) && !(noExportField.indexOf(","+ef.title()+",") > -1)){
								annotationList.add(new Object[]{ef, m});
							}
							
						}
					}
				}
			}
			// Field sorting
			Collections.sort(annotationList, new Comparator<Object[]>() {
				public int compare(Object[] o1, Object[] o2) {
					return new Integer(((ExcelField)o1[0]).sort()).compareTo(
							new Integer(((ExcelField)o2[0]).sort()));
				};
			});
			// Initialize
			List<String> headerList = Lists.newArrayList();
			List<String> commentList = Lists.newArrayList();
			for (Object[] os : annotationList){
				String titleStr = ((ExcelField)os[0]).title();
				String commentStr = ((ExcelField)os[0]).comment();
				
				// 如果是导出，则去掉注释
				if (type==1){
					commentStr="";
				}
				if(!StringUtil.isEmpty(titleStr)){
					//去掉不需要展示的header
					if(!(noExportField.indexOf(","+titleStr+",") > -1)){
						//需要替换的header
						boolean flag=true;
						if(noExportField.contains("&")){
							String replaceField=noExportField.substring(noExportField.indexOf("&")+1);
							String[] replaceStr=replaceField.split("-");
							for (int i = 0; i < replaceStr.length; i++) {
								String [] fields=replaceStr[i].split(":");
								if(fields[0].equals(titleStr)){
									titleStr=msgProp.getProperty(fields[1]);
									flag=false;
									break;
							 }
						   }
						}
						if(flag){
						   titleStr=msgProp.getProperty(titleStr)==null?titleStr:msgProp.getProperty(titleStr);
						}
						headerList.add(titleStr);
						commentList.add(commentStr);
					}
				}
			}
			headerMap.put(title, headerList);
			commentMap.put(title, commentList);
			annotationMap.put(title, annotationList);
		}
		initializeMultiSheet(msgProp,titleList, headerMap,commentMap);
	}
	
	/**
	 * 初始化函数
	 * @param title 表格标题，传“空值”，表示无标题
	 * @param headerList 表头列表
	 */
	private void initializeMultiSheet(Properties msgProp,List<String> titleList,Map<String, List<String>> headerMap,Map<String,List<String>> commentMap) {
		this.wb = new SXSSFWorkbook(500);
		int j=0;
		sheets=new HashMap<>();
		rownums=new HashMap<>();
		for (String title : titleList) {
			List<String> headerList=headerMap.get(title);
			List<String> commentList=commentMap.get(title);
			Sheet sheet = wb.createSheet(StringUtil.isEmpty(msgProp.get(title)) ? title:msgProp.get(title).toString());
			rownums.put(title, 0);
			sheets.put(title, sheet);
			this.styles = createStyles(wb);
			// Create header
			if (headerList == null){
				throw new RuntimeException("headerList not null!");
			}
			Integer rownum=rownums.get(title);
			Row headerRow = sheet.createRow(rownum++);
			headerRow.setHeightInPoints(16);
			for (int i = 0; i < headerList.size(); i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellStyle(styles.get("header"));
				String  commentStr = commentList.get(i);
				if (!StringUtil.isEmpty(commentStr)){
					cell.setCellValue(headerList.get(i));
					Comment comment = sheet.createDrawingPatriarch().createCellComment(
							new XSSFClientAnchor(0, 0, 0, 0, (short) i, 0, (short) i+4, 3));
					comment.setString(new XSSFRichTextString(commentStr));
					cell.setCellComment(comment);
				}else{
					cell.setCellValue(headerList.get(i));
				}
				sheet.autoSizeColumn(i);
			}
			for (int i = 0; i < headerList.size(); i++) {  
				int colWidth = sheet.getColumnWidth(i)*2;
				sheet.setColumnWidth(i, colWidth < 3000 ? 3000 : colWidth);  
			}
			j++;
		}
		log.debug("Initialize success.");
	}
	
	
	
	
	/**
	 * 输出数据流
	 * @param os 输出数据流
	 */
	public ExportExcel write(OutputStream os) throws IOException{
		wb.write(os);
		return this;
	}
	
	/**
	 * 输出到客户端
	 * @param fileName 输出文件名
	 */
	public ExportExcel write(HttpServletResponse response, String fileName) throws IOException{
		response.reset();
        response.setContentType("application/octet-stream; charset=utf-8");
        if(!StringUtil.isEmpty(fileName)){
        	fileName=fileName.replace(" ", "_");
        }else{
        	fileName="export.xlsx";
        }
        response.setHeader("Content-Disposition", "attachment; filename=\""+fileName+"\"");
		write(response.getOutputStream());
		return this;
	}
	
	/**
	 * 输出到文件
	 * @param fileName 输出文件名
	 */
	public ExportExcel writeFile(String name) throws FileNotFoundException, IOException{
		FileOutputStream os = new FileOutputStream(name);
		this.write(os);
		return this;
	}
	/**
	 * 输出到客户端
	 * @param fileName 输出文件名
	 */
	public ExportExcel write(HttpServletRequest request,HttpServletResponse response, String fileName) throws IOException{
		
		if(!StringUtil.isEmpty(fileName)){
        	fileName=fileName.replace(" ", "_");
        }else{
        	fileName="export.xlsx";
        }
		final String userAgent = request.getHeader("USER-AGENT");  
        String finalFileName = null;  
        if(StringUtils.contains(userAgent, "MSIE")){//IE浏览器  
	        finalFileName = Encodes.urlEncode(fileName);  
	    }else if(StringUtils.contains(userAgent, "Mozilla")){//google,火狐浏览器  
	        finalFileName = new String(fileName.getBytes(), "ISO8859-1");  
	    }else{  
	        finalFileName = Encodes.urlEncode(fileName);//其他浏览器  
	    }
		
		response.reset();
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename="+finalFileName);
		write(response.getOutputStream());
		return this;
	}
	/**
	 * 清理临时文件
	 */
	public ExportExcel dispose(){
		wb.dispose();
		return this;
	}
	
	/**
	 * 导出测试
	 */
	public static void main(String[] args) throws Throwable {
		
		/*List<String> headerList = Lists.newArrayList();
		for (int i = 1; i <= 10; i++) {
			headerList.add("表头"+i);
		}
		
		List<String> dataRowList = Lists.newArrayList();
		for (int i = 1; i <= headerList.size(); i++) {
			dataRowList.add("数据"+i);
		}
		
		List<List<String>> dataList = Lists.newArrayList();
		for (int i = 1; i <=1000000; i++) {
			dataList.add(dataRowList);
		}

		ExportExcel ee = new ExportExcel("表格标题", headerList);
		
		for (int i = 0; i < dataList.size(); i++) {
			Row row = ee.addRow();
			for (int j = 0; j < dataList.get(i).size(); j++) {
				ee.addCell(row, j, dataList.get(i).get(j));
			}
		}
		
		ee.writeFile("target/export.xlsx");

		ee.dispose();
		
		log.debug("Export success.");
		*/
	}

}
