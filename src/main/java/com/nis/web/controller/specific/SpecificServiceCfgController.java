package com.nis.web.controller.specific;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nis.domain.Page;
import com.nis.domain.SysDataDictionaryItem;
import com.nis.domain.specific.SpecificServiceCfg;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.Constants;
import com.nis.util.DictUtils;
import com.nis.util.StringUtils;
import com.nis.web.controller.BaseController;

@Controller
@RequestMapping(value = "${adminPath}/specific/specificServiceCfg")
public class SpecificServiceCfgController extends BaseController {

/*	@ModelAttribute
	public SpecificServiceCfg get(@RequestParam(required=false)Integer specServiceId) {
		if (!StringUtil.isEmpty(specServiceId)) {
			SpecificServiceCfg ss = specificServiceCfgService.getBySpecServiceId(specServiceId);
			return ss==null?new SpecificServiceCfg():ss;
		} else {
			return new SpecificServiceCfg();
		}
	}*/

	/**
	 * 查出分页列表
	 * 
	 * @param specificServiceCfg
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("specific:service:view")
	@RequestMapping(value = "list")
	public String list(SpecificServiceCfg specificServiceCfg, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		String searchType = null;
		String searchContent = null;
		if(specificServiceCfg.getSpecServiceCode()!=null){
			searchType = "specServiceCode";
			searchContent = specificServiceCfg.getSpecServiceCode().toString();
		}
		if(!StringUtils.isBlank(specificServiceCfg.getSpecServiceName())){
			searchType = "specServiceName";
			searchContent = specificServiceCfg.getSpecServiceName();
		}
		if(!StringUtils.isBlank(specificServiceCfg.getSpecServiceDesc())){
			searchType = "specServiceDesc";
			searchContent = specificServiceCfg.getSpecServiceDesc();
		}
		if(specificServiceCfg.getGroupId()!=null){
			searchType = "groupId";
			searchContent = specificServiceCfg.getGroupId().toString();
		}
		if(specificServiceCfg.getCfgType()==null){
			specificServiceCfg.setCfgType(1);
		}
		model.addAttribute("searchType", searchType);
		model.addAttribute("searchContent", searchContent);
		
		Page<SpecificServiceCfg> pageCondition = new Page<SpecificServiceCfg>(request, response);
		// 取出所有符合条件的数据
		List<SpecificServiceCfg> allList = specificServiceCfgService.findAllSpecificServiceCfg(specificServiceCfg,pageCondition.getOrderBy());
		model.addAttribute("showTotalCount", allList.size());
		// 取出所有符合条件的顶层分页
		Page<SpecificServiceCfg> page = specificServiceCfgService
				.findTopPage(pageCondition, specificServiceCfg);
		// 删除顶层数据、取出id 
		List<Integer> intList = Lists.newArrayList();
		// 植入序号
		for (int i = 0; i < page.getList().size(); i++) {
			if (page.getList().get(i) != null) {
				page.getList().get(i).setShowSequence("" + (i + 1 + ((page.getPageNo() - 1) * page.getPageSize())));
				intList.add(page.getList().get(i).getSpecServiceId());
			}
		}
		for (int i = allList.size() - 1; i >= 0; i--) {
			SpecificServiceCfg ss = allList.get(i);
			if ((ss != null && intList.contains(ss.getSpecServiceId())) || (ss != null && ss.getParent().getSpecServiceId() == 0)) {
				allList.remove(ss);
			}
		}
		allList.addAll(page.getList());
		
		List<SpecificServiceCfg> list = Lists.newArrayList();
		SpecificServiceCfg.sortList(list, allList, 0, true);
		//处理下级序号
		SpecificServiceCfg.addChildrenSeq(list, 0);
		model.addAttribute("page", page);
		model.addAttribute("list", list);

		return "/specific/specificServiceCfgList";
	}

	/**
	 * 进入查看或新增、修改页面
	 * 
	 * @param specificServiceCfg
	 * @param model
	 * @param doAction
	 * @param mulitId
	 * @return
	 */
	@RequiresPermissions(value={"specific:service:add","specific:service:edit","specific:service:view"},logical=Logical.OR)
	@RequestMapping(value = { "form" })
	public String form(SpecificServiceCfg specificServiceCfg, Model model, String doAction) {
		if(specificServiceCfg!=null&&specificServiceCfg.getSpecServiceId()!=null){
			specificServiceCfg = specificServiceCfgService.getBySpecServiceId(specificServiceCfg.getSpecServiceId());
		}
		if (specificServiceCfg == null || specificServiceCfg.getSpecServiceId()==null) {
			SpecificServiceCfg parent = new SpecificServiceCfg();
			parent.setSpecServiceId(0);
			specificServiceCfg.setParent(parent);
			specificServiceCfg.setParentType(0);
		}
		if(specificServiceCfg!=null&&specificServiceCfg.getParent().getSpecServiceId()!=null){//获取父配置的id
			specificServiceCfg.setParentType(specificServiceCfgService.getParentType(specificServiceCfg.getParent().getSpecServiceId()));
		}
		model.addAttribute("specificServiceCfg", specificServiceCfg);
		if (doAction != null && doAction.equals("0")) {
			return "/specific/specificServiceCfgInfo";
		}
		return "/specific/specificServiceCfgForm";
	}

