package com.nis.util;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.method.HandlerMethod;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nis.domain.SysDataDictionaryItem;
import com.nis.domain.SysLog;
import com.nis.domain.SysMenu;
import com.nis.domain.SysUser;
import com.nis.web.dao.SysLogDao;
import com.nis.web.dao.SysMenuDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.SpringContextHolder;
import com.sun.corba.se.impl.orbutil.closure.Constant;


public class LogUtils {
	
	public static final String CACHE_MENU_NAME_PATH_MAP = "menuNamePathMap";
	public static final String CACHE_MENU_NAME_PATH_MAP2 = "menuNamePathMap2";
	
	private static SysLogDao logDao = SpringContextHolder.getBean(SysLogDao.class);
	private static SysMenuDao menuDao = SpringContextHolder.getBean(SysMenuDao.class);
	
	/**
	 * 保存日志
	 */
	public static void saveLog(HttpServletRequest request, String title){
		saveLog(request, null, null, title, 0);
	}
	
	/**
	 * 保存日志
	 */
	public static void saveLog(HttpServletRequest request, Object handler, Exception ex, String title){
		saveLog(request, handler, ex, title, 0);
	}
	
	public static void saveLog(HttpServletRequest request, Object handler, Exception ex, String title, long consumerTime){
		SysUser user = UserUtils.getUser();
		if (user != null && user.getId() != null){
			SysLog log = new SysLog(); 
			log.setType(getOperateType(handler));
			log.setTitle(title);
			log.setConsumerTime(consumerTime);
			log.setState(ex == null ? Constants.LOG_ACCESS_SUCCESS : Constants.LOG_ACCESS_EXCEPTION);
			log.setRemoteAddr(StringUtils.getRemoteAddr(request));
			log.setUserAgent(request.getHeader("user-agent"));
			log.setRequestUri(request.getRequestURI());
			log.setParams(request.getParameterMap());
			log.setMethod(request.getMethod());
			log.setCreateBy(user.getName());
			log.setCreateDate(new Date());
			if(StringUtil.isEmpty(request.getParameter("functionId"))){
				log.setFunctionId(0);
			}else{
				log.setFunctionId(Integer.parseInt(request.getParameter("functionId")));
			}
			if(!StringUtil.isEmpty(request.getParameter("compileId"))){
				log.setCompileIds(request.getParameter("compileId"));
			}else{
				log.setCompileIds(request.getParameter("compileIds"));
			}
			
			if(!StringUtil.isEmpty(request.getParameter("cfgId"))){
				log.setCfgIds(request.getParameter("cfgId"));
			}else{
				log.setCfgIds(request.getParameter("ids"));
			}
			if(!StringUtil.isEmpty(request.getParameter("isAudit"))){
				log.setAuditState(Integer.parseInt(request.getParameter("isAudit")));
			}
			if(!StringUtil.isEmpty(request.getParameter("action"))){
				log.setAction(Integer.parseInt(request.getParameter("action")));
			}
			if(!(StringUtil.isEmpty(request.getParameter("isValid")))){
				log.setIsValid(Integer.parseInt(request.getParameter("isValid")));
			}
			// 异步保存日志
			new SaveLogThread(log, handler, ex).start();
		}
	}

	/**
	 * 保存日志线程
	 */
	public static class SaveLogThread extends Thread{
		
		private SysLog log;
		private Object handler;
		private Exception ex;
		
		public SaveLogThread(SysLog log, Object handler, Exception ex){
			super(SaveLogThread.class.getSimpleName());
			this.log = log;
			this.handler = handler;
			this.ex = ex;
		}
		
		@Override
		public void run() {
			// 获取日志标题
			if (StringUtils.isBlank(log.getTitle())){
				String permission = "";
				if (handler instanceof HandlerMethod){
					Method m = ((HandlerMethod)handler).getMethod();
					log.setMethodName(m.getName());
					RequiresPermissions rp = m.getAnnotation(RequiresPermissions.class);
					permission = (rp != null ? StringUtils.join(rp.value(), ",") : "");
				}
				Integer functionId = log.getFunctionId();
				log.setTitle(getMenuNamePath(log.getRequestUri(), permission,functionId));
			}
			// 如果有异常，设置异常信息
			log.setException(Exceptions.getStackTraceAsString(ex));
			// 如果无标题并无异常日志并且无具体的功能id(非业务配置的操作），则不保存信息
			if ((StringUtils.isBlank(log.getTitle()) && StringUtils.isBlank(log.getException()) && (log.getFunctionId()==null ||  log.getFunctionId()==0))){
				return;
			}
			// 保存日志信息
			if(log.getFunctionId()!=null 
					&& log.getFunctionId()!=0 ){
				String functionName = DictUtils.getDictLabel("SYS_SERVICE_FUNCTION", log.getFunctionId().toString());
				if(!StringUtil.isEmpty(log.getMethodName())){
					String operation = DictUtils.getDictLabel("SYS_SERVICE_OPERATION", log.getMethodName());
					if("saveOrUpdate".equals(operation)){
						if (log.getCfgId()!=null || log.getCfgIds()!=null || log.getCompileId()!=null || log.getCompileIds()!=null){
							log.setOperation("edit");
						}else{
							log.setOperation("add");
						}
					}else{
						log.setOperation(operation);
					}
				}
				if(log.getOperation()!=null && !"默认".equals(log.getOperation()) && !"默认".equals(functionName)){
					log.setFunctionName(functionName);
					if(!StringUtil.isEmpty(log.getCompileIds())){
						for(String compileId:StringUtils.split(log.getCompileIds(),",")){
							log.setCompileId(Integer.parseInt(compileId));
							logDao.insertCfgOperationLog(log);
						}
					}else if(!StringUtil.isEmpty(log.getCfgIds())){
						for(String cfgId:StringUtils.split(log.getCfgIds(),",")){
							log.setCfgId(Integer.parseInt(cfgId));
							logDao.insertCfgOperationLog(log);
						}
					}else{
						logDao.insertCfgOperationLog(log);
					}
				}
			}
			
			logDao.insert(log);
		}
	}

	
	
