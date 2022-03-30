package com.cat.system.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cat.boot.catconst.RedisConst;
import com.cat.boot.config.JedisUtil;
import com.cat.boot.jsonbean.PostBean;
import com.cat.boot.jsonbean.ResultBean;
import com.cat.boot.jsonbean.UserBean;
import com.cat.boot.service.BaseService;
import com.cat.boot.util.NameQueryUtil;
import com.cat.boot.util.StringUtil;
import com.cat.system.jsonbean.AppTreeInfoBean;
import com.cat.system.model.AppTree;
import com.cat.system.model.Post;
import com.cat.system.model.PostInformation;

@RestController
@RequestMapping("/info")
public class UserInfoController {

	@Autowired
	public BaseService baseService;

	@Autowired
	public JedisUtil jedisUtil;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public String user(HttpServletRequest request) throws Exception {
		String token = request.getHeader("Access-Token");
		UserBean ybean = JSON.parseObject(jedisUtil.get(token,RedisConst.token_db), UserBean.class);
		
		List<String> userRoles = new ArrayList<String>();
		if ("admin".equals(ybean.getUsername())) {

			userRoles.add("SYS_ADMINISTRATOR");
		} else {
			// 查询通用角色
			List<String> tys = (List<String>) baseService.getList("SYS_Role", null, true, "id",NameQueryUtil.setParams("r_type", "RT001"));
			tys.remove("SYS_ADMINISTRATOR");
			if(!StringUtil.isListEmpty(tys)) {
				userRoles.addAll(tys);
			}
			if(jedisUtil.exists("organ_config", RedisConst.config_db)) {
				JSONObject organ_config = JSONObject.parseObject(jedisUtil.get("organ_config", RedisConst.config_db));
				if(organ_config.getString("hasOrgan")=="1" && organ_config.getString("hasPost")=="1") {
					// 查询用户所在机构
					List<PostInformation> pis = (List<PostInformation>)baseService.
							getList("PostInformation", "o.ct desc", true,NameQueryUtil.setParams("userId",ybean.getId(),"isleaf","在职"));
					ybean.getPosts().clear();
					if(!StringUtil.isListEmpty(pis)) {
						for (PostInformation pi : pis) {
							PostBean pb = new PostBean();
							pb.setPiId(pi.getId());
							pb.setOrganId(pi.getOrganId());
							pb.setOrganName(pi.getOrganName());
							pb.getUserRoles().addAll(userRoles);
							if(pi.getPosts()!=null && !pi.getPosts().isEmpty()) {
								pb.setPost_names(StringUtil.listToString(pi.getPost_names()));
								
								for (Post p : pi.getPosts()) {
									pb.getUserRoles().addAll(p.getRole_ls());
									userRoles.addAll(p.getRole_ls());
								}
							}
							ybean.getPosts().add(pb);
						}
					}
				} else if(organ_config.getString("hasOrgan")=="1" && organ_config.getString("hasPost")=="0") {
					
				}
			} else {
				return ResultBean.getResultBean("400", "未找到用户配置文件", "未找到用户配置文件");
			}
			
			// 查询用户角色
			//List<String> us = (List<String>) baseService.getList("Sys_UserRole", null, true, "id",NameQueryUtil.setParams("ywid", ybean.getId()));
			//if(!StringUtil.isListEmpty(us)) {
				//userRoles.addAll(us);
			//}
		}
		
		ybean.setUserRoles(userRoles);
		ybean.setAvatar("/logo.png");
		
		jedisUtil.set(token, JSONObject.toJSONString(ybean),RedisConst.token_db);
		
		return ResultBean.getSucess(ybean);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/router", method = RequestMethod.POST)
	public String router(@RequestBody UserBean bean) throws Exception {
		List<String> roles = bean.getUserRoles();
		if (!StringUtil.isListEmpty(roles)) {
			List<AppTree> apps = (List<AppTree>) baseService.getList("AppTree", "system", "AppTree_by_Role",
					NameQueryUtil.setParams("roles", roles));
			if (!StringUtil.isListEmpty(apps)) {
				return ResultBean.getSucess(AppTreeInfoBean.iniAppTree(apps, baseService));
			}
		}
		return ResultBean.getSucess("");
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/app_router", method = RequestMethod.POST)
	public String app_router(@RequestBody UserBean bean) throws Exception {
		List<String> roles = bean.getUserRoles();
		if (!StringUtil.isListEmpty(roles)) {
			List<AppTree> apps = (List<AppTree>) baseService.getList("AppTree", "system", "AppTree_by_Role",
					NameQueryUtil.setParams("roles", roles));
			if (!StringUtil.isListEmpty(apps)) {
				return ResultBean.getSucess(AppTreeInfoBean.iniAppTree_Apps(apps, baseService));
			}
		}
		return ResultBean.getSucess("");
	}
}
