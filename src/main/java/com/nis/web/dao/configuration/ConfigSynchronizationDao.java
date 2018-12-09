package com.nis.web.dao.configuration;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Param;

import com.nis.domain.basics.AsnIpCfg;
import com.nis.domain.configuration.AppByteCfg;
import com.nis.domain.configuration.AppComplexKeywordCfg;
import com.nis.domain.configuration.AppDomainCfg;
import com.nis.domain.configuration.AppFeatureIndex;
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
import com.nis.domain.configuration.AvFileSampleCfg;
import com.nis.domain.configuration.AvSignSampleCfg;
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
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;


/**
 * 配置全量同步相关配置数据处理类
 * @author zhangwei
 *
 */
@MyBatisDao
public interface ConfigSynchronizationDao {
	//app策略配置增删改查
//	public List<AppPolicyCfg> findAppPolicyList(AppPolicyCfg entity) ;
//	public List<IpPortCfg> getAppPolicyIpList(AppPolicyCfg entity);
//	//app协议IP配置增删改查
//	public List<AppIpCfg> findAppIpList(AppIpCfg entity) ;
//	//app http特征增删改查
//	public List<AppHttpCfg> findAppHttpList(AppHttpCfg entity) ;
//	//app 域名配置增删改查
//	public List<AppDomainCfg> findAppDomainList(AppDomainCfg entity) ;
//	//app 主题网站配置增删改查
//	public List<AppTopicDomainCfg> findAppTopicDomainList(AppTopicDomainCfg entity) ;
//	//app 字节特征增删改查
//	public List<AppByteCfg> findAppByteList(AppByteCfg entity) ;
//	//app ssl证书特征配置CRUD
//	public List<AppSslCertCfg> findAppSslList(AppSslCertCfg entity);
//	//app header特征配置CRUD
//	public List<AppHeaderCfg> findAppHeaderList(AppHeaderCfg entity);
//	//域名关联表操作
//	public List<WebsiteDomainTopic> getDomainDict(WebsiteDomainTopic websiteDomainTopic);
	
	public List<CfgIndexInfo> getCfgIndexList(BaseCfg entity);
	public List<IpPortCfg> getIpPortList(@Param("tableName")String tableName,@Param("compileIds")List compileIds);
	public List<AsnIpCfg> getAsnIpList(@Param("asnGroupIds")List asnGroupIds,@Param("isAudit")Integer isAudit);
	public List<BaseStringCfg> getStrList(@Param("tableName")String tableName,@Param("compileIds")List compileIds);
	public List<ComplexkeywordCfg> getComplexStrList(@Param("tableName")String tableName,@Param("compileIds")List compileIds);
	public List<FileDigestCfg> getFileDigestList(@Param("tableName")String tableName,@Param("compileIds")List compileIds);
	public List<DdosIpCfg> getDdosIpCfgList(BaseCfg entity);
	public List<FileDigestCfg> getFileDigestListByService(BaseCfg entity);
	
	public List<AvFileSampleCfg> getAvFileCfgList(BaseCfg entity);
	public List<AvSignSampleCfg> getAvSignCfgList(BaseCfg entity);
	public List<PxyObjKeyring> getPxyObjKeyringCfgList(BaseCfg entity);
	public List<PxyObjTrustedCaCert> getPxyObjTrustedCertCfgList(BaseCfg entity);
	public List<PxyObjTrustedCaCrl> getPxyObjTrustedCrlCfgList(BaseCfg entity);
	public List<PxyObjTrustedCaCrl> getPxyObjTrustedCrlCfgListByCertId(@Param("certIds")List certIds);
	public List<DnsResStrategy> getDnsStrategyList(BaseCfg entity);
	public List<DnsIpCfg> getDnsIpCfgList(BaseCfg entity);
	public List<IpPortCfg> getIpPortListByService(BaseCfg entity);
	public List<IpPortCfg> getIpDropList(@Param("tableName")String tableName,@Param("compileIds")List compileIds);
	
	public List<AppPolicyCfg> getAppPolicyList(BaseCfg entity);
	public List<AppFeatureIndex> getAppFeatureIndexList(BaseCfg entity);
	public List<BaseStringCfg> getStrListByService(BaseCfg entity);
	public List<ComplexkeywordCfg> getComplexStrListByService(BaseCfg entity);
	public List<IpPortCfg> getAppIpPortList(@Param("tableName")String tableName,@Param("compileIds")List compileIds);
	public List<AppIpCfg> getAppIpFeatureList(BaseCfg entity);
}
