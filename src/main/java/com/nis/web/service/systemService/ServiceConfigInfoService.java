package com.nis.web.service.systemService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.ServiceConfigInfo;
import com.nis.web.dao.systemService.ServiceConfigInfoDao;

@Service
public class ServiceConfigInfoService {
	@Autowired
	protected ServiceConfigInfoDao serviceConfigInfoDao;
	public ServiceConfigInfo findSysServiceConfigInfo(int serviceId){
		return serviceConfigInfoDao.findSysServiceConfigInfo(serviceId);
	}
	public List<ServiceConfigInfo> findList(int serviceId){
		return serviceConfigInfoDao.findList(serviceId);
	}
}
