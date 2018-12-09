package com.nis.web.service.specific;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nis.domain.ImportErrorInfo;
import com.nis.domain.Page;
import com.nis.domain.SysUser;
import com.nis.domain.specific.SpecificServiceCfg;
import com.nis.domain.specific.SpecificServiceHostCfg;
import com.nis.util.BasicProvingUtil;
import com.nis.util.StringUtil;
import com.nis.web.dao.specific.SpecificServiceHostCfgDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.BaseService;

@Service
public class SpecificServiceHostCfgService extends BaseService{
	
	@Autowired
     private SpecificServiceHostCfgDao specificServiceHostCfgDao;

	/**
	 * 根据主键查询数据对象
	 * @param hostId
	 * @return
	 */
	public SpecificServiceHostCfg getDictByHostId(Integer hostId) {
		return specificServiceHostCfgDao.getByHostId(hostId);
	}

	/**
	 * 查出分页
	 * @param page
	 * @param specificServiceHostCfg
	 * @return
	 */
	public Page<SpecificServiceHostCfg> findSpecHostList(Page<SpecificServiceHostCfg> page,
			SpecificServiceHostCfg specificServiceHostCfg) {
		specificServiceHostCfg.setPage(page);
		page.setList(specificServiceHostCfgDao.findSpecHostList(specificServiceHostCfg));
		return page;
	}

