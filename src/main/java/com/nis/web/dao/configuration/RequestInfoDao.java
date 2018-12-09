package com.nis.web.dao.configuration;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.configuration.RequestInfo;
import com.nis.domain.configuration.TaskInfo;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;

@MyBatisDao
public interface RequestInfoDao  extends CrudDao {
	
	List<RequestInfo> findRequestInfo(RequestInfo requestInfo);
	
	RequestInfo getRequestInfoByRequestNumber(@Param("requestNumber") String requestNumber);
	
	RequestInfo getRequestInfoById(@Param("id") Long id);
	
    int deleteByPrimaryKey(Long id);

	List<TaskInfo> showTask(TaskInfo taskInfo);

	void delete(@Param("id") Long id);
	
	String getRequestTitleById(@Param("id")Long id);
	
	List<RequestInfo> findAllLwhhList();


}