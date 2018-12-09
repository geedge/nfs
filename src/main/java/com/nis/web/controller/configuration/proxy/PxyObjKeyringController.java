package com.nis.web.controller.configuration.proxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.Principal;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;
import com.nis.domain.Page;
import com.nis.domain.basics.PolicyGroupInfo;
import com.nis.domain.configuration.DnsResStrategy;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.configuration.PxyObjKeyring;
import com.nis.domain.configuration.PxyObjTrustedCaCert;
import com.nis.domain.configuration.PxyObjTrustedCaCrl;
import com.nis.domain.maat.ToMaatResult;
import com.nis.domain.maat.ToMaatResult.ResponseData;
import com.nis.exceptions.CallExternalProceduresException;
import com.nis.exceptions.MaatConvertException;
import com.nis.exceptions.MultiPartNewException;
import com.nis.util.ConfigDictUtils;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.Constants;
import com.nis.util.FileUtils;
import com.nis.util.JsonMapper;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseController;

/**
 * 拦截策略
 * @author ddm
 *
 */
@Controller
@RequestMapping("${adminPath}/proxy/intercept/strateagy")
public class PxyObjKeyringController extends BaseController  {
	public Map certInfoMap=new HashMap<>();  
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@RequestMapping(value = {"/form"})
	@RequiresPermissions(value={"proxy:intercept:config"})
	public String from(Model model,
							HttpServletRequest request,
							HttpServletResponse response,
							String ids,
							@ModelAttribute("cfg")PxyObjKeyring cfg
							,RedirectAttributes redirectAttributes){
		if(cfg == null){
			cfg=new PxyObjKeyring();
		}
		if(!StringUtil.isEmpty(ids)){
			cfg = pxyObjKeyringService.getPxyObjKeyring(Long.valueOf(ids),-1);
			initFormCondition(model, cfg);
			model.addAttribute("isAdd", false);
		}else{
			initFormCondition(model, cfg);
			model.addAttribute("isAdd", true);
		}
		
		model.addAttribute("_cfg", cfg);
		return "/cfg/intercept/strateagy/form";
	}
	
