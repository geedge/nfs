package com.nis.web.dao.configuration;

import java.util.List;

import org.apache.ibatis.annotations.Param;

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
import com.nis.domain.configuration.NtcBgpAsCfg;
import com.nis.domain.configuration.NtcSubscribeIdCfg;
import com.nis.domain.configuration.SslKeywordCfg;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;


/**
 * WEB相关配置数据处理类
 * @author dell
 *
 */
@MyBatisDao
public interface BgpCfgDao extends CrudDao<CfgIndexInfo>{

	public CfgIndexInfo getCfgIndexInfo(@Param("cfgId")Long id,@Param("compileId") Integer compileId);
	public List<CfgIndexInfo> getBgpList(CfgIndexInfo entity);
	public List<IpPortCfg> getIpPortList(CfgIndexInfo entity);
	public List<HttpUrlCfg> getHttpUrlList(CfgIndexInfo entity);
	public List<NtcBgpAsCfg> getNtcBgpAsList(CfgIndexInfo entity);
	public void saveNtcBgpAsCfg(NtcBgpAsCfg entity);
	public void deleteBgpAsCfg(CfgIndexInfo entity);
	public void saveCfgIndex(CfgIndexInfo entity);
	public void saveIpPortCfg(IpPortCfg entity);
	public void updateBgpAsCfg(CfgIndexInfo entity);
	public void updateSubscribeIdCfg(CfgIndexInfo entity);
	public void deleteSubscribeIdCfg(CfgIndexInfo entity);
	//public void saveHttpUrlCfg(HttpUrlCfg entity);
	public void deleteHttpIpCfg(CfgIndexInfo entity);
	//public void deleteHttpUrlCfg(CfgIndexInfo entity);
	public void updateCfgIndex(CfgIndexInfo entity);
	public void updateCfgValid(BaseCfg entity);
	public void auditCfg(BaseCfg entity);
	public void saveDnsDomainCfg(ComplexkeywordCfg entity);
	public void deleteIpCfg(CfgIndexInfo entity);
	
	
}
