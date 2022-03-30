package com.cat.quartz.listener;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManagerFactory;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.cat.boot.jsonbean.PropParamBean;
import com.cat.boot.model.BaseEntity;
import com.cat.boot.util.CalendarUtil;
import com.cat.boot.util.NameQueryUtil;
import com.cat.boot.util.SpringContextUtil;
import com.cat.boot.util.StringUtil;

public class BaseJobDao {

	Session session;
	
	private NameQueryUtil nameQueryUtil;

	public boolean noAnnotationSave(Object u, boolean flag) {
		Session s = session;
		Transaction tr = s.beginTransaction();
		try {
			if (flag) {
				preUpdate(u);
				s.update(u);
			} else {
				prePersist(u);
				s.save(u);
			}
			s.flush();
			tr.commit();
			return true;
		} catch (Exception e) {
			tr.rollback();
			e.printStackTrace();
			return false;
		}
	}

	public boolean noAnnotationDelete(Object u) {
		Session s = session;
		Transaction tr = s.beginTransaction();
		try {
			s.delete(u);
			s.flush();
			tr.commit();
			return true;
		} catch (Exception e) {
			tr.rollback();
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings("rawtypes")
	public void noAnnotationDelete4Prop(String name, boolean isnative, Map<Object, Object> map) {
		Session s = session;
		Transaction tr = s.beginTransaction();
		try {
			String sql = getDeleteSql(name, isnative, map);
			Query query;
			if (isnative) {
				query = s.createSQLQuery(sql);
			} else {
				query = s.createQuery(sql);
			}
			NameQueryUtil.setParameter(query, map);
			query.executeUpdate();
			tr.commit();
		} catch (Exception e) {
			tr.rollback();
			e.printStackTrace();
		}
	}

	public String getDeleteSql(String name, boolean isnative, Map<Object, Object> map) {
		String sql = "delete " + name + " o ";
		sql = NameQueryUtil.linkSql(sql, map);
		return sql;
	}

	@SuppressWarnings("rawtypes")
	public void noAnnotationDelete4Prop(String name, boolean isnative, List<PropParamBean> beans) {
		Session s = session;
		Transaction tr = s.beginTransaction();
		try {
			String sql = getDeleteSql(name, isnative, beans);
			Query query;
			if (isnative) {
				query = s.createSQLQuery(sql);
			} else {
				query = s.createQuery(sql);
			}
			NameQueryUtil.setParameter(query, beans);
			query.executeUpdate();
			tr.commit();
		} catch (Exception e) {
			tr.rollback();
			e.printStackTrace();
		}
	}

	public String getDeleteSql(String name, boolean isnative, List<PropParamBean> beans) {
		String sql = "delete " + name + " o ";
		return NameQueryUtil.linkSql(sql, beans);
	}

	@SuppressWarnings("rawtypes")
	public void noAnnotationUpdate4Prop(String name, boolean isnative, Map<Object, Object> map,
			Map<Object, Object> parmmap) {
		Session s = session;
		Transaction tr = s.beginTransaction();
		try {
			String sql = getUpdateSql(name, isnative, map, parmmap);
			Query query;
			if (isnative) {
				query = s.createSQLQuery(sql);
			} else {
				query = s.createQuery(sql);
			}
			NameQueryUtil.setParameter(query, map);
			NameQueryUtil.setParameter(query, parmmap);
			query.executeUpdate();
			tr.commit();
			// s.close();
		} catch (Exception e) {
			tr.rollback();
			// s.close();
			e.printStackTrace();
		}
	}

	public String getUpdateSql(String name, boolean isnative, Map<Object, Object> map, Map<Object, Object> parmmap) {
		String sql = "update " + name + " o set ";
		if (!StringUtil.isMapEmpty(map)) {
			for (Object key : map.keySet()) {
				sql = sql + "o." + key + " =:" + ((String) key).replaceAll("\\.", "") + ",";
			}
			sql = StringUtil.removeLast(sql);
		}
		sql = NameQueryUtil.linkSql(sql, parmmap);
		return sql;
	}

	@SuppressWarnings("rawtypes")
	public void noAnnotationUpdate4Prop(String name, boolean isnative, Map<Object, Object> map,
			List<PropParamBean> beans) {
		Session s = session;
		Transaction tr = s.beginTransaction();
		try {
			String sql = getUpdateSql(name, isnative, map, beans);
			Query query;
			if (isnative) {
				query = s.createSQLQuery(sql);
			} else {
				query = s.createQuery(sql);
			}
			NameQueryUtil.setParameter(query, map);
			NameQueryUtil.setParameter(query, beans);
			query.executeUpdate();
			tr.commit();
		} catch (Exception e) {
			tr.rollback();
			e.printStackTrace();
		}
	}

	public String getUpdateSql(String name, boolean isnative, Map<Object, Object> map, List<PropParamBean> beans) {
		String sql = "update " + name + " o set ";
		if (!StringUtil.isMapEmpty(map)) {
			for (Object key : map.keySet()) {
				sql = sql + "o." + key + " =:" + ((String) key).replaceAll("\\.", "") + ",";
			}
			sql = StringUtil.removeLast(sql);
		}
		return NameQueryUtil.linkSql(sql, beans);
	}

	@SuppressWarnings("rawtypes")
	public void noAnnotationUpdate(Query query) {
		Session s = session;
		Transaction tr = s.beginTransaction();
		try {
			query.executeUpdate();
			tr.commit();
		} catch (Exception e) {
			tr.rollback();
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object findById(Class clazz, String id) {
		Object t = (Object) session.get(clazz, id);
		return t;
	}

	@SuppressWarnings("rawtypes")
	public Object query(String namespace, String xmlpath, String methodname, Map<Object, Object> map, int i) {
		Map maps = nameQueryUtil.getQueryMap(namespace, xmlpath, methodname, i);
		return query(maps, map);
	}

	public String getCode(String clazz, String supcode, int i) {
		String sql = "select nvl2(max(substr(lower(o.clc),1,@@size@@)),"
				+ " to_char(max(substr(lower(o.clc),1,@@size@@))+1,'@@MI@@'),to_char('@@to_char@@')) from @@clazz@@ o "
				+ " where ((:supcode is null and o.superior.code is null) or (o.superior.code=:supcode))";
		String MI = "";
		for (int j = 0; j < i; j++) {
			MI = MI + "0";
		}
		sql = sql.replaceAll("@@size@@", String.valueOf(i)).replaceAll("@@MI@@", MI + "MI")
				.replaceAll("@@to_char@@", MI.substring(0, MI.length() - 1) + "1").replaceAll("@@clazz@@", clazz);

		String code = (String) session.createQuery(sql).setParameter("supcode", supcode).uniqueResult();

		return code;
	}

	@SuppressWarnings("rawtypes")
	public String getCode(int i, String clazz, String prop, List<PropParamBean> beans) {
		String sql = "select nvl2(max(substr(lower(o.@@prop@@),1,@@size@@)),"
				+ " to_char(max(substr(lower(o.@@prop@@),1,@@size@@))+1,'@@MI@@'),to_char('@@to_char@@')) from @@clazz@@ o ";
		String MI = "";
		for (int j = 0; j < i; j++) {
			MI = MI + "0";
		}
		sql = sql.replaceAll("@@prop@@", prop).replaceAll("@@size@@", String.valueOf(i)).replaceAll("@@MI@@", MI + "MI")
				.replaceAll("@@to_char@@", MI.substring(0, MI.length() - 1) + "1").replaceAll("@@clazz@@", clazz);

		sql = NameQueryUtil.linkSql(sql, beans);
		Query query = session.createQuery(sql);
		NameQueryUtil.setParameter(query, beans);
		String code = (String) query.uniqueResult();

		return code;
	}

	@SuppressWarnings("rawtypes")
	public Object query4Prop(String name, String orderby, boolean isfiter, boolean iscount, boolean isdistinct,
			Map<Object, Object> map) {
		// 拼接查询字符串
		String sql = (iscount
				? (StringUtil
						.isEmpty(orderby)
								? "select count(*) from "
								: (isdistinct ? "select count(distinct o." + orderby + ") from "
										: "select count(o." + orderby + ") from "))
				: "select o from ") + name + " o ";
		sql = NameQueryUtil.linkSql(sql, map);
		if (isfiter) {
			sql = sql + " and o.dictionaryData in ('1','2')";
		}
		if (!StringUtil.isEmpty(orderby) && !iscount) {
			sql = sql + " order by " + orderby;
		}
		Query query = session.createQuery(sql);
		NameQueryUtil.setParameter(query, map);
		return iscount ? query.uniqueResult() : query.list();
	}
	
	@SuppressWarnings("rawtypes")
	public Object query4Prop(String table_name, String orderby, boolean isfiter, String prop, Map<Object, Object> map) {
		// 拼接查询字符串
		String sql = "select " + prop + " from " + table_name + " o ";
		sql = NameQueryUtil.linkSql(sql, map);
		if (isfiter) {
			sql = sql + " and o.dictionary_Data in ('1','2')";
		}
		if (!StringUtil.isEmpty(orderby)) {
			sql = sql + " order by " + orderby;
		}
		Query query = session.createSQLQuery(sql);
		NameQueryUtil.setParameter(query, map);
		return query.list();
	}

	public Object query4Prop(String table_name, String orderby, boolean isfiter, String prop,
							 List<PropParamBean> beans) {
		// 拼接查询字符串
		String sql = "select " + prop + " from " + table_name + " o ";
		sql = NameQueryUtil.linkSql(sql, beans);
		if (isfiter) {
			sql = sql + " and o.dictionary_Data in ('1','2')";
		}
		if (!StringUtil.isEmpty(orderby)) {
			sql = sql + " order by " + orderby;
		}
		Query query = session.createSQLQuery(sql);
		NameQueryUtil.setParameter(query, beans);
		return query.list();
	}

	@SuppressWarnings("rawtypes")
	public Object query4Prop(String name, String orderby, boolean isfiter, boolean iscount, boolean isdistinct,
			List<PropParamBean> beans) {
		// 拼接查询字符串
		String sql = (iscount
				? (StringUtil
						.isEmpty(orderby)
								? "select count(*) from "
								: (isdistinct ? "select count(distinct o." + orderby + ") from "
										: "select count(o." + orderby + ") from "))
				: "select o from ") + name + " o ";
		sql = NameQueryUtil.linkSql(sql, beans);
		if (isfiter) {
			sql = sql + " and o.dictionaryData in ('1','2')";
		}
		if (!StringUtil.isEmpty(orderby) && !iscount) {
			sql = sql + " order by " + orderby;
		}
		Query query = session.createQuery(sql);
		NameQueryUtil.setParameter(query, beans);
		return iscount ? query.uniqueResult() : query.list();
	}

	@SuppressWarnings("rawtypes")
	public Object query(Map maps, Map<Object, Object> map) {
		Object object = null;
		String sql = (String) maps.get("sql");
		if (maps.containsKey("orderby")) {
			sql = sql + " order by " + maps.get("orderby");
		}
		// sql = NameQueryUtil.replaceUserId(sql);
		Query query;
		// 是否native查询
		if (NameQueryUtil.isnative(maps)) {
			query = session.createSQLQuery(sql);
		} else {
			query = session.createQuery(sql);
		}
		if (!StringUtil.isMapEmpty(map)) {
			for (Object obj : map.keySet()) {
				Map map2s = (Map) maps.get("parm");
				if (StringUtil.isMapContainsKey(map2s, "name" + obj)) {
					if (!NameQueryUtil.isList(map2s, "name" + obj)) {
						query.setParameter((String) obj, map.get(obj));
					} else {
						query.setParameterList((String) obj, (List) map.get(obj));
					}
				}
			}
		}
		if (NameQueryUtil.iscount(maps)) {
			object = query.uniqueResult();
		} else {
			object = query.list();
		}
		return object;
	}

	@SuppressWarnings("rawtypes")
	public Query query(String ql, boolean isnative) {
		if (isnative) {
			return session.createSQLQuery(ql);
		} else {
			return session.createQuery(ql);
		}
	}

	public void prePersist(Object entity) {
		if (entity instanceof BaseEntity) {
			BaseEntity bo = (BaseEntity) entity;
			bo.setCt(CalendarUtil.getYyyyMmDdHHmmss(CalendarUtil.now()));
			bo.setToken(UUID.randomUUID().toString());
			if (bo.getDictionaryData() == null) {
				bo.setDictionaryData("1");
			}
		}
	}

	public void preUpdate(Object entity) {
		if (entity instanceof BaseEntity) {
			BaseEntity bo = (BaseEntity) entity;
			bo.setLmt(CalendarUtil.getYyyyMmDdHHmmss(CalendarUtil.now()));
			bo.setToken(UUID.randomUUID().toString());
		}
	}

	public BaseJobDao() {
		this.nameQueryUtil = (NameQueryUtil) SpringContextUtil.getBean("nameQueryUtil");
		EntityManagerFactory manager = (EntityManagerFactory) SpringContextUtil.getBean("entityManagerFactory");
		this.session = manager.createEntityManager().unwrap(Session.class);
	}
	
	public void close() {
		if(this.session!=null) {
			this.session.close();
		}
	}
}
