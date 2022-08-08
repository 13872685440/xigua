package com.cat.system.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.cat.boot.jsonbean.ResultBean;
import com.cat.boot.jsonbean.UserBean;
import com.cat.boot.service.BaseHome;
import com.cat.boot.util.NameQueryUtil;
import com.cat.boot.util.StringUtil;
import com.cat.system.model.Organ;

@RestController
@RequestMapping("/organ")
public class OrganHome extends BaseHome<Organ>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -921396825886502260L;

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam String id) {
		if (!StringUtil.isEmpty(id)) {
			Organ entity = findById(id);
			return ResultBean.getSucess(entity);
		} else {
			Organ entity = new Organ();
			entity.setClc(baseService.getCode("Org_Organ", null, 4).trim());
			entity.setId(entity.getClc());
			return ResultBean.getSucess(entity);
		}
	}
	
	@RequestMapping(value = "/addlower", method = RequestMethod.GET)
	public String addlower(@RequestParam String scId) {
		Organ entity = new Organ();
		Organ superior = (Organ) baseService.findById(Organ.class, scId);
		entity.setScId(scId);
		entity.setScName(superior.getName());
		entity.setClc(baseService.getCode("Org_Organ", scId, 4).trim());
		entity.setId(scId + entity.getClc());

		return ResultBean.getSucess(entity);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Organ entity) throws Exception {
		entity.setWn(StringUtil.isEmpty(entity.getScWnName()) ? entity.getName() : entity.getScWnName() + "_" + entity.getName());
		baseService.save(entity);
		return ResultBean.getSucess(entity);
	}
	
	@RequestMapping(value = "/get_myorgan", method = RequestMethod.GET)
	public String get_myorgan(HttpServletRequest request) throws Exception {
		UserBean bean = (UserBean)baseService.getUserInfo(request);
		return "";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/get_fzr", method = RequestMethod.GET)
	public String get_fzr(String org_id,String role_id) throws Exception {
		List<Object[]> xs = (List<Object[]>)baseService.getList("User", "system", "User_by_OrganAndRole", 
				NameQueryUtil.setParams("orgs",Arrays.asList(org_id),"roles",Arrays.asList(role_id)));
		JSONObject j = new JSONObject();
		if(!StringUtil.isListEmpty(xs)) {
			j.put("id", xs.get(0)[0]);
			j.put("name", xs.get(0)[1]);
			j.put("phone", xs.get(0)[2]);
		}
		return ResultBean.getSucess(j);
	}
}
