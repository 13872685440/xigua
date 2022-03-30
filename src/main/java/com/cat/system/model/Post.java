package com.cat.system.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;

import com.cat.boot.model.BaseEntity;
import com.cat.boot.util.StringUtil;
import com.cat.dictionary.model.Dictionary;

@Entity
@Table(name = "Org_Post")
@BatchSize(size = 50)
@DynamicInsert
public class Post extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -903411746936167148L;
	
	/** 名称 作为ID使用 */
	@Id
	@GeneratedValue(generator = "assignedGenerator")
	@GenericGenerator(name = "assignedGenerator", strategy = "assigned")
	@Column(length = 40)
	private String id;
	
	/** 别名 */
	@Column(length = 40)
	private String name;
	
	@Column
	private Integer xh = 1;
	
	/** 组织机构类型 */
	@ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinTable(name = "Org_Post_Orgtype", joinColumns = {
			@JoinColumn(name = "Post") }, inverseJoinColumns = { @JoinColumn(name = "Orgtype_ID") })
	@OrderBy("id")
	@BatchSize(size = 50)
	private Set<Dictionary> orgtypes = new HashSet<Dictionary>();

	/** 角色 */
	@ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinTable(name = "org_Post_Role", joinColumns = {
			@JoinColumn(name = "Post") }, inverseJoinColumns = { @JoinColumn(name = "role") })
	@OrderBy("id")
	@BatchSize(size = 50)
	private Set<Role> roles = new HashSet<Role>();
	
	@Transient
	private List<String> org_ls = new ArrayList<String>();
	
	@Transient
	private List<String> org_names = new ArrayList<String>();
	
	@Transient
	private List<String> role_ls = new ArrayList<String>();
	
	@Transient
	private List<String> role_names = new ArrayList<String>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getXh() {
		return xh;
	}

	public void setXh(Integer xh) {
		this.xh = xh;
	}

	public Set<Dictionary> getOrgtypes() {
		return orgtypes;
	}

	public void setOrgtypes(Set<Dictionary> orgtypes) {
		this.orgtypes = orgtypes;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public List<String> getOrg_ls() {
		if(!StringUtil.isListEmpty(org_ls)) {
			return org_ls;
		}
		if(this.orgtypes!=null && !this.orgtypes.isEmpty()) {
			for (Dictionary d : this.orgtypes) {
				org_ls.add(d.getId());
			}
		}
		return org_ls;
	}

	public void setOrg_ls(List<String> org_ls) {
		this.org_ls = org_ls;
	}

	public List<String> getRole_ls() {
		if(!StringUtil.isListEmpty(role_ls)) {
			return role_ls;
		}
		if(this.roles!=null && !this.roles.isEmpty()) {
			for (Role d : this.roles) {
				role_ls.add(d.getId());
			}
		}
		return role_ls;
	}

	public void setRole_ls(List<String> role_ls) {
		this.role_ls = role_ls;
	}

	public List<String> getOrg_names() {
		if(this.orgtypes!=null && !this.orgtypes.isEmpty()) {
			for (Dictionary d : this.orgtypes) {
				org_names.add(d.getName());
			}
		}
		return org_names;
	}

	public void setOrg_names(List<String> org_names) {
		this.org_names = org_names;
	}

	public List<String> getRole_names() {
		if(this.roles!=null && !this.roles.isEmpty()) {
			for (Role d : this.roles) {
				role_names.add(d.getDes());
			}
		}
		return role_names;
	}

	public void setRole_names(List<String> role_names) {
		this.role_names = role_names;
	}

	@Override
	public String toString() {
		return this.id;
	}
	
	
}
