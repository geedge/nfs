package com.nis.web.dao.configuration;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.configuration.AppByteCfg;
import com.nis.domain.configuration.AppComplexKeywordCfg;
import com.nis.domain.configuration.AppDomainCfg;
import com.nis.domain.configuration.AppHeaderCfg;
import com.nis.domain.configuration.AppHttpCfg;
import com.nis.domain.configuration.AppIdCfg;
import com.nis.domain.configuration.AppIdCfg.AppFeaturesIndex;
import com.nis.domain.configuration.AppInnerRuleCfg;
import com.nis.domain.configuration.AppIpCfg;
import com.nis.domain.configuration.AppPolicyCfg;
import com.nis.domain.configuration.AppSslCertCfg;
import com.nis.domain.configuration.AppStringCfg;
import com.nis.domain.configuration.AppTcpCfg;
import com.nis.domain.configuration.AppTopicDomainCfg;
import com.nis.domain.configuration.BaseCfg;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.ComplexkeywordCfg;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.configuration.WebsiteDomainTopic;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;


/**
 * 特定协议相关配置数据处理类
 * @author dell
 *
 */
@MyBatisDao
public interface AppCfgDao {
	//app策略配置增删改查
	public List<AppPolicyCfg> findAppPolicyList(AppPolicyCfg entity) ;
	public AppPolicyCfg getAppPolicyCfg(@Param("cfgId")Long cfgId,@Param("compileId")Integer compileId) ;
	public IpPortCfg getAppPolicyIpCfg(AppPolicyCfg entity) ;
	public List<IpPortCfg> getAppPolicyIpList(AppPolicyCfg entity);
	public int insertAppPolicyCfg(AppPolicyCfg entity);
	public int insertAppPolicyCfgForBatch(AppPolicyCfg entity);
	public int updateAppPolicyCfg(AppPolicyCfg entity);	
	public int insertAppPolicyIpCfg(IpPortCfg entity);
	public int deleteAppPolicyIpCfg(AppPolicyCfg entity);
	
	//app协议IP配置增删改查
	public List<AppIpCfg> findAppIpList(AppIpCfg entity) ;
	public AppIpCfg getAppIpCfg(Long cfgId) ;
	public int insertAppIpCfg(AppIpCfg entity);
	public int insertAppIpCfgForBatch(AppIpCfg entity);
	public int insertAppIpCfgBatch(List<AppIpCfg> list);
	public int updateAppIpCfg(AppIpCfg entity);
	//app http特征增删改查
	public List<AppHttpCfg> findAppHttpList(AppHttpCfg entity) ;
	public AppHttpCfg getAppHttpCfg(Long cfgId) ;
	public int insertAppHttpCfg(AppHttpCfg entity);
	public int updateAppHttpCfg(AppHttpCfg entity);
	//app 域名配置增删改查
	public List<AppDomainCfg> findAppDomainList(AppDomainCfg entity) ;
	public AppDomainCfg getAppDomainCfg(Long cfgId) ;
	public int insertAppDomainCfg(AppDomainCfg entity);
	public int updateAppDomainCfg(AppDomainCfg entity);
	//app 主题网站配置增删改查
	public List<AppTopicDomainCfg> findAppTopicDomainList(AppTopicDomainCfg entity) ;
	public AppTopicDomainCfg getAppTopicDomainCfg(Long cfgId) ;
	public int insertAppTopicDomainCfg(AppTopicDomainCfg entity);
	public int updateAppTopicDomainCfg(AppTopicDomainCfg entity);
	//app 字节特征增删改查
	public List<AppByteCfg> findAppByteList(AppByteCfg entity) ;
	public AppByteCfg getAppByteCfg(Long cfgId) ;
	public int insertAppByteCfg(AppByteCfg entity);
	public int updateAppByteCfg(AppByteCfg entity);
	//app内置文件增删改查
	public List<AppInnerRuleCfg> findAppInnerRuleList(AppInnerRuleCfg entity) ;
	public AppInnerRuleCfg getAppInnerRuleCfg(Long cfgId) ;
	public int insertAppInnerRuleCfg(AppInnerRuleCfg entity);
	public int updateAppInnerRuleCfg(AppInnerRuleCfg entity);
	//app ssl证书特征配置CRUD
	public List<AppSslCertCfg> findAppSslList(AppSslCertCfg entity);
	public AppSslCertCfg getAppSslCfg(Long cfgId);
	public int insertAppSslCfg(AppSslCertCfg entity);
	public int updateAppSslCfg(AppSslCertCfg entity);
	//app header特征配置CRUD
	public List<AppHeaderCfg> findAppHeaderList(AppHeaderCfg entity);
	public AppHeaderCfg getAppHeaderCfg(Long cfgId);
	public int insertAppHeaderCfg(AppHeaderCfg entity);
	public int updateAppHeaderCfg(AppHeaderCfg entity);
	//APP TCP会话字节数特征
	public List<AppTcpCfg> findAppTcpList(AppTcpCfg entity);
	public AppTcpCfg getAppTcpCfg(Long cfgId);
	public int insertAppTcpCfg(AppTcpCfg entity);
	public int updateAppTcpCfg(AppTcpCfg entity);
	//审核配置
	public void auditCfg(BaseCfg entity);
	//修改配置状态
	public void updateCfgValid(BaseCfg entity);
	public void deleteSubscribeIdCfg(AppPolicyCfg entity);
	//域名关联表操作
	public List<WebsiteDomainTopic> getDomainDict(WebsiteDomainTopic websiteDomainTopic);
	public void saveDomainDict(WebsiteDomainTopic websiteDomainTopic);
	public List<AppIpCfg> getAppIpCfgBySpecServiceId(Integer specServiceId);
	
	// 导入配置时数据批量入库使用
	public void insertAppDomainCfgBatch(AppDomainCfg cfg);
	public void insertAppHttpCfgBatch(AppHttpCfg cfg);
}