	@RequestMapping(value = {"/saveOrUpdate"})
	@RequiresPermissions(value={"proxy:intercept:config"})
	public String saveOrUpdate(Model model,HttpServletRequest request,HttpServletResponse response,
			@ModelAttribute("cfg")PxyObjKeyring cfg,
			MultipartFile privateKeyFileI, 
			MultipartFile publicKeyFileI,
			RedirectAttributes redirectAttributes){
 		File file = null;
 		boolean validFlag=true;
 		try {
 			boolean publicKeyFileflag=validCertFileContent(publicKeyFileI,"-incert");
 			boolean privateKeyFileflag=validCertFileContent(privateKeyFileI,"-inkey");
 			if(!publicKeyFileflag && !privateKeyFileflag){
 				addMessage(redirectAttributes,"error","save_failed");
 				logger.error(publicKeyFileI.getOriginalFilename()+" and "+privateKeyFileI.getOriginalFilename()+" file non certificate file format ");
 				throw new MultiPartNewException(
 												this.getMsgProp().getProperty("certificate_error")
 												);
 			}else if(!publicKeyFileflag){
 				addMessage(redirectAttributes,"error","save_failed");
 				logger.error(publicKeyFileI.getOriginalFilename()+" file non public key file format ");
 				throw new MultiPartNewException(this.getMsgProp().getProperty("public_file_error"));
 			}else if(!privateKeyFileflag){
 				addMessage(redirectAttributes,"error","save_failed");
 				logger.error(privateKeyFileI.getOriginalFilename()+" file non private key file format ");
 				throw new MultiPartNewException(this.getMsgProp().getProperty("private_file_error"));
 			}
		} catch (Exception e) {
			validFlag=false;
			logger.error("证书文件校验失败",e);
			if(e instanceof MaatConvertException) {
 				addMessage(redirectAttributes,"error","request_service_failed");
 			}else if(e instanceof MaatConvertException){
 				addMessage(redirectAttributes,"error",e.getMessage());
 			}else {
 				addMessage(redirectAttributes,"error","save_failed");
 			}
		}
 		
 		try{
 			if(validFlag){
 				validFlag=true;
				if(publicKeyFileI != null) {
					// 获取公钥信息
					if(certInfoMap != null && certInfoMap.size() >0){
						
						String issuer=StringUtil.isEmpty(certInfoMap.get("ca issuer")) ? "":certInfoMap.get("ca issuer").toString();//颁发者
						String subject=StringUtil.isEmpty(certInfoMap.get("ca subjectname")) ? "":certInfoMap.get("ca subjectname").toString();//颁发给
						String notBeforeStr="";
						if (!StringUtil.isEmpty(certInfoMap.get("ca notbefore"))) {
							Date notBeforeTime=new Date(certInfoMap.get("ca notbefore").toString());//开始时间
							notBeforeStr=sdf.format(notBeforeTime);
						}
						String notAfterStr="";
						if (!StringUtil.isEmpty(certInfoMap.get("ca notafter"))) {
							Date notAfterTime=new Date(certInfoMap.get("ca notafter").toString());//结束时间
							notAfterStr=sdf.format(notAfterTime);
						}
						String cn="";//CN
						//CN精确信息获取
						if(!StringUtil.isEmpty(subject)){
							for (String  cnStr : subject.split(",")) {
								cnStr=StringUtil.isEmpty(cnStr) ? "":cnStr.trim();
								if(cnStr.split("=").length > 1){
									cn=cnStr.split("=")[1];
									cn=StringUtil.isEmpty(cn) ? "":cn.trim();
								}
							}
						}
						String altName=StringUtil.isEmpty(certInfoMap.get("ca altname")) ? "":certInfoMap.get("ca altname").toString();//SAN
						
						cfg.setIssuer(issuer);
						cfg.setSubject(subject);
						cfg.setCn(cn);
						cfg.setAltName(altName);
						cfg.setNotBeforeTime(notBeforeStr);
						cfg.setNotAfterTime(notAfterStr);
					}else{
						logger.info("无证书信息");
					}
				}
			}
 		}catch (Exception e) {
 			validFlag=false;
 			logger.error("证书信息获取失败",e);
 			logger.error("证书文件校验失败",e);
			if(e instanceof MaatConvertException){
 				addMessage(redirectAttributes,"error","request_service_failed");
 			}else if(e instanceof MultiPartNewException){
 				addMessage(redirectAttributes,"error",e.getMessage());
 			}else{
 				addMessage(redirectAttributes,"error","save_failed");
 			}
 		}
 		try{
 			if(validFlag){
				if(publicKeyFileI != null) {
					String filename = publicKeyFileI.getOriginalFilename();
					String prefix = FileUtils.getPrefix(filename, false);
					String suffix = FileUtils.getSuffix(filename, false);
					file = File.createTempFile("file_"+ prefix, suffix);
					publicKeyFileI.transferTo(file);//复制文件
					String md5 = FileUtils.getFileMD5(file);
					Map<String,Object> srcMap = Maps.newHashMap();
					srcMap.put("filetype", suffix);
					srcMap.put("datatype", "dbSystem");//源文件存入数据中心
					srcMap.put("createTime",new Date());
					srcMap.put("key",prefix);
					srcMap.put("fileName", filename);
					srcMap.put("checksum", md5);
					ToMaatResult result = ConfigServiceUtil.postFileCfg(null, file, JsonMapper.toJsonString(srcMap));
					logger.info("proxy 证书文件策略公钥 文件上传响应信息："+JsonMapper.toJsonString(result));
					String publicKeyFileAccessUrl = null;
					if(!StringUtil.isEmpty(result)){
						ResponseData data = result.getData();
						publicKeyFileAccessUrl=data.getAccessUrl();
						cfg.setPublicKeyFile(publicKeyFileAccessUrl);;
					}
	 			}
				if(privateKeyFileI != null) {
					String filename = privateKeyFileI.getOriginalFilename();
					String prefix = FileUtils.getPrefix(filename, false);
					String suffix = FileUtils.getSuffix(filename, false);
					file = File.createTempFile("file_"+ prefix, suffix);
					privateKeyFileI.transferTo(file);//复制文件
					String md5 = FileUtils.getFileMD5(file);
					Map<String,Object> srcMap = Maps.newHashMap();
					srcMap.put("filetype", suffix);
					srcMap.put("datatype", "dbSystem");//源文件存入数据中心
					srcMap.put("createTime",new Date());
					srcMap.put("key",prefix);
					srcMap.put("fileName", filename);
					srcMap.put("checksum", md5);
					ToMaatResult result = ConfigServiceUtil.postFileCfg(null, file, JsonMapper.toJsonString(srcMap));
					logger.info("proxy 证书文件策略私钥 上传响应信息："+JsonMapper.toJsonString(result));
					String privateKeyFileAccessUrl = null;
					if(!StringUtil.isEmpty(result)){
						ResponseData data = result.getData();
						privateKeyFileAccessUrl=data.getAccessUrl();
						cfg.setPrivateKeyFile(privateKeyFileAccessUrl);;
					}
				}
				pxyObjKeyringService.saveOrUpdate(cfg);
				addMessage(redirectAttributes,"success","save_success");
 			}
 			
 		}catch(Exception e){
 			logger.error("证书上传失败",e);
			if(e instanceof MaatConvertException){
 				addMessage(redirectAttributes,"error","request_service_failed");
 			}else if(e instanceof MultiPartNewException){
 				addMessage(redirectAttributes,"error",e.getMessage());
 			}else{
 				addMessage(redirectAttributes,"error","save_failed");
 			}
 		}
		
		return "redirect:" + adminPath +"/proxy/intercept/strateagy/list?functionId="+cfg.getFunctionId();
	}
	
