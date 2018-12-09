/**
 *@Title: CommonController.java
 *@Package com.nis.web.controller.configuration 
 *@Description TODO
 *@author dell
 *@date 2018年6月22日 下午6:35:37
 *@version  版本号
 */
package com.nis.web.controller.configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis2.databinding.types.soapencoding.Array;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nis.domain.FunctionRegionDict;
import com.nis.domain.FunctionServiceDict;
import com.nis.domain.Page;
import com.nis.domain.basics.ServiceDictInfo;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.HttpBodyCfg;
import com.nis.domain.configuration.HttpReqHeadCfg;
import com.nis.domain.configuration.HttpResHeadCfg;
import com.nis.domain.configuration.HttpUrlCfg;
import com.nis.domain.configuration.template.IpAllTemplate;
import com.nis.domain.configuration.template.IpCfgTemplate;
import com.nis.domain.specific.ConfigGroupInfo;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.configuration.NtcSubscribeIdCfg;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.Constants;
import com.nis.util.DateUtils;
import com.nis.util.DictUtils;
import com.nis.util.StringUtil;
import com.nis.util.excel.ExportExcel;
import com.nis.util.excel.ImportExcel;
import com.nis.web.controller.BaseController;
import com.nis.web.dao.configuration.IpCfgDao;
import com.nis.web.security.UserUtils;

/**
 * @ClassName: CommonController.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年6月22日 下午6:35:37
 * @version V1.0
 */
