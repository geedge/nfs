package com.nis.web.service.basics;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.nis.domain.Page;
import com.nis.domain.SysUser;
import com.nis.domain.basics.ServiceDictInfo;
import com.nis.util.ConfigDictUtils;
import com.nis.util.Configurations;
import com.nis.util.Constants;
import com.nis.util.StringUtil;
import com.nis.web.dao.basics.ServiceDictInfoDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.BaseService;

@Service
public class ServiceDictInfoService extends BaseService{
	
	@Autowired
     private ServiceDictInfoDao serviceDictInfoDao;
	
	/**
	 * 查询业务字典顶层分页
	 * @param page
	 * @param serviceDictInfo
	 * @return
	 */
	public Page<ServiceDictInfo> findTopDictList(Page<ServiceDictInfo> page, ServiceDictInfo serviceDictInfo,List<Integer> itType) {
		// 设置分页参数 
		serviceDictInfo.setPage(page);
		serviceDictInfo.setConditionType(itType);
		List<ServiceDictInfo> parentList = serviceDictInfoDao.findTopDictList(serviceDictInfo);
		ServiceDictInfo sd = new ServiceDictInfo();
		sd.setServiceDictId(0);
		for(ServiceDictInfo se:parentList){
			se.setParent(sd);
		}
		page.setList(parentList);
		return page;
	}
	/**
	 * 根据条件查询所有数据
	 * @param serviceDictInfo
	 * @param itType
	 * @return
	 */
	public List<ServiceDictInfo> findAllServiceDictInfo(ServiceDictInfo serviceDictInfo,List<Integer> itType,String orderBy){
		
		serviceDictInfo.setConditionType(itType);;
		return serviceDictInfoDao.findAllServiceDictInfo(serviceDictInfo,orderBy);
	}
	
	
	
	
	
	
	
	
	/**
	 * 查询所有有效字典配置
	 * @return
	 */
	public List<ServiceDictInfo> findAllDictList() {
		
		return serviceDictInfoDao.findAllDictList(new ServiceDictInfo());
	}
	
	/**
	 * 查询该类型所有的非叶子配置
	 */
	public List<ServiceDictInfo> findAllNoLeafDictList(String itType) {
		List<ServiceDictInfo> list = Lists.newArrayList();
		List<Integer> intArr = ConfigDictUtils.dealTypeCondition(Configurations.getStringProperty("SERVICE_DICT_ITM_TYPE", ""), itType);

		for(Integer intType:intArr){
			List<ServiceDictInfo> tempList = serviceDictInfoDao.findAllNoLeafDictList(intType);
			list.addAll(tempList);
		}
		return list;
	}
	/**
	 * 查询该类型所有的非叶子配置
	 */
	public List<ServiceDictInfo> findAllNoLeafDictList(String itType,Integer cfgType) {
		List<ServiceDictInfo> list = Lists.newArrayList();
		List<Integer> intArr = ConfigDictUtils.dealTypeCondition(Configurations.getStringProperty("SERVICE_DICT_ITM_TYPE", ""), itType);
		
		for(Integer intType:intArr){
			List<ServiceDictInfo> tempList = serviceDictInfoDao.findAllNoLeafDictList(intType);
			list.addAll(tempList);
		}
		return list;
	}
	/**
	 * 根据主键查询字典详细信息
	 * @param serviceDictId
	 * @return
	 */
	public ServiceDictInfo getDictById(Integer serviceDictId) {
		return serviceDictInfoDao.getDictById(serviceDictId);
	}
	
	/**
	 * 根据上级id选出所有下级
	 * @param parentId
	 * @return
	 */
	public List<ServiceDictInfo> getDictByParentId(Integer parentId) {
		
		return serviceDictInfoDao.getDictByParentId(parentId);
	}

