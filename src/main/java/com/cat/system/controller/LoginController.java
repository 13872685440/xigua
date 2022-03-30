package com.cat.system.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.cat.boot.catconst.RedisConst;
import com.cat.boot.config.JedisUtil;
import com.cat.boot.jsonbean.UserBean;
import com.cat.boot.service.BaseService;
import com.cat.boot.util.StringUtil;
import com.cat.boot.util.JWTUtil;

public class LoginController {

	@Autowired(required = true)
	public BaseService baseService;

	@Autowired
	public JedisUtil jedisUtil;

	// 双设备 web app
	protected String iniToken(UserBean bean) {
		String userId = bean.getId();
		// web
		// if (isWeb(bean.getService())) {
		// userId = userId + "_web";
		// } else {
		// userId = userId + "_app";
		// }
		userId = userId + "_" +suffix(bean.getService());
		String JWTtoken = JWTUtil.generateToken(bean.getId());
		// 查询是否有redis中是否有userId
		if (jedisUtil.exists(userId, RedisConst.token_db)) {
			String ytoken = jedisUtil.get(userId,RedisConst.token_db);
			if (jedisUtil.exists(ytoken, RedisConst.token_db)) {
				// 删除原token
				jedisUtil.del(RedisConst.token_db, ytoken);
			}
			// 替换userId的token
			jedisUtil.set(userId, JWTtoken,RedisConst.token_db);
		} else {
			if (!StringUtil.isEmpty(bean.getToken())) {
				if (jedisUtil.exists(bean.getToken(), RedisConst.token_db)) {
					// 删除原token
					jedisUtil.del(RedisConst.token_db, bean.getToken());
				}
			}
			jedisUtil.set(userId, JWTtoken,RedisConst.token_db);
		}
		// 加入token
		bean.setToken(JWTtoken);
		jedisUtil.set(JWTtoken, JSONObject.toJSONString(bean),RedisConst.token_db);

		return JWTtoken;
	}

	protected String suffix(String service) {
		switch (service) {
		case "webLoginService":
			return "web";
		case "flutterLoginService":
			return "app";
		default:
			return "web";
		}
	}
}
