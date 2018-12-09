package com.nis.web.dao;

import java.util.List;

import com.nis.domain.SysLog;
@MyBatisDao
public interface SysLogDao extends CrudDao<SysLog>{
    public void insertCfgOperationLog(SysLog log);
    public List<SysLog> findCfgOperationLogList(SysLog log);
}