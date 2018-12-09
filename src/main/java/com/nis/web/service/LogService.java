package com.nis.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.SysLog;
import com.nis.util.DateUtils;
import com.nis.web.dao.SysLogDao;

@Service
public class LogService extends CrudService<SysLogDao, SysLog> {
	@Autowired
	protected SysLogDao sysLogDao;
	public Page<SysLog> findPage(Page<SysLog> page, SysLog sysLog) {
		
		// 设置默认时间范围，默认当前月
		if (sysLog.getBeginDate() == null){
			sysLog.setBeginDate(DateUtils.setDays(DateUtils.parseDate(DateUtils.getDate()), 1));
		}
		if (sysLog.getEndDate() == null){
			sysLog.setEndDate(DateUtils.addMonths(sysLog.getBeginDate(), 1));
		}
		
		return super.findPage(page, sysLog);
		
	}

	public Page<SysLog> findCfgOperationLogPage(Page<SysLog> page, SysLog sysLog) {
		
		// 设置默认时间范围，默认当前月
		if (sysLog.getBeginDate() == null){
			sysLog.setBeginDate(DateUtils.setDays(DateUtils.parseDate(DateUtils.getDate()), 1));
		}
		if (sysLog.getEndDate() == null){
			sysLog.setEndDate(DateUtils.addMonths(sysLog.getBeginDate(), 1));
		}
		sysLog.setPage(page);
		List<SysLog> list = sysLogDao.findCfgOperationLogList(sysLog);
		return page.setList(list);		
	}
}
