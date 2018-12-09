/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.nis.web.service;

import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.nis.domain.BaseEntity;
import com.nis.domain.Page;
import com.nis.domain.configuration.AreaIpCfg;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.ComplexkeywordCfg;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.configuration.AreaIpCfgDao;
import com.nis.web.dao.configuration.IpCfgDao;
import com.nis.web.dao.configuration.StringCfgDao;
import com.nis.web.dao.configuration.WebsiteCfgDao;

/**
 * Service基类
 * @author ThinkGem
 * @version 2014-05-16
 */
public abstract class CrudService<D extends CrudDao<T>, T extends BaseEntity<T>> extends BaseService {
	
	/**
	 * 持久层对象
	 */
	@Autowired
	protected D dao;
	
	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	public T get(Long id) {
		return dao.get(id);
	}
	
	/**
	 * 获取单条数据
	 * @param entity
	 * @return
	 */
	public T get(T entity) {
		return dao.get(entity);
	}
	
	/**
	 * 查询列表数据
	 * @param entity
	 * @return
	 */
	public List<T> findList(T entity) {
		return dao.findList(entity);
	}
	
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<T> findPage(Page<T> page, T entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"r"));
		entity.setPage(page);
		page.setList(dao.findList(entity));
		return page;
	}

	/**
	 * 保存数据（插入或更新）
	 * @param entity
	 */
	public void save(T entity) {
		if (entity.getIsNewRecord()){
			dao.insert(entity);
		}else{
			dao.update(entity);
		}
	}
	
	/**
	 * 删除数据
	 * @param entity
	 */
	public void delete(T entity) {
		dao.delete(entity);
	}
	
	/**
	 * 
	 * saveBatch(批量全部保存数据)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param data 数据集合
	 * @param mClass 传入的dao.xml里的mapper 
	 * @author wx
	 * @since  1.0.0
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveBatch(List<T> data,@SuppressWarnings("rawtypes")Class mClass) {
		SqlSessionFactory sqlSessionFactory=SpringContextHolder.getBean(SqlSessionFactory.class);
        SqlSession batchSqlSession = null;
        try{
//        	int batchSize=1000;
            batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
            for(int index = 0; index < data.size();index++){
                T t = data.get(index);
                //insertForBatch不要带上ID，会影响效率
                ((CrudDao<T>) batchSqlSession.getMapper(mClass)).insertForBatch(t);
//                if(index>0&&index%batchSize==0) {
//                	 batchSqlSession.commit();
//                	 batchSqlSession.clearCache();
//                	 logger.warn("session commit");
//                }
            }
            batchSqlSession.commit();
        }finally {
            if(batchSqlSession != null){
                batchSqlSession.close();
            }
        }
    }
	
	
	/**
	 * 
	 * saveIpBatch(非Ip类配置调用，批量新增区域IP)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param areaIpCfgs 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	public void saveIpBatch(List<AreaIpCfg> areaIpCfgs) {
		SqlSessionFactory sqlSessionFactory=SpringContextHolder.getBean(SqlSessionFactory.class);
        SqlSession batchSqlSession = null;
        try{
            batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
            for(int index = 0; index < areaIpCfgs.size();index++){
            	AreaIpCfg t = areaIpCfgs.get(index);
                ((CrudDao<AreaIpCfg>) batchSqlSession.getMapper(AreaIpCfgDao.class)).insert(t);
            }
            batchSqlSession.commit();
        }finally {
            if(batchSqlSession != null){
                batchSqlSession.close();
            }
        }
    }
	/**
	 * 
	 * updateBatch(批量更新全部数据)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param areaCfg 数据集合
	 * @param mClass 传入的dao.xml里的mapper 
	 * @author wx
	 * @since  1.0.0
	 */
	@SuppressWarnings("unchecked")
	public void updateBatch(List<T> data,@SuppressWarnings("rawtypes") Class mClass) {
		SqlSessionFactory sqlSessionFactory=SpringContextHolder.getBean(SqlSessionFactory.class);
		SqlSession batchSqlSession = null;
		try{
			batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
			for(int index = 0; index < data.size();index++){
				T t = data.get(index);
				((CrudDao<T>) batchSqlSession.getMapper(mClass)).update(t);
			}
			batchSqlSession.commit();
		}finally {
			if(batchSqlSession != null){
				batchSqlSession.close();
			}
		}
	}
	/**
	 * 
	 * updateIpBatch(非IP类配置调用，批量更新区域IP)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param areaCfg 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	public void updateIpBatch(List<BaseIpCfg> areaCfg) {
		SqlSessionFactory sqlSessionFactory=SpringContextHolder.getBean(SqlSessionFactory.class);
        SqlSession batchSqlSession = null;
        try{
            batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
            for(int index = 0; index < areaCfg.size();index++){
            	BaseIpCfg t = areaCfg.get(index);
                ((CrudDao<BaseIpCfg>) batchSqlSession.getMapper(IpCfgDao.class)).update(t);
            }
            batchSqlSession.commit();
        }finally {
            if(batchSqlSession != null){
                batchSqlSession.close();
            }
        }
    }
	/**
	 * 
	 * deleteBatch(批量删除全部数据)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param data 数据集合
	 * @param mClass 传入的dao.xml里的mapper 
	 * @author wx
	 * @since  1.0.0
	 */
	@SuppressWarnings("unchecked")
	public void deleteBatch(List<T> data,@SuppressWarnings("rawtypes") Class mClass) {
		SqlSessionFactory sqlSessionFactory=SpringContextHolder.getBean(SqlSessionFactory.class);
		SqlSession batchSqlSession = null;
		try{
			batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
			for(int index = 0; index < data.size();index++){
				T t = data.get(index);
				((CrudDao<T>) batchSqlSession.getMapper(mClass)).delete(t);
			}
			batchSqlSession.commit();
		}finally {
			if(batchSqlSession != null){
				batchSqlSession.close();
			}
		}
	}
	/**
	 * 
	 * auditBatch(批量审核)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param data 数据集合
	 * @param mClass 传入的dao.xml里的mapper 
	 * @author wx
	 * @since  1.0.0
	 */
	@SuppressWarnings("unchecked")
	public void auditBatch(List<T> data,@SuppressWarnings("rawtypes") Class mClass) {
		SqlSessionFactory sqlSessionFactory=SpringContextHolder.getBean(SqlSessionFactory.class);
		SqlSession batchSqlSession = null;
		try{
			batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
			for(int index = 0; index < data.size();index++){
				T t = data.get(index);
				((CrudDao<T>) batchSqlSession.getMapper(mClass)).audit(t);
			}
			batchSqlSession.commit();
		}finally {
			if(batchSqlSession != null){
				batchSqlSession.close();
			}
		}
	}
	/**
	 * 
	 * auditIpBatch(非IP类配置用，审核区域IP)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param data 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	public void auditIpBatch(List<BaseIpCfg> data) {
		SqlSessionFactory sqlSessionFactory=SpringContextHolder.getBean(SqlSessionFactory.class);
		SqlSession batchSqlSession = null;
		try{
			batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
			for(int index = 0; index < data.size();index++){
				BaseIpCfg t = data.get(index);
				((CrudDao<BaseIpCfg>) batchSqlSession.getMapper(IpCfgDao.class)).audit(t);
			}
			batchSqlSession.commit();
		}finally {
			if(batchSqlSession != null){
				batchSqlSession.close();
			}
		}
	}
	/**
	 * 
	 * deleteIpBatch(非IP类配置调用，批量删除区域IP)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param data 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	public void deleteIpBatch(List<BaseIpCfg> data) {
		SqlSessionFactory sqlSessionFactory=SpringContextHolder.getBean(SqlSessionFactory.class);
		SqlSession batchSqlSession = null;
		try{
			batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
			for(int index = 0; index < data.size();index++){
				BaseIpCfg t = data.get(index);
				((CrudDao<BaseIpCfg>) batchSqlSession.getMapper(IpCfgDao.class)).delete(t);
			}
			batchSqlSession.commit();
        }finally {
			if(batchSqlSession != null){
				batchSqlSession.close();
			}
		}
	}
}
