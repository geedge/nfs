package com.nis.web.dao.configuration.statistics;


import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.FunctionServiceDict;
import com.nis.domain.Page;
import com.nis.domain.SysMenu;
import com.nis.domain.configuration.HttpBodyCfg;
import com.nis.domain.configuration.RequestInfo;
import com.nis.web.dao.MyBatisDao;


@MyBatisDao
public interface ConfigureStatisticsDao {
	public List<Object[]> getConfigStateStatistics(@Param("statisticTime")String statisticTime);
	public Date getConfigStatisticTime();
	public Date getRequestStatisticTime();
	public List<Object[]> getRequestStateStatistics(@Param("requestList")List<RequestInfo> requestList,@Param("menuList")List<SysMenu> menuList,@Param("statisticTime")String statisticTime) ;
	/*public List<Object[]> getRequestStateStatistics(@Param("requestList")List<RequestInfo> requestList,@Param("serviceList")List<FunctionServiceDict> serviceList,@Param("statisticTime")String statisticTime) ;*/
	public Integer getEffectiveCfgNum(@Param("serviceId")Integer serviceId);
}
