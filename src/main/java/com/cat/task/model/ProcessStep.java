package com.cat.task.model;

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
@Table(name = "Task_Process_Step")
@BatchSize(size = 50)
@DynamicInsert
public class ProcessStep extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3526381516313640211L;

	@Id
	@GeneratedValue(generator = "assignedGenerator")
	@GenericGenerator(name = "assignedGenerator", strategy = "assigned")
	@Column(length = 40)
	private String id;
	
	@Column(length = 40)
	private String appId;

	/** 本级代码 current Level Code */
	@Column(length = 8, nullable = false)
	private String clc;

	/** 名称 */
	@Column(length = 500, nullable = false)
	private String name;
	
	/** 关联的表单的table名 用来更新实体 */
	@Column(length = 500)
	private String table_name;

	/** 上级  */
	@Column(length = 40)
	private String scId;

	/** 末级 */
	@Column(nullable = false,columnDefinition="int")
	private boolean isLeaf = true;
	
	/** 组件 */
	@Column(length = 100)
	private String component;
	
	/** 主服务路径 */
	@Column(length = 100)
	private String service_path;
	
	/** 服务子路径 */
	@Column(length = 20)
	private String service_path_type;
	
	/** 步骤 */
	@Column(length = 10)
	private Integer step;
	
	/** 当前审核人类型 1:用户 2:角色 3:其他*/
	@Column(length = 10)
	private String shr_type;
	
	/** 当前审核人 用户IDs*/
	@Column(length = 1000)
	private String userIds;
	
	/** 当前审核角色 */
	@Column(length = 1000)
	private String roles;
	
	/** 当前审核角色类型 1:上级部门 2:本级部门 3:指定部门 4:不指定部门*/
	@Column(length = 10)
	private String role_type;
	
	/** 当前审核机构 与角色配套使用*/
	@Column(length = 1000)
	private String organIds;
	
	@Transient
	private String scName;
	
	@Transient
	private String sc_TableName;
	
	@Transient
	private List<String> user_s = new ArrayList<String>();
	
	@Transient
	private List<String> role_s = new ArrayList<String>();
	
	@Transient
	private List<String> organ_s = new ArrayList<String>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClc() {
		return clc;
	}

	public void setClc(String clc) {
		this.clc = clc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScId() {
		return scId;
	}

	public void setScId(String scId) {
		this.scId = scId;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	public String getScName() {
		if(!StringUtil.isEmpty(this.scId)) {
			ProcessStep o = (ProcessStep)getService().findById(ProcessStep.class, this.scId);
			if(o!=null) {
				return o.getName();
			}
		}
		return scName;
	}

	public void setScName(String scName) {
		this.scName = scName;
	}

	public String getShr_type() {
		return shr_type;
	}

	public void setShr_type(String shr_type) {
		this.shr_type = shr_type;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String getRole_type() {
		return role_type;
	}

	public void setRole_type(String role_type) {
		this.role_type = role_type;
	}

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public String getOrganIds() {
		return organIds;
	}

	public void setOrganIds(String organIds) {
		this.organIds = organIds;
	}

	public String getService_path() {
		return service_path;
	}

	public void setService_path(String service_path) {
		this.service_path = service_path;
	}

	public String getService_path_type() {
		return service_path_type;
	}

	public void setService_path_type(String service_path_type) {
		this.service_path_type = service_path_type;
	}

	public List<String> getUser_s() {
		if(!StringUtil.isListEmpty(user_s)) {
			return user_s;
		}
		if(!StringUtil.isEmpty(this.userIds)) {
			user_s.addAll(Arrays.asList(this.userIds.split(",")));
			return user_s;
		}
		return user_s;
	}

	public void setUser_s(List<String> user_s) {
		this.user_s = user_s;
	}
	
	public List<String> getRole_s() {
		if(!StringUtil.isListEmpty(role_s)) {
			return role_s;
		}
		if(!StringUtil.isEmpty(this.roles)) {
			role_s.addAll(Arrays.asList(this.roles.split(",")));
			return role_s;
		}
		return role_s;
	}

	public void setRole_s(List<String> role_s) {
		this.role_s = role_s;
	}

	public List<String> getOrgan_s() {
		if(!StringUtil.isListEmpty(organ_s)) {
			return organ_s;
		}
		if(!StringUtil.isEmpty(this.organIds)) {
			organ_s.addAll(Arrays.asList(this.organIds.split(",")));
			return organ_s;
		}
		return organ_s;
	}

	public void setOrgan_s(List<String> organ_s) {
		this.organ_s = organ_s;
	}

	public String getSc_TableName() {
		if(!StringUtil.isEmpty(this.scId)) {
			ProcessStep p = (ProcessStep)getService().findById(ProcessStep.class, this.scId);
			return p.getTable_name();
		}
		return sc_TableName;
	}

	public void setSc_TableName(String sc_TableName) {
		this.sc_TableName = sc_TableName;
	}

	@Override
	public String toString() {
		return this.id;
	}

}
