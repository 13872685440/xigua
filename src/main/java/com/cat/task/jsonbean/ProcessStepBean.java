package com.cat.task.jsonbean;

import java.util.ArrayList;
import java.util.List;

public class ProcessStepBean {

	private String name;
	
	private String jl;
	
	private List<CommentBean> currentTask = new ArrayList<CommentBean>();
	
	private List<CommentBean> comments = new ArrayList<CommentBean>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJl() {
		return jl;
	}

	public void setJl(String jl) {
		this.jl = jl;
	}

	public List<CommentBean> getCurrentTask() {
		return currentTask;
	}

	public void setCurrentTask(List<CommentBean> currentTask) {
		this.currentTask = currentTask;
	}

	public List<CommentBean> getComments() {
		return comments;
	}

	public void setComments(List<CommentBean> comments) {
		this.comments = comments;
	}
}
