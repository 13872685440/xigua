package com.cat.system.jsonbean;

import java.util.ArrayList;
import java.util.List;

import com.cat.boot.jsonbean.BaseAppBean;
import com.cat.boot.model.BaseEntity;
import com.cat.boot.util.StringUtil;
import com.cat.system.model.Role;
import com.cat.system.model.User;

/**
 * 防止密码信息通过服务返回到页面，通过bean来进行处理
 * */
public class UserBean extends BaseAppBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2211649263840695651L;
	
	private String id;

	private String name;
	
	private String phone;
	
	private String radom;
	
	private Integer xh;
	
	private String organ_name;
	
	private String duty;
	
	private List<String> role_ls = new ArrayList<String>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getRadom() {
		return radom;
	}

	public void setRadom(String radom) {
		this.radom = radom;
	}

	public List<String> getRole_ls() {
		return role_ls;
	}

	public void setRole_ls(List<String> role_ls) {
		this.role_ls = role_ls;
	}

	public UserBean() {

	}

	public UserBean(BaseEntity entity) {
		super(entity);
	}

	public static UserBean setThis(User entity) {
		UserBean bean = new UserBean(entity);
		bean.setName(entity.getName());
		bean.setPhone(entity.getPhone());
		bean.setId(entity.getId());
		bean.setRadom(entity.getRadom());
		bean.getRole_ls().clear();
		bean.setXh(entity.getXh());
		bean.setOrgan_name(StringUtil.isEmpty((String)entity.getMaps().get("organ_name")) ? "" : (String)entity.getMaps().get("organ_name"));
		bean.setDuty(StringUtil.isEmpty((String)entity.getMaps().get("duty")) ? "" : (String)entity.getMaps().get("duty"));
		
		
		if(entity.getRoles()!=null && !entity.getRoles().isEmpty()) {
			for (Role d : entity.getRoles()) {
				bean.getRole_ls().add(d.getId());
			}
		}

		return bean;
	}

	public static List<UserBean> setThis(List<User> entitys) {
		List<UserBean> beans = new ArrayList<UserBean>();
		if (!StringUtil.isListEmpty(entitys)) {
			for (User bean : entitys) {
				beans.add(setThis(bean));
			}
		}
		return beans;
	}

	public static void clone(User bean, UserBean entity) {
		
		bean.setName(entity.getName());
		bean.setPhone(entity.getPhone());
		bean.setXh(entity.getXh());
	}

	public Integer getXh() {
		return xh;
	}

	public void setXh(Integer xh) {
		this.xh = xh;
	}

	public String getOrgan_name() {
		return organ_name;
	}

	public void setOrgan_name(String organ_name) {
		this.organ_name = organ_name;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}
}
