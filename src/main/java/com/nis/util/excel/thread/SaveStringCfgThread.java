package com.nis.util.excel.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.beust.jcommander.internal.Lists;
import com.nis.domain.FunctionRegionDict;
import com.nis.domain.FunctionServiceDict;
import com.nis.domain.configuration.AppDomainCfg;
import com.nis.domain.configuration.AppPolicyCfg;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.maat.MaatCfg;
import com.nis.domain.maat.MaatCfg.DigestCfg;
import com.nis.domain.maat.MaatCfg.GroupCfg;
import com.nis.domain.maat.MaatCfg.IpCfg;
import com.nis.domain.maat.MaatCfg.NumBoundaryCfg;
import com.nis.domain.maat.MaatCfg.StringCfg;
import com.nis.domain.maat.ToMaatBean;
import com.nis.domain.maat.ToMaatResult;
import com.nis.domain.specific.SpecificServiceCfg;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.Constants;
import com.nis.util.DictUtils;
import com.nis.util.StringUtil;
import com.nis.web.dao.configuration.AppCfgDao;
import com.nis.web.dao.configuration.IpCfgDao;
import com.nis.web.dao.configuration.StringCfgDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.BaseService;
import com.nis.web.service.SpringContextHolder;

public class SaveStringCfgThread implements Callable<Throwable>{
	private Logger logger=LoggerFactory.getLogger(getClass());
	private BlockingQueue<BaseStringCfg<?>> stringCfgs;
	private FunctionServiceDict serviceDict;
	private FunctionRegionDict regionDict;
	private Integer requestId;
	private SpecificServiceCfg specificServiceCfg;
	private String specServiceId;
	private String behaviorId;
	private String asnIpGroup;
	private boolean audit;//是否下发
	private static final SqlSessionFactory sqlSessionFactory=SpringContextHolder.getBean(SqlSessionFactory.class);
	public SaveStringCfgThread(FunctionServiceDict serviceDict,FunctionRegionDict regionDict,Integer requestId,BlockingQueue<BaseStringCfg<?>> stringCfgs,boolean audit) {
		this.serviceDict=serviceDict;
		this.regionDict=regionDict;
		this.stringCfgs=stringCfgs;
		this.requestId=requestId;
		this.audit=audit;
	}
	@Override
	public Throwable call() {
		// TODO Auto-generated method stub
		try {
			FunctionRegionDict appRegion = null;
			if(serviceDict!=null) {
				String regionCode = serviceDict.getRegionCode();
				for (String code : regionCode.split(",")) {
					if (StringUtils.isNotBlank(code)) {
						FunctionRegionDict d = DictUtils.getFunctionRegionDictByRegionCode(serviceDict.getFunctionId(),
								Integer.parseInt(code));
						if (d != null && d.getConfigServiceType() != null
								&& d.getConfigServiceType().equals("app_policy")) {
							appRegion = d;
							break;
						}
					}

				}
			}
			DataSourceTransactionManager transactionManager=(DataSourceTransactionManager)SpringContextHolder.getBean("transactionManager");
			List<BaseStringCfg> _stringCfgs=Lists.newArrayList(Constants.MAAT_JSON_SEND_SIZE);
			List<AppPolicyCfg> appPolicyCfgs = new ArrayList<AppPolicyCfg>();
			List<CfgIndexInfo> cfgIndexInfos = new ArrayList<CfgIndexInfo>();
			while(!stringCfgs.isEmpty()) {
				//开启一个新事物
				DefaultTransactionDefinition def = new DefaultTransactionDefinition();
				def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 事物隔离级别，开启新事务，这样会比较安全些。
				TransactionStatus status = transactionManager.getTransaction(def); // 获得事务状态
				try {
					stringCfgs.drainTo(_stringCfgs, Constants.MAAT_JSON_SEND_SIZE);
					if(_stringCfgs.size()>0) {
						List<Integer> compileIds=Lists.newArrayList();
						List<Integer> groupIds=Lists.newArrayList();
						List<Integer> regionIds=Lists.newArrayList();
						try {
							 compileIds = ConfigServiceUtil.getId(1,_stringCfgs.size());
							 if(audit) {
								 groupIds = ConfigServiceUtil.getId(2,_stringCfgs.size());
								 regionIds = ConfigServiceUtil.getId(3,_stringCfgs.size());
							 }
						} catch (Exception e) {
							e.printStackTrace();
							logger.info("获取编译ID出错");
							throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+e.getMessage());
						}
						int ind=0;
						Date date=new Date();
						for (BaseStringCfg cfg : _stringCfgs) {
							cfg.setAction(serviceDict.getAction());
							cfg.setCfgRegionCode(regionDict.getConfigRegionCode());
							cfg.setCfgType(regionDict.getConfigRegionValue());
							cfg.setCreateTime(date);
							cfg.setCreatorId(UserUtils.getUser().getId());
							cfg.setDoLog(1);
							cfg.setFunctionId(regionDict.getFunctionId());
							if(audit){
								cfg.setIsAudit(Constants.AUDIT_YES);
								cfg.setIsValid(Constants.VALID_YES);
								cfg.setAuditorId(UserUtils.getUser().getId());
								cfg.setAuditTime(date);
								if(groupIds!=null&&groupIds.size()==_stringCfgs.size()) {
								cfg.setGroupId(groupIds.get(ind));
								}
								if(regionIds!=null&&regionIds.size()==_stringCfgs.size()) {
								cfg.setRegionId(regionIds.get(ind));
								}
							}else {
								cfg.setIsAudit(Constants.AUDIT_NOT_YET);
								cfg.setIsValid(Constants.VALID_NO);
							}
							
							cfg.setIsAreaEffective(0);
							cfg.setAttribute("0");
							cfg.setClassify("0");
							cfg.setLable("0");
							cfg.setRequestId(StringUtil.isEmpty(requestId) ? 0 : requestId);
							cfg.setServiceId(serviceDict.getServiceId());
							if(compileIds!=null&&compileIds.size()==_stringCfgs.size()) {
								cfg.setCompileId(compileIds.get(ind));
							}
							if(specificServiceCfg!=null) {
								cfg.setAppCode(specificServiceCfg.getSpecServiceCode());
								cfg.setSpecServiceId(specificServiceCfg.getSpecServiceId());
								if(StringUtils.isNotBlank(behaviorId))
								cfg.setBehavCode(Integer.parseInt(behaviorId));
							}
							if (serviceDict.getServiceId().intValue() == 33
									|| serviceDict.getServiceId().intValue() == 145
									|| serviceDict.getServiceId().intValue() == 35
									|| serviceDict.getServiceId().intValue() == 147
									|| serviceDict.getServiceId().intValue() == 36
									|| serviceDict.getServiceId().intValue() == 148) {
								if (appRegion != null) {
									AppPolicyCfg appPolicyCfg = new AppPolicyCfg();
									BeanUtils.copyProperties(cfg, appPolicyCfg,new String[] {"cfgId"});
									appPolicyCfg.setMatchMethod(0);
									appPolicyCfg.setIsHexbin(0);
									appPolicyCfg.setCfgType(appRegion.getConfigRegionValue());
									appPolicyCfg.setCfgRegionCode(appRegion.getConfigRegionCode());
									if (StringUtils.isNotBlank(behaviorId)) {
										appPolicyCfg.setExprType(1);
									} else {
										appPolicyCfg.setExprType(0);
									}
									appPolicyCfgs.add(appPolicyCfg);
								}
							} else {
								if(!regionDict.getFunctionId().equals(403)) {
									CfgIndexInfo cfgIndexInfo = new CfgIndexInfo();
									BeanUtils.copyProperties(cfg, cfgIndexInfo, new String[] {"cfgId"});
									cfgIndexInfos.add(cfgIndexInfo);
								}
								
							}
							ind++;
						}
						this.saveAndSend(_stringCfgs, cfgIndexInfos, appPolicyCfgs);
						cfgIndexInfos.clear();
						appPolicyCfgs.clear();
						_stringCfgs.clear();
						transactionManager.commit(status);
					}
					
				}catch (Throwable e) {
					transactionManager.rollback(status);
					// TODO: handle exception
					return e;
				}
				
			}	
			
		}catch (Throwable e) {
			// TODO: handle exception
			return e;
		}
		
