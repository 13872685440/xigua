package com.cat.system.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.cat.boot.util.NameQueryUtil;
import com.cat.boot.util.StringUtil;
import com.cat.system.model.Role;

@RestController
@Scope("prototype")
@RequestMapping("/role")
public class RoleQuery extends BaseNqtQuery<Role> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8020811162265557457L;

	@Override
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String query(BaseQueryHelp parms) throws Exception {
		excuteQuery(parms);
		return ResultBean.getSucess(new QueryResultBean(parms, results));
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam String id,@RequestParam String app_code) {
		return super.delete(id,app_code);
	}
	
	@RequestMapping(value = "/get_user", method = RequestMethod.GET)
	public String get_user_j(String org_id,String role_id) throws Exception {
		return ResultBean.getSucess(get_user(org_id,role_id));
	}
	
	@SuppressWarnings("unchecked")
	public List<JSONObject> get_user(String org_id,String role_id) throws Exception {
		List<Object[]> xs = (List<Object[]>)baseService.getList("User", "system", "User_by_OrganAndRole", 
				NameQueryUtil.setParams("orgs",Arrays.asList(org_id),"roles",Arrays.asList(role_id)));
		List<JSONObject> js = new ArrayList<JSONObject>();
		if(!StringUtil.isListEmpty(xs)) {
			for (Object[] x : xs) {
				JSONObject j = new JSONObject();
				j.put("id", x[0]);
				j.put("name", x[1]);
				j.put("phone", x[2]);
				js.add(j);
			}
		}
		return js;
	}

}
