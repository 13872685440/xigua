package com.cat.system.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import com.cat.boot.model.BaseEntity;
import com.cat.boot.util.StringUtil;
import com.cat.dictionary.model.Dictionary;

@Entity
@Table(name = "SYS_App_Tree")
@BatchSize(size = 50)
@DynamicInsert
public class AppTree extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2670124752028136041L;

	/** 描述 */
	@Column(length = 50)
	@Length(max = 50)
	private String des;

	/** 路径 */
	@Column(length = 500)
	@Length(max = 500)
	private String path;

	/** 排序 */
	@Column
	private Integer xh = 10;

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
	@Column(length = 600, nullable = false)
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
	
	/** 组件 */
	@Column(length = 100)
	private String component;
	
	/** 图标 */
	@Column(length = 20)
	private String icon;

	@Lob
	@Column
	@Basic(fetch = FetchType.LAZY)
	private String titleImage;

	@Column
	private Long lxh;
	
	/** 多端设备渲染，当为空的时候，只渲染web端，中间用','隔开*/
	@Column(length = 50)
	private String multi_ape;
	
	/** 图标背景颜色(app) */
	@Column(length = 10)
	private String colorCode;
	
	/** 图标字体代码(app)*/
	@Column
	private Long fontCode;
	
	/** 图标库(app)*/
	@Column(length = 30)
	private String fontFamily; 
	
	/** 是否常用应用*/
	@Column(columnDefinition="int")
	private Boolean usually = false;
	
	/** 关联表 表名+.属性的方式 用 |隔开*/
	@Column(length = 500)
	private String relational_table;
	
	/** 查询参数 */
	@Lob
	@Column
	@Basic(fetch = FetchType.LAZY)
	private String query_params;
	
	@Transient
	private String scName;
	
	@Transient
	private String scWnName;
	
	@Transient
	private List<String> multi_apes = new ArrayList<String>();

	public Long getLxh() {
		return lxh;
	}

	public void setLxh(Long lxh) {
		this.lxh = lxh;
	}

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

	
	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getXh() {
		return xh;
	}

	public void setXh(Integer xh) {
		this.xh = xh;
	}

	public String getTitleImage() {
		return titleImage;
	}

	public void setTitleImage(String titleImage) {
		this.titleImage = titleImage;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
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
			AppTree o = (AppTree)getService().findById(AppTree.class, this.scId);
			if(o!=null) {
				return o.getName();
			}
		}
		return scName;
	}

	public void setScName(String scName) {
		this.scName = scName;
	}

	@Override
	public String toString() {
		return this.id;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public Long getFontCode() {
		return fontCode;
	}

	public void setFontCode(Long fontCode) {
		this.fontCode = fontCode;
	}

	public String getFontFamily() {
		return fontFamily;
	}

	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}

	public Boolean getUsually() {
		return usually;
	}

	public void setUsually(Boolean usually) {
		this.usually = usually;
	}

	public String getMulti_ape() {
		return multi_ape;
	}

	public void setMulti_ape(String multi_ape) {
		this.multi_ape = multi_ape;
	}

	public List<String> getMulti_apes() {
		if(StringUtil.isListEmpty(multi_apes)) {
			if(!StringUtil.isEmpty(multi_ape)) {
				multi_apes.addAll(Arrays.asList(multi_ape.split(",")));
			}
		}
		return multi_apes;
	}

	public void setMulti_apes(List<String> multi_apes) {
		this.multi_apes = multi_apes;
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

	public String getRelational_table() {
		return relational_table;
	}

	public void setRelational_table(String relational_table) {
		this.relational_table = relational_table;
	}
	
	public String getQuery_params() {
		return query_params;
	}

	public void setQuery_params(String query_params) {
		this.query_params = query_params;
	}

	public String getAppPath() {
		return "/listview";
	}
	
	public String getQueryPath() {
		return this.getPath() + "/main";
	}
	
	@Override
	public String getList_title() {
		return this.name;
	}
	
	@Override
	public String getSub_title() {
		return this.id;
	}
}