		return null;
	}
	
	public String getBehaviorId() {
		return behaviorId;
	}
	public void setBehaviorId(String behaviorId) {
		this.behaviorId = behaviorId;
	}
	public String getAsnIpGroup() {
		return asnIpGroup;
	}
	public void setAsnIpGroup(String asnIpGroup) {
		this.asnIpGroup = asnIpGroup;
	}
	public SpecificServiceCfg getSpecificServiceCfg() {
		return specificServiceCfg;
	}
	public void setSpecificServiceCfg(SpecificServiceCfg specificServiceCfg) {
		this.specificServiceCfg = specificServiceCfg;
	}
	public FunctionServiceDict getServiceDict() {
		return serviceDict;
	}
	public void setServiceDict(FunctionServiceDict serviceDict) {
		this.serviceDict = serviceDict;
	}
	public Integer getRequestId() {
		return requestId;
	}
	public void setRequestId(Integer requestId) {
		this.requestId = requestId;
	}
	public String getSpecServiceId() {
		return specServiceId;
	}
	public void setSpecServiceId(String specServiceId) {
		this.specServiceId = specServiceId;
	}
	public void saveAndSend(List<BaseStringCfg> _stringCfgs,List<CfgIndexInfo> cfgIndexInfos, List<AppPolicyCfg> appPolicyCfgs) {
		logger.warn("saveAndSend start");
		long start=System.currentTimeMillis();
		if(_stringCfgs.size()>0) {
			SqlSession batchSqlSession = null;
			List<MaatCfg> configCompileList = new ArrayList();
			try{
				batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
				for(int index = 0; index < _stringCfgs.size();index++){
					if (regionDict.getDictId().intValue() == 167) {
						BaseStringCfg<?> cfg = _stringCfgs.get(index);
		                cfg.setTableName("intercept_pkt_bin");
						((StringCfgDao) batchSqlSession.getMapper(StringCfgDao.class)).saveStringCfgBatch(cfg);
					}else
					// http url
					if (regionDict.getDictId().intValue() == 9||regionDict.getDictId().intValue() == 15 || regionDict.getDictId().intValue() == 596) {
						BaseStringCfg<?> cfg = _stringCfgs.get(index);
		                cfg.setTableName("http_url_cfg");
						((StringCfgDao) batchSqlSession.getMapper(StringCfgDao.class)).saveStringCfgBatch(cfg);
					}else
					// FTP url/content
					if (regionDict.getDictId().intValue() == 36||regionDict.getDictId().intValue() == 37) {
						BaseStringCfg<?> cfg = _stringCfgs.get(index);
		                cfg.setTableName("ftp_keyword_cfg");
						((StringCfgDao) batchSqlSession.getMapper(StringCfgDao.class)).saveStringCfgBatch(cfg);
					}else
					// P2P eMule Keyword
					if(regionDict.getDictId().intValue() == 158){
						BaseStringCfg<?> cfg = _stringCfgs.get(index);
		                cfg.setTableName("p2p_keyword_cfg");
						((StringCfgDao) batchSqlSession.getMapper(StringCfgDao.class)).saveStringCfgBatch(cfg);
					}else
					// P2P File Marking
					if(regionDict.getDictId().intValue() == 159){
						BaseStringCfg<?> cfg = _stringCfgs.get(index);
		                cfg.setTableName("p2p_hash_cfg");
						((StringCfgDao) batchSqlSession.getMapper(StringCfgDao.class)).saveStringCfgBatch(cfg);
					}else
					// BGP AS
					if (regionDict.getFunctionId().equals(61)) {
						BaseStringCfg<?> cfg = _stringCfgs.get(index);
		                cfg.setTableName("ntc_bgp_as_cfg");
						((StringCfgDao) batchSqlSession.getMapper(StringCfgDao.class)).saveStringCfgBatch(cfg);
					}else
					// SSL SNI/SAN/CN
					if (regionDict.getFunctionId().intValue() == 34) {
						BaseStringCfg<?> cfg = _stringCfgs.get(index);
		                cfg.setTableName("ssl_keyword_cfg");
						((StringCfgDao) batchSqlSession.getMapper(StringCfgDao.class)).saveStringCfgBatch(cfg);
					}else
					// HTTP 请求/应答内容
					if(regionDict.getDictId().intValue()==12||regionDict.getDictId().intValue()==13||regionDict.getDictId().intValue()==601||regionDict.getDictId().intValue()==602) {
						BaseStringCfg<?> cfg = _stringCfgs.get(index);
		                cfg.setTableName("http_body_cfg");
						((StringCfgDao) batchSqlSession.getMapper(StringCfgDao.class)).saveStringCfgBatch(cfg);
					}else
					// 流媒体协议
					if(regionDict.getDictId().intValue()==25){
						BaseStringCfg<?> cfg = _stringCfgs.get(index);
		                cfg.setTableName("av_cont_url_cfg");
						((StringCfgDao) batchSqlSession.getMapper(StringCfgDao.class)).saveStringCfgBatch(cfg);
					}else
					// Voip Account
					if(regionDict.getDictId().intValue()==16){
						BaseStringCfg<?> cfg = _stringCfgs.get(index);
		                cfg.setTableName("av_voip_account_cfg");
						((StringCfgDao) batchSqlSession.getMapper(StringCfgDao.class)).saveStringCfgBatch(cfg);
					}else
					// APP域名特征
					if(regionDict.getDictId().intValue()==64){
						AppDomainCfg cfg=new AppDomainCfg();
						BeanUtils.copyProperties(_stringCfgs.get(index), cfg);
						((AppCfgDao) batchSqlSession.getMapper(AppCfgDao.class)).insertAppDomainCfgBatch(cfg);
					}
					if (cfgIndexInfos != null && cfgIndexInfos.size() > 0) {
						CfgIndexInfo cfgIndexInfo = cfgIndexInfos.get(index);
						((IpCfgDao) batchSqlSession.getMapper(IpCfgDao.class)).saveCfgIndexForBatch(cfgIndexInfo);
					}
					if (appPolicyCfgs != null && appPolicyCfgs.size() > 0) {
						SpringContextHolder.getBean(AppCfgDao.class).insertAppPolicyCfgForBatch(appPolicyCfgs.get(index));
					}
					if(audit) {
						List<GroupCfg> groupRelationList = new ArrayList();
						List<IpCfg> ipRegionList = new ArrayList();
						List<StringCfg> strRegionList = new ArrayList();
						List<NumBoundaryCfg> numRegionList = new ArrayList();
						List<DigestCfg> digestRegionList = new ArrayList();
						List<IpCfg> areaIpRegionList = new ArrayList();
						GroupCfg group = new GroupCfg();
						StringCfg cfg = new StringCfg();
						BeanUtils.copyProperties(_stringCfgs.get(index), cfg);
						group.setGroupId(_stringCfgs.get(index).getGroupId());
						group.setCompileId(_stringCfgs.get(index).getCompileId());
						group.setAuditTime(_stringCfgs.get(index).getAuditTime());
						group.setIsValid(_stringCfgs.get(index).getIsValid());
						groupRelationList.add(group);
						cfg.setGroupId(group.getGroupId());
						//处理配置关键字转译
						cfg.setCfgKeywords(BaseService.keywordsEscape(cfg.getCfgKeywords()));
						//增强字符串转换
						cfg.setDistrict(BaseService.keywordsEscape(cfg.getDistrict()));
						strRegionList.add(cfg);
						MaatCfg maatCfg = new MaatCfg();
						maatCfg.initDefaultValue();
						BeanUtils.copyProperties(_stringCfgs.get(index), maatCfg);
						maatCfg.setAction(_stringCfgs.get(index).getAction());
						maatCfg.setAuditTime(_stringCfgs.get(index).getAuditTime());
						maatCfg.setIpRegionList(ipRegionList);
						maatCfg.setStrRegionList(strRegionList);
						maatCfg.setNumRegionList(numRegionList);
						maatCfg.setDigestRegionList(digestRegionList);
						maatCfg.setGroupRelationList(groupRelationList);
						maatCfg.setGroupNum(groupRelationList.size());
						maatCfg.setAreaIpRegionList(areaIpRegionList);
						maatCfg.setIsValid(_stringCfgs.get(index).getIsValid());
						configCompileList.add(maatCfg);
					}
					
				}
				batchSqlSession.commit();
			}finally {
				if(batchSqlSession != null){
					batchSqlSession.close();
				}
			}
			
			if(audit&&configCompileList.size()>0) {
				
				ToMaatBean maatBean = new ToMaatBean();	
				maatBean.setConfigCompileList(configCompileList);
				maatBean.setAuditTime(new Date());
				maatBean.setCreatorName(UserUtils.getUser().getName());
				maatBean.setVersion(Constants.MAAT_VERSION);
				maatBean.setOpAction(Constants.INSERT_ACTION);
				//调用服务接口下发配置数据
				String json=BaseService.gsonToJson(maatBean);
				logger.info("http配置下发配置参数："+json);
				//调用服务接口下发配置
				ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
				logger.info("http配置下发响应信息："+result.getMsg());
				
			}
		}
		long end=System.currentTimeMillis();
		logger.warn("saveAndSend finish,cost:"+(end-start));
	}
}