	/**
	 * 新增或修改
	 * @param specificServiceHostCfg
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveOrUpdate(SpecificServiceHostCfg specificServiceHostCfg) {
		SysUser user = UserUtils.getUser();
		String defaultIp = "0.0.0.0"; //缺省0.0.0.0值表示任意
		if(specificServiceHostCfg.getIpType().equals(4)){
			if(specificServiceHostCfg.getIpPattern()==1){
				defaultIp = "0.0.0.0/32"; //0.0.0.0表示任意
			}else if(specificServiceHostCfg.getIpPattern()==2){
				defaultIp = "0.0.0.0-0.0.0.0"; 
			}else{
				defaultIp = "0.0.0.0";
			}
			
		}
		if(specificServiceHostCfg.getIpType().equals(6)){
			if(specificServiceHostCfg.getIpPattern()==1){
				defaultIp = "::/64";
			}else if(specificServiceHostCfg.getIpPattern()==2){
				defaultIp = "::-::"; 
			}else{
				defaultIp = "::";
			}
		}
		String defaultPort = "0"; //0表示任意
		if(specificServiceHostCfg.getPortPattern().equals(1)){
			defaultPort = "0";
		}else{
			defaultPort = "0/0";
		}
		//ip地址默认  缺省0.0.0.0值表示任意
		if(StringUtil.isBlank(specificServiceHostCfg.getSrcIpAddress())){
			specificServiceHostCfg.setSrcIpAddress(defaultIp);
		}
		if(StringUtil.isBlank(specificServiceHostCfg.getDestIpAddress())){
			specificServiceHostCfg.setDestIpAddress(defaultIp);
		}
		//端口掩码默认
		if(StringUtil.isBlank(specificServiceHostCfg.getSrcPort())){
			specificServiceHostCfg.setSrcPort(defaultPort);
		}
		if(StringUtil.isBlank(specificServiceHostCfg.getDestPort())){
			specificServiceHostCfg.setDestPort(defaultPort);
		}
		//方向缺省
		if(specificServiceHostCfg.getDirection()==null){
			specificServiceHostCfg.setDirection(0);
		}
		Date date = new Date();
		if(specificServiceHostCfg.getHostId()==null){//新增
			specificServiceHostCfg.setIsValid(1);
			specificServiceHostCfg.setCreator(user);
			specificServiceHostCfg.setCreateTime(date);
			specificServiceHostCfg.setEditor(user);
			specificServiceHostCfg.setEditTime(date);
			specificServiceHostCfg.setAuditor(user);
			specificServiceHostCfg.setAuditTime(date);
			specificServiceHostCfgDao.insert(specificServiceHostCfg);
		}else{//修改
			//是否进行了审核操作
			//SpecificServiceHostCfg ssh = specificServiceHostCfgDao.getByHostId(specificServiceHostCfg.getHostId());
			/*if(ssh.getIsAudit()!=specificServiceHostCfg.getIsAudit()){
				specificServiceHostCfg.setAuditor(user);
				specificServiceHostCfg.setAuditTime(date);
			}*/
			specificServiceHostCfg.setEditor(user);
			specificServiceHostCfg.setEditTime(date);
			specificServiceHostCfg.setAuditor(user);
			specificServiceHostCfg.setAuditTime(date);
			specificServiceHostCfgDao.update(specificServiceHostCfg);
		}
	}

	/**
	 * 删除
	 * @param mulitId
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void delete(String mulitId) {
		String[] ids = mulitId.split(",");
		for(String hostId:ids){
			if (!StringUtils.isEmpty(hostId)) {
				specificServiceHostCfgDao.delete(Integer.valueOf(hostId));
			}
		}
	}

	/**
	 * 根据协议ID查询对象
	 * @param specServiceId
	 * @return
	 */
	public SpecificServiceHostCfg getBySpecServiceId(Integer specServiceId) {
		
		return specificServiceHostCfgDao.getBySpecServiceId(specServiceId);
	}
	

	/**
	 * 导入文件
	 * @param ei
	 */
	public List<ImportErrorInfo> importFile(List<SpecificServiceHostCfg> list,List<SpecificServiceCfg> listSpecService,Properties msgProp) throws Exception  {
		List<ImportErrorInfo> importErrorInfos = new ArrayList<ImportErrorInfo>();
				//特定服务信息<协议名称,协议ID>
				Map<String, Integer> serviceIdMap = new HashMap<String, Integer>();
				for (SpecificServiceCfg specificServiceCfg : listSpecService) {
					serviceIdMap.put(specificServiceCfg.getSpecServiceName(), specificServiceCfg.getSpecServiceId());
				}
				for (int i = 0; i < list.size(); i++) {
					SpecificServiceHostCfg specificServiceHostCfg = list.get(i);
					ImportErrorInfo importErrorInfo = null;
					boolean valFlag = true;
					SysUser user = UserUtils.getUser();
					String defaultIp = "0.0.0.0"; //缺省0.0.0.0值表示任意
					String defaultIpMask = "";
					//验证
					//协议名称=>协议ID
					if(StringUtil.isEmpty(specificServiceHostCfg.getSpecServiceName())||!(!StringUtil.isEmpty(specificServiceHostCfg.getSpecServiceName())&&serviceIdMap.containsKey(specificServiceHostCfg.getSpecServiceName()))){
						logger.info(msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_protocol_id"));
						importErrorInfo=new ImportErrorInfo(i+3+"",msgProp.getProperty("protocol"),msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_protocol_id"));
						importErrorInfos.add(importErrorInfo);
						valFlag = false;
						//throw new RuntimeException(msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_protocol_id"));
					}else{
						specificServiceHostCfg.setSpecServiceId(serviceIdMap.get(specificServiceHostCfg.getSpecServiceName()));
					}
					//IP类型
					if(!(!StringUtil.isEmpty(specificServiceHostCfg.getIpType())&&("4".equals((specificServiceHostCfg.getIpType().toString()))||"6".equals((specificServiceHostCfg.getIpType().toString()))))){
						logger.info(msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_ip_type"));
						importErrorInfo=new ImportErrorInfo(i+3+"",msgProp.getProperty("ip_type"),msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_ip_type"));
						importErrorInfos.add(importErrorInfo);
						valFlag = false;
//						throw new RuntimeException(msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_ip_type"));
					}
					
					//源IP
					/*if(!StringUtil.isEmpty(specificServiceHostCfg.getSrcIp())&&!BasicProvingUtil.isIpOrIpMask(specificServiceHostCfg.getSrcIp(), specificServiceHostCfg.getIpType())){
						logger.info(msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_src_ip"));
//						throw new RuntimeException(msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_src_ip"));
						importErrorInfo=new ImportErrorInfo(i+3+"",msgProp.getProperty("client_ip"),msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_src_ip"));
						importErrorInfos.add(importErrorInfo);
						valFlag = false;
					}
					//源IP掩码
					if(!StringUtil.isEmpty(specificServiceHostCfg.getSrcIpMask())&&!BasicProvingUtil.isIpOrIpMask(specificServiceHostCfg.getSrcIpMask(), specificServiceHostCfg.getIpType())){
						logger.info(msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_src_mask"));
//						throw new RuntimeException(msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_src_mask"));
						importErrorInfo=new ImportErrorInfo(i+3+"",msgProp.getProperty("client_address_mask"),msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_src_mask"));
						importErrorInfos.add(importErrorInfo);
						valFlag = false;
					}
					//源端口
					if(!(!StringUtil.isEmpty(specificServiceHostCfg.getSrcPort())&&BasicProvingUtil.isPortOrPortMask(specificServiceHostCfg.getSrcPort()))){
						logger.info(msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_src_port"));
//						throw new RuntimeException(msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_src_port"));
						importErrorInfo=new ImportErrorInfo(i+3+"",msgProp.getProperty("client_port"),msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_src_port"));
						importErrorInfos.add(importErrorInfo);
						valFlag = false;
					}
					//源端口掩码
					if(!StringUtil.isEmpty(specificServiceHostCfg.getSrcPortMask())&&!BasicProvingUtil.isPortOrPortMask(specificServiceHostCfg.getSrcPortMask())){
						logger.info(msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_src_port_mask"));
//						throw new RuntimeException(msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_src_port_mask"));
						importErrorInfo=new ImportErrorInfo(i+3+"",msgProp.getProperty("client_port_mask"),msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_src_port_mask"));
						importErrorInfos.add(importErrorInfo);
						valFlag = false;
					}
					//目的IP
					if(!StringUtil.isEmpty(specificServiceHostCfg.getDstIp())&&!BasicProvingUtil.isIpOrIpMask(specificServiceHostCfg.getDstIp(), specificServiceHostCfg.getIpType())){
						logger.info(msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_dst_ip"));
//						throw new RuntimeException(msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_dst_ip"));
						importErrorInfo=new ImportErrorInfo(i+3+"",msgProp.getProperty("server_ip"),msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_dst_ip"));
						importErrorInfos.add(importErrorInfo);
						valFlag = false;
					}
					//目的IP掩码
					if(!StringUtil.isEmpty(specificServiceHostCfg.getDstIpMask())&&!BasicProvingUtil.isIpOrIpMask(specificServiceHostCfg.getDstIpMask(), specificServiceHostCfg.getIpType())){
						logger.info(msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_dst_mask"));
//						throw new RuntimeException(msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_dst_mask"));
						importErrorInfo=new ImportErrorInfo(i+3+"",msgProp.getProperty("server_address_mask"),msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_dst_mask"));
						importErrorInfos.add(importErrorInfo);
						valFlag = false;
					}
					//目的端口
					if(!(!StringUtil.isEmpty(specificServiceHostCfg.getDstPort())&&BasicProvingUtil.isPortOrPortMask(specificServiceHostCfg.getDstPort()))){
						logger.info(msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_dst_port"));
//						throw new RuntimeException(msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_dst_port"));
						importErrorInfo=new ImportErrorInfo(i+3+"",msgProp.getProperty("server_port"),msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_dst_port"));
						importErrorInfos.add(importErrorInfo);
						valFlag = false;
					}
					//目的端口掩码
					if(!(!StringUtil.isEmpty(specificServiceHostCfg.getDstPortMask())&&BasicProvingUtil.isPortOrPortMask(specificServiceHostCfg.getDstPortMask()))){
						logger.info(msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_dst_port_mask"));
//						throw new RuntimeException(msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_dst_port_mask"));
						importErrorInfo=new ImportErrorInfo(i+3+"",msgProp.getProperty("server_port_mask"),msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_dst_port_mask"));
						importErrorInfos.add(importErrorInfo);
						valFlag = false;
					}
					//协议
					if(!(!StringUtil.isEmpty(specificServiceHostCfg.getProtocol())&&("6".equals(specificServiceHostCfg.getProtocol().toString())||"17".equals(specificServiceHostCfg.getProtocol().toString())||"0".equals(specificServiceHostCfg.getProtocol().toString())))){
						logger.info(msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_protocol"));
//						throw new RuntimeException(msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_protocol"));
						importErrorInfo=new ImportErrorInfo(i+3+"",msgProp.getProperty("protocol"),msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_protocol"));
						importErrorInfos.add(importErrorInfo);
						valFlag = false;
					}
					//方向
					if(!StringUtil.isEmpty(specificServiceHostCfg.getDirection())&&!(("1".equals(specificServiceHostCfg.getDirection().toString())||"0".equals(specificServiceHostCfg.getDirection().toString())))){
						logger.info(msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_direction"));
//						throw new RuntimeException(msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_direction"));
						importErrorInfo=new ImportErrorInfo(i+3+"",msgProp.getProperty("direction"),msgProp.getProperty("the_line_of").replace("rowNum", (i+3)+"")+ msgProp.getProperty("val_direction"));
						importErrorInfos.add(importErrorInfo);
						valFlag = false;
					}
					
					//默认值
					if(specificServiceHostCfg.getIpType().equals(4)){
						defaultIpMask = "255.255.255.255"; //255.255.255.255表示无掩码
					}
					if(specificServiceHostCfg.getIpType().equals(6)){
						defaultIpMask = "FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"; //FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF表示无掩码
					}
					String defaultPortMask = "65535"; //65535表示无掩码
					//ip地址默认  缺省0.0.0.0值表示任意
					if(StringUtil.isBlank(specificServiceHostCfg.getSrcIp())){
						specificServiceHostCfg.setSrcIp(defaultIp);
					}
					if(StringUtil.isBlank(specificServiceHostCfg.getDstIp())){
						specificServiceHostCfg.setDstIp(defaultIp);
					}
					//ip掩码默认
					if(StringUtil.isBlank(specificServiceHostCfg.getSrcIpMask())){
						specificServiceHostCfg.setSrcIpMask(defaultIpMask);
					}
					if(StringUtil.isBlank(specificServiceHostCfg.getDstIpMask())){
						specificServiceHostCfg.setDstIpMask(defaultIpMask);
					}
					//端口掩码默认
					if(StringUtil.isBlank(specificServiceHostCfg.getSrcPortMask())){
						specificServiceHostCfg.setSrcPortMask(defaultPortMask);
					}
					if(StringUtil.isBlank(specificServiceHostCfg.getDstPortMask())){
						specificServiceHostCfg.setDstPortMask(defaultPortMask);
					}
					//方向缺省
					if(specificServiceHostCfg.getDirection()==null){
						specificServiceHostCfg.setDirection(0);
					}*/
					Date date = new Date();
					specificServiceHostCfg.setIsValid(1);
					specificServiceHostCfg.setCreator(user);
					specificServiceHostCfg.setCreateTime(date);
//					specificServiceHostCfg.setEditor(user);
//					specificServiceHostCfg.setEditTime(date);
//					specificServiceHostCfg.setAuditor(user);
//					specificServiceHostCfg.setAuditTime(date);
					
					if (valFlag) {
						specificServiceHostCfgDao.insert(specificServiceHostCfg);
					}
					
				}
				return importErrorInfos;
		}
}
