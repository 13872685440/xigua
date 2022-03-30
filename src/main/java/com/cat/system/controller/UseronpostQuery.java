package com.cat.system.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cat.boot.jsonbean.BaseQueryHelp;
import com.cat.boot.jsonbean.QueryResultBean;
import com.cat.boot.jsonbean.ResultBean;
import com.cat.boot.service.BaseNqtQuery;
import com.cat.system.jsonbean.UserBean;
import com.cat.system.model.User;

@RestController
@Scope("prototype")
@RequestMapping("/useronpost")
public class UseronpostQuery extends BaseNqtQuery<User>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3810790903215560061L;

	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String query(BaseQueryHelp parms) throws Exception {
		excuteQuery(parms);
		return ResultBean.getSucess(new QueryResultBean(parms, UserBean.setThis(results)));
	}
}
