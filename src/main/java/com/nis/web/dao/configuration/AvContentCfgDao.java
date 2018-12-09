package com.nis.web.dao.configuration;


import java.util.List;

import com.nis.domain.configuration.AvContUrlCfg;
import com.nis.domain.configuration.AvVoipAccountCfg;
import com.nis.domain.configuration.AvVoipIpCfg;
import com.nis.domain.configuration.BaseCfg;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.web.dao.MyBatisDao;


@MyBatisDao
public interface AvContentCfgDao {
	public List<CfgIndexInfo> findVoipList(CfgIndexInfo entity) ;
	public List<CfgIndexInfo> findAccountList(CfgIndexInfo entity) ;
	public List<AvVoipIpCfg> findVoipIpCfgList(CfgIndexInfo entity);
	public List<AvVoipAccountCfg> findVoipAccountCfgList(CfgIndexInfo entity);
	public CfgIndexInfo getCfgIndexInfo(CfgIndexInfo entity);
	public List<CfgIndexInfo> findStreamList(CfgIndexInfo entity) ;
	
	public List<IpPortCfg> getIpPortList(CfgIndexInfo entity);
	public void saveIpPortCfg(IpPortCfg entity);
	public void deleteIpCfg(CfgIndexInfo entity);
	
	public List<AvContUrlCfg> getAvContUrlList(CfgIndexInfo entity);
	public void saveAvContUrlCfg(CfgIndexInfo entity);
	public void saveAvContUrlCfgNew(AvContUrlCfg entity);
	public void deleteAvContUrlCfg(CfgIndexInfo entity);
	
	public void insertCfgIndexInfo(CfgIndexInfo entity);
	public void insertAvVoipIp(CfgIndexInfo entity);
	public void insertAvVoipAccountCfg(CfgIndexInfo entity);
	public void saveAvVoipAccountCfg(AvVoipAccountCfg entity);
	public void insertAvVoipKeywordCfg(CfgIndexInfo entity);
	
	public void updateCfgIndexInfo(CfgIndexInfo entity);
	public void updateAvVoipIp(CfgIndexInfo entity);
	public void updateAvVoipAccount(CfgIndexInfo entity);
	public void updateAvVoipKeywordCfgt(CfgIndexInfo entity);
	
	public void deleteAvVoipIp(CfgIndexInfo entity);
	public void deleteAvVoipAccountCfg(CfgIndexInfo entity);
	
	public BaseIpCfg findIpCfgById(BaseIpCfg entity) ;
	public void insertIp(BaseIpCfg entity);
	public void updateIp(BaseIpCfg entity);
	public List<BaseIpCfg> findIpList(BaseIpCfg entity) ;
	public List<BaseIpCfg> getIpList(BaseIpCfg entity) ;
	public void updateCfgValid(BaseCfg entity);
	public BaseStringCfg findStringCfgById(BaseStringCfg entity) ;
	public void insertString(BaseStringCfg entity);
	public void updateString(BaseStringCfg entity);
	public List<BaseStringCfg> findStringList(BaseStringCfg entity) ;
	public List<BaseStringCfg> getStringList(BaseStringCfg entity) ;
	
}
