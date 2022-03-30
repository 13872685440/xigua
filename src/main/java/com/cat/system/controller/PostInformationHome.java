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
import com.cat.system.model.Post;
import com.cat.system.model.PostInformation;

@RestController
@RequestMapping("/postinformation")
public class PostInformationHome extends BaseHome<PostInformation>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 314586715829350604L;
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam String id) {
		if (!StringUtil.isEmpty(id)) {
			PostInformation entity = findById(id);
			return ResultBean.getSucess(entity);
		} else {
			return ResultBean.getSucess(new PostInformation());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(PostInformation entity) throws Exception {
		entity.getPosts().clear();
		if(!StringUtil.isListEmpty(entity.getPost_ls())) {
			List<Post> ds = (List<Post>)baseService.getList("Post", null, true,
					NameQueryUtil.setParams("id",entity.getPost_ls()));
			entity.getPosts().addAll(ds);
		}
		baseService.save(entity);
		return ResultBean.getSucess("sucess");
	}

}
