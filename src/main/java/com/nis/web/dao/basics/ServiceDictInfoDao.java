package com.nis.web.dao.basics;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.SysMenu;
import com.nis.domain.basics.ServiceDictInfo;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;

@MyBatisDao
public interface ServiceDictInfoDao extends CrudDao<ServiceDictInfo> {
	
	/**
	 * 查询顶层字典列表
	 * @param serviceDictInfo
	 * @return
	 */
	List<ServiceDictInfo> findTopDictList(ServiceDictInfo serviceDictInfo);
	/**
	 * findAllServiceDictInfo
	 */
	/**
	 * 查询所有符合条件的字典列表
	 * @param serviceDictInfo
	 * @return
	 */
	List<ServiceDictInfo> findAllServiceDictInfo(@Param("serviceDictInfo")ServiceDictInfo serviceDictInfo,@Param("orderBy")String orderBy);
	/**
	 * 查出所有有效数据父级(!=0)
	 */
	List<ServiceDictInfo> findAllDictList(ServiceDictInfo serviceDictInfo);
	/**
	 * 查询所有的非叶子配置
	 */
	List<ServiceDictInfo> findAllNoLeafDictList(@Param("itemType")Integer itemType);
	/**
	 * 根据上级id选出所有下级
	 * @param parentId
	 * @return
	 */
	List<ServiceDictInfo> getDictByParentId(Integer parentId);
    /**
     * 添加字典信息
     * @param serviceDictInfo
     */
	void insertDict(ServiceDictInfo serviceDictInfo);
    /**
     * 保存层级
     * @param serviceDictInfo
     */
	void updateLevelNo(ServiceDictInfo serviceDictInfo);

	/**
	 * 根据主键查询字典详细信息
	 * @param serviceDictId
	 * @return
	 */
	ServiceDictInfo getDictById(Integer serviceDictId);
	/**
	 * 根据itemCode查询字典对象列表
	 * @param itemCode
	 * @return
	 */
	List<ServiceDictInfo> findByItemCode(String itemCode);
	/**
	 * 查询itemCode最大值
	 * @return
	 */
	Integer findMaxItemCode();

	
	
	
	List<ServiceDictInfo> findItemDict(@Param("itemType")int itemType,@Param("isValid")int isValid,@Param("isLeaf")Integer isLeaf);
	List<ServiceDictInfo> findAllItemDictByItemType(@Param("itemType")int itemType,@Param("isLeaf")Integer isLeaf);
	
	void auditCancleRequestInfo(@Param("cancelRequestId")Integer cancelRequestId, @Param("indexTable")String indexTable,@Param("id") String id);
	Integer requestCancleInfoNumber(@Param("indexTable")String indexTable,@Param("id")Long id);
	
	ServiceDictInfo getByItemValue(@Param("itemValue")String itemValue,@Param("itemType")Integer itemType);


	



	



	
	
}