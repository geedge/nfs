package com.nis.web.dao.configuration;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.configuration.BaseCfg;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.ComplexkeywordCfg;
import com.nis.domain.configuration.FileDigestCfg;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.domain.configuration.NtcSubscribeIdCfg;
import com.nis.domain.configuration.P2pHashCfg;
import com.nis.domain.configuration.P2pKeywordCfg;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;


/**
 * 文件传输相关配置数据处理类
 * @author dell
 *
 */
@MyBatisDao
public interface FileTransferCfgDao extends CrudDao<CfgIndexInfo> {
	public CfgIndexInfo getCfgIndexInfo(@Param("cfgId")Long id,@Param("compileId")Integer compileId);
	public List<CfgIndexInfo> getFtpList(CfgIndexInfo entity);
	public List<IpPortCfg> getIpPortList(CfgIndexInfo entity);
	public List<BaseStringCfg> getFtpKeywordList(CfgIndexInfo entity);
	public List<FileDigestCfg> getFileDigestList(FileDigestCfg entity);
	public void saveCfgIndex(CfgIndexInfo entity);
	public void saveIpPortCfg(IpPortCfg entity);
	public void saveFtpKeywordCfg(BaseStringCfg entity);
	public void deleteFtpIpCfg(CfgIndexInfo entity);
	public void deleteFtpKeywordCfg(CfgIndexInfo entity);
	public void deleteFileDigestCfg(CfgIndexInfo entity);
	public void updateCfgIndex(CfgIndexInfo entity);
	public void updateCfgValid(BaseCfg entity);
	public void auditCfg(BaseCfg entity);
	public FileDigestCfg getFileDigest(@Param("cfgId")Long id,@Param("compileId")Integer compileId);
	public void updateFileDigestCfg(FileDigestCfg entity);
	public void deleteFileDigestSubscribeIdCfg(FileDigestCfg entity);
	public List<NtcSubscribeIdCfg> getFileDigestSubscribeidList(FileDigestCfg entity);
	
	// P2P域配置新增
	public void saveP2pHashCfg(BaseStringCfg cfg);
	public void saveP2pKeywordCfg(BaseStringCfg cfg);
	public void saveP2pSubscribeIdCfg(NtcSubscribeIdCfg cfg);
	// 获取P2P域配置信息
	public List<CfgIndexInfo> getP2pList(CfgIndexInfo entity);
	public List<P2pHashCfg> getP2pHashList(CfgIndexInfo entity);
	public List<P2pKeywordCfg> getP2pKeywordList(CfgIndexInfo entity);
	public List<NtcSubscribeIdCfg> getP2pSubscribeidList(CfgIndexInfo entity);
	// P2P修改配置操作时，先删除旧域配置信息
	public void deleteP2pIpCfg(CfgIndexInfo entity);
	public void deleteP2pHashCfg(CfgIndexInfo entity);
	public void deleteP2pKeywordCfg(CfgIndexInfo entity);
	public void deleteP2pSubscribeIdCfg(CfgIndexInfo entity);
	
}
