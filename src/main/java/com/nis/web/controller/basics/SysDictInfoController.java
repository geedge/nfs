package com.nis.web.controller.basics;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nis.domain.Page;
import com.nis.domain.basics.ServiceDictInfo;
import com.nis.domain.basics.SysDictInfo;
import com.nis.util.ConfigDictUtils;
import com.nis.util.Configurations;
import com.nis.util.StringUtil;
import com.nis.util.StringUtils;
import com.nis.web.controller.BaseController;

@Controller
@RequestMapping(value = "${adminPath}/basics/sysDictInfo")
public class SysDictInfoController extends BaseController {
	

	@ModelAttribute
	public SysDictInfo get(@RequestParam(required=false) Integer sysDictId) {
		if (!StringUtil.isEmpty(sysDictId)){
			return sysDictInfoService.getDictById(sysDictId);
		}else{
			return new SysDictInfo();
		}
		
		
	}
	
	
	/**
	 * 查询业务辅助表-生效范围系统字典信息列表(无条件分页查询)
	 * @param sysDictInfo
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"basics:area:view","basics:isp:view","basics:scope:view"},logical=Logical.OR)
	@RequestMapping(value = {"list", ""})
	public String list(String itType, SysDictInfo sysDictInfo,HttpServletRequest request, HttpServletResponse response, Model model) {
		//处理数据
		List<Integer> intArr = ConfigDictUtils.dealTypeCondition(Configurations.getStringProperty("SYS_DICT_ITM_TYPE", ""), itType);
		if(intArr.size()==1){
			model.addAttribute("specType", intArr.get(0));
		}
		String searchType = null;
		String searchContent = null;
		if(!StringUtils.isBlank(sysDictInfo.getItemCode())){
			searchType = "itemCode";
			searchContent = sysDictInfo.getItemCode();
		}
		if(!StringUtils.isBlank(sysDictInfo.getItemValue())){
			searchType = "itemValue";
			searchContent = sysDictInfo.getItemValue();
		}
		model.addAttribute("searchType", searchType);
		model.addAttribute("searchContent", searchContent);
		
		Page<SysDictInfo> pageCondition = new Page<SysDictInfo>(request, response);
		
		//查询符合条件总数
		List<SysDictInfo> allList = sysDictInfoService.findAllSysDictInfo(sysDictInfo,intArr,pageCondition.getOrderBy());
		model.addAttribute("showTotalCount", allList.size());
		
		//查出顶层分页数据
		Page<SysDictInfo> page = sysDictInfoService.findTopDictList(pageCondition, sysDictInfo,intArr);
		//植入序号
		for(int i=0;i<page.getList().size();i++){
			page.getList().get(i).setShowSequence(""+(i+1+((page.getPageNo()-1)*page.getPageSize())));
		}
		model.addAttribute("page", page);
		
		//处理数据，取出主键
		List<Integer> intList = Lists.newArrayList();
		for(SysDictInfo se:page.getList()){
			if(se!=null){
				intList.add(se.getSysDictId());
			}
		}
		
		List<SysDictInfo> list = Lists.newArrayList();
		for(int i=allList.size()-1;i>=0;i--){
			SysDictInfo se = allList.get(i);
			if(se!=null&&intList.contains(se.getSysDictId())||(se!=null&&se.getParent().getSysDictId()==0)){
				allList.remove(se);
			 }	
		}
		allList.addAll(page.getList());
		SysDictInfo.sortList(list,allList,0,true);
		
		//处理下级序号
		SysDictInfo.addChildrenSeq(list, 0);
		model.addAttribute("itType", itType);
		model.addAttribute("intArr", intArr);
		model.addAttribute("list", list);
		return "/basics/sysDictList";
	}
	
	/**
	 * 进入添加或修改页面
	 * @param sysDictInfo
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"basics:area:add","basics:isp:add","basics:scope:add","basics:area:edit","basics:isp:edit","basics:scope:edit","basics:area:view","basics:isp:view","basics:scope:view"},logical=Logical.OR)
	@RequestMapping(value={"form"})
	public String form(SysDictInfo sysDictInfo, Model model, String doAction, String itType) {
		if(doAction!=null&&doAction.equals("0")){
			model.addAttribute("specType", sysDictInfo.getItemType());
			return "/basics/sysDictInfo";
		}
		if (sysDictInfo==null||sysDictInfo.getParent() == null || sysDictInfo.getParent().getSysDictId() == null) {
			SysDictInfo parent = new SysDictInfo();
			parent.setSysDictId(0);
			sysDictInfo.setParent(parent);
		}
		List<Integer> intArr = ConfigDictUtils.dealTypeCondition(Configurations.getStringProperty("SYS_DICT_ITM_TYPE", ""), itType);
		if(intArr.size()==1){
			model.addAttribute("specType", intArr.get(0));
		}
		model.addAttribute("intArr", intArr);
		model.addAttribute("sysDictInfo", sysDictInfo);
		model.addAttribute("itType", itType);
		return "/basics/sysDictForm";
	}
	/**
	 * 新增或修改
	 * @param sysDictInfo
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"basics:area:add","basics:isp:add","basics:scope:add","basics:area:edit","basics:isp:edit","basics:scope:edit"},logical=Logical.OR)
	@RequestMapping(value = "saveOrUpdate")
	public String saveOrUpdate(SysDictInfo sysDictInfo,Model model, RedirectAttributes redirectAttributes, String itType) {
		
		try {
			sysDictInfoService.saveOrUpdate(sysDictInfo);
			addMessage(redirectAttributes, "save_success");
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(redirectAttributes, "save_failed");
		}
		
		return "redirect:" + adminPath + "/basics/sysDictInfo/list?itType="+itType;
	}
	
	/**
	 * 删除
	 * @param sysDictInfo
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"basics:area:del","basics:isp:del","basics:scope:del"},logical=Logical.OR)
	@RequestMapping(value={"delete"})
	public String delete(SysDictInfo sysDictInfo, RedirectAttributes redirectAttributes, String itType, String mulitId) {
		try {
			sysDictInfoService.deleteDict(mulitId);
			addMessage(redirectAttributes, "delete_success");
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(redirectAttributes, "delete_failed");
		}
		return "redirect:" + adminPath + "/basics/sysDictInfo/list?itType="+itType;
	}
	
	
	/**
	 * isShowHide是否显示隐藏菜单
	 * @param extId
	 * @param isShowHidden
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId,@RequestParam(required=false) String isShowHide, HttpServletResponse response, String itType) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		Map<String, Object> map2 = Maps.newHashMap();
		map2.put("id", 0);
		map2.put("pId", 0);
		map2.put("name","root_node");
		//map2.put("placeholder","0");
		mapList.add(map2);
		//找出该类型所有的非叶子配置
		List<SysDictInfo> list = sysDictInfoService.findAllNoLeafDictList(itType);
		for (int i=0; i<list.size(); i++){
			SysDictInfo sysDictInfo = list.get(i);
			if (StringUtils.isBlank(extId) || (extId !=null && !extId.equals(sysDictInfo.getSysDictId().toString()))) {
				if(sysDictInfo.getIsValid().equals(0)){
					continue;
				}
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", sysDictInfo.getSysDictId());
				map.put("pId", sysDictInfo.getParent().getSysDictId());
				map.put("name",sysDictInfo.getItemValue());
				
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
	public boolean isItemCodeRepeat(String itemCode,String oldItemCode,String itType) {
		List<SysDictInfo> list = Lists.newArrayList();
		if(oldItemCode!=null&&itemCode.trim().equals(oldItemCode.trim())){
			return true;
		}
		if(!StringUtil.isEmpty(itemCode)){
			SysDictInfo dict=new SysDictInfo();
			dict.setItemCode(itemCode);
			dict.setItemType(Integer.valueOf(itType));
			list = sysDictInfoService.findByItemCode(dict);
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
		if(parent==null||parent==-1||parent==0){
			return true;
		}
		SysDictInfo p = sysDictInfoService.getDictById(parent);
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
		List<SysDictInfo> list = sysDictInfoService.getDictByParentId(parent);
		if(list==null||list.size()==0){
			return true;
		}else{
			for(SysDictInfo sysDictInfo:list){
				if(sysDictInfo.getItemType()==itemType){
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
		SysDictInfo p = sysDictInfoService.getDictById(parent);
		
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
		List<SysDictInfo> list = sysDictInfoService.getDictByParentId(parent);
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
	public boolean ajaxLeafHasTree(Integer sysDictId,Integer newIsLeaf,Integer parentId) {
		
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
			SysDictInfo p = sysDictInfoService.getDictById(parentId);
			if(currentId==null){
				if(p.getLevelNo()<max){
					return true;
				}
			}else{
				if(p.getLevelNo()<max){
					//查出该节点所有下级的层级数之和
					List<SysDictInfo> list = sysDictInfoService.getDictByParentId(currentId);
					if(list==null||list.size()==0){
						return true;
					}
					List<SysDictInfo> resultList = Lists.newArrayList();
					List<Integer> intList = Lists.newArrayList();
					allTreeNode(1,list,resultList);
					for(SysDictInfo se:resultList){
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

	private void allTreeNode(int levelNo, List<SysDictInfo> list, List<SysDictInfo> resultList) {
		if(list!=null&&list.size()>0){
			for(SysDictInfo se:list){
				se.setLevelNo(levelNo+1);
				resultList.add(se);
				List<SysDictInfo> newList = sysDictInfoService.getDictByParentId(se.getSysDictId());
				allTreeNode(se.getLevelNo(),newList,resultList);
			}
		}
		
	}
	
}
