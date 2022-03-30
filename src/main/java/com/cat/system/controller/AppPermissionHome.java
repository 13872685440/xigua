package com.cat.system.controller;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cat.boot.jsonbean.ResultBean;
import com.cat.boot.service.BaseHome;
import com.cat.boot.util.StringUtil;
import com.cat.system.model.AppPermission;

@RestController
@RequestMapping("/apppermission")
public class AppPermissionHome extends BaseHome<AppPermission>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2913462472226519093L;

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam String id) {
		if (!StringUtil.isEmpty(id)) {
			AppPermission entity = findById(id);
			return ResultBean.getSucess(entity);
		} else {
			return ResultBean.getSucess(new AppPermission());
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(AppPermission entity) throws Exception {
		if(!StringUtil.isListEmpty(entity.getRoles())) {
			entity.setRole(StringUtil.listToString(entity.getRoles()));
		}
		baseService.save(entity);
		return ResultBean.getSucess("sucess");
	}
}
