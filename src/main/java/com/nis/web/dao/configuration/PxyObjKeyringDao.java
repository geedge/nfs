package com.nis.web.dao.configuration;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.configuration.PxyObjKeyring;
import com.nis.domain.configuration.PxyObjTrustedCaCert;
import com.nis.domain.configuration.PxyObjTrustedCaCrl;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;

@MyBatisDao
public interface PxyObjKeyringDao  extends CrudDao<PxyObjKeyring>{
	List<PxyObjKeyring> findPage( PxyObjKeyring  pxyObjKeyring);
	List<PxyObjTrustedCaCert> findTrustedCertPage( PxyObjTrustedCaCert  PxyObjTrustedCaCert);
	List<PxyObjKeyring> findList(@Param("cfgId")Long cfgId
			,@Param("isAudit")Integer isAudit
			,@Param("isValid")Integer isValid,@Param("cfgType")String cfgType);
	PxyObjTrustedCaCert getPxyObjTrustedCaCert(Long  id);
	PxyObjTrustedCaCrl getPxyObjTrustedCaCrl(PxyObjTrustedCaCrl  entity);
	void insertPxyObjTrustedCaCert(PxyObjTrustedCaCert PxyObjTrustedCaCert);
	void updatePxyObjTrustedCaCert(PxyObjTrustedCaCert PxyObjTrustedCaCert);
	void insertPxyObjTrustedCaCrl(PxyObjTrustedCaCrl pxyObjTrustedCaCrl);
	void updatePxyObjTrustedCaCrl(PxyObjTrustedCaCrl pxyObjTrustedCaCrl);
	
}