package com.nis.web.service.basics;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.nis.domain.Page;
import com.nis.domain.SysUser;
import com.nis.domain.basics.SysDictInfo;
import com.nis.domain.maat.EffectiveRangeCfg;
import com.nis.domain.maat.EffectiveRangeCfg.AreaIsp;
import com.nis.util.ConfigDictUtils;
import com.nis.util.Configurations;
import com.nis.util.Constants;
import com.nis.util.StringUtil;
import com.nis.util.StringUtils;
import com.nis.web.dao.basics.SysDictInfoDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.BaseService;

@Service
public class SysDictInfoService extends BaseService{
	
	@Autowired
    private SysDictInfoDao sysDictInfoDao;
	
	
	
	/**
	 * 查询顶层分页()
	 * @param page
	 * @param sysDictInfo
	 * @param itType
	 * @return
	 */
	public Page<SysDictInfo> findTopDictList(Page<SysDictInfo> page, SysDictInfo sysDictInfo, List<Integer> itType) {
		// 设置分页参数 
		        sysDictInfo.setPage(page);
		//根据条件查询符合数据
		        sysDictInfo.setConditionType(itType);
		        List<SysDictInfo> allList = sysDictInfoDao.findTopDictList(sysDictInfo);
		        SysDictInfo tempSe = new SysDictInfo();
				tempSe.setSysDictId(0);
				for(SysDictInfo se:allList){
					se.setParent(tempSe);
				}
				page.setList(allList);
				return page;
	}

	
	/**
	 * 根据条件查询所有数据
	 * @param page
	 * @param sysDictInfo
	 * @param intArr
	 * @return
	 */
	public List<SysDictInfo> findAllSysDictInfo(SysDictInfo sysDictInfo, List<Integer> itType,String orderBy) {
	
		sysDictInfo.setConditionType(itType);
		return sysDictInfoDao.findAllSysDictInfo(sysDictInfo,orderBy);
	}
	
	
	
	/**
	 * 查询所有有效字典配置
	 */
	public List<SysDictInfo> findAllDictList() {
		return sysDictInfoDao.findAllDictList(new SysDictInfo());

	}
	
	

	/**
	 * 根据主键查询生效范围字典详细信息
	 * @param sysDictId
	 * @return
	 */
	public SysDictInfo getDictById(Integer sysDictId) {
		return sysDictInfoDao.getDictById(sysDictId);
	}

	/**
	 * 新增或修改生效范围字典表
	 * @param sysDictInfo
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveOrUpdate(SysDictInfo sysDictInfo) {
		 if(sysDictInfo.getParent()==null
					||(sysDictInfo.getParent()!=null&&sysDictInfo.getParent().getSysDictId()==null)
					||(sysDictInfo.getParent()!=null&&sysDictInfo.getParent().getSysDictId()==-1)
					||(sysDictInfo.getParent()!=null&&sysDictInfo.getParent().getSysDictId()==0)
						  ){
					  SysDictInfo parent = new SysDictInfo();
					  parent.setSysDictId(0);
					  parent.setLevelNo(0);
					  sysDictInfo.setParent(parent);
				  }
		 if(sysDictInfo.getParent().getSysDictId()!=0){
			 SysDictInfo parent = sysDictInfoDao.getDictById(sysDictInfo.getParent().getSysDictId());
			 sysDictInfo.setParent(parent);
		 }
		SysUser user = UserUtils.getUser();
      if(StringUtil.isEmpty(sysDictInfo.getSysDictId())) {//新增
    	  sysDictInfo.setLevelNo(sysDictInfo.getParent().getLevelNo()+1);
    	  sysDictInfo.setIsValid(1);
    	  sysDictInfo.setSysDictCreator(user);
    	  sysDictInfo.setCreateTime(new Date());
    	  sysDictInfo.setSysDictEditor(user);
    	  sysDictInfo.setEditTime(sysDictInfo.getCreateTime());
    	  sysDictInfo.setIsInitianlize(0);//是否为初始化数据，默认为0：否
    	  sysDictInfoDao.insertDict(sysDictInfo);
    	  
		}
      
      else {//修改
    	  List<SysDictInfo> resultList = Lists.newArrayList();
    	  List<SysDictInfo> list = sysDictInfoDao.getDictByParentId(sysDictInfo.getSysDictId());
    	  
    	  //处理层级
    	  if(sysDictInfo.getParent().getSysDictId()==0){
    		  sysDictInfo.setLevelNo(1);
    		  //处理子集
    		  childLevel(sysDictInfo,list,resultList);
    	  }else{
    		  sysDictInfo.setLevelNo(sysDictInfo.getParent().getLevelNo()+1);
        	//处理子集
    		  childLevel(sysDictInfo,list,resultList);
    	  }
    	  
    	  
    	  sysDictInfo.setSysDictEditor(user);
    	  sysDictInfo.setEditTime(new Date());
    	  for(SysDictInfo sc:resultList){
        	  sysDictInfoDao.updateLevelNo(sc);
    	  }
    	  sysDictInfoDao.update(sysDictInfo);
		}
		
	}
	
	//处理子集层级
	
	public  void childLevel(SysDictInfo sysDictInfo, List<SysDictInfo> list,List<SysDictInfo> resultList){
		for(SysDictInfo se:list){
			se.setLevelNo(sysDictInfo.getLevelNo()+1);
			resultList.add(se);
			List<SysDictInfo> newList = sysDictInfoDao.getDictByParentId(se.getSysDictId());
			childLevel(se,newList,resultList);
		}
		
	}
	

	/**
	 * 删除
	 * @param sysDictInfo
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void deleteDict(String mulitId) {
		String[] ids = mulitId.split(",");
		for(String id:ids){
			if (!id.equals("")) {
				List<SysDictInfo> list = Lists.newArrayList();
				SysDictInfo sysDictInfo = sysDictInfoDao.getDictById(Integer.valueOf(id));
				if(sysDictInfo!=null){
					SysDictInfo.sortList(list, sysDictInfoDao.findAllDictList(new SysDictInfo()), sysDictInfo.getSysDictId(), true);
					list.add(sysDictInfo);
					for(SysDictInfo se:list){
						se.setIsValid(0);
						sysDictInfoDao.delete(se);
					}
				}
			}
		}
		
		
		
	}
	/**
	 * 查询该类型所有的非叶子配置
	 * @param itType
	 * @return
	 */
	public List<SysDictInfo> findAllNoLeafDictList(String itType) {
		List<SysDictInfo> list = Lists.newArrayList();
		List<Integer> intArr = ConfigDictUtils.dealTypeCondition(Configurations.getStringProperty("SYS_DICT_ITM_TYPE", ""), itType);
		for(Integer intType:intArr){
			List<SysDictInfo> tempList = sysDictInfoDao.findAllNoLeafDictList(intType);
			list.addAll(tempList);
		}
		return list;
	}
	/**
	 * 根据itemCode查出对象集合
	 * @param itemCode
	 * @return
	 */
	public List<SysDictInfo> findByItemCode(SysDictInfo sysDictInfo) {
		return sysDictInfoDao.findByItemCode(sysDictInfo);
	}
	/**
	 * 根据上级id选出所有下级
	 * @param parent
	 * @return
	 */
	public List<SysDictInfo> getDictByParentId(Integer parentId) {
		
		return sysDictInfoDao.getDictByParentId(parentId);
	}