	/**
	 * 新增或修改业务字典表(添加层级改变)
	 * @param serviceDictInfo
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveOrUpdate(ServiceDictInfo serviceDictInfo) {
		 if(serviceDictInfo.getParent()==null
					||(serviceDictInfo.getParent()!=null&&serviceDictInfo.getParent().getServiceDictId()==null)
					||(serviceDictInfo.getParent()!=null&&serviceDictInfo.getParent().getServiceDictId()==-1)
					||(serviceDictInfo.getParent()!=null&&serviceDictInfo.getParent().getServiceDictId()==0)
						  ){
					  ServiceDictInfo parent = new ServiceDictInfo();
					  parent.setServiceDictId(0);
					  parent.setLevelNo(0);
					  serviceDictInfo.setParent(parent);
				  }
		 if(serviceDictInfo.getParent().getServiceDictId()!=0){
          	  ServiceDictInfo parent = serviceDictInfoDao.getDictById(serviceDictInfo.getParent().getServiceDictId()); 
          	  serviceDictInfo.setParent(parent);
   	  }
		SysUser user = UserUtils.getUser();
      if(StringUtil.isEmpty(serviceDictInfo.getServiceDictId())) {//新增
    	  serviceDictInfo.setLevelNo(serviceDictInfo.getParent().getLevelNo()+1);
    	  serviceDictInfo.setIsValid(1);
    	  serviceDictInfo.setServiceDictCreator(user);
    	  serviceDictInfo.setCreateTime(new Date());
    	  serviceDictInfo.setServiceDictEditor(user);
		  serviceDictInfo.setEditTime(serviceDictInfo.getCreateTime());
    	  serviceDictInfoDao.insertDict(serviceDictInfo);
		}
      else {//修改
    	  List<ServiceDictInfo> resultList = Lists.newArrayList();
		  List<ServiceDictInfo> list = serviceDictInfoDao.getDictByParentId(serviceDictInfo.getServiceDictId());
    	 
		  //处理层级
    	  if(serviceDictInfo.getParent().getServiceDictId()==0){
    		  serviceDictInfo.setLevelNo(1);
    		  //处理子集
    		  childLevel(serviceDictInfo,list,resultList);
    	  }else{
        	  serviceDictInfo.setLevelNo(serviceDictInfo.getParent().getLevelNo()+1);
        	//处理子集
    		  childLevel(serviceDictInfo,list,resultList);
    	  }
    	  serviceDictInfo.setServiceDictEditor(user);
    	  serviceDictInfo.setEditTime(new Date());
    	  for(ServiceDictInfo sc:resultList){
        	  serviceDictInfoDao.updateLevelNo(sc);
    	  }
    	  serviceDictInfoDao.update(serviceDictInfo);
		}
		
	}
	
	
	//处理子集层级
	
	public  void childLevel(ServiceDictInfo serviceDictInfo, List<ServiceDictInfo> list,List<ServiceDictInfo> resultList){
		for(ServiceDictInfo se:list){
			se.setLevelNo(serviceDictInfo.getLevelNo()+1);
			resultList.add(se);
			List<ServiceDictInfo> newList = serviceDictInfoDao.getDictByParentId(se.getServiceDictId());
			childLevel(se,newList,resultList);
		}
		
	}

	/**
	 * 删除
	 * @param serviceDictInfo
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void deleteDict(String mulitId) {
		
		String[] ids = mulitId.split(",");
		for(String id:ids){
			if (!id.equals("")) {
				ServiceDictInfo serviceDictInfo = serviceDictInfoDao.getDictById(Integer.valueOf(id));
				List<ServiceDictInfo> list = Lists.newArrayList();
				//找出所有下级
				//查出所有节点
				if(serviceDictInfo!=null){
					ServiceDictInfo.sortList(list, serviceDictInfoDao.findAllDictList(new ServiceDictInfo()), serviceDictInfo.getServiceDictId(), true);
					list.add(serviceDictInfo);
					for(ServiceDictInfo se:list){
						se.setIsValid(0);
						serviceDictInfoDao.delete(se);
					}
				}	
			}
		}
		
		
		
		
	}
	
	/**
	 * 根据itemCode查出对象集合
	 */
	public List<ServiceDictInfo> findByItemCode(String itemCode){
		return serviceDictInfoDao.findByItemCode(itemCode);
	}
	
	
	
	
	
	
	/**
	 * 
	 * findFlDict(查找所有有效的分类)
	 * (这里描述这个方法适用条件 – 可选)
	 * @return 
	 *List<ServiceDictInfo>
	 * @exception 
	 * @since  1.0.0
	 */
	public List<ServiceDictInfo> findFlDict() {
		return serviceDictInfoDao.findItemDict(Constants.ITEM_TYPE_FL,Constants.VALID_YES,null);
	}
	/**
	 * 
	 * findFlDict(查找所有有效的分类)
	 * (这里描述这个方法适用条件 – 可选)
	 * @return 
	 *List<ServiceDictInfo>
	 * @exception 
	 * @since  1.0.0
	 */
	public List<ServiceDictInfo> findAllFlDict() {
		return serviceDictInfoDao.findAllItemDictByItemType(Constants.ITEM_TYPE_FL,null);
	}
	/**
	 * 
	 * findFlDict(查找所有有效的性质)
	 * (这里描述这个方法适用条件 – 可选)
	 * @return 
	 *List<ServiceDictInfo>
	 * @exception 
	 * @since  1.0.0
	 */
	public List<ServiceDictInfo> findXzDict() {
		return serviceDictInfoDao.findItemDict(Constants.ITEM_TYPE_XZ,Constants.VALID_YES,null);
	}
	/**
	 * 
	 * findFlDict(查找所有有效的性质)
	 * (这里描述这个方法适用条件 – 可选)
	 * @return 
	 *List<ServiceDictInfo>
	 * @exception 
	 * @since  1.0.0
	 */
	public List<ServiceDictInfo> findAllXzDict() {
		return serviceDictInfoDao.findAllItemDictByItemType(Constants.ITEM_TYPE_XZ,null);
	}
	/**
	 * 
	 * findFlDict(查找所有有效的标签)
	 * (这里描述这个方法适用条件 – 可选)
	 * @return 
	 *List<ServiceDictInfo>
	 * @exception 
	 * @since  1.0.0
	 */
	public List<ServiceDictInfo> findLableDict() {
		return serviceDictInfoDao.findItemDict(Constants.ITEM_TYPE_LABEL,Constants.VALID_YES,null);
	}
	
