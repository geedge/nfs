package com.nis.web.service.configuration;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.poi.ss.formula.functions.T;
import org.dom4j.Node;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import antlr.StringUtils;

import com.google.common.collect.Lists;
import com.nis.domain.FunctionServiceDict;
import com.nis.domain.Page;
import com.nis.domain.basics.AsnIpCfg;
import com.nis.domain.configuration.AppFeatureIndex;
import com.nis.domain.configuration.AppIpCfg;
import com.nis.domain.configuration.AppPolicyCfg;
import com.nis.domain.configuration.AvFileSampleCfg;
import com.nis.domain.configuration.BaseCfg;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.ComplexkeywordCfg;
import com.nis.domain.configuration.DdosIpCfg;
import com.nis.domain.configuration.DnsIpCfg;
import com.nis.domain.configuration.DnsResStrategy;
import com.nis.domain.configuration.FileDigestCfg;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.configuration.PxyObjKeyring;
import com.nis.domain.configuration.PxyObjTrustedCaCert;
import com.nis.domain.configuration.PxyObjTrustedCaCrl;
import com.nis.domain.configuration.WebsiteDomainTopic;
import com.nis.domain.maat.GroupReuseAddBean;
import com.nis.domain.maat.GroupReuseCfg;
import com.nis.domain.maat.MaatCfg;
import com.nis.domain.maat.ToMaatBean;
import com.nis.domain.maat.ToMaatResult;
import com.nis.domain.maat.MaatCfg.DigestCfg;
import com.nis.domain.maat.MaatCfg.GroupCfg;
import com.nis.domain.maat.MaatCfg.IpCfg;
import com.nis.domain.maat.MaatCfg.NumBoundaryCfg;
import com.nis.domain.maat.MaatCfg.StringCfg;
import com.nis.domain.specific.ConfigGroupInfo;
import com.nis.domain.specific.SpecificServiceCfg;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.Constants;
import com.nis.util.DateUtil;
import com.nis.util.DateUtils;
import com.nis.util.DictUtils;
import com.nis.util.FileUtils;
import com.nis.util.ServiceConfigTemplateUtil;
import com.nis.util.StringUtil;
import com.nis.web.dao.configuration.AppCfgDao;
import com.nis.web.dao.configuration.CommonPolicyDao;
import com.nis.web.dao.configuration.ConfigSynchronizationDao;
import com.nis.web.dao.specific.ConfigGroupInfoDao;
import com.nis.web.dao.specific.SpecificServiceCfgDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.BaseService;

/**
 * 配置全量同步事务类
 * @author zhangwei
 *
 */
