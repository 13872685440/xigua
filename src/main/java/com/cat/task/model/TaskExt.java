package com.cat.task.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;

import com.cat.boot.model.BaseEntity;
import com.cat.boot.util.NameQueryUtil;
import com.cat.boot.util.StringUtil;
import com.cat.system.model.User;
import com.cat.task.enumable.TaskType;

@Entity
@Table(name = "Task_TaskExt")
@BatchSize(size = 50)
@DynamicInsert
public class TaskExt extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1402605775630460371L;
	
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(length = 40)
	private String id;
	
	/** 对应的流程步骤 退回申请人时 用process0000记录 */
	@Column(length = 40, nullable= false)
	private String step_id;
	
	/** 实体bean关键域值 */
	@Column(nullable = false, length = 50)
	private String key_value;
	
	/** 经办人 当经办人为多人时,用,隔开 */
	@Column(nullable = true, length = 500, name = "transactor_groups")
	private String transactorgroups;

	/** 当前经办人 */
	@Column(nullable = true, length = 40, name = "transactor")
	private String transactor;
	
	/** 申请人 id 用来发送提醒 */
	@Column(nullable = true, length = 50, name = "sqr_id")
	private String sqr_id;
	
	/** 上一步审核人 id 用来发送提醒 */
	@Column(nullable = true, length = 50, name = "previous_id")
	private String previous_id;
	
	/** 任务办理状态 */
	@Enumerated(EnumType.STRING)
	@Column(name = "task_type")
	private TaskType taskType = TaskType.待办;
	
	@Transient
	private String task_name;
	
	@Transient
	private String sqr_name;
	
	@Transient
	private String previous_name;
	
	@Transient
	private String step_name;
	
	@Transient
	private String transactor_name;
	
	@Transient
	private String transactorgroups_name;
	
	@Transient
	private Map<String,String> params_map = new HashMap<String,String>();
	
	public String getStep_id() {
		return step_id;
	}

	public void setStep_id(String step_id) {
		this.step_id = step_id;
	}

	public String getKey_value() {
		return key_value;
	}

	public void setKey_value(String key_value) {
		this.key_value = key_value;
	}

	public String getTransactorgroups() {
		return transactorgroups;
	}

	public void setTransactorgroups(String transactorgroups) {
		this.transactorgroups = transactorgroups;
	}

	public String getTransactor() {
		return transactor;
	}

	public void setTransactor(String transactor) {
		this.transactor = transactor;
	}

	public String getSqr_id() {
		return sqr_id;
	}

	public void setSqr_id(String sqr_id) {
		this.sqr_id = sqr_id;
	}

	public String getPrevious_id() {
		return previous_id;
	}

	public void setPrevious_id(String previous_id) {
		this.previous_id = previous_id;
	}

	public TaskType getTaskType() {
		return taskType;
	}

	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getTask_name() {
		if(!StringUtil.isEmpty(this.step_id)) {
			ProcessStep p = (ProcessStep)getService().findById(ProcessStep.class, this.step_id.substring(0, 4));
			return p.getName();
		}
		return task_name;
	}

	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}

	public String getStep_name() {
		if(!StringUtil.isEmpty(this.step_id)) {
			if(this.step_id.endsWith("0000")) {
				return "申请人";
			} else {
				ProcessStep p = (ProcessStep)getService().findById(ProcessStep.class, this.step_id);
				return p.getName();
			}
		}
		return step_name;
	}

	public void setStep_name(String step_name) {
		this.step_name = step_name;
	}

	public String getSqr_name() {
		if(!StringUtil.isEmpty(this.sqr_id)) {
			User p = (User)getService().findById(User.class, this.sqr_id);
			return p.getName();
		}
		return sqr_name;
	}

	public void setSqr_name(String sqr_name) {
		this.sqr_name = sqr_name;
	}
	
	public String getPrevious_name() {
		if(!StringUtil.isEmpty(this.previous_id)) {
			User p = (User)getService().findById(User.class, this.previous_id);
			return p.getName();
		}
		return previous_name;
	}

	public void setPrevious_name(String previous_name) {
		this.previous_name = previous_name;
	}

	public Map<String, String> getParams_map() {
		params_map.clear();
		if(!StringUtil.isEmpty(this.step_id)) {
			String step_id_x = this.step_id.substring(0, 4);
			params_map.put("step_id", step_id_x);
			if(this.step_id.endsWith("0000")) {
				ProcessStep p = (ProcessStep)getService().findById(ProcessStep.class, this.step_id.substring(0, 4));
				initMap(params_map,p);
				return params_map;
			} else {
				ProcessStep p = (ProcessStep)getService().findById(ProcessStep.class, this.step_id);
				initMap(params_map,p);
				return params_map;
			}
		}
		return params_map;
	}

	public void setParams_map(Map<String, String> params_map) {
		this.params_map = params_map;
	}

	@Override
	public String toString() {
		return this.id;
	}
	
	public String getTransactor_name() {
		if(!StringUtil.isEmpty(this.transactor)) {
			User u = (User)getService().findById(User.class, this.transactor);
			return u.getName();
		}
		return transactor_name;
	}

	public void setTransactor_name(String transactor_name) {
		this.transactor_name = transactor_name;
	}

	@SuppressWarnings("unchecked")
	public String getTransactorgroups_name() {
		if(!StringUtil.isEmpty(this.transactorgroups)) {
			List<String> ids = Arrays.asList(this.transactorgroups.split(","));
			List<String> u = (List<String>)getService().
					getList("SYS_USERS", "o.name asc", true, "name",NameQueryUtil.setParams("id",ids));
			return StringUtil.listToString(u);
		}
		return transactorgroups_name;
	}

	public void setTransactorgroups_name(String transactorgroups_name) {
		this.transactorgroups_name = transactorgroups_name;
	}

	private void initMap(Map<String,String> params_map,ProcessStep p) {
		ProcessStep pe = new ProcessStep();
		if(!StringUtil.isEmpty(p.getScId())) {
			pe = (ProcessStep)getService().findById(ProcessStep.class, p.getScId());
		}
		params_map.put("component_path", StringUtil.isEmpty(p.getComponent()) ? pe.getComponent() : p.getComponent());
		params_map.put("service_path", StringUtil.isEmpty(p.getService_path()) ? pe.getService_path() : p.getService_path());
		params_map.put("service_path_type", StringUtil.isEmpty(p.getService_path_type()) ? pe.getService_path_type() : p.getService_path_type());
	}
}
