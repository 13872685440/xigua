package com.cat.rmtj.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;

import com.cat.boot.model.BaseEntity;
import com.cat.boot.util.StringUtil;
import com.cat.dictionary.model.Dictionary;

@Entity
@Table(name = "rmtj_tiaowh")
@Inheritance(strategy = InheritanceType.JOINED)
@BatchSize(size = 50)
@DynamicInsert
public class Tiaowh extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9051760407524600989L;

	/** 组织机构类型 */
	@Column(length = 40)
	private String orgtype;
	
	/** 联系电话 */
	@Column(length = 40)
	private String lxdh;
	
	/** 地址 */
	@Column(length = 200)
	private String address;
	
	/** 监控编号 */
	@Column(length = 50)
	private String jksbh;

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
	@Column(length = 1000)
	private String wn;

	/** 名称 */
	@Column(length = 500, nullable = false)
	private String name;

	/** 上级 */
	@Column(length = 40)
	private String scId;

	/** 末级 */
	@Column(nullable = false,columnDefinition="int")
	private boolean isLeaf = true;

	/** 本级排序 */
	@Column
	private Integer xh = 1;

	/** 权值 */
	@Column
	private Integer weighted;
	
	@Transient
	private String scName;
	
	@Transient
	private String typeName;
	
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

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public Integer getXh() {
		return xh;
	}

	public void setXh(Integer xh) {
		this.xh = xh;
	}

	public Integer getWeighted() {
		return weighted;
	}

	public void setWeighted(Integer weighted) {
		this.weighted = weighted;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return this.id;
	}

	@Override
	public String getId() {
		return this.id;
	}

	public String getOrgtype() {
		return orgtype;
	}

	public void setOrgtype(String orgtype) {
		this.orgtype = orgtype;
	}

	public String getScId() {
		return scId;
	}

	public void setScId(String scId) {
		this.scId = scId;
	}

	public String getScName() {
		if(!StringUtil.isEmpty(this.scId)) {
			Tiaowh o = (Tiaowh)getService().findById(Tiaowh.class, this.scId);
			if(o!=null) {
				return o.getName();
			}
		}
		return scName;
	}

	public void setScName(String scName) {
		this.scName = scName;
	}

	public String getTypeName() {
		if(!StringUtil.isEmpty(this.orgtype)) {
			Dictionary o = (Dictionary)getService().findById(Dictionary.class, this.orgtype);
			if(o!=null) {
				return o.getName();
			}
		}
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
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

	public String getLxdh() {
		return lxdh;
	}

	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getJksbh() {
		return jksbh;
	}

	public void setJksbh(String jksbh) {
		this.jksbh = jksbh;
	}
}
