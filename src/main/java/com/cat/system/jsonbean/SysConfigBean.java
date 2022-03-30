package com.cat.system.jsonbean;

/** 系统配置bean */
public class SysConfigBean {
	
	/** 系统名称 */
	private String system_name;
	
	/** 数据库*/
	private String db_type;
	
	/** 开发单位*/
	private String company_name;
	
	/** 项目经理*/
	private String manager_name;
	
	/** 联系电话*/
	private String phone;
	
	/** 服务时间*/
	private String service_time;

	public String getSystem_name() {
		return system_name;
	}

	public void setSystem_name(String system_name) {
		this.system_name = system_name;
	}

	public String getDb_type() {
		return db_type;
	}

	public void setDb_type(String db_type) {
		this.db_type = db_type;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getManager_name() {
		return manager_name;
	}

	public void setManager_name(String manager_name) {
		this.manager_name = manager_name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getService_time() {
		return service_time;
	}

	public void setService_time(String service_time) {
		this.service_time = service_time;
	}
}
