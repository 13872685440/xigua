package com.cat.boot.service;

import com.cat.boot.jsonbean.BaseQueryHelp;

public abstract class BaseNqtQueryRedis extends BaseQueryRedis {

	/**
	 * 
	 */
	private static final long serialVersionUID = -370765071946053646L;

	public abstract String query(BaseQueryHelp parms) throws Exception;

	public void excuteQuery(String key,int i,BaseQueryHelp parms) throws Exception {
		iniQhb(parms);
		executeQuery(key,i);
		executeCountQuery(key,i);
		parms.setTotalRecordCount(getQhb().getTotalRecordCount());
	};

	private void iniQhb(BaseQueryHelp parms) {
		getQhb().setParams(parms.getParams());
		getQhb().setPageSize(parms.getPageSize() == 0 ? 20 : parms.getPageSize());
		getQhb().setPageNo(parms.getPageNo()== 0 ? 1 : parms.getPageNo());
	}
}
