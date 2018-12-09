package com.nis.web.service.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nis.domain.Page;
import com.nis.domain.SysDataDictionaryItem;
import com.nis.domain.configuration.AreaIpCfg;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.ComplexkeywordCfg;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.FileDigestCfg;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.configuration.NtcSubscribeIdCfg;
import com.nis.domain.configuration.P2pHashCfg;
import com.nis.domain.configuration.P2pKeywordCfg;
import com.nis.domain.configuration.FtpKeywordCfg;
import com.nis.domain.maat.MaatCfg;
import com.nis.domain.maat.ToMaatBean;
import com.nis.domain.maat.ToMaatResult;
import com.nis.domain.maat.MaatCfg.DigestCfg;
import com.nis.domain.maat.MaatCfg.GroupCfg;
import com.nis.domain.maat.MaatCfg.IpCfg;
import com.nis.domain.maat.MaatCfg.NumBoundaryCfg;
import com.nis.domain.maat.MaatCfg.StringCfg;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.Constants;
import com.nis.util.DictUtils;
import com.nis.util.FileUtils;
import com.nis.util.JsonMapper;
import com.nis.util.StringUtil;
import com.nis.web.dao.configuration.AreaIpCfgDao;
import com.nis.web.dao.configuration.FileTransferCfgDao;
import com.nis.web.dao.configuration.MailCfgDao;
import com.nis.web.dao.configuration.StringCfgDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.CrudService;

/**
 * 文件传输相关配置事务类
 * @author dell
 *
 */
@Service
public class FileTransferCfgService extends CrudService<FileTransferCfgDao,CfgIndexInfo> {
	@Autowired
	protected FileTransferCfgDao fileTransferCfgDao;
	@Autowired
	protected AreaIpCfgDao areaIpCfgDao;
	@Autowired
	protected MailCfgDao mailCfgDao;
	@Autowired
	protected StringCfgDao stringCfgDao;
	
