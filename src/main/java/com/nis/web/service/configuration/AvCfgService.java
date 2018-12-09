package com.nis.web.service.configuration;



import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.nis.domain.Page;
import com.nis.domain.configuration.AvFileSampleCfg;
import com.nis.domain.configuration.AvSignSampleCfg;
import com.nis.domain.maat.ToMaatResult;
import com.nis.domain.maat.ToMaatResult.ResponseData;
import com.nis.exceptions.CallExternalProceduresException;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.Constants;
import com.nis.util.FileUtils;
import com.nis.util.JsonMapper;
import com.nis.util.StringUtil;
import com.nis.web.dao.configuration.AvCfgDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.BaseService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 音视频配置相关事务类
 * @author dell
 *
 */
@Service
public class AvCfgService extends BaseService{
//	@Autowired
//	protected FunctionRegionDictDao functionRegionDictDao;
//	@Autowired
//	protected FunctionServiceDictDao functionServiceDictDao;
	@Autowired
	protected AvCfgDao avCfgDao;
	
	public AvFileSampleCfg getAvFileSampleById(Long cfgId,Integer compileId){
		return avCfgDao.getAvFileSampleById(cfgId,compileId);
	}
	public AvSignSampleCfg getAvSignSampleById(Long cfgId){
		return avCfgDao.getAvSignSampleById(cfgId);
	}
	public Page<AvFileSampleCfg> getAvFileSampleList(Page<AvFileSampleCfg> page, AvFileSampleCfg entity){
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"a"));
		entity.setPage(page);
		List<AvFileSampleCfg> list = avCfgDao.getAvFileSampleList(entity);
		page.setList(list);
		return page;
	}
	public Page<AvSignSampleCfg> getAvSignSampleList(Page<AvSignSampleCfg> page, AvSignSampleCfg entity){
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"a"));
		entity.setPage(page);
		List<AvSignSampleCfg> list = avCfgDao.getAvSignSampleList(entity);
		page.setList(list);
		return page;
	}
	public List<AvSignSampleCfg> getSignSampleList(AvSignSampleCfg entity){
		List<AvSignSampleCfg> list = avCfgDao.getAvSignSampleList(entity);
		return list;
	}
	public void saveOrUpdateAvFileSample(AvFileSampleCfg entity,MultipartFile srcFile){
		//设置区域运营商信息
		setAreaEffectiveIds(entity);
		if(entity.getCfgId()==null){
			entity.setCreatorId(UserUtils.getUser().getId());
			entity.setCreateTime(new Date());
			//调用服务接口获取compileId
			Integer compileId = 0;
			try {
				List<Integer> compileIds = ConfigServiceUtil.getId(1,1);
				if(!StringUtil.isEmpty(compileIds)){
					compileId = compileIds.get(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("获取编译ID出错");
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+e.getMessage());
			}
			if(compileId!=0){
				entity.setCompileId(compileId);
				entity.setIsSampleCreated(0);
				File uploadSrcFile = new File(entity.getSrcPath());
				String srcMd5 = FileUtils.getFileMD5(uploadSrcFile);
				entity.setSrcMd5(srcMd5);
				avCfgDao.insertAvFileSample(entity);
				
				//调用外部脚本，生成样例文件
				entity = createSampleFileParam(entity);
				avCfgDao.updateAvFileSample(entity);
				
			}else{
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>");
			}
		}else{
			AvFileSampleCfg oldEntity = this.getAvFileSampleById(entity.getCfgId(),entity.getCompileId());
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			entity.setIsValid(0);
			entity.setIsAudit(0);
			if(null!=srcFile){
				//删除旧的文件
				FileUtils.deleteFile(oldEntity.getSrcPath());
				FileUtils.deleteFile(oldEntity.getSamplePath());
				
				//调用外部脚本，生成样例文件
				entity = createSampleFileParam(entity);
			}
			
			avCfgDao.updateAvFileSample(entity);
		}
	}
	/**
	 * 处理创建样例文件所需输入输出参数
	 * @param entity
	 * @return
	 */
	public AvFileSampleCfg createSampleFileParam(AvFileSampleCfg entity){
		List list = new ArrayList();
		Map<String,Object> map = new HashMap();
		map.put("srcFile", entity.getSrcPath());
		map.put("dstFile", entity.getSamplePath());
		map.put("resultFile", entity.getResultPath());
		map.put("fileId", entity.getCfgId());
		list.add(map);				
		String param = gsonToJson(list);
		String sampleCreatelProc = "";
		if(entity.getCfgType().equals(Constants.AV_SAMPLE_AUDIO_REGION)||
				entity.getCfgType().equals(Constants.AV_SAMPLE_VOIP_REGION)){//音频样例配置或VOIP样例配置
			sampleCreatelProc = Constants.AUDIO_SAMPLE_CREATE_PROC;
			if(Constants.AUDIO_SAMPLE_PROC_PARAM_IS_TRANSLATION){
				param = StringEscapeUtils.escapeJava(param);
			}
			if(Constants.AUDIO_SAMPLE_PROC_PARAM_IS_QUOTATION){//json参数是否需要前后单引号处理
				param = "'"+param+"'";
			}
		}else if(entity.getCfgType().equals(Constants.AV_SAMPLE_VIDEO_REGION)){//视频样例配置
			sampleCreatelProc = Constants.VIDEO_SAMPLE_CREATE_PROC;
			if(Constants.VIDEO_SAMPLE_PROC_PARAM_IS_TRANSLATION){
				param = StringEscapeUtils.escapeJava(param);
			}
			if(Constants.VIDEO_SAMPLE_PROC_PARAM_IS_QUOTATION){//json参数是否需要前后单引号处理
				param = "'"+param+"'";
			}
		}else if(entity.getCfgType().equals(Constants.AV_SAMPLE_PICTURE_REGION)){//图片样例配置
			sampleCreatelProc = Constants.PICTURE_SAMPLE_CREATE_PROC;
			if(Constants.PICTURE_SAMPLE_PROC_PARAM_IS_TRANSLATION){
				param = StringEscapeUtils.escapeJava(param);
			}
			if(Constants.PICTURE_SAMPLE_PROC_PARAM_IS_QUOTATION){//json参数是否需要前后单引号处理
				param = "'"+param+"'";
			}
		}else if(entity.getCfgType().equals(Constants.MM_SPEAKER_RECOGNIZATION_REGION)){//说话人识别音频样例配置
			sampleCreatelProc = Constants.SPEAKER_SAMPLE_CREATE_PROC;
			if(Constants.SPEAKER_SAMPLE_PROC_PARAM_IS_TRANSLATION){
				param = StringEscapeUtils.escapeJava(param);
			}
			if(Constants.SPEAKER_SAMPLE_PROC_PARAM_IS_QUOTATION){//json参数是否需要前后单引号处理
				param = "'"+param+"'";
			}
		}else if(entity.getCfgType().equals(Constants.MM_LOGO_DETECTION_REGION)){//台标识别图片样例配置
			sampleCreatelProc = Constants.LOGO_SAMPLE_CREATE_PROC;
			if(Constants.LOGO_SAMPLE_PROC_PARAM_IS_TRANSLATION){
				param = StringEscapeUtils.escapeJava(param);
			}
			if(Constants.LOGO_SAMPLE_PROC_PARAM_IS_QUOTATION){//json参数是否需要前后单引号处理
				param = "'"+param+"'";
			}
		}else if(entity.getCfgType().equals(Constants.MM_FACE_RECOGNIZATION_REGION)){//人脸识别视频样例配置
			sampleCreatelProc = Constants.FACE_SAMPLE_CREATE_PROC;
			if(Constants.FACE_SAMPLE_PROC_PARAM_IS_TRANSLATION){
				param = StringEscapeUtils.escapeJava(param);
			}
			if(Constants.FACE_SAMPLE_PROC_PARAM_IS_QUOTATION){//json参数是否需要前后单引号处理
				param = "'"+param+"'";
			}
		}
		
		logger.info("调用外部程序输入参数："+param);
		try {
			Map resultMap = execShell(sampleCreatelProc,param);
			logger.info("调用外部程序结果："+resultMap);
			if(resultMap.get("exitStatus").equals(0)){//调用外部程序成功
				String out = resultMap.get("out").toString();//输出参数
				JSONArray resArray =JSONArray.fromObject(out);
				JSONObject resObject = resArray.getJSONObject(0);
				logger.info("调用外部程序输出参数："+resObject);
				int state = resObject.getInt("state");
				int fileSize = resObject.getInt("fileSize");
				Integer fileId = resObject.getInt("fileId");
				if(state==1 && fileSize>0){//成功
					entity.setIsSampleCreated(1);//样例文件创建成功
					if(fileId.equals(entity.getCfgId().intValue())){
						File uploadSampleFile = new File(entity.getSamplePath());
						String sampleMd5 = FileUtils.getFileMD5(uploadSampleFile);				
						entity.setSampleMd5(sampleMd5);
					}else{
						logger.error("调用外部程序返回fileId错误,输入的fileId为："+entity.getCfgId()+",输出的fileId为："+fileId);
						throw new CallExternalProceduresException();
					}
					//文件上传至综合服务
					Date creatTime = entity.getCreateTime();
					//音视频文件上传接口调用
					File srcFile = new File(entity.getSrcPath());
					Map<String,Object> srcMap = new HashMap();
					srcMap.put("filetype", FileUtils.getSuffix(srcFile.getName(), false));
					srcMap.put("datatype", "dbSystem");//源文件存入数据中心
					
					srcMap.put("createTime",creatTime);
					srcMap.put("key",FileUtils.getPrefix(srcFile.getName(), false));
					srcMap.put("fileName", srcFile.getName());
					srcMap.put("checksum", entity.getSrcMd5());
					ToMaatResult result1 = ConfigServiceUtil.postFileCfg(null, srcFile, JsonMapper.toJsonString(srcMap));
					logger.info("音视频源文件上传响应信息："+result1.toString());
					//获取文件上传响应信息（新的文件访问路径）
					String srcAccessUrl = null;
					if(!StringUtil.isEmpty(result1)){
						ResponseData data = result1.getData();
						srcAccessUrl=data.getAccessUrl();
						entity.setSrcUrl(srcAccessUrl);
					}
					File sampleFile = new File(entity.getSamplePath());
					Map<String,Object> sampleMap = new HashMap();
					sampleMap.put("filetype", FileUtils.getSuffix(sampleFile.getName(), false));
					sampleMap.put("datatype", "fileSystem");//样例文件存入fastdfs
					sampleMap.put("createTime", creatTime);
					sampleMap.put("key",FileUtils.getPrefix(sampleFile.getName(), false));
					sampleMap.put("fileName", sampleFile.getName());
					sampleMap.put("checksum", entity.getSampleMd5());
					ToMaatResult result2 = ConfigServiceUtil.postFileCfg(null, sampleFile, JsonMapper.toJsonString(sampleMap));
					logger.info("音视频样例文件上传响应信息："+result2.toString());					
					//获取文件上传响应信息（新的文件访问路径）
					String sampleAccessUrl = null;
					if(!StringUtil.isEmpty(result2)){
						ResponseData data = result2.getData();
						sampleAccessUrl = data.getAccessUrl();
						entity.setSampleUrl(sampleAccessUrl);
						
					}
					//删除本地源文件和样例文件
					FileUtils.deleteFile(entity.getSrcPath());
					FileUtils.deleteFile(entity.getSamplePath());
				}else{
					entity.setIsSampleCreated(-1);//样例文件创建失败
					logger.error("样例文件生成失败");
					if(StringUtil.isEmpty(resObject.getString("message"))){
						throw new CallExternalProceduresException();
					}else{
						throw new CallExternalProceduresException(resObject.getString("message"));
					}
					
				}
				
			}else{
				throw new CallExternalProceduresException();
			}
		}catch (Exception e) {
			if(e instanceof MaatConvertException ) {
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+e.getMessage());
			}else {
				throw new CallExternalProceduresException("<spring:message code=\"request_service_failed\"/>:"+e.getMessage());
			}
		}
		return entity;
	}
	public void saveOrUpdateAvSignSample(AvSignSampleCfg entity){
		//设置区域运营商信息
		setAreaEffectiveIds(entity);
		if(entity.getCfgId()==null){
			entity.setCreatorId(UserUtils.getUser().getId());
			entity.setCreateTime(new Date());
			//调用服务接口获取compileId
			Integer compileId = 0;
			/*try {
				List<Integer> compileIds = ConfigServiceUtil.getId(1,1);
				if(!StringUtil.isEmpty(compileIds)){
					compileId = compileIds.get(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("获取编译ID出错");
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+e.getMessage());
			}*/
//			if(compileId!=0){
				entity.setCompileId(compileId);
				avCfgDao.insertAvSignSample(entity);
			/*}else{
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>");
			}*/
			
		}else{
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			entity.setIsValid(0);
			entity.setIsAudit(0);
			avCfgDao.updateAvSignSample(entity);
		}
		//添加即时生效
		entity.setIsValid(1);
		entity.setIsAudit(1);
		audioAuditAvSignSample(entity,1,true);
	}
	public void updateAvFileSampleValid(Integer isAudit,Integer isValid,String ids){
		String[] idArray = ids.split(",");
		for(String id :idArray){
			AvFileSampleCfg entity = new AvFileSampleCfg();
			entity.setCfgId(Long.parseLong(id));
			entity.setIsAudit(isAudit);
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			avCfgDao.updateAvFileSampleValid(entity);
		}
		
		
	}
	public void auditAvFileSample(AvFileSampleCfg entity,Integer isAudit) throws MaatConvertException{
		//修改数据库审核状态信息
		avCfgDao.auditAvFileSample(entity);
		List<AvFileSampleCfg> list = new ArrayList<AvFileSampleCfg>();
		//一条配置提交一次综合服务
		if(isAudit==1){
			list.add(entity);
			//调用服务接口下发配置数据
			String json=gsonToJson(list);
			logger.info("音视频文件样例下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.postCallbackCfg(json);
			logger.info("音视频文件样例配置下发响应信息："+result.getMsg());

		}else if(isAudit==3){
			AvFileSampleCfg cfg = new AvFileSampleCfg();
			cfg.setIsValid(0);
			cfg.setCompileId(entity.getCompileId());
			cfg.setServiceId(entity.getServiceId());
			list.add(cfg);
			//调用服务接口取消配置
			String json=gsonToJson(list);
			logger.info("音视频文件样例下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json,2);
			logger.info("音视频文件样例取消配置响应信息："+result.getMsg());
		}
	}
	public void updateAvSignSampleValid(Integer isAudit,Integer isValid,String ids){
		String[] idArray = ids.split(",");
		for(String id :idArray){
			AvSignSampleCfg entity = new AvSignSampleCfg();
			entity.setCfgId(Long.parseLong(id));
			entity.setIsAudit(isAudit);
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			avCfgDao.updateAvSignSampleValid(entity);
		}
	}
	
	public void audioAuditAvSignSample(AvSignSampleCfg entity,Integer isAudit,boolean init) throws MaatConvertException{
		entity.setAuditTime(new Date());
		entity.setAuditorId(UserUtils.getUser().getId());
		avCfgDao.auditAvSignSample(entity);
		List<AvSignSampleCfg> list = new ArrayList<AvSignSampleCfg>();
		if(isAudit==1){		
			if(init){//预置配置时初始下发配置
				list.add(entity);
				//调用服务接口下发配置数据
				String json=gsonToJson(list);
				logger.info("文件样例下发配置参数："+json);
				//调用服务接口下发配置
				ToMaatResult result = ConfigServiceUtil.postCallbackCfg(json);
				logger.info("音视频标志样例配置下发响应信息："+result.getMsg());
			}else{
				list.add(entity);
				String json=gsonToJson(list);
				logger.info("标志状态变更："+json);
				//调用服务接口修改配置内容
				String result = ConfigServiceUtil.patch(json, 2);
				logger.info("视频标志样状态变更响应信息："+result);
			}
		}else if(isAudit==3){
			AvSignSampleCfg cfg = new AvSignSampleCfg();
			cfg.setIsValid(0);
			cfg.setCompileId(entity.getCompileId());
			cfg.setServiceId(entity.getServiceId());
			list.add(cfg);
			//调用服务接口取消配置
			String json=gsonToJson(list);
			logger.info("标志样例下发配置参数："+json);
			//调用服务接口取消配置
			ToMaatResult result = ConfigServiceUtil.put(json, 2);
			logger.info("音视频标志样例配置取消配置响应信息："+result.getMsg());
		}		
	}
	//预置配置下发
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void auditAvSignSample() throws MaatConvertException{
		try {
			AvSignSampleCfg entity = new AvSignSampleCfg();
			List<AvSignSampleCfg> avSignSampleList = avCfgDao.getAvSignSampleList(entity);
			if(avSignSampleList!=null&&avSignSampleList.size()>0){
//				avCfgDao.updateAvSignSampleValid(entity);
//				avCfgDao.auditAvSignSample(entity);
//				List<AvSignSampleCfg> list = new ArrayList<AvSignSampleCfg>();
//				avSignSampleList.add(entity);
				for (AvSignSampleCfg av : avSignSampleList) {
					av.setAuditTime(new Date());
					av.setAuditorId(UserUtils.getUser().getId());
					if(av.getIsValid()!=null&&av.getIsValid()!=1){
						av.setIsValid(1);
					}
					avCfgDao.updateAvSignSampleValid(av);
				}
				//调用服务接口取消配置
				String json=gsonToJson(avSignSampleList);
				logger.info("标志状态变更："+json);
				//调用服务接口取消配置
				ToMaatResult result = ConfigServiceUtil.postCallbackCfg(json);
				logger.info("音视频标志样状态变更响应信息："+result.getMsg());
			}else{
				logger.info("视频场景预置失败，数据为空");
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("视频场景预置失败，数据为空");
			throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>");
		}
		
	}
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void auditAvSignSample(AvSignSampleCfg entity) throws Exception{
		List<AvSignSampleCfg> list = new ArrayList<AvSignSampleCfg>();
		entity.setEditTime(new Date());
		entity.setDescription("0");
		list.add(entity);
		try {
			avCfgDao.updateAvSignSampleValid(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/*public void auditAvSignSample(AvSignSampleCfg entity,Integer isAudit) throws MaatConvertException{
		avCfgDao.auditAvSignSample(entity);
		List<AvSignSampleCfg> list = new ArrayList<AvSignSampleCfg>();
		if(isAudit==1){		
			list.add(entity);
			//调用服务接口下发配置数据
			String json=gsonToJson(list);
			logger.info("文件样例下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.postCallbackCfg(json);
			logger.info("音视频标志样例配置下发响应信息："+result.getMsg());
			
		}else if(isAudit==3){
			AvSignSampleCfg cfg = new AvSignSampleCfg();
			cfg.setIsValid(0);
			cfg.setCompileId(entity.getCompileId());
			cfg.setServiceId(entity.getServiceId());
			list.add(cfg);
			//调用服务接口取消配置
			String json=gsonToJson(list);
			logger.info("标志样例下发配置参数："+json);
			//调用服务接口取消配置
			ToMaatResult result = ConfigServiceUtil.put(json, 2);
			logger.info("音视频标志样例配置取消配置响应信息："+result.getMsg());
		}
		
	}*/
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
		try {
			Process exec = Runtime.getRuntime().exec(cmdarray);
			exec.getInputStream();
			br = new BufferedReader(
					new InputStreamReader(exec.getInputStream()));
			bre = new BufferedReader(new InputStreamReader(
					exec.getErrorStream()));
			String s = null;
			StringBuilder out = new StringBuilder();
			while ((s = br.readLine()) != null) {
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
		}
		return result;
	}
}
