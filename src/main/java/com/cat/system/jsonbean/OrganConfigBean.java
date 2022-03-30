package com.cat.system.jsonbean;

/** 组织机构 配置bean*/
public class OrganConfigBean {

	/** 是否引入组织机构*/
	private String hasOrgan;
	
	/** 是否引入岗位*/
	private String hasPost;
	
	/** 是否支持一人多部门*/
	private String mutiOrgan;
	
	/** 是否支持一人多岗*/
	private String mutiPost;

	public String getHasOrgan() {
		return hasOrgan;
	}

	public void setHasOrgan(String hasOrgan) {
		this.hasOrgan = hasOrgan;
	}

	public String getHasPost() {
		return hasPost;
	}

	public void setHasPost(String hasPost) {
		this.hasPost = hasPost;
	}

	public String getMutiOrgan() {
		return mutiOrgan;
	}

	public void setMutiOrgan(String mutiOrgan) {
		this.mutiOrgan = mutiOrgan;
	}

	public String getMutiPost() {
		return mutiPost;
	}

	public void setMutiPost(String mutiPost) {
		this.mutiPost = mutiPost;
	}

}
