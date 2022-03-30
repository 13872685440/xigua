package com.cat.boot.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cat.boot.catconst.RedisConst;
import com.cat.boot.filter.AuthenticationInterceptor;
import com.cat.boot.filter.WhiteListInterceptor;
import com.cat.boot.util.StringUtil;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {

	@Autowired
	private AuthenticationInterceptor authenticationInterceptor;
	
	@Autowired
	private WhiteListInterceptor whiteListInterceptor;
	
	@Autowired
	private JedisUtil jedisUtil;

	// 这个方法是用来配置静态资源的，比如html，js，css，等等
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	}

	// 这个方法用来注册拦截器，我们自己写好的拦截器需要通过这里添加注册才能生效
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		List<String> s = new ArrayList<String>();
		List<String> s2 = new ArrayList<String>();
		s.add("/auth/*");
		s.add("/init/*");
		if(jedisUtil.exists(RedisConst.whitelist_key, RedisConst.reload_db)) {
			String whitelist = jedisUtil.get(RedisConst.whitelist_key, RedisConst.reload_db);
			if(!StringUtil.isEmpty(whitelist)) {
				String[] ls = whitelist.split(",");
				s.addAll(Arrays.asList(ls));
				s2.addAll(Arrays.asList(ls));
			}
		}
		registry.addInterceptor(authenticationInterceptor).addPathPatterns("/**").excludePathPatterns(s);
		
		registry.addInterceptor(whiteListInterceptor).addPathPatterns(s2);
	}
}
