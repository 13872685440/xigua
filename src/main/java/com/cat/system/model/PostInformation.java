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

@Entity
@Table(name = "Org_Post_Information")
@BatchSize(size = 50)
@DynamicInsert
public class PostInformation extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -956910465204052816L;
	
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(length = 40)
	private String id;
	
	/**
	 * 用户ID*/
	@Column(length = 40, nullable = false)
	private String userId;
	
	/**
	 * 机构ID*/
	@Column(length = 40, nullable = false)
	private String organId;
	
	/**
	 * 在职 兼职 离职*/
	@Column(length = 40, nullable = false)
	private String isleaf;
	
	/**
	 * 职务
	 */
	@Column(length = 30)
	private String duty;

	/**
	 * 入职时间
	 */
	@Column(length = 30)
	private String entrytime;

	/**
	 * 离开时间
	 */
	@Column(length = 30)
	private String leaftime;
	
	/** 角色 */
	@ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinTable(name = "org_Information_Role", joinColumns = {
			@JoinColumn(name = "Information") }, inverseJoinColumns = { @JoinColumn(name = "role") })
	@OrderBy("id")
	@BatchSize(size = 50)
	private Set<Role> roles = new HashSet<Role>();
	
	/** 岗位（多选） */
	//@ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY)
	//@JoinTable(name = "Org_Information_Post", joinColumns = {
		//	@JoinColumn(name = "Post_Information_ID") }, inverseJoinColumns = { @JoinColumn(name = "post") })
	//@OrderBy("xh")
	//@BatchSize(size = 50)
	//private Set<Post> posts = new HashSet<Post>();
	
	@Transient
	private String userName;
	
	@Transient
	private String organName;
	
	//@Transient
	//private List<String> post_ls = new ArrayList<String>();
	
	//@Transient
	//private List<String> post_names = new ArrayList<String>();
	
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrganId() {
		return organId;
	}

	public void setOrganId(String organId) {
		this.organId = organId;
	}

	public String getIsleaf() {
		return isleaf;
	}

	public void setIsleaf(String isleaf) {
		this.isleaf = isleaf;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getEntrytime() {
		return entrytime;
	}

	public void setEntrytime(String entrytime) {
		this.entrytime = entrytime;
	}

	public String getLeaftime() {
		return leaftime;
	}

	public void setLeaftime(String leaftime) {
		this.leaftime = leaftime;
	}

	public String getUserName() {
		if(!StringUtil.isEmpty(this.userId)) {
			User o = (User)getService().findById(User.class, this.userId);
			if(o!=null) {
				return o.getName();
			}
		}
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrganName() {
		if(!StringUtil.isEmpty(this.organId)) {
			Organ o = (Organ)getService().findById(Organ.class, this.organId);
			if(o!=null) {
				return o.getName();
			}
		}
		return organName;
	}

	public void setOrganName(String organName) {
		this.organName = organName;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
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
