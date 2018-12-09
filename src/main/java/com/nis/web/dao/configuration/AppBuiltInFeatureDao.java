package com.nis.web.dao.configuration;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.configuration.AppBuiltInFeatureFile;
import com.nis.domain.configuration.DnsResStrategy;
import com.nis.domain.configuration.PxyObjKeyring;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;

@MyBatisDao
public interface AppBuiltInFeatureDao extends CrudDao< AppBuiltInFeatureFile> {
	List<AppBuiltInFeatureFile> findPage( AppBuiltInFeatureFile  entity);
	List<AppBuiltInFeatureFile> findList(@Param("cfgId")Long cfgId
			,@Param("isAudit")Integer isAudit
			,@Param("isValid")Integer isValid);
	
}