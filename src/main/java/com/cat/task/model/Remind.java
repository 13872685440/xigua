package com.cat.task.model;

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
import com.cat.boot.util.StringUtil;
import com.cat.system.model.User;

@Entity
@Table(name = "Task_Remind")
@BatchSize(size = 50)
@DynamicInsert
public class Remind extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3551771885934004628L;
	
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(length = 40)
	private String id;
	
	@Column(length = 200)
	private String name;
	
	/** 接收人 */
	@Column(length = 40)
	private String jsr_id;
	
	/** 任务超时 督办 */
	@Column(length = 20)
	private String r_type;
	
	/** 提醒时间 */
	@Column(length = 40)
	private String txsj;
	
	@Column(length = 40)
	private String task_id;
	
	@Transient
	private String jsr_name;
	
	@Transient
	private TaskExt task;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getR_type() {
		return r_type;
	}

	public void setR_type(String r_type) {
		this.r_type = r_type;
	}

	public String getTxsj() {
		return txsj;
	}

	public void setTxsj(String txsj) {
		this.txsj = txsj;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public TaskExt getTask() {
		if(!StringUtil.isEmpty(this.task_id)) {
			TaskExt t = (TaskExt)getService().findById(TaskExt.class, this.task_id);
			return t;
		}
		return task;
	}

	public void setTask(TaskExt task) {
		this.task = task;
	}

	public String getJsr_id() {
		if(!StringUtil.isEmpty(this.jsr_id)) {
			User u = (User)getService().findById(User.class, this.jsr_id);
			return u.getName();
		}
		return jsr_id;
	}

	public void setJsr_id(String jsr_id) {
		this.jsr_id = jsr_id;
	}

	public String getJsr_name() {
		return jsr_name;
	}

	public void setJsr_name(String jsr_name) {
		this.jsr_name = jsr_name;
	}

	@Override
	public String toString() {
		return this.id;
	}
		
}