public class CommonController extends BaseController {
	public void _ipList(String cfgName,Model model,IpPortCfg cfg,HttpServletRequest request,HttpServletResponse response) {
		model.addAttribute("cfgName", cfgName);
		cfg.setTableName(IpPortCfg.getTablename());
		Page<BaseIpCfg> searchPage=new Page<BaseIpCfg>(request,response,"r");
		Page<BaseIpCfg> page = ipCfgService.findPage(searchPage, cfg);
		model.addAttribute("page", page);
		initPageCondition(model,cfg);
	}
	public void _domainList(String cfgName,Model model,HttpUrlCfg cfg,HttpServletRequest request,HttpServletResponse response) {
		model.addAttribute("cfgName", cfgName);
		cfg.setTableName(HttpUrlCfg.getTablename());
		Page<HttpUrlCfg> searchPage=new Page<HttpUrlCfg>(request,response,"r");
		Page<HttpUrlCfg> page = domainService.findPage(searchPage, cfg);
		model.addAttribute("page", page);
		initPageCondition(model,cfg);
	}
	public void _ipForm(String cfgName,Model model,String ids,Integer functionId,BaseIpCfg entity) {
		model.addAttribute("cfgName", cfgName);
		if(StringUtils.isNotBlank(ids)){
			entity = ipCfgService.getIpCfgById(IpPortCfg.getTablename(),Long.parseLong(ids));
		}
		if(entity.getCfgId()!=null){
			model.addAttribute("_cfg", entity);
			initUpdateFormCondition(model,entity);
		}else{
			IpPortCfg cfg=new IpPortCfg();
			cfg.initDefaultValueImpl();
			cfg.setFunctionId(entity.getFunctionId());
			cfg.setProtocolId(entity.getProtocolId());
			model.addAttribute("_cfg", cfg);
			initFormCondition(model,entity);
		}
	}
	public void _domainForm(String cfgName,Model model,String ids,Integer functionId,HttpUrlCfg entity) {
		model.addAttribute("cfgName", cfgName);
		if(StringUtils.isNotBlank(ids)){
			entity = domainService.getStringCfgById(Long.parseLong(ids));
		}
		if(entity.getCfgId()!=null){
			model.addAttribute("_cfg", entity);
			initUpdateFormCondition(model,entity);
		}else{
			HttpUrlCfg cfg=new HttpUrlCfg();
			cfg.initDefaultValue();
			cfg.setFunctionId(entity.getFunctionId());
			model.addAttribute("_cfg", cfg);
			initFormCondition(model,entity);
		}
	}
	public void _saveOrUpdateIp(String cfgName,RedirectAttributes model, IpPortCfg cfg) {
		model.addAttribute("cfgName", cfgName);
		Date date=new Date();
		cfg.setTableName(IpPortCfg.getTablename());
		logger.info("saveOrUpdateIp loaded");
		try{
			cfg.setIsValid(Constants.VALID_NO);
			cfg.setIsAudit(Constants.AUDIT_NOT_YET);
			if(cfg.getCfgId()==null){//新增
				cfg.setCreatorId(cfg.getCurrentUser().getId());
				cfg.setCreateTime(date);
				ipCfgService.addIpCfg(cfg);
			}else{//修改
				cfg.setEditorId(cfg.getCurrentUser().getId());
				cfg.setEditTime(new Date());
				ipCfgService.updateIpCfg(cfg);
			} 
			addMessage(model,"success","save_success");
		}catch(Exception e){
			logger.error("保存失败",e);
			if(e instanceof MaatConvertException) {
				addMessage(model,"error", "request_service_failed");
			}else {
				addMessage(model,"error","save_failed");
			}
		}
	}
	public void _saveOrUpdateDomain(String cfgName,RedirectAttributes model, HttpUrlCfg cfg) {
		model.addAttribute("cfgName", cfgName);
		cfg.setTableName(HttpUrlCfg.getTablename());
		logger.info("saveOrUpdateDomain loaded");
		try{
			Date date=new Date();
			cfg.setIsValid(Constants.VALID_NO);
			cfg.setIsAudit(Constants.AUDIT_NOT_YET);
			if(cfg.getCfgId()==null){//新增
				cfg.setCreatorId(cfg.getCurrentUser().getId());
				cfg.setCreateTime(date);
				domainService.addStringCfg(cfg);
			}else{//修改
				cfg.setEditorId(cfg.getCurrentUser().getId());
				cfg.setEditTime(new Date());
				domainService.updateStringCfg(cfg);
			} 
			addMessage(model,"success","save_success");
		}catch(Exception e){
			logger.error("保存失败",e);
			if(e instanceof MaatConvertException) {
				addMessage(model,"error", "request_service_failed");
			}else {
				addMessage(model,"error", "save_failed");
			}
		}
	}
	public void _deleteIp(String cfgName,String ids,String compileIds,Integer functionId,RedirectAttributes model) {
		model.addAttribute("cfgName", cfgName);
		try{
			ipCfgService.deleteIp(ids,compileIds,functionId.intValue());
			addMessage(model,"success","delete_success");
		}catch(Exception e){
			logger.error("删除失败", e);
			addMessage(model,"error","delete_failed");
		}
	}
	public void _deleteDomain(String cfgName,String ids,String compileIds,Integer functionId,RedirectAttributes model) {
		model.addAttribute("cfgName", cfgName);
		try{
			domainService.deleteDomain(ids,compileIds,functionId.intValue());
			addMessage(model,"success","delete_success");
		}catch(Exception e){
			logger.error("删除失败", e);
			addMessage(model,"error","delete_failed");
		}
	}
	public void _auditIp(String cfgName,String ids,IpPortCfg cfg,RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute("cfgName", cfgName);
		try{
			for(String id:ids.split(",")){
				Long.parseLong(id);
			}
			List<BaseIpCfg> beans=ipCfgService.getListByCfgId(IpPortCfg.getTablename(),cfg.getFunctionId().intValue(),ids);
			Date date=new Date();
			for(BaseIpCfg bean:beans){
				bean.setTableName(IpPortCfg.getTablename());
				bean.setAuditorId(bean.getCurrentUser().getId());
				bean.setAuditTime(date);
				bean.setIsAudit(cfg.getIsAudit());
				bean.setIsValid(cfg.getIsValid());
				ipCfgService.audit(bean);
			}
			addMessage(redirectAttributes,"success","audit_success");
		}catch(MaatConvertException e){
			logger.error("审核失败", e);
			addMessage(redirectAttributes,"error","request_service_failed");
		}catch(Exception e){
			logger.error("审核失败", e);
			addMessage(redirectAttributes,"error","audit_failed");
		}
	}
	public void _auditDomain(String ids,HttpUrlCfg cfg,RedirectAttributes redirectAttributes) {
		try{
			for(String id:ids.split(",")){
				Long.parseLong(id);
			}
			List<HttpUrlCfg> beans=domainService.getListByCfgId(ids);
			Date date=new Date();
			for(HttpUrlCfg bean:beans){
				HttpUrlCfg domain=new HttpUrlCfg();
				BeanUtils.copyProperties(bean, domain);
				domain.setTableName(HttpUrlCfg.getTablename());
				domain.setAuditorId(bean.getCurrentUser().getId());
				domain.setAuditTime(date);
				domain.setIsAudit(cfg.getIsAudit());
				domain.setIsValid(cfg.getIsValid());
				domainService.auditDomain(domain);
			}
			addMessage(redirectAttributes,"success","audit_success");
		}catch(MaatConvertException e){
			logger.error("审核失败", e);
			addMessage(redirectAttributes,"error", "request_service_failed");
		}catch(Exception e){
			logger.error("审核失败", e);
			addMessage(redirectAttributes,"error", "audit_failed");
		}
	}
	/*public void _importIp(RedirectAttributes redirectAttributes,MultipartFile file,IpPortCfg ipCfg) {
		try {
			ImportExcel ei = new ImportExcel(file, 0, 0);
			List<IpCfgTemplate> list = ei.getDataList(IpCfgTemplate.class);
			this.checkIpCfg(ipCfg.getAction().intValue(),ipCfg.getFunctionId(),list);
			List<BaseIpCfg> ipList = new ArrayList<>();
			Date date=new Date();
			for(IpCfgTemplate cfg : list){
				IpPortCfg ip = new IpPortCfg();
				BeanUtils.copyProperties(ipCfg, ip);
	        	BeanUtils.copyProperties(cfg, ip);
	        	ip.setTableName(IpPortCfg.getTablename());
	        	ip.setIsAreaEffective(0);
	        	ip.setIsValid(Constants.VALID_NO);
	        	ip.setIsAudit(Constants.AUDIT_NOT_YET);
	        	ip.setCreatorId(ipCfg.getCurrentUser().getId());
	        	ip.setCreateTime(date);
	        	ip.setCompileId(ipCfgService.getCompileId());
	        	ipList.add(ip);
			}
			ipCfgService.saveBatch(ipList, IpCfgDao.class);
		} catch (Exception e) {
			addMessage(redirectAttributes,  e.getMessage());
			e.printStackTrace();
		}
	}*/
	/*public void _importIp(String cfgName,RedirectAttributes redirectAttributes,MultipartFile file,IpPortCfg ipCfg,Class clazz) {
		redirectAttributes.addAttribute("cfgName", cfgName);
		try {
			ImportExcel ei = new ImportExcel(file, 0, 0);
			List<IpCfgTemplate> list = ei.getDataList(clazz);
			this.checkIpCfg(ipCfg.getAction().intValue(),ipCfg.getFunctionId(),list);
			List<BaseIpCfg> ipList = new ArrayList<>();
			Date date=new Date();
			for(IpCfgTemplate cfg : list){
				IpPortCfg ip = new IpPortCfg();
				BeanUtils.copyProperties(ipCfg, ip);
				BeanUtils.copyProperties(cfg, ip);
				ip.setTableName(IpPortCfg.getTablename());
				ip.setIsAreaEffective(0);
				ip.setIsValid(Constants.VALID_NO);
				ip.setIsAudit(Constants.AUDIT_NOT_YET);
				ip.setCreatorId(ipCfg.getCurrentUser().getId());
				ip.setCreateTime(date);
				ip.setCompileId(ipCfgService.getCompileId());
				ip.setDnsStrategyId(cfg.getDnsStrategyId());
				ipList.add(ip);
			}
			ipCfgService.saveBatch(ipList, IpCfgDao.class);
		} catch (Exception e) {
			addMessage(redirectAttributes,  e.getMessage());
			e.printStackTrace();
		}
	}*/
	public void _importFileTemplate(HttpServletRequest request,HttpServletResponse response, 
    		RedirectAttributes redirectAttributes,Integer functionId,Integer cfgRegionCode) {
		this.importCfgTemplate(request, response, redirectAttributes, functionId, cfgRegionCode);
	}
	@Deprecated
	public void _importFileTemplate(HttpServletRequest request,HttpServletResponse response, 
			RedirectAttributes redirectAttributes,Integer functionId,Integer cfgRegionCode,Class clazz) {
		this.importCfgTemplate(request, response, redirectAttributes, functionId, cfgRegionCode,clazz);
	}
	//ip配置导出
	public void _exportIp(String columns,Model model,HttpServletRequest  request,HttpServletResponse response,
			IpPortCfg entity,String ids,RedirectAttributes redirectAttributes){
		try {
			//获取国际化配置
			Properties msgProp = getMsgProp();
			//获取分类、性质、标签
			List<ServiceDictInfo> fls=serviceDictInfoService.findAllFlDict();
			List<ServiceDictInfo> xzs=serviceDictInfoService.findAllXzDict();
			List<ServiceDictInfo> labels=serviceDictInfoService.findAllLableDict();
			Map<Object, Object> map=new HashMap<Object, Object>();
			map.put("fls", fls);
			map.put("xzs", xzs);
			map.put("labels", labels);
			
            String fileName = msgProp.getProperty("date_list", "data")+"-"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            
            //导出选中记录
    		if(!StringUtil.isEmpty(ids)){
    			for(String id:ids.split(",")){
    				Long.parseLong(id);
    			}
    			List<BaseIpCfg> list=ipCfgService.getListByCfgIdWithName(IpPortCfg.getTablename(), entity.getFunctionId(), ids);
    			new ExportExcel(columns,msgProp,null, IpPortCfg.class,1).setDataList(columns,msgProp,list,map).write(response, fileName).dispose();
    		}else{
			//条件导出数据大于最大导出数，只导出最大导出条数
    		entity.setTableName(IpPortCfg.getTablename());
			Page<BaseIpCfg> pageInfo=new Page<BaseIpCfg>(request, response,"r");
			if(pageInfo.getCount()>Constants.MAX_EXPORT_SIZE){
				pageInfo.setPageNo(1);
				pageInfo.setPageSize(Constants.MAX_EXPORT_SIZE);
			}else{
				pageInfo.setPageNo(1);
				pageInfo.setPageSize(-1);
			}
			Page<BaseIpCfg> page = ipCfgService.findPage(pageInfo, entity);
			new ExportExcel(columns,msgProp,null, IpPortCfg.class,1).setDataList(columns,msgProp,page.getList(),map).write(response, fileName).dispose();
    		}
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(redirectAttributes,"error", "export_failed" );
		}
	}
}
