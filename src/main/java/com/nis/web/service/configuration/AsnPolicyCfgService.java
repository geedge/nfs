package com.nis.web.service.configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nis.domain.Page;
import com.nis.domain.basics.AsnIpCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.maat.MaatCfg;
import com.nis.domain.maat.MaatCfg.DigestCfg;
import com.nis.domain.maat.MaatCfg.GroupCfg;
import com.nis.domain.maat.MaatCfg.IpCfg;
import com.nis.domain.maat.MaatCfg.NumBoundaryCfg;
import com.nis.domain.maat.MaatCfg.StringCfg;
import com.nis.domain.maat.ToMaatBean;
import com.nis.domain.maat.ToMaatResult;
import com.nis.domain.specific.ConfigGroupInfo;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.Constants;
import com.nis.web.dao.basics.AsnIpCfgDao;
import com.nis.web.dao.configuration.AsnPolicyCfgDao;
import com.nis.web.dao.specific.ConfigGroupInfoDao;
import com.nis.web.dao.specific.SpecificServiceCfgDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.BaseService;
@Service
public class AsnPolicyCfgService extends BaseService {
	@Autowired
	private AsnPolicyCfgDao asnPolicyCfgDao;
	@Autowired
	private AsnIpCfgDao asnIpCfgDao;
	@Autowired
	//private SpecificServiceCfgDao specificServiceCfgDao;
	private ConfigGroupInfoDao configGroupInfoDao;
	/**
	 * @param page
	 * @param entity
	 * @return
	 */
	public Page<CfgIndexInfo> findPage(Page<CfgIndexInfo> page, CfgIndexInfo entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"r"));
		entity.setPage(page);
		List<CfgIndexInfo> list=asnPolicyCfgDao.findPage(entity);
		page.setList(list);
		return page;
	}
	public CfgIndexInfo get(Long id ) {
		return asnPolicyCfgDao.get(id);
	}
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveOrUpdate(CfgIndexInfo entity){
		Date time=new Date();
		if(entity.getCfgId()==null) {//新增，获取compileId
			entity.setCreateTime(time);
			entity.setCreatorId(entity.getCurrentUser().getId());
			//调用服务接口获取compileId
			List<Integer> compileIds = new ArrayList<Integer>();
			try {
				compileIds = ConfigServiceUtil.getId(1,1);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("获取编译ID出错");
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+e.getMessage());
			}
			if(compileIds != null && compileIds.size() >0  && compileIds.get(0) != 0){
				entity.setCompileId(compileIds.get(0));
				asnPolicyCfgDao.save(entity);
			}else{
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>");
			}
		}else {
			entity.setEditTime(time);
			entity.setEditorId(entity.getCurrentUser().getId());
			asnPolicyCfgDao.updateCfgIndexInfo(entity);
		}
		
	}
	
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void delete(Integer functionId,Integer isValid,String ids) {
		asnPolicyCfgDao.updateValid(isValid, ids);
	}
	public List<ConfigGroupInfo> getConfigGroupInfoList(Integer groupType) {
		// TODO Auto-generated method stub
		return asnPolicyCfgDao.getConfigGroupInfoList(groupType);
	}
	/**
	 * 
	 * @param isAudit
	 * @param isValid
	 * @param ids cfgId
	 * @param functionId
	 */
