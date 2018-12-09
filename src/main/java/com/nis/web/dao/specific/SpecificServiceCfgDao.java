package com.nis.web.dao.specific;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import com.nis.domain.specific.ConfigGroupInfo;
import com.nis.domain.specific.SpecificServiceCfg;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;

@MyBatisDao
public interface SpecificServiceCfgDao extends CrudDao<SpecificServiceCfg> {
	
	/**
	 * 根据id查出对象
	 * @param specServiceId
	 * @return
	 */
	SpecificServiceCfg getBySpecServiceId(Integer specServiceId);
	List<SpecificServiceCfg> getBySpecServiceCodes(@Param("ids")String ids);
	/**
	 * 查询所有符合条件顶层分页列表
	 * @param specificServiceCfg
	 */
	List<SpecificServiceCfg> findTopPage(SpecificServiceCfg specificServiceCfg);
	/**
	 * 查询所有符合条件的数据
	 * @param specificServiceCfg
	 * @return
	 */
	List<SpecificServiceCfg> findAllSpecificServiceCfg(@Param("specificServiceCfg")SpecificServiceCfg specificServiceCfg,@Param("orderBy")String orderBy);
	/**
	 * 修改配置信息
	 * @param specificServiceCfg
	 * @param oldId
	 */
	void update(@Param("specificServiceCfg")SpecificServiceCfg specificServiceCfg, @Param("oldId")Integer oldId);
	/**
	 * 根据specServiceId查询所有下级
	 * @param specServiceId
	 * @return
	 */
	List<SpecificServiceCfg> getChildrenById(Integer specServiceId);
	Integer getParentType(Integer specServiceId);
	Integer getParentCode(Integer specServiceId);
	SpecificServiceCfg getRepeat(@Param("specServiceCode")Integer code, @Param("cfgType")Integer cfgType,@Param("parentId")Integer parentId);
	Integer getMaxServiceCode(@Param("maxCode")Integer code, @Param("cfgType")Integer cfgType,@Param("addFlag")Integer addFlag);
	
}