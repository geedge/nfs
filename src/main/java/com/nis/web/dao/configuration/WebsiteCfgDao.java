package com.nis.web.dao.configuration;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.configuration.BaseCfg;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.ComplexkeywordCfg;
import com.nis.domain.configuration.HttpBodyCfg;
import com.nis.domain.configuration.HttpReqHeadCfg;
import com.nis.domain.configuration.HttpResHeadCfg;
import com.nis.domain.configuration.HttpUrlCfg;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;


/**
 * WEB相关配置数据处理类
 * @author dell
 *
 */
@MyBatisDao
public interface WebsiteCfgDao extends CrudDao<CfgIndexInfo>{

	public List<CfgIndexInfo> getCfgIndexInfos(CfgIndexInfo entity);
	public CfgIndexInfo getCfgIndexInfo(@Param("cfgId")Long id,@Param("compileId")Integer compileId);
	public List<CfgIndexInfo> getWebsiteList(CfgIndexInfo entity);
	public List<IpPortCfg> getIpPortList(CfgIndexInfo entity);
	public List<HttpUrlCfg> getHttpUrlList(CfgIndexInfo entity);
	public List<HttpResHeadCfg> getHttpResHdrList(CfgIndexInfo entity);
	public List<HttpBodyCfg> getHttpResBodyList(CfgIndexInfo entity);
	public List<HttpBodyCfg> getHttpBodyList(CfgIndexInfo entity);
	public List<HttpReqHeadCfg> getHttpReqHdrList(CfgIndexInfo entity);
	public List<HttpBodyCfg> getHttpReqBodyList(CfgIndexInfo entity);
	public void saveCfgIndex(CfgIndexInfo entity);
	public void saveIpPortCfg(IpPortCfg entity);
	public void saveHttpUrlCfg(HttpUrlCfg entity);
	public void saveHttpReqHdrCfg(HttpReqHeadCfg entity);
	public void saveHttpResHdrCfg(HttpResHeadCfg entity);
	public void saveHttpBodyCfg(HttpBodyCfg entity);
	public void deleteHttpIpCfg(CfgIndexInfo entity);
	public void deleteHttpUrlCfg(CfgIndexInfo entity);
	public void deleteHttpReqHdrCfg(CfgIndexInfo entity);
	public void deleteHttpResHdrCfg(CfgIndexInfo entity);
	public void deleteHttpBodyCfg(CfgIndexInfo entity);
	public void updateCfgIndex(CfgIndexInfo entity);
	
	public void updateCfgValid(BaseCfg entity);
	public void auditCfg(BaseCfg entity);

//	public List<CfgIndexInfo> getDnsList(CfgIndexInfo entity);
//	public List<DnsIpCfg> getDnsIp(CfgIndexInfo entity);
	public List<ComplexkeywordCfg> getDnsDomainList(CfgIndexInfo entity);
//	public void saveDnsIpCfg(DnsIpCfg entity);
	public void saveDnsDomainCfg(ComplexkeywordCfg entity);
	public void deleteDnsIpCfg(CfgIndexInfo entity);
	public void deleteDnsDomainCfg(CfgIndexInfo entity);
	
	public List<BaseStringCfg> getSslKewordList(CfgIndexInfo entity);
	public void saveSslKeywordCfg(BaseStringCfg entity);
	public void deleteSslIpCfg(CfgIndexInfo entity);
	public void deleteSslKeywordCfg(CfgIndexInfo entity);
	
}
