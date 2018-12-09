package com.nis.web.service.log;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service
public class CodeDicService {
	@Autowired
	private static CodeAppDicDao codeAppDicDao;
	@Autowired
	private static CodeBehaviorTypeDicDao codeBehaviorTypeDicDao;
	@Autowired
	private static CodeBrowserTypeDicDao codeBrowserTypeDicDao;
	@Autowired
	private static CodeOsTypeDicDao codeOsTypeDicDao;
	@Autowired
	private static CodeProtocolTypeDicDao codeProtocolTypeDicDao;
	@Autowired
	private static CodeServiceTypeDicDao codeServiceTypeDicDao;
	@Autowired
	private static CodeWebServiceDicDao codeWebServiceDicDao;

	
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
		List<CodeResult> result = new ArrayList<>();
		if (name.equals(APP_CODE)) {
			List<CodeAppDic> codeDicList = codeAppDicDao.getCodeDicList();
			CodeResult codeResult = new CodeResult();
			for (int i = 0; i < codeDicList.size(); i++) {
				codeResult.setItem(codeDicList.get(i).getAppName());
				codeResult.setCode(codeDicList.get(i).getViewCode());
				codeResult.setCoreApp(codeDicList.get(i).getCoreApp());
				result.add(codeResult);
			}
			return result;

		} else if (name.equals(BEHAVIOR_CODE)) {
			List<CodeBehaviorTypeDic> codeDicList = codeBehaviorTypeDicDao.getCodeDicList();
			CodeResult codeResult = new CodeResult();
			for (int i = 0; i < codeDicList.size(); i++) {
				codeResult.setItem(codeDicList.get(i).getBehaviorType());
				codeResult.setCode(codeDicList.get(i).getViewCode());
				
				result.add(codeResult);
			}
			return result;
		} else if (name.equals(BROWSER_CODE)) {
			List<CodeBrowserTypeDic> codeDicList = codeBrowserTypeDicDao.getCodeDicList();
			CodeResult codeResult = new CodeResult();
			for (int i = 0; i < codeDicList.size(); i++) {
				codeResult.setItem(codeDicList.get(i).getBrowserType());
				codeResult.setCode(codeDicList.get(i).getViewCode());
				
				result.add(codeResult);
			}
			return result;
		} else if (name.equals(OS_CODE)) {
			List<CodeOsTypeDic> codeDicList = codeOsTypeDicDao.getCodeDicList();
			CodeResult codeResult = new CodeResult();
			for (int i = 0; i < codeDicList.size(); i++) {
				codeResult.setItem(codeDicList.get(i).getOsType());
				codeResult.setCode(codeDicList.get(i).getViewCode());
				
				result.add(codeResult);
			}
			return result;
		} else if (name.equals(PROTOCOL_CODE)) {
			List<CodeProtocolTypeDic> codeDicList = codeProtocolTypeDicDao.getCodeDicList();
			CodeResult codeResult = new CodeResult();
			for (int i = 0; i < codeDicList.size(); i++) {
				codeResult.setItem(codeDicList.get(i).getProtocolType());
				codeResult.setCode(codeDicList.get(i).getViewCode());
				
				result.add(codeResult);
			}
			return result;
		} else if (name.equals(SERVICE_CODE)) {
			List<CodeServiceTypeDic> codeDicList = codeServiceTypeDicDao.getCodeDicList();
			CodeResult codeResult = new CodeResult();
			for (int i = 0; i < codeDicList.size(); i++) {
				codeResult.setItem(codeDicList.get(i).getServiceType());
				codeResult.setCode(codeDicList.get(i).getViewCode());
				
				result.add(codeResult);
			}
			return result;
		} else if (name.equals(WEB_CODE)) {
			List<CodeWebServiceDic> codeDicList = codeWebServiceDicDao.getCodeDicList();
			CodeResult codeResult = new CodeResult();
			for (int i = 0; i < codeDicList.size(); i++) {
				codeResult.setItem(codeDicList.get(i).getWebsite());
				codeResult.setCode(codeDicList.get(i).getViewCode());
				
				result.add(codeResult);
			}
			return result;
		}
		return result;
	}
	public static void main(String[] args) {
		getCodeList(APP_CODE);
	}
}
