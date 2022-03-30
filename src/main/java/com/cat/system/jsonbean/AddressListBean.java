package com.cat.system.jsonbean;

import java.util.ArrayList;
import java.util.List;

public class AddressListBean {

	private List<AddressBean> organs = new ArrayList<AddressBean>();
	
	private List<AddressBean> persons = new ArrayList<AddressBean>();
	
	private List<AddressBean> roles = new ArrayList<AddressBean>();

	public List<AddressBean> getOrgans() {
		return organs;
	}

	public void setOrgans(List<AddressBean> organs) {
		this.organs = organs;
	}

	public List<AddressBean> getPersons() {
		return persons;
	}

	public void setPersons(List<AddressBean> persons) {
		this.persons = persons;
	}

	public List<AddressBean> getRoles() {
		return roles;
	}

	public void setRoles(List<AddressBean> roles) {
		this.roles = roles;
	}
}
