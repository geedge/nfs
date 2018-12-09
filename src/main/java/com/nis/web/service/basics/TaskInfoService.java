package com.nis.web.service.basics;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nis.domain.Page;
import com.nis.domain.configuration.TaskInfo;
import com.nis.util.Constants;
import com.nis.util.StringUtil;
import com.nis.web.dao.configuration.TaskInfoDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.BaseService;

@Service
@Transactional(readOnly=true)
public class TaskInfoService extends BaseService{

	@Autowired
	private TaskInfoDao taskInfoDao;
	
	public Page<TaskInfo> findTaskInfo(Page<TaskInfo> page, TaskInfo taskInfo) {
		taskInfo.getSqlMap().put("dsf", configScopeFilter(taskInfo.getCurrentUser(),"r"));
		// 设置分页参数
		taskInfo.setPage(page);
		// 执行分页查询
		page.setList(taskInfoDao.findTaskInfo(taskInfo));
		return page;
	}
	
	@Transactional(readOnly=false,rollbackFor=DataAccessException.class)
	public void saveOrUpdate(TaskInfo taskInfo) {
		if (StringUtil.isEmpty(taskInfo.getId())) {
			//设置默认参数值
			taskInfo.setIsValid(0);//无效
			taskInfo.setIsAudit(0);//未审核
			taskInfo.setCreatorId((UserUtils.getUser().getId()).intValue());//创建人员
			taskInfo.setCreateTime(new Date());//创建时间
			taskInfoDao.insert(taskInfo);
		}else{
			taskInfo.setIsAudit(0);//修改后状态为未审核
			taskInfo.setEditorId((UserUtils.getUser().getId()).intValue());//修改人员
			taskInfo.setEditTime(new Date());//修改时间
			taskInfoDao.update(taskInfo);
		}
		
	}

	public TaskInfo getTaskInfoByTaskName(String taskName) {
		
		return taskInfoDao.getTaskInfoByTaskName(taskName);
	}
	public TaskInfo getTaskInfoById(Long id) {
		
		return taskInfoDao.getTaskInfoById(id);
	}
	@Transactional(readOnly=false,rollbackFor=DataAccessException.class)
	public void taskExamine(String[] exId){
		TaskInfo taskInfo = new TaskInfo();
		for (int i = 0; i < exId.length; i++) {
			taskInfo.setId(Long.valueOf(exId[i]));
			taskInfo.setIsAudit(1);//审核通过
			taskInfo.setIsValid(1);//审核通过
			taskInfo.setAuditTime(new Date());
			taskInfo.setAuditorId((UserUtils.getUser().getId()).intValue());//审核人员
			taskInfoDao.update(taskInfo);
		}
	}
	@Transactional(readOnly=false,rollbackFor=DataAccessException.class)
	public void taskExamineNo(String[] noId){
		TaskInfo taskInfo = new TaskInfo();
		for (int i = 0; i < noId.length; i++) {
			taskInfo.setId(Long.valueOf(noId[i]));
			taskInfo.setIsAudit(2);//审核未通过
			taskInfo.setAuditTime(new Date());
			taskInfo.setAuditorId((UserUtils.getUser().getId()).intValue());//审核人员
			taskInfoDao.update(taskInfo);
		}
	}
	@Transactional(readOnly=false,rollbackFor=DataAccessException.class)
	public void taskCancelExamine(String[] cancelId){
		TaskInfo taskInfo = new TaskInfo();
		for (int i = 0; i < cancelId.length; i++) {
			taskInfo.setId(Long.valueOf(cancelId[i]));
			taskInfo.setIsAudit(3);//取消审核通过
			taskInfo.setIsValid(0);
			taskInfo.setAuditTime(new Date());
			taskInfo.setAuditorId((UserUtils.getUser().getId()).intValue());//审核人员
			taskInfoDao.update(taskInfo);
		}
	}
	@Transactional(readOnly=false,rollbackFor=DataAccessException.class)
	public void delete(String[] delId){
		for (int i = 0; i < delId.length; i++) {
			taskInfoDao.delete(Long.valueOf(delId[i]));//删除
		}
	}
}
