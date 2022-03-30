package com.cat.boot.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.cat.boot.config.JedisUtil;

@Component
public class WhiteListInterceptor implements HandlerInterceptor {

	@Autowired
	public JedisUtil jedisUtil;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if("get".equals(request.getMethod()) || "GET".equals(request.getMethod())) {
			System.out.println(request.getQueryString());
		} else {
			byte[] bodyBytes = StreamUtils.copyToByteArray(request.getInputStream());
			String body = new String(bodyBytes, request.getCharacterEncoding());
			System.out.println("请求体：{}"+body);
		}
		return true;
	}
}
