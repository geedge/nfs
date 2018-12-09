package com.nis.web.controller.basics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.zookeeper.ZooDefs.Ids;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nis.domain.Page;
import com.nis.domain.basics.ServiceDictInfo;
import com.nis.domain.configuration.RequestInfo;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.ConfigDictUtils;
import com.nis.util.Configurations;
import com.nis.util.StringUtil;
import com.nis.util.StringUtils;
import com.nis.web.controller.BaseController;

@Controller
@RequestMapping(value = "${adminPath}/basics/serviceDictInfo")
public class ServiceDictInfoController extends BaseController {
	

	@ModelAttribute
	public ServiceDictInfo get(@RequestParam(required=false) Integer serviceDictId) {
		if (!StringUtil.isEmpty(serviceDictId)){
			return serviceDictInfoService.getDictById(serviceDictId);
		}else{
			return new ServiceDictInfo();
		}
		
		
	}

	@ResponseBody
	@RequestMapping(value="requestCancleInfoAjax", method = RequestMethod.POST)
	public String requestCancleInfoAjax(Integer cancelRequestId,String ids,String indexTable){
		if(!StringUtil.isEmpty(ids)){
			String[] idArray = ids.split(",");
			for(String id :idArray){
				try {
					serviceDictInfoService.auditCancleRequestInfo(cancelRequestId,indexTable,id);
				} catch (Exception e) {
						e.printStackTrace();
						logger.error("审核添加取消来函信息失败",e);
						return  "false";
				}
			}
		}
		return  "true";
	}
	/**
	 * 
	 * 根据id获取取消审核时的来函号信息
	 * @param ids
	 * @param indexTable
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="requestCancleInfoNumber")
	public Map requestCancleInfoNumber(String ids,String indexTable){
		List list = new ArrayList();
		String requestTitle="";
		Map map=new HashMap();
		if(!StringUtil.isEmpty(ids)){
			String[] idArray = ids.split(",");
//			for(String id :idArray){
				try {
					Integer	cancelRequestId= serviceDictInfoService.requestCancleInfoNumber(indexTable.trim(),Long.parseLong(ids));
					if(cancelRequestId!=null) {
						requestTitle = requestInfoService.getRequestTitleById(cancelRequestId.longValue());
						if(StringUtils.isBlank(requestTitle)) {
							return map;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("查询取消审核来函信息失败",e);
					return  map;
				}
//			}
		}
		map.put("requestTitle", requestTitle);
		return map;
	}
	
	/**
	 * 查询业务辅助表-业务字典信息列表(无条件分页查询)
	 * （修改内容：添加查询数据总数，添加原则：不得修改原对象引用参数，不得影响原分页参数page数据）
	 * @param serviceDictInfo
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"basics:classification:view","basics:attribute:view","basics:label:view","basics:vpn_ip:view"},logical=Logical.OR)
	@RequestMapping(value = {"list", ""})
	public String list(String itType, ServiceDictInfo serviceDictInfo,HttpServletRequest request, HttpServletResponse response, Model model) {
		//处理数据
		List<Integer> intArr = ConfigDictUtils.dealTypeCondition(Configurations.getStringProperty("SERVICE_DICT_ITM_TYPE", ""), itType);
		String searchType = null;
		String searchContent = null;
		if(!StringUtils.isBlank(serviceDictInfo.getItemCode())){
			searchType = "itemCode";
			searchContent = serviceDictInfo.getItemCode();
		}
		if(!StringUtils.isBlank(serviceDictInfo.getItemValue())){
			searchType = "itemValue";
			searchContent = serviceDictInfo.getItemValue();
		}
		model.addAttribute("searchType", searchType);
		model.addAttribute("searchContent", searchContent);
		
		Page<ServiceDictInfo> pageCondition = new Page<ServiceDictInfo>(request, response);
		
		//查询符合条件总数
		List<ServiceDictInfo> allList = serviceDictInfoService.findAllServiceDictInfo(serviceDictInfo,intArr,pageCondition.getOrderBy());
		model.addAttribute("showTotalCount", allList.size());
		
		//查出顶层分页数据
		Page<ServiceDictInfo> page = serviceDictInfoService.findTopDictList(pageCondition, serviceDictInfo,intArr);	
		
		
		// 植入序号
		for (int i = 0; i < page.getList().size(); i++) {
			page.getList().get(i).setShowSequence("" + (i + 1 + ((page.getPageNo() - 1) * page.getPageSize())));
		}
		model.addAttribute("page", page);
		
		// 删除顶层数据、取出id 
		List<Integer> intList = Lists.newArrayList();
		
		for(ServiceDictInfo tempSe : page.getList()) {
			if (tempSe != null) {
				intList.add(tempSe.getServiceDictId());
			}
		}
		
		for (int i = allList.size() - 1; i >= 0; i--) {
			ServiceDictInfo se = allList.get(i);
			if(se!=null&&intList.contains(se.getServiceDictId())||(se!=null&&se.getParent().getServiceDictId()==0)){
				allList.remove(se);
			}
		}
		
		List<ServiceDictInfo> list = Lists.newArrayList();
		
		allList.addAll(page.getList());
		ServiceDictInfo.sortList(list,allList,0,true);
		
		//处理下级序号
		ServiceDictInfo.addChildrenSeq(list, 0);
		model.addAttribute("itType", itType);
		model.addAttribute("intArr", intArr);
		model.addAttribute("list", list);
		return "/basics/serviceDictList";
	}
	
	
	/**
	 * 进入添加或修改页面
	 * @param serviceDictInfo
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"basics:classification:add","basics:attribute:add","basics:vpn_ip:add","basics:label:add","basics:classification:edit","basics:attribute:edit","basics:vpn_ip:edit","basics:label:edit","basics:classification:view","basics:attribute:view","basics:label:view","basics:vpn_ip:view"},logical=Logical.OR)
	@RequestMapping(value={"form"})
	public String form(ServiceDictInfo serviceDictInfo, Model model,String doAction,String itType) {
		if(doAction!=null&&doAction.equals("0")){
			return "/basics/serviceDictInfo";
		}
		if (serviceDictInfo==null||serviceDictInfo.getParent() == null || serviceDictInfo.getParent().getServiceDictId() == null) {
			ServiceDictInfo parent = new ServiceDictInfo();
			parent.setServiceDictId(0);
			serviceDictInfo.setParent(parent);
		}
		List<Integer> intArr = ConfigDictUtils.dealTypeCondition(Configurations.getStringProperty("SERVICE_DICT_ITM_TYPE", ""), itType);
		model.addAttribute("intArr", intArr);
		model.addAttribute("serviceDictInfo", serviceDictInfo);
		model.addAttribute("itType", itType);
		return "/basics/serviceDictForm";
	}
	/**
	 * 新增或修改
	 * @param serviceDictInfo
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"basics:classification:add","basics:attribute:add","basics:label:add","basics:vpn_ip:add","basics:classification:edit","basics:attribute:edit","basics:label:edit","basics:vpn_ip:edit"},logical=Logical.OR)
	@RequestMapping(value = "saveOrUpdate")
	public String saveOrUpdate(ServiceDictInfo serviceDictInfo,Model model, RedirectAttributes redirectAttributes,String itType) {
		
		try {
			serviceDictInfoService.saveOrUpdate(serviceDictInfo);
			addMessage(redirectAttributes, "save_success");
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(redirectAttributes, "save_failed");
		}
		
		return "redirect:" + adminPath + "/basics/serviceDictInfo/list?itType="+itType;
		
		
	}
	
	
	/**
	 * 删除(删除时删除所有下级菜单)
	 * @param serviceDictInfo
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"basics:classification:del","basics:attribute:del","basics:label:del","basics:vpn_ip:del"},logical=Logical.OR)
	@RequestMapping(value={"delete"})
	public String delete(ServiceDictInfo serviceDictInfo, RedirectAttributes redirectAttributes, String itType, String mulitId) {
		try {
			serviceDictInfoService.deleteDict(mulitId);
			addMessage(redirectAttributes, "delete_success");
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(redirectAttributes, "delete_failed");
		}
		return "redirect:" + adminPath + "/basics/serviceDictInfo/list?itType="+itType;
	}
	
	
	/**
	 * isShowHide是否显示隐藏菜单
	 * @param extId
	 * @param isShowHidden
	 * @param response
	 * @param isShowLeaf是否查询叶子节点
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId,@RequestParam(required=false) boolean isShowLeaf,
			@RequestParam(required=false) String isShowHide, HttpServletResponse response, String itType) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		Map<String, Object> map2 = Maps.newHashMap();
		map2.put("id", 0);
		map2.put("pId", 0);
		map2.put("name","root_node");
		//map2.put("placeholder","0");
		mapList.add(map2);
		List<ServiceDictInfo> list = new ArrayList();
		
		if(isShowLeaf){
			if(itType.equals("1")){
				list = serviceDictInfoService.findFlDict();
			}else if(itType.equals("2")){
				list = serviceDictInfoService.findXzDict();
			}else{
				list = serviceDictInfoService.findLableDict();
			}
			
		}else{
			//找出该类型所有的非叶子配置
			list = serviceDictInfoService.findAllNoLeafDictList(itType);
		}
		
		for (int i=0; i<list.size(); i++){
			ServiceDictInfo serviceDictInfo = list.get(i);
			if (StringUtils.isBlank(extId) || (extId !=null && !extId.equals(serviceDictInfo.getServiceDictId().toString()))) {
				if(serviceDictInfo.getIsValid().equals(0)){
					continue;
				}
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", serviceDictInfo.getServiceDictId());
				map.put("pId", serviceDictInfo.getParent().getServiceDictId());
				map.put("name",serviceDictInfo.getItemValue());
				
				mapList.add(map);
			}
		}
		return mapList;
	}


	/**
	 * 校验itemCode是否重复
	 * @param itemCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "isItemCodeRepeat")
	public boolean isItemCodeRepeat(String itemCode,String oldItemCode) {
		List<ServiceDictInfo> list = Lists.newArrayList();
		if(oldItemCode!=null&&itemCode.trim().equals(oldItemCode.trim())){
			return true;
		}
		if(!StringUtil.isEmpty(itemCode)){
			list = serviceDictInfoService.findByItemCode(itemCode);
		}
		if(list==null||list.size()==0){
			return true;
		}
		return false;
	}
	
	/**
	 * 校验上下级配置数据类型
	 * @param parent 父级id
	 * param parent 子级类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "ajaxType")
	public boolean ajaxType(Integer parent,Integer child) {
		if(parent==-1||parent==0){
			return true;
		}
		ServiceDictInfo p = serviceDictInfoService.getDictById(parent);
		if(p.getItemType()==child){
			return true;
		}
		return false;
	}
	
	/**
	 * 校验更改数据类型后校验数据类型是否与下级冲突
	 * @param parent 父级id
	 * param parent 子级类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "ajaxChildrenType")
	public boolean ajaxChildrenType(Integer parent,Integer itemType) {
		if(parent==null){
			return true;
		}
		//比较下级任意一个对象
		List<ServiceDictInfo> list = serviceDictInfoService.getDictByParentId(parent);
		if(list==null||list.size()==0){
			return true;
		}else{
			for(ServiceDictInfo serviceDictInfo:list){
				if(serviceDictInfo.getItemType()==itemType){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 根据id取itemType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "ajaxItemType")
	public Integer ajaxItemType(Integer parent) {
		if(parent==0||parent==-1){
			return 0;
		}
		ServiceDictInfo p = serviceDictInfoService.getDictById(parent);
		
		return p.getItemType();
	}
	/**
	 * 校验叶子节点有下级不得更改为叶子节点
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "ajaxLeafChange")
	public boolean ajaxLeafChange(Integer parent,Integer newIsLeaf) {
		if(parent==null||parent==0){
			return true;
		}
		List<ServiceDictInfo> list = serviceDictInfoService.getDictByParentId(parent);
		if(list==null||list.size()==0){
			return true;
		}else{
			if(newIsLeaf==0){
            return true;
			}
		}
		return false;
	}
	
	/**
	 * 校验叶子节点无父级不得更改为叶子节点
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "ajaxLeafHasTree")
	public boolean ajaxLeafHasTree(Integer serviceDictId,Integer newIsLeaf,Integer parentId) {
		
		if(parentId==null||parentId==0||parentId==-1){
			if(newIsLeaf==0){
				return true;
			}
		}
		if(parentId!=null&&parentId!=0){
			return true;
		}
		return false;
	}
	
	/**
	 * 校验层级关系
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "isLevelTotalSure")
	public boolean isLevelTotalSure(Integer parentId,Integer currentId) {
		//获取层级配置
		int max = Integer.valueOf(Configurations.getIntProperty("maxLevelNo", 4));
		if(parentId==-1||parentId==0){
			return true;
		}else{
			ServiceDictInfo p = serviceDictInfoService.getDictById(parentId);
			if(currentId==null){
				if(p.getLevelNo()<max){
					return true;
				}
			}else{
				if(p.getLevelNo()<max){
					//查出该节点所有下级的层级数之和
					List<ServiceDictInfo> list = serviceDictInfoService.getDictByParentId(currentId);
					if(list==null||list.size()==0){
						return true;
					}
					List<ServiceDictInfo> resultList = Lists.newArrayList();
					List<Integer> intList = Lists.newArrayList();
					allTreeNode(1,list,resultList);
					for(ServiceDictInfo se:resultList){
						intList.add(se.getLevelNo());
					}
					int x = Collections.max(intList);
					if((p.getLevelNo()+x)<(max+1)){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 查出所该节点下有树形子节点
	 */
	 
	public void allTreeNode(Integer levelNo, List<ServiceDictInfo> list,List<ServiceDictInfo> resultList){
		if(list!=null&&list.size()>0){
			for(ServiceDictInfo se:list){
				se.setLevelNo(levelNo+1);
				resultList.add(se);
				List<ServiceDictInfo> newList = serviceDictInfoService.getDictByParentId(se.getServiceDictId());
				allTreeNode(se.getLevelNo(),newList,resultList);
			}
		}
	}
	
	
}