	/**
	 * 
	 * @param file
	 * @param validateType --incert证书校验 --inkey 私钥
	 * @param certType 证书类型
	 * @return
	 * @throws Exception
	 */
	public boolean validCertFileContent(MultipartFile file,String validateType)throws Exception{
		String os = System.getProperty("os.name").toLowerCase();
		if(!os.contains("windows") && file != null){
			//证书文件临时保存路径
			String certFilePath = Constants.CERT_FILE_PATH;
			FileUtils.createDirectory(certFilePath);
			String filePath=certFilePath
									+File.separator
									+UUID.randomUUID()
									+FileUtils.getSuffix(file.getOriginalFilename(), true);
			File uploadFile = new File(filePath);
			FileCopyUtils.copy(file.getBytes(), uploadFile);
			//加载x509脚本
			String x509Shell=Thread.currentThread()
												.getContextClassLoader()
												.getResource(
															File.separator+"shell"
															+File.separator
															+Constants.CERT_VALIDATE_FILE).getPath();
			//x509脚本分配可执行权限
			Map<String, Object> resultMap1=this.execShell("","chmod","+x",x509Shell);
			logger.info("x509 chmod +x :"+resultMap1.get("out").toString());
			logger.info("x509脚本分配可执行权限:"+"chmod"+" "+"+x"+" "+x509Shell);
			//验证文件
			logger.info(x509Shell+" "+validateType+" "+filePath);
			Map<String, Object> resultMap=this.execShell(x509Shell,validateType,filePath);
			
			if(resultMap == null || StringUtil.isEmpty(resultMap.get("out"))){
				//临时文件删除
				logger.info("delete file"+filePath);
				FileUtils.deleteFile(filePath);
				return false;
			}else{
				/*logger.info("x509 Out Info:"+resultMap.get("out").toString());
				Pattern p = Pattern.compile("\\s*|\t|\r|\n");
				Matcher m = p.matcher(resultMap.get("out").toString());
				logger.info(m.replaceAll("test"));*/
			} 
			if(resultMap != null 
					&& !StringUtil.isEmpty(resultMap.get("out"))
					&& (!(resultMap.get("out").toString().indexOf(Constants.CERT_VALIDATE_SUCCESS_INFO) > -1))
					){
				logger.error("x509 Out Info:"+resultMap.get("out").toString());
				//临时文件删除
				logger.info("delete file"+filePath);
				FileUtils.deleteFile(filePath);
				
				return false;
			}
			//临时文件删除
			logger.info("delete file"+filePath);
			FileUtils.deleteFile(filePath);
		}
		return true;
	}
	@RequestMapping(value = {"/list"})
	public String list(Model model,HttpServletRequest  request,HttpServletResponse response
			,@ModelAttribute("cfg")PxyObjKeyring entity
			,RedirectAttributes redirectAttributes){
		//查询时left join policyGroup
		Page<PxyObjKeyring> page = pxyObjKeyringService.findPage(new Page<PxyObjKeyring>(request, response,"r"), entity);
		model.addAttribute("page", page);
		initPageCondition(model,entity);
		return "/cfg/intercept/strateagy/list";
	}
	
