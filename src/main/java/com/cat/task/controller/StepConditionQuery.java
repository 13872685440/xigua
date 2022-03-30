package com.cat.task.controller;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cat.boot.jsonbean.BaseQueryHelp;
import com.cat.boot.jsonbean.ResultBean;
import com.cat.boot.service.BaseNqtQuery;
import com.cat.boot.util.NameQueryUtil;
import com.cat.task.model.StepCondition;

@RestController
@Scope("prototype")
@RequestMapping("/stepcondition")
public class StepConditionQuery extends BaseNqtQuery<StepCondition>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7251934785508718482L;

	@Override
	public String query(BaseQueryHelp parms) throws Exception {
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public String get(@RequestParam String step_id) {
		List<StepCondition> list = (List<StepCondition>)baseService.getList("StepCondition", "o.xh asc", 
				true,NameQueryUtil.setParams("step_id",step_id));
		return ResultBean.getSucess(list);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam String id) {
		return super.delete(id);
	}

}
