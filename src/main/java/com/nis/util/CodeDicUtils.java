package com.nis.util;

import java.util.ArrayList;
import java.util.List;

import com.nis.domain.dashboard.codedic.CodeAppDic;
import com.nis.domain.dashboard.codedic.CodeBehaviorTypeDic;
import com.nis.domain.dashboard.codedic.CodeBrowserTypeDic;
import com.nis.domain.dashboard.codedic.CodeOsTypeDic;
import com.nis.domain.dashboard.codedic.CodeProtocolTypeDic;
import com.nis.domain.dashboard.codedic.CodeServiceTypeDic;
import com.nis.domain.dashboard.codedic.CodeWebServiceDic;
import com.nis.web.dao.dashboard.codedic.CodeAppDicDao;
import com.nis.web.dao.dashboard.codedic.CodeBehaviorTypeDicDao;
import com.nis.web.dao.dashboard.codedic.CodeBrowserTypeDicDao;
import com.nis.web.dao.dashboard.codedic.CodeOsTypeDicDao;
import com.nis.web.dao.dashboard.codedic.CodeProtocolTypeDicDao;
import com.nis.web.dao.dashboard.codedic.CodeResult;
import com.nis.web.dao.dashboard.codedic.CodeServiceTypeDicDao;
import com.nis.web.dao.dashboard.codedic.CodeWebServiceDicDao;
import com.nis.web.service.SpringContextHolder;

