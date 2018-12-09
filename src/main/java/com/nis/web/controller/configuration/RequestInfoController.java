package com.nis.web.controller.configuration;

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
import com.nis.domain.configuration.RequestInfo;
import com.nis.domain.configuration.TaskInfo;
import com.nis.web.controller.BaseController;
import com.nis.web.service.configuration.RequestInfoService;

@Controller
@RequestMapping("${adminPath}/cfg/request")
public class RequestInfoController extends BaseController{
	
	@Autowired
	private RequestInfoService requestInfoService;
	
	
	/**
	 *来函列表
	 */
	@RequestMapping(value = {"list",""})
	public String list(RequestInfo requestInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RequestInfo> page = requestInfoService.findRequestInfo(new Page<RequestInfo>(request, response,"r"), requestInfo);
        model.addAttribute("page", page);
        return "/cfg/requestList";
	}
	/**
	 * 进入用户添加或修改页面
	 */
	@RequestMapping(value={"form"})
	public String form(RequestInfo requestInfo, Model model) {
		TaskInfo taskInfo = new TaskInfo();
		taskInfo.setId(requestInfo.getTaskId());
		showTask(taskInfo,model);
		if(requestInfo.getId()!=null){
			requestInfo = requestInfoService.getRequestInfoById(requestInfo.getId());
			model.addAttribute("requestInfo", requestInfo);
		}else{
			model.addAttribute("requestInfo", requestInfo);
		}
		return "/cfg/requestForm";
	}

	/**
	 * 新增/修改
	 */
	@RequestMapping(value = "saveOrUpdate")
	public String saveOrUpdate(RequestInfo requestInfo, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		try {
			if(requestInfo.getId()!=null){
				// 保存用户信息
				logger.info(requestInfo.getId()+"修改成功");
				requestInfoService.saveOrUpdate(requestInfo);
				addMessage(redirectAttributes,"success","save_success");
			}else{
				if (!"true".equals(checkRequestNumber(requestInfo.getRequestNumber()))){
					logger.info(requestInfo.getRequestNumber()+"重复数据");
					addMessage(model,"error","save_failed");
					return form(requestInfo, model);
				}
				// 保存用户信息
				requestInfoService.saveOrUpdate(requestInfo);
				addMessage(redirectAttributes,"success","save_success");
				logger.info(requestInfo.getId()+"保存成功");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			addMessage(model,"error","save_failed");
		}
		return "redirect:" + adminPath + "/cfg/request/list?repage";
	}
	
	/**
	 * 验证是否有效
	 */
	@ResponseBody
	@RequestMapping(value = "checkRequestNumber")
	public String checkRequestNumber(String requestNumber) {
		if (requestNumber !=null && requestInfoService.getRequestInfoByRequestNumber(requestNumber) == null) {
			return "true";
		}
		return "false";
	}

    /**
     * 审核通过
     * @param requestInfo
     * @param model
     * @return
     */
	@RequestMapping(value = "requestExamine")
    public String requestExamine(String ids, Model model,RedirectAttributes redirectAttributes){
		String[] exId = ids.split(",");
    	requestInfoService.requestExamine(exId);
    	addMessage(redirectAttributes,"success","save_success");
    	return "redirect:" + adminPath + "/cfg/request/list?repage";
    	
    }
	/**
	 * 审核未通过
	 * @param requestInfo
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "requestExamineNo")
	public String requestExamineNo(String ids, Model model,RedirectAttributes redirectAttributes){
		String[] noId = ids.split(",");
		requestInfoService.requestExamineNo(noId);
		addMessage(redirectAttributes,"success","save_success");
		return "redirect:" + adminPath + "/cfg/request/list?repage";
		
	}
	/**
	 * 取消审核
	 * @param requestInfo
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "requestCancelExamine")
	public String requestCancelExamine(String ids, Model model,RedirectAttributes redirectAttributes){
		String[] canclelId = ids.split(",");
		requestInfoService.requestCancelExamine(canclelId);
		addMessage(redirectAttributes,"success","save_success");
		return "redirect:" + adminPath + "/cfg/request/list?repage";
		
	}
	/**
	 * 删除
	 * @param requestInfo
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "delete")
	public String delete(String ids, Model model,RedirectAttributes redirectAttributes){
		String[] delId = ids.split(",");
		requestInfoService.delete(delId);
		addMessage(redirectAttributes,"success","delete_success");
		return "redirect:" + adminPath + "/cfg/request/list?repage";
		
	}

	/**
	 * 查询专项任务
	 * @param taskInfo
	 * @param model
	 */
	public void showTask(TaskInfo taskInfo ,Model model){
		List<TaskInfo> taskInfos = requestInfoService.showTask(taskInfo);
		model.addAttribute("taskInfos", taskInfos);
	}
	
	/**
	 * 配置取消审核操作时选择来函
	 * @param model
	 * @return
	 */
	@RequestMapping(value="requestSelectInfo")
	public String requestSelectInfo(Model model){
		List<RequestInfo> requestInfos=requestInfoService.getValidRequestInfo();
		model.addAttribute("requestInfos", requestInfos);
		return "/basics/requestSelectInfo";
	}
}
