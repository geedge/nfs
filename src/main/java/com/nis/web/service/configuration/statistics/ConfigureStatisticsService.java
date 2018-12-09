package com.nis.web.service.configuration.statistics;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nis.domain.FunctionServiceDict;
import com.nis.domain.Page;
import com.nis.domain.SysMenu;
import com.nis.domain.configuration.AreaIpCfg;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.NumBoundaryCfg;
import com.nis.domain.configuration.RequestInfo;
import com.nis.util.Constants;
import com.nis.web.dao.configuration.NumCfgDao;
import com.nis.web.dao.configuration.statistics.ConfigureStatisticsDao;
import com.nis.web.service.CrudService;

/**
 *配置统计service
 */
@Service
public class ConfigureStatisticsService extends CrudService<NumCfgDao,NumBoundaryCfg> {
	@Autowired
	protected ConfigureStatisticsDao configureStatisticsDao;
	 
	public List<Object[]> getConfigStateStatistics(String statisticTime){
		return configureStatisticsDao.getConfigStateStatistics(statisticTime);
	}
	public Date getConfigStatisticTime(){
		Date statisticTime=configureStatisticsDao.getConfigStatisticTime();
		return statisticTime;
	}
	public Date getRequestStatisticTime(){
		Date statisticTime=configureStatisticsDao.getRequestStatisticTime();
		return statisticTime;
	}
	public List<Object[]>  getRequestStateStatistics(List<RequestInfo> requestList,List<SysMenu> menuList,String statistcTime){
		List<Object[]> dataList=configureStatisticsDao.getRequestStateStatistics(requestList,menuList,statistcTime);
		return dataList;
	}
/*	public List<Object[]>  getRequestStateStatistics(List<RequestInfo> requestList,List<FunctionServiceDict> serviceList,String statistcTime){
		List<Object[]> dataList=configureStatisticsDao.getRequestStateStatistics(requestList,serviceList,statistcTime);
		return dataList;
	}
*/	public Integer getEffectiveCfgNum(Integer serviceId){
		return configureStatisticsDao.getEffectiveCfgNum(serviceId);
	}
}