@Service
public class ConfigSynchronizationService  extends BaseService{
	@Autowired
	protected ConfigSynchronizationDao configSynchronizationDao;
	@Autowired
	protected CommonPolicyDao commonPolicyDao;
	@Autowired
	protected SpecificServiceCfgDao specificServiceCfgDao;
	@Autowired
	protected ConfigGroupInfoDao configGroupInfoDao;
	@Autowired
	protected AppCfgDao appCfgDao;
	private boolean lastServiceTag = false;//标识是否是最后一个同步业务
	private boolean isFinished = false;
	/**
	 * 配置全量更新下发
	 * @param request
	 * @param response
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws ClassNotFoundException 
	 */
	public void send(HttpServletRequest request,HttpServletResponse response,BaseCfg cfg) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException {
		long start = System.currentTimeMillis();
		ServiceConfigTemplateUtil serviceTemplate = new ServiceConfigTemplateUtil();
		if(cfg==null){
			List<Map<String,Object>> serviceList = serviceTemplate.getServiceList();
			int lastService = 0;
			lastServiceTag = false;
			for(Map<String,Object> service:serviceList){
				lastService ++;
				if(lastService==serviceList.size()){
					lastServiceTag = true;
				}
				String tableName = service.get("tableName").toString();
				String serviceType = service.get("serviceType").toString();
				String className = service.get("className").toString();
				String serviceId = service.get("id").toString();
				BaseCfg entity = new BaseCfg();
				entity.setServiceId(Integer.valueOf(serviceId));
				entity.setIsAudit(1);
				entity.setIsValid(1);
				entity.setTableName(tableName);
				if("1".equals(serviceType)){//maat类配置
					List<Map<String,Object>> cfgList = (List<Map<String, Object>>) service.get("cfgList");
					List<Map<String,Object>> userRegionList = (List<Map<String, Object>>) service.get("userRegionList");
					if(cfgList.size()>0){
						Page page=new Page(request,response,Constants.MAAT_JSON_SEND_SIZE,"a");
						handleNtcMaatData(cfgList,userRegionList,page,entity,request,response,false,tableName);
					}else{
						int cfgType = Integer.parseInt(service.get("cfgType").toString());
						 if("ddos_ip_cfg".equals(tableName)){
								Page<DdosIpCfg> page=new Page<DdosIpCfg>(request,response,Constants.MAAT_JSON_SEND_SIZE,"a");
								handleDdosMaatData(cfgList,userRegionList,page,entity,request,response,false);
						}else{
							Page<T> page=new Page<T>(request,response,Constants.MAAT_JSON_SEND_SIZE,"a");
							handleSingleMaatData(cfgType,userRegionList,page,entity,request,response,false);
						}
					}
				}else if("2".equals(serviceType)){//回调类配置
					entity.setTableName(tableName);
					entity.setServiceId(Integer.valueOf(serviceId));
					Page<BaseCfg> page=new Page<BaseCfg>(request,response,Constants.MAAT_JSON_SEND_SIZE,"a");
					handleCallbackData(className,page,entity,request,response,false);
				}
				if(!isFinished && lastServiceTag){//如果业务没有配置数据，并且为最后一个业务，需要发送给服务接口一个结束标识
					String json = "{}";
					/*if("1".equals(serviceType)){
						json="{}";
					}else{
						json="[]";
					}*/
					JSONObject result = ConfigServiceUtil.configSync(json,Integer.parseInt(serviceType),entity.getServiceId(),entity.getTableName(),"FINISHED");
				}
			}
			long end = System.currentTimeMillis();
			logger.info("本次配置全量同步,开始时间："+start
					+",结束时间："+end
					+"，共计花费"+((end-start)/1000+"秒"));
		}else{
			
		}
		
		
	}
	/**
	 * 处理ntc业务maat类配置
	 * @param serviceId
	 * @param request
	 * @param response
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public void handleNtcMaatData(List<Map<String,Object>>cfgList,List<Map<String,Object>>userRegionList,
			Page<T> page,BaseCfg entity,HttpServletRequest request,HttpServletResponse response,
			boolean isUpdateCfg,String tableName) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		
		boolean hasData = true;
		while(hasData){
			entity.setPage(page);
			List list = Lists.newArrayList();
			if("cfg_index_info".equals(tableName)){
				list = configSynchronizationDao.getCfgIndexList(entity);
				if(!StringUtil.isEmpty(list)){
					hasData=auditNtcMaatData(cfgList,userRegionList,page,entity,list,hasData,isUpdateCfg);
					if(hasData) {
						page.setPageNo(page.getNext());
					}
				}else{
					hasData = false;
				}
			}else if("file_digest_cfg".equals(tableName)){
				list = configSynchronizationDao.getFileDigestListByService(entity);
				if(!StringUtil.isEmpty(list)){
					hasData=auditFileDigestData(cfgList,userRegionList,page,entity,list,hasData,isUpdateCfg);
					if(hasData) {
						page.setPageNo(page.getNext());
					}
				}else{
					hasData = false;
				}
			}else if("app_policy_cfg".equals(tableName)){
				list = configSynchronizationDao.getAppPolicyList(entity);
				if(!StringUtil.isEmpty(list)){
					hasData=auditAppPolicyData(cfgList,userRegionList,page,entity,list,hasData,isUpdateCfg);
					if(hasData) {
						page.setPageNo(page.getNext());
					}
				}else{
					hasData = false;
				}
			}else if("app_feature_index".equals(tableName)){
				list = configSynchronizationDao.getAppFeatureIndexList(entity);
				if(!StringUtil.isEmpty(list)){
					hasData=auditAppFeatureData(cfgList,userRegionList,page,entity,list,hasData,isUpdateCfg);
					if(hasData) {
						page.setPageNo(page.getNext());
					}
				}else{
					hasData = false;
				}
			}else{
				logger.info("全量同步未知业务");
				hasData = false;
			}
			
		}
	}
	/**
	 * MAAT配置批量下发
	 * @param cfgList
	 * @param userRegionList
	 * @param page
	 * @param entity
	 * @param list
	 * @param hasData
	 * @param isUpdateCfg  业务配置全部生效时需同步更新库表配置状态
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public boolean auditNtcMaatData(List<Map<String,Object>> cfgList,
										List<Map<String,Object>> userRegionList,
										Page page,
										BaseCfg entity,
										List<CfgIndexInfo> list,
										boolean hasData,
										boolean isUpdateCfg)throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		ToMaatBean maatBean;		
		MaatCfg maatCfg;
		List<MaatCfg> configCompileList;
		List<GroupCfg> groupRelationList;
		List<IpCfg> ipRegionList;
		List<StringCfg> strRegionList;
		List<NumBoundaryCfg> numRegionList;
		List<DigestCfg> digestRegionList;
		List<IpCfg> areaIpRegionList;
		
		List<IpPortCfg> ipList = new ArrayList();
		List<AsnIpCfg> asnIpList = new ArrayList();
		List<BaseStringCfg> strList = new ArrayList();
		List<ComplexkeywordCfg> complexStrList = new ArrayList();
		List<com.nis.domain.configuration.NumBoundaryCfg> numList = new ArrayList();
		List<FileDigestCfg> fileList = new ArrayList();
		maatBean = new ToMaatBean();		
		configCompileList = new ArrayList();
		List<Integer> compileIds = new ArrayList();
		List<String> asnGroupIds = new ArrayList();
		for(CfgIndexInfo cfg:list){
			compileIds.add(cfg.getCompileId());
			if(entity.getServiceId().equals(37)||entity.getServiceId().equals(149)){
				asnGroupIds.add(cfg.getUserRegion4());
			}
		}
		
		if(isUpdateCfg) {
			if(!StringUtil.isEmpty(compileIds) && !StringUtil.isEmpty(entity.getTableName())) {
				commonPolicyDao.auditCfgBatch( entity.getTableName(), entity,compileIds,null);
			}
		}
		
		if(cfgList!=null){
			for(Map<String,Object> m:cfgList){
				String tableName = m.get("tableName").toString();
				if("1".equals(m.get("cfgType"))){
					if(tableName.equals("asn_ip_cfg")){
						if(!StringUtil.isEmpty(asnGroupIds)) {
							if(isUpdateCfg) { //批量审核时只查询未下发的asnip
								asnIpList.addAll(configSynchronizationDao.getAsnIpList(asnGroupIds,0));
							}else { //全量下发时需要检索所有asnip
								asnIpList.addAll(configSynchronizationDao.getAsnIpList(asnGroupIds,null));
							}
						}
					}else{
						ipList.addAll(configSynchronizationDao.getIpPortList(tableName, compileIds));
					}
				}else if("2".equals(m.get("cfgType"))){
					strList.addAll(configSynchronizationDao.getStrList(tableName,compileIds));
				}else if("3".equals(m.get("cfgType"))){
					complexStrList.addAll(configSynchronizationDao.getComplexStrList(tableName,compileIds));
				}else if("4".equals(m.get("cfgType"))){
					
				}else if("5".equals(m.get("cfgType"))){
					fileList.addAll(configSynchronizationDao.getFileDigestList(tableName, compileIds));
				}
				if(isUpdateCfg) {
					if(tableName.equals("asn_ip_cfg")){
						if(!StringUtil.isEmpty(asnGroupIds)) {
							commonPolicyDao.auditCfgBatch(tableName, entity,asnGroupIds,null);
						}
					}else{
						commonPolicyDao.auditCfgBatch( tableName, entity,compileIds,null);
					}
				}
			}
		}
		//批量获取regionId,groupId(相同编译下的IP类配置多条ip只获取一个组号），分组复用的域配置不需要重新获取regionId,groupId
		List<Integer> regionIds = ConfigServiceUtil.getId(3, ipList.size()+strList.size()+complexStrList.size()+numList.size()+fileList.size());
		List<Integer> groupIds = ConfigServiceUtil.getId(2, ipList.size()+strList.size()+complexStrList.size()+numList.size()+fileList.size());
		for(CfgIndexInfo cfg:list){
			maatCfg = new MaatCfg();
			maatCfg.initDefaultValue();
			groupRelationList = new ArrayList();
			ipRegionList = new ArrayList();
			strRegionList = new ArrayList();
			numRegionList = new ArrayList();
			digestRegionList = new ArrayList();
			areaIpRegionList = new ArrayList();
			List list1 = new ArrayList();
			List<BaseStringCfg> list2 = new ArrayList();
			List<ComplexkeywordCfg> list3 = new ArrayList();
			List<com.nis.domain.configuration.NumBoundaryCfg> list4 = new ArrayList();
			List<FileDigestCfg> list5 = new ArrayList();
			String userRegion = "";
			//处理自定义域
			if(userRegionList!=null){
				for(Map<String,Object> n:userRegionList){
					Object userRegionPosition = n.get("userRegionPosition");
					if(userRegionPosition!=null && (userRegionPosition.toString().equals("1")||userRegionPosition.toString().equals("0"))){
						//通过反射机制获取自定义域字段值
						String regionColumn = n.get("regionColumn").toString();
						Class aClass = null;
						if(userRegionPosition.toString().equals(("0"))){
							aClass = BaseCfg.class;
						}else{
							aClass = CfgIndexInfo.class;
						}
								
						Object value = "";
						if(entity.getServiceId().equals(517)||entity.getServiceId().equals(560)){//代理替换策略
							String[] regionArray = regionColumn.split(",");
							for(int r=0;r<regionArray.length;r++){
								Field field = aClass.getDeclaredField(regionArray[r]);
								field.setAccessible(true);
								if(n.get("regionKey").equals("substitute")){
									value += "/"+replaceContentEscape(field.get(cfg)==null?"":field.get(cfg).toString());//自定义域以/分割时的处理
								}else{
									value += replaceContentEscape(field.get(cfg)==null?"":field.get(cfg).toString());
								}
								
							}
							userRegion += n.get("regionKey")+"="+value+Constants.USER_REGION_SPLIT;
						}else{
							Field field = aClass.getDeclaredField(regionColumn);
							field.setAccessible(true);
							value = field.get(cfg);
							if(!StringUtil.isEmpty(value)){
								if(StringUtil.isEmpty(n.get("regionKey"))){
									userRegion = value.toString();
								}else{
									userRegion += n.get("regionKey")+"="+value+Constants.USER_REGION_SPLIT;
								}
							}
						}
					}
//					System.out.println("userRegionList:"+n.get("regionKey")+","+n.get("regionColumn")+","+n.get("handleType"));
				}
				if(userRegion.endsWith(Constants.USER_REGION_SPLIT)){
					userRegion = userRegion.substring(0, userRegion.length()-1);
				}
			}
			for(AsnIpCfg ip:asnIpList){//asn不需要再次获取regionId,groupId
				if(ip.getAsnIpGroup().toString().equals(cfg.getUserRegion4())){//根据组号对应
					ip.setIsValid(entity.getIsValid());
					ip.setAuditTime(StringUtil.isEmpty(entity.getAuditTime()) ? cfg.getAuditTime():entity.getAuditTime());
					list1.add(ip);
				}
			}
			
			if(list1.size()>0){
				GroupCfg groupCfg=new GroupCfg();
				groupCfg.setCompileId(cfg.getCompileId());
				groupCfg.setGroupId(Integer.parseInt(cfg.getUserRegion4()));
				groupCfg.setIsValid(entity.getIsValid());
				groupCfg.setAuditTime(StringUtil.isEmpty(entity.getAuditTime()) ? cfg.getAuditTime():entity.getAuditTime());
				groupRelationList.add(groupCfg);
				ipRegionList.addAll(groupReuseCfgAddRemoveConvert(list1,Constants.VALID_YES,null));
			}else if(!StringUtil.isEmpty(asnGroupIds)) {
				GroupCfg groupCfg=new GroupCfg();
				groupCfg.setCompileId(cfg.getCompileId());
				groupCfg.setGroupId(Integer.parseInt(cfg.getUserRegion4()));
				groupCfg.setIsValid(entity.getIsValid());
				groupCfg.setAuditTime(StringUtil.isEmpty(entity.getAuditTime()) ? cfg.getAuditTime():entity.getAuditTime());
				groupRelationList.add(groupCfg);
				ipRegionList.addAll(ipRegionList);
			}
			//配置域转换
			userRegion = configCovert(ipList,strList,complexStrList,
					numList,fileList,regionIds,groupIds,entity,cfg.getCompileId(),
					StringUtil.isEmpty(entity.getAuditTime()) ? cfg.getAuditTime():entity.getAuditTime(),
					groupRelationList,ipRegionList,strRegionList,numRegionList,digestRegionList,areaIpRegionList,userRegion);
			
			BeanUtils.copyProperties(cfg, maatCfg);
			maatCfg.setAction(cfg.getAction());
			maatCfg.setAuditTime(StringUtil.isEmpty(entity.getAuditTime()) ? cfg.getAuditTime():entity.getAuditTime());
			maatCfg.setIpRegionList(ipRegionList);
			maatCfg.setStrRegionList(strRegionList);
			maatCfg.setNumRegionList(numRegionList);
			maatCfg.setDigestRegionList(digestRegionList);
			maatCfg.setGroupRelationList(groupRelationList);
			maatCfg.setGroupNum(groupRelationList.size());
			maatCfg.setAreaIpRegionList(areaIpRegionList);
			maatCfg.setIsValid(entity.getIsValid());
			if(!StringUtil.isEmpty(userRegion)){
				maatCfg.setUserRegion(userRegion);
			}
			configCompileList.add(maatCfg);
		}
		page.setList(list);
		if(page.isLastPage()){
			hasData = false;
		}
		if(!StringUtil.isEmpty(configCompileList.size())){
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(new Date());
			maatBean.setCreatorName(UserUtils.getUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.INSERT_ACTION);
			String json=gsonToJson(maatBean);
			//调用服务接口下发配置数据
			if(isUpdateCfg) {
				logger.info("配置批量下发："+json);
				//调用服务接口同步回调类配置
				ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
				if(result!=null){
					logger.info("配置批量下发响应信息："+result.getMsg());
				}
			}else {
				//调用服务接口配置全量更新
				isFinished = ((!hasData)&&lastServiceTag)?true:false;
				FileUtils.writeToFile("/home/ceiec/configSync/"+DateUtils.getDate("yyyy-MM-dd")+"/"+entity.getServiceId()+"_"+page.getPageNo()+"_"+System.currentTimeMillis()+".json", json, false);
				JSONObject result = ConfigServiceUtil.configSync(json,1,entity.getServiceId(),null,isFinished?"FINISHED":null);
			}
		}
		return hasData;
	}
	/**
	 * 文件摘要配置批量下发
	 * @param cfgList
	 * @param userRegionList
	 * @param page
	 * @param entity
	 * @param list
	 * @param hasData
	 * @param isUpdateCfg  业务配置全部生效时需同步更新库表配置状态
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public boolean auditFileDigestData(List<Map<String,Object>> cfgList,
										List<Map<String,Object>> userRegionList,
										Page page,
										BaseCfg entity,
										List<FileDigestCfg> list,
										boolean hasData,
										boolean isUpdateCfg)throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		ToMaatBean maatBean;		
		MaatCfg maatCfg;
		List<MaatCfg> configCompileList;
		List<GroupCfg> groupRelationList;
		List<IpCfg> ipRegionList;
		List<StringCfg> strRegionList;
		List<NumBoundaryCfg> numRegionList;
		List<DigestCfg> digestRegionList;
		List<IpCfg> areaIpRegionList;
		
		List<IpPortCfg> ipList = new ArrayList();
		List<BaseStringCfg> strList = new ArrayList();
		List<ComplexkeywordCfg> complexStrList = new ArrayList();
		List<com.nis.domain.configuration.NumBoundaryCfg> numList = new ArrayList();
		List<FileDigestCfg> fileList = new ArrayList();
		maatBean = new ToMaatBean();		
		configCompileList = new ArrayList();
		List<Integer> compileIds = new ArrayList();
		for(FileDigestCfg cfg:list){
			compileIds.add(cfg.getCompileId());
			if(!StringUtil.isEmpty(cfg.getCfdsLevel()) && (cfg.getCfdsLevel() > 10)){
				cfg.setCfdsLevel((int)(cfg.getCfdsLevel()/10));//文件摘要置信度界面显示为70,80,90,100，下发为7,8,9,10
			}
			fileList.add(cfg);
		}
		
		if(isUpdateCfg) {
			if(!StringUtil.isEmpty(compileIds) && !StringUtil.isEmpty(entity.getTableName())) {
				commonPolicyDao.auditCfgBatch( entity.getTableName(), entity,compileIds,null);
			}
		}
		
		if(cfgList!=null){
			for(Map<String,Object> m:cfgList){
				String tableName = m.get("tableName").toString();
				if("1".equals(m.get("cfgType"))){
					ipList.addAll(configSynchronizationDao.getIpPortList(tableName, compileIds));
				}else if("2".equals(m.get("cfgType"))){
					strList.addAll(configSynchronizationDao.getStrList(tableName,compileIds));
				}else if("3".equals(m.get("cfgType"))){
					complexStrList.addAll(configSynchronizationDao.getComplexStrList(tableName,compileIds));
				}else if("4".equals(m.get("cfgType"))){
					
				}else if("5".equals(m.get("cfgType"))){
					fileList.addAll(configSynchronizationDao.getFileDigestList(tableName, compileIds));
				}
				if(isUpdateCfg) {
					commonPolicyDao.auditCfgBatch( tableName, entity,compileIds,null);
				}
			}
		}
		//批量获取regionId,groupId(相同编译下的IP类配置多条ip只获取一个组号），分组复用的域配置不需要重新获取regionId,groupId
		List<Integer> regionIds = ConfigServiceUtil.getId(3, ipList.size()+strList.size()+complexStrList.size()+numList.size()+fileList.size());
		List<Integer> groupIds = ConfigServiceUtil.getId(2, ipList.size()+strList.size()+complexStrList.size()+numList.size()+fileList.size());
		for(FileDigestCfg cfg:list){
			maatCfg = new MaatCfg();
			maatCfg.initDefaultValue();
			groupRelationList = new ArrayList();
			ipRegionList = new ArrayList();
			strRegionList = new ArrayList();
			numRegionList = new ArrayList();
			digestRegionList = new ArrayList();
			areaIpRegionList = new ArrayList();
			List list1 = new ArrayList();
			List<BaseStringCfg> list2 = new ArrayList();
			List<ComplexkeywordCfg> list3 = new ArrayList();
			List<com.nis.domain.configuration.NumBoundaryCfg> list4 = new ArrayList();
			List<FileDigestCfg> list5 = new ArrayList();
			String userRegion = "";
			//处理自定义域
			if(userRegionList!=null){
				for(Map<String,Object> n:userRegionList){
					Object userRegionPosition = n.get("userRegionPosition");
					if(userRegionPosition!=null && (userRegionPosition.toString().equals("1")||userRegionPosition.toString().equals("0"))){
						//通过反射机制获取自定义域字段值
						String regionColumn = n.get("regionColumn").toString();
						Class aClass = null;
						if(userRegionPosition.toString().equals(("0"))){
							aClass = BaseCfg.class;
						}else{
							aClass = FileDigestCfg.class;
						}
						Object value = "";
						Field field = aClass.getDeclaredField(regionColumn);
						field.setAccessible(true);
						value = field.get(cfg);
						if(!StringUtil.isEmpty(value)){
							if(StringUtil.isEmpty(n.get("regionKey"))){
								userRegion = value.toString();
							}else{
								userRegion += n.get("regionKey")+"="+value+Constants.USER_REGION_SPLIT;
							}
						}
					}
//					System.out.println("userRegionList:"+n.get("regionKey")+","+n.get("regionColumn")+","+n.get("handleType"));
				}
				if(userRegion.endsWith(Constants.USER_REGION_SPLIT)){
					userRegion = userRegion.substring(0, userRegion.length()-1);
				}
			}
			//配置域转换
			configCovert(ipList,strList,complexStrList,
					numList,fileList,regionIds,groupIds,entity,cfg.getCompileId(),
					StringUtil.isEmpty(entity.getAuditTime()) ? cfg.getAuditTime():entity.getAuditTime(),
					groupRelationList,ipRegionList,strRegionList,numRegionList,digestRegionList,areaIpRegionList,userRegion);
			BeanUtils.copyProperties(cfg, maatCfg);
			maatCfg.setAction(cfg.getAction());
			maatCfg.setAuditTime(StringUtil.isEmpty(entity.getAuditTime()) ? cfg.getAuditTime():entity.getAuditTime());
			maatCfg.setIpRegionList(ipRegionList);
			maatCfg.setStrRegionList(strRegionList);
			maatCfg.setNumRegionList(numRegionList);
			maatCfg.setDigestRegionList(digestRegionList);
			maatCfg.setGroupRelationList(groupRelationList);
			maatCfg.setGroupNum(groupRelationList.size());
			maatCfg.setAreaIpRegionList(areaIpRegionList);
			maatCfg.setIsValid(entity.getIsValid());
			if(!StringUtil.isEmpty(userRegion)){
				maatCfg.setUserRegion(userRegion);
			}
			configCompileList.add(maatCfg);
		}
		page.setList(list);
		if(page.isLastPage()){
			hasData = false;
		}
		if(!StringUtil.isEmpty(configCompileList.size())){
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(new Date());
			maatBean.setCreatorName(UserUtils.getUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.INSERT_ACTION);
			String json=gsonToJson(maatBean);
			//调用服务接口下发配置数据
			if(isUpdateCfg) {
				logger.info("配置批量下发："+json);
				//调用服务接口同步回调类配置
				ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
				if(result!=null){
					logger.info("配置批量下发响应信息："+result.getMsg());
				}
			}else {
				//调用服务接口配置全量更新
				isFinished = ((!hasData)&&lastServiceTag)?true:false;
				FileUtils.writeToFile("/home/ceiec/configSync/"+DateUtils.getDate("yyyy-MM-dd")+"/"+entity.getServiceId()+"_"+page.getPageNo()+"_"+System.currentTimeMillis()+".json", json, false);
				JSONObject result = ConfigServiceUtil.configSync(json,1,entity.getServiceId(),null,isFinished?"FINISHED":null);
			}
		}
		return hasData;
	}
	/**
	 * APP策略配置批量下发
	 * @param cfgList
	 * @param userRegionList
	 * @param page
	 * @param entity
	 * @param list
	 * @param hasData
	 * @param isUpdateCfg  业务配置全部生效时需同步更新库表配置状态
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public boolean auditAppPolicyData(List<Map<String,Object>> cfgList,
										List<Map<String,Object>> userRegionList,
										Page page,
										BaseCfg entity,
										List<AppPolicyCfg> list,
										boolean hasData,
										boolean isUpdateCfg)throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		ToMaatBean maatBean;		
		MaatCfg maatCfg;
		List<MaatCfg> configCompileList;
		List<GroupCfg> groupRelationList;
		List<IpCfg> ipRegionList;
		List<StringCfg> strRegionList;
		List<NumBoundaryCfg> numRegionList;
		List<DigestCfg> digestRegionList;
		List<IpCfg> areaIpRegionList;
		
		List<IpPortCfg> ipList = new ArrayList();
		List<BaseStringCfg> strList = new ArrayList();
		List<ComplexkeywordCfg> complexStrList = new ArrayList();
		List<com.nis.domain.configuration.NumBoundaryCfg> numList = new ArrayList();
		List<FileDigestCfg> fileList = new ArrayList();
		maatBean = new ToMaatBean();		
		configCompileList = new ArrayList();
		List<Integer> compileIds = new ArrayList();
		List<String> asnGroupIds = new ArrayList();
		for(AppPolicyCfg cfg:list){
			compileIds.add(cfg.getCompileId());
		}
		if(isUpdateCfg) {
			if(!StringUtil.isEmpty(compileIds) && !StringUtil.isEmpty(entity.getTableName())) {
				commonPolicyDao.auditCfgBatch( entity.getTableName(), entity,compileIds,null);
			}
		}
		if(cfgList!=null){
			for(Map<String,Object> m:cfgList){
				String tableName = m.get("tableName").toString();
				if("1".equals(m.get("cfgType"))){
					ipList.addAll(configSynchronizationDao.getIpPortList(tableName, compileIds));
				}else if("2".equals(m.get("cfgType"))){
					strList.addAll(configSynchronizationDao.getStrList(tableName,compileIds));
				}else if("3".equals(m.get("cfgType"))){
					complexStrList.addAll(configSynchronizationDao.getComplexStrList(tableName,compileIds));
				}else if("4".equals(m.get("cfgType"))){
					
				}else if("5".equals(m.get("cfgType"))){
					fileList.addAll(configSynchronizationDao.getFileDigestList(tableName, compileIds));
				}
				if(isUpdateCfg) {
					if(!StringUtil.isEmpty(compileIds) && !StringUtil.isEmpty(entity.getTableName())) {
						commonPolicyDao.auditCfgBatch( entity.getTableName(), entity,compileIds,null);
					}
				}
			}
		}
		//批量获取regionId,groupId(相同编译下的IP类配置多条ip只获取一个组号），compileIds.size()表示app策略本身有一个字符串域
		List<Integer> regionIds = ConfigServiceUtil.getId(3, ipList.size()+strList.size()+compileIds.size()+complexStrList.size()+numList.size()+fileList.size());
		List<Integer> groupIds = ConfigServiceUtil.getId(2, ipList.size()+strList.size()+compileIds.size()+complexStrList.size()+numList.size()+fileList.size());
		for(AppPolicyCfg cfg:list){
			maatCfg = new MaatCfg();
			maatCfg.initDefaultValue();
			groupRelationList = new ArrayList();
			ipRegionList = new ArrayList();
			strRegionList = new ArrayList();
			numRegionList = new ArrayList();
			digestRegionList = new ArrayList();
			areaIpRegionList = new ArrayList();
			List list1 = new ArrayList();
			List<BaseStringCfg> list2 = new ArrayList();
			List<ComplexkeywordCfg> list3 = new ArrayList();
			List<com.nis.domain.configuration.NumBoundaryCfg> list4 = new ArrayList();
			List<FileDigestCfg> list5 = new ArrayList();
			String userRegion = "";
			//处理自定义域
			if(userRegionList!=null){
				for(Map<String,Object> n:userRegionList){
					Object userRegionPosition = n.get("userRegionPosition");
					if(userRegionPosition!=null && (userRegionPosition.toString().equals("1")||userRegionPosition.toString().equals("0"))){
						//通过反射机制获取自定义域字段值
						String regionColumn = n.get("regionColumn").toString();
						Class aClass = null;
						if(userRegionPosition.toString().equals(("0"))){
							aClass = BaseCfg.class;
						}else{
							aClass = AppPolicyCfg.class;
						}
						Object value = "";
						Field field = aClass.getDeclaredField(regionColumn);
						field.setAccessible(true);
						value = field.get(cfg);
						if(!StringUtil.isEmpty(value)){
							if(StringUtil.isEmpty(n.get("regionKey"))){
								userRegion = value.toString();
							}else{
								userRegion += n.get("regionKey")+"="+value+Constants.USER_REGION_SPLIT;
							}
						}
					}
				}
				if(userRegion.endsWith(Constants.USER_REGION_SPLIT)){
					userRegion = userRegion.substring(0, userRegion.length()-1);
				}
			}
			//将app码与行为码设置为字符串域
			StringBuffer cfgKeywords = new StringBuffer();
			BaseStringCfg strCfg = new BaseStringCfg();
			if(cfg.getServiceId().equals(35)||cfg.getServiceId().equals(147)||cfg.getServiceId().equals(1059)){//基础协议
				cfgKeywords.append(Constants.PROTO_ID_REGION + "=" + cfg.getAppCode());
			}else if(cfg.getServiceId().equals(33)||cfg.getServiceId().equals(145)||cfg.getServiceId().equals(1056)){
				cfgKeywords.append(Constants.APP_ID_REGION + "=" + cfg.getAppCode());
				if(cfg.getBehavCode()!=null){
					cfgKeywords.append(Constants.KEYWORD_EXPR);
					cfgKeywords.append(Constants.BEHAV_ID_REGION + "=" + cfg.getBehavCode());
				}
			}else if(cfg.getServiceId().equals(36)||cfg.getServiceId().equals(148)||cfg.getServiceId().equals(1060)){//隧道加密协议
				cfgKeywords.append(Constants.PROTO_ID_REGION + "=" + cfg.getAppCode());
				if(cfg.getBehavCode()!=null){
					cfgKeywords.append(Constants.KEYWORD_EXPR);
					cfgKeywords.append(Constants.BEHAV_ID_REGION + "=" + cfg.getBehavCode());
				}
			}
			cfg.setCfgKeywords(cfgKeywords.toString());
			BeanUtils.copyProperties(cfg, strCfg);
			strList.add(strCfg);
			//配置域转换
			configCovert(ipList,strList,complexStrList,
					numList,fileList,regionIds,groupIds,entity,cfg.getCompileId(),
					StringUtil.isEmpty(entity.getAuditTime()) ? cfg.getAuditTime():entity.getAuditTime(),
					groupRelationList,ipRegionList,strRegionList,numRegionList,digestRegionList,areaIpRegionList,userRegion);
			
			BeanUtils.copyProperties(cfg, maatCfg);
			maatCfg.setAction(cfg.getAction());
			maatCfg.setAuditTime(StringUtil.isEmpty(entity.getAuditTime()) ? cfg.getAuditTime():entity.getAuditTime());
			maatCfg.setIpRegionList(ipRegionList);
			maatCfg.setStrRegionList(strRegionList);
			maatCfg.setNumRegionList(numRegionList);
			maatCfg.setDigestRegionList(digestRegionList);
			maatCfg.setGroupRelationList(groupRelationList);
			maatCfg.setGroupNum(groupRelationList.size());
			maatCfg.setAreaIpRegionList(areaIpRegionList);
			maatCfg.setIsValid(entity.getIsValid());
			if(!StringUtil.isEmpty(userRegion)){
				maatCfg.setUserRegion(userRegion);
			}
			configCompileList.add(maatCfg);
		}
		page.setList(list);
		if(page.isLastPage()){
			hasData = false;
		}
		if(!StringUtil.isEmpty(configCompileList.size())){
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(new Date());
			maatBean.setCreatorName(UserUtils.getUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.INSERT_ACTION);
			String json=gsonToJson(maatBean);
			//调用服务接口下发配置数据
			if(isUpdateCfg) {
				logger.info("配置批量下发："+json);
				//调用服务接口同步回调类配置
				ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
				if(result!=null){
					logger.info("配置批量下发响应信息："+result.getMsg());
				}
			}else {
				//调用服务接口配置全量更新
				isFinished = ((!hasData)&&lastServiceTag)?true:false;
				FileUtils.writeToFile("/home/ceiec/configSync/"+DateUtils.getDate("yyyy-MM-dd")+"/"+entity.getServiceId()+"_"+page.getPageNo()+"_"+System.currentTimeMillis()+".json", json, false);
//				JSONObject result = ConfigServiceUtil.configSync(json,1,entity.getServiceId(),null,isFinished?"FINISHED":null);
			}
		}
		return hasData;
	}
	/**
	 * APP特征批量下发
	 * @param cfgList
	 * @param userRegionList
	 * @param page
	 * @param entity
	 * @param list
	 * @param hasData
	 * @param isUpdateCfg  业务配置全部生效时需同步更新库表配置状态
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public boolean auditAppFeatureData(List<Map<String,Object>> cfgList,
										List<Map<String,Object>> userRegionList,
										Page page,
										BaseCfg entity,
										List<AppFeatureIndex> list,
										boolean hasData,
										boolean isUpdateCfg)throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		ToMaatBean maatBean;		
		MaatCfg maatCfg;
		List<MaatCfg> configCompileList;
		List<GroupCfg> groupRelationList;
		List<IpCfg> ipRegionList;
		List<StringCfg> strRegionList;
		List<NumBoundaryCfg> numRegionList;
		List<DigestCfg> digestRegionList;
		List<IpCfg> areaIpRegionList;
		
		List<IpPortCfg> ipList = new ArrayList();
		List<BaseStringCfg> strList = new ArrayList();
		List<ComplexkeywordCfg> complexStrList = new ArrayList();
		List<com.nis.domain.configuration.NumBoundaryCfg> numList = new ArrayList();
		List<FileDigestCfg> fileList = new ArrayList();
		maatBean = new ToMaatBean();		
		configCompileList = new ArrayList();
		List<Integer> compileIds = new ArrayList();
		List<String> asnGroupIds = new ArrayList();
		for(AppFeatureIndex cfg:list){
			compileIds.add(cfg.getCompileId());
			if(entity.getServiceId().equals(37)||entity.getServiceId().equals(149)){
				asnGroupIds.add(cfg.getUserRegion4());
			}
		}
		if(isUpdateCfg) {
			if(!StringUtil.isEmpty(compileIds) && !StringUtil.isEmpty(entity.getTableName())) {
				commonPolicyDao.auditCfgBatch( entity.getTableName(), entity,compileIds,null);
			}
		}
		if(cfgList!=null){
			for(Map<String,Object> m:cfgList){
				String tableName = m.get("tableName").toString();
				if("1".equals(m.get("cfgType"))){
					ipList.addAll(configSynchronizationDao.getAppIpPortList(tableName, compileIds));
				}else if("2".equals(m.get("cfgType"))){
					strList.addAll(configSynchronizationDao.getStrList(tableName,compileIds));
				}else if("3".equals(m.get("cfgType"))){
					complexStrList.addAll(configSynchronizationDao.getComplexStrList(tableName,compileIds));
				}else if("4".equals(m.get("cfgType"))){
					
				}else if("5".equals(m.get("cfgType"))){
					fileList.addAll(configSynchronizationDao.getFileDigestList(tableName, compileIds));
				}
				if(isUpdateCfg) {
					if(!StringUtil.isEmpty(compileIds) && !StringUtil.isEmpty(entity.getTableName())) {
						commonPolicyDao.auditCfgBatch( entity.getTableName(), entity,compileIds,null);
					}
				}
			}
		}
		//批量获取regionId,groupId(相同编译下的IP类配置多条ip只获取一个组号），分组复用的域配置不需要重新获取regionId,groupId
		List<Integer> regionIds = ConfigServiceUtil.getId(3, ipList.size()+strList.size()+complexStrList.size()+numList.size()+fileList.size());
		List<Integer> groupIds = ConfigServiceUtil.getId(2, ipList.size()+strList.size()+complexStrList.size()+numList.size()+fileList.size());
		for(AppFeatureIndex cfg:list){
			maatCfg = new MaatCfg();
			maatCfg.initDefaultValue();
			groupRelationList = new ArrayList();
			ipRegionList = new ArrayList();
			strRegionList = new ArrayList();
			numRegionList = new ArrayList();
			digestRegionList = new ArrayList();
			areaIpRegionList = new ArrayList();
			List list1 = new ArrayList();
			List<BaseStringCfg> list2 = new ArrayList();
			List<ComplexkeywordCfg> list3 = new ArrayList();
			List<com.nis.domain.configuration.NumBoundaryCfg> list4 = new ArrayList();
			List<FileDigestCfg> list5 = new ArrayList();
			String userRegion = "";
			//处理自定义域
			if(userRegionList!=null){
				for(Map<String,Object> n:userRegionList){
					Object userRegionPosition = n.get("userRegionPosition");
					if(userRegionPosition!=null && (userRegionPosition.toString().equals("1")||userRegionPosition.toString().equals("0"))){
						//通过反射机制获取自定义域字段值
						String regionColumn = n.get("regionColumn").toString();
						Class aClass = null;
						if(userRegionPosition.toString().equals(("0"))){
							aClass = BaseCfg.class;
						}else{
							aClass = AppFeatureIndex.class;
						}
						Object value = "";
						Field field = aClass.getDeclaredField(regionColumn);
						field.setAccessible(true);
						value = field.get(cfg);
						if(!StringUtil.isEmpty(value)){
							if(StringUtil.isEmpty(n.get("regionKey"))){
								userRegion = value.toString();
							}else{
								userRegion += n.get("regionKey")+"="+value+Constants.USER_REGION_SPLIT;
							}
						}
					}
				}
				if(userRegion.endsWith(Constants.USER_REGION_SPLIT)){
					userRegion = userRegion.substring(0, userRegion.length()-1);
				}
			}
			//配置域转换
			configCovert(ipList,strList,complexStrList,
					numList,fileList,regionIds,groupIds,entity,cfg.getCompileId(),
					StringUtil.isEmpty(entity.getAuditTime()) ? cfg.getAuditTime():entity.getAuditTime(),
					groupRelationList,ipRegionList,strRegionList,numRegionList,digestRegionList,areaIpRegionList,userRegion);
			BeanUtils.copyProperties(cfg, maatCfg);
			maatCfg.setAction(cfg.getAction());
			maatCfg.setAuditTime(StringUtil.isEmpty(entity.getAuditTime()) ? cfg.getAuditTime():entity.getAuditTime());
			maatCfg.setIpRegionList(ipRegionList);
			maatCfg.setStrRegionList(strRegionList);
			maatCfg.setNumRegionList(numRegionList);
			maatCfg.setDigestRegionList(digestRegionList);
			maatCfg.setGroupRelationList(groupRelationList);
			maatCfg.setGroupNum(groupRelationList.size());
			maatCfg.setAreaIpRegionList(areaIpRegionList);
			maatCfg.setIsValid(entity.getIsValid());
			if(!StringUtil.isEmpty(userRegion)){
				maatCfg.setUserRegion(userRegion);
			}
			configCompileList.add(maatCfg);
		}
		page.setList(list);
		if(page.isLastPage()){
			hasData = false;
		}
		if(!StringUtil.isEmpty(configCompileList.size())){
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(new Date());
			maatBean.setCreatorName(UserUtils.getUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.INSERT_ACTION);
			String json=gsonToJson(maatBean);
			//调用服务接口下发配置数据
			if(isUpdateCfg) {
				logger.info("配置批量下发："+json);
				//调用服务接口同步回调类配置
				ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
				if(result!=null){
					logger.info("配置批量下发响应信息："+result.getMsg());
				}
			}else {
				//调用服务接口配置全量更新
				isFinished = ((!hasData)&&lastServiceTag)?true:false;
				FileUtils.writeToFile("/home/ceiec/configSync/"+DateUtils.getDate("yyyy-MM-dd")+"/"+entity.getServiceId()+"_"+page.getPageNo()+"_"+System.currentTimeMillis()+".json", json, false);
				JSONObject result = ConfigServiceUtil.configSync(json,1,entity.getServiceId(),null,isFinished?"FINISHED":null);
			}
		}
		return hasData;
	}
	/**
	 * 处理ddos配置
	 * @param serviceId
	 * @param request
	 * @param response
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public void handleDdosMaatData(List<Map<String,Object>>cfgList,List<Map<String,Object>>userRegionList,
			Page<DdosIpCfg> page,BaseCfg entity,HttpServletRequest request,HttpServletResponse response,
			boolean isUpdateCfg) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		
		boolean hasData = true;
		while(hasData){
			entity.setPage(page);
			List<DdosIpCfg> list = configSynchronizationDao.getDdosIpCfgList(entity);
			if(!StringUtil.isEmpty(list)){
				hasData=auditDdosMaatData(cfgList,userRegionList,
						page,entity,list,hasData,isUpdateCfg);
				if(hasData) {
					page.setPageNo(page.getNext());
				}
			}else{
				hasData = false;
			}
		}
	}
	/**
	 * 处理单域maat配置
	 * @param serviceId
	 * @param request
	 * @param response
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public void handleSingleMaatData(int cfgType,List<Map<String,Object>>userRegionList,
			Page<T> page,BaseCfg entity,HttpServletRequest request,HttpServletResponse response,
			boolean isUpdateCfg) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		
		boolean hasData = true;
		while(hasData){
			entity.setPage(page);
			List list = Lists.newArrayList();
			if(cfgType==1){
				if(entity.getServiceId().equals(1028)){
					list = configSynchronizationDao.getAppIpFeatureList(entity);
				}else{
					list = configSynchronizationDao.getIpPortListByService(entity);
				}
			}else if(cfgType==2){
				list = configSynchronizationDao.getStrListByService(entity);
			}else if(cfgType==3){
				list = configSynchronizationDao.getComplexStrListByService(entity);
			}
			if(!StringUtil.isEmpty(list)){
				if(entity.getServiceId().equals(1028)){
					ConfigGroupInfo groupInfo=new ConfigGroupInfo();
					groupInfo.setIsIssued(1);//全量下发只用获取已下发的group信息
					List<ConfigGroupInfo> groupInfos=configGroupInfoDao.findAllList(groupInfo);//每一批次下发都取一次最新的group_info信息
					Map<Integer, Integer> compileGroups=new HashMap<>();
					if(!StringUtil.isEmpty(groupInfos)) {
						for (ConfigGroupInfo configGroupInfo : groupInfos) {
							compileGroups.put(configGroupInfo.getCompileId(), configGroupInfo.getGroupId());
						}
					}
					entity.setCompileGroupMap(compileGroups);
					hasData=auditAppIpData(userRegionList,
							page,entity,list,hasData,isUpdateCfg);
				}else{
					hasData=auditSingleMaatData(cfgType,userRegionList,
							page,entity,list,hasData,isUpdateCfg);
				}
				
				if(hasData) {
					page.setPageNo(page.getNext());
				}
			}else{
				hasData = false;
			}
		}
	}
	/**
	 * ddos配置批量下发
	 * @param cfgList
	 * @param userRegionList
	 * @param page
	 * @param entity
	 * @param list
	 * @param hasData
	 * @param isUpdateCfg 业务配置全部生效时需同步更新库表配置状态
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public boolean auditDdosMaatData(List<Map<String,Object>> cfgList,
										List<Map<String,Object>>userRegionList,
										Page<DdosIpCfg> page,
										BaseCfg entity,
										List<DdosIpCfg> list,
										boolean hasData,
										boolean isUpdateCfg) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		ToMaatBean maatBean;		
		MaatCfg maatCfg;
		List<MaatCfg> configCompileList;
		List<GroupCfg> groupRelationList;
		List<IpCfg> ipRegionList;
		List<StringCfg> strRegionList;
		List<NumBoundaryCfg> numRegionList;
		List<DigestCfg> digestRegionList;
		List<IpCfg> areaIpRegionList;
		maatBean = new ToMaatBean();		
		configCompileList = new ArrayList();
		List<Integer> compileIds=new ArrayList();
		//批量获取regionId,groupId
 		List<Integer> regionIds = ConfigServiceUtil.getId(3, list.size());
		List<Integer> groupIds = ConfigServiceUtil.getId(2, list.size());
		for(int i=0;i<list.size();i++){
			DdosIpCfg cfg=list.get(i);
			cfg.setRegionId(regionIds.get(i));
			cfg.setGroupId(groupIds.get(i));
			cfg.setIsValid(entity.getIsValid());
			cfg.setAuditTime(StringUtil.isEmpty(entity.getAuditTime()) ? cfg.getAuditTime():entity.getAuditTime());
			maatCfg = new MaatCfg();
			maatCfg.initDefaultValue();
			groupRelationList = new ArrayList();
			ipRegionList = new ArrayList();
			strRegionList = new ArrayList();
			numRegionList = new ArrayList();
			digestRegionList = new ArrayList();
			areaIpRegionList = new ArrayList();
			List<DdosIpCfg> list1 = new ArrayList();
			String userRegion = "";
			//处理自定义域
			if(userRegionList!=null){
				for(Map<String,Object> n:userRegionList){
					Object userRegionPosition = n.get("userRegionPosition");
					if(userRegionPosition!=null && (userRegionPosition.toString().equals("1")||userRegionPosition.toString().equals("0"))){
						//通过反射机制获取自定义域字段值
						String regionColumn = n.get("regionColumn").toString();
						Class aClass = null;
						if(userRegionPosition.toString().equals(("0"))){
							aClass = BaseCfg.class;
						}else{
							aClass = DdosIpCfg.class;
						}
						Object value = "";
						Field field = aClass.getDeclaredField(regionColumn);
						field.setAccessible(true);
						value = field.get(cfg);
						if(!StringUtil.isEmpty(value)){
							if(StringUtil.isEmpty(n.get("regionKey"))){
								userRegion = value.toString();
							}else{
								userRegion += n.get("regionKey")+"="+value+Constants.USER_REGION_SPLIT;
							}
						}
					}
				}
				if(userRegion.endsWith(Constants.USER_REGION_SPLIT)){
					userRegion = userRegion.substring(0, userRegion.length()-1);
				}
			}
			if(isUpdateCfg && !StringUtil.isEmpty(cfg) && !StringUtil.isEmpty(cfg.getCompileId())) {
				compileIds.add(cfg.getCompileId());
			}
			list1.add(cfg);
			if(list1.size()>0){
				Map<String,List> map = cfgToMaatConvert(ipRegionList,list1,1,groupRelationList);
				groupRelationList=map.get("groupList");
				ipRegionList=map.get("dstList");
				if(map.get("numRegionList")!=null){
					numRegionList.addAll(map.get("numRegionList"));
				}
			}
			BeanUtils.copyProperties(cfg, maatCfg);
			maatCfg.setAction(cfg.getAction());
			maatCfg.setAuditTime(StringUtil.isEmpty(entity.getAuditTime()) ? cfg.getAuditTime():entity.getAuditTime());
			maatCfg.setIpRegionList(ipRegionList);
			maatCfg.setStrRegionList(strRegionList);
			maatCfg.setNumRegionList(numRegionList);
			maatCfg.setDigestRegionList(digestRegionList);
			maatCfg.setGroupRelationList(groupRelationList);
			maatCfg.setGroupNum(groupRelationList.size());
			maatCfg.setAreaIpRegionList(areaIpRegionList);
			maatCfg.setIsValid(entity.getIsValid());
			if(!StringUtil.isEmpty(userRegion)){
				maatCfg.setUserRegion(userRegion);
			}
			configCompileList.add(maatCfg);
		}
		if(isUpdateCfg) {
			if(!StringUtil.isEmpty(compileIds) && !StringUtil.isEmpty(entity.getTableName())) {
				commonPolicyDao.auditCfgBatch( entity.getTableName(), entity,compileIds,null);
			}
		}
		page.setList(list);
		if(page.isLastPage()){
			hasData = false;
		}
		if(!StringUtil.isEmpty(configCompileList.size())){
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(new Date());
			maatBean.setCreatorName(UserUtils.getUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.INSERT_ACTION);
			String json=gsonToJson(maatBean);
			//调用服务接口下发配置数据
			if(isUpdateCfg) {
				logger.info("配置批量下发："+json);
				//调用服务接口同步回调类配置
				ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
				if(result!=null){
					logger.info("配置批量下发响应信息："+result.getMsg());
				}
			}else {
				//调用服务接口配置全量更新
				isFinished = ((!hasData)&&lastServiceTag)?true:false;
				FileUtils.writeToFile("/home/ceiec/configSync/"+DateUtils.getDate("yyyy-MM-dd")+"/"+entity.getServiceId()+"_"+page.getPageNo()+"_"+System.currentTimeMillis()+".json", json, false);
				JSONObject result = ConfigServiceUtil.configSync(json,1,entity.getServiceId(),null,isFinished?"FINISHED":null);
			}
			
		}
		return hasData;
	}
	/**
	 * 单域配置批量下发
	 * @param cfgList
	 * @param userRegionList
	 * @param page
	 * @param entity
	 * @param list
	 * @param hasData
	 * @param isUpdateCfg 业务配置全部生效时需同步更新库表配置状态
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public boolean auditSingleMaatData(int cfgType,
										List<Map<String,Object>>userRegionList,
										Page<T> page,
										BaseCfg entity,
										List<T> list,
										boolean hasData,
										boolean isUpdateCfg) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		ToMaatBean maatBean;		
		MaatCfg maatCfg;
		List<MaatCfg> configCompileList;
		List<GroupCfg> groupRelationList;
		List<IpCfg> ipRegionList;
		List<StringCfg> strRegionList;
		List<NumBoundaryCfg> numRegionList;
		List<DigestCfg> digestRegionList;
		List<IpCfg> areaIpRegionList;
		
		maatBean = new ToMaatBean();		
		configCompileList = new ArrayList();
		List<Integer> ids = new ArrayList();
		//批量获取regionId,groupId
		List<Integer> regionIds = ConfigServiceUtil.getId(3, list.size());
		List<Integer> groupIds = ConfigServiceUtil.getId(2, list.size());
		IpPortCfg ipCfg = new IpPortCfg();
		BaseStringCfg strCfg = new BaseStringCfg();
		ComplexkeywordCfg complexStrCfg = new ComplexkeywordCfg();
		for(int i=0;i<list.size();i++){
			BaseCfg cfg = new BaseCfg();
			if(cfgType==1){
				BeanUtils.copyProperties(list.get(i), ipCfg);
			}else if(cfgType==2){
				BeanUtils.copyProperties(list.get(i), strCfg);
			}else if(cfgType==3){
				BeanUtils.copyProperties(list.get(i), complexStrCfg);
			}
			BeanUtils.copyProperties(list.get(i), cfg);
			cfg.setIsValid(entity.getIsValid());
			cfg.setAuditTime(StringUtil.isEmpty(entity.getAuditTime()) ? cfg.getAuditTime():entity.getAuditTime());
			ids.add(cfg.getCompileId());
			maatCfg = new MaatCfg();
			maatCfg.initDefaultValue();
			groupRelationList = new ArrayList();
			ipRegionList = new ArrayList();
			strRegionList = new ArrayList();
			numRegionList = new ArrayList();
			digestRegionList = new ArrayList();
			areaIpRegionList = new ArrayList();
			List list1 = new ArrayList();
			String userRegion = "";
			
			//处理自定义域
			if(userRegionList!=null){
				for(Map<String,Object> n:userRegionList){
					if(entity.getTableName().equals("app_topic_domain_cfg")){
						String domain = strCfg.getCfgKeywords();
						WebsiteDomainTopic websiteDomainTopic = new WebsiteDomainTopic();
						websiteDomainTopic.setDomain(domain);
						//查询domainId
						List<WebsiteDomainTopic> domainDict = appCfgDao.getDomainDict(websiteDomainTopic);
						Long domainId = domainDict.get(0).getId();
						userRegion += n.get("regionKey")+"="+domainId+Constants.USER_REGION_SPLIT;
					}else{
						Object userRegionPosition = n.get("userRegionPosition");
						if(userRegionPosition!=null){
							//通过反射机制获取自定义域字段值
							String regionColumn = n.get("regionColumn").toString();
							Class aClass = null;
							if(userRegionPosition.toString().equals(("0"))){
								aClass = BaseCfg.class;
							}else{
								if(cfgType==1){
									aClass = IpPortCfg.class;
								}else if(cfgType==2){
									aClass = BaseStringCfg.class;
								}else if(cfgType==3){
									aClass = ComplexkeywordCfg.class;
								}
							}
							Object value = "";
							Field field = aClass.getDeclaredField(regionColumn);
							field.setAccessible(true);
							if(cfgType==1){
								value = field.get(ipCfg);
							}else if(cfgType==2){
								value = field.get(strCfg);
							}else if(cfgType==3){
								value = field.get(complexStrCfg);
							}
							
							if(!StringUtil.isEmpty(value)){
								if(StringUtil.isEmpty(n.get("regionKey"))){
									userRegion = value.toString();
								}else{
									userRegion += n.get("regionKey")+"="+value+Constants.USER_REGION_SPLIT;
								}
							}
						}
					}
				}
				if(userRegion.endsWith(Constants.USER_REGION_SPLIT)){
					userRegion = userRegion.substring(0, userRegion.length()-1);
				}
			}
			if(cfgType==1){
				ipCfg.setRegionId(regionIds.get(0));
				regionIds.remove(0);
				ipCfg.setGroupId(groupIds.get(0));
				ipCfg.setIsValid(entity.getIsValid());
				ipCfg.setAuditTime(StringUtil.isEmpty(entity.getAuditTime()) ? cfg.getAuditTime():entity.getAuditTime());
				groupIds.remove(0);
				list1.add(ipCfg);
			}else if(cfgType==2){
				strCfg.setRegionId(regionIds.get(0));
				regionIds.remove(0);
				strCfg.setGroupId(groupIds.get(0));
				strCfg.setIsValid(entity.getIsValid());
				strCfg.setAuditTime(StringUtil.isEmpty(entity.getAuditTime()) ? cfg.getAuditTime():entity.getAuditTime());
				groupIds.remove(0);
				list1.add(strCfg);
			}else if(cfgType==3){
				complexStrCfg.setRegionId(regionIds.get(0));
				regionIds.remove(0);
				complexStrCfg.setGroupId(groupIds.get(0));
				complexStrCfg.setIsValid(entity.getIsValid());
				complexStrCfg.setAuditTime(StringUtil.isEmpty(entity.getAuditTime()) ? cfg.getAuditTime():entity.getAuditTime());
				groupIds.remove(0);
				list1.add(complexStrCfg);
			}
			
			if(list1.size()>0){
				if(cfgType==1){
					Map<String,List> map = cfgToMaatConvert(ipRegionList,list1,1,groupRelationList);
					groupRelationList=map.get("groupList");
					ipRegionList=map.get("dstList");
					if(map.get("numRegionList")!=null){
						numRegionList.addAll(map.get("numRegionList"));
					}
				}else if(cfgType==2||cfgType==3){
					Map<String,List> map = cfgToMaatConvert(strRegionList,list1,2,groupRelationList);
					groupRelationList=map.get("groupList");
					strRegionList=map.get("dstList");
				}
			}
			BeanUtils.copyProperties(cfg, maatCfg);
			maatCfg.setAction(cfg.getAction());
			maatCfg.setAuditTime(StringUtil.isEmpty(entity.getAuditTime()) ? cfg.getAuditTime():entity.getAuditTime());
			maatCfg.setIpRegionList(ipRegionList);
			maatCfg.setStrRegionList(strRegionList);
			maatCfg.setNumRegionList(numRegionList);
			maatCfg.setDigestRegionList(digestRegionList);
			maatCfg.setGroupRelationList(groupRelationList);
			maatCfg.setGroupNum(groupRelationList.size());
			maatCfg.setAreaIpRegionList(areaIpRegionList);
			maatCfg.setIsValid(entity.getIsValid());
			if(!StringUtil.isEmpty(userRegion)){
				maatCfg.setUserRegion(userRegion);
			}
			configCompileList.add(maatCfg);
		}
		if(isUpdateCfg) {
			if(!StringUtil.isEmpty(ids) && !StringUtil.isEmpty(entity.getTableName())) {
				commonPolicyDao.auditCfgBatch( entity.getTableName(), entity,ids,null);
			}
		}
		page.setList(list);
		if(page.isLastPage()){
			hasData = false;
		}
		if(!StringUtil.isEmpty(configCompileList.size())){
			maatBean.setConfigCompileList(configCompileList);
			maatBean.setAuditTime(new Date());
			maatBean.setCreatorName(UserUtils.getUser().getName());
			maatBean.setVersion(Constants.MAAT_VERSION);
			maatBean.setOpAction(Constants.INSERT_ACTION);
			String json=gsonToJson(maatBean);
			//调用服务接口下发配置数据
			if(isUpdateCfg) {
				logger.info("配置批量下发："+json);
				//调用服务接口同步回调类配置
				ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
				if(result!=null){
					logger.info("配置批量下发响应信息："+result.getMsg());
				}
			}else {
				//调用服务接口配置全量更新
				isFinished = ((!hasData)&&lastServiceTag)?true:false;
				FileUtils.writeToFile("/home/ceiec/configSync/"+DateUtils.getDate("yyyy-MM-dd")+"/"+entity.getServiceId()+"_"+page.getPageNo()+"_"+System.currentTimeMillis()+".json", json, false);
				JSONObject result = ConfigServiceUtil.configSync(json,1,entity.getServiceId(),null,isFinished?"FINISHED":null);
			}
			
		}
		return hasData;
	}
	/**
	 * 处理app ip特征（分组复用）配置
	 * 批量下发：【第一次下发为正常配置下发，非第一次下发为分组复用接口下发】
	 * @param serviceId
	 * @param request
	 * @param response
	 */
	public boolean auditAppIpData(List<Map<String,Object>>userRegionList,
			Page<T> page,
			BaseCfg entity,
			List<T> list,
			boolean hasData,
			boolean isUpdateCfg) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		
		ToMaatBean maatBean;		
		GroupReuseAddBean reuseMaatBean;
		MaatCfg maatCfg;
		List<MaatCfg> configCompileList;
		List<GroupCfg> groupRelationList;
		List<IpCfg> ipRegionList;
		List<StringCfg> strRegionList;
		List<NumBoundaryCfg> numRegionList;
		List<DigestCfg> digestRegionList;
		List<IpCfg> areaIpRegionList;
		
