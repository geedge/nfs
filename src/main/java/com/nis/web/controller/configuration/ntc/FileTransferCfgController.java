
package com.nis.web.controller.configuration.ntc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.taglibs.standard.functions.Functions;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nis.domain.Page;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.FileDigestCfg;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.configuration.NtcSubscribeIdCfg;
import com.nis.domain.configuration.P2pHashCfg;
import com.nis.domain.configuration.P2pKeywordCfg;
import com.nis.domain.maat.ToMaatResult;
import com.nis.exceptions.CallExternalProceduresException;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.Constants;
import com.nis.util.FileUtils;
import com.nis.util.JsonMapper;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseController;
import com.nis.web.security.UserUtils;

/**
 * 文件传输相关配置控制类
 * @author dell
 *
 */
@Controller
@RequestMapping("${adminPath}/ntc/fileTransfer/")
public class FileTransferCfgController  extends BaseController{
	@RequestMapping(value = {"ftpList"})
//	@RequiresPermissions(value={"fileTransfer:ftp:config"})
	public String ftpList(Model model,@ModelAttribute("cfg")CfgIndexInfo cfg,HttpServletRequest request,HttpServletResponse response) {
		Page<CfgIndexInfo> searchPage=new Page<CfgIndexInfo>(request,response,"a");
		Page<CfgIndexInfo> page = fileTransferCfgService.getFtpList(searchPage, cfg);
		model.addAttribute("page", page);
		initPageCondition(model,cfg);
		return "/cfg/fileTransfer/ftpList";
	}
	@RequestMapping(value = {"ftpForm"})
	@RequiresPermissions(value={"fileTransfer:ftp:config"})
	public String ftpForm(Model model,String compileIds,String ids,CfgIndexInfo entity) {
		if(StringUtils.isNotBlank(ids)){
			entity = fileTransferCfgService.getFtpCfg(Long.parseLong(ids),null);
			initUpdateFormCondition(model,entity);
		}else{
			initFormCondition(model,entity);
		}
		model.addAttribute("_cfg", entity);
		return "/cfg/fileTransfer/ftpForm";
	}
	@RequestMapping(value = {"saveFtpCfg"})
	@RequiresPermissions(value={"fileTransfer:ftp:config"})
	public String saveFtpCfg(Model model,HttpServletRequest request,HttpServletResponse response,String ids,
			CfgIndexInfo entity,RedirectAttributes redirectAttributes) {
		try {
			fileTransferCfgService.saveFtpCfg(entity);
			addMessage(redirectAttributes,"success","save_success");
		} catch (Exception e) {
			if(e instanceof MaatConvertException) {
				e.printStackTrace();
				logger.error("ftp信息保存失败",e);
				addMessage(redirectAttributes,"error","request_service_failed");
			}else {
				e.printStackTrace();
				logger.error("ftp信息保存失败",e);
				addMessage(redirectAttributes,"error","save_failed");
			}
		}
		return "redirect:" + adminPath +"/ntc/fileTransfer/ftpList?functionId="+entity.getFunctionId();
	}
	@RequestMapping(value = {"ajaxFtpSubList"})
	public String ajaxFtpSubList(Model model,Long cfgId,Integer index,Integer compileId) {
		CfgIndexInfo cfg = fileTransferCfgService.getFtpCfg(cfgId,compileId);
		List<String[]> tabList = new ArrayList();
		if(cfg.getIpPortList()!=null){
			String cfgType = null;
			for(IpPortCfg ip:cfg.getIpPortList()){
				if(!ip.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"1",ip.getCfgType()});
					cfgType = ip.getCfgType();
				}
			}
		}
		if(cfg.getStringList()!=null){
			String cfgType = null;
			for(BaseStringCfg keyword:cfg.getStringList()){
				if(!keyword.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"2",keyword.getCfgType()});
					cfgType = keyword.getCfgType();
				}
			}
		}
		//查询关键字
		if(cfg.getNtcSubscribeIdCfgList()!=null){
			String cfgType = null;
			for(NtcSubscribeIdCfg ntc:cfg.getNtcSubscribeIdCfgList()){
				if(!ntc.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"2",ntc.getCfgType()});
					cfgType = ntc.getCfgType();
				}
			}
		}
		model.addAttribute("_cfg", cfg);	
		model.addAttribute("index", index);
		model.addAttribute("tabList", tabList);
		return "/cfg/fileTransfer/ftpSubList";
	}
	/**
	 * 做删除操作
	 * @param isValid
	 * @param ids
	 * @param functionId
	 * @return
	 */
	@RequestMapping(value = {"updateFtpCfgValid"})
	@RequiresPermissions(value={"fileTransfer:ftp:config"})
	public String updateFtpCfgValid(Integer isValid,String ids,Integer functionId, RedirectAttributes redirectAttributes) {
		try {
			fileTransferCfgService.updateFtpCfgValid(isValid,ids,functionId);
			addMessage(redirectAttributes,"delete_success");
		} catch (Exception e) {
			if(e instanceof MaatConvertException) {
				e.printStackTrace();
				logger.info("ftp配置删除失败："+e.getMessage());;
				addMessage(redirectAttributes,"error","request_service_failed");
			}else {
				e.printStackTrace();
				logger.error("ftp配置删除失败",e);
				addMessage(redirectAttributes,"error","delete_failed");
			}
		}
		return "redirect:" + adminPath +"/ntc/fileTransfer/ftpList?functionId="+functionId;
	}
	@RequestMapping(value = {"auditFtpCfg"})
	@RequiresPermissions(value={"fileTransfer:ftp:confirm"})
	public String auditFtpCfg(Model model,@ModelAttribute("cfg")CfgIndexInfo cfg,
								Integer isValid,
								Integer isAudit,
								String ids,
								Integer functionId, 
								RedirectAttributes redirectAttributes,
								HttpServletResponse response,
								HttpServletRequest request) {
		if(!StringUtil.isEmpty(ids)){
			CfgIndexInfo entity = new CfgIndexInfo();
			String[] idArray = ids.split(",");
			for(String id :idArray){
				entity = fileTransferCfgService.getFtpCfg(Long.parseLong(id),null);
				entity.setIsAudit(isAudit);
				entity.setIsValid(isValid);
				entity.setAuditorId(UserUtils.getUser().getId());
				entity.setAuditTime(new Date());
				entity.setFunctionId(functionId);
				try {
					fileTransferCfgService.auditFtpCfg(entity,isAudit);
					addMessage(redirectAttributes,"success","audit_success");
				} catch ( Exception e) {
					if(e instanceof MaatConvertException) {
						e.printStackTrace();
						logger.error("ftp配置下发失败："+e.getMessage());;
						addMessage(redirectAttributes,"error","request_service_failed");
					}else {
						e.printStackTrace();
						logger.error("ftp配置下发失败",e);
						addMessage(redirectAttributes,"error","audit_failed");
					}
				}
			}
		}else {
			Page<CfgIndexInfo> searchPage=new Page<CfgIndexInfo>(request,response,"a");
			Page<CfgIndexInfo> auditPage=new Page<CfgIndexInfo>(request,response,"a");
			BeanUtils.copyProperties(searchPage, auditPage);
			
			try {
				auditAll(auditPage,isValid , cfg);
			} catch (Exception e) {
				logger.error("配置下发失败：",e);
				if(e instanceof MaatConvertException) {
					addMessage(redirectAttributes,"error", "request_service_failed");
				}else {
					addMessage(redirectAttributes,"error", "audit_failed");
				}
				
			}
			return ftpList(model, cfg, request, response);
		}
		return "redirect:" + adminPath +"/ntc/fileTransfer/ftpList?functionId="+functionId;
	}
	
	@RequestMapping(value = {"fileDigestList"})
