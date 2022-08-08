package com.cat.system.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;

import com.cat.boot.model.BaseEntity;

@Entity
@Table(name = "Sys_Role_Organ")
@BatchSize(size = 50)
@DynamicInsert
public class RoleOrgan extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 565237557743919140L;

	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(length = 40)
	private String id;

	/** 角色 */
	@Column(length = 40, nullable = false)
	private String role;

	/** 部门 */
	@Column(length = 40, nullable = false)
	private String org_id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getOrg_id() {
		return org_id;
	}

	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}

	@Override
	public String toString() {
		return this.id;
	}
}
