package com.nis.web.dao.specific;

import java.util.List;

import com.nis.domain.specific.SpecificServiceHostCfg;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;


@MyBatisDao
public interface SpecificServiceHostCfgDao extends CrudDao<SpecificServiceHostCfg> {

	/**
	 * 根据主键查询数据对象
	 * @param hostId
	 * @return
	 */
	SpecificServiceHostCfg getByHostId(Integer hostId);

	/**
	 * 查询分页
	 * @param specificServiceHostCfg
	 * @return
	 */
	List<SpecificServiceHostCfg> findSpecHostList(SpecificServiceHostCfg specificServiceHostCfg);

	/**
	 * 删除
	 * @param hostId
	 */
	void delete(Integer hostId);

	/**
	 * 根据协议ID查询对象
	 * @param specServiceId
	 * @return
	 */
	SpecificServiceHostCfg getBySpecServiceId(Integer specServiceId);
	
	

}