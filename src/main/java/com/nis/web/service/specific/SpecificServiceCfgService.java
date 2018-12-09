package com.nis.web.service.specific;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beust.jcommander.internal.Lists;
import com.nis.domain.Page;
import com.nis.domain.specific.ConfigGroupInfo;
import com.nis.domain.specific.SpecificServiceCfg;
import com.nis.util.AsnCacheUtils;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.Constants;
import com.nis.web.dao.specific.ConfigGroupInfoDao;
import com.nis.web.dao.specific.SpecificServiceCfgDao;
import com.nis.web.service.BaseService;

@Service
public class SpecificServiceCfgService extends BaseService{
	
	@Autowired
    private SpecificServiceCfgDao specificServiceCfgDao;
	@Autowired
	private ConfigGroupInfoDao configGroupInfoDao;
	
	/**
	 * 根据id查询对象
	 * @param specServiceId
	 * @return
	 */
	public SpecificServiceCfg getBySpecServiceId(Integer specServiceId) {
		return specificServiceCfgDao.getBySpecServiceId(specServiceId);
	}
	public List<SpecificServiceCfg> getBySpecServiceCodes(String specServiceCodes) {
		for(String specServiceId:specServiceCodes.split(",")) {
			Integer.parseInt(specServiceId);
		}
		return specificServiceCfgDao.getBySpecServiceCodes(specServiceCodes);
	}
	/**
	 * 查询所有符合条件的顶层分页
	 * @param page
	 * @param specificServiceCfg
	 * @return
	 */
	public Page<SpecificServiceCfg> findTopPage(Page<SpecificServiceCfg> page, SpecificServiceCfg specificServiceCfg){
		// 设置分页参数 
		specificServiceCfg.setPage(page);
		// 执行分页查询
		List<SpecificServiceCfg> list = Lists.newArrayList();
		list = specificServiceCfgDao.findTopPage(specificServiceCfg);
		SpecificServiceCfg ss = new SpecificServiceCfg();
		ss.setSpecServiceId(0);
		for(SpecificServiceCfg ssc:list){
			ssc.setParent(ss);
		}
		page.setList(list);
		return page;
	}
	/**
	 * 查询所有符合条件的数据
	 * @param specificServiceCfg
	 * @return
	 */
	public List<SpecificServiceCfg> findAllSpecificServiceCfg(SpecificServiceCfg specificServiceCfg, String orderBy) {
		return specificServiceCfgDao.findAllSpecificServiceCfg(specificServiceCfg,orderBy);
	}
	/**
	 * 保存或修改
	 * @param specificServiceCfg
	 * @throws Exception 
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveOrUpdate(SpecificServiceCfg specificServiceCfg) throws Exception {
		if(specificServiceCfg.getGroupId()==null || specificServiceCfg.getGroupId()==0){
			Integer groupId = ConfigServiceUtil.getId(2, 1).get(0);
			specificServiceCfg.setGroupId(groupId);
		}
		//新增协议分组
		ConfigGroupInfo group = configGroupInfoDao.getConfigGroupInfoByGroupId(specificServiceCfg.getGroupId());
		if(group==null){
			group = new ConfigGroupInfo();
			group.setGroupId(specificServiceCfg.getGroupId());
			group.setGroupName(specificServiceCfg.getSpecServiceName());
			group.setIsIssued(0);
			group.setGroupType(1);
			configGroupInfoDao.insertConfigGroupInfo(group);
		}
		if(specificServiceCfg.getSpecServiceId()==null){//新增
			if(specificServiceCfg.getIsLeaf()==null){
				if(specificServiceCfg.getParent().getSpecServiceId().equals(0)){
					specificServiceCfg.setIsLeaf(0);
				}else{
					specificServiceCfg.setIsLeaf(1);
				}
			}
			
			specificServiceCfg.setIsValid(1);
			specificServiceCfg.setOpTime(new Date());
			//用户添加的需要自增
			if(specificServiceCfg.getAddFlag()!=null) {
				Integer serviceCode=specificServiceCfgDao.getMaxServiceCode(Constants.APP_SPEC_SERVICE_CODE_MAX_VAL,specificServiceCfg.getCfgType(),specificServiceCfg.getAddFlag());
				if(serviceCode==null) {//用户没有添加标签，获取最大的
					serviceCode=specificServiceCfgDao.getMaxServiceCode(Constants.APP_SPEC_SERVICE_CODE_MAX_VAL,specificServiceCfg.getCfgType(),null);
				}
				if(serviceCode==null) {//没有标签，取开始值
					serviceCode=Constants.APP_SPEC_SERVICE_CODE_MIN_VAL;
				}else if(serviceCode>Constants.APP_SPEC_SERVICE_CODE_MAX_VAL){
					throw new RuntimeException("Get specific service code failed,specific service code is beyond the scope of application");
				}else if(serviceCode<Constants.APP_SPEC_SERVICE_CODE_MIN_VAL) {
					serviceCode=Constants.APP_SPEC_SERVICE_CODE_MIN_VAL;
				}
				specificServiceCfg.setSpecServiceCode(serviceCode.intValue()+1);
			}
			specificServiceCfgDao.insert(specificServiceCfg);
		}else{//修改
			if(specificServiceCfg.getIsLeaf()==null){
				if(specificServiceCfg.getParent().getSpecServiceId().equals(0)){
					specificServiceCfg.setIsLeaf(0);
				}else{
					specificServiceCfg.setIsLeaf(1);
				}
			}
			specificServiceCfg.setOpTime(new Date());
			specificServiceCfgDao.update(specificServiceCfg);
		}
	}
	/**
	 * 删除
	 * @param specificServiceCfg
	 * @throws Exception 
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void delete(String mulitId){
		String[] ids = mulitId.split(",");
		for(String id:ids){
			if (!id.equals("")) {
				SpecificServiceCfg specificServiceCfg = specificServiceCfgDao.getBySpecServiceId(Integer.valueOf(id));
				List<SpecificServiceCfg> list = Lists.newArrayList();
				//找出所有下级
				if(specificServiceCfg!=null){
					SpecificServiceCfg.sortList(list, specificServiceCfgDao.findAllSpecificServiceCfg(new SpecificServiceCfg(),""), specificServiceCfg.getSpecServiceId(), true);
					list.add(specificServiceCfg);
					for(SpecificServiceCfg ss:list){
						ss.setIsValid(0);
						specificServiceCfgDao.delete(ss);
					}
				}
			}
		}
	}
	
	/**
	 * 根据specServiceId查询所有下级
	 * @param specServiceId
	 * @return
	 */
	public List<SpecificServiceCfg> getChildrenById(Integer specServiceId) {
		return specificServiceCfgDao.getChildrenById(specServiceId);
	}
	
	public Integer getParentType(Integer specServiceId) {
		return specificServiceCfgDao.getParentType(specServiceId);
	}
	public Integer getParentCode(Integer specServiceId) {
		return specificServiceCfgDao.getParentCode(specServiceId);
	}
	public SpecificServiceCfg getRepeat (Integer code,Integer cfgType,Integer parentId) {
		return specificServiceCfgDao.getRepeat(code,cfgType,parentId);
	}
	public Integer getMaxServiceCode (Integer code, Integer cfgType,Integer addFlag) {
		return specificServiceCfgDao.getMaxServiceCode(code,cfgType,addFlag);
	}
	
}
