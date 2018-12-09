package com.nis.web.dao.configuration;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.configuration.AvFileSampleCfg;
import com.nis.domain.configuration.AvSignSampleCfg;
import com.nis.web.dao.MyBatisDao;

@MyBatisDao
public interface AvCfgDao {

	public AvFileSampleCfg getAvFileSampleById(@Param("cfgId")Long cfgId,@Param("compileId")Integer compileId);
	public AvSignSampleCfg getAvSignSampleById(Long cfgId);
	public List<AvFileSampleCfg> getAvFileSampleList(AvFileSampleCfg entity);	
	public List<AvSignSampleCfg> getAvSignSampleList(AvSignSampleCfg entity);	
	public void insertAvFileSample(AvFileSampleCfg entity);
	public void insertAvSignSample(AvSignSampleCfg entity);
	public void updateAvFileSample(AvFileSampleCfg entity);
	public void updateAvSignSample(AvSignSampleCfg entity);
	public void updateAvFileSampleValid(AvFileSampleCfg entity);
	public void updateAvSignSampleValid(AvSignSampleCfg entity);
	public void auditAvFileSample(AvFileSampleCfg entity);
	public void auditAvSignSample(AvSignSampleCfg entity);
}
