package com.nis.web.dao.configuration;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.configuration.TaskInfo;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;
@MyBatisDao
public interface TaskInfoDao extends CrudDao<TaskInfo>{
	List<TaskInfo> findTaskInfo(TaskInfo requestInfo);
	
	TaskInfo getTaskInfoByTaskName(@Param("taskName") String taskName);
	
	TaskInfo getTaskInfoById(@Param("id") Long id);
	
	void delete(@Param("id") Long id);

}