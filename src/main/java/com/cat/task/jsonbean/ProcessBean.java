package com.cat.task.jsonbean;

import java.util.HashMap;
import java.util.Map;

public class ProcessBean {

	private String step_id;
	
	private String step_name;
	
	private String app_id;
	
	private String entity_id;
	
	private String sqr_id;
	
	private String organ_id;
	
	private String task_id;
	
	private String shyj;
	
	// 标志 0=初始提交 00=重新提交
	private String biaozhi;
	
	private Map<String,Object> maps = new HashMap<String,Object>();

	public String getStep_id() {
		return step_id;
	}

	public void setStep_id(String step_id) {
		this.step_id = step_id;
	}

	public String getEntity_id() {
		return entity_id;
	}

	public void setEntity_id(String entity_id) {
		this.entity_id = entity_id;
	}

	public String getSqr_id() {
		return sqr_id;
	}

	public void setSqr_id(String sqr_id) {
		this.sqr_id = sqr_id;
	}

	public String getOrgan_id() {
		return organ_id;
	}

	public void setOrgan_id(String organ_id) {
		this.organ_id = organ_id;
	}

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public String getStep_name() {
		return step_name;
	}

	public void setStep_name(String step_name) {
		this.step_name = step_name;
	}

	public String getShyj() {
		return shyj;
	}

	public void setShyj(String shyj) {
		this.shyj = shyj;
	}

	public Map<String, Object> getMaps() {
		return maps;
	}

	public void setMaps(Map<String, Object> maps) {
		this.maps = maps;
	}

	public String getBiaozhi() {
		return biaozhi;
	}

	public void setBiaozhi(String biaozhi) {
		this.biaozhi = biaozhi;
	}

}