		maatBean = new ToMaatBean();
		reuseMaatBean = new GroupReuseAddBean();
		configCompileList = new ArrayList();
		List<Integer> regionIds= new ArrayList();//存储app ip域id
		List<Integer> compileIds= new ArrayList();//存储app compileId
		Map<Integer, Integer> compileGroups=new HashMap<>();
		compileGroups=entity.getCompileGroupMap();
		Integer isIssued=entity.getCompileIsIssued();//批量下发时使用，需要用来判断是否走分组复用接口
		
		//同一编译IP的分组
		Map<Integer,List<AppIpCfg>> dataMap=new HashMap<>();
		for(int i=0;i<list.size();i++){
			AppIpCfg cfg = new AppIpCfg();
			BeanUtils.copyProperties(list.get(i), cfg);
			cfg.setIsValid(entity.getIsValid());
			cfg.setAuditTime(StringUtil.isEmpty(entity.getAuditTime()) ? cfg.getAuditTime():entity.getAuditTime());
			cfg.setGroupId(compileGroups.get(cfg.getCompileId()));
			if(dataMap.containsKey(cfg.getCompileId())) {
				dataMap.get(cfg.getCompileId()).add(cfg);
			}else {
				List<AppIpCfg> ipList=new ArrayList<>();
				ipList.add(cfg);
				dataMap.put(cfg.getCompileId(), ipList);
			}
			if(isUpdateCfg) {
				if(!compileIds.contains(cfg.getCompileId())) {
					compileIds.add(cfg.getCompileId());
				}
				if(!StringUtil.isEmpty(cfg.getUserRegion1())) {
					regionIds.add(Integer.parseInt(cfg.getUserRegion1()));
				}
			}
		}
		for(List<AppIpCfg> entitys:dataMap.values()) {
			maatCfg = new MaatCfg();
			reuseMaatBean = new GroupReuseAddBean();
			maatCfg.initDefaultValue();
			groupRelationList = new ArrayList();
			ipRegionList = new ArrayList();
			strRegionList = new ArrayList();
			numRegionList = new ArrayList();
			digestRegionList = new ArrayList();
			areaIpRegionList = new ArrayList();

			/*if(configGroupInfo==null) {
				Integer groupId = ConfigServiceUtil.getId(2, 1).get(0);
				specificServiceCfg.setGroupId(groupId);
				configGroupInfo = new ConfigGroupInfo();
				configGroupInfo.setGroupId(specificServiceCfg.getGroupId());
				configGroupInfo.setGroupName(specificServiceCfg.getSpecServiceName());
				configGroupInfo.setIsIssued(0);
				configGroupInfo.setCompileId(entitys.get(0).getCompileId());
				configGroupInfo.setGroupType(1);
				configGroupInfoDao.insertConfigGroupInfo(configGroupInfo);
				//更新group_id
				specificServiceCfg.setOpTime(new Date());
				specificServiceCfgDao.update(specificServiceCfg);
			}*/
			
			if(isUpdateCfg && (isIssued.equals(1) || entity.getIsValid().equals(0))) {
				List<GroupReuseCfg> groupReuseList=new ArrayList<>();
				GroupReuseCfg groupReuseCfg=new GroupReuseCfg();
				/*groupReuseCfg.setUserRegion(userRegion);*/
				groupReuseCfg.setServiceId(entitys.get(0).getServiceId());
				ipRegionList.addAll(groupReuseCfgAddRemoveConvert(entitys,entity.getIsValid(),entitys.get(0).getGroupId()));
				groupReuseCfg.setIpRegionList(ipRegionList);
				groupReuseCfg.setStrRegionList(strRegionList);
				groupReuseCfg.setNumRegionList(numRegionList);
				groupReuseList.add(groupReuseCfg);
				reuseMaatBean.setGroupReuseCfgList(groupReuseList);
				reuseMaatBean.setAuditTime(StringUtil.isEmpty(entity.getAuditTime())?entitys.get(0).getAuditTime():entity.getAuditTime());
				reuseMaatBean.setCreatorName(entitys.get(0).getCurrentUser().getName());
				reuseMaatBean.setVersion(Constants.MAAT_VERSION);
				if(entity.getIsValid().equals(0)) {
					reuseMaatBean.setOpAction(Constants.UPDATE_ACTION);
				}else {
					reuseMaatBean.setOpAction(Constants.INSERT_ACTION);
				}
			}else {
				GroupCfg groupCfg=new GroupCfg();
				groupCfg.setCompileId(entitys.get(0).getCompileId());
				groupCfg.setGroupId(entitys.get(0).getGroupId());
				groupCfg.setIsValid(Constants.VALID_YES);
				groupCfg.setAuditTime(StringUtil.isEmpty(entity.getAuditTime())?entitys.get(0).getAuditTime():entity.getAuditTime());
				groupRelationList.add(groupCfg);
				BeanUtils.copyProperties(entitys.get(0), maatCfg);
				ipRegionList.addAll(groupReuseCfgAddRemoveConvert(entitys,Constants.VALID_YES,entitys.get(0).getGroupId()));
				maatCfg.setAction(entitys.get(0).getAction());
				maatCfg.setAuditTime(entitys.get(0).getAuditTime());
				maatCfg.setIpRegionList(ipRegionList);
				maatCfg.setStrRegionList(strRegionList);
				maatCfg.setNumRegionList(numRegionList);
				maatCfg.setDigestRegionList(digestRegionList);
				maatCfg.setGroupRelationList(groupRelationList);
				maatCfg.setGroupNum(groupRelationList.size());
				maatCfg.setAreaIpRegionList(areaIpRegionList);
				maatCfg.setIsValid(entitys.get(0).getIsValid());
				// 设置APP自定义域
				String userRegion = "APP_ID=" + entitys.get(0).getAppCode()/*+Constants.USER_REGION_SPLIT+"ASEV_ID="+businessType*/;
				maatCfg.setUserRegion(userRegion);
				configCompileList.add(maatCfg);
				maatBean.setConfigCompileList(configCompileList);
				maatBean.setAuditTime(entitys.get(0).getAuditTime());
				maatBean.setCreatorName(entitys.get(0).getCurrentUser().getName());
				maatBean.setVersion(Constants.MAAT_VERSION);
				maatBean.setOpAction(Constants.INSERT_ACTION);
			}
			
			
		}
		if(isUpdateCfg) {
			if(!StringUtil.isEmpty(regionIds)) {
				commonPolicyDao.auditCfgBatch(entity.getTableName(), entity, regionIds, null);
			}
			if(isIssued.equals(0) && entity.getIsValid().equals(1)) {//group第一次下发需要修改状态为已下发
				if(!StringUtil.isEmpty(compileIds)) {
					commonPolicyDao.auditCfgBatch("config_group_info", entity, compileIds, null);
				}
				
			}
		}
		page.setList(list);
		if(page.isLastPage()){
			hasData = false;
		}
		if(!StringUtil.isEmpty(configCompileList.size())){
			String json=gsonToJson(maatBean);
			//调用服务接口下发配置数据
			if(isUpdateCfg) {
				
				if(isIssued.equals(1)) {//已下发过的compile或配置取消直接走ip复用接口
					json=gsonToJson(reuseMaatBean);
					if(entity.getIsValid().equals(0)) {
						logger.info("app ip批量失效："+json);
						//调用服务接口同步回调类配置
						ToMaatResult result = ConfigServiceUtil.put(json, 3);
						if(result!=null){
							logger.info("app ip批量失效响应信息："+result.getMsg());
						}
					}else {
						logger.info("app ip批量生效："+json);
						//调用服务接口同步回调类配置
						ToMaatResult result = ConfigServiceUtil.postGroupReuseSources(json);
						if(result!=null){
							logger.info("app ip批量生效响应信息："+result.getMsg());
						}
					}
					
				}else {//未下发过的compile直接走正常maat配置接口
					logger.info("配置批量下发："+json);
					//调用服务接口同步回调类配置
					ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
					if(result!=null){
						logger.info("配置批量下发响应信息："+result.getMsg());
					}
				}
			}else {
				//调用服务接口配置全量更新
				isFinished = ((!hasData)&&lastServiceTag)?true:false;
				FileUtils.writeToFile("/home/ceiec/configSync/"+DateUtils.getDate("yyyy-MM-dd")+"/"+entity.getServiceId()+"_"+page.getPageNo()+"_"+System.currentTimeMillis()+".json", json, false);
				JSONObject result = ConfigServiceUtil.configSync(json,1,entity.getServiceId(),null,isFinished?"FINISHED":null);
			}
			
		}
		return hasData;
	}

	/**
	 * 处理回调类业务配置
	 * @param serviceId
	 * @param request
	 * @param response
	 * @throws ClassNotFoundException 
	 */
	public void handleCallbackData(String className,
			Page<BaseCfg> page,BaseCfg entity,HttpServletRequest request,HttpServletResponse response,
			boolean isUpdateCfg) throws ClassNotFoundException{
		
		boolean hasData = true;
		while(hasData){
			entity.setPage(page);
			hasData=auditCallbackData(className,page,entity,hasData,isUpdateCfg);
			if(hasData){
				page.setPageNo(page.getNext());
			}
		}
		
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
	public boolean auditCallbackData(String className,
									Page<BaseCfg> page,BaseCfg entity,
									boolean hasData,
									boolean isUpdateCfg) throws ClassNotFoundException{
		ToMaatBean maatBean;		
		MaatCfg maatCfg;
		List<MaatCfg> configCompileList;
		List<GroupCfg> groupRelationList;
		List<IpCfg> ipRegionList;
		List<StringCfg> strRegionList;
		List<NumBoundaryCfg> numRegionList;
		List<DigestCfg> digestRegionList;
		List<IpCfg> areaIpRegionList;
		
		maatBean = new ToMaatBean();		
		configCompileList = new ArrayList();
		List list = new ArrayList();
		List newList = new ArrayList();
		List ids = new ArrayList();
		if(entity.getServiceId().equals(3)){//ip drop回调类配置用了主表和子表关系
			list = configSynchronizationDao.getCfgIndexList(entity);
			if(!StringUtil.isEmpty(list)){
				List<Integer> compileIds = Lists.newArrayList();
				for(int i=0;i<list.size();i++){
					CfgIndexInfo cfg = (CfgIndexInfo) list.get(i);
					compileIds.add(cfg.getCompileId());
				}
				List subList = configSynchronizationDao.getIpDropList("ip_port_cfg", compileIds);
				for(int i=0;i<subList.size();i++){
					IpPortCfg cfg = (IpPortCfg) subList.get(i);
					newList.add(convertCallBackIp(cfg,cfg.getGroupId()));
				}
			}
			
		}else{
			if(className.equals("AvFileSampleCfg")){
				list = configSynchronizationDao.getAvFileCfgList(entity);
				newList.addAll(list);
			}else if(className.equals("AvSignSampleCfg")){
				list = configSynchronizationDao.getAvSignCfgList(entity);
				newList.addAll(list);
			}else if(className.equals("PxyObjKeyring")){
				list = configSynchronizationDao.getPxyObjKeyringCfgList(entity);
				for(int i=0;i<list.size();i++){
					PxyObjKeyring cfg = (PxyObjKeyring) list.get(i);
					newList.add(convertCallBackProxyObjKeyring(cfg));
				}
			}else if(className.equals("PxyObjTrustedCaCert")){
				//下发cert配置时，需绑定下发crl配置
				list = configSynchronizationDao.getPxyObjTrustedCertCfgList(entity);
				for(int i=0;i<list.size();i++){
					PxyObjTrustedCaCert cfg = (PxyObjTrustedCaCert) list.get(i);
					newList.addAll(convertCallBackProxyObjTrustedCa(cfg,null));
					ids.add(cfg.getCompileId());
				}
				if(!StringUtil.isEmpty(ids)){
					list=new ArrayList<>();
					list=configSynchronizationDao.getPxyObjTrustedCrlCfgListByCertId(ids);
					for(int i=0;i<list.size();i++){
						PxyObjTrustedCaCrl cfg = (PxyObjTrustedCaCrl) list.get(i);
						newList.addAll(convertCallBackProxyObjTrustedCa(null,cfg));
					}
				}
				
			}else if(className.equals("PxyObjTrustedCaCrl")){
				ids=new ArrayList<>();
				//只允许单独下发certId为空或0的crl配置
				list = configSynchronizationDao.getPxyObjTrustedCrlCfgList(entity);
				for(int i=0;i<list.size();i++){
					PxyObjTrustedCaCrl cfg = (PxyObjTrustedCaCrl) list.get(i);
					newList.addAll(convertCallBackProxyObjTrustedCa(null,cfg));
				}
				
			}else if(className.equals("DnsResStrategy")){
				list = configSynchronizationDao.getDnsStrategyList(entity);
				for(int i=0;i<list.size();i++){
					DnsResStrategy cfg = (DnsResStrategy) list.get(i);
					newList.add(convertCallBackDnsResStrategy(cfg));
				}
			}else if(className.equals("DnsIpCfg")){
				list = configSynchronizationDao.getDnsIpCfgList(entity);
				for(int i=0;i<list.size();i++){
					DnsIpCfg cfg = (DnsIpCfg) list.get(i);
					newList.add(convertCallBackIp(cfg,cfg.getDnsStrategyId()));
				}
			}else if(className.equals("IpPortCfg")){
				list = configSynchronizationDao.getIpPortListByService(entity);
				for(int i=0;i<list.size();i++){
					IpPortCfg cfg = (IpPortCfg) list.get(i);
					newList.add(convertCallBackIp(cfg,cfg.getGroupId()));
				}
			}
		}
		
		page.setList(list);
		if(page.isLastPage()){
			hasData = false;
		}
		if(newList.size()>0){
			//调用服务接口下发配置数据
			String json=gsonToJson(newList);
			if(isUpdateCfg) {
				logger.info("配置批量下发："+json);
				//调用服务接口同步回调类配置
				ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
				if(result!=null){
					logger.info("配置批量下发响应信息："+result.getMsg());
				}
			}else {
				FileUtils.writeToFile("/home/ceiec/configSync/"+DateUtils.getDate("yyyy-MM-dd")+"/"+entity.getServiceId()+"_"+page.getPageNo()+"_"+System.currentTimeMillis()+".json", json, false);
				//调用服务接口同步回调类配置
				isFinished = ((!hasData)&&lastServiceTag)?true:false;
				JSONObject result = ConfigServiceUtil.configSync(json,2,entity.getServiceId(),entity.getTableName(),isFinished?"FINISHED":null);
			}
		}
		
		return hasData;
	}
	/**
	 * 配置域转换
	 * @param ipList
	 * @param strList
	 * @param complexStrList
	 * @param numList
	 * @param fileList
	 * @param regionIds
	 * @param groupIds
	 * @param entity
	 * @param compileId
	 * @param auditTime
	 * @param groupRelationList
	 * @param ipRegionList
	 * @param strRegionList
	 * @param numRegionList
	 * @param digestRegionList
	 * @param areaIpRegionList
	 * @param userRegion
	 */
	public String configCovert(List<IpPortCfg> ipList,List<BaseStringCfg> strList,List<ComplexkeywordCfg> complexStrList,
			List<com.nis.domain.configuration.NumBoundaryCfg > numList,
			List<FileDigestCfg> fileList,List<Integer> regionIds,List<Integer> groupIds,BaseCfg entity,
			Integer compileId,Date auditTime,List<GroupCfg> groupRelationList,
			List<IpCfg> ipRegionList,
			List<StringCfg> strRegionList,
			List<NumBoundaryCfg> numRegionList,
			List<DigestCfg> digestRegionList,
			List<IpCfg> areaIpRegionList,String userRegion){
		List list1 = new ArrayList();
		List<BaseStringCfg> list2 = new ArrayList();
		List<ComplexkeywordCfg> list3 = new ArrayList();
		List<com.nis.domain.configuration.NumBoundaryCfg> list4 = new ArrayList();
		List<FileDigestCfg> list5 = new ArrayList();
		if(ipList.size()>0){
			for(int index=0;index<ipList.size();index++){
				IpPortCfg ip = ipList.get(index);
				if(ip.getCompileId().equals(compileId)){
					ip.setRegionId(regionIds.get(0));
					regionIds.remove(0);
					ip.setGroupId(groupIds.get(0));
					groupIds.remove(0);
					ip.setIsValid(entity.getIsValid());
					ip.setAuditTime(auditTime);
					list1.add(ip);
				}
			}
		}
		for(int index=0;index<strList.size();index++){
			BaseStringCfg str = strList.get(index);
			if(str.getCompileId().equals(compileId)){
				str.setRegionId(regionIds.get(0));
				regionIds.remove(0);
				str.setGroupId(groupIds.get(0));
				groupIds.remove(0);
				str.setIsValid(entity.getIsValid());
				str.setAuditTime(auditTime);
				list2.add(str);
				if(entity.getServiceId().equals(513)||entity.getServiceId().equals(515)){
					if(userRegion.equals("")){
						userRegion += Constants.USERREGION_DOMAIN_STR+"="+str.getCfgKeywords();
					}else{
						userRegion += Constants.USER_REGION_SPLIT+Constants.USERREGION_DOMAIN_STR+"="+str.getCfgKeywords();
					}
				}
			}
		}
		for(int index=0;index<complexStrList.size();index++){
			ComplexkeywordCfg str = complexStrList.get(index);
			if(str.getCompileId().equals(compileId)){
				str.setRegionId(regionIds.get(0));
				regionIds.remove(0);
				str.setGroupId(groupIds.get(0));
				groupIds.remove(0);
				str.setIsValid(entity.getIsValid());
				str.setAuditTime(auditTime);
				list3.add(str);
				if(entity.getServiceId().equals(129) && str.getDistrict()!=null){//http监测
					String dictValue = DictUtils.getDictCode(Constants.HTTP_HEADER_DICT_MODULE, str.getDistrict());
					if(StringUtil.isEmpty(dictValue) || dictValue.equals("默认")){
						if(userRegion.equals("")){
							userRegion += Constants.HTTP_HEADER_USER_REGION_KEY+"="+str.getDistrict();
						}else{
							userRegion += Constants.USER_REGION_SPLIT+Constants.HTTP_HEADER_USER_REGION_KEY+"="+str.getDistrict();
						}
					}
				}
			}
		}
		for(int index=0;index<numList.size();index++){
			com.nis.domain.configuration.NumBoundaryCfg num = numList.get(index);
			if(num.getCompileId().equals(compileId)){
				num.setRegionId(regionIds.get(0));
				regionIds.remove(0);
				num.setGroupId(groupIds.get(0));
				groupIds.remove(0);
				num.setIsValid(entity.getIsValid());
				num.setAuditTime(auditTime);
				list4.add(num);
			}
		}
		for(int index=0;index<fileList.size();index++){
			FileDigestCfg file = fileList.get(index);
			if(file.getCompileId().equals(compileId)){
				file.setRegionId(regionIds.get(0));
				regionIds.remove(0);
				file.setGroupId(groupIds.get(0));
				groupIds.remove(0);
				file.setIsValid(entity.getIsValid());
				file.setAuditTime(auditTime);
				list5.add(file);
			}
		}
		if(list1.size()>0){
			ipList.removeAll(list1);
			Map<String,List> map = cfgToMaatConvert(ipRegionList,list1,1,groupRelationList);
			groupRelationList=map.get("groupList");
			ipRegionList=map.get("dstList");
			if(map.get("numRegionList")!=null){
				numRegionList.addAll(map.get("numRegionList"));
			}
		}
		if(list2.size()>0){
			strList.removeAll(list2);
			Map<String,List> map = cfgToMaatConvert(strRegionList,list2,2,groupRelationList);
			groupRelationList=map.get("groupList");
			strRegionList=map.get("dstList");
		}
		if(list3.size()>0){
			complexStrList.removeAll(list3);
			Map<String,List> map = cfgToMaatConvert(strRegionList,list3,3,groupRelationList);
			groupRelationList=map.get("groupList");
			strRegionList=map.get("dstList");
		}
		if(list4.size()>0){
			numList.removeAll(list4);
			Map<String,List> map = cfgToMaatConvert(numRegionList,list4,4,groupRelationList);
			groupRelationList=map.get("groupList");
			numRegionList=map.get("dstList");
		}
		if(list5.size()>0){
			fileList.removeAll(list5);
			Map<String,List> map = cfgToMaatConvert(digestRegionList,list5,5,groupRelationList);
			groupRelationList=map.get("groupList");
			digestRegionList=map.get("dstList");
		}
		return userRegion;
	}
}
