package com.nis.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.nis.domain.FunctionServiceDict;

public class ServiceConfigTemplateUtil {

	private Logger logger = Logger.getLogger(getClass());

	private Node root;
	/**
	 * 配置文件内容
	 * @return
	 */
	public ServiceConfigTemplateUtil(){
		SAXReader reader = new SAXReader();
		org.dom4j.Document document = null;
		String configPath = "/service/service_config.xml";
		try {
			document = reader.read(ServiceConfigTemplateUtil.class.getResourceAsStream(configPath));
			root = document.getRootElement();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	/**
	 * 获取业务节点列表
	 * @return
	 */
	public List<Node> getServiceNodeList(){
		List<Node> nodes = root.selectNodes("service");
		return nodes;
	}
	/**
	 * 获取业务列表
	 * @return
	 */
	public List<Map<String,Object>> getServiceList(){
		List<Map<String,Object>> list =new ArrayList();
		List<Element> elements = root.selectNodes("service");
		for(Element element:elements){
			Map<String,Object> map = new HashMap();
			for(int i=0;i<element.attributes().size();i++){
				String attributeName = element.attribute(i).getName();
				map.put(attributeName, element.attributeValue(attributeName));
			}
			map.put("cfgList", getServiceCfgList(element));
			map.put("userRegionList",getUserRegionList(element));
			list.add(map);
		}
		return list;
	}
	/**
	 * 
	 * @param functionId
	 * @param serviceId
	 * @return
	 */
	public List<Map<String,Object>> getServiceList(Integer functionId){
		List<Map<String,Object>> list =new ArrayList();
		if(!StringUtil.isEmpty(functionId)) {
			List<FunctionServiceDict> serviceList = DictUtils.getFunctionServiceDictList(functionId);
			List<String> serviceIdList = new ArrayList();
			for (FunctionServiceDict service : serviceList) {
				if(!StringUtil.isEmpty(service.getServiceId())) {
					serviceIdList.add(service.getServiceId().toString());
				}
			}
			List<Element> elements = root.selectNodes("service");
			for(Element element:elements){
				String serviceIdC=element.attributeValue("id");
				if(!StringUtil.isEmpty(serviceIdC)
						&& !StringUtil.isEmpty(serviceIdList)
						&& serviceIdList.contains(serviceIdC)) {
					Map<String,Object> map = new HashMap();
					for(int i=0;i<element.attributes().size();i++){
						String attributeName = element.attribute(i).getName();
						
						map.put(attributeName, element.attributeValue(attributeName));
					}
					map.put("cfgList", getServiceCfgList(element));
					map.put("userRegionList",getUserRegionList(element));
					list.add(map);
				}
				
				
			}
		}
		return list;
	}
	
	/**
	 * 获取业务配置列表
	 * @return
	 */
	public List<Map<String,Object>> getServiceCfgList(Element serviceNode){
		List<Map<String,Object>> list = new ArrayList();
		List<Element> elements = serviceNode.selectNodes("serviceCfg");
		for(Element element:elements){
			Map<String,Object> map = new HashMap();
			for(int i=0;i<element.attributes().size();i++){
				map.put(element.attribute(i).getName(), element.attributeValue(element.attribute(i).getName()));
			}
			list.add(map);
		}
		return list;
	}
	/**
	 * 获取用户自定义域列表
	 * @return
	 */
	public List<Map<String,Object>> getUserRegionList(Element serviceNode){
		List<Map<String,Object>> list = new ArrayList();
		List<Element> elements = serviceNode.selectNodes("userRegion");
		for(Element element:elements){
			Map<String,Object> map = new HashMap();
			for(int i=0;i<element.attributes().size();i++){
				map.put(element.attribute(i).getName(), element.attributeValue(element.attribute(i).getName()));
			}
			list.add(map);
		}
		return list;
	}
	/**
	 * 根据标签获取配置属性列表
	 * @param tag,如多层标签，格式为/tag/subTag/...
	 * @param attribute
	 * @return
	 */
	public List getXmlParamListByTag(String tag,String attribute){
		List list =new ArrayList();
		List<Element> elements = root.selectNodes(tag);
		for(Element element:elements){
			list.add(element.attributeValue(attribute));
		}
		return list;
	}
	public static void main(String[] args) {
		try {
			ServiceConfigTemplateUtil serviceTemplate = new ServiceConfigTemplateUtil();
			List<Map<String,Object>> list = serviceTemplate.getServiceList(5);
			for(Map<String,Object> map :list){
				System.out.println("业务配置："+map.get("id")+","+map.get("functionId")+","+map.get("serviceType")+","+map.get("tableName"));
				List<Map<String,Object>> cfgList = (List<Map<String, Object>>) map.get("cfgList");
				List<Map<String,Object>> userRegionList = (List<Map<String, Object>>) map.get("userRegionList");
				if(cfgList!=null){
					for(Map<String,Object> m:cfgList){
						System.out.println("cfgList:"+m.get("cfgType")+","+m.get("tableName")+","+m.get("groupReuse")+","+m.get("groupId"));
					}
				}
				if(userRegionList!=null){
					for(Map<String,Object> n:userRegionList){
						System.out.println("userRegionList:"+n.get("regionKey")+","+n.get("regionColumn")+","+n.get("handleType"));
					}
				}
				
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
