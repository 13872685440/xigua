package com.cat.system.model;

import java.util.ArrayList;
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
import com.cat.boot.util.NameQueryUtil;
import com.cat.boot.util.StringUtil;

@Entity
@Table(name = "SYS_Role")
@BatchSize(size = 50)
@DynamicInsert
public class Role extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1011294694744644357L;

	/** 角色名称 作为ID使用 */
	@Id
	@GeneratedValue(generator = "assignedGenerator")
	@GenericGenerator(name = "assignedGenerator", strategy = "assigned")
	@Column(length = 40)
	private String id;

	/** 描述 */
	@Column(length = 200, nullable = true)
	private String des;
	
	/** 角色类型 */
	@Column(name = "r_type")
	private String rtype;
	
	@Transient
	private List<String> app_data = new ArrayList<String>();
	
	@Transient
	private List<String> app_data_names = new ArrayList<String>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getRtype() {
		return rtype;
	}

	public void setRtype(String rtype) {
		this.rtype = rtype;
	}

	@Override
	public String toString() {
		// TODO 自动生成的方法存根
		return this.id;
	}

	@SuppressWarnings("unchecked")
	public List<String> getApp_data() {
		if(!StringUtil.isListEmpty(app_data)) {
			return app_data;
		}
		if(!StringUtil.isEmpty(this.id)) {
			List<String> app_datas = (List<String>) getService().getList("sys_role_app", null, true, "app",
				NameQueryUtil.setParams("role", this.id));
			return app_datas;
		}
		return app_data;
	}

	public void setApp_data(List<String> app_data) {
		this.app_data = app_data;
	}

	@SuppressWarnings("unchecked")
	public List<String> getApp_data_names() {
		if(!StringUtil.isListEmpty(app_data_names)) {
			return app_data_names;
		}
		if(!StringUtil.isEmpty(this.id)) {
			List<String> app_datas = (List<String>) getService().getList("sys_role_app", null, true, "app",
				NameQueryUtil.setParams("role", this.id));
			if(!StringUtil.isListEmpty(app_datas)) {
				List<String> app_data_name = (List<String>) getService().getList("SYS_App_Tree", null, true, "name",
						NameQueryUtil.setParams("id", app_datas));
				return app_data_name;
			}
		}
		return app_data_names;
	}

	public void setApp_data_names(List<String> app_data_names) {
		this.app_data_names = app_data_names;
	}
}
