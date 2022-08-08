package com.cat.rmtj.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cat.boot.jsonbean.BaseQueryHelp;
import com.cat.boot.jsonbean.QueryResultBean;
import com.cat.boot.jsonbean.ResultBean;
import com.cat.boot.service.BaseNqtQuery;
import com.cat.rmtj.model.Shouli;

@RestController
@Scope("prototype")
@RequestMapping("/shouli")
public class ShouliQuery extends BaseNqtQuery<Shouli>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3527395693660770100L;

	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String query(BaseQueryHelp parms) throws Exception {
		excuteQuery(parms);
		return ResultBean.getSucess(new QueryResultBean(parms, results));
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam String id,@RequestParam String app_code) {
		return super.delete(id);
	}
}
