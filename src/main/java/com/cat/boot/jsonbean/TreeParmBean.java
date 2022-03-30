package com.cat.boot.jsonbean;

import java.util.ArrayList;
import java.util.List;

public class TreeParmBean {

	// 根code 全部时为空
	private String root;
	
	// 展开至第几层 =999时全部展开
	private Integer open;
	
	// 其他参数
	private Integer other;
	
	//表名
	private String table_name;
	
	// 主键
	private String key;
	
	// 排序
	private String sort;
	
	// 属性
	private String props;
	
	private List<String> ids = new ArrayList<String>(); 

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public Integer getOpen() {
		return open;
	}

	public void setOpen(Integer open) {
		this.open = open;
	}

	public Integer getOther() {
		return other;
	}

	public void setOther(Integer other) {
		this.other = other;
	}

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getProps() {
		return props;
	}

	public void setProps(String props) {
		this.props = props;
	}
}
