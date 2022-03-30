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

import com.cat.boot.jsonbean.PropParamBean;
import com.cat.boot.jsonbean.ResultBean;
import com.cat.boot.service.BaseHome;
import com.cat.boot.util.NameQueryUtil;
import com.cat.boot.util.PassWordUtil;
import com.cat.boot.util.RadomUtil;
import com.cat.boot.util.StringUtil;
import com.cat.system.jsonbean.UserBean;
import com.cat.system.model.Role;
import com.cat.system.model.User;

@RestController
@RequestMapping("/useronpost")
public class UseronpostHome extends BaseHome<User>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3085680281237892660L;

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam String id) {
		if (!StringUtil.isEmpty(id)) {
			User entity = findById(id);
			return ResultBean.getSucess(UserBean.setThis(entity));
		} else {
			return ResultBean.getSucess(UserBean.setThis(new User()));
		}
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(UserBean entity) {
		User bean = new User();
		if (!StringUtil.isEmpty(entity.getId())) {
			bean = findById(entity.getId());
		}
		UserBean.clone(bean, entity);
		bean.setLoginName(entity.getPhone());
		
		List<PropParamBean> beans = new ArrayList<PropParamBean>();
		if(!StringUtil.isEmpty(entity.getId())) {
			beans.add(new PropParamBean("!=", "and", "id",entity.getId()));
		}
		beans.add(new PropParamBean("=", "and", "loginName",bean.getLoginName()));
		List<User> users = (List<User>)baseService.getList("User", null, true,beans);
		if(!StringUtil.isListEmpty(users)) {
			return ResultBean.getResultBean("400", "手机号码重复", "手机号码重复");
		}
		
		bean.getRoles().clear();
		if(!StringUtil.isListEmpty(entity.getRole_ls())) {
			List<Role> ds = (List<Role>)baseService.getList("Role", null, true,
					NameQueryUtil.setParams("id",entity.getRole_ls()));
			bean.getRoles().addAll(ds);
		}
		
		
		if(StringUtil.isEmpty(entity.getId())) {
			makePwd(bean);
		}
		baseService.save(bean);
		return ResultBean.getSucess(UserBean.setThis(bean));
	}
	
	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public String resetRadom(@RequestParam String id) {
		User user = findById(id);
		makePwd(user);
		baseService.noAnnotationSave(user, true);
		return ResultBean.getSucess(user);
	}
	
	private void makePwd(User user) {
		user.setRadom(RadomUtil.radom(6));
		Map<String, String> pwds = PassWordUtil.entryptPassword(user.getRadom(), true);
		user.setPwd(pwds.get("pwd"));
		user.setSalt(pwds.get("salt"));
	}
}
