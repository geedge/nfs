package com.nis.web.service.basics;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nis.domain.Page;
import com.nis.domain.basics.AsnIpCfg;
import com.nis.domain.basics.IpReuseIpCfg;
import com.nis.domain.callback.InlineIp;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.maat.ToMaatResult;
import com.nis.domain.specific.ConfigGroupInfo;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.Constants;
import com.nis.web.dao.basics.IpReuseIpCfgDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.BaseService;



/**
 * 音视频文本
 * @author dell
 *
 */
@Service
public class IpReuseIpCfgService extends BaseService{
	@Autowired
	protected IpReuseIpCfgDao ipReuseIpCfgDao;
	
	
	public Page<IpReuseIpCfg> findPage(Page page, IpReuseIpCfg entity) {
		entity.getSqlMap().put("dsf", configScopeFilter(entity.getCurrentUser(),"r"));
		entity.setPage(page);
		List<IpReuseIpCfg> list=ipReuseIpCfgDao.findPage(entity);
		page.setList(list);
		return page;
	}
	
	public List<IpReuseIpCfg>  findList(IpReuseIpCfg entity){
		List<IpReuseIpCfg> list=ipReuseIpCfgDao.findPage(entity);
		return list;
	}
	public IpReuseIpCfg getUserIpCfg(Long cfgId) {
		return ipReuseIpCfgDao.getUserIpCfg(cfgId);
	}
	
	public IpReuseIpCfg getIpByIp(String destIpAddress) {
		return ipReuseIpCfgDao.getIpByIp(destIpAddress);
	}

	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void save(CfgIndexInfo entity){
		Date createTime=new Date();
			for(IpReuseIpCfg cfg:entity.getIpReuseIpCfgs()) {
				if(cfg.getCfgId()==null){
				BeanUtils.copyProperties(entity, cfg, new String[]{"cfgId"});
				cfg.setCreateTime(createTime);
				cfg.setCreatorId(entity.getCurrentUser().getId());
				cfg.setIsValid(Constants.VALID_NO);
				ipReuseIpCfgDao.insert(cfg);
			}
		}
	}
	
	public void update(IpReuseIpCfg entity){
		Date editTime=new Date();
		entity.setEditTime(editTime);
		entity.setEditorId(entity.getCurrentUser().getId());
		ipReuseIpCfgDao.update(entity);
		
	}
	
	/**
	 * 
	 * @param isAudit
	 * @param isValid
	 * @param ids compileIds
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void delete(Integer isValid,String ids){
		String[] idArray = ids.split(",");
		for(String id :idArray){
			IpReuseIpCfg entity = new IpReuseIpCfg();
			entity.setCfgId(Long.valueOf(id));
			entity.setIsValid(isValid);
			entity.setEditorId(UserUtils.getUser().getId());
			entity.setEditTime(new Date());
			ipReuseIpCfgDao.update(entity);
		}
	}
	
	/**
	 * 策略管理：获取所有IP信息
	 * @param addrPoolId
	 * @return
	 */
	public List<IpReuseIpCfg> getPolicyIpInfo(Integer addrPoolId) {
		return ipReuseIpCfgDao.getPolicyIpInfo(addrPoolId);
	}
}
