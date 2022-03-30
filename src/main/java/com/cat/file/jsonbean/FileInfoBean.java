package com.cat.file.jsonbean;

import java.util.ArrayList;
import java.util.List;

import com.cat.boot.util.StringUtil;
import com.cat.file.model.FileInfo;
import com.cat.file.model.FileRecord;

public class FileInfoBean {
	
	private String id;

	private String uid;

	private String name;

	private String status = "done";

	private ResultFileBean response;

	private String url;
	
	private String file_ext;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ResultFileBean getResponse() {
		return response;
	}

	public void setResponse(ResultFileBean response) {
		this.response = response;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFile_ext() {
		return file_ext;
	}

	public void setFile_ext(String file_ext) {
		this.file_ext = file_ext;
	}

	public static FileInfoBean setThis(FileRecord entity) {
		FileInfo info = entity.getFileInfo();
		FileInfoBean bean = new FileInfoBean();
		bean.setId(entity.getId());
		bean.setUid(info.getId());
		bean.setName(info.getOriginalName());
		bean.setUrl(info.getUrlName());
		bean.setResponse(new ResultFileBean(200, entity.getId()));

		return bean;
	}

	public static List<FileInfoBean> setThis(List<FileRecord> entitys) {
		List<FileInfoBean> beans = new ArrayList<FileInfoBean>();
		if (!StringUtil.isListEmpty(entitys)) {
			for (FileRecord bean : entitys) {
				beans.add(setThis(bean));
			}
		}
		return beans;
	}
}
