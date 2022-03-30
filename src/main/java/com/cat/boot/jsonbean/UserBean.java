package com.cat.boot.jsonbean;

import java.util.ArrayList;
import java.util.List;

public class UserBean {

	private String id;

	private String qyWechatId;

	private String username;

	private String password;

	private String name;

	private String token;

	private String salt;

	private String status;

	private String service;
	
	private String registration_id;
	
	private String avatar;

	private List<String> userRoles = new ArrayList<String>();
	
	private List<PostBean> posts = new ArrayList<PostBean>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQyWechatId() {
		return qyWechatId;
	}

	public void setQyWechatId(String qyWechatId) {
		this.qyWechatId = qyWechatId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public List<String> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<String> userRoles) {
		this.userRoles = userRoles;
	}

	public String getRegistration_id() {
		return registration_id;
	}

	public void setRegistration_id(String registration_id) {
		this.registration_id = registration_id;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public static UserBean getUserBean(Object obj) {
		if (obj == null) {
			return null;
		}
		Object[] object = (Object[]) obj;
		UserBean bean = new UserBean();
		bean.setId((String) object[0]);
		bean.setUsername((String) object[1]);
		bean.setName((String) object[2]);
		bean.setPassword((String) object[3]);
		bean.setSalt((String) object[4]);
		bean.setStatus((String) object[5]);
		return bean;
	}

	public UserBean() {

	}

	public UserBean(UserBean bean) {
		this.id = bean.getId();
		this.qyWechatId = bean.getQyWechatId();
		this.username = bean.getUsername();
		this.password = bean.getPassword();
		this.name = bean.getName();
		this.token = bean.getToken();
		this.salt = bean.getSalt();
		this.status = bean.getStatus();
		this.avatar = bean.getAvatar();
		this.userRoles = bean.getUserRoles();
	}

	public List<PostBean> getPosts() {
		return posts;
	}

	public void setPosts(List<PostBean> posts) {
		this.posts = posts;
	}
}
