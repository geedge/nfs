package com.nis.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nis.domain.SysMenu;
import com.nis.util.StringUtil;
import com.nis.web.security.UserUtils;

@Controller
@RequestMapping("${adminPath}/sys/")
public class SystemController  extends BaseController{
	
	@RequestMapping("index")
	public String index(HttpServletRequest request, HttpServletResponse response,ModelMap model){
		return "/sys/sysIndex";
	}
	
	@RequestMapping("help")
	public String help(HttpServletRequest request, HttpServletResponse response,ModelMap model){
		List<SysMenu> menuList = UserUtils.getMenuList();
		List<SysMenu>  newList = new ArrayList();
		for(SysMenu menu:menuList){
			if(menu.getIsTop()==1 && !StringUtil.isEmpty(menu.getChildren())){
				boolean topShow = false;
				for(SysMenu second:menu.getChildren()){
					boolean secondShow = false;
					if(second.getIsShow()==1){
						if(!StringUtil.isEmpty(second.getMenuBg())){
							secondShow = true;
						}
						if(!StringUtil.isEmpty(second.getChildren())){
							for(SysMenu third:second.getChildren()){
								boolean thirdShow = false;
								if(third.getIsShow()==1){
									if(!StringUtil.isEmpty(third.getMenuBg())){
										thirdShow = true;
										secondShow = true;
									}
									if(!StringUtil.isEmpty(third.getChildren())){
										for(SysMenu fourth:third.getChildren()){
											if(!StringUtil.isEmpty(fourth.getMenuBg())&&fourth.getIsShow()==1){
												newList.add(fourth);
												thirdShow = true;
												secondShow = true;
											}
										}
									}
								}
								if(thirdShow){
									newList.add(third);
								}
							}
						}
						
						if(secondShow){
							newList.add(second);
							topShow = true;
						}
						
					}
				}
				if(topShow){
					newList.add(menu);
				}
				
			}
			
			/*if(menu.getChildren()!=null && menu.getChildren().size()>0 && menu.getIsShow()==1){
				newList.add(menu);
			}else if(menu.getMenuBg()!=null && !"".equals(menu.getMenuBg())){
				newList.add(menu);
			}else if(menu.getId()==1){
				newList.add(menu);
			}*/
			
			/*if((menu.getParentIds().startsWith("0,1,86,") 
					|| menu.getId()==86
					||menu.getId()==0
					||menu.getId()==1)
					&& menu.getIsShow()==1
					){
				System.out.println(menu.getParentIds());
				newList.add(menu);
			}*/
			
		}
		model.addAttribute("menuList",newList);
		return "/help";
	}
	

}
