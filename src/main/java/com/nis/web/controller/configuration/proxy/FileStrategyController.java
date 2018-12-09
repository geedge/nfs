/**
 *@Title: ControlController.java
 *@Package com.nis.web.controller.configuration.proxy 
 *@Description TODO
 *@author dell
 *@date 2018年6月22日 下午4:35:42
 *@version  版本号
 */
package com.nis.web.controller.configuration.proxy;

import java.io.File;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;
import com.nis.domain.Page;
import com.nis.domain.callback.ProxyFileStrategyCfg;
import com.nis.domain.maat.ToMaatResult;
import com.nis.domain.maat.ToMaatResult.ResponseData;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.FileUtils;
import com.nis.util.JsonMapper;
import com.nis.util.StringUtil;
import com.nis.web.controller.configuration.CommonController;

/**
 * @ClassName: ControlController.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年6月22日 下午4:35:42
 * @version V1.0
 */
@Controller
@RequestMapping("${adminPath}/proxy/fileStrategy")
public class FileStrategyController extends CommonController {
	
	@RequestMapping(value = {"list"})
	public String fileStrategyList(ProxyFileStrategyCfg cfg,Model model,HttpServletRequest request,HttpServletResponse response) {
		Page<ProxyFileStrategyCfg> page = proxyFileStrategyService.findPage(new Page<ProxyFileStrategyCfg>(request, response,"a"), cfg);
		model.addAttribute("page", page);
		model.addAttribute("cfg", cfg);
		initPageCondition(model);
		return "/cfg/proxy/fileStrategy/list";
	}

	@RequestMapping(value = {"form"})
	@RequiresPermissions(value={"proxy:fileStrategy:config"})
	public String fileform(Model model,HttpServletRequest request
			,HttpServletResponse response
			,@ModelAttribute("cfg")ProxyFileStrategyCfg cfg
			,String ids
			,RedirectAttributes redirectAttributes) {
		if(!StringUtil.isEmpty(ids)){
			cfg = proxyFileStrategyService.getCfgById(Long.valueOf(ids));
		}
//		initFormCondition(model);
		initFormCondition(model, cfg);
		model.addAttribute("_cfg", cfg);
		return "/cfg/proxy/fileStrategy/form";
	}

	/**
	 * 保存 文件
	 * 1、将文件上传到 service 接口
	 * 2、将文件信息及 1返回的结果 保存到数据库
	 * @param model
	 * @param request
	 * @param response
	 * @param cfgFile
	 * @param cfg
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"proxy:fileStrategy:config"})
	@RequestMapping(value = {"saveOrUpdate"})
	public String saveOrUpdate(Model model,HttpServletRequest request
			,HttpServletResponse response,MultipartFile cfgFile,
			@ModelAttribute("cfg")ProxyFileStrategyCfg cfg,
			RedirectAttributes redirectAttributes){
		File file = null;
		try{
			if(cfgFile != null) {
				String filename = cfgFile.getOriginalFilename();
				String prefix = FileUtils.getPrefix(filename, false);
				String suffix = FileUtils.getSuffix(filename, false);
				file = File.createTempFile("file_"+ prefix, suffix);
				cfgFile.transferTo(file);//复制文件
				String md5 = FileUtils.getFileMD5(file);
				Map<String,Object> srcMap = Maps.newHashMap();
				srcMap.put("filetype", suffix);
				srcMap.put("datatype", "dbSystem");//源文件存入数据中心
				srcMap.put("createTime",new Date());
				srcMap.put("key",prefix);
				srcMap.put("fileName", filename);
				srcMap.put("checksum", md5);
				ToMaatResult result = ConfigServiceUtil.postFileCfg(null, file, JsonMapper.toJsonString(srcMap));
				logger.info("http 重定向阻断文件上传响应信息："+JsonMapper.toJsonString(result));
				String srcAccessUrl = null;
				if(!StringUtil.isEmpty(result)){
					ResponseData data = result.getData();
					srcAccessUrl=data.getAccessUrl();
					cfg.setUrl(srcAccessUrl);;
				}
				cfg.setMd5(md5);//文件md5值
				cfg.setContentLength(file.length());//文件长度
 			}
			proxyFileStrategyService.saveOrUpdate(cfg);
			addMessage(redirectAttributes,"success","save_success");
		}catch(Exception e){
			logger.error("信息保存失败",e);
			if(e instanceof MaatConvertException) {
				addMessage(redirectAttributes,"error","request_service_failed");
			}else {
				addMessage(redirectAttributes,"error","save_failed");
			}
		}finally {
			if(file != null) {
				file.delete();//删除临时文件
			}
		}
		return "redirect:" + adminPath +"/proxy/fileStrategy/list?functionId="+cfg.getFunctionId();
	}
	
	@RequestMapping(value = {"delete"})
	@RequiresPermissions(value={"proxy:fileStrategy:config"})	
	public String delete(Integer isAudit,Integer isValid,
			String ids,
			Integer functionId,
			RedirectAttributes redirectAttributes){
		proxyFileStrategyService.update(isAudit,isValid,ids,functionId);
		return "redirect:" + adminPath +"/proxy/fileStrategy/list?functionId="+functionId;
	}
	
	
	@RequestMapping(value = {"audit"})
	@RequiresPermissions(value={"proxy:fileStrategy:confirm"})
	public String audit(Integer isAudit,Integer isValid,String ids
			,Integer functionId, RedirectAttributes redirectAttributes) {
		if(!StringUtil.isEmpty(ids)){
			proxyFileStrategyService.audit(isAudit, isValid, functionId, ids);
		}
		return "redirect:" + adminPath +"/proxy/fileStrategy/list?functionId="+functionId;
	}
}
