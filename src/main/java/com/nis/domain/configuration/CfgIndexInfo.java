/**
 *@Title: CfgIndexInfo.java
 *@Package com.nis.domain.restful 
 *@Description 索引公共表
 *@author dell
 *@date 2018年5月17日 下午16:59:17
 *@version  版本号
 */
package com.nis.domain.configuration;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.cache.CacheKey;

import com.nis.domain.basics.AsnIpCfg;
import com.nis.domain.basics.IpReuseIpCfg;
import com.nis.util.excel.ExcelField;


/**
 * @ClassName: CfgIndexInfo.java 
 * @author (dell)
 * @date 2018年5月17日 下午16:59:17
 * @version V1.0
 */
public class CfgIndexInfo extends BaseCfg<CfgIndexInfo> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2796500715438264119L;
	private static final String tableName="cfg_index_info";
	private String indexTable="cfg_index_info";
	private List<AvVoipAccountCfg> voipAccounts;//Add表单使用
	private List<AsnIpCfg> asnIpCfgs;//Add表单使用
	private List<AvVoipIpCfg> voipIps;	//Add表单使用
	private AvVoipAccountCfg voipAccount;//Search使用
	private AvVoipIpCfg voipIp;//Search使用
	private IpPortCfg ipPort;
	private HttpUrlCfg httpUrl;
	private HttpReqHeadCfg httpReqHdr;
	private HttpResHeadCfg httpResHdr;
	private HttpBodyCfg httpReqBody;
	private HttpBodyCfg httpResBody;
	private List<IpPortCfg> ipPortList;
	private List<HttpUrlCfg> httpUrlList;
	private List<HttpReqHeadCfg> httpReqHdrList;
	private List<HttpResHeadCfg> httpResHdrList;
	private List<HttpBodyCfg> httpReqBodyList;
	private List<HttpBodyCfg> httpResBodyList;
	private SslKeywordCfg sslCfg;
	private List<BaseStringCfg> sslList;
	private List<ComplexkeywordCfg> domainList;
	private List<ComplexkeywordCfg> complexList;
	private List<BaseStringCfg> stringList;
	private List<FileDigestCfg> digestList;
	private List<NtcSubscribeIdCfg> ntcSubscribeIdCfgList;//新增SUBSCRIBE_ID
	private List<NtcBgpAsCfg> ntcBgpAsCfgList;
	private NtcSubscribeIdCfg ntcSubscribeIdCfg;
	private NtcBgpAsCfg ntcBgpAsCfg;
	private InterceptPktBin interceptPktBin;
	private List<InterceptPktBin> interceptPktBinList;
	private AvContUrlCfg avContUrlCfg;
	private List<AvContUrlCfg> avContUrlCfgList;
	private BaseStringCfg strCfg;
	private ComplexkeywordCfg complexCfg;
	private List<IpReuseIpCfg> ipReuseIpCfgs;
	private Long dnsStrategyId;
	@ExcelField(title="policy_name",sort=21)
	private String dnsStrategyName;
	private Integer asnIpGroup;//asn ip使用的组，仅用来帮助asnIpCfgs中的 ip保存组名，数据库并不需要新增字段
	private String asnIpGroupName;
	private P2pHashCfg p2pHash;
	private P2pKeywordCfg p2pKeyword;
	private List<P2pHashCfg> p2pHashList;
	private List<P2pKeywordCfg> p2pKeywordList;
	
	private Integer sourceCompileId;
	private String searchKeywords;// 列表关键字查询字段
	
	private Map<String,Object> userRegion;
	
	/*private CachePolicyUserRegion cachePolicyUserRegion;//缓存策略用户自定义域参数
	
	public static class CachePolicyUserRegion{
		private  CacheKey cache_key;
		public static class CecheKey{
			
		}
	}*/
	
	
	public Integer getSourceCompileId() {
		return sourceCompileId;
	}
	public List<IpReuseIpCfg> getIpReuseIpCfgs() {
		return ipReuseIpCfgs;
	}
	public void setIpReuseIpCfgs(List<IpReuseIpCfg> ipReuseIpCfgs) {
		this.ipReuseIpCfgs = ipReuseIpCfgs;
	}
	public void setSourceCompileId(Integer sourceCompileId) {
		this.sourceCompileId = sourceCompileId;
	}
	
	public AvContUrlCfg getAvContUrlCfg() {
		return avContUrlCfg;
	}
	public void setAvContUrlCfg(AvContUrlCfg avContUrlCfg) {
		this.avContUrlCfg = avContUrlCfg;
	}
	public List<AvContUrlCfg> getAvContUrlCfgList() {
		return avContUrlCfgList;
	}
	public void setAvContUrlCfgList(List<AvContUrlCfg> avContUrlCfgList) {
		this.avContUrlCfgList = avContUrlCfgList;
	}
	public Integer getAsnIpGroup() {
		return asnIpGroup;
	}
	public void setAsnIpGroup(Integer asnIpGroup) {
		this.asnIpGroup = asnIpGroup;
	}
	public String getAsnIpGroupName() {
		return asnIpGroupName;
	}
	public void setAsnIpGroupName(String asnIpGroupName) {
		this.asnIpGroupName = asnIpGroupName;
	}
	public List<AsnIpCfg> getAsnIpCfgs() {
		return asnIpCfgs;
	}
	public void setAsnIpCfgs(List<AsnIpCfg> asnIpCfgs) {
		this.asnIpCfgs = asnIpCfgs;
	}
	public NtcBgpAsCfg getNtcBgpAsCfg() {
		return ntcBgpAsCfg;
	}
	public void setNtcBgpAsCfg(NtcBgpAsCfg ntcBgpAsCfg) {
		this.ntcBgpAsCfg = ntcBgpAsCfg;
	}
	public List<NtcBgpAsCfg> getNtcBgpAsCfgList() {
		return ntcBgpAsCfgList;
	}
	public void setNtcBgpAsCfgList(List<NtcBgpAsCfg> ntcBgpAsCfgList) {
		this.ntcBgpAsCfgList = ntcBgpAsCfgList;
	}
	public InterceptPktBin getInterceptPktBin() {
		return interceptPktBin;
	}
	public void setInterceptPktBin(InterceptPktBin interceptPktBin) {
		this.interceptPktBin = interceptPktBin;
	}
	public void setInterceptPktBinList(List<InterceptPktBin> interceptPktBinList) {
		this.interceptPktBinList = interceptPktBinList;
	}
	public List<InterceptPktBin> getInterceptPktBinList() {
		return interceptPktBinList;
	}
	public List<NtcSubscribeIdCfg> getNtcSubscribeIdCfgList() {
		return ntcSubscribeIdCfgList;
	}
	public void setNtcSubscribeIdCfgList(List<NtcSubscribeIdCfg> ntcSubscribeIdCfgList) {
		this.ntcSubscribeIdCfgList = ntcSubscribeIdCfgList;
	}
	public NtcSubscribeIdCfg getNtcSubscribeIdCfg() {
		return ntcSubscribeIdCfg;
	}
	public void setNtcSubscribeIdCfg(NtcSubscribeIdCfg ntcSubscribeIdCfg) {
		this.ntcSubscribeIdCfg = ntcSubscribeIdCfg;
	}
	public Long getDnsStrategyId() {
		return dnsStrategyId;
	}
	public void setDnsStrategyId(Long dnsStrategyId) {
		this.dnsStrategyId = dnsStrategyId;
	}
	public String getDnsStrategyName() {
		return dnsStrategyName;
	}
	public void setDnsStrategyName(String dnsStrategyName) {
		this.dnsStrategyName = dnsStrategyName;
	}
	
	public AvVoipAccountCfg getVoipAccount() {
		return voipAccount;
	}
	public void setVoipAccount(AvVoipAccountCfg voipAccount) {
		this.voipAccount = voipAccount;
	}
	public List<AvVoipAccountCfg> getVoipAccounts() {
		return voipAccounts;
	}
	public void setVoipAccounts(List<AvVoipAccountCfg> voipAccounts) {
		this.voipAccounts = voipAccounts;
	}
	public AvVoipIpCfg getVoipIp() {
		return voipIp;
	}
	public void setVoipIp(AvVoipIpCfg voipIp) {
		this.voipIp = voipIp;
	}
	public SslKeywordCfg getSslCfg() {
		return sslCfg;
	}
	public void setSslCfg(SslKeywordCfg sslCfg) {
		this.sslCfg = sslCfg;
	}
	public List<BaseStringCfg> getSslList() {
		return sslList;
	}
	public void setSslList(List<BaseStringCfg> sslList) {
		this.sslList = sslList;
	}
	public void setVoipIps(List<AvVoipIpCfg> voipIps) {
		this.voipIps = voipIps;
	}
	public List<AvVoipIpCfg> getVoipIps() {
		return voipIps;
	}
	public IpPortCfg getIpPort() {
		return ipPort;
	}
	public void setIpPort(IpPortCfg ipPort) {
		this.ipPort = ipPort;
	}
	public HttpUrlCfg getHttpUrl() {
		return httpUrl;
	}
	public void setHttpUrl(HttpUrlCfg httpUrl) {
		this.httpUrl = httpUrl;
	}
	public HttpReqHeadCfg getHttpReqHdr() {
		return httpReqHdr;
	}
	public void setHttpReqHdr(HttpReqHeadCfg httpReqHdr) {
		this.httpReqHdr = httpReqHdr;
	}
	public HttpResHeadCfg getHttpResHdr() {
		return httpResHdr;
	}
	public void setHttpResHdr(HttpResHeadCfg httpResHdr) {
		this.httpResHdr = httpResHdr;
	}
	public HttpBodyCfg getHttpReqBody() {
		return httpReqBody;
	}
	public void setHttpReqBody(HttpBodyCfg httpReqBody) {
		this.httpReqBody = httpReqBody;
	}
	public HttpBodyCfg getHttpResBody() {
		return httpResBody;
	}
	public void setHttpResBody(HttpBodyCfg httpResBody) {
		this.httpResBody = httpResBody;
	}
	public List<IpPortCfg> getIpPortList() {
		return ipPortList;
	}
	public void setIpPortList(List<IpPortCfg> ipPortList) {
		this.ipPortList = ipPortList;
	}
	public List<HttpUrlCfg> getHttpUrlList() {
		return httpUrlList;
	}
	public void setHttpUrlList(List<HttpUrlCfg> httpUrlList) {
		this.httpUrlList = httpUrlList;
	}
	public List<HttpReqHeadCfg> getHttpReqHdrList() {
		return httpReqHdrList;
	}
	public void setHttpReqHdrList(List<HttpReqHeadCfg> httpReqHdrList) {
		this.httpReqHdrList = httpReqHdrList;
	}
	public List<HttpResHeadCfg> getHttpResHdrList() {
		return httpResHdrList;
	}
	public void setHttpResHdrList(List<HttpResHeadCfg> httpResHdrList) {
		this.httpResHdrList = httpResHdrList;
	}
	public List<HttpBodyCfg> getHttpReqBodyList() {
		return httpReqBodyList;
	}
	public void setHttpReqBodyList(List<HttpBodyCfg> httpReqBodyList) {
		this.httpReqBodyList = httpReqBodyList;
	}
	public List<HttpBodyCfg> getHttpResBodyList() {
		return httpResBodyList;
	}
	public void setHttpResBodyList(List<HttpBodyCfg> httpResBodyList) {
		this.httpResBodyList = httpResBodyList;
	}
	public List<ComplexkeywordCfg> getDomainList() {
		return domainList;
	}
	public void setDomainList(List<ComplexkeywordCfg> domainList) {
		this.domainList = domainList;
	}
	public List<ComplexkeywordCfg> getComplexList() {
		return complexList;
	}
	public void setComplexList(List<ComplexkeywordCfg> complexList) {
		this.complexList = complexList;
	}
	public List<BaseStringCfg> getStringList() {
		return stringList;
	}
	public void setStringList(List<BaseStringCfg> stringList) {
		this.stringList = stringList;
	}
	public List<FileDigestCfg> getDigestList() {
		return digestList;
	}
	public void setDigestList(List<FileDigestCfg> digestList) {
		this.digestList = digestList;
	}
	public P2pHashCfg getP2pHash() {
		return p2pHash;
	}
	public void setP2pHash(P2pHashCfg p2pHash) {
		this.p2pHash = p2pHash;
	}
	public P2pKeywordCfg getP2pKeyword() {
		return p2pKeyword;
	}
	public void setP2pKeyword(P2pKeywordCfg p2pKeyword) {
		this.p2pKeyword = p2pKeyword;
	}
	public List<P2pHashCfg> getP2pHashList() {
		return p2pHashList;
	}
	public void setP2pHashList(List<P2pHashCfg> p2pHashList) {
		this.p2pHashList = p2pHashList;
	}
	public List<P2pKeywordCfg> getP2pKeywordList() {
		return p2pKeywordList;
	}
	public void setP2pKeywordList(List<P2pKeywordCfg> p2pKeywordList) {
		this.p2pKeywordList = p2pKeywordList;
	}
	public static String getTablename() {
		return tableName;
	}
	public String getIndexTable() {
		return indexTable;
	}
	public void setIndexTable(String indexTable) {
		this.indexTable = indexTable;
	}
	public String getSearchKeywords() {
		return searchKeywords;
	}
	public void setSearchKeywords(String searchKeywords) {
		this.searchKeywords = searchKeywords;
	}
	public Map<String, Object> getUserRegion() {
		return userRegion;
	}
	public void setUserRegion(Map<String, Object> userRegion) {
		this.userRegion = userRegion;
	}
	public BaseStringCfg getStrCfg() {
		return strCfg;
	}
	public void setStrCfg(BaseStringCfg strCfg) {
		this.strCfg = strCfg;
	}
	public ComplexkeywordCfg getComplexCfg() {
		return complexCfg;
	}
	public void setComplexCfg(ComplexkeywordCfg complexCfg) {
		this.complexCfg = complexCfg;
	}
	
	
}
