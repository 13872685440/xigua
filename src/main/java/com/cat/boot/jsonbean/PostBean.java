package com.cat.boot.jsonbean;

import java.util.ArrayList;
import java.util.List;

public class PostBean {
	
	private String piId;

	private String organId;
	
	private String organName;
	
	private String isleaf;
	
	private String post_names;
	
	private List<String> userRoles = new ArrayList<String>();

	public String getPiId() {
		return piId;
	}

	public void setPiId(String piId) {
		this.piId = piId;
	}

	public String getOrganId() {
		return organId;
	}

	public void setOrganId(String organId) {
		this.organId = organId;
	}

	public String getOrganName() {
		return organName;
	}

	public void setOrganName(String organName) {
		this.organName = organName;
	}

	public String getPost_names() {
		return post_names;
	}

	public void setPost_names(String post_names) {
		this.post_names = post_names;
	}

	public List<String> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<String> userRoles) {
		this.userRoles = userRoles;
	}

	public String getIsleaf() {
		return isleaf;
	}

	public void setIsleaf(String isleaf) {
		this.isleaf = isleaf;
	}
}
