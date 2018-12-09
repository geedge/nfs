package com.nis.web.service.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nis.domain.configuration.AreaIpCfg;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.NumBoundaryCfg;
import com.nis.util.Constants;
import com.nis.web.dao.configuration.NumCfgDao;
import com.nis.web.service.CrudService;

/**
 * Num相关配置事务类
 * @author dell
 *
 */
@Service
public class NumCfgService extends CrudService<NumCfgDao,NumBoundaryCfg> {
	@Autowired
	protected NumCfgDao numCfgDao;
	/**
	 * 
	 * addNumCfg(新增IP类配置)
	 * (继承NumBoundaryCfg这个类方可使用)
	 * @param cfg
	 * @return 
	 *int
	 * @exception 
	 * @since  1.0.0
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public int addNumCfg(NumBoundaryCfg cfg,List<BaseIpCfg> areaIpCfgs){
		if(areaIpCfgs!=null&&areaIpCfgs.size()>0){
//			this.saveIpBatch(areaIpCfgs);
		}
		return numCfgDao.insert(cfg);
	} 
	/**
	 * 
	 * updateNumCfg(更新IP类配置)
	 * (继承NumBoundaryCfg这个类方可使用)
	 * @param cfg
	 * @return 
	 *int
	 * @exception 
	 * @since  1.0.0
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public int updateNumCfg(NumBoundaryCfg cfg,List<BaseIpCfg> addAreaCfg,List<BaseIpCfg> updateAreaCfg,List<BaseIpCfg> deleteAreaCfgs){
		if(addAreaCfg!=null&&addAreaCfg.size()>0){
//			this.saveIpBatch(addAreaCfg);
		}
		if(updateAreaCfg!=null&&updateAreaCfg.size()>0){
			this.updateIpBatch(updateAreaCfg);
		}
		if(deleteAreaCfgs!=null&&deleteAreaCfgs.size()>0){
			this.deleteIpBatch(deleteAreaCfgs);
		}
		return numCfgDao.updateByPrimaryKeySelective(cfg);
	} 
	
	/**
	 * 
	 * deleteNumCfg(删除IP类配置)
	 * (继承NumBoundaryCfg这个类方可使用)
	 * @param cfg
	 * @return 
	 *int
	 * @exception 
	 * @since  1.0.0
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public int deleteNumCfg(NumBoundaryCfg cfg){
		return numCfgDao.updateValid(cfg);
	} 
	/**
	 * 
	 * getNumCfg(根据IP与类名获取IP配置)
	 * (继承NumBoundaryCfg这个类方可使用)
	 * @param clazz
	 * @param id
	 * @return 
	 *NumBoundaryCfg
	 * @exception 
	 * @since  1.0.0
	 */
	public NumBoundaryCfg getNumCfgById(long id){
		return numCfgDao.getById(id);
	}
	/**
	 * 
	 * getNumCfg(根据IP与类名获取IP配置)
	 * (继承NumBoundaryCfg这个类方可使用)
	 * @param clazz
	 * @param id
	 * @return 
	 *NumBoundaryCfg
	 * @exception 
	 * @since  1.0.0
	 */
	public NumBoundaryCfg getNumCfgById(NumBoundaryCfg cfg){
		return numCfgDao.getById(cfg.getCfgId());
	}
	public Integer getIsValid(NumBoundaryCfg cfg){
		return numCfgDao.getIsValid(cfg);
	}
	public Integer getIsValid(long id){
		return numCfgDao.getIsValid(id);
	}
	public Integer getIsAudit(NumBoundaryCfg cfg){
		return numCfgDao.getIsAudit(cfg);
	}
	public Integer getIsAudit(long id){
		return numCfgDao.getIsAudit(id);
	}
}
