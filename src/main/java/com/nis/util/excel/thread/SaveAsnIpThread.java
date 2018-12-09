package com.nis.util.excel.thread;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

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
import com.nis.domain.basics.AsnIpCfg;
import com.nis.domain.basics.Varibles;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.specific.ConfigGroupInfo;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.AsnCacheUtils;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.Constants;
import com.nis.util.StringUtil;
import com.nis.web.dao.basics.AsnIpCfgDao;
import com.nis.web.dao.specific.ConfigGroupInfoDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.SpringContextHolder;
import com.nis.web.service.basics.AsnIpCfgService;

public class SaveAsnIpThread implements Callable<Throwable>{
	private Logger logger=LoggerFactory.getLogger(getClass());
	private BlockingQueue<BaseIpCfg> ipPortCfgs;
	private FunctionServiceDict serviceDict;
	private FunctionRegionDict regionDict;
	private Map<Long,Boolean> fullMap;
	private List<Map<Long,Integer>> asnNoMaps;
	private Integer requestId;
	private AsnIpCfgDao asnIpCfgDao;
	private ConfigGroupInfoDao configGroupInfoDao;
//	private AsnIpCfgService asnIpCfgService;
	public SaveAsnIpThread(FunctionServiceDict serviceDict,FunctionRegionDict regionDict,Integer requestId,BlockingQueue<BaseIpCfg> ipPortCfgs) {
		this.serviceDict=serviceDict;
		this.regionDict=regionDict;
		this.ipPortCfgs=ipPortCfgs;
		this.requestId=requestId;
		this.asnIpCfgDao=SpringContextHolder.getBean(AsnIpCfgDao.class);
		this.configGroupInfoDao=SpringContextHolder.getBean(ConfigGroupInfoDao.class);
	}
	@Override
	public Throwable call() {
		// TODO Auto-generated method stub
		try {
		
			DataSourceTransactionManager transactionManager=(DataSourceTransactionManager)SpringContextHolder.getBean("transactionManager");

			List<AsnIpCfg> asnIpCfgs=Lists.newArrayList(Constants.MAAT_JSON_SEND_SIZE);
			List<BaseIpCfg> _ipPortCfgs=Lists.newArrayList(Constants.MAAT_JSON_SEND_SIZE);
			while(!ipPortCfgs.isEmpty()) {
				//开启一个新事物
				DefaultTransactionDefinition def = new DefaultTransactionDefinition();
				def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 事物隔离级别，开启新事务，这样会比较安全些。
				TransactionStatus status = transactionManager.getTransaction(def); // 获得事务状态
				try {
					Date date=new Date();
					int size=ipPortCfgs.drainTo(_ipPortCfgs,Constants.MAAT_JSON_SEND_SIZE);
					List<Integer> regionIds=Lists.newArrayList();
					if(_ipPortCfgs.size()>0) {
						try {
							regionIds = ConfigServiceUtil.getId(3,_ipPortCfgs.size());
						} catch (Exception e) {
							e.printStackTrace();
							logger.info("获取域ID出错");
							throw new MaatConvertException("<spring:message code=\"request_service_failed\"/>:"+e.getMessage());
						}
						int ind=0;
						for (BaseIpCfg cfg : _ipPortCfgs) {
							AsnIpCfg _cfg=new AsnIpCfg();
							BeanUtils.copyProperties(cfg, _cfg,new String[] {"cfgId"});
							_cfg.setTableName(AsnIpCfg.getTablename());
							_cfg.setAction(0);
							_cfg.setCfgRegionCode(regionDict.getConfigRegionCode());
							_cfg.setCfgType(regionDict.getConfigRegionValue());
							_cfg.setCreateTime(date);
							_cfg.setCreatorId(UserUtils.getUser().getId());
							_cfg.setDoLog(1);
							_cfg.setFunctionId(regionDict.getFunctionId());
							_cfg.setIsAudit(0);
							if(fullMap.get(Long.parseLong(_cfg.getUserRegion1()))) {
								_cfg.setIsValid(Constants.VALID_YES);
							}else {
								_cfg.setIsValid(Constants.VALID_NO);
							}
							_cfg.setIsAreaEffective(0);
							_cfg.setAttribute("0");
							_cfg.setClassify("0");
							_cfg.setLable("0");
							_cfg.setRequestId(StringUtil.isEmpty(requestId) ? 0 : requestId);
							_cfg.setServiceId(0);
							//设置region id
							if(regionIds!=null&&regionIds.size()==_ipPortCfgs.size()) {
								_cfg.setRegionId(regionIds.get(ind));
							}
							//设置group id
							if(asnNoMaps.get(0).containsKey(Long.parseLong(_cfg.getUserRegion1()))) {
								_cfg.setAsnIpGroup(asnNoMaps.get(0).get(Long.parseLong(_cfg.getUserRegion1())));
							}else if(asnNoMaps.get(1).containsKey(Long.parseLong(_cfg.getUserRegion1()))) {
								_cfg.setAsnIpGroup(asnNoMaps.get(1).get(Long.parseLong(_cfg.getUserRegion1())));
							}else {
		//						ConfigGroupInfo info=asnIpCfgService.getConfigGroupInfoByAsnNo(Long.parseLong(_cfg.getUserRegion1()));
								ConfigGroupInfo info=AsnCacheUtils.get(Long.parseLong(_cfg.getUserRegion1()));
								if(info==null) {
									info=configGroupInfoDao.getInfoByAsnNo(Long.parseLong(_cfg.getUserRegion1()));
								}
								_cfg.setAsnIpGroup(info.getGroupId());
							}
							asnIpCfgs.add(_cfg);
							ind++;
						}
						this.saveAsnIpBatch(asnIpCfgs);
					}
					transactionManager.commit(status);
				}catch (Throwable e) {
					transactionManager.rollback(status);
					// TODO: handle exception
					throw e;
				}
				_ipPortCfgs.clear();
				asnIpCfgs.clear();
			}
		}catch (Exception e) {
			// TODO: handle exception
			return e;
		}
		
		return null;
	}
	public Map<Long, Boolean> getFullMap() {
		return fullMap;
	}
	public void setFullMap(Map<Long, Boolean> fullMap) {
		this.fullMap = fullMap;
	}
	public List<Map<Long, Integer>> getAsnNoMaps() {
		return asnNoMaps;
	}
	public void setAsnNoMaps(List<Map<Long, Integer>> asnNoMaps) {
		this.asnNoMaps = asnNoMaps;
	}
	public void saveAsnIpBatch(List<AsnIpCfg> cfgs){
		//需要通过新增域接口新增的ip集合
		List<AsnIpCfg> toAddRegionAsnIpCfgs=Lists.newArrayList();
		long start=System.currentTimeMillis();
		for(AsnIpCfg cfg:cfgs) {
			if(Constants.VALID_YES==cfg.getIsValid().intValue()) {
				toAddRegionAsnIpCfgs.add(cfg);
			}
		}
		long end=System.currentTimeMillis();
		this.save(cfgs);
//		splitAndSend(toAddRegionAsnIpCfgs,Constants.VALID_YES);
		if(toAddRegionAsnIpCfgs.size()>0) {
			new AsnIpCfgService().asnIPRegionSendToMaat(toAddRegionAsnIpCfgs,Constants.VALID_YES);
		}
		cfgs.clear();
		toAddRegionAsnIpCfgs.clear();
	}
	public void save(List<AsnIpCfg> entitys) {
		logger.warn("Start to save IP");
		long start=System.currentTimeMillis();
		int len=0;
		List<AsnIpCfg> tempList=Lists.newArrayList();
//		Varibles maxPacket=asnIpCfgDao.getVaribles("max_allowed_packet");
		for(AsnIpCfg asnIpCfg:entitys) {
			int tempLen=asnIpCfg.toString().getBytes(Charset.forName("UTF-8")).length;
			if((len+tempLen)<Constants.MAX_ALLOWED_PACKET) {
				tempList.add(asnIpCfg);
				len+=tempLen;
			}else {
				logger.warn("save ip size:"+tempList.size());
				asnIpCfgDao.insertBatch(tempList);
				tempList.clear();
				tempList.add(asnIpCfg);
				len=tempLen;
			}
		}
		if(tempList.size()>0) {
			logger.warn("save ip size:"+tempList.size());
			asnIpCfgDao.insertBatch(tempList);
			tempList.clear();
		}
//		entitys.clear();
		long end=System.currentTimeMillis();
		logger.warn("Save IP finish,cost:"+(end-start));
	}
}