	@RequestMapping(value = {"/delete"})
	@RequiresPermissions(value={"proxy:intercept:config"})
	public String delete(Integer isAudit,Integer isValid,String ids,Integer functionId
			,Model model,HttpServletRequest request
			,HttpServletResponse response
			,RedirectAttributes redirectAttributes){
		if(!StringUtil.isEmpty(ids)){
			pxyObjKeyringService.delete(isAudit,isValid,ids,functionId);
		}
		
		return "redirect:" + adminPath +"/proxy/intercept/strateagy/list?functionId="+functionId;
	}
	
	@RequestMapping(value = {"/audit"})
	@RequiresPermissions(value={"proxy:intercept:confirm"})
	public String audit(Model model,@ModelAttribute("cfg")PxyObjKeyring cfg,
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
					pxyObjKeyringService.audit(isAudit,isValid,functionId,id,auditTime);
				} catch (MaatConvertException e) {
					logger.error(e);
					addMessage(redirectAttributes,"error", "request_service_failed");
				}catch (Exception e) {
					logger.error(e);
					addMessage(redirectAttributes,"error", "audit_failed");
				}
			}
			
		}else {
			Page<PxyObjKeyring> searchPage=new Page<PxyObjKeyring>(request,response,"r");
			Page<PxyObjKeyring> auditPage=new Page<PxyObjKeyring>(request,response,"r");

			try {
				BeanUtils.copyProperties(searchPage, auditPage);
				auditAll(auditPage,isValid , cfg);
			} catch (Exception e) {
				logger.error("配置下发失败：",e);
				if(e instanceof MaatConvertException) {
					addMessage(redirectAttributes,"error", "request_service_failed");
				}else {
					addMessage(redirectAttributes,"error", "audit_failed");
				}
				
			}
			
			return list(model, request, response, cfg,redirectAttributes);
		}
		return "redirect:" + adminPath +"/proxy/intercept/strateagy/list?functionId="+functionId;
	}
	@ResponseBody
	@RequestMapping(value = "/validCfgId")
	public boolean validCfgId(Long cfgId) {
		PxyObjKeyring dns=pxyObjKeyringService.getPxyObjKeyring(cfgId,null);
		if(dns == null ){
			return false;
		}else{
			return true;
		}
	}
	
	
	//pxyObjKeyring配置导出
		@RequestMapping(value = "exportPxy")
		public void exportPxy(Model model,HttpServletRequest  request,HttpServletResponse response,
				@ModelAttribute("cfg")PxyObjKeyring entity,String ids,RedirectAttributes redirectAttributes){
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
					Page<PxyObjKeyring> pageInfo=new Page<PxyObjKeyring>(request, response,"r");
					pageInfo.setPageNo(1);
					pageInfo.setPageSize(Constants.MAX_EXPORT_SIZE);
					Page<PxyObjKeyring> page = pxyObjKeyringService.findPage(pageInfo, entity);
					for (int i = 0; i <page.getList().size(); i++) {
						page.getList().get(i).setKeyringName(page.getList().get(i).getCfgDesc());
					}
					model.addAttribute("page", page);
					titleList.add(entity.getMenuNameCode());
					classMap.put(entity.getMenuNameCode(), PxyObjKeyring.class);
					String cfgIndexInfoNoExport=",config_describe,whether_area_block,block_type,valid_identifier,do_log,client_port,ir_type,group_name,userregion1,userregion2,userregion3,userregion4,userregion5,";
					noExportMap.put(entity.getMenuNameCode(),cfgIndexInfoNoExport);
					dataMap.put(entity.getMenuNameCode(), page.getList());
				/*}*/
				this._export(model, request, response, redirectAttributes,entity.getMenuNameCode(),titleList,classMap,dataMap,noExportMap);
			} catch (Exception e) {
				logger.error("pxyObjKeyring export failed",e);
				addMessage(redirectAttributes, "error","export_failed");
			}
			//return "redirect:" + adminPath +"/ntc/iplist/list?functionId="+entity.getFunctionId();
		}
		
		//pxyObjTrustedCert配置导出
				@RequestMapping(value = "exportCert")
				public void exportCert(Model model,HttpServletRequest  request,HttpServletResponse response,
						@ModelAttribute("cfg")PxyObjTrustedCaCert entity,String ids,RedirectAttributes redirectAttributes){
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
							Page<PxyObjTrustedCaCert> pageInfo=new Page<PxyObjTrustedCaCert>(request, response,"r");
							pageInfo.setPageNo(1);
							pageInfo.setPageSize(Constants.MAX_EXPORT_SIZE);
							Page<PxyObjTrustedCaCert> page = pxyObjKeyringService.findTrustedCertPage(pageInfo, entity);
							for (PxyObjTrustedCaCert cert:page.getList()) {
								if(!StringUtil.isEmpty(cert.getCompileId())){
									cert.setCrlFile(ConfigDictUtils.getTrustedCrlByCerId(cert.getCompileId()));
							   }
							}
							model.addAttribute("page", page);
							titleList.add(entity.getMenuNameCode());
							classMap.put(entity.getMenuNameCode(), PxyObjTrustedCaCert.class);
							String cfgIndexInfoNoExport=",whether_area_block,block_type,valid_identifier,do_log,client_port,ir_type,group_name,userregion1,userregion2,userregion3,userregion4,userregion5,&config_describe:cert_name-";
							noExportMap.put(entity.getMenuNameCode(),cfgIndexInfoNoExport);
							dataMap.put(entity.getMenuNameCode(), page.getList());
						/*}*/
						this._export(model, request, response, redirectAttributes,entity.getMenuNameCode(),titleList,classMap,dataMap,noExportMap);
					} catch (Exception e) {
						logger.error("pxyObjKeyring export failed",e);
						addMessage(redirectAttributes, "error","export_failed");
					}
					//return "redirect:" + adminPath +"/ntc/iplist/list?functionId="+entity.getFunctionId();
				}
		
		
		@RequestMapping(value = {"/trustedCertList"})
		public String trustedCertList(Model model,HttpServletRequest  request,HttpServletResponse response
				,@ModelAttribute("cfg")PxyObjTrustedCaCert entity
				,RedirectAttributes redirectAttributes){
			Page<PxyObjTrustedCaCert> page = pxyObjKeyringService.findTrustedCertPage(new Page<PxyObjTrustedCaCert>(request, response,"r"), entity);
			model.addAttribute("page", page);
			initPageCondition(model,entity);
			return "/cfg/intercept/strateagy/trustedCertList";
		}
		
		@RequestMapping(value = {"/trustedCertForm"})
		@RequiresPermissions(value={"proxy:trustedCert:config"})
		public String trustedCertFrom(Model model,
								HttpServletRequest request,
								HttpServletResponse response,
								String ids,
								@ModelAttribute("cfg")PxyObjTrustedCaCert cfg
								,RedirectAttributes redirectAttributes){
			if(cfg == null){
				cfg=new PxyObjTrustedCaCert();
			}
			if(!StringUtil.isEmpty(ids)){
				cfg = pxyObjKeyringService.getPxyObjTrustedCaCert(Long.valueOf(ids));
				initFormCondition(model, cfg);
				model.addAttribute("isAdd", false);
			}else{
				initFormCondition(model, cfg);
				model.addAttribute("isAdd", true);
			}
			
			model.addAttribute("_cfg", cfg);
			return "/cfg/intercept/strateagy/trustedCertForm";
		}
		
		@RequestMapping(value = {"/trustedCertSaveOrUpdate"})
		@RequiresPermissions(value={"proxy:trustedCert:config"})
		public String trustedCertSaveOrUpdate(Model model,HttpServletRequest request,HttpServletResponse response,
				@ModelAttribute("cfg")PxyObjTrustedCaCert cfg,
				MultipartFile certFileI, 
				RedirectAttributes redirectAttributes){
	 		File file = null;
	 		boolean validFlag=true;
	 		try {
	 			boolean certFileflag=validCertFileContent(certFileI,"-incert");
	 			if(!certFileflag){
	 				addMessage(redirectAttributes,"save_failed");
	 				logger.error(certFileI.getOriginalFilename()+" file non Certificate file format ");
	 				throw new MultiPartNewException(this.getMsgProp().getProperty("certificate_file_error"));
	 			}
			} catch (Exception e) {
				validFlag=false;
				logger.error("证书文件校验失败",e);
				if(e instanceof MaatConvertException){
	 				addMessage(redirectAttributes,"error",e.getMessage());
	 			}else{
	 				addMessage(redirectAttributes,"error","save_failed");
	 			}
			}
	 		
	 		try{
	 			if(validFlag){
	 				validFlag=true;
					if(certFileI != null) {
						// 获取公钥信息
						if(certInfoMap != null && certInfoMap.size() >0){
							String issuer=StringUtil.isEmpty(certInfoMap.get("ca issuer")) ? "":certInfoMap.get("ca issuer").toString();//颁发者
							logger.info("issuer："+issuer);
							cfg.setIssuer(issuer);
						}else{
							logger.info("无证书信息");
						}
					}
				}
	 		}catch (Exception e) {
	 			validFlag=false;
	 			logger.error("证书信息获取失败",e);
	 			addMessage(redirectAttributes,"error","save_failed");
	 		}
	 		try{
	 			if(validFlag){
					if(certFileI != null) {
						String filename = certFileI.getOriginalFilename();
						String prefix = FileUtils.getPrefix(filename, false);
						String suffix = FileUtils.getSuffix(filename, false);
						file = File.createTempFile("file_"+ prefix, suffix);
						certFileI.transferTo(file);//复制文件
						String md5 = FileUtils.getFileMD5(file);
						Map<String,Object> srcMap = Maps.newHashMap();
						srcMap.put("filetype", suffix);
						srcMap.put("datatype", "dbSystem");//源文件存入数据中心
						srcMap.put("createTime",new Date());
						srcMap.put("key",prefix);
						srcMap.put("fileName", filename);
						srcMap.put("checksum", md5);
						ToMaatResult result = ConfigServiceUtil.postFileCfg(null, file, JsonMapper.toJsonString(srcMap));
						logger.info("proxy 可信证书 文件上传响应信息："+JsonMapper.toJsonString(result));
						String certFileAccessUrl = null;
						if(!StringUtil.isEmpty(result)){
							ResponseData data = result.getData();
							certFileAccessUrl=data.getAccessUrl();
							cfg.setCertFile(certFileAccessUrl);;
						}
		 			}
					pxyObjKeyringService.trustedCertsaveOrUpdate(cfg);
					addMessage(redirectAttributes,"success","save_success");
	 			}
	 			
	 		}catch(Exception e){
	 			logger.error("证书上传失败",e);
	 			if(e instanceof MultiPartNewException) {
	 				addMessage(redirectAttributes,"error",e.getMessage());
	 			}else if(e instanceof MaatConvertException) {
	 				addMessage(redirectAttributes,"error","request_service_failed");
	 			}else {
	 				addMessage(redirectAttributes,"error","save_failed");
	 			}
	 		}
			
			return "redirect:" + adminPath +"/proxy/intercept/strateagy/trustedCertList?functionId="+cfg.getFunctionId();
		}
		@RequestMapping(value = {"/addOrAuditCrl"})
		/*@RequiresPermissions(value={"proxy:trustedCert:config","proxy:trustedCert:confirm"})*/
		public String trustedCrlSaveOrUpdate(Model model,HttpServletRequest request,HttpServletResponse response,
				@ModelAttribute("cfg")PxyObjTrustedCaCrl cfg,
				MultipartFile crlFileI, 
				RedirectAttributes redirectAttributes){
			File file = null;
			boolean validFlag=true;
			try {
				if( crlFileI != null){
					boolean certFileflag=validCertFileContent(crlFileI,"-incrl");
					if(!certFileflag){
						addMessage(redirectAttributes,"save_failed");
						logger.error(crlFileI.getOriginalFilename()+" file non crl file format ");
						throw new MultiPartNewException(this.getMsgProp().getProperty("crl_file_error"));
					}
				}
			} catch (Exception e) {
				validFlag=false;
				logger.error("证书文件校验失败",e);
				if(e instanceof MaatConvertException){
	 				addMessage(redirectAttributes,"error","request_service_failed");
	 			}else if(e instanceof MultiPartNewException){
	 				addMessage(redirectAttributes,"error",e.getMessage());
	 			}else{
	 				addMessage(redirectAttributes,"error","save_failed");
	 			}
			}
			
			try{
				if(validFlag){
					validFlag=true;
					if(crlFileI != null) {
						// 获取issuer
						if(certInfoMap != null && certInfoMap.size() >0){
							String issuer=StringUtil.isEmpty(certInfoMap.get("crl issuer")) ? "":certInfoMap.get("crl issuer").toString();//颁发者
							if(cfg != null){
								if((cfg.getCertId() != null && cfg.getCertId() > 0) && (!cfg.getIssuer().equals(issuer))){
									logger.error("cert 和 crl的issuser不符合");
									throw new MultiPartNewException(this.getMsgProp().getProperty("crl_issuer_error"));
								}else{
									cfg.setIssuer(issuer);
								}
								
							}
						}else{
							logger.error("crl的issuser为空");
							throw new MultiPartNewException(this.getMsgProp().getProperty("crl_issuer_null"));
						}
					}
				}
			}catch (Exception e) {
				validFlag=false;
				logger.error("crl issuer比对失败",e);
				if(e instanceof MultiPartNewException){
	 				addMessage(redirectAttributes,"error",e.getMessage());
	 			}else{
	 				addMessage(redirectAttributes,"error","save_failed");
	 			}
			}
			try{
				if(validFlag){
					if(crlFileI != null) {
						String filename = crlFileI.getOriginalFilename();
						String prefix = FileUtils.getPrefix(filename, false);
						String suffix = FileUtils.getSuffix(filename, false);
						file = File.createTempFile("file_"+ prefix, suffix);
						crlFileI.transferTo(file);//复制文件
						String md5 = FileUtils.getFileMD5(file);
						Map<String,Object> srcMap = Maps.newHashMap();
						srcMap.put("filetype", suffix);
						srcMap.put("datatype", "dbSystem");//源文件存入数据中心
						srcMap.put("createTime",new Date());
						srcMap.put("key",prefix);
						srcMap.put("fileName", filename);
						srcMap.put("checksum", md5);
						ToMaatResult result = ConfigServiceUtil.postFileCfg(null, file, JsonMapper.toJsonString(srcMap));
						logger.info("可信证书crl 文件上传响应信息："+JsonMapper.toJsonString(result));
						String crlFileAccessUrl = null;
						if(!StringUtil.isEmpty(result)){
							ResponseData data = result.getData();
							crlFileAccessUrl=data.getAccessUrl();
							cfg.setCrlFile(crlFileAccessUrl);
						}
					}
					pxyObjKeyringService.trustedCrlsaveOrUpdate(cfg);
					
					addMessage(redirectAttributes,"success","save_success");
				}
				
			}catch(Exception e){
				logger.error("crl上传失败",e);
				if(e instanceof MaatConvertException){
	 				addMessage(redirectAttributes,"error","request_service_failed");
	 			}else if(e instanceof MultiPartNewException){
	 				addMessage(redirectAttributes,"error",e.getMessage());
	 			}else{
	 				addMessage(redirectAttributes,"error","save_failed");
	 			}
			}
			
			return "redirect:" + adminPath +"/proxy/intercept/strateagy/trustedCertList?functionId="+cfg.getFunctionId();
		}
		
		/**
		 * 调用shell脚本 返回运行结果
		 * 
		 * @param shellName
		 * @param params
		 * @return
		 */
		public Map<String, Object> execShell(String shellName,
				String... params) {
			Map<String, Object> result = new HashMap<String, Object>();
			StringBuilder sb = new StringBuilder();
			sb.append(shellName);
			for (String temp : params) {
				sb.append(" " + temp);
			}
			String os = System.getProperty("os.name").toLowerCase();
			String cmd1 = "";
			String cmd2 = "";
			if(os.contains("windows")){
				cmd1 = "cmd.exe";
				cmd2 = "/c";
			}else{
				cmd1 = "/bin/sh";
				cmd2 = "-c";
			}
			logger.info("调用脚本信息,cmd1:"+cmd1+",cmd2:"+cmd2);
			String cmdarray[] = new String[] {cmd1, cmd2 ,sb.toString() };
			BufferedReader br = null;
			BufferedReader bre = null;
			Process exec=null;
			try {
				exec = Runtime.getRuntime().exec(cmdarray);
				exec.getInputStream();
				br = new BufferedReader(
						new InputStreamReader(exec.getInputStream()));
				bre = new BufferedReader(new InputStreamReader(
						exec.getErrorStream()));
				String s = null;
				StringBuilder out = new StringBuilder();
				String key="";
				String value="";
				if((sb.toString().indexOf("inlist") > -1) 
						|| ( sb.toString().indexOf("incrl") > -1 )
						|| ( sb.toString().indexOf("incert") > -1 )){
					certInfoMap=new HashMap<>();
				}
				while ((s = br.readLine()) != null) {
					logger.info(s);
					//可信证书pem信息收集
					if(sb.toString().indexOf("inlist") > -1){
						if(s.indexOf(":") > -1){
							key=s.substring(0, s.indexOf(":", 0));
							key=StringUtil.isEmpty(key) ?"": key.toLowerCase().trim();
							value=s.substring(s.indexOf(":", 0)+1, s.length());
							value=StringUtil.isEmpty(value) ?"": value.trim();
							certInfoMap.put(key, value);	
							
						}
					}
					//crl信息收集
					if(sb.toString().indexOf("incrl") > -1){
						if(s.indexOf(":") > -1){
							key=s.substring(0, s.indexOf(":", 0));
							key=StringUtil.isEmpty(key) ?"": key.toLowerCase().trim();
							value=s.substring(s.indexOf(":", 0)+1, s.length());
							value=StringUtil.isEmpty(value) ?"": value.trim();
							certInfoMap.put(key, value);	
						}
					}
					//证书信息收集
					if(sb.toString().indexOf("incert") > -1){
						if(s.indexOf(":") > -1){
							key=s.substring(0, s.indexOf(":", 0));
							key=StringUtil.isEmpty(key) ?"": key.toLowerCase().trim();
							value=s.substring(s.indexOf(":", 0)+1, s.length());
							value=StringUtil.isEmpty(value) ?"": value.trim();
							certInfoMap.put(key, value);	
						}
					}
					out.append(s);
				}
				result.put("out", out.toString());//输出参数
				out.setLength(0);//清空
				while ((s = bre.readLine()) != null) {
					out.append(s);
				}
				result.put("error", out.toString());//错误信息
				int waitFor = exec.waitFor();
				logger.info("调用脚本:"+sb.toString()+",执行返回状态值："+waitFor);
				result.put("exitStatus", waitFor);//执行状态
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("调用 " + shellName + " 脚本异常", e);
			} finally {
				if (br != null)
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				if (bre != null)
					try {
						bre.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				if (exec != null)
					exec.destroy();
					
			}
			return result;
		}
		

		@RequestMapping(value = {"/trustedCertDelete"})
		@RequiresPermissions(value={"proxy:trustedCert:config"})
		public String trustedCertDelete(Integer isAudit,Integer isValid,String ids,Integer functionId
				,Model model,HttpServletRequest request
				,HttpServletResponse response
				,RedirectAttributes redirectAttributes){
			if(!StringUtil.isEmpty(ids)){
				pxyObjKeyringService.trustedCertDelete(isAudit,isValid,ids,functionId);
			}
			
			return "redirect:" + adminPath +"/proxy/intercept/strateagy/trustedCertList?functionId="+functionId;
		}
		
		@RequestMapping(value = {"/trustedCertAudit"})
		@RequiresPermissions(value={"proxy:trustedCert:confirm"})
		public String trustedCertAudit(Model model,@ModelAttribute("cfg")PxyObjTrustedCaCert cfg,
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
						pxyObjKeyringService.trustedCertAudit(isAudit,isValid,functionId,id,auditTime);
					} catch (MaatConvertException e) {
						e.printStackTrace();
						logger.error(e);
						addMessage(redirectAttributes,"error", "request_service_failed");
					}catch (Exception e) {
						e.printStackTrace();
						logger.error(e);
						addMessage(redirectAttributes,"error", "audit_failed");
					}
				}
				
			}else {
				Page<PxyObjTrustedCaCert> searchPage=new Page<PxyObjTrustedCaCert>(request,response,"r");
				Page<PxyObjTrustedCaCert> auditPage=new Page<PxyObjTrustedCaCert>(request,response,"r");

				try {
					BeanUtils.copyProperties(searchPage, auditPage);
					auditAll(auditPage,isValid , cfg);
				} catch (Exception e) {
					logger.error("配置下发失败：",e);
					if(e instanceof MaatConvertException) {
						addMessage(redirectAttributes,"error", "request_service_failed");
					}else {
						addMessage(redirectAttributes,"error", "audit_failed");
					}
					
				}
				
				return trustedCertList(model, request, response, cfg, redirectAttributes);
			}
			return "redirect:" + adminPath +"/proxy/intercept/strateagy/trustedCertList?functionId="+functionId;
		}
}
