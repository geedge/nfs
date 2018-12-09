package com.nis.web.service.configuration;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.nis.domain.Page;
import com.nis.domain.basics.AsnIpCfg;
import com.nis.domain.configuration.AppPolicyCfg;
import com.nis.domain.configuration.AreaIpCfg;
import com.nis.domain.configuration.AvFileSampleCfg;
import com.nis.domain.configuration.BaseCfg;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.CommonStringCfg;
import com.nis.domain.configuration.ComplexkeywordCfg;
import com.nis.domain.configuration.DnsIpCfg;
import com.nis.domain.configuration.DnsResStrategy;
import com.nis.domain.configuration.FileDigestCfg;
import com.nis.domain.configuration.HttpBodyCfg;
import com.nis.domain.configuration.HttpReqHeadCfg;
import com.nis.domain.configuration.HttpResHeadCfg;
import com.nis.domain.configuration.HttpUrlCfg;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.configuration.NtcSubscribeIdCfg;
import com.nis.domain.configuration.PxyObjKeyring;
import com.nis.domain.configuration.PxyObjTrustedCaCert;
import com.nis.domain.configuration.PxyObjTrustedCaCrl;
import com.nis.domain.maat.MaatCfg;
import com.nis.domain.maat.MaatCfg.DigestCfg;
import com.nis.domain.maat.MaatCfg.GroupCfg;
import com.nis.domain.maat.MaatCfg.IpCfg;
import com.nis.domain.maat.MaatCfg.NumBoundaryCfg;
import com.nis.domain.maat.MaatCfg.StringCfg;
import com.nis.domain.maat.ToMaatBean;
import com.nis.domain.maat.ToMaatResult;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.Constants;
import com.nis.util.DateUtils;
import com.nis.util.FileUtils;
import com.nis.util.StringUtil;
import com.nis.web.dao.configuration.AreaIpCfgDao;
import com.nis.web.dao.configuration.CommonPolicyDao;
import com.nis.web.dao.configuration.ConfigSynchronizationDao;
import com.nis.web.dao.configuration.StringCfgDao;
import com.nis.web.dao.configuration.WebsiteCfgDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.BaseService;
import com.nis.web.service.CrudService;

@Service
public class CommonPolicyService extends CrudService<WebsiteCfgDao, CfgIndexInfo> {
	@Autowired
	protected CommonPolicyDao commonPolicyDao;
	@Autowired
	protected ConfigSynchronizationDao synchronizationDao;
	
	/**
	 * maat配置批量失效
	 * @param cfgList
	 * @param page
	 * @param entity
	 * @param list
	 * @param hasData
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public boolean auditNtcMaatData(List<Map<String,Object>> cfgList,
										Page page,
										BaseCfg entity,
										List<BaseCfg> list,
										boolean hasData)throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		ToMaatBean maatBean;		
		MaatCfg maatCfg;
		List<MaatCfg> configCompileList;
		
		maatBean = new ToMaatBean();		
		configCompileList = new ArrayList();
		List<Integer> compileIds = new ArrayList();
		for(BaseCfg cfg:list){
			compileIds.add(cfg.getCompileId());
		}
		
		if(!StringUtil.isEmpty(compileIds) && !StringUtil.isEmpty(entity.getTableName())) {
			commonPolicyDao.auditCfgBatch( entity.getTableName(), entity,compileIds,null);
		}
		
		if(cfgList!=null){
			for(Map<String,Object> m:cfgList){
				String tableName = m.get("tableName").toString();
				commonPolicyDao.auditCfgBatch( tableName, entity,compileIds,null);
			}
		}
		for(BaseCfg cfg:list){
			maatCfg = new MaatCfg();
			maatCfg.setCompileId(cfg.getCompileId());
			maatCfg.setServiceId(cfg.getServiceId());
			maatCfg.setIsValid(0);//无效
			configCompileList.add(maatCfg);
		}
		page.setList(list);
		if(page.isLastPage()){
			hasData = false;
		}
		if(!StringUtil.isEmpty(configCompileList.size())){
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(entity.getAuditTime());
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.UPDATE_ACTION);
			//调用服务接口取消配置
			String json=gsonToJson(maatBean);
			logger.info("配置下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json,1);
			logger.info("配置取消配置响应信息："+result.getMsg());
		}
		return hasData;
	}
	public List<IpPortCfg> getIpDropList(String tabName,List compileIds){
		return synchronizationDao.getIpDropList(tabName, compileIds);
	}
	public List<IpPortCfg> getIpPortList(IpPortCfg entity){
		return synchronizationDao.getIpPortListByService(entity);
	}
	public List<PxyObjTrustedCaCrl> getPxyObjTrustedCrlCfgListByCertId(List certIds){
		return synchronizationDao.getPxyObjTrustedCrlCfgListByCertId(certIds);
	}
	/**
	 * 回调类配置下发
	 * @param className
	 * @param page
	 * @param entity
	 * @param hasData
	 * @param isUpdateCfg
	 * @throws ClassNotFoundException
	 */
	public void auditCallBackData(List auditList,List compileIds,List ids,BaseCfg entity){
		if(!StringUtil.isEmpty(compileIds) && !StringUtil.isEmpty(entity.getTableName())) {
			commonPolicyDao.auditCfgBatch( "cfg_index_info", entity,compileIds,null);
		}
		if(!StringUtil.isEmpty(ids) && !StringUtil.isEmpty(entity.getTableName())) {
			commonPolicyDao.auditCfgBatch( entity.getTableName(), entity,ids,null);
			if(entity.getTableName().equals("pxy_obj_trusted_ca_cert")) {
				commonPolicyDao.auditCfgBatch("pxy_obj_trusted_ca_crl", entity,ids,null);
			}
		}
		
		if(entity.getIsAudit()==1){
			//调用服务接口下发配置数据
			String json=gsonToJson(auditList);
			logger.info("批量下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.postCallbackCfg(json);
			logger.info("批量下发响应信息："+result.getMsg());

		}else if(entity.getIsAudit()==3){
			//调用服务接口取消配置
			String json=gsonToJson(auditList);
			logger.info("批量下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json,2);
			logger.info("批量响应信息："+result.getMsg());
		}
	}
}