public class CodeDicUtils {
	private final static CodeAppDicDao codeAppDicDao = SpringContextHolder.getBean(CodeAppDicDao.class);
	private final static CodeBehaviorTypeDicDao codeBehaviorTypeDicDao = SpringContextHolder.getBean(CodeBehaviorTypeDicDao.class);
	private final static CodeBrowserTypeDicDao codeBrowserTypeDicDao = SpringContextHolder.getBean(CodeBrowserTypeDicDao.class);
	private final static CodeOsTypeDicDao codeOsTypeDicDao = SpringContextHolder.getBean(CodeOsTypeDicDao.class);
	private final static CodeProtocolTypeDicDao codeProtocolTypeDicDao = SpringContextHolder.getBean(CodeProtocolTypeDicDao.class);
	private final static CodeServiceTypeDicDao codeServiceTypeDicDao = SpringContextHolder.getBean(CodeServiceTypeDicDao.class);
	private final static CodeWebServiceDicDao codeWebServiceDicDao = SpringContextHolder.getBean(CodeWebServiceDicDao.class);

	
	private static final String APP_CODE="appCode";
	private static final String BEHAVIOR_CODE="behaviorCode";
	private static final String BROWSER_CODE="browserCode";
	private static final String OS_CODE="osCode";
	private static final String PROTOCOL_CODE="protocolCode";
	private static final String SERVICE_CODE="serviceCode";
	private static final String WEB_CODE="webCode";
	/**
	 * 适用于大屏图表标签显示
	 */
	public static List<CodeResult> getCodeList(String name) {
//		List<SysMenu> menuList = (List<SysMenu>)getCache(CACHE_MENU_LIST);
//		if (menuList == null){
//			SysUser user = getUser();
//			if (user.isAdmin()){
//				menuList = sysMenuDao.findAllList(new SysMenu());
//			}else{
//				menuList = sysMenuDao.findSysMenuByUserId(user.getId());
//			}
//			putCache(CACHE_MENU_LIST, menuList);
//		}
		//注释码表获取缓存，增加修改清除缓存功能后可以添加缓存
		List<CodeResult> result = new ArrayList<>();
		if (name.equals(APP_CODE)) {
//			List<CodeAppDic> codeDicList = (List<CodeAppDic>) CacheUtils.get(APP_CODE);
			List<CodeAppDic> codeDicList = codeAppDicDao.getCodeDicList();
//			if(StringUtil.isEmpty(codeDicList)){
//				codeDicList = codeAppDicDao.getCodeDicList();
//				CacheUtils.put(APP_CODE,codeDicList);
//			}
			if(codeDicList!=null&&codeDicList.size()>0){
				for (int i = 0; i < codeDicList.size(); i++) {
					CodeResult codeResult = new CodeResult();
					codeResult.setItem(codeDicList.get(i).getAppName());
					codeResult.setCode(codeDicList.get(i).getViewCode());
					codeResult.setCoreApp(codeDicList.get(i).getCoreApp());
					result.add(codeResult);
				}
			}
			return result;

		} else if (name.equals(BEHAVIOR_CODE)) {
//			List<CodeBehaviorTypeDic> codeDicList = (List<CodeBehaviorTypeDic>) CacheUtils.get(BEHAVIOR_CODE);
//			if(StringUtil.isEmpty(codeDicList)){
//			codeDicList = codeBehaviorTypeDicDao.getCodeDicList();
//			CacheUtils.put(BEHAVIOR_CODE,codeDicList);
//			}
			List<CodeBehaviorTypeDic> codeDicList = codeBehaviorTypeDicDao.getCodeDicList();
			if(codeDicList!=null&&codeDicList.size()>0){
				for (int i = 0; i < codeDicList.size(); i++) {
					CodeResult codeResult = new CodeResult();
					codeResult.setItem(codeDicList.get(i).getBehaviorType());
					codeResult.setCode(codeDicList.get(i).getViewCode());
					
					result.add(codeResult);
				}
			}
			return result;
		} else if (name.equals(BROWSER_CODE)) {
//			List<CodeBrowserTypeDic> codeDicList = (List<CodeBrowserTypeDic>) CacheUtils.get(BROWSER_CODE);
//			if(StringUtil.isEmpty(codeDicList)){
//				codeDicList = codeBrowserTypeDicDao.getCodeDicList();
//				CacheUtils.put(BROWSER_CODE, codeDicList);
//			}
			List<CodeBrowserTypeDic> codeDicList = codeBrowserTypeDicDao.getCodeDicList();
			if(codeDicList!=null&&codeDicList.size()>0){
				for (int i = 0; i < codeDicList.size(); i++) {
					CodeResult codeResult = new CodeResult();
					codeResult.setItem(codeDicList.get(i).getBrowserType());
					codeResult.setCode(codeDicList.get(i).getViewCode());
					
					result.add(codeResult);
				}
			}
			return result;
		} else if (name.equals(OS_CODE)) {
//			List<CodeOsTypeDic> codeDicList = (List<CodeOsTypeDic>) CacheUtils.get(OS_CODE);
//			if(StringUtil.isEmpty(codeDicList)){
//				codeDicList = codeOsTypeDicDao.getCodeDicList();
//				CacheUtils.put(OS_CODE, codeDicList);
//			}
			List<CodeOsTypeDic> codeDicList = codeOsTypeDicDao.getCodeDicList();
			if(codeDicList!=null&&codeDicList.size()>0){
				for (int i = 0; i < codeDicList.size(); i++) {
					CodeResult codeResult = new CodeResult();
					codeResult.setItem(codeDicList.get(i).getOsType());
					codeResult.setCode(codeDicList.get(i).getViewCode());
					
					result.add(codeResult);
				}
			}
			return result;
		} else if (name.equals(PROTOCOL_CODE)) {
//			List<CodeProtocolTypeDic> codeDicList = (List<CodeProtocolTypeDic>) CacheUtils.get(PROTOCOL_CODE);
//			if(StringUtil.isEmpty(codeDicList)){
//				codeDicList = codeProtocolTypeDicDao.getCodeDicList();
//				CacheUtils.put(PROTOCOL_CODE, codeDicList);
//			}
			List<CodeProtocolTypeDic> codeDicList = codeProtocolTypeDicDao.getCodeDicList();
			if(codeDicList!=null&&codeDicList.size()>0){
				for (int i = 0; i < codeDicList.size(); i++) {
					CodeResult codeResult = new CodeResult();
					codeResult.setItem(codeDicList.get(i).getProtocolType());
					codeResult.setCode(codeDicList.get(i).getViewCode());
					
					result.add(codeResult);
				}
			}
			return result;
		} else if (name.equals(SERVICE_CODE)) {
//			List<CodeServiceTypeDic> codeDicList = (List<CodeServiceTypeDic>) CacheUtils.get(SERVICE_CODE);
//			if(StringUtil.isEmpty(codeDicList)){
//				codeDicList = codeServiceTypeDicDao.getCodeDicList();
//				CacheUtils.put(SERVICE_CODE, codeDicList);
//			}
			List<CodeServiceTypeDic> codeDicList = codeServiceTypeDicDao.getCodeDicList();
			if(codeDicList!=null&&codeDicList.size()>0){
				for (int i = 0; i < codeDicList.size(); i++) {
					CodeResult codeResult = new CodeResult();
					codeResult.setItem(codeDicList.get(i).getServiceType());
					codeResult.setCode(codeDicList.get(i).getViewCode());
					
					result.add(codeResult);
				}
			}
			return result;
		} else if (name.equals(WEB_CODE)) {
//			List<CodeWebServiceDic> codeDicList = (List<CodeWebServiceDic>) CacheUtils.get(WEB_CODE);
//			if(StringUtil.isEmpty(codeDicList)){
//				codeDicList = codeWebServiceDicDao.getCodeDicList();
//				CacheUtils.put(WEB_CODE, codeDicList);
//			}
			List<CodeWebServiceDic> codeDicList = codeWebServiceDicDao.getCodeDicList();
			if(codeDicList!=null&&codeDicList.size()>0){
				for (int i = 0; i < codeDicList.size(); i++) {
					CodeResult codeResult = new CodeResult();
					codeResult.setItem(codeDicList.get(i).getWebsite());
					codeResult.setCode(codeDicList.get(i).getViewCode());
					
					result.add(codeResult);
				}
			}
			return result;
		}
		return result;
	}
	public static void main(String[] args) {
		getCodeList(APP_CODE);
	}

}