	public static int getOperateType(Object handler) {
		int type = Constants.DEFAULT_METHOD_TYPE;
		
		if (!StringUtil.isEmpty(handler)) {
			if (handler instanceof HandlerMethod) {
				Method m = ((HandlerMethod)handler).getMethod();
				String methodName = m.getName();
				List<SysDataDictionaryItem> dictList = DictUtils.getDictList("SYS_LOG_TYPE");
				for(SysDataDictionaryItem sysDataDictionaryItem:dictList){
					if(methodName.toLowerCase().matches(sysDataDictionaryItem.getItemCode().toLowerCase()+".*")){
						type = Integer.parseInt(sysDataDictionaryItem.getItemValue());
						break;
					}
				}	
			}
			
		}
		
		
		return type;
	}
	
	
	
	/**
	 * 获取菜单名称路径（如：系统设置-机构用户-用户管理-编辑）
	 */
	public static String getMenuNamePath(String requestUri, String permission,Integer functionId){
		String href = StringUtils.substringAfter(requestUri, Configurations.getStringProperty("adminPath", "/nis"));
		@SuppressWarnings("unchecked")
		Map<String, String> menuMap = (Map<String, String>)CacheUtils.get(CACHE_MENU_NAME_PATH_MAP);
		//用于记录功能按钮的操作日志【新增，修改，删除，审核等】
		Map<String, String> menuMap2 = (Map<String, String>)CacheUtils.get(CACHE_MENU_NAME_PATH_MAP2);
		if (menuMap == null){
			menuMap = Maps.newHashMap();
			menuMap2 = Maps.newHashMap();
			Properties msgProp = new Properties();
			try {
				msgProp.load(Configurations.class.getResourceAsStream("/messages/message_en.properties"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			List<SysMenu> menuList = menuDao.findAllList(new SysMenu());
			for (SysMenu menu : menuList){
				// 获取菜单名称路径（如：系统设置-机构用户-用户管理-编辑）
				String namePath = "";
				if (menu.getParentIds() != null){
					List<String> namePathList = Lists.newArrayList();
					for (String id : StringUtils.split(menu.getParentIds(), ",")){
						
						
						if (SysMenu.getRootId().equals(Long.valueOf(id))){
							continue; // 过滤跟节点
						}
						for (SysMenu m : menuList){
							if (m.getId().equals(Long.valueOf(id))){
//								namePathList.add(m.getName());
								namePathList.add(msgProp.getProperty(m.getCode())==null?m.getCode():msgProp.getProperty(m.getCode()));
								break;
							}
						}
						
					}
//					namePathList.add(menu.getName());
					namePathList.add(msgProp.getProperty(menu.getCode())==null?menu.getCode():msgProp.getProperty(menu.getCode()));
					namePath = StringUtils.join(namePathList, "-");
				}
				// 设置菜单名称路径
				if (StringUtils.isNotBlank(menu.getHref())){
					if(!StringUtil.isEmpty(menu.getFunctionId())&&menu.getFunctionId()!=0){
						menuMap.put(menu.getHref()+"?functionId"+menu.getFunctionId(), namePath);
						menuMap2.put("functionId"+menu.getFunctionId(), namePath);//用于记录功能按钮的操作日志
					}else{
						menuMap.put(menu.getHref(), namePath);
					}
				}else if (StringUtils.isNotBlank(menu.getPermission())){
					for (String p : StringUtils.split(menu.getPermission())){
						if(!StringUtil.isEmpty(menu.getFunctionId())&&menu.getFunctionId()!=0){
							menuMap.put(p+"?functionId"+menu.getFunctionId(), namePath);
							menuMap2.put("functionId"+menu.getFunctionId(), namePath);//用于记录功能按钮的操作日志
						}else{
							menuMap.put(p, namePath);
						}
							
					}
				}
			}
			CacheUtils.put(CACHE_MENU_NAME_PATH_MAP, menuMap);
			CacheUtils.put(CACHE_MENU_NAME_PATH_MAP2, menuMap2);
		}
		String menuNamePath = null;
		if(functionId==0){
			menuNamePath = menuMap.get(href);
		}else{
			menuNamePath = menuMap.get(href+"?functionId"+functionId);
			if(menuNamePath==null){
				menuNamePath = menuMap2.get("functionId"+functionId);
				/*String methodName = href.substring(href.lastIndexOf("/")+1).toLowerCase();
				if(!StringUtil.isEmpty(methodName)){
					if(methodName.contains("insert")||methodName.contains("save")||methodName.contains("update")||methodName.contains("edit")){
						menuNamePath +="-"+"Save or Update";
					}else if(methodName.contains("valid")){
						menuNamePath +="-"+"Delete";
					}else if(methodName.contains("audit")){
						menuNamePath +="-"+"Confirmation";
					}
					
				}*/
			}
		}
		if (menuNamePath == null){
			for (String p : StringUtils.split(permission)){
				if(functionId!=0){
					p += "?functionId"+functionId;
				}
				menuNamePath = menuMap.get(p);
				if (StringUtils.isNotBlank(menuNamePath)){
					break;
				}
			}
			if (menuNamePath == null){
				return "";
			}
		}
		return menuNamePath;
	}

	
}
