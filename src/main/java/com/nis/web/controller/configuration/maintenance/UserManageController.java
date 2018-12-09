package com.nis.web.controller.configuration.maintenance;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.eclipse.jetty.util.ajax.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nis.domain.Page;
import com.nis.domain.basics.IpReuseIpCfg;
import com.nis.domain.basics.ServiceDictInfo;
import com.nis.domain.configuration.UserManage;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.Constants;
import com.nis.util.StringUtil;
import com.nis.util.StringUtils;
import com.nis.web.controller.BaseController;

@Controller
@RequestMapping("${adminPath}/maintenance/userManage")
public class UserManageController  extends BaseController{

	@RequestMapping(value = {"/list"})
	@RequiresPermissions(value={"user:manage:view"})
	public String list(Model model,HttpServletRequest  request,HttpServletResponse response
			,@ModelAttribute("cfg")UserManage entity){
		Page<UserManage> page = userManageService.findPage(new Page<UserManage>(request, response,"a"), entity);
		model.addAttribute("page", page);
		return "/cfg/maintenance/userManage/list";
	}
	
	/**
	 * 进入用户添加或修改页面
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"form"})
	@RequiresPermissions(value={"user:manage:config"})
	public String form(UserManage user, Model model,String ids) {
		if(StringUtils.isNotBlank(ids)){
			user = userManageService.getUserById(ids);
		}
		model.addAttribute("user", user);
		return "/cfg/maintenance/userManage/userForm";
	}
	
	@RequestMapping(value = {"/delete"})
	@RequiresPermissions(value={"user:manage:config"})
	public String delete(Integer isValid
			,String ids
			,RedirectAttributes redirectAttributes,HttpServletRequest  request){
			String deleteSuccessIp="";
			String message="";
			Properties msgProp = getMsgProp();
			try{
				for (String id : ids.split(",")) {
					UserManage user=userManageService.getUserById(id);
					if(user != null && !StringUtil.isEmpty(user.getServerIp())) {
						boolean lastIp=false;
						int deleteIpLenth=1;
						String serverIp=user.getServerIp().substring(0, user.getServerIp().length()-1);
						int serverIpNum=user.getServerIp().split(",").length;
						for (String ip : user.getServerIp().split(",")) {
							try {
								if(deleteIpLenth == serverIpNum) {
									lastIp=true;
								}
								userManageService.delete(-1,user,ip,lastIp);
								deleteIpLenth++;
							} catch (MaatConvertException e) {
								message=msgProp.getProperty("user")+" "+user.getUserName()+" "+ip+" "+msgProp.getProperty("cgi_failed")+" "+msgProp.getProperty("delete_failed");
								logger.error("用户vpn新增失败",e);
								throw e;
							}catch (Exception e) {
								message=msgProp.getProperty("user")+" "+user.getUserName()+" "+ip+" "+msgProp.getProperty("cgi_failed")+" "+msgProp.getProperty("delete_failed");
								logger.error("用户vpn新增失败",e);
								throw e;
							}
						}
					}
				}
				addMessage(redirectAttributes,"success","delete_success");
			}catch(Exception e){
				logger.error("Delete failed",e);
				if(e instanceof MaatConvertException) {
					addMessage(redirectAttributes,"error",message);
				}else {
					addMessage(redirectAttributes,"error",message);
				}
			}
			
		return "redirect:" + adminPath +"/maintenance/userManage/list";
	}
	
	
	@RequestMapping(value = {"save"})
	@RequiresPermissions(value={"user:manage:config"})
	public String save(Model model,HttpServletRequest request,HttpServletResponse response
			,String ids,UserManage entity
			,RedirectAttributes redirectAttributes) {
				Date createTime=new Date();
			 String addIp="";
			 String addSuccessIp="";
			 String deleteIp="";
			 String deleteSuccessIp="";
			 String message="";
			 Properties msgProp = getMsgProp();
			 boolean isExistUser=false;
			try {
				String[] ipArray =entity.getServerIp().split(",");
				String serverIp =entity.getServerIp();
				if(entity.getId()==null){
					addIp=serverIp;
					entity.setCreateTime(createTime);
					entity.setCreatorId(entity.getCurrentUser().getId());
					entity.setIsValid(Constants.VALID_YES);
					for (String ip : ipArray) {
						try {
							if(!StringUtil.isEmpty(ip)) {
								if(!isExistUser) {
									entity.setServerIp(ip+",");
									userManageService.saveip(entity,ip);
									//用户第一个vpn创建成功之后即可走update
									isExistUser=true;
								}else {
									entity.setServerIp(addSuccessIp+ip+",");
									userManageService.updateip(entity,ip);
								}
								addSuccessIp+=addSuccessIp+",";
							}
						} catch (MaatConvertException e) {
							message=msgProp.getProperty("user")+" "+entity.getUserName()+" "+ip+" "+msgProp.getProperty("cgi_failed")+" "+msgProp.getProperty("save_failed");
							logger.error("用户vpn新增失败",e);
							throw e;
						}catch (Exception e) {
							message=msgProp.getProperty("user")+" "+entity.getUserName()+" "+ip+" "+msgProp.getProperty("cgi_failed")+" "+msgProp.getProperty("save_failed");
							logger.error("用户vpn新增失败",e);
							throw e;
						}
					}
				}else{
					entity.setEditTime(createTime);
					entity.setEditorId(entity.getCurrentUser().getId());
					UserManage oldUser=userManageService.getUserById(String.valueOf(entity.getId()));
					
					if(oldUser != null) {
						//密码修改
						if(oldUser != null && !StringUtil.isEmpty(entity.getUserPwd()) && !entity.getUserPwd().equals(oldUser.getUserPwd())) {
							for (String ip : oldUser.getServerIp().split(",")) {
								try {
									if(!StringUtil.isEmpty(ip)) {
										entity.setServerIp(oldUser.getServerIp());
										entity.setUserPwd(entity.getUserPwd());
										entity.setNewUserPwd(entity.getUserPwd());
										userManageService.updatePwd(entity,ip);
									}
								} catch (MaatConvertException e) {
									message=msgProp.getProperty("user")+" "+entity.getUserName()+msgProp.getProperty("password")+" "+msgProp.getProperty("cgi_failed")+" "+msgProp.getProperty("update_failed");
									logger.error("用户vpn密码修改失败",e);
									throw e;
								}catch (Exception e) {
									message=msgProp.getProperty("user")+" "+entity.getUserName()+msgProp.getProperty("password")+" "+msgProp.getProperty("cgi_failed")+" "+msgProp.getProperty("update_failed");
									logger.error("用户vpn密码修改失败",e);
									throw e;
								}
							}
						}
						
						//获取需求新增的vpn ip
						for (String oldIp : oldUser.getServerIp().split(",")) {
							if(!StringUtil.isEmpty(oldIp) && !serverIp.contains(oldIp)) {
								deleteIp+=oldIp+",";
							}
						}
						//获取需求删除的vpn ip
						for (String newIp : serverIp.split(",")) {
							if(!StringUtil.isEmpty(newIp) && !oldUser.getServerIp().contains(newIp)) {
								addIp+=newIp+",";
							}
						}
						//新增用户vpnip
						if(!StringUtil.isEmpty(addIp)){
							for (String ip : addIp.split(",")) {
								try {
									if(!StringUtil.isEmpty(ip)) {
										entity.setServerIp(oldUser.getServerIp()+ip+",");
										entity.setUserName(oldUser.getUserName());
										entity.setUserPwd(oldUser.getUserPwd());
										userManageService.updateip(entity,ip);
										addSuccessIp+=addSuccessIp+",";
									}
								} catch (MaatConvertException e) {
									message=msgProp.getProperty("user")+" "+entity.getUserName()+" "+ip+" "+msgProp.getProperty("cgi_failed")+" "+msgProp.getProperty("save_failed");
									logger.error("用户vpn新增失败",e);
									throw e;
								}catch (Exception e) {
									message=msgProp.getProperty("user")+" "+entity.getUserName()+" "+ip+" "+msgProp.getProperty("cgi_failed")+" "+msgProp.getProperty("save_failed");
									logger.error("用户vpn新增失败",e);
									throw e;
								}
							}
						}
						//删除用户vpnip
						if(!StringUtil.isEmpty(deleteIp)){
							for (String ip : deleteIp.split(",")) {
								try {
									if(!StringUtil.isEmpty(ip)) {
										entity.setServerIp(oldUser.getServerIp().replace(ip+",", ""));
										userManageService.deleteip(entity,ip);
										deleteSuccessIp+=deleteSuccessIp+",";
									}
								} catch (MaatConvertException e) {
									message=msgProp.getProperty("user")+" "+entity.getUserName()+" "+ip+" "+msgProp.getProperty("cgi_failed")+" "+msgProp.getProperty("save_failed");
									logger.error("用户vpn删除失败",e);
									throw e;
								}catch (Exception e) {
									message=msgProp.getProperty("user")+" "+entity.getUserName()+" "+ip+" "+msgProp.getProperty("cgi_failed")+" "+msgProp.getProperty("save_failed");
									logger.error("用户vpn删除失败",e);
									throw e;
								}
							}
						}
					}
					
				}
			} catch (MaatConvertException e) {
				addMessage(redirectAttributes,"error",message);
				return "redirect:" + adminPath +"/maintenance/userManage/list";
			} catch (Exception e) {
				addMessage(redirectAttributes,"error",message);
				return "redirect:" + adminPath +"/maintenance/userManage/list";
			}
		return "redirect:" + adminPath +"/maintenance/userManage/list";
	}

	/**
	 * 验证登录名是否有效
	 * @param oldLoginName
	 * @param loginName
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("user:manage:edit")
	@RequestMapping(value = "checkLoginName")
	public String checkLoginName(String oldUserName, String userName) {
		if (userName !=null && userName.equals(oldUserName)) {
			return "true";
		} else if (userName !=null && userManageService.getUserByLoginName(userName) == null) {
			return "true";
		}
		return "false";
	}
	
	/**
	 * 父节点选择树形结构
	 * @param extId排除节点id
	 * @param isShowHide
	 * @param isLeafShow:叶子节点是否显示
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false)Boolean specific,@RequestParam(required=false) String extId,@RequestParam(required=false) String isShowHide, 
			@RequestParam(required=false)boolean isLeafShow,@RequestParam(required=true)Integer cfgType,HttpServletResponse response){
		List<Map<String, Object>> mapList = Lists.newArrayList();
		Map<String, Object> map2 = Maps.newHashMap();
		map2.put("id", 0);
		map2.put("pId", 0);
		map2.put("groupId",0);
		map2.put("name","root_node");
		mapList.add(map2);
		IpReuseIpCfg ip=new IpReuseIpCfg();
		ip.setFunctionId(636);
		List<ServiceDictInfo> allList = serviceDictInfoService.findAllNoLeafDictList("5");
		for (int i = 0; i < allList.size(); i++) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", allList.get(i).getItemValue());
			map.put("pId", 0);
			map.put("name",allList.get(i).getItemValue());
			mapList.add(map);
		}
		return mapList;
	}
	
	@ResponseBody
	@RequestMapping(value = "userInfo")
	public Map<String,Object> getUserInfo(String serverIp,String userName,HttpServletRequest request,HttpServletResponse response){
		String[] ipArray = serverIp.split(",");
		List<Map<String,String>> userManageList=new ArrayList<Map<String,String>>();
		String message="";
		Map<String,Object> mess=new HashMap<String,Object>();
		for(String ip :ipArray){
			Map<String,String> map=new HashMap<String,String>();
			ServiceDictInfo	sysDictInfo=serviceDictInfoService.getByItemValue(ip, 5);
			//根据ip调用接口获取数据
			try{
			String recv=userManageService.getUser(ip,userName);
			if (StringUtils.isNotBlank(recv)) {
				map=(Map<String,String>)JSON.parse(recv);
				if(sysDictInfo!=null ){
					map.put("serverIp", sysDictInfo.getItemValue());
					userManageList.add(map);
				 }else{
					 if(message==""){
				    	message=ip;
				    }else{
				    	message+=","+ip;
				    }
				 }
			   }
		    } catch (Exception e) {
		    	if(message==""){
		    		message=ip;
		    	}else{
		    		message+=","+ip;
		    	}
			logger.error("查询失败", e);
		   }
		}
		mess.put("message", message);
		mess.put("user", userManageList);
		return mess;
	}

/*	@RequestMapping(value = "view")
	@RequiresPermissions(value={"user:manage:config"})
	public String view(String serverIp,String userName,HttpServletResponse response,Model model){
			UserManage  user=new UserManage();
			user=userManageService.getUserByLoginName(userName);
			IpReuseIpCfg  ipReuseIpCfg=ipReuseIpCfgService.getIpByIp(serverIp);
			//user=userManageService.getUserManage(ip, request);
			if(ipReuseIpCfg!=null){
				user.setServerIp(ipReuseIpCfg.getDestIpAddress());
				user.setRemarks(ipReuseIpCfg.getCfgDesc());
		}
		model.addAttribute("user", user);
		return "/cfg/maintenance/userManage/userView";
	}*/
}
