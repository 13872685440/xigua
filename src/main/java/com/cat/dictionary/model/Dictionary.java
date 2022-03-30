package com.cat.dictionary.model;

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

@Entity
@Table(name = "Data_Dictionary")
@BatchSize(size = 50)
@DynamicInsert
public class Dictionary extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2552638125742346820L;

	@Column(length = 20)
	private String typeCode;
	
	@Column(length = 20)
	private String colorCode;
	
	@Column(length = 50, nullable = true)
	private String expand;
	
	@Column
	private Integer xh = 1;

	/** 编码 作为ID使用 */
	@Id
	@GeneratedValue(generator = "assignedGenerator")
	@GenericGenerator(name = "assignedGenerator", strategy = "assigned")
	@Column(length = 40)
	private String id;

	/** 本级代码 current Level Code */
	@Column(length = 8, nullable = false)
	private String clc;

	/** 完整的名称 wholeName */
	@Column(length = 250, nullable = false)
	private String wn;

	/** 名称 */
	@Column(length = 50, nullable = false)
	private String name;

	/** 上级 */
	@Column(length = 40)
	private String scId;

	/** 简称 ShortName */
	@Column(length = 50, nullable = true)
	private String sn;

	/** 末级 */
	@Column(nullable = false,columnDefinition="int")
	private boolean isLeaf = true;
	
	@Transient
	private String scName;
	
	@Transient
	private String scWnName;

	public String getClc() {
		return clc;
	}

	public void setClc(String clc) {
		this.clc = clc;
	}

	public String getWn() {
		return wn;
	}

	public void setWn(String wn) {
		this.wn = wn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	
	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public Integer getXh() {
		return xh;
	}

	public void setXh(Integer xh) {
		this.xh = xh;
	}

	public String getExpand() {
		return expand;
	}

	public void setExpand(String expand) {
		this.expand = expand;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getScId() {
		return scId;
	}

	public void setScId(String scId) {
		this.scId = scId;
	}

	public String getScName() {
		if(!StringUtil.isEmpty(this.scId)) {
			Dictionary o = (Dictionary)getService().findById(Dictionary.class, this.scId);
			if(o!=null) {
				return o.getName();
			}
		}
		return scName;
	}

	public void setScName(String scName) {
		this.scName = scName;
	}

	public String getScWnName() {
		if(!StringUtil.isEmpty(this.scId)) {
			Dictionary o = (Dictionary)getService().findById(Dictionary.class, this.scId);
			if(o!=null) {
				return o.getWn();
			}
		}
		return scWnName;
	}

	public void setScWnName(String scWnName) {
		this.scWnName = scWnName;
	}

	@Override
	public String toString() {
		return this.id;
	}

}