	public Page<CfgIndexInfo> getFtpList(Page<CfgIndexInfo> page, CfgIndexInfo entity){
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"a"));
		entity.setPage(page);
		List<CfgIndexInfo> list = fileTransferCfgDao.getFtpList(entity);
		page.setList(list);
		return page;
	}
	public CfgIndexInfo getFtpCfg(Long cfgId,Integer compileId){
		CfgIndexInfo entity = fileTransferCfgDao.getCfgIndexInfo(cfgId,compileId);
		List<IpPortCfg> ipPortList = fileTransferCfgDao.getIpPortList(entity);
		List<BaseStringCfg> keywordList = fileTransferCfgDao.getFtpKeywordList(entity);
		//设置关键字
		if(entity.getCompileId()!=null){
			NtcSubscribeIdCfg ntcSubscribeIdCfg = new NtcSubscribeIdCfg();
			ntcSubscribeIdCfg.setCompileId(entity.getCompileId());
			List<NtcSubscribeIdCfg> ntcList = stringCfgDao.findSubscribeIdCfgList(ntcSubscribeIdCfg);
			entity.setNtcSubscribeIdCfgList(ntcList);
		}
		entity.setIpPortList(ipPortList);
		entity.setStringList(keywordList);
		return entity;
	}
	

	
	public Map<String, List>  exportftp(CfgIndexInfo entity){
		Map<String, List> dataMap=new HashMap<String, List>();
		List<IpPortCfg> ipPortList = fileTransferCfgDao.getIpPortList(entity);
		entity.setCfgType("NTC_FTP_CONTENT");
		List<BaseStringCfg> keywordList = fileTransferCfgDao.getFtpKeywordList(entity);
		entity.setCfgType("NTC_FTP_URL");
		List<BaseStringCfg> urlwordList = fileTransferCfgDao.getFtpKeywordList(entity);
		List<NtcSubscribeIdCfg> subscribeIdList = stringCfgDao.findSubscribeIdCfgListByCfgIndexInfo(entity);
		dataMap.put("NTC_SUBSCRIBE_ID", subscribeIdList);
		dataMap.put("NTC_FTP_CONTENT", keywordList);
		dataMap.put("NTC_FTP_URL", urlwordList);
		dataMap.put("NTC_UNIVERSAL_IP", ipPortList);
		return dataMap;
	}
	
	public void saveFtpCfg(List<BaseStringCfg<?>> cfgs){
		for(BaseStringCfg cfg:cfgs){
				fileTransferCfgDao.saveFtpKeywordCfg(cfg);
		}
	}
	/**
	 * 保存ftp配置
	 * @param entity
	 */
	
	public void saveFtpCfg(CfgIndexInfo entity){
		//设置区域运营商信息
		setAreaEffectiveIds(entity);
		entity.setIsValid(0);
		entity.setIsAudit(0);
		if(entity.getCfgId()==null){
			Integer compileId = 0;
			try {
				List<Integer> idList = ConfigServiceUtil.getId(1, 1);
				if(idList!=null && idList.size()>0){
					compileId = idList.get(0);
				}
				entity.setCompileId(compileId);
				entity.setCreateTime(new Date());
				entity.setCreatorId(entity.getCurrentUser().getId());
				fileTransferCfgDao.saveCfgIndex(entity);
				if(entity.getIpPortList()!=null){
					for(IpPortCfg cfg:entity.getIpPortList()){
						BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
						fileTransferCfgDao.saveIpPortCfg(cfg);
					}
				}
				if(entity.getStringList()!=null){
					for(BaseStringCfg cfg:entity.getStringList()){
						if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
							BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
							fileTransferCfgDao.saveFtpKeywordCfg(cfg);
						}
					}
				}
				if(entity.getNtcSubscribeIdCfgList()!=null){
					for(NtcSubscribeIdCfg cfg:entity.getNtcSubscribeIdCfgList()){
						if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
//							entity.setNtcSubscribeIdCfg(cfg);
							BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
							stringCfgDao.saveSubscribeIdCfg(cfg);
						}
					}
				}
				//保存区域IP信息
				if(entity.getAreaCfg()!=null){
					for(AreaIpCfg cfg:entity.getAreaCfg()){
						cfg.initDefaultValue();
						BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
						areaIpCfgDao.saveAreaIpCfg(cfg);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("获取编译ID出错");
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+e.getMessage());
			}
			
		}else{
			entity.setEditorId(entity.getCurrentUser().getId());
			entity.setEditTime(new Date());
			fileTransferCfgDao.updateCfgIndex(entity);
			//无效子配置后，再新增子配置
			fileTransferCfgDao.deleteFtpIpCfg(entity);
			fileTransferCfgDao.deleteFtpKeywordCfg(entity);
			fileTransferCfgDao.deleteFileDigestCfg(entity);
			//删除关键字配置后再新增
			if(entity!=null&&entity.getCompileId()!=null){
				NtcSubscribeIdCfg ntcSubscribeIdCfg = new NtcSubscribeIdCfg();
					ntcSubscribeIdCfg.setCompileId(entity.getCompileId());
					stringCfgDao.deleteSubscribeIdCfg(ntcSubscribeIdCfg);
			}
			AreaIpCfg area = new AreaIpCfg();
			area.setCompileId(entity.getCompileId());
			area.setFunctionId(entity.getFunctionId());
			areaIpCfgDao.deleteAreaIpCfg(area);
			entity.setCreateTime(new Date());
			entity.setCreatorId(entity.getCurrentUser().getId());
			if(entity.getIpPortList()!=null){
				for(IpPortCfg cfg:entity.getIpPortList()){
					BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
					fileTransferCfgDao.saveIpPortCfg(cfg);
				}
			}
			if(entity.getStringList()!=null){
				for(BaseStringCfg cfg:entity.getStringList()){
					if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
						BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
						fileTransferCfgDao.saveFtpKeywordCfg(cfg);
					}
				}
			}
			if(entity!=null&&entity.getNtcSubscribeIdCfgList()!=null){
				for(NtcSubscribeIdCfg cfg:entity.getNtcSubscribeIdCfgList()){
					if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
					BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
					stringCfgDao.saveSubscribeIdCfg(cfg);
					}
				}
			}
			//保存区域IP信息
			if(entity.getAreaCfg()!=null){
				for(AreaIpCfg cfg:entity.getAreaCfg()){
					cfg.initDefaultValue();
					BeanUtils.copyProperties(entity, cfg,new String[]{"cfgDesc","cfgRegionCode","cfgType"});
					areaIpCfgDao.saveAreaIpCfg(cfg);
				}
			}
		}
	}
	public void updateFtpCfgValid(Integer isValid,String ids,Integer functionId){
		String[] idArray = ids.split(",");
		for(String id :idArray){
			CfgIndexInfo entity = new CfgIndexInfo();
			entity.setCfgId(Long.parseLong(id));
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			entity.setTableName(CfgIndexInfo.getTablename());
			entity.setFunctionId(functionId);
			fileTransferCfgDao.updateCfgValid(entity);
			//查询子配置
			entity = this.getFtpCfg(Long.parseLong(id),entity.getCompileId());
			if(entity.getIpPortList()!=null && entity.getIpPortList().size()>0){
				IpPortCfg cfg = new IpPortCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(IpPortCfg.getTablename());
				fileTransferCfgDao.updateCfgValid(cfg);
			}
			if(entity.getStringList()!=null && entity.getStringList().size()>0)
			{
				FtpKeywordCfg cfg = new FtpKeywordCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(FtpKeywordCfg.getTablename());
				fileTransferCfgDao.updateCfgValid(cfg);
			}
			if(entity.getNtcSubscribeIdCfgList()!=null && entity.getNtcSubscribeIdCfgList().size()>0)
			{
				NtcSubscribeIdCfg ntc = new NtcSubscribeIdCfg();
				BeanUtils.copyProperties(entity, ntc, new String[]{"cfgId"});
				ntc.setTableName(ntc.getTablename());
				stringCfgDao.updateSubscribeIdCfg(ntc);
			}
			
			//保存区域IP信息
			if(entity.getAreaCfg()!=null && entity.getAreaCfg().size()>0){
				AreaIpCfg cfg = new AreaIpCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(AreaIpCfg.getTablename());
				fileTransferCfgDao.updateCfgValid(cfg);
			}
			
		}
		
	}
	public void auditFtpCfg(CfgIndexInfo entity,Integer isAudit) throws MaatConvertException{
		//修改数据库审核状态信息
		entity.setTableName(CfgIndexInfo.getTablename());
		fileTransferCfgDao.auditCfg(entity);
		
		ToMaatBean maatBean = new ToMaatBean();		
		MaatCfg maatCfg = new MaatCfg();
		List<MaatCfg> configCompileList = new ArrayList();
		List<GroupCfg> groupRelationList = new ArrayList();
		List<IpCfg> ipRegionList = new ArrayList();
		List<StringCfg> strRegionList = new ArrayList();
		List<NumBoundaryCfg> numRegionList = new ArrayList();
		List<DigestCfg> digestRegionList = new ArrayList();
		List<IpCfg> areaIpRegionList = new ArrayList();
		
		//查询子配置并修改审核状态
		entity = this.getFtpCfg(entity.getCfgId(),entity.getCompileId());
		if(entity.getIpPortList()!=null && entity.getIpPortList().size()>0){
			IpPortCfg cfg = new IpPortCfg();
			BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
			cfg.setTableName(IpPortCfg.getTablename());
			fileTransferCfgDao.auditCfg(cfg);
			if(isAudit==1){
				Map<String,List> map = cfgConvert(ipRegionList,entity.getIpPortList(),1,entity,groupRelationList);
				groupRelationList=map.get("groupList");
				ipRegionList=map.get("dstList");
				if(map.get("numRegionList")!=null){
					numRegionList.addAll(map.get("numRegionList"));
				}
			}
		}
		if(entity.getStringList()!=null && entity.getStringList().size()>0){
			FtpKeywordCfg cfg = new FtpKeywordCfg();
			BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
			cfg.setTableName(FtpKeywordCfg.getTablename());
			fileTransferCfgDao.auditCfg(cfg);
			if(isAudit==1){
				Map<String,List> map = cfgConvert(strRegionList,entity.getStringList(),2,entity,groupRelationList);
				groupRelationList=map.get("groupList");
				strRegionList=map.get("dstList");
			}
		}
		if(entity.getNtcSubscribeIdCfgList()!=null && entity.getNtcSubscribeIdCfgList().size()>0)
		{
			NtcSubscribeIdCfg ntc = new NtcSubscribeIdCfg();
			BeanUtils.copyProperties(entity, ntc, new String[]{"cfgId"});
			ntc.setTableName(ntc.getTablename());
			stringCfgDao.updateSubscribeIdCfg(ntc);
			if(isAudit==1){
					Map<String,List> ntcMap = cfgConvert(strRegionList,entity.getNtcSubscribeIdCfgList(),2,entity,groupRelationList);
					groupRelationList=ntcMap.get("groupList");
					strRegionList=ntcMap.get("dstList");
			}
		}
		if(entity.getDigestList()!=null && entity.getDigestList().size()>0){
			FileDigestCfg cfg = new FileDigestCfg();
			BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
			cfg.setTableName(FileDigestCfg.getTablename());
			fileTransferCfgDao.auditCfg(cfg);
			if(isAudit==1){
				Map<String,List> map = cfgConvert(digestRegionList,entity.getDigestList(),5,entity,groupRelationList);
				groupRelationList=map.get("groupList");
				digestRegionList=map.get("dstList");
			}
		}
		//保存区域IP信息
		List<AreaIpCfg> areaIpCfgList=areaIpCfgDao.getByCompileId(entity.getCompileId());
		if(!StringUtil.isEmpty(areaIpCfgList)){
			AreaIpCfg cfg = new AreaIpCfg();
			BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
			cfg.setTableName(AreaIpCfg.getTablename());
			fileTransferCfgDao.auditCfg(cfg);
			if(isAudit==1){
				Map<String,List> map = cfgConvert(areaIpRegionList,areaIpCfgList,1,entity,groupRelationList);
				groupRelationList=map.get("groupList");
				areaIpRegionList=map.get("dstList");
			}
		}
		
		//构造提交综合服务参数格式，一条配置提交一次综合服务
		if(isAudit==1){
			maatCfg.initDefaultValue();
			BeanUtils.copyProperties(entity, maatCfg);
			maatCfg.setAction(entity.getAction());
			maatCfg.setAuditTime(entity.getAuditTime());
			maatCfg.setIpRegionList(ipRegionList);
			maatCfg.setStrRegionList(strRegionList);
			maatCfg.setNumRegionList(numRegionList);
			maatCfg.setDigestRegionList(digestRegionList);
			maatCfg.setGroupRelationList(groupRelationList);
			maatCfg.setGroupNum(groupRelationList.size());
			maatCfg.setAreaIpRegionList(areaIpRegionList);
			maatCfg.setIsValid(entity.getIsValid());
			
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(entity.getAuditTime());
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.INSERT_ACTION);
			//调用服务接口下发配置数据
			String json=gsonToJson(maatBean);
			logger.info("ftp配置下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
			logger.info("ftp配置下发响应信息："+result.getMsg());

		}else if(isAudit==3){
			maatCfg.setCompileId(entity.getCompileId());
			maatCfg.setServiceId(entity.getServiceId());
			maatCfg.setIsValid(0);//无效
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(entity.getAuditTime());
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.UPDATE_ACTION);
			//调用服务接口取消配置
			String json=gsonToJson(maatBean);
			logger.info("ftp配置下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json,1);
			logger.info("ftp配置取消配置响应信息："+result.getMsg());
		}
	}
	
	public Page<FileDigestCfg> getFileDigestList(Page<FileDigestCfg> page, FileDigestCfg entity){
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"a"));
		entity.setPage(page);
		List<FileDigestCfg> list = fileTransferCfgDao.getFileDigestList(entity);
		page.setList(list);
		return page;
	}
	
	public FileDigestCfg getFileDigestCfg(Long cfgId,Integer compileId){
		FileDigestCfg entity = fileTransferCfgDao.getFileDigest(cfgId,compileId);
		List<NtcSubscribeIdCfg> subscribeidList = fileTransferCfgDao.getFileDigestSubscribeidList(entity);
		entity.setNtcSubscribeIdCfgList(subscribeidList);
		return entity;
	}
	
	public void saveOrUpdateFileDigestCfg(FileDigestCfg entity,ToMaatResult result,String areaCfgIds){
		//设置区域运营商信息 
		setAreaEffectiveIds(entity);
		
		entity.setIsValid(0);
		entity.setIsAudit(0);
		
		if(entity.getCfgId()==null){
			Integer compileId = 0;
			try {
				List<Integer> idList = ConfigServiceUtil.getId(1, 1);
				if(idList!=null && idList.size()>0){
					compileId = idList.get(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("获取编译ID出错");
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+e.getMessage());
			}
			entity.setCompileId(compileId);
			entity.setCreatorId(entity.getCurrentUser().getId());
			entity.setRawLen(result.getData().getRawLen());
			entity.setDigest(result.getData().getDigest());
			entity.setFileUrl(result.getData().getAccessUrl());
			if(!StringUtil.isEmpty(entity.getCfdsLevel()) && entity.getCfdsLevel() > 0) {
				int cfdsLevel=entity.getCfdsLevel();
				cfdsLevel= cfdsLevel > 10 ? (cfdsLevel/10):cfdsLevel;
				entity.setCfdsLevel(cfdsLevel);
			}
			mailCfgDao.saveMailFileDigestCfg(entity);
			
			// 保存SubscribeId域配置信息
			if(entity.getNtcSubscribeIdCfgList()!=null){
				for(NtcSubscribeIdCfg cfg:entity.getNtcSubscribeIdCfgList()){
					if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
						BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
						fileTransferCfgDao.saveP2pSubscribeIdCfg(cfg);
					}
				}
			}
			//保存区域IP信息
			if(entity.getAreaCfg()!=null&&entity.getAreaCfg().size()>0){
				for (AreaIpCfg areaIpCfg : entity.getAreaCfg()) {
					BeanUtils.copyProperties(entity, areaIpCfg,new String[]{"cfgRegionCode","cfgType"});
					areaIpCfgDao.saveAreaIpCfg(areaIpCfg);
				}
			}
			
		}else{
			
			if(!StringUtil.isEmpty(entity.getCfdsLevel()) && entity.getCfdsLevel() > 0) {
				int cfdsLevel=entity.getCfdsLevel();
				cfdsLevel= cfdsLevel > 10 ? (cfdsLevel/10):cfdsLevel;
				entity.setCfdsLevel(cfdsLevel);
			}	
			entity.setEditorId(entity.getCurrentUser().getId());
			entity.setEditTime(new Date());
			if(result.getData() !=null ) {
				entity.setRawLen(result.getData().getRawLen());
				entity.setDigest(result.getData().getDigest());
				entity.setFileUrl(result.getData().getAccessUrl());
			}
			
			fileTransferCfgDao.updateFileDigestCfg(entity);
			entity.setCreatorId(entity.getCurrentUser().getId());
			entity.setCreateTime(new Date());
			
			// SubscribeId域配置 先删后加
			fileTransferCfgDao.deleteFileDigestSubscribeIdCfg(entity);
			if(entity.getNtcSubscribeIdCfgList()!=null){
				for(NtcSubscribeIdCfg cfg:entity.getNtcSubscribeIdCfgList()){
					if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
						BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
						fileTransferCfgDao.saveP2pSubscribeIdCfg(cfg);
					}
				}
			}
			
			areaCfgIds=!StringUtil.isEmpty(areaCfgIds)? ","+areaCfgIds:"";
			if(!StringUtil.isEmpty(entity.getAreaCfg())){
				for (AreaIpCfg areaIpCfg : entity.getAreaCfg()) {
					if(!StringUtil.isEmpty(areaIpCfg.getCfgId())){
						if(areaCfgIds.contains(","+areaIpCfg.getCfgId()+",")){
							areaCfgIds=areaCfgIds.replace(areaIpCfg.getCfgId()+",", "");
						}
						//修改
						entity.setEditorId(UserUtils.getUser().getId());
						entity.setEditTime(entity.getEditTime());
						BeanUtils.copyProperties(entity, areaIpCfg,new String[]{"cfgRegionCode","cfgType","cfgId"});
						areaIpCfgDao.updateAreaIpCfg(areaIpCfg);
					}else{
						//新增
						entity.setCreatorId(UserUtils.getUser().getId());
						entity.setCreateTime(entity.getEditTime());
						BeanUtils.copyProperties(entity, areaIpCfg,new String[]{"cfgRegionCode"
																				,"cfgType"
																				});
						areaIpCfgDao.saveAreaIpCfg(areaIpCfg);
					}
				}
			}
			//delete 真是删除areaIpCfg信息
			if(!StringUtil.isEmpty(areaCfgIds.replaceAll(",", ""))){
				areaCfgIds=areaCfgIds.substring(1,areaCfgIds.length());
				for (String cfgId : areaCfgIds.split(",")) {
					CfgIndexInfo cfg=new CfgIndexInfo();
					AreaIpCfg areaIpCfg=new AreaIpCfg();
					areaIpCfg.setCfgId(Long.parseLong(cfgId));
					areaIpCfgDao.deleteAreaIpCfgByCfgId(areaIpCfg);
				}
			}
			
		}
	}
	
	public void updateFileDigestCfgValid(Integer isValid,String ids,Integer functionId){
		String[] idArray = ids.split(",");
		for(String id :idArray){
			FileDigestCfg entity = new FileDigestCfg();
			entity.setCfgId(Long.parseLong(id));
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			entity.setFunctionId(functionId);
			fileTransferCfgDao.updateFileDigestCfg(entity);
			
			entity = this.getFileDigestCfg(Long.parseLong(id),entity.getCompileId());
			// 修改子域配置状态
			if((entity.getNtcSubscribeIdCfgList() != null) && (entity.getNtcSubscribeIdCfgList().size() > 0)) {
				NtcSubscribeIdCfg cfg = new NtcSubscribeIdCfg();
				BeanUtils.copyProperties(entity, cfg, new String[] {"cfgId"});
				stringCfgDao.updateSubscribeIdCfg(cfg);
			}
			AreaIpCfg areaIpCfg=new AreaIpCfg();
			BeanUtils.copyProperties(entity, areaIpCfg,new String[]{"cfgRegionCode"
																	,"cfgType"
																	,"cfgId"
																	});
			areaIpCfgDao.updateAreaIpCfgValid(areaIpCfg);
		}
		
	}
	
	public void auditFileDigestCfg(Integer isAudit,Integer isValid,Integer functionId,String id,Date auditTime) throws MaatConvertException{
		FileDigestCfg entity = new FileDigestCfg();
		entity.setCfgId(Long.parseLong(id));
		entity.setIsAudit(isAudit);
		entity.setIsValid(isValid);
		entity.setAuditorId(UserUtils.getUser().getId());
		entity.setAuditTime(auditTime);
		fileTransferCfgDao.updateFileDigestCfg(entity);
		
		entity = this.getFileDigestCfg(Long.parseLong(id),entity.getCompileId());
		// 修改域配置审核状态
		if((entity.getNtcSubscribeIdCfgList() != null) && (entity.getNtcSubscribeIdCfgList().size() > 0)) {
			NtcSubscribeIdCfg cfg = new NtcSubscribeIdCfg();
			BeanUtils.copyProperties(entity, cfg, new String[] {"cfgId","cfgType"});
			stringCfgDao.updateSubscribeIdCfg(cfg);
		}
		
		List list=new ArrayList();
		list.add(entity);
		AreaIpCfg areaIpCfg=new AreaIpCfg();
		BeanUtils.copyProperties(entity, areaIpCfg,new String[]{"cfgRegionCode"
																,"cfgType"
																,"cfgId"
																});
		areaIpCfgDao.updateAreaIpCfgValid(areaIpCfg);
		List<AreaIpCfg> areaIpCfgList=areaIpCfgDao.getByCompileId(entity.getCompileId());
		
		ToMaatBean maatBean = new ToMaatBean();		
		MaatCfg maatCfg = new MaatCfg();
		List<MaatCfg> configCompileList = new ArrayList();
		List<GroupCfg> groupRelationList = new ArrayList();
		List<IpCfg> ipRegionList = new ArrayList();
		List<StringCfg> strRegionList = new ArrayList();
		List<NumBoundaryCfg> numRegionList = new ArrayList();
		List<DigestCfg> digestRegionList = new ArrayList();
		List<IpCfg> areaIpRegionList = new ArrayList();
		
		if(isAudit==1){
				Map<String,List> map = cfgConvert(digestRegionList,list,5,entity,groupRelationList);
				groupRelationList=map.get("groupList");
				digestRegionList=map.get("dstList");
				if(map.get("numRegionList")!=null){
					numRegionList.addAll(map.get("numRegionList"));
				}
				// 审核通过：添加各子域配置
				if((entity.getNtcSubscribeIdCfgList() != null) && (entity.getNtcSubscribeIdCfgList().size() > 0)) {
					Map<String,List> subIdMap = cfgConvert(strRegionList,entity.getNtcSubscribeIdCfgList(),2,entity,groupRelationList);
					groupRelationList=subIdMap.get("groupList");
					strRegionList=subIdMap.get("dstList");
				}
				
				if(areaIpCfgList != null && areaIpCfgList.size() >0){
					Map<String,List> areaMap = cfgConvert(areaIpRegionList,areaIpCfgList,1,entity,groupRelationList);
					groupRelationList=areaMap.get("groupList");
					areaIpRegionList=areaMap.get("dstList");
				}
		}
		
		//构造提交综合服务参数格式，一条配置提交一次综合服务
		if(isAudit==1){
			maatCfg.initDefaultValue();
			BeanUtils.copyProperties(entity, maatCfg);
			maatCfg.setAction(entity.getAction());
			maatCfg.setAuditTime(auditTime);
			maatCfg.setIpRegionList(ipRegionList);
			maatCfg.setStrRegionList(strRegionList);
			maatCfg.setNumRegionList(numRegionList);
			maatCfg.setDigestRegionList(digestRegionList);
			maatCfg.setGroupRelationList(groupRelationList);
			maatCfg.setGroupNum(groupRelationList.size());
			maatCfg.setAreaIpRegionList(areaIpRegionList);
			maatCfg.setIsValid(entity.getIsValid());
			
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(auditTime);
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.INSERT_ACTION);
			//调用服务接口下发配置数据
			String json=gsonToJson(maatBean);
			logger.info("File Digest配置下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
			logger.info("File Digest配置下发响应信息："+result.getMsg());
			
		}else if(isAudit==3){
			maatCfg.setCompileId(entity.getCompileId());
			maatCfg.setServiceId(entity.getServiceId());
			maatCfg.setIsValid(0);//无效
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(auditTime);
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.UPDATE_ACTION);
			//调用服务接口取消配置
			String json=gsonToJson(maatBean);
			logger.info("File Digest配置下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json,1);
			logger.info("File Digest配置取消配置响应信息："+result.getMsg());
		}
	}
	
	public FileDigestCfg getFileDigestSubIdCfg(Long cfgId,Integer compileId) {
		FileDigestCfg entity = fileTransferCfgDao.getFileDigest(cfgId,compileId);
		if(entity != null && entity.getCompileId()!=null){
			NtcSubscribeIdCfg ntcSubscribeIdCfg = new NtcSubscribeIdCfg();
			ntcSubscribeIdCfg.setCompileId(entity.getCompileId());
			List<NtcSubscribeIdCfg> ntcList = stringCfgDao.findSubscribeIdCfgList(ntcSubscribeIdCfg);
			entity.setNtcSubscribeIdCfgList(ntcList);
		}
		return entity;
	}
	
	public Page<CfgIndexInfo> getP2pList(Page<CfgIndexInfo> page, CfgIndexInfo entity){
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"a"));
		entity.setPage(page);
		List<CfgIndexInfo> list = fileTransferCfgDao.getP2pList(entity);
		page.setList(list);
		return page;
	}
	
	public CfgIndexInfo getP2pCfg(Long cfgId,Integer compileId){
		// 查询各域配置信息
		CfgIndexInfo entity = fileTransferCfgDao.getCfgIndexInfo(cfgId,compileId);
		List<IpPortCfg> ipPortList = fileTransferCfgDao.getIpPortList(entity);
		List<P2pHashCfg> hashList = fileTransferCfgDao.getP2pHashList(entity);
		List<P2pKeywordCfg> keywordList = fileTransferCfgDao.getP2pKeywordList(entity);
		List<NtcSubscribeIdCfg> subscribeIdList = fileTransferCfgDao.getP2pSubscribeidList(entity);
		entity.setIpPortList(ipPortList);
		entity.setP2pHashList(hashList);
		entity.setP2pKeywordList(keywordList);
		entity.setNtcSubscribeIdCfgList(subscribeIdList);
		return entity;
	}
	
	public Map<String, List>  exportp2p(CfgIndexInfo entity){
		Map<String, List> dataMap=new HashMap<String, List>();
		List<IpPortCfg> ipPortList = fileTransferCfgDao.getIpPortList(entity);
		for (int i = 0; i < ipPortList.size(); i++) {
			ipPortList.get(i).setUserRegion1(DictUtils.getDictLabel("P2P_IP_TYPE",entity.getUserRegion2()));
		}
		List<P2pHashCfg> hashList = fileTransferCfgDao.getP2pHashList(entity);
		for (int i = 0; i < hashList.size(); i++) {
			BaseStringCfg base =hashList.get(i);
			base.setUserRegion1(DictUtils.getDictLabel("P2P_HASH_TYPE",entity.getUserRegion1()));
		}
		List<P2pKeywordCfg> keywordList = fileTransferCfgDao.getP2pKeywordList(entity);
		List<NtcSubscribeIdCfg> subscribeIdList = fileTransferCfgDao.getP2pSubscribeidList(entity);
		dataMap.put("NTC_SUBSCRIBE_ID", subscribeIdList);
		dataMap.put("NTC_P2P_KEYWORDS", keywordList);
		dataMap.put("NTC_P2P_HASH_BIN", hashList);
		dataMap.put("NTC_UNIVERSAL_IP", ipPortList);
		return dataMap;
	}
	/*public void saveP2pHashCfg(List<BaseStringCfg<?>> cfgs) {
		for(BaseStringCfg cfg:cfgs){
			if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
				cfg.setUserRegion1(cfg.getUserRegion2());
				cfg.setUserRegion2(null);
				fileTransferCfgDao.saveP2pHashCfg(cfg);
			}
		}
	}*/
	public void saveP2pCfg(List<BaseStringCfg<?>> cfgs) {
		for(BaseStringCfg cfg:cfgs){
			if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
				fileTransferCfgDao.saveP2pHashCfg(cfg);
			}
		}
	}
	/**
	 * 保存p2p配置
	 * @param entity
	 */
	public void saveP2pCfg(CfgIndexInfo entity){
		// 设置区域运营商信息
		setAreaEffectiveIds(entity);
		if(entity.getCfgId()==null){
				Integer compileId = 0;
				try {
					List<Integer> idList = ConfigServiceUtil.getId(1, 1);
					if(idList!=null && idList.size()>0){
						compileId = idList.get(0);
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.info("获取编译ID出错");
					throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+e.getMessage());
				}
				entity.setCompileId(compileId);
				entity.setCreateTime(new Date());
				entity.setCreatorId(entity.getCurrentUser().getId());
				fileTransferCfgDao.saveCfgIndex(entity);
				// 保存IP域配置信息
				if(entity.getIpPortList()!=null){
					for(IpPortCfg cfg:entity.getIpPortList()){
						BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType","userRegion1","userRegion2"});
						fileTransferCfgDao.saveIpPortCfg(cfg);
					}
				}
				// 保存Hash类型域配置信息
				if(entity.getP2pHashList()!=null){
					for(BaseStringCfg cfg:entity.getP2pHashList()){
						if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
							BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
							fileTransferCfgDao.saveP2pHashCfg(cfg);
						}
					}
				}
				// 保存关键字域配置信息
				if(entity.getP2pKeywordList()!=null){
					for(P2pKeywordCfg cfg:entity.getP2pKeywordList()){
						if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
							BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
							fileTransferCfgDao.saveP2pKeywordCfg(cfg);
						}
					}
				}
				// 保存SubscribeId域配置信息
				if(entity.getNtcSubscribeIdCfgList()!=null){
					for(NtcSubscribeIdCfg cfg:entity.getNtcSubscribeIdCfgList()){
						if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
							BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
							fileTransferCfgDao.saveP2pSubscribeIdCfg(cfg);
						}
					}
				}
				// 保存区域IP信息
				if(entity.getAreaCfg()!=null){
					for(AreaIpCfg cfg:entity.getAreaCfg()){
						cfg.initDefaultValue();
						BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
						areaIpCfgDao.saveAreaIpCfg(cfg);
					}
				}
			
			
		}else{
			// 审核未通过状态的配置 修改后状态改为未审核
			entity.setIsValid(0);
			entity.setIsAudit(0);
			entity.setEditTime(new Date());
			entity.setEditorId(entity.getCurrentUser().getId());
			fileTransferCfgDao.updateCfgIndex(entity);
			// 删除无效子配置后，再新增子配置
			fileTransferCfgDao.deleteP2pIpCfg(entity);
			fileTransferCfgDao.deleteP2pHashCfg(entity);
			fileTransferCfgDao.deleteP2pKeywordCfg(entity);
			fileTransferCfgDao.deleteP2pSubscribeIdCfg(entity);
			AreaIpCfg area = new AreaIpCfg();
			area.setCompileId(entity.getCompileId());
			area.setFunctionId(entity.getFunctionId());
			areaIpCfgDao.deleteAreaIpCfg(area);
			entity.setCreateTime(new Date());
			entity.setCreatorId(entity.getCurrentUser().getId());
			
			if(entity.getIpPortList()!=null){
				for(IpPortCfg cfg:entity.getIpPortList()){
					BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
					cfg.setUserRegion2(null);
					fileTransferCfgDao.saveIpPortCfg(cfg);
				}
			}
			if(entity.getP2pHashList()!=null){
				for(BaseStringCfg cfg:entity.getP2pHashList()){
					if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
						BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
						cfg.setUserRegion1(cfg.getUserRegion2());
						cfg.setUserRegion2(null);
						fileTransferCfgDao.saveP2pHashCfg(cfg);
					}
				}
			}
			if(entity.getP2pKeywordList()!=null){
				for(P2pKeywordCfg cfg:entity.getP2pKeywordList()){
					if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
						BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
						fileTransferCfgDao.saveP2pKeywordCfg(cfg);
					}
				}
			}
			if(entity.getNtcSubscribeIdCfgList()!=null){
				for(NtcSubscribeIdCfg cfg:entity.getNtcSubscribeIdCfgList()){
					if(StringUtils.isNotBlank(cfg.getCfgKeywords())){
						BeanUtils.copyProperties(entity, cfg,new String[]{"cfgRegionCode","cfgType"});
						fileTransferCfgDao.saveP2pSubscribeIdCfg(cfg);
					}
				}
			}
			//保存区域IP信息
			if(entity.getAreaCfg()!=null){
				for(AreaIpCfg cfg:entity.getAreaCfg()){
					cfg.initDefaultValue();
					BeanUtils.copyProperties(entity, cfg,new String[]{"cfgDesc","cfgRegionCode","cfgType"});
					areaIpCfgDao.saveAreaIpCfg(cfg);
				}
			}
		}
	}
	
	public void updateP2pCfgValid(Integer isValid, String ids, Integer functionId) {
		String[] idArray = ids.split(",");
		for(String id :idArray){
			CfgIndexInfo entity = new CfgIndexInfo();
			entity.setCfgId(Long.parseLong(id));
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			entity.setTableName(CfgIndexInfo.getTablename());
			entity.setFunctionId(functionId);
			fileTransferCfgDao.updateCfgValid(entity);
			//查询子配置
			entity = this.getP2pCfg(Long.parseLong(id),entity.getCompileId());
			if(entity.getIpPortList()!=null && entity.getIpPortList().size()>0){
				IpPortCfg cfg = new IpPortCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(IpPortCfg.getTablename());
				cfg.setUserRegion2(null);
				fileTransferCfgDao.updateCfgValid(cfg);
			}
			if(entity.getP2pHashList()!=null && entity.getP2pHashList().size()>0){
				P2pHashCfg cfg = new P2pHashCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(P2pHashCfg.getTablename());
				cfg.setUserRegion1(cfg.getUserRegion2());
				cfg.setUserRegion2(null);
				fileTransferCfgDao.updateCfgValid(cfg);
			}
			if(entity.getP2pKeywordList()!=null && entity.getP2pKeywordList().size()>0){
				P2pKeywordCfg cfg = new P2pKeywordCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(P2pKeywordCfg.getTablename());
				fileTransferCfgDao.updateCfgValid(cfg);
			}
			if(entity.getNtcSubscribeIdCfgList()!=null && entity.getNtcSubscribeIdCfgList().size()>0){
				NtcSubscribeIdCfg cfg = new NtcSubscribeIdCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(NtcSubscribeIdCfg.getTablename());
				fileTransferCfgDao.updateCfgValid(cfg);
			}
			//保存区域IP信息
			if(entity.getAreaCfg()!=null && entity.getAreaCfg().size()>0){
				AreaIpCfg cfg = new AreaIpCfg();
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setTableName(AreaIpCfg.getTablename());
				fileTransferCfgDao.updateCfgValid(cfg);
			}
		}
		
	}
	
	public void auditP2pCfg(CfgIndexInfo entity,Integer isAudit) throws MaatConvertException{
		//修改数据库审核状态信息
		entity.setTableName(CfgIndexInfo.getTablename());
		fileTransferCfgDao.auditCfg(entity);
		
		ToMaatBean maatBean = new ToMaatBean();		
		MaatCfg maatCfg = new MaatCfg();
		List<MaatCfg> configCompileList = new ArrayList();
		List<GroupCfg> groupRelationList = new ArrayList();
		List<IpCfg> ipRegionList = new ArrayList();
		List<StringCfg> strRegionList = new ArrayList();
		List<NumBoundaryCfg> numRegionList = new ArrayList();
		List<DigestCfg> digestRegionList = new ArrayList();
		List<IpCfg> areaIpRegionList = new ArrayList();
		
		//查询子配置并修改审核状态
		entity = this.getP2pCfg(entity.getCfgId(),entity.getCompileId());
		if(entity.getIpPortList()!=null && entity.getIpPortList().size()>0){
			IpPortCfg cfg = new IpPortCfg();
			BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
			cfg.setTableName(IpPortCfg.getTablename());
			fileTransferCfgDao.auditCfg(cfg);
			if(isAudit==1){
				Map<String,List> map = cfgConvert(ipRegionList,entity.getIpPortList(),1,entity,groupRelationList);
				groupRelationList=map.get("groupList");
				ipRegionList=map.get("dstList");
				if(map.get("numRegionList")!=null){
					numRegionList.addAll(map.get("numRegionList"));
				}
			}
		}
		if(entity.getP2pHashList()!=null && entity.getP2pHashList().size()>0){
			P2pHashCfg cfg = new P2pHashCfg();
			BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
			cfg.setTableName(P2pHashCfg.getTablename());
			fileTransferCfgDao.auditCfg(cfg);
			if(isAudit==1){
				Map<String,List> map = cfgConvert(strRegionList,entity.getP2pHashList(),2,entity,groupRelationList);
				groupRelationList=map.get("groupList");
				strRegionList=map.get("dstList");
			}
		}
		if(entity.getP2pKeywordList()!=null && entity.getP2pKeywordList().size()>0){
			P2pKeywordCfg cfg = new P2pKeywordCfg();
			BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
			cfg.setTableName(P2pKeywordCfg.getTablename());
			fileTransferCfgDao.auditCfg(cfg);
			if(isAudit==1){
				Map<String,List> map = cfgConvert(strRegionList,entity.getP2pKeywordList(),2,entity,groupRelationList);
				groupRelationList=map.get("groupList");
				strRegionList=map.get("dstList");
			}
		}
		if(entity.getNtcSubscribeIdCfgList()!=null && entity.getNtcSubscribeIdCfgList().size()>0){
			NtcSubscribeIdCfg cfg = new NtcSubscribeIdCfg();
			BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
			cfg.setTableName(NtcSubscribeIdCfg.getTablename());
			fileTransferCfgDao.auditCfg(cfg);
			if(isAudit==1){
				Map<String,List> map = cfgConvert(strRegionList,entity.getNtcSubscribeIdCfgList(),2,entity,groupRelationList);
				groupRelationList=map.get("groupList");
				strRegionList=map.get("dstList");
			}
		}
		
		
		//保存区域IP信息
		List<AreaIpCfg> areaIpCfgList=areaIpCfgDao.getByCompileId(entity.getCompileId());
		if(!StringUtil.isEmpty(areaIpCfgList)){
			AreaIpCfg cfg = new AreaIpCfg();
			BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
			cfg.setTableName(AreaIpCfg.getTablename());
			fileTransferCfgDao.auditCfg(cfg);
			if(isAudit==1){
				Map<String,List> map = cfgConvert(areaIpRegionList,areaIpCfgList,1,entity,groupRelationList);
				groupRelationList=map.get("groupList");
				areaIpRegionList=map.get("dstList");
			}
		}
		
		//构造提交综合服务参数格式，一条配置提交一次综合服务
		if(isAudit==1){
			maatCfg.initDefaultValue();
			BeanUtils.copyProperties(entity, maatCfg);
			maatCfg.setAction(entity.getAction());
			maatCfg.setAuditTime(entity.getAuditTime());
			maatCfg.setIpRegionList(ipRegionList);
			maatCfg.setStrRegionList(strRegionList);
			maatCfg.setNumRegionList(numRegionList);
			maatCfg.setDigestRegionList(digestRegionList);
			maatCfg.setGroupRelationList(groupRelationList);
			maatCfg.setGroupNum(groupRelationList.size());
			maatCfg.setAreaIpRegionList(areaIpRegionList);
			maatCfg.setIsValid(entity.getIsValid());
			
			
			String userRegion = "";
			//存在IP域
			if(entity != null && entity.getIpPortList() != null && entity.getIpPortList().size() >0){
				userRegion += Constants.P2P_IP_TYPE_USER_REGION_KEY+"="+entity.getUserRegion2();
			}
			//存在hash域
			if(entity != null && entity.getP2pHashList() != null && entity.getP2pHashList().size() >0){
				if(!StringUtil.isEmpty(userRegion) && !StringUtil.isEmpty(entity.getUserRegion1())){
					userRegion = userRegion+Constants.USER_REGION_SPLIT+Constants.P2P_HASH_TYPE_USER_REGION_KEY+"="+entity.getUserRegion1();
				}else if(!StringUtil.isEmpty(entity.getUserRegion1())){
					userRegion += Constants.P2P_HASH_TYPE_USER_REGION_KEY+"="+entity.getUserRegion1();
				}
			}
			//存在IP域
			/*if(entity != null && entity.getIpPortList() != null && entity.getIpPortList().size() >0){
				IpPortCfg ipport=entity.getIpPortList().get(0);
				ipport.setUserRegion1(StringUtil.isEmpty(ipport.getUserRegion1()) ? "":ipport.getUserRegion1());
				userRegion += Constants.P2P_IP_TYPE_USER_REGION_KEY+"="+ipport.getUserRegion1();
			}
			//存在hash域
			if(entity != null && entity.getP2pHashList() != null && entity.getP2pHashList().size() >0){
				BaseStringCfg hashCfg=entity.getP2pHashList().get(0);
				hashCfg.setUserRegion1(StringUtil.isEmpty(hashCfg.getUserRegion1()) ? "":hashCfg.getUserRegion1());
				if(!StringUtil.isEmpty(userRegion)){
					userRegion =userRegion+Constants.USER_REGION_SPLIT+Constants.P2P_HASH_TYPE_USER_REGION_KEY+"="+hashCfg.getUserRegion1();
				}else{
					userRegion += Constants.P2P_HASH_TYPE_USER_REGION_KEY+"="+hashCfg.getUserRegion1();
				}
			}*/
			if(!StringUtil.isEmpty(userRegion)){
				maatCfg.setUserRegion(userRegion);
			}
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(entity.getAuditTime());
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.INSERT_ACTION);
			
			//调用服务接口下发配置数据
			String json=gsonToJson(maatBean);
			logger.info("p2p配置下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
			logger.info("p2p配置下发响应信息："+result.getMsg());

		}else if(isAudit==3){
			maatCfg.setCompileId(entity.getCompileId());
			maatCfg.setServiceId(entity.getServiceId());
			maatCfg.setIsValid(0);//无效
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(entity.getAuditTime());
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.UPDATE_ACTION);
			//调用服务接口取消配置
			String json=gsonToJson(maatBean);
			logger.info("p2p配置下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json,1);
			logger.info("p2p配置取消配置响应信息："+result.getMsg());
		}
	}
	
	
}
