package com.cat.boot.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.cat.boot.config.JedisUtil;
import com.cat.boot.jsonbean.BaseQueryHelp;
import com.cat.boot.jsonbean.IResultConvert;

public abstract class BaseQueryRedis implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3053594133513794832L;

	@Autowired
	public JedisUtil jedisUtil;

	private BaseQueryHelp qhb = new BaseQueryHelp();

	/** 查询结果 */
	@SuppressWarnings("rawtypes")
	protected List results = new ArrayList();

	public void executeCountQuery(String key,int i) throws Exception {
		setTotalRecordCount(jedisUtil.llen(key, i));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void executeQuery(String key,int i) throws Exception {
		if (getPageSize() == -1) {
			setResults(((IResultConvert) this).toResult(jedisUtil.lrange(key, 0, -1, i)));
		} else {
			setResults(((IResultConvert) this).toResult(jedisUtil.lrange(key, getOffset(), getPageSize(), i)));
		}
	}

	@SuppressWarnings("rawtypes")
	protected void setResults(List results) {
		this.results = null;
		this.results = results;
	}

	public long getPageSize() {
		return getQhb().getPageSize()*getQhb().getPageNo()-1;
	}

	public BaseQueryHelp getQhb() {
		return this.qhb;
	}

	public void setQhb(BaseQueryHelp qhb) {
		this.qhb = qhb;
	}

	@SuppressWarnings("rawtypes")
	public List getResults() {
		return results;
	}
	
	protected void setTotalRecordCount(long totalRecordCount) {
		getQhb().setTotalRecordCount(totalRecordCount);
	}

	public long getOffset() {
		return getQhb().getOffset();
	}

}