	/**
	 * 
	 * findFlDict(查找所有有效的标签)
	 * (这里描述这个方法适用条件 – 可选)
	 * @return 
	 *List<ServiceDictInfo>
	 * @exception 
	 * @since  1.0.0
	 */
	public List<ServiceDictInfo> findAllLableDict() {
		return serviceDictInfoDao.findAllItemDictByItemType(Constants.ITEM_TYPE_LABEL,null);
	}
	
	
	/**
	 * 
	 * 取消审核的时候增加取消来函
	 * @param cancelRequestId
	 * @param indexTable
	 * @param id
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void auditCancleRequestInfo(Integer cancelRequestId, String indexTable, String id) {
		serviceDictInfoDao.auditCancleRequestInfo(cancelRequestId, indexTable, id);
	}
	public Integer requestCancleInfoNumber(String indexTable, Long id) {
		Integer cancleRequestId = serviceDictInfoDao.requestCancleInfoNumber( indexTable, id);
		return cancleRequestId;
	}




	
	/**
	 * 根据itemValue查出对象集合
	 * @param itemCode
	 * @return
	 */
	public ServiceDictInfo getByItemValue(String itemValue,Integer itemType) {
		return serviceDictInfoDao.getByItemValue(itemValue, itemType);
	}



	
}