//	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
//	@Deprecated
//	public void audit(Integer isAudit,Integer isValid,Integer functionId,String id,Date auditTime){
//		CfgIndexInfo entity=asnPolicyCfgDao.get(Long.valueOf(id));
//		entity.setIsAudit(isAudit);
//		entity.setIsValid(isValid);
//		entity.setAuditorId(UserUtils.getUser().getId());
//		entity.setAuditTime(auditTime);
//		asnPolicyCfgDao.updateCfgIndexInfo(entity);
//		ToMaatBean maatBean = new ToMaatBean();		
//		MaatCfg maatCfg = new MaatCfg();
//		List<MaatCfg> configCompileList = new ArrayList<>();
//		List<GroupCfg> groupRelationList = new ArrayList<>();
//		List<IpCfg> ipRegionList = new ArrayList<>();
//		List<StringCfg> strRegionList = new ArrayList<>();
//		List<NumBoundaryCfg> numRegionList = new ArrayList<>();
//		List<DigestCfg> digestRegionList = new ArrayList<>();
//		List<IpCfg> areaIpRegionList = new ArrayList<>();
//		if(isAudit==Constants.AUDIT_YES) {
//			ConfigGroupInfo group=configGroupInfoDao.getConfigGroupInfoByGroupId(Integer.parseInt(entity.getUserRegion1()));
//			if(group.getIsIssued()==1) {//复用，值下编译跟分组
//				maatCfg.initDefaultValue();
//				BeanUtils.copyProperties(entity, maatCfg);
//				maatCfg.setAction(entity.getAction());
//				maatCfg.setAuditTime(entity.getAuditTime());
//				maatCfg.setIpRegionList(ipRegionList);
//				maatCfg.setStrRegionList(strRegionList);
//				maatCfg.setNumRegionList(numRegionList);
//				maatCfg.setDigestRegionList(digestRegionList);
//				maatCfg.setGroupRelationList(groupRelationList);
//				maatCfg.setAreaIpRegionList(areaIpRegionList);
//				maatCfg.setIsValid(entity.getIsValid());
//				//group
//				GroupCfg groupCfg=new GroupCfg();
//				groupCfg.setCompileId(entity.getCompileId());
//				groupCfg.setGroupId(Integer.parseInt(entity.getUserRegion1()));
//				groupCfg.setIsValid(Constants.VALID_YES);
//				groupCfg.setAuditTime(auditTime);
//				groupRelationList.add(groupCfg);
//				maatCfg.setGroupNum(groupRelationList.size());
//				configCompileList.add(maatCfg);
//				maatBean.setConfigCompileList(configCompileList);
//				maatBean.setAuditTime(entity.getAuditTime());
//				maatBean.setCreatorName(entity.getCurrentUser().getName());
//				maatBean.setVersion(Constants.MAAT_VERSION);
//				maatBean.setOpAction(Constants.INSERT_ACTION);
//				//调用服务接口下发配置数据
//				String json=gsonToJson(maatBean);
//				logger.info("asn策略组复用配置下发配置参数："+json);
//				//调用服务接口下发配置
//				ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
//				logger.info("asn策略组复用配置下发响应信息："+result.getMsg());
//			}else {//首次下发
//				maatCfg.initDefaultValue();
//				BeanUtils.copyProperties(entity, maatCfg);
//				maatCfg.setAction(entity.getAction());
//				maatCfg.setAuditTime(entity.getAuditTime());
//				maatCfg.setStrRegionList(strRegionList);
//				maatCfg.setNumRegionList(numRegionList);
//				maatCfg.setDigestRegionList(digestRegionList);
//				maatCfg.setGroupRelationList(groupRelationList);
//				maatCfg.setAreaIpRegionList(areaIpRegionList);
//				maatCfg.setIsValid(entity.getIsValid());
//				//group
//				GroupCfg groupCfg=new GroupCfg();
//				groupCfg.setCompileId(entity.getCompileId());
//				groupCfg.setGroupId(Integer.parseInt(entity.getUserRegion1()));
//				groupCfg.setIsValid(Constants.VALID_YES);
//				groupCfg.setAuditTime(auditTime);
//				groupRelationList.add(groupCfg);
//				maatCfg.setGroupNum(groupRelationList.size());
//				//region
//				//查询asn group id下所有的 ip
//				AsnIpCfg asnIpCfg=new AsnIpCfg();
//				asnIpCfg.setAsnIpGroup(Integer.parseInt(entity.getUserRegion1()));
//				List<AsnIpCfg> allAsnIpCfgs=asnIpCfgDao.findAllList(asnIpCfg);
//				ipRegionList.addAll(groupReuseCfgAddRemoveConvert(allAsnIpCfgs,Constants.VALID_YES,null));
//				maatCfg.setIpRegionList(ipRegionList);
//				configCompileList.add(maatCfg);
//				maatBean.setConfigCompileList(configCompileList);
//				maatBean.setAuditTime(entity.getAuditTime());
//				maatBean.setCreatorName(entity.getCurrentUser().getName());
//				maatBean.setVersion(Constants.MAAT_VERSION);
//				maatBean.setOpAction(Constants.INSERT_ACTION);
//				
//				//调用服务接口下发配置数据
//				String json=gsonToJson(maatBean);
//				logger.info("asn策略配置下发配置参数："+json);
//				//调用服务接口下发配置
//				ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
//				logger.info("asn策略配置下发响应信息："+result.getMsg());
//				ConfigGroupInfo info=new ConfigGroupInfo();
//				info.setIsIssued(1);
//				info.setGroupId(Integer.parseInt(entity.getUserRegion1()));
//				configGroupInfoDao.updateConfigGroupInfobyGroupId(info);
//				AsnIpCfg cfg=new AsnIpCfg();
//				cfg.setIsValid(Constants.VALID_YES);
//				cfg.setAsnIpGroup(Integer.parseInt(entity.getUserRegion1()));
//				asnIpCfgDao.updateIssued(cfg);
//			}
//		}else if(isAudit==Constants.AUDIT_NOT_YES) {//取消审核通过
//			maatCfg.setCompileId(entity.getCompileId());
//			maatCfg.setServiceId(entity.getServiceId());
//			maatCfg.setIsValid(0);//无效
//			configCompileList.add(maatCfg);
//			maatBean.setConfigCompileList(configCompileList);
//			maatBean.setAuditTime(entity.getAuditTime());
//			maatBean.setCreatorName(entity.getCurrentUser().getName());
//			maatBean.setVersion(Constants.MAAT_VERSION);
//			maatBean.setOpAction(Constants.UPDATE_ACTION);
//			//调用服务接口取消配置
//			String json=gsonToJson(maatBean);
//			logger.info("asn策略取消下发配置参数："+json);
//			//调用服务接口下发配置
//			ToMaatResult result = ConfigServiceUtil.put(json,1);
//			logger.info("asn策略取消配置响应信息："+result.getMsg());
//		}else {
//			throw new RuntimeException("unknown isAudit value "+isAudit);
//		}
//		
//	}
}
