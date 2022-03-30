package com.cat.boot.filter;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.alibaba.fastjson.JSON;
import com.cat.boot.annotation.RequiredRoles;
import com.cat.boot.catconst.RedisConst;
import com.cat.boot.config.JedisUtil;
import com.cat.boot.exception.CatException;
import com.cat.boot.jsonbean.UserBean;
import com.cat.boot.util.StringUtil;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

	@Autowired
	public JedisUtil jedisUtil;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String token = request.getHeader("Access-Token");
		UserBean bean = new UserBean();
		if (StringUtil.isEmpty(token) || !jedisUtil.exists(token,RedisConst.token_db)) {

			CatException.responseData(response, 401, "认证超时");
			throw new CatException("认证超时");
		} else {
			bean = JSON.parseObject(jedisUtil.get(token,RedisConst.token_db), UserBean.class);
			jedisUtil.addLog(request, bean.getUsername());
		}

		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();

		RequiredRoles methodAnnotation = method.getAnnotation(RequiredRoles.class);
		if (methodAnnotation != null) {
			//UserBean bean = JSON.parseObject(jedisUtil.get(token,RedisConst.token_db), UserBean.class);
			String[] roles = methodAnnotation.value();
			if (StringUtil.isListEmpty(bean.getUserRoles()) || roles.length <= 0) {
				throw new CatException("没有访问权限");
			} else {
				for (String role : roles) {
					if (bean.getUserRoles().contains(role)) {
						return true;
					}
				}
			}
			throw new CatException("没有访问权限");
		} else {
			return true;
		}
	}
}
