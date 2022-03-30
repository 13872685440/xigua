package com.cat.demo.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.cat.dictionary.model.Canton;

@Entity
@Table(name = "Demo_Tree")
@BatchSize(size = 50)
@DynamicInsert
public class DemoTree extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6114603793412073367L;

	@Id
	@GeneratedValue(generator = "assignedGenerator")
	@GenericGenerator(name = "assignedGenerator", strategy = "assigned")
	@Column(length = 40)
	private String id;
	
	/** 本级 */
	@Column(length = 40)
	private String clc;
	
	/** 上级 */
	@Column(length = 40)
	private String scId;
	
	@Transient
	private String scName;
	
	/** 末级 */
	@Column(nullable = false,columnDefinition="int")
	private boolean isLeaf = true;
	
	/** 文字输入框*/
	@Column(length = 40)
	private String input;
	
	/** 数字输入框 */
	@Column
	private Double inputNumber;
	
	/** 密码输入框 */
	@Column(length = 40)
	private String pwd;
	
	/** textarea输入框*/
	@Column(length = 400)
	private String textarea;
	
	/** 日期选择框 （yymmdd）*/
	@Column(length = 40)
	private String yymmdd;
	
	/** 日期选择框 （yymmddhhmmss）*/
	@Column(length = 40)
	private String yymmddhhmmss;
	
	/** 日期选择框 开始*/
	@Column(length = 40)
	private String bengin_time;
	
	/** 日期选择框 截止*/
	@Column(length = 40)
	private String end_time;
	
	/** 选择器 单选*/
	@Column(length = 40)
	private String sim_select;
	
	/** 选择器 slot联动*/
	private String slot_select;
	
	/** 选择器 多选*/
	@Column(length = 200)
	private String mul_select;
	
	@Transient
	private List<String> mul_selects = new ArrayList<String>();
	
	/** 树 单选*/
	@Column(length = 40)
	private String sim_tree;
	
	/** 树 多选*/
	@Column(length = 200)
	private String mul_tree;
	
	@Transient
	private List<String> mul_trees = new ArrayList<String>();
	
	/** 单选 */
	@Column(length = 40)
	private String radio;
	
	/** 开关*/
	@Column(columnDefinition="int")
	private Boolean switch_sel;
	
	/** 多选 */
	@Column(length = 200)
	private String checkbox;
	
	@Transient
	private List<String> checkboxs = new ArrayList<String>();
	
	/** 勾选是否 */
	@Column(columnDefinition="int")
	private Boolean checkbox_b;
	
	/** 级联选择 */
	@Column(length = 200)
	private String cascader;
	
	/** 评分 */
	@Column(length = 40)
	private Double rate;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public Double getInputNumber() {
		return inputNumber;
	}

	public void setInputNumber(Double inputNumber) {
		this.inputNumber = inputNumber;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getTextarea() {
		return textarea;
	}

	public void setTextarea(String textarea) {
		this.textarea = textarea;
	}

	public String getYymmdd() {
		return yymmdd;
	}

	public void setYymmdd(String yymmdd) {
		this.yymmdd = yymmdd;
	}

	public String getYymmddhhmmss() {
		return yymmddhhmmss;
	}

	public void setYymmddhhmmss(String yymmddhhmmss) {
		this.yymmddhhmmss = yymmddhhmmss;
	}

	public String getBengin_time() {
		return bengin_time;
	}

	public void setBengin_time(String bengin_time) {
		this.bengin_time = bengin_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getSim_select() {
		return sim_select;
	}

	public void setSim_select(String sim_select) {
		this.sim_select = sim_select;
	}

	public String getMul_select() {
		return mul_select;
	}

	public void setMul_select(String mul_select) {
		this.mul_select = mul_select;
	}

	public String getSim_tree() {
		return sim_tree;
	}

	public void setSim_tree(String sim_tree) {
		this.sim_tree = sim_tree;
	}

	public String getMul_tree() {
		return mul_tree;
	}

	public void setMul_tree(String mul_tree) {
		this.mul_tree = mul_tree;
	}

	public String getRadio() {
		return radio;
	}

	public void setRadio(String radio) {
		this.radio = radio;
	}

	public Boolean getSwitch_sel() {
		return switch_sel;
	}

	public void setSwitch_sel(Boolean switch_sel) {
		this.switch_sel = switch_sel;
	}

	public String getCheckbox() {
		return checkbox;
	}

	public void setCheckbox(String checkbox) {
		this.checkbox = checkbox;
	}

	public Boolean getCheckbox_b() {
		return checkbox_b;
	}

	public void setCheckbox_b(Boolean checkbox_b) {
		this.checkbox_b = checkbox_b;
	}

	public String getCascader() {
		return cascader;
	}

	public void setCascader(String cascader) {
		this.cascader = cascader;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public List<String> getMul_selects() {
		if(StringUtil.isListEmpty(this.mul_selects)) {
			if(!StringUtil.isEmpty(this.mul_select)) {
				mul_selects.addAll(Arrays.asList(this.mul_select.split(",")));
			}
		}
		return mul_selects;
	}

	public void setMul_selects(List<String> mul_selects) {
		this.mul_selects = mul_selects;
	}

	public List<String> getMul_trees() {
		if(StringUtil.isListEmpty(this.mul_trees)) {
			if(!StringUtil.isEmpty(this.mul_tree)) {
				mul_trees.addAll(Arrays.asList(this.mul_tree.split(",")));
			}
		}
		return mul_trees;
	}

	public void setMul_trees(List<String> mul_trees) {
		this.mul_trees = mul_trees;
	}

	public List<String> getCheckboxs() {
		if(StringUtil.isListEmpty(this.checkboxs)) {
			if(!StringUtil.isEmpty(this.checkbox)) {
				checkboxs.addAll(Arrays.asList(this.checkbox.split(",")));
			}
		}
		return checkboxs;
	}

	public void setCheckboxs(List<String> checkboxs) {
		this.checkboxs = checkboxs;
	}

	public String getSlot_select() {
		return slot_select;
	}

	public void setSlot_select(String slot_select) {
		this.slot_select = slot_select;
	}

	public String getScId() {
		return scId;
	}

	public void setScId(String scId) {
		this.scId = scId;
	}

	public String getScName() {
		if(!StringUtil.isEmpty(this.scId)) {
			Canton o = (Canton)getService().findById(Canton.class, this.scId);
			if(o!=null) {
				return o.getName();
			}
		}
		return scName;
	}

	public void setScName(String scName) {
		this.scName = scName;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	
	public String getClc() {
		return clc;
	}

	public void setClc(String clc) {
		this.clc = clc;
	}

	@Override
	public String toString() {
		return this.id;
	}
	
}
