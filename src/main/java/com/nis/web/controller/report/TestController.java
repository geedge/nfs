package com.nis.web.controller.report;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nis.domain.Page;
import com.nis.domain.log.SearchReport;
import com.nis.web.controller.BaseController;

@Controller
@RequestMapping("${adminPath}/report/test")
public class TestController extends BaseController {
	
	@RequestMapping("list")
	public String list(@ModelAttribute("bean") SearchReport bean,Model model, HttpServletRequest request, HttpServletResponse response) {
		Page<TestBean> page=new Page<TestBean>(request, response);
		List<TestBean> list=new ArrayList<>();
		list.add(new TestBean());
		page.setList(list);
		model.addAttribute("page", page);
		model.addAttribute("bean", bean);
		return "/report/test";
	}
}
