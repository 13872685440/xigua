package com.cat.system.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;

import com.cat.boot.model.BaseEntity;
import com.cat.boot.util.StringUtil;

@Entity
@Table(name = "Sys_App_Permission")
@BatchSize(size = 50)
@DynamicInsert
public class AppPermission extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -482414144582779361L;

	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(length = 40)
	private String id;
	
	/** 应用 */
	@Column(length = 40, nullable = false)
	private String app;
	
	/** 操作 */
	@Column(length = 40)
	private String oper;
	
	/** 角色 */
	@Column(length = 200)
	private String role;
	
	@Transient
	private List<String> roles = new ArrayList<String>();
	
	@Transient
	private String appName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getOper() {
		return oper;
	}

	public void setOper(String oper) {
		this.oper = oper;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<String> getRoles() {
		if(StringUtil.isListEmpty(roles)) {
			if(!StringUtil.isEmpty(this.role)) {
				roles.addAll(Arrays.asList(this.role.split(",")));
			}
		}
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getAppName() {
		if(!StringUtil.isEmpty(this.app)) {
			AppTree a = (AppTree)getService().findById(AppTree.class, this.app);
			return a.getName();
		}
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	@Override
	public String toString() {
		return this.id;
	}
} 
