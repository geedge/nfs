package com.nis.web.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.OMNamespaceImpl;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.commons.io.FileUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.SrcIp;
import com.nis.domain.SysRole;
import com.nis.domain.SysUser;
import com.nis.util.Configurations;
import com.nis.util.DateUtils;
import com.nis.util.IpUtil;
import com.nis.util.StringUtil;
import com.nis.util.TimeConstants;
import com.nis.web.dao.SrcIpDao;
import com.nis.web.dao.SysOfficeDao;
import com.nis.web.dao.UserDao;
import com.nis.web.security.Servlets;
import com.nis.web.security.SessionDAO;
import com.nis.web.security.UserUtils;

@Service
public class SystemService extends BaseService{
	
	@Autowired
	private SessionDAO sessionDao;
	
	@Autowired
	private UserDao userDao;

	@Autowired
	private SysOfficeDao sysOfficeDao;
	
	@Autowired
	private SrcIpDao srcIpDao;
		
	
	public Collection<Session> getActiveSessions(boolean includeLeave) {
		return  sessionDao.getActiveSessions(includeLeave);
	}
	
	
	public Collection<Session> getActiveSessions(boolean includeLeave, Object principal, Session filterSession) {
		return  sessionDao.getActiveSessions(includeLeave, principal, filterSession);
	}
	
	public void deleteSession(Session session) {
		sessionDao.delete(session);
	}
	
	
	public SysUser getUserByLoginName(String loginName) {
		return UserUtils.getByLoginName(loginName);
	}
	
	public List<SrcIp> ipLookUp(String ip) {
		return srcIpDao.getIpInfo(IpUtil.getIpHostDesimal(ip));
	}
	
	
	public SysUser assignUserToRole(SysRole role, SysUser user) {
		if (user == null){
			return null;
		}
		List<Long> roleIds = user.getRoleIdList();
		if (roleIds.contains(role.getId())) {
			return null;
		}
		user.getUserRoleList().clear();
		user.getUserRoleList().add(role);
		userDao.insertUserRole(user);
		UserUtils.clearCache(user);
		return user;
	}
	
	public Boolean outUserInRole(SysRole role, SysUser user) {
		List<SysRole> roles = user.getUserRoleList();
		for (SysRole e : roles){
			if (e.getId().equals(role.getId())){
				roles.remove(e);
				userDao.removeUserInRole(user.getId(),role.getId());
				return true;
			}
		}
		return false;
	}
	
	public boolean officeIsValid(Long officeId, Long companyId) {
		return userDao.officeIsExistOfCompany(officeId, companyId) >0 ?  true : false;
	}

	/**
	 * 结果信息存入文件
	 * @param result
	 * @throws IOException 
	 */
	private void saveToFile(String prefixName, String result) throws IOException{
		String flieName = prefixName + DateUtils.formatDate(new Date(), TimeConstants.YYYYMMDDHH24MMSS);
		String filePath = Servlets.getRequest().getServletContext().getRealPath(Configurations.getStringProperty("userfiles.basedir", "")) + File.separator
				+ "upload" + File.separator + flieName + ".txt";
		FileUtils.writeStringToFile(new File(filePath), result, false);		
		
	}
	
	/**
	 * 新增设置header信息，需要修改里面rid、sid对应的值、sid为服务唯一标识、 rid为请求者唯一标识
	 */
	private static OMElement setHeader(String ns, String rid, String sid) {
		OMFactory fac = OMAbstractFactory.getOMFactory();
		// OMNamespace指定此SOAP文档名称空间。
		OMNamespaceImpl omNs =  (OMNamespaceImpl) fac.createOMNamespace(ns, "ns1");
		// 创建header元素，并指定其在omNs指代的名称空间中,header名称固定为CyberpoliceSBReqHeader。
		OMElement method = fac.createOMElement("CyberpoliceSBReqHeader", omNs);
	
		// 指定元素的文本内容。
		OMElement ridE = fac.createOMElement("rid", omNs);
		// TODO将下面的值修改为请求者在系统中的唯一标识
		ridE.setText(rid);
		method.addChild(ridE);
	
		OMElement sidE = fac.createOMElement("sid", omNs);
		// TODO将下面的值修改要请求服务的唯一标识
		sidE.setText(sid);
		method.addChild(sidE);
	
		OMElement timeoutE = fac.createOMElement("timeout", omNs);
		// TODO将下面的值修改为请求的超时时间，单位秒
		timeoutE.setText(Configurations.getStringProperty("webservice.request.timeout", "60"));
		method.addChild(timeoutE);
	
		OMElement secE = fac.createOMElement("sec", omNs);
		// TODO将下面的值修改为请求密码，如果使用其他加密方式，则根据要求统一修改即可
		secE.setText("");
		method.addChild(secE);
		return method;
	}
}
