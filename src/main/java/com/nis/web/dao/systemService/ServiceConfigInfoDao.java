package com.nis.web.dao.systemService;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.ServiceConfigInfo;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;
@MyBatisDao
public interface ServiceConfigInfoDao extends CrudDao<ServiceConfigInfo>{
	public ServiceConfigInfo findSysServiceConfigInfo(@Param("service_id")int serviceId);
	public List<ServiceConfigInfo> findList(@Param("service_id")int serviceId);
}
