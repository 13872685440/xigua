package com.cat.task.jsonbean;

public class CommentBean {
	
	// 经办步骤名
	private String name;

	// 经办人
	private String jbr;
	
	// 经办时间
	private String jbsj;
	
	// 经办结论
	private String shjl;
	
	// 经办意见
	private String shyj;
	
	// 当前经办未办的步骤=0 同一步骤多次经办的最后一次经办=1 同一步骤多次经办的其他经办=2
	private String level;
	
	private String time_out_type;

	public String getJbr() {
		return jbr;
	}

	public void setJbr(String jbr) {
		this.jbr = jbr;
	}

	public String getJbsj() {
		return jbsj;
	}

	public void setJbsj(String jbsj) {
		this.jbsj = jbsj;
	}

	public String getShjl() {
		return shjl;
	}

	public void setShjl(String shjl) {
		this.shjl = shjl;
	}

	public String getShyj() {
		return shyj;
	}

	public void setShyj(String shyj) {
		this.shyj = shyj;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTime_out_type() {
		return time_out_type;
	}

	public void setTime_out_type(String time_out_type) {
		this.time_out_type = time_out_type;
	}
}