//	@RequiresPermissions(value={"fileTransfer:fileDigest:config"})
	public String fileDigestList(Model model,@ModelAttribute("cfg")FileDigestCfg cfg
			,HttpServletRequest request,HttpServletResponse response
			,RedirectAttributes redirectAttributes) {
		Page<FileDigestCfg> searchPage=new Page<FileDigestCfg>(request,response,"a");
		Page<FileDigestCfg> page = fileTransferCfgService.getFileDigestList(searchPage, cfg);
		model.addAttribute("page", page);
		initPageCondition(model,cfg);
		return "/cfg/fileTransfer/fileDigestList";
	}
	
	@RequestMapping(value = {"fileDigestForm"})
	@RequiresPermissions(value={"fileTransfer:fileDigest:config"})
	public String fileDigestForm(Model model,String ids,FileDigestCfg entity,RedirectAttributes redirectAttributes) {
		if(StringUtils.isNotBlank(ids)){
			entity = fileTransferCfgService.getFileDigestCfg(Long.parseLong(ids),null);
			initUpdateFormCondition(model,entity);
		}else{
			initFormCondition(model,entity);
		}
		model.addAttribute("_cfg", entity);
		return "/cfg/fileTransfer/fileDigestForm";
	}
	@RequestMapping(value = {"saveFileDigestCfg"})
	@RequiresPermissions(value={"fileTransfer:fileDigest:config"})
	public String saveFileDigestCfg(Model model,HttpServletRequest request
			,HttpServletResponse response,String ids,
			FileDigestCfg entity,MultipartFile file
			,@ModelAttribute("areaCfgIds")String areaCfgIds
			,RedirectAttributes redirectAttributes) {
		try {  
			Date fileTime=null;
			Date date=new Date();
			if(!StringUtil.isEmpty(entity.getCfgId())){
				entity.setEditTime(date);
				fileTime=entity.getEditTime();
			}else{
				entity.setCreateTime(date);
				fileTime=entity.getCreateTime();
			}
			ToMaatResult result = new ToMaatResult();
			if(file!=null && file.getSize()>0 && entity!=null){
				String sep = System.getProperty("file.separator");
				String digestFilePath = request.getRealPath("/")+"digestFile";
				FileUtils.createDirectory(digestFilePath);
				String fileName = UUID.randomUUID()+FileUtils.getSuffix(file.getOriginalFilename(), true);
				File uploadFile = new File(digestFilePath+sep+fileName);
				FileCopyUtils.copy(file.getBytes(), uploadFile);
				Date creatTime = entity.getCreateTime();
				//获取文件摘要接口调用
				Map<String,Object> fileMap = new HashMap();
				fileMap.put("filetype", FileUtils.getSuffix(file.getOriginalFilename(), false));
				fileMap.put("datatype", "dbSystem");//源文件存入数据中心
				
				fileMap.put("createTime",fileTime);
				fileMap.put("key",FileUtils.getPrefix(file.getOriginalFilename(), false));
				fileMap.put("fileName", file.getOriginalFilename());
				String md5 = DigestUtils.md5Hex(file.getBytes());
				fileMap.put("checksum", md5);
				result = ConfigServiceUtil.getFileDigest(null, uploadFile, JsonMapper.toJsonString(fileMap));
				if((result.getData().getRawLen() == null) || (result.getData().getDigest() == null)) {
					logger.error("文件摘要信息获取有误！");
					throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>");
				}
				logger.info("获取文件摘要响应信息："+result);
			}			
			fileTransferCfgService.saveOrUpdateFileDigestCfg(entity,result,areaCfgIds);
			addMessage(redirectAttributes,"success","save_success");
		} catch ( Exception e) {
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,"error","request_service_failed");
				
			}else {
				addMessage(redirectAttributes,"error","save_failed");
			}
		}
		return "redirect:" + adminPath +"/ntc/fileTransfer/fileDigestList?functionId="+entity.getFunctionId();
	}
	@RequestMapping(value = {"updateFileDigestValid"})
	@RequiresPermissions(value={"fileTransfer:fileDigest:config"})
	public String updateFileDigestValid(Integer isValid,String ids,Integer functionId
			,RedirectAttributes redirectAttributes) {
		fileTransferCfgService.updateFileDigestCfgValid(isValid,ids,functionId);
		return "redirect:" + adminPath +"/ntc/fileTransfer/fileDigestList?functionId="+functionId;
	}
	
	@RequestMapping(value = {"auditFileDigestCfg"})
	@RequiresPermissions(value={"fileTransfer:fileDigest:confirm"})
	public String auditFileDigestCfg(Model model,@ModelAttribute("cfg")FileDigestCfg cfg,
									Integer isValid,
									Integer isAudit,
									String ids,
									Integer functionId, 
									RedirectAttributes redirectAttributes,
									HttpServletResponse response,
									HttpServletRequest request) {
		if(!StringUtil.isEmpty(ids)){
			String[] idArray = ids.split(",");
			Date auditTime=new Date();
			for(String id :idArray){
				try {
					fileTransferCfgService.auditFileDigestCfg(isAudit,isValid,functionId,id,auditTime);
				} catch ( Exception e) {
					logger.error("File Digest配置下发失败",e);
					if(e instanceof MaatConvertException) {
						addMessage(redirectAttributes,"error","request_service_failed");
						
					}else if(e instanceof CallExternalProceduresException) {
						addMessage(redirectAttributes,"error","call_external_procedures_failed");
					}else {
						addMessage(redirectAttributes,"error","audit_failed");
					}
					
				}
			}
			
		}else {
			Page<FileDigestCfg> searchPage=new Page<FileDigestCfg>(request,response,"a");
			Page<FileDigestCfg> auditPage=new Page<FileDigestCfg>(request,response,"a");
			BeanUtils.copyProperties(searchPage, auditPage);
			
			try {
				auditAll(auditPage,isValid , cfg);
			} catch (Exception e) {
				logger.error("配置下发失败：",e);
				if(e instanceof MaatConvertException) {
					addMessage(redirectAttributes,"error", "request_service_failed");
				}else {
					addMessage(redirectAttributes,"error", "audit_failed");
				}
				
			}
			return fileDigestList(model, cfg, request, response, redirectAttributes);
		}
		return "redirect:" + adminPath +"/ntc/fileTransfer/fileDigestList?functionId="+functionId;
	}
	@RequestMapping(value = {"ajaxFileDigestSubIdList"})
	public String ajaxFileDigestSubIdList(Model model,Long cfgId,Integer index,Integer compileId) {
		FileDigestCfg cfg = fileTransferCfgService.getFileDigestSubIdCfg(cfgId,compileId);
		List<String[]> tabList = new ArrayList();
		//查询SubscribeId域配置
		if(cfg.getNtcSubscribeIdCfgList()!=null){
			String cfgType = null;
			for(NtcSubscribeIdCfg ntc:cfg.getNtcSubscribeIdCfgList()){
				if(!ntc.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"2",ntc.getCfgType()});
					cfgType = ntc.getCfgType();
				}
			}
		}
		model.addAttribute("_cfg", cfg);	
		model.addAttribute("index", index);
		model.addAttribute("tabList", tabList);
		return "/cfg/fileTransfer/fileDigestSubList";
	}
	
	@RequestMapping(value = {"p2pList"})
	public String p2pList(Model model,@ModelAttribute("cfg")CfgIndexInfo cfg,HttpServletRequest request,HttpServletResponse response) {
		Page<CfgIndexInfo> searchPage=new Page<CfgIndexInfo>(request,response,"a");
		Page<CfgIndexInfo> page = fileTransferCfgService.getP2pList(searchPage, cfg);
		model.addAttribute("page", page);
		initPageCondition(model,cfg);
		return "/cfg/fileTransfer/p2pList";
	}
	
	@RequestMapping(value = {"p2pForm"})
	@RequiresPermissions(value={"fileTransfer:p2p:config"})
	public String p2pForm(Model model,String ids,CfgIndexInfo entity) {
		// 跳转操作页面（根据ids判断是新增   or 修改）
		if(StringUtils.isNotBlank(ids)){
			entity = fileTransferCfgService.getP2pCfg(Long.parseLong(ids),null);
			
			// 添加配置域Key,用于修改页面区分各域配置
			P2pHashCfg hashCfg = new P2pHashCfg();
			hashCfg.setCfgType(Constants.P2P_HASH_BIN_REGION);
			entity.setP2pHash(hashCfg);
			P2pKeywordCfg keywordCfg = new P2pKeywordCfg();
			keywordCfg.setCfgType(Constants.P2P_KEYWORDS_REGION);
			entity.setP2pKeyword(keywordCfg);
			NtcSubscribeIdCfg subscribeIdCfg = new NtcSubscribeIdCfg();
			subscribeIdCfg.setCfgType(Constants.NTC_SUBSCRIBE_ID_REGION);
			entity.setNtcSubscribeIdCfg(subscribeIdCfg);
			
			if(entity.getP2pHashList().size() == 0) {
				entity.getP2pHashList().add(hashCfg);
			}
			if(entity.getP2pKeywordList().size() == 0) {
				entity.getP2pKeywordList().add(keywordCfg);
			}
			if(entity.getNtcSubscribeIdCfgList().size() == 0) {
				entity.getNtcSubscribeIdCfgList().add(subscribeIdCfg);
			}
			
			initUpdateFormCondition(model,entity);
		}else{
			// 添加配置域Key,用于新增页面区分各域配置
			P2pHashCfg hashCfg = new P2pHashCfg();
			hashCfg.setCfgType(Constants.P2P_HASH_BIN_REGION);
			entity.setP2pHash(hashCfg);
			P2pKeywordCfg keywordCfg = new P2pKeywordCfg();
			keywordCfg.setCfgType(Constants.P2P_KEYWORDS_REGION);
			entity.setP2pKeyword(keywordCfg);
			NtcSubscribeIdCfg subscribeIdCfg = new NtcSubscribeIdCfg();
			subscribeIdCfg.setCfgType(Constants.NTC_SUBSCRIBE_ID_REGION);
			entity.setNtcSubscribeIdCfg(subscribeIdCfg);
			
			List<P2pHashCfg> hashList = new ArrayList<P2pHashCfg>();
			hashList.add(hashCfg);
			entity.setP2pHashList(hashList);
			List<P2pKeywordCfg> keywordList = new ArrayList<P2pKeywordCfg>();
			keywordList.add(keywordCfg);
			entity.setP2pKeywordList(keywordList);
			ArrayList<NtcSubscribeIdCfg> subscribeIdList = new ArrayList<NtcSubscribeIdCfg>();
			subscribeIdList.add(subscribeIdCfg);
			entity.setNtcSubscribeIdCfgList(subscribeIdList);
			
			initFormCondition(model,entity);
		}
		
		model.addAttribute("_cfg", entity);
		return "/cfg/fileTransfer/p2pForm";
	}
	
	@RequestMapping(value = {"saveP2pCfg"})
	@RequiresPermissions(value={"fileTransfer:p2p:config"})
	public String saveP2pCfg(Model model,HttpServletRequest request,HttpServletResponse response,String ids,CfgIndexInfo entity) {
		fileTransferCfgService.saveP2pCfg(entity);
		return "redirect:" + adminPath +"/ntc/fileTransfer/p2pList?functionId="+entity.getFunctionId();
	}
	
	@RequestMapping(value = {"updateP2pCfgValid"})
	@RequiresPermissions(value={"fileTransfer:p2p:config"})
	public String updateP2pCfgValid(Integer isValid,String ids,Integer functionId) {
		fileTransferCfgService.updateP2pCfgValid(isValid,ids,functionId);
		return "redirect:" + adminPath +"/ntc/fileTransfer/p2pList?functionId="+functionId;
	}
	
	@RequestMapping(value = {"ajaxP2pSubList"})
	public String ajaxP2pSubList(Model model,Long cfgId,Integer index,Integer compileId) {
		CfgIndexInfo cfg = fileTransferCfgService.getP2pCfg(cfgId,compileId);
		List<String[]> tabList = new ArrayList();
		if(cfg.getIpPortList()!=null){
			String cfgType = null;
			for(IpPortCfg ip:cfg.getIpPortList()){
				if(!ip.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"1",ip.getCfgType()});
					cfgType = ip.getCfgType();
				}
			}
		}
		if(cfg.getP2pHashList()!=null){
			String cfgType = null;
			for(BaseStringCfg hash:cfg.getP2pHashList()){
				if(!hash.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"2",hash.getCfgType()});
					cfgType = hash.getCfgType();
				}
			}
		}
		if(cfg.getP2pKeywordList()!=null){
			String cfgType = null;
			for(BaseStringCfg keyword:cfg.getP2pKeywordList()){
				if(!keyword.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"2",keyword.getCfgType()});
					cfgType = keyword.getCfgType();
				}
			}
		}
		if(cfg.getNtcSubscribeIdCfgList()!=null){
			String cfgType = null;
			for(BaseStringCfg subscribeId:cfg.getNtcSubscribeIdCfgList()){
				if(!subscribeId.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"2",subscribeId.getCfgType()});
					cfgType = subscribeId.getCfgType();
				}
			}
		}
		model.addAttribute("_cfg", cfg);	
		model.addAttribute("index", index);
		model.addAttribute("tabList", tabList);
		return "/cfg/fileTransfer/p2pSubList";
	}
	
	@RequestMapping(value = {"auditP2pCfg"})
	@RequiresPermissions(value={"fileTransfer:p2p:confirm"})
	public String auditP2pCfg(Model model,@ModelAttribute("cfg")CfgIndexInfo cfg,
									Integer isValid,
									Integer isAudit,
									String ids,
									Integer functionId, 
									RedirectAttributes redirectAttributes,
									HttpServletResponse response,
									HttpServletRequest request) {
		if(!StringUtil.isEmpty(ids)) {
			CfgIndexInfo entity = new CfgIndexInfo();
			String[] idArray = ids.split(",");
			for(String id :idArray){
				entity = fileTransferCfgService.getP2pCfg(Long.parseLong(id),null);
				entity.setIsAudit(isAudit);
				entity.setIsValid(isValid);
				entity.setAuditorId(UserUtils.getUser().getId());
				entity.setAuditTime(new Date());
				entity.setFunctionId(functionId);
				try {
					fileTransferCfgService.auditP2pCfg(entity,isAudit);
				} catch ( Exception e) {
					e.printStackTrace();
					logger.info("p2p配置下发失败："+e.getMessage());
					if(e instanceof MaatConvertException) {
						addMessage(redirectAttributes,"error","request_service_failed");
						
					}else if(e instanceof CallExternalProceduresException) {
						addMessage(redirectAttributes,"error","call_external_procedures_failed");
					}else {
						addMessage(redirectAttributes,"error","audit_failed");
					}
				}
			}
		}else {
			Page<CfgIndexInfo> searchPage=new Page<CfgIndexInfo>(request,response,"a");
			Page<CfgIndexInfo> auditPage=new Page<CfgIndexInfo>(request,response,"a");
			BeanUtils.copyProperties(searchPage, auditPage);
			
			try {
				auditAll(auditPage,isValid , cfg);
			} catch (Exception e) {
				logger.error("配置下发失败：",e);
				if(e instanceof MaatConvertException) {
					addMessage(redirectAttributes,"error", "request_service_failed");
				}else {
					addMessage(redirectAttributes,"error", "audit_failed");
				}
				
			}
			return p2pList(model, cfg, request, response);
		}
		return "redirect:" + adminPath +"/ntc/fileTransfer/p2pList?functionId="+functionId;
	}
	
	//ftp配置导出
			@RequestMapping(value = "exportftp")
			public void exportftp(Model model,HttpServletRequest  request,HttpServletResponse response,
					@ModelAttribute("cfg")CfgIndexInfo entity,String ids,RedirectAttributes redirectAttributes){
				try {
					//export data info
					List<String> titleList=new ArrayList<String>();
					Map<String, Class<?>> classMap=new HashMap<String, Class<?>>();
					Map<String, List> dataMap=new HashMap<String, List>();
					Map<String, String> noExportMap=new HashMap<String, String>();
					
					
					/*//导出选中记录
					 * if(!StringUtil.isEmpty(ids)){
						for(String id:ids.split(",")){
							Long.parseLong(id);
						}
						//List<CfgIndexInfo> list=ipCfgService.getListByCfgIdWithName(IpPortCfg.getTablename(), entity.getFunctionId(), ids);
					}else{*/
						//条件导出数据大于最大导出数，只导出最大导出条数
						entity.setTableName(IpPortCfg.getTablename());
						Page<CfgIndexInfo> pageInfo=new Page<CfgIndexInfo>(request, response,"a");
						pageInfo.setPageNo(1);
						pageInfo.setPageSize(Constants.MAX_EXPORT_SIZE);
						Page<CfgIndexInfo> page = fileTransferCfgService.getFtpList(pageInfo, entity);
						titleList.add(entity.getMenuNameCode());
						titleList.add("NTC_UNIVERSAL_IP");
						titleList.add("NTC_FTP_URL");								
						titleList.add("NTC_FTP_CONTENT");
						titleList.add("NTC_SUBSCRIBE_ID");
						classMap.put(entity.getMenuNameCode(), CfgIndexInfo.class);
						classMap.put("NTC_UNIVERSAL_IP", IpPortCfg.class);
						classMap.put("NTC_FTP_URL", BaseStringCfg.class);
						classMap.put("NTC_FTP_CONTENT", BaseStringCfg.class);
						classMap.put("NTC_SUBSCRIBE_ID", NtcSubscribeIdCfg.class);
						
						String cfgIndexInfoNoExport=",policy_name,group_name,userregion1,userregion2,userregion3,userregion4,userregion5,";
						String ipPortInfoNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
												+ ",config_time,editor,edit_time,auditor,audit_time"
												+",letter,whether_area_block,classification,attribute,label"
												+",userregion1,userregion2,userregion3,userregion4,userregion5,ir_type,group_name,";
						String ntcSubscribeIdCfgNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
								+ ",config_time,editor,edit_time,auditor,audit_time"
								+",letter,whether_area_block,classification,attribute,label"
								+",userregion1,userregion2,userregion3,userregion4,userregion5,";
						String regionCfgNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
								+ ",config_time,editor,edit_time,auditor,audit_time"
								+",letter,whether_area_block,classification,attribute,label"
								+",userregion1,userregion2,userregion3,userregion4,userregion5,";
						
						noExportMap.put(entity.getMenuNameCode(),cfgIndexInfoNoExport);
						noExportMap.put("NTC_UNIVERSAL_IP", ipPortInfoNoExport);
						noExportMap.put("NTC_FTP_URL", regionCfgNoExport);
						noExportMap.put("NTC_FTP_CONTENT", regionCfgNoExport);
						noExportMap.put("NTC_SUBSCRIBE_ID", ntcSubscribeIdCfgNoExport);
						List<IpPortCfg> ipList=new ArrayList<IpPortCfg>();
						List<BaseStringCfg> subscribeInfoList=new ArrayList<BaseStringCfg>();
						List<BaseStringCfg> ftpkeyList=new ArrayList<BaseStringCfg>();
						List<BaseStringCfg> ftpurlList=new ArrayList<BaseStringCfg>();
						for (CfgIndexInfo cfg : page.getList()) {
							 Map<String, List> maps=fileTransferCfgService.exportftp(cfg);
							 ftpkeyList.addAll(maps.get("NTC_FTP_CONTENT"));
							 ftpurlList.addAll(maps.get("NTC_FTP_URL"));
							 ipList.addAll(maps.get("NTC_UNIVERSAL_IP"));
							 subscribeInfoList.addAll(maps.get("NTC_SUBSCRIBE_ID"));
						}
						subscribeInfoList=BaseStringCfg.baseHexList(subscribeInfoList);
						ftpkeyList=BaseStringCfg.replaceBaseKeyList(ftpkeyList);
						ftpurlList=BaseStringCfg.replaceBaseKeyList(ftpurlList);
						dataMap.put(entity.getMenuNameCode(), page.getList());
						dataMap.put("NTC_UNIVERSAL_IP", ipList);
						dataMap.put("NTC_FTP_URL", ftpurlList);
						dataMap.put("NTC_FTP_CONTENT", ftpkeyList);
						dataMap.put("NTC_SUBSCRIBE_ID", subscribeInfoList);
					/*}*/
					this._export(model, request, response, redirectAttributes,entity.getMenuNameCode(),titleList,classMap,dataMap,noExportMap);
				} catch (Exception e) {
					logger.error("ftp export failed",e);
					addMessage(redirectAttributes,"error","export_failed");
				}
				//return "redirect:" + adminPath +"/ntc/iplist/list?functionId="+entity.getFunctionId();
			}
			
			//p2p配置导出
			@RequestMapping(value = "exportP2p")
			public void exportP2p(Model model,HttpServletRequest  request,HttpServletResponse response,
					@ModelAttribute("cfg")CfgIndexInfo entity,String ids,RedirectAttributes redirectAttributes){
				try {
					//export data info
					List<String> titleList=new ArrayList<String>();
					Map<String, Class<?>> classMap=new HashMap<String, Class<?>>();
					Map<String, List> dataMap=new HashMap<String, List>();
					Map<String, String> noExportMap=new HashMap<String, String>();
					
					
					/*//导出选中记录
					 * if(!StringUtil.isEmpty(ids)){
						for(String id:ids.split(",")){
							Long.parseLong(id);
						}
						//List<CfgIndexInfo> list=ipCfgService.getListByCfgIdWithName(IpPortCfg.getTablename(), entity.getFunctionId(), ids);
					}else{*/
						//条件导出数据大于最大导出数，只导出最大导出条数
						entity.setTableName(IpPortCfg.getTablename());
						Page<CfgIndexInfo> pageInfo=new Page<CfgIndexInfo>(request, response,"a");
						pageInfo.setPageNo(1);
						pageInfo.setPageSize(Constants.MAX_EXPORT_SIZE);
						Page<CfgIndexInfo> page = fileTransferCfgService.getP2pList(pageInfo, entity);
						
						titleList.add(entity.getMenuNameCode());
						titleList.add("NTC_UNIVERSAL_IP");
						titleList.add("NTC_P2P_KEYWORDS");								
						titleList.add("NTC_P2P_HASH_BIN");
						titleList.add("NTC_SUBSCRIBE_ID");
						classMap.put(entity.getMenuNameCode(), CfgIndexInfo.class);
						classMap.put("NTC_UNIVERSAL_IP", IpPortCfg.class);
						classMap.put("NTC_P2P_KEYWORDS", BaseStringCfg.class);
						classMap.put("NTC_P2P_HASH_BIN", BaseStringCfg.class);
						classMap.put("NTC_SUBSCRIBE_ID", NtcSubscribeIdCfg.class);
						
						String cfgIndexInfoNoExport=",policy_name,group_name,userregion1,userregion2,userregion3,userregion4,userregion5,";
						String ipPortInfoNoExport=",do_log,action,config_describe,valid_identifier,is_audit,ir_type,creator,creator"
												+ ",config_time,editor,edit_time,auditor,audit_time"
												+",letter,whether_area_block,classification,attribute,label"
												+",userregion2,userregion3,userregion4,userregion5,group_name,&userregion1:p2p_ip_config_type-";
						String ntcSubscribeIdCfgNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
								+ ",config_time,editor,edit_time,auditor,audit_time"
								+",letter,whether_area_block,classification,attribute,label"
								+",userregion1,userregion2,userregion3,userregion4,userregion5,";
						String hashCfgNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
								+ ",config_time,editor,edit_time,auditor,audit_time"
								+",letter,whether_area_block,classification,attribute,label"
								+",userregion2,userregion3,userregion4,userregion5,&userregion1:p2p_hash_type-";
						String keyCfgNoExport=",do_log,action,config_describe,valid_identifier,is_audit,creator,creator"
								+ ",config_time,editor,edit_time,auditor,audit_time"
								+",letter,whether_area_block,classification,attribute,label"
								+",userregion1,userregion2,userregion3,userregion4,userregion5,";
						
						noExportMap.put(entity.getMenuNameCode(),cfgIndexInfoNoExport);
						noExportMap.put("NTC_UNIVERSAL_IP", ipPortInfoNoExport);
						noExportMap.put("NTC_P2P_KEYWORDS", keyCfgNoExport);
						noExportMap.put("NTC_P2P_HASH_BIN", hashCfgNoExport);
						noExportMap.put("NTC_SUBSCRIBE_ID", ntcSubscribeIdCfgNoExport);
						List<IpPortCfg> ipList=new ArrayList<IpPortCfg>();
						List<BaseStringCfg> subscribeInfoList=new ArrayList<BaseStringCfg>();
						List<BaseStringCfg> hashList=new ArrayList<BaseStringCfg>();
						List<BaseStringCfg> keywordList=new ArrayList<BaseStringCfg>();
						for (CfgIndexInfo cfg : page.getList()) {
							 Map<String, List> maps=fileTransferCfgService.exportp2p(cfg);
							 keywordList.addAll(maps.get("NTC_P2P_KEYWORDS"));
							 hashList.addAll(maps.get("NTC_P2P_HASH_BIN"));
							 ipList.addAll(maps.get("NTC_UNIVERSAL_IP"));
							 subscribeInfoList.addAll(maps.get("NTC_SUBSCRIBE_ID"));
						}
						subscribeInfoList=BaseStringCfg.baseHexList(subscribeInfoList);
						keywordList=BaseStringCfg.replaceBaseKeyList(keywordList);
						hashList=BaseStringCfg.replaceBaseKeyList(hashList);
						dataMap.put(entity.getMenuNameCode(), page.getList());
						dataMap.put("NTC_UNIVERSAL_IP", ipList);
						dataMap.put("NTC_P2P_KEYWORDS", keywordList);
						dataMap.put("NTC_P2P_HASH_BIN", hashList);
						dataMap.put("NTC_SUBSCRIBE_ID", subscribeInfoList);
					/*}*/
					this._export(model, request, response, redirectAttributes,entity.getMenuNameCode(),titleList,classMap,dataMap,noExportMap);
				} catch (Exception e) {
					logger.error("p2p export failed",e);
					addMessage(redirectAttributes,"error","export_failed");
				}
				//return "redirect:" + adminPath +"/ntc/iplist/list?functionId="+entity.getFunctionId();
			}
			
			
			//file配置导出
			@RequestMapping(value = "exportFile")
			public void exportFile(Model model,HttpServletRequest  request,HttpServletResponse response,
					@ModelAttribute("cfg")FileDigestCfg entity,String ids,RedirectAttributes redirectAttributes){
				try {
					//export data info
					List<String> titleList=new ArrayList<String>();
					Map<String, Class<?>> classMap=new HashMap<String, Class<?>>();
					Map<String, List> dataMap=new HashMap<String, List>();
					Map<String, String> noExportMap=new HashMap<String, String>();
					
					
					/*//导出选中记录
					 * if(!StringUtil.isEmpty(ids)){
						for(String id:ids.split(",")){
							Long.parseLong(id);
						}
						//List<CfgIndexInfo> list=ipCfgService.getListByCfgIdWithName(IpPortCfg.getTablename(), entity.getFunctionId(), ids);
					}else{*/
						//条件导出数据大于最大导出数，只导出最大导出条数
						entity.setTableName(IpPortCfg.getTablename());
						Page<FileDigestCfg> pageInfo=new Page<FileDigestCfg>(request, response,"a");
						pageInfo.setPageNo(1);
						pageInfo.setPageSize(Constants.MAX_EXPORT_SIZE);
						Page<FileDigestCfg> page = fileTransferCfgService.getFileDigestList(pageInfo, entity);
						titleList.add(entity.getMenuNameCode());
						classMap.put(entity.getMenuNameCode(), FileDigestCfg.class);
						String cfgIndexInfoNoExport=",policy_name,group_name,userregion1,userregion2,userregion3,userregion4,userregion5,";
						noExportMap.put(entity.getMenuNameCode(),cfgIndexInfoNoExport);
						dataMap.put(entity.getMenuNameCode(), page.getList());
					/*}*/
					this._export(model, request, response, redirectAttributes,entity.getMenuNameCode(),titleList,classMap,dataMap,noExportMap);
				} catch (Exception e) {
					logger.error("file export failed",e);
					addMessage(redirectAttributes,"error","export_failed");
				}
				//return "redirect:" + adminPath +"/ntc/iplist/list?functionId="+entity.getFunctionId();
			}
}
