/**
 *@Title: AppIdConfig.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2018年2月6日 上午11:24:28
 *@version  版本号
 */
package com.nis.domain.configuration;

import java.util.List;

/**
 * @ClassName: AppIdConfig.java 
 * @Description: TODO
 * @author (dell)
 * @date 2018年2月6日 上午11:24:28
 * @version V1.0
 */
public class AppIdCfg extends BaseCfg<AppIdCfg> {
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -5385478577426593882L;
	/**
	 * 应用名称
	 */
	private String appName;
	/**
	 * 应用协议id
	 */
	private Integer appId;
	
	/**
	 * 编译id
	 */
	private Integer compileId;

	/**
	 * 协议字符串特征表信息列表
	 */
	private List<AppComplexKeywordCfg> complexFeaturesList;
	
	/**
	 * 协议增强字符串特征配置信息列表
	 */
	private List<AppStringCfg> strFeaturesCfgList;
	
	/**
	 * 协议服务器IP配置信息列表
	 */
	private List<BaseIpCfg> ipCfgList;
	
	private List<AppFeaturesIndex> featuresList;
	
	public static class AppFeaturesIndex{
		private Long indexId;
		private Integer appCompileId;
		private String featuresTable;
		private Integer featuresCompileId;
		private Integer featuresTableType;
		public Long getIndexId() {
			return indexId;
		}
		public void setIndexId(Long indexId) {
			this.indexId = indexId;
		}
		public String getFeaturesTable() {
			return featuresTable;
		}
		public void setFeaturesTable(String featuresTable) {
			this.featuresTable = featuresTable;
		}

		public Integer getFeaturesTableType() {
			return featuresTableType;
		}
		public void setFeaturesTableType(Integer featuresTableType) {
			this.featuresTableType = featuresTableType;
		}
		public Integer getAppCompileId() {
			return appCompileId;
		}
		public void setAppCompileId(Integer appCompileId) {
			this.appCompileId = appCompileId;
		}
		public Integer getFeaturesCompileId() {
			return featuresCompileId;
		}
		public void setFeaturesCompileId(Integer featuresCompileId) {
			this.featuresCompileId = featuresCompileId;
		}
		
	}
	/**
	 * appName
	 * @return  appName
	*/
	
	public String getAppName() {
		return appName;
	}

	/**
	 * @param appName the appName to set
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}

	/**
	 * appId
	 * @return  appId
	*/
	
	public Integer getAppId() {
		return appId;
	}

	/**
	 * @param appId the appId to set
	 */
	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	/**
	 * compileId
	 * @return  compileId
	*/
	
	public Integer getCompileId() {
		return compileId;
	}

	/**
	 * @param compileId the compileId to set
	 */
	public void setCompileId(Integer compileId) {
		this.compileId = compileId;
	}


	/* (non-Javadoc)
	 * @see com.nis.domain.configuration.BaseCfg#initDefaultValue()
	 */
	@Override
	public void initDefaultValue() {
		// TODO Auto-generated method stub
		super.initDefaultValue();
		
	}

	public List<AppComplexKeywordCfg> getComplexFeaturesList() {
		return complexFeaturesList;
	}

	public void setComplexFeaturesList(List<AppComplexKeywordCfg> complexFeaturesList) {
		this.complexFeaturesList = complexFeaturesList;
	}

	public List<AppStringCfg> getStrFeaturesCfgList() {
		return strFeaturesCfgList;
	}

	public void setStrFeaturesCfgList(List<AppStringCfg> strFeaturesCfgList) {
		this.strFeaturesCfgList = strFeaturesCfgList;
	}

	public List<AppFeaturesIndex> getFeaturesList() {
		return featuresList;
	}

	public void setFeaturesList(List<AppFeaturesIndex> featuresList) {
		this.featuresList = featuresList;
	}

	public List<BaseIpCfg> getIpCfgList() {
		return ipCfgList;
	}

	public void setIpCfgList(List<BaseIpCfg> ipCfgList) {
		this.ipCfgList = ipCfgList;
	}
	
}
