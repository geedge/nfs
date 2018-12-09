package com.nis.web.dao.configuration;

import java.util.List;

import com.nis.domain.configuration.AvFileSampleCfg;
import com.nis.domain.configuration.AvSignSampleCfg;
import com.nis.domain.configuration.BaseCfg;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.ComplexkeywordCfg;
import com.nis.domain.configuration.DnsDomainCfg;
import com.nis.domain.configuration.DnsIpCfg;
import com.nis.domain.configuration.HttpBodyCfg;
import com.nis.domain.configuration.HttpReqHeadCfg;
import com.nis.domain.configuration.HttpResHeadCfg;
import com.nis.domain.configuration.HttpUrlCfg;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.configuration.SslKeywordCfg;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;


/**
 * WEB相关配置数据处理类
 * @author dell
 *
 */
@MyBatisDao
public interface XmppCfgDao extends CrudDao<CfgIndexInfo>{
	public CfgIndexInfo getCfgIndexInfo(Long id);
	public List<CfgIndexInfo> getXmppList(CfgIndexInfo entity);
	public List<IpPortCfg> getIpPortList(CfgIndexInfo entity);
	public void saveCfgIndex(CfgIndexInfo entity);
	public void saveIpPortCfg(IpPortCfg entity);
	public void updateCfgIndex(CfgIndexInfo entity);
	public void updateCfgValid(BaseCfg entity);
	public void auditCfg(BaseCfg entity);
	public void deleteXmppIpCfg(BaseCfg entity);
}
