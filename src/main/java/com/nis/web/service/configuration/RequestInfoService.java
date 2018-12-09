package com.nis.web.service.configuration;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nis.domain.Page;
import com.nis.domain.configuration.RequestInfo;
import com.nis.domain.configuration.TaskInfo;
import com.nis.util.Constants;
import com.nis.util.StringUtil;
import com.nis.web.dao.configuration.RequestInfoDao;
import com.nis.web.dao.configuration.TaskInfoDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.BaseService;

@Service
@Transactional(readOnly=true)
public class RequestInfoService extends BaseService{

	@Autowired
	private RequestInfoDao requestInfoDao;
	@Autowired
	private TaskInfoDao taskInfoDao;
	
	public Page<RequestInfo> findRequestInfo(Page<RequestInfo> page, RequestInfo requestInfo) {
		requestInfo.getSqlMap().put("dsf", configScopeFilter(requestInfo.getCurrentUser(),"r"));
		// 设置分页参数
		requestInfo.setPage(page);
		// 执行分页查询
		page.setList(requestInfoDao.findRequestInfo(requestInfo));
		return page;
	}
	
	@Transactional(readOnly=false,rollbackFor=DataAccessException.class)
	public void saveOrUpdate(RequestInfo requestInfo) {
		if (StringUtil.isEmpty(requestInfo.getId())) {
			//设置默认参数值
			requestInfo.setIsValid(0);//无效
			requestInfo.setIsAudit(0);//未审核
			requestInfo.setCreatorId((UserUtils.getUser().getId()).intValue());//创建人员
			requestInfo.setCreateTime(new Date());//创建时间
			requestInfoDao.insert(requestInfo);
		}else{
			requestInfo.setIsAudit(0);//修改后状态为未审核
			requestInfo.setEditorId((UserUtils.getUser().getId()).intValue());//修改人员
			requestInfo.setEditTime(new Date());//修改时间
			requestInfoDao.update(requestInfo);
		}
		
	}

	public RequestInfo getRequestInfoByRequestNumber(String requestNumber) {
		
		return requestInfoDao.getRequestInfoByRequestNumber(requestNumber);
	}
	public RequestInfo getRequestInfoById(Long id) {
		
		return requestInfoDao.getRequestInfoById(id);
	}
	@Transactional(readOnly=false,rollbackFor=DataAccessException.class)
	public void requestExamine(String[] exId){
		RequestInfo requestInfo = new RequestInfo();
		for (int i = 0; i < exId.length; i++) {
			requestInfo.setId(Long.valueOf(exId[i]));
			requestInfo.setIsAudit(1);//审核通过
			requestInfo.setIsValid(1);//审核通过有效
			requestInfo.setAuditTime(new Date());
			requestInfo.setAuditorId((UserUtils.getUser().getId()).intValue());//审核人员
			requestInfoDao.update(requestInfo);
		}
	}
	@Transactional(readOnly=false,rollbackFor=DataAccessException.class)
	public void requestExamineNo(String[] noId){
		RequestInfo requestInfo = new RequestInfo();
		for (int i = 0; i < noId.length; i++) {
			requestInfo.setId(Long.valueOf(noId[i]));
			requestInfo.setIsAudit(2);//审核未通过
			requestInfo.setAuditTime(new Date());
			requestInfo.setAuditorId((UserUtils.getUser().getId()).intValue());//审核人员
			requestInfoDao.update(requestInfo);
		}
	}
	@Transactional(readOnly=false,rollbackFor=DataAccessException.class)
	public void requestCancelExamine(String[] cancelId){
		RequestInfo requestInfo = new RequestInfo();
		for (int i = 0; i < cancelId.length; i++) {
			requestInfo.setId(Long.valueOf(cancelId[i]));
			requestInfo.setIsAudit(3);//取消审核通过
			requestInfo.setIsValid(0);//取消审核通过无效
			requestInfo.setAuditTime(new Date());
			requestInfo.setAuditorId((UserUtils.getUser().getId()).intValue());//审核人员
			requestInfoDao.update(requestInfo);
		}
	}
	@Transactional(readOnly=false,rollbackFor=DataAccessException.class)
	public void delete(String[] delId){
		for (int i = 0; i < delId.length; i++) {
			requestInfoDao.delete(Long.valueOf(delId[i]));//删除
		}
	}
	public List<RequestInfo> getValidRequestInfo(){
		RequestInfo requestInfo=new RequestInfo();
		requestInfo.setIsValid(Constants.VALID_YES);
//		requestInfo.setIsAudit(Constants.AUDIT_YES);
		return requestInfoDao.findRequestInfo(requestInfo);
	}
	public List<RequestInfo> getAllRequestInfo(){
		RequestInfo requestInfo=new RequestInfo();
		return requestInfoDao.findAllList(requestInfo);
	}
	
	public List<RequestInfo> getAllLwhhRequestInfo(){
		return requestInfoDao.findAllLwhhList();
	}
	

	public List<TaskInfo> showTask(TaskInfo taskInfo) {
		return taskInfoDao.findList(taskInfo);
	}
	
	public String getRequestTitleById(Long id) {
		return requestInfoDao.getRequestTitleById(id);
		
	}
}
