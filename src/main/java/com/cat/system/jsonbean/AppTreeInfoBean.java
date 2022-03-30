package com.cat.system.jsonbean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cat.boot.service.BaseService;
import com.cat.boot.util.NameQueryUtil;
import com.cat.boot.util.StringUtil;
import com.cat.system.model.AppPermission;
import com.cat.system.model.AppTree;

public class AppTreeInfoBean {

	// 构建菜单
	private String title;

	private String key;

	private String redirect;

	private String path;

	private String code;

	private String name;

	private String component;

	private String icon;

	private String fontFamily;

	private Long fontCode;

	private String colorCode;

	private boolean hide = false;

	private List<AppTreeInfoBean> children = new ArrayList<AppTreeInfoBean>();
	
	private Map<String,String> permissionMap = new HashMap<String,String>();
	
	private String appPath;
	
	private String queryPath;
	
	private String query_params;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public boolean isHide() {
		return hide;
	}

	public void setHide(boolean hide) {
		this.hide = hide;
	}

	public String getFontFamily() {
		return fontFamily;
	}

	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}

	public Long getFontCode() {
		return fontCode;
	}

	public void setFontCode(Long fontCode) {
		this.fontCode = fontCode;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public List<AppTreeInfoBean> getChildren() {
		return children;
	}

	public void setChildren(List<AppTreeInfoBean> children) {
		this.children = children;
	}

	public Map<String, String> getPermissionMap() {
		return permissionMap;
	}

	public void setPermissionMap(Map<String, String> permissionMap) {
		this.permissionMap = permissionMap;
	}

	public String getAppPath() {
		return appPath;
	}

	public void setAppPath(String appPath) {
		this.appPath = appPath;
	}

	public String getQueryPath() {
		return queryPath;
	}

	public void setQueryPath(String queryPath) {
		this.queryPath = queryPath;
	}

	public String getQuery_params() {
		return query_params;
	}

	public void setQuery_params(String query_params) {
		this.query_params = query_params;
	}

	public static List<AppTreeInfoBean> iniAppTree(List<AppTree> entitys, BaseService baseService) {
		// 构建根目录
		AppTreeInfoBean bean = new AppTreeInfoBean();
		bean.setTitle("首页");
		bean.setKey("");
		bean.setName("index");
		bean.setComponent("BasicLayout");
		bean.setRedirect("/index");

		Map<String, List<AppTree>> map = new HashMap<String, List<AppTree>>();
		Map<String, AppTree> map2 = new HashMap<String, AppTree>();

		for (AppTree entity : entitys) {
			do {
				if (!StringUtil.isMapContainsKey(map2, entity.getId())) {
					map2.put(entity.getId(), entity);
					String key = "index";
					if(!StringUtil.isEmpty(entity.getScId())) {
						key = entity.getScId();
					}
					if (StringUtil.isMapContainsKey(map, key)) {
						List<AppTree> a = map.get(key);
						a.add(entity);
						map.replace(key, a);
					} else {
						List<AppTree> a = new ArrayList<AppTree>();
						a.add(entity);
						map.put(key, a);
					}
				}
				if(!StringUtil.isEmpty(entity.getScId())) {
					entity = (AppTree)baseService.findById(AppTree.class, entity.getScId());
				} else {
					entity = null;
				}
			} while (entity != null);
		}

		for (AppTree appTree : map.get("index")) {
			AppTreeInfoBean a = iniTree(appTree.getId(), map, map2, baseService);
			bean.getChildren().add(a);
		}
		List<AppTreeInfoBean> bs = new ArrayList<AppTreeInfoBean>();
		bs.add(bean);
		return bs;
	}

	@SuppressWarnings("unchecked")
	static AppTreeInfoBean iniTree(String key, Map<String, List<AppTree>> map, Map<String, AppTree> map2,
			BaseService baseService) {
		AppTreeInfoBean tbean = new AppTreeInfoBean();
		AppTree bean = map2.get(key);
		List<AppTreeInfoBean> beans = new ArrayList<AppTreeInfoBean>();
		if (StringUtil.isMapContainsKey(map, key)) {
			List<AppTree> value = map.get(key);
			if (!StringUtil.isListEmpty(value)) {
				for (AppTree bean1 : value) {
					AppTreeInfoBean bean2 = iniTree(bean1.getId(), map, map2, baseService);
					beans.add(bean2);
				}
			}
		}
		tbean.setTitle(bean.getName());
		tbean.setComponent(bean.getComponent());
		tbean.setIcon(bean.getIcon());
		if (!StringUtil.isListEmpty(beans)) {
			if ("0001".equals(bean.getId())) {
				//dddbeans.addAll(iniTaskRouter(baseService));
			}
			tbean.setChildren(beans);
			tbean.setKey("/" + bean.getId());
		} else {
			tbean.setKey("A" + bean.getId());
			tbean.setPath(bean.getPath());
			
			// 查询对应的权限
			List<AppPermission> aps = (List<AppPermission>)baseService.getList("AppPermission", null, true
					,NameQueryUtil.setParams("app",bean.getId()));
			if(!StringUtil.isListEmpty(aps)) {
				Map<String,String> maps = new HashMap<String,String>();
				for (AppPermission appPermission : aps) {
					maps.put(appPermission.getOper(), appPermission.getRole());
				}
				tbean.setPermissionMap(maps);
			}
		}
		
		return tbean;
	}
	
	public static List<AppTreeInfoBean> iniAppTree_Apps(List<AppTree> entitys,BaseService baseService) {
		// 只渲染一级和末级菜单
		List<AppTreeInfoBean> bs = new ArrayList<AppTreeInfoBean>();
		Map<String,List<AppTreeInfoBean>> map = new HashMap<String,List<AppTreeInfoBean>>();

		for (AppTree entity : entitys) {
			if (entity.getFontCode() != null && !StringUtil.isEmpty(entity.getFontFamily())) {
				String x = entity.getId().substring(0, 4);
				if(!map.containsKey(x)) {
					List<AppTreeInfoBean> a = new ArrayList<AppTreeInfoBean>();
					a.add(iniAppTree_App(entity));
					map.put(x, a);
				} else {
					List<AppTreeInfoBean> a = map.get(x);
					a.add(iniAppTree_App(entity));
					map.replace(x, a);
				}
			}
		}
		
		if(!StringUtil.isMapEmpty(map)) {
			for (String key : map.keySet()) {
				AppTree app = (AppTree)baseService.findById(AppTree.class, key);
				// 排除掉常用应用
				if(!"常用应用".equals(app.getName())) {
					AppTreeInfoBean bean = iniAppTree_App(app);
					bean.setChildren(map.get(key));
					bs.add(bean);
				}
			}
		}
		return bs;
	}

	public static List<AppTreeInfoBean> iniAppTree_App(List<AppTree> entitys) {
		List<AppTreeInfoBean> bs = new ArrayList<AppTreeInfoBean>();
		for (AppTree entity : entitys) {
			if (entity.getFontCode() != null && !StringUtil.isEmpty(entity.getFontFamily())) {
				AppTreeInfoBean bean = new AppTreeInfoBean();
				bean.setTitle(entity.getName());
				bean.setPath(entity.getPath());
				bean.setColorCode(entity.getColorCode());
				bean.setFontCode(entity.getFontCode());
				bean.setFontFamily(entity.getFontFamily());
				bean.setCode(entity.getId());
				bs.add(bean);
			}
		}
		return bs;
	}

	public static AppTreeInfoBean iniAppTree_App(AppTree entity) {
		AppTreeInfoBean bean = new AppTreeInfoBean();
		bean.setTitle(entity.getName());
		bean.setPath(entity.getPath());
		bean.setColorCode(entity.getColorCode());
		bean.setFontCode(entity.getFontCode());
		bean.setFontFamily(entity.getFontFamily());
		bean.setCode(entity.getId());
		bean.setAppPath(entity.getAppPath());
		bean.setQueryPath(entity.getQueryPath());
		bean.setQuery_params(entity.getQuery_params());
		return bean;
	}

}