	public List<SysDictInfo> getDistrictDict(String tableName){
		return sysDictInfoDao.getDistrictDict(tableName);
	}
	
	
	/**
	 * 组织区域、运营商配置下发格式
	 * @param areaEffectiveIds
	 * @return
	 */
	public String setEffectiveRange(String areaEffectiveIds){
		HashMap<String, List<String>> map = new HashMap<String,List<String>>();
		List<String> ispList = new ArrayList();
		for (String areaEffectiveId : StringUtils.split(areaEffectiveIds, ",")) {
			if(areaEffectiveId.contains(":")) {
				// 区域和运营商
				int index = areaEffectiveId.indexOf(":");
				String areaCode = areaEffectiveId.substring(0, index);
				String ispCode = areaEffectiveId.substring(index+1);
				
				if(!map.containsKey(areaCode)){
					ispList = new ArrayList();
					ispList.add(ispCode);
					map.put(areaCode, ispList);
				}else{
					if(!ispList.contains(ispCode)){
						ispList.add(ispCode);
					}
					map.replace(areaCode, ispList);
				}
			}else {
				// 区域
				if(!map.containsKey(areaEffectiveId)){
					map.put(areaEffectiveId, null);
				}
				
			}
			
		}

		EffectiveRangeCfg rangeCfg = new EffectiveRangeCfg();
		List<List<AreaIsp>> effectiveRangeList = new ArrayList();
		Set<String> keySet = map.keySet();
		for (String key : keySet) {
			AreaIsp area = new AreaIsp();
			area.setTag(Constants.AREA_TAG);
			AreaIsp isp = new AreaIsp();
			isp.setTag(Constants.ISP_TAG);
			List<String> locaValueList = new ArrayList();
			List<String> ispValueList = new ArrayList();
			List<AreaIsp> areaIspList = new ArrayList();
			// 获取区域信息
			String areaStr = "";
			SysDictInfo areaDict = sysDictInfoDao.getDictByItemCode(key);
			areaStr += areaDict.getItemValue();
			while(areaDict.getParent().getSysDictId() != 0) {
				areaDict = sysDictInfoDao.getDictById(areaDict.getParent().getSysDictId());
				areaStr += "/"+areaDict.getItemValue();
			}
			locaValueList.add(StringUtils.revertStr(areaStr, "/"));
			area.setValue(locaValueList);
			areaIspList.add(area);
			
			// 获取运营商信息
			if(map.get(key) != null){
				for (String ispCode : map.get(key)) {
					SysDictInfo ispDict = sysDictInfoDao.getDictByItemCode(ispCode);
					ispValueList.add(ispDict.getItemValue());
				}
				isp.setValue(ispValueList);
				areaIspList.add(isp);
			}
			effectiveRangeList.add(areaIspList);
			
		}
		rangeCfg.setEffectiveRangeList(effectiveRangeList);
		return gsonToJson(rangeCfg);
		
	}
	
}
