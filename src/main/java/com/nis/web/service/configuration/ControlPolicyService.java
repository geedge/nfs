package com.nis.web.service.configuration;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nis.domain.Page;
import com.nis.domain.configuration.AreaIpCfg;
import com.nis.domain.configuration.HttpBodyCfg;
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
import com.nis.util.StringUtil;
import com.nis.web.dao.configuration.AreaIpCfgDao;
import com.nis.web.dao.configuration.ControlPolicyDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.BaseService;


@Service
public class ControlPolicyService extends BaseService{
	@Autowired
	protected ControlPolicyDao controlPolicyDao;
	@Autowired
	protected AreaIpCfgDao areaIpCfgDao;
	
	
	public Page<HttpBodyCfg> findPage(Page page, HttpBodyCfg entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"r"));
		entity.setPage(page);
		List<HttpBodyCfg> list=controlPolicyDao.findPage(entity);
		page.setList(list);
		return page;
	}
	
	public HttpBodyCfg getHttpBodyCfgById(Long cfgId) {
		return controlPolicyDao.getHttpBodyCfgById(cfgId);
	}
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveOrUpdate(HttpBodyCfg entity,String areaCfgIds){
		Date createTime=new Date();
		//设置区域运营商信息
		setAreaEffectiveIds(entity);
		//新增
		if(entity.getCfgId()==null){
			entity.setCreatorId(UserUtils.getUser().getId());
			entity.setCreateTime(createTime);
			entity.setIsValid(0);
			entity.setIsAudit(0);
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
				
				//保存区域IP信息
				if(entity.getAreaCfg()!=null&&entity.getAreaCfg().size()>0){
					for (AreaIpCfg areaIpCfg : entity.getAreaCfg()) {
						BeanUtils.copyProperties(entity, areaIpCfg,new String[]{"cfgRegionCode"
								,"cfgType"
						});
						areaIpCfgDao.saveAreaIpCfg(areaIpCfg);
					}
				}
				
				controlPolicyDao.insert(entity);
			}else{
				throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>");
			}
			
			//修改
		}else{
			Date editTime=new Date();
			entity.setIsValid(0);
			entity.setIsAudit(0);
			
			areaCfgIds=!StringUtil.isEmpty(areaCfgIds)? ","+areaCfgIds:"";
			
			if(!StringUtil.isEmpty(entity.getAreaCfg())){
				for (AreaIpCfg areaIpCfg : entity.getAreaCfg()) {
					if(!StringUtil.isEmpty(areaIpCfg.getCfgId())){
						if(areaCfgIds.contains(","+areaIpCfg.getCfgId()+",")){
							areaCfgIds=areaCfgIds.replace(areaIpCfg.getCfgId()+",", "");
						}
						//修改
						entity.setEditorId(UserUtils.getUser().getId());
						entity.setEditTime(editTime);
						BeanUtils.copyProperties(entity, areaIpCfg,new String[]{"cfgRegionCode"
								,"cfgType"
								,"cfgId"
						});
						areaIpCfgDao.updateAreaIpCfg(areaIpCfg);
					}else{
						//新增
						entity.setCreatorId(UserUtils.getUser().getId());
						entity.setCreateTime(createTime);
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
					AreaIpCfg areaIpCfg=new AreaIpCfg();
					areaIpCfg.setCfgId(Long.parseLong(cfgId));
					areaIpCfgDao.deleteAreaIpCfgByCfgId(areaIpCfg);
				}
			}
			
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(editTime);
			controlPolicyDao.update(entity);
		}
	}
	
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void update(Integer isAudit,Integer isValid,String ids,Integer functionId){
		HttpBodyCfg entity = new HttpBodyCfg();
		String[] idArray = ids.split(",");
		for(String id :idArray){
			entity.setCfgId(Long.parseLong(id));
			entity.setFunctionId(functionId);
			entity.setIsAudit(isAudit);
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			controlPolicyDao.update(entity);
			AreaIpCfg areaIpCfg=new AreaIpCfg();
			BeanUtils.copyProperties(entity, areaIpCfg);
			areaIpCfgDao.updateAreaIpCfgValid(areaIpCfg);
		}
	}
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void audit(Integer isAudit,Integer isValid,Integer functionId,String id,Date auditTime,String replaceKeyValue){
		HttpBodyCfg entity = new HttpBodyCfg();
		List<HttpBodyCfg> list = new ArrayList();
		List<AreaIpCfg> areaIpCfgList = new ArrayList();
		
		entity = controlPolicyDao.getHttpBodyCfgById(Long.parseLong(id));
		entity.setIsAudit(isAudit);
		entity.setIsValid(isValid);
		entity.setAuditorId(UserUtils.getUser().getId());
		entity.setAuditTime(auditTime);
		
		
		controlPolicyDao.update(entity);
		
		list.add(entity);
		
		AreaIpCfg areaIpCfg=new AreaIpCfg();
		BeanUtils.copyProperties(entity, areaIpCfg,new String[]{"cfgRegionCode"
				,"cfgType"
				,"cfgId"
		});
		areaIpCfgDao.updateAreaIpCfg(areaIpCfg);
		
		areaIpCfgList=areaIpCfgDao.getByCompileId(entity.getCompileId());
		
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
			if(!StringUtil.isEmpty(list)){
				Map<String,List> strMap = cfgConvert(strRegionList,list,2,entity,groupRelationList);
				groupRelationList=strMap.get("groupList");
				strRegionList=strMap.get("dstList");
			}
			if(!StringUtil.isEmpty(areaIpCfgList)){
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
			
			if(StringUtil.isEmpty(entity.getReplaceContent())) entity.setReplaceContent("");
			// req replace body set zone
			String replaceType=Constants.REPLACE_TYPE_KEY+"=";
			replaceType=replaceType+replaceKeyValue;
			//req replace body set regex
			String replaceRegex=Constants.REPLACE_REGEX_KEY+"=";
			replaceRegex=replaceRegex+entity.getReplaceContent();
			//req replace body set user_region
			maatCfg.setUserRegion(replaceType+	Constants.USER_REGION_SPLIT+replaceRegex);
			
			configCompileList.add(maatCfg);
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(auditTime);
			maatBean.setCreatorName(entity.getCurrentUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.INSERT_ACTION);
			//调用服务接口下发配置数据
			String json=gsonToJson(maatBean);
			logger.info("配置下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
			logger.info("配置下发响应信息："+result.getMsg());
			
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
			logger.info("配置下发配置参数："+json);
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json,1);
			logger.info("配置取消配置响应信息："+result.getMsg());
		}
	}
}
