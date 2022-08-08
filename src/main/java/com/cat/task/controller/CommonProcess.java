package com.cat.task.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cat.boot.jsonbean.ResultBean;
import com.cat.boot.util.NameQueryUtil;
import com.cat.task.model.StepCondition;

@RestController
@RequestMapping("/commonprocess")
public class CommonProcess extends BaseProcess{

	// 初始化button
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/init_button", method = RequestMethod.GET)
	public String initButton(@RequestParam String step_id) {
		List<StepCondition> scs = (List<StepCondition>)baseService.getList("StepCondition", "o.xh asc", true
				,NameQueryUtil.setParams("step_id",step_id));
		return ResultBean.getSucess(scs);
	}
}