	/**
	 * 新增或保存
	 * @param specificServiceCfg
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value= {"specific:service:add","specific:service:edit"},logical=Logical.OR)
	@RequestMapping(value="saveOrUpdate")
	public String saveOrUpdate(SpecificServiceCfg specificServiceCfg, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			specificServiceCfgService.saveOrUpdate(specificServiceCfg);
			addMessage(redirectAttributes, "save_success");
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(redirectAttributes, "save_failed");
		}
		return "redirect:" + adminPath + "/specific/specificServiceCfg/list?cfgType="+specificServiceCfg.getCfgType();
	}
	
	/**
	 * 删除
	 * @param specificServiceCfg
	 * @param redirectAttributes
	 * @param mulitId
	 * @return
	 */
	@RequiresPermissions("specific:service:del")
	@RequestMapping(value="delete")
	public String delete(SpecificServiceCfg specificServiceCfg, RedirectAttributes redirectAttributes, String mulitId){
		try{
			specificServiceCfgService.delete(mulitId);
			addMessage(redirectAttributes,"delete_success");
		}catch (Exception e){
			e.printStackTrace();
			addMessage(redirectAttributes,"delete_failed");
		}
		
		return "redirect:"+adminPath+"/specific/specificServiceCfg/list";
	}
	
	/**
	 * 父节点选择树形结构
	 * @param extId排除节点id
	 * @param isShowHide
	 * @param isLeafShow:叶子节点是否显示
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false)Boolean specific,@RequestParam(required=false) String extId,@RequestParam(required=false) String isShowHide, 
			@RequestParam(required=false)boolean isLeafShow,@RequestParam(required=true)Integer cfgType,HttpServletResponse response){
		List<Map<String, Object>> mapList = Lists.newArrayList();
		Map<String, Object> map2 = Maps.newHashMap();
		map2.put("id", 0);
		map2.put("pId", 0);
		map2.put("serviceType",0);
		map2.put("business",-2);
		map2.put("groupId",0);
		map2.put("name","root_node");
		//map2.put("placeholder","0");
		mapList.add(map2);
		if(specific==null||!specific) {
			Properties props=this.getMsgProp();
			List<SysDataDictionaryItem>  businessTypeList=Lists.newArrayList();
			if(cfgType==1) {
				businessTypeList=DictUtils.getDictList("APP_BUSINESS_TYPE");
			}else if(cfgType==3) {
				businessTypeList=DictUtils.getDictList("BASIC_PROTOCOL_BUSINESS_TYPE");
			}
			List<Map<String, Object>> businessList = Lists.newArrayList();
			//对业务分类进行排序
			Collections.sort(businessTypeList, new Comparator<SysDataDictionaryItem>() {
				@Override
				public int compare(SysDataDictionaryItem o1, SysDataDictionaryItem o2) {
					// TODO Auto-generated method stub
					Integer o1Code=new Integer(o1.getItemCode());
					Integer o2Code=new Integer(o2.getItemCode());
					return o2Code.compareTo(o1Code);
				}
			});
			for(SysDataDictionaryItem dict:businessTypeList) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", "businessType"+dict.getItemCode());
				map.put("pId", 0);
				map.put("name",props.getProperty(dict.getItemValue(), dict.getItemValue()));
				map.put("serviceType",cfgType);
				map.put("businessType","-1");
				map.put("groupId",0);
				businessList.add(map);
			}
			mapList.addAll(businessList);
			List<SpecificServiceCfg> list = specificServiceCfgService.findAllSpecificServiceCfg(new SpecificServiceCfg(),"");
			for (int i=0; i<list.size(); i++){
				SpecificServiceCfg specificServiceCfg = list.get(i);
				if(StringUtils.isBlank(extId)||(extId!=null&&!extId.equals(specificServiceCfg.getSpecServiceId().toString()))){
					if(specificServiceCfg.getIsValid().equals(0)||
							(!isLeafShow && specificServiceCfg.getIsLeaf().equals(1))||
							(cfgType.intValue()!=0&&specificServiceCfg.getCfgType().intValue()!=cfgType)){
						continue;
					}
					if(businessTypeList.size()>0) {
						boolean hasBusinessType=false;
						for(SysDataDictionaryItem business:businessTypeList) {
							if(String.valueOf(business.getItemCode()).equals(specificServiceCfg.getBusinessType())) {
								Map<String, Object> map = Maps.newHashMap();
								map.put("id", specificServiceCfg.getSpecServiceId());
								map.put("pId", "businessType"+business.getItemCode());
								map.put("name",specificServiceCfg.getSpecServiceName());
								map.put("serviceType",specificServiceCfg.getCfgType());
								map.put("serviceCode",specificServiceCfg.getSpecServiceCode());
								map.put("businessType",specificServiceCfg.getBusinessType());
								map.put("groupId",specificServiceCfg.getGroupId());
								if(specificServiceCfg.getAddFlag()!=null) {
									map.put("user",specificServiceCfg.getAddFlag());
								}
								mapList.add(map);
								hasBusinessType=true;
								break;
							}
						}
						if(!hasBusinessType) {//没有对应的业务类别
							Map<String, Object> map = Maps.newHashMap();
							map.put("id", specificServiceCfg.getSpecServiceId());
							map.put("pId", specificServiceCfg.getParent().getSpecServiceId());
							map.put("name",specificServiceCfg.getSpecServiceName());
							map.put("serviceType",specificServiceCfg.getCfgType());
							map.put("serviceCode",specificServiceCfg.getSpecServiceCode());
							map.put("businessType",specificServiceCfg.getBusinessType());
							map.put("groupId",specificServiceCfg.getGroupId());
							if(specificServiceCfg.getAddFlag()!=null) {
								map.put("user",specificServiceCfg.getAddFlag());
							}
							mapList.add(map);
						}
					}else {
						Map<String, Object> map = Maps.newHashMap();
						map.put("id", specificServiceCfg.getSpecServiceId());
						map.put("pId", specificServiceCfg.getParent().getSpecServiceId());
						map.put("name",specificServiceCfg.getSpecServiceName());
						map.put("serviceType",specificServiceCfg.getCfgType());
						map.put("groupId",specificServiceCfg.getGroupId());
						mapList.add(map);
					}
				}
			}
		}else {
			List<SpecificServiceCfg> list = specificServiceCfgService.findAllSpecificServiceCfg(new SpecificServiceCfg(),"");
			for (int i=0; i<list.size(); i++){
				SpecificServiceCfg specificServiceCfg = list.get(i);
				if(StringUtils.isBlank(extId)||(extId!=null&&!extId.equals(specificServiceCfg.getSpecServiceId().toString()))){
					if(specificServiceCfg.getIsValid().equals(0)||
							(!isLeafShow && specificServiceCfg.getIsLeaf().equals(1))||
							(cfgType.intValue()!=0&&specificServiceCfg.getCfgType().intValue()!=cfgType)){
						continue;
					}
					Map<String, Object> map = Maps.newHashMap();
					map.put("id", specificServiceCfg.getSpecServiceId());
					map.put("pId", specificServiceCfg.getParent().getSpecServiceId());
					map.put("name",specificServiceCfg.getSpecServiceName());
					map.put("type",specificServiceCfg.getCfgType());
					map.put("groupId",specificServiceCfg.getGroupId());
					if(specificServiceCfg.getAddFlag()!=null) {
						map.put("user",specificServiceCfg.getAddFlag());
					}
					mapList.add(map);
				}
			}
		}
//		map2.put("nodes", businessList);
		return mapList;
	}
	/**
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "childrenList")
	public List<Map<String, Object>> childrenList(@RequestParam(required=false)Integer parent,HttpServletResponse response){
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<SpecificServiceCfg> list = specificServiceCfgService.getChildrenById(parent);
		for (int i=0; i<list.size(); i++){
			Map<String, Object> map = Maps.newHashMap();
			SpecificServiceCfg specificServiceCfg = list.get(i);
			if(specificServiceCfg.getIsLeaf().equals(1)){
				map.put("code", specificServiceCfg.getSpecServiceCode());
				map.put("pId", specificServiceCfg.getParent().getSpecServiceId());
				map.put("name",specificServiceCfg.getSpecServiceName());
				map.put("type",specificServiceCfg.getCfgType());
				mapList.add(map);
			}
		}
		return mapList;
	}
	/**
	 * 校验协议id是否重复
	 * @param specServiceId
	 * @param oldId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "isCodeNotRepeat")
	public boolean isIdRepeat(@RequestParam(required=false,value="parentId")Integer parentId,@RequestParam(required=false,value="specServiceId")Integer specServiceId,@RequestParam(required=true,value="specServiceCode")Integer specServiceCode,@RequestParam(required=true,value="cfgType")Integer cfgType){
		if(parentId!=null&&parentId.intValue()!=0) {
			Integer parentCode=specificServiceCfgService.getParentCode(parentId);
			if(parentCode!=null&&parentCode.intValue()==specServiceCode) {
				return false;
			}
		}
		if(specServiceId!=null){
			SpecificServiceCfg ss = specificServiceCfgService.getBySpecServiceId(Integer.valueOf(specServiceId));
		    if(ss!=null){
		    	if(ss.getSpecServiceCode().intValue()==specServiceCode.intValue()&&cfgType.intValue()==ss.getCfgType().intValue()) {
		    		return true;
		    	}else {
		    		ss=specificServiceCfgService.getRepeat(specServiceCode, cfgType,parentId);
		    		if(ss==null) {
		    			return true;
		    		}
		    	}
		    }
		}else {
			SpecificServiceCfg ss=specificServiceCfgService.getRepeat(specServiceCode, cfgType,parentId);
			if(ss==null) {
				return true;
    		}
		}
		return false;
	}
	
	
	/**
	 * 校验叶子节点无上级不得选为叶子节点
	 * @param specServiceId
	 * @param oldId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "ajaxLeafHasTree")
	public boolean ajaxLeafHasTree(Integer parentId, Integer newIsLeaf){
		if(parentId==null||parentId==0){
			if(newIsLeaf==0){
				return true;
			}
		}
		if(parentId!=null&parentId!=0){
			return true;
		}
		return false;
	}
	
	
	/**
	 * 校验叶子节点有下级不得更改为叶子节点
	 * @param parent
	 * @param newIsLeaf
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "ajaxLeafChange")
	public boolean ajaxLeafChange(Integer parent,Integer newIsLeaf){
		if(parent==null){
			return true;
		}
		List<SpecificServiceCfg> list = specificServiceCfgService.getChildrenById(parent);
		if(list==null||list.size()==0){
			return true;
		}else{
			if(newIsLeaf==0){
				return true;
			}
		}
		return false;
	}
	@ResponseBody
	@RequestMapping(value = "ajaxSaveOrUpdateApp",method=RequestMethod.POST )
	public Map<String,Object> ajaxSaveOrUpdateApp(@RequestParam(required=true,value="specServiceId")String specServiceId,@RequestParam(required=false,value="specServiceCode")String specServiceCode,@RequestParam(required=true,value="specServiceName") String specServiceName,
			@RequestParam(required=true,value="cfgType") String cfgType, @RequestParam(required=true,value="businessType")String businessType,@RequestParam(required=false,value="addFlag")String addFlag,
			@RequestParam(required=false,value="groupId")String groupId){
		Map<String, Object> map = Maps.newHashMap();
		//校验是真ID还是假ID，假的Id以manual开头无法转换成数字
		boolean isTrueId=false;
		try {
			Long.parseLong(specServiceId);
			isTrueId=true;
		}catch (Exception e) {
			// TODO: handle exception
		}
		try{
			SpecificServiceCfg cfg=new SpecificServiceCfg();
			if(isTrueId) {
				cfg.setSpecServiceId(Integer.parseInt(specServiceId));
			}
			if(StringUtils.isNotBlank(specServiceCode)) {
				cfg.setSpecServiceCode(Integer.parseInt(specServiceCode));
			}
			cfg.setAddFlag(Integer.parseInt(addFlag));
			cfg.setSpecServiceName(specServiceName);
			cfg.setBusinessType(businessType);
			cfg.setCfgType(Integer.parseInt(cfgType));
			cfg.setIsValid(Constants.VALID_YES);
			if(StringUtils.isNotBlank(groupId)) {
				cfg.setGroupId(Integer.parseInt(groupId));
			}
			
			SpecificServiceCfg parent=new SpecificServiceCfg();
			parent.setSpecServiceId(0);
			cfg.setParent(parent);
			specificServiceCfgService.saveOrUpdate(cfg);
			cfg=specificServiceCfgService.getBySpecServiceId(cfg.getSpecServiceId());
			map.put("id", cfg.getSpecServiceId());
			map.put("pId", "businessType"+cfg.getBusinessType());
			map.put("name",cfg.getSpecServiceName());
			map.put("serviceType",cfg.getCfgType());
			map.put("businessType",cfg.getBusinessType());
			map.put("groupId",cfg.getGroupId());
			return map;
		}catch (Exception e) {
			e.printStackTrace();
			if(e instanceof MaatConvertException) {
				map.put("errTip", "request_service_failed");
			}else {
				map.put("errTip", "save_failed");
			}
		}
		
		return map;
	}
	@ResponseBody
	@RequestMapping(value = "ajaxRemoveApp",method=RequestMethod.POST )
	public boolean ajaxRemoveApp(@RequestParam(required=true,value="specServiceId")Integer specServiceId){
		try{
			specificServiceCfgService.delete(String.valueOf(specServiceId));
			return true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
