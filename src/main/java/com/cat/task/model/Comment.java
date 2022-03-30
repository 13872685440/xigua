package com.cat.task.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;

import com.cat.boot.model.BaseEntity;

@Entity
@Table(name = "Task_Comment")
@BatchSize(size = 50)
@DynamicInsert
public class Comment extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3547787770637803114L;
	
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(length = 40)
	private String id;
	
	/** 对应的流程步骤 申请人提交时 用process0000记录 */
	@Column(length = 40, nullable= false)
	private String step_id;
	
	/** 实体bean关键域值 */
	@Column(nullable = false, length = 50)
	private String key_value;
	
	/** 审核意见 */
	@Lob
	private String shyj;
	
	/** 审核时间 */
	@Column(name = "shtime", length = 30)
	private String shtime;
	
	/** 审核用户 */
	@Column(nullable = true)
	private String shr_id;

	/** 冗余字段 */
	@Column(nullable = true)
	private String shr;
	
	/** 审核机构 */
	@Column(nullable = true, name = "shjg_id", length = 50)
	private String shjg_id;
	
	/** 审核机构名称 */
	@Column(nullable = true, name = "shjg_name", length = 50)
	private String shjg_name;
	
	/** 审核结论 */
	@Column(nullable = true, name = "shjl", length = 50)
	private String shjl;

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

	public String getKey_value() {
		return key_value;
	}

	public void setKey_value(String key_value) {
		this.key_value = key_value;
	}

	public String getShyj() {
		return shyj;
	}

	public void setShyj(String shyj) {
		this.shyj = shyj;
	}

	public String getShtime() {
		return shtime;
	}

	public void setShtime(String shtime) {
		this.shtime = shtime;
	}

	public String getShr_id() {
		return shr_id;
	}

	public void setShr_id(String shr_id) {
		this.shr_id = shr_id;
	}

	public String getShr() {
		return shr;
	}

	public void setShr(String shr) {
		this.shr = shr;
	}

	public String getShjg_id() {
		return shjg_id;
	}

	public void setShjg_id(String shjg_id) {
		this.shjg_id = shjg_id;
	}

	public String getShjg_name() {
		return shjg_name;
	}

	public void setShjg_name(String shjg_name) {
		this.shjg_name = shjg_name;
	}

	public String getShjl() {
		return shjl;
	}

	public void setShjl(String shjl) {
		this.shjl = shjl;
	}

	@Override
	public String toString() {
		return this.id;
	}
}
