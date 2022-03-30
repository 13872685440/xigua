package com.cat.task.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.cat.boot.model.BaseEntity;
import com.cat.boot.util.StringUtil;

@MappedSuperclass
public abstract class ProcessEntity extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1890647570802670984L;
	
	/** 流程主ID */
	@Column(length = 40)
	private String step_id;
	
	/** 当前步骤 */
	@Column(length = 40)
	private Integer step = 0;
	
	/** 当前节点 */
	@Column(length = 40)
	private String step_name;
	
	/** 当前结论 */
	@Column(length = 40)
	private String conclusion;
	
	@Transient
	private Boolean canEdit;
	
	@Transient
	private Boolean canView;
	
	@Transient
	private Boolean canDelete;

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	public String getStep_name() {
		return step_name;
	}

	public void setStep_name(String step_name) {
		this.step_name = step_name;
	}

	public String getConclusion() {
		return conclusion;
	}

	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}

	public String getStep_id() {
		return step_id;
	}

	public void setStep_id(String step_id) {
		this.step_id = step_id;
	}

	public Boolean getCanEdit() {
		if (this.step==0) {
			return true;
		} else {
			return false;
		}
	}

	public void setCanEdit(Boolean canEdit) {
		this.canEdit = canEdit;
	}

	public Boolean getCanView() {
		if(this.step!=0) {
			return true;
		} else {
			return false;
		}
	}

	public void setCanView(Boolean canView) {
		this.canView = canView;
	}

	public Boolean getCanDelete() {
		if(this.step==0 && StringUtil.isEmpty(this.step_id)) {
			return true;
		} else {
			return false;
		}
	}

	public void setCanDelete(Boolean canDelete) {
		this.canDelete = canDelete;
	}
	
	
}
