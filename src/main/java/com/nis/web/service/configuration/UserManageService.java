package com.nis.web.service.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.eclipse.jetty.util.ajax.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import com.nis.domain.Page;
import com.nis.domain.configuration.AppPolicyCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.UserManage;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.Constants;
import com.nis.util.StringUtil;
import com.nis.web.dao.configuration.AppCfgDao;
import com.nis.web.dao.configuration.IpCfgDao;
import com.nis.web.dao.configuration.UserManageDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.BaseService;
import com.nis.web.service.SpringContextHolder;

@Service
public class UserManageService extends BaseService{
	
	@Autowired
	protected UserManageDao userManageDao;
	
	public Page<UserManage> findPage(Page<UserManage> page, UserManage entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"a"));
		entity.setPage(page);
		List<UserManage> list=userManageDao.findList(entity);
		page.setList(list);
		return page;
	}
	public UserManage getUserByLoginName(String userName) {
		return userManageDao.getUserByLoginName(userName);
	}
	
	public UserManage getUserById(String id) {
		return userManageDao.getUserById(id);
	}
	
	public static String getMessageIp(String[] oldIp,String[] ip){
		String ips=null;
		for (int i = 0; i < oldIp.length; i++) {
			 boolean flag=true;
			for (int j = 0; j < ip.length; j++) {
				if(oldIp[i].equals(ip[j])){
					flag=false;
					break;
				}
			}
			if(flag){
				if(ips==null){
					ips=oldIp[i];
				}else{
					ips=ips+","+oldIp[i];
				}
			}
		}
		return ips;
	}

	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveip(UserManage entity,String ip)throws Exception{
		userManageDao.insert(entity);
		//cgi接口添加
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", Constants.NTC_IP_REUSE_USER_CREATE);
		params.put("server_ip", ip);
		params.put("user_name", entity.getUserName());
		params.put("user_pwd", entity.getUserPwd());
		String url =Constants.IP_REUSE_CALL_CGI_URL ;
		String recv = ConfigServiceUtil.getCGI(url, params);
	}
	
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void updatePwd(UserManage entity,String ip){
		userManageDao.update(entity);
		//密码修改
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd",Constants.NTC_IP_REUSE_USER_PASSWORD);
		params.put("server_ip", ip);
		params.put("user_name", entity.getUserName());
		params.put("user_pwd", entity.getUserName());
		String url =Constants.IP_REUSE_CALL_CGI_URL ;
		String recv = ConfigServiceUtil.getCGI(url, params);
	}
	
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void deleteip(UserManage entity,String ip){
		userManageDao.update(entity);
		//循环调用接口删除
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd",Constants.NTC_IP_REUSE_USER_DELETE);
		params.put("server_ip", ip);
		params.put("user_name", entity.getUserName());
		String url =Constants.IP_REUSE_CALL_CGI_URL ;
		String recv = ConfigServiceUtil.getCGI(url, params);
	}
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void updateip(UserManage entity,String ip){
		userManageDao.update(entity);
		//cgi接口添加
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", Constants.NTC_IP_REUSE_USER_CREATE);
		params.put("server_ip", ip);
		params.put("user_name", entity.getUserName());
		params.put("user_pwd", entity.getUserPwd());
		String url =Constants.IP_REUSE_CALL_CGI_URL ;
		String recv = ConfigServiceUtil.getCGI(url, params);
		
		/*SqlSessionFactory sqlSessionFactory=SpringContextHolder.getBean(SqlSessionFactory.class);
		SqlSession batchSqlSession = null;
		try {
			batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
			if(num==0){
				UserManage olduser=userManageDao.getUserById(String.valueOf(entity.getId()));
			//接口旧数据删除
				//cgi接口删除旧数据
				String[] ipArray = olduser.getServerIp().split(",");
				for(String ip :ipArray){
					//循环调用接口删除
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("cmd",Constants.NTC_IP_REUSE_USER_DELETE);
					params.put("server_ip", ip);
					params.put("user_name", olduser.getUserName());
					Map<String,String> map=getUrl(params);
					//返回处理
				}
			entity.setServerIp(ips);
			((UserManageDao) batchSqlSession.getMapper(UserManageDao.class)).update(entity);
		}else{
			UserManage user=userManageDao.getUserById(String.valueOf(entity.getId()));
			user.setServerIp(user.getServerIp()+","+ips);
			((UserManageDao) batchSqlSession.getMapper(UserManageDao.class)).update(user);
		}
			// cgi接口添加
			// 循环调用接口增加
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("cmd", Constants.NTC_IP_REUSE_USER_CREATE);
			params.put("server_ip", ips);
			params.put("user_name", entity.getUserName());
			params.put("user_pwd", entity.getUserPwd());
			String url = Constants.IP_REUSE_CALL_CGI_URL;
			String recv = ConfigServiceUtil.getCGI(url, params);
			if (StringUtils.isNotBlank(recv)) {
				Map<String, String> map = new HashMap<String, String>();
				map = (Map<String, String>) JSON.parse(recv);
				String error = map.get("error");
				if (!StringUtil.isEmpty(error)) {
					batchSqlSession.rollback();
				} else {
					batchSqlSession.commit();
				}
			}
		} catch (MaatConvertException e) {
			throw e;
		} finally {
			if (batchSqlSession != null) {
				batchSqlSession.close();
			}
		}*/
	}
	
	
	public  Map<String,String> getUrl(Map<String, Object> params){
		Map<String,String> map=new HashMap<String,String>();
		try{ 
			String url =Constants.IP_REUSE_CALL_CGI_URL ;
			String recv = com.nis.util.ConfigServiceUtil.getCGI(url, params);
				if (StringUtils.isNotBlank(recv)) {
					map=(Map<String,String>)JSON.parse(recv);
				}
			} catch (Exception e) {
				logger.error("查询失败", e);
		}
		return map;
	}
	
	//根据vpn服务器ip获取用户vpn服务器ip信息
	public String  getUser(String serverIp,String userName){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", Constants.NTC_IP_REUSE_USER_GET);
		params.put("server_ip", serverIp);
		params.put("user_name", userName);
		Map<String,String> map=new HashMap<String,String>();
		String url =Constants.IP_REUSE_CALL_CGI_URL;
		String recv = ConfigServiceUtil.getCGI(url, params);
		return recv;
	}
	

	
	
	/**
	 * 
	 * @param isAudit
	 * @param isValid
	 * @param ids compileIds
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void delete(Integer isValid,UserManage entity,String ip,boolean lastIp){
		UserManage user=new UserManage();
		user.setId(entity.getId());
		if(!lastIp) {
			user.setServerIp(entity.getServerIp().replace(ip+",", ""));
		}else {
			user.setIsValid(-1);
		}
		userManageDao.update(user);
		//cgi接口删除用户vpnip
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", Constants.NTC_IP_REUSE_USER_DELETE);
		params.put("server_ip", ip);
		params.put("user_name", entity.getUserName());
		String url =Constants.IP_REUSE_CALL_CGI_URL ;
		String recv = ConfigServiceUtil.getCGI(url, params);
  }
	
	public List<UserManage> findUsers() {
		return userManageDao.findUsers();
	}
}

