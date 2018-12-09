package com.nis.web.dao.configuration;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.configuration.BaseCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.ComplexkeywordCfg;
import com.nis.domain.configuration.FileDigestCfg;
import com.nis.domain.configuration.IpPortCfg;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;


/**
 * 邮件相关配置数据处理类
 * @author dell
 *
 */
@MyBatisDao
public interface MailCfgDao  extends CrudDao<CfgIndexInfo>{
	public CfgIndexInfo getCfgIndexInfo(@Param("cfgId")Long id,@Param("compileId")Integer compileId);
	public List<CfgIndexInfo> getMailList(CfgIndexInfo entity);
	public List<IpPortCfg> getIpPortList(CfgIndexInfo entity);
	public List<ComplexkeywordCfg> getMailKeywordList(CfgIndexInfo entity);
	public List<FileDigestCfg> getMailFileDigestList(CfgIndexInfo entity);
	public void saveCfgIndex(CfgIndexInfo entity);
	public void saveIpPortCfg(IpPortCfg entity);
	public void saveMailKeywordCfg(ComplexkeywordCfg entity);
	public void saveMailFileDigestCfg(FileDigestCfg entity);
	public void deleteMailIpCfg(CfgIndexInfo entity);
	public void deleteMailKeywordCfg(CfgIndexInfo entity);
	public void deleteMailFileDigestCfg(CfgIndexInfo entity);
	public void updateCfgIndex(CfgIndexInfo entity);
	public void updateCfgValid(BaseCfg entity);
	public void auditCfg(BaseCfg entity);
}
