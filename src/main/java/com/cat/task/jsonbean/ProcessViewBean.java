package com.cat.task.jsonbean;

import java.util.ArrayList;
import java.util.List;

public class ProcessViewBean {

	private String title;
	
	private Integer current;
	
	private List<ProcessStepBean> steps = new ArrayList<ProcessStepBean>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getCurrent() {
		return current;
	}

	public void setCurrent(Integer current) {
		this.current = current;
	}

	public List<ProcessStepBean> getSteps() {
		return steps;
	}

	public void setSteps(List<ProcessStepBean> steps) {
		this.steps = steps;
	}

}
