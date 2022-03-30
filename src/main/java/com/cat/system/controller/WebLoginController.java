package com.cat.system.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.cat.boot.catconst.RedisConst;
import com.cat.boot.jsonbean.ResultBean;
import com.cat.boot.jsonbean.UserBean;
import com.cat.boot.service.BaseLoginService;
import com.cat.boot.util.SpringContextUtil;
import com.cat.boot.util.StringUtil;

@RestController
@RequestMapping("/auth")
public class WebLoginController extends LoginController {
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test() throws Exception {
		return "1";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestBody UserBean token,HttpServletRequest request) throws Exception {
		try {
			BaseLoginService service = (BaseLoginService) SpringContextUtil.getBean(token.getService());
			UserBean bean = service.login(token);
			if (bean != null) {
				bean.setService(token.getService());
				bean.setToken(iniToken(bean));
				
				jedisUtil.addLoginLog(request, bean.getUsername(),suffix(token.getService()));
				return ResultBean.getSucess(bean);
			} else {
				return ResultBean.getResultBean("401", "用户名或者密码错误", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResultBean.getResultBean("500", "服务异常", null);
		}
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public void logout(@RequestParam String token, @RequestParam String type,HttpServletRequest request) throws Exception {
		if (!StringUtil.isEmpty(token)) {
			UserBean ybean = JSON.parseObject(jedisUtil.get(token,RedisConst.token_db), UserBean.class);
			// 清理掉redis
			if (!StringUtil.isEmpty(ybean.getId())) {
				jedisUtil.del(RedisConst.token_db,ybean.getId() + "_" + type);
			}
			jedisUtil.del(RedisConst.token_db,token);
			jedisUtil.addLoginLog(request, ybean.getUsername(),type);
		}
	}
}
