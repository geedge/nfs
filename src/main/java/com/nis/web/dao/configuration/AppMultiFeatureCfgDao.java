package com.nis.web.dao.configuration;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.configuration.AppByteCfg;
import com.nis.domain.configuration.AppComplexFeatureCfg;
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
import com.nis.domain.configuration.AppStringFeatureCfg;
import com.nis.domain.configuration.AppTcpCfg;
import com.nis.domain.configuration.AppTopicDomainCfg;
import com.nis.domain.configuration.BaseCfg;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.CfgIndexInfo;
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
public interface AppMultiFeatureCfgDao {
	//app复杂多域特征索引配置
	public int insertAppFeatureIndex(AppFeatureIndex entity);
	public int updateAppFeatureIndex(AppFeatureIndex entity);
	public List<AppFeatureIndex> findAppFeatureIndexList(AppFeatureIndex entity);
	public AppFeatureIndex getAppFeatureIndex(Long cfgId) ;
	//审核配置
	public void auditCfg(BaseCfg entity);
	//修改配置状态
	public void updateCfgValid(BaseCfg entity);	
	//字符串类特征配置
	public List<AppStringFeatureCfg> getAppStringFeatureCfg(@Param("compileId")Integer compileId,
			@Param("functionId")Integer functionId,@Param("cfgRegionCode")Integer cfgRegionCode);
	public int insertAppStringFeatureCfg(AppStringFeatureCfg entity);
	public void deleteAppStringFeatureCfg(BaseCfg entity);
	//增强字符串类特征配置
	public List<AppComplexFeatureCfg> getAppComplexFeatureCfg(@Param("compileId")Integer compileId,
			@Param("functionId")Integer functionId,@Param("cfgRegionCode")Integer cfgRegionCode);
	public int insertAppComplexFeatureCfg(AppComplexFeatureCfg entity);
	public void deleteAppComplexFeatureCfg(BaseCfg entity);
	//IP RANGE配置
	public List<AppIpCfg> getAppIpRangeCfg(@Param("compileId")Integer compileId,@Param("functionId")Integer functionId) ;
	public int insertAppIpRangeCfg(AppIpCfg entity);
	public void deleteAppIpRangeCfg(BaseCfg entity);
}
