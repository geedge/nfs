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
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nis.domain.Page;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.ComplexkeywordCfg;
import com.nis.domain.configuration.FileDigestCfg;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.configuration.NtcSubscribeIdCfg;
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
 * 邮件相关配置控制类
 * @author dell
 *
 */
@Controller
@RequestMapping("${adminPath}/ntc/mail/")
public class MailCfgController  extends BaseController{
	@RequestMapping(value = {"mailList"})
//	@RequiresPermissions(value={"mail:config"})
	public String mailList(Model model,@ModelAttribute("cfg")CfgIndexInfo cfg,HttpServletRequest request,HttpServletResponse response) {
		Page<CfgIndexInfo> searchPage=new Page<CfgIndexInfo>(request,response,"a");
		Page<CfgIndexInfo> page = mailCfgService.getMailList(searchPage, cfg);
		model.addAttribute("page", page);
		initPageCondition(model,cfg);
		return "/cfg/mail/mailList";
	}
	@RequestMapping(value = {"mailForm"})
	@RequiresPermissions(value={"mail:config"})
	public String mailForm(Model model,String ids,CfgIndexInfo entity) {
		if(StringUtils.isNotBlank(ids)){
			entity = mailCfgService.getMailCfg(Long.parseLong(ids),null);
			initUpdateFormCondition(model,entity);
		}else{
			initFormCondition(model,entity);
		}
		model.addAttribute("_cfg", entity);
		return "/cfg/mail/mailForm";
	}
	@RequestMapping(value = {"saveMailCfg"})
	@RequiresPermissions(value={"mail:config"})
	public String saveMailCfg( RedirectAttributes redirectAttributes, Model model,HttpServletRequest request,HttpServletResponse response,String ids,
			CfgIndexInfo entity,MultipartFile file) {
		
		try {
			ToMaatResult result = null;
			if(file!=null && file.getSize()>0 && entity.getDigestList()!=null && entity.getDigestList().size()>0){
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
				
				fileMap.put("createTime",creatTime);
				fileMap.put("key",FileUtils.getPrefix(file.getOriginalFilename(), false));
				fileMap.put("fileName", file.getOriginalFilename());
				String md5 = DigestUtils.md5Hex(file.getBytes());
				fileMap.put("checksum", md5);
				result = ConfigServiceUtil.getFileDigest(null, uploadFile, JsonMapper.toJsonString(fileMap));
				logger.info("获取文件摘要响应信息："+result);
			}			
			mailCfgService.saveMailCfg(entity,result);
		} catch ( Exception e) {
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,"error","request_service_failed");
				
			}else if(e instanceof CallExternalProceduresException) {
				addMessage(redirectAttributes,"error","call_external_procedures_failed");
			}else {
				addMessage(redirectAttributes,"error","save_failed");
			}
		}
		
		return "redirect:" + adminPath +"/ntc/mail/mailList?functionId="+entity.getFunctionId();
	}
	@RequestMapping(value = {"ajaxMailSubList"})
	public String ajaxMailSubList(Model model,Long cfgId,Integer index,Integer compileId) {
		CfgIndexInfo cfg = mailCfgService.getMailCfg(cfgId,compileId);
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
		if(cfg.getComplexList()!=null){
			String cfgType = null;
			for(ComplexkeywordCfg keyword:cfg.getComplexList()){
				if(!keyword.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"3",keyword.getCfgType()});
					cfgType = keyword.getCfgType();
				}
			}
		}
		if(cfg.getDigestList()!=null){
			String cfgType = null;
			for(FileDigestCfg digest:cfg.getDigestList()){
				if(!digest.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"5",digest.getCfgType()});
					cfgType = digest.getCfgType();
				}
			}
		}
		if(cfg.getNtcSubscribeIdCfgList()!=null){
			String cfgType = null;
			for(NtcSubscribeIdCfg keyword:cfg.getNtcSubscribeIdCfgList()){
				if(!keyword.getCfgType().equals(cfgType)){
					tabList.add(new String[]{"2",keyword.getCfgType()});
					cfgType = keyword.getCfgType();
				}
			}
		}
		model.addAttribute("_cfg", cfg);	
		model.addAttribute("index", index);
		model.addAttribute("tabList", tabList);
		return "/cfg/mail/mailSubList";
	}
	@RequestMapping(value = {"updateMailCfgValid"})
	@RequiresPermissions(value={"mail:config"})
	public String updateMailCfgValid(Integer isValid,String ids,Integer functionId) {
		mailCfgService.updateMailCfgValid(isValid,ids,functionId);
		return "redirect:" + adminPath +"/ntc/mail/mailList?functionId="+functionId;
	}
	@RequestMapping(value = {"auditMailCfg"})
	@RequiresPermissions(value={"mail:confirm"})
	public String auditMailCfg(Model model,@ModelAttribute("cfg")CfgIndexInfo cfg,
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
				entity = mailCfgService.getMailCfg(Long.parseLong(id),null);
				entity.setIsAudit(isAudit);
				entity.setIsValid(isValid);
				entity.setAuditorId(UserUtils.getUser().getId());
				entity.setAuditTime(new Date());
				entity.setFunctionId(functionId);
				try {
					mailCfgService.auditMailCfg(entity,isAudit);
				} catch ( Exception e) {
					e.printStackTrace();
					logger.error("mail配置下发失败："+e.getMessage());
					if(e instanceof MaatConvertException) {
						addMessage(redirectAttributes,"error","request_service_failed");
						
					}else if(e instanceof CallExternalProceduresException) {
						addMessage(redirectAttributes,"error","call_external_procedures_failed");
					}else {
						addMessage(redirectAttributes,"error","save_failed");
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
			return mailList(model, cfg, request, response);
		}
		return "redirect:" + adminPath +"/ntc/mail/mailList?functionId="+functionId;
	}
	
	//mail配置导出
		@RequestMapping(value = "exportmail")
		public void exportmail(Model model,HttpServletRequest  request,HttpServletResponse response,
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
					Page<CfgIndexInfo> page = mailCfgService.getMailList(pageInfo, entity);
					titleList.add(entity.getMenuNameCode());
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
					if(entity.getFunctionId()!=null){
						if(entity.getFunctionId()!=37){
							titleList.add("NTC_IP");
							titleList.add("NTC_MAIL_BODY");
							
							classMap.put("NTC_IP", IpPortCfg.class);
							classMap.put("NTC_MAIL_BODY", ComplexkeywordCfg.class);
							
							noExportMap.put("NTC_IP", ipPortInfoNoExport);
							noExportMap.put("NTC_MAIL_BODY", regionCfgNoExport);
							
						}
					}
					titleList.add("NTC_MAIL_HDR");
					titleList.add("NTC_SUBSCRIBE_ID");
					classMap.put(entity.getMenuNameCode(), CfgIndexInfo.class);
					classMap.put("NTC_MAIL_HDR", ComplexkeywordCfg.class);
					classMap.put("NTC_SUBSCRIBE_ID", NtcSubscribeIdCfg.class);
					noExportMap.put(entity.getMenuNameCode(),cfgIndexInfoNoExport);
					noExportMap.put("NTC_MAIL_HDR", regionCfgNoExport);
					noExportMap.put("NTC_SUBSCRIBE_ID", ntcSubscribeIdCfgNoExport);
					List<IpPortCfg> ipList=new ArrayList<IpPortCfg>();
					List<BaseStringCfg> subscribeInfoList=new ArrayList<BaseStringCfg>();
					List<ComplexkeywordCfg> mailBodyList=new ArrayList<ComplexkeywordCfg>();
					List<ComplexkeywordCfg> mailReqHdrList=new ArrayList<ComplexkeywordCfg>();
					for (CfgIndexInfo cfg : page.getList()) {
						 Map<String, List> maps=mailCfgService.exportmail(cfg);
						 mailReqHdrList.addAll(maps.get("NTC_MAIL_HDR"));
						 mailBodyList.addAll(maps.get("NTC_MAIL_BODY"));
						 ipList.addAll(maps.get("NTC_UNIVERSAL_IP"));
						 subscribeInfoList.addAll(maps.get("NTC_SUBSCRIBE_ID"));
					}
					subscribeInfoList=BaseStringCfg.baseHexList(subscribeInfoList);
					mailBodyList=ComplexkeywordCfg.replaceComplexKeyList(mailBodyList);
					mailReqHdrList=ComplexkeywordCfg.replaceComplexKeyList(mailReqHdrList);
					if(entity.getFunctionId()!=null){
						if(entity.getFunctionId()!=37){
							dataMap.put("NTC_IP", ipList);
							dataMap.put("NTC_MAIL_BODY", mailBodyList);
							
						}
					}
					dataMap.put(entity.getMenuNameCode(), page.getList());
					dataMap.put("NTC_MAIL_HDR", mailReqHdrList);
					dataMap.put("NTC_SUBSCRIBE_ID", subscribeInfoList);
				/*}*/
				this._export(model, request, response, redirectAttributes,entity.getMenuNameCode(),titleList,classMap,dataMap,noExportMap);
			} catch (Exception e) {
				logger.error("mail export failed",e);
				addMessage(redirectAttributes,"error","export_failed");
			}
			//return "redirect:" + adminPath +"/ntc/iplist/list?functionId="+entity.getFunctionId();
		}
		
}
