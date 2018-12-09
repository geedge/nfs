package com.nis.web.dao.basics;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.basics.SysDictInfo;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;

@MyBatisDao
public interface SysDictInfoDao extends CrudDao<SysDictInfo> {
	
	
	
	/**
	 * 查询顶层字典列表
	 * @param sysDictInfo
	 * @return
	 */
	List<SysDictInfo> findTopDictList(SysDictInfo sysDictInfo);
	/**
	 * 查询所有符合条件的字典列表
	 * @param sysDictInfo
	 * @return
	 */
	List<SysDictInfo> findAllSysDictInfo(@Param("sysDictInfo")SysDictInfo sysDictInfo,@Param("orderBy")String orderBy);
    /**
	 * 查出所有有效数据
	 * @param sysDictInfo
	 * @return
	 */
	List<SysDictInfo> findAllDictList(SysDictInfo sysDictInfo);
    /**
     * 添加字典信息
     * @param sysDictInfo
     */
	void insertDict(SysDictInfo sysDictInfo);

	/**
	 * 根据主键查询字典详细信息
	 * @param sysDictId
	 * @return
	 */
	SysDictInfo getDictById(Integer sysDictId);
	/**
	 * 查询所有的非叶子配置
	 * @param itemType
	 * @return
	 */
	List<SysDictInfo> findAllNoLeafDictList(Integer itemType);

	/**
	 * 根据itemCode查询字典对象列表
	 * @param itemCode
	 * @return
	 */
	List<SysDictInfo> findByItemCode(@Param("sysDictInfo")SysDictInfo sysDictInfo);

	/**
	 * 根据上级id选出所有下级
	 * @param parentId
	 * @return
	 */
	List<SysDictInfo> getDictByParentId(Integer parentId);

	/**
	 * 查询最大itemCode值
	 * @return
	 */
	Integer findMaxItemCode();
	/**
	 * 保存层级
	 * @param sysDictInfo
	 */
	void updateLevelNo(SysDictInfo sysDictInfo);
	/**
	 * getDistrictDict(这里用一句话描述这个方法的作用)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param sysDictInfo
	 * @return 
	 *List<SysDictInfo>
	 * @exception 
	 * @since  1.0.0
	*/
	List<SysDictInfo> getDistrictDict(String itemCode);

	List<SysDictInfo> getEffectiveArea(String itemCode);

	SysDictInfo getDictByItemCode(String itemCode);





	


	

	
}