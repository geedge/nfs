package com.nis.web.service.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nis.domain.configuration.AreaIpCfg;
import com.nis.domain.configuration.BaseCfg;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.ComplexkeywordCfg;
import com.nis.util.Constants;
import com.nis.web.dao.configuration.ComplexStringCfgDao;
import com.nis.web.dao.configuration.IpCfgDao;
import com.nis.web.service.CrudService;

/**
 * String相关配置事务类
 * @author dell
 *
 */
@Service
public class ComplexStringCfgService extends CrudService<ComplexStringCfgDao,ComplexkeywordCfg> {
	@Autowired
	protected ComplexStringCfgDao complexStringCfgDao;
	/**
	 * 
	 * addStringCfg(新增IP类配置)
	 * (继承ComplexkeywordCfg这个类方可使用)
	 * @param cfg
	 * @return 
	 *int
	 * @exception 
	 * @since  1.0.0
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public int addStringCfg(ComplexkeywordCfg cfg,List<BaseIpCfg> areaIpCfgs){
		if(areaIpCfgs!=null&&areaIpCfgs.size()>0){
//			this.saveIpBatch(areaIpCfgs);
		}
		return complexStringCfgDao.insert(cfg);
	} 
	/**
	 * 
	 * updateStringCfg(更新IP类配置)
	 * (继承ComplexkeywordCfg这个类方可使用)
	 * @param cfg
	 * @return 
	 *int
	 * @exception 
	 * @since  1.0.0
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public int updateStringCfg(ComplexkeywordCfg cfg,List<BaseIpCfg> addAreaCfg,List<BaseIpCfg> updateAreaCfg,List<BaseIpCfg> deleteAreaCfgs){
		if(addAreaCfg!=null&&addAreaCfg.size()>0){
//			this.saveIpBatch(addAreaCfg);
		}
		if(updateAreaCfg!=null&&updateAreaCfg.size()>0){
			this.updateIpBatch(updateAreaCfg);
		}
		if(deleteAreaCfgs!=null&&deleteAreaCfgs.size()>0){
			this.deleteIpBatch(deleteAreaCfgs);
		}
		return complexStringCfgDao.update(cfg);
	} 
	/**
	 * 
	 * auditStringCfg(审核IP类配置)
	 * (继承ComplexkeywordCfg这个类方可使用)
	 * @param cfg
	 * @return 
	 *int
	 * @throws Exception 
	 * @exception 
	 * @since  1.0.0
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void auditStringCfg(int isAduit,List<ComplexkeywordCfg> auditCfg,List<BaseIpCfg> areaCfg,List<ComplexkeywordCfg> sendCfg) throws Exception{
//		List<List<BaseCfg>> sendCfgs=new ArrayList<List<BaseCfg>>();
//		for(ComplexkeywordCfg c:sendCfg){
//			List<BaseCfg> cArr=new ArrayList<>();
//			cArr.add(c);
//			sendCfgs.add(cArr);
//		}
//		if(Constants.AUDIT_NOT_YES==isAduit||
//				Constants.AUDIT_YES==isAduit){//审核通过,取消审核通过需要发到maat
//			if(sendToMaatConvertorBatch(isAduit,sendCfgs)){
//				if(areaCfg!=null&&areaCfg.size()>0){
//					this.auditIpBatch(areaCfg);
//				}
//				this.auditBatch(auditCfg, ComplexStringCfgDao.class);
//			}
//		}else{
//			if(areaCfg!=null&&areaCfg.size()>0){
//				this.auditIpBatch(areaCfg);
//			}
//			this.auditBatch(auditCfg, ComplexStringCfgDao.class);
//		}
	} 
	/**
	 * 
	 * deleteStringCfg(删除IP类配置)
	 * (继承ComplexkeywordCfg这个类方可使用)
	 * @param cfg
	 * @return 
	 *int
	 * @exception 
	 * @since  1.0.0
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void deleteStringCfg(List<ComplexkeywordCfg> cfgs,List<BaseIpCfg> areaCfg){
		if(areaCfg!=null&&areaCfg.size()>0){
			this.deleteIpBatch(areaCfg);
		}
		this.deleteBatch(cfgs, ComplexStringCfgDao.class);
	} 
	/**
	 * 
	 * getStringCfg(根据IP与类名获取IP配置)
	 * (继承ComplexkeywordCfg这个类方可使用)
	 * @param clazz
	 * @param id
	 * @return 
	 *ComplexkeywordCfg
	 * @exception 
	 * @since  1.0.0
	 */
	public ComplexkeywordCfg getStringCfgById(ComplexkeywordCfg cfg){
		return complexStringCfgDao.getById(cfg.getTableName(),cfg.getCfgId());
	}
	public ComplexkeywordCfg getStringCfgById(String tableName,long id){
		return complexStringCfgDao.getById(tableName,id);
	}
	public Integer getIsValid(ComplexkeywordCfg cfg){
		return complexStringCfgDao.getIsValid(cfg);
	}
	public Integer getIsValid(String tableName, long id){
		return complexStringCfgDao.getIsValid(tableName,id);
	}
	public Integer getIsAudit(ComplexkeywordCfg cfg){
		return complexStringCfgDao.getIsAudit(cfg);
	}
	public Integer getIsAudit(String tableName, long id){
		return complexStringCfgDao.getIsAudit(tableName,id);
	}
	/**
	 * getList(这里用一句话描述这个方法的作用)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param tableName
	 * @param ids
	 * @return 
	 *List<BaseIpCfg>
	 * @exception 
	 * @since  1.0.0
	*/
	public List<ComplexkeywordCfg> getList(String tableName, String ids) {
		// TODO Auto-generated method stub
		return complexStringCfgDao.getList(tableName, ids);
	}
}
