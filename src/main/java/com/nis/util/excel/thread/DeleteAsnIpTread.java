package com.nis.util.excel.thread;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.beust.jcommander.internal.Lists;
import com.nis.domain.basics.AsnIpCfg;
import com.nis.domain.specific.ConfigGroupInfo;
import com.nis.util.AsnCacheUtils;
import com.nis.util.Constants;
import com.nis.web.dao.basics.AsnIpCfgDao;
import com.nis.web.dao.specific.ConfigGroupInfoDao;
import com.nis.web.service.SpringContextHolder;
import com.nis.web.service.basics.AsnIpCfgService;

public class DeleteAsnIpTread implements Callable<Throwable> {
	private BlockingQueue<Long> asnNos;
	private ConfigGroupInfoDao configGroupInfoDao;
	private AsnIpCfgDao asnIpCfgDao;
	public DeleteAsnIpTread(BlockingQueue<Long> asnNos) {
		this.asnNos=asnNos;
		this.configGroupInfoDao=SpringContextHolder.getBean(ConfigGroupInfoDao.class);
		this.asnIpCfgDao=SpringContextHolder.getBean(AsnIpCfgDao.class);
	}
	@Override
	public Throwable call() throws Exception {
		// TODO Auto-generated method stub
		List<Long> asnNoList=Lists.newArrayList();
		List<Long> asnIds=Lists.newArrayList();
		DataSourceTransactionManager transactionManager=(DataSourceTransactionManager)SpringContextHolder.getBean("transactionManager");
		LinkedBlockingQueue<AsnIpCfg> toDelAndSendAsnIpCfgs=new LinkedBlockingQueue<AsnIpCfg>();
		while(!asnNos.isEmpty()) {
			asnNos.drainTo(asnNoList,5);
			for(Long asnNo:asnNoList) {
				ConfigGroupInfo configGroupInfo=AsnCacheUtils.get(asnNo);
				if(configGroupInfo==null) {
					configGroupInfo=configGroupInfoDao.getInfoByAsnNo(asnNo);
				}
				if(configGroupInfo.getIsIssued()==1) {//已下发
					List<AsnIpCfg> _toDelAsnIpCfgs=asnIpCfgDao.getByAsnNo(configGroupInfo.getAsnId());
					toDelAndSendAsnIpCfgs.addAll(_toDelAsnIpCfgs);
				}else {
					asnIds.add(asnNo);
				}
				
			}
			if(asnIds.size()>0) {
				this.deleteByAsnNo(asnIds);
			}
			if(!toDelAndSendAsnIpCfgs.isEmpty()) {
				Integer size = toDelAndSendAsnIpCfgs.size();
				int pointsDataLimit = Constants.MAAT_JSON_SEND_SIZE;//限制条数
				List<AsnIpCfg> listPage=Lists.newArrayList(Constants.MAAT_JSON_SEND_SIZE);
				while(!toDelAndSendAsnIpCfgs.isEmpty()) {
					//开启一个新事物
					DefaultTransactionDefinition def = new DefaultTransactionDefinition();
					def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 事物隔离级别，开启新事务，这样会比较安全些。
					TransactionStatus status = transactionManager.getTransaction(def); // 获得事务状态
					try {
						toDelAndSendAsnIpCfgs.drainTo(listPage, Constants.MAAT_JSON_SEND_SIZE);
						StringBuilder sb=new StringBuilder();
						for(AsnIpCfg ip:listPage) {
							sb.append(ip.getCfgId());
							sb.append(",");
						}
						sb.deleteCharAt(sb.toString().lastIndexOf(","));
						asnIpCfgDao.delete(sb.toString());
						new AsnIpCfgService().asnIPRegionSendToMaat(listPage, Constants.VALID_NO);
						transactionManager.commit(status);
						listPage.clear();
					} catch (Throwable e) {
						// TODO: handle exception
						transactionManager.rollback(status);
						throw e;
					}
				}
			}
			asnNoList.clear();
			asnIds.clear();
		}
		return null;
	}
	public void deleteByAsnNo(List<Long> asnNoList) {
		int pointsDataLimit = 1000;//限制条数
		Integer size = asnNoList.size();
		if(pointsDataLimit<size){
			int part = size/pointsDataLimit;//分批数
			for (int i = 0; i < part; i++) {
				List<Long> listPage = asnNoList.subList(0, pointsDataLimit);
				StringBuilder asnNoStr=new StringBuilder();
				for(Long asnNo:asnNoList) {
					asnNoStr.append(asnNo);
					asnNoStr.append(",");
				}
				int result=0;
				do {
					result=asnIpCfgDao.deleteByAsnGroup(asnNoStr.toString().substring(0,asnNoStr.toString().length()-1));
				}while(result>0);
				//剔除
				asnNoList.subList(0, pointsDataLimit).clear();
			}
			if(!asnNoList.isEmpty()){
				StringBuilder asnNoStr=new StringBuilder();
				for(Long asnNo:asnNoList) {
					asnNoStr.append(asnNo);
					asnNoStr.append(",");
				}
				int result=0;
				do {
					result=asnIpCfgDao.deleteByAsnGroup(asnNoStr.toString().substring(0,asnNoStr.toString().length()-1));
				}while(result>0);
				asnNoList.clear();
			}
		}else {
			StringBuilder asnNoStr=new StringBuilder();
			for(Long asnNo:asnNoList) {
				asnNoStr.append(asnNo);
				asnNoStr.append(",");
			}
			int result=0;
			do {
				result=asnIpCfgDao.deleteByAsnGroup(asnNoStr.toString().substring(0,asnNoStr.toString().length()-1));
			}while(result>0);
			asnNoList.clear();
		}
	}
	
}
