package com.cat.demo.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.cat.boot.jsonbean.BaseQueryHelp;
import com.cat.boot.jsonbean.QueryResultBean;
import com.cat.boot.jsonbean.ResultBean;
import com.cat.boot.service.BaseNqtQuery;
import com.cat.demo.model.Demo;

@RestController
@Scope("prototype")
@RequestMapping("/demo")
public class DemoQuery extends BaseNqtQuery<Demo>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6570160136492716496L;

	@Override
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String query(BaseQueryHelp parms) throws Exception {
		excuteQuery(parms);
		return ResultBean.getSucess(new QueryResultBean(parms, results));
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam String id,@RequestParam String app_code) {
		JSONObject result = JSONObject.parseObject(super.delete(id,app_code));
		if("200".equals(result.getString("code"))) {
			super.deleteFile(id, "com.cat.demo.model.Demo");
		}
		return result.toJSONString();
	}
}
