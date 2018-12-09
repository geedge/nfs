package com.nis.web.controller.basics;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nis.domain.Page;
import com.nis.domain.configuration.TaskInfo;
import com.nis.web.controller.BaseController;
import com.nis.web.service.basics.TaskInfoService;

@Controller
@RequestMapping("${adminPath}/basics/taskInfo")
public class TaskInfoController extends BaseController{
	
	@Autowired
	private TaskInfoService taskInfoService;
	
	
	/**
	 *来函列表
	 */
	@RequestMapping(value = {"list",""})
	public String list(TaskInfo taskInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TaskInfo> page = taskInfoService.findTaskInfo(new Page<TaskInfo>(request, response,"r"), taskInfo);
        model.addAttribute("page", page);
        return "/basics/taskInfoList";
	}
	/**
	 * 进入用户添加或修改页面
	 */
	@RequestMapping(value={"form"})
	public String form(TaskInfo taskInfo, Model model) {
		if(taskInfo.getId()!=null){
			taskInfo = taskInfoService.getTaskInfoById(taskInfo.getId());
			model.addAttribute("taskInfo", taskInfo);
		}else{
			model.addAttribute("taskInfo", taskInfo);
		}
		return "/basics/taskInfoForm";
	}

	/**
	 * 新增/修改
	 */
	@RequestMapping(value = "saveOrUpdate")
	public String saveOrUpdate(TaskInfo taskInfo, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		try {
			if(taskInfo.getId()!=null){
				// 保存用户信息
				logger.info(taskInfo.getId()+"修改成功");
				taskInfoService.saveOrUpdate(taskInfo);
				addMessage(redirectAttributes, "save_success");
			}else{
				if (!"true".equals(checkTaskName(taskInfo.getTaskName()))){
					logger.info(taskInfo.getTaskName()+"重复数据");
					addMessage(model, "save_failed");
					return form(taskInfo, model);
				}
				// 保存用户信息
				taskInfoService.saveOrUpdate(taskInfo);
				addMessage(redirectAttributes, "save_success");
				logger.info(taskInfo.getId()+"保存成功");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			addMessage(redirectAttributes, "save_failed");
		}
		return "redirect:" + adminPath + "/basics/taskInfo/list?repage";
	}
	
	/**
	 * 验证是否有效
	 */
	@ResponseBody
	@RequestMapping(value = "checkTaskName")
	public String checkTaskName(String taskName) {
		if (taskName !=null && taskInfoService.getTaskInfoByTaskName(taskName) == null) {
			return "true";
		}
		return "false";
	}

    /**
     * 审核通过
     * @param taskInfo
     * @param model
     * @return
     */
	@RequestMapping(value = "taskExamine")
    public String taskExamine(String ids, Model model,RedirectAttributes redirectAttributes){
		String[] exId = ids.split(",");
    	taskInfoService.taskExamine(exId);
    	addMessage(redirectAttributes, "save_success");
    	return "redirect:" + adminPath + "/basics/taskInfo/list?repage";
    	
    }
	/**
	 * 审核未通过
	 * @param taskInfo
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "taskExamineNo")
	public String taskExamineNo(String ids, Model model,RedirectAttributes redirectAttributes){
		String[] noId = ids.split(",");
		taskInfoService.taskExamineNo(noId);
		addMessage(redirectAttributes, "save_success");
		return "redirect:" + adminPath + "/basics/taskInfo/list?repage";
		
	}
	/**
	 * 取消审核
	 * @param taskInfo
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "taskCancelExamine")
	public String taskCancelExamine(String ids, Model model,RedirectAttributes redirectAttributes){
		String[] canclelId = ids.split(",");
		taskInfoService.taskCancelExamine(canclelId);
		addMessage(redirectAttributes, "save_success");
		return "redirect:" + adminPath + "/basics/taskInfo/list?repage";
		
	}
	/**
	 * 删除
	 * @param taskInfo
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "delete")
	public String delete(String ids, Model model,RedirectAttributes redirectAttributes){
		String[] delId = ids.split(",");
		taskInfoService.delete(delId);
		addMessage(redirectAttributes, "delete_success");
		return "redirect:" + adminPath + "/basics/taskInfo/list?repage";
		
	}

}
