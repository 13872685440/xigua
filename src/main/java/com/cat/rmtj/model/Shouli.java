package com.cat.rmtj.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;
import com.cat.task.model.ProcessEntity;

@Entity
@Table(name = "rmtj_shouli")
@Inheritance(strategy = InheritanceType.JOINED)
@BatchSize(size = 50)
@DynamicInsert
public class Shouli extends ProcessEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6235009038081877625L;
	
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(length = 40)
	private String id;
	
	/** 纠纷类型 */
	@Column(length = 40)
	private String jflx;
	
	/** 难易程度 */
	@Column(length = 40)
	private String nycd;
	
	/** 纠纷概况 */
	@Lob
	private String jfgk;
	
	/** 申请事项 */
	@Column(length = 1000)
	private String sqxs;
	
	/** 所属区域 */
	@Column(length = 40)
	private String ssqy;
	
	/** 详细地址 */
	@Column(length = 200)
	private String xxdz;
	
	/** 受理时间 */
	@Column(length = 30)
	private String slsj;
	
	/** 指派单位 */
	@Column(length = 40)
	private String zpdw;
	
	/** 指派时间 */
	@Column(length = 40)
	private String zpsj;
	
	/** 调解情况 */
	@Lob
	private String tjqk;
	
	/** 调解时间 */
	@Column(length = 40)
	private String tjsj;
	
	/** 调解结果 */
	@Column(length = 40)
	private String tjjg;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJflx() {
		return jflx;
	}

	public void setJflx(String jflx) {
		this.jflx = jflx;
	}

	public String getJfgk() {
		return jfgk;
	}

	public void setJfgk(String jfgk) {
		this.jfgk = jfgk;
	}

	public String getSqxs() {
		return sqxs;
	}

	public void setSqxs(String sqxs) {
		this.sqxs = sqxs;
	}

	public String getSsqy() {
		return ssqy;
	}

	public void setSsqy(String ssqy) {
		this.ssqy = ssqy;
	}

	public String getXxdz() {
		return xxdz;
	}

	public void setXxdz(String xxdz) {
		this.xxdz = xxdz;
	}

	public String getNycd() {
		return nycd;
	}

	public void setNycd(String nycd) {
		this.nycd = nycd;
	}

	public String getSlsj() {
		return slsj;
	}

	public void setSlsj(String slsj) {
		this.slsj = slsj;
	}

	public String getZpdw() {
		return zpdw;
	}

	public void setZpdw(String zpdw) {
		this.zpdw = zpdw;
	}

	public String getZpsj() {
		return zpsj;
	}

	public void setZpsj(String zpsj) {
		this.zpsj = zpsj;
	}

	public String getTjqk() {
		return tjqk;
	}

	public void setTjqk(String tjqk) {
		this.tjqk = tjqk;
	}

	public String getTjsj() {
		return tjsj;
	}

	public void setTjsj(String tjsj) {
		this.tjsj = tjsj;
	}

	public String getTjjg() {
		return tjjg;
	}

	public void setTjjg(String tjjg) {
		this.tjjg = tjjg;
	}

	@Override
	public String toString() {
		return this.id;
	}
}
