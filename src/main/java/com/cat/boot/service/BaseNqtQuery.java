package com.cat.boot.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.cat.boot.jsonbean.BaseQueryHelp;
import com.cat.boot.jsonbean.ResultBean;
import com.cat.boot.jsonbean.UserBean;
import com.cat.boot.util.NameQueryUtil;
import com.cat.boot.util.StringUtil;
import com.cat.system.model.AppTree;

public abstract class BaseNqtQuery<T> extends BaseQuery<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 736515704643423088L;

	public abstract String query(BaseQueryHelp parms) throws Exception;

	public void excuteQuery(BaseQueryHelp parms) throws Exception {
		iniQhb(parms);
		executeQuery(2);
		executeCountQuery(2);
		parms.setTotalRecordCount(getQhb().getTotalRecordCount());
	};

	private void iniQhb(BaseQueryHelp parms) {
		getQhb().setParams(parms.getParams());
		getQhb().setPageSize(parms.getPageSize() == 0 ? 20 : parms.getPageSize());
		getQhb().setPageNo(parms.getPageNo()== 0 ? 1 : parms.getPageNo());
		getQhb().setSortOrder(parms.getSortOrder());
		getQhb().setSortField(parms.getSortField());
		getQhb().setUserId(parms.getUserId());
	}
	
	@SuppressWarnings("unchecked")
	protected String delete(String id,String app_code) {
		AppTree app = (AppTree)baseService.findById(AppTree.class, app_code.substring(1));
		if(StringUtil.isEmpty(app.getRelational_table())) {
			return delete(id);
		} else {
			String[] xxs = app.getRelational_table().split("\\|");
			for (String xx : xxs) {
				String[] yys = xx.split("\\.");
				// 查询关联表是否有信息
				List<Object[]> os = (List<Object[]>)baseService.getList(yys[0], null, true, yys[1],
						NameQueryUtil.setParams(yys[1],id));
				if(!StringUtil.isListEmpty(os)) {
					return ResultBean.getResultBean("400", "", "删除失败");
				}
			}
			return delete(id);
		}
	}

	@SuppressWarnings("unchecked")
	protected String delete(String id) {
		T entity = (T) baseService.findById(getEntityClass(), id);
		boolean flag = baseService.noAnnotationDelete(entity);
		if (flag) {
			return ResultBean.getSucess("删除成功");
		} else {
			return ResultBean.getResultBean("400", "", "删除失败");
		}
	}
	
	@SuppressWarnings("unchecked")
	protected String deleteTr(String id) {
		T entity = (T) baseService.findById(getEntityClass(), id);
		boolean flag = baseService.delete(entity);
		if (flag) {
			return ResultBean.getSucess("删除成功");
		} else {
			return ResultBean.getResultBean("400", "", "删除失败");
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected String delete(Class clazz,String id) {
		T entity = (T) baseService.findById(clazz, id);
		boolean flag = baseService.noAnnotationDelete(entity);
		if (flag) {
			return ResultBean.getSucess("删除成功");
		} else {
			return ResultBean.getResultBean("400", "删除失败", "删除失败");
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected String deleteTr(Class clazz,String id) {
		T entity = (T) baseService.findById(clazz, id);
		boolean flag = baseService.delete(entity);
		if (flag) {
			return ResultBean.getSucess("删除成功");
		} else {
			return ResultBean.getResultBean("400", "删除失败", "删除失败");
		}
	}
	
	protected String deleteFile(String id,String ebcn) {
		return minioFileController.deleteFile_Entity(id, ebcn);
	}
	
	protected boolean hasRole(HttpServletRequest request,String role) {
		String token = request.getHeader("Access-Token");
		UserBean bean = JSON.parseObject(jedisUtil.get(token,0), UserBean.class);
		if(StringUtil.isListEmpty(bean.getUserRoles())) {
			return false;
		}else {
			if(bean.getUserRoles().contains(role)) {
				return true;
			}else {
				return false;
			}
		}
	}
	
	protected boolean hasRoles(HttpServletRequest request,List<String> roles) {
		String token = request.getHeader("Access-Token");
		UserBean bean = JSON.parseObject(jedisUtil.get(token,0), UserBean.class);
		if(StringUtil.isListEmpty(bean.getUserRoles())) {
			return false;
		}else {
			for (String role : roles) {
				if (bean.getUserRoles().contains(role)) {
					return true;
				}
			}
			return false;
		}
	}
}
