package com.nis.web.controller.report;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nis.domain.log.SearchReport;
import com.nis.web.controller.BaseController;

@Controller
@RequestMapping("${adminPath}/report")
public class ReportController extends BaseController {
	@RequestMapping("/list")
	public String list(@ModelAttribute("bean") SearchReport bean,Model model, HttpServletRequest request, HttpServletResponse response) {
		initReportCondition(model, bean);
		return "/report/list";
	}
}
