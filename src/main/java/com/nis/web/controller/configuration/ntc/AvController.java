package com.nis.web.controller.configuration.ntc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.BeanUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nis.domain.FunctionRegionDict;
import com.nis.domain.FunctionServiceDict;
import com.nis.domain.Page;
import com.nis.domain.configuration.AppPolicyCfg;
import com.nis.domain.configuration.AvFileSampleCfg;
import com.nis.domain.configuration.AvSignSampleCfg;
import com.nis.domain.configuration.AvSignSampleCfgModel;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.FileDigestCfg;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.maat.ToMaatResult;
import com.nis.domain.maat.ToMaatResult.ResponseData;
import com.nis.exceptions.CallExternalProceduresException;
import com.nis.exceptions.MaatConvertException;
import com.nis.exceptions.MultiPartNewException;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.Configurations;
import com.nis.util.Constants;
import com.nis.util.DictUtils;
import com.nis.util.FileUtils;
import com.nis.util.JsonMapper;
import com.nis.util.StringUtil;
import com.nis.util.StringUtils;
import com.nis.web.controller.BaseController;
import com.nis.web.security.UserUtils;
import com.nis.web.security.SystemAuthorizingRealm.Principal;

import it.sauronsoftware.jave.AudioInfo;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.InputFormatException;
import it.sauronsoftware.jave.MultimediaInfo;
import it.sauronsoftware.jave.VideoInfo;

/**
 * 处理音视频业务
 * @author zhangwei
 *
 */
@Controller
@RequestMapping("${adminPath}/ntc/av")
public class AvController extends BaseController  {

	//音视频文件样例配置列表
	@RequestMapping(value = {"/sample/fileSampleList","/sample/fileSampleSpeakerList","/sample/fileSampleLogoList","/sample/fileSampleFaceList"})
	public String fileSampleList(Model model,HttpServletRequest request,HttpServletResponse response,@ModelAttribute("cfg")AvFileSampleCfg entity){
		Page<AvFileSampleCfg> page = avCfgService.getAvFileSampleList(new Page<AvFileSampleCfg>(request, response,"a"), entity);
		model.addAttribute("page", page);
		initPageCondition(model,entity);
		List<FunctionRegionDict> regionList = DictUtils.getFunctionRegionDictList(entity.getFunctionId());
		for(FunctionRegionDict region:regionList){
			model.addAttribute("regionValue",region.getConfigRegionValue());
			break;
		}
		return "/cfg/av/fileSampleList";
	}
	//音视频标志样例配置列表
	@RequestMapping(value = {"/sample/audioSignSampleList"})
	public String audioSignSampleList(Model model,HttpServletRequest request,HttpServletResponse response,@ModelAttribute("cfg")AvSignSampleCfg entity){
		Page<AvSignSampleCfg> page = avCfgService.getAvSignSampleList(new Page<AvSignSampleCfg>(request, response,"a"), entity);
		model.addAttribute("page", page);
		initPageCondition(model,entity);
		List<FunctionRegionDict> regionList = DictUtils.getFunctionRegionDictList(entity.getFunctionId());
		for(FunctionRegionDict region:regionList){
			model.addAttribute("regionValue",region.getConfigRegionValue());
			break;
		}
		return "/cfg/av/signSampleList";
	}
	//视频标志样例配置列表
	@RequestMapping(value = {"/sample/signSampleList"})
	public String signSampleList(Model model,HttpServletRequest request,HttpServletResponse response,@ModelAttribute("cfg")AvSignSampleCfg entity){
		List<AvSignSampleCfg> list = avCfgService.getSignSampleList(entity);
		model.addAttribute("cfgs", list);
		model.addAttribute("functionId", entity.getFunctionId());
		initPageCondition(model,entity);
		/*List<FunctionRegionDict> regionList = DictUtils.getFunctionRegionDictList(entity.getFunctionId());
		for(FunctionRegionDict region:regionList){
			model.addAttribute("regionValue",region.getConfigRegionValue());
			break;
		}*/
		List<FunctionServiceDict> serviceList = DictUtils.getFunctionServiceDictList(entity.getFunctionId());
		model.addAttribute("serviceList", serviceList);
		//return "/cfg/av/signSampleList";
		return "/cfg/av/switchSignSample";
	}
	// 日志获取样例获取域配置信息
	@RequestMapping(value = {"ajaxSignSampleList"})
	public String ajaxSignSampleList(Model model,Long cfgId,Integer index,Integer compileId) {
		List<String[]> tabList = new ArrayList<String[]>();
		AvSignSampleCfg cfg = new AvSignSampleCfg();
		cfg.setCfgId(cfgId);
		cfg.setCompileId(compileId);
		List<AvSignSampleCfg> list = avCfgService.getSignSampleList(cfg);
		if(list!=null){
			cfg=list.get(0);
		}
		cfg.setCfgType("5");
			String cfgType = null;
			if(cfg.getCfgType()!=null){
				tabList.add(new String[]{"5",cfg.getCfgType()});
				cfgType = cfg.getCfgType();
			}
		model.addAttribute("_cfg", cfg);	
		model.addAttribute("index", index);
		model.addAttribute("tabList", tabList);
		return "/cfg/av/switchSignSampleSubList";
	}

