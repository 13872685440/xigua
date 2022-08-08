package com.cat.task.jsonbean;

import java.util.ArrayList;
import java.util.List;

public class ProcessViewBean {

	private String title;
	
	private Integer current;
	
	private Integer comment_current;

	private List<CommentBean> comments = new ArrayList<CommentBean>();
	
	private List<CommentBean> prcocess_steps = new ArrayList<CommentBean>();
	
	// 废弃
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

	public List<CommentBean> getComments() {
		return comments;
	}

	public void setComments(List<CommentBean> comments) {
		this.comments = comments;
	}

	public Integer getComment_current() {
		return comment_current;
	}

	public void setComment_current(Integer comment_current) {
		this.comment_current = comment_current;
	}

	public List<CommentBean> getPrcocess_steps() {
		return prcocess_steps;
	}

	public void setPrcocess_steps(List<CommentBean> prcocess_steps) {
		this.prcocess_steps = prcocess_steps;
	}

}
