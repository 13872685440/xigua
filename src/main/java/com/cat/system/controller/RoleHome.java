package com.cat.system.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cat.boot.jsonbean.ResultBean;
import com.cat.boot.service.BaseHome;
import com.cat.boot.util.NameQueryUtil;
import com.cat.boot.util.StringUtil;
import com.cat.system.model.Role;
import com.cat.system.model.RoleApp;

@RestController
@RequestMapping("/role")
public class RoleHome extends BaseHome<Role> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7239096940289755826L;

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam String id) {
		if (!StringUtil.isEmpty(id)) {
			Role entity = findById(id);
			return ResultBean.getSucess(entity);
		} else {
			return ResultBean.getSucess(new Role());
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Role entity) throws Exception {
		baseService.save(entity);
		
		// 清理原关联
		baseService.delete4Prop("sys_role_app", true, NameQueryUtil.setParams("role", entity.getId()));

		if (!StringUtil.isListEmpty(entity.getApp_data())) {
			for (String a : entity.getApp_data()) {
				RoleApp r = new RoleApp();
				r.setApp(a);
				r.setRole(entity.getId());
				baseService.save(r);
			}
		}
		return ResultBean.getSucess(entity);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@RequestMapping(value = "/save_app", method = RequestMethod.POST)
	public String save_app(Role entity) throws Exception {
		// 清理原关联
		baseService.delete4Prop("sys_role_app", true, NameQueryUtil.setParams("role", entity.getId()));

		if (!StringUtil.isListEmpty(entity.getApp_data())) {
			for (String a : entity.getApp_data()) {
				RoleApp r = new RoleApp();
				r.setApp(a);
				r.setRole(entity.getId());
				baseService.save(r);
			}
		}
		return ResultBean.getSucess(entity);
	}

	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public String get(@RequestParam String rtype) {
		Map<Object,Object> maps = NameQueryUtil.setParams("rtype",rtype);
		List<Role> roles = new ArrayList<Role>();
		roles =	(List<Role>)baseService.getList("Role", "o.id asc", true, maps);
		return ResultBean.getSucess(roles);
	}
}
