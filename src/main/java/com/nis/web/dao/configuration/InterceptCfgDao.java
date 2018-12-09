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
import com.nis.domain.configuration.InterceptPktBin;
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
public interface InterceptCfgDao extends CrudDao<CfgIndexInfo>{

	public List<InterceptPktBin> getInterceptPktBin(CfgIndexInfo entity);
	public void saveInterceptPktBin(InterceptPktBin entity);
	public void deleteInterceptPktBin(CfgIndexInfo entity);
	public void deleteInterceptIpCfg(CfgIndexInfo entity);
	
}
