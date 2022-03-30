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
import com.cat.boot.util.NameQueryUtil;
import com.cat.boot.util.StringUtil;

@Entity
@Table(name = "Task_Step_Condition")
@BatchSize(size = 50)
@DynamicInsert
public class StepCondition extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -905342692003324643L;
	
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(length = 40)
	private String id;
	
	/** 步骤id */
	@Column(length = 40, nullable= false)
	private String step_id;
	
	/** 流程走向 =0时，回到申请人 =99时 流程结束*/
	@Column(length = 10)
	private Integer to_next;
	
	/** 显示的button 或者条件单的名称*/
	@Column(length = 40)
	private String name;
	
	/** 结论 */
	@Column(length = 40)
	private String conclusion;
	
	/** 排序 显示*/
	@Column(length = 10)
	private Integer xh;
	
	/** 如果是按钮，显示的颜色*/
	@Column(length = 20)
	private String color;
	
	/** 是否显示意见 0=不填写意见 1=可填写意见 2=意见必填*/
	@Column(length = 20)
	private String showYj;
	
	@Transient
	private String step_name;
	
	@Transient
	private String next_step_name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStep_id() {
		return step_id;
	}

	public void setStep_id(String step_id) {
		this.step_id = step_id;
	}

	public Integer getTo_next() {
		return to_next;
	}

	public void setTo_next(Integer to_next) {
		this.to_next = to_next;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConclusion() {
		return conclusion;
	}

	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}

	public Integer getXh() {
		return xh;
	}

	public void setXh(Integer xh) {
		this.xh = xh;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	public String getStep_name() {
		if(!StringUtil.isEmpty(this.step_id)) {
			ProcessStep s = (ProcessStep)getService().findById(ProcessStep.class, this.step_id);
			return s.getName();
		}
		return step_name;
	}

	public void setStep_name(String step_name) {
		this.step_name = step_name;
	}

	public String getNext_step_name() {
		if(this.to_next!=null) {
			if(this.to_next == 0) {
				this.next_step_name = "申请人";
			} else if(this.to_next == 99) {
				this.next_step_name = "结束";
			} else {
				// 查询指向
				ProcessStep p = (ProcessStep)getService().findById(ProcessStep.class, this.step_id);
				if(p!=null) {
					ProcessStep ps = (ProcessStep)getService().getSimple("ProcessStep", null, true,
							NameQueryUtil.setParams("scId",p.getScId(),"step",this.to_next));
					if(ps!=null) {
						this.next_step_name = ps.getName();
					}
				}
						
			}
		}
		return next_step_name;
	}

	public void setNext_step_name(String next_step_name) {
		this.next_step_name = next_step_name;
	}

	public String getShowYj() {
		return showYj;
	}

	public void setShowYj(String showYj) {
		this.showYj = showYj;
	}

	@Override
	public String toString() {
		return this.id;
	}
	
	
}