	//文件样例配置界面
	@RequestMapping(value = {"/sample/fileSampleForm"})
	public String fileSampleForm(Model model,HttpServletRequest request,HttpServletResponse response,String ids,AvFileSampleCfg entity){
		if(!StringUtil.isEmpty(ids)){
			entity = avCfgService.getAvFileSampleById(Long.parseLong(ids),null);
		}
		
		initFormCondition(model,entity);
		model.addAttribute("_cfg", entity);
		return "/cfg/av/fileSampleForm";
	}
	// 日志获取样例获取域配置信息
	@RequestMapping(value = {"ajaxfileSampleList"})
	public String ajaxAvContIpList(Model model,Long cfgId,Integer index,Integer compileId) {
		AvFileSampleCfg cfg = avCfgService.getAvFileSampleById(cfgId,compileId);
		List<String[]> tabList = new ArrayList<String[]>();
		if(cfg!=null){
			String cfgType = null;
			if(!cfg.getCfgType().equals(cfgType)){
				tabList.add(new String[]{"5",cfg.getCfgType()});
				cfgType = cfg.getCfgType();
			}
		}
		model.addAttribute("_cfg", cfg);	
		model.addAttribute("index", index);
		model.addAttribute("tabList", tabList);
		return "/cfg/av/fileSampleSubList";
	}
	//标志样例配置界面
	@RequestMapping(value = {"/sample/audioSignSampleForm"})
	public String audioSignSampleForm(Model model,HttpServletRequest request,HttpServletResponse response,String ids,AvSignSampleCfg entity){
		if(!StringUtil.isEmpty(ids)){
			entity = avCfgService.getAvSignSampleById(Long.parseLong(ids));
		}
		initFormCondition(model,entity);
		model.addAttribute("_cfg", entity);
		return "/cfg/av/signSampleForm";
	}
	//保存文件样例配置
	@RequestMapping(value = {"/sample/saveFileSample"})
	public String saveFileSample(Model model,HttpServletRequest request,HttpServletResponse response, RedirectAttributes redirectAttributes,
			String ids,AvFileSampleCfg entity,MultipartFile srcFile,MultipartFile sampleFile,String picPath,String videoToPicture){
		try{
//			if(srcFile!=null && sampleFile!=null && 
//			   srcFile.getSize()>0 && sampleFile.getSize()>0){
			String sep = System.getProperty("file.separator");
			String srcFilePath = Constants.AV_FILE_PATH+entity.getCfgType()+sep+"srcFile";//源文件保存路径
			String sampleFilePath = Constants.AV_FILE_PATH+entity.getCfgType()+sep+"sampleFile";//样例文件保存路径
			String resultFilePath = Constants.AV_FILE_PATH+entity.getCfgType()+sep+"resultFile";//结果文件保存路径
			FileUtils.createDirectory(srcFilePath);
			FileUtils.createDirectory(sampleFilePath);			
			String fileName = UUID.randomUUID()+"";
			
			//视频样例生成并选中的图片压缩为zip文件
			if("true".equals(videoToPicture)){
				if(!StringUtils.isBlank(entity.getSrcPath())){
					String[] srcArray = entity.getSrcPath().split("\\|");//所选图片数组
					String srcFileAllPath = srcFilePath+sep+fileName+".zip";
					File zipFile = new File(srcFileAllPath);
					ZipOutputStream zouts = new ZipOutputStream(new FileOutputStream(zipFile));
					for(String s:srcArray){
						if(!"".equals(s)){
							String filePath = picPath+sep+s;
							File file = new File(filePath);
							//将文件添加至压缩文件
							FileUtils.zipFilesToZipFile(file.getParent(), file, zouts);
							file.delete();
						}
					}
					FileUtils.deleteDirectory(picPath);
					if(zouts!=null){
						zouts.close();
					}
					FileInputStream input = new FileInputStream(srcFileAllPath);
			        srcFile = new MockMultipartFile(zipFile.getName(), input);
			        if(input!=null){
			        	input.close();
			        }
				}
			}
			
			if(srcFile!=null && srcFile.getSize()>0 && entity!=null){
				String srcFileAllPath = "";
				if(StringUtil.isBlank(srcFile.getOriginalFilename())){
					srcFileAllPath = srcFilePath+sep+fileName+FileUtils.getSuffix(srcFile.getName(), true);
				}else{
					srcFileAllPath = srcFilePath+sep+fileName+FileUtils.getSuffix(srcFile.getOriginalFilename(), true);
				}
				
				String sampleFileAllPath = sampleFilePath+sep+fileName+".sample";
				String resultFileAllPath = resultFilePath+sep+fileName+".result"; 
				entity.setSrcPath(srcFileAllPath);
				entity.setSamplePath(sampleFileAllPath);
				entity.setResultPath(resultFileAllPath);
				
				File uploadSrcFile = new File(srcFileAllPath);
				if(!uploadSrcFile.exists()){
					FileCopyUtils.copy(srcFile.getBytes(), uploadSrcFile);//保存源文件
				}
				entity.setSrcUrl("");
				entity.setSampleUrl("");
				/*String host = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
				String srcUrl = host+srcFilePath.substring(srcFilePath.indexOf(Constants.AV_FILE_PATH)+Constants.AV_FILE_PATH.length()-1)+sep+uploadSrcFile.getName();
				String sampleUrl = host+sampleFilePath.substring(sampleFilePath.indexOf(Constants.AV_FILE_PATH)+Constants.AV_FILE_PATH.length()-1)+sep+uploadSampleFile.getName();
				srcUrl = srcUrl.replace("\\", "/");
				sampleUrl = sampleUrl.replace("\\", "/");
				logger.info("srcUrl:"+srcUrl);
				logger.info("sampleUrl:"+sampleUrl);
				entity.setSrcUrl(srcUrl);
				entity.setSampleUrl(sampleUrl);*/
				
//					File uploadSrcFile = new File(srcFilePath);
//					FileCopyUtils.copy(srcFile.getBytes(), uploadSrcFile);
//					String srcMd5 = FileUtils.getFileMD5(uploadSrcFile);
//					File uploadSampleFile = new File(sampleFilePath);
//					String sampleMd5 = FileUtils.getFileMD5(uploadSampleFile);
				
//					entity.setSrcMd5(srcMd5);
//					entity.setSampleMd5(sampleMd5);
				
				//音频、视频、VoIP、说话人（音频）、人脸识别（视频）样例需要验证时长
				if(Constants.AUDIO_FILE_TYPE.contains(FileUtils.getSuffix(uploadSrcFile.getName(),false))
						||Constants.VIDEO_FILE_TYPE.contains(FileUtils.getSuffix(uploadSrcFile.getName(),false))
						||Constants.VOIP_FILE_TYPE.contains(FileUtils.getSuffix(uploadSrcFile.getName(),false))
						||Constants.SPEAKER_FILE_TYPE.contains(FileUtils.getSuffix(uploadSrcFile.getName(),false))
						){ 
					if(!validateAvDuration(uploadSrcFile)){
						addMessage(redirectAttributes,"exceeds_duration_limit");
						logger.error("The duration of uploaded files exceeds the limit("+Constants.AV_DURATION_LIMIT+"s).");
						throw new MultiPartNewException(this.getMsgProp().getProperty("exceeds_duration_limit"));
					}

				}
				
			}
			
			
			avCfgService.saveOrUpdateAvFileSample(entity, srcFile);
			addMessage(redirectAttributes,"success","save_success");
		}catch(Exception e){
			logger.error("文件上传失败",e);
			e.printStackTrace();
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,"error","request_service_failed");
			}else if(e instanceof CallExternalProceduresException) {
				addMessage(redirectAttributes,"error","call_external_procedures_failed");
			}else{
				addMessage(redirectAttributes,"error","save_failed");
			}
		}
		
		return "redirect:" + adminPath +"/ntc/av/sample/fileSampleList?functionId="+entity.getFunctionId();
	}
	//保存标志样例配置
	@RequestMapping(value = {"/sample/saveAudioSignSample"})
	public String saveAudioSignSample(Model model,HttpServletRequest request,HttpServletResponse response, RedirectAttributes redirectAttributes,String ids,AvSignSampleCfg entity){
		try{
			avCfgService.saveOrUpdateAvSignSample(entity);
			addMessage(redirectAttributes,"success","save_success");
		}catch(Exception e){
			logger.error("saveAudioSignSample failed",e);
			e.printStackTrace();
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,"error","request_service_failed");
			}else if(e instanceof CallExternalProceduresException) {
				addMessage(redirectAttributes,"error","call_external_procedures_failed");
			}else {
				addMessage(redirectAttributes,"error","save_failed");
			}
		}
		return "redirect:" + adminPath +"/ntc/av/sample/audioSignSampleList?functionId="+entity.getFunctionId();
	}
	//修改文件样例配置状态
	@RequestMapping(value = {"/sample/updateAvFileSampleValid"})
	public String updateAvFileSampleValid(Integer isAudit,Integer isValid,String ids,Integer functionId,RedirectAttributes redirectAttributes){
		avCfgService.updateAvFileSampleValid(isAudit,isValid,ids);
		addMessage(redirectAttributes,"delete_success");
		return "redirect:" + adminPath +"/ntc/av/sample/fileSampleList?functionId="+functionId;
	}
	//修改文件样例配置审核状态
	/**
	 * 审核配置下发，为了保证配置下发过程事务正确，一条配置提交一次
	 * @param isAudit
	 * @param isValid
	 * @param ids
	 * @param functionId
	 * @return
	 */
	@RequestMapping(value = {"/sample/auditAvFileSample"})
	public String auditAvFileSample(Model model,@ModelAttribute("cfg")AvFileSampleCfg cfg,
										Integer isValid,
										Integer isAudit,
										String ids,
										Integer functionId, 
										RedirectAttributes redirectAttributes,
										HttpServletResponse response,
										HttpServletRequest request){
		if(!StringUtil.isEmpty(ids)){
			//avCfgService.auditAvFileSample(isAudit,isValid,ids);
			AvFileSampleCfg entity = new AvFileSampleCfg();
			String[] idArray = ids.split(",");
			for(String id :idArray){
				entity = avCfgService.getAvFileSampleById(Long.parseLong(id),null);
				entity.setIsAudit(isAudit);
				entity.setIsValid(isValid);
				entity.setAuditorId(UserUtils.getUser().getId());
				entity.setAuditTime(new Date());
//				String oldSrcUrl = entity.getSrcPath();
//				String oldSampleUrl = entity.getSamplePath();
				try {
					/*if(isAudit==1){	
						Date creatTime = entity.getCreateTime();
						//音视频文件上传接口调用
						File srcFile = new File(oldSrcUrl);
						Map<String,Object> srcMap = new HashMap();
						srcMap.put("filetype", FileUtils.getSuffix(srcFile.getName(), false));
						srcMap.put("datatype", "dbSystem");//源文件存入数据中心
						
						srcMap.put("createTime",creatTime);
						srcMap.put("key",FileUtils.getPrefix(srcFile.getName(), false));
						srcMap.put("fileName", srcFile.getName());
						srcMap.put("checksum", entity.getSrcMd5());
						ToMaatResult result1 = ConfigServiceUtil.postFileCfg(null, srcFile, JsonMapper.toJsonString(srcMap));
						logger.info("音视频源文件上传响应信息："+result1);
						//获取文件上传响应信息（新的文件访问路径）
						String srcAccessUrl = null;
						if(!StringUtil.isEmpty(result1)){
							ResponseData data = result1.getData();
							srcAccessUrl=data.getAccessUrl();
							entity.setSrcUrl(srcAccessUrl);
//									entity.setSrcPath("");
						}
						File sampleFile = new File(oldSampleUrl);
						Map<String,Object> sampleMap = new HashMap();
						sampleMap.put("filetype", FileUtils.getSuffix(sampleFile.getName(), false));
						sampleMap.put("datatype", "fileSystem");//样例文件存入fastdfs
						sampleMap.put("createTime", creatTime);
						sampleMap.put("key",FileUtils.getPrefix(sampleFile.getName(), false));
						sampleMap.put("fileName", sampleFile.getName());
						sampleMap.put("checksum", entity.getSampleMd5());
						ToMaatResult result2 = ConfigServiceUtil.postFileCfg(null, sampleFile, JsonMapper.toJsonString(sampleMap));
						logger.info("音视频样例文件上传响应信息："+result2);
						
						//获取文件上传响应信息（新的文件访问路径）
						String sampleAccessUrl = null;
						if(!StringUtil.isEmpty(result2)){
							ResponseData data = result2.getData();
							sampleAccessUrl = data.getAccessUrl();
							entity.setSampleUrl(sampleAccessUrl);
//								entity.setSamplePath("");
							
						}
					}*/
					avCfgService.auditAvFileSample(entity,isAudit);	
					//删除本地源文件和样例文件
					/*if(!oldSrcUrl.equals(entity.getSrcUrl())){
						FileUtils.deleteFile(oldSrcUrl);
					}
					if(!oldSampleUrl.equals(entity.getSampleUrl())){
						FileUtils.deleteFile(oldSampleUrl);
					}*/
				}catch(Exception e){
					e.printStackTrace();
					if(e instanceof MaatConvertException) {
						logger.info("音视频文件样例配置下发失败："+e.getMessage());
						addMessage(redirectAttributes,"error","request_service_failed");
					}else {
						logger.error("auditAvFileSample failed",e);
						addMessage(redirectAttributes,"error","audit_failed");
					}
				}
			}
		}else {
			Page<AvFileSampleCfg> searchPage=new Page<AvFileSampleCfg>(request,response,"a");
			Page<AvFileSampleCfg> auditPage=new Page<AvFileSampleCfg>(request,response,"a");
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
			
			return fileSampleList(model, request, response, cfg);
		}
		return "redirect:" + adminPath +"/ntc/av/sample/fileSampleList?functionId="+functionId;
	}
	//修改标志样例配置状态
	@RequestMapping(value = {"/sample/updateAvSignSampleValid"})
	public String updateAvSignSampleValid(Integer isAudit,Integer isValid,String ids,Integer functionId){
		avCfgService.updateAvSignSampleValid(isAudit,isValid,ids);
		return "redirect:" + adminPath +"/ntc/av/sample/signSampleList?functionId="+functionId;
	}
	
	//修改标志样例配置审核状态
	/**
	 * 审核配置下发，为了保证配置下发过程事务正确，一条配置提交一次
	 * @param isAudit
	 * @param isValid
	 * @param ids
	 * @param functionId
	 * @return
	 */
	@RequestMapping(value = {"/sample/auditAvAudioSignSample"})
	public String auditAvAudioSignSample(Integer isAudit,Integer isValid,String ids,Integer functionId, RedirectAttributes redirectAttributes){
//			avCfgService.auditAvSignSample(isAudit,isValid,ids);
		AvSignSampleCfg entity = new AvSignSampleCfg();
		String[] idArray = ids.split(",");
		for(String id :idArray){
			entity = avCfgService.getAvSignSampleById(Long.parseLong(id));
			entity.setIsAudit(isAudit);
			entity.setIsValid(isValid);
			entity.setAuditorId(UserUtils.getUser().getId());
			entity.setAuditTime(new Date());
			try {
				avCfgService.audioAuditAvSignSample(entity,isAudit,false);
			}catch(Exception e){
				e.printStackTrace();
				if(e instanceof MaatConvertException) {
					logger.error("音视频标识样例配置下发失败："+e.getMessage());
					addMessage(redirectAttributes,"error","request_service_failed");
				}else {
					logger.error("auditAvAudioSignSample failed",e);
					addMessage(redirectAttributes,"error","audit_failed");
				}
			}
		}
		return "redirect:" + adminPath +"/ntc/av/sample/signSampleList?functionId="+functionId;
	}
	//修改标志样例配置审核状态
	/**
	 * 审核配置下发，为了保证配置下发过程事务正确，一条配置提交一次
	 * @param isAudit
	 * @param isValid
	 * @param ids
	 * @param functionId
	 * @return
	 */
	@RequestMapping(value = {"/sample/auditAvSignSample"})
	public String auditAvSignSample(Integer preset,AvSignSampleCfgModel cfg,Integer functionId,RedirectAttributes redirectAttributes){
		//预置配置
		try {
			if(preset!=null&&1==preset){//预置信息需要在数据库中插入一条记录
				for (AvSignSampleCfg avSignSampleCfg : cfg.getCfgs()) {
					avCfgService.saveOrUpdateAvSignSample(avSignSampleCfg);
				}
				addMessage(redirectAttributes,"success","save_success");
				return "redirect:" + adminPath +"/ntc/av/sample/signSampleList?functionId="+functionId;
			}
		} catch (Exception e1) {
			if(e1 instanceof MaatConvertException) {
				logger.error("预置失败",e1);
				addMessage(redirectAttributes,"error","request_service_failed");
			}else {
				logger.error("auditAvSignSample failed",e1);
				addMessage(redirectAttributes,"error","save_failed");
			}
		}
		//修改下发配置
		if(cfg != null && cfg.getCfgs() != null ){
			for (AvSignSampleCfg avSignSampleCfg : cfg.getCfgs()) {
				/*if(StringUtil.isEmpty(avSignSampleCfg.getIsValid())){
					avSignSampleCfg.setIsValid(0);
				}*/
				if(cfg.getAction()!=null){
					avSignSampleCfg.setAction(cfg.getAction());
				}
				//isValid值变更才下发
//				if(!avSignSampleCfg.getAction().equals(avSignSampleCfg.getActionOld())||!avSignSampleCfg.getIsValid().equals(avSignSampleCfg.getIsValidOld())){
					try {
						avCfgService.auditAvSignSample(avSignSampleCfg);
						addMessage(redirectAttributes,"success","save_success");
					}catch(Exception e){
						e.printStackTrace();
						if(e instanceof MaatConvertException) {
							logger.error("下发失败",e);
							addMessage(redirectAttributes,"error","request_service_failed");
						}else {
							logger.error("auditAvSignSample failed",e);
							addMessage(redirectAttributes,"error","save_failed");
						}
					}
				}
				
//			}
		}
		return "redirect:" + adminPath +"/ntc/av/sample/signSampleList?functionId="+functionId;
	}
	
	//ysp配置导出
	@RequestMapping(value = "exportYsp")
	public void exportYsp(Model model,HttpServletRequest  request,HttpServletResponse response,
			@ModelAttribute("cfg")AvFileSampleCfg entity,String ids,RedirectAttributes redirectAttributes){
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
				Page<AvFileSampleCfg> pageInfo=new Page<AvFileSampleCfg>(request, response,"a");
				pageInfo.setPageNo(1);
				pageInfo.setPageSize(Constants.MAX_EXPORT_SIZE);
				Page<AvFileSampleCfg> page = avCfgService.getAvFileSampleList(pageInfo, entity);
				titleList.add(entity.getMenuNameCode());
				classMap.put(entity.getMenuNameCode(), AvFileSampleCfg.class);
				String cfgIndexInfoNoExport=",group_name,userregion1,userregion2,userregion3,userregion4,userregion5,";
				noExportMap.put(entity.getMenuNameCode(),cfgIndexInfoNoExport);
				dataMap.put(entity.getMenuNameCode(), page.getList());
			/*}*/
			this._export(model, request, response, redirectAttributes,entity.getMenuNameCode(),titleList,classMap,dataMap,noExportMap);
		} catch (Exception e) {
			logger.error("ysp export failed",e);
			addMessage(redirectAttributes,"error","export_failed");
		}
		//return "redirect:" + adminPath +"/ntc/iplist/list?functionId="+entity.getFunctionId();
	}
	
	/**
	 * 验证音视频时长
	 * @throws EncoderException 
	 * @throws InputFormatException 
	 */
	public boolean validateAvDuration(File file) throws InputFormatException, EncoderException{
		//验证音视频文件时长
		Encoder encoder = new Encoder();
		String length = "";
		MultimediaInfo m = encoder.getInfo(file);
		long ls = m.getDuration()/1000;
		int hour = (int) (ls/3600);
		int minute = (int) (ls%3600)/60;
		int second = (int) (ls-hour*3600-minute*60);
		length = hour+"'"+minute+"''"+second+"'''";
		logger.info(file.getName()+"时长："+length);
		if(ls>0 && ls>Constants.AV_DURATION_LIMIT){
			return false;
		}else{
			return true;
		}
	}
	/**
	 * 上传视频文件，调用脚本生成关键帧图片，返回图片保存路径
	 * @param cfg
	 * @param functionId
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = {"/sample/videoToPicture"})
	public Map videoToPicture(Model model,@RequestParam("srcFile") CommonsMultipartFile srcFile){
		
		String sep = System.getProperty("file.separator");
		String random = UUID.randomUUID()+"";
		String srcFilePath = Constants.AV_FILE_PATH+"video"+sep+"srcFile";//视频源文件保存路径
		String picFilePath = StringUtils.getUserfilesBaseDir()+"video"+sep+"picFile"+sep+random+sep;//视频生成的关键帧图片文件保存路径 
		
		FileUtils.createDirectory(srcFilePath);
		FileUtils.createDirectory(picFilePath);
		String srcFileAllPath = srcFilePath+sep+random+FileUtils.getSuffix(srcFile.getOriginalFilename(), true);//新的文件名
		File uploadSrcFile = new File(srcFileAllPath);
		Map map = new HashMap();
		map.put("picFilePath", picFilePath);
		try {
			FileCopyUtils.copy(srcFile.getBytes(), uploadSrcFile);//保存源文件
			if(validateAvDuration(uploadSrcFile)){
				String shellName = Constants.VEDIO_TO_PICTURE_PROC;
//				String params = srcFileAllPath+" "+picFilePath+" 0.95 90.0 0.5";
				String[] params = new String[]{srcFileAllPath,picFilePath};
				logger.info("调用视频转关键帧图片程序："+shellName+" " +StringUtils.join(params," "));
				Map resultMap = avCfgService.execShell(shellName, params);
				if(resultMap.get("exitStatus").equals(0)){//调用外部程序成功
					String out = resultMap.get("out").toString();//输出参数
					logger.info("调用视频转关键帧图片程序输出参数："+out);
					JSONArray resArray =JSONArray.fromObject(out);
					JSONObject resObject = resArray.getJSONObject(0);
					int state = resObject.getInt("state");
					if(state==1){
						//关键帧图片生成成功，删除原视频文件
						FileUtils.deleteFile(srcFileAllPath);
						//获取图片列表
						Collection<File> files = FileUtils.listFiles(new File(picFilePath), null, true);
						String srcPath = null;
						for(File f:files){
							if(srcPath==null){
								srcPath = f.getName();
							}else{
								srcPath += "|"+f.getName();
							}
						}
						//将图片文件列表赋值给生成样例的源文件路径参数
						map.put("srcPath", srcPath);
						map.put("status", 1);
						map.put("msg", "success");
					}else{
						if(StringUtil.isEmpty(resObject.getString("message"))){
							map.put("status", 0);
							map.put("msg", getMsgProp().getProperty("call_external_procedures_failed", "Call external procedures failed"));
						}else{
							map.put("status", 0);
							map.put("msg", resObject.getString("message"));
						}
					}
					
				}else{
					map.put("status", 0);
					map.put("msg", getMsgProp().getProperty("call_external_procedures_failed", "Call external procedures failed"));
				}
			}else{
				map.put("status", 0);
				map.put("msg", this.getMsgProp().get("exceeds_duration_limit"));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			map.put("status", 0);
			map.put("msg", e.getMessage());
		} catch (InputFormatException e) {
			e.printStackTrace();
			map.put("status", 0);
			map.put("msg", e.getMessage());
		} catch (EncoderException e) {
			e.printStackTrace();
			map.put("status", 0);
			map.put("msg", e.getMessage());
		}
		return map;
	}
	/**
	 * 上传图片文件，调用脚本压缩图片，返回图片保存路径
	 * @param cfg
	 * @param functionId
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = {"/sample/faceToPicture"})
	public Map faceToPicture(Model model,@RequestParam("srcFile") CommonsMultipartFile[] srcFile,@RequestParam("faceFilePath")String faceFilePath){
		String sep = System.getProperty("file.separator");
		String random = UUID.randomUUID()+"";
//		String srcFilePath = Constants.AV_FILE_PATH+"face"+random+sep+"srcFile";//视频源文件保存路径
		String srcFilePath="";
		if(StringUtil.isBlank(faceFilePath)){
			srcFilePath = StringUtils.getUserfilesBaseDir()+"face"+sep+"picFile"+sep+random+sep;//视频生成的关键帧图片文件保存路径 
			faceFilePath=srcFilePath;
			FileUtils.createDirectory(srcFilePath);
		}else{
			srcFilePath=faceFilePath;
		}
		//保存源文件
		for (CommonsMultipartFile sinFile : srcFile) {
			String r = UUID.randomUUID()+"";
			String srcFileAllPath = srcFilePath+sep+r+FileUtils.getSuffix(sinFile.getOriginalFilename(), true);//新的文件名
			File uploadSrcFile = new File(srcFileAllPath);
			try {
				FileCopyUtils.copy(sinFile.getBytes(), uploadSrcFile);
			} catch (IOException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
		
		
		Map map = new HashMap();
		map.put("picFilePath", srcFilePath);
		map.put("faceFilePath", faceFilePath);
		
		//获取图片列表
		Collection<File> files = FileUtils.listFiles(new File(srcFilePath), null, true);
					String srcPath = null;
					for(File f:files){
						if(srcPath==null){
							srcPath = f.getName();
						}else{
							srcPath += "|"+f.getName();
						}
					}
					//将图片文件列表赋值给生成样例的源文件路径参数
					map.put("srcPath", srcPath);
					map.put("status", 1);
					map.put("msg", "success");
			
		return map;
	}
	@RequestMapping(value = {"/sample/selectVedioPicture"})
	public String selectVedioPicture(Model model,HttpServletRequest request,String picFilePath,String srcPath){
		Collection<File> files = FileUtils.listFiles(new File(picFilePath), null, true);
		List<Map<String,String>> fileList = new ArrayList();
		String picUrl = picFilePath.substring(picFilePath.indexOf(Configurations.getStringProperty("userfiles.basedir", "upload")));
		String sep = System.getProperty("file.separator");
		String[] checkedPic = null;
		if(srcPath!=null){
			checkedPic = srcPath.split("\\|");
		}
		for(File f:files){
			Map map = new HashMap();
			map.put("picUrl", StringUtils.replace(picUrl+"/"+f.getName(), "\\", "/"));
			map.put("picName", f.getName());
			boolean checked = false;
			if(checkedPic!=null){
				for(String pic:checkedPic){
					if(f.getName().equals(pic)){
						checked = true;
					}
				}
			}else{
				checked = true;//默认全选
			}
			
			map.put("checked", checked);
			fileList.add(map);
		}
		model.addAttribute("fileList", fileList);
		model.addAttribute("picFilePath",picFilePath);
		return "/cfg/av/videoPictureList";
	}
	
}
