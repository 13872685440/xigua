package com.cat.system.controller;

import java.util.List;

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
import com.cat.dictionary.model.Dictionary;
import com.cat.system.model.Post;
import com.cat.system.model.Role;

@RestController
@RequestMapping("/post")
public class PostHome extends BaseHome<Post>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7905289745579898789L;

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam String id) {
		if (!StringUtil.isEmpty(id)) {
			Post entity = findById(id);
			return ResultBean.getSucess(entity);
		} else {
			return ResultBean.getSucess(new Post());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Post entity) throws Exception {
		entity.getOrgtypes().clear();
		entity.getRoles().clear();
		if(!StringUtil.isListEmpty(entity.getOrg_ls())) {
			List<Dictionary> ds = (List<Dictionary>)baseService.getList("Dictionary", null, true,
					NameQueryUtil.setParams("id",entity.getOrg_ls()));
			entity.getOrgtypes().addAll(ds);
		}
		if(!StringUtil.isListEmpty(entity.getRole_ls())) {
			List<Role> ds = (List<Role>)baseService.getList("Role", null, true,
					NameQueryUtil.setParams("id",entity.getRole_ls()));
			entity.getRoles().addAll(ds);
		}
		baseService.save(entity);
		return ResultBean.getSucess(entity);
	}
}
