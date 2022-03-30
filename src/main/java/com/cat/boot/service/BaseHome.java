package com.cat.boot.service;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import org.springframework.beans.factory.annotation.Autowired;

import com.cat.boot.config.JedisUtil;
import com.cat.boot.model.BaseEntity;
import com.cat.boot.util.StringUtil;
import com.cat.file.controller.MinioFileController;

public abstract class BaseHome<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5126008259707450002L;
	
	@Autowired
	public BaseService baseService;
	
	@Autowired
	public JedisUtil jedisUtil;
	
	@Autowired
	public MinioFileController minioFileController;

	@SuppressWarnings("unchecked")
	public T findById(String id) {
		return (T) baseService.findById(getEntityClass(), id);
	}

	public boolean isNew(BaseEntity be) {
		if (StringUtil.isEmpty(be.getId()) && StringUtil.isEmpty(be.getCt())) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Class getEntityClass() {
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	//public void replaceTmpId(String tmpId, String entityId, String ebcn) {
		//baseService.update4Prop("File_Yewublzlwd", true, 
			//	NameQueryUtil.setParams("key_value",entityId), 
				//NameQueryUtil.setParams("ebcn",ebcn,"key_value",tmpId));
		//baseService.update("YeWuBLZLWD", "file", "YeWuBLZLWD_replaceTmpId",
			//	NameQueryUtil.setParams("entityId", entityId, "tmpId", tmpId, "ebcn", ebcn));
	//}
}
