package com.cat.quartz.listener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.query.Query;

import com.cat.boot.jsonbean.PropParamBean;
import com.cat.boot.util.StringUtil;

public class BaseJobService {

	protected BaseJobDao baseJobDao = new BaseJobDao();

	public boolean noAnnotationSave(Object u, boolean flag) {
		return baseJobDao.noAnnotationSave(u, flag);
	}

	public boolean noAnnotationDelete(Object u) {
		return baseJobDao.noAnnotationDelete(u);
	}

	public void noAnnotationDelete4Prop(String name, boolean isnative, Map<Object, Object> map) {
		baseJobDao.noAnnotationDelete4Prop(name, isnative, map);
	}

	public void noAnnotationDelete4Prop(String name, boolean isnative, List<PropParamBean> beans) {
		baseJobDao.noAnnotationDelete4Prop(name, isnative, beans);
	}

	public void noAnnotationUpdate4Prop(String name, boolean isnative, Map<Object, Object> map,
			Map<Object, Object> parmmap) {
		baseJobDao.noAnnotationUpdate4Prop(name, isnative, map, parmmap);
	}

	public void noAnnotationUpdate4Prop(String name, boolean isnative, Map<Object, Object> map,
			List<PropParamBean> beans) {
		baseJobDao.noAnnotationUpdate4Prop(name, isnative, map, beans);
	}

	@SuppressWarnings("rawtypes")
	public void noAnnotationUpdate(Query query) {
		baseJobDao.noAnnotationUpdate(query);
	}

	@SuppressWarnings("rawtypes")
	public Object findById(Class clazz, String id) {
		Object t = baseJobDao.findById(clazz, id);
		return t;
	}

	public String getCode(String clazz, String supcode, int i) {
		return baseJobDao.getCode(clazz, supcode, i);
	}

	public String getCode(int i, String clazz, String prop) {
		return baseJobDao.getCode(i, clazz, prop, new ArrayList<PropParamBean>());
	}

	public String getCode(int i, String clazz, String prop, List<PropParamBean> beans) {
		return baseJobDao.getCode(i, clazz, prop, beans);
	}

	public Long longResult(String name, String countby, boolean isfiter, boolean isdistinct) {
		Object tmp = baseJobDao.query4Prop(name, countby, isfiter, true, isdistinct, new HashMap<>());
		if (tmp == null) {
			return null;
		}
		if (tmp instanceof java.math.BigDecimal) {
			return ((BigDecimal) tmp).longValue();
		} else {
			return ((Long) tmp).longValue();
		}
	}

	public Long longResult(String name, String countby, boolean isfiter, boolean isdistinct, Map<Object, Object> map) {
		Object tmp = baseJobDao.query4Prop(name, countby, isfiter, true, isdistinct, map);
		if (tmp == null) {
			return null;
		}
		if (tmp instanceof java.math.BigDecimal) {
			return ((BigDecimal) tmp).longValue();
		} else {
			return ((Long) tmp).longValue();
		}
	}

	public Long longResult(String name, String countby, boolean isfiter, boolean isdistinct,
			List<PropParamBean> beans) {
		Object tmp = baseJobDao.query4Prop(name, countby, isfiter, true, isdistinct, beans);
		if (tmp == null) {
			return null;
		}
		if (tmp instanceof java.math.BigDecimal) {
			return ((BigDecimal) tmp).longValue();
		} else {
			return ((Long) tmp).longValue();
		}
	}

	public Long longResult(String namespace, String xmlpath, String methodname, Map<Object, Object> map) {
		Object tmp = baseJobDao.query(namespace, xmlpath, methodname, map, 0);
		if (tmp == null) {
			return null;
		}
		if (tmp instanceof java.math.BigDecimal) {
			return ((BigDecimal) tmp).longValue();
		} else {
			return ((Long) tmp).longValue();
		}
	}

	public Object getList(String namespace, String xmlpath, String methodname, Map<Object, Object> map) {
		return baseJobDao.query(namespace, xmlpath, methodname, map, 0);
	}

	public Object getList(String name, String orderby, boolean isfiter) {
		return baseJobDao.query4Prop(name, orderby, isfiter, false, false, new HashMap<>());
	}

	public Object getList(String name, String orderby, boolean isfiter, Map<Object, Object> map) {
		return baseJobDao.query4Prop(name, orderby, isfiter, false, false, map);
	}

	public Object getList(String name, String orderby, boolean isfiter, List<PropParamBean> beans) {
		return baseJobDao.query4Prop(name, orderby, isfiter, false, false, beans);
	}
	
	public Object getList(String table_name, String orderby, boolean isfiter, String prop, Map<Object, Object> map) {
		return baseJobDao.query4Prop(table_name, orderby, isfiter, prop, map);
	}

	public Object getList(String table_name, String orderby, boolean isfiter, String prop, List<PropParamBean> beans) {
		return baseJobDao.query4Prop(table_name, orderby, isfiter, prop, beans);
	}

	@SuppressWarnings("unchecked")
	public Object getSimple(String namespace, String xmlpath, String methodname, Map<Object, Object> map) {
		List<Object> objs = (List<Object>) getList(namespace, xmlpath, methodname, map);
		if (StringUtil.isListEmpty(objs)) {
			return null;
		} else {
			return objs.get(0);
		}
	}

	@SuppressWarnings("unchecked")
	public Object getSimple(String name, String orderby, boolean isfiter) {
		List<Object> objs = (List<Object>) getList(name, orderby, isfiter, new HashMap<>());
		if (StringUtil.isListEmpty(objs)) {
			return null;
		} else {
			return objs.get(0);
		}
	}

	@SuppressWarnings("unchecked")
	public Object getSimple(String name, String orderby, boolean isfiter, Map<Object, Object> map) {
		List<Object> objs = (List<Object>) getList(name, orderby, isfiter, map);
		if (StringUtil.isListEmpty(objs)) {
			return null;
		} else {
			return objs.get(0);
		}
	}

	@SuppressWarnings("unchecked")
	public Object getSimple(String name, String orderby, boolean isfiter, List<PropParamBean> beans) {
		List<Object> objs = (List<Object>) getList(name, orderby, isfiter, beans);
		if (StringUtil.isListEmpty(objs)) {
			return null;
		} else {
			return objs.get(0);
		}
	}

	@SuppressWarnings("unchecked")
	public boolean isUnion(String namespace, String xmlpath, String methodname, Map<Object, Object> map) {
		List<Object> objs = (List<Object>) getList(namespace, xmlpath, methodname, map);
		if (StringUtil.isListEmpty(objs)) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public boolean isUnion(String name, String orderby, boolean isfiter) {
		List<Object> objs = (List<Object>) getList(name, orderby, isfiter, new HashMap<>());
		if (StringUtil.isListEmpty(objs)) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public boolean isUnion(String name, String orderby, boolean isfiter, Map<Object, Object> map) {
		List<Object> objs = (List<Object>) getList(name, orderby, isfiter, map);
		if (StringUtil.isListEmpty(objs)) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public boolean isUnion(String name, String orderby, boolean isfiter, List<PropParamBean> beans) {
		List<Object> objs = (List<Object>) getList(name, orderby, isfiter, beans);
		if (StringUtil.isListEmpty(objs)) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("rawtypes")
	public Object query(Map maps, Map<Object, Object> map) {
		return baseJobDao.query(maps, map);
	}

	@SuppressWarnings("rawtypes")
	public Query query(String ql, boolean isnative) {
		return baseJobDao.query(ql, isnative);
	}
	
	public void close() {
		baseJobDao.close();
	}
}
