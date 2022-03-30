package com.cat.file.jsonbean;

import com.alibaba.fastjson.JSONObject;

public class ResultFileBean {

	private Integer status;

	private String url;
	
	private FileInfoBean fileinfo;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public FileInfoBean getFileinfo() {
		return fileinfo;
	}

	public void setFileinfo(FileInfoBean fileinfo) {
		this.fileinfo = fileinfo;
	}

	public ResultFileBean() {

	}

	public ResultFileBean(Integer status, String url) {
		this.status = status;
		this.url = url;
	}

	public static String getSucess(FileInfoBean fileinfo) {
		ResultFileBean bean = new ResultFileBean();
		bean.setStatus(200);
		bean.setFileinfo(fileinfo);
		return JSONObject.toJSONString(bean);
	}
	
	public static String getSucess(String url) {
		ResultFileBean bean = new ResultFileBean();
		bean.setStatus(200);
		bean.setUrl(url);
		return JSONObject.toJSONString(bean);
	}

	public static String getError() {
		ResultFileBean bean = new ResultFileBean();
		bean.setStatus(900);
		return JSONObject.toJSONString(bean);
	}
}
